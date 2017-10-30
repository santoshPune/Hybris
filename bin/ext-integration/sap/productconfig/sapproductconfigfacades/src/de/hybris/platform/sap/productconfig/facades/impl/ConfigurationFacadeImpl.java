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
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.ConfigConsistenceChecker;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.ConfigurationFacade;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.UiType;
import de.hybris.platform.sap.productconfig.runtime.interf.impl.KBKeyImpl;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class ConfigurationFacadeImpl extends ConfigurationBaseFacadeImpl implements ConfigurationFacade
{
	private static final Logger LOG = Logger.getLogger(ConfigurationFacadeImpl.class);

	private ProductConfigurationService configurationService;
	private ConfigConsistenceChecker configConsistenceChecker;
	private boolean conflictGroupProcessing = true;

	/**
	 * This setting is active per default but can be deactivated to ease an upgrade from previous versions.
	 *
	 * @return Are we processing conflict groups (which have been introduced in 6.0)?
	 */
	public boolean isConflictGroupProcessing()
	{
		return conflictGroupProcessing;
	}

	@Required
	public void setConfigurationService(final ProductConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Required
	public void setConfigConsistenceChecker(final ConfigConsistenceChecker configConsistenceChecker)
	{
		this.configConsistenceChecker = configConsistenceChecker;
	}

	@Override
	public ConfigurationData getConfiguration(final ConfigurationData configData)
	{
		long startTime = 0;
		final String configId = configData.getConfigId();
		if (LOG.isDebugEnabled())
		{
			startTime = System.currentTimeMillis();
			final KBKeyData kbKey = configData.getKbKey();
			String productCode = null;
			if (kbKey != null)
			{
				productCode = kbKey.getProductCode();
			}
			LOG.debug("get configuration by configId [CONFIG_ID='" + configId + "';PRODUCT_CODE='" + productCode + "']");
		}

		final ConfigModel configModel = configurationService.retrieveConfigurationModel(configId);

		final List<UiGroupData> csticGroupsFlat = new ArrayList<>();
		final List<UiGroupData> csticGroups = getCsticGroupsFromModel(configModel, csticGroupsFlat);
		configData.setGroups(csticGroups);
		configData.setCsticGroupsFlat(csticGroupsFlat);
		finalizeUiGroups(configData, configModel);

		configData.setShowLegend(isShowLegend(configData.getGroups()));

		final PricingData pricingData = getConfigPricing().getPricingData(configModel);
		configData.setPricing(pricingData);

		configConsistenceChecker.checkConfiguration(configData);
		configData.setConsistent(configModel.isConsistent());
		configData.setComplete(configModel.isComplete());
		configData.setSingleLevel(configModel.isSingleLevel());
		if (LOG.isDebugEnabled())
		{
			final long duration = System.currentTimeMillis() - startTime;
			LOG.debug("GET CONFIG in FACADE took " + duration + " ms");
		}
		return configData;
	}






	@Override
	public ConfigurationData getConfiguration(final KBKeyData kbKey)
	{

		long startTime = 0;
		if (LOG.isDebugEnabled())
		{
			startTime = System.currentTimeMillis();
			LOG.debug("get configuration by kbkey [PRODUCT_CODE='" + kbKey.getProductCode() + "']");
		}

		final ConfigModel configModel = configurationService.createDefaultConfiguration(
				new KBKeyImpl(kbKey.getProductCode(), kbKey.getKbName(), kbKey.getKbLogsys(), kbKey.getKbVersion()));

		final ConfigurationData configData = convert(kbKey, configModel);
		if (LOG.isDebugEnabled())
		{
			final long duration = System.currentTimeMillis() - startTime;
			LOG.debug("GET CONFIG in FACADE took " + duration + " ms");
		}
		return configData;
	}


	@Override
	public void updateConfiguration(final ConfigurationData configContent)
	{
		long startTime = 0;
		final String configId = configContent.getConfigId();
		if (LOG.isDebugEnabled())
		{
			startTime = System.currentTimeMillis();
			LOG.debug("update configuration [CONFIG_ID='" + configId + "';PRODUCT_CODE='" + configContent.getKbKey().getProductCode()
					+ "']");
		}

		final ConfigModel configModel = configurationService.retrieveConfigurationModel(configId);

		final PricingData pricingData = getConfigPricing().getPricingData(configModel);
		configContent.setPricing(pricingData);

		final InstanceModel rootInstance = configModel.getRootInstance();

		if (configContent.getGroups() != null)
		{
			for (final UiGroupData uiGroup : configContent.getGroups())
			{
				updateUiGroup(rootInstance, uiGroup);
			}
		}

		configurationService.updateConfiguration(configModel);
		if (LOG.isDebugEnabled())
		{
			final long duration = System.currentTimeMillis() - startTime;
			LOG.debug("UPDATE in FACADE took " + duration + " ms");
		}
	}


	protected void updateUiGroup(final InstanceModel instance, final UiGroupData uiGroup)
	{

		final GroupType groupType = uiGroup.getGroupType() != null ? uiGroup.getGroupType() : GroupType.INSTANCE;

		switch (groupType)
		{
			case CSTIC_GROUP:
				// cstic group
				updateCsticGroup(instance, uiGroup);
				break;
			case INSTANCE:
				// (sub)instance
				updateSubInstances(instance, uiGroup);
				break;
			case CONFLICT:
				updateConflictGroup(instance, uiGroup);
				break;
			case CONFLICT_HEADER:
				updateConflictHeader(instance, uiGroup);
				break;
			default:
				throw new IllegalArgumentException("Group type not supported: " + groupType);
		}
	}

	protected void updateConflictHeader(final InstanceModel instance, final UiGroupData uiGroup)
	{
		final List<UiGroupData> conflictGroups = uiGroup.getSubGroups();

		if (instance != null && conflictGroups != null)
		{
			for (final UiGroupData uiSubGroup : conflictGroups)
			{
				updateUiGroup(instance, uiSubGroup);
			}
		}
	}

	protected void updateSubInstances(final InstanceModel instance, final UiGroupData uiGroup)
	{
		final InstanceModel subInstance = retrieveRelatedInstanceModel(instance, uiGroup);
		updateConflictHeader(subInstance, uiGroup);
	}


	protected void updateConflictGroup(final InstanceModel instance, final UiGroupData uiGroup)
	{
		if (!isConflictGroupProcessing())
		{
			return;
		}

		for (final CsticData cstic : uiGroup.getCstics())
		{
			if (cstic.getType() != UiType.NOT_IMPLEMENTED)
			{
				final InstanceModel instanceCarryingTheConflict = getSubInstance(instance, cstic.getInstanceId());
				if (instanceCarryingTheConflict == null)
				{
					throw new IllegalStateException("No instance found for id: " + cstic.getInstanceId());
				}
				updateCsticModelFromCsticData(instanceCarryingTheConflict, cstic);
			}
		}
	}



	InstanceModel getSubInstance(final InstanceModel instance, final String instanceId)
	{
		final String id = instance.getId();
		if (id != null && id.equals(instanceId))
		{
			return instance;
		}
		for (final InstanceModel subInstance : instance.getSubInstances())
		{
			final InstanceModel foundInstance = getSubInstance(subInstance, instanceId);
			if (foundInstance != null)
			{
				return foundInstance;
			}
		}
		return null;
	}



	protected InstanceModel retrieveRelatedInstanceModel(final InstanceModel instance, final UiGroupData uiSubGroup)
	{
		InstanceModel instToReturn = null;
		final String uiGroupId = uiSubGroup.getId();
		if (uiGroupId != null)
		{
			final String instanceId = UiGroupHelperImpl.retrieveInstanceId(uiGroupId);
			final List<InstanceModel> subInstances = instance.getSubInstances();
			for (final InstanceModel subInstance : subInstances)
			{
				if (subInstance.getId().equals(instanceId))
				{
					instToReturn = subInstance;
					break;
				}
			}
		}
		return instToReturn;
	}

	protected void updateCsticGroup(final InstanceModel instance, final UiGroupData csticGroup)
	{
		// we need this check for null, in the model the empty lists will be changed to null
		if (csticGroup != null && csticGroup.getCstics() != null)
		{
			for (final CsticData csticData : csticGroup.getCstics())
			{
				if (csticData.getType() != UiType.NOT_IMPLEMENTED)
				{
					updateCsticModelFromCsticData(instance, csticData);
				}
			}
		}
	}

	protected void updateCsticModelFromCsticData(final InstanceModel instance, final CsticData csticData)
	{
		final String csticName = csticData.getName();
		final CsticModel cstic = instance.getCstic(csticName);
		if (cstic == null)
		{
			throw new IllegalStateException("No cstic available at instance " + instance.getId() + " : " + csticName);
		}
		if (cstic.isChangedByFrontend())
		{
			return;
		}
		getCsticTypeMapper().updateCsticModelValuesFromData(csticData, cstic);
	}



	protected ProductConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	protected ConfigConsistenceChecker getConfigConsistenceChecker()
	{
		return configConsistenceChecker;
	}



	/**
	 * @param b
	 *           Is conflict group processing active?
	 */
	public void setConflictGroupProcessing(final boolean b)
	{
		this.conflictGroupProcessing = b;
	}

	@Override
	public int getNumberOfErrors(final String configId)
	{
		return getConfigurationService().calculateNumberOfIncompleteCsticsAndSolvableConflicts(configId);
	}
}
