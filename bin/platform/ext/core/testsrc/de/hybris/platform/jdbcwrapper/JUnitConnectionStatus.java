/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.jdbcwrapper;

import java.util.concurrent.atomic.AtomicBoolean;


public class JUnitConnectionStatus extends ConnectionStatus
{
	private final AtomicBoolean forceHasConnectionErrors = new AtomicBoolean(false);

	@Override
	public boolean hadError()
	{
		return super.hadError();
	}

	public void setPoolHasConnectionErrors(final boolean hasErrors)
	{
		forceHasConnectionErrors.set(hasErrors);
	}
}
