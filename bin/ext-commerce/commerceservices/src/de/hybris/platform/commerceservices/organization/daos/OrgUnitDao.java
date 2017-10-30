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
package de.hybris.platform.commerceservices.organization.daos;

import de.hybris.platform.commerceservices.model.OrgUnitModel;
import de.hybris.platform.commerceservices.search.dao.PagedGenericDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.security.PrincipalModel;


/**
 * Interface for {@link OrgUnitModel} related paged data access.
 */
public interface OrgUnitDao<T extends OrgUnitModel> extends PagedGenericDao<T>
{
	/**
	 * Find all unit member of a given type.
	 *
	 * @param unit
	 *           the unit to find all members for
	 * @param memberType
	 *           the model type of the members to find
	 * @param pageableData
	 *           paging information
	 * @return paged search results
	 */
	<T extends PrincipalModel> SearchPageData<T> findMembersOfType(final OrgUnitModel unit, Class<T> memberType,
			PageableData pageableData);
}
