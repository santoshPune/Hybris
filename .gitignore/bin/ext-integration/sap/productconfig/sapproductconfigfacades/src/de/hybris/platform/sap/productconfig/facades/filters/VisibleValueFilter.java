/**
 *
 */
package de.hybris.platform.sap.productconfig.facades.filters;

import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class VisibleValueFilter extends AbstractConfigOverviewFilter
{

	@Override
	public boolean isActive(final List<FilterEnum> appliedFilters)
	{
		return appliedFilters.contains(FilterEnum.VISIBLE);
	}


	@Override
	public List<CsticValueModel> filter(final List<CsticValueModel> values, final CsticModel cstic)
	{
		return cstic.isVisible() ? new ArrayList<>(values) : Collections.emptyList();
	}


	@Override
	public List<CsticValueModel> noMatch(final List<CsticValueModel> values, final CsticModel cstic)
	{
		return cstic.isVisible() ? Collections.emptyList() : new ArrayList<>(values);
	}

}
