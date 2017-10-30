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
package de.hybris.platform.cmswebservices.pages.controller;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.CatalogPageModel;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.pages.ProductPageModel;
import de.hybris.platform.cmswebservices.data.AbstractPageData;
import de.hybris.platform.cmswebservices.data.CatalogPageData;
import de.hybris.platform.cmswebservices.data.CategoryPageData;
import de.hybris.platform.cmswebservices.data.ContentPageData;
import de.hybris.platform.cmswebservices.data.EmailPageData;
import de.hybris.platform.cmswebservices.data.PageListData;
import de.hybris.platform.cmswebservices.data.ProductPageData;
import de.hybris.platform.cmswebservices.pages.facade.impl.DefaultPageFacade;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationErrorResponse;
import de.hybris.platform.cmswebservices.util.apiclient.response.ValidationObjectError;
import de.hybris.platform.cmswebservices.util.models.BaseStoreModelMother;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentPageModelMother;
import de.hybris.platform.cmswebservices.util.models.LanguageModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.SiteModelMother;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


@IntegrationTest
public class PageControllerWebServiceTest extends ApiBaseIntegrationTest
{
	private static final String PAGE_ENDPOINT = "/v1/sites/{siteId}/catalogs/{catalogId}/versions/{versionId}/pages";
	private static final Locale LOCALE_ENGLISH = Locale.ENGLISH;
	private static final String PAGE2_TITLE_SUFFIX = "page2_pagetitle";
	private static final String PAGE1_TITLE_SUFFIX = "page1_pagetitle";
	private static final String PAGE2 = "page2";
	private static final String PAGE1 = "page1";
	private static final String UID2 = "uid2";
	private static final String UID1 = "uid1";
	private static final String CONTENT_PAGE = "ContentPage";
	private static final String FROM_EMAIL = "admin@hybris.com";
	private static final String FROM_NAME = "Hybris SAP";
	private static final Boolean DEFAULT_PAGE = true;
	private static final String PAGE_LABEL = "pageLabel";
	private static final String PAGE_TITLE = "pageTitle";
	private static final String PAGE_NAME = "pageName";
	private static final String PAGE_UID = "pageUid";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;
	@Resource
	private ContentPageModelMother contentPageModelMother;
	@Resource
	private SiteModelMother siteModelMother;
	@Resource
	private BaseStoreModelMother baseStoreModelMother;
	@Resource
	private LanguageModelMother languageModelMother;

	private CatalogVersionModel catalogVersion;

	enum ValidationFields
	{
		MESSAGE("This field is required."), REASON("missing"), SUBJECTTYPE("parameter"), TYPE("ValidationError");
		String value;

		ValidationFields(final String value)
		{
			this.value = value;
		}
	}

	enum PageFields
	{
		UID("uid"), NAME("name"), TITLE("title"), DEFAULTPAGE("defaultPage"), TYPECODE("typeCode"), LABEL("label"), TEMPLATE(
				"template");
		String property;

		private PageFields(final String property)
		{
			this.property = property;
		}
	}

	@Before
	public void setup()
	{
		createSomeContentPages();
	}

