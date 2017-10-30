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
package de.hybris.platform.fractussyncservices.cronjob;


import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.fractussyncservices.model.FractusSyncCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.y2ysync.services.SyncExecutionService;

import org.springframework.beans.factory.annotation.Required;


public class FractusSyncCronJobPerformable extends AbstractJobPerformable<FractusSyncCronJobModel>
{
	private SyncExecutionService syncExecutionService;

	@Override
	public PerformResult perform(final FractusSyncCronJobModel jobModel)
	{
		getSyncExecutionService().startSync(jobModel.getY2ySyncJob(), SyncExecutionService.ExecutionMode.ASYNC);

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
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
}
