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
 *
 */
package de.hybris.platform.financialfacades.strategies.impl;


import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Test;


/**
 * The class of LifeQuotationPricingStrategyTest.
 */
@UnitTest
public class LifeQuotationPricingStrategyTest
{
	@Test
	public void shouldReturnLifeQuotationFormattedPrice()
	{
		final String expectedPriceString = "1234.56";
		final double priceValue = 1234.56d;

		final LifeQuotationPricingStrategy strategy = new LifeQuotationPricingStrategy();
		final String formattedPrice = strategy.getPriceFormat(priceValue);

		Assert.assertEquals(expectedPriceString, formattedPrice);
	}

	@Test
	public void shouldReturnLifeQuotationFormattedPriceWithMoreThanTwoDecimalDigit()
	{
		final String expectedPriceString = "1234.57";
		final double priceValue = 1234.5678d;

		final LifeQuotationPricingStrategy strategy = new LifeQuotationPricingStrategy();
		final String formattedPrice = strategy.getPriceFormat(priceValue);

		Assert.assertEquals(expectedPriceString, formattedPrice);
	}
}
