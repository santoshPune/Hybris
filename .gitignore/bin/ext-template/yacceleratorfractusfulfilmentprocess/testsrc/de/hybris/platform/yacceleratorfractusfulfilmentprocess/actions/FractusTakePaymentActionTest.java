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
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import java.util.ArrayList;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;


/**
 * Unit test for the FractusTakePaymentAction class.
 */
@UnitTest
public class FractusTakePaymentActionTest
{
	@InjectMocks
	FractusTakePaymentAction action;
	@Mock
	ModelService modelService;
	@Mock
	private FractusOrderProcessModel orderProcess;
	@Mock
	private OrderModel order;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		action = new FractusTakePaymentAction();

		initMocks(this);
		when(orderProcess.getOrder()).thenReturn(order);
	}

	/**
	 * Test method for
	 * {@link de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions.FractusTakePaymentAction#executeAction(de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel)}
	 * .
	 *
	 * @throws Exception
	 * @throws RetryLaterException
	 */
	@Test
	public void shouldUpdateOrderStatusToPaymentCaptured()
	{
		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(FractusTakePaymentAction.TAKEPAYMENT_REJECT_PRODUCT_CODE, StringUtils.EMPTY))
				.thenReturn(StringUtils.EMPTY);
		final Transition execute = action.executeAction(orderProcess);
		assertThat(execute).isEqualTo(Transition.OK);
		verify(order).setStatus(OrderStatus.PAYMENT_CAPTURED);
		verifyNoMoreInteractions(order);
		verify(modelService).save(order);
		verifyNoMoreInteractions(modelService);
	}

	@Test
	public void shouldUpdateOrderStatusToPaymentNotCaptured()
	{
		final ProductModel product = Mockito.mock(ProductModel.class);
		final OrderEntryModel entry = Mockito.mock(OrderEntryModel.class);
		final ArrayList<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(entry);

		Mockito.when(configurationService.getConfiguration()).thenReturn(configuration);
		Mockito.when(configuration.getString(FractusTakePaymentAction.TAKEPAYMENT_REJECT_PRODUCT_CODE, StringUtils.EMPTY))
				.thenReturn("12345");
		Mockito.when(order.getEntries()).thenReturn(entries);
		Mockito.when(entry.getProduct()).thenReturn(product);
		Mockito.when(product.getCode()).thenReturn("12345");
		final Transition execute = action.executeAction(orderProcess);
		assertThat(execute).isEqualTo(Transition.NOK);
		verify(order).setStatus(OrderStatus.PAYMENT_NOT_CAPTURED);
		verify(modelService).save(order);
		verifyNoMoreInteractions(modelService);
	}

}
