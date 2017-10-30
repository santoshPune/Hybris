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
package de.hybris.platform.sap.productconfig.facades.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit tests: ConfigurationBaseFacadeImpl
 */
@UnitTest
public class ConfigurationBaseFacadeImplTest
{
	private final static String NAME = "A";
	private final static String DESCRIPTION = "B";

	ConfigurationBaseFacadeImpl classUnderTest = new ConfigurationBaseFacadeImpl();
	private UiGroupData uiGroup;
	private List<UiGroupData> subGroups;
	private boolean oneSubGroupConfigurable;
	private final UiGroupData subGroup = new UiGroupData();

	@Test
	public void testOneGroupConfigurableFalse()
	{
		oneSubGroupConfigurable = false;
		classUnderTest.checkAdoptSubGroup(uiGroup, subGroups, oneSubGroupConfigurable);
		assertNull(subGroup.getName());
		assertNull(subGroup.getDescription());
	}

	@Test
	public void testOneGroupConfigurableTrue()
	{
		oneSubGroupConfigurable = true;
		classUnderTest.checkAdoptSubGroup(uiGroup, subGroups, oneSubGroupConfigurable);
		assertNotNull(subGroup.getName());
		assertNotNull(subGroup.getDescription());
	}


	@Before
	public void createTestData()
	{
		uiGroup = new UiGroupData();
		subGroups = new ArrayList<UiGroupData>();
		uiGroup.setName(NAME);
		uiGroup.setDescription(DESCRIPTION);
		uiGroup.setSubGroups(subGroups);
		subGroups.add(subGroup);
	}
}
