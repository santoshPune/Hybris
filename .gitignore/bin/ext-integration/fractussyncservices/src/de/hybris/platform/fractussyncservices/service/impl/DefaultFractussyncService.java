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

import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.fractussyncservices.service.FractussyncService;
import de.hybris.platform.servicelayer.cronjob.JobDao;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;


/**
 * Default implementation of {@link FractussyncService}
 */
public class DefaultFractussyncService implements FractussyncService
{
	private SyncExecutionService syncExecutionService;

	private JobDao jobDao;

	private String y2ySyncYaasConfigurationsJobCode;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void pushYaasConfiguration()
	{
		final List<JobModel> jobs = getJobDao().findJobs(getY2ySyncYaasConfigurationsJobCode());

		if (!jobs.isEmpty())
		{
			jobs.stream().filter(jobModel -> jobModel instanceof Y2YSyncJobModel).forEach(
					jobModel -> getSyncExecutionService().startSync(jobModel.getCode(), SyncExecutionService.ExecutionMode.ASYNC));
		}
	}


	protected SyncExecutionService getSyncExecutionService()
	{
		return syncExecutionService;
	}

	@Required
	public void setSyncExecutionService(final SyncExecutionService syncExecutionService)
	{
		this.syncExecutionService = syncExecutionService;
	}

	protected String getY2ySyncYaasConfigurationsJobCode()
	{
		return y2ySyncYaasConfigurationsJobCode;
	}

	@Required
	public void setY2ySyncYaasConfigurationsJobCode(final String y2ySyncYaasConfigurationsJobCode)
	{
		this.y2ySyncYaasConfigurationsJobCode = y2ySyncYaasConfigurationsJobCode;
	}

	protected JobDao getJobDao()
	{
		return jobDao;
	}

	@Required
	public void setJobDao(final JobDao jobDao)
	{
		this.jobDao = jobDao;
	}
}
