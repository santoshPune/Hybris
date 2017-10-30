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

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.OrderModificationEntryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fractussyncservices.exception.YaasBusinessException;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordermodify.model.OrderModificationRecordModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.YaasOrderStatusService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.UnknownEcpStatusException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test for order cancel listener.
 */
@UnitTest
public class FractusOrderCancelListenerTest
{

	@InjectMocks
	private FractusOrderCancelListener listener;
	@Mock
	private FractusSyncExecutionService syncExecutionService;
	@Mock
	private YaasOrderStatusService yaasOrderStatusService;
	@Mock
	private ModelService modelService;
	@Mock
	private OrderModel orderModel;
	@Mock
	private OrderCancelRecordEntryModel orderCancelRecordEntryModel;
	@Mock
	private OrderModificationRecordModel orderModificationRecordModel;


	@Before
	public void setup()
	{
		listener = new FractusOrderCancelListener();
		listener.setSleepTimer(100);
		listener.setSyncExecutionId("id");
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testOnEventShouldSyncOrderStatus() throws YaasBusinessException
	{
		final FractusOrderCancelEvent event = new FractusOrderCancelEvent();
		event.setOrderModel(orderModel);
		event.setOrderCancelRecordEntryModel(orderCancelRecordEntryModel);

		Mockito.when(orderCancelRecordEntryModel.getModificationRecord()).thenReturn(orderModificationRecordModel);
		Mockito.when(orderModificationRecordModel.isInProgress()).thenReturn(false);
		Mockito.when(orderCancelRecordEntryModel.getStatus()).thenReturn(OrderModificationEntryStatus.SUCCESSFULL);
		Mockito.when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);

		listener.onEvent(event);

		Mockito.verify(yaasOrderStatusService).determineYaasStatus(orderModel);
		Mockito.verify(syncExecutionService).synchOrderStatus(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testOnEventShouldNotSyncOrderStatus() throws YaasBusinessException
	{
		final FractusOrderCancelEvent event = new FractusOrderCancelEvent();
		event.setOrderModel(orderModel);
		event.setOrderCancelRecordEntryModel(orderCancelRecordEntryModel);

		Mockito.when(orderCancelRecordEntryModel.getModificationRecord()).thenReturn(orderModificationRecordModel);
		Mockito.when(orderModificationRecordModel.isInProgress()).thenReturn(false);
		Mockito.when(orderCancelRecordEntryModel.getStatus()).thenReturn(OrderModificationEntryStatus.FAILED);
		Mockito.when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);

		listener.onEvent(event);

		Mockito.verify(yaasOrderStatusService, Mockito.never()).determineYaasStatus(orderModel);
		Mockito.verify(syncExecutionService, Mockito.never()).synchOrderStatus(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testOnEventShouldNotSyncOrderStatusWithUnknownEcpStatus() throws YaasBusinessException
	{
		final FractusOrderCancelEvent event = new FractusOrderCancelEvent();
		event.setOrderModel(orderModel);
		event.setOrderCancelRecordEntryModel(orderCancelRecordEntryModel);

		Mockito.when(orderCancelRecordEntryModel.getModificationRecord()).thenReturn(orderModificationRecordModel);
		Mockito.when(orderModificationRecordModel.isInProgress()).thenReturn(false);
		Mockito.when(orderCancelRecordEntryModel.getStatus()).thenReturn(OrderModificationEntryStatus.SUCCESSFULL);
		Mockito.when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);
		Mockito.when(yaasOrderStatusService.determineYaasStatus(orderModel)).thenThrow(UnknownEcpStatusException.class);

		listener.onEvent(event);

		Mockito.verify(yaasOrderStatusService).determineYaasStatus(orderModel);
		Mockito.verify(syncExecutionService, Mockito.never()).synchOrderStatus(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testOnEventShouldNotSyncOrderStatusWithNoMatchingYaasStatus() throws YaasBusinessException
	{
		final FractusOrderCancelEvent event = new FractusOrderCancelEvent();
		event.setOrderModel(orderModel);
		event.setOrderCancelRecordEntryModel(orderCancelRecordEntryModel);

		Mockito.when(orderCancelRecordEntryModel.getModificationRecord()).thenReturn(orderModificationRecordModel);
		Mockito.when(orderModificationRecordModel.isInProgress()).thenReturn(false);
		Mockito.when(orderCancelRecordEntryModel.getStatus()).thenReturn(OrderModificationEntryStatus.SUCCESSFULL);
		Mockito.when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);
		Mockito.when(yaasOrderStatusService.determineYaasStatus(orderModel)).thenThrow(NoMatchingYaasStatusException.class);

		listener.onEvent(event);

		Mockito.verify(yaasOrderStatusService).determineYaasStatus(orderModel);
		Mockito.verify(syncExecutionService, Mockito.never()).synchOrderStatus(Mockito.anyString(), Mockito.anyString());
	}

	@Test
	public void testOnEventShouldNotSyncOrderWithThreadInterrupt()
	{
		Thread.currentThread().interrupt();
		final FractusOrderCancelEvent event = new FractusOrderCancelEvent();
		event.setOrderModel(orderModel);
		event.setOrderCancelRecordEntryModel(orderCancelRecordEntryModel);

		Mockito.when(orderCancelRecordEntryModel.getModificationRecord()).thenReturn(orderModificationRecordModel);
		Mockito.when(orderModificationRecordModel.isInProgress()).thenReturn(false);
		Mockito.when(orderCancelRecordEntryModel.getStatus()).thenReturn(OrderModificationEntryStatus.SUCCESSFULL);
		Mockito.when(orderModel.getStatus()).thenReturn(OrderStatus.CANCELLED);

		listener.onEvent(event);

		Mockito.verify(syncExecutionService, Mockito.never()).synchOrderStatus(Mockito.anyString(), Mockito.anyString());
	}

}
