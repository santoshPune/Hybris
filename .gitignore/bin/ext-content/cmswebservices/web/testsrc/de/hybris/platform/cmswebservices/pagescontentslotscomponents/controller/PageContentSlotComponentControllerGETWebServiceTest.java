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
package de.hybris.platform.cmswebservices.pagescontentslotscomponents.controller;

import static de.hybris.platform.cmswebservices.constants.CmswebservicesConstants.URI_PAGE_ID;
import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.ELECTRONICS;
import static de.hybris.platform.cmswebservices.util.models.ContentPageModelMother.UID_HOMEPAGE;
import static de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother.UID_HEADER;
import static de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother.UID_LOGO;
import static de.hybris.platform.cmswebservices.util.models.SimpleBannerComponentModelMother.UID_HEADER_LOGO;
import static org.apache.commons.beanutils.BeanUtils.copyProperties;
import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;
import static org.fest.assertions.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentData;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.ParagraphComponentModelMother;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Test;


public class PageContentSlotComponentControllerGETWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String PAGE_CONTENT_SLOT_COMPONENTS_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagescontentslotscomponents";
	private static final String INVALID_PAGE_ID = "INVALID_PAGE_ID";

	@Resource
	private CMSSiteModelMother cmsSiteModelMother;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	private CatalogVersionModel catalogVersion;

	@Test
	public void shouldGetComponentsByPage() throws Exception
	{
		setupTestData();

		final ApiClient apiClient = getApiClientInstance();

		final Response<PageContentSlotComponentListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_COMPONENTS_ENDPOINT,
						new HashMap<>()))
				.parameter(URI_PAGE_ID, UID_HOMEPAGE)
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(PageContentSlotComponentListData.class);

		final ComparablePageContentSlotComponentListData actual = new ComparablePageContentSlotComponentListData(response.getBody());
		final ComparablePageContentSlotComponentListData expected = new ComparablePageContentSlotComponentListData(buildExpectedPageContentSlotComponentListData());

		assertThat(expected).isEqualTo(actual);
	}

	@Test
	public void shouldReturnEmptyObject_OnException() throws Exception
	{
		createSiteCatalogVersion();
		final ApiClient apiClient = getApiClientInstance();

		final Response<PageContentSlotComponentListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_CONTENT_SLOT_COMPONENTS_ENDPOINT,
						new HashMap<>()))
				.parameter(URI_PAGE_ID, INVALID_PAGE_ID)
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(PageContentSlotComponentListData.class);

		assertThat(response.getBody().getPageContentSlotComponentList(), empty());
	}

	protected void createSiteCatalogVersion()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);
	}

	protected void setupTestData() {
		createSiteCatalogVersion();

		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		contentSlotForPageModelMother.HeaderHomepage_ParagraphAndBanner(catalogVersion);
		contentSlotForPageModelMother.LogoHomepage(catalogVersion);

		contentSlotNameModelMother.Header(pageTemplate);
		contentSlotNameModelMother.Logo(pageTemplate);
	}

	protected PageContentSlotComponentListData buildExpectedPageContentSlotComponentListData()
	{
		final PageContentSlotComponentListData data = new PageContentSlotComponentListData();

		final PageContentSlotComponentData paragraphHeaderData = new PageContentSlotComponentData();
		paragraphHeaderData.setPageId(UID_HOMEPAGE);
		paragraphHeaderData.setSlotId(UID_HEADER);
		paragraphHeaderData.setComponentId(ParagraphComponentModelMother.UID_HEADER);
		paragraphHeaderData.setPosition(0);

		final PageContentSlotComponentData bannerHeaderData = new PageContentSlotComponentData();
		bannerHeaderData.setPageId(UID_HOMEPAGE);
		bannerHeaderData.setSlotId(UID_HEADER);
		bannerHeaderData.setComponentId(UID_HEADER_LOGO);
		bannerHeaderData.setPosition(1);

		final PageContentSlotComponentData bannerLogoData = new PageContentSlotComponentData();
		bannerLogoData.setPageId(UID_HOMEPAGE);
		bannerLogoData.setSlotId(UID_LOGO);
		bannerLogoData.setComponentId(UID_HEADER_LOGO);
		bannerLogoData.setPosition(0);

		final List<PageContentSlotComponentData> pageContentSlotComponentList = new ArrayList<>();
		pageContentSlotComponentList.add(paragraphHeaderData);
		pageContentSlotComponentList.add(bannerHeaderData);
		pageContentSlotComponentList.add(bannerLogoData);
		data.setPageContentSlotComponentList(pageContentSlotComponentList);

		return data;
	}

	protected class ComparablePageContentSlotComponentListData extends PageContentSlotComponentListData {
		public ComparablePageContentSlotComponentListData(final PageContentSlotComponentListData pageContentSlotComponentListData)
				throws InvocationTargetException, IllegalAccessException
		{
			copyProperties(this, pageContentSlotComponentListData);
		}

		@Override
		public int hashCode()
		{
			return reflectionHashCode(this);
		}

		@Override
		public boolean equals(final Object that)
		{
			return reflectionEquals(this.getPageContentSlotComponentList(), ((ComparablePageContentSlotComponentListData) that)
					.getPageContentSlotComponentList());
		}
	}
}
