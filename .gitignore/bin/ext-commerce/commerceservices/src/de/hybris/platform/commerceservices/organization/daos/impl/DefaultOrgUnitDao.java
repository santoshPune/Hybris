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
 *
 */
package de.hybris.platform.commerceservices.organization.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.organization.daos.OrgUnitDao;
import de.hybris.platform.commerceservices.search.dao.impl.DefaultPagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link OrgUnitDao} interface extending {@link DefaultPagedGenericDao}.
 */
public class DefaultOrgUnitDao<T extends OrgUnitModel> extends DefaultPagedGenericDao<T> implements OrgUnitDao<T>
{
	public DefaultOrgUnitDao(final String typeCode)
	{
		super(typeCode);
	}

	private ModelService modelService;

	@Override
	public <U extends PrincipalModel> SearchPageData<U> findMembersOfType(final OrgUnitModel unit, final Class<U> memberType,
			final PageableData pageableData)
	{
		validateParameterNotNullStandardMessage("unit", unit);
		validateParameterNotNullStandardMessage("memberType", memberType);
		validateParameterNotNullStandardMessage("pageableData", pageableData);

		final StringBuilder sql = new StringBuilder();
		sql.append("SELECT {m:pk}	");
		sql.append("FROM	");
		sql.append("{	");
		sql.append("OrgUnit as unit ");
		sql.append("	JOIN PrincipalGroupRelation as unit_rel ");
		sql.append("	ON   {unit_rel:target} = {unit:pk} ");
		sql.append("	JOIN ").append(getModelService().getModelType(memberType)).append(" as m ");
		sql.append("	ON   {m:pk} = {unit_rel:source} ");
		sql.append("} ");
		sql.append("WHERE {unit:pk} = ?unit");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(sql.toString());
		query.getQueryParameters().put("unit", unit);
		query.setNeedTotal(true);
		query.setCount(pageableData.getPageSize());
		query.setStart(pageableData.getCurrentPage() * pageableData.getPageSize());

		return getPagedFlexibleSearchService().search(query, pageableData);
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

}
