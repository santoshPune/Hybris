/*
* [y] hybris Platform
*
* Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*
*/

package de.hybris.platform.yaasconfiguration.service.impl;

import static de.hybris.platform.yaasconfiguration.model.YaasApplicationModel._TYPECODE;
import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;

import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link YaasConfigurationService}
 */
public class DefaultYaasConfigurationService implements YaasConfigurationService
{
	private FlexibleSearchService flexibleSearchService;

	@Override
	public YaasApplicationModel getYaasApplicationForId(final String applicationId)
	{
		final YaasApplicationModel model = new YaasApplicationModel();
		model.setIdentifier(applicationId);
		return getFlexibleSearchService().getModelByExample(model);
	}


	@Override
	public Optional<YaasApplicationModel> takeFirstModel()
	{
		try
		{
			final FlexibleSearchQuery query = new FlexibleSearchQuery(format("select {pk} from {%s}", _TYPECODE));
			query.setCount(1);
			return ofNullable(getFlexibleSearchService().searchUnique(query));
		}
		catch (final ModelNotFoundException exception)
		{
			return empty();
		}
	}

	protected FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	@Required
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}


	@Override
	public List<YaasApplicationModel> getYaaSApplications()
	{
		List<YaasApplicationModel> yaasApplcations = new ArrayList<>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery("select {pk} from {" + YaasApplicationModel._TYPECODE + "}");
		yaasApplcations = getFlexibleSearchService().<YaasApplicationModel> search(query).getResult();
		return yaasApplcations;
	}
}
