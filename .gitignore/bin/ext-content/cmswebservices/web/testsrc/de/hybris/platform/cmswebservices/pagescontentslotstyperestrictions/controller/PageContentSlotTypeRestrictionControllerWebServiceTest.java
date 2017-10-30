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
package de.hybris.platform.cmswebservices.pagescontentslotstyperestrictions.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import de.hybris.platform.cmswebservices.dto.ContentSlotTypeRestrictionsWsDTO;
import de.hybris.platform.cmswebservices.util.ApiBaseIntegrationTest;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.Response;
import de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotForPageModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotModelMother;
import de.hybris.platform.cmswebservices.util.models.ContentSlotNameModelMother;
import de.hybris.platform.cmswebservices.util.models.PageTemplateModelMother;
import de.hybris.platform.cmswebservices.util.models.ParagraphComponentModelMother;

import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Maps;


/**
 * Integration test for {@link PageContentSlotTypeRestrictionController}
 */
@IntegrationTest
public class PageContentSlotTypeRestrictionControllerWebServiceTest extends ApiBaseIntegrationTest
{

	private static final String SLOT_ID = "slotId";

	private static final String PAGE_UID = "pageUid";

	private static final String ENDPOINT = "/v1/catalogs/{catalogId}/versions/{versionId}/pages/{pageUid}/contentslots/{slotId}/typerestrictions";

	@Resource
	private CatalogVersionModelMother catalogVersionModelMother;

	@Resource
	private PageTemplateModelMother pageTemplateModelMother;

	@Resource
	private ContentSlotForPageModelMother contentSlotForPageModelMother;

	@Resource
	private ContentSlotNameModelMother contentSlotNameModelMother;

	@Resource
	private ParagraphComponentModelMother paragraphComponentModelMother;

	@Test
	public void shouldGetRestrictions() throws Exception
	{

		final ApiClient apiClient = getApiClientInstance();
		final HashMap<String, String> variables = Maps.newHashMap();
		variables.put(PAGE_UID, ContentPageModelMother.UID_HOMEPAGE);
		variables.put(SLOT_ID, ContentSlotModelMother.UID_LOGO);

		final String enpoint = replaceUriVariablesWithDefaultCatalogAndCatalogVersion(ENDPOINT, variables);
		final Response<ContentSlotTypeRestrictionsWsDTO> response = apiClient.request().endpoint(enpoint).acceptJson()
				.acceptLanguages(Locale.ENGLISH).get(ContentSlotTypeRestrictionsWsDTO.class);
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		assertFalse(response.getBody().getValidComponentTypes().isEmpty());
	}

	@Before
	public void setup()
	{
		final CatalogVersionModel catalogVersion = catalogVersionModelMother.createAppleStagedCatalogVersionModel();
		final PageTemplateModel pageTemplate = pageTemplateModelMother.HomePage_Template(catalogVersion);
		contentSlotForPageModelMother.LogoHomepage(catalogVersion);
		contentSlotNameModelMother.Logo(pageTemplate);
	}
}
