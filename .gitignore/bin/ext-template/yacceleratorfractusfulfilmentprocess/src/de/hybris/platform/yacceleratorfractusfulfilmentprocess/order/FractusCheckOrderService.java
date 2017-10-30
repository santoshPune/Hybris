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


/**
 * Used by FractusCheckOrderService, this service is designed to validate the order prior to running the fulfilment
 * process.
 */
public interface FractusCheckOrderService
{
	/**
	 * Check if yaas order is valid
	 *
	 * @param order
	 * @return
	 */
	boolean check(OrderModel order);
}
