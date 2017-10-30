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
package de.hybris.platform.cmswebservices.security;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.data.ComponentTypeListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;

import java.util.Locale;

import org.junit.Test;


@IntegrationTest
public class IsAuthorizedCmsManagerIntegrationTest extends ApiBaseIntegrationTest
{
	private static final String URI = "/v1/types";
	private static final String INVALID = "invalid";

	@SuppressWarnings("unchecked")
	@Test
	public void shouldFailAuthorization_emptyToken() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(URI).acceptJson().acceptLanguages(Locale.ENGLISH)
				.authorizationHeader(String.format(TOKEN_TEMPLATE, ""))
				.get(ComponentTypeListData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 401);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldFailAuthorization_invalidToken() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(URI).acceptJson().acceptLanguages(Locale.ENGLISH)
				.authorizationHeader(String.format(TOKEN_TEMPLATE, INVALID))
				.get(ComponentTypeListData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 401);
	}

}
