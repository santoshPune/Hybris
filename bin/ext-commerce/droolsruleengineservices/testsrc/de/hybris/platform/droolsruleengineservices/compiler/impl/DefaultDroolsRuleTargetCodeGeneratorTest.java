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
package de.hybris.platform.droolsruleengineservices.compiler.impl;

import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleActionsGenerator;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleConditionsGenerator;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesContainer;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultDroolsRuleTargetCodeGeneratorTest
{
	public static final String RULE_UUID = "7be85ae9-8f69-4d51-a3b4-a4b08b457798";
	public static final String RULE_CODE = "rule_code";
	public static final String RULE_NAME = "rule_name";

	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private CatalogUnawareMediaModel mediaModel;

	@Mock
	private SourceRuleModel sourceRule;

	@Mock
	private DroolsRuleModel droolsRule;

	@Mock
	private RuleCompilerContext compilerContext;

	@Mock
	private RuleIrAction action;

	@Mock
	private RuleIrCondition condition;

	@Mock
	private ModelService modelService;

	@Mock
	private RuleEngineService platformRuleEngineService;

	@Mock
	private DroolsRuleConditionsGenerator droolsRuleConditionsGenerator;

	@Mock
	private DroolsRuleActionsGenerator droolsRuleActionsGenerator;

	@Mock
	private CommonI18NService commonI18NService;

	private DefaultDroolsRuleTargetCodeGenerator droolsRuleTargetCodeGenerator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		final RuleEngineActionResult result = new RuleEngineActionResult();
		result.setActionFailed(false);

		given(modelService.create(DroolsRuleModel.class)).willReturn(droolsRule);
		given(modelService.create(CatalogUnawareMediaModel.class)).willReturn(mediaModel);

		given(commonI18NService.getAllLanguages()).willReturn(new ArrayList());

		given(sourceRule.getUuid()).willReturn(RULE_UUID);
		given(sourceRule.getCode()).willReturn(RULE_CODE);
		given(sourceRule.getName()).willReturn(RULE_NAME);
		given(sourceRule.getStartDate()).willReturn(new Date());
		given(sourceRule.getEndDate()).willReturn(new Date());

		given(compilerContext.getRule()).willReturn(sourceRule);
		given(platformRuleEngineService.getRuleForUuid(RULE_UUID)).willReturn(droolsRule);
		given(platformRuleEngineService.updateEngineRule(droolsRule)).willReturn(result);

		droolsRuleTargetCodeGenerator = new DefaultDroolsRuleTargetCodeGenerator();
		droolsRuleTargetCodeGenerator.setModelService(modelService);
		droolsRuleTargetCodeGenerator.setPlatformRuleEngineService(platformRuleEngineService);
		droolsRuleTargetCodeGenerator.setDroolsRuleConditionsGenerator(droolsRuleConditionsGenerator);
		droolsRuleTargetCodeGenerator.setDroolsRuleActionsGenerator(droolsRuleActionsGenerator);
		droolsRuleTargetCodeGenerator.setCommonI18NService(commonI18NService);
	}

	@Test
	public void nullTest() throws RuleCompilerException
	{
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		droolsRuleTargetCodeGenerator.generate(compilerContext, null);
	}

	@Test
	public void emptyActionsTest() throws Exception
	{
		// given
		final RuleIr ruleIr = new RuleIr();
		ruleIr.setVariablesContainer(new RuleIrVariablesContainer());
		ruleIr.setConditions(Collections.singletonList(condition));
		ruleIr.setActions(Collections.EMPTY_LIST);

		// expect
		expectedException.expect(UnknownIdentifierException.class);

		// when
		droolsRuleTargetCodeGenerator.generate(compilerContext, ruleIr);
	}

	@Test
	@Ignore("CT-307")
	public void validRuleIrTest() throws RuleCompilerException
	{
		// given
		final RuleIr ruleIr = new RuleIr();
		ruleIr.setVariablesContainer(new RuleIrVariablesContainer());
		ruleIr.setConditions(Collections.singletonList(condition));
		ruleIr.setActions(Collections.singletonList(action));
		given(droolsRuleConditionsGenerator.generateConditions(new DefaultDroolsGeneratorContext(compilerContext, ruleIr),
				StringUtils.EMPTY)).willReturn("");

		// when
		droolsRuleTargetCodeGenerator.generate(compilerContext, ruleIr);

		//then
		Mockito.verify(platformRuleEngineService, Mockito.times(1)).updateEngineRule(droolsRule);

		//then
		Mockito.verify(platformRuleEngineService, Mockito.times(1)).updateEngineRule(droolsRule);
	}

	@Test
	@Ignore("CT-307")
	public void updateRuleFailed() throws RuleCompilerException
	{
		// given
		final RuleIr ruleIr = new RuleIr();
		ruleIr.setVariablesContainer(new RuleIrVariablesContainer());
		ruleIr.setConditions(Collections.singletonList(condition));
		ruleIr.setActions(Collections.singletonList(action));
		final RuleEngineActionResult result = new RuleEngineActionResult();
		result.setActionFailed(true);
		Mockito.when(platformRuleEngineService.updateEngineRule(droolsRule)).thenReturn(result);

		//expect
		expectedException.expect(RuleCompilerException.class);

		// when
		droolsRuleTargetCodeGenerator.generate(compilerContext, ruleIr);
	}

	@Test
	@Ignore("CT-307")
	public void validCreateNewRule() throws RuleCompilerException
	{
		// given
		final RuleIr ruleIr = new RuleIr();
		ruleIr.setVariablesContainer(new RuleIrVariablesContainer());
		ruleIr.setConditions(Collections.singletonList(condition));
		ruleIr.setActions(Collections.singletonList(action));
		given(platformRuleEngineService.getRuleForUuid(RULE_UUID)).willThrow(ModelNotFoundException.class);

		// when
		droolsRuleTargetCodeGenerator.generate(compilerContext, ruleIr);

		//then
		Mockito.verify(platformRuleEngineService, Mockito.times(1)).updateEngineRule(droolsRule);
	}
}
