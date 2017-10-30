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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.daos.ProductDao;
import de.hybris.platform.sap.productconfig.facades.ConfigPricing;
import de.hybris.platform.sap.productconfig.facades.ConfigurationData;
import de.hybris.platform.sap.productconfig.facades.CsticData;
import de.hybris.platform.sap.productconfig.facades.CsticTypeMapper;
import de.hybris.platform.sap.productconfig.facades.FirstOrLastGroupType;
import de.hybris.platform.sap.productconfig.facades.GroupType;
import de.hybris.platform.sap.productconfig.facades.KBKeyData;
import de.hybris.platform.sap.productconfig.facades.PricingData;
import de.hybris.platform.sap.productconfig.facades.UiGroupData;
import de.hybris.platform.sap.productconfig.facades.populator.SolvableConflictPopulator;
import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Base functions for configuration facades, e.g. capabilities to create DTO representation of a configuration from the
 * model representation. <br>
 * Also see {@link ConfigurationFacadeImpl} and {@link ConfigurationCartIntegrationFacadeImpl}
 */
public class ConfigurationBaseFacadeImpl
{
	private static final Logger LOG = Logger.getLogger(ConfigurationBaseFacadeImpl.class);
	private ConfigPricing configPricing;
	private ProductDao productDao;
	private CsticTypeMapper csticTypeMapper;

	private SessionAccessService sessionAccessService;


	private SolvableConflictPopulator conflictPopulator;

	/**
	 * @return the conflictPopulator
	 */
	public SolvableConflictPopulator getConflictPopulator()
	{
		return conflictPopulator;
	}

	/**
	 * @param conflictPopulator
	 *           the conflictPopulator to set
	 */
	public void setConflictPopulator(final SolvableConflictPopulator conflictsPopulator)
	{
		this.conflictPopulator = conflictsPopulator;
	}



	/**
	 * Setter for pricing bean
	 *
	 * @param configPricing
	 */
	@Required
	public void setConfigPricing(final ConfigPricing configPricing)
	{
		this.configPricing = configPricing;
	}

	/**
	 * Setter for product data access object
	 *
	 * @param productDao
	 */
	@Required
	public void setProductDao(final ProductDao productDao)
	{
		this.productDao = productDao;
	}

	/**
	 * Setter for characteristic type mapper
	 *
	 * @param csticTypeMapper
	 */
	@Required
	public void setCsticTypeMapper(final CsticTypeMapper csticTypeMapper)
	{
		this.csticTypeMapper = csticTypeMapper;
	}

	/**
	 * @return Product access object
	 */
	protected ProductDao getProductDao()
	{
		return productDao;
	}

	/**
	 * @return Factory for pricing data for product configuration
	 */
	protected ConfigPricing getConfigPricing()
	{
		return configPricing;
	}

	/**
	 * Converts a configuration model to its DTO representation
	 *
	 * @param kbKey
	 * @param configModel
	 * @return DTO representation of model
	 */
	protected ConfigurationData convert(final KBKeyData kbKey, final ConfigModel configModel)
	{
		final ConfigurationData configData = new ConfigurationData();
		configData.setKbKey(kbKey);
		configData.setConfigId(configModel.getId());
		configData.setConsistent(configModel.isConsistent());
		configData.setComplete(configModel.isComplete());
		configData.setSingleLevel(configModel.isSingleLevel());

		final PricingData pricingData = configPricing.getPricingData(configModel);
		configData.setPricing(pricingData);

		final List<UiGroupData> csticGroupsFlat = new ArrayList<>();
		final List<UiGroupData> csticGroups = getCsticGroupsFromModel(configModel, csticGroupsFlat);

		configData.setGroups(csticGroups);
		configData.setCsticGroupsFlat(csticGroupsFlat);

		finalizeUiGroups(configData, configModel);


		if (LOG.isDebugEnabled())
		{
			LOG.debug("Configuration has " + csticGroups.size() + " group(s)");
		}

		final boolean showLegend = isShowLegend(configData.getGroups());
		configData.setShowLegend(showLegend);
		return configData;
	}

	protected void finalizeUiGroups(final ConfigurationData configData, final ConfigModel configModel)
	{
		applyAdditionalPopulators(configData, configModel);
		// Mark the first and last group or the only one.
		markFirstAndLastGroup(configData.getCsticGroupsFlat());
	}

	/**
	 * This method is used to apply populators which translate {@link ConfigModel} into {@link ConfigurationData}. In
	 * this default implementation, {@link SolvableConflictPopulator } is applied.
	 *
	 * @param configData
	 * @param configModel
	 */
	protected void applyAdditionalPopulators(final ConfigurationData configData, final ConfigModel configModel)
	{
		getConflictPopulator().populate(configModel, configData);
	}

