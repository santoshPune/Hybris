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
package de.hybris.platform.chinesepspwechatpayservices.notifications.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepaymentservices.order.service.ChineseOrderService;
import de.hybris.platform.chinesepspwechatpayservices.constants.WechatPaymentConstants;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayOrderDao;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;
import de.hybris.platform.chinesepspwechatpayservices.strategies.WechatPayPaymentTransactionStrategy;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 *
 */
@UnitTest
public class DefaultWechatPayNotificationServiceTest
{
	private DefaultWechatPayNotificationService defaultWechatPayNotificationService;

	@Mock
	private ModelService modelService;

	@Mock
	private WechatPayPaymentTransactionStrategy wechatPayPaymentTransactionStrategy;

	@Mock
	private WechatPayOrderDao wechatPayOrderDao;

	@Mock
	private ChineseOrderService chineseOrderService;

	private OrderModel order;

	private WechatRawDirectPayNotification wechatpayNotification;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		defaultWechatPayNotificationService = new DefaultWechatPayNotificationService();

		defaultWechatPayNotificationService.setModelService(modelService);
		defaultWechatPayNotificationService.setChineseOrderService(chineseOrderService);
		defaultWechatPayNotificationService.setWechatPayOrderDao(wechatPayOrderDao);
		defaultWechatPayNotificationService.setWechatPayPaymentTransactionStrategy(wechatPayPaymentTransactionStrategy);

		wechatpayNotification = new WechatRawDirectPayNotification();
		wechatpayNotification.setResultCode(WechatPaymentConstants.Notification.RESULT_SUCCESS);
		wechatpayNotification.setReturnCode(WechatPaymentConstants.Notification.RETURN_SUCCESS);

		order = new OrderModel();
		order.setCode("00000001");
		order.setTotalPrice((Double.valueOf(1524.62)));
		order.setPaymentStatus(PaymentStatus.NOTPAID);

		Mockito.doNothing().when(chineseOrderService).markOrderAsPaid(Mockito.any());
		Mockito.doReturn(Optional.of(order)).when(wechatPayOrderDao).findOrderByCode(Mockito.any());
		Mockito.doNothing().when(wechatPayPaymentTransactionStrategy).updateForNotification(order, wechatpayNotification);
		Mockito.doNothing().when(modelService).save(Mockito.any());
	}

	@Test
	public void test_Handle_WechatPay_Payment_Success_Response()
	{
		wechatpayNotification.setResultCode(WechatPaymentConstants.Notification.RESULT_SUCCESS);
		wechatpayNotification.setReturnCode(WechatPaymentConstants.Notification.RETURN_SUCCESS);

		defaultWechatPayNotificationService.handleWechatPayPaymentResponse(wechatpayNotification);

		assertEquals("PAID", order.getPaymentStatus().toString());
	}

	@Test
	public void test_Handle_WechatPay_Payment_Failed_Response()
	{
		wechatpayNotification.setResultCode(WechatPaymentConstants.Notification.RESULT_SUCCESS);
		wechatpayNotification.setReturnCode(WechatPaymentConstants.Notification.RETURN_FAIL);

		defaultWechatPayNotificationService.handleWechatPayPaymentResponse(wechatpayNotification);

		assertEquals("NOTPAID", order.getPaymentStatus().toString());
	}
}
