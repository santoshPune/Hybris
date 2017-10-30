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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConflictingAssumptionModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConflictingAssumptionModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.SolvableConflictModelImpl;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.custdev.projects.fbs.slc.cfg.IConfigSession;
import com.sap.custdev.projects.fbs.slc.cfg.client.IAssumptions;
import com.sap.custdev.projects.fbs.slc.cfg.client.IConflictingAssumptionsContainer;
import com.sap.custdev.projects.fbs.slc.cfg.client.ITextDescription;
import com.sap.custdev.projects.fbs.slc.cfg.exception.IpcCommandException;


/**
 * Tests
 */
@UnitTest
public class SolvableConflictAdapterImplTest
{
	SolvableConflictAdapterImpl classUnderTest = new SolvableConflictAdapterImpl();

	String configId = "1";

	String instanceId = "2";

	@Mock
	IConfigSession configSession;

	@Mock
	IConflictingAssumptionsContainer solvableConflict;

	@Mock
	private IAssumptions assumption;


	private IConflictingAssumptionsContainer[] solvableConflictcontainer;
	private final IConflictingAssumptionsContainer[] solvableConflictcontainerNull = null;
	private IConflictingAssumptionsContainer[] solvableConflictcontainerEmpty;

	private static final String conflictName = "Name";

	private IAssumptions[] assumptions;
	private IAssumptions[] assumptionsEmpty;

	private static final String csticName = "cstic";
	private static final String valueName = "value";

	private ConfigModel configModel;

	private SolvableConflictModel conflict;

	private static String assumptionId = "123";

	private final ITextDescription textDescription = new ITextDescription()
	{

		@Override
		public void setTextLineId(final Integer arg0)
		{//
		}

		@Override
		public void setTextLine(final String arg0)
		{
			//
		}

		@Override
		public void setTextFormat(final String arg0)
		{
			//
		}

		@Override
		public Integer getTextLineId()
		{
			return Integer.valueOf(7);
		}

		@Override
		public String getTextLine()
		{
			return conflictName;
		}

		@Override
		public String getTextFormat()
		{
			return "X";
		}
	};



	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		solvableConflictcontainer = new IConflictingAssumptionsContainer[]
		{ solvableConflict };
		assumptions = new IAssumptions[]
		{ assumption };
		assumptionsEmpty = new IAssumptions[] {};
		solvableConflictcontainerEmpty = new IConflictingAssumptionsContainer[] {};
		final ITextDescription[] description =
		{ textDescription };
		Mockito.when(solvableConflict.getConflictLongText()).thenReturn(description);
		Mockito.when(assumption.getObservableName()).thenReturn(csticName);
		Mockito.when(assumption.getObservableValueName()).thenReturn(valueName);
		Mockito.when(assumption.getInstanceId()).thenReturn(instanceId);
		Mockito.when(assumption.getAsumptionId()).thenReturn(assumptionId);

