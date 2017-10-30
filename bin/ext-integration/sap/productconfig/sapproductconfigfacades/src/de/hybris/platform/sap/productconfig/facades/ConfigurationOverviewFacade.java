package de.hybris.platform.sap.productconfig.facades;

import de.hybris.platform.sap.productconfig.facades.overview.ConfigurationOverviewData;


/**
 * Facade for Configuration Overview.
 */
public interface ConfigurationOverviewFacade
{
	/**
	 * Determine DTO which represents the configuration overview for a configuration available in the session
	 *
	 * @param configId
	 *           Configuration ID
	 * @param overview
	 *           DTO representing overview in case it has been once determined previously. Holds applied filters. In case
	 *           not determined so far, called as null
	 * @return DTO representing configuration overview
	 */
	public ConfigurationOverviewData getOverviewForConfiguration(String configId, ConfigurationOverviewData overview);

}
