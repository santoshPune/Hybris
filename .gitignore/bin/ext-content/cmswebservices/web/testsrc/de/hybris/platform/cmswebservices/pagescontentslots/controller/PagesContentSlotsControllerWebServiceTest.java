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
package de.hybris.platform.cmswebservices.pagescontentslots.controller;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.PageContentSlotListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother;

import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;


@IntegrationTest
public class PagesContentSlotsControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String PAGE_CONTENT_SLOT_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagescontentslots";

	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private ContentSlotForTemplateModelMother contentSlotForTemplateModelMother;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private CMSSiteModelMother cmsSiteModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;
	@Resource
	private ContentPageModelMother contentPageModelMother;

	private static final String INVALID_ID = "invalid-id";

	private CatalogVersionModel catalogVersion;
	private ApiClient apiClient;

	@Before
	public void setup()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(TemplateSite.ELECTRONICS, catalogVersion);

		apiClient = getApiClientInstance();
	}

	@Test
	public void shouldGetPagesForFooterSlotFromPageRelations() throws Exception
	{
		// footer slot is present in 2 pages
		contentSlotForPageModelMother.FooterHomepage_Empty(catalogVersion);
		contentSlotForPageModelMother.FooterEmailPage_Empty(catalogVersion);

		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_SLOT_ID, ContentSlotModelMother.UID_FOOTER).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList().size(), is(2));
	}

	@Test
	public void shouldGetSinglePageForFooterSlot() throws Exception
	{
		// footer slot is present in the page template and a single page
		contentSlotForPageModelMother.FooterHomepage_Empty(catalogVersion);
		contentSlotForTemplateModelMother.FooterHomepage(catalogVersion);

		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_SLOT_ID, ContentSlotModelMother.UID_FOOTER).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList().size(), is(1));
	}

	@Test
	public void shouldGetEmptyForHeaderSlot() throws Exception
	{
		contentSlotModelMother.createHeaderSlotWithParagraph(catalogVersion);

		// No CMS relation was created for the header content slot
		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_SLOT_ID, ContentSlotModelMother.UID_HEADER).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList(), empty());
	}

	@Test
	public void shouldGetEmptyForInvalidSlot() throws Exception
	{
		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_SLOT_ID, INVALID_ID).acceptJson().acceptLanguages(Locale.ENGLISH)
				.get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList(), empty());
	}

	@Test
	public void shouldGetPagesForFooterSlotInTemplates() throws Exception
	{
		// footer slot is present in 3 templates
		contentPageModelMother.HomePage(catalogVersion);
		contentPageModelMother.EmailPageFromEmailPageTemplate(catalogVersion);
		contentSlotForTemplateModelMother.FooterHomepage(catalogVersion);
		contentSlotForTemplateModelMother.FooterEmailPage(catalogVersion);
		contentSlotForTemplateModelMother.FooterSearchPage(catalogVersion);

		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_SLOT_ID, ContentSlotModelMother.UID_FOOTER).acceptJson()
				.get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList().size(), is(3));
	}

	@Test
	public void shouldGetContentSlotsInPage() throws Exception
	{
		contentSlotForPageModelMother.LogoHomepage(catalogVersion);
		contentSlotForPageModelMother.FooterHomepage_Empty(catalogVersion);
		contentSlotForPageModelMother.HeaderHomepage_FlashComponentOnly(catalogVersion);

		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_PAGE_ID, ContentPageModelMother.UID_HOMEPAGE).acceptJson()
				.get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList().size(), is(3));
	}

	@Test
	public void shouldGetEmptyForInvalidPage() throws Exception
	{
		final Response<PageContentSlotListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_ENDPOINT, new HashMap<>()))
				.parameter(CmswebservicesConstants.URI_PAGE_ID, INVALID_ID).acceptJson().get(PageContentSlotListData.class);

		assertStatusCode(response, HttpStatus.OK.value());
		assertThat(response.getBody().getPageContentSlotList(), empty());
	}
}
