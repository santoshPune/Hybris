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
package de.hybris.platform.acceleratorcms.component.cache;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.regioncache.key.CacheKey;

import javax.servlet.http.HttpServletRequest;


public interface CmsCacheService
{
	boolean useCache(HttpServletRequest request, AbstractCMSComponentModel component);

	String get(CacheKey key);

	void put(CacheKey key, String content);

	CacheKey getKey(HttpServletRequest request, AbstractCMSComponentModel component);

}
