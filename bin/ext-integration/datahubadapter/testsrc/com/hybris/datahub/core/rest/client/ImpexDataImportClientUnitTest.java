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
package com.hybris.datahub.core.rest.client;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

@UnitTest
public class ImpexDataImportClientUnitTest {

	private static final String sContentDisposition = "attachment; filename=\"noFile\"";
	private static final String tenantID = "master";

	private final ImpexDataImportClient impexDataImportClient = new ImpexDataImportClient();

	@Test(expected = IllegalStateException.class)
	public void testReadDataInvalidURL()
	{
		impexDataImportClient.setClientConfig(null);
		impexDataImportClient.setRetryTemplate(setupRetryTemplate());
		impexDataImportClient.readData("http://fake_url", requestHeaders());
	}

	@Test(expected = IllegalStateException.class)
	public void testReturnImportResultInvalidURL()
	{
		impexDataImportClient.setClientConfig(null);
		impexDataImportClient.setRetryTemplate(setupRetryTemplate());
		impexDataImportClient.returnImportResult("http://fake_url", null);
	}

	private HashMap<String, String> requestHeaders()
	{
		final HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Disposition", sContentDisposition);
		headers.put("tenantID", tenantID);
		return headers;
	}

	private RetryTemplate setupRetryTemplate()
	{
		final ClientRetryListener retryListener = new ClientRetryListener();
		retryListener.setInitialInterval(1000);
		retryListener.setMaxAttempts(3);	// default
		retryListener.setMultiplier(2);

		final RetryTemplate retryTemplate = new RetryTemplate();
		retryTemplate.registerListener(retryListener);
		retryTemplate.setBackOffPolicy(new ExponentialBackOffPolicy());

		return retryTemplate;
	}

}