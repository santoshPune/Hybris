/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.test;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.CoreAlgorithms;

import java.math.BigDecimal;

import org.junit.Test;


/**
 *
 */
@UnitTest
public class CoreAlgorithmsTest
{
	// see HORST-1652
	@Test
	public void testRoundHalfUp()
	{
		final BigDecimal v0 = new BigDecimal("1150.5");
		final String v0r = "1151.0";

		final BigDecimal v00 = new BigDecimal("1150.25");
		final String v00r = "1150.3";

		final BigDecimal v0000 = new BigDecimal("1150.2335");
		final String v0000r = "1150.234";

		final BigDecimal v0001 = new BigDecimal("1150.2334");
		final String v0001r = "1150.233";

		assertEquals(v0r, Double.toString(CoreAlgorithms.round(v0.doubleValue(), 0)));
		assertEquals(v00r, Double.toString(CoreAlgorithms.round(v00.doubleValue(), 1)));
		assertEquals(v0000r, Double.toString(CoreAlgorithms.round(v0000.doubleValue(), 3)));
		assertEquals(v0001r, Double.toString(CoreAlgorithms.round(v0001.doubleValue(), 3)));


		final BigDecimal v1150_235 = new BigDecimal("1150.235");
		final String v1150_235r = "1150.24";

		final BigDecimal v1100_235 = new BigDecimal("1100.235");
		final String v1100_235r = "1100.24";

		final BigDecimal v1400_235 = new BigDecimal("1400.235");
		final String v1400_235r = "1400.24";

		assertEquals(v1150_235r, Double.toString(CoreAlgorithms.round(v1150_235.doubleValue(), 2)));
		assertEquals(v1100_235r, Double.toString(CoreAlgorithms.round(v1100_235.doubleValue(), 2)));
		assertEquals(v1400_235r, Double.toString(CoreAlgorithms.round(v1400_235.doubleValue(), 2)));

	}

	@Test
	public void testConvertAndRound()
	{
		final BigDecimal price = new BigDecimal("20.75");
		final BigDecimal tgtRatio = new BigDecimal("1.38");
		final BigDecimal srcRatio = new BigDecimal("1");

		assertEquals("28.635",
				Double.toString(CoreAlgorithms.convert(srcRatio.doubleValue(), tgtRatio.doubleValue(), price.doubleValue())));
		assertEquals("28.64", Double.toString(CoreAlgorithms
				.round(CoreAlgorithms.convert(srcRatio.doubleValue(), tgtRatio.doubleValue(), price.doubleValue()), 2)));
	}

	@Test()
	public void testRoundNaN()
	{
		assertEquals("" + Double.POSITIVE_INFINITY, Double.toString(CoreAlgorithms.round(100.0 / 0.0, 2)));
		assertEquals("" + Double.NEGATIVE_INFINITY, Double.toString(CoreAlgorithms.round(-100.0 / 0.0, 2)));
		assertEquals("" + Double.NaN, Double.toString(CoreAlgorithms.round(0.0 / 0.0, 2)));
	}
}
