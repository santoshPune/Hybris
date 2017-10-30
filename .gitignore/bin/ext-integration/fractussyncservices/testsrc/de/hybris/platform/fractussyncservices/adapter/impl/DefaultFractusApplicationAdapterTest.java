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

package de.hybris.platform.fractussyncservices.adapter.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.fractussyncservices.adapter.FractusApplicationLookupStrategy;
import de.hybris.platform.fractussyncservices.adapter.FractusNoApplicationLookupStrategyFoundException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;


@UnitTest
public class DefaultFractusApplicationAdapterTest
{

	private DefaultFractusApplicationAdapter fractusApplicationAdapter;

	@Mock
	private Item item;

	@Mock
	private ComposedType composedType;

	@Mock
	private FractusApplicationLookupStrategy strategy;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		fractusApplicationAdapter = new DefaultFractusApplicationAdapter();
		fractusApplicationAdapter.setLookupStrategies(Lists.newArrayList(strategy));
	}

	@Test
	public void shouldReturnApplicationId() throws FractusNoApplicationLookupStrategyFoundException
	{
		final String expectedVal = "ApplicationId";
		Mockito.when(strategy.getTypeCode()).thenReturn("code");
		Mockito.when(strategy.lookup(item)).thenReturn(expectedVal);
		Mockito.when(item.getComposedType()).thenReturn(composedType);
		Mockito.when(composedType.getCode()).thenReturn("code");

		final String actualVal = fractusApplicationAdapter.getApplicationId(item);

		Assert.assertEquals(expectedVal, actualVal);
	}

	@Test(expected = FractusNoApplicationLookupStrategyFoundException.class)
	public void shouldThrowException() throws FractusNoApplicationLookupStrategyFoundException
	{
		final String expectedVal = "ApplicationId";
		Mockito.when(strategy.getTypeCode()).thenReturn("code1");
		Mockito.when(strategy.lookup(item)).thenReturn(expectedVal);
		Mockito.when(item.getComposedType()).thenReturn(composedType);
		Mockito.when(composedType.getCode()).thenReturn("code2");

		fractusApplicationAdapter.getApplicationId(item);
	}



}
