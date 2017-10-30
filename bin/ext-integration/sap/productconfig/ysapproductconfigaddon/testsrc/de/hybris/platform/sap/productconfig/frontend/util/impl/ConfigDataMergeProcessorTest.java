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
package de.hybris.platform.sap.productconfig.frontend.util.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticValueData;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiGroupForDisplayData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.facades.UiValidationType;
import de.hybris.platform.sap.productconfig.frontend.util.impl.ConfigDataMergeProcessorImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class ConfigDataMergeProcessorTest
{


	private ConfigDataMergeProcessorImpl mergeProcessor;
	private Map<String, Object> sourceConfigMap;
	private ConfigurationData target;
	private String idSub;
	private String idMain;
	private String idSub2;

	@Before
	public void setup()
	{
		mergeProcessor = new ConfigDataMergeProcessorImpl();
	}

	@Test
	public void testAllGroupsCollapsed()
	{
		// prepare source configuration data (recreated from model)
		final ConfigurationData source = new ConfigurationData();
		source.setConfigId("CONFIG_ID");

		final List<UiGroupData> sourceGroups = new ArrayList<UiGroupData>();
		source.setGroups(sourceGroups);

		final UiGroupData sourceGroup = new UiGroupData();
		sourceGroups.add(sourceGroup);
		sourceGroup.setId("GROUP_ID_1");
		sourceGroup.setName("GROUP_NAME_1");
		sourceGroup.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup.setCollapsed(true);
		sourceGroup.setCollapsedInSpecificationTree(false);

		// prepare target configuration data (retrieved from UI)
		final ConfigurationData target = new ConfigurationData();
		target.setConfigId("CONFIG_ID");
		target.setGroups(null);


		mergeProcessor.mergeConfigurationData(source, target);
	}

	@Test
	public void testNullGroup()
	{
		// prepare source configuration data (recreated from model)
		final ConfigurationData source = new ConfigurationData();
		source.setConfigId("CONFIG_ID");

		final List<UiGroupData> sourceGroups = new ArrayList<UiGroupData>();
		source.setGroups(sourceGroups);

		final UiGroupData sourceGroup = new UiGroupData();
		sourceGroups.add(sourceGroup);
		sourceGroup.setId("GROUP_ID_1");
		sourceGroup.setName("GROUP_NAME_1");
		sourceGroup.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup.setCollapsed(true);
		sourceGroup.setCollapsedInSpecificationTree(false);

		// prepare target configuration data (retrieved from UI)
		final ConfigurationData target = new ConfigurationData();
		target.setConfigId("CONFIG_ID");

		final List<UiGroupData> targetGroups = new ArrayList<UiGroupData>();
		target.setGroups(targetGroups);

		final UiGroupData targetGroup = new UiGroupData();
		targetGroups.add(targetGroup);



		mergeProcessor.mergeConfigurationData(source, target);
	}


	@Test
	public void testMergeResult()
	{
		// prepare source configuration data (recreated from model)
		final ConfigurationData source = new ConfigurationData();
		source.setConfigId("CONFIG_ID");

		final List<UiGroupData> sourceGroups = new ArrayList<UiGroupData>();
		source.setGroups(sourceGroups);

		final UiGroupData sourceGroup = new UiGroupData();
		sourceGroups.add(sourceGroup);
		sourceGroup.setId("GROUP_ID_1");
		sourceGroup.setName("GROUP_NAME_1");
		sourceGroup.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup.setCollapsed(false);
		sourceGroup.setCollapsedInSpecificationTree(false);

		final List<CsticData> sourceCstics = new ArrayList<CsticData>();
		final CsticData sourceCstic = new CsticData();
		sourceCstics.add(sourceCstic);
		sourceGroup.setCstics(sourceCstics);

		sourceCstic.setKey("CSTIC_KEY_1");
		sourceCstic.setType(UiType.CHECK_BOX_LIST);
		sourceCstic.setName("CSTIC_NAME_1");
		sourceCstic.setLangdepname("CSTIC_LD_NAME_1");
		sourceCstic.setTypeLength(10);
		sourceCstic.setNumberScale(3);
		sourceCstic.setEntryFieldMask("CSTIC_ENTRY_FIELD_MASk_1");
		sourceCstic.setValidationType(UiValidationType.NUMERIC);
		sourceCstic.setVisible(true);
		sourceCstic.setLastValidValue("CSTIC_LAST_VALID_INPUT_1");

		final List<CsticValueData> sourceValues = new ArrayList<CsticValueData>();
		final CsticValueData sourceValue1 = new CsticValueData();
		sourceValue1.setKey("CSTIC_VALUE_KEY_1");
		sourceValue1.setName("CSTIC_VALUE_NAME_1");
		sourceValues.add(sourceValue1);
		final CsticValueData sourceValue2 = new CsticValueData();
		sourceValue2.setKey("CSTIC_VALUE_KEY_2");
		sourceValue2.setName("CSTIC_VALUE_NAME_2");
		sourceValues.add(sourceValue2);
		sourceCstic.setDomainvalues(sourceValues);

		// prepare target configuration data (retrieved from UI)
		final ConfigurationData target = new ConfigurationData();
		target.setConfigId("CONFIG_ID");

		final List<UiGroupData> targetGroups = new ArrayList<UiGroupData>();
		target.setGroups(targetGroups);

		final UiGroupData targetGroup = new UiGroupData();
		targetGroups.add(targetGroup);
		targetGroup.setId("GROUP_ID_1");
		targetGroup.setCollapsed(true);
		targetGroup.setCollapsedInSpecificationTree(true);


		final List<CsticData> targetCstics = new ArrayList<CsticData>();
		final CsticData targetCstic = new CsticData();
		targetCstics.add(targetCstic);
		targetGroup.setCstics(targetCstics);
		targetCstic.setKey("CSTIC_KEY_1");

		final List<CsticValueData> targetValues = new ArrayList<CsticValueData>();
		final CsticValueData targetValue1 = new CsticValueData();
		targetValues.add(targetValue1);
		final CsticValueData targetValue2 = new CsticValueData();
		targetValues.add(targetValue2);
		targetCstic.setDomainvalues(targetValues);

		mergeProcessor.mergeConfigurationData(source, target);

		assertEquals(source.getConfigId(), target.getConfigId());
		assertTrue("Input merged flag is not set", target.isInputMerged());

		final UiGroupData targetGroupToCheck = target.getGroups().get(0);
		assertEquals("Wrong target group id", "GROUP_ID_1", targetGroupToCheck.getId());
		assertEquals("Wrong target group name", "GROUP_NAME_1", targetGroupToCheck.getName());
		assertEquals("Wrong target group type", GroupType.CSTIC_GROUP, targetGroupToCheck.getGroupType());
		assertTrue("Wrong target group collapsed status", targetGroupToCheck.isCollapsed());
		assertTrue("Wrong target group collapsed in spec status", targetGroupToCheck.isCollapsedInSpecificationTree());

		final CsticData targetCsticToCheck = targetGroupToCheck.getCstics().get(0);
		assertEquals("Wrong target cstic key", "CSTIC_KEY_1", targetCsticToCheck.getKey());
		assertEquals("Wrong target cstic type", UiType.CHECK_BOX_LIST, targetCsticToCheck.getType());
		assertEquals("Wrong target cstic name", "CSTIC_NAME_1", targetCsticToCheck.getName());
		assertEquals("Wrong target cstic type length", 10, targetCsticToCheck.getTypeLength());
		assertEquals("Wrong target cstic number scale", 3, targetCsticToCheck.getNumberScale());
		assertEquals("Wrong target cstic type entry field mask", "CSTIC_ENTRY_FIELD_MASk_1",
				targetCsticToCheck.getEntryFieldMask());
		assertEquals("Wrong target cstic validation type", UiValidationType.NUMERIC, targetCsticToCheck.getValidationType());
		assertTrue("Wrong target cstic visible", targetCsticToCheck.isVisible());
		assertEquals("Wrong target cstic last valid input", "CSTIC_LAST_VALID_INPUT_1", targetCsticToCheck.getLastValidValue());

		final CsticValueData targetValueToCheck1 = targetCsticToCheck.getDomainvalues().get(0);
		assertEquals("Wrong target value name 1", "CSTIC_VALUE_NAME_1", targetValueToCheck1.getName());

		final CsticValueData targetValueToCheck2 = targetCsticToCheck.getDomainvalues().get(1);
		assertEquals("Wrong target value name 2", "CSTIC_VALUE_NAME_2", targetValueToCheck2.getName());

	}

	@Test
	public void testUpdateCsticValues_baseData()
	{
		final CsticData cstic = new CsticData();
		final List<CsticValueData> uiOptions = new ArrayList();
		cstic.setDomainvalues(uiOptions);
		final CsticValueData uiOption = new CsticValueData();
		uiOptions.add(uiOption);
		uiOption.setSelected(true);


		final CsticData sourceCstic = new CsticData();
		final List<CsticValueData> domainvalues = new ArrayList();
		sourceCstic.setDomainvalues(domainvalues);
		final CsticValueData domainValue = new CsticValueData();
		domainvalues.add(domainValue);
		domainValue.setName("1.0");
		domainValue.setLangdepname("Eins");
		domainValue.setKey("1");


		mergeProcessor.updateCsticValues(cstic, sourceCstic);
		assertTrue(cstic.getDomainvalues().get(0).isSelected());
		assertEquals("1.0", cstic.getDomainvalues().get(0).getName());
		assertEquals("1", cstic.getDomainvalues().get(0).getKey());
		assertEquals("Eins", cstic.getDomainvalues().get(0).getLangdepname());
	}

	@Test
	public void testUpdateCsticValues_readOnlyDomain()
	{
		final CsticData cstic = new CsticData();
		final List<CsticValueData> uiOptions = new ArrayList();
		cstic.setDomainvalues(uiOptions);
		final CsticValueData uiOption = new CsticValueData();
		uiOptions.add(uiOption);

		final CsticData sourceCstic = new CsticData();
		final List<CsticValueData> domainvalues = new ArrayList();
		sourceCstic.setDomainvalues(domainvalues);
		final CsticValueData domainValue = new CsticValueData();
		domainvalues.add(domainValue);
		domainValue.setReadonly(true);
		domainValue.setSelected(true);


		mergeProcessor.updateCsticValues(cstic, sourceCstic);
		assertTrue(cstic.getDomainvalues().get(0).isSelected());
	}

	@Test
	public void testUpdateCsticValues_readOnlyDomainMissing()
	{
		final CsticData cstic = new CsticData();
		final List<CsticValueData> uiOptions = new ArrayList();
		cstic.setDomainvalues(uiOptions);

		final CsticData sourceCstic = new CsticData();
		final List<CsticValueData> domainvalues = new ArrayList();
		sourceCstic.setDomainvalues(domainvalues);
		final CsticValueData domainValue = new CsticValueData();
		domainvalues.add(domainValue);
		domainValue.setReadonly(true);
		domainValue.setSelected(true);


		mergeProcessor.updateCsticValues(cstic, sourceCstic);
		assertTrue(cstic.getDomainvalues().get(0).isSelected());
	}

	@Test
	public void testGetIndex()
	{
		mergeProcessor.setTokenizerPath(new StringTokenizer("groups[2].subGroups[3]", "."));
		assertEquals(2, mergeProcessor.getCurrentIndex());
	}

	@Test
	public void testUpdateTargetConfigurationIncompleteTreeNoPathInfo()
	{
		createTestDataForUpdateTargetIncomplete();

		mergeProcessor.updateTargetConfiguration(target, sourceConfigMap);

		//check: first group doesn't have ID since we don't provide path information
		assertNull(target.getGroups().get(0).getId());
	}

	@Test
	public void testUpdateTargetConfigurationIncompleteTree()
	{
		createTestDataForUpdateTargetIncomplete();
		final UiGroupForDisplayData groupToDisplay = new UiGroupForDisplayData();
		groupToDisplay.setPath("groups[0].subGroups[0].");
		groupToDisplay.setGroupIdPath(idMain + "," + idSub);
		target.setGroupToDisplay(groupToDisplay);

		mergeProcessor.updateTargetConfiguration(target, sourceConfigMap);

		//check: first group has ID since we provide path information
		final String id = target.getGroups().get(0).getId();
		assertNotNull(id);
		assertEquals(idMain, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateTargetConfigurationIncompleteTreeWrongPattern()
	{
		createTestDataForUpdateTargetIncomplete();
		final UiGroupForDisplayData groupToDisplay = new UiGroupForDisplayData();
		groupToDisplay.setPath("groups[0].subGroups[0].");
		//omit separator! This must lead to an exception
		groupToDisplay.setGroupIdPath(idMain + idSub);
		target.setGroupToDisplay(groupToDisplay);

		mergeProcessor.updateTargetConfiguration(target, sourceConfigMap);
	}

	@Test
	public void testUpdateTargetConfigurationIncompleteTree2SubItems()
	{
		createTestDataForUpdateTargetIncomplete();
		final UiGroupForDisplayData groupToDisplay = new UiGroupForDisplayData();
		groupToDisplay.setPath("groups[0].subGroups[1].");
		groupToDisplay.setGroupIdPath(idMain + "," + idSub2);
		target.setGroupToDisplay(groupToDisplay);

		mergeProcessor.updateTargetConfiguration(target, sourceConfigMap);

		//check: first group has ID since we provide path information
		final String id = target.getGroups().get(0).getId();
		assertNotNull(id);
		assertEquals(idMain, id);
	}

	@Test
	public void testUpdateTargetConfigurationIncompleteInconsistentPathGroupId()
	{
		createTestDataForUpdateTargetIncomplete();
		final UiGroupForDisplayData groupToDisplay = new UiGroupForDisplayData();
		groupToDisplay.setPath("groups[0].subGroups[1].");
		groupToDisplay.setGroupIdPath(null);
		target.setGroupToDisplay(groupToDisplay);

		mergeProcessor.updateTargetConfiguration(target, sourceConfigMap);

		//check: first group has no ID since path info was not complete
		final String id = target.getGroups().get(0).getId();
		assertNull(id);
	}

	@Test
	public void testUpdateTargetConfigurationIncompleteInconsistentPath()
	{
		createTestDataForUpdateTargetIncomplete();
		final UiGroupForDisplayData groupToDisplay = new UiGroupForDisplayData();
		groupToDisplay.setPath(null);
		groupToDisplay.setGroupIdPath(idMain + "," + idSub2);
		target.setGroupToDisplay(groupToDisplay);

		mergeProcessor.updateTargetConfiguration(target, sourceConfigMap);

		//check: first group has no ID since path info was not complete
		final String id = target.getGroups().get(0).getId();
		assertNull(id);
	}

	@Test
	public void testUpdateGroupList()
	{
		createTestDataForUpdateTargetIncomplete();

		final List<UiGroupData> groups = target.getGroups();
		mergeProcessor.setTokenizerPath(new StringTokenizer("groups[0].subGroups[1]", "."));
		mergeProcessor.setTokenizerGroupId(new StringTokenizer(idMain + "," + idSub2, ","));
		mergeProcessor.calculateCurrentIndicesForPathToDisplayGroup();
		mergeProcessor.updateGroupList(groups, sourceConfigMap);
		//check: first group has ID since we provide path information
		final String id = groups.get(0).getId();
		assertNotNull(id);
		assertEquals(idMain, id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateGroupListWrongBrackets()
	{
		createTestDataForUpdateTargetIncomplete();

		mergeProcessor.setTokenizerPath(new StringTokenizer("groups(5).subGroups(0)"));
		mergeProcessor.setTokenizerGroupId(new StringTokenizer(idMain + "," + idSub2, ","));
		mergeProcessor.calculateCurrentIndicesForPathToDisplayGroup();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateGroupListCorruptPathNoNumber()
	{
		createTestDataForUpdateTargetIncomplete();

		mergeProcessor.setTokenizerPath(new StringTokenizer("groups[].subGroups[0]."));
		mergeProcessor.setTokenizerGroupId(new StringTokenizer(idMain + "," + idSub2, ","));
		mergeProcessor.calculateCurrentIndicesForPathToDisplayGroup();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUpdateGroupListCorruptPath()
	{
		createTestDataForUpdateTargetIncomplete();

		mergeProcessor.setTokenizerPath(new StringTokenizer("groups]2[.subGroups]0[."));
		mergeProcessor.setTokenizerGroupId(new StringTokenizer(idMain + "," + idSub2, ","));
		mergeProcessor.calculateCurrentIndicesForPathToDisplayGroup();
	}



	private void createTestDataForUpdateTargetIncomplete()
	{
		target = new ConfigurationData();
		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData emptyGroup = new UiGroupData();
		final List<UiGroupData> subGroups = new ArrayList<>();
		final UiGroupData subGroup = new UiGroupData();
		idSub = "SUB";
		idSub2 = "SUB2";
		idMain = "MAIN";
		subGroup.setId(idSub);
		final UiGroupData subGroup2 = new UiGroupData();
		subGroup2.setId(idSub2);
		subGroups.add(subGroup);
		subGroups.add(subGroup2);
		emptyGroup.setSubGroups(subGroups);
		groups.add(emptyGroup);
		target.setGroups(groups);
		sourceConfigMap = new HashMap<>();
		final UiGroupData sourceGroup = new UiGroupData();
		sourceConfigMap.put(idSub, sourceGroup);
		sourceConfigMap.put(idMain, sourceGroup);
		sourceConfigMap.put(idSub2, sourceGroup);
	}

	@Test
	public void testConflictGroups()
	{
		// prepare source configuration data (recreated from model)
		final ConfigurationData source = new ConfigurationData();
		source.setConfigId("CONFIG_ID");

		final List<UiGroupData> sourceGroups = new ArrayList<UiGroupData>();
		source.setGroups(sourceGroups);

		final UiGroupData conflictHeaderGroup = new UiGroupData();
		final List<UiGroupData> conflictSubGroups = new ArrayList<UiGroupData>();
		sourceGroups.add(conflictHeaderGroup);
		conflictHeaderGroup.setId("CONFLICT_HEADER");
		conflictHeaderGroup.setName("Resolve issues for characteristics:");
		conflictHeaderGroup.setGroupType(GroupType.CONFLICT_HEADER);
		conflictHeaderGroup.setCollapsed(false);
		conflictHeaderGroup.setCollapsedInSpecificationTree(false);
		conflictHeaderGroup.setSubGroups(conflictSubGroups);

		// Two conflict groups
		final UiGroupData conflictGroup = new UiGroupData();
		conflictSubGroups.add(conflictGroup);
		conflictGroup.setId("CONFLICT123");
		conflictGroup.setName("Conflict for Color");
		conflictGroup.setGroupType(GroupType.CONFLICT);
		conflictGroup.setCollapsed(false);
		conflictGroup.setCollapsedInSpecificationTree(false);

		final UiGroupData conflictGroup2 = new UiGroupData();
		conflictSubGroups.add(conflictGroup2);
		conflictGroup2.setId("CONFLICT234");
		conflictGroup2.setName("Conflict for Size");
		conflictGroup2.setGroupType(GroupType.CONFLICT);
		conflictGroup2.setCollapsed(false);
		conflictGroup2.setCollapsedInSpecificationTree(false);

		final List<CsticData> sourceCstics = new ArrayList<CsticData>();
		conflictGroup2.setCstics(sourceCstics);
		final CsticData sourceCstic = new CsticData();
		sourceCstics.add(sourceCstic);
		conflictGroup2.setCstics(sourceCstics);

		sourceCstic.setKey("CSTIC_KEY_1");
		sourceCstic.setType(UiType.CHECK_BOX_LIST);
		sourceCstic.setName("CSTIC_NAME_1");
		sourceCstic.setLangdepname("CSTIC_LD_NAME_1");
		sourceCstic.setTypeLength(10);
		sourceCstic.setNumberScale(3);
		sourceCstic.setEntryFieldMask("CSTIC_ENTRY_FIELD_MASk_1");
		sourceCstic.setValidationType(UiValidationType.NUMERIC);
		sourceCstic.setVisible(true);
		sourceCstic.setLastValidValue("CSTIC_LAST_VALID_INPUT_1");

		final List<CsticValueData> sourceValues = new ArrayList<CsticValueData>();
		final CsticValueData sourceValue1 = new CsticValueData();
		sourceValue1.setKey("CSTIC_VALUE_KEY_1");
		sourceValue1.setName("CSTIC_VALUE_NAME_1");
		sourceValues.add(sourceValue1);
		final CsticValueData sourceValue2 = new CsticValueData();
		sourceValue2.setKey("CSTIC_VALUE_KEY_2");
		sourceValue2.setName("CSTIC_VALUE_NAME_2");
		sourceValues.add(sourceValue2);
		sourceCstic.setDomainvalues(sourceValues);

		final UiGroupData sourceGroup = new UiGroupData();
		sourceGroups.add(sourceGroup);
		sourceGroup.setId("GROUP_ID_1");
		sourceGroup.setName("GROUP_NAME_1");
		sourceGroup.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup.setCollapsed(false);
		sourceGroup.setCollapsedInSpecificationTree(false);

		final UiGroupData sourceGroup2 = new UiGroupData();
		sourceGroups.add(sourceGroup2);
		sourceGroup2.setId("GROUP_ID_2");
		sourceGroup2.setName("GROUP_NAME_2");
		sourceGroup2.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup2.setCollapsed(false);
		sourceGroup2.setCollapsedInSpecificationTree(false);

		// prepare target configuration data (retrieved from UI)
		final ConfigurationData target = new ConfigurationData();
		target.setConfigId("CONFIG_ID");

		final List<UiGroupData> targetGroups = new ArrayList<UiGroupData>();
		target.setGroups(targetGroups);

		final UiGroupData conflictHeaderTargetGroup = new UiGroupData();
		final List<UiGroupData> conflictSubTargetGroups = new ArrayList<UiGroupData>();
		targetGroups.add(conflictHeaderTargetGroup);
		conflictHeaderTargetGroup.setId(null);
		conflictHeaderTargetGroup.setCollapsed(false);
		conflictHeaderTargetGroup.setCollapsedInSpecificationTree(false);
		conflictHeaderTargetGroup.setSubGroups(conflictSubTargetGroups);

		// Two conflict groups are retrieved from UI
		final UiGroupData conflictTargetGroup = new UiGroupData();
		conflictSubTargetGroups.add(conflictTargetGroup);
		conflictTargetGroup.setId(null);
		conflictTargetGroup.setCollapsed(false);
		conflictTargetGroup.setCollapsedInSpecificationTree(false);

		final UiGroupData conflictTargetGroup2 = new UiGroupData();
		conflictSubTargetGroups.add(conflictTargetGroup2);
		conflictTargetGroup2.setId("CONFLICT234");
		conflictTargetGroup2.setCollapsed(false);
		conflictTargetGroup2.setCollapsedInSpecificationTree(false);


		final List<CsticData> targetCstics = new ArrayList<CsticData>();
		final CsticData targetCstic = new CsticData();
		targetCstics.add(targetCstic);
		conflictTargetGroup2.setCstics(targetCstics);
		targetCstic.setKey("CSTIC_KEY_1");

		final List<CsticValueData> targetValues = new ArrayList<CsticValueData>();
		final CsticValueData targetValue1 = new CsticValueData();
		targetValues.add(targetValue1);
		final CsticValueData targetValue2 = new CsticValueData();
		targetValues.add(targetValue2);
		targetCstic.setDomainvalues(targetValues);

		mergeProcessor.mergeConfigurationData(source, target);

		final List<UiGroupData> targetConflictGroupList = target.getGroups().get(0).getSubGroups();
		// After merge still two conflict groups exist
		assertEquals("Two conflict groups should exist", 2, targetConflictGroupList.size());

		final UiGroupData targetGroupToTest = targetConflictGroupList.get(1);
		assertEquals("Group ID should be CONFLICT234", "CONFLICT234", targetGroupToTest.getId());
		assertEquals("Group type should be CONFLICT", GroupType.CONFLICT, targetGroupToTest.getGroupType());

		final CsticData targetCsticToTest = targetGroupToTest.getCstics().get(0);
		assertEquals("Cstic Type length should be 10", 10, targetCsticToTest.getTypeLength());

		final CsticValueData targetValueToTest = targetCsticToTest.getDomainvalues().get(1);
		assertEquals("Cstic value name should be CSTIC_VALUE_NAME_2", "CSTIC_VALUE_NAME_2", targetValueToTest.getName());

	}

	@Test
	public void testOutdatedConflictGroups()
	{
		// prepare source configuration data (recreated from model)
		final ConfigurationData source = new ConfigurationData();
		source.setConfigId("CONFIG_ID");

		final List<UiGroupData> sourceGroups = new ArrayList<UiGroupData>();
		source.setGroups(sourceGroups);

		final UiGroupData conflictHeaderGroup = new UiGroupData();
		final List<UiGroupData> conflictSubGroups = new ArrayList<UiGroupData>();
		sourceGroups.add(conflictHeaderGroup);
		conflictHeaderGroup.setId("CONFLICT_HEADER");
		conflictHeaderGroup.setName("Resolve issues for characteristics:");
		conflictHeaderGroup.setGroupType(GroupType.CONFLICT_HEADER);
		conflictHeaderGroup.setCollapsed(false);
		conflictHeaderGroup.setCollapsedInSpecificationTree(false);
		conflictHeaderGroup.setSubGroups(conflictSubGroups);

		// Only one conflict group exists
		final UiGroupData conflictGroup = new UiGroupData();
		conflictSubGroups.add(conflictGroup);
		conflictGroup.setId("CONFLICT123");
		conflictGroup.setName("Conflict for Color");
		conflictGroup.setGroupType(GroupType.CONFLICT);
		conflictGroup.setCollapsed(false);
		conflictGroup.setCollapsedInSpecificationTree(false);

		final UiGroupData sourceGroup = new UiGroupData();
		sourceGroups.add(sourceGroup);
		sourceGroup.setId("GROUP_ID_1");
		sourceGroup.setName("GROUP_NAME_1");
		sourceGroup.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup.setCollapsed(false);
		sourceGroup.setCollapsedInSpecificationTree(false);

		final UiGroupData sourceGroup2 = new UiGroupData();
		sourceGroups.add(sourceGroup2);
		sourceGroup2.setId("GROUP_ID_2");
		sourceGroup2.setName("GROUP_NAME_2");
		sourceGroup2.setGroupType(GroupType.CSTIC_GROUP);
		sourceGroup2.setCollapsed(false);
		sourceGroup2.setCollapsedInSpecificationTree(false);

		// prepare target configuration data (retrieved from UI)
		final ConfigurationData target = new ConfigurationData();
		target.setConfigId("CONFIG_ID");

		final List<UiGroupData> targetGroups = new ArrayList<UiGroupData>();
		target.setGroups(targetGroups);

		final UiGroupData conflictHeaderTargetGroup = new UiGroupData();
		final List<UiGroupData> conflictSubTargetGroups = new ArrayList<UiGroupData>();
		targetGroups.add(conflictHeaderTargetGroup);
		conflictHeaderTargetGroup.setId(null);
		conflictHeaderTargetGroup.setCollapsed(false);
		conflictHeaderTargetGroup.setCollapsedInSpecificationTree(false);
		conflictHeaderTargetGroup.setSubGroups(conflictSubTargetGroups);

		// Two conflict groups are retrieved from UI
		final UiGroupData conflictTargetGroup = new UiGroupData();
		conflictSubTargetGroups.add(conflictTargetGroup);
		conflictTargetGroup.setId(null);
		conflictTargetGroup.setCollapsed(false);
		conflictTargetGroup.setCollapsedInSpecificationTree(false);

		final UiGroupData conflictTargetGroup2 = new UiGroupData();
		conflictSubTargetGroups.add(conflictTargetGroup2);
		conflictTargetGroup2.setId("CONFLICT234");
		conflictTargetGroup2.setCollapsed(false);
		conflictTargetGroup2.setCollapsedInSpecificationTree(false);


		final List<CsticData> targetCstics = new ArrayList<CsticData>();
		final CsticData targetCstic = new CsticData();
		targetCstics.add(targetCstic);
		conflictTargetGroup2.setCstics(targetCstics);
		targetCstic.setKey("CSTIC_KEY_1");

		final List<CsticValueData> targetValues = new ArrayList<CsticValueData>();
		final CsticValueData targetValue1 = new CsticValueData();
		targetValues.add(targetValue1);
		final CsticValueData targetValue2 = new CsticValueData();
		targetValues.add(targetValue2);
		targetCstic.setDomainvalues(targetValues);

		mergeProcessor.mergeConfigurationData(source, target);

		final List<UiGroupData> targetConflictGroupList = target.getGroups().get(0).getSubGroups();
		// Merge has deleted the outdated conflict group CONFLICT234
		assertEquals("Only one conflict group should exist after merge", 1, targetConflictGroupList.size());

	}

}
