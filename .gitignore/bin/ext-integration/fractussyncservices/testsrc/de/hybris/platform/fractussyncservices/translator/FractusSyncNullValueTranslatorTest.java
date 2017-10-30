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
import de.hybris.platform.jalo.Item;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import junit.framework.Assert;


@UnitTest
public class FractusSyncNullValueTranslatorTest
{
	private FractusSyncNullValueTranslator fractusSyncNullValueTranslator;
	private final String valueExpr = "anyValue";

	@Mock
	private Item item;

	@Before
	public void setup()
	{
		fractusSyncNullValueTranslator = new FractusSyncNullValueTranslator();
	}

	@Test
	public void testExportValue()
	{
		Assert.assertEquals("<empty>", fractusSyncNullValueTranslator.exportValue(null));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testImportValue()
	{
		fractusSyncNullValueTranslator.importValue(valueExpr, item);
	}
}
