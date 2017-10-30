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
package de.hybris.platform.ruleengineservices.maintenance.impl;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengineservices.RuleEngineServiceException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem.Severity;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerService;
import de.hybris.platform.ruleengineservices.compiler.impl.DefaultRuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.impl.DefaultRuleCompilerResult;
import de.hybris.platform.ruleengineservices.enums.RuleStatus;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult.Result;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;


@UnitTest
public class DefaultRuleMaintenanceServiceTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private ModelService modelService;
	@Mock
	private RuleEngineService ruleEngineService;
	@Mock
	private RuleCompilerService ruleCompilerService;
	@Mock
	private AbstractRuleModel rule;
	@Mock
	private AbstractRuleEngineRuleModel engineRule;
	@Mock
	private DroolsKIEModuleModel ruleModule;

	@InjectMocks
	private final DefaultRuleMaintenanceService service = new DefaultRuleMaintenanceService();

	@Before
	public void setUp() throws RuleCompilerException
	{
		initMocks(this);

		final RuleEngineActionResult publisherResult = new RuleEngineActionResult();
		publisherResult.setActionFailed(false);

		when(ruleEngineService.getRuleForUuid(Matchers.anyString())).thenReturn(engineRule);
		when(ruleEngineService.archiveRule(engineRule)).thenReturn(publisherResult);

		final RuleCompilerResult compilerResult = new DefaultRuleCompilerResult(Collections.EMPTY_LIST);
		when(ruleCompilerService.compile(rule)).thenReturn(compilerResult);

		when(ruleEngineService.getRuleModuleForRule(engineRule)).thenReturn(ruleModule);
		when(ruleEngineService.initialize(ruleModule, true)).thenReturn(publisherResult);
	}

	@Test
	public void archiveRuleNull() throws RuleEngineServiceException
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//then
		service.archiveRule(null);
	}

	@Test
	public void archiveRuleValid() throws RuleEngineServiceException
	{
		//given

		//when
		service.archiveRule(rule);

		//then
		Mockito.verify(rule, Mockito.times(1)).setStatus(RuleStatus.ARCHIVED);
		Mockito.verify(modelService, Mockito.times(1)).save(rule);
	}

	@Test
	public void archiveRuleFailedToArchive() throws RuleEngineServiceException
	{
		//given
		final RuleEngineActionResult publisherResult = new RuleEngineActionResult();
		publisherResult.setActionFailed(true);
		Mockito.when(ruleEngineService.archiveRule(engineRule)).thenReturn(publisherResult);

		//expect
		expectedException.expect(RuleEngineServiceException.class);

		//when
		service.archiveRule(rule);
	}

	@Test
	public void compileAndPublishRulesNull() throws RuleEngineServiceException
	{
		//expect
		expectedException.expect(IllegalArgumentException.class);

		//then
		service.compileAndPublishRules(null);
	}

	@Test
	public void compileAndPublishRulesEmpty() throws RuleEngineServiceException
	{
		//given
		final List<AbstractRuleModel> emptyList = Collections.EMPTY_LIST;

		//when
		final RuleCompilerPublisherResult result = service.compileAndPublishRules(emptyList);

		//then
		Assert.assertEquals(Result.SUCCESS, result.getResult());
	}

	@Test
	public void compileAndPublishRuleSomeRule() throws RuleEngineServiceException
	{
		//given

		//when
		final RuleCompilerPublisherResult result = service.compileAndPublishRules(Arrays.asList(rule));

		//then
		Assert.assertEquals(Result.SUCCESS, result.getResult());
	}

	@Test
	public void compileAndPublishRuleCompilerProblem() throws RuleEngineServiceException
	{
		//given
		final RuleCompilerResult failedCompilerResult = new DefaultRuleCompilerResult(
				Arrays.asList(new DefaultRuleCompilerProblem(Severity.ERROR, "test message")));
		Mockito.when(ruleCompilerService.compile(rule)).thenReturn(failedCompilerResult);
		//when
		final RuleCompilerPublisherResult result = service.compileAndPublishRules(Arrays.asList(rule));

		//then
		Assert.assertEquals(Result.COMPILER_ERROR, result.getResult());
	}

	@Test
	public void compileAndPublishRulePublisherProblem() throws RuleEngineServiceException
	{
		//given
		final RuleEngineActionResult failedPublisherResult = new RuleEngineActionResult();
		failedPublisherResult.setActionFailed(true);
		Mockito.when(ruleEngineService.initialize(ruleModule, true)).thenReturn(failedPublisherResult);

		Mockito.when(ruleEngineService.initializeAllRulesModules()).thenReturn(Arrays.asList(failedPublisherResult));

		//when
		final RuleCompilerPublisherResult result = service.compileAndPublishRules(Arrays.asList(rule));

		//then
		Assert.assertEquals(Result.PUBLISHER_ERROR, result.getResult());
	}

}
