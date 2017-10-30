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

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.log4j.Logger;


/**
 *
 */
public class TrustAllSSLProtocolSocketFactory implements ProtocolSocketFactory
{

	private final Logger LOG = Logger.getLogger(TrustAllSSLProtocolSocketFactory.class);
	private SSLContext sslcontext = null;

	protected SSLContext createSSLContext()
	{
		SSLContext sslcontext = null;
		try
		{
			sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[]
			{ new TrustAnyTrustManager() }, new java.security.SecureRandom());
		}
		catch (final NoSuchAlgorithmException | KeyManagementException e)
		{
			LOG.error("createSSLContext error!", e);
		}

		return sslcontext;
	}

	protected SSLContext getSSLContext()
	{
		if (this.sslcontext == null)
		{
			this.sslcontext = createSSLContext();
		}
		return this.sslcontext;
	}

	public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
			throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	@Override
	public Socket createSocket(final String host, final int port) throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(host, port);
	}


	@Override
	public Socket createSocket(final String host, final int port, final InetAddress clientHost, final int clientPort)
			throws IOException, UnknownHostException
	{
		return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
	}

	@Override
	public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort,
			final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException
	{
		if (params == null)
		{
			throw new IllegalArgumentException("Parameters may not be null");
		}
		final int timeout = params.getConnectionTimeout();
		final SocketFactory socketfactory = getSSLContext().getSocketFactory();
		if (timeout == 0)
		{
			return socketfactory.createSocket(host, port, localAddress, localPort);
		}
		else
		{
			return ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
		}
	}

	private static class TrustAnyTrustManager extends X509ExtendedTrustManager
	{

		@Override
		public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException
		{

		}

		@Override
		public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
				throws CertificateException
		{

		}

		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, Socket paramSocket)
				throws CertificateException
		{

		}

		@Override
		public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, Socket paramSocket)
				throws CertificateException
		{

		}

		@Override
		public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, SSLEngine paramSSLEngine)
				throws CertificateException
		{

		}

		@Override
		public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString, SSLEngine paramSSLEngine)
				throws CertificateException
		{

		}
	}


}
