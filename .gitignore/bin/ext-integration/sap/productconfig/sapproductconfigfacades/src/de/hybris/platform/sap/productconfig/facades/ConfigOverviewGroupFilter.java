package de.hybris.platform.sap.productconfig.facades;

import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;

import java.util.Set;


public interface ConfigOverviewGroupFilter
{
	public Set<String> getGroupsToBeDisplayed(final InstanceModel instanceModel, final Set<String> filteredOutgroups);
}
