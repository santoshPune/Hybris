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
import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;


@UnitTest
/**
 * Tests: ProductConfigurationUpdateCartEntryHookImpl
 */
public class ProductConfigurationUpdateCartEntryHookImplTest
{

	ProductConfigurationUpdateCartEntryHookImpl classUnderTest = new ProductConfigurationUpdateCartEntryHookImpl();

	@Mock
	private ProductConfigurationService productConfigurationService;

	@Mock
	private CommerceCartParameter parameter;

	@Mock
	private CommerceCartModification result;

	@Mock
	private SessionAccessServiceImpl sessionAccessService;

	@Mock
	private SessionService sessionService;

	private CartModel cart;

	@Mock
	private AbstractOrderEntryModel entry1;

	@Mock
	private AbstractOrderEntryModel entry2;

	@Mock
	private AbstractOrderEntryModel entry3;

	@Mock
	private AbstractOrderEntryModel entry4;

	@Mock
	private ProductModel product;

	String configId = "A";

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		classUnderTest.setProductConfigurationService(productConfigurationService);
		classUnderTest.setSessionAccessService(sessionAccessService);

		Mockito.when(sessionAccessService.getSessionService()).thenReturn(sessionService);
		Mockito.when(entry1.getEntryNumber()).thenReturn(Integer.valueOf(0));
		Mockito.when(entry1.getPk()).thenReturn(PK.fromLong(0));
		Mockito.when(entry2.getEntryNumber()).thenReturn(Integer.valueOf(1));
		Mockito.when(entry2.getPk()).thenReturn(PK.fromLong(1));
		Mockito.when(entry3.getEntryNumber()).thenReturn(Integer.valueOf(2));
		Mockito.when(entry3.getProduct()).thenReturn(product);
		Mockito.when(entry3.getPk()).thenReturn(PK.fromLong(2));
		Mockito.when(entry4.getEntryNumber()).thenReturn(Integer.valueOf(3));
		Mockito.when(entry4.getPk()).thenReturn(PK.fromLong(3));

