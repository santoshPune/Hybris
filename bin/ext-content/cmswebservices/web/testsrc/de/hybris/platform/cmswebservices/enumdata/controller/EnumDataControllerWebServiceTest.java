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
package de.hybris.platform.cmswebservices.enumdata.controller;

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.data.EnumListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;

import java.util.Locale;

import org.junit.Test;

/**
 * Test for <code>EnumDataController</code>.
 *
 */
@IntegrationTest
public class EnumDataControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String GET_ALL_ENDPOINT = "/v1/enums";
	private static final String AB_TEST_SCOPE_CLASS = "de.hybris.platform.cms2.enums.ABTestScopes";

	@Test
	public void getEnumerationValues() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();

		final Response<EnumListData> response = apiClient.request()
				.endpoint(GET_ALL_ENDPOINT).acceptJson()
				.parameter("enumClass", AB_TEST_SCOPE_CLASS)
				.acceptLanguages(Locale.ENGLISH).get(EnumListData.class);

		assertStatusCode(response, 200);

		final EnumListData enumValues = response.getBody();
		assertNotNull("Expected values back for call to get enums but was null", enumValues);
	}

}
