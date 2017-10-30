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
package de.hybris.platform.cmswebservices.pagetemplates.contoller;

import com.google.common.collect.Maps;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cmswebservices.data.PageTemplateListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.BaseStoreModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.SiteModelMother;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Locale;

import static de.hybris.platform.cmswebservices.pagetemplates.contoller.PageTemplateControllerWebServiceTest.PageTemplateField.FRONTEND_NAME;
import static de.hybris.platform.cmswebservices.pagetemplates.contoller.PageTemplateControllerWebServiceTest.PageTemplateField.NAME;
import static de.hybris.platform.cmswebservices.pagetemplates.contoller.PageTemplateControllerWebServiceTest.PageTemplateField.UID;
import static de.hybris.platform.cmswebservices.util.models.CMSPageTypeModelMother.CODE_CONTENT_PAGE;
import static de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother.TEMPLATE_NAME_HOME_PAGE;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.OK;


@IntegrationTest
public class PageTemplateControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String PAGE_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagetemplates";
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;
	@Resource
	private SiteModelMother siteModelMother;
	@Resource
	private BaseStoreModelMother baseStoreModelMother;
	private CatalogVersionModel catalogVersion;

	@Before
	public void setup()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		siteModelMother.createElectronicsWithAppleCatalog();
		pageTemplateModelMother.HomePage_Template(catalogVersion);
		//still needed ?
		baseStoreModelMother.createNorthAmerica(catalogVersion);
	}

	@Test
	public void willRetrieveAllActivetemplatesForTheGivenPageType() throws Exception {
		final Response<PageTemplateListData> response = getApiClientInstance().request().parameter("pageTypeCode", CODE_CONTENT_PAGE)
				.endpoint(replaceUriVariablesWithElectronicsSiteAndDefaultCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(PageTemplateListData.class);
		assertStatusCode(response, OK.value());

		final PageTemplateListData body = response.getBody();
		assertThat(body.getTemplates().size(), is(1));


		assertThat(
				body.getTemplates().get(0),
				allOf(hasProperty(UID.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(FRONTEND_NAME.property, is(TEMPLATE_NAME_HOME_PAGE))//, hasProperty(PREVIEW_ICON.property, is(LOGO.getUrl())) dynamic attribute in ORM so getetr returns null
				));
	}

	@Test
	public void willRetrieveEmptyCollectionIfNoMatchingTemplateForGivenPageType() throws Exception {
		final Response<PageTemplateListData> response = getApiClientInstance().request().parameter("pageTypeCode", "someunknownvalue")
				.endpoint(replaceUriVariablesWithElectronicsSiteAndDefaultCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(PageTemplateListData.class);
		assertStatusCode(response, OK.value());

		final PageTemplateListData body = response.getBody();
		assertThat(body.getTemplates().size(), is(0));

	}

	enum PageTemplateField
	{
		UID("uid"), NAME("name"), PREVIEW_ICON("previewIcon"), FRONTEND_NAME("frontEndName");
		String property;

		private PageTemplateField(final String property)
		{
			this.property = property;
		}
	}
}
