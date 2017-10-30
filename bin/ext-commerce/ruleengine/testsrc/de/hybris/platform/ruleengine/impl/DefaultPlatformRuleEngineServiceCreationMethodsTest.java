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
package de.hybris.platform.ruleengine.impl;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import de.hybris.platform.ruleengine.RuleExecutionCountListener;
import de.hybris.platform.ruleengine.exception.RuleEngineRuntimeException;

import org.junit.Before;
import org.junit.Test;


public class DefaultPlatformRuleEngineServiceCreationMethodsTest
{
	private DefaultPlatformRuleEngineService service;

	@Before
	public void setUp()
	{
		service = new DefaultPlatformRuleEngineService();
	}

	@Test
	public void testCreateRuleExecutionCounterListener() throws RuleEngineRuntimeException
	{
		service.setRuleExecutionCounterClass(RuleExecutionCountListener.class);
		try
		{
			service.createRuleExecutionCounterListener();
			fail("Exception expected");
		}
		catch (final RuleEngineRuntimeException e)
		{
			assertThat(e.getMessage(), is(not(nullValue())));
		}

		service.setRuleExecutionCounterClass(RuleMatchCountListener.class);
		final RuleExecutionCountListener createRuleExecutionCounterListener = service.createRuleExecutionCounterListener();
		assertThat(createRuleExecutionCounterListener, is(instanceOf(RuleExecutionCountListener.class)));
	}


}
