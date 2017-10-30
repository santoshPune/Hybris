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
package de.hybris.platform.cmswebservices.items.controller;

import static de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother.TemplateSite.ELECTRONICS;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.CMSParagraphComponentModel;
import de.hybris.platform.cms2.model.contents.containers.ABTestCMSComponentContainerModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cms2lib.model.components.FlashComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.data.ComponentItemListData;
import de.hybris.platform.cmswebservices.data.SimpleResponsiveBannerComponentData;
import de.hybris.platform.cmswebservices.items.facade.validator.CreateComponentValidator;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.models.ABTestCMSComponentContainerModelMother;
import de.hybris.platform.cmswebservices.util.models.BaseStoreModelMother;
import de.hybris.platform.cmswebservices.util.models.CMSSiteModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmswebservices.util.models.FlashComponentModelMother;
import de.hybris.platform.cmswebservices.util.models.LanguageModelMother;
import de.hybris.platform.cmswebservices.util.models.MediaFormatModelMother;
import de.hybris.platform.cmswebservices.util.models.MediaModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.ParagraphComponentModelMother;
import de.hybris.platform.cmswebservices.util.models.SimpleBannerComponentModelMother;
import de.hybris.platform.cmswebservices.util.models.SimpleResponsiveBannerComponentModelMother;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


