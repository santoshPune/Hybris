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
 *
 *
 */
package de.hybris.platform.customerticketingc4cintegration.factory;

import de.hybris.platform.customerticketingc4cintegration.facade.C4CTicketFacadeImpl;
import de.hybris.platform.customerticketingc4cintegration.facade.C4CTicketFacadeMock;
import de.hybris.platform.customerticketingfacades.TicketFacade;
import de.hybris.platform.util.Config;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

/**
 * Factory decides which facade will be used mock/real.
 */
public class TicketFacadeFactory
{
	@Resource
	private C4CTicketFacadeImpl c4cTicketFacadeImpl;
	@Resource
	private C4CTicketFacadeMock c4cTicketFacadeMock;

	/**
	 * @return chosen facade
	 * @throws Exception
	 */
	public TicketFacade getTicketFacade() throws Exception
	{
		final String param = Config.getParameter("customerticketingc4cintegration.facade.mock");
		final boolean mockEnabled = StringUtils.isEmpty(param) ? true : Boolean.valueOf(param);
		if (mockEnabled)
		{
			return c4cTicketFacadeMock;
		}
		else
		{
			return c4cTicketFacadeImpl;
		}
	}
}
