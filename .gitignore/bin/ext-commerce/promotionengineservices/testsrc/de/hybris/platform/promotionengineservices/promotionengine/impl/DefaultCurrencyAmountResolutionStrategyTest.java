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
 *
 */
package de.hybris.platform.promotionengineservices.promotionengine.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultCurrencyAmountResolutionStrategyTest
{


	private final DefaultCurrencyAmountResolutionStrategy strategy = new DefaultCurrencyAmountResolutionStrategy();

	@Mock
	private PromotionResultModel promotionResult;

	@Mock
	private AbstractOrderModel order;

	@Mock
	private CurrencyModel currency;

	@Mock
	private RuleParameterData data;

	@Rule
	public final ExpectedException expectedException = ExpectedException.none(); //NOPMD


	@Before
	public void setUp()
	{
		final Map<String, BigDecimal> entries = new LinkedHashMap<>();
		entries.put("USD", new BigDecimal("20.00"));
		entries.put("JPY", new BigDecimal("200.00"));

		MockitoAnnotations.initMocks(this);
		when(promotionResult.getOrder()).thenReturn(order);
		when(order.getCurrency()).thenReturn(currency);
		when(currency.getDigits()).thenReturn(Integer.valueOf(2));
		when(data.getValue()).thenReturn(entries);
	}

	@Test
	public void testNullPromotionResult()
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		strategy.getValue(data, null, Locale.US);
	}

	@Test
	public void testNullData()
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		strategy.getValue(null, promotionResult, Locale.US);
	}

	@Test
	public void testNullLocale()
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		strategy.getValue(data, promotionResult, null);
	}

	@Test
	public void testWrongInputType()
	{
		when(data.getValue()).thenReturn("WRONG_VALUE_TYPE");

		// expect
		expectedException.expect(ClassCastException.class);

		//when
		strategy.getValue(data, promotionResult, Locale.US);
	}

	@Test
	public void testSimpleResolutionUSD()
	{
		// setting up currency to be USD
		when(currency.getIsocode()).thenReturn("USD");
		when(currency.getSymbol()).thenReturn("$");

		final String value = strategy.getValue(data, promotionResult, Locale.US);
		Assert.assertEquals("$20.00", value);
	}

	@Test
	public void testSimpleResolutionJPY()
	{
		// setting up currency to be JPY
		when(currency.getIsocode()).thenReturn("JPY");

		final String value = strategy.getValue(data, promotionResult, Locale.JAPAN);
		Assert.assertEquals("￥200.00", value);
	}
}
