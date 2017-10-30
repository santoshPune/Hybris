/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspalipayservices.order;

import de.hybris.platform.core.model.order.OrderModel;


/**
 * Provide method to get order model
 */
public interface AlipayOrderService
{
	/**
	 * Get OrderModel by OrderCode
	 *
	 * @param code
	 *           The order code of the wanted order
	 * @return OrderModel if found and null otherwise
	 */
	public OrderModel getOrderByCode(final String code);

}