	/**
	 * Reads characteristic groups from model representation of configuration
	 *
	 * @param configModel
	 *           Configuration
	 * @param csticGroupsFlat
	 *           flat list of cstic groups in correct order
	 * @return List of UI group DTO representations
	 */
	protected List<UiGroupData> getCsticGroupsFromModel(final ConfigModel configModel, final List<UiGroupData> csticGroupsFlat)
	{

		final Map<String, ClassificationSystemCPQAttributesContainer> hybrisNamesMap = getSessionAccessService().getCachedNameMap();
		final List<UiGroupData> csticGroups = getGroupsFromInstance(configModel.getRootInstance(), hybrisNamesMap, csticGroupsFlat,
				0);

		getSessionAccessService().putCachedNameMap(hybrisNamesMap);
		return csticGroups;
	}

	/**
	 * Marks the first and last cstic-group of the whole model. <br/>
	 * If only one group exists, mark the group as "only one".
	 *
	 * @param csticGroupsFlat
	 */
	protected void markFirstAndLastGroup(final List<UiGroupData> csticGroupsFlat)
	{
		if (csticGroupsFlat != null && !csticGroupsFlat.isEmpty())
		{
			final int numberOfCsticGroups = csticGroupsFlat.size();
			if (numberOfCsticGroups == 1)
			{
				// Only one group exists
				csticGroupsFlat.get(0).setFirstOrLastGroup(FirstOrLastGroupType.ONLYONE);
			}
			else
			{
				// More than one group exists
				csticGroupsFlat.get(0).setFirstOrLastGroup(FirstOrLastGroupType.FIRST);
				csticGroupsFlat.get(numberOfCsticGroups - 1).setFirstOrLastGroup(FirstOrLastGroupType.LAST);
			}
		}
	}

	/**
	 * @param groups
	 *           List of UI groups, DTO representation
	 * @return true is at least one mandatory cstic exists
	 */
	protected boolean isShowLegend(final List<UiGroupData> groups)
	{
		if (groups == null || groups.isEmpty())
		{
			return false;
		}

		for (final UiGroupData group : groups)
		{
			for (final CsticData cstic : group.getCstics())
			{
				if (cstic.isRequired())
				{
					return true;
				}
			}

			if (isShowLegend(group.getSubGroups()))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Reads groups per instance
	 *
	 * @param instance
	 *           Instance model
	 * @param nameMap
	 *           cache
	 * @param csticGroupsFlat
	 *           flat list of cstic groups in correct order
	 * @param level
	 * @return List of UI groups
	 */
	protected List<UiGroupData> getGroupsFromInstance(final InstanceModel instance,
			final Map<String, ClassificationSystemCPQAttributesContainer> nameMap, final List<UiGroupData> csticGroupsFlat, final int level)
	{
		final int nextLevel = level + 1;
		final List<UiGroupData> csticGroups = new ArrayList<>();

		final List<CsticGroup> csticModelGroups = instance.retrieveCsticGroupsWithCstics();
		for (final CsticGroup csticModelGroup : csticModelGroups)
		{
			final UiGroupData csticDataGroup = createCsticGroup(csticModelGroup, instance, nameMap);
			if (csticDataGroup.getCstics() == null || csticDataGroup.getCstics().isEmpty())
			{
				continue;
			}
			csticDataGroup.setConfigurable(true);
			csticGroups.add(csticDataGroup);
			csticGroupsFlat.add(csticDataGroup);
		}

		final List<InstanceModel> subInstances = instance.getSubInstances();
		for (final InstanceModel subInstance : subInstances)
		{
			final UiGroupData uiGroup = createUiGroup(subInstance, nameMap, csticGroupsFlat, nextLevel);
			csticGroups.add(uiGroup);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("get subgroups for instance [ID='" + instance.getId() + "';NAME='" + instance.getName() + "';NUM_GROUPS='"
					+ csticGroups.size() + "']");
		}
		return csticGroups;
	}

	/**
	 * Creates a new UI group based on the characteristic group model
	 *
	 * @param csticModelGroup
	 *           Model representation of characteristic group
	 * @param prefix
	 *           Is put as prefix to the group name to calculate the groups identifiert
	 * @param nameMap
	 * @return UI group
	 */
	protected UiGroupData createCsticGroup(final CsticGroup csticModelGroup, final InstanceModel instance,
			final Map<String, ClassificationSystemCPQAttributesContainer> nameMap)
	{
		final UiGroupData uiGroupData = new UiGroupData();

		// cstic group name is unique (inside an instance), there is no cstic group id
		// For ui groups we can use the cstic group name as ui group id as well (additional to the ui group name)
		final String csticGroupName = csticModelGroup.getName();
		final String groupKey = UiGroupHelperImpl.generateGroupIdForGroup(instance, csticModelGroup);
		uiGroupData.setId(groupKey);
		uiGroupData.setName(csticGroupName);

		uiGroupData.setDescription(csticModelGroup.getDescription());
		uiGroupData.setGroupType(GroupType.CSTIC_GROUP);
		uiGroupData.setFirstOrLastGroup(FirstOrLastGroupType.INTERJACENT);

		uiGroupData.setCstics(getListOfCsticData(csticModelGroup.getCstics(), groupKey, nameMap));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("create UI group for csticGroup [NAME='" + csticModelGroup.getName() + "';GROUP_PREFIX='" + groupKey
					+ "';CSTICS_IN_GROUP='" + uiGroupData.getCstics().size() + "']");
		}

		return uiGroupData;
	}

	/**
	 * Creates an UI group from an instance model.
	 *
	 * @param instance
	 *           Model representation of an instance
	 * @param nameMap
	 * @param csticGroupsFlat
	 *           flat list of cstic groups in correct order
	 * @param level
	 * @return UI group, as transformation result of the (sub) instance
	 */
	protected UiGroupData createUiGroup(final InstanceModel instance, final Map<String, ClassificationSystemCPQAttributesContainer> nameMap,
			final List<UiGroupData> csticGroupsFlat, final int level)
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug("create UI group for instance [ID='" + instance.getId() + "';NAME='" + instance.getName() + "']");
		}

