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

import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.consingmentprocessing.model.FractusConsignmentProcessModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.constants.YAcceleratorfractusfulfilmentProcessConstants;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class FractusConsignmentSubprocessEndAction extends AbstractProceduralAction<FractusConsignmentProcessModel>
{
	private static final Logger LOG = Logger.getLogger(FractusConsignmentSubprocessEndAction.class);

	private BusinessProcessService businessProcessService;

	private static final String PROCESS = "Process: ";

	protected BusinessProcessService getBusinessProcessService()
	{
		return businessProcessService;
	}

	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

	@Override
	public void executeAction(final FractusConsignmentProcessModel process)
	{
		LOG.info(PROCESS + process.getCode() + " in step " + getClass());

		try
		{
			while (!process.isProcessDone())
			{
				Thread.sleep(5000);
				Utilities.invalidateCache(process.getPk());
				refresh(process);
			}
		}
		catch (final InterruptedException ex)
		{
			LOG.warn(ex.getMessage(), ex);
		}

		LOG.info(PROCESS + process.getCode() + " wrote DONE marker");

		getBusinessProcessService().triggerEvent(process.getParentProcess().getCode() + "_"
				+ YAcceleratorfractusfulfilmentProcessConstants.CONSIGNMENT_SUBPROCESS_END_EVENT_NAME);
		LOG.info(PROCESS + process.getCode() + " fired event "
				+ YAcceleratorfractusfulfilmentProcessConstants.CONSIGNMENT_SUBPROCESS_END_EVENT_NAME);
	}
}
