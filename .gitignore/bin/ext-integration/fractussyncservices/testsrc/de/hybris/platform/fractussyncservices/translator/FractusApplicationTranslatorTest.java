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
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.fractussyncservices.adapter.FractusApplicationAdapter;
import de.hybris.platform.fractussyncservices.adapter.FractusNoApplicationLookupStrategyFoundException;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FractusApplicationTranslatorTest
{

	private FractusApplicationTranslator translator;

	@Mock
	private FractusApplicationAdapter adapter;

	@Mock
	private Item item;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		translator = new FractusApplicationTranslator();
		translator.setApplicationAdapter(adapter);
	}

	@Test
	public void shouldTranslateValueForExport() throws ImpExException, FractusNoApplicationLookupStrategyFoundException
	{
		final String expectedVal = "ApplicationId";
		Mockito.when(adapter.getApplicationId(item)).thenReturn("ApplicationId");
		final String actualVal = translator.performExport(item);

		Assert.assertEquals(expectedVal, actualVal);
		Mockito.verify(adapter, Mockito.times(1)).getApplicationId(item);
	}

	@Test(expected = ImpExException.class)
	public void shouldThrowExceptionWhenExport() throws FractusNoApplicationLookupStrategyFoundException, ImpExException
	{
		Mockito.when(adapter.getApplicationId(item)).thenThrow(FractusNoApplicationLookupStrategyFoundException.class);
		translator.performExport(item);
	}

}
