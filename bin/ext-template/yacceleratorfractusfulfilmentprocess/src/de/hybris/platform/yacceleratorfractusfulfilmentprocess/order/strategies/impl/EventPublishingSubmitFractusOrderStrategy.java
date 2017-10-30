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
*/
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.strategies.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.events.SubmitFractusOrderEvent;

import org.springframework.beans.factory.annotation.Required;


/**
 * This implementation sends {@link SubmitFractusOrderEvent} event when order is submitted.
 */
public class EventPublishingSubmitFractusOrderStrategy implements SubmitOrderStrategy
{
	private EventService eventService;

	@Override
	public void submitOrder(final OrderModel order)
	{
		final SubmitFractusOrderEvent orderEvent = new SubmitFractusOrderEvent(order);
		orderEvent.setOrder(order);
		getEventService().publishEvent(orderEvent);
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
