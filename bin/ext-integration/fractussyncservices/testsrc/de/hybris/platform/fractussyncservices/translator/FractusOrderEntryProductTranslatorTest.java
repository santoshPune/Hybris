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
import de.hybris.platform.fractussyncservices.adapter.FractusValueOrderEntryProductAdapter;
import de.hybris.platform.jalo.Item;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class FractusOrderEntryProductTranslatorTest
{
	@InjectMocks
	private FractusOrderEntryProductTranslator translator;

	@Mock
	private FractusValueOrderEntryProductAdapter fractusOrderEntryProductAdapter;

	@Mock
	private Item item;

	@Before
	public void setup()
	{
		translator = new FractusOrderEntryProductTranslator();
		MockitoAnnotations.initMocks(this);
	}


	@Test
	public void shouldPerformImport()
	{
		final String cellVal = "productCode|orderCode";

		translator.importValue(cellVal, item);

		Mockito.verify(fractusOrderEntryProductAdapter, Mockito.times(1)).performImport(cellVal);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void shouldThrowUnsupportedOperationException()
	{
		final Object o = "anyStringObject";

		translator.exportValue(o);
	}
}
