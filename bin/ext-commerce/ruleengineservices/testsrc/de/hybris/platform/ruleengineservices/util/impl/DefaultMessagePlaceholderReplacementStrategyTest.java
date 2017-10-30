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
package de.hybris.platform.ruleengineservices.util.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.util.impl.DefaultMessagePlaceholderReplacementStrategy;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@UnitTest
public class DefaultMessagePlaceholderReplacementStrategyTest
{

	private static final String ANY_STRING = "anyString";

	private final DefaultMessagePlaceholderReplacementStrategy strategy = new DefaultMessagePlaceholderReplacementStrategy();

	@Rule
	public final ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Test
	public void testNullMessage()
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		strategy.replace(null, ANY_STRING, ANY_STRING);
	}

	@Test
	public void testNullFind()
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		strategy.replace(ANY_STRING, null, ANY_STRING);
	}

	@Test
	public void testNullReplaceWith()
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//when
		strategy.replace(ANY_STRING, ANY_STRING, null);
	}

	@Test
	public void testSimpleReplacement()
	{
		final String message = "You qualify for a discount of {368d4c9e-74db-4f12-a17c-a96282d5d7ac}";
		final String find = "368d4c9e-74db-4f12-a17c-a96282d5d7ac";
		final String replace = "0";
		final String expected = "You qualify for a discount of {0}";
		final String result = strategy.replace(message, find, replace);
		Assert.assertEquals("result differs from expected replacement", expected, result);
	}

	@Test
	public void testParameterizedReplacement()
	{
		final String message = "You qualify for a discount of {368d4c9e-74db-4f12-a17c-a96282d5d7ac,number,integer}";
		final String find = "368d4c9e-74db-4f12-a17c-a96282d5d7ac";
		final String replace = "0";
		final String expected = "You qualify for a discount of {0,number,integer}";
		final String result = strategy.replace(message, find, replace);
		Assert.assertEquals("result differs from expected replacement", expected, result);
	}

	@Test
	public void testReplaceStringThatOccurrsAsSubString()
	{
		final String message = "You qualify for a discount of {parameter} because you spent {parameter2}.";
		final String find = "parameter";
		final String replace = "0";
		final String expected = "You qualify for a discount of {0} because you spent {parameter2}.";
		final String result = strategy.replace(message, find, replace);
		Assert.assertEquals("result differs from expected replacement", expected, result);
	}

	@Test
	public void testMultipleReplacementsWithStringThatOccurrsAsSubString()
	{
		final String message = "You qualify for a discount of {parameter} because you spent {parameter2}.";

		final String find1 = "parameter";
		final String replace1 = "0";
		final String expected1 = "You qualify for a discount of {0} because you spent {parameter2}.";
		final String result1 = strategy.replace(message, find1, replace1);
		Assert.assertEquals("result differs from expected replacement", expected1, result1);

		final String find2 = "parameter2";
		final String replace2 = "1";
		final String expected2 = "You qualify for a discount of {0} because you spent {1}.";
		final String result2 = strategy.replace(result1, find2, replace2);
		Assert.assertEquals("result differs from expected replacement", expected2, result2);

	}
}
