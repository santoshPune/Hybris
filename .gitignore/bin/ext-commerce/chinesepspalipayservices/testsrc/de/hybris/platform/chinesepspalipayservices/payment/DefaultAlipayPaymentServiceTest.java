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
package de.hybris.platform.chinesepspalipayservices.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepspalipayservices.alipay.AlipayConfiguration;
import de.hybris.platform.chinesepspalipayservices.alipay.AlipayUtil;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundRequestData;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayPaymentTransactionStrategy;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayResponseValidationStrategy;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;




@UnitTest
public class DefaultAlipayPaymentServiceTest
{

	private static final Logger LOG = Logger.getLogger(DefaultAlipayPaymentServiceTest.class);

	private DefaultAlipayPaymentService defaultAlipayPaymentService;

	private AlipayPaymentTransactionModel alipayPaymentTransactionModel;

	private OrderModel orderModel;

	@Mock
	private AlipayPaymentTransactionStrategy alipayPaymentTransactionStrategy;

	@Mock
	private AlipayResponseValidationStrategy alipayResponseValidationStrategy;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse response;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		alipayPaymentTransactionModel = new AlipayPaymentTransactionModel();
		alipayPaymentTransactionModel.setAlipayCode("2011011201037066");

		orderModel = new OrderModel();
		orderModel.setCode("00000001");
		orderModel.setTotalPrice(1.5);

		AlipayConfiguration alipayConfiguration = new AlipayConfiguration();
		alipayConfiguration.setRefundServiceApiName("refund_fastpay_by_platform_pwd");
		alipayConfiguration.setWebPartner("2088101008267254");
		alipayConfiguration.setBasepath("https://electronics.local:9002/yacceleratorstorefront/");
		alipayConfiguration.setWebSellerEmail("Jier1105@alitest.com");
		alipayConfiguration.setWebSellerId("2088101008267254");
		alipayConfiguration.setRefundReason("协商退款");

		defaultAlipayPaymentService = new DefaultAlipayPaymentService();
		defaultAlipayPaymentService.setAlipayPaymentTransactionStrategy(alipayPaymentTransactionStrategy);
		defaultAlipayPaymentService.setAlipayConfiguration(alipayConfiguration);

	}


	@Test
	public void test_Create_Alipay_Refund_Request_Data_By_Order_Successfully()
	{
		given(alipayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(Mockito.any(), Mockito.any())).willReturn(
				Optional.of(alipayPaymentTransactionModel));

		Optional<AlipayRefundRequestData> result = defaultAlipayPaymentService.createAlipayRefundRequestDataByOrder(orderModel);
		assertTrue(result.isPresent());
		AlipayRefundRequestData alipayRefundRequestData = result.get();
		assertEquals("refund_fastpay_by_platform_pwd", alipayRefundRequestData.getService());
		assertEquals("2088101008267254", alipayRefundRequestData.getPartner());
		assertEquals("utf-8", alipayRefundRequestData.getInputCharset());
		assertEquals(
				"https://electronics.local:9002/yacceleratorstorefront/checkout/multi/summary/alipay/pspasynresponse/refundnotifyController",
				alipayRefundRequestData.getNotifyUrl());
		assertEquals("Jier1105@alitest.com", alipayRefundRequestData.getSellerEmail());
		assertEquals("2088101008267254", alipayRefundRequestData.getSellerUserId());
		assertEquals("1", alipayRefundRequestData.getBatchNum());
		assertEquals("2011011201037066^1.50^协商退款", alipayRefundRequestData.getDetailData());
	}

	@Test
	public void test_Create_Alipay_Refund_Request_Data_By_Order_With_Unpaid_Order()
	{
		given(alipayPaymentTransactionStrategy.getPaymentTransactionWithCaptureEntry(Mockito.any(), Mockito.any())).willReturn(
				Optional.empty());

		Optional<AlipayRefundRequestData> result = defaultAlipayPaymentService.createAlipayRefundRequestDataByOrder(orderModel);
		assertFalse(result.isPresent());
	}

	@Test
	public void test_Handle_Notification_with_inValid_Response()
	{
		given(request.getRequestURL()).willReturn(new StringBuffer(
				"https://electronics.local:9002/yacceleratorstorefront/checkout/multi/summary/alipay/pspasynresponse/refundnotifyController"));

		Map<String, String> responseMap = new HashMap<String, String>();
		responseMap.put("notify_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		responseMap.put("notify_type", "batch_refund_notify");
		responseMap.put("notify_id", AlipayUtil.encrypt("MD5", String.valueOf(System.currentTimeMillis())));
		responseMap.put("sign_type", "MD5");
		responseMap.put("batch_no", "2016033100008001");
		responseMap.put("success_num", "1");
		responseMap.put("result_details", "2010031906272929^80^SUCCESS$jax_chuanhang@alipay.com^2088101003147483^0.01^SUCCESS");
		responseMap.put("sign", "12345678910524860135");

		Map<String, String[]> parameterMap = new HashMap<String, String[]>();
		parameterMap.put("notify_time", new String[]
		{ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) });
		parameterMap.put("notify_type", new String[]
		{ "batch_refund_notify" });
		parameterMap.put("notify_id", new String[]
		{ AlipayUtil.encrypt("MD5", String.valueOf(System.currentTimeMillis())) });
		parameterMap.put("sign_type", new String[]
		{ "MD5" });
		parameterMap.put("batch_no", new String[]
		{ "2016033100008001" });
		parameterMap.put("success_num", new String[]
		{ "1" });
		parameterMap.put("result_details", new String[]
		{ "2010031906272929^80^SUCCESS$jax_chuanhang@alipay.com^2088101003147483^0.01^SUCCESS" });
		parameterMap.put("sign", new String[]
		{ "12345678910524860135" });

		DefaultAlipayPaymentService alipayPaymentService = new DefaultAlipayPaymentService();
		DefaultAlipayPaymentService spAlipayPaymentService = Mockito.spy(alipayPaymentService);

		spAlipayPaymentService.setAlipayResponseValidationStrategy(alipayResponseValidationStrategy);
		Mockito.when(request.getParameterMap()).thenReturn(parameterMap);
		Mockito.when(alipayResponseValidationStrategy.validateResponse(Mockito.any())).thenReturn(false);
		Mockito.when(spAlipayPaymentService.unifyRequestParameterValue(Mockito.anyMap())).thenReturn(responseMap);

		try
		{
			spAlipayPaymentService.handleAsyncResponse(request, response);
			Mockito.verify(spAlipayPaymentService, Mockito.times(0)).handleNotification(responseMap, response);
		}
		catch (IOException e1)
		{
			LOG.error("IOException!", e1);
		}
	}


}
