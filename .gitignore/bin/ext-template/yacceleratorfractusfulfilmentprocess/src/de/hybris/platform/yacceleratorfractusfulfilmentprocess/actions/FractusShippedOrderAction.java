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

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;


/**
 * This action runs the sync job to send order shipment information to Yaas
 */
public class FractusShippedOrderAction extends AbstractSimpleDecisionAction<FractusOrderProcessModel>
{

	private String syncExecutionId;

	private FractusSyncExecutionService syncExecutionService;


	/**
	 * @return the syncExecutionService
	 */
	public FractusSyncExecutionService getSyncExecutionService()
	{
		return syncExecutionService;
	}

	/**
	 * @param syncExecutionService
	 *           the syncExecutionService to set
	 */
	public void setSyncExecutionService(final FractusSyncExecutionService syncExecutionService)
	{
		this.syncExecutionService = syncExecutionService;
	}

	/**
	 * @return the syncExecutionId
	 */
	public String getSyncExecutionId()
	{
		return syncExecutionId;
	}

	/**
	 * @param syncExecutionId
	 *           the syncExecutionId to set
	 */
	public void setSyncExecutionId(final String syncExecutionId)
	{
		this.syncExecutionId = syncExecutionId;
	}


	@Override
	public Transition executeAction(final FractusOrderProcessModel process) throws RetryLaterException, Exception
	{
		final Transition returnValue = Transition.OK;

		final OrderModel order = process.getOrder();
		syncExecutionService.syncOrderShipmentDetails(order.getCode(), syncExecutionId);
		return returnValue;
	}



}
