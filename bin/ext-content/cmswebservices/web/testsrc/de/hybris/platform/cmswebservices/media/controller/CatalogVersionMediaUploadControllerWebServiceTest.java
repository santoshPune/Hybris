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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.MediaData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationObjectError;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.MediaModelMother;

import java.util.Locale;

import javax.annotation.Resource;

import org.apache.logging.log4j.util.Strings;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;



@IntegrationTest
public class CatalogVersionMediaUploadControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String POST_ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/media";

	private static final String CONTENT_TYPE = "Content-Type";
	private static final String MULTIPART_HEADER = "multipart/form-data";
	private static final String ALT_TEXT = "altText";
	private static final String CODE = "code";
	private static final String DESCRIPTION = "description";
	private static final String FILE = "file";

	private static final String CODE_WITH_JPG_EXTENSION = "some-Media_Code.jpg";
	private static final String MEDIA_CODE = "abc123";
	private static final String MEDIA_DESCRIPTION = "Some lengthy description.";
	private static final String MEDIA_ALT_TEXT = "Alternative text.";
	private static final String MEDIA_PATH = "cmswebservices/test/images/SAP-hybris-logo.png";

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
	}

	@SuppressWarnings("resource")
	@Test
	public void shouldUploadImageForMedia() throws Exception
	{
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(POST_ENDPOINT, Maps.newHashMap());

		final FileDataBodyPart filePart = new FileDataBodyPart(FILE, new ClassPathResource(MEDIA_PATH).getFile());
		final MultiPart multipart = new FormDataMultiPart() //
				.field(CODE, MEDIA_CODE) //
				.field(ALT_TEXT, MEDIA_ALT_TEXT) //
				.field(DESCRIPTION, MEDIA_DESCRIPTION) //
				.bodyPart(filePart);

		final ApiClient apiClient = getApiClientInstance();
		final Response<MediaData> response = apiClient.request() //
				.endpoint(endpoint) //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.header(CONTENT_TYPE, MULTIPART_HEADER) //
				.post(multipart, MediaData.class);

		assertStatusCode(response, HttpStatus.CREATED.value());
		assertThat(response.getHeaders().get("Location"), Matchers.containsString(endpoint + "/" + MEDIA_CODE));
	}

	@SuppressWarnings("resource")
	@Test
	public void shouldFailUploadImageNoCode() throws Exception
	{
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(POST_ENDPOINT, Maps.newHashMap());

		final FileDataBodyPart filePart = new FileDataBodyPart(FILE, new ClassPathResource(MEDIA_PATH).getFile());
		final MultiPart multipart = new FormDataMultiPart() //
				.field(CODE, Strings.EMPTY) //
				.field(ALT_TEXT, MEDIA_ALT_TEXT) //
				.field(DESCRIPTION, MEDIA_DESCRIPTION) //
				.bodyPart(filePart);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request() //
				.endpoint(endpoint) //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.header(CONTENT_TYPE, MULTIPART_HEADER) //
				.post(multipart, ValidationErrorResponse.class);

		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
		assertEquals(1, response.getErrorResponse().getErrors().size());
		final ValidationObjectError e = response.getErrorResponse().getErrors().get(0);
		assertEquals(CODE, e.getSubject());
		assertTrue(e.getMessage().contains("required"));
	}

	@SuppressWarnings("resource")
	@Test
	public void shouldFailUploadImageCodeNotUnique() throws Exception
	{
		final String endpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(POST_ENDPOINT, Maps.newHashMap());

		final FileDataBodyPart filePart = new FileDataBodyPart(FILE, new ClassPathResource(MEDIA_PATH).getFile());
		final MultiPart multipart = new FormDataMultiPart() //
				.field(CODE, CODE_WITH_JPG_EXTENSION) //
				.field(ALT_TEXT, MEDIA_ALT_TEXT) //
				.field(DESCRIPTION, MEDIA_DESCRIPTION) //
				.bodyPart(filePart);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request() //
				.endpoint(endpoint) //
				.acceptJson().acceptLanguages(Locale.ENGLISH) //
				.header(CONTENT_TYPE, MULTIPART_HEADER) //
				.post(multipart, ValidationErrorResponse.class);

		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
		assertEquals(1, response.getErrorResponse().getErrors().size());
		final ValidationObjectError e = response.getErrorResponse().getErrors().get(0);
		assertEquals(CODE, e.getSubject());
		assertTrue(e.getMessage().contains("already in use"));
	}

}
