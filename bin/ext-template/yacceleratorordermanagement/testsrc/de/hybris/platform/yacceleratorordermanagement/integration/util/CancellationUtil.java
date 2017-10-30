/*
 * [y] hybris Platform
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.yacceleratorordermanagement.integration.util;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.model.OrderCancelConfigModel;
import de.hybris.platform.warehousing.util.CancellationEntryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * this class is mainly to cancel orders
 */
@Component
public class CancellationUtil extends ProcessUtil
{
	/**
	 * cancel consignment
	 *
	 * @param orderModel
	 * @param cancellationEntryInfo
	 * @param cancelReason
	 * @throws InterruptedException
	 * @throws OrderCancelException
	 */
	public void cancelDefaultConsignment(final OrderModel orderModel,
			final Map<AbstractOrderEntryModel, Long> cancellationEntryInfo, final CancelReason cancelReason)
			throws InterruptedException, OrderCancelException
	{
		final List<OrderCancelEntry> cancellationEntries = CancellationEntryBuilder.aCancellation().build(cancellationEntryInfo,
				cancelReason);
		orderCancelService
				.requestOrderCancel(new OrderCancelRequest(orderModel, cancellationEntries), userService.getCurrentUser());
	}

	public void setOrderCancelConfig()
	{
		OrderCancelConfigModel orderCancelConfigModel= orderCancelService.getConfiguration() == null ? new OrderCancelConfigModel() : orderCancelService.getConfiguration();
		orderCancelConfigModel.setPartialCancelAllowed(true);
		orderCancelConfigModel.setOrderCancelAllowed(true);
		orderCancelConfigModel.setPartialOrderEntryCancelAllowed(true);
		orderCancelConfigModel.setCancelAfterWarehouseAllowed(true);
		getModelService().save(orderCancelConfigModel);
	}

	public boolean isCancelPossible (OrderModel order, boolean partialCancel, boolean partialEntryCancel )
	{
		return orderCancelService.isCancelPossible(order, userService.getAdminUser(), partialCancel, partialEntryCancel).isAllowed();
	}

}