		Mockito.when(sessionAccessService.getConfigIdForCartEntry("2")).thenReturn(configId);

	}

	@Test
	public void testConfigService()
	{
		assertNotNull(classUnderTest.getProductConfigurationService());
	}

	@Test
	@SuppressWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
	public void testAfterUpdateCartEntryNoConfigToBeDeleted()
	{
		cart = new CartModel();
		// Add four entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry2 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry3 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry4 = new AbstractOrderEntryModel();
		entry1.setEntryNumber(Integer.valueOf(0));
		entries.add(entry1);
		entry2.setEntryNumber(Integer.valueOf(1));
		entries.add(entry2);
		// The third entry is configurable
		//final String configId = "A";
		entry3.setEntryNumber(Integer.valueOf(2));
		//entry3.setConfigId(configId);
		entries.add(entry3);
		entry4.setEntryNumber(Integer.valueOf(3));
		entries.add(entry4);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// Set quantity to 0 in order to signal a deletion
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(0));
		classUnderTest.afterUpdateCartEntry(parameter, result);
		// configToBeDeleted should be requested once to check if it is filled
		Mockito.verify(parameter, Mockito.times(1)).getConfigToBeDeleted();
		// As configToBeDeleted is not filled, releaseSession should not be called
		Mockito.verify(productConfigurationService, Mockito.times(0)).releaseSession(Mockito.anyString());
	}

	@Test
	@SuppressWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
	public void testAfterUpdateCartEntryConfigToBeDeleted()
	{
		cart = new CartModel();
		// Add four entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry2 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry3 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry4 = new AbstractOrderEntryModel();
		entry1.setEntryNumber(Integer.valueOf(0));
		entries.add(entry1);
		entry2.setEntryNumber(Integer.valueOf(1));
		entries.add(entry2);
		// The third entry is configurable
		//final String configId = "A";
		entry3.setEntryNumber(Integer.valueOf(2));
		//entry3.setConfigId(configId);
		entries.add(entry3);
		entry4.setEntryNumber(Integer.valueOf(3));
		entries.add(entry4);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// Set quantity to 0 in order to signal a deletion
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(0));
		// Set configToBeFilled to configId "B"
		final String configToBeDeleted = "B";
		Mockito.when(parameter.getConfigToBeDeleted()).thenReturn(configToBeDeleted);
		classUnderTest.afterUpdateCartEntry(parameter, result);
		// configToBeDeleted should be requested once to check if it is filled
		Mockito.verify(parameter, Mockito.times(1)).getConfigToBeDeleted();
		// releaseSession() should be called once with configId "B"
		Mockito.verify(productConfigurationService, Mockito.times(1)).releaseSession(configToBeDeleted);
	}

	@Test
	@SuppressWarnings("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
	public void testAfterUpdateCartEntryUpdateConfigurable()
	{
		cart = new CartModel();
		// Add four entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry2 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry3 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry4 = new AbstractOrderEntryModel();
		entry1.setEntryNumber(Integer.valueOf(0));
		entries.add(entry1);
		entry2.setEntryNumber(Integer.valueOf(1));
		entries.add(entry2);
		// The third entry is configurable
		//final String configId = "A";
		entry3.setEntryNumber(Integer.valueOf(2));
		//entry3.setConfigId(configId);
		entries.add(entry3);
		entry4.setEntryNumber(Integer.valueOf(3));
		entries.add(entry4);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// Set quantity to a value != 0 in order to signal an update not a deletion
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(5));
		// Set configToBeFilled to configId "B"
		final String configToBeDeleted = "B";
		Mockito.when(parameter.getConfigToBeDeleted()).thenReturn(configToBeDeleted);
		classUnderTest.afterUpdateCartEntry(parameter, result);
		// configToBeDeleted should not be requested as qty is not 0
		Mockito.verify(parameter, Mockito.times(0)).getConfigToBeDeleted();
		// releaseSession() should not be called
		Mockito.verify(productConfigurationService, Mockito.times(0)).releaseSession(configToBeDeleted);
	}

	@Test
	public void testBeforeUpdateCartEntryNoConfigurableEntries()
	{
		cart = new CartModel();
		// Add three non-configurable entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();



		entries.add(entry1);
		entries.add(entry2);
		entries.add(entry3);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// The second entry is set to be deleted
		Mockito.when(Long.valueOf(parameter.getEntryNumber())).thenReturn(Long.valueOf(1));
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(0));
		classUnderTest.beforeUpdateCartEntry(parameter);
		// setConfigToBeDeleted should not be called
		Mockito.verify(parameter, Mockito.times(0)).setConfigToBeDeleted(Mockito.anyString());
	}

	@Test
	public void testBeforeUpdateCartEntryDeleteNonConfigurable()
	{
		cart = new CartModel();
		// Add four entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		entries.add(entry1);
		entries.add(entry2);
		entries.add(entry3);
		entries.add(entry4);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// The second entry is set to be deleted
		Mockito.when(Long.valueOf(parameter.getEntryNumber())).thenReturn(Long.valueOf(1));
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(0));
		classUnderTest.beforeUpdateCartEntry(parameter);
		// setConfigToBeDeleted should not be called
		Mockito.verify(parameter, Mockito.times(0)).setConfigToBeDeleted(Mockito.anyString());
	}

	@Test
	public void testBeforeUpdateCartEntryDeleteConfigurable()
	{
		cart = new CartModel();
		// Add four entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		entries.add(entry1);
		entries.add(entry2);
		entries.add(entry3);
		entries.add(entry4);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// The third entry (configurable) is set to be deleted
		Mockito.when(Long.valueOf(parameter.getEntryNumber())).thenReturn(Long.valueOf(2));
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(0));
		classUnderTest.beforeUpdateCartEntry(parameter);
		// setConfigToBeDeleted should be called once with the config Id "A"
		Mockito.verify(parameter, Mockito.times(1)).setConfigToBeDeleted(configId);
	}

	@Test
	public void testBeforeUpdateCartEntryUpdateConfigurable()
	{
		cart = new CartModel();
		// Add four entries
		final List<AbstractOrderEntryModel> entries = new ArrayList();
		final AbstractOrderEntryModel entry1 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry2 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry3 = new AbstractOrderEntryModel();
		final AbstractOrderEntryModel entry4 = new AbstractOrderEntryModel();
		entry1.setEntryNumber(Integer.valueOf(0));
		entries.add(entry1);
		entry2.setEntryNumber(Integer.valueOf(1));
		entries.add(entry2);
		// The third entry is configurable
		//final String configId = "A";
		entry3.setEntryNumber(Integer.valueOf(2));
		//entry3.setConfigId(configId);
		entries.add(entry3);
		entry4.setEntryNumber(Integer.valueOf(3));
		entries.add(entry4);
		cart.setEntries(entries);
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// The third entry (configurable) gets a qty different to 0.
		Mockito.when(Long.valueOf(parameter.getEntryNumber())).thenReturn(Long.valueOf(2));
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(5));
		classUnderTest.beforeUpdateCartEntry(parameter);
		// setConfigToBeDeleted should not be called as this is a qty change no deletion
		Mockito.verify(parameter, Mockito.times(0)).setConfigToBeDeleted(Mockito.anyString());
	}

	@Test
	public void testBeforeUpdateCartNoEntries()
	{
		// Cart without entries
		cart = new CartModel();
		Mockito.when(parameter.getCart()).thenReturn(cart);
		// A non-existing entry is marked as to be deleted.
		Mockito.when(Long.valueOf(parameter.getEntryNumber())).thenReturn(Long.valueOf(2));
		Mockito.when(Long.valueOf(parameter.getQuantity())).thenReturn(Long.valueOf(0));
		classUnderTest.beforeUpdateCartEntry(parameter);
		// setConfigToBeDeleted should not be called (as there are no entries)
		Mockito.verify(parameter, Mockito.times(0)).setConfigToBeDeleted(Mockito.anyString());
	}


}
