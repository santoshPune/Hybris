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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusOrderService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The class of FractusCommercePlaceOrderStrategyTest.
 */
@UnitTest
public class FractusCommercePlaceOrderStrategyTest
{
	@InjectMocks
	private FractusCommercePlaceOrderStrategy fractusCommercePlaceOrderStrategy = new FractusCommercePlaceOrderStrategy();

	@Mock
	private FractusOrderService orderService;

	@Mock
	private CommercePlaceOrderStrategy defaultCommercePlaceOrderStrategy;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		fractusCommercePlaceOrderStrategy = Mockito.spy(fractusCommercePlaceOrderStrategy);
	}


	@Test
	public void shouldPlaceFractusOrder() throws InvalidCartException
	{
		final OrderModel orderModel = new OrderModel();
		orderModel.setStatus(OrderStatus.CREATED);
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setOrder(orderModel);

		Mockito.when(Boolean.valueOf(orderService.isFractusOrder(orderModel))).thenReturn(Boolean.TRUE);

		fractusCommercePlaceOrderStrategy.placeOrder(parameter);

		Mockito.verify(orderService).submitOrder(orderModel);
	}

	@Test
	public void shouldPlaceEcpOrder() throws InvalidCartException
	{
		final CartModel cart = new CartModel();
		final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
		parameter.setCart(cart);

		fractusCommercePlaceOrderStrategy.placeOrder(parameter);

		Mockito.verify(orderService, Mockito.never()).isFractusOrder(Mockito.any(OrderModel.class));
		Mockito.verify(defaultCommercePlaceOrderStrategy).placeOrder(parameter);
	}

}
