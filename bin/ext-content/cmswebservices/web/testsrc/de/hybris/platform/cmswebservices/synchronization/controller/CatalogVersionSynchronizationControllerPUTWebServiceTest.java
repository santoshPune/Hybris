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

import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.hamcrest.Matchers.isOneOf;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.SyncJobData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class CatalogVersionSynchronizationControllerPUTWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String ENDPOINT = "/v1/catalogs/{catalogId}/versions/{sourceVersionId}/synchronizations/versions/{targetVersionId}";
	private static final String CATALOG_UID = "testCatalogSync";
	private static final String SOURCE_VERSION = "STAGE";
	private static final String TARGET_VERSION = "ONLINE";
	private static final String CATALOG_KEY = "catalogId";
	private static final String SOURCE_VERSION_KEY = "sourceVersionId";
	private static final String TARGET_VERSION_KEY = "targetVersionId";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;

	private CatalogVersionModel sourceCatalogVersion;
	private CatalogVersionModel targetCatalogVersion;


	@Before
	public void setup()
	{
		sourceCatalogVersion = catalogVersionModelMother.createCatalogVersionModel(CATALOG_UID, SOURCE_VERSION);
		targetCatalogVersion = catalogVersionModelMother.createCatalogVersionModel(CATALOG_UID, TARGET_VERSION);
	}

	@Test
	public void testCreateSynchronization() throws Exception
	{
		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CATALOG_KEY, CATALOG_UID);
		uriVariables.put(SOURCE_VERSION_KEY, SOURCE_VERSION);
		uriVariables.put(TARGET_VERSION_KEY, TARGET_VERSION);

		final ApiClient apiClient = getApiClientInstance();
		Response<SyncJobData> response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, uriVariables))
				.acceptJson().put(new SyncJobData(), SyncJobData.class);

		assertStatusCode(response, 200);

		response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, uriVariables))
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(SyncJobData.class);

		assertStatusCode(response, 200);

		final SyncJobData syncJobData = response.getBody();
		Assert.assertNotNull(syncJobData);
		final String[] expectedStatusValues = allCronJobStatusCodes();
		final String[] expectedResultsValues = allCronJobResultsCodes();

		Assert.assertThat(upperCase(syncJobData.getSyncStatus()), isOneOf(expectedStatusValues));
		Assert.assertThat(upperCase(syncJobData.getSyncResult()), isOneOf(expectedResultsValues));
		Assert.assertEquals(new SimpleDateFormat("dd MMM yyyy HH:mm").format(syncJobData.getCreationDate()),
				new SimpleDateFormat("dd MMM yyyy HH:mm").format(DateTime.now().toDate()));

	}

	private String[] allCronJobStatusCodes() {
		final Collection<String> allCodes = new ArrayList<>();
		for (final CronJobStatus cronJobStatus : CronJobStatus.values()) {
			allCodes.add(upperCase(cronJobStatus.getCode()));
		}
		return allCodes.toArray(new String[allCodes.size()]);
	}

	private String[] allCronJobResultsCodes() {
		final Collection<String> allResults = new ArrayList<>();
		for (final CronJobResult cronJobResult : CronJobResult.values()) {
			allResults.add(upperCase(cronJobResult.getCode()));
		}
		return allResults.toArray(new String[allResults.size()]);
	}

	@Test
	public void testCreateSynchronizationFailDueToRunningCronJob() throws Exception
	{

		catalogVersionModelMother.createCatalogSyncronizationSyncItemCronJobModel(sourceCatalogVersion, targetCatalogVersion);

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CATALOG_KEY, CATALOG_UID);
		uriVariables.put(SOURCE_VERSION_KEY, SOURCE_VERSION);
		uriVariables.put(TARGET_VERSION_KEY, TARGET_VERSION);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, uriVariables))
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH).put(new SyncJobData(), ValidationErrorResponse.class);

		assertEquals(1, response.getErrorResponse().getErrors().size());
		assertStatusCode(response, 409);
	}


	@Test
	public void testCreateSynchronizationFailDueToInexistingCatalog() throws Exception
	{
		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(CATALOG_KEY, ID_APPLE.name());
		uriVariables.put(SOURCE_VERSION_KEY, SOURCE_VERSION);
		uriVariables.put(TARGET_VERSION_KEY, "CATALOG_NOT_CREATED");

		final ApiClient apiClient = getApiClientInstance();
		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariables(ENDPOINT, uriVariables))
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH).put(new SyncJobData());

		assertStatusCode(response, 400);

	}

}
