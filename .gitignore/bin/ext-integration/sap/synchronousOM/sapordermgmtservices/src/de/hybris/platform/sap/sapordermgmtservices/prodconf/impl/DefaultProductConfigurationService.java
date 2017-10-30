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

import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProviderFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.sapordermgmtservices.prodconf.ProductConfigurationService;

import org.apache.log4j.Logger;


/**
 * Default implementation for {@link ProductConfigurationService}
 */
public class DefaultProductConfigurationService implements ProductConfigurationService
{
	private static final Logger LOG = Logger.getLogger(DefaultProductConfigurationService.class);
	private SessionAccessService sessionAccessService;
	private ConfigurationProviderFactory configurationProviderFactory;


	@Override
	public void setIntoSession(final String itemKey, final String configId)
	{
		getSessionAccessService().setConfigIdForCartEntry(itemKey, configId);

	}


	/**
	 * @return the sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}





	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}





	@Override
	public boolean isInSession(final String itemKey)
	{
		return (getSessionAccessService().getConfigIdForCartEntry(itemKey) != null);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.sap.sapordermgmtservices.prodconf.ConfigurationContainer#getExternalConfiguration()
	 */
	@Override
	public String getExternalConfiguration(final String itemKey)
	{
		final String configId = getConfigIdFromSession(itemKey);
		final ConfigurationProvider configurationProvider = configurationProviderFactory.getProvider();
		return configurationProvider.retrieveExternalConfiguration(configId);
	}


	/**
	 * @return the configurationProviderFactory
	 */
	public ConfigurationProviderFactory getConfigurationProviderFactory()
	{
		return configurationProviderFactory;
	}


	/**
	 * @param configurationProviderFactory
	 *           the configurationProviderFactory to set
	 */
	public void setConfigurationProviderFactory(final ConfigurationProviderFactory configurationProviderFactory)
	{
		this.configurationProviderFactory = configurationProviderFactory;
	}



	@Override
	public ConfigModel getConfigModel(final String productCode, final String externalConfiguration)
	{
		if (externalConfiguration != null && !externalConfiguration.isEmpty())
		{
			final KBKey kbKey = new KBKeyImpl(productCode);
			return configurationProviderFactory.getProvider().createConfigurationFromExternalSource(kbKey, externalConfiguration);
		}
		else
		{
			return null;
		}
	}


	@Override
	public Double getTotalPrice(final String itemKey)
	{
		final String configId = getConfigIdFromSession(itemKey);
		final ConfigurationProvider configurationProvider = configurationProviderFactory.getProvider();
		final ConfigModel configModel = configurationProvider.retrieveConfigurationModel(configId);
		final PriceModel currentTotalPrice = configModel.getCurrentTotalPrice();
		if (currentTotalPrice != null)
		{
			return new Double(currentTotalPrice.getPriceValue().doubleValue());
		}
		else
		{
			return new Double(0);
		}

	}


	@Override
	public String getGetConfigId(final String itemKey)
	{
		return getConfigIdFromSession(itemKey);

	}


	/**
	 * Fetches config ID from hybris session
	 *
	 * @param itemKey
	 * @return Config ID
	 */
	protected String getConfigIdFromSession(final String itemKey)
	{
		final String configId = getSessionAccessService().getConfigIdForCartEntry(itemKey);

		if (configId == null)
		{
			LOG.info("No configuration found for item key: " + itemKey);
		}
		return configId;
	}
}
