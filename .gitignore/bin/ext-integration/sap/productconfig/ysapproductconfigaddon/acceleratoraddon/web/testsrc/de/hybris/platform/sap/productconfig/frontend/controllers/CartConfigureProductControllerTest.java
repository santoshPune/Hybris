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
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.frontend.UiStatus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


@UnitTest
public class CartConfigureProductControllerTest extends AbstractProductConfigControllerTest
{
	@InjectMocks
	private final CartConfigureProductController cartConfigController = new CartConfigureProductController();
	@InjectMocks
	private final AbstractProductConfigController updateConfigController = new UpdateConfigureProductController();


	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);


		kbKey = createKbKey();
		csticList = createCsticsList();
		configData = createConfigurationDataWithGeneralGroupOnly();
	}

	@Test
	public void testGetConfigForRestoredProduct()
	{
		final ConfigurationData configData = cartConfigController.getConfigDataForRestoredProduct(kbKey, "id", null);
		Assert.assertNull(configData);
	}

	@Test
	public void testGetConfigForRestoredProductWithNoConfiguration() throws Exception
	{
		initializeFirstCall();
		final ConfigurationData configData = cartConfigController.getConfigDataForRestoredProduct(kbKey, null, null);
		Assert.assertNotNull(configData);
	}

	@Test
	public void testUiStatusFromSessionInCaseOfRestore() throws Exception
	{
		initializeFirstCall();
		given(sessionAccessFacade.getConfigIdForCartEntry("TR")).willReturn("configId");
		final UiStatus stat = cartConfigController.getUiStatusFromSession("TR", kbKey);
		Assert.assertNotNull(stat);
		//assertEquals("confId", stat.getConfigId());
	}

	@Test
	public void testUiStatusFromSession() throws Exception
	{
		initializeFirstCall();

		final UiStatus uiStatus = createUiStatus("1");
		given(sessionAccessFacade.getUiStatusForCartEntry("IT")).willReturn(uiStatus);

		final UiStatus stat = cartConfigController.getUiStatusFromSession("IT", null);
		assertEquals(uiStatus, stat);
	}
}
