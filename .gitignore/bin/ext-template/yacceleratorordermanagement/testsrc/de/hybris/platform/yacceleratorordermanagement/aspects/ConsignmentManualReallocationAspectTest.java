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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehousing.process.BusinessProcessException;
import de.hybris.platform.warehousing.process.WarehousingBusinessProcessService;
import de.hybris.platform.yacceleratorordermanagement.aspects.ConsignmentManualReallocationAspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class ConsignmentManualReallocationAspectTest
{
	@InjectMocks
	private final ConsignmentManualReallocationAspect aspect = new ConsignmentManualReallocationAspect();
	@Mock
	private WarehousingBusinessProcessService<OrderModel> orderBusinessProcessService;
	@Mock
	private ModelService modelService;
	@Mock
	private ProceedingJoinPoint joinPoint;
	@Mock
	private ConsignmentModel consignment;
	@Mock
	private OrderModel order;
	@Mock
	private OrderProcessModel orderProcess;
	private Collection<ConsignmentModel> consignments = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Throwable
	{
		consignments.add(consignment);
		when(consignment.getCode()).thenReturn("CODE");
		when(consignment.getOrder()).thenReturn(order);
		when(order.getOrderProcess()).thenReturn(Arrays.asList(orderProcess));

		when(joinPoint.proceed()).thenReturn(null);
		when(orderBusinessProcessService.createProcess(any(), any())).thenReturn(new ConsignmentProcessModel());
		when(orderBusinessProcessService.getProcess(any())).thenReturn(orderProcess);
	}

	@Test
	public void shouldAdviseSuccessfully() throws Throwable
	{

		aspect.advise(joinPoint, consignments);
		
		verify(orderBusinessProcessService,times(1)).createProcess(any(), any());
		verify(modelService,times(1)).save(any());
	}

	@Test
	public void shouldFailAdvice_BusinessProcessException() throws Throwable
	{
		Mockito.doThrow(new BusinessProcessException("error")).when(orderBusinessProcessService).getProcessCode(any());

		aspect.advise(joinPoint, consignments);

		verify(orderBusinessProcessService, never()).createProcess(any(), any());
		verify(modelService, never()).save(any());
	}
}
