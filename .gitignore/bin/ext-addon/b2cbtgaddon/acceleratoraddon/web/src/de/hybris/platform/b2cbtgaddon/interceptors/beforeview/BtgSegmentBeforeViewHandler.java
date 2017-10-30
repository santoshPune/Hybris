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
package de.hybris.platform.b2cbtgaddon.interceptors.beforeview;

import de.hybris.platform.acceleratorstorefrontcommons.interceptors.BeforeViewHandler;
import de.hybris.platform.b2cbtgaddon.filters.btg.support.BTGSegmentStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;


public class BtgSegmentBeforeViewHandler implements BeforeViewHandler
{
	private BTGSegmentStrategy btgSegmentStrategy;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		getBtgSegmentStrategy().evaluateSegment(request);
	}

	protected BTGSegmentStrategy getBtgSegmentStrategy()
	{
		return btgSegmentStrategy;
	}

	@Required
	public void setBtgSegmentStrategy(final BTGSegmentStrategy btgSegmentStrategy)
	{
		this.btgSegmentStrategy = btgSegmentStrategy;
	}
}
