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
package de.hybris.platform.yacceleratorordermanagement.aspects;

import com.google.common.collect.Sets;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.OrderCancelResponse;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.constants.YAcceleratorOrderManagementConstants;
import org.aspectj.lang.JoinPoint;
import org.fest.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConsignmentActionAspectTest
{
	private static final String CHOICE = "CHOICE";

	@InjectMocks
	private final ConsignmentActionAspect aspect = new ConsignmentActionAspect();

	@Mock
	private WarehousingBusinessProcessService<ConsignmentModel> consignmentBusinessProcessService;
	@Mock
	private JoinPoint joinPoint;
	@Mock
	private ConsignmentModel consignment;
	@Mock
	private OrderCancelResponse orderCancelResponse;
	@Mock
	private OrderModel order;
	@Mock
	private ConsignmentProcessModel consignmentProcess;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Throwable
	{
		aspect.setChoice(CHOICE);

		when(consignment.getCode()).thenReturn("CODE");
		when(joinPoint.getArgs()).thenReturn(Arrays.array(orderCancelResponse));
		when(orderCancelResponse.getOrder()).thenReturn(order);
		when(order.getConsignments()).thenReturn(Sets.newHashSet(consignment));
		when(consignment.getStatus()).thenReturn(ConsignmentStatus.CANCELLED);

		when(consignment.getConsignmentProcesses()).thenReturn(java.util.Arrays.asList(consignmentProcess));
		when(consignmentProcess.isDone()).thenReturn(false);
		doNothing().when(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
	}

	@Test
	public void shouldAdviseSuccessfully_WithRemainingCancellationEntries() throws Throwable
	{
		aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
	}

	@Test
	public void shouldAdviseSuccessfully_NoRemainingCancellationEntry() throws Throwable
	{
		aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
	}

	@Test(expected = BusinessProcessException.class)
	public void shouldFailAdvice_BusinessProcessException() throws Throwable
	{
		doThrow(new BusinessProcessException("error")).when(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);

		aspect.advise(joinPoint);

		verify(consignmentBusinessProcessService).triggerChoiceEvent(consignment,
				YAcceleratorOrderManagementConstants.CONSIGNMENT_ACTION_EVENT_NAME, CHOICE);
	}

}
