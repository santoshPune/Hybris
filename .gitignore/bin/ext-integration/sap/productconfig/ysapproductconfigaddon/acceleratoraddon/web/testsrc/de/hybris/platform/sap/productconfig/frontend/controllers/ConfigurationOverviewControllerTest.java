/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.frontend.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import de.hybris.platform.sap.productconfig.frontend.FilterData;
import de.hybris.platform.sap.productconfig.frontend.OverviewUiData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests
 */
@UnitTest
public class ConfigurationOverviewControllerTest extends AbstractProductConfigControllerTest
{

	private ConfigurationOverviewController configOverviewController;

	@Before
	public void setup()
	{
		configOverviewController = new ConfigurationOverviewController();
		csticList = createCsticsList();
	}

	@Test
	public void testGenerateFilterDataListNoneSelected()
	{
		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		final List<FilterData> filterDataList = configOverviewController.generateCsticFilterDataList(overview);

		assertFalse(filterDataList.isEmpty());
		for (final FilterData filterData : filterDataList)
		{
			assertFalse(filterData.isSelected());
			assertFalse(filterData.getKey().equals(FilterEnum.VISIBLE.toString()));
		}
	}

	@Test
	public void testGenerateFilterDataListNull()
	{
		final ConfigurationOverviewData overview = null;
		final List<FilterData> filterDataList = configOverviewController.generateCsticFilterDataList(overview);

		assertTrue(filterDataList.isEmpty());
	}

	@Test
	public void testGenerateFilterDataListSelected()
	{
		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		final List appliedFilters = new ArrayList<FilterEnum>();
		appliedFilters.add(FilterEnum.USER_INPUT);
		overview.setAppliedCsticFilters(appliedFilters);
		final List<FilterData> filterDataList = configOverviewController.generateCsticFilterDataList(overview);

		assertFalse(filterDataList.isEmpty());
		for (final FilterData filterData : filterDataList)
		{
			if (FilterEnum.USER_INPUT.toString().equals(filterData.getKey()))
			{
				assertTrue(filterData.isSelected());
			}
			else
			{
				assertFalse(filterData.isSelected());
			}
			assertFalse(filterData.getKey().equals(FilterEnum.VISIBLE.toString()));
		}

	}

	@Test
	public void testGenerateFilterDataListIgnoreVisible()
	{
		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		final List appliedFilters = new ArrayList<FilterEnum>();
		appliedFilters.add(FilterEnum.VISIBLE);
		overview.setAppliedCsticFilters(appliedFilters);
		final List<FilterData> filterDataList = configOverviewController.generateCsticFilterDataList(overview);

		assertFalse(filterDataList.isEmpty());
		for (final FilterData filterData : filterDataList)
		{
			assertFalse(filterData.isSelected());
			assertFalse(filterData.getKey().equals(FilterEnum.VISIBLE.toString()));
		}
	}

	@Test(expected = NullPointerException.class)
	public void testInitializeGroupFilterDataListNull()
	{
		configOverviewController.initializeGroupFilterDataList(null);
	}

	@Test
	public void testInitializeGroupFilterDataListNotNull()
	{
		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		overview.setGroups(new ArrayList<>());
		final CharacteristicGroup group = new CharacteristicGroup();
		group.setId("CPQ");
		group.setIsSelectedTopLevelGroup(true);
		overview.getGroups().add(group);
		final List<FilterData> groupFilterDataList = configOverviewController.initializeGroupFilterDataList(overview);
		assertNotNull("We expect groupFilterdataList does not equal null: ", groupFilterDataList);
		assertFalse("We expect empty groupFilterdataList: ", groupFilterDataList.isEmpty());
		assertTrue(groupFilterDataList.size() == 1);
		assertEquals(group.getId(), groupFilterDataList.get(0).getKey());
		assertFalse(groupFilterDataList.get(0).isSelected());
	}

	@Test(expected = NullPointerException.class)
	public void testComputeUiGroupFilterListNull()
	{
		configOverviewController.computeUiGroupFilterList(null);
	}

	@Test
	public void testComputeUiGroupFilterListNotNull()
	{
		final List<FilterData> maxGroupFilterList = new ArrayList<>();
		final FilterData uiFilter1 = new FilterData();
		uiFilter1.setKey("WCEM_MULTI");
		uiFilter1.setDescription("SAP Hardware Centre");
		uiFilter1.setSelected(true);
		maxGroupFilterList.add(uiFilter1);

		final List<FilterData> filterDataList = configOverviewController.computeUiGroupFilterList(maxGroupFilterList);
		assertNotNull("We expect one filterDataList: ", filterDataList);
		assertFalse("We expect not an empty list: ", filterDataList.isEmpty());
		assertTrue("We expect a list with one element in it: ", filterDataList.size() == 1);
		assertTrue("We expect a groupId 'WCEM_SIMPLE_GROUP': ",
				filterDataList.get(0).getKey().equals(maxGroupFilterList.get(0).getKey()));
		assertTrue("We expect a group to be selected: ",
				filterDataList.get(0).isSelected() == maxGroupFilterList.get(0).isSelected());
	}

