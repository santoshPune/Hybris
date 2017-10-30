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
package de.hybris.platform.promotionengineservices.compiler.listeners;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.compiler.strategies.ConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.compiler.strategies.impl.DefaultCatConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.compiler.strategies.impl.DefaultProductConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PromotionRuleCompilerListenerUnitTest
{
	private static final String RULE_UUID = "111-222";
	private static final String CAT_CONDITION_DEFINITION_ID = "y_qualifying_categories";
	private static final String CAT_PARAM_KEY = "categories";
	private static final String PROD_CONDITION_DEFINITION_ID = "y_qualifying_products";
	private static final String PROD_PARAM_KEY = "products";

	@InjectMocks
	private PromotionRuleCompilerListener listener;

	@Mock
	private Map<String, ConditionResolutionStrategy> parameterResolutionStrategies;

	@Mock
	private DefaultCatConditionResolutionStrategy catStrategy;

	@Mock
	private DefaultProductConditionResolutionStrategy productStrategy;

	@Mock
	private RuleCompilerContext context;

	@Mock
	private EngineRuleDao engineRuleDao;

	@Before
	public void setUp() throws ImpExException, IOException
	{
		final Collection strategies = Arrays.asList(catStrategy, productStrategy);
		when(parameterResolutionStrategies.values()).thenReturn(strategies);
		when(parameterResolutionStrategies.get(PROD_CONDITION_DEFINITION_ID)).thenReturn(productStrategy);
		when(parameterResolutionStrategies.get(CAT_CONDITION_DEFINITION_ID)).thenReturn(catStrategy);
	}

	@Test
	public void testCleanStoredParameterValues()
	{
		final PromotionSourceRuleModel rule = new PromotionSourceRuleModel();

		doNothing().when(catStrategy).cleanStoredParameterValues(rule);
		doNothing().when(productStrategy).cleanStoredParameterValues(rule);

		listener = spy(listener);

		listener.cleanStoredParameterValues(rule);

		verify(catStrategy).cleanStoredParameterValues(rule);
		verify(productStrategy).cleanStoredParameterValues(rule);
	}

	@Test
	public void testAfterCompile() throws RuleCompilerException
	{
		final PromotionSourceRuleModel rule = new PromotionSourceRuleModel();
		rule.setUuid(RULE_UUID);

		final RuleBasedPromotionModel promotion = new RuleBasedPromotionModel();

		final AbstractRuleEngineRuleModel ruleModel = new AbstractRuleEngineRuleModel();
		ruleModel.setPromotion(promotion);

		final RuleParameterData parameter1 = new RuleParameterData();
		parameter1.setValue(Arrays.asList("prod1", "prod2"));
		final Map<String, RuleParameterData> prodParameters = new HashMap();
		prodParameters.put(PROD_PARAM_KEY, parameter1);

		final RuleParameterData parameter2 = new RuleParameterData();
		parameter2.setValue(Arrays.asList("cat1"));
		final Map<String, RuleParameterData> catParameters = new HashMap();
		catParameters.put(CAT_PARAM_KEY, parameter2);

		final RuleConditionData prodCondition = new RuleConditionData();
		prodCondition.setDefinitionId(PROD_CONDITION_DEFINITION_ID);
		prodCondition.setParameters(prodParameters);

		final RuleConditionData catCondition = new RuleConditionData();
		catCondition.setDefinitionId(CAT_CONDITION_DEFINITION_ID);
		catCondition.setParameters(catParameters);

		when(context.getRuleConditions()).thenReturn(Arrays.asList(prodCondition, catCondition));
		when(context.getRule()).thenReturn(rule);
		doNothing().when(productStrategy).getAndStoreParameterValues(prodCondition, rule, promotion);
		doNothing().when(catStrategy).getAndStoreParameterValues(catCondition, rule, promotion);
		when(engineRuleDao.getRuleByUuid(RULE_UUID)).thenReturn(ruleModel);

		listener = spy(listener);

		listener.afterCompile(context);

		verify(productStrategy).getAndStoreParameterValues(prodCondition, rule, promotion);
		verify(catStrategy).getAndStoreParameterValues(catCondition, rule, promotion);
	}


}
