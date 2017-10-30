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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.servicelayer.cronjob.impl.DefaultCronJobService;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService;
import de.hybris.y2ysync.model.Y2YStreamConfigurationContainerModel;
import de.hybris.y2ysync.model.Y2YSyncCronJobModel;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link FractusSyncExecutionService}
 *
 */
public class DefaultFractusSyncExecutionService implements FractusSyncExecutionService
{

	private static final Logger LOG = Logger.getLogger(DefaultFractusSyncExecutionService.class);

	private Y2YSyncDAO y2YSyncDAO;
	private DefaultCronJobService cronJobService;
	protected ModelService modelService;

	@Override
	public void synchOrderStatus(final String orderCode, final String syncExecutionId)
	{
		validateParameterNotNull(orderCode, "Order code should not be null");

		Y2YSyncCronJobModel cronJob = null;

		try
		{
			cronJob = y2YSyncDAO.findSyncCronJobByCode(syncExecutionId + orderCode);
		}
		catch (final ModelNotFoundException ex)
		{
			LOG.info("No existing Cron job for the given order " + orderCode);
			final String orderStatusSyncWhereClause = "{code} IN ({{SELECT {O:code} FROM {Order AS O} WHERE {O:code}='" + orderCode
					+ "'}})";
			cronJob = createNewCronJobForOrderSync(orderCode, syncExecutionId, orderStatusSyncWhereClause);
		}

		LOG.info("Synchronising Order status with the Job id " + cronJob.getCode());

		//The given Job will be performed synchronous, which means that this method will return the control
		//only after the Job is performed.
		cronJobService.performCronJob(cronJob, true);

	}

	protected Y2YSyncCronJobModel createNewCronJobForOrderSync(final String orderCode, final String syncExecutionId,
			final String whereClause)
	{

		Y2YSyncCronJobModel cronJob = y2YSyncDAO.findSyncCronJobByCode(syncExecutionId);

		//Clone Y2YSyncCronJob
		cronJob = getModelService().clone(cronJob, Y2YSyncCronJobModel.class);
		cronJob.setCode(cronJob.getCode() + orderCode);

		// Clone Y2YSyncJob
		Y2YSyncJobModel job = cronJob.getJob();
		job = getModelService().clone(job, Y2YSyncJobModel.class);
		job.setCode(job.getCode() + orderCode);

		// Clone Y2YStreamConfigurationContainer
		final Y2YStreamConfigurationContainerModel container = getModelService().clone(job.getStreamConfigurationContainer(),
				Y2YStreamConfigurationContainerModel.class);

		container.setId(container.getId() + orderCode);

		// Amend the whereClause of the Stream configuration
		final StreamConfigurationModel stream = container.getConfigurations().iterator().next();

		stream.setStreamId(stream.getStreamId() + orderCode);
		stream.setWhereClause(appendWhereClauseWithAnd(stream.getWhereClause(), whereClause));
		getModelService().save(container);

		//associate container with Y2YSyncJob
		job.setStreamConfigurationContainer(container);
		getModelService().save(job);

		//associate Y2YSyncJob with cronJob
		cronJob.setJob(job);
		getModelService().save(cronJob);

		return cronJob;
	}

	/**
	 * Append the where clause with AND logic to the original where clause
	 */
	protected String appendWhereClauseWithAnd(final String originalWhereClause, final String appendWhereClause)
	{
		if (StringUtils.isBlank(originalWhereClause))
		{
			return appendWhereClause;
		}
		else
		{
			return originalWhereClause + " AND " + appendWhereClause;
		}
	}

	public DefaultCronJobService getCronJobService()
	{
		return cronJobService;
	}

	@Required
	public void setCronJobService(final DefaultCronJobService cronJobService)
	{
		this.cronJobService = cronJobService;
	}

	public Y2YSyncDAO getY2YSyncDAO()
	{
		return y2YSyncDAO;
	}

	@Required
	public void setY2YSyncDAO(final Y2YSyncDAO y2ySyncDAO)
	{
		y2YSyncDAO = y2ySyncDAO;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService#syncOrderShipmentDetails
	 * (java.lang. String, java.lang.String)
	 */
	@Override
	public void syncOrderShipmentDetails(final String orderCode, final String syncExecutionId)
	{
		validateParameterNotNull(orderCode, "Order code should not be null");

		Y2YSyncCronJobModel cronJob = null;

		try
		{
			cronJob = y2YSyncDAO.findSyncCronJobByCode(syncExecutionId + orderCode);
		}
		catch (final ModelNotFoundException ex)
		{
			LOG.info("No existing shipment sync Cron job for the given order " + orderCode);
			final String orderShipmentInfoSyncWhereClause = "{order} IN ({{SELECT {O:pk} FROM {Order AS O} WHERE {O:code}='"
					+ orderCode + "'}})";
			cronJob = createNewCronJobForOrderSync(orderCode, syncExecutionId, orderShipmentInfoSyncWhereClause);
		}

		LOG.info("Synchronising Order status with the Job id " + cronJob.getCode());

		//The given Job will be performed synchronous, which means that this method will return the control
		//only after the Job is performed.
		cronJobService.performCronJob(cronJob, true);

	}

}
