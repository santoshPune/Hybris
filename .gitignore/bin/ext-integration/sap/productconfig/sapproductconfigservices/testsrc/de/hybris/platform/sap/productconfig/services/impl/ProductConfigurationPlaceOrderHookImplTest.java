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

import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;



/**
 * Tests: ProductConfigurationPlaceOrderHookImpl
 *
 */
@UnitTest
public class ProductConfigurationPlaceOrderHookImplTest
{
	ProductConfigurationPlaceOrderHookImpl classUnderTest = new ProductConfigurationPlaceOrderHookImpl();
	@Mock
	ProductConfigurationService productConfigurationService;
	@Mock
	CommerceCheckoutParameter parameter;
	@Mock
	private CommerceOrderResult orderModel;

	@Mock
	SessionAccessService sessionAccessService;

	private CartModel cartModel;
	private List<AbstractOrderEntryModel> entries;

	@Mock
	private AbstractOrderEntryModel entry1;

	@Mock
	private AbstractOrderEntryModel entry2;

	@Mock
	private ProductModel product1;

	@Mock
	private ProductModel product2;


	/**
	 * Setup method
	 */
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(entry1.getEntryNumber()).thenReturn(Integer.valueOf(1));
		Mockito.when(entry1.getPk()).thenReturn(PK.fromLong(1));
		Mockito.when(entry1.getProduct()).thenReturn(product1);

		Mockito.when(entry2.getEntryNumber()).thenReturn(Integer.valueOf(2));
		Mockito.when(entry2.getPk()).thenReturn(PK.fromLong(2));
		Mockito.when(entry2.getProduct()).thenReturn(product2);

		Mockito.when(sessionAccessService.getConfigIdForCartEntry("1")).thenReturn("A");
		classUnderTest.setProductConfigurationService(productConfigurationService);
		classUnderTest.setSessionAccessService(sessionAccessService);
	}

	/**
	 * Access to productConfigurationService
	 */
	@Test
	public void testConfigService()
	{
		assertNotNull(classUnderTest.getProductConfigurationService());
	}

	@Test
	public void testAfterPlaceOrderNoConfigurables() throws InvalidCartException
	{

		final String configId = prepareEntryList(false);

		entries.add(entry2);

		classUnderTest.afterPlaceOrder(parameter, orderModel);

		Mockito.verify(productConfigurationService, Mockito.times(0)).releaseSession(configId);

	}

	@Test
	public void testAfterPlaceOrder() throws InvalidCartException
	{
		final String configId = prepareEntryList(true);
		classUnderTest.afterPlaceOrder(parameter, orderModel);

		Mockito.verify(productConfigurationService, Mockito.times(1)).releaseSession(configId);

	}

	private String prepareEntryList(final boolean withCfg)
	{
		cartModel = new CartModel();
		entries = new ArrayList();
		String configId = "";
		if (withCfg)
		{
			configId = "A";
		}
		entries.add(entry1);
		cartModel.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cartModel);
		return configId;
	}
}
