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
 */
package de.hybris.platform.cmswebservices.synchronization.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cmswebservices.common.facade.validator.FacadeValidationService;
import de.hybris.platform.cmswebservices.data.SyncJobData;
import de.hybris.platform.cmswebservices.data.SyncJobRequestData;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Date;
import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultSynchronizationFacadeTest
{
	private static final String ONLINE = "online";
	private static final String STAGED = "staged";
	private static final String APPAREL_UK = "apparel-uk";

	@InjectMocks
	@Spy
	private DefaultSynchronizationFacade defaultSynchronizationFacade;

	@Mock
	private CatalogVersionService catalogVersionService;

	@Mock
	private Converter<Optional<CronJobModel>, SyncJobData> convertor;

	@Mock
	private FacadeValidationService facadeValidationService;

	@Spy
	private SyncItemJobModel syncItemJobModel;

	@Spy
	private CatalogVersionModel source;

	private CronJobModel firstCronJob;

	private CronJobModel secondCronJob;

	private CronJobModel thirdCronJob;


	@Before
	public void setup()
	{
		final CatalogVersionModel target = new CatalogVersionModel();
		target.setVersion(ONLINE);
		syncItemJobModel.setTargetVersion(target);
		syncItemJobModel.setActive(true);
		source.setSynchronizations(Lists.newArrayList(syncItemJobModel));

		firstCronJob = new CronJobModel();
		firstCronJob.setStatus(CronJobStatus.RUNNING);
		firstCronJob.setResult(CronJobResult.UNKNOWN);
		firstCronJob.setEndTime(DateTime.now().toDate());
		firstCronJob.setCreationtime(DateTime.now().toDate());
		firstCronJob.setStartTime(DateTime.now().toDate());

		firstCronJob.setModifiedtime(DateTime.now().toDate());

		secondCronJob = new CronJobModel();
		secondCronJob.setStatus(CronJobStatus.UNKNOWN);
		secondCronJob.setModifiedtime(DateTime.now().plusMinutes(1).toDate());
		firstCronJob.setResult(CronJobResult.UNKNOWN);
		firstCronJob.setEndTime(DateTime.now().plusMinutes(1).toDate());
		firstCronJob.setCreationtime(DateTime.now().plusMinutes(1).toDate());
		firstCronJob.setStartTime(DateTime.now().plusMinutes(1).toDate());

		thirdCronJob = new CronJobModel();
		thirdCronJob.setStatus(CronJobStatus.PAUSED);
		thirdCronJob.setModifiedtime(DateTime.now().plusMinutes(2).toDate());
		firstCronJob.setResult(CronJobResult.UNKNOWN);
		firstCronJob.setEndTime(DateTime.now().plusMinutes(2).toDate());
		firstCronJob.setCreationtime(DateTime.now().plusMinutes(2).toDate());
		firstCronJob.setStartTime(DateTime.now().plusMinutes(2).toDate());

		doNothing().when(facadeValidationService).validate(Mockito.any(), Mockito.any());
	}

	@Test
	public void shouldGetStatus()
	{
		final SyncJobRequestData request = createRequestData();
		when(catalogVersionService.getCatalogVersion(APPAREL_UK, STAGED)).thenReturn(source);
		final SyncJobData value = createSyncJobData(CronJobStatus.RUNNING.name(), new Date(),CronJobResult.UNKNOWN.name(),new Date(),new Date(),new Date(),new Date());
		when(convertor.convert(Mockito.any())).thenReturn(value);

		when(syncItemJobModel.getCronJobs()).thenReturn(Lists.newArrayList(firstCronJob));

		final SyncJobData result = defaultSynchronizationFacade.getSynchronizationByCatalogSourceTarget(request);

		//asserts
		verify(defaultSynchronizationFacade).findTheCronJob(APPAREL_UK, STAGED, ONLINE);
		verify(defaultSynchronizationFacade.getSyncJobConverter()).convert(Optional.of(firstCronJob));

		assertEquals(CronJobStatus.RUNNING.name(), result.getSyncStatus());
		assertEquals(CronJobResult.UNKNOWN.name(), result.getSyncResult());
		assertNotNull(result.getCreationDate());
		assertNotNull(result.getEndDate());
		assertNotNull(result.getStartDate());
		assertNotNull(result.getLastModifiedDate());


	}


	@Test
	public void shouldNotHaveCronJobs()
	{
		final SyncJobRequestData syncJobRequest = createRequestData();
		when(catalogVersionService.getCatalogVersion(APPAREL_UK, STAGED)).thenReturn(source);
		when(convertor.convert(Mockito.any())).thenReturn(createSyncJobData(null, null,null, null,null, null, null));
		when(syncItemJobModel.getCronJobs()).thenReturn(Lists.newArrayList());

		final SyncJobData result = defaultSynchronizationFacade.getSynchronizationByCatalogSourceTarget(syncJobRequest);

		//asserts
		verify(defaultSynchronizationFacade).findTheCronJob(APPAREL_UK, STAGED, ONLINE);
		verify(defaultSynchronizationFacade.getSyncJobConverter()).convert(Optional.empty());

		assertNull(result.getSyncResult());
		assertNull(result.getSyncStatus());
		assertNull(result.getCreationDate());
		assertNull(result.getEndDate());
		assertNull(result.getStartDate());
		assertNull(result.getLastModifiedDate());

	}

	@Test
	public void shouldNotHaveSynchronization_forCatalog()
	{
		final SyncJobRequestData request = createRequestData();
		when(catalogVersionService.getCatalogVersion(APPAREL_UK, STAGED)).thenReturn(source);

		//empty sync list
		source.setSynchronizations(Lists.newArrayList());

		when(convertor.convert(Mockito.any())).thenReturn(createSyncJobData(null, null, null, null, null, null, null));
		final SyncJobData result = defaultSynchronizationFacade.getSynchronizationByCatalogSourceTarget(request);

		//asserts
		verify(defaultSynchronizationFacade).findTheCronJob(APPAREL_UK, STAGED, ONLINE);
		verify(defaultSynchronizationFacade.getSyncJobConverter()).convert(Optional.empty());
	}

	@Test
	public void shouldHaveMultipleCronJobs()
	{
		final SyncJobRequestData request = createRequestData();
		when(catalogVersionService.getCatalogVersion(APPAREL_UK, STAGED)).thenReturn(source);


		when(convertor.convert(Mockito.any())).thenReturn(createSyncJobData(CronJobStatus.PAUSED.name(), DateTime.now().toDate(), CronJobResult.UNKNOWN.name(), DateTime.now().toDate(), DateTime.now().toDate(), DateTime.now().toDate(), DateTime.now().toDate()));

		when(syncItemJobModel.getCronJobs()).thenReturn(Lists.newArrayList(secondCronJob, thirdCronJob, firstCronJob));

		defaultSynchronizationFacade.getSynchronizationByCatalogSourceTarget(request);

		//asserts
		verify(defaultSynchronizationFacade).findTheCronJob(APPAREL_UK, STAGED, ONLINE);
		verify(defaultSynchronizationFacade.getSyncJobConverter()).convert(Optional.of(thirdCronJob));
	}

	private SyncJobData createSyncJobData(final String status, final Date lastModified,final String syncResult, final Date startDate, final Date creationDate, final Date modifiedDate, final Date endDate)
	{
		final SyncJobData syncJobData = new SyncJobData();
		syncJobData.setSyncStatus(status);
		syncJobData.setLastModifiedDate(lastModified);
		syncJobData.setCreationDate(creationDate);
		syncJobData.setEndDate(endDate);
		syncJobData.setStartDate(startDate);
		syncJobData.setSyncResult(syncResult);
		return syncJobData;
	}

	private SyncJobRequestData createRequestData()
	{
		final SyncJobRequestData syncJobRequest = new SyncJobRequestData();
		syncJobRequest.setCatalogId(APPAREL_UK);
		syncJobRequest.setSourceVersionId(STAGED);
		syncJobRequest.setTargetVersionId(ONLINE);
		return syncJobRequest;
	}
}
