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
package de.hybris.platform.cmswebservices.languages.controller;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.LanguageListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.LanguageModelMother;
import de.hybris.platform.cmswebservices.util.models.SiteModelMother;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;


@IntegrationTest
public class LanguageControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String INVALID = "invalid";
	private static final String GET_ALL_LANGUAGES = "/v1/sites/{siteId}/languages";

	@Resource
	private SiteModelMother siteModelMother;

	protected void createElectronicsSiteWithEmptyAppleCatalog()
	{
		siteModelMother.createElectronicsWithAppleCatalog();
	}

	@Test
	public void shouldGetAllLanguagesFromElectronicsSite() throws Exception
	{
		createElectronicsSiteWithEmptyAppleCatalog();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CmswebservicesConstants.URI_SITE_ID, SiteModelMother.ELECTRONICS);

		final ApiClient apiClient = getApiClientInstance();
		final Response<LanguageListData> response = apiClient.request()
				.endpoint(replaceUriVariables(GET_ALL_LANGUAGES, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(LanguageListData.class);

		assertStatusCode(response, 200);

		assertEquals(1, response.getBody().getLanguages().size());
		assertEquals(LanguageModelMother.CODE_EN.toLowerCase(), response.getBody().getLanguages().get(0).getIsocode());
	}

	@Test
	public void shouldReturnNotFoundResponseCode_InvalidSiteId() throws Exception
	{
		createElectronicsSiteWithEmptyAppleCatalog();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CmswebservicesConstants.URI_SITE_ID, INVALID);

		final ApiClient apiClient = getApiClientInstance();

		final Response<LanguageListData> response = apiClient.request()
				.endpoint(replaceUriVariables(GET_ALL_LANGUAGES, uriVariables))
				.acceptJson().acceptLanguages(Locale.ENGLISH)
				.get(LanguageListData.class);

		assertStatusCode(response, 404);
	}

}
