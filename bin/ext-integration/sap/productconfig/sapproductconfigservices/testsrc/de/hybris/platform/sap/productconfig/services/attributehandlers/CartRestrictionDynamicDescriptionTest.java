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
package de.hybris.platform.sap.productconfig.services.attributehandlers;

import static org.junit.Assert.assertFalse;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.sap.productconfig.services.model.CMSCartConfigurationRestrictionModel;

import org.junit.Test;


/**
 * Tests: CartRestrictionDynamicDescription
 */
@IntegrationTest
public class CartRestrictionDynamicDescriptionTest //extends ServicelayerTest
{
	CartRestrictionDynamicDescription classUnderTest = new CartRestrictionDynamicDescription();

	/**
	 * Testing description
	 */
	@Test
	public void testDescription()
	{
		final CMSCartConfigurationRestrictionModel restrictionModel = new CMSCartConfigurationRestrictionModel();
		Registry.setCurrentTenantByID("junit");
		assertFalse("We expect a non empty description", classUnderTest.get(restrictionModel).isEmpty());
	}

	/**
	 * Test set (which will raise an exception)
	 */
	@Test(expected = UnsupportedOperationException.class)
	public void testSet()
	{
		final CMSCartConfigurationRestrictionModel restrictionModel = new CMSCartConfigurationRestrictionModel();
		classUnderTest.set(restrictionModel, "");
	}

}