	@Test
	public void testUpdateGroupFilterList()
	{
		final OverviewUiData overviewUIData = new OverviewUiData();
		final List<FilterData> groupFilterList = new ArrayList<>();
		final FilterData groupFilter = new FilterData();
		groupFilter.setKey("WCEM_SIMPLE");
		groupFilter.setDescription("SAP Software Centre");
		groupFilter.setSelected(true);
		groupFilterList.add(groupFilter);
		overviewUIData.setGroupFilterList(groupFilterList);

		final UiStatus uiStatus = new UiStatus();
		final List<FilterData> maxGroupFilterList = new ArrayList<>();
		final FilterData uiFilter1 = new FilterData();
		uiFilter1.setKey("WCEM_MULTI");
		uiFilter1.setDescription("SAP Hardware Centre");
		uiFilter1.setSelected(false);
		maxGroupFilterList.add(uiFilter1);

		final FilterData uiFilter2 = new FilterData();
		uiFilter2.setKey("WCEM_SIMPLE");
		uiFilter2.setDescription("SAP Software Centre");
		uiFilter2.setSelected(false);
		maxGroupFilterList.add(uiFilter2);
		uiStatus.setMaxGroupFilterList(maxGroupFilterList);

		configOverviewController.updateGroupFilterList(overviewUIData, uiStatus);

		final List<FilterData> uiFilterDataList = overviewUIData.getGroupFilterList();
		final List<FilterData> maxFilterDataList = uiStatus.getMaxGroupFilterList();

		assertNotNull(uiFilterDataList);
		assertNotNull(maxFilterDataList);

		assertTrue(uiFilterDataList.size() == 1);
		assertTrue(maxFilterDataList.size() == 2);

		assertEquals(uiFilterDataList.get(0).getKey(), maxFilterDataList.get(1).getKey());
		assertTrue(uiFilterDataList.get(0).isSelected() == maxFilterDataList.get(1).isSelected());
	}

	@Test
	public void testUpdateGroupFilterListGroupFilterNull()
	{
		final OverviewUiData overviewUIData = new OverviewUiData();
		final List<FilterData> groupFilterList = null;
		overviewUIData.setGroupFilterList(groupFilterList);

		final UiStatus uiStatus = new UiStatus();
		final List<FilterData> maxGroupFilterList = new ArrayList<>();
		final FilterData uiFilter1 = new FilterData();
		uiFilter1.setKey("WCEM_MULTI");
		uiFilter1.setDescription("SAP Hardware Centre");
		uiFilter1.setSelected(true);
		maxGroupFilterList.add(uiFilter1);

		final FilterData uiFilter2 = new FilterData();
		uiFilter2.setKey("WCEM_SIMPLE");
		uiFilter2.setDescription("SAP Software Centre");
		uiFilter2.setSelected(false);
		maxGroupFilterList.add(uiFilter2);
		uiStatus.setMaxGroupFilterList(maxGroupFilterList);

		configOverviewController.updateGroupFilterList(overviewUIData, uiStatus);

		final List<FilterData> uiFilterDataList = overviewUIData.getGroupFilterList();
		final List<FilterData> maxFilterDataList = uiStatus.getMaxGroupFilterList();

		assertNull(uiFilterDataList);
		assertNotNull(maxFilterDataList);

		assertTrue(maxFilterDataList.size() == 2);

		assertTrue(maxFilterDataList.get(0).getKey().equals("WCEM_MULTI"));
		assertTrue(maxFilterDataList.get(0).getDescription().equals("SAP Hardware Centre"));
		assertTrue(maxFilterDataList.get(0).isSelected());

		assertTrue(maxFilterDataList.get(1).getKey().equals("WCEM_SIMPLE"));
		assertTrue(maxFilterDataList.get(1).getDescription().equals("SAP Software Centre"));
		assertFalse(maxFilterDataList.get(1).isSelected());

	}

	@Test
	public void testUpdateAppliedFiltersNoneSelected()
	{
		final OverviewUiData overviewUIData = new OverviewUiData();
		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		overviewUIData.setConfigurationOverviewData(overview);

		final List<FilterData> filterDataList = new ArrayList<FilterData>();

		final FilterData userInput = new FilterData();
		userInput.setKey(FilterEnum.USER_INPUT.toString());
		userInput.setSelected(false);
		filterDataList.add(userInput);

		final FilterData priceRelevant = new FilterData();
		priceRelevant.setKey(FilterEnum.PRICE_RELEVANT.toString());
		priceRelevant.setSelected(false);
		filterDataList.add(priceRelevant);

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setCsticFilterList(filterDataList);

		configOverviewController.updateAppliedFilters(uiStatus, overviewUIData);

		final List appliedFilters = overviewUIData.getConfigurationOverviewData().getAppliedCsticFilters();

		assertTrue(appliedFilters.size() == 1);
		assertTrue(FilterEnum.VISIBLE.equals(appliedFilters.get(0)));
	}

