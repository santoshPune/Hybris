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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.dao.impl;

import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.dao.YaasOrderStatusDao;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.model.YaasOrderStatusMapModel;

import java.util.HashMap;
import java.util.Map;


public class DefaultYaasOrderStatusDao extends DefaultGenericDao<YaasOrderStatusMapModel> implements YaasOrderStatusDao
{
	/**
	 * Default constructor needed in class
	 */
	public DefaultYaasOrderStatusDao()
	{
		super(YaasOrderStatusMapModel._TYPECODE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.orderstatus.dao.YaasOrderStatusDao#findYaasOrderStatus(de.hybris.platform.core.enums.
	 * OrderStatus )
	 */
	@Override
	public YaasOrderStatusMapModel findYaasOrderStatus(final String ecpOrderStatus)
	{
		final StringBuilder queryString = new StringBuilder(100);
		queryString.append("SELECT {s:").append(YaasOrderStatusMapModel.PK).append("} FROM { ")
				.append(YaasOrderStatusMapModel._TYPECODE).append(" AS s ");
		queryString.append("} WHERE (");
		queryString.append("{s:").append(YaasOrderStatusMapModel.ECPORDERSTATUS).append("} = ?orderStatus").append(" ) ");

		final Map<String, Object> attributes = new HashMap();
		attributes.put("orderStatus", ecpOrderStatus);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());
		query.getQueryParameters().putAll(attributes);

		final SearchResult<YaasOrderStatusMapModel> result = this.getFlexibleSearchService().search(query);

		if (result == null || result.getCount() == 0)
		{
			return null;
		}

		return result.getResult().get(0);

	}

}
