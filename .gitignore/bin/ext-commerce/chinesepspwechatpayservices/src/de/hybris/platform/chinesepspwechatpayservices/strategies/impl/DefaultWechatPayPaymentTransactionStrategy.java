/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspwechatpayservices.strategies.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chinesepspwechatpayservices.constants.WechatPaymentConstants;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayPaymentTransactionDao;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayPaymentTransactionEntryDao;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatPayQueryResult;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;
import de.hybris.platform.chinesepspwechatpayservices.strategies.WechatPayPaymentTransactionStrategy;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.dto.TransactionStatusDetails;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionModel;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Supplier;


public class DefaultWechatPayPaymentTransactionStrategy implements WechatPayPaymentTransactionStrategy
{
	private final Logger LOG = Logger.getLogger(DefaultWechatPayPaymentTransactionStrategy.class.getName());

	private ModelService modelService;
	private KeyGenerator paymentTransactionKeyGenerator;
	private WechatPayPaymentTransactionDao wechatPayPaymentTransactionDao;
	private WechatPayPaymentTransactionEntryDao wechatPayPaymentTransactionEntryDao;


	@Override
	public void createForNewRequest(final OrderModel orderModel)
	{
		final WechatpayPaymentTransactionModel transaction = createTransacionForNewRequest(orderModel);
		createTransactionEntryForNewRequest(orderModel, transaction);
	}

	protected final WechatpayPaymentTransactionModel createTransacionForNewRequest(final OrderModel orderModel)
	{
		final WechatpayPaymentTransactionModel transaction = modelService.create(WechatpayPaymentTransactionModel.class);
		transaction.setOrder(orderModel);
		transaction.setCode(orderModel.getCode());
		transaction.setRequestId(orderModel.getCode());
		transaction.setPaymentProvider(WechatPaymentConstants.Basic.PAYMENT_PROVIDER);
		transaction.setCreationtime(new Date());
		modelService.save(transaction);
		return transaction;
	}

	protected void createTransactionEntryForNewRequest(final OrderModel orderModel,
			final WechatpayPaymentTransactionModel transaction)
	{
		final WechatpayPaymentTransactionEntryModel entry = modelService.create(WechatpayPaymentTransactionEntryModel.class);
		entry.setAmount(BigDecimal.valueOf(orderModel.getTotalPrice().doubleValue()));
		if (orderModel.getCurrency() != null)
		{
			entry.setCurrency(orderModel.getCurrency());
		}
		entry.setType(PaymentTransactionType.WECHAT_REQUEST);
		entry.setTime(new Date());
		entry.setPaymentTransaction(transaction);
		entry.setRequestId(transaction.getRequestId());
		entry.setTransactionStatus(TransactionStatus.ACCEPTED.name());
		entry.setTransactionStatusDetails(TransactionStatusDetails.SUCCESFULL.name());
		entry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
		modelService.save(entry);
	}

	@Override
	public void updateForNotification(final OrderModel orderModel,
			final WechatRawDirectPayNotification wechatPayNotifyResponseData)
	{
		validateParameterNotNull(orderModel, "The given order is null!");
		validateParameterNotNull(wechatPayNotifyResponseData, "The given notifyData is null!");

		if (wechatPayNotifyResponseData.getOutTradeNo() != null)
		{
			final TransactionStatus status;
			if (WechatPaymentConstants.Notification.RESULT_SUCCESS.equals(wechatPayNotifyResponseData.getResultCode()))
			{
				status = TransactionStatus.ACCEPTED;
			}
			else
			{
				status = TransactionStatus.REJECTED;
			}
			final Optional<WechatpayPaymentTransactionModel> wechatpayPaymentTransactionModel = getPaymentTransactionToUpdate(
					orderModel, status, wechatPayNotifyResponseData.getTransactionId());

			wechatpayPaymentTransactionModel.ifPresent(wechatpayTransaction -> {
				wechatpayTransaction.setWechatpayCode(wechatPayNotifyResponseData.getTransactionId());
				getModelService().save(wechatpayTransaction);
				final WechatpayPaymentTransactionEntryModel wechatpayPaymentTransactionEntry = (WechatpayPaymentTransactionEntryModel) getModelService()
						.create(WechatpayPaymentTransactionEntryModel.class);
				setEntryByTransaction(wechatpayTransaction, wechatpayPaymentTransactionEntry);
				setEntryByNotification(wechatPayNotifyResponseData, wechatpayPaymentTransactionEntry, status);
				wechatpayPaymentTransactionEntry.setAmount(BigDecimal.valueOf(orderModel.getTotalPrice().doubleValue()));
				getModelService().save(wechatpayPaymentTransactionEntry);
				LOG.info("Update transaction for Wechat's notification suceessfully");
			});
		}
	}

