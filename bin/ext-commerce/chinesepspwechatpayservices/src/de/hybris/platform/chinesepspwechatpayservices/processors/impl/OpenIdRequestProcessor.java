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
package de.hybris.platform.chinesepspwechatpayservices.processors.impl;

import de.hybris.platform.chinesepspwechatpayservices.exception.WechatPayException;
import de.hybris.platform.chinesepspwechatpayservices.processors.AbstractWechatPayRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;

import java.util.Map;

import groovy.json.JsonSlurper;


/**
 * Processor for fetching openid.
 */
public class OpenIdRequestProcessor extends AbstractWechatPayRequestProcessor<String>
{
	public OpenIdRequestProcessor(final WechatPayConfiguration config, final WechatPayHttpClient httpClient, final String code)
	{
		super(config, httpClient);
		this.setUrl(config.getAccessTokenURL());
		this.addParameter("secret", config.getAppSecret());
		this.addParameter("code", code);
		this.addParameter("grant_type", "authorization_code");
	}

	@Override
	public String process()
	{
		final JsonSlurper slurper = new JsonSlurper();
		final Map<String, Object> map = (Map<String, Object>) slurper.parseText(get());
		final Object openId = map.get("openid");
		if (openId == null)
		{
			throw new WechatPayException("Get openid error!");
		}
		else
		{
			return openId.toString();
		}
	}
}
