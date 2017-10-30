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
package de.hybris.platform.personalizationservices.customization.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.personalizationservices.AbstractCxServiceTest;
import de.hybris.platform.personalizationservices.customization.CxCustomizationService;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;

import com.google.common.collect.Sets;

@IntegrationTest
public class DefaultCxCustomizationServiceIntegrationTest extends AbstractCxServiceTest
{

	@Resource
	private CxCustomizationService cxCustomizationService;

	@Resource
	private CatalogVersionService catalogVersionService;

	@Test
	public void findCustomizationByCodeTest()
	{
		final Optional<CxCustomizationModel> customization = cxCustomizationService.getCustomization(CUSTOMIZATION_CODE,
				catalogVersionService.getCatalogVersion("testCatalog", "Online"));

		assertTrue(customization.isPresent());
		assertTrue(CUSTOMIZATION_CODE.equals(customization.get().getCode()));
		assertNotNull(customization.get().getName());
	}

	@Test
	public void findNoCustomizationByCodeTest()
	{
		final Optional<CxCustomizationModel> customization = cxCustomizationService.getCustomization(CUSTOMIZATION_CODE + ".....",
				catalogVersionService.getCatalogVersion("testCatalog", "Online"));

		assertFalse(customization.isPresent());
	}

	@Test
	public void findCustomizationsTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization1", "customization2", "otherC");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");

		//when
		final List<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion);

		//then
		assertNotNull(customizations);
		assertEquals(expectedCodes, customizations.stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));

		assertFalse(customizations.stream().filter(c -> StringUtils.isEmpty(c.getName())).findAny().isPresent());
	}


	@Test
	public void findCustomizationsWithCodeTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization1", "customization2");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.singletonMap("code", "mizatio");

		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				0, 100);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));

		assertFalse(customizations.getResult().stream().filter(c -> StringUtils.isEmpty(c.getName())).findAny().isPresent());
	}

	@Test
	public void findCustomizationsWithPaginationTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("otherC");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.emptyMap();

		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				2, 2);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(1, customizations.getCount());
		assertEquals(3, customizations.getTotalCount());
		assertEquals(2, customizations.getRequestedCount());
		assertEquals(2, customizations.getRequestedStart());

		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));


		assertFalse(customizations.getResult().stream().filter(c -> StringUtils.isEmpty(c.getName())).findAny().isPresent());
	}

	@Test
	public void findCustomizationsWithNegativePageAndPageSizeTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("customization1", "customization2", "otherC");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.emptyMap();

		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				-10, -10);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(3, customizations.getCount());
		assertEquals(3, customizations.getTotalCount());
		assertEquals(-10, customizations.getRequestedCount());
		assertEquals(-10, customizations.getRequestedStart());

		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));

		assertFalse(customizations.getResult().stream().filter(c -> StringUtils.isEmpty(c.getName())).findAny().isPresent());
	}

	@Test
	public void findCustomizationsWithZeroPageSizeTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet();

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.emptyMap();

		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				2, 0);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(0, customizations.getCount());
		assertEquals(3, customizations.getTotalCount());
		assertEquals(0, customizations.getRequestedCount());
		assertEquals(2, customizations.getRequestedStart());

		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));

		assertFalse(customizations.getResult().stream().filter(c -> StringUtils.isEmpty(c.getName())).findAny().isPresent());
	}

	@Test
	public void findCustomizationsWithNegativePageTest()
	{
		//given
		// final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		// final Map<String, String> params = Collections.emptyMap();

		//when
		// cxCustomizationService.getCustomizations(catalogVersion, params,-5, 2);

		//then
		// we have no idea what will happen, this is database specific.
		// some databases will accept this parameter and start form first page, others will throw exception
	}

	@Test
	public void findCustomizationsWithNegativePageSizeTest()
	{
		//given
		final Set<String> expectedCodes = Sets.newHashSet("otherC");

		final CatalogVersionModel catalogVersion = catalogVersionService.getCatalogVersion("testCatalog", "Online");
		final Map<String, String> params = Collections.emptyMap();

		//when
		final SearchResult<CxCustomizationModel> customizations = cxCustomizationService.getCustomizations(catalogVersion, params,
				2, -10);

		//then
		assertNotNull(customizations);
		assertNotNull(customizations.getResult());
		assertEquals(1, customizations.getCount());
		assertEquals(3, customizations.getTotalCount());
		assertEquals(-10, customizations.getRequestedCount());
		assertEquals(2, customizations.getRequestedStart());

		assertEquals(expectedCodes,
				customizations.getResult().stream().map(CxCustomizationModel::getCode).collect(Collectors.toSet()));

		assertFalse(customizations.getResult().stream().filter(c -> StringUtils.isEmpty(c.getName())).findAny().isPresent());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInputForCustomizations()
	{
		cxCustomizationService.getCustomizations(null);
	}

	@Test
	public void testCreateCustomization()
	{
		//given
		final String custCode = "newCust";
		final String custName = "newCustName";
		CxCustomizationModel cust = new CxCustomizationModel();
		cust.setCode(custCode);
		cust.setName(custName);
		final CxCustomizationsGroupModel group = cxCustomizationService
				.getDefaultGroup(catalogVersionService.getCatalogVersion("testCatalog", "Online"));
		final int expectedRank = group.getCustomizations().size();

		//when
		cust = cxCustomizationService.createCustomization(cust, group, null);

		//then
		assertEquals(custCode, cust.getCode());
		assertEquals(group.getCatalogVersion().getPk(), cust.getCatalogVersion().getPk());
		assertEquals(group.getPk(), cust.getGroup().getPk());
		assertEquals(expectedRank, cust.getRank().intValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCustomizationWithoutCode()
	{
		//given
		final String custName = "newCustName";
		CxCustomizationModel cust = new CxCustomizationModel();
		cust.setName(custName);
		final CxCustomizationsGroupModel group = cxCustomizationService
				.getDefaultGroup(catalogVersionService.getCatalogVersion("testCatalog", "Online"));

		//when
		cxCustomizationService.createCustomization(cust, group, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateCustomizationWithoutName()
	{
		//given
		final String custCode = "newCust";
		CxCustomizationModel cust = new CxCustomizationModel();
		cust.setCode(custCode);
		final CxCustomizationsGroupModel group = cxCustomizationService
				.getDefaultGroup(catalogVersionService.getCatalogVersion("testCatalog", "Online"));

		//when
		cxCustomizationService.createCustomization(cust, group, null);
	}

	@Test
	public void testCreateCustomizationWithRank()
	{
		//given
		final String custCode = "newCust";
		final String custName = "newCustName";
		final Integer rank = Integer.valueOf(0);
		CxCustomizationModel cust = new CxCustomizationModel();
		cust.setCode(custCode);
		cust.setName(custName);
		final CxCustomizationsGroupModel group = cxCustomizationService
				.getDefaultGroup(catalogVersionService.getCatalogVersion("testCatalog", "Online"));
		final int custSize = group.getCustomizations().size();

		//when
		cust = cxCustomizationService.createCustomization(cust, group, rank);

		//then
		assertEquals(custCode, cust.getCode());
		assertEquals(custName, cust.getName());
		assertEquals(group.getCatalogVersion().getPk(), cust.getCatalogVersion().getPk());
		assertEquals(group.getPk(), cust.getGroup().getPk());
		assertEquals(rank.intValue(), cust.getRank().intValue());
		assertEquals(custSize + 1, group.getCustomizations().size());
	}

}
