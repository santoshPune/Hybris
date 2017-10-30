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
import de.hybris.platform.basecommerce.enums.OrderCancelEntryStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelRequest;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Tests
 */
@UnitTest
public class FractusOrderCancelServiceTest
{

	private static final Logger LOG = Logger.getLogger(FractusOrderCancelServiceTest.class);

	@InjectMocks
	private FractusOrderCancelService orderCancelService;

	@Mock
	private EventService eventService;

	@Mock
	private OrderModel orderModel;

	@Mock
	private OrderCancelRecordEntryModel orderCancelRecordEntryModel;

	@Mock
	private OrderCancelRequest orderCancelRequest;

	@Mock
	private PrincipalModel requestor;

	@Before
	public void customSetUp() throws InvalidCartException
	{
		orderCancelService = new FractusOrderCancelService();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void shouldSentOrderCancelEventWhenOrderFullCancel() throws OrderCancelException
	{
		Mockito.when(orderCancelRecordEntryModel.getCancelResult()).thenReturn(OrderCancelEntryStatus.FULL);

		orderCancelService.sendYaasOrderCancelEvent(orderModel, orderCancelRecordEntryModel);


		Mockito.verify(eventService).publishEvent(Mockito.any(FractusOrderCancelEvent.class));
	}

	@Test
	public void shouldNotSentOrderCancelEventWhenOrderPartialCancel() throws OrderCancelException
	{
		Mockito.when(orderCancelRecordEntryModel.getCancelResult()).thenReturn(OrderCancelEntryStatus.PARTIAL);

		orderCancelService.sendYaasOrderCancelEvent(orderModel, orderCancelRecordEntryModel);


		Mockito.verify(eventService, Mockito.never()).publishEvent(Mockito.any(FractusOrderCancelEvent.class));
	}

}
