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
package de.hybris.platform.ruledefinitions.validation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblemFactory;
import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class RulePositiveNumberParameterValidatorTest
{
	@Mock
	private RuleCompilerContext context;
	@Mock
	private RuleCompilerProblemFactory ruleCompilerProblemFactory;

	private AbstractRuleDefinitionData ruleDefinition;
	private RuleParameterData parameter;
	private RuleParameterDefinitionData parameterDefinition;

	private RulePositiveNumberParameterValidator rulePositiveNumberParameterValidator;

	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);

		ruleDefinition = new AbstractRuleDefinitionData();
		parameter = new RuleParameterData();
		parameterDefinition = new RuleParameterDefinitionData();
		rulePositiveNumberParameterValidator = new RulePositiveNumberParameterValidator();

		rulePositiveNumberParameterValidator.setRuleCompilerProblemFactory(ruleCompilerProblemFactory);
	}

	@Test
	public void testValueIsNegative() throws RuleEngineServiceException
	{
		// given
		parameter.setValue(Double.valueOf(Double.NEGATIVE_INFINITY));

		// when
		rulePositiveNumberParameterValidator.validate(context, ruleDefinition, parameter, parameterDefinition);

		// then
		verify(context).addProblem(any(RuleCompilerProblem.class));
	}


}
