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
package de.hybris.platform.chinesepspwechatpayservices.processors;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepspwechatpayservices.data.StartPaymentData;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.StartPaymentRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


@UnitTest
public class StartPaymentRequestProcessorTest
{

	private WechatPayConfiguration config;

	private StartPaymentRequestProcessor processor;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		config = new WechatPayConfiguration();
		config.setAppId("wx901a348feec91234");
		config.setAccessTokenURL("https://api.weixin.qq.com/sns/oauth2/access_token");
		config.setAppSecret("381e161cff142a51ba70e554c5b3ae6c");
		config.setBasePath("https://abc129.cn/yacceleratorstorefront");
		config.setMechId("132193615701");
		config.setMechKey("56339caf2d55495fba8171d9b8823ef2");
		config.setOauthURL("https://open.weixin.qq.com/connect/oauth2/authorize");
		config.setOrderQueryURL("https://api.mch.weixin.qq.com/pay/orderquery");
		config.setUnifiedOrderURL("https://api.mch.weixin.qq.com/pay/unifiedorder");

		final String prepay_id = "12345678910";

		processor = new StartPaymentRequestProcessor(config, prepay_id);
	}

	@Test
	public void test_Process()
	{
		final StartPaymentData data = processor.process();
		assertEquals("wx901a348feec91234", data.getAppId());
		assertEquals(10, data.getTimeStamp().length());
		assertEquals(32, data.getNonceStr().length());
		assertEquals("prepay_id=12345678910", data.getPackageName());
		assertEquals("MD5", data.getSignType());


	}
}
