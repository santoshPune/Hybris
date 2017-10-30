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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.dao.impl;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.daos.impl.DefaultOrderDao;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.dao.YaasOrderDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.google.common.collect.Maps;


public class DefaultYaasOrderDao extends DefaultOrderDao implements YaasOrderDao
{
	private static final Logger LOG = Logger.getLogger(DefaultYaasOrderDao.class);
	private static final String ORDER_FULFILLMENT_QUERY = "SELECT {" + OrderModel.PK + "} FROM {" + OrderModel._TYPECODE
			+ "} WHERE {" + OrderModel.STATUS + "} = ?status AND {" + OrderModel.SALESAPPLICATION + "}=?salesapplication";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OrderModel> findOrdersForFulFilment()
	{
		final Map<String, Object> params = Maps.newHashMap();
		params.put("status", OrderStatus.CREATED);
		params.put("salesapplication", SalesApplication.YAAS);
		final List<OrderModel> result = getFlexibleSearchService().<OrderModel> search(ORDER_FULFILLMENT_QUERY, params).getResult();
		if (CollectionUtils.isNotEmpty(result))
		{
			return result;
		}
		LOG.info("No YAAS orders to fulfill");
		return new ArrayList();
	}

}
