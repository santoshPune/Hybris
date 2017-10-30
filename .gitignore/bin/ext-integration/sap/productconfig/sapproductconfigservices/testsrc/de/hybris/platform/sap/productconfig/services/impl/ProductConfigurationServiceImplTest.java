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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProviderFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.InstanceModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.SolvableConflictModelImpl;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.data.CartEntryConfigurationAttributes;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.umd.cs.findbugs.annotations.SuppressWarnings;


@UnitTest
public class ProductConfigurationServiceImplTest
{
	static class ThreadBlocking extends Thread
	{
		long startTime = 0;
		long waitTime = 40;

		@Override
		public void run()
		{
			synchronized (ProductConfigurationServiceImpl.PROVIDER_LOCK)
			{
				try
				{
					startTime = System.currentTimeMillis();
					ProductConfigurationServiceImpl.PROVIDER_LOCK.wait(waitTime);
				}
				catch (final InterruptedException e)
				{
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	static class ThreadAccessing extends Thread
	{
		long endTime = 0;

		@Override
		public void run()
		{
			ProductConfigurationServiceImpl.getLock(CONFIG_ID_1);
			endTime = System.currentTimeMillis();
		}
	}

	private static final String DUMMY_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOLUTION><CONFIGURATION CFGINFO=\"\" CLIENT=\"000\" COMPLETE=\"F\" CONSISTENT=\"T\" KBBUILD=\"3\" KBNAME=\"DUMMY_KB\" KBPROFILE=\"DUMMY_KB\" KBVERSION=\"3800\" LANGUAGE=\"E\" LANGUAGE_ISO=\"EN\" NAME=\"SCE 5.0\" ROOT_NR=\"1\" SCEVERSION=\" \"><INST AUTHOR=\"5\" CLASS_TYPE=\"300\" COMPLETE=\"F\" CONSISTENT=\"T\" INSTANCE_GUID=\"\" INSTANCE_ID=\"01\" NR=\"1\" OBJ_KEY=\"DUMMY_KB\" OBJ_TXT=\"Dummy KB\" OBJ_TYPE=\"MARA\" QTY=\"1.0\" UNIT=\"ST\"><CSTICS><CSTIC AUTHOR=\"8\" CHARC=\"DUMMY_CSTIC\" CHARC_TXT=\"Dummy CStic\" VALUE=\"8\" VALUE_TXT=\"Value 8\"/></CSTICS></INST><PARTS/><NON_PARTS/></CONFIGURATION><SALES_STRUCTURE><ITEM INSTANCE_GUID=\"\" INSTANCE_ID=\"1\" INSTANCE_NR=\"1\" LINE_ITEM_GUID=\"\" PARENT_INSTANCE_NR=\"\"/></SALES_STRUCTURE></SOLUTION>";


	/**
	 *
	 */
	private static final String CONFIG_ID_2 = "asdasdwer4543556zgfhvchtr";

	/**
	 *
	 */
	private static final String CONFIG_ID_1 = "asdsafsdgftert6er6erzz";

	private ProductConfigurationServiceImpl cut;

	@Mock
	private ConfigurationProvider configurationProviderMock;

	@Mock
	private ConfigModel modelMock;

	@Mock
	private ConfigurationProviderFactory configurationProviderFactoryMock;

	@Mock
	private SessionService sessionService;


	private static final String CONFIG_ID = "abc123";

	@Mock
	private CartEntryModel cartEntry;

	@Mock
	private ProductModel productModel;

	@Mock
	private SessionAccessService sessionAccessService;


	private static final long keyAsLong = 12;


	private final PK primaryKey = PK.fromLong(keyAsLong);


	private static final String configId = "1";


	private final ConfigModel configModel = new ConfigModelImpl();

	private final InstanceModel instanceModel = new InstanceModelImpl();



	@Before
	public void setup()
	{
		cut = new ProductConfigurationServiceImpl();
		MockitoAnnotations.initMocks(this);
		cut.setConfigurationProviderFactory(configurationProviderFactoryMock);
		Mockito.when(configurationProviderFactoryMock.getProvider()).thenReturn(configurationProviderMock);
		Mockito.when(configurationProviderMock.createConfigurationFromExternalSource(Mockito.any(KBKey.class), Mockito.anyString()))
				.thenReturn(configModel);
		Mockito.when(configurationProviderMock.createDefaultConfiguration((Mockito.any(KBKey.class)))).thenReturn(configModel);
		cut.setSessionService(sessionService);
		cut.setSessionAccessService(sessionAccessService);

		Mockito.when(modelMock.getId()).thenReturn(CONFIG_ID);
		Mockito.when(cartEntry.getPk()).thenReturn(primaryKey);
		Mockito.when(cartEntry.getProduct()).thenReturn(productModel);
		Mockito.when(sessionAccessService.getConfigIdForCartEntry(primaryKey.toString())).thenReturn(configId);
		Mockito.when(sessionService.getAttribute(ProductConfigurationServiceImpl.SESSION_CACHE_KEY_PREFIX + configId))
				.thenReturn(configModel);

		configModel.setRootInstance(instanceModel);
		configModel.setId(configId);
		instanceModel.setSubInstances(Collections.EMPTY_LIST);
		//getSessionAccessService().getConfigIdForCartEntry(entryKey);

	}

	@Test
	public void testRetrieveConfiguration() throws Exception
	{
		Mockito.when(configurationProviderMock.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);

		final ConfigModel retrievedModel = cut.retrieveConfigurationModel(CONFIG_ID);

		assertTrue("Not delegated", retrievedModel == modelMock);
	}


	@Test
	public void testRetrieveExternalConfiguration() throws Exception
	{
		Mockito.when(configurationProviderMock.retrieveExternalConfiguration(CONFIG_ID)).thenReturn(DUMMY_XML);

		final String xmlString = cut.retrieveExternalConfiguration(CONFIG_ID);

		assertTrue("Not delegated", xmlString == DUMMY_XML);
	}

	@Test
	public void testRetrieveConfigurationNull() throws Exception
	{
		Mockito.when(configurationProviderMock.retrieveConfigurationModel(CONFIG_ID)).thenReturn(null);

		final ConfigModel retrievedModel = cut.retrieveConfigurationModel(CONFIG_ID);

		assertNull("Not just delegated", retrievedModel);

	}

	@Test
	public void testGetLockNotNull()
	{
		Assert.assertNotNull("Lock objects may not be null", ProductConfigurationServiceImpl.getLock(CONFIG_ID_1));
	}

	@Test
	public void testGetLockDifferrentForDifferntConfigIds()
	{
		final Object lock1 = ProductConfigurationServiceImpl.getLock(CONFIG_ID_1);
		final Object lock2 = ProductConfigurationServiceImpl.getLock(CONFIG_ID_2);
		Assert.assertNotSame("Lock objects should not be same!", lock1, lock2);
	}

	@Test
	public void testGetLockSameforSameConfigIds()
	{
		final Object lock1 = ProductConfigurationServiceImpl.getLock(CONFIG_ID_1);
		final Object lock2 = ProductConfigurationServiceImpl.getLock(CONFIG_ID_1);
		Assert.assertSame("Lock objects should be same!", lock1, lock2);
	}

	@Test
	public void testGetLockMapShouldNotGrowEndless()
	{

		final Object lock1 = ProductConfigurationServiceImpl.getLock(CONFIG_ID_1);
		final int maxLocks = ProductConfigurationServiceImpl.getMaxLocksPerMap() * 2;
		for (int ii = 0; ii <= maxLocks; ii++)
		{
			ProductConfigurationServiceImpl.getLock(String.valueOf(ii));
		}
		final Object lock2 = ProductConfigurationServiceImpl.getLock(CONFIG_ID_1);
		Assert.assertNotSame("Lock objects should not be same!", lock1, lock2);
	}


	@Test
	public void testRetrieveConfigurationCached()
	{

		Mockito.when(configurationProviderMock.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);

		ConfigModel retrievedModel = cut.retrieveConfigurationModel(CONFIG_ID);
		Mockito.verify(sessionService, Mockito.times(1)).setAttribute(Mockito.contains(CONFIG_ID), Mockito.same(retrievedModel));
		Mockito.when(sessionService.getAttribute(Mockito.contains(CONFIG_ID))).thenReturn(modelMock);

		retrievedModel = cut.retrieveConfigurationModel(CONFIG_ID);

		Mockito.verify(configurationProviderMock, Mockito.times(1)).retrieveConfigurationModel(CONFIG_ID);
		assertTrue("Not delegated", retrievedModel == modelMock);
	}


	@Test
	public void testUpdateConfigurationInvalidateCache()
	{
		Mockito.when(configurationProviderMock.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);
		Mockito.when(Boolean.valueOf(configurationProviderMock.updateConfiguration(modelMock))).thenReturn(Boolean.TRUE);

		cut.updateConfiguration(modelMock);
		Mockito.verify(sessionService, Mockito.times(1)).removeAttribute(Mockito.contains(CONFIG_ID));
	}


	@Test
	public void testConfigCacheGrowsNotEndless()
	{
		Mockito.when(configurationProviderMock.retrieveConfigurationModel(CONFIG_ID)).thenReturn(modelMock);
		cut.retrieveConfigurationModel(CONFIG_ID);
		final int maxCachedConfigs = cut.getMaxCachedConfigsInSession() * 2;
		for (int ii = 0; ii <= maxCachedConfigs; ii++)
		{
			final String configId = String.valueOf(ii);
			cut.retrieveConfigurationModel(configId);
		}
		Mockito.verify(sessionService, Mockito.times(1)).removeAttribute(Mockito.contains(CONFIG_ID));
	}

	@Test
	public void testSessionAccessService()
	{
		final SessionAccessService sessionAccessService = new SessionAccessServiceImpl();
		cut.setSessionAccessService(sessionAccessService);
		assertEquals("Service should be available", sessionAccessService, cut.getSessionAccessService());
	}

	@Test
	public void testGetCartEntryConfigurationAttributesEmptyConfig()
	{
		final CartEntryConfigurationAttributes entryAttribs = cut.calculateCartEntryConfigurationAttributes(cartEntry);
		assertNotNull(entryAttribs);
		assertEquals("Empty configuration not consistent", Boolean.FALSE, entryAttribs.getConfigurationConsistent());
		assertEquals("No errors expected", 0, entryAttribs.getNumberOfErrors().intValue());
	}

	@Test
	public void testGetCartEntryConfigurationAttributesNoExternalCFG()
	{
		Mockito.when(sessionService.getAttribute(ProductConfigurationServiceImpl.SESSION_CACHE_KEY_PREFIX + configId))
				.thenReturn(null);

		//no configuration: in this case we create a default configuration which should not contain issues
		final CartEntryConfigurationAttributes cartEntryConfigurationAttributes = cut
				.calculateCartEntryConfigurationAttributes(cartEntry);
		assertEquals("No errors expected", 0, cartEntryConfigurationAttributes.getNumberOfErrors().intValue());

	}

	@Test
	public void testGetCartEntryConfigurationAttributesNumberOfIssues()
	{
		final SolvableConflictModel conflict = new SolvableConflictModelImpl();
		configModel.setSolvableConflicts(Arrays.asList(conflict));
		final CartEntryConfigurationAttributes entryAttribs = cut.calculateCartEntryConfigurationAttributes(cartEntry);
		assertNotNull(entryAttribs);
		assertEquals("One error expected", 1, entryAttribs.getNumberOfErrors().intValue());
	}

	@Test
	public void testGetCartEntryConfigurationAttributes()
	{
		configModel.setComplete(true);
		configModel.setConsistent(true);
		checkCartEntryConsistent();
	}

	@Test
	public void testGetCartEntryConfigurationAttributesNotComplete()
	{
		configModel.setComplete(false);
		configModel.setConsistent(true);
		checkCartEntryNotConsistent();
	}

	@Test
	public void testGetCartEntryConfigurationAttributesNotConsistent()
	{
		configModel.setComplete(true);
		configModel.setConsistent(false);
		checkCartEntryNotConsistent();
	}

	/**
	 *
	 */
	private void checkCartEntryConsistent()
	{
		final CartEntryConfigurationAttributes entryAttribs = cut.calculateCartEntryConfigurationAttributes(cartEntry);
		assertNotNull(entryAttribs);
		assertEquals("Configuration should be consistent ", Boolean.TRUE, entryAttribs.getConfigurationConsistent());
	}

	private void checkCartEntryNotConsistent()
	{
		final CartEntryConfigurationAttributes entryAttribs = cut.calculateCartEntryConfigurationAttributes(cartEntry);
		assertNotNull(entryAttribs);
		assertEquals("Configuration shouldn't be consistent ", Boolean.FALSE, entryAttribs.getConfigurationConsistent());
	}


	@Test
	public void testGetNumberOfConflictsEmptyConfig()
	{
		final int numberOfConflicts = cut.countNumberOfSolvableConflicts(configModel);
		assertEquals("No conflicts", 0, numberOfConflicts);
	}

	@Test
	public void testGetNumberOfConflicts()
	{
		final SolvableConflictModel conflict = new SolvableConflictModelImpl();
		configModel.setSolvableConflicts(Arrays.asList(conflict));
		final int numberOfConflicts = cut.countNumberOfSolvableConflicts(configModel);
		assertEquals("We expect one conflict", 1, numberOfConflicts);
	}

	@Test
	public void testNoConfigID()
	{
		final String externalConfig = "testExternalConfig";
		Mockito.when(cartEntry.getExternalConfiguration()).thenReturn(externalConfig);
		Mockito.when(sessionAccessService.getConfigIdForCartEntry(primaryKey.toString())).thenReturn(null);
		final CartEntryConfigurationAttributes entryAttribs = cut.calculateCartEntryConfigurationAttributes(cartEntry);
		assertNotNull(entryAttribs);
	}

	@Test
	@SuppressWarnings("RU_INVOKE_RUN")
	public void testSynchronizationBlockingIsFirst()
	{
		final ThreadBlocking threadBlocking = new ThreadBlocking();
		final ThreadAccessing threadAccessing = new ThreadAccessing();
		threadBlocking.run();
		threadAccessing.run();
		assertTrue("We expect second thread needs to wait",
				threadAccessing.endTime - threadBlocking.startTime >= threadBlocking.waitTime);
	}

	@Test
	public void testSynchronizationAccessingIsFirst()
	{
		final ThreadBlocking threadBlocking = new ThreadBlocking();
		final ThreadAccessing threadAccessing = new ThreadAccessing();
		final long currentTime = System.currentTimeMillis();
		threadAccessing.start();
		threadBlocking.start();
		assertTrue("We expect acessing thread returns instantly",
				threadAccessing.endTime - currentTime < threadBlocking.waitTime / 10);
	}

}
