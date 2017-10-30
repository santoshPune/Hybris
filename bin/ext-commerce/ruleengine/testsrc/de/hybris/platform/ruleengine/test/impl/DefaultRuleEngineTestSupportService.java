/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.ruleengine.test.impl;

import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.AbstractRulesModuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Default implementation (basically empty) of RuleEngineTestSupportService
 */
public class DefaultRuleEngineTestSupportService implements RuleEngineTestSupportService
{

	@Override
	public AbstractRulesModuleModel getTestRulesModule(final AbstractRuleEngineContextModel abstractContext,
			final Set<AbstractRuleEngineRuleModel> ruleModels)
	{
		throw new UnsupportedOperationException(getnotImplementedErrorMessage());
	}

	@Override
	public Optional<AbstractRulesModuleModel> resolveAssociatedRuleModule(final AbstractRuleEngineRuleModel ruleModel)
	{
		throw new UnsupportedOperationException(getnotImplementedErrorMessage());
	}

	@Override
	public Consumer<AbstractRuleEngineRuleModel> decorateRuleForTest(final Map<String, String> params)
	{
		throw new UnsupportedOperationException(getnotImplementedErrorMessage());
	}

	@Override
	public String getTestModuleName(final AbstractRuleEngineRuleModel ruleModel)
	{
		throw new UnsupportedOperationException(getnotImplementedErrorMessage());
	}

	@Override
	public AbstractRuleEngineRuleModel createRuleModel()
	{
		throw new UnsupportedOperationException(getnotImplementedErrorMessage());
	}

	protected String getnotImplementedErrorMessage()
	{
		return "In order to provide the correct test execution, the consistent implementation of the rule engine extension must be added";
	}

}
