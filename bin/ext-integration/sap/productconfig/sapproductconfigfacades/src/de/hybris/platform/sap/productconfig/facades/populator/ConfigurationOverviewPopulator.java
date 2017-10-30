package de.hybris.platform.sap.productconfig.facades.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.sap.productconfig.facades.ConfigOverviewFilter;
import de.hybris.platform.sap.productconfig.facades.ClassificationSystemCPQAttributesProvider;
import de.hybris.platform.sap.productconfig.facades.filters.OverviewFilterList;
import de.hybris.platform.sap.productconfig.facades.overview.CharacteristicGroup;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.impl.ClassificationSystemCPQAttributesContainer;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ConfigurationOverviewPopulator implements Populator<ConfigModel, ConfigurationOverviewData>
{

	private ClassificationSystemCPQAttributesProvider nameProvider;
	private SessionAccessService sessionAccessService;

	private ConfigurationOverviewInstancePopulator configurationOverviewInstancePopulator;
	private OverviewFilterList overviewFilterList;

	/**
	 * @return the overviewFilterList
	 */
	public OverviewFilterList getOverviewFilterList()
	{
		return overviewFilterList;
	}

	/**
	 * @param overviewFilterList
	 *           the overviewFilterList to set
	 */
	public void setOverviewFilterList(final OverviewFilterList overviewFilterList)
	{
		this.overviewFilterList = overviewFilterList;
	}

	/**
	 * @return the configurationOverviewInstancePopulator
	 */
	public ConfigurationOverviewInstancePopulator getConfigurationOverviewInstancePopulator()
	{
		return configurationOverviewInstancePopulator;
	}

	/**
	 * @param configurationOverviewInstancePopulator
	 *           the configurationOverviewInstancePopulator to set
	 */
	public void setConfigurationOverviewInstancePopulator(
			final ConfigurationOverviewInstancePopulator configurationOverviewInstancePopulator)
	{
		this.configurationOverviewInstancePopulator = configurationOverviewInstancePopulator;
	}

	@Override
	public void populate(final ConfigModel source, final ConfigurationOverviewData target) throws ConversionException
	{

		final List<CharacteristicGroup> groups = new ArrayList<>();
		final Map<String, ClassificationSystemCPQAttributesContainer> nameMap = sessionAccessService.getCachedNameMap();
		final Collection<Map> options = fillOptions(target, nameMap);

		target.setId(source.getId());
		configurationOverviewInstancePopulator.populate(source.getRootInstance(), groups, options);
		target.setGroups(groups);
		sessionAccessService.putCachedNameMap(nameMap);
	}

	protected Collection<Map> fillOptions(final ConfigurationOverviewData target,
			final Map<String, ClassificationSystemCPQAttributesContainer> nameMap)
	{
		final Collection<Map> options = new ArrayList<Map>();
		final HashMap<String, Object> optionsMap = new HashMap<String, Object>();
		options.add(optionsMap);

		List<FilterEnum> filterIds = target.getAppliedCsticFilters();
		if (filterIds == null)
		{
			filterIds = new ArrayList<FilterEnum>();
		}
		final List<ConfigOverviewFilter> filters = overviewFilterList.getAppliedFilters(filterIds);
		optionsMap.put(ConfigurationOverviewInstancePopulator.APPLIED_CSTIC_FILTERS, filters);
		optionsMap.put(ConfigurationOverviewInstancePopulator.HYBRIS_NAME_MAP, nameMap);

		Set<String> filteredOutGroups = target.getAppliedGroupFilters();
		if (filteredOutGroups == null)
		{
			filteredOutGroups = new HashSet<>();
		}
		optionsMap.put(ConfigurationOverviewInstancePopulator.APPLIED_GROUP_FILTERS, filteredOutGroups);
		return options;
	}

	/**
	 * @return the hybris characteristic and value name provider
	 */
	protected ClassificationSystemCPQAttributesProvider getNameProvider()
	{
		return nameProvider;
	}

	/**
	 * @param nameProvider
	 *           hybris characteristic and value name provider
	 */
	public void setNameProvider(final ClassificationSystemCPQAttributesProvider nameProvider)
	{
		this.nameProvider = nameProvider;
	}

	/**
	 * @return the session access service
	 */
	protected SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}

	/**
	 * @param sessionAccessService
	 *           session access service
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}

}
