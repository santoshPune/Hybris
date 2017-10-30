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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.data.MediaListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.MediaModelMother;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.fest.util.Collections;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class MediaControllerWebServiceTest extends ApiBaseIntegrationTest
{

	//private static final String GET_ALL_ENDPOINT = "/v1/media?namedQuery=%s&params=%s&pageSize=%s&currentPage=%s&sort=%s";
	private static final String GET_ALL_ENDPOINT = "/v1/media";

	private static final String CODE_PHONE_1 = "some.awesome.phone.from.Apple";
	private static final String CODE_PHONE_2 = "another-PHONE-from-Apple";
	private static final String CODE_PHONE_3 = "standard iPhone";
	private static final String CODE_PHONE_4 = "phone... by Apple";
	private static final String CODE_CAMERA_1 = "that-red-camera";
	private static final String CODE_CAMERA_2 = "that.phony/fake camera";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private MediaModelMother mediaModelMother;
	@Resource
	private ModelService modelService;

	private CatalogVersionModel catalogVersion;

	@Before
	public void setupFixtures()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		mediaModelMother.createLogoMediaModel(catalogVersion);
	}

	@Test
	public void shouldGetMediaByQuery() throws Exception
	{
		createMediaModels();

//		"", //
//						"", //
//						"25", //
//						"0", //
//						"code:ASC")) //
		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaListData> response = apiClient.request() //
				.endpoint(GET_ALL_ENDPOINT) //
				.parameter("namedQuery", "namedQueryMediaSearchByCodeCatalogVersion") //
				.parameter("params", "code:phone,catalogId:ID_APPLE,catalogVersion:staged") //
				.parameter("pageSize", "25") //
				.parameter("currentPage", "0") //
				.parameter("sort", "code:ASC")   //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.get(MediaListData.class);

		assertStatusCode(response, 200);

		final List<MediaData> media = response.getBody().getMedia();
		assertEquals(4, media.size());
	}

	@Test
	public void shouldFailGetMediaByQuery_ValidationErrors() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient
				.request()
				.endpoint(GET_ALL_ENDPOINT) //
				.parameter("namedQuery", "") //
				.parameter("params", "code:banner") //
				.parameter("pageSize", "invalid") //
				.parameter("currentPage", "0") //
				.parameter("sort", "code:banner")   //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.get(MediaListData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertStatusCode(response, 400);

		// TODO: Assert the validation errors after merging with Flavio's branch.
	}

	@Test
	public void shouldGetMediaByQueryWithEmptyResults_CodeNoMatch() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaListData> response = apiClient.request() //
				.endpoint(GET_ALL_ENDPOINT) //
				.parameter("namedQuery", "namedQueryMediaSearchByCodeCatalogVersion") //
				.parameter("params", "code:invalid,catalogId:ID_APPLE,catalogVersion:staged") //
				.parameter("pageSize", "25") //
				.parameter("currentPage", "0") //
				.parameter("sort", "code:ASC")   //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.get(MediaListData.class);

		assertStatusCode(response, 200);
		assertTrue(Collections.isEmpty(response.getBody().getMedia()));
	}

	@Test
	public void shouldGetMediaByQueryWithEmptyResults_CatalogVersionNoMatch() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaListData> response = apiClient.request() //
				.endpoint(GET_ALL_ENDPOINT) //
				.parameter("namedQuery", "namedQueryMediaSearchByCodeCatalogVersion") //
				.parameter("params", "code:" + LOGO.getCode() + ",catalogId:apple,catalogVersion:invalid") //
				.parameter("pageSize", "25") //
				.parameter("currentPage", "0") //
				.parameter("sort", "code:ASC")   //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.get(MediaListData.class);

		assertStatusCode(response, 200);
		assertTrue(Collections.isEmpty(response.getBody().getMedia()));
	}

	protected void createMediaModels()
	{
		createMediaModel(CODE_PHONE_1);
		createMediaModel(CODE_PHONE_2);
		createMediaModel(CODE_PHONE_3);
		createMediaModel(CODE_PHONE_4);
		createMediaModel(CODE_CAMERA_1);
		createMediaModel(CODE_CAMERA_2);
	}

	protected void createMediaModel(final String code)
	{
		final MediaModel media = modelService.create(MediaModel.class);
		media.setCode(code);
		media.setCatalogVersion(catalogVersion);
		modelService.save(media);
	}

}
