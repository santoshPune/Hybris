package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.ConfigurationOverviewFacade;
import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;
import de.hybris.platform.sap.productconfig.facades.populator.ConfigurationOverviewPopulator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import org.apache.log4j.Logger;


public class ConfigurationOverviewFacadeImpl implements ConfigurationOverviewFacade
{
	private ProductConfigurationService configurationService;
	private ConfigurationOverviewPopulator configurationOverviewPopulator;
	private static final Logger LOG = Logger.getLogger(ConfigurationOverviewFacadeImpl.class);

	/**
	 * @return the configurationOverviewPopulator
	 */
	public ConfigurationOverviewPopulator getConfigurationOverviewPopulator()
	{
		return configurationOverviewPopulator;
	}

	/**
	 * @param configurationOverviewPopulator
	 *           the configurationOverviewPopulator to set
	 */
	public void setConfigurationOverviewPopulator(final ConfigurationOverviewPopulator configurationOverviewPopulator)
	{
		this.configurationOverviewPopulator = configurationOverviewPopulator;
	}

	/**
	 * @return the configurationService
	 */
	public ProductConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @param configurationService
	 *           the configurationService to set
	 */
	public void setConfigurationService(final ProductConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	@Override
	public ConfigurationOverviewData getOverviewForConfiguration(final String configId,
			final ConfigurationOverviewData oldConfigOverview)
	{
		ConfigurationOverviewData configOverview = oldConfigOverview;
		if (configOverview == null)
		{
			LOG.debug("configOverview is null and a new instance has to be created");
			configOverview = new ConfigurationOverviewData();
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("cstic filters: " + configOverview.getAppliedCsticFilters().toString());
			LOG.debug("group filters: " + configOverview.getAppliedGroupFilters().toString());
		}

		final ConfigModel configModel = configurationService.retrieveConfigurationModel(configId);
		configurationOverviewPopulator.populate(configModel, configOverview);

		return configOverview;
	}

}
