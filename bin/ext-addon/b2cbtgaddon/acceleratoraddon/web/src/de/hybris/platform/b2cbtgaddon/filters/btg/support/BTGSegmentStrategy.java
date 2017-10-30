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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;


/**
 */
public interface BTGSegmentStrategy
{
	// It is possible for this class to be extended or for method to be used in other extensions - so no sonar added
	void evaluateSegment(HttpServletRequest httpRequest) throws ServletException, IOException; // NOSONAR
}
