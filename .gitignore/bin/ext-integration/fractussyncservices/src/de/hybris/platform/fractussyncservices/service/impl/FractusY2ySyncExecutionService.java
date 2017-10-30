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

package de.hybris.platform.fractussyncservices.service.impl;

import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.impl.DefaultSyncExecutionService;
import org.springframework.beans.factory.annotation.Required;


/**
 * This implementation is to reset sync job streams and run sync jobs
 */
public class FractusY2ySyncExecutionService extends DefaultSyncExecutionService
{

	private ChangeDetectionService changeDetectionService;

	@Override
	public Y2YSyncCronJobModel startSync(final Y2YSyncJobModel job, final ExecutionMode executionMode)
	{
		if (job.getResetStream())
		{
			resetJobSteams(job);
		}

		return startSyncInternal(job, executionMode);
	}

	protected Y2YSyncCronJobModel startSyncInternal(final Y2YSyncJobModel job, final ExecutionMode executionMode)
	{
		return super.startSync(job, executionMode);
	}

	/**
	 * Reset all streams for given job.
	 */
	protected void resetJobSteams(final Y2YSyncJobModel job)
	{
		if (job.getStreamConfigurationContainer() != null && job.getStreamConfigurationContainer().getConfigurations() != null)
		{
			job.getStreamConfigurationContainer().getConfigurations().stream()
					.forEach(stream -> getChangeDetectionService().resetStream(stream.getStreamId()));
		}
	}

	protected ChangeDetectionService getChangeDetectionService()
	{
		return changeDetectionService;
	}

	@Required
	public void setChangeDetectionService(final ChangeDetectionService changeDetectionService)
	{
		this.changeDetectionService = changeDetectionService;
	}
}
