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
package de.hybris.platform.core;

import de.hybris.platform.servicelayer.event.events.AfterTenantRestartEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class TestAfterTenantStartupEventListener extends AbstractEventListener<AfterTenantRestartEvent>
{

	private final List<AfterTenantRestartEvent> eventsRecorded = new ArrayList<AfterTenantRestartEvent>();

	@Override
	protected void onEvent(final AfterTenantRestartEvent event)
	{
		eventsRecorded.add(event);
	}


	public List<AfterTenantRestartEvent> getEventsRecoreded()
	{
		return eventsRecorded;
	}
}
