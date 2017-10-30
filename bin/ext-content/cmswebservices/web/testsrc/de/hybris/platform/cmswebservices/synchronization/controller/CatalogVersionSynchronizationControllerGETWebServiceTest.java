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
package de.hybris.platform.cmswebservices.synchronization.controller;

import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.ONLINE;
import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.SyncJobData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother;

import java.util.HashMap;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


@IntegrationTest
public class CatalogVersionSynchronizationControllerGETWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String INVALID = "INVALID";
	private static final String TARGET_VERSION = "targetVersionId";
	private static final String SOURCE_VERSION = "sourceVersionId";
	private static final String CATALOG = "catalogId";
	private static final String ENDPOINT = "/v1/catalogs/{catalogId}/versions/{sourceVersionId}/synchronizations/versions/{targetVersionId}";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	private CatalogVersionModel stagedCatalogVersionModel;
	private CatalogVersionModel onlineCatalogVersionModel;

	@Before
	public void setup()
	{
		stagedCatalogVersionModel = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		onlineCatalogVersionModel = catalogVersionModelMother.createAppleOnlineCatalogVersionModel();
	}

	@Test
	public void shouldGetSyncronizationStatus() throws Exception
	{


		catalogVersionModelMother.performCatalogSyncronization(stagedCatalogVersionModel, onlineCatalogVersionModel);

		final ApiClient apiClient = getApiClientInstance();
		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(CATALOG, ContentCatalogModelMother.CatalogTemplate.ID_APPLE.name());
		variables.put(SOURCE_VERSION, STAGED.getVersion());
		variables.put(TARGET_VERSION, ONLINE.getVersion());

		final Response<SyncJobData> response = apiClient.request().endpoint(replaceUriVariables(ENDPOINT, variables)).acceptJson()
				.get(SyncJobData.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNotNull(response.getBody().getSyncStatus());
		assertNotNull(response.getBody().getLastModifiedDate());
		assertNotNull(response.getBody().getSyncResult());
		assertNotNull(response.getBody().getCreationDate());
		assertNotNull(response.getBody().getEndDate());
		assertNotNull(response.getBody().getStartDate());

	}

	@Test
	public void shouldStatusBeNull() throws Exception
	{

		final ApiClient apiClient = getApiClientInstance();
		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(CATALOG, ID_APPLE.name());
		variables.put(SOURCE_VERSION, STAGED.getVersion());
		variables.put(TARGET_VERSION, ONLINE.getVersion());

		final Response<SyncJobData> response = apiClient.request().endpoint(replaceUriVariables(ENDPOINT, variables)).acceptJson()
				.get(SyncJobData.class);

		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertNull(response.getBody().getSyncStatus());
		assertNull(response.getBody().getLastModifiedDate());
		assertNull(response.getBody().getSyncResult());
		assertNull(response.getBody().getCreationDate());
		assertNull(response.getBody().getEndDate());
		assertNull(response.getBody().getStartDate());
	}



	@Test
	public void shouldReturnErrorValidation_catalogDoesNotExist() throws Exception
	{

		final ApiClient apiClient = getApiClientInstance();
		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(CATALOG, INVALID);
		variables.put(SOURCE_VERSION, STAGED.getVersion());
		variables.put(TARGET_VERSION, ONLINE.getVersion());

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, variables)).acceptJson().get(ValidationErrorResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertTrue(response.getErrorResponse().getErrors().size() == 1);
	}

	@Test
	public void shouldReturnErrorValidation_sourceDoesNotExist() throws Exception
	{

		final ApiClient apiClient = getApiClientInstance();
		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(CATALOG, ID_APPLE.name());
		variables.put(SOURCE_VERSION, INVALID);
		variables.put(TARGET_VERSION, ONLINE.getVersion());

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, variables)).acceptJson().get(ValidationErrorResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertTrue(response.getErrorResponse().getErrors().size() == 1);
	}

	@Test
	public void shouldReturnErrorValidation_targetDoesNotExist() throws Exception
	{

		final ApiClient apiClient = getApiClientInstance();
		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(CATALOG, ID_APPLE.name());
		variables.put(SOURCE_VERSION, STAGED.getVersion());
		variables.put(TARGET_VERSION, INVALID);

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, variables)).acceptJson().get(ValidationErrorResponse.class);

		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
		assertTrue(response.getErrorResponse().getErrors().size() == 1);
	}

}
