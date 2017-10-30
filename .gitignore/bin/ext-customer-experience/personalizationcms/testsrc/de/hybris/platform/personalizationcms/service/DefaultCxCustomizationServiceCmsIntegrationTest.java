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
 *
 *
 */
package de.hybris.platform.personalizationcms.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;


@IntegrationTest
public class DefaultCxCustomizationServiceCmsIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String USER_ID = "customer1@hybris.com";

	@Resource
	private CxService cxService;
	@Resource
	private CxCustomizationService cxCustomizationService;
	@Resource
	private UserService userService;
	@Resource
	private CatalogVersionService catalogVersionService;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importCsv("/personalizationcms/test/testdata_personalizationcms.impex", "utf-8");

	}


	@Test
	public void findCustomizationsWithPageIdFromContentOnlyTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization1");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.singletonMap("pageId", "page1");
		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));
	}


	@Test
	public void findCustomizationsWithPageIdFromContentOnlyNegatedTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization2", "customization3", "customization4");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = new HashMap<String, String>();
		params.put("pageId", "page1");
		params.put("negatePageId", "true");

		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));
	}



	@Test
	public void findCustomizationsWithPageIdFromTemplateOnlyTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization2", "customization3", "customization4");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.singletonMap("pageId", "page2");
		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));
	}

	@Test
	public void findCustomizationsWithPageIdFromTemplateOnlyNegatedTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization1");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = new HashMap<String, String>();
		params.put("pageId", "page2");
		params.put("negatePageId", "true");
		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));
	}

	@Test
	public void findCustomizationsWithPageIdFromBothTemplateAndContentTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization1", "customization2", "customization3", "customization4");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.singletonMap("pageId", "page3");
		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));
	}


	@Test
	public void findCustomizationsWithPageIdFromBothTemplateAndContentNegatedTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet();

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = new HashMap<String, String>();
		params.put("pageId", "page3");
		params.put("negatePageId", "true");
		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));
	}

	public CxService getCxService()
	{
		return cxService;
	}

	public void setCxService(final CxService cxService)
	{
		this.cxService = cxService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}
