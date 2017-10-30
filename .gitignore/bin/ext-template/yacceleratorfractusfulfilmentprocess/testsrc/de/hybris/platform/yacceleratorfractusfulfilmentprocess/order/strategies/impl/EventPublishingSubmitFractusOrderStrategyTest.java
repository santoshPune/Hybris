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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.events.SubmitFractusOrderEvent;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * The class of EventPublishingSubmitFractusOrderStrategyTest.
 */
@UnitTest
public class EventPublishingSubmitFractusOrderStrategyTest
{
	@InjectMocks
	EventPublishingSubmitFractusOrderStrategy eventPublishingSubmitFractusOrderStrategy = new EventPublishingSubmitFractusOrderStrategy();

	@Mock
	EventService eventService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSubmitFractusOrder()
	{
		final OrderModel order = new OrderModel();

		eventPublishingSubmitFractusOrderStrategy.submitOrder(order);

		final Matcher<SubmitOrderEvent> matcher = new BaseMatcher<SubmitOrderEvent>()
		{

			@Override
			public boolean matches(final Object compareTo)
			{
				if (compareTo instanceof SubmitFractusOrderEvent)
				{
					final SubmitFractusOrderEvent event = (SubmitFractusOrderEvent) compareTo;

					return event.getOrder() == order;
				}
				return false;
			}

			@Override
			public void describeTo(final Description description)
			{
				description.appendText("Argument should be an SubmitFractusOrderEvent for order= " + order);

			}
		};

		Mockito.verify(eventService).publishEvent(Mockito.argThat(matcher));
	}
}
