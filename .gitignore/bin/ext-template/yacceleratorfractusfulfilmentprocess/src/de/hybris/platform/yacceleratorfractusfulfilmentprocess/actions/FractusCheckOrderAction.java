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

package de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusCheckOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * This example action checks the fractus order for required data in the business process. Skipping this action may
 * result in failure in one of the subsequent steps of the process.
 */
public class FractusCheckOrderAction extends AbstractSimpleDecisionAction<FractusOrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(FractusCheckOrderAction.class);

	private FractusCheckOrderService checkOrderService;

	@Override
	public Transition executeAction(final FractusOrderProcessModel process) throws RetryLaterException, Exception
	{
		final OrderModel order = process.getOrder();

		if (order == null)
		{
			LOG.error("Missing the order, exiting the process");
			return Transition.NOK;
		}

		if (isOrderValid(order))
		{
			LOG.info("Order validated and the status has been updated to " + OrderStatus.CHECKED_VALID);

			setOrderStatus(order, OrderStatus.CHECKED_VALID);
			return Transition.OK;
		}
		else
		{
			LOG.error("It is an invalid Order and the status has been updated to " + OrderStatus.CHECKED_INVALID);

			setOrderStatus(order, OrderStatus.CHECKED_INVALID);
			return Transition.NOK;
		}
	}

	protected boolean isOrderValid(final OrderModel order)
	{
		return getCheckOrderService().check(order);
	}

	protected FractusCheckOrderService getCheckOrderService()
	{
		return checkOrderService;
	}

	@Required
	public void setCheckOrderService(final FractusCheckOrderService checkOrderService)
	{
		this.checkOrderService = checkOrderService;
	}
}
