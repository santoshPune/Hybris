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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
/**
 * Unit tests for {@link SessionAccessServiceImpl}
 */
public class SessionAccessServiceImplTest
{
	SessionAccessServiceImpl classUnderTest = new SessionAccessServiceImpl();

	@Mock
	SessionService sessionService;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setSessionService(sessionService);
	}

	@Test
	public void testSessionService()
	{
		assertNotNull(classUnderTest.getSessionService());
	}

	@Test
	public void testCartEntryConfigId()
	{
		final String configId = "1";
		final String cartEntryKey = "X";
		final Map<String, String> configs = new HashMap<>();
		configs.put(cartEntryKey, configId);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS)).thenReturn(
				configs);
		classUnderTest.setConfigIdForCartEntry(cartEntryKey, configId);
		assertEquals(configId, classUnderTest.getConfigIdForCartEntry(cartEntryKey));
	}

	@Test
	public void testUIStatus()
	{
		final String cartEntryKey = "X";
		final Object status = "S";
		final Map<String, Object> statuses = new HashMap<>();
		statuses.put(cartEntryKey, status);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_CART_ENTRY_UISTATUSES)).thenReturn(statuses);
		classUnderTest.setUiStatusForCartEntry(cartEntryKey, status);
		assertEquals(status, classUnderTest.getUiStatusForCartEntry(cartEntryKey));
		statuses.remove(cartEntryKey);
		classUnderTest.removeUiStatusForCartEntry(cartEntryKey);
		assertNull(classUnderTest.getUiStatusForCartEntry(cartEntryKey));
	}


	@Test
	public void testUIStatusProduct()
	{
		final String productKey = "X";
		final Object status = "S";
		final Map<String, Object> statuses = new HashMap<>();
		statuses.put(productKey, status);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_PRODUCT_UISTATUSES)).thenReturn(statuses);
		classUnderTest.setUiStatusForProduct(productKey, status);
		assertEquals(status, classUnderTest.getUiStatusForProduct(productKey));
		statuses.remove(productKey);
		classUnderTest.removeUiStatusForProduct(productKey);
		assertNull(classUnderTest.getUiStatusForProduct(productKey));
	}

	@Test
	public void testConfigIdForCartEntry()
	{
		final String configId = "1";
		final String cartEntryKey = "X";
		final Map<String, String> configs = new HashMap<>();
		configs.put(cartEntryKey, configId);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS)).thenReturn(
				configs);
		classUnderTest.setConfigIdForCartEntry(cartEntryKey, configId);
		assertEquals(cartEntryKey, classUnderTest.getCartEntryForConfigId(configId));
	}

	@Test
	public void testRemoveConfigIdForCartEntry()
	{
		final String configId = "1";
		final String cartEntryKey = "X";
		final Map<String, String> configs = new HashMap<>();
		configs.put(cartEntryKey, configId);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS)).thenReturn(
				configs);
		classUnderTest.setConfigIdForCartEntry(cartEntryKey, configId);
		configs.remove(cartEntryKey);
		classUnderTest.removeConfigIdForCartEntry(cartEntryKey);
		assertNull(classUnderTest.getCartEntryForConfigId(configId));

	}

	@Test
	public void testCartEntryForProduct()
	{
		final String cartEntryId = "1";
		final String productKey = "X";
		final Map<String, String> cartEntryIds = new HashMap<>();
		cartEntryIds.put(productKey, cartEntryId);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_PRODUCT_CART_ENTRIES)).thenReturn(
				cartEntryIds);
		classUnderTest.setCartEntryForProduct(productKey, cartEntryId);
		assertEquals(cartEntryId, classUnderTest.getCartEntryForProduct(productKey));
		classUnderTest.removeCartEntryForProduct(productKey);
		cartEntryIds.remove(productKey);
		assertNull(classUnderTest.getCartEntryForProduct(productKey));
	}

	@Test
	public void testRemoveSessionArtifactsForCartEntryCartEntryMap()
	{
		final String cartEntryId = "1";
		final String productKey = "X";
		final Map<String, String> cartEntryIds = new HashMap<>();
		cartEntryIds.put(productKey, cartEntryId);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_PRODUCT_CART_ENTRIES)).thenReturn(
				cartEntryIds);
		classUnderTest.setCartEntryForProduct(productKey, cartEntryId);
		assertEquals(cartEntryId, classUnderTest.getCartEntryForProduct(productKey));
		classUnderTest.removeSessionArtifactsForCartEntry(cartEntryId, productKey);
		//We expect that the corresponding product/cartEntry entry is gone!
		cartEntryIds.remove(productKey);
		assertNull(classUnderTest.getCartEntryForProduct(productKey));
	}

	@Test
	public void testRemoveSessionArtifactsForCartEntryConfigMap()
	{
		final String configId = "1";
		final String cartEntryKey = "X";
		final Map<String, String> configs = new HashMap<>();
		configs.put(cartEntryKey, configId);
		Mockito.when(sessionService.getAttribute(SessionAccessService.SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS)).thenReturn(
				configs);
		classUnderTest.setConfigIdForCartEntry(cartEntryKey, configId);
		configs.remove(cartEntryKey);
		classUnderTest.removeSessionArtifactsForCartEntry(cartEntryKey, "");
		assertNull(classUnderTest.getConfigIdForCartEntry(cartEntryKey));

	}



}
