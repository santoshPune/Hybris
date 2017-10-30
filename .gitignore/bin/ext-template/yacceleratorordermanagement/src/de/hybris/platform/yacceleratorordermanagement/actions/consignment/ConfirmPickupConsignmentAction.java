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
package de.hybris.platform.yacceleratorordermanagement.actions.consignment;


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.orderprocessing.events.ConsignmentProcessingEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;

import de.hybris.platform.servicelayer.event.EventService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Updates a consignment status to Pickup Complete.
 */
public class ConfirmPickupConsignmentAction extends AbstractProceduralAction<ConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(ConfirmPickupConsignmentAction.class);

	private EventService eventService;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		LOG.info("Process: " + process.getCode() + " in step " + getClass().getSimpleName());
		final ConsignmentModel consignment = process.getConsignment();
		consignment.setStatus(ConsignmentStatus.PICKUP_COMPLETE);
		save(consignment);

		getEventService().publishEvent(getEvent(process));
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

	protected ConsignmentProcessingEvent getEvent(final ConsignmentProcessModel process)
	{
		return new ConsignmentProcessingEvent(process);
	}

}
