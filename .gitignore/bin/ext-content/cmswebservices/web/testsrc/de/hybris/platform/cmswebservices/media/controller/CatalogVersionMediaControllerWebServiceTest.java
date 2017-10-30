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
package de.hybris.platform.cmswebservices.media.controller;

import static de.hybris.platform.cmswebservices.util.models.MediaModelMother.MediaTemplate.LOGO;
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.MediaModelMother;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;



@IntegrationTest
public class CatalogVersionMediaControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String GET_ONE_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/media/{code}";
	private static final String MEDIA_CODE = "code";
	private static final String INVALID = "invalid";
	private static final String CODE_WITH_JPG_EXTENSION = "some-Media_Code.jpg";
	private static final String CODE_WITH_MULTIPATH = "/path/to/mediaCode";
	private static final String CODE_WITH_MULTIPATH_AND_EXTENSION_AND_WITH_SPACE = "/path/to/mediaCode separated by space";
	private static final String CODE_WITH_MULTIPATH_AND_EXTENSION = "//path/to/mediaCode.jpg";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private MediaModelMother mediaModelMother;

	private CatalogVersionModel catalogVersion;

	@Before
	public void setupFixtures()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		mediaModelMother.createLogoMediaModel(catalogVersion);
		mediaModelMother.createLogoMediaModelWithCode(catalogVersion, CODE_WITH_JPG_EXTENSION);
		mediaModelMother.createLogoMediaModelWithCode(catalogVersion, CODE_WITH_MULTIPATH);
		mediaModelMother.createLogoMediaModelWithCode(catalogVersion, CODE_WITH_MULTIPATH_AND_EXTENSION);
		mediaModelMother.createLogoMediaModelWithCode(catalogVersion, CODE_WITH_MULTIPATH_AND_EXTENSION);
		mediaModelMother.createLogoMediaModelWithCode(catalogVersion, CODE_WITH_MULTIPATH_AND_EXTENSION_AND_WITH_SPACE);
	}

	@Test
	public void shouldGetMediaByCode() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, LOGO.getCode());
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaData> response = apiClient.request().endpoint(endpoint)
				.acceptJson().acceptLanguages(Locale.ENGLISH).get(MediaData.class);

		assertStatusCode(response, 200);

		final MediaData media = response.getBody();
		assertEquals(LOGO.getCode(), media.getCode());
		assertEquals(LOGO.getAltText(), media.getAltText());
		assertEquals(LOGO.getDescription(), media.getDescription());
		assertEquals(LOGO.getMimetype(), media.getMime());
		//		assertEquals(MediaModelMother.URL_LOGO, media.getUrl()); // TODO: URL is a dynamic attribute!
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shoulFailGetMediaByCode_InvalidCatalog() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, LOGO.getCode());
		variables.put(CATALOG_ID, INVALID);
		final String endpoint = replaceUriVariablesWithDefaultCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient
				.request().endpoint(endpoint).acceptJson().acceptLanguages(Locale.ENGLISH)
				.get(MediaData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 404);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shoulFailGetMediaByCode_InvalidVersion() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, LOGO.getCode());
		variables.put(VERSION_ID, INVALID);
		final String endpoint = replaceUriVariablesWithDefaultCatalog(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient
				.request().endpoint(endpoint).acceptJson().acceptLanguages(Locale.ENGLISH)
				.get(MediaData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 404);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shoulFailGetMediaByCode_InvalidMediaCode() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, INVALID);
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient
				.request().endpoint(endpoint).acceptJson().acceptLanguages(Locale.ENGLISH)
				.get(MediaData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 404);
	}


	@SuppressWarnings("unchecked")
	@Test
	public void shoulGetMediaByCode_MediaCodeWithJPGExtension() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, CODE_WITH_JPG_EXTENSION);
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaData> response = apiClient.request().endpoint(endpoint)
				.acceptJson().acceptLanguages(Locale.ENGLISH).get(MediaData.class);

		assertStatusCode(response, 200);

		final MediaData media = response.getBody();
		assertEquals(CODE_WITH_JPG_EXTENSION, media.getCode());
	}


	@SuppressWarnings("unchecked")
	@Test
	@Ignore("Rest template is removing the leading '/', making it impossible to test it")
	public void shoulGetMediaByCode_MediaCodeWithMultiPath() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, CODE_WITH_MULTIPATH);
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaData> response = apiClient.request().endpoint(endpoint)
				.acceptJson().acceptLanguages(Locale.ENGLISH).get(MediaData.class);

		assertStatusCode(response, 200);

		final MediaData media = response.getBody();
		assertEquals(CODE_WITH_MULTIPATH, media.getCode());
	}


	@SuppressWarnings("unchecked")
	@Test
	@Ignore("Rest template is removing the leading '/', making it impossible to test it")
	public void shoulGetMediaByCode_MediaCodeWithMultiPathAndJpgExtension() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, CODE_WITH_MULTIPATH_AND_EXTENSION);
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaData> response = apiClient.request().endpoint(endpoint)
				.acceptJson().acceptLanguages(Locale.ENGLISH).get(MediaData.class);

		assertStatusCode(response, 200);

		final MediaData media = response.getBody();
		assertEquals(CODE_WITH_MULTIPATH_AND_EXTENSION, media.getCode());
	}

	@Test
	public void shouldGetMediaByCodeWithSpace() throws Exception
	{
		final Map<String, String> variables = new HashMap<String, String>();
		variables.put(MEDIA_CODE, CODE_WITH_MULTIPATH_AND_EXTENSION_AND_WITH_SPACE);
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(GET_ONE_ENDPOINT, variables);

		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaData> response = apiClient.request().endpoint(endpoint)
				.acceptJson().acceptLanguages(Locale.ENGLISH).get(MediaData.class);

		assertStatusCode(response, 200);

		final MediaData media = response.getBody();
		assertEquals(CODE_WITH_MULTIPATH_AND_EXTENSION_AND_WITH_SPACE, media.getCode());
	}

}
