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

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusCheckOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


/**
 * Unit Test for the FractusCheckOrderAction class.
 */
@UnitTest
public class FractusCheckOrderActionTest
{

	@InjectMocks
	FractusCheckOrderAction action;
	@Mock
	FractusCheckOrderService checkOrderService;
	@Mock
	ModelService modelService;
	@Mock
	private FractusOrderProcessModel model1;
	@Mock
	private OrderModel order1;
	@Mock
	private FractusOrderProcessModel model2;
	@Mock
	private OrderModel order2;
	@Mock
	private FractusOrderProcessModel model3;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		action = new FractusCheckOrderAction();
		initMocks(this);
		when(model1.getOrder()).thenReturn(order1);
		when(model2.getOrder()).thenReturn(order2);

		//Mockito needs to deal with primitives more nicely than this!
		when(Boolean.valueOf(checkOrderService.check(order2))).thenReturn(Boolean.TRUE);


	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions.FractusCheckOrderAction#executeAction(de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel)}
	 * .
	 *
	 * @throws Exception
	 * @throws RetryLaterException
	 */
	@Test
	public void testExecuteActionFractusOrderProcessModel() throws RetryLaterException, Exception
	{
		final Transition executeAction = action.executeAction(model1);
		assertThat(executeAction).isEqualTo(Transition.NOK);
		final Transition executeAction2 = action.executeAction(model2);
		assertThat(executeAction2).isEqualTo(Transition.OK);
		final Transition executeAction3 = action.executeAction(model3);
		assertThat(executeAction3).isEqualTo(Transition.NOK);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions.FractusCheckOrderAction#isOrderValid(de.hybris.platform.core.model.order.OrderModel)}
	 * .
	 */
	@Test
	public void testIsOrderValid()
	{
		action.isOrderValid(order1);
		//ensure that the action is delegating to the CheckOrderService at this time.
		verify(checkOrderService).check(order1);
	}

}
