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
 */
package de.hybris.platform.b2bacceleratoraddon.event;

import de.hybris.platform.b2b.event.QuoteRejectedEvent;
import de.hybris.platform.b2b.model.B2BCommentModel;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


/**
 * A listener of {@link de.hybris.platform.b2b.event.QuoteRejectedEvent} which is fired when a quote is rejected by a
 * manager
 */
public class QuoteRejectedEventListener extends AbstractQuoteEventListener<QuoteRejectedEvent>
{
	@Override
	protected void onEvent(final QuoteRejectedEvent event)
	{
		final OrderModel quoteOrder = event.getOrder();
		final OrderProcessModel orderProcess = (OrderProcessModel) getBusinessProcessService().createProcess(
				"quoteOrderRejectEmailProcess" + "-" + quoteOrder.getCode() + "-" + System.currentTimeMillis(),
				"quoteOrderRejectEmailProcess");
		orderProcess.setOrder(quoteOrder);
		getModelService().save(orderProcess);
		getBusinessProcessService().startProcess(orderProcess);
		final UserModel manager = (UserModel) event.getManager();
		final B2BCommentModel comment = getLatestCommentByUser(quoteOrder, manager);
		createSnapshot(quoteOrder, comment, manager, OrderStatus.REJECTED_QUOTE);
	}
}
