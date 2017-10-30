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
package de.hybris.platform.ruleengineservices.rule.services.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultRuleServiceUnitTest
{
	private static final String DEFAULT_RULE_TYPE = "DEFAULT";

	@Mock
	private RuleDao ruleDao;

	private DefaultRuleService defaultRuleService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		defaultRuleService = new DefaultRuleService();
		defaultRuleService.setRuleDao(ruleDao);
	}

	@Test
	public void testFindEngineRuleTypeForRuleType()
	{
		when(ruleDao.findEngineRuleTypeByRuleType(SourceRuleModel.class)).thenReturn(RuleType.DEFAULT);

		final RuleType ruleType = defaultRuleService.getEngineRuleTypeForRuleType(SourceRuleModel.class);
		assertEquals(DEFAULT_RULE_TYPE, ruleType.getCode());
	}

	@Test
	public void testFindEngineRuleTypeForRuleTypeNotFound()
	{
		when(ruleDao.findEngineRuleTypeByRuleType(SourceRuleModel.class)).thenReturn(null);

		final RuleType ruleType = defaultRuleService.getEngineRuleTypeForRuleType(SourceRuleModel.class);
		assertEquals(DEFAULT_RULE_TYPE, ruleType.getCode());
	}
}
