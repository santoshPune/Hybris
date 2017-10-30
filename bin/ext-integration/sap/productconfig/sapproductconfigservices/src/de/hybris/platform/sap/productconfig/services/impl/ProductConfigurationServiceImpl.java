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

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProvider;
import de.hybris.platform.sap.productconfig.runtime.interf.ConfigurationProviderFactory;
import de.hybris.platform.sap.productconfig.runtime.interf.KBKey;
import de.hybris.platform.sap.productconfig.runtime.interf.external.Configuration;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.SolvableConflictModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.data.CartEntryConfigurationAttributes;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;


public class ProductConfigurationServiceImpl implements ProductConfigurationService
{

	private static final String DEBUG_CONFIG_WITH_ID = "Config with id '";
	static final Object PROVIDER_LOCK = new Object();
	static final Object CACHE_LOCK = new Object();




	private static final Logger LOG = Logger.getLogger(ProductConfigurationServiceImpl.class);

	private int maxCachedConfigMapSize = 5;
	private Set<String> cachedConfigIds = new HashSet<>(maxCachedConfigMapSize);
	private Set<String> oldCachedConfigIds = new HashSet<>(maxCachedConfigMapSize);

	private static int maxLocksPerMap = 1024;
	private static Map<String, Object> locks = new HashMap<>(maxLocksPerMap);
	private static Map<String, Object> oldLocks = new HashMap<>(maxLocksPerMap);

	private ConfigurationProviderFactory configurationProviderFactory;
	private SessionService sessionService;

	private SessionAccessService sessionAccessService;

	static final String SESSION_CACHE_KEY_PREFIX = ProductConfigurationServiceImpl.class.getSimpleName() + "_last_"
			+ Configuration.class.getSimpleName() + "_";

	@Override
	public ConfigModel createDefaultConfiguration(final KBKey kbKey)
	{
		// no need to synchronize create, because config session (identified by
		// the config ID)
		// is only exposed once the object has been created
		final ConfigModel config = getConfigurationProvider().createDefaultConfiguration(kbKey);
		cacheConfig(config);

		return config;

	}

