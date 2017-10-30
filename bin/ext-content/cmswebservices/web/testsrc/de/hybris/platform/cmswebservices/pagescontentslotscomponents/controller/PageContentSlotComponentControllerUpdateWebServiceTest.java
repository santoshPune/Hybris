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

import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.ELECTRONICS;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentData;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentListData;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationObjectError;
import de.hybris.platform.cmswebservices.util.models.*;


import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


@IntegrationTest
public class PageContentSlotComponentControllerUpdateWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String INVALID_SLOT_ID = "INVALID_SLOT_ID";
	private static final String UPDATE_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagescontentslotscomponents";
	private static final String SLOT_ID = "slotId";
	private static final String COMPONENT_ID = "componentId";
	private static final String PAGE_ID = "pageId";
	private static final String COMPONENT_ALREADY_EXIST_SLOT = "You cannot add more than one instance of a component to a content slot.";

	@Resource
	private CMSSiteModelMother cmsSiteModelMother;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;
	@Resource
	private SimpleBannerComponentModelMother simpleBannerComponentModelMother;
	@Resource
	private ContentPageModelMother contentPageModelMother;
	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	private CatalogVersionModel catalogVersion;

	@Test
	public void shouldMoveComponentFromHeaderSlotToFootSlot() throws Exception
	{
		createParagraphWithinHeaderSlot_electronicsSite_appleCatalog_emptyFootSlot();

		final PageContentSlotComponentData entity = new PageContentSlotComponentData();
		entity.setSlotId(ContentSlotModelMother.UID_FOOTER);
		entity.setComponentId(ParagraphComponentModelMother.UID_HEADER);
		entity.setPosition(0);
		entity.setPageId(ContentPageModelMother.UID_HOMEPAGE);

		final Response<Void> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity);
		assertStatusCode(response, HttpStatus.OK.value());
	}

	@Test
	public void shouldMoveComponentToPositionInHeader() throws Exception
	{
		createParagraphAndBannerWithinHeaderSlot_electronicsSite_appleCatalog();

		final Response<PageContentSlotComponentListData> initialResponse = getApiClientInstance()
				.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap()))
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(PageContentSlotComponentListData.class);
		
		int initialNumberOfComponents = initialResponse.getBody().getPageContentSlotComponentList().size();
		assertThat(initialNumberOfComponents, greaterThanOrEqualTo(2));
		
		//get the first item (the one at position zero)
		PageContentSlotComponentData entity = initialResponse.getBody().getPageContentSlotComponentList().get(0);
		entity.setPosition(1);
		
		final Response<Void> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID,  ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, entity.getComponentId()) //
				.put(entity);
		
		final Response<PageContentSlotComponentListData> finalResponse = getApiClientInstance()
				.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap()))
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(PageContentSlotComponentListData.class);
		
		int finalNumberOfComponents = finalResponse.getBody().getPageContentSlotComponentList().size();

		assertThat(finalNumberOfComponents, is(equalTo(initialNumberOfComponents)));
		
		assertThat(finalResponse.getBody().getPageContentSlotComponentList().get(1).getComponentId(), is(equalTo(entity.getComponentId())));
	}
	
	@Test
	public void shouldNotMoveComponentInvalidSlot() throws Exception
	{
		createParagraphWithinHeaderSlot_electronicsSite_appleCatalog_emptyFootSlot();

		final PageContentSlotComponentData entity = new PageContentSlotComponentData();
		//invalid uid
		entity.setSlotId(INVALID_SLOT_ID);

		entity.setComponentId(ParagraphComponentModelMother.UID_FOOTER);
		entity.setPosition(0);
		entity.setPageId(ContentPageModelMother.UID_HOMEPAGE);

		final Response<Void> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity);
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void shouldNotMoveComponentValidationErrors() throws Exception
	{
		createParagraphWithinHeaderSlot_electronicsSite_appleCatalog_emptyFootSlot();

		final PageContentSlotComponentData entity = new PageContentSlotComponentData();

		final Response<ValidationErrorResponse> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity, ValidationErrorResponse.class);

		final List<ValidationObjectError> errors = response.getErrorResponse().getErrors();
		assertEquals(4, errors.size());
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void shouldNotMoveComponentIntoASlotThatAlreadyHasAnInstanceValidationErrors() throws Exception
	{
		createParagraphWithinHeaderSlot_electronicsSite_appleCatalog_emptyFootSlot();
		
		final PageContentSlotComponentData entity = new PageContentSlotComponentData();
		entity.setComponentId(ParagraphComponentModelMother.UID_HEADER);
		entity.setPosition(0);
		entity.setSlotId(ContentSlotModelMother.UID_HEADER);
		entity.setPageId(ContentPageModelMother.UID_HOMEPAGE);

		final Response<ValidationErrorResponse> response = getApiClientInstance() //
				.request() //
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ENDPOINT, Maps.newHashMap())) //
				.parameter(PAGE_ID, ContentPageModelMother.UID_HOMEPAGE) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_FOOTER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.put(entity, ValidationErrorResponse.class);

		final List<ValidationObjectError> errors = response.getErrorResponse().getErrors();
		assertEquals(1, errors.size());
		assertEquals(errors.get(0).getMessage(), COMPONENT_ALREADY_EXIST_SLOT);
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	protected void createParagraphWithinHeaderSlot_electronicsSite_appleCatalog_emptyFootSlot()
	{
		// Create catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);

		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);

		//create home page
		contentPageModelMother.HomePage(catalogVersion);

		// Create homepage page and content slot header with paragraph component
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		contentSlotForPageModelMother.FooterHomepage_Empty(catalogVersion);

		// Create header content slot name with paragraph + banner restrictions
		contentSlotNameModelMother.Header(pageTemplate);

		// Create footer slot
		contentSlotNameModelMother.Footer(pageTemplate);
	}
	//
	
	protected void createParagraphAndBannerWithinHeaderSlot_electronicsSite_appleCatalog()
	{
		// Create catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);

		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);

		//create home page
		contentPageModelMother.HomePage(catalogVersion);

		// Create homepage page and content slot header with paragraph and Banner components
		contentSlotForPageModelMother.HeaderHomepage_ParagraphAndBanner(catalogVersion);

		// Create header content slot name with paragraph + banner restrictions
		contentSlotNameModelMother.Header(pageTemplate);

	}
}
