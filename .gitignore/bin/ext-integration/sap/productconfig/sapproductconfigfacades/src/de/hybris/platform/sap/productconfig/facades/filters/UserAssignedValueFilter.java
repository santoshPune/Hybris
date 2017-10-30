/**
 *
 */
package de.hybris.platform.sap.productconfig.facades.filters;

import de.hybris.platform.sap.productconfig.facades.overview.FilterEnum;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 *
 */
public class UserAssignedValueFilter extends AbstractConfigOverviewFilter
{

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
	public boolean isActive(final List<FilterEnum> appliedFilters)
	{
		return appliedFilters.contains(FilterEnum.USER_INPUT);
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
			return CsticValueModel.AUTHOR_EXTERNAL_USER.equals(value.getAuthorExternal());
		}
	}

}
