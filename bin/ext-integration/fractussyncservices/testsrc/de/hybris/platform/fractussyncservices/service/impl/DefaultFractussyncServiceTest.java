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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.deltadetection.ChangeDetectionService;
import de.hybris.platform.servicelayer.cronjob.JobDao;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultFractussyncServiceTest
{

	@InjectMocks
	private DefaultFractussyncService defaultFractussyncService;
	@Mock
	private SyncExecutionService syncExecutionService;
	@Mock
	private ChangeDetectionService changeDetectionService;
	@Mock
	private JobDao jobDao;
	@Mock
	private Y2YSyncJobModel y2YSyncJobModel;
	@Mock
	private ServicelayerJobModel otherJob;
	@Mock
	private Y2YStreamConfigurationContainerModel y2YStreamConfigurationContainerModel;
	@Mock
	private Y2YStreamConfigurationModel y2YStreamConfigurationModel;

	private String y2ySyncYaasConfigurationsJob;

	@Before
	public void setup()
	{
		defaultFractussyncService = new DefaultFractussyncService();
		MockitoAnnotations.initMocks(this);
		y2ySyncYaasConfigurationsJob = "jobCode";
		defaultFractussyncService.setY2ySyncYaasConfigurationsJobCode(y2ySyncYaasConfigurationsJob);

		Mockito.when(jobDao.findJobs(y2ySyncYaasConfigurationsJob)).thenReturn(Lists.newArrayList(y2YSyncJobModel, otherJob));
		Mockito.when(y2YSyncJobModel.getCode()).thenReturn(y2ySyncYaasConfigurationsJob);
	}

	@Test
	public void shouldPushYaasConfiguration()
	{
		defaultFractussyncService.pushYaasConfiguration();

		Mockito.verify(syncExecutionService, Mockito.times(1)).startSync(y2ySyncYaasConfigurationsJob,
				SyncExecutionService.ExecutionMode.ASYNC);
	}
}
