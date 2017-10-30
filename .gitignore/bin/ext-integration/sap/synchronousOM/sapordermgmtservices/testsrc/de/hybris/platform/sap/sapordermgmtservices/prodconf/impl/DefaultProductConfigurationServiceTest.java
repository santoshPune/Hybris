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
package de.hybris.platform.sap.sapordermgmtservices.prodconf.impl;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProviderFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.ConfigModelImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.impl.PriceModelImpl;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;

import java.math.BigDecimal;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("javadoc")
public class DefaultProductConfigurationServiceTest
{
	/**
	 *
	 */
	public static final String CONFIG_ID = "id";
	DefaultProductConfigurationService classUnderTest = new DefaultProductConfigurationService();
	private final String itemKey = "A";
	private final String externalConfiguration = "XML";
	private ConfigModel configModel;
	final String productCode = "Product";
	Double price = new Double("123");


	@Before
	public void init()
	{
		configModel = new ConfigModelImpl();
		final PriceModel currentTotalPrice = new PriceModelImpl();
		currentTotalPrice.setPriceValue(new BigDecimal(price.intValue()));
		configModel.setCurrentTotalPrice(currentTotalPrice);
		final SessionAccessService sessionAccessService = EasyMock.createMock(SessionAccessService.class);
		sessionAccessService.setConfigIdForCartEntry(itemKey, null);
		EasyMock.expect(sessionAccessService.getConfigIdForCartEntry(itemKey)).andReturn(CONFIG_ID);
		final ConfigurationProviderFactory configurationProviderFactory = EasyMock.createMock(ConfigurationProviderFactory.class);
		final ConfigurationProvider configurationProvider = EasyMock.createMock(ConfigurationProvider.class);
		EasyMock.expect(configurationProviderFactory.getProvider()).andReturn(configurationProvider);
		EasyMock.expect(configurationProvider.retrieveExternalConfiguration(CONFIG_ID)).andReturn(externalConfiguration);
		EasyMock.expect(configurationProvider.retrieveConfigurationModel(CONFIG_ID)).andReturn(configModel);
		EasyMock.expect(
				configurationProvider.createConfigurationFromExternalSource(EasyMock.anyObject(KBKey.class),
						EasyMock.anyObject(String.class))).andReturn(configModel);
		EasyMock.replay(sessionAccessService, configurationProviderFactory, configurationProvider);
		classUnderTest.setSessionAccessService(sessionAccessService);
		classUnderTest.setConfigurationProviderFactory(configurationProviderFactory);

	}



	@Test
	public void testSetIntoSession()
	{
		final ConfigModel configModel = new ConfigModelImpl();
		classUnderTest.setIntoSession(itemKey, configModel.getId());
	}

	@Test
	public void testIsInSession()
	{
		Assert.assertTrue(classUnderTest.isInSession(itemKey));
	}

	@Test
	public void testGetExternalConfiguration()
	{
		assertEquals(externalConfiguration, classUnderTest.getExternalConfiguration(itemKey));
	}

	@Test
	public void testGetTotalPrice()
	{
		assertEquals(price, classUnderTest.getTotalPrice(itemKey));
	}

	@Test
	public void testGetConfigId()
	{
		assertEquals(CONFIG_ID, classUnderTest.getGetConfigId(itemKey));
	}

	@Test
	public void testGetConfigModel()
	{

		assertEquals(configModel, classUnderTest.getConfigModel(productCode, externalConfiguration));
	}
}
