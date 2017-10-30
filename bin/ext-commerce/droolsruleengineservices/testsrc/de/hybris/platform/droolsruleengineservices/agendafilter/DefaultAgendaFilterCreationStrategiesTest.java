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
package de.hybris.platform.droolsruleengineservices.agendafilter;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.droolsruleengineservices.impl.AbstractRuleEngineServicesTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.model.DroolsRuleEngineContextModel;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Integration tests for DefaultAgendaFilterFactory
 *
 */
@IntegrationTest
public class DefaultAgendaFilterCreationStrategiesTest extends AbstractRuleEngineServicesTest
{

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/droolsruleengineservices/test/ruleenginesetup.impex", "utf-8");
	}

	@Test
	public void testActionTriggerintLimitAgendaFilter() throws ImpExException
	{
		importCsv("/droolsruleengineservices/test/agendafilter-limitexecutions-testdata.impex", "utf-8");
		final DroolsRuleEngineContextModel context = (DroolsRuleEngineContextModel) getRuleEngineContextDao()
				.getRuleEngineContextByName(RULE_ENGINGE_CONTEXT_NAME);
		// initialize the engine to make sure drools kiebase is setup
		initializeRuleEngine(context);
		final RuleEvaluationResult engineResult = evaluate(Collections.singleton(createCartRAO("123", "USD")));
		Assert.assertFalse(engineResult.isEvaluationFailed());
		final RuleEngineResultRAO result = engineResult.getResult();
		Assert.assertNotNull(result);
		Assert.assertNotNull(result.getActions());
		Assert.assertEquals(5, result.getActions().size());

	}
}
