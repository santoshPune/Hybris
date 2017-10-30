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

import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;


/**
 * Default implementation of {@link SessionAccessService}
 */
public class SessionAccessServiceImpl implements SessionAccessService
{

	private static final String TRACE_MESSAGE_FOR_CART_ENTRY = "for cart entry: ";
	private static final String TRACE_MESSAGE_FOR_PRODUCT = "for product: ";
	private static final Logger LOG = Logger.getLogger(SessionAccessServiceImpl.class);
	private SessionService sessionService;


	//

	/**
	 * @param sessionService
	 *           the sessionService to set
	 */
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	/**
	 * @return sessionService
	 */
	public SessionService getSessionService()
	{
		return this.sessionService;
	}

	@Override
	public void setConfigIdForCartEntry(final String cartEntryKey, final String configId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Put config ID " + configId + " into session for cart entry: " + cartEntryKey);
		}
		final Map<String, String> newSessionConfigCartEntryCache = getCartEntryConfigCache();

		newSessionConfigCartEntryCache.put(cartEntryKey, configId);

		getSessionService().setAttribute(SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS, newSessionConfigCartEntryCache);

	}

	@Override
	public String getConfigIdForCartEntry(final String cartEntryKey)
	{
		String configId = null;
		final Map<String, String> sessionConfigCartEntryCache = (Map<String, String>) getSessionService()
				.getAttribute(SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS);
		if (sessionConfigCartEntryCache != null)
		{
			configId = sessionConfigCartEntryCache.get(cartEntryKey);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Get config ID " + configId + " from session for cart entry: " + cartEntryKey);
		}

		return configId;

	}


	@Override
	public <T> T getUiStatusForCartEntry(final String cartEntryKey)
	{
		return getUiStatusFromSession(cartEntryKey, SESSION_STORAGE_CART_ENTRY_UISTATUSES, TRACE_MESSAGE_FOR_CART_ENTRY);
	}


	/**
	 * Retrieves UiStatus from session
	 *
	 * @param key
	 *           Key of object in map
	 * @param sessionKey
	 *           The key which identifies our map in the session
	 * @param traceMessage
	 *           Post fix of the trace message which identifies the type of key
	 * @return UiStatus
	 */
	protected <T> T getUiStatusFromSession(final String key, final String sessionKey, final String traceMessage)
	{
		Object uiStatus = null;
		final Map<String, Object> sessionUiStatusCache = (Map<String, Object>) getSessionService().getAttribute(sessionKey);
		if (sessionUiStatusCache != null)
		{
			uiStatus = sessionUiStatusCache.get(key);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Get UiStatus " + uiStatus + " from session " + traceMessage + key);
		}

		return (T) uiStatus;
	}


	@Override
	public void setUiStatusForCartEntry(final String cartEntryKey, final Object uiStatus)
	{
		setUiStatusIntoSession(cartEntryKey, uiStatus, SESSION_STORAGE_CART_ENTRY_UISTATUSES, TRACE_MESSAGE_FOR_CART_ENTRY);

	}

	@Override
	public Object getUiStatusForProduct(final String productKey)
	{
		return getUiStatusFromSession(productKey, SESSION_STORAGE_PRODUCT_UISTATUSES, TRACE_MESSAGE_FOR_PRODUCT);

	}




	@Override
	public void setUiStatusForProduct(final String productKey, final Object uiStatus)
	{
		setUiStatusIntoSession(productKey, uiStatus, SESSION_STORAGE_PRODUCT_UISTATUSES, TRACE_MESSAGE_FOR_PRODUCT);

	}

	/**
	 * Puts UiStatus object into session
	 *
	 * @param key
	 *           Key for object
	 * @param uiStatus
	 *           The object we want to store in session
	 * @param sessionKey
	 *           The key which identifies our map in the session
	 * @param traceMessage
	 *           Post fix of the trace message which identifies the type of key
	 */
	protected void setUiStatusIntoSession(final String key, final Object uiStatus, final String sessionKey,
			final String traceMessage)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Put UiStatus " + uiStatus + " into session " + traceMessage + key);
		}
		final Map<String, Object> sessionUiStatusCartEntryCache = (Map<String, Object>) getSessionService()
				.getAttribute(sessionKey);

		final Map<String, Object> newSessionUiStatusCartEntryCache = new HashMap<String, Object>();

		if (!MapUtils.isEmpty(sessionUiStatusCartEntryCache))
		{
			newSessionUiStatusCartEntryCache.putAll(sessionUiStatusCartEntryCache);
		}

		newSessionUiStatusCartEntryCache.put(key, uiStatus);

		getSessionService().setAttribute(sessionKey, newSessionUiStatusCartEntryCache);
	}


	@Override
	public void removeUiStatusForCartEntry(final String cartEntryKey)
	{
		removeUiStatusFromSession(cartEntryKey, SESSION_STORAGE_CART_ENTRY_UISTATUSES, TRACE_MESSAGE_FOR_CART_ENTRY);
	}

	/**
	 * Removes UiStatus object from session
	 *
	 * @param key
	 *           Key for object
	 * @param sessionKey
	 *           The key which identifies our map in the session
	 * @param traceMessage
	 *           Post fix of the trace message which identifies the type of key
	 */
	protected void removeUiStatusFromSession(final String key, final String sessionKey, final String traceMessage)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Remove UiStatus from session " + traceMessage + key);
		}
		final Map<String, Object> uiStatusMap = sessionService.getAttribute(sessionKey);
		final Map<String, Object> newUiStatusMap = new HashMap<>();
		if (!MapUtils.isEmpty(uiStatusMap))
		{
			newUiStatusMap.putAll(uiStatusMap);
			newUiStatusMap.remove(key);
			sessionService.setAttribute(sessionKey, newUiStatusMap);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("Map does not exist in session");
			}
		}

	}

	@Override
	public void removeUiStatusForProduct(final String productKey)
	{
		removeUiStatusFromSession(productKey, SESSION_STORAGE_PRODUCT_UISTATUSES, TRACE_MESSAGE_FOR_PRODUCT);

	}

	@Override
	public String getCartEntryForConfigId(final String configId)
	{
		String cartEntryKey = null;
		final Map<String, Object> sessionUiStatusCache = (Map<String, Object>) getSessionService()
				.getAttribute(SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS);
		if (sessionUiStatusCache != null)
		{
			for (final Map.Entry<String, Object> entry : sessionUiStatusCache.entrySet())
			{
				if (entry.getValue().equals(configId))
				{
					cartEntryKey = entry.getKey();
					break;
				}
			}
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Get cart entry key " + cartEntryKey + " from session for config ID" + configId);
		}

		return cartEntryKey;
	}


	@Override
	public void setCartEntryForProduct(final String productKey, final String cartEntryId)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Put cartEntryId " + cartEntryId + " into session for product: " + productKey);
		}
		final Map<String, String> newSessionProductCartEntryCache = getProductCartEntryCache();

		newSessionProductCartEntryCache.put(productKey, cartEntryId);

		getSessionService().setAttribute(SESSION_STORAGE_PRODUCT_CART_ENTRIES, newSessionProductCartEntryCache);

	}


	@Override
	public String getCartEntryForProduct(final String productKey)
	{
		String cartEntryKey = null;
		final Map<String, String> sessionProductCartEntryCache = (Map<String, String>) getSessionService()
				.getAttribute(SESSION_STORAGE_PRODUCT_CART_ENTRIES);
		if (sessionProductCartEntryCache != null)
		{
			cartEntryKey = sessionProductCartEntryCache.get(productKey);
		}

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Get cart entry key " + cartEntryKey + " from session for product: " + productKey);
		}

		return cartEntryKey;
	}


	@Override
	public void removeCartEntryForProduct(final String productKey)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Remove cartEntryId for product: " + productKey);
		}
		final Map<String, String> newSessionProductCartEntryCache = getProductCartEntryCache();

		newSessionProductCartEntryCache.remove(productKey);

		getSessionService().setAttribute(SESSION_STORAGE_PRODUCT_CART_ENTRIES, newSessionProductCartEntryCache);

	}

	/**
	 * @return Cache for cart entries per product
	 */
	protected Map<String, String> getProductCartEntryCache()
	{
		final Map<String, String> sessionProductCartEntryCache = (Map<String, String>) getSessionService()
				.getAttribute(SESSION_STORAGE_PRODUCT_CART_ENTRIES);

		final Map<String, String> newSessionProductCartEntryCache = new HashMap<String, String>();

		if (!MapUtils.isEmpty(sessionProductCartEntryCache))
		{
			newSessionProductCartEntryCache.putAll(sessionProductCartEntryCache);
		}
		return newSessionProductCartEntryCache;
	}


	@Override
	public void removeSessionArtifactsForCartEntry(final String cartEntryId, final String productKey)
	{

		//remove configuration ID if needed
		removeConfigIdForCartEntry(cartEntryId);

		//remove UI status attached to cart entry
		removeUiStatusForCartEntry(cartEntryId);

		//check if this configuration is maintained at product level also
		final String currentCartEntryForProduct = getCartEntryForProduct(productKey);
		if (currentCartEntryForProduct != null && (currentCartEntryForProduct.equals(cartEntryId)))
		{
			//We need to clean up more storages
			removeCartEntryForProduct(productKey);
			removeUiStatusForProduct(productKey);
		}

	}


	@Override
	public void removeConfigIdForCartEntry(final String cartEntryKey)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Remove config ID for cart entry: " + cartEntryKey);
		}

		final Map<String, String> newSessionConfigCartEntryCache = getCartEntryConfigCache();

		newSessionConfigCartEntryCache.remove(cartEntryKey);

		getSessionService().setAttribute(SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS, newSessionConfigCartEntryCache);

	}

	/**
	 * @return Map: Configuration ID's for cart entry
	 */
	protected Map<String, String> getCartEntryConfigCache()
	{
		final Map<String, String> sessionConfigCartEntryCache = (Map<String, String>) getSessionService()
				.getAttribute(SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS);

		final Map<String, String> newSessionConfigCartEntryCache = new HashMap<String, String>();

		if (!MapUtils.isEmpty(sessionConfigCartEntryCache))
		{
			newSessionConfigCartEntryCache.putAll(sessionConfigCartEntryCache);
		}
		return newSessionConfigCartEntryCache;
	}

	@Override
	public Map<String, ClassificationSystemCPQAttributesContainer> getCachedNameMap()
	{
		Map<String, ClassificationSystemCPQAttributesContainer> nameMap;

		nameMap = sessionService.getAttribute(ClassificationSystemCPQAttributesContainer.class.getName());
		if (nameMap == null)
		{
			nameMap = new HashMap<>(512);
		}
		else
		{
			nameMap = new HashMap(nameMap);
		}

		return nameMap;
	}

	@Override
	public void putCachedNameMap(final Map<String, ClassificationSystemCPQAttributesContainer> nameMap)
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("HybrisCsticAndValueNamesCache has size: " + nameMap.size());
		}

		sessionService.setAttribute(ClassificationSystemCPQAttributesContainer.class.getName(), nameMap);
	}
}