		final UiGroupData uiGroup = new UiGroupData();

		final String groupId = UiGroupHelperImpl.generateGroupIdForInstance(instance);
		final String groupName = instance.getName();
		uiGroup.setId(groupId);
		uiGroup.setName(groupName);
		uiGroup.setDescription(instance.getLanguageDependentName());
		uiGroup.setCollapsed(true);
		uiGroup.setCollapsedInSpecificationTree(level > 1);

		//retrieve (sub)instance product description from catalog if available
		final List<ProductModel> products = productDao.findProductsByCode(groupName);

		if (products != null && products.size() == 1)
		{
			final ProductModel product = products.get(0);
			final String productName = product.getName();
			if (productName != null && !productName.isEmpty())
			{
				uiGroup.setDescription(productName);
			}
			final String summaryText = product.getSummary();
			uiGroup.setSummaryText(summaryText);
		}

		// if no group (subinstance) language dependent description available at all, use the subinstance name
		if (uiGroup.getDescription() == null || uiGroup.getDescription().isEmpty())
		{
			uiGroup.setDescription("[" + groupName + "]");
		}

		uiGroup.setGroupType(GroupType.INSTANCE);

		final List<UiGroupData> subGroups = getGroupsFromInstance(instance, nameMap, csticGroupsFlat, level);
		uiGroup.setSubGroups(subGroups);
		uiGroup.setCstics(new ArrayList<CsticData>());
		uiGroup.setConfigurable(isUiGroupConfigurable(subGroups));
		final boolean oneSubGroupConfigurable = isOneSubGroupConfigurable(subGroups);
		checkAdoptSubGroup(uiGroup, subGroups, oneSubGroupConfigurable);
		uiGroup.setOneConfigurableSubGroup(oneSubGroupConfigurable);
		return uiGroup;
	}

	/**
	 * Adapts sub group if it is the only configurable child of the current group, and if no other child exists
	 *
	 * @param uiGroup
	 * @param subGroups
	 * @param oneSubGroupConfigurable
	 */
	void checkAdoptSubGroup(final UiGroupData uiGroup, final List<UiGroupData> subGroups, final boolean oneSubGroupConfigurable)
	{
		if (oneSubGroupConfigurable && subGroups.size() == 1)
		{
			final UiGroupData childGroup = subGroups.get(0);
			//We need to change the description, as it should carry the one from the parent
			childGroup.setDescription(uiGroup.getDescription());
			//We need to change the name as well, as it is used to tell whether a group is a 'general' group with specific treatment
			childGroup.setName(uiGroup.getName());
		}
	}

	protected List<CsticData> getListOfCsticData(final List<CsticModel> csticModelList, final String prefix,
			final Map<String, ClassificationSystemCPQAttributesContainer> nameMap)
	{
		final List<CsticData> cstics = new ArrayList<>();
		for (final CsticModel model : csticModelList)
		{
			if (!model.isVisible())
			{
				continue;
			}

			cstics.add(csticTypeMapper.mapCsticModelToData(model, prefix, nameMap));
		}
		return cstics;
	}

	protected boolean isUiGroupConfigurable(final List<UiGroupData> subGroups)
	{
		if (subGroups == null || subGroups.isEmpty())
		{
			return false;
		}

		for (final UiGroupData uiGroup : subGroups)
		{
			if (uiGroup.isConfigurable())
			{
				return true;
			}

		}
		return false;
	}

	protected boolean isOneSubGroupConfigurable(final List<UiGroupData> subGroups)
	{
		if (subGroups == null || subGroups.isEmpty())
		{
			return false;
		}

		int numberOfConfigurableGroups = 0;

		for (final UiGroupData uiGroup : subGroups)
		{
			if (uiGroup.isConfigurable() && ++numberOfConfigurableGroups > 1)
			{
				return false;
			}

		}

		return numberOfConfigurableGroups == 1;

	}

	/**
	 * @return Characteristics mapper
	 */
	protected CsticTypeMapper getCsticTypeMapper()
	{
		return csticTypeMapper;
	}

	/**
	 * @return the sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}

	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}

}
