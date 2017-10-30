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
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConflictingAssumptionModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConflictingAssumptionModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.CsticModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.SolvableConflictModelImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.cfg.client.IConfigSessionClient;
import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticValueData;
import com.sap.custdev.projects.fbs.slc.cfg.exception.IpcCommandException;


@UnitTest
public class ConfigurationUpdateAdapterImplTest
{
	/**
	 *
	 */
	private static final String VALUE = "Value";
	ConfigurationUpdateAdapterImpl classUnderTest = new ConfigurationUpdateAdapterImpl();
	//
	private CsticModel csticModel;
	private ConfigModel configModel;
	@Mock
	private IConfigSessionClient session;
	private String configId;
	private SolvableConflictModel solvableConflict;
	private String name;
	private ConflictingAssumptionModel conflictingAssumption;
	private final List<String> newValues = new ArrayList<String>();
	private final List<String> oldValues = new ArrayList<String>();

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		csticModel = new CsticModelImpl();
		name = "CsticName";
		csticModel.setName(name);
		configModel = new ConfigModelImpl();
		configId = "1";
		classUnderTest.setConflictAdapter(new SolvableConflictAdapterImpl());
		solvableConflict = new SolvableConflictModelImpl();
		conflictingAssumption = new ConflictingAssumptionModelImpl();
	}

	@Test
	public void testHasBeenRetractedNoRetraction() throws IpcCommandException
	{
		classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId);
	}

	@Test(expected = IllegalStateException.class)
	public void testHasBeenRetractedNoAssumptions() throws IpcCommandException
	{
		csticModel.setRetractTriggered(true);
		classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId);
	}

	@Test(expected = IllegalStateException.class)
	public void testHasBeenRetractedAssumptionsNoMatch() throws IpcCommandException
	{
		csticModel.setRetractTriggered(true);
		configModel.setSolvableConflicts(Arrays.asList(solvableConflict));
		classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId);
	}

	@Test
	public void testHasBeenRetractedAssumptions() throws IpcCommandException
	{
		csticModel.setRetractTriggered(true);
		configModel.setSolvableConflicts(Arrays.asList(solvableConflict));
		solvableConflict.setConflictingAssumptions(Arrays.asList(conflictingAssumption));
		conflictingAssumption.setCsticName(name);
		conflictingAssumption.setId("A");
		assertTrue("Retraction expected", classUnderTest.hasBeenRetracted(csticModel, configModel, session, configId));
	}

	@Test
	public void testDetermineValuesToDeleteEmptyLists()
	{
		final ICsticValueData[] determineValuesToDelete = classUnderTest.determineValuesToDelete(newValues, oldValues);
		assertTrue(determineValuesToDelete.length == 0);
	}

	@Test
	public void testDetermineValuesToDeleteOnlyNew()
	{
		newValues.add(VALUE);
		final ICsticValueData[] determineValuesToDelete = classUnderTest.determineValuesToDelete(newValues, oldValues);
		assertTrue(determineValuesToDelete.length == 0);
	}

	@Test
	public void testDetermineValuesToDeleteOld()
	{
		final String oldValue = VALUE;
		oldValues.add(oldValue);
		final ICsticValueData[] determineValuesToDelete = classUnderTest.determineValuesToDelete(newValues, oldValues);
		assertTrue(determineValuesToDelete.length == 1);
		assertEquals(oldValue, determineValuesToDelete[0].getValueName());
	}

	@Test
	public void testDetermineValuesToSetEmptyList()
	{
		final ICsticValueData[] valuesToSet = classUnderTest.determineValuesToSet(newValues, oldValues);
		assertTrue(valuesToSet.length == 0);

	}

	@Test
	public void testDetermineValuesToSetOnlyOldValue()
	{
		final String oldValue = VALUE;
		oldValues.add(oldValue);
		final ICsticValueData[] valuesToSet = classUnderTest.determineValuesToSet(newValues, oldValues);
		assertTrue(valuesToSet.length == 0);
	}

	@Test
	public void testDetermineValuesToSetNew()
	{
		final String newValue = VALUE;
		newValues.add(newValue);
		final ICsticValueData[] valuesToSet = classUnderTest.determineValuesToSet(newValues, oldValues);
		assertTrue(valuesToSet.length == 1);
		assertEquals(newValue, valuesToSet[0].getValueName());
	}

}
