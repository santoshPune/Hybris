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
package de.hybris.platform.cmswebservices.catalogversiondetails.controller;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.OK;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.cmswebservices.catalogversiondetails.facade.populator.model.ComparableCatalogVersionDetailData;
import de.hybris.platform.cmswebservices.data.CatalogVersionData;
import de.hybris.platform.cmswebservices.data.CatalogVersionDetailData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother;
import de.hybris.platform.cmswebservices.util.models.SiteModelMother;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CatalogVersionDetailsControllerIntegrationTest extends ApiBaseIntegrationTest {

	private static final String ENDPOINT = "/v1/sites/{siteId}/catalogversiondetails";

	private ApiClient apiClient;

	@Resource
	private CMSSiteModelMother cmsSiteModelMother;
	@Resource
	private SiteModelMother siteModelMother;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentCatalogModelMother catalogModelMother;

	@Before
	public void setup()
	{
		apiClient = getApiClientInstance();
	}

	@Test
	public void postOnCatalogVersionDetailsWillCauseMETHOD_NOT_ALLOWED() throws Exception {
		final Response<Void> post = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSite(ENDPOINT, new HashMap<>()))
				.acceptLanguages(Locale.ENGLISH)
				.acceptJson()
				.post(new CatalogVersionDetailData());
		assertStatusCode(post, METHOD_NOT_ALLOWED.value());
	}

	@Test
	public void putOnCatalogVersionDetailsWillCauseMETHOD_NOT_ALLOWED() throws Exception {
		final Response<Void> post = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSite(ENDPOINT, new HashMap<>()))
				.acceptLanguages(Locale.ENGLISH)
				.acceptJson()
				.put(new CatalogVersionDetailData());
		assertStatusCode(post, METHOD_NOT_ALLOWED.value());
	}

	@Test
	public void getOnCatalogVersionDetailsWillReturnAnEmptyList() throws Exception {
		// create site with empty catalog
		siteModelMother.createElectronicsWithAppleCatalog();

		final Response<CatalogVersionData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSite(ENDPOINT, new HashMap<>()))
				.acceptLanguages(Locale.ENGLISH)
				.acceptJson()
				.get(CatalogVersionData.class);

		assertStatusCode(response, OK.value());
		assertTrue(CollectionUtils.isEmpty(response.getBody().getCatalogVersionDetails()));
	}

	@Test
	public void getOnCatalogVersionDetailsWillReturnAListOfOnlineAndStaged() throws Exception {
		// create site with staged and online catalog
		siteModelMother.createElectronicsWithAppleStagedAndOnlineCatalog();

		final Response<CatalogVersionData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSite(ENDPOINT, new HashMap<>()))
				.acceptLanguages(Locale.ENGLISH)
				.acceptJson()
				.get(CatalogVersionData.class);

		assertStatusCode(response, OK.value());
		assertThat(response.getBody().getCatalogVersionDetails(), hasSize(2));

		final Collection<CatalogVersionDetailData> catalogVersions = makeCatalogVersionDetailDataComparable(
				response.getBody().getCatalogVersionDetails());
		final ComparableCatalogVersionDetailData expectedCatalogDetailData = new ComparableCatalogVersionDetailData();
		expectedCatalogDetailData.setCatalogId(ContentCatalogModelMother.CatalogTemplate.ID_APPLE.name());
		expectedCatalogDetailData.setRedirectUrl(SiteModelMother.REDIRECT_URL);
		expectedCatalogDetailData.setVersion(CatalogVersionModelMother.CatalogVersion.STAGED.getVersion());
		expectedCatalogDetailData.setName(ContentCatalogModelMother.CatalogTemplate.ID_APPLE.getHumanName());
		assertThat(catalogVersions, hasItems(expectedCatalogDetailData));
	}

	private static Collection<CatalogVersionDetailData> makeCatalogVersionDetailDataComparable(final Collection<CatalogVersionDetailData> catalogData) throws Exception {
		return catalogData.stream().map(ComparableCatalogVersionDetailData::new).collect(toList());
	}

}