	@Override
	public Optional<WechatpayPaymentTransactionEntryModel> saveForStatusCheck(final OrderModel orderModel,
			final WechatPayQueryResult wechatPayQueryResult)
	{
		validateParameterNotNull(orderModel, "The given order is null!");
		validateParameterNotNull(wechatPayQueryResult, "The given status data is null!");

		final String error = wechatPayQueryResult.getErrCode();
		if (error == null && wechatPayQueryResult.getOutTradeNo() != null)
		{
			final TransactionStatus status = WechatPaymentConstants.TransactionStatusMap.WechatPayToHybris
					.get(wechatPayQueryResult.getTradeState());
			if (status == null)
			{
				return Optional.empty();
			}

			final Optional<WechatpayPaymentTransactionModel> wechatpayPaymentTransactionModel = getPaymentTransactionToUpdate(
					orderModel, status, wechatPayQueryResult.getOutTradeNo());
			return savePaymentTransactionEntryForQueryOrder(orderModel, wechatPayQueryResult, status,
					wechatpayPaymentTransactionModel);

		}
		return Optional.empty();
	}

	private Optional<WechatpayPaymentTransactionEntryModel> savePaymentTransactionEntryForQueryOrder(final OrderModel orderModel,
			final WechatPayQueryResult wechatPayQueryResult, final TransactionStatus status,
			final Optional<WechatpayPaymentTransactionModel> wechatpayPaymentTransactionModel)
	{
		final WechatpayPaymentTransactionEntryModel wechatpayPaymentTransactionEntry = (WechatpayPaymentTransactionEntryModel) modelService
				.create(WechatpayPaymentTransactionEntryModel.class);
		wechatpayPaymentTransactionModel.ifPresent(wechatpayPaymentTransaction -> {
			wechatpayPaymentTransaction.setWechatpayCode(wechatPayQueryResult.getTransactionId());
			modelService.save(wechatpayPaymentTransaction);

			setEntryByTransaction(wechatpayPaymentTransaction, wechatpayPaymentTransactionEntry);
			setEntryByQueryResult(wechatPayQueryResult, wechatpayPaymentTransactionEntry);

			wechatpayPaymentTransactionEntry.setType(PaymentTransactionType.CAPTURE);
			wechatpayPaymentTransactionEntry.setTransactionStatus(status.name());
			wechatpayPaymentTransactionEntry.setTransactionStatusDetails("Trade Status:" + wechatPayQueryResult.getTradeState());
			wechatpayPaymentTransactionEntry.setAmount(BigDecimal.valueOf(orderModel.getTotalPrice().doubleValue()));
			modelService.save(wechatpayPaymentTransactionEntry);
			modelService.refresh(wechatpayPaymentTransaction);

		});

		if (wechatpayPaymentTransactionEntry.getCode() == null)
		{
			return Optional.empty();
		}
		return Optional.of(wechatpayPaymentTransactionEntry);
	}

	protected Optional<WechatpayPaymentTransactionModel> getPaymentTransactionToUpdate(final OrderModel orderModel,
			final TransactionStatus status, final String wechatpayCode)
	{
		final Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = getPaymentTransactionWithCaptureEntry(orderModel,
				status);
		if (wechatpayTransaction.isPresent())
		{
			//If the transaction has a captured entry, this transaction has been updated already. Don't need to be updated again.
			return Optional.empty();
		}

		final Supplier<Optional<WechatpayPaymentTransactionModel>> Supplier1 = () -> getWechatPayPaymentTransactionDao()
				.findTransactionByWechatPayCode(wechatpayCode);
		final Supplier<Optional<WechatpayPaymentTransactionModel>> Supplier2 = () -> getWechatPayPaymentTransactionDao()
				.findTransactionByLatestRequestEntry(orderModel, true);
		final Supplier<Optional<WechatpayPaymentTransactionModel>> Supplier3 = () -> getWechatPayPaymentTransactionDao()
				.findTransactionByLatestRequestEntry(orderModel, false);
		return Stream.of(Supplier1, Supplier2, Supplier3).map(Supplier::get).filter(Optional::isPresent).findFirst()
				.orElse(Optional.empty());
	}

	protected Optional<WechatpayPaymentTransactionModel> getPaymentTransactionWithCaptureEntry(final OrderModel orderModel,
			final TransactionStatus status)
	{
		final List<WechatpayPaymentTransactionEntryModel> entryList = getPaymentTransactionEntryByType(orderModel, status,
				PaymentTransactionType.CAPTURE);

		if (entryList.size() == 1)
		{
			final WechatpayPaymentTransactionModel wechatpayTransaction = (WechatpayPaymentTransactionModel) entryList.get(0)
					.getPaymentTransaction();
			return Optional.of(wechatpayTransaction);
		}
		return Optional.empty();
	}

