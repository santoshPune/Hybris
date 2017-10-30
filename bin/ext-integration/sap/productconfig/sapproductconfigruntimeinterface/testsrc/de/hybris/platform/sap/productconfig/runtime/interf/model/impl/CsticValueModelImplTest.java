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
 */
package de.hybris.platform.sap.productconfig.runtime.interf.model.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticValueModel;

import org.junit.Test;


@UnitTest
public class CsticValueModelImplTest
{

	@Test
	public void testEquals_smallWith0Fraction()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("1");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("1.0");
		assertTrue(value1.equals(value2));
	}

	@Test
	public void testEquals_bigWith0Fraction()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("12345678.0");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("12345678");
		assertTrue(value1.equals(value2));
	}

	@Test
	public void testEquals_bigWith00Fraction()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("12345678.00");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("12345678");
		assertTrue(value1.equals(value2));
	}

	@Test
	public void testEquals_bigWithFractionAndExponential()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("9999999999.99999");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("9.99999999999999E9");
		assertTrue(value1.equals(value2));
	}

	@Test
	public void testEquals_bigNegativeWithFractionAndExponential()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("-9999999999.99999");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("-9.99999999999999E9");
		assertTrue(value1.equals(value2));
	}


	@Test
	public void testEquals_bigWithExponential()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("12345678");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("1.2345678E7");
		assertTrue(value1.equals(value2));
	}

	@Test
	public void testEquals_bigNegativeWithExponential()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("-12343678");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("-1.2343678E7");
		assertTrue(value1.equals(value2));
	}

	@Test
	public void testNotEquals()
	{
		final CsticValueModel value1 = new CsticNumericValueModelImpl();
		value1.setName("123a43678");
		final CsticValueModel value2 = new CsticNumericValueModelImpl();
		value2.setName("12343678");
		assertFalse(value1.equals(value2));
	}

	@Test
	public void testNotEqualsNoNumericCstic()
	{
		final CsticValueModel value1 = new CsticValueModelImpl();
		value1.setName("01");
		final CsticValueModel value2 = new CsticValueModelImpl();
		value2.setName("1");
		assertFalse(value1.equals(value2));
	}


}
