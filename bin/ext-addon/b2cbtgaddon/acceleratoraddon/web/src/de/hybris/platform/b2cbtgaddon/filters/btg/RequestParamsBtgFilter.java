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
package de.hybris.platform.b2cbtgaddon.filters.btg;

import de.hybris.platform.btg.events.AbstractBTGRuleDataEvent;
import de.hybris.platform.btg.events.RequestParametersUsedBTGRuleDataEvent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;


/**
 * FilterBean to produce the request scoped BTG event {@link RequestParametersUsedBTGRuleDataEvent} This is a spring
 * configured filter that is executed by the PlatformFilterChain.
 */
public class RequestParamsBtgFilter extends AbstractBtgFilter
{
	@Override
	protected AbstractBTGRuleDataEvent getEvent(final HttpServletRequest request)
	{
		RequestParametersUsedBTGRuleDataEvent result = null;
		final Map<String, String[]> params = request.getParameterMap();
		if (!MapUtils.isEmpty(params))
		{
			result = new RequestParametersUsedBTGRuleDataEvent(params);
		}
		return result;
	}

	@Override
	protected boolean isRequestScoped()
	{
		return true;
	}
}
