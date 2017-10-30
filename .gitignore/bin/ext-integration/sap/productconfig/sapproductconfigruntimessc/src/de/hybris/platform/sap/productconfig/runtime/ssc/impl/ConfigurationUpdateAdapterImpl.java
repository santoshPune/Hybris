/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.runtime.ssc.ConfigurationUpdateAdapter;
import de.hybris.platform.sap.productconfig.runtime.ssc.SolvableConflictAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.sap.custdev.projects.fbs.slc.cfg.client.IConfigSessionClient;
import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticData;
import com.sap.custdev.projects.fbs.slc.cfg.client.ICsticValueData;
import com.sap.custdev.projects.fbs.slc.cfg.command.beans.CsticValueData;
import com.sap.custdev.projects.fbs.slc.cfg.exception.IpcCommandException;


/**
 * Default implementation of {@link ConfigurationUpdateAdapter}
 */
public class ConfigurationUpdateAdapterImpl implements ConfigurationUpdateAdapter
{
	private static final Logger LOG = Logger.getLogger(ConfigurationUpdateAdapterImpl.class);
	private final SSCTimer timer = new SSCTimer();
	private SolvableConflictAdapter conflictAdapter;

	@Override
	public boolean updateConfiguration(final ConfigModel configModel, final String plainId, final IConfigSessionClient session)
	{

		final String qualifiedId = configModel.getId();
		final InstanceModel rootInstanceModel = configModel.getRootInstance();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Updating config with data: " + configModel.toString());
		}

		final boolean sscUpdated;
		try
		{
			sscUpdated = updateInstance(qualifiedId, plainId, rootInstanceModel, configModel, session);
		}
		catch (final IpcCommandException e)
		{
			throw new IllegalStateException("Could not update instance", e);
		}