	@Override
	public void updateConfiguration(final ConfigModel model)
	{
		final String id = model.getId();
		final Object lock = ProductConfigurationServiceImpl.getLock(id);
		synchronized (lock)
		{

			final boolean updateExecuted = getConfigurationProvider().updateConfiguration(model);
			if (updateExecuted)
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug(DEBUG_CONFIG_WITH_ID + model.getId() + "' updated, removing it from cache");
				}
				removeConfigFromCache(id);
			}
		}
	}

	@Override
	public ConfigModel retrieveConfigurationModel(final String configId)
	{
		final Object lock = ProductConfigurationServiceImpl.getLock(configId);
		synchronized (lock)
		{

			ConfigModel cachedModel = sessionService.getAttribute(SESSION_CACHE_KEY_PREFIX + configId);
			if (cachedModel == null)
			{
				cachedModel = getConfigurationProvider().retrieveConfigurationModel(configId);
				cacheConfig(configId, cachedModel);
			}
			else
			{
				LOG.debug(DEBUG_CONFIG_WITH_ID + configId + "' retrieved from cache");
			}
			return cachedModel;
		}
	}

	@Override
	public String retrieveExternalConfiguration(final String configId)
	{
		final Object lock = getLock(configId);
		synchronized (lock)
		{
			return getConfigurationProvider().retrieveExternalConfiguration(configId);
		}
	}

	public void setConfigurationProviderFactory(final ConfigurationProviderFactory configurationProviderFactory)
	{
		this.configurationProviderFactory = configurationProviderFactory;
	}

	public static void setMaxLocksPerMap(final int maxLocksPerMap)
	{
		ProductConfigurationServiceImpl.maxLocksPerMap = maxLocksPerMap;
	}

	protected static int getMaxLocksPerMap()
	{
		return ProductConfigurationServiceImpl.maxLocksPerMap;
	}

	protected int getMaxCachedConfigsInSession()
	{
		return maxCachedConfigMapSize * 2;
	}

	public void setMaxCachedConfigsInSession(final int maxCachedConfigsInSession)
	{
		this.maxCachedConfigMapSize = maxCachedConfigsInSession / 2;
	}

	protected ConfigurationProvider getConfigurationProvider()
	{
		return configurationProviderFactory.getProvider();
	}

	protected static Object getLock(final String configId)
	{
		synchronized (PROVIDER_LOCK)
		{

			Object lock = locks.get(configId);
			if (lock == null)
			{
				lock = oldLocks.get(configId);
				if (lock == null)
				{
					ensureThatLockMapIsNotTooBig();
					lock = new Object();
					locks.put(configId, lock);
				}
			}
			return lock;
		}
	}

	protected static void ensureThatLockMapIsNotTooBig()
	{
		if (locks.size() >= maxLocksPerMap)
		{
			oldLocks.clear();
			oldLocks = locks;
			locks = new HashMap<>(maxLocksPerMap);
		}
	}

	protected void ensureThatNotToManyConfigsAreCachedInSession()
	{
		if (cachedConfigIds.size() >= maxCachedConfigMapSize)
		{
			for (final String configId : oldCachedConfigIds)
			{
				// clear old configs from session cache
				removeConfigFromSessionCache(configId);
			}
			oldCachedConfigIds = cachedConfigIds;
			cachedConfigIds = new HashSet<>(maxCachedConfigMapSize);
		}
	}

	@Override
	public ConfigModel createConfigurationFromExternal(final KBKey kbKey, final String externalConfiguration)
	{
		final ConfigModel config = getConfigurationProvider().createConfigurationFromExternalSource(kbKey, externalConfiguration);
		cacheConfig(config);

		return config;
	}

	@Override
	public ConfigModel createConfigurationFromExternalSource(final Configuration extConfig)
	{
		final ConfigModel config = getConfigurationProvider().createConfigurationFromExternalSource(extConfig);
		cacheConfig(config);

		return config;
	}

	@Override
	public void releaseSession(final String configId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Releasing config session with id " + configId);
		}

		getConfigurationProvider().releaseSession(configId);
		removeConfigFromCache(configId);
	}

	protected void removeConfigFromCache(final String configId)
	{
		removeConfigFromSessionCache(configId);
		synchronized (CACHE_LOCK)
		{
			cachedConfigIds.remove(configId);
		}
	}

	protected void removeConfigFromSessionCache(final String configId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Removing config with id '" + configId + "' from cache");
		}

		sessionService.removeAttribute(SESSION_CACHE_KEY_PREFIX + configId);
	}

	protected void cacheConfig(final ConfigModel config)
	{
		cacheConfig(config.getId(), config);
	}

	protected void cacheConfig(final String configId, final ConfigModel config)
	{
		synchronized (CACHE_LOCK)
		{
			ensureThatNotToManyConfigsAreCachedInSession();
			cachedConfigIds.add(configId);
		}
		sessionService.setAttribute(SESSION_CACHE_KEY_PREFIX + configId, config);

		if (LOG.isDebugEnabled())
		{
			LOG.debug(DEBUG_CONFIG_WITH_ID + configId + "' read frist time, caching it for further access");
		}
	}

	protected ConfigurationProviderFactory getConfigurationProviderFactory()
	{
		return configurationProviderFactory;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}


	/**
	 * Sets session access service (Accessing mappings which we store in the hybris session)
	 *
	 * @param sessionAccessService
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;

	}

	/**
	 * Accessing mappings which we store in the hybris session
	 *
	 * @return sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}

	@Override
	public CartEntryConfigurationAttributes calculateCartEntryConfigurationAttributes(final AbstractOrderEntryModel entryModel)
	{
		final String cartEntryKey = entryModel.getPk().toString();
		final String productCode = entryModel.getProduct().getCode();
		final String externalConfiguration = entryModel.getExternalConfiguration();

		return calculateCartEntryConfigurationAttributes(cartEntryKey, productCode, externalConfiguration);


	}

	@Override
	public CartEntryConfigurationAttributes calculateCartEntryConfigurationAttributes(final String cartEntryKey,
			final String productCode, final String externalConfiguration)
	{
		final CartEntryConfigurationAttributes attributes = new CartEntryConfigurationAttributes();


		final String configId = getSessionAccessService().getConfigIdForCartEntry(cartEntryKey);
		if (LOG.isDebugEnabled())
		{
			LOG.debug("ConfigID=" + configId + " is mapped to cartentry with PK=" + cartEntryKey);
		}
		ConfigModel configurationModel = null;
		if (configId != null)
		{
			configurationModel = retrieveConfigurationModel(configId);
		}

		if (configurationModel == null)
		{
			final KBKeyImpl kbKey = new KBKeyImpl(productCode);
			if (externalConfiguration == null)
			{
				//this means the item was put into the cart without touching CPQ, e.g. through order forms
				//as this is not the standard process, log this in info level
				LOG.info(
						"No external configuration provided for cart entry key: " + cartEntryKey + ". Creating default configuration");
				configurationModel = createDefaultConfiguration(kbKey);
			}
			else
			{
				LOG.debug("Creating config model form external XML");
				configurationModel = createConfigurationFromExternal(kbKey, externalConfiguration);
			}
			getSessionAccessService().setConfigIdForCartEntry(cartEntryKey, configurationModel.getId());
		}
		final boolean isConfigurationConsistent = configurationModel.isConsistent() && configurationModel.isComplete();

		attributes.setConfigurationConsistent(Boolean.valueOf(isConfigurationConsistent));
		final int numberOfIssues = countNumberOfIncompleteCstics(configurationModel.getRootInstance())
				+ countNumberOfSolvableConflicts(configurationModel);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Number of issues: " + numberOfIssues);
		}
		attributes.setNumberOfErrors(Integer.valueOf(numberOfIssues));

		return attributes;
	}

	@Override
	public int calculateNumberOfIncompleteCsticsAndSolvableConflicts(final String configId)
	{
		final ConfigModel configurationModel = retrieveConfigurationModel(configId);

		return countNumberOfIncompleteCstics(configurationModel.getRootInstance())
				+ countNumberOfSolvableConflicts(configurationModel);

	}

	protected int countNumberOfIncompleteCstics(final InstanceModel rootInstance)
	{

		int numberOfErrors = 0;
		for (final InstanceModel subInstace : rootInstance.getSubInstances())
		{
			numberOfErrors += countNumberOfIncompleteCstics(subInstace);
		}
		for (final CsticModel cstic : rootInstance.getCstics())
		{
			if (cstic.isRequired() && !cstic.isComplete())
			{
				numberOfErrors++;
				if (LOG.isDebugEnabled())
				{
					LOG.debug("Mandatory Cstic missing: " + cstic.getName());
				}
			}
		}
		return numberOfErrors;


	}

	protected int countNumberOfSolvableConflicts(final ConfigModel configModel)
	{
		final int result = 0;
		final List<SolvableConflictModel> solvableConflicts = configModel.getSolvableConflicts();
		if (solvableConflicts != null)
		{
			return solvableConflicts.size();
		}
		return result;
	}

}
