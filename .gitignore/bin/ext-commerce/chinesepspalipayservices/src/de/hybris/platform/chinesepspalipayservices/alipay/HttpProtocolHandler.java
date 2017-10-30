/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspalipayservices.alipay;

import de.hybris.platform.chinesepspalipayservices.constants.PaymentConstants;
import de.hybris.platform.chinesepspalipayservices.data.HttpRequest;
import de.hybris.platform.chinesepspalipayservices.data.HttpResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;
import org.apache.log4j.Logger;



public class HttpProtocolHandler
{
	private static Logger LOG = Logger.getLogger(HttpProtocolHandler.class);

	private static String DEFAULT_CHARSET = "utf-8";

	private final int defaultConnectionTimeout = 20000;

	private final int defaultSoTimeout = 30000;

	private final int defaultIdleConnTimeout = 60000;

	private final int defaultMaxConnPerHost = 30;

	private final int defaultMaxTotalConn = 80;

	private static final long DefaultHttpConnectionManagerTimeout = 3 * 1000;

	private final HttpConnectionManager connectionManager;

	private static HttpProtocolHandler httpProtocolHandler = new HttpProtocolHandler();


	public static HttpProtocolHandler getInstance(final String testMode)
	{
		if ("true".equalsIgnoreCase(testMode.trim()))
		{
			final Protocol trustAllHttps = new Protocol("https", new TrustAllSSLProtocolSocketFactory(), 9002);

			Protocol.registerProtocol("https", trustAllHttps);

		}
		else
		{

			Protocol.unregisterProtocol("https");
		}


		return httpProtocolHandler;
	}


	private HttpProtocolHandler()
	{
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnPerHost);
		connectionManager.getParams().setMaxTotalConnections(defaultMaxTotalConn);

		final IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
		ict.addConnectionManager(connectionManager);
		ict.setConnectionTimeout(defaultIdleConnTimeout);

		ict.start();
	}

	public HttpResponse execute(final HttpRequest request)
	{

		final HttpClient httpclient = new HttpClient(connectionManager);

		int connectionTimeout = defaultConnectionTimeout;
		if (request.getConnectionTimeout() > 0)
		{
			connectionTimeout = request.getConnectionTimeout();
		}
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);

		int soTimeout = defaultSoTimeout;
		if (request.getTimeout() > 0)
		{
			soTimeout = request.getTimeout();
		}
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);

		httpclient.getParams().setConnectionManagerTimeout(DefaultHttpConnectionManagerTimeout);

		String charset = request.getCharset();
		charset = charset == null ? DEFAULT_CHARSET : charset;
		HttpMethod method = null;

		if (PaymentConstants.HTTP.METHOD_GET.equals(request.getMethod()))
		{
			method = new GetMethod(request.getUrl());
			method.getParams().setCredentialCharset(charset);
		}
		else
		{
			method = new PostMethod(request.getUrl());
			((PostMethod) method).addParameters(list2NameValuePairArray(request.getParameters()));
			method.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=" + charset);

		}

		method.addRequestHeader("User-Agent", "Mozilla/4.0");
		final HttpResponse response = new HttpResponse();

		try
		{
			httpclient.executeMethod(method);
			response.setStringResult(method.getResponseBodyAsString());
			response.setResponseHeaders(Arrays.asList(method.getResponseHeaders()));

		}
		catch (final IOException e)
		{
			LOG.warn("error on execute http request", e);
			return null;
		}
		finally

		{
			method.releaseConnection();
		}
		return response;

	}


	protected NameValuePair[] list2NameValuePairArray(final List<NameValuePair> nameValuePairList)
	{

		final NameValuePair[] nameValuePairArray = new NameValuePair[nameValuePairList.size()];

		return nameValuePairList.toArray(nameValuePairArray);

	}

}

