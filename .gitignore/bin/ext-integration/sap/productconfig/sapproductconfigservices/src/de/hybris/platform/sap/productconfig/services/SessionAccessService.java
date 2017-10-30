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
package de.hybris.platform.sap.productconfig.services;

import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;

import java.util.Map;


/**
 * Accessing the session to set and read product configuration related entities like UIStatus or runtime configuration
 * ID per cart entry
 */
public interface SessionAccessService
{
	public static final String SESSION_STORAGE_CART_ENTRY_CONFIGURATIONS = "productconfigCartEntryConfigurations";
	public static final String SESSION_STORAGE_CART_ENTRY_UISTATUSES = "productconfigCartEntryUiStatuses";
	public static final String SESSION_STORAGE_PRODUCT_UISTATUSES = "productconfigProductUiStatuses";
	public static final String SESSION_STORAGE_PRODUCT_CART_ENTRIES = "productconfigProductCartEntries";

	/**
	 * Stores configuration ID for a cart entry key into the session
	 *
	 * @param cartEntryKey
	 *           String representation of the cart entry primary key
	 * @param configId
	 *           ID of a runtime configuration object
	 */
	void setConfigIdForCartEntry(String cartEntryKey, String configId);

	/**
	 * Retrieves config identifier from the session for a given cart entry key
	 *
	 * @param cartEntryKey
	 *           String representation of the cart entry primary key
	 * @return ID of a runtime configuration object
	 */
	String getConfigIdForCartEntry(String cartEntryKey);

	/**
	 * Retrieves object from the session for a given cart entry key
	 *
	 * @param cartEntryKey
	 *           String representation of the cart entry primary key
	 * @return Object
	 */
	<T> T getUiStatusForCartEntry(String cartEntryKey);

	/**
	 * Retrieves object from the session for a given cart entry key
	 *
	 * @param productKey
	 *           Product key
	 * @return Object
	 */
	<T> T getUiStatusForProduct(String productKey);

	/**
	 * Stores object for a cart entry key into the session
	 *
	 * @param cartEntryKey
	 *           String representation of the cart entry primary key
	 * @param uiStatus
	 *           Object
	 */
	void setUiStatusForCartEntry(String cartEntryKey, Object uiStatus);

	/**
	 * Stores object for a product key into the session
	 *
	 * @param productKey
	 *           Product key
	 * @param uiStatus
	 *           Object
	 */
	void setUiStatusForProduct(String productKey, Object uiStatus);

	/**
	 * Removes object for a cart entry
	 *
	 * @param cartEntryKey
	 *           String representation of the cart entry primary key
	 */
	void removeUiStatusForCartEntry(String cartEntryKey);

	/**
	 * Removes object for a product
	 *
	 * @param productKey
	 *           Product key
	 */
	void removeUiStatusForProduct(String productKey);

	/**
	 * Retrieves cart entry key belonging to a specific config ID
	 *
	 * @param configId
	 * @return String representation of the cart entry primary key
	 */
	String getCartEntryForConfigId(String configId);

	/**
	 * Stores cart entry in session per product key
	 *
	 * @param productKey
	 * @param cartEntryId
	 *           String representation of the cart entry primary key
	 */
	void setCartEntryForProduct(String productKey, String cartEntryId);

	/**
	 * Retrieves cart entry key per product
	 *
	 * @param productKey
	 * @return String representation of the cart entry primary key
	 */
	String getCartEntryForProduct(String productKey);

	/**
	 * Removes cart entry key for product
	 *
	 * @param productKey
	 */
	void removeCartEntryForProduct(String productKey);

	/**
	 * Removes config ID for cart entry
	 *
	 * @param cartEntryKey
	 */
	void removeConfigIdForCartEntry(String cartEntryKey);

	/**
	 * Removes all session artifacts belonging to a cart entry
	 *
	 * @param cartEntryId
	 * @param productKey
	 */
	void removeSessionArtifactsForCartEntry(String cartEntryId, String productKey);

	/**
	 * @return Map of names from the hybris classification system
	 */
	public Map<String, ClassificationSystemCPQAttributesContainer> getCachedNameMap();

	/**
	 * Stores map of names from the hybris classification system in the session
	 * 
	 * @param nameMap
	 */
	public void putCachedNameMap(final Map<String, ClassificationSystemCPQAttributesContainer> nameMap);

}
