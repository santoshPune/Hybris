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
package de.hybris.platform.yacceleratorordermanagement.actions.order.cancel;

import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordercancel.OrderCancelCallbackService;
import de.hybris.platform.ordercancel.OrderCancelRecordsHandler;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.exceptions.OrderCancelRecordsHandlerException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.cancellation.OmsOrderCancelService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ProcessOrderCancellationActionTest
{

	private OrderEntryModel orderEntryModel;
	private OrderProcessModel orderProcessModel;
	private OrderModel orderModel;
	private ConsignmentModel consignment;
	private OrderEntryCancelRecordEntryModel orderEntryCancelRecordEntryModel;
	private Collection<OrderEntryModificationRecordEntryModel> orderEntryModificationRecordEntryModels = new ArrayList<>();

	@InjectMocks
	private final ProcessOrderCancellationAction action = new ProcessOrderCancellationAction();

	@Mock
	private ModelService modelService;

	@Mock
	private UserService userService;

	@Mock
	private OmsOrderCancelService omsOrderCancelService;

	@Mock
	private OrderCancelRecordsHandler orderCancelRecordsHandler;

	@Mock
	private OrderCancelRecordEntryModel orderCancelRecordEntryModel;

	@Mock
	private OrderCancelCallbackService orderCancelCallbackService;

	@Mock
	private OrderCancelService orderCancelService;

	@Before
	public void setup() throws OrderCancelRecordsHandlerException
	{
		consignment = new ConsignmentModel();
		consignment.setStatus(ConsignmentStatus.CANCELLED);

		orderEntryModel = spy(new OrderEntryModel());

		final List<AbstractOrderEntryModel> orderEntriesModel = new ArrayList<>();
		orderEntriesModel.add(orderEntryModel);

		orderModel = new OrderModel();
		orderModel.setEntries(orderEntriesModel);
		orderModel.setConsignments(Sets.newHashSet(consignment));

		orderProcessModel = new OrderProcessModel();
		orderProcessModel.setOrder(orderModel);

		when(orderEntryModel.getQuantity()).thenReturn(Long.valueOf(5L));
		when(orderEntryModel.getQuantityPending()).thenReturn(Long.valueOf(0L));
		when(orderEntryModel.getOrder()).thenReturn(orderModel);
		when(orderEntryModel.getQuantityUnallocated()).thenReturn(Long.valueOf(0L));

		when(userService.getCurrentUser()).thenReturn(new UserModel());

		orderEntryCancelRecordEntryModel =  new OrderEntryCancelRecordEntryModel();
		orderEntryCancelRecordEntryModel.setOrderEntry(orderEntryModel);
		orderEntryCancelRecordEntryModel.setCancelRequestQuantity(Integer.valueOf(3));
		orderEntryModificationRecordEntryModels.add(orderEntryCancelRecordEntryModel);

		when(orderCancelRecordsHandler.getPendingCancelRecordEntry(orderModel)).thenReturn(orderCancelRecordEntryModel);
		when(orderCancelRecordEntryModel.getOrderEntriesModificationEntries()).thenReturn(orderEntryModificationRecordEntryModels);


	}

	@Test
	public void shouldWaitWhenQuantityPendingIsMoreThanZero() throws Exception
	{
		when(orderEntryModel.getQuantityPending()).thenReturn(Long.valueOf(5L));
		when(orderCancelService.getPendingCancelRecordEntry(orderModel)).thenReturn(orderCancelRecordEntryModel);
		when(orderCancelRecordEntryModel.getCancelResult()).thenReturn(OrderCancelEntryStatus.PARTIAL);

		final String transition = action.execute(orderProcessModel);
		assertEquals(ProcessOrderCancellationAction.Transition.WAIT.toString(), transition);
	}

	@Test
	public void shouldOKWhenQuantityPendingIsZero() throws Exception
	{
		when(orderCancelService.getPendingCancelRecordEntry(orderModel)).thenReturn(orderCancelRecordEntryModel);
		when(orderCancelRecordEntryModel.getCancelResult()).thenReturn(OrderCancelEntryStatus.FULL);

		final String transition = action.execute(orderProcessModel);
		assertEquals(ProcessOrderCancellationAction.Transition.OK.toString(), transition);
	}

	@Test
	public void shouldSetOrderStatusToCancelledWhenQuantityPendingIsZero() throws Exception
	{
		when(orderEntryModel.getQuantity()).thenReturn(Long.valueOf(0L));
		when(orderCancelService.getPendingCancelRecordEntry(orderModel)).thenReturn(orderCancelRecordEntryModel);
		orderEntryCancelRecordEntryModel.setCancelRequestQuantity(Integer.valueOf(0));
		when(orderCancelRecordEntryModel.getCancelResult()).thenReturn(OrderCancelEntryStatus.FULL);

		action.execute(orderProcessModel);

		verify(modelService).save(orderModel);
		assertTrue(orderModel.getStatus().toString().equals(OrderStatus.CANCELLED.toString()));
	}

}
