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
package de.hybris.platform.ruleengineservices.definitions.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultRuleContainerValidatorTest
{
	private DefaultRuleContainerValidator ruleConditionContainerValidator;

	@Before
	public void setUp()
	{
		ruleConditionContainerValidator = new DefaultRuleContainerValidator();
	}

	@Test
	public void validContainerId()
	{
		//when
		final boolean isValid = ruleConditionContainerValidator.isValidContainerId("container-test");

		//then
		Assert.assertTrue(isValid);
	}

	@Test
	public void invalidContainerId()
	{
		//when
		final boolean isValid = ruleConditionContainerValidator.isValidContainerId("container:test");

		//then
		Assert.assertFalse(isValid);
	}
}
