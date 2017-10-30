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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.ordercancel.impl;

import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.EventService;

import org.springframework.beans.factory.annotation.Required;


/**
 * The order cancel service extends from default service to send the fractus order cancel event.
 */
public class FractusOrderCancelService extends DefaultOrderCancelService
{

	private EventService eventService;

	@Override
	public OrderCancelRecordEntryModel requestOrderCancel(final OrderCancelRequest orderCancelRequest,
			final PrincipalModel requestor) throws OrderCancelException
	{
		final OrderCancelRecordEntryModel orderCancelRecordEntryModel = super.requestOrderCancel(orderCancelRequest, requestor);

		sendYaasOrderCancelEvent(orderCancelRequest.getOrder(), orderCancelRecordEntryModel);

		return orderCancelRecordEntryModel;
	}

	/**
	 * Send fractus Order cancel event.
	 */
	protected void sendYaasOrderCancelEvent(final OrderModel orderModel,
			final OrderCancelRecordEntryModel orderCancelRecordEntryModel)
	{

		if (OrderCancelEntryStatus.FULL.equals(orderCancelRecordEntryModel.getCancelResult()))
		{
			final FractusOrderCancelEvent event = new FractusOrderCancelEvent();
			event.setOrderModel(orderModel);
			event.setOrderCancelRecordEntryModel(orderCancelRecordEntryModel);

			getEventService().publishEvent(event);
		}
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
