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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.CharEncoding;


/**
 * Processor for fetching usercode.
 */
public class UserCodeRequestProcessor extends AbstractWechatPayRequestProcessor<Void>
{

	private static final String WECHAT_REDIRECT_PARAM = "#wechat_redirect";

	private final HttpServletRequest request;
	private final HttpServletResponse response;

	public UserCodeRequestProcessor(final WechatPayConfiguration config, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		super(config, null);
		this.setUrl(config.getOauthURL());
		this.request = request;
		this.response = response;
		this.addParameter("redirect_uri", getRedirectUri());
		this.addParameter("response_type", "code");
		this.addParameter("scope", "snsapi_base");
	}

	private String getRedirectUri()
	{
		final String redirectUri = request.getRequestURL().toString() + "?showwxpaytitle=1";
		try
		{
			return URLEncoder.encode(redirectUri, CharEncoding.UTF_8);
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new WechatPayException("Error encode redirect uri: " + redirectUri, e);
		}
	}

	@Override
	public Void process()
	{
		final String url = getParams().generateGetURL(this.getUrl()) + WECHAT_REDIRECT_PARAM;
		try
		{
			this.response.sendRedirect(url);
		}
		catch (final IOException e)
		{
			throw new WechatPayException("Error redirect to " + url, e);
		}
		return null;
	}
}
