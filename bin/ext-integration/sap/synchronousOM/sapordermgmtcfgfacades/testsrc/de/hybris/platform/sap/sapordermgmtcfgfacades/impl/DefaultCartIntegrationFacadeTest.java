/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.sapordermgmtcfgfacades.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.sap.productconfig.facades.ConfigurationCartIntegrationFacade;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.sap.sapordermgmtb2bfacades.cart.CartRestorationFacade;
import de.hybris.platform.sap.sapordermgmtservices.BackendAvailabilityService;
import de.hybris.platform.sap.sapordermgmtservices.cart.CartService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



@UnitTest
public class DefaultCartIntegrationFacadeTest
{
	@Mock
	ProductConfigurationService productConfigurationService;
	@Mock
	CartService cartService;
	@Mock
	ProductService productService;
	@Mock
	BackendAvailabilityService backendAvailabilityService;
	@Mock
	ConfigurationCartIntegrationFacade configurationCartIntegrationFacade;
	@Mock
	SessionAccessService sessionAccessService;
	@Mock
	CartRestorationFacade cartRestorationFacade;
	@Mock
	ConfigurationData configurationData;

	DefaultCartIntegrationFacade defaultCartIntegrationFacade;

	private static final String ITEM_KEY = "123";

	@Before
	public void setUp()
	{

		MockitoAnnotations.initMocks(this);

		defaultCartIntegrationFacade = new DefaultCartIntegrationFacade()
		{

			@Override
			protected boolean isSapOrderMgmtEnabled()
			{

				return true;
			}
		};

		defaultCartIntegrationFacade.setSessionAccessService(sessionAccessService);
		defaultCartIntegrationFacade.setBackendAvailabilityService(backendAvailabilityService);
		defaultCartIntegrationFacade.setProductConfigDefaultCartIntegrationFacade(configurationCartIntegrationFacade);
		defaultCartIntegrationFacade.setProductConfigurationService(productConfigurationService);
		defaultCartIntegrationFacade.setCartService(cartService);
		defaultCartIntegrationFacade.setCartRestorationFacade(cartRestorationFacade);
		defaultCartIntegrationFacade.setBackendAvailabilityService(backendAvailabilityService);
	}

	/**
	 * Expect that the asynchronous facade is called
	 *
	 * @throws CommerceCartModificationException
	 */
	@Test
	public void testAddToCartNoBackend() throws CommerceCartModificationException
	{
		given(Boolean.valueOf(backendAvailabilityService.isBackendDown())).willReturn(Boolean.TRUE);
		given(configurationCartIntegrationFacade.addConfigurationToCart(configurationData)).willReturn(ITEM_KEY);


		final String key = defaultCartIntegrationFacade.addConfigurationToCart(configurationData);

		Assert.assertEquals(key, ITEM_KEY);
	}


	/**
	 * Expect that both configuration integration facades are called
	 *
	 * @throws CommerceCartModificationException
	 */
	@Test
	public void testAddToCartBackendAvailable() throws CommerceCartModificationException
	{

		given(cartService.addConfigurationToCart(null)).willReturn(ITEM_KEY);

		final String key = defaultCartIntegrationFacade.addConfigurationToCart(configurationData);

		assertEquals(key, ITEM_KEY);
	}

	/**
	 * Expect that an update and and add is done
	 *
	 * @throws CommerceCartModificationException
	 */
	@Test
	public void testAddToCartUpdateConfigurationAndBackendAvailable() throws CommerceCartModificationException
	{

		given(configurationData.getCartItemPK()).willReturn("PK");
		given(Boolean.valueOf(cartService.isItemAvailable("PK"))).willReturn(Boolean.TRUE);

		final String key = defaultCartIntegrationFacade.addConfigurationToCart(configurationData);

		assertEquals(key, "PK");
	}

}
