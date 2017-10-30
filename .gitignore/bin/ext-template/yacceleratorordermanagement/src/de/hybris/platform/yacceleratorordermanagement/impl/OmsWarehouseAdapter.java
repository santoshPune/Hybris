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
package de.hybris.platform.yacceleratorordermanagement.impl;


import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.OrderCancelWarehouseAdapter;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;


/**
 * OMS implementation of {@link OrderCancelWarehouseAdapter}
 */
public class OmsWarehouseAdapter implements OrderCancelWarehouseAdapter
{
	protected static final String CANCEL_ORDER_CHOICE = "cancelOrder";
	protected static final String ORDER_ACTION_EVENT_NAME = "OrderActionEvent";

	private BusinessProcessService businessProcessService;

	@Override
	public void requestOrderCancel(final OrderCancelRequest orderCancelRequest)
	{
		orderCancelRequest.getOrder().getOrderProcess().stream()
				.filter(process -> process.getCode().startsWith(orderCancelRequest.getOrder().getStore().getSubmitOrderProcessCode()))
				.forEach(filteredProcess -> {
					final BusinessProcessEvent businessProcessEvent = BusinessProcessEvent
							.builder(filteredProcess.getCode() + "_" + ORDER_ACTION_EVENT_NAME).withChoice(CANCEL_ORDER_CHOICE)
							.withEventTriggeringInTheFutureDisabled().build();
					getBusinessProcessService().triggerEvent(businessProcessEvent);
				});
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
}
