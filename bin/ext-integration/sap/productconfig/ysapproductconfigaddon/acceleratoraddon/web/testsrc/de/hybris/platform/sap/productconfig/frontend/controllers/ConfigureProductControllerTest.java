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
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorservices.data.RequestContextData;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;


@UnitTest
public class ConfigureProductControllerTest extends AbstractProductConfigControllerTest
{

	@Mock
	protected BindingResult bindingResults;
	@Mock
	protected ConfigurationData configData2;

	@InjectMocks
	private final ConfigureProductController configController = new ConfigureProductController();

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);


		kbKey = createKbKey();
		csticList = createCsticsList();
		configData = createConfigurationDataWithGeneralGroupOnly();
	}



	@Test
	public void testUpdateConfigureProductRedirectIsCorrect() throws Exception
	{
		initializeFirstCall();

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		final String forward = configController.configureProduct(PRODUCT_CODE, model, request);
		assertEquals("addon:/ysapproductconfigaddon/pages/configuration/configurationPage", forward);
	}

	@Test
	public void testBreadcrumbIsSet() throws Exception
	{
		initializeFirstCall();

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		configController.configureProduct(PRODUCT_CODE, model, request);
		Mockito.verify(model).addAttribute(Mockito.eq(WebConstants.BREADCRUMBS_KEY), Mockito.any(List.class));
	}

	@Test
	public void testProductDataIsSet() throws Exception
	{
		initializeFirstCall();

		request.setAttribute("de.hybris.platform.acceleratorcms.utils.SpringHelper.bean.requestContextData",
				new RequestContextData());
		configController.configureProduct(PRODUCT_CODE, model, request);
		Mockito.verify(model).addAttribute(Mockito.eq("product"), Mockito.any(ProductData.class));
	}

	@Test
	public void testCreateKBKey() throws Exception
	{
		when(productModel.getCode()).thenReturn(PRODUCT_CODE);

		final KBKeyData actualKBKey = configController.createKBKeyForProduct(productModel);

		assertEquals("Must be equals", kbKey.getProductCode(), actualKBKey.getProductCode());
		assertEquals("Must be equals", kbKey.getKbName(), actualKBKey.getKbName());
		assertEquals("Must be equals", kbKey.getKbVersion(), actualKBKey.getKbVersion());
		assertEquals("Must be equals", kbKey.getKbLogsys(), actualKBKey.getKbLogsys());
	}

	@Test
	public void testRemoveNullCStics_InGroup()
	{
		final String name = "XYZ";
		final List<CsticData> dirtyList = createDirtyListWithCstic(name);

		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData group = new UiGroupData();
		group.setCstics(dirtyList);
		groups.add(group);
		assertTrue("Must be 2 groups", groups.get(0).getCstics().size() > 1);

		configController.removeNullCstics(groups);
		assertEquals(1, groups.get(0).getCstics().size());
		Assert.assertEquals("Wrong cstic found", name, groups.get(0).getCstics().get(0).getName());
	}



	@Test
	public void testRemoveNullCStics_retractionTriggered()
	{
		final String name = "XYZ";
		final List<CsticData> dirtyList = createDirtyListWithCstic(name);
		dirtyList.get(0).setRetractTriggered(true);

		final List<UiGroupData> groups = new ArrayList<>();
		final UiGroupData group = new UiGroupData();
		group.setCstics(dirtyList);
		groups.add(group);
		assertTrue("Must be 2 groups", groups.get(0).getCstics().size() > 1);

		configController.removeNullCstics(groups);
		assertEquals(2, groups.get(0).getCstics().size());
		Assert.assertEquals("Wrong cstic found", name, groups.get(0).getCstics().get(1).getName());
		Assert.assertEquals("Wrong cstic found", "readOnly", groups.get(0).getCstics().get(0).getName());
	}

	protected List<CsticData> createDirtyListWithCstic(final String name)
	{
		final CsticData csticWithNameAndValue = new CsticData();
		csticWithNameAndValue.setName(name);
		csticWithNameAndValue.setValue("value");
		csticWithNameAndValue.setType(UiType.STRING);
		final CsticData readOnly = new CsticData();
		readOnly.setName("readOnly");
		readOnly.setType(UiType.READ_ONLY);
		final List<CsticData> dirtyList = new ArrayList<>();
		dirtyList.add(readOnly);
		dirtyList.add(csticWithNameAndValue);
		dirtyList.add(new CsticData());
		return dirtyList;
	}

	@Test
	public void testRemoveNullCStics_InSubGroup()
	{
		final String name = "XYZ";
		final List<CsticData> dirtyList = createDirtyListWithCstic(name);

		final List<UiGroupData> subGroups = new ArrayList<>();
		UiGroupData group = new UiGroupData();
		group.setCstics(dirtyList);
		subGroups.add(group);

		final ArrayList<UiGroupData> groups = new ArrayList<>();
		group = new UiGroupData();
		group.setSubGroups(subGroups);
		groups.add(group);

		assertTrue("Must be 2 groups", groups.get(0).getSubGroups().get(0).getCstics().size() > 1);

		configController.removeNullCstics(groups);

		final UiGroupData uiGroup = groups.get(0).getSubGroups().get(0);
		assertEquals(1, uiGroup.getCstics().size());
		Assert.assertSame(name, uiGroup.getCstics().get(0).getName());
	}

	@Test
	public void testLoggingOfConflictCheckDeviation() throws Exception
	{
		final KBKeyData kbKey = new KBKeyData();
		kbKey.setProductCode("MY_PRODUCT");
		given(configData2.getKbKey()).willReturn(kbKey);

		// Test 1: Configuration is consistent and complete, no errors are returned from ConflictChecker -> No Logging.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.FALSE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(0)).getKbKey();

		// Test 2: Configuration is incomplete, errors are returned from ConflictChecker -> No Logging.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.TRUE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(0)).getKbKey();

		// Test 3: Configuration is inconsistent, errors are returned from ConflictChecker -> No Logging.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.TRUE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(0)).getKbKey();

		// Test 4: Configuration is inconsistent and incomplete, errors are returned from ConflictChecker -> No Logging.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.TRUE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(0)).getKbKey();

		// Test 5: Configuration is incomplete, but no errors are returned from ConflictChecker -> Logging first time.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.FALSE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(1)).getKbKey();

		// Test 6: Configuration is inconsistent, but no errors are returned from ConflictChecker -> Logging second time.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.TRUE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.FALSE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(2)).getKbKey();

		// Test 7: Configuration is inconsistent and incomplete, but no errors are returned from ConflictChecker -> Logging third time.
		given(Boolean.valueOf(configData2.isComplete())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(configData2.isConsistent())).willReturn(Boolean.FALSE);
		given(Boolean.valueOf(bindingResults.hasErrors())).willReturn(Boolean.FALSE);
		configController.logConfigurationCheckDeviation(bindingResults, configData2);
		// getKbKey() is only called if a log-statement is written
		verify(configData2, times(3)).getKbKey();

	}

}