	@Test
	public void testUpdateAppliedFiltersOneSelected()
	{
		final OverviewUiData overviewUIData = new OverviewUiData();
		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		overviewUIData.setConfigurationOverviewData(overview);

		final List<FilterData> filterDataList = new ArrayList<FilterData>();

		final FilterData userInput = new FilterData();
		userInput.setKey(FilterEnum.USER_INPUT.toString());
		userInput.setSelected(true);
		filterDataList.add(userInput);

		final FilterData priceRelevant = new FilterData();
		priceRelevant.setKey(FilterEnum.PRICE_RELEVANT.toString());
		priceRelevant.setSelected(false);
		filterDataList.add(priceRelevant);

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setCsticFilterList(filterDataList);

		configOverviewController.updateAppliedFilters(uiStatus, overviewUIData);

		final List appliedFilters = overviewUIData.getConfigurationOverviewData().getAppliedCsticFilters();

		assertTrue(appliedFilters.size() == 2);
		assertTrue(FilterEnum.VISIBLE.equals(appliedFilters.get(0)));
		assertTrue(FilterEnum.USER_INPUT.equals(appliedFilters.get(1)));
	}

	@Test
	public void testUpdateAppliedFiltersTwoSelected()
	{
		final OverviewUiData overviewUIData = new OverviewUiData();

		final ConfigurationOverviewData overview = new ConfigurationOverviewData();
		overviewUIData.setConfigurationOverviewData(overview);

		final List<FilterData> filterDataList = new ArrayList<FilterData>();

		final FilterData userInput = new FilterData();
		userInput.setKey(FilterEnum.USER_INPUT.toString());
		userInput.setSelected(true);
		filterDataList.add(userInput);

		final FilterData priceRelevant = new FilterData();
		priceRelevant.setKey(FilterEnum.PRICE_RELEVANT.toString());
		priceRelevant.setSelected(true);
		filterDataList.add(priceRelevant);

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setCsticFilterList(filterDataList);

		configOverviewController.updateAppliedFilters(uiStatus, overviewUIData);

		final List appliedFilters = overviewUIData.getConfigurationOverviewData().getAppliedCsticFilters();

		assertTrue(appliedFilters.size() == 3);
		assertTrue(FilterEnum.VISIBLE.equals(appliedFilters.get(0)));
		assertTrue(FilterEnum.USER_INPUT.equals(appliedFilters.get(1)));
		assertTrue(FilterEnum.PRICE_RELEVANT.equals(appliedFilters.get(2)));
	}

	@Test
	public void testUpdateAppliedFiltersConfigOverviewDataNull()
	{
		final OverviewUiData overviewUIData = new OverviewUiData();
		overviewUIData.setConfigurationOverviewData(null);

		final List<FilterData> filterDataList = new ArrayList<FilterData>();

		final FilterData userInput = new FilterData();
		userInput.setKey(FilterEnum.USER_INPUT.toString());
		userInput.setSelected(true);
		filterDataList.add(userInput);

		final FilterData priceRelevant = new FilterData();
		priceRelevant.setKey(FilterEnum.PRICE_RELEVANT.toString());
		priceRelevant.setSelected(false);
		filterDataList.add(priceRelevant);

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setCsticFilterList(filterDataList);

		configOverviewController.updateAppliedFilters(uiStatus, overviewUIData);
		assertNotNull(overviewUIData.getConfigurationOverviewData());

		final List appliedFilters = overviewUIData.getConfigurationOverviewData().getAppliedCsticFilters();
		assertTrue(appliedFilters.size() == 2);
		assertTrue(FilterEnum.VISIBLE.equals(appliedFilters.get(0)));
		assertTrue(FilterEnum.USER_INPUT.equals(appliedFilters.get(1)));
	}

	@Test
	public void testUpdateAppliedFiltersNull()
	{
		final OverviewUiData overviewUIData = null;
		final UiStatus uiStatus = new UiStatus();
		uiStatus.setCsticFilterList(null);

		configOverviewController.updateAppliedFilters(uiStatus, overviewUIData);

		assertNull(overviewUIData);
	}

	@Test
	public void testUpdateGroupsConfigOverviewDataNullNull()
	{
		final UiStatus uiStatus = new UiStatus();
		final List<FilterData> maxGroupFilterList = new ArrayList<>();
		final FilterData uiFilter = new FilterData();
		uiFilter.setKey("WCEM_MULTI");
		uiFilter.setDescription("SAP Hardware Centre");
		uiFilter.setSelected(true);
		maxGroupFilterList.add(uiFilter);
		uiStatus.setMaxGroupFilterList(maxGroupFilterList);

		final OverviewUiData overviewUiData = new OverviewUiData();
		configOverviewController.updateGroups(uiStatus, overviewUiData);

		assertNotNull(overviewUiData);
		assertNotNull(overviewUiData.getConfigurationOverviewData());
		assertNotNull(overviewUiData.getConfigurationOverviewData().getAppliedGroupFilters());
		assertFalse(overviewUiData.getConfigurationOverviewData().getAppliedGroupFilters().isEmpty());
	}
}