	protected List<WechatpayPaymentTransactionEntryModel> getPaymentTransactionEntryByType(final OrderModel orderModel,
			final TransactionStatus status, final PaymentTransactionType paymentTransactionType)
	{
		for (final PaymentTransactionModel transaction : orderModel.getPaymentTransactions())
		{
			if (transaction instanceof WechatpayPaymentTransactionModel)
			{
				final WechatpayPaymentTransactionModel wechatpayTransaction = ((WechatpayPaymentTransactionModel) transaction);
				if (wechatpayTransaction.getWechatpayCode() != null)
				{
					final List<WechatpayPaymentTransactionEntryModel> entryList = getWechatPayPaymentTransactionEntryDao()
							.findPaymentTransactionEntryByTypeAndStatus(paymentTransactionType, status, wechatpayTransaction);
					if (entryList.size() > 0)
					{
						return entryList;
					}
				}
			}
		}
		return Collections.emptyList();
	}

	protected void setEntryByTransaction(final WechatpayPaymentTransactionModel wechatpayPaymentTransaction,
			final WechatpayPaymentTransactionEntryModel wechatpayPaymentTransactionEntry)
	{
		wechatpayPaymentTransactionEntry.setRequestId(wechatpayPaymentTransaction.getRequestId());
		wechatpayPaymentTransactionEntry.setPaymentTransaction(wechatpayPaymentTransaction);
	}

	protected void setEntryByNotification(final WechatRawDirectPayNotification wechatPayNotifyResponseData,
			final WechatpayPaymentTransactionEntryModel wechatpayPaymentTransactionEntry, final TransactionStatus status)
	{
		wechatpayPaymentTransactionEntry.setTime(new Date());
		wechatpayPaymentTransactionEntry.setOpenId(wechatPayNotifyResponseData.getOpenid());
		wechatpayPaymentTransactionEntry.setCouponFee(Double.valueOf(wechatPayNotifyResponseData.getCouponFee() / 100));
		wechatpayPaymentTransactionEntry.setSettlementTotalFee(Double.valueOf(wechatPayNotifyResponseData.getTotalFee() / 100));
		wechatpayPaymentTransactionEntry.setType(PaymentTransactionType.CAPTURE);
		wechatpayPaymentTransactionEntry.setTransactionStatus(status.name());
		wechatpayPaymentTransactionEntry.setTransactionStatusDetails("Trade Status:" + status);
		wechatpayPaymentTransactionEntry.setCode(String.valueOf(getPaymentTransactionKeyGenerator().generate()));
	}

	protected void setEntryByQueryResult(final WechatPayQueryResult wechatPayQueryResult,
			final WechatpayPaymentTransactionEntryModel wechatpayPaymentTransactionEntry)
	{
		wechatpayPaymentTransactionEntry.setTime(new Date());
		wechatpayPaymentTransactionEntry.setOpenId(wechatPayQueryResult.getOpenid());
		wechatpayPaymentTransactionEntry.setCouponFee(Double.valueOf(wechatPayQueryResult.getCouponFee() / 100));
		wechatpayPaymentTransactionEntry.setSettlementTotalFee(Double.valueOf(wechatPayQueryResult.getTotalFee()));
		wechatpayPaymentTransactionEntry.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()));
	}

	protected WechatPayPaymentTransactionDao getWechatPayPaymentTransactionDao()
	{
		return wechatPayPaymentTransactionDao;
	}

	@Required
	public void setWechatPayPaymentTransactionDao(final WechatPayPaymentTransactionDao wechatPayPaymentTransactionDao)
	{
		this.wechatPayPaymentTransactionDao = wechatPayPaymentTransactionDao;
	}

	protected WechatPayPaymentTransactionEntryDao getWechatPayPaymentTransactionEntryDao()
	{
		return wechatPayPaymentTransactionEntryDao;
	}

	@Required
	public void setWechatPayPaymentTransactionEntryDao(
			final WechatPayPaymentTransactionEntryDao wechatPayPaymentTransactionEntryDao)
	{
		this.wechatPayPaymentTransactionEntryDao = wechatPayPaymentTransactionEntryDao;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected KeyGenerator getPaymentTransactionKeyGenerator()
	{
		return paymentTransactionKeyGenerator;
	}

	@Required
	public void setPaymentTransactionKeyGenerator(final KeyGenerator paymentTransactionKeyGenerator)
	{
		this.paymentTransactionKeyGenerator = paymentTransactionKeyGenerator;
	}


}
