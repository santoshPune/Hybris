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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;
import de.hybris.platform.order.strategies.impl.EventPublishingSubmitOrderStrategy;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.dao.impl.DefaultYaasOrderDao;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.impl.DefaultCheckYAASOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.impl.DefaultFractusOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.strategies.impl.EventPublishingSubmitFractusOrderStrategy;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Unit test for the DefaultFractusOrderService class.
 */
@UnitTest
public class FractusOrderServiceTest
{

	@InjectMocks
	private final DefaultFractusOrderService orderService = new DefaultFractusOrderService()
	{

		@Override
		protected ModelService getModelService()
		{
			return modelServiceInTest;
		}

	};

	@Mock
	private EventPublishingSubmitOrderStrategy ecpSubmitOrderStrategy;

	@Mock
	private EventPublishingSubmitFractusOrderStrategy fractusSubmitOrderStrategy;

	@Mock
	private DefaultCheckYAASOrderService checkOrderService;

	@Mock
	private DefaultYaasOrderDao yaasOrderDao;

	@Mock
	private OrderModel invalidOrder, validOrder;

	@Mock
	private FractusOrderProcessModel fractusOrderProcess;

	@Mock
	private OrderProcessModel orderProcess;

	@Mock
	private BaseStoreModel baseStore;

	@Mock
	private CommerceCheckoutService commerceCheckoutService;

	private OrderModel ecpOrder;
	private OrderModel fractusOrder;

	@Mock
	private ModelService modelServiceInTest;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		final Map<String, SubmitOrderStrategy> strategies = Collections.unmodifiableMap(new HashMap<String, SubmitOrderStrategy>()
		{
			{
				put("ecp", ecpSubmitOrderStrategy);
				put("fractus", fractusSubmitOrderStrategy);
			}
		});

		orderService.setSubmitOrderStrategies(strategies);

		ecpOrder = new OrderModel();
		ecpOrder.setCode("ecp order test");
		ecpOrder.setDate(new Date());
		ecpOrder.setNet(Boolean.FALSE);

		fractusOrder = new OrderModel();
		fractusOrder.setCode("fractus order test");
		fractusOrder.setDate(new Date());
		fractusOrder.setNet(Boolean.FALSE);
		fractusOrder.setStatus(OrderStatus.CREATED);
		fractusOrder.setSalesApplication(SalesApplication.YAAS);
	}

	@Test
	public void shouldSubmitEcpOrder()
	{
		orderService.submitOrder(ecpOrder);

		Mockito.verify(ecpSubmitOrderStrategy).submitOrder(ecpOrder);
	}

	@Test
	public void shouldSubmitFractusOrder()
	{
		orderService.submitOrder(fractusOrder);

		Mockito.verify(fractusSubmitOrderStrategy).submitOrder(fractusOrder);
	}


	@Test
	public void testGetOrdersForFulfilment()
	{
		Mockito.when(yaasOrderDao.findOrdersForFulFilment()).thenReturn(Arrays.asList(invalidOrder, validOrder));
		Mockito.when(invalidOrder.getOrderProcess()).thenReturn(Arrays.asList(orderProcess));
		Mockito.when(validOrder.getOrderProcess()).thenReturn(Arrays.asList(orderProcess));
		Mockito.when(new Boolean(checkOrderService.check(validOrder))).thenReturn(Boolean.TRUE);
		Mockito.when(new Boolean(checkOrderService.check(invalidOrder))).thenReturn(Boolean.FALSE);
		Assert.assertArrayEquals(new OrderModel[]
		{ validOrder }, orderService.getOrdersForFulfilment().toArray());
	}

	@Test
	public void testGetOrdersForFulfilmentWhenFulfilmentProcessExists()
	{
		Mockito.when(yaasOrderDao.findOrdersForFulFilment()).thenReturn(Arrays.asList(validOrder));
		Mockito.when(validOrder.getOrderProcess()).thenReturn(Arrays.asList((OrderProcessModel) fractusOrderProcess));
		Mockito.when(new Boolean(checkOrderService.check(validOrder))).thenReturn(Boolean.TRUE);
		Assert.assertTrue(CollectionUtils.isEmpty(orderService.getOrdersForFulfilment()));
	}

	@Test
	public void testStartFulFilmentProcess() throws ParseException, InvalidCartException
	{
		Mockito.when(yaasOrderDao.findOrdersForFulFilment()).thenReturn(Arrays.asList(validOrder));
		Mockito.when(validOrder.getCode()).thenReturn("validOrder");
		Mockito.when(new Boolean(checkOrderService.check(validOrder))).thenReturn(Boolean.TRUE);
		Mockito.when(validOrder.getStore()).thenReturn(baseStore);
		orderService.startFulfilmentProcess(validOrder);
		Mockito.verify(commerceCheckoutService, Mockito.times(1)).placeOrder(Mockito.any(CommerceCheckoutParameter.class));
	}
}
