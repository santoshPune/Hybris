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
package de.hybris.platform.droolsruleengineservices.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCommerceRuleEngineServiceTest
{
	@InjectMocks
	private DefaultCommerceRuleEngineService service;
	@Mock
	private RuleEngineService platformRuleEngineService;

	@Test
	public void testGetRuleModuleForRuleForSuccessCondition()
	{
		final DroolsRuleModel ruleEngineRule = mock(DroolsRuleModel.class);
		final DroolsKIEModuleModel ruleModule = mock(DroolsKIEModuleModel.class);
		when(platformRuleEngineService.getRuleModuleForRule(ruleEngineRule)).thenReturn(ruleModule);
		final AbstractRulesModuleModel rulesModuleModel = service.getRuleModuleForRule(ruleEngineRule);
		assertEquals(ruleModule, rulesModuleModel);
	}
}
