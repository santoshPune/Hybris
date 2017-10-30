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
package de.hybris.platform.ordermanagementfacade.order;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.ordermanagementfacade.fraud.data.FraudReportData;

import java.util.List;
import java.util.Set;


/**
 * OrderManagement facade exposing operations on {@link de.hybris.platform.core.model.order.OrderModel}
 */
public interface OmsOrderFacade
{
	/**
	 * API to get all orders in the system
	 *
	 * @param pageableData
	 *           pageable object that contains info on the number or pages and how many items in each page in addition
	 *           the sorting info
	 * @return SearchPageData that contains a list of orders
	 */
	SearchPageData<OrderData> getOrders(PageableData pageableData);

	/**
	 * API to get an order by it's code
	 *
	 * @param code
	 *           the order's code
	 * @return the order
	 */
	OrderData getOrderForCode(String code);

	/**
	 * API to get all orders in the system that have certain status(es)
	 *
	 * @param pageableData
	 *           pageable object that contains info on the number or pages and how many items in each page in addition
	 *           the sorting info
	 * @param orderStatusSet
	 *           set of order status(s) in which we want to get list of orders for
	 * @return SearchPageData that contains a list of orders that complies with passed order status(es)
	 */
	SearchPageData<OrderData> getOrdersByStatuses(PageableData pageableData, Set<OrderStatus> orderStatusSet);

	/**
	 * API to get all order statuses
	 *
	 * @return a list of {@link OrderStatus}
	 */
	List<OrderStatus> getOrderStatuses();

	/**
	 * API to get orderEntries for the given {@link de.hybris.platform.core.model.order.OrderModel#CODE}
	 *
	 * @param code
	 *           the order's code
	 * @param pageableData
	 *           pageable object that contains info on the number or pages and how many items in each page in addition
	 *           the sorting info
	 * @return SearchPageData that contains a list of the orderEntries for the given order
	 */
	SearchPageData<OrderEntryData> getOrderEntriesForOrderCode(String code, PageableData pageableData);

	/**
	 * API to get an orderEntry by it's entryNumber and its order's code
	 *
	 * @param orderCode
	 *           the order's code
	 * @param entryNumber
	 *           the order entry's number
	 * @return the order entry
	 */
	OrderEntryData getOrderEntryForOrderCodeAndEntryNumber(String orderCode, Integer entryNumber);

	/**
	 * API to get an order's fraud reports
	 *
	 * @param orderCode
	 *           code of the order for which to get the fraud reports
	 * @return a list of fraud reports
	 */
	List<FraudReportData> getOrderFraudReports(String orderCode);

	/**
	 * API to get all order cancel reasons
	 *
	 * @return a list of {@link CancelReason}
	 */
	List<CancelReason> getCancelReasons();
}