	@Test
	public void willLoadExpectedListOfContentPages() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<PageListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).get(PageListData.class);

		assertStatusCode(response, 200);

		final List<AbstractPageData> entities = response.getBody().getPages();
		assertThat(entities.size(), is(2));

		final AbstractPageData page1 = getPageByUid(entities, UID1);

		final Map<String, String> pageTitle1 = page1.getTitle();
		assertThat(pageTitle1.get(LOCALE_ENGLISH.toString()), is(PAGE1_TITLE_SUFFIX));
		assertThat(page1.getTypeCode(), is(CONTENT_PAGE));
		assertThat(page1.getTemplate(), is(PageTemplateModelMother.UID_HOME_PAGE));

		final AbstractPageData page2 = getPageByUid(entities, UID2);

		final Map<String, String> pageTitle2 = page2.getTitle();
		assertThat(pageTitle2.get(LOCALE_ENGLISH.toString()), is(PAGE2_TITLE_SUFFIX));
		assertThat(page2.getTypeCode(), is(CONTENT_PAGE));
		assertThat(page2.getTemplate(), is(PageTemplateModelMother.UID_HOME_PAGE));
	}

	@Test
	public void shouldLoadPagesMultipleLanguages() throws Exception
	{
		final ApiClient apiClient = getApiClientInstance();
		final Response<PageListData> response = apiClient.request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).get(PageListData.class);

		assertStatusCode(response, 200);
		assertThat(response.getBody().getPages().size(), is(2));
	}

	@Test
	public void shouldCreateContentPageMultipleLanguages() throws Exception
	{
		final ContentPageData page = populatePage(ContentPageModel._TYPECODE, new ContentPageData());
		page.setLabel(PAGE_LABEL);
		final Response<ContentPageData> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(page, ContentPageData.class);
		assertStatusCode(response, HttpStatus.CREATED.value());

		final ContentPageData body = response.getBody();

		assertThat(
				body,
				allOf(hasProperty(PageFields.UID.property, is(PAGE_UID)), hasProperty(PageFields.NAME.property, is(PAGE_NAME)),
						hasProperty(PageFields.LABEL.property, is(PAGE_LABEL)),
						hasProperty(PageFields.TYPECODE.property, is(ContentPageModel._TYPECODE)),
						hasProperty(PageFields.TEMPLATE.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(PageFields.DEFAULTPAGE.property, is(DEFAULT_PAGE))));
	}

	@Test
	public void shouldCreateContentPage() throws Exception
	{
		final ContentPageData page = populatePage(ContentPageModel._TYPECODE, new ContentPageData());
		page.setLabel(PAGE_LABEL);
		page.setUid(null);
		final Response<ContentPageData> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(page, ContentPageData.class);
		assertStatusCode(response, HttpStatus.CREATED.value());

		final ContentPageData body = response.getBody();

		assertThat(
				body,
				allOf(hasProperty(PageFields.UID.property, startsWith(DefaultPageFacade.DEFAULT_UID_PREFIX)),
						hasProperty(PageFields.NAME.property, is(PAGE_NAME)), hasProperty(PageFields.LABEL.property, is(PAGE_LABEL)),
						hasProperty(PageFields.TYPECODE.property, is(ContentPageModel._TYPECODE)),
						hasProperty(PageFields.TEMPLATE.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(PageFields.DEFAULTPAGE.property, is(DEFAULT_PAGE))));
	}

	@Test
	public void shouldCreateProductPage() throws Exception
	{
		final ProductPageData createContentPageData = populatePage(ProductPageModel._TYPECODE, new ProductPageData());

		final Response<ProductPageData> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(createContentPageData, ProductPageData.class);
		assertStatusCode(response, HttpStatus.CREATED.value());
		assertThat(
				response.getBody(),
				allOf(hasProperty(PageFields.UID.property, is(PAGE_UID)), hasProperty(PageFields.NAME.property, is(PAGE_NAME)),
						hasProperty(PageFields.TYPECODE.property, is(ProductPageModel._TYPECODE)),
						hasProperty(PageFields.TEMPLATE.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(PageFields.DEFAULTPAGE.property, is(DEFAULT_PAGE))));
	}

	@Test
	public void shouldCreateCatalogPage() throws Exception
	{

		final Response<CatalogPageData> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH)
				.post(populatePage(CatalogPageModel._TYPECODE, new CatalogPageData()), CatalogPageData.class);
		assertStatusCode(response, HttpStatus.CREATED.value());

		assertThat(
				response.getBody(),
				allOf(hasProperty(PageFields.UID.property, is(PAGE_UID)), hasProperty(PageFields.NAME.property, is(PAGE_NAME)),
						hasProperty(PageFields.TYPECODE.property, is(CatalogPageModel._TYPECODE)),
						hasProperty(PageFields.TEMPLATE.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(PageFields.DEFAULTPAGE.property, is(DEFAULT_PAGE))));
	}

	@Test
	public void shouldCreateCategoryPage() throws Exception
	{
		final Response<CategoryPageData> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH)
				.post(populatePage(CategoryPageModel._TYPECODE, new CategoryPageData()), CategoryPageData.class);
		assertStatusCode(response, HttpStatus.CREATED.value());

		assertThat(
				response.getBody(),
				allOf(hasProperty(PageFields.UID.property, is(PAGE_UID)), hasProperty(PageFields.NAME.property, is(PAGE_NAME)),
						hasProperty(PageFields.TYPECODE.property, is(CategoryPageModel._TYPECODE)),
						hasProperty(PageFields.TEMPLATE.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(PageFields.DEFAULTPAGE.property, is(DEFAULT_PAGE))));
	}

	@Test
	public void shouldCreateEmailPage() throws Exception
	{
		final EmailPageData emailPageData = populatePage(EmailPageModel._TYPECODE, new EmailPageData());
		emailPageData.setFromName(getLocalizedContent(FROM_NAME));
		emailPageData.setFromEmail(getLocalizedContent(FROM_EMAIL));

		final Response<EmailPageData> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(emailPageData, EmailPageData.class);
		assertStatusCode(response, HttpStatus.CREATED.value());

		assertThat(
				response.getBody(),
				allOf(hasProperty(PageFields.UID.property, is(PAGE_UID)), hasProperty(PageFields.NAME.property, is(PAGE_NAME)),
						hasProperty(PageFields.TYPECODE.property, is(EmailPageModel._TYPECODE)),
						hasProperty(PageFields.TEMPLATE.property, is(PageTemplateModelMother.UID_HOME_PAGE)),
						hasProperty(PageFields.DEFAULTPAGE.property, is(DEFAULT_PAGE))));
	}

	@Test
	public void shouldNotCreatePage_ValidationErrors() throws Exception
	{
		final ContentPageData contentPageData = populatePage(ContentPageModel._TYPECODE, new ContentPageData());
		contentPageData.setUid(null);
		contentPageData.setTypeCode(null);
		contentPageData.setName(null);

		final Response<ValidationErrorResponse> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(contentPageData, ValidationErrorResponse.class);
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());

		final ValidationObjectError nameError = createRequiredFieldValidationObjectError(PageFields.NAME.property);
		final ValidationObjectError typeCodeError = createRequiredFieldValidationObjectError(PageFields.TYPECODE.property);

		assertThat(response.getErrorResponse().getErrors(),
				hasItems(samePropertyValuesAs(nameError), samePropertyValuesAs(typeCodeError)));
	}

	@Test
	public void should_get_validation_errors_when_page_uid_exist() throws Exception
	{
		final ContentPageData contentPageData = populatePage(ContentPageModel._TYPECODE, new ContentPageData());
		contentPageData.setUid(UID1);

		final Response<ValidationErrorResponse> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(contentPageData, ValidationErrorResponse.class);
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());
		
		assertThat(response.getErrorResponse().getErrors().get(0).getSubject(), is(CMSItemModel.UID));
	}

	@Test
	public void should_get_validation_errors_when_page_title_not_provided() throws Exception
	{
		final ContentPageData contentPageData = populatePage(ContentPageModel._TYPECODE, new ContentPageData());
		contentPageData.setTitle(null);

		final Response<ValidationErrorResponse> response = getApiClientInstance().request()
				.endpoint(replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(PAGE_ENDPOINT, Maps.newHashMap()))
				.acceptJson().acceptLanguages(LOCALE_ENGLISH).post(contentPageData, ValidationErrorResponse.class);
		assertStatusCode(response, HttpStatus.BAD_REQUEST.value());

		assertThat(response.getErrorResponse().getErrors().get(0).getSubject(), is(AbstractPageModel.TITLE));
	}

	private Map<String, String> getLocalizedContent(final String value)
	{
		final Map<String, String> localizedMap = new HashMap<>();
		localizedMap.put(LOCALE_ENGLISH.getLanguage(), value);
		return localizedMap;
	}

	private ValidationObjectError createRequiredFieldValidationObjectError(final String field)
	{
		final ValidationObjectError error = new ValidationObjectError();
		error.setMessage(ValidationFields.MESSAGE.value);
		error.setReason(ValidationFields.REASON.value);
		error.setSubject(field);
		error.setSubjectType(ValidationFields.SUBJECTTYPE.value);
		error.setType(ValidationFields.TYPE.value);
		return error;
	}

	private <T extends AbstractPageData> T populatePage(final String pageType, final T page)
	{
		page.setUid(PAGE_UID);
		page.setName(PAGE_NAME);
		page.setTypeCode(pageType);
		page.setTemplate(PageTemplateModelMother.UID_HOME_PAGE);
		page.setTitle(getLocalizedContent(PAGE_TITLE));
		page.setDefaultPage(DEFAULT_PAGE);
		return page;
	}

	protected void createSomeContentPages()
	{
		siteModelMother.createNorthAmericaElectronicsWithAppleStagedCatalog();
		catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		contentPageModelMother.somePage(catalogVersion, UID1, PAGE1);
		contentPageModelMother.somePage(catalogVersion, UID2, PAGE2);
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
	protected AbstractPageData getPageByUid(final List<AbstractPageData> items, final String uid)
	{
		return items.stream().filter(item -> item.getUid().equals(uid)).findAny().get();
	}
}
