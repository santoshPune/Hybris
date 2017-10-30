package de.hybris.platform.sap.productconfig.frontend.util.impl;

import de.hybris.platform.sap.productconfig.frontend.OverviewUiData;


@SuppressWarnings("squid:S1118")
public class ConfigOverviewFilterEvaluator
{

	public static boolean hasAppliedFilters(final OverviewUiData overviewData)
	{
		return overviewData.getCsticFilterList().stream().anyMatch(filter -> filter.isSelected())
				|| overviewData.getGroupFilterList().stream().anyMatch(filter -> filter.isSelected());
	}
}
