/**
 *
 */
package de.hybris.platform.sap.productconfig.facades.filters;

import de.hybris.platform.sap.productconfig.facades.ConfigOverviewFilter;
import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class OverviewFilterList
{
	private List<ConfigOverviewFilter> filters;

	/**
	 * @return the filters
	 */
	public List<ConfigOverviewFilter> getFilters()
	{
		return filters;
	}

	/**
	 * @param filters
	 *           the filters to set
	 */
	public void setFilters(final List<ConfigOverviewFilter> filters)
	{
		this.filters = filters;
	}

	/**
	 * Provides list of filter objects depending on the given list of filter IDs. <br />
	 *
	 * @param appliedFilterIDs
	 * @return list of filter objects to be applied
	 */
	public List<ConfigOverviewFilter> getAppliedFilters(final List<FilterEnum> appliedFilterIDs)
	{
		final List<ConfigOverviewFilter> appliedFilters = new ArrayList<>();
		for (final ConfigOverviewFilter filter : filters)
		{
			if (filter.isActive(appliedFilterIDs))
			{
				appliedFilters.add(filter);
			}
		}
		return appliedFilters;
	}
}