@IntegrationTest
public class ItemControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String GET_ALL_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/items";
	private static final String GET_SEARCH_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/items?mask={mask}&currentPage={currentPage}&pageSize={pageSize}&sort={sort}";
	private static final String GET_ONE_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/items/{uid}";
	private static final String UPDATE_ITEM_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/items/{uid}";
	private static final String CREATE_ITEM_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/items";
	private static final String REMOVE_ITEM_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/items/{uid}";
	private static final String UIDS_REQUEST_PARAMETER = "uids";

	private static final String PAGE_ID = "uid-homepage";
	private static final String INVALID_PAGE_ID = "invalid_uid-homepage";
	private static final String INVALID_COMPONENT_ID = "invalid_uid-component";
	private static final Integer SLOT_POSITION = 2;

	private static final String NEW_CONTENT_HEADER_EN = "new-content-header";
	private static final String NEW_CONTENT_HEADER_FR = "new-content-header-fr";
	private static final String NEW_NAME_HEADER = "new-name-header";

	private static final String UID = "uid";
	private static final String INVALID_UID = "invalid_uid";

	private static final String PAGE_SIZE_PARAMETER = "pageSize";
	private static final String CURRENT_PAGE_PARAMETER = "currentPage";
	private static final String MASK_PARAMETER = "mask";
	private static final String SORT_PARAMETER = "sort";

	private static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
	private static final Locale LOCALE_FRENCH = Locale.FRENCH;
	private static final String MEDIA_WIDESCREEN = "media-widescreen";
	private static final String MEDIA_DESKTOP = "media-desktop";
	private static final String MEDIA_TABLET = "media-tablet";
	private static final String MEDIA_MOBILE = "media-mobile";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentSlotModelMother contentSlotModelMother;
	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;
	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;
	@Resource
	private PageTemplateModelMother pageTemplateModelMother;
	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;
	@Resource
	private BaseStoreModelMother baseStoreModelMother;
	@Resource
	private MediaFormatModelMother mediaFormatModelMother;
	@Resource
	private LanguageModelMother languageModelMother;
	@Resource
	private SimpleResponsiveBannerComponentModelMother simpleResponsiveBannerComponentModelMother;
	@Resource
	private MediaModelMother mediaModelMother;
	@Resource
	private CMSSiteModelMother cmsSiteModelMother;

	private CatalogVersionModel catalogVersion;

	protected void createElectronicsSite()
	{
		cmsSiteModelMother.createSiteWithTemplate(ELECTRONICS);
	}

	protected void createEmptyAppleCatalog()
	{
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
	}

	protected void createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphAndBanner()
	{
		createElectronicsSite();
		createEmptyAppleCatalog();
		contentSlotModelMother.createHeaderSlotWithParagraphAndBanner(catalogVersion);
	}

	protected void createPagesOfComponents()
	{
		createElectronicsSite();
		createEmptyAppleCatalog();
		contentSlotModelMother.createPagesOfComponents(catalogVersion);
	}

	protected void createElectronicsSiteAndHomeAppleCatalogPageHeaderWithABTestContainer()
	{
		createElectronicsSite();
		createEmptyAppleCatalog();
		contentSlotModelMother.createHeaderSlotWithABTestParagraphsContainer(catalogVersion);
	}

	protected void createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction()
	{
		// Create Electronics site to associate with Apple catalog
		createElectronicsSite();
		// Create empty Apple catalog
		createEmptyAppleCatalog();
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		// Create header content slot name with paragraph and no type restriction
		contentSlotNameModelMother.Header_without_restriction(pageTemplate);
	}

	protected void createElectronicsSiteAndHomeAppleCatalogPageHeaderWithFlashComponentWithoutRestriction()
	{
		// Create Electronics site to associate with Apple catalog
		createElectronicsSite();
		// Create empty Apple catalog
		createEmptyAppleCatalog();
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header
		contentSlotForPageModelMother.HeaderHomepage_FlashComponentOnly(catalogVersion);
		// Create header content slot name with link and no type restriction
		contentSlotNameModelMother.Header_without_restriction(pageTemplate);
	}

	protected void createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions()
	{
		// Create Electronics site to associate with Apple catalog
		createElectronicsSite();
		// Create empty Apple catalog
		createEmptyAppleCatalog();
		// Create homepage template
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		// Create homepage page and content slot header
		contentSlotForPageModelMother.HeaderHomepage_ParagraphOnly(catalogVersion);
		// Create header content slot name with paragraph and banner type restrictions
		contentSlotNameModelMother.Header(pageTemplate);
	}

	@Test
	public void shouldGetAllItemsAndAllItemsArePresent_NoContainer() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final List<AbstractCMSComponentData> entities = executeGetAllItems();
		assertEquals(2, entities.size());

		final AbstractCMSComponentData headerParagraph = getComponentItemByUid(entities, ParagraphComponentModelMother.UID_HEADER);
		assertEquals(CMSParagraphComponentModel._TYPECODE, headerParagraph.getTypeCode());
		assertEquals(ParagraphComponentModelMother.UID_HEADER, headerParagraph.getUid());
		assertEquals(ParagraphComponentModelMother.NAME_HEADER, headerParagraph.getName());
		Assert.assertNull(headerParagraph.getSlotId());

		final AbstractCMSComponentData headerBanner = getComponentItemByUid(entities,
				SimpleBannerComponentModelMother.UID_HEADER_LOGO);
		assertEquals(SimpleBannerComponentModel._TYPECODE, headerBanner.getTypeCode());
		assertEquals(SimpleBannerComponentModelMother.UID_HEADER_LOGO, headerBanner.getUid());
		assertEquals(SimpleBannerComponentModelMother.NAME_HEADER_LOGO, headerBanner.getName());
		Assert.assertNull(headerBanner.getSlotId());
	}

	@Test
	public void whenNoFitlerAndSortingByNameWillReturnFullFirstPage() throws Exception
	{
		createPagesOfComponents();

		final ComponentItemListData page = executeGetPageOfItems(null, 2, 0, "name");
		final List<AbstractCMSComponentData> results = page.getComponentItems();
		assertThat("page 0 (no filter, name sort) failed to return a full page", results.size(), is(2));
		assertThat("page 0 (no filter, name sort) failed to return the maximum number of results",
				page.getPagination().getTotalCount(), is(5));

		final String[] orderedUids = results.stream().map(e -> e.getUid()).collect(toList()).toArray(new String[] {});
		assertThat(orderedUids,
				Matchers.arrayContaining(ParagraphComponentModelMother.UID_HEADER + 2, ParagraphComponentModelMother.UID_HEADER + 5));

	}

	@Test
	public void whenNoFitlerAndSortingByNameWillReturnFullSecondPage() throws Exception
	{
		createPagesOfComponents();

		final ComponentItemListData page = executeGetPageOfItems(null, 2, 1, "name");
		final List<AbstractCMSComponentData> results = page.getComponentItems();
		assertThat("page 1 (no filter, name sort) failed to return a full page", results.size(), is(2));
		assertThat("page 1 (no filter, name sort) failed to return the maximum number of results",
				page.getPagination().getTotalCount(), is(5));

		final String[] orderedUids = results.stream().map(e -> e.getUid()).collect(toList()).toArray(new String[] {});
		assertThat(orderedUids,
				Matchers.arrayContaining(ParagraphComponentModelMother.UID_HEADER + 4, ParagraphComponentModelMother.UID_HEADER + 1));
	}

	@Test
	public void whenNoFitlerAndSortingByNameWillReturnPartialThirdPage() throws Exception
	{
		createPagesOfComponents();

		final ComponentItemListData page = executeGetPageOfItems(null, 2, 2, "name");
		final List<AbstractCMSComponentData> results = page.getComponentItems();
		assertThat("page 2 (no filter, name sort) failed to return a list of 1", results.size(), is(1));
		assertThat("page 2 (no filter, name sort) failed to return the maximum number of results",
				page.getPagination().getTotalCount(), is(5));

		final String[] orderedUids = results.stream().map(e -> e.getUid()).collect(toList()).toArray(new String[] {});
		assertThat(orderedUids, Matchers.arrayContaining(ParagraphComponentModelMother.UID_HEADER + 3));
	}

	@Test
	public void whenFilterAndSortingByNameWillReturnFullFirstPage() throws Exception
	{
		createPagesOfComponents();

		final ComponentItemListData page = executeGetPageOfItems("lukE", 2, 0, "name");
		final List<AbstractCMSComponentData> results = page.getComponentItems();
		assertThat("page 0 (filter, name sort) failed to return a full page", results.size(), is(2));
		assertThat("page 0 (filter, name sort) failed to return the maximum number of results",
				page.getPagination().getTotalCount(), is(3));

		final String[] orderedUids = results.stream().map(e -> e.getUid()).collect(toList()).toArray(new String[] {});
		assertThat(orderedUids,
				Matchers.arrayContaining(ParagraphComponentModelMother.UID_HEADER + 5, ParagraphComponentModelMother.UID_HEADER + 1));

	}

	@Test
	public void whenFilterAndSortingByNameWillReturnPartialSecondPage() throws Exception
	{
		createPagesOfComponents();

		final ComponentItemListData page = executeGetPageOfItems("lukE", 2, 1, "name");
		final List<AbstractCMSComponentData> results = page.getComponentItems();
		assertThat("page 1 (no filter, no sort) failed to return a full page", results.size(), is(1));
		assertThat("page 1 (no filter, no sort) failed to return the maximum number of results",
				page.getPagination().getTotalCount(), is(3));

		final String[] orderedUids = results.stream().map(e -> e.getUid()).collect(toList()).toArray(new String[] {});
		assertThat(orderedUids, Matchers.arrayContaining(ParagraphComponentModelMother.UID_HEADER + 3));
	}


	@Test
	public void shouldGetAllItemsFromCatalogWithContainerComponent_GetsContainerAndComponents() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithABTestContainer();

		final List<AbstractCMSComponentData> entities = executeGetAllItems();
		assertEquals(3, entities.size());

		final AbstractCMSComponentData container = getComponentItemByUid(entities,
				ABTestCMSComponentContainerModelMother.UID_HEADER);
		assertEquals(ABTestCMSComponentContainerModel._TYPECODE, container.getTypeCode());
		assertEquals(ABTestCMSComponentContainerModelMother.UID_HEADER, container.getUid());

		final AbstractCMSComponentData headerParagraph = getComponentItemByUid(entities, ParagraphComponentModelMother.UID_HEADER);
		assertEquals(CMSParagraphComponentModel._TYPECODE, headerParagraph.getTypeCode());
		assertEquals(ParagraphComponentModelMother.UID_HEADER, headerParagraph.getUid());
		assertEquals(ParagraphComponentModelMother.NAME_HEADER, headerParagraph.getName());

		final AbstractCMSComponentData footerParagraph = getComponentItemByUid(entities, ParagraphComponentModelMother.UID_FOOTER);
		assertEquals(CMSParagraphComponentModel._TYPECODE, footerParagraph.getTypeCode());
		assertEquals(ParagraphComponentModelMother.UID_FOOTER, footerParagraph.getUid());
		assertEquals(ParagraphComponentModelMother.NAME_FOOTER, footerParagraph.getName());
	}


	@Ignore("Ignoring for now, as the mothers are creating components with the same modified time")
	@Test
	public void shouldGetAllItemsAndAllItemsArePresentOnTheRightOrder() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final List<AbstractCMSComponentData> entities = executeGetAllItems();
		assertEquals(2, entities.size());

		final AbstractCMSComponentData headerParagraph = entities.stream().findFirst().get();
		final AbstractCMSComponentData headerBanner = entities.stream().skip(1).findFirst().get();

		Assert.assertTrue("The response should return a list in descending order by modification time. ",
				headerParagraph.getModifiedtime().after(headerBanner.getModifiedtime()));
	}

	protected List<AbstractCMSComponentData> executeGetAllItems() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(
						replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);

		return response.getBody().getComponentItems();
	}

	protected ComponentItemListData executeGetPageOfItems(final String mask, final int pageSize, final int currentPage,
			final String sort) throws Exception
	{
		final Map<String, String> params = new HashMap<>();
		params.put(MASK_PARAMETER, mask);
		params.put(PAGE_SIZE_PARAMETER, String.valueOf(pageSize));
		params.put(CURRENT_PAGE_PARAMETER, String.valueOf(currentPage));
		params.put(SORT_PARAMETER, sort);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_SEARCH_ENDPOINT, params)).acceptJson()
				.acceptLanguages(ENGLISH).get(ComponentItemListData.class);

		assertStatusCode(response, 200);

		return response.getBody();
	}

	@Test
	public void shouldGetAllItemsButWithEmptyCollection() throws Exception
	{
		createElectronicsSite();
		createEmptyAppleCatalog();

		final ApiClient apiClient = getApiClientInstance();


		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);

		final List<AbstractCMSComponentData> entities = response.getBody().getComponentItems();

		Assert.assertTrue(CollectionUtils.isEmpty(entities));
	}

	@Test
	public void shouldGetOneItem() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);

		final String endPoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);

		final Response<AbstractCMSComponentData> response = apiClient.request().endpoint(endPoint).acceptJson()
				.acceptLanguages(ENGLISH).get(AbstractCMSComponentData.class);

		assertStatusCode(response, 200);

		assertEquals(CMSParagraphComponentModel._TYPECODE, response.getBody().getTypeCode());
		assertEquals(ParagraphComponentModelMother.UID_HEADER, response.getBody().getUid());
		assertEquals(ParagraphComponentModelMother.NAME_HEADER, response.getBody().getName());
	}

	@Test
	public void shouldReturnHttpStatus404DueToItemNotFound() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final HashMap<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, SimpleBannerComponentModelMother.UID_HEADER_LOGO);

		final Response<ValidationErrorResponse> response = apiClient
				.request().endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables))
				.acceptJson().acceptLanguages(ENGLISH)
				.get(AbstractCMSComponentData.class, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertStatusCode(response, 404);
	}

	@Test
	public void shouldUpdateOneItemExceptUID() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		//send updates to paragraphComponent
		final Map<String, String> localizedValueString = new HashMap<>();
		localizedValueString.put("en", NEW_CONTENT_HEADER_EN);

		final CMSParagraphComponentData componentData = new CMSParagraphComponentData();
		componentData.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		componentData.setUid(ParagraphComponentModelMother.UID_HEADER);
		componentData.setContent(localizedValueString);
		componentData.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);


		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(ENGLISH)
				.put(componentData);

		assertStatusCode(response, 204);


		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(ENGLISH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		assertEquals(NEW_CONTENT_HEADER_EN, responseGet.getBody().getContent().get(Locale.ENGLISH.toLanguageTag()));
		assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	@Test
	public void shouldRemoveAnItem() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		//send updates to paragraphComponent
		final Map<String, String> localizedValueString = new HashMap<>();
		localizedValueString.put("en", NEW_CONTENT_HEADER_EN);

		final CMSParagraphComponentData componentData = new CMSParagraphComponentData();
		componentData.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		componentData.setUid(ParagraphComponentModelMother.UID_HEADER);
		componentData.setContent(localizedValueString);
		componentData.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);

		Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(ENGLISH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, HttpStatus.OK.value());

		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(REMOVE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(ENGLISH)
				.delete();

		assertStatusCode(response, HttpStatus.NO_CONTENT.value());

		apiClient = getApiClientInstance();
		responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(ENGLISH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, HttpStatus.NOT_FOUND.value());
	}

	@Test
	public void shouldReturn404ErrorMessageOnInvalidUidForRemoveCmsComponent() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, INVALID_UID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(REMOVE_ITEM_ENDPOINT, uriVariables))
				.acceptJson()
				.acceptLanguages(ENGLISH)
				.delete(ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertStatusCode(response, HttpStatus.NOT_FOUND.value());
		assertEquals(errorResponse.getErrors().get(0).getMessage(), "CMSComponent with id [" + INVALID_UID + "] not found.");
	}

	@Test
	public void shouldNotUpdateItemDueToNoTypeCodeProvided() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();


		final ApiClient apiClient = getApiClientInstance();


		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);
		final String endpoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables);

		final Response<Void> response = apiClient.request().endpoint(endpoint).acceptJson().acceptLanguages(ENGLISH).put(
				new CMSParagraphComponentData());

		assertStatusCode(response, 400);
	}

	@Test
	public void shouldCreateNewComponentBasedOnType() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH).post(component, CMSParagraphComponentData.class);
		assertStatusCode(response, 201);

		assertEquals(uidNewComponent, response.getBody().getUid());
		assertEquals(nameNewComponent, response.getBody().getName());
		assertEquals(SLOT_POSITION, response.getBody().getPosition());
		assertEquals(ContentSlotModelMother.UID_HEADER, response.getBody().getSlotId());
		assertEquals(PAGE_ID, response.getBody().getPageId());

		assertTrue(response.getHeaders().get(CmswebservicesConstants.HEADER_LOCATION).contains(uidNewComponent));
	}

	@Test
	public void shouldCreateNewComponentBasedOnTypeWithNullContentSlotAndNullPosition() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(null);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(null);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(ENGLISH).post(component, CMSParagraphComponentData.class);
		assertStatusCode(response, 201);

		assertEquals(uidNewComponent, response.getBody().getUid());
		assertEquals(nameNewComponent, response.getBody().getName());
		assertEquals(null, response.getBody().getPosition());
		assertEquals(null, response.getBody().getSlotId());
		assertEquals(PAGE_ID, response.getBody().getPageId());

		assertTrue(response.getHeaders().get(CmswebservicesConstants.HEADER_LOCATION).contains(uidNewComponent));
	}


	@Test
	public void shouldNotCreateNewComponentBasedOnTypeDueToUnkownSlotId() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";
		final String someWrongSlotIDHeader = "unknown-slot-uid";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(someWrongSlotIDHeader);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();


		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.SLOT_ID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}

	@Test
	public void shouldNotCreateNewComponentBasedOnType_TypeRestrictionValidation() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(ProductListComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(PAGE_ID);
		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.UID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}


	@Test
	public void shouldNotCreateNewComponent_InvalidSlotPosition() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(null);
		component.setPageId(PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT, Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.POSITION, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}

	@Test
	public void shouldNotCreateNewComponent_InvalidPageId() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraph_WithTypeRestrictions();

		final String uidNewComponent = "uid-new-component-added";
		final String nameNewComponent = "name-new-component-added";

		final CMSParagraphComponentData component = new CMSParagraphComponentData();

		component.setName(nameNewComponent);
		component.setSlotId(ContentSlotModelMother.UID_HEADER);
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(uidNewComponent);
		component.setPosition(SLOT_POSITION);
		component.setPageId(INVALID_PAGE_ID);

		final ApiClient apiClient = getApiClientInstance();
		final Response<ValidationErrorResponse> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(CREATE_ITEM_ENDPOINT,
						Maps.newHashMap())).acceptJson()
				.acceptLanguages(ENGLISH).post(component, ValidationErrorResponse.class);

		final ValidationErrorResponse errorResponse = response.getErrorResponse();

		assertEquals(1, errorResponse.getErrors().size());
		assertEquals(CreateComponentValidator.PAGE_ID, errorResponse.getErrors().get(0).getSubject());

		assertStatusCode(response, 400);
	}

	@Test
	public void shouldUpdateOneItemExceptUIDWithMultipleHybrisLanguages() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		// creates a store with english and french languages
		baseStoreModelMother.createNorthAmerica(catalogVersion);

		final Map<String, String> localizedMap = new HashMap<>();
		localizedMap.put(LOCALE_ENGLISH.getLanguage(), NEW_CONTENT_HEADER_EN);
		localizedMap.put(LOCALE_FRENCH.getLanguage(), NEW_CONTENT_HEADER_FR);

		final CMSParagraphComponentData component = new CMSParagraphComponentData();
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(ParagraphComponentModelMother.UID_HEADER);
		component.setContent(localizedMap);
		component.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);


		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH)

				.put(component);

		assertStatusCode(response, 204);


		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		assertEquals(NEW_CONTENT_HEADER_EN, responseGet.getBody().getContent().get(LOCALE_ENGLISH.getLanguage()));
		assertEquals(NEW_CONTENT_HEADER_FR, responseGet.getBody().getContent().get(LOCALE_FRENCH.getLanguage()));
		assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	@Ignore("Ignore for now. Updating the FR content to null will throw a ValidationError because the field is required. "
			+ "Revisit after implementing validation for required fields/languages")
	@Test
	public void shouldUpdateOneItemExceptUIDWithMultipleHybrisLanguagesOneValueIsNull() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();

		// creates a store with english and french languages
		baseStoreModelMother.createNorthAmerica(catalogVersion);

		final Map<String, String> localizedMap = new HashMap<>();
		localizedMap.put(LOCALE_ENGLISH.getLanguage(), NEW_CONTENT_HEADER_EN);

		final CMSParagraphComponentData component = new CMSParagraphComponentData();
		component.setTypeCode(CMSParagraphComponentModel._TYPECODE);
		component.setUid(ParagraphComponentModelMother.UID_HEADER);
		component.setName(NEW_NAME_HEADER);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, ParagraphComponentModelMother.UID_HEADER);


		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables))
				.acceptJson().acceptLanguages(Locale.ENGLISH)

				.put(component);

		assertStatusCode(response, 204);


		apiClient = getApiClientInstance();
		final Response<CMSParagraphComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables)).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(CMSParagraphComponentData.class);

		assertStatusCode(responseGet, 200);
		assertEquals(NEW_CONTENT_HEADER_EN, responseGet.getBody().getContent().get(LOCALE_ENGLISH.getLanguage()));
		Assert.assertNull(responseGet.getBody().getContent().get(LOCALE_FRENCH.getLanguage()));
		assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
	}

	/**
	 * Get the component with the matching uid.
	 *
	 * @param items
	 *           - list of component items
	 * @param uid
	 *           - the uid to search for
	 * @return the component with the matching uid
	 */
	protected AbstractCMSComponentData getComponentItemByUid(final List<AbstractCMSComponentData> items, final String uid)
	{
		return items.stream().filter(item -> item.getUid().equals(uid)).findAny().get();
	}

	@Test
	public void shouldGetEmptyMediaContainerFromSimpleResponsiveBannerComponent() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();
		createDefaultMediaFormats();

		simpleResponsiveBannerComponentModelMother
		.createSimpleResponsiveBannerComponentModel(catalogVersion);

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO);

		final String endPoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);

		final Response<SimpleResponsiveBannerComponentData> response = apiClient.request().endpoint(endPoint).acceptJson()
				.acceptLanguages(ENGLISH)
				.get(SimpleResponsiveBannerComponentData.class);

		assertStatusCode(response, 200);

		assertEquals(SimpleResponsiveBannerComponentModel._TYPECODE, response.getBody().getTypeCode());
		assertEquals(SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO, response.getBody().getUid());
		assertEquals(SimpleResponsiveBannerComponentModelMother.NAME_HEADER_LOGO, response.getBody().getName());
		assertThat(response.getBody().getMedia().get(MediaFormatModelMother.WIDESCREEN), is(nullValue()));
		assertThat(response.getBody().getMedia().get(MediaFormatModelMother.DESKTOP), is(nullValue()));
		assertThat(response.getBody().getMedia().get(MediaFormatModelMother.TABLET), is(nullValue()));
		assertThat(response.getBody().getMedia().get(MediaFormatModelMother.MOBILE), is(nullValue()));
	}

	@Test
	public void shouldAddMediaContainerToSimpleResponsiveBannerMultipleLanguages() throws Exception
	{
		languageModelMother.createFrench();
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();
		createDefaultMediaFormats();

		simpleResponsiveBannerComponentModelMother.createSimpleResponsiveBannerComponentModel(catalogVersion);

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO);

		final String getEndpoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);
		final String updateEndpoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT,
				uriVariables);

		final SimpleResponsiveBannerComponentData componentData = new SimpleResponsiveBannerComponentData();
		componentData.setTypeCode(SimpleResponsiveBannerComponentModel._TYPECODE);
		componentData.setUid(SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO);
		componentData.setName(SimpleResponsiveBannerComponentModelMother.NAME_HEADER_LOGO);
		componentData.setMedia(getMediaContainerLocalizedValueMapData());
		componentData.setUrlLink(SimpleResponsiveBannerComponentModelMother.URL_HEADER_LOGO);

		final Response<Void> putResponse = apiClient.request().endpoint(updateEndpoint).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.put(componentData);

		assertStatusCode(putResponse, 204);

		final Response<SimpleResponsiveBannerComponentData> getResponse = apiClient.request().endpoint(getEndpoint).acceptJson()
				.acceptLanguages(Locale.ENGLISH)
				.get(SimpleResponsiveBannerComponentData.class);

		assertStatusCode(getResponse, 200);

		assertEquals(SimpleResponsiveBannerComponentModel._TYPECODE, getResponse.getBody().getTypeCode());
		assertEquals(SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO, getResponse.getBody().getUid());
		assertEquals(SimpleResponsiveBannerComponentModelMother.NAME_HEADER_LOGO, getResponse.getBody().getName());
		assertThat(getResponse.getBody().getMedia(), instanceOf(Map.class));
		final Map<String,Map<String, String>> localizedValueData = getResponse.getBody().getMedia();

		assertThat(localizedValueData.size(), is(2));

		final Map<String, String> formatCodesEnglish = localizedValueData.get(LOCALE_ENGLISH.getLanguage());
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.WIDESCREEN), is(MEDIA_WIDESCREEN));
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.DESKTOP), nullValue());
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.MOBILE), is(MEDIA_MOBILE));
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.TABLET), nullValue());

		final Map<String, String> formatCodesFrench = localizedValueData.get(LOCALE_FRENCH.getLanguage());
		assertThat(formatCodesFrench.get(MediaFormatModelMother.WIDESCREEN), nullValue());
		assertThat(formatCodesFrench.get(MediaFormatModelMother.TABLET), is(MEDIA_TABLET));
		assertThat(formatCodesFrench.get(MediaFormatModelMother.DESKTOP), is(MEDIA_DESKTOP));
		assertThat(formatCodesFrench.get(MediaFormatModelMother.MOBILE), nullValue());
	}

	@Test
	public void shouldRemoveMobileMediaFromContainer() throws Exception
	{
		createDefaultMediaFormats();
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphWithoutRestriction();
		final SimpleResponsiveBannerComponentModel component = simpleResponsiveBannerComponentModelMother
				.createSimpleResponsiveBannerComponentModelWithLogoMedia(catalogVersion);

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO);

		final String getEndpoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);
		final String updateEndpoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT,
				uriVariables);

		final SimpleResponsiveBannerComponentData componentData = new SimpleResponsiveBannerComponentData();
		componentData.setTypeCode(SimpleResponsiveBannerComponentModel._TYPECODE);
		componentData.setUid(SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO);
		componentData.setName(SimpleResponsiveBannerComponentModelMother.NAME_HEADER_LOGO);
		componentData.setMedia(getMediaContainerLocalizedValueMapDataWidescreen());
		componentData.setUrlLink(SimpleResponsiveBannerComponentModelMother.URL_HEADER_LOGO);

		final Response<Void> putResponse = apiClient.request().endpoint(updateEndpoint).acceptJson().acceptLanguages(Locale.ENGLISH)
				.put(componentData);

		assertStatusCode(putResponse, 204);

		final Response<SimpleResponsiveBannerComponentData> getResponse = apiClient.request().endpoint(getEndpoint).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(SimpleResponsiveBannerComponentData.class);

		assertStatusCode(getResponse, 200);

		assertEquals(SimpleResponsiveBannerComponentModel._TYPECODE, getResponse.getBody().getTypeCode());
		assertEquals(SimpleResponsiveBannerComponentModelMother.UID_HEADER_LOGO, getResponse.getBody().getUid());
		assertEquals(SimpleResponsiveBannerComponentModelMother.NAME_HEADER_LOGO, getResponse.getBody().getName());
		final Map<String, String> formatCodesEnglish = getResponse.getBody().getMedia().get(LOCALE_ENGLISH.getLanguage());
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.WIDESCREEN),
				is(MediaModelMother.MediaTemplate.LOGO.getCode() + "-" + MediaFormatModelMother.WIDESCREEN));
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.DESKTOP), nullValue());
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.MOBILE), nullValue());
		assertThat(formatCodesEnglish.get(MediaFormatModelMother.TABLET), nullValue());
	}

	@Test
	public void shouldGetComponentItem_SingleValidUidInUidsParameter() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.parameter(UIDS_REQUEST_PARAMETER, ParagraphComponentModelMother.UID_HEADER)
				.acceptLanguages(ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);
		assertThat(response.getBody().getComponentItems().size(), is(1));

		assertThat(response.getBody().getComponentItems().get(0).getUid(), is(ParagraphComponentModelMother.UID_HEADER));
		assertThat(response.getBody().getComponentItems().get(0).getName(), is(ParagraphComponentModelMother.NAME_HEADER));
		assertThat(response.getBody().getComponentItems().get(0).getTypeCode(), is(CMSParagraphComponentModel._TYPECODE));
	}

	@Test
	public void shouldGetMultipleComponentItems_MultipleValidUidsInUidsParameter() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.parameter(UIDS_REQUEST_PARAMETER,
						ParagraphComponentModelMother.UID_HEADER + "," + SimpleBannerComponentModelMother.UID_HEADER_LOGO)
				.acceptLanguages(ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);
		assertThat(response.getBody().getComponentItems().size(), is(2));

		final List<AbstractCMSComponentData> sorted = response.getBody().getComponentItems().stream()
				.sorted((cmsData1, cmsData2) -> cmsData1.getUid().compareTo(cmsData2.getUid()))
				.collect(Collectors.toList());

		assertThat(sorted.get(0).getUid(), is(SimpleBannerComponentModelMother.UID_HEADER_LOGO));
		assertThat(sorted.get(0).getName(), is(SimpleBannerComponentModelMother.NAME_HEADER_LOGO));
		assertThat(sorted.get(0).getTypeCode(), is(SimpleBannerComponentModel._TYPECODE));

		assertThat(sorted.get(1).getUid(), is(ParagraphComponentModelMother.UID_HEADER));
		assertThat(sorted.get(1).getName(), is(ParagraphComponentModelMother.NAME_HEADER));
		assertThat(sorted.get(1).getTypeCode(), is(CMSParagraphComponentModel._TYPECODE));
	}

	@Test
	public void shouldReturnEmptyObject_InvalidUidInUidsParameter() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithParagraphAndBanner();

		final ApiClient apiClient = getApiClientInstance();
		final Response<ComponentItemListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ALL_ENDPOINT, Maps.newHashMap())).acceptJson()
				.parameter(UIDS_REQUEST_PARAMETER, INVALID_COMPONENT_ID)
				.acceptLanguages(Locale.ENGLISH)
				.get(ComponentItemListData.class);

		assertStatusCode(response, 200);
		assertThat(response.getBody().getComponentItems(), empty());
	}


	@Test
	public void shouldGetOneItemWithUnsupportedType() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithFlashComponentWithoutRestriction();

		final ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = Maps.newHashMap();
		uriVariables.put(UID, FlashComponentModelMother.UID_HEADER);

		final String endPoint = replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(GET_ONE_ENDPOINT, uriVariables);

		final Response<AbstractCMSComponentData> response = apiClient.request().endpoint(endPoint).acceptJson()
				.acceptLanguages(ENGLISH).get(AbstractCMSComponentData.class);

		assertStatusCode(response, 200);

		assertEquals(FlashComponentModel._TYPECODE, response.getBody().getTypeCode());
		assertEquals(FlashComponentModelMother.UID_HEADER, response.getBody().getUid());
		assertEquals(FlashComponentModelMother.NAME_HEADER, response.getBody().getName());
	}

	@Test
	public void shouldUpdateOneItemWithUnsupportedType() throws Exception
	{
		createElectronicsSiteAndHomeAppleCatalogPageHeaderWithFlashComponentWithoutRestriction();

		//send updates to simpleCMSComponent
		final AbstractCMSComponentData componentData = new AbstractCMSComponentData();
		componentData.setTypeCode(FlashComponentModel._TYPECODE);
		componentData.setUid(FlashComponentModelMother.UID_HEADER);
		componentData.setName(NEW_NAME_HEADER);
		componentData.setVisible(Boolean.FALSE);

		ApiClient apiClient = getApiClientInstance();

		final Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(UID, FlashComponentModelMother.UID_HEADER);

		final Response<Void> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables))
				.acceptJson().acceptLanguages(ENGLISH).put(componentData);

		assertStatusCode(response, 204);

		apiClient = getApiClientInstance();
		final Response<AbstractCMSComponentData> responseGet = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(UPDATE_ITEM_ENDPOINT, uriVariables))
				.acceptJson().acceptLanguages(ENGLISH).get(AbstractCMSComponentData.class);

		assertStatusCode(responseGet, 200);
		assertEquals(NEW_NAME_HEADER, responseGet.getBody().getName());
		assertFalse(responseGet.getBody().getVisible());
	}

	private Map<String, Map<String, String>> getMediaContainerLocalizedValueMapDataWidescreen()
	{
		final MediaModel mediaWidescreen = mediaModelMother.createWidescreenLogoMediaModel(catalogVersion);

		final Map<String, String> formatCodeMapEnglish = new LinkedHashMap<>();
		formatCodeMapEnglish.put(MediaFormatModelMother.WIDESCREEN, mediaWidescreen.getCode());
		formatCodeMapEnglish.put(MediaFormatModelMother.DESKTOP, null);
		formatCodeMapEnglish.put(MediaFormatModelMother.MOBILE, null);
		formatCodeMapEnglish.put(MediaFormatModelMother.TABLET, null);

		final Map<String, Map<String, String>> mediaContainer = new HashMap<>();
		mediaContainer.put(LOCALE_ENGLISH.getLanguage(), formatCodeMapEnglish);
		return mediaContainer;
	}

	private Map<String, Map<String, String>> getMediaContainerLocalizedValueMapData()
	{
		final MediaModel mediaWidescreen = mediaModelMother.createLogoMediaModelWithCode(catalogVersion,
				MEDIA_WIDESCREEN);
		final MediaModel mediaDesktop = mediaModelMother.createLogoMediaModelWithCode(catalogVersion, MEDIA_DESKTOP);
		final MediaModel mediaMobile = mediaModelMother.createLogoMediaModelWithCode(catalogVersion, MEDIA_MOBILE);
		final MediaModel mediaTablet = mediaModelMother.createLogoMediaModelWithCode(catalogVersion, MEDIA_TABLET);

		final Map<String, String> formatCodeMapEnglish = new LinkedHashMap<>();
		formatCodeMapEnglish.put(MediaFormatModelMother.WIDESCREEN, mediaWidescreen.getCode());
		formatCodeMapEnglish.put(MediaFormatModelMother.DESKTOP, null);
		formatCodeMapEnglish.put(MediaFormatModelMother.MOBILE, mediaMobile.getCode());
		formatCodeMapEnglish.put(MediaFormatModelMother.TABLET, null);

		final Map<String, String> formatCodeMapFrench = new LinkedHashMap<>();
		formatCodeMapFrench.put(MediaFormatModelMother.WIDESCREEN, null);
		formatCodeMapFrench.put(MediaFormatModelMother.DESKTOP, mediaDesktop.getCode());
		formatCodeMapFrench.put(MediaFormatModelMother.MOBILE, null);
		formatCodeMapFrench.put(MediaFormatModelMother.TABLET, mediaTablet.getCode());

		final Map<String, Map<String, String>> mediaContainer = new HashMap<>();
		mediaContainer.put(ENGLISH.getLanguage(), formatCodeMapEnglish);
		mediaContainer.put(FRENCH.getLanguage(), formatCodeMapFrench);
		return mediaContainer;
	}

	private void createDefaultMediaFormats()
	{
		mediaFormatModelMother.createWidescreenFormat();
		mediaFormatModelMother.createDesktopFormat();
		mediaFormatModelMother.createTabletFormat();
		mediaFormatModelMother.createMobileFormat();
	}
}
