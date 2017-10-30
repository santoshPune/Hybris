/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.smartedit.ssl;

import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSLValidationManager {
	
	public static final Logger LOG = LoggerFactory.getLogger(SSLValidationManager.class);
	
	private final boolean enableSSLValidation;

	public SSLValidationManager(boolean enable_ssl_validation) {
	      this.enableSSLValidation = enable_ssl_validation;
	   }
	
	@PostConstruct
	public void disableSSLValidation() throws Exception{
		if(!enableSSLValidation) {
            SSLContext sslc = SSLContext.getInstance("TLS");
            TrustManager[] trustManagerArray = { new NullX509TrustManager() };
            sslc.init(null, trustManagerArray, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new NullHostnameVerifier());
		}
    }
 
    private static class NullX509TrustManager extends X509ExtendedTrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2) throws CertificateException {
	
		}
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2)
				throws CertificateException {

		}
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2) throws CertificateException {

		}
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2)
				throws CertificateException {
		}
    }
 
    private static class NullHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

}
