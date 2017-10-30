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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessage;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.sap.productconfig.facades.CPQActionType;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticStatusType;
import de.hybris.platform.sap.productconfig.facades.FirstOrLastGroupType;
import de.hybris.platform.sap.productconfig.facades.GroupStatusType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiGroupForDisplayData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;
import de.hybris.platform.sap.productconfig.frontend.constants.SapproductconfigaddonConstants;
import de.hybris.platform.sap.productconfig.frontend.util.impl.UiStatusSync;
import de.hybris.platform.sap.productconfig.frontend.validator.ConflictError;
import de.hybris.platform.sap.productconfig.frontend.validator.MandatoryFieldError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;


@UnitTest
public class UpdateConfigureProductControllerTest extends AbstractProductConfigControllerTest
{
	@Mock
	protected BindingResult bindingResults;

	@InjectMocks
	private final UpdateConfigureProductController updateConfigController = new UpdateConfigureProductController();

	private UiStatusSync uiStatusSync;

	private UpdateDataHolder updateData;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		uiStatusSync = new UiStatusSync();
		kbKey = createKbKey();
		csticList = createCsticsList();
		configData = createConfigurationDataWithGeneralGroupOnly();
		updateData = new UpdateDataHolder();
		updateData.setUiStatus(new UiStatus());
		updateData.setConfigData(configData);
	}

	@Test
	public void testResetGroupStatusNoGroups()
	{
		configData.setGroups(null);
		updateConfigController.resetGroupStatus(configData);
		//run without NPE
	}

	@Test
	public void testExecuteUpdateNoGroups()
	{
		final ConfigurationData configDataFromRequest = new ConfigurationData();
		configDataFromRequest.setGroups(null);
		updateData.setConfigData(configDataFromRequest);

		Mockito.doThrow(NullPointerException.class).when(configFacade).updateConfiguration(configDataFromRequest);
		given(configFacade.getConfiguration(configDataFromRequest)).willReturn(configData);

		updateConfigController.executeUpdate(updateData);
		assertNotNull(updateData.getConfigData().getGroups());
	}

	@Test
	public void testConfigureProductForwardIsCorrect() throws Exception
	{
		initializeFirstCall();
		configData.setGroupIdToDisplay("_GEN");
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		when(Boolean.valueOf(bindingResults.hasErrors())).thenReturn(Boolean.FALSE);


		final UiStatus uiStatus = uiStatusSync.extractUiStatusFromConfiguration(configData);
		given(sessionAccessFacade.getUiStatusForProduct("YSAP_SIMPLE_POC")).willReturn(uiStatus);

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		updateConfigController.updateConfigureProduct(configData, bindingResults, model, request);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
	}

	@Test
	public void testUpdateConfigureWithErrors() throws Exception
	{
		initializeFirstCall();
		configData.setGroupIdToDisplay("_GEN");
		given(configFacade.getConfiguration(configData)).willReturn(configData);
		when(Boolean.valueOf(bindingResults.hasErrors())).thenReturn(Boolean.TRUE);

		final UiStatus uiStatus = uiStatusSync.extractUiStatusFromConfiguration(configData);
		given(sessionAccessFacade.getUiStatusForProduct("YSAP_SIMPLE_POC")).willReturn(uiStatus);

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		updateConfigController.updateConfigureProduct(configData, bindingResults, model, request);

		verify(configFacade, times(1)).updateConfiguration(any(ConfigurationData.class));
	}

	@Test
	public void testRemoveOutdatedValidationMultiLevelErrors()
	{
		final Map<String, FieldError> inputToRestore = new HashMap<>();
		inputToRestore.put("Group_1.CSTIC_3", new ConflictError(null, "group1.cstics.A", null, null, null));
		inputToRestore.put("Group_1.CSTIC_2", new MandatoryFieldError(null, "group1.cstics.B", null, null, null));
		inputToRestore.put("Group_1.CSTIC_1", new FieldError("Object", "group1.cstics.C", null));
		inputToRestore.put("Group_2.CSTIC_2", new FieldError("Object", "group2.cstics.C", null));

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setUserInputToRestore(inputToRestore);

		final UiGroupForDisplayData groupToDisplay = new UiGroupForDisplayData();
		groupToDisplay.setPath("Group_1");
		final UiGroupData uiGroup = new UiGroupData();
		uiGroup.setId("Group_1");

		final CsticData cstic = new CsticData();
		cstic.setKey("Group_1.CSTIC_1");
		final List<CsticData> cstics = new ArrayList<>();
		cstics.add(cstic);
		uiGroup.setCstics(cstics);

		groupToDisplay.setGroup(uiGroup);
		configData.setGroupToDisplay(groupToDisplay);

		final UpdateDataHolder updateData = new UpdateDataHolder();
		updateData.setConfigData(configData);
		updateData.setUiStatus(uiStatus);

		final Map<String, FieldError> cleanedInputToRember = updateConfigController.removeOutdatedValidationErrors(updateData);
		assertNotNull(cleanedInputToRember);
		assertEquals(1, cleanedInputToRember.size());
	}

	@Test
	public void testRemoveOutdatedValidationSingleLevelErrors()
	{
		final Map<String, FieldError> inputToRestore = new HashMap<>();
		inputToRestore.put("Group_1.CSTIC_3", new ConflictError(null, "group1.cstics.A", null, null, null));
		inputToRestore.put("Group_2.CSTIC_2", new MandatoryFieldError(null, "group1.cstics.B", null, null, null));
		inputToRestore.put("Group_2.CSTIC_1", new FieldError("Object", "group1.cstics.C", null));
		inputToRestore.put("Group_2.CSTIC_2", new FieldError("Object", "group2.cstics.F", null));
		inputToRestore.put("Group_3.CSTIC_3", new FieldError("Object", "group3.cstics.D", null));
		inputToRestore.put("Group_4.CSTIC_3", new FieldError("Object", "group4.cstics.E", null));
		inputToRestore.put("Group_0.CSTIC_3", new FieldError("Object", "group4.cstics.E", null));

		final UiStatus uiStatus = new UiStatus();
		uiStatus.setUserInputToRestore(inputToRestore);

		configData.setSingleLevel(true);
		final List<UiGroupData> groups = new ArrayList<>();

		for (int i = 0; i < 5; i++)
		{
			final UiGroupData uiGroup = new UiGroupData();
			uiGroup.setId("Group_" + i);
			uiGroup.setCollapsed(i % 2 == 0);
			final List<CsticData> cstics = new ArrayList<>();
			for (int j = 0; j < 4; j++)
			{
				final CsticData cstic = new CsticData();
				cstic.setKey("Group_" + i + ".CSTIC_" + j);
				cstics.add(cstic);
			}
			uiGroup.setCstics(cstics);

			groups.add(uiGroup);
		}

		configData.setGroups(groups);
		final UpdateDataHolder updateData = new UpdateDataHolder();
		updateData.setConfigData(configData);
		updateData.setUiStatus(uiStatus);

		final Map<String, FieldError> cleanedInputToRember = updateConfigController.removeOutdatedValidationErrors(updateData);
		assertNotNull(cleanedInputToRember);
		assertEquals(4, cleanedInputToRember.size());
	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_noErr()
	{
		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				SapproductconfigaddonConstants.CONFIG_ATTRIBUTE);
		final Map<String, FieldError> inputToRestore = updateConfigController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);
		assertEquals(0, inputToRestore.size());
	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_error()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setValue("aaa");
		numericCstic.setCsticStatus(CsticStatusType.ERROR);

		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				SapproductconfigaddonConstants.CONFIG_ATTRIBUTE);
		final FieldError error = createErrorForCstic3();
		bindingResult.addError(error);

		final Map<String, FieldError> inputToRestore = updateConfigController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);

		assertEquals(1, inputToRestore.size());
		assertSame(error, inputToRestore.get("root.WCEM_NUMERIC"));
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getFormattedValue());

	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_error_addInput()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setType(UiType.RADIO_BUTTON_ADDITIONAL_INPUT);
		numericCstic.setAdditionalValue("aaa");
		numericCstic.setCsticStatus(CsticStatusType.ERROR);

		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				SapproductconfigaddonConstants.CONFIG_ATTRIBUTE);
		final FieldError error = createErrorForCstic3();
		bindingResult.addError(error);

		final Map<String, FieldError> inputToRestore = updateConfigController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);

		assertEquals(1, inputToRestore.size());
		assertSame(error, inputToRestore.get("root.WCEM_NUMERIC"));
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getValue());
		assertEquals("", numericCstic.getAdditionalValue());

	}

	@Test
	public void testexpandGroupCloseOthers()
	{
		final List<UiGroupData> uiGroups = new ArrayList<>();
		for (int i = 0; i < 5; i++)
		{
			final UiGroupData uiGroup = new UiGroupData();
			uiGroup.setName("Group_" + i);
			uiGroup.setCollapsed(true);

			uiGroups.add(uiGroup);
		}

		updateConfigController.expandGroupCloseOthers(uiGroups, uiGroups.get(2));

		for (int i = 0; i < 5; i++)
		{
			if (i == 2)
			{
				assertFalse(uiGroups.get(i).isCollapsed());
			}
			else
			{
				assertTrue(uiGroups.get(i).isCollapsed());
			}
		}
	}


	protected FieldError createErrorForCstic3()
	{
		final FieldError error = new FieldError(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE, "groups[0].cstics[3]", "aaa",
				false, new String[]
				{ "msg.key" }, null, "error msg");
		return error;
	}

	@Test
	public void testHandleValidationErrorsBeforeUpdate_findErrorInSubgroup0Cstic0()
	{
		final List<UiGroupData> subGroups = createCsticsGroup();
		final String csticKey = subGroups.get(0).getCstics().get(0).getKey();
		configData.getGroups().get(0).setSubGroups(subGroups);
		final CsticData numericCstic = subGroups.get(0).getCstics().get(0);
		numericCstic.setValue("aaa");
		numericCstic.setCsticStatus(CsticStatusType.ERROR);

		final BindingResult bindingResult = new BeanPropertyBindingResult(configData,
				SapproductconfigaddonConstants.CONFIG_ATTRIBUTE);
		final FieldError error = createErrorForSubgoup0Cstic0();
		bindingResult.addError(error);

		final Map<String, FieldError> inputToRestore = updateConfigController.handleValidationErrorsBeforeUpdate(configData,
				bindingResult);

		assertEquals(1, inputToRestore.size());
		assertSame(error, inputToRestore.get(csticKey));
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getFormattedValue());

	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_errorInSubgroup0Cstic0()
	{
		final List<UiGroupData> subGroups = createCsticsGroup();
		final String csticKey = subGroups.get(0).getCstics().get(0).getKey();
		configData.getGroups().get(0).setSubGroups(subGroups);
		final CsticData numericCstic = subGroups.get(0).getCstics().get(0);

		final FieldError error = createErrorForSubgoup0Cstic0();
		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put(csticKey, error);

		final BindingResult errors = updateConfigController
				.restoreValidationErrorsAfterUpdate(userInputToRestore, configData, null);

		assertEquals(1, errors.getErrorCount());
		assertEquals("CStic should have an error", CsticStatusType.ERROR, numericCstic.getCsticStatus());

	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_visibleCstic()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setAdditionalValue("");
		final FieldError error = createErrorForCstic3();

		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put("root.WCEM_NUMERIC", error);
		final BindingResult errors = updateConfigController
				.restoreValidationErrorsAfterUpdate(userInputToRestore, configData, null);

		assertEquals(1, errors.getErrorCount());
		assertEquals("CStic should have an error", CsticStatusType.ERROR, numericCstic.getCsticStatus());
		assertEquals("aaa", numericCstic.getFormattedValue());
		assertEquals("", numericCstic.getAdditionalValue());


	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_visibleCstic_addInput()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setType(UiType.RADIO_BUTTON_ADDITIONAL_INPUT);
		final FieldError error = createErrorForCstic3();

		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put("root.WCEM_NUMERIC", error);
		final BindingResult errors = updateConfigController
				.restoreValidationErrorsAfterUpdate(userInputToRestore, configData, null);

		assertEquals(1, errors.getErrorCount());
		assertEquals("CStic should have an error", CsticStatusType.ERROR, numericCstic.getCsticStatus());
		assertEquals(numericCstic.getLastValidValue(), numericCstic.getValue());
		assertEquals("aaa", numericCstic.getAdditionalValue());


	}

	@Test
	public void testRestoreValidationErrorsAfterUpdate_invisibleCstic()
	{
		final CsticData numericCstic = csticList.get(3);
		numericCstic.setVisible(false);
		final FieldError error = createErrorForCstic3();

		final Map<String, FieldError> userInputToRestore = new HashMap<>();
		userInputToRestore.put("root.WCEM_NUMERIC", error);
		final BindingResult errors = updateConfigController
				.restoreValidationErrorsAfterUpdate(userInputToRestore, configData, null);

		assertEquals(0, errors.getErrorCount());

	}

	@Test
	public void testFindFirstGroupForCsticIdSimple()
	{
		final String csticId = "root.WCEM_NUMERIC";
		final UiGroupData uiGroup = updateConfigController.findFirstGroupForCsticId(configData.getGroups(), csticId);

		assertNotNull(uiGroup);
		assertEquals(configData.getGroups().get(0), uiGroup);
	}

	@Test
	public void testFindFirstConflictGroupForCsticId()
	{
		final String csticId = "root.WCEM_Conflict2";
		final ConfigurationData configData = createEmptyConfigData();

		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData uiConflictData = createCsticsGroupWithConflicts(csticId);
		groups.add(uiConflictData);

		final UiGroupData uiGroupData = createUiGroup("1", GroupStatusType.ERROR, true);
		final List<CsticData> cstics = new ArrayList<>();
		final CsticData cstic = new CsticData();
		cstic.setKey("ABC");
		cstic.setLongText("lorem ipsum");
		cstic.setShowFullLongText(false);
		cstics.add(cstic);
		uiGroupData.setCstics(cstics);
		groups.add(uiGroupData);

		groups.add(createUiGroup("2", GroupStatusType.WARNING, true));
		final UiGroupData group3 = createUiGroup("3", GroupStatusType.CONFLICT, true);
		group3.setCstics(groups.get(0).getSubGroups().get(1).getCstics());
		groups.add(group3);
		groups.add(createUiGroup("4", GroupStatusType.DEFAULT, true));

		final UiGroupData group5 = createUiGroup("5", GroupStatusType.CONFLICT, true);
		group5.setCstics(groups.get(0).getSubGroups().get(0).getCstics());
		groups.add(group5);

		configData.setGroups(groups);

		final UiGroupData uiGroup = updateConfigController.findFirstConflictGroupForCsticId(configData.getGroups(), csticId);

		assertNotNull(uiGroup);
		assertEquals(configData.getGroups().get(0).getSubGroups().get(1).getId(), uiGroup.getId());
	}

	@Test
	public void testNotFindFirstConflictGroupForCsticId()
	{
		final String csticId = "root.WCEM_Conflict2";
		final ConfigurationData configData = createEmptyConfigData();

		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData uiConflictData = createCsticsGroupWithConflicts(csticId);
		groups.add(uiConflictData);

		final UiGroupData uiGroupData = createUiGroup("1", GroupStatusType.ERROR, true);
		final List<CsticData> cstics = new ArrayList<>();
		final CsticData cstic = new CsticData();
		cstic.setKey("ABC");
		cstic.setLongText("lorem ipsum");
		cstic.setShowFullLongText(false);
		cstics.add(cstic);
		uiGroupData.setCstics(cstics);
		groups.add(uiGroupData);

		groups.add(createUiGroup("2", GroupStatusType.WARNING, true));
		final UiGroupData group3 = createUiGroup("3", GroupStatusType.CONFLICT, true);
		group3.setCstics(groups.get(0).getSubGroups().get(1).getCstics());
		groups.add(group3);
		groups.add(createUiGroup("4", GroupStatusType.DEFAULT, true));

		final UiGroupData group5 = createUiGroup("5", GroupStatusType.CONFLICT, true);
		group5.setCstics(groups.get(0).getSubGroups().get(0).getCstics());
		groups.add(group5);

		configData.setGroups(groups);

		final UiGroupData uiGroup = updateConfigController.findFirstConflictGroupForCsticId(configData.getGroups(), "wrongCsticId");

		assertNull(uiGroup);
	}

	/**
	 * There are 2 conflicts with the same cstic, but only the first should be found.
	 */
	@Test
	public void testFindFirstConflictGroupForCsticId2()
	{
		final String csticId = "root.WCEM_Conflict2";
		final ConfigurationData configData = createEmptyConfigData();

		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData uiConflictData = createConflictGroups(csticId);
		groups.add(uiConflictData);

		final UiGroupData uiGroupData = createUiGroup("1", GroupStatusType.ERROR, true);
		final List<CsticData> cstics = new ArrayList<>();
		final CsticData cstic = new CsticData();
		cstic.setKey("ABC");
		cstic.setLongText("lorem ipsum");
		cstic.setShowFullLongText(false);
		cstics.add(cstic);
		uiGroupData.setCstics(cstics);
		groups.add(uiGroupData);

		groups.add(createUiGroup("2", GroupStatusType.WARNING, true));
		final UiGroupData group3 = createUiGroup("3", GroupStatusType.CONFLICT, true);
		group3.setCstics(groups.get(0).getSubGroups().get(1).getCstics());
		groups.add(group3);
		groups.add(createUiGroup("4", GroupStatusType.DEFAULT, true));

		final UiGroupData group5 = createUiGroup("5", GroupStatusType.CONFLICT, true);
		group5.setCstics(groups.get(0).getSubGroups().get(0).getCstics());
		groups.add(group5);

		configData.setGroups(groups);

		final UiGroupData uiGroup = updateConfigController.findFirstConflictGroupForCsticId(configData.getGroups(), csticId);

		assertNotNull(uiGroup);
		assertEquals(configData.getGroups().get(0).getSubGroups().get(1).getId(), uiGroup.getId());
	}

	@Test
	public void testFindFirstGtroupForCsticIdComplexSingleLevel()
	{
		final String csticId = "subGroup.NUMERIC";

		final CsticData cstic = new CsticData();
		cstic.setKey(csticId);

		final List<CsticData> cstics = new ArrayList<>();
		cstics.add(cstic);

		final ConfigurationData configData = createConfigurationDataMultiLevel();
		final UiGroupData uiGroupToSearch = configData.getGroups().get(1);
		uiGroupToSearch.setCstics(cstics);

		final UiGroupData uiGroup = updateConfigController.findFirstGroupForCsticId(configData.getGroups(), csticId);
		assertNotNull(uiGroup);
		assertEquals(uiGroupToSearch.getId(), uiGroup.getId());
	}

	@Test
	public void testFindFirstGtroupForCsticIdComplexMultiLevel()
	{
		final String csticId = "subGroup.NUMERIC";

		final CsticData cstic = new CsticData();
		cstic.setKey(csticId);

		final List<CsticData> cstics = new ArrayList<>();
		cstics.add(cstic);

		final ConfigurationData configData = createConfigurationDataMultiLevel();
		final UiGroupData uiGroupToSearch = configData.getGroups().get(4).getSubGroups().get(1);
		uiGroupToSearch.setCstics(cstics);

		final UiGroupData uiGroup = updateConfigController.findFirstGroupForCsticId(configData.getGroups(), csticId);
		assertNotNull(uiGroup);
		assertEquals(uiGroupToSearch.getId(), uiGroup.getId());
	}

	@Test
	public void testIsCsticPartOfGroup()
	{
		final String csticId = "root.WCEM_NUMERIC";
		final UiGroupData uiGroup = create4CsticGroups("root", "root").get(0);

		final boolean result = updateConfigController.isCsticPartOfGroup(uiGroup, csticId);
		assertTrue(result);
	}

	@Test
	public void testIsCsticPartOfGroupEmptyGroup()
	{
		final String csticId = "root.WCEM_NUMERIC";
		final UiGroupData uiGroup = new UiGroupData();

		final boolean result = updateConfigController.isCsticPartOfGroup(uiGroup, csticId);
		assertFalse(result);
	}

	@Test
	public void testNotIsCsticPartOfGroup()
	{
		final String csticId = "root.DOES_NOT_EXISTC";
		final UiGroupData uiGroup = create4CsticGroups("root", "root").get(0);

		final boolean result = updateConfigController.isCsticPartOfGroup(uiGroup, csticId);
		assertFalse(result);
	}


	@Test
	public void testGroupStatusReset()
	{
		final ConfigurationData configData = new ConfigurationData();

		final List<UiGroupData> groups = new ArrayList<>();
		groups.add(createUiGroup("1", GroupStatusType.ERROR, true));
		groups.add(createUiGroup("2", GroupStatusType.WARNING, true));
		groups.add(createUiGroup("3", GroupStatusType.FLAG, true));
		groups.add(createUiGroup("4", GroupStatusType.CONFLICT, true));
		configData.setGroups(groups);

		updateConfigController.resetGroupStatus(configData);

		for (final UiGroupData group : configData.getGroups())
		{
			assertEquals(GroupStatusType.DEFAULT, group.getGroupStatus());
		}
	}

	@Test
	public void testGetFirstCsticWithErrorInGroupWithoutErrorCstic()
	{
		final UiGroupData group = new UiGroupData();
		final List<CsticData> cstics = createCsticsList();
		group.setCstics(cstics);

		final CsticData cstic = updateConfigController.getFirstCsticWithErrorInGroup(group);

		assertNull(cstic);
	}

	@Test
	public void testGetFirstCsticWithErrorInGroupWithErrorCstic()
	{
		final UiGroupData group = new UiGroupData();
		final List<CsticData> cstics = createCsticsList();
		final CsticData warningCstic = cstics.get(0);
		warningCstic.setCsticStatus(CsticStatusType.WARNING);

		group.setCstics(cstics);

		final CsticData cstic = updateConfigController.getFirstCsticWithErrorInGroup(group);

		assertNotNull(cstic);
		assertEquals(warningCstic.getKey(), cstic.getKey());
	}

	@Test
	public void testGetFirstCsticWithErrorInGroupWithErrorCsticInSubgroup()
	{
		final UiGroupData group = new UiGroupData();
		final List<CsticData> cstics = createCsticsList();
		group.setCstics(cstics);

		final List<UiGroupData> subGroups = createCsticsGroup();
		final UiGroupData uiSubGroupData = subGroups.get(0);
		uiSubGroupData.setGroupStatus(GroupStatusType.WARNING);
		final CsticData warningCstic = uiSubGroupData.getCstics().get(0);
		warningCstic.setCsticStatus(CsticStatusType.ERROR);

		group.setSubGroups(subGroups);

		final CsticData cstic = updateConfigController.getFirstCsticWithErrorInGroup(group);

		assertNotNull(cstic);
		assertEquals(warningCstic.getKey(), cstic.getKey());
	}

	@Test
	public void testGetFirstCsticWithConflictInGroupWithErrorCstic()
	{
		final UiGroupData group = new UiGroupData();
		final List<CsticData> cstics = createCsticsList();
		final CsticData conflictCstic = cstics.get(0);
		conflictCstic.setCsticStatus(CsticStatusType.CONFLICT);

		group.setCstics(cstics);

		final CsticData cstic = updateConfigController.getFirstCsticWithErrorInGroup(group);

		assertNotNull(cstic);
		assertEquals(conflictCstic.getKey(), cstic.getKey());
	}


	protected FieldError createErrorForSubgoup0Cstic0()
	{
		final FieldError error = new FieldError(SapproductconfigaddonConstants.CONFIG_ATTRIBUTE,
				"groups[0].subGroups[0].cstics[0]", "aaa", false, new String[]
				{ "msg.key" }, null, "error msg");
		return error;
	}

	@Test
	public void countNumberOfUiErrorsPerGroup_null()
	{
		updateConfigController.countNumberOfUiErrorsPerGroup(null);
		//no exception
	}


	@Test
	public void countNumberOfUiErrorsPerGroup_emptyGroup()
	{
		final UiGroupData uiGroup = new UiGroupData();
		updateConfigController.countNumberOfUiErrorsPerGroup(Collections.singletonList(uiGroup));
		//no exception
	}

	@Test
	public void countNumberOfUiErrorsPerGroup_2errors()
	{
		final List<CsticData> cstics = createCsticsList();
		final List<UiGroupData> uiGroups = createEmptyGroup();
		uiGroups.get(0).setCstics(cstics);
		uiGroups.get(0).setNumberErrorCstics(0);

		cstics.get(0).setCsticStatus(CsticStatusType.ERROR);
		cstics.get(1).setCsticStatus(CsticStatusType.WARNING);
		cstics.get(2).setCsticStatus(CsticStatusType.FINISHED);
		cstics.get(3).setCsticStatus(CsticStatusType.CONFLICT);

		updateConfigController.countNumberOfUiErrorsPerGroup(uiGroups);
		assertEquals("Group contains 2 Cstcis with error or warning status ", 2, uiGroups.get(0).getNumberErrorCstics());
	}

	@Test
	public void countNumberOfUiErrorsPerGroup_subGroupsSum()
	{
		final List<CsticData> cstics1 = createCsticsList();
		final List<CsticData> cstics2 = createCsticsList();
		final List<UiGroupData> uiGroups = createEmptyGroup();
		final List<UiGroupData> subGroups1 = createEmptyGroup();
		final List<UiGroupData> subGroups2 = createEmptyGroup();
		subGroups1.get(0).setCstics(cstics1);
		subGroups2.get(0).setCstics(cstics2);
		uiGroups.get(0).setSubGroups(new ArrayList(subGroups1));
		uiGroups.get(0).getSubGroups().addAll(subGroups2);

		cstics1.get(0).setCsticStatus(CsticStatusType.ERROR);
		cstics1.get(1).setCsticStatus(CsticStatusType.WARNING);
		cstics1.get(2).setCsticStatus(CsticStatusType.CONFLICT);
		cstics1.get(3).setCsticStatus(CsticStatusType.FINISHED);

		cstics2.get(0).setCsticStatus(CsticStatusType.ERROR);
		cstics2.get(1).setCsticStatus(CsticStatusType.FINISHED);
		cstics2.get(2).setCsticStatus(CsticStatusType.DEFAULT);

		updateConfigController.countNumberOfUiErrorsPerGroup(uiGroups);
		assertEquals("Group contains 2 Cstcis with error or warning status ", 3, uiGroups.get(0).getNumberErrorCstics());
	}

	@Test
	public void countNumberOfUiErrorsPerGroup_noErrors()
	{
		final List<CsticData> cstics = createCsticsList();
		final List<UiGroupData> uiGroups = createEmptyGroup();
		uiGroups.get(0).setCstics(cstics);
		uiGroups.get(0).setNumberErrorCstics(0);

		cstics.get(0).setCsticStatus(CsticStatusType.DEFAULT);
		cstics.get(1).setCsticStatus(CsticStatusType.FINISHED);
		cstics.get(2).setCsticStatus(CsticStatusType.FINISHED);

		updateConfigController.countNumberOfUiErrorsPerGroup(uiGroups);
		assertEquals("Group cntains 0 Cstcis with error or warning status ", 0, uiGroups.get(0).getNumberErrorCstics());
	}

	@Test
	public void testHandleAutoExpand()
	{
		configData.setAutoExpand(false);
		updateConfigController.handleAutoExpandAndSyncUIStatus(updateData, configData);
		assertNull(configData.getFocusId());
		assertNull(configData.getGroupIdToDisplay());
	}

	@Test
	public void testHandleAutoExpand_noError()
	{
		configData.setAutoExpand(true);
		updateConfigController.handleAutoExpandAndSyncUIStatus(updateData, configData);
		assertNull(configData.getFocusId());
		assertFalse(configData.isAutoExpand());
		assertNull(configData.getGroupIdToDisplay());
	}

	@Test
	public void testHandleAutoExpand_Error()
	{
		configData.setAutoExpand(true);
		configData.getGroups().get(0).getCstics().get(3).setCsticStatus(CsticStatusType.ERROR);
		configData.getGroups().get(0).setGroupStatus(GroupStatusType.ERROR);
		updateConfigController.handleAutoExpandAndSyncUIStatus(updateData, configData);
		assertEquals("root.WCEM_NUMERIC", configData.getFocusId());
		assertEquals("_GEN", configData.getGroupIdToDisplay());
		assertEquals(configData.getGroups().get(0), configData.getGroupToDisplay().getGroup());
		assertTrue(configData.isAutoExpand());

	}

	@Test
	public void testHandleAutoExpand_Conflict()
	{
		configData.setAutoExpand(true);
		configData.getGroups().get(0).getCstics().get(3).setCsticStatus(CsticStatusType.CONFLICT);
		configData.getGroups().get(0).setGroupType(GroupType.CONFLICT);
		configData.getGroups().get(0).setGroupStatus(GroupStatusType.CONFLICT);
		updateConfigController.handleAutoExpandAndSyncUIStatus(updateData, configData);
		assertEquals("conflict.root.WCEM_NUMERIC", configData.getFocusId());
		assertEquals("_GEN", configData.getGroupIdToDisplay());
		assertEquals(configData.getGroups().get(0), configData.getGroupToDisplay().getGroup());
		assertTrue(configData.isAutoExpand());

	}

	@Test
	public void testExpandFirstGroupWithError_null()
	{
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(null);
		assertNull(expandedGroup);
	}

	@Test
	public void testExpandFirstGroupWithError_empty()
	{
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(Collections.EMPTY_LIST);
		assertNull(expandedGroup);
	}

	@Test
	public void testExpandFirstGroupWithError_noError()
	{
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(configData.getGroups());
		assertNull(expandedGroup);
	}

	@Test
	public void testExpandFirstGroupWithError_Warning()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		errorGroup.setGroupStatus(GroupStatusType.WARNING);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(configData.getGroups());
		assertSame(errorGroup, expandedGroup);
		assertFalse(errorGroup.isCollapsed());
		assertFalse(errorGroup.isCollapsedInSpecificationTree());

	}

	@Test
	public void testExpandFirstGroupWithError_Conflict()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		errorGroup.setGroupStatus(GroupStatusType.CONFLICT);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(configData.getGroups());
		assertSame(errorGroup, expandedGroup);
		assertFalse(errorGroup.isCollapsed());
		assertFalse(errorGroup.isCollapsedInSpecificationTree());
	}

	@Test
	public void testExpandFirstGroupWithError_ConflictGroupType()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		errorGroup.setGroupStatus(GroupStatusType.CONFLICT);
		errorGroup.setGroupType(GroupType.CONFLICT);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(configData.getGroups());
		assertNull(expandedGroup);
		assertTrue(errorGroup.isCollapsed());
		assertTrue(errorGroup.isCollapsedInSpecificationTree());
	}

	@Test
	public void testExpandFirstGroupWithError_ErrorInSubGroup()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		final UiGroupData rootGroup = createUiGroup("root", GroupStatusType.ERROR, true);
		rootGroup.setGroupType(GroupType.INSTANCE);
		rootGroup.setSubGroups(configData.getGroups());
		configData.setGroups(Collections.singletonList(rootGroup));
		errorGroup.setGroupStatus(GroupStatusType.ERROR);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithError(configData.getGroups());
		assertSame(errorGroup, expandedGroup);
		assertFalse(errorGroup.isCollapsed());
		assertFalse(errorGroup.isCollapsedInSpecificationTree());
		assertFalse(rootGroup.isCollapsed());
		assertFalse(rootGroup.isCollapsedInSpecificationTree());
	}

	@Test
	public void testExpandFirstGroupWithErrorOrConflict_null()
	{
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithErrorOrConflict(null);
		assertNull(expandedGroup);
	}

	@Test
	public void testExpandFirstGroupWithErrorOrConflict_empty()
	{
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithErrorOrConflict(Collections.EMPTY_LIST);
		assertNull(expandedGroup);
	}

	@Test
	public void testExpandFirstGroupWithErrorOrConflict_noError()
	{
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithErrorOrConflict(configData.getGroups());
		assertNull(expandedGroup);
	}

	@Test
	public void testExpandFirstGroupWithErrorOrConflict_Warning()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		errorGroup.setGroupStatus(GroupStatusType.WARNING);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithErrorOrConflict(configData.getGroups());
		assertSame(errorGroup, expandedGroup);
		assertFalse(errorGroup.isCollapsed());
		assertFalse(errorGroup.isCollapsedInSpecificationTree());

	}

	@Test
	public void testExpandFirstGroupWithErrorOrConflict_Conflict()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		errorGroup.setGroupStatus(GroupStatusType.CONFLICT);
		errorGroup.setGroupType(GroupType.CONFLICT);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithErrorOrConflict(configData.getGroups());
		assertSame(errorGroup, expandedGroup);
		assertFalse(errorGroup.isCollapsed());
		assertFalse(errorGroup.isCollapsedInSpecificationTree());
	}



	@Test
	public void testExpandFirstGroupWithErrorOrConflict_ErrorInSubGroup()
	{
		final UiGroupData errorGroup = configData.getGroups().get(0);
		final UiGroupData rootGroup = createUiGroup("root", GroupStatusType.ERROR, true);
		rootGroup.setGroupType(GroupType.INSTANCE);
		rootGroup.setSubGroups(configData.getGroups());
		configData.setGroups(Collections.singletonList(rootGroup));
		errorGroup.setGroupStatus(GroupStatusType.ERROR);
		errorGroup.setCollapsed(true);
		errorGroup.setCollapsedInSpecificationTree(true);
		final UiGroupData expandedGroup = updateConfigController.expandFirstGroupWithErrorOrConflict(configData.getGroups());
		assertSame(errorGroup, expandedGroup);
		assertFalse(errorGroup.isCollapsed());
		assertFalse(errorGroup.isCollapsedInSpecificationTree());
		assertFalse(rootGroup.isCollapsed());
		assertFalse(rootGroup.isCollapsedInSpecificationTree());
	}

	@Test
	public void testPreviousNextButtonClicked()
	{

		final ConfigurationData myConfigData = createConfigurationDataWith4Groups();

		// Testing next
		// Current group is Group2
		myConfigData.setGroupIdToDisplay("1-" + KB_NAME + ".Group2");
		// Next button has been clicked
		myConfigData.setCpqAction(CPQActionType.NEXT_BTN);
		CPQActionType action = myConfigData.getCpqAction();

		final UpdateDataHolder myUpdateData = new UpdateDataHolder();
		myUpdateData.setUiStatus(new UiStatus());
		myUpdateData.setConfigData(myConfigData);
		myConfigData.setAutoExpand(false);

		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		// identified group should be Group3
		final String identifiedGroupIdNext = "1-" + KB_NAME + ".Group3";
		assertEquals("Next group should be Group3 of instance 1", identifiedGroupIdNext, myUpdateData.getUiStatus()
				.getGroupIdToDisplay());

		// Testing previous
		// Current group is now Group3
		myConfigData.setGroupIdToDisplay(myUpdateData.getUiStatus().getGroupIdToDisplay());
		// Previous button has been clicked
		myConfigData.setCpqAction(CPQActionType.PREV_BTN);
		action = myConfigData.getCpqAction();

		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		// identified group should be Group2
		final String identifiedGroupIdPrevious = "1-" + KB_NAME + ".Group2";
		assertEquals("Next group should be Group3 of instance 1", identifiedGroupIdPrevious, myUpdateData.getUiStatus()
				.getGroupIdToDisplay());


	}

	@Test
	public void testPreviousNextButtonClickedMultiLevel()
	{
		// Remark: To understand the test-data see the comments in AbstractPorductConfigController.createConfigurationDataMultiLevel()
		final ConfigurationData myConfigData = createConfigurationDataMultiLevel();
		final UpdateDataHolder myUpdateData = new UpdateDataHolder();
		myUpdateData.setUiStatus(new UiStatus());
		myUpdateData.setConfigData(myConfigData);
		myConfigData.setAutoExpand(false);

		// Set current group to Group4 of instance 0.1.2 (6-SUBINST-0.1.2.Group4)
		myConfigData.setGroupIdToDisplay("6-SUBINST-0.1.2.Group4");
		// Test next: it should be Group1 of instance 0.2 (3-SUBINST-0.2.Group1)
		myConfigData.setCpqAction(CPQActionType.NEXT_BTN);
		CPQActionType action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		String identifiedGroupId = "3-SUBINST-0.2.Group1";
		assertEquals("Next group should be Group1 of instance 0.2 (3-SUBINST-0.2.Group1)", identifiedGroupId, myUpdateData
				.getUiStatus().getGroupIdToDisplay());

		// Set current group to Group1 of instance 0.2 (3-SUBINST-0.2.Group1)
		myConfigData.setGroupIdToDisplay(myUpdateData.getUiStatus().getGroupIdToDisplay());
		// Test previous: it should be Group4 of instance 0.1.2 (6-SUBINST-0.1.2.Group4)
		myConfigData.setCpqAction(CPQActionType.PREV_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		identifiedGroupId = "6-SUBINST-0.1.2.Group4";
		assertEquals("Previous group should be Group4 of instance 0.1.2 (6-SUBINST-0.1.2.Group4)", identifiedGroupId, myUpdateData
				.getUiStatus().getGroupIdToDisplay());

		// Set current group to Group3 of instance 0.1 (2-SUBINST-0.1.Group3)
		myConfigData.setGroupIdToDisplay("2-SUBINST-0.1.Group3");
		// Test next: it should be Group4 of instance 0.1 (2-SUBINST-0.1.Group4)
		myConfigData.setCpqAction(CPQActionType.NEXT_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		identifiedGroupId = "2-SUBINST-0.1.Group4";
		assertEquals("Next group should be Group4 of instance 0.1 (2-SUBINST-0.1.Group4)", identifiedGroupId, myUpdateData
				.getUiStatus().getGroupIdToDisplay());

		// Set current group to Group4 of instance 0.1 (2-SUBINST-0.1.Group4)
		myConfigData.setGroupIdToDisplay(myUpdateData.getUiStatus().getGroupIdToDisplay());
		// Test next: it should be Group1 of instance 0.1.2 (6-SUBINST-0.1.2.Group1)
		myConfigData.setCpqAction(CPQActionType.NEXT_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		identifiedGroupId = "6-SUBINST-0.1.2.Group1";
		assertEquals("Next group should be Group1 of instance 0.1.2 (6-SUBINST-0.1.2.Group1)", identifiedGroupId, myUpdateData
				.getUiStatus().getGroupIdToDisplay());

		// Set current group to Group1 of instance 0.1.2 (6-SUBINST-0.1.2.Group1)
		myConfigData.setGroupIdToDisplay(myUpdateData.getUiStatus().getGroupIdToDisplay());
		// Test previous: it should be Group4 of instance 0.1 (2-SUBINST-0.1.Group4)
		myConfigData.setCpqAction(CPQActionType.PREV_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		identifiedGroupId = "2-SUBINST-0.1.Group4";
		assertEquals("Previous group should be Group4 of instance 0.1 (2-SUBINST-0.1.Group4)", identifiedGroupId, myUpdateData
				.getUiStatus().getGroupIdToDisplay());

		// Should never happen: clicking next on last group (because button should be disabled)
		// In this case the groupIdToDisplay should stay the same
		// Set current group to Group4 of instance 0.2 (3-SUBINST-0.2.Group4)
		myConfigData.setGroupIdToDisplay("3-SUBINST-0.2.Group4");
		// Test next: it should stay the same group
		myConfigData.setCpqAction(CPQActionType.NEXT_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		identifiedGroupId = "3-SUBINST-0.2.Group4";
		assertEquals("Clicking Next on last group: it should stay on the same group (3-SUBINST-0.2.Group4)", identifiedGroupId,
				myUpdateData.getUiStatus().getGroupIdToDisplay());

		// Should never happen: clicking previous on first group (because button should be disabled)
		// In this case the groupIdToDisplay should stay the same
		// Set current group to Group1 of root-instance 0 (1-YSAP_SIMPLE_POC.Group1)
		myConfigData.setGroupIdToDisplay("1-YSAP_SIMPLE_POC.Group1");
		// Test previous: it should stay the same group
		myConfigData.setCpqAction(CPQActionType.PREV_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		identifiedGroupId = "1-YSAP_SIMPLE_POC.Group1";
		assertEquals("Clicking Previous on first group: it should stay on the same group (1-YSAP_SIMPLE_POC.Group1)",
				identifiedGroupId, myUpdateData.getUiStatus().getGroupIdToDisplay());
	}


	@Test
	public void testPreviousNextButtonClickedOnlyOneGroup()
	{
		final ConfigurationData myConfigData = createConfigurationDataWithGeneralGroupOnly();
		// Mark as ONLYONE
		myConfigData.getGroups().get(0).setFirstOrLastGroup(FirstOrLastGroupType.ONLYONE);

		final UpdateDataHolder myUpdateData = new UpdateDataHolder();
		myUpdateData.setUiStatus(new UiStatus());
		myUpdateData.setConfigData(myConfigData);
		myConfigData.setAutoExpand(false);

		// Should never happen: clicking next on only group (because button should be disabled)
		// In this case the groupIdToDisplay should stay the same

		// Set current group
		myConfigData.setGroupIdToDisplay("_GEN");
		// Test next: it should stay the same group
		myConfigData.setCpqAction(CPQActionType.NEXT_BTN);
		CPQActionType action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		final String identifiedGroupId = "_GEN";
		assertEquals("Clicking Next on only group: it should stay on the same group", identifiedGroupId, myUpdateData.getUiStatus()
				.getGroupIdToDisplay());

		// Should never happen: clicking previous on only group (because button should be disabled)
		// In this case the groupIdToDisplay should stay the same
		// Test previous: it should stay the same group
		myConfigData.setCpqAction(CPQActionType.PREV_BTN);
		action = myConfigData.getCpqAction();
		updateConfigController.identifyPrevNextGroup(action, myUpdateData);
		assertEquals("Clicking Previous on only group: it should stay on the same group", identifiedGroupId, myUpdateData
				.getUiStatus().getGroupIdToDisplay());

	}


	@Test
	public void testHandleConflictSolverMessage_allSolved() throws Exception
	{
		final int oldNumberOfConflicts = 1;
		final UiStatus uiStatus = updateData.getUiStatus();
		uiStatus.setNumberOfConflictsToDisplay(oldNumberOfConflicts);

		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(0, newNumberOfConflicts);
		updateConfigController.handleConflictSolverMessage(updateData.getUiStatus(), configData, model);

		Mockito.verify(model).addAttribute(Mockito.eq(GlobalMessages.CONF_MESSAGES_HOLDER), Mockito.any(Collection.class));

	}

	@Test
	public void testHandleConflictSolverMessage_firstConflict() throws Exception
	{
		final int oldNumberOfConflicts = 0;
		final UiStatus uiStatus = updateData.getUiStatus();
		uiStatus.setNumberOfConflictsToDisplay(oldNumberOfConflicts);

		final UiGroupData uiConflictData = createUiConflictGroupsWOCstics(new String[]
		{ "Conflict1" });
		configData.getGroups().add(uiConflictData);
		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(1, newNumberOfConflicts);
		updateConfigController.handleConflictSolverMessage(updateData.getUiStatus(), configData, model);

		Mockito.verify(model).addAttribute(Mockito.eq(GlobalMessages.INFO_MESSAGES_HOLDER), Mockito.any(Collection.class));
	}

	@Test
	public void testHandleConflictSolverMessage_notSolved() throws Exception
	{
		final int oldNumberOfConflicts = 1;
		final UiStatus uiStatus = updateData.getUiStatus();
		uiStatus.setNumberOfConflictsToDisplay(oldNumberOfConflicts);

		final UiGroupData uiConflictData = createUiConflictGroupsWOCstics(new String[]
		{ "Conflict1" });
		configData.getGroups().add(uiConflictData);
		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(1, newNumberOfConflicts);
		updateConfigController.handleConflictSolverMessage(updateData.getUiStatus(), configData, model);

		Mockito.verify(model).addAttribute(Mockito.eq(GlobalMessages.INFO_MESSAGES_HOLDER), Mockito.any(Collection.class));

	}


	@Test
	public void testHandleConflictSolverMessage_noUiStatus() throws Exception
	{
		final UiGroupData uiConflictData = createUiConflictGroupsWOCstics(new String[]
		{ "Conflict1" });
		configData.getGroups().add(uiConflictData);
		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(1, newNumberOfConflicts);
		updateConfigController.handleConflictSolverMessage(null, configData, model);

		Mockito.verify(model).addAttribute(Mockito.eq(GlobalMessages.INFO_MESSAGES_HOLDER), Mockito.any(Collection.class));

	}

	@Test
	public void testHandleConflictSolverMessage_notSolved4Old() throws Exception
	{
		final int oldNumberOfConflicts = 4;
		final UiStatus uiStatus = updateData.getUiStatus();
		uiStatus.setNumberOfConflictsToDisplay(oldNumberOfConflicts);

		final UiGroupData uiConflictData = createUiConflictGroupsWOCstics(new String[]
		{ "Conflict1" });
		configData.getGroups().add(uiConflictData);
		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(1, newNumberOfConflicts);

		final Model model = new ExtendedModelMap();
		final Model spy = spy(model);
		updateConfigController.handleConflictSolverMessage(updateData.getUiStatus(), configData, spy);

		Mockito.verify(spy).addAttribute(Mockito.eq(GlobalMessages.CONF_MESSAGES_HOLDER), Mockito.any(Collection.class));
		final Set<Entry<String, Object>> entries = spy.asMap().entrySet();
		assertEquals(1, entries.size());

		final Entry<String, Object> entry = entries.iterator().next();
		assertEquals(GlobalMessages.CONF_MESSAGES_HOLDER, entry.getKey());

		final List<GlobalMessage> messages = (List<GlobalMessage>) entry.getValue();

		final GlobalMessage message = messages.get(0);
		assertEquals("sapproductconfig.conflict.messages.resolved", message.getCode());
		assertFalse(message.getAttributes().isEmpty());
		assertEquals(Integer.valueOf(3), message.getAttributes().iterator().next());
	}

	@Test
	public void testHandleConflictSolverMessage_newConflict() throws Exception
	{
		final int oldNumberOfConflicts = 1;
		final UiStatus uiStatus = updateData.getUiStatus();
		uiStatus.setNumberOfConflictsToDisplay(oldNumberOfConflicts);

		final UiGroupData uiConflictData = createUiConflictGroupsWOCstics(new String[]
		{ "Conflict1", "Conflict2" });
		configData.getGroups().add(uiConflictData);
		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(2, newNumberOfConflicts);
		updateConfigController.handleConflictSolverMessage(updateData.getUiStatus(), configData, model);

		Mockito.verify(model).addAttribute(Mockito.eq(GlobalMessages.INFO_MESSAGES_HOLDER), Mockito.any(Collection.class));

	}

	@Test
	public void testHandleConflictSolverMessage_oneSolved() throws Exception
	{
		final int oldNumberOfConflicts = 2;
		final UiStatus uiStatus = updateData.getUiStatus();
		uiStatus.setNumberOfConflictsToDisplay(oldNumberOfConflicts);

		final UiGroupData uiConflictData = createUiConflictGroupsWOCstics(new String[]
		{ "Conflict1" });
		configData.getGroups().add(uiConflictData);
		final int newNumberOfConflicts = uiStatusSync.getNumberOfConflicts(configData);
		assertEquals(1, newNumberOfConflicts);
		updateConfigController.handleConflictSolverMessage(updateData.getUiStatus(), configData, model);

		Mockito.verify(model).addAttribute(Mockito.eq(GlobalMessages.CONF_MESSAGES_HOLDER), Mockito.any(Collection.class));

	}


	@Test
	public void testPrepareGroupIdToDisplayWhenSolvingConflicts()
	{
		final CPQActionType action = CPQActionType.VALUE_CHANGED;

		updateData.getUiStatus().setGroupIdToDisplay("GroupId");
		updateConfigController.handleGroupIdToDisplayWhenSolvingConflicts(action, updateData);
		assertEquals("GroupId", updateData.getUiStatus().getGroupIdToDisplay());

		updateData.getUiStatus().setGroupIdToDisplay("CONFLICTGroupId");
		updateConfigController.handleGroupIdToDisplayWhenSolvingConflicts(action, updateData);
		assertEquals("_GEN", updateData.getUiStatus().getGroupIdToDisplay());
	}

	@Test
	public void testDonotPrepareGroupIdToDisplayWhenSolvingConflicts()
	{
		CPQActionType action = CPQActionType.MENU_NAVIGATION;
		updateData.getUiStatus().setGroupIdToDisplay("COFLICT_CART_TYPE");
		updateConfigController.handleGroupIdToDisplayWhenSolvingConflicts(action, updateData);
		assertNull("groupIdToDisplay is 'CONFLICT_CART_TYPE': ", updateData.getConfigData().getGroupIdToDisplay());

		action = CPQActionType.VALUE_CHANGED;
		updateData.getUiStatus().setGroupIdToDisplay("CONFLICT_GroupId");
		updateConfigController.handleGroupIdToDisplayWhenSolvingConflicts(action, updateData);
		assertEquals("_GEN", updateData.getUiStatus().getGroupIdToDisplay());

		action = CPQActionType.SHOW_FULL_LONG_TEXT;
		updateData.getUiStatus().setGroupIdToDisplay("WCEM_USED_SCENARIOS");
		updateConfigController.handleGroupIdToDisplayWhenSolvingConflicts(action, updateData);
		assertNull("groupIdToDisplay is 'WCEM_USED_SCENARIOS': ", updateData.getConfigData().getGroupIdToDisplay());

		action = CPQActionType.HIDE_FULL_LONG_TEXT;
		updateData.getUiStatus().setGroupIdToDisplay("CONFLICT_WCEM_RELEASE");
		updateConfigController.handleGroupIdToDisplayWhenSolvingConflicts(action, updateData);
		assertNull("groupIdToDisplay is 'CONFLICT_WCEM_RELEASE': ", updateData.getConfigData().getGroupIdToDisplay());
	}

	@Test
	public void testIsFirstErrorCurrentFocusCsticTrue()
	{
		final String focusId = "errorCstic";
		final String firstErrorCsticId = "errorCstic";
		assertTrue(updateConfigController.isFirstErrorCurrentFocusCstic(firstErrorCsticId, focusId));
	}

	@Test
	public void testIsFirstErrorCurrentFocusCsticFalse()
	{
		final String focusId = "notErrorCstic";
		final String firstErrorCsticId = "errorCstic";
		assertFalse(updateConfigController.isFirstErrorCurrentFocusCstic(firstErrorCsticId, focusId));
	}

	@Test
	public void testIsFirstErrorCurrentFocusCsticTrueConflict()
	{
		final String focusId = "errorCstic";
		final String firstErrorCsticId = "conflict.errorCstic";
		assertTrue(updateConfigController.isFirstErrorCurrentFocusCstic(firstErrorCsticId, focusId));
	}

	@Test
	public void testIsFirstErrorCurrentFocusCsticFalseConflict()
	{
		final String focusId = "notErrorCstic";
		final String firstErrorCsticId = "conflict.errorCstic";
		assertFalse(updateConfigController.isFirstErrorCurrentFocusCstic(firstErrorCsticId, focusId));
	}

}
