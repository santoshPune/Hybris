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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.listeners;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.startsWith;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.events.SubmitFractusOrderEvent;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


/**
 * Unit test for the SubmitFractusOrderEventListener class.
 */
@UnitTest
public class SubmitFractusOrderEventListenerTest
{
	@InjectMocks
	SubmitFractusOrderEventListener listener;
	@Mock
	FractusOrderService orderService;
	@Mock
	ModelService modelService;
	@Mock
	BusinessProcessService businessProcessService;
	@Mock
	private SubmitFractusOrderEvent event;
	@Mock
	private OrderModel order;
	@Mock
	private OrderModel storelessOrder;
	@Mock
	private BaseStoreModel baseStore;
	@Mock
	private FractusOrderProcessModel businessProcess;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		listener = new SubmitFractusOrderEventListener();
		initMocks(this);
		when(order.getStore()).thenReturn(baseStore);
		when(baseStore.getSubmitOrderProcessCode()).thenReturn("submitOrderProcessCode");
		when(event.getOrder()).thenReturn(order);
		when(Boolean.valueOf(orderService.isFractusOrder(order))).thenReturn(Boolean.TRUE);
		when(businessProcessService.createProcess(anyString(), anyString())).thenReturn(businessProcess);
	}


	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.listeners.SubmitFractusOrderEventListener#onSiteEvent(de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.events.SubmitFractusOrderEvent)}
	 * .
	 */
	@Test
	public void testOnSiteEventSubmitFractusOrderEvent()
	{
		listener.onSiteEvent(event);
		verifyStartProcess();
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.listeners.SubmitFractusOrderEventListener#startProcess(de.hybris.platform.core.model.order.OrderModel)}
	 * .
	 */
	@Test
	public void testStartProcess() throws NullPointerException
	{
		listener.startProcess(order);
		verifyStartProcess();
	}


	/**
	 */
	private void verifyStartProcess()
	{
		verify(businessProcess).setOrder(order);
		verify(businessProcessService).createProcess(startsWith(order.getStore().getSubmitOrderProcessCode()), anyString());
		verify(businessProcessService).startProcess(businessProcess);
		verify(modelService).save(businessProcess);
	}

	public void testStartProcessForStorelessOrder()
	{
		listener.startProcess(storelessOrder);
		verifyZeroInteractions(modelService);
		verifyZeroInteractions(businessProcessService);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.listeners.SubmitFractusOrderEventListener#shouldHandleEvent(de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.events.SubmitFractusOrderEvent)}
	 * .
	 */
	@Test
	public void testShouldHandleEventSubmitFractusOrderEvent()
	{
		final boolean shouldHandleEvent = listener.shouldHandleEvent(event);
		assertThat(shouldHandleEvent).isTrue();
	}

}
