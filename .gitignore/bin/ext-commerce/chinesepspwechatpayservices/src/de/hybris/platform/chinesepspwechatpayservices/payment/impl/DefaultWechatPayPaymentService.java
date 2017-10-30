/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.chinesepspwechatpayservices.payment.impl;

import de.hybris.platform.chinesepaymentservices.model.ChinesePaymentInfoModel;
import de.hybris.platform.chinesepaymentservices.order.service.ChineseOrderService;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayOrderDao;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatPayQueryResult;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.OrderQueryRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.strategies.WeChatPayPaymentInfoStrategy;
import de.hybris.platform.chinesepspwechatpayservices.strategies.WechatPayPaymentTransactionStrategy;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.impl.DefaultPaymentServiceImpl;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;


/**
 * A default implements of chinese payment service
 */
public class DefaultWechatPayPaymentService extends DefaultPaymentServiceImpl implements ChinesePaymentService
{
	private static final String WECHAT_LOGO = "/images/theme/wechatpay.png";

	private static final String UNSUPPORTED_MESSAGE = "WeChat Pay does NOT support this operation.";

	private MediaService mediaService;

	private WeChatPayPaymentInfoStrategy weChatPayPaymentInfoStrategy;

	private CommerceCheckoutService commerceCheckoutService;

	private ConfigurationService configurationService;

	private WechatPayOrderDao orderDao;

	private WechatPayPaymentTransactionStrategy wechatPayPaymentTransactionStrategy;

	private WechatPayConfiguration wechatPayConfiguration;

	private WechatPayHttpClient wechatPayHttpClient;

	private ModelService modelService;

	private ChineseOrderService chineseOrderService;

	@Override
	public void handleAsyncResponse(final HttpServletRequest request, final HttpServletResponse response)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}

	@Override
	public String handleSyncResponse(final HttpServletRequest request, final HttpServletResponse response)
	{
		throw new UnsupportedOperationException(UNSUPPORTED_MESSAGE);
	}

	@Override
	public boolean cancelPayment(final String orderCode)
	{
		// TODO implement this method in further story/sprint
		return false;
	}

	@Override
	public String getPaymentRequestUrl(final String orderCode)
	{
		return "/checkout/multi/wechat/pay/" + orderCode + "?showwxpaytitle=1";
	}

	@Override
	public void syncPaymentStatus(final String orderCode)
	{
		final OrderQueryRequestProcessor orderQueryRequestProcessor = new OrderQueryRequestProcessor(getWechatPayHttpClient(),
				orderCode, getWechatPayConfiguration());
		final Optional<WechatPayQueryResult> processResult = orderQueryRequestProcessor.process();
		processResult
				.ifPresent(wechatPayQueryResult -> {
					final Optional<OrderModel> orderModel = getOrderDao().findOrderByCode(orderCode);
					orderModel
							.ifPresent(order -> {
								final Optional<WechatpayPaymentTransactionEntryModel> wechatpayPaymentTransactionEntry = getWechatPayPaymentTransactionStrategy()
										.saveForStatusCheck(order, wechatPayQueryResult);
								wechatpayPaymentTransactionEntry.ifPresent(entry -> {
									if (TransactionStatus.ACCEPTED.name().equals(entry.getTransactionStatus()))
									{
										order.setPaymentStatus(PaymentStatus.PAID);
										getModelService().save(order);
										getChineseOrderService().markOrderAsPaid(orderCode);
									}
								});
							});
				});
	}

	@Override
	public boolean setPaymentInfo(final CartModel cartModel, final ChinesePaymentInfoModel chinesePaymentInfoModel)
	{
		cartModel.setChinesePaymentInfo(chinesePaymentInfoModel);
		getWeChatPayPaymentInfoStrategy().updatePaymentInfoForPayemntMethod(chinesePaymentInfoModel);
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setEnableHooks(true);
		parameter.setCart(cartModel);
		parameter.setPaymentInfo(chinesePaymentInfoModel);
		return getCommerceCheckoutService().setPaymentInfo(parameter);
	}

	@Override
	public String getPspLogoUrl()
	{
		final MediaModel weChatLogo = getMediaService().getMedia(WECHAT_LOGO);
		return weChatLogo.getURL();
	}

	@Override
	public void updatePaymentInfoForPlaceOrder(final String orderCode)
	{
		final Optional<OrderModel> optional = getOrderDao().findOrderByCode(orderCode);
		if (optional.isPresent())
		{
			final OrderModel order = optional.get();
			getWeChatPayPaymentInfoStrategy().updatePaymentInfoForPlaceOrder(order);
		}
	}


	@Override
	public Optional<String> getRefundRequestUrl(final String orderCode)
	{
		// TODO implement this method in further story/sprint
		return null;
	}

	public void createTransactionForNewRequest(final String orderCode)
	{
		final Optional<OrderModel> optional = getOrderDao().findOrderByCode(orderCode);
		if (optional.isPresent())
		{
			final OrderModel order = optional.get();
			wechatPayPaymentTransactionStrategy.createForNewRequest(order);
		}
	}

	public MediaService getMediaService()
	{
		return mediaService;
	}

	@Required
	public void setMediaService(final MediaService mediaService)
	{
		this.mediaService = mediaService;
	}

	public WeChatPayPaymentInfoStrategy getWeChatPayPaymentInfoStrategy()
	{
		return weChatPayPaymentInfoStrategy;
	}

	@Required
	public void setWeChatPayPaymentInfoStrategy(final WeChatPayPaymentInfoStrategy weChatPayPaymentInfoStrategy)
	{
		this.weChatPayPaymentInfoStrategy = weChatPayPaymentInfoStrategy;
	}

	public CommerceCheckoutService getCommerceCheckoutService()
	{
		return commerceCheckoutService;
	}

	@Required
	public void setCommerceCheckoutService(final CommerceCheckoutService commerceCheckoutService)
	{
		this.commerceCheckoutService = commerceCheckoutService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public WechatPayOrderDao getOrderDao()
	{
		return orderDao;
	}

	@Required
	public void setOrderDao(final WechatPayOrderDao orderDao)
	{
		this.orderDao = orderDao;
	}

	/**
	 * @return the wechatPayPaymentTransactionStrategy
	 */
	public WechatPayPaymentTransactionStrategy getWechatPayPaymentTransactionStrategy()
	{
		return wechatPayPaymentTransactionStrategy;
	}

	/**
	 * @param wechatPayPaymentTransactionStrategy
	 *           the wechatPayPaymentTransactionStrategy to set
	 */
	public void setWechatPayPaymentTransactionStrategy(
			final WechatPayPaymentTransactionStrategy wechatPayPaymentTransactionStrategy)
	{
		this.wechatPayPaymentTransactionStrategy = wechatPayPaymentTransactionStrategy;
	}

	protected WechatPayConfiguration getWechatPayConfiguration()
	{
		return wechatPayConfiguration;
	}

	@Required
	public void setWechatPayConfiguration(final WechatPayConfiguration wechatPayConfiguration)
	{
		this.wechatPayConfiguration = wechatPayConfiguration;
	}

	protected WechatPayHttpClient getWechatPayHttpClient()
	{
		return wechatPayHttpClient;
	}

	@Required
	public void setWechatPayHttpClient(final WechatPayHttpClient wechatPayHttpClient)
	{
		this.wechatPayHttpClient = wechatPayHttpClient;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Override
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ChineseOrderService getChineseOrderService()
	{
		return chineseOrderService;
	}

	@Required
	public void setChineseOrderService(final ChineseOrderService chineseOrderService)
	{
		this.chineseOrderService = chineseOrderService;
	}



}
