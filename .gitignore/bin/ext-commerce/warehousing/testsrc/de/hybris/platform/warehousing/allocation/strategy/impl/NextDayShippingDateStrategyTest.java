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
package de.hybris.platform.warehousing.allocation.strategy.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.warehousing.model.SourcingConfigModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class NextDayShippingDateStrategyTest
{
	private Calendar calendar;

	@Mock
	private TimeService timeService;
	@Mock
	private ConsignmentModel consignment;

	@InjectMocks
	private NextDayShippingDateStrategy nextDayShippingDateStrategy = new NextDayShippingDateStrategy();


	@Before
	public void setup()
	{
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);

		when(timeService.getCurrentTime()).thenReturn(new Date());
	}

	@Test
	public void shouldGetExpectedShippingDate()
	{
		//When
		final Date date = nextDayShippingDateStrategy.getExpectedShippingDate(consignment);

		//Then
		verify(timeService).getCurrentTime();
		assertEquals(calendar.getTime().getDate(), date.getDate());
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldNotGetExpectedShippingDate()
	{
		//When
		nextDayShippingDateStrategy.getExpectedShippingDate(null);
	}
}
