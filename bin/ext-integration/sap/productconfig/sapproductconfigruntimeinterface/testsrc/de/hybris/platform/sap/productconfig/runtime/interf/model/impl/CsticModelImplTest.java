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
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class CsticModelImplTest
{
	private CsticModel model;

	@Before
	public void setUp()
	{
		model = new CsticModelImpl();
	}

	@Test
	public void testSetSingleValue()
	{
		assertFalse(model.isChangedByFrontend());
		assertEquals(0, model.getAssignedValues().size());

		model.setSingleValue("newValue");
		assertTrue(model.isChangedByFrontend());
		assertEquals(1, model.getAssignedValues().size());
		assertEquals("newValue", model.getSingleValue());
	}

	@Test
	public void testSetSingleValue_notChanged()
	{
		final CsticValueModel value = new CsticValueModelImpl();
		value.setName("newValue");
		model.setAssignedValuesWithoutCheckForChange(Collections.singletonList(value));
		assertFalse(model.isChangedByFrontend());
		assertEquals(1, model.getAssignedValues().size());

		model.setSingleValue("newValue");
		assertFalse(model.isChangedByFrontend());
		assertEquals(1, model.getAssignedValues().size());
		assertEquals("newValue", model.getAssignedValues().get(0).getName());
	}


	@Test
	public void testAddValue()
	{
		final CsticValueModel value = new CsticValueModelImpl();
		value.setName("anotherValue");
		model.setAssignedValuesWithoutCheckForChange(Collections.singletonList(value));
		assertFalse(model.isChangedByFrontend());
		assertEquals(1, model.getAssignedValues().size());

		model.addValue("newValue");
		assertTrue(model.isChangedByFrontend());
		assertEquals(2, model.getAssignedValues().size());
		assertEquals("newValue", model.getAssignedValues().get(1).getName());
	}

	@Test
	public void testRemoveExistingValue()
	{
		final String value = "value";
		model.addValue(value);
		model.setChangedByFrontend(false);

		model.removeValue(value);

		assertTrue("Model was changed", model.isChangedByFrontend());
		assertEquals("Wrong number of values", 0, model.getAssignedValues().size());
	}

	@Test
	public void testRemoveNonExistingValue()
	{
		model.addValue("value1");
		model.setChangedByFrontend(false);

		model.removeValue("value2");

		assertFalse("Model was not changed", model.isChangedByFrontend());
		assertEquals("Wrong number of values", 1, model.getAssignedValues().size());
	}

	@Test
	public void testAddValue_notChanged()
	{
		final List<CsticValueModel> assignedValues = new ArrayList<CsticValueModel>();
		CsticValueModel value = new CsticValueModelImpl();
		value.setName("anotherValue");
		assignedValues.add(value);
		value = new CsticValueModelImpl();
		value.setName("newValue");
		assignedValues.add(value);
		model.setAssignedValuesWithoutCheckForChange(assignedValues);
		assertFalse(model.isChangedByFrontend());
		assertEquals(2, model.getAssignedValues().size());

		model.addValue("newValue");
		assertFalse(model.isChangedByFrontend());
		assertEquals(2, model.getAssignedValues().size());
		assertEquals("newValue", model.getAssignedValues().get(1).getName());
	}


	@Test
	public void testEquals()
	{
		final CsticModel cstic1 = new CsticModelImpl();
		assertFalse(cstic1.equals(null));
		assertFalse(cstic1.equals("FALSE"));

		final CsticModel cstic2 = new CsticModelImpl();

		assertTrue(cstic1.equals(cstic2));
	}

	@Test
	public void testInstanceId()
	{
		final String instanceId = "1";
		model.setInstanceId(instanceId);
		assertEquals(instanceId, model.getInstanceId());
	}

	@Test
	public void testClone()
	{
		final CsticModel cstic = new CsticModelImpl();
		cstic.setAuthor(CsticModel.AUTHOR_SYSTEM);

		final CsticModel clonedCstic = cstic.clone();
		assertEquals(cstic.getAuthor(), clonedCstic.getAuthor());
	}

	@Test
	public void testRetractTriggered()
	{
		model.setRetractTriggered(true);
		assertTrue(model.isRetractTriggered());
	}


}
