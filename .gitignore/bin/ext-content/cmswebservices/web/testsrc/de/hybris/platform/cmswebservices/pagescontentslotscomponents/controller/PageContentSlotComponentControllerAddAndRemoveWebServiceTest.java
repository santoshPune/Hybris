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
import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.data.PageContentSlotComponentData;
import de.hybris.platform.cmswebservices.pagescontentslotscomponents.facade.validator.ComponentExistsInSlotValidator;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.*;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


@IntegrationTest
public class PageContentSlotComponentControllerAddAndRemoveWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String INVALID = "INVALID";
	private static final String CONTENTSLOT_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pagescontentslotscomponents";
	private static final String SLOT_ID = "slotId";
	private static final String COMPONENT_ID = "componentId";
	private static final String POSITION = "position";

	@Resource
	private CMSSiteModelMother cmsSiteModelMother;
	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;
	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;
	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;
	@Resource
	private SimpleBannerComponentModelMother simpleBannerComponentModelMother;
	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;

	private CatalogVersionModel catalogVersion;

	@Test
	public void shouldAddComponentToSlot_FirstPosition() throws Exception
	{
		createAppleCatalogWithHeaderParagraphOnly();

		final ApiClient apiClient = getApiClientInstance();

		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap()))
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.post(buildBannerContentSlotComponentItemDto(0, ContentSlotModelMother.UID_HEADER));

		assertStatusCode(response, HttpStatus.CREATED.value());
	}

	@Test
	public void shouldAddComponentToSlot_LastPosition() throws Exception
	{
		createAppleCatalogWithHeaderParagraphOnly();
		final ApiClient apiClient = getApiClientInstance();
		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap()))
				.acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.post(buildBannerContentSlotComponentItemDto(5, ContentSlotModelMother.UID_HEADER));

		assertStatusCode(response, HttpStatus.CREATED.value());
	}

	@Test
	public void shouldAddComponentToSlot_FailedTypeRestriction() throws Exception
	{
		createAppleCatalogWithLogoWithRestrictions();

		final ApiClient apiClient = getApiClientInstance();

		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap())) //
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.post(buildParagraphContentSlotComponentItemDto(0, ContentSlotModelMother.UID_LOGO), ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());

		assertStatusCode(response, 400);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddComponentToSlot_AlreadyPresent() throws Exception
	{
		createAppleCatalogWithHeaderParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap()))
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.post(buildParagraphContentSlotComponentItemDto(0, ContentSlotModelMother.UID_HEADER), ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());

		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddComponentToSlot_SlotNotFound() throws Exception
	{
		createAppleCatalogWithHeaderParagraphOnly();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap())) //
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.post(buildBannerContentSlotComponentItemDto(0, ContentSlotModelMother.UID_FOOTER), ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());

		assertStatusCode(response, 404);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddComponentToSlot_PageNotFound() throws Exception
	{
		createAppleCatalogWithHeaderParagraphOnly();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap())) //
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.post(buildInvalidPageBannerComponentItemDto(0, ContentSlotModelMother.UID_HEADER), ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());

		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddComponentToSlot_ComponentNotFound() throws Exception
	{
		createAppleCatalogWithHeaderParagraphNoRestriction();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap()))
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.post(buildInvalidContentSlotComponentItemDto(0, ContentSlotModelMother.UID_HEADER), ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(ComponentExistsInSlotValidator.COMPONENT_ID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldAddComponentToSlot_InvalidIndex() throws Exception
	{
		createAppleCatalogWithHeaderParagraphOnly();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap()))
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.post(buildBannerContentSlotComponentItemDto(-3, ContentSlotModelMother.UID_HEADER), ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(POSITION, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	public void shouldRemoveComponentFromSlot() throws Exception
	{
		createAppleCatalogWithHeaderParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap())) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, ParagraphComponentModelMother.UID_HEADER) //
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.delete();

		assertStatusCode(response, HttpStatus.NO_CONTENT.value());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotRemoveComponentFromSlot_ComponentNotFound() throws Exception
	{
		createAppleCatalogWithHeaderParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap())) //
				.parameter(SLOT_ID, ContentSlotModelMother.UID_HEADER) //
				.parameter(COMPONENT_ID, INVALID) //
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.delete();
		assertStatusCode(response, HttpStatus.NOT_FOUND.value());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void shouldNotRemoveComponentFromSlot_SlotNotFound() throws Exception
	{
		createAppleCatalogWithHeaderParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CONTENTSLOT_ENDPOINT, Maps.newHashMap())) //
				.parameter(SLOT_ID, INVALID) //
				.parameter(COMPONENT_ID, COMPONENT_ID) //
				.acceptJson() //
				.acceptLanguages(Locale.ENGLISH) //
				.delete();

		assertStatusCode(response, HttpStatus.NOT_FOUND.value());
	}

	protected void createAppleCatalogWithHeaderParagraphAndBanner()
	{
		// Create site & catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header with paragraph and banner component
		contentSlotForPageModelMother.HeaderHomepage_ParagraphAndBanner(catalogVersion);
		// Create header content slot name with paragraph + banner restrictions
		contentSlotNameModelMother.Header(pageTemplate);
	}

	protected void createAppleCatalogWithLogoWithRestrictions()
	{
		// Create site & catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot logo
		contentSlotForPageModelMother.LogoHomepage(catalogVersion);
		// Create logo content slot name with banner only restrictions
		contentSlotNameModelMother.Logo(pageTemplate);
		// Create paragraph not in slot
		paragraphComponentModelMother.createHeaderParagraphComponentModel(catalogVersion);
	}

	protected void createAppleCatalogWithHeaderParagraphOnly()
	{
		// Create site & catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header with paragraph component
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		// Create header content slot name with paragraph + banner restrictions
		contentSlotNameModelMother.Header(pageTemplate);

		// Create banner not in slot
		simpleBannerComponentModelMother.createHeaderLogoBannerComponentModel(catalogVersion);
	}


	protected void createAppleCatalogWithHeaderParagraphNoRestriction()
	{
		// Create site & catalog & catalog version
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS, catalogVersion);
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header with paragraph component
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		// Create header content slot name with paragraph + banner restrictions
		contentSlotNameModelMother.Header_without_restriction(pageTemplate);

		// Create banner not in slot
		simpleBannerComponentModelMother.createHeaderLogoBannerComponentModel(catalogVersion);
	}

	protected PageContentSlotComponentData buildParagraphContentSlotComponentItemDto(final int index, final String slotId)
	{
		final PageContentSlotComponentData dto = new PageContentSlotComponentData();
		dto.setComponentId(ParagraphComponentModelMother.UID_HEADER);
		dto.setPosition(index);
		dto.setSlotId(slotId);
		dto.setPageId(ContentPageModelMother.UID_HOMEPAGE);
		return dto;
	}

	protected PageContentSlotComponentData buildBannerContentSlotComponentItemDto(final int index, final String slotId)
	{
		final PageContentSlotComponentData dto = new PageContentSlotComponentData();
		dto.setComponentId(SimpleBannerComponentModelMother.UID_HEADER_LOGO);
		dto.setPosition(index);
		dto.setSlotId(slotId);
		dto.setPageId(ContentPageModelMother.UID_HOMEPAGE);
		return dto;
	}

	protected PageContentSlotComponentData buildInvalidContentSlotComponentItemDto(final int index, final String slotId)
	{
		final PageContentSlotComponentData dto = new PageContentSlotComponentData();
		dto.setComponentId(INVALID);
		dto.setPosition(index);
		dto.setSlotId(slotId);
		dto.setPageId(ContentPageModelMother.UID_HOMEPAGE);
		return dto;
	}

	protected PageContentSlotComponentData buildInvalidPageBannerComponentItemDto(final int index, final String slotId)
	{
		final PageContentSlotComponentData dto = new PageContentSlotComponentData();
		dto.setComponentId(SimpleBannerComponentModelMother.UID_HEADER_LOGO);
		dto.setPosition(index);
		dto.setSlotId(slotId);
		dto.setPageId(INVALID);
		return dto;
	}
}
