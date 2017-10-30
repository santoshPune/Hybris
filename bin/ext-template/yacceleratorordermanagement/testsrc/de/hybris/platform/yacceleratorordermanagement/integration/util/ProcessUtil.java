/*
 * [y] hybris Platform
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.yacceleratorordermanagement.integration.util;


import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


/**
 * this class is mainly to create and modify processes
 */
@Component
public class ProcessUtil extends BaseUtil
{
	private static final Logger LOG = Logger.getLogger(ProcessUtil.class);
	protected static final String ORDER_TEST_PROCESS = "orderTest";
	protected static final String ORDER_PROCESS_DEFINITION_NAME = "order-process";
	protected static final String RETURN_PROCESS_DEFINITION_NAME = "return-process";
	protected static final String CONSIGNMENT_ACTION_EVENT_NAME = "ConsignmentActionEvent";
	protected static final String SHIP_CONSIGNMENT_CHOICE = "confirmShipConsignment";

	protected final static int timeOut = 15; //seconds

	/**
	 * wait until process is not running
	 *
	 * @param process
	 * @param timeOut
	 * @throws InterruptedException
	 */
	public void waitUntilProcessIsNotRunning(final BusinessProcessModel process, final int timeOut) throws InterruptedException
	{
		int timeCount = 0;
		do
		{
			Thread.sleep(1000);
			getModelService().refresh(process);
		}
		while (ProcessState.RUNNING.equals(process.getState()) && timeCount++ < timeOut);
	}

	/**
	 * wait for consignment process is not running
	 *
	 * @param orderProcessModel
	 * @param consignment
	 * @param timeOut
	 * @throws InterruptedException
	 */
	public void waitUntilConsignmentProcessIsNotRunning(final OrderProcessModel orderProcessModel,
			final ConsignmentModel consignment, final int timeOut) throws InterruptedException
	{
		int timeCount = 0;
		do
		{
			Thread.sleep(1000);
			getModelService().refresh(orderProcessModel);
			getModelService().refresh(consignment);
		}
		while (ProcessState.RUNNING.equals(consignment.getConsignmentProcesses().iterator().next().getProcessState())
				&& ProcessState.RUNNING.equals(orderProcessModel.getProcessState()) && timeCount++ < timeOut);
	}

	/**
	 * wait for return process is not running
	 *
	 * @param returnRequestModel
	 * @param timeOut
	 * @throws InterruptedException
	 */
	public void waitUntilReturnProcessIsNotRunning(final ReturnRequestModel returnRequestModel, final int timeOut)
			throws InterruptedException
	{
		int timeCount = 0;
		do
		{
			Thread.sleep(1000);
			getModelService().refresh(returnRequestModel);
		}
		while (ProcessState.RUNNING.equals(returnRequestModel.getReturnProcess().iterator().next().getProcessState())
				&& timeCount++ < timeOut);
	}

	protected WarehousingBusinessProcessService<ReturnRequestModel> getReturnBusinessProcessService()
	{
		return returnBusinessProcessService;
	}

	public void setReturnBusinessProcessService(
			final WarehousingBusinessProcessService<ReturnRequestModel> returnBusinessProcessService)
	{
		this.returnBusinessProcessService = returnBusinessProcessService;
	}

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}
}
