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

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chinesepspwechatpayservices.exception.WechatPayException;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.OpenIdRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class OpenIdRequestProcessorTest
{

	private WechatPayConfiguration config;

	@Mock
	private WechatPayHttpClient httpClient;

	private OpenIdRequestProcessor processor;

	private final String userCode = "sfgd141fvsd5";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		config = new WechatPayConfiguration();
		config.setAppId("wx901a348feec9d417");
		config.setAccessTokenURL("https://api.weixin.qq.com/sns/oauth2/access_token");
		config.setAppSecret("381e161cff142a51ba70e554c5b3ae6c");
		config.setBasePath("https://abc129.cn/yacceleratorstorefront");
		config.setMechId("132193615701");
		config.setMechKey("56339caf2d55495fba8171d9b8823ef2");
		config.setOauthURL("https://open.weixin.qq.com/connect/oauth2/authorize");
		config.setOrderQueryURL("https://api.mch.weixin.qq.com/pay/orderquery");
		config.setUnifiedOrderURL("https://api.mch.weixin.qq.com/pay/unifiedorder");

		processor = new OpenIdRequestProcessor(config, httpClient, userCode);
	}

	@Test
	public void valid_openid_returned()
	{
		given(processor.get()).willReturn("{\"openid\":\"openidsample\"}");
		final String openId = processor.process();
		Assert.assertEquals("openidsample", openId);
	}

	@Test(expected = WechatPayException.class)
	public void no_openid_returned()
	{
		given(processor.get()).willReturn("{\"errcode\":40029,\"errmsg\":\"invalid code\"}");
		processor.process();
	}

}
