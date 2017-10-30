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
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;


@UnitTest
public class FractusY2ySyncExecutionServiceTest
{

	@InjectMocks
	private FractusY2ySyncExecutionService service;

	@Mock
	private ChangeDetectionService changeDetectionService;

	@Mock
	private Y2YSyncJobModel y2YSyncJobModel;
	@Mock
	private Y2YStreamConfigurationContainerModel configurationContainerModel;
	@Mock
	private StreamConfigurationModel streamConfigurationModel;
	@Mock
	private Consumer consumer;
	@Mock
	private Function function;

	@Before
	public void setup()
	{

	}

	@Test
	public void shouldResetJobStreams()
	{
		service = new FractusY2ySyncExecutionService();
		MockitoAnnotations.initMocks(this);

		Mockito.when(y2YSyncJobModel.getStreamConfigurationContainer()).thenReturn(configurationContainerModel);
		Mockito.when(configurationContainerModel.getConfigurations()).thenReturn(Sets.newHashSet(streamConfigurationModel));
		Mockito.when(streamConfigurationModel.getStreamId()).thenReturn("streamId");

		service.resetJobSteams(y2YSyncJobModel);
		Mockito.verify(changeDetectionService, Mockito.times(1)).resetStream("streamId");
	}

	@Test
	public void shouldStartSyncWithResetStreams()
	{
		service = new FractusY2ySyncExecutionService()
		{

			private Consumer testFunction;

			private Function testSuper;

			@Override
			protected void resetJobSteams(final Y2YSyncJobModel job)
			{
				testFunction.accept(job);
			}

			@Override
			protected Y2YSyncCronJobModel startSyncInternal(final Y2YSyncJobModel job, final ExecutionMode executionMode)
			{
				return (Y2YSyncCronJobModel) testSuper.apply(job);
			}
		};
		MockitoAnnotations.initMocks(this);

		Mockito.when(y2YSyncJobModel.getResetStream()).thenReturn(true);

		service.startSync(y2YSyncJobModel, SyncExecutionService.ExecutionMode.ASYNC);

		Mockito.verify(consumer, Mockito.times(1)).accept(y2YSyncJobModel);
		Mockito.verify(function, Mockito.times(1)).apply(y2YSyncJobModel);
	}

	@Test
	public void shouldStartSyncWithNoResetStreams()
	{
		service = new FractusY2ySyncExecutionService()
		{

			private Consumer testFunction;

			private Function testSuper;

			@Override
			protected void resetJobSteams(final Y2YSyncJobModel job)
			{
				testFunction.accept(job);
			}

			@Override
			protected Y2YSyncCronJobModel startSyncInternal(final Y2YSyncJobModel job, final ExecutionMode executionMode)
			{
				return (Y2YSyncCronJobModel) testSuper.apply(job);
			}
		};
		MockitoAnnotations.initMocks(this);

		Mockito.when(y2YSyncJobModel.getResetStream()).thenReturn(false);

		service.startSync(y2YSyncJobModel, SyncExecutionService.ExecutionMode.ASYNC);

		Mockito.verify(consumer, Mockito.never()).accept(y2YSyncJobModel);
		Mockito.verify(function, Mockito.times(1)).apply(y2YSyncJobModel);
	}
}
