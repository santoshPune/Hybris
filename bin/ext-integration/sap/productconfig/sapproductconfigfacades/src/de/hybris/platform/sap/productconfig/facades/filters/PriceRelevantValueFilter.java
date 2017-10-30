/**
 *
 */
package de.hybris.platform.sap.productconfig.facades.filters;

import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.PriceModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 *
 */
public class PriceRelevantValueFilter extends AbstractConfigOverviewFilter
{

	@Override
	public boolean isActive(final List<FilterEnum> appliedFilters)
	{
		return appliedFilters.contains(FilterEnum.PRICE_RELEVANT);
	}

	@Override
	public List<CsticValueModel> filter(final List<CsticValueModel> values, final CsticModel cstic)
	{
		final ArrayList<CsticValueModel> filterResult = values//
				.stream() //
				.filter(new FilterPredicate()) //
				.collect(Collectors.toCollection(ArrayList::new));
		return filterResult;
	}

	@Override
	public List<CsticValueModel> noMatch(final List<CsticValueModel> values, final CsticModel cstic)
	{
		final ArrayList<CsticValueModel> filterResult = values//
				.stream() //
				.filter(new FilterPredicate().negate()) //
				.collect(Collectors.toCollection(ArrayList::new));
		return filterResult;
	}

	static class FilterPredicate implements Predicate<CsticValueModel>
	{
		@Override
		public boolean test(final CsticValueModel value)
		{
			return PriceModel.NO_PRICE != value.getDeltaPrice();
		}
	}
}
