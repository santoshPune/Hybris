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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.enums.DroolsEqualityBehavior;
import de.hybris.platform.ruleengine.enums.DroolsEventProcessingMode;
import de.hybris.platform.ruleengine.enums.DroolsSessionType;

import org.junit.Before;
import org.junit.Test;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.model.KieSessionModel.KieSessionType;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;


@UnitTest
public class DefaultPlatformRuleEngineServiceConverterMethodsTest
{

	private DefaultPlatformRuleEngineService service;

	@Before
	public void setUp()
	{
		service = new DefaultPlatformRuleEngineService();
	}

	@Test
	public void testGetSessionType()
	{
		assertThat(service.getSessionType(DroolsSessionType.STATEFUL), is(KieSessionType.STATEFUL));
		assertThat(service.getSessionType(DroolsSessionType.STATELESS), is(KieSessionType.STATELESS));
	}

	@Test
	public void testGetEqualityBehaviorOption()
	{
		final DroolsEqualityBehavior equality = DroolsEqualityBehavior.EQUALITY;
		assertThat(service.getEqualityBehaviorOption(equality), is(EqualityBehaviorOption.EQUALITY));
		final DroolsEqualityBehavior identity = DroolsEqualityBehavior.IDENTITY;
		assertThat(service.getEqualityBehaviorOption(identity), is(EqualityBehaviorOption.IDENTITY));
	}

	@Test
	public void testGetEventProcessingOption()
	{
		assertThat(service.getEventProcessingOption(DroolsEventProcessingMode.STREAM), is(EventProcessingOption.STREAM));
		assertThat(service.getEventProcessingOption(DroolsEventProcessingMode.CLOUD), is(EventProcessingOption.CLOUD));
	}

	@Test
	public void testConvertLevel()
	{
		assertThat(service.convertLevel(null), is(nullValue()));
		assertThat(service.convertLevel(Level.ERROR), is(MessageLevel.ERROR));
		assertThat(service.convertLevel(Level.INFO), is(MessageLevel.INFO));
		assertThat(service.convertLevel(Level.WARNING), is(MessageLevel.WARNING));
	}

}
