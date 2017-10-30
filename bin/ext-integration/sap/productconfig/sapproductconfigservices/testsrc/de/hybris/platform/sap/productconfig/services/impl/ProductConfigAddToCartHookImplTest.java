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
package de.hybris.platform.sap.productconfig.services.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ProductConfigAddToCartHookImplTest
{

	private static final String SAP_CONFIGURABLE = "sapConfigurable";
	private ProductConfigAddToCartHookImpl classUnderTest;
	private CommerceCartParameter parameters;

	@Mock
	private ProductModel mockedProduct;
	@Mock
	private ModelService mockedModelService;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		classUnderTest = new ProductConfigAddToCartHookImpl();
		classUnderTest.setModelService(mockedModelService);
		classUnderTest.setConfigurableSource(SAP_CONFIGURABLE);


		parameters = new CommerceCartParameter();
		parameters.setProduct(mockedProduct);
	}

	@Test
	public void test_beforeAddToCart_noConfig_noNewEntry() throws Exception
	{
		parameters.setCreateNewEntry(false);
		Mockito.when(mockedModelService.getAttributeValue(mockedProduct, SAP_CONFIGURABLE)).thenReturn(Boolean.FALSE);
		classUnderTest.beforeAddToCart(parameters);
		assertFalse("createNewEntry changed for non-configurable product", parameters.isCreateNewEntry());
	}

	@Test
	public void test_beforeAddToCart_noConfig_newEntry() throws Exception
	{
		parameters.setCreateNewEntry(true);
		Mockito.when(mockedModelService.getAttributeValue(mockedProduct, SAP_CONFIGURABLE)).thenReturn(Boolean.FALSE);
		classUnderTest.beforeAddToCart(parameters);
		assertTrue("createNewEntry changed for non-configurable product", parameters.isCreateNewEntry());
	}

	@Test
	public void test_beforeAddToCart_config_newEntry() throws Exception
	{
		parameters.setCreateNewEntry(true);
		Mockito.when(mockedModelService.getAttributeValue(mockedProduct, SAP_CONFIGURABLE)).thenReturn(Boolean.TRUE);
		classUnderTest.beforeAddToCart(parameters);
		assertTrue("createNewEntry should be always true for configurable product", parameters.isCreateNewEntry());
	}

	@Test
	public void test_beforeAddToCart_config_noNewEntry() throws Exception
	{
		parameters.setCreateNewEntry(false);
		Mockito.when(mockedModelService.getAttributeValue(mockedProduct, SAP_CONFIGURABLE)).thenReturn(Boolean.TRUE);
		classUnderTest.beforeAddToCart(parameters);
		assertTrue("createNewEntry should be always true for configurable product", parameters.isCreateNewEntry());
	}

	@Test
	public void test_afterAddToCart_executable() throws Exception
	{
		classUnderTest.afterAddToCart(null, null);
	}

}
