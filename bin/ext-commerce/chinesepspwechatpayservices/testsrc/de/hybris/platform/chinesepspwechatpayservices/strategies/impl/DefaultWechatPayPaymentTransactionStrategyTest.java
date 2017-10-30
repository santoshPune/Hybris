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
package de.hybris.platform.chinesepspwechatpayservices.strategies.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.chinesepspwechatpayservices.constants.WechatPaymentConstants;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayPaymentTransactionDao;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayPaymentTransactionEntryDao;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatPayQueryResult;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 *
 */
@IntegrationTest
public class DefaultWechatPayPaymentTransactionStrategyTest extends ServicelayerTransactionalTest
{
	@Resource(name = "wechatPayPaymentTransactionStrategy")
	private DefaultWechatPayPaymentTransactionStrategy defaultWechatPayPaymentTransactionStrategy;

	private OrderModel order;

	private CurrencyModel currencyModel;

	private WechatRawDirectPayNotification wechatPayNotifyResponseData;

	private WechatPayQueryResult wechatPayQueryResult;

	private WechatpayPaymentTransactionModel wechatpayPaymentTransactionModel;

	private WechatpayPaymentTransactionEntryModel wechatpayPaymentTransactionEntryModel;

	@Resource(name = "wechatPayPaymentTransactionEntryDao")
	private WechatPayPaymentTransactionEntryDao wechatPayPaymentTransactionEntryDao;

	@Resource(name = "wechatPayPaymentTransactionDao")
	private WechatPayPaymentTransactionDao wechatPayPaymentTransactionDao;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userService")
	private UserService userService;

	@Mock
	private KeyGenerator paymentTransactionKeyGenerator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		defaultWechatPayPaymentTransactionStrategy = new DefaultWechatPayPaymentTransactionStrategy();
		defaultWechatPayPaymentTransactionStrategy.setModelService(modelService);
		defaultWechatPayPaymentTransactionStrategy.setPaymentTransactionKeyGenerator(paymentTransactionKeyGenerator);
		defaultWechatPayPaymentTransactionStrategy.setWechatPayPaymentTransactionDao(wechatPayPaymentTransactionDao);
		defaultWechatPayPaymentTransactionStrategy.setWechatPayPaymentTransactionEntryDao(wechatPayPaymentTransactionEntryDao);

		wechatpayPaymentTransactionModel = new WechatpayPaymentTransactionModel();
		wechatpayPaymentTransactionModel.setCode("000001");
		wechatpayPaymentTransactionModel.setOrder(order);
		wechatpayPaymentTransactionModel.setWechatpayCode("000111");
		wechatpayPaymentTransactionModel.setRequestId("000011");

		wechatpayPaymentTransactionEntryModel = new WechatpayPaymentTransactionEntryModel();
		wechatpayPaymentTransactionEntryModel.setPaymentTransaction(wechatpayPaymentTransactionModel);
		wechatpayPaymentTransactionEntryModel.setType(PaymentTransactionType.WECHAT_REQUEST);
		wechatpayPaymentTransactionEntryModel.setCode(String.valueOf(paymentTransactionKeyGenerator.generate()) + "44");

		final List<PaymentTransactionModel> paymentTransactionModels = new ArrayList<>();
		paymentTransactionModels.add(wechatpayPaymentTransactionModel);

		currencyModel = new CurrencyModel();
		currencyModel.setIsocode("USD");

		order = new OrderModel();
		order.setCode("00000001");
		order.setTotalPrice((Double.valueOf(1524.62)));
		order.setCurrency(currencyModel);
		order.setDate(new Date());
		order.setUser(userService.getCurrentUser());
		order.setPaymentTransactions(paymentTransactionModels);

		wechatPayNotifyResponseData = new WechatRawDirectPayNotification();
		wechatPayNotifyResponseData.setOutTradeNo("00000001");
		wechatPayNotifyResponseData.setOpenid("openid000001");
		wechatPayNotifyResponseData.setCouponFee(9542);
		wechatPayNotifyResponseData.setTotalFee(152462);
		wechatPayNotifyResponseData.setTransactionId("111111");

		wechatPayQueryResult = new WechatPayQueryResult();
		wechatPayQueryResult.setOutTradeNo("00000001");
		wechatPayQueryResult.setOpenid("openid000001");
		wechatPayQueryResult.setCouponFee(9542);
		wechatPayQueryResult.setTotalFee(152462);
		wechatPayQueryResult.setTransactionId("111111");

