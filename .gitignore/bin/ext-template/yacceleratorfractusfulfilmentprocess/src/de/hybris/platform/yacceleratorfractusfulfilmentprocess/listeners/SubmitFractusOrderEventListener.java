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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.listeners;

import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.events.SubmitFractusOrderEvent;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Listener for the submit fractus order event then trigger the fractus order fulfilment process.
 */
public class SubmitFractusOrderEventListener extends AbstractSiteEventListener<SubmitFractusOrderEvent>
{
	private static final Logger LOG = Logger.getLogger(SubmitFractusOrderEventListener.class);

	private FractusOrderService orderService;

	private BusinessProcessService businessProcessService;

	private ModelService modelService;

	@Override
	protected void onSiteEvent(final SubmitFractusOrderEvent event)
	{
		final OrderModel order = event.getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);

		startProcess(order);
	}

	protected void startProcess(final OrderModel order)
	{
		// Try the store set on the Order first, then fallback to the session
		final BaseStoreModel store = order.getStore();

		if (store == null)
		{
			LOG.warn("Unable to start fulfilment process for order [" + order.getCode()
					+ "]. Store not set on Order and no current base store defined in session.");
		}
		else
		{
			final String fulfilmentProcessDefinitionName = store.getSubmitOrderProcessCode();
			final String processCode = fulfilmentProcessDefinitionName + "-" + order.getCode() + "-" + System.currentTimeMillis();
			final FractusOrderProcessModel businessProcessModel = getBusinessProcessService().createProcess(processCode,
					fulfilmentProcessDefinitionName);
			businessProcessModel.setOrder(order);
			getModelService().save(businessProcessModel);
			getBusinessProcessService().startProcess(businessProcessModel);
			if (LOG.isInfoEnabled())
			{
				LOG.info(String.format("Started the process %s", processCode));
			}
		}
	}

	@Override
	protected boolean shouldHandleEvent(final SubmitFractusOrderEvent event)
	{
		final OrderModel order = event.getOrder();
		ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
		return getOrderService().isFractusOrder(event.getOrder());
	}

	protected FractusOrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final FractusOrderService orderService)
	{
		this.orderService = orderService;
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

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}
