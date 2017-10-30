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
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.YaasOrderStatusService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.UnknownEcpStatusException;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class FractusUpdateOrderStatusAction extends AbstractSimpleDecisionAction<FractusOrderProcessModel>
{
	private YaasOrderStatusService yaasOrderStatusService;

	private static final Logger LOG = Logger.getLogger(FractusUpdateOrderStatusAction.class);

	private String syncExecutionId;

	private FractusSyncExecutionService syncExecutionService;

	@Override
	public Transition executeAction(final FractusOrderProcessModel process) throws RetryLaterException, Exception
	{
		Transition returnValue = Transition.OK;

		final OrderModel order = process.getOrder();

		String yaasStatus = null;

		try
		{
			yaasStatus = getYaasOrderStatusService().determineYaasStatus(order);

			final String message = "Determined new order status for Order : {0}, ECP status : {1}, YaaS Status : {2}";
			LOG.info(MessageFormat.format(message, order.getCode(), order.getStatus().getCode(), yaasStatus));

			order.setYaasOrderStatus(yaasStatus);
			getModelService().save(order);

		}
		catch (final UnknownEcpStatusException e)
		{
			final String errorMessage = "Unknown ECP Status found for order {0} with status of {1}";
			LOG.error(MessageFormat.format(errorMessage, order.getCode(), order.getStatus().getCode()), e);
		}
		catch (final NoMatchingYaasStatusException e)
		{
			final String warnMessage = "Unknown Yaas Status found for order {0} with status of {1}";
			LOG.warn(MessageFormat.format(warnMessage, order.getCode(), order.getStatus().getCode()), e);
		}

		//Logic to trigger DH service /cron job to update the YAAS order status based on OrderStatus value.
		if (order.getStatus().equals(OrderStatus.CHECKED_VALID))
		{
			LOG.info(
					"The YAAS Order have been validated and the process is triggered to propagate Order confirmed Message to DataHub");
			// propagate Order confirmed Message to DH

			//Update the status to confirmed then synch
			syncExecutionService.synchOrderStatus(order.getCode(), syncExecutionId);

		}
		else if (order.getStatus().equals(OrderStatus.ORDER_SPLIT))
		{
			LOG.info(" Yaas order has been shipped and the process is triggered to send Order Shipped message to DataHub");
			syncExecutionService.synchOrderStatus(order.getCode(), syncExecutionId);
		}
		else
		{
			LOG.error("The Order process is failed and terminated with the status " + order.getStatus());
			returnValue = Transition.NOK;
		}

		return returnValue;
	}

	/**
	 * @return the yaasOrderStatusService
	 */
	public YaasOrderStatusService getYaasOrderStatusService()
	{
		return yaasOrderStatusService;
	}

	/**
	 * @param yaasOrderStatusService
	 *           the yaasOrderStatusService to set
	 */
	public void setYaasOrderStatusService(final YaasOrderStatusService yaasOrderStatusService)
	{
		this.yaasOrderStatusService = yaasOrderStatusService;
	}

	public FractusSyncExecutionService getSyncExecutionService()
	{
		return syncExecutionService;
	}

	@Required
	public void setSyncExecutionService(final FractusSyncExecutionService syncExecutionService)
	{
		this.syncExecutionService = syncExecutionService;
	}


	public String getSyncExecutionId()
	{
		return syncExecutionId;
	}

	@Required
	public void setSyncExecutionId(final String syncExecutionId)
	{
		this.syncExecutionId = syncExecutionId;
	}
}