		if (LOG.isDebugEnabled())
		{
			if (sscUpdated)
			{
				LOG.debug("Update for config with id: " + configModel.getId() + " executed");
			}
			else
			{
				LOG.debug("There was nothing to update for config with id: " + configModel.getId());
			}
		}
		return sscUpdated;
	}

	/**
	 * @param qualifiedId
	 * @param instanceModel
	 * @param configModel
	 * @throws IpcCommandException
	 */
	protected boolean updateInstance(final String qualifiedId, final String plainId, final InstanceModel instanceModel,
			final ConfigModel configModel, final IConfigSessionClient session) throws IpcCommandException
	{

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Update instance : InstanceID = " + instanceModel.getId());
		}

		final String rootInstanceId = instanceModel.getId();
		final List<CsticModel> csticModels = instanceModel.getCstics();

		final List<ICsticData> csticDataList = new ArrayList<>();
		final List<ICsticData> csticDataListToClear = new ArrayList<>();


		final String configId = plainId;

		boolean instanceUpdated = handleCstics(configModel, session, rootInstanceId, csticModels, csticDataList,
				csticDataListToClear, configId);

		final boolean doDeleteCstics = !csticDataListToClear.isEmpty();
		if (doDeleteCstics)
		{
			deleteCsticValues(plainId, session, rootInstanceId, csticDataListToClear);
			instanceUpdated = true;
		}

		final boolean doSetCstics = !csticDataList.isEmpty();
		if (doSetCstics)
		{
			setCsticValues(plainId, session, rootInstanceId, csticDataList);
			instanceUpdated = true;
		}

		final boolean subInstanceUpdated = updateSubInstances(qualifiedId, plainId, instanceModel, configModel, session);
		return instanceUpdated || subInstanceUpdated;
	}

	protected boolean handleCstics(final ConfigModel configModel, final IConfigSessionClient session, final String rootInstanceId,
			final List<CsticModel> csticModels, final List<ICsticData> csticDataList, final List<ICsticData> csticDataListToClear,
			final String configId) throws IpcCommandException
	{
		boolean instanceUpdated = false;
		for (final CsticModel csticModel : csticModels)
		{
			instanceUpdated = instanceUpdated
					|| handleCstic(configModel, session, rootInstanceId, csticDataList, csticDataListToClear, configId, csticModel);
		}
		return instanceUpdated;
	}

	protected boolean handleCstic(final ConfigModel configModel, final IConfigSessionClient session, final String rootInstanceId,
			final List<ICsticData> csticDataList, final List<ICsticData> csticDataListToClear, final String configId,
			final CsticModel csticModel) throws IpcCommandException
	{
		boolean instanceUpdated = false;
		if (csticModel.isChangedByFrontend())
		{

			if (LOG.isDebugEnabled())
			{
				LOG.debug("Cstic changed by Frontend: " + csticModel.toString());
			}

			if (hasBeenRetracted(csticModel, configModel, session, configId))
			{
				instanceUpdated = true;
				return instanceUpdated;
			}
			final String csticName = csticModel.getName();
			final boolean withoutDescription = true;

			timer.start("getCstic");
			final ICsticData csticData = session.getCstic(rootInstanceId, csticName, withoutDescription, configId);
			timer.stop();

			final List<String> newValues = getValuesToBeAssigned(csticModel);
			final List<String> oldValues = getValuesPreviouslyAssigned(csticData);

			final ICsticValueData[] valuesToSet = determineValuesToSet(newValues, oldValues);
			if (valuesToSet.length > 0)
			{
				csticData.setCsticValues(valuesToSet);
				csticDataList.add(csticData);
			}

			// explicitly delete old value for single-value cstics only if there is no new value
			final boolean isMultiValue = csticData.getCsticHeader().getCsticMulti().booleanValue();
			if (valuesToSet.length == 0 || isMultiValue)
			{
				final ICsticValueData[] valuesToDelete = determineValuesToDelete(newValues, oldValues);
				if (valuesToDelete.length > 0)
				{
					timer.start("getCstic");
					final ICsticData csticDataToClear = session.getCstic(rootInstanceId, csticName, withoutDescription, configId);
					timer.stop();

					csticDataToClear.setCsticValues(valuesToDelete);
					csticDataListToClear.add(csticDataToClear);
				}
			}
		}
		return instanceUpdated;
	}

	/**
	 * @param qualifiedId
	 * @param instanceModel
	 * @throws IpcCommandException
	 */
	protected boolean updateSubInstances(final String qualifiedId, final String plainId, final InstanceModel instanceModel,
			final ConfigModel configModel, final IConfigSessionClient session) throws IpcCommandException
	{

		boolean subInstanceUpdated = false;
		final List<InstanceModel> subInstances = instanceModel.getSubInstances();
		for (final InstanceModel subInstanceModel : subInstances)
		{
			final boolean instanceUpdated = updateInstance(qualifiedId, plainId, subInstanceModel, configModel, session);
			subInstanceUpdated = subInstanceUpdated || instanceUpdated;
		}
		return subInstanceUpdated;
	}

	/**
	 * Handles the retraction of a cstic
	 *
	 * @param csticModel
	 * @param configModel
	 * @param session
	 * @param configId
	 * @throws IpcCommandException
	 */
	protected boolean hasBeenRetracted(final CsticModel csticModel, final ConfigModel configModel,
			final IConfigSessionClient session, final String configId) throws IpcCommandException
	{
		if (csticModel.isRetractTriggered())
		{
			final String assumptionId = conflictAdapter.getAssumptionId(csticModel.getName(), configModel);
			if (assumptionId == null)
			{
				throw new IllegalStateException("In case a cstic is to be retracted, an assumption ID is needed");
			}
			session.deleteConflictingAssumption(configId, assumptionId);
			return true;
		}
		return false;
	}

	protected List<String> getValuesToBeAssigned(final CsticModel csticModel)
	{
		return csticModel.getAssignedValues().stream()//
				.map(a -> a.getName())//
				.collect(Collectors.toList());
	}

	protected List<String> getValuesPreviouslyAssigned(final ICsticData csticData)
	{
		return Arrays.asList(csticData.getCsticValues()).stream()//
				.filter(a -> a.getValueAssigned().booleanValue())//
				.map(a -> a.getValueName())//
				.collect(Collectors.toList());
	}

	protected ICsticValueData[] determineValuesToDelete(final List<String> newValues, final List<String> oldValues)
	{
		final List<ICsticValueData> csticValueDataList = new ArrayList<>();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cstic values to delete newValues/oldValues: " + newValues + " / " + oldValues);
		}

		for (final String value : oldValues)
		{
			if (!newValues.contains(value))
			{
				final ICsticValueData valueData = new CsticValueData();
				valueData.setValueName(value);
				csticValueDataList.add(valueData);
			}
		}

		return csticValueDataList.toArray(new ICsticValueData[csticValueDataList.size()]);

	}

	protected ICsticValueData[] determineValuesToSet(final List<String> newValues, final List<String> oldValues)
	{
		final List<ICsticValueData> csticValueDataList = new ArrayList<>();

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Cstic values to set newValues/oldValues: " + newValues + " / " + oldValues);
		}

		for (final String value : newValues)
		{
			if (!oldValues.contains(value))
			{
				final ICsticValueData valueData = new CsticValueData();
				valueData.setValueName(value);
				csticValueDataList.add(valueData);
			}
		}

		return csticValueDataList.toArray(new ICsticValueData[csticValueDataList.size()]);

	}

	/**
	 * @param qualifiedId
	 * @param rootInstanceId
	 * @param csticDataListToClear
	 * @throws IpcCommandException
	 */
	protected void deleteCsticValues(final String plainId, final IConfigSessionClient session, final String rootInstanceId,
			final List<ICsticData> csticDataListToClear) throws IpcCommandException
	{
		final ICsticData[] csticDataArray = csticDataListToClear.toArray(new ICsticData[csticDataListToClear.size()]);

		final String configId = plainId;
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Delete Cstic values : InstanceID = " + rootInstanceId + " / csticDataList = " + csticDataListToClear);
		}

		timer.start("deleteCsticValues");
		session.deleteCsticValues(rootInstanceId, "false", csticDataArray, configId);
		timer.stop();
	}


	protected void setCsticValues(final String plainId, final IConfigSessionClient session, final String rootInstanceId,
			final List<ICsticData> csticDataList) throws IpcCommandException
	{
		final ICsticData[] csticDataArray = csticDataList.toArray(new ICsticData[csticDataList.size()]);
		final String configId = plainId;

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Set Cstic values : InstanceID = " + rootInstanceId + " / csticDataList = " + csticDataList);
		}

		timer.start("setCsticsValues");
		session.setCsticsValues(rootInstanceId, configId, false, csticDataArray);
		timer.stop();
	}

	/**
	 * @param solvableConflictAdapterImpl
	 */
	public void setConflictAdapter(final SolvableConflictAdapter solvableConflictAdapterImpl)
	{
		conflictAdapter = solvableConflictAdapterImpl;

	}
}
