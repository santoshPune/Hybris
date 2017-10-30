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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusSyncExecutionService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.YaasOrderStatusService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


/**
 * Unit test for the FractusUpdateOrderStatusAction class.
 */
@UnitTest
public class FractusUpdateOrderStatusActionTest
{
	@InjectMocks
	FractusUpdateOrderStatusAction action;
	@Mock
	ModelService modelService;
	@Mock
	YaasOrderStatusService yaasOrderStatusService;
	@Mock
	FractusSyncExecutionService fractusSyncExecutionService;
	String syncExecutionId = "1234";
	@Mock
	private FractusOrderProcessModel orderProcess;
	@Mock
	private OrderModel order;
	@Mock
	private OrderStatus orderStatus;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		action = new FractusUpdateOrderStatusAction();
		initMocks(this);
		when(orderStatus.getCode()).thenReturn("orderStatusCode");
		when(order.getStatus()).thenReturn(orderStatus);
		when(orderProcess.getOrder()).thenReturn(order);

	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions.FractusUpdateOrderStatusAction#executeAction(de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel)}
	 * .
	 *
	 * @throws Exception
	 * @throws RetryLaterException
	 */
	@Test
	public void testExecuteActionFractusOrderProcessModel() throws RetryLaterException, Exception
	{
		action.executeAction(orderProcess);
		verify(yaasOrderStatusService).determineYaasStatus(order);
		verify(modelService).save(order);
	}

}
