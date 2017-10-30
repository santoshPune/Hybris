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
package de.hybris.platform.ruleengineservices.compiler.impl;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContextFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListener;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListenersFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResultFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessor;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesGeneratorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleSourceCodeTranslatorFactory;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGenerator;
import de.hybris.platform.ruleengineservices.compiler.RuleTargetCodeGeneratorFactory;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Collections;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultRuleCompilerServiceTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Mock
	private AbstractRuleModel rule;

	@Mock
	private RuleIrVariablesGenerator variablesGenerator;

	@Mock
	private DefaultRuleCompilerContext context;

	@Mock
	private RuleCompilerResult ruleCompilerResult;

	@Mock
	private RuleCompilerListenersFactory ruleCompilerListenersFactory;

	@Mock
	private RuleIrVariablesGeneratorFactory ruleIrVariablesGeneratorFactory;

	@Mock
	private RuleCompilerContextFactory<DefaultRuleCompilerContext> ruleCompilerContextFactory;

	@Mock
	private RuleSourceCodeTranslatorFactory ruleSourceCodeTranslatorFactory;

	@Mock
	private RuleSourceCodeTranslator ruleSourceCodeTranslator;

	@Mock
	private RuleIrProcessorFactory ruleIrProcessorFactory;

	@Mock
	private RuleIrProcessor ruleIrProcessor;

	@Mock
	private RuleTargetCodeGeneratorFactory ruleTargetCodeGeneratorFactory;

	@Mock
	private RuleTargetCodeGenerator ruleTargetCodeGenerator;

	@Mock
	private RuleCompilerResultFactory ruleCompilerResultFactory;

	@Mock
	private ModelService modelService;

	private DefaultRuleCompilerService ruleCompilerService;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);

		when(ruleCompilerListenersFactory.getListeners(RuleCompilerListener.class)).thenReturn(Collections.emptyList());
		when(ruleIrVariablesGeneratorFactory.createVariablesGenerator()).thenReturn(variablesGenerator);
		when(ruleCompilerContextFactory.createContext(rule, variablesGenerator)).thenReturn(context);
		when(context.getProblems()).thenReturn(Collections.EMPTY_LIST);
		when(ruleCompilerResultFactory.createCompilerResult(context.getProblems())).thenReturn(ruleCompilerResult);

		when(ruleSourceCodeTranslatorFactory.getSourceCodeTranslator(context)).thenReturn(ruleSourceCodeTranslator);
		when(ruleIrProcessorFactory.getRuleIrProcessors()).thenReturn(Collections.singletonList(ruleIrProcessor));
		when(ruleTargetCodeGeneratorFactory.getTargetCodeGenerator(context)).thenReturn(ruleTargetCodeGenerator);


		ruleCompilerService = new DefaultRuleCompilerService();
		ruleCompilerService.setRuleCompilerListenersFactory(ruleCompilerListenersFactory);
		ruleCompilerService.setRuleIrVariablesGeneratorFactory(ruleIrVariablesGeneratorFactory);
		ruleCompilerService.setRuleCompilerContextFactory(ruleCompilerContextFactory);
		ruleCompilerService.setRuleSourceCodeTranslatorFactory(ruleSourceCodeTranslatorFactory);
		ruleCompilerService.setRuleIrProcessorFactory(ruleIrProcessorFactory);
		ruleCompilerService.setRuleTargetCodeGeneratorFactory(ruleTargetCodeGeneratorFactory);
		ruleCompilerService.setRuleCompilerResultFactory(ruleCompilerResultFactory);
		ruleCompilerService.setModelService(modelService);
	}

	@Test
	public void testCompile() throws Exception
	{
		ruleCompilerService.compile(rule);
	}
}
