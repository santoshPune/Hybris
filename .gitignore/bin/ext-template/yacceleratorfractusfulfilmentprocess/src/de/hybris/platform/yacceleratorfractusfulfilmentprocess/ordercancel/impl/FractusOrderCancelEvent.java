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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.ordercancel.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Yaas Order cancel event.
 */
public class FractusOrderCancelEvent extends AbstractEvent implements ClusterAwareEvent
{

	private OrderModel orderModel;
	private OrderCancelRecordEntryModel orderCancelRecordEntryModel;

	public OrderModel getOrderModel()
	{
		return orderModel;
	}

	public void setOrderModel(final OrderModel orderModel)
	{
		this.orderModel = orderModel;
	}

	public OrderCancelRecordEntryModel getOrderCancelRecordEntryModel()
	{
		return orderCancelRecordEntryModel;
	}

	public void setOrderCancelRecordEntryModel(final OrderCancelRecordEntryModel orderCancelRecordEntryModel)
	{
		this.orderCancelRecordEntryModel = orderCancelRecordEntryModel;
	}

	@Override
	public boolean publish(final int sourceNodeId, final int targetNodeId)
	{
		return sourceNodeId == targetNodeId;
	}
}