		configModel = new ConfigModelImpl();
		classUnderTest.setTextConverter(new TextConverterImpl());
	}


	private List<SolvableConflictModel> performTransfer(final ConfigModel configModel)
	{
		classUnderTest.transferSolvableConflicts(configSession, configId, configModel);
		return configModel.getSolvableConflicts();
	}

	@Test
	public void testTransferSolvableConflictsNullConflicts() throws IpcCommandException
	{
		final ConfigModel configModel = new ConfigModelImpl();
		Mockito.when(configSession.getConflictingAssumptions(configId)).thenReturn(solvableConflictcontainerNull);
		final List<SolvableConflictModel> solvableConflicts = performTransfer(configModel);
		assertNull("We expect no conflicts as no conflicts are available at config session", solvableConflicts);
	}

	@Test
	public void testTransferSolvableConflictsEmptyConflicts() throws IpcCommandException
	{
		final ConfigModel configModel = new ConfigModelImpl();
		Mockito.when(configSession.getConflictingAssumptions(configId)).thenReturn(solvableConflictcontainerEmpty);
		final List<SolvableConflictModel> solvableConflicts = performTransfer(configModel);
		assertNotNull("We expect an empty conflict list", solvableConflicts);
		assertEquals("We expect no entries", 0, solvableConflicts.size());
	}

	@Test
	public void testTransferSolvableConflictsNoAssumptions() throws IpcCommandException
	{
		final ConfigModel configModel = prepareSolvableConflictFromSSC();
		classUnderTest.transferSolvableConflicts(configSession, configId, configModel);
		final List<SolvableConflictModel> solvableConflicts = configModel.getSolvableConflicts();
		assertNotNull("We expect conflicts", solvableConflicts);
		assertEquals("We expect one conflict", 1, solvableConflicts.size());
		final SolvableConflictModel solvableConflictModel = solvableConflicts.get(0);
		assertEquals("We expect conflict description", conflictName, solvableConflictModel.getDescription());
		assertNull(solvableConflictModel.getConflictingAssumptions());
	}


	@Test
	public void testTransferSolvableConflictsEmptyAssumptions() throws IpcCommandException
	{
		Mockito.when(solvableConflict.getAssumptions()).thenReturn(assumptionsEmpty);
		final ConfigModel configModel = prepareSolvableConflictFromSSC();
		classUnderTest.transferSolvableConflicts(configSession, configId, configModel);
		final List<SolvableConflictModel> solvableConflicts = configModel.getSolvableConflicts();
		assertNotNull("We expect conflicts", solvableConflicts);
		assertEquals("We expect one conflict", 1, solvableConflicts.size());
		final SolvableConflictModel solvableConflictModel = solvableConflicts.get(0);
		assertNotNull(solvableConflictModel.getConflictingAssumptions());
		assertEquals(0, solvableConflictModel.getConflictingAssumptions().size());
	}

	@Test
	public void testTransferSolvableConflictsCheckAssumptions() throws IpcCommandException
	{
		Mockito.when(solvableConflict.getAssumptions()).thenReturn(assumptions);
		final ConfigModel configModel = prepareSolvableConflictFromSSC();
		classUnderTest.transferSolvableConflicts(configSession, configId, configModel);
		final List<SolvableConflictModel> solvableConflicts = configModel.getSolvableConflicts();
		assertNotNull("We expect conflicts", solvableConflicts);
		assertEquals("We expect one conflict", 1, solvableConflicts.size());
		final SolvableConflictModel solvableConflictModel = solvableConflicts.get(0);
		assertNotNull(solvableConflictModel.getConflictingAssumptions());
		assertEquals(1, solvableConflictModel.getConflictingAssumptions().size());
	}

	@Test
	public void testCreateSolvableConflictModelNoAssumptions()
	{
		final SolvableConflictModel solvableConflictModel = classUnderTest.createSolvableConflictModel(solvableConflict);
		assertNotNull("We expect conflict", solvableConflictModel);
		assertNull("No entries", solvableConflictModel.getConflictingAssumptions());
	}

	@Test
	public void testCreateSolvableConflictModelGroupId()
	{
		Mockito.when(solvableConflict.getAssumptions()).thenReturn(assumptions);
		final SolvableConflictModel solvableConflictModel = classUnderTest.createSolvableConflictModel(solvableConflict);
		assertNotNull("We expect a conflict", solvableConflictModel);
		assertEquals("We expect a group ID", assumptionId, solvableConflictModel.getId());
	}

	@Test
	public void testCreateSolvableConflictModelAssumptions()
	{
		Mockito.when(solvableConflict.getAssumptions()).thenReturn(assumptions);
		final SolvableConflictModel solvableConflictModel = classUnderTest.createSolvableConflictModel(solvableConflict);
		assertNotNull("We expect conflict", solvableConflictModel);
		final List<ConflictingAssumptionModel> conflictingAssumptions = solvableConflictModel.getConflictingAssumptions();
		assertNotNull("We expect assumptions", conflictingAssumptions);
		final ConflictingAssumptionModel conflictingAssumptionModel = conflictingAssumptions.get(0);
		assertNotNull("We expect one assumption", conflictingAssumptionModel);
		assertEquals("Cstic name must be matching", csticName, conflictingAssumptionModel.getCsticName());
		assertEquals("Value name must be matching", valueName, conflictingAssumptionModel.getValueName());
	}

	@Test
	public void testCreateConflictingAssumptionsModel()
	{
		final ConflictingAssumptionModel conflictingAssumptionModel = SolvableConflictAdapterImpl
				.createConflictingAssumptionsModel(assumption);
		assertNotNull("We expect one assumption", conflictingAssumptionModel);
		assertEquals("Cstic name must be matching", csticName, conflictingAssumptionModel.getCsticName());
		assertEquals("Value name must be matching", valueName, conflictingAssumptionModel.getValueName());
	}

	@Test
	public void testCreateConflictingAssumptionsModelInstanceId()
	{
		final ConflictingAssumptionModel conflictingAssumptionModel = SolvableConflictAdapterImpl
				.createConflictingAssumptionsModel(assumption);
		assertNotNull("We expect one assumption", conflictingAssumptionModel);
		assertEquals("Instance must be matching", instanceId, conflictingAssumptionModel.getInstanceId());
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAssumptionIdNoConflicts()
	{

		final String assumptionId = classUnderTest.getAssumptionId(csticName, configModel);
		assertNotNull(assumptionId);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAssumptionIdNoAssumption()
	{
		createConflictAtConfigModel();
		final String assumptionId = classUnderTest.getAssumptionId(csticName, configModel);
		assertNotNull(assumptionId);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAssumptionIdNoMatch()
	{
		createConflictAtConfigModel();
		final ConflictingAssumptionModel assumption = new ConflictingAssumptionModelImpl();
		assumption.setCsticName("Not Known");
		conflict.setConflictingAssumptions(Arrays.asList(assumption));
		final String assumptionId = classUnderTest.getAssumptionId(csticName, configModel);
		assertNotNull(assumptionId);
	}

	@Test
	public void testGetAssumptionMatch()
	{
		createConflictAtConfigModel();
		final ConflictingAssumptionModel assumption = new ConflictingAssumptionModelImpl();
		assumption.setCsticName(csticName);
		conflict.setConflictingAssumptions(Arrays.asList(assumption));
		final String assumptionId = "A1";
		assumption.setId(assumptionId);
		assertEquals("We expect ID", assumptionId, classUnderTest.getAssumptionId(csticName, configModel));
	}


	private void createConflictAtConfigModel()
	{
		conflict = new SolvableConflictModelImpl();
		configModel.setSolvableConflicts(Arrays.asList(conflict));
	}

	private ConfigModel prepareSolvableConflictFromSSC() throws IpcCommandException
	{
		Mockito.when(configSession.getConflictingAssumptions(configId)).thenReturn(solvableConflictcontainer);
		final ConfigModel configModel = new ConfigModelImpl();
		return configModel;
	}
}
