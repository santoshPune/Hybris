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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.OrderService;

import java.util.List;


/**
 * Focuses on methods to retrieval and fulfilment of orders.
 *
 */
public interface FractusOrderService extends OrderService
{
	/**
	 * Check if order is yaas order
	 *
	 * @param order
	 * @return
	 */
	boolean isFractusOrder(OrderModel order);

	/**
	 * YaaS orders ready for fulfilment
	 *
	 * @return
	 */
	List<OrderModel> getOrdersForFulfilment();

	/**
	 * Starts fulfilment process for the YaaS order
	 *
	 * @param order
	 */
	void startFulfilmentProcess(final OrderModel order);

}
