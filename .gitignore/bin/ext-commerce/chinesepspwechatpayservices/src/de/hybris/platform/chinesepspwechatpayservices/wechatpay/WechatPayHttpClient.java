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
package de.hybris.platform.chinesepspwechatpayservices.wechatpay;

import de.hybris.platform.chinesepspwechatpayservices.exception.WechatPayException;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.http.MediaType;


/**
 * A wrapper around Apache Common HttpClient
 */
public class WechatPayHttpClient
{
	private final HttpClient httpClient;

	private static final Logger LOG = Logger.getLogger(WechatPayHttpClient.class);

	public WechatPayHttpClient(final HttpClient httpClient)
	{
		super();
		this.httpClient = httpClient;
	}

	/**
	 * Process a POST request
	 *
	 * @param url
	 *           URL of the post target
	 * @param requestBody
	 *           body of the request, MUST be XML format
	 *
	 * @return the request response
	 */
	public String post(final String url, final String requestBody)
	{
		final HttpMethod m = new PostMethod(url);
		m.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try
		{
			((PostMethod) m).setRequestEntity(new StringRequestEntity(requestBody, MediaType.APPLICATION_XML.toString(),
					CharEncoding.UTF_8));

			final int statusCode = httpClient.executeMethod(m);
			final String result = m.getResponseBodyAsString();

			if (statusCode != HttpStatus.SC_OK || StringUtils.isEmpty(result))
			{
				throw new WechatPayException("Wechat Pay post method failed: " + m.getStatusLine());
			}

			LOG.debug("POST to URL " + url + " with body=" + requestBody + ", response code=" + statusCode + ", response=" + result);

			return result;
		}
		catch (final IOException e)
		{
			throw new WechatPayException("Wechat Pay post method failed: ", e);
		}
		finally
		{
			m.releaseConnection();
		}
	}

	/**
	 * Process a GET request
	 *
	 * @param url
	 *           URL of the get target
	 *
	 * @return the request response
	 */
	public String get(final String url)
	{
		final HttpMethod m = new GetMethod(url);
		m.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());

		try
		{
			final int statusCode = httpClient.executeMethod(m);
			final String result = m.getResponseBodyAsString();

			if (statusCode != HttpStatus.SC_OK || StringUtils.isEmpty(result))
			{
				throw new WechatPayException("Wechat Pay get method failed: " + m.getStatusLine());
			}

			LOG.debug("GET to URL " + url + ", response code=" + statusCode + ", response=" + result);

			return result;
		}
		catch (final IOException e)
		{
			throw new WechatPayException("Wechat Pay get method failed: ", e);
		}
		finally
		{
			m.releaseConnection();
		}
	}


}
