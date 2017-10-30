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
package de.hybris.platform.b2cbtgaddon.filters.btg.support;

import javax.servlet.http.HttpServletRequest;


/**
 * Strategy for retrieving a pk from the request
 */
public interface PkResolvingStrategy
{
	/**
	 * Retrieve a primary key as {@link String}
	 *
	 * @param request
	 *           the request
	 * @return the primary key or null, if no key can be retrieved
	 */
	String resolvePrimaryKey(HttpServletRequest request);
}
