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

import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import org.apache.log4j.Logger;


public class FracutsSubprocessesCompletedAction extends AbstractSimpleDecisionAction<FractusOrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(FracutsSubprocessesCompletedAction.class);

	private static final String PROCESS_MSG = "Process: ";

	@Override
	public Transition executeAction(final FractusOrderProcessModel process)
	{
		LOG.info(PROCESS_MSG + process.getCode() + " in step " + getClass());

		LOG.info(PROCESS_MSG + process.getCode() + " is checking for  " + process.getConsignmentProcesses().size()
				+ " subprocess results");

		for (final ConsignmentProcessModel subProcess : process.getConsignmentProcesses())
		{
			if (subProcess instanceof FractusConsignmentProcessModel)
			{
				if (!((FractusConsignmentProcessModel) subProcess).isProcessDone())
				{
					LOG.info(PROCESS_MSG + process.getCode() + " found subprocess " + subProcess.getCode()
							+ " incomplete -> wait again!");
					return Transition.NOK;
				}
				LOG.info(PROCESS_MSG + process.getCode() + " found subprocess " + subProcess.getCode() + " complete ...");
			}
		}
		LOG.info(PROCESS_MSG + process.getCode() + " found all subprocesses complete");
		return Transition.OK;
	}
}