		modelService.save(wechatpayPaymentTransactionEntryModel);
		modelService.save(wechatpayPaymentTransactionModel);
		modelService.save(order);
		modelService.refresh(wechatpayPaymentTransactionEntryModel);
		modelService.refresh(wechatpayPaymentTransactionModel);
		modelService.refresh(order);

		Mockito.doReturn("001111").when(paymentTransactionKeyGenerator).generate();
	}

	@Test
	public void test_Update_For_Successful_Notification()
	{
		wechatPayNotifyResponseData.setResultCode(WechatPaymentConstants.Notification.RESULT_SUCCESS);

		Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(order, TransactionStatus.ACCEPTED);
		assertFalse(wechatpayTransaction.isPresent());

		defaultWechatPayPaymentTransactionStrategy.updateForNotification(order, wechatPayNotifyResponseData);

		wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(order,
				TransactionStatus.ACCEPTED);
		assertTrue(wechatpayTransaction.isPresent());
	}

	@Test
	public void test_Update_For_Failed_Notification()
	{
		wechatPayNotifyResponseData.setResultCode(WechatPaymentConstants.Notification.RESULT_FAIL);

		Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(order, TransactionStatus.REJECTED);
		assertFalse(wechatpayTransaction.isPresent());

		defaultWechatPayPaymentTransactionStrategy.updateForNotification(order, wechatPayNotifyResponseData);

		wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(order,
				TransactionStatus.REJECTED);
		assertTrue(wechatpayTransaction.isPresent());
	}

	@Test
	public void test_Save_For_Status_Check_With_Succuss_Result()
	{
		wechatPayQueryResult.setTradeState("SUCCESS");
		wechatPayQueryResult.setTradeStateDesc("SUCCESS");

		Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(order, TransactionStatus.ACCEPTED);
		assertFalse(wechatpayTransaction.isPresent());

		defaultWechatPayPaymentTransactionStrategy.saveForStatusCheck(order, wechatPayQueryResult);

		wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(order,
				TransactionStatus.ACCEPTED);
		assertTrue(wechatpayTransaction.isPresent());
	}

	@Test
	public void test_Save_For_Status_Check_With_Closed_Result()
	{
		wechatPayQueryResult.setTradeState("CLOSED");
		wechatPayQueryResult.setTradeStateDesc("CLOSED");

		Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(order, TransactionStatus.REJECTED);
		assertFalse(wechatpayTransaction.isPresent());

		defaultWechatPayPaymentTransactionStrategy.saveForStatusCheck(order, wechatPayQueryResult);

		wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(order,
				TransactionStatus.REJECTED);
		assertTrue(wechatpayTransaction.isPresent());
	}

	@Test
	public void test_Save_For_Status_Check_With_Payerror_Result()
	{
		wechatPayQueryResult.setTradeState("PAYERROR");
		wechatPayQueryResult.setTradeStateDesc("PAYERROR");

		Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy
				.getPaymentTransactionWithCaptureEntry(order, TransactionStatus.ERROR);
		assertFalse(wechatpayTransaction.isPresent());

		defaultWechatPayPaymentTransactionStrategy.saveForStatusCheck(order, wechatPayQueryResult);

		wechatpayTransaction = defaultWechatPayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(order,
				TransactionStatus.ERROR);
		assertTrue(wechatpayTransaction.isPresent());
	}

	@Test
	public void test_create_Transacion_ForNew_Request()
	{

		final OrderModel newOrder = new OrderModel();
		newOrder.setCode("00000002");
		newOrder.setTotalPrice((Double.valueOf(1524.62)));
		newOrder.setCurrency(currencyModel);
		newOrder.setDate(new Date());
		newOrder.setUser(userService.getCurrentUser());

		defaultWechatPayPaymentTransactionStrategy.createForNewRequest(newOrder);
		final Optional<WechatpayPaymentTransactionModel> wechatpayTransaction = wechatPayPaymentTransactionDao
				.findTransactionByLatestRequestEntry(newOrder, true);
		assertTrue(wechatpayTransaction.isPresent());


		final List<WechatpayPaymentTransactionEntryModel> wechatTransactionEntries = wechatPayPaymentTransactionEntryDao
				.findPaymentTransactionEntryByTypeAndStatus(PaymentTransactionType.WECHAT_REQUEST, TransactionStatus.ACCEPTED,
						wechatpayTransaction.get());

		assertTrue(wechatTransactionEntries.size() > 0);
	}
}
