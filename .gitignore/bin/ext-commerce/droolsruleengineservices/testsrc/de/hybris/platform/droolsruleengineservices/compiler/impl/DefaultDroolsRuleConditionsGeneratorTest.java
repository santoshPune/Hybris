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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleGeneratorContext;
import de.hybris.platform.droolsruleengineservices.compiler.DroolsRuleValueFormatter;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariable;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class DefaultDroolsRuleConditionsGeneratorTest extends AbstractGeneratorTest
{
	public static final String INDENTATION = "  ";
	public static final String VARIABLE_PREFIX = "$";

	public static final String CART_VARIABLE_NAME = "cart";
	public static final String CART_VARIABLE_CLASS_NAME = "CartRAO";

	public static final String PRODUCT_VARIABLE_NAME = "product";
	public static final String PRODUCT_VARIABLE_CLASS_NAME = "ProductRAO";

	public static final String RESULT_VARIABLE_NAME = "result";
	public static final String RESULT_VARIABLE_CLASS_NAME = "RuleEngineResultRAO";

	public static final String ACTION_CONTEXT_CLASS_NAME = "DefaultDroolsRuleActionContext";

	@Mock
	private DroolsRuleGeneratorContext droolsRuleGeneratorContext;

	@Mock
	private DroolsRuleValueFormatter droolsRuleValueFormatter;

	private RuleIrVariable cartVariable;
	private RuleIrVariable productVariable;
	private RuleIrVariable resultVariable;

	private RuleIr ruleIr;
	private Map<String, RuleIrVariable> ruleIrVariables;
	private RuleIrTypeCondition ruleIrResultCondition;

	private DefaultDroolsRuleConditionsGenerator conditionsGenerator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		cartVariable = new RuleIrVariable();
		cartVariable.setName(CART_VARIABLE_NAME);
		cartVariable.setType(CartRAO.class);

		productVariable = new RuleIrVariable();
		productVariable.setName(PRODUCT_VARIABLE_NAME);
		productVariable.setType(ProductRAO.class);

		resultVariable = new RuleIrVariable();
		resultVariable.setName(RESULT_VARIABLE_NAME);
		resultVariable.setType(RuleEngineResultRAO.class);

		ruleIr = new RuleIr();
		ruleIrVariables = new LinkedHashMap<>();

		ruleIrResultCondition = new RuleIrTypeCondition();
		ruleIrResultCondition.setVariable(RESULT_VARIABLE_NAME);

		when(droolsRuleGeneratorContext.getIndentationSize()).thenReturn(INDENTATION);
		when(droolsRuleGeneratorContext.getVariablePrefix()).thenReturn(VARIABLE_PREFIX);
		when(droolsRuleGeneratorContext.getRuleIr()).thenReturn(ruleIr);
		when(droolsRuleGeneratorContext.getVariables()).thenReturn(ruleIrVariables);
		when(droolsRuleGeneratorContext.getLocalVariables()).thenReturn(new ArrayDeque<>());
		when(droolsRuleGeneratorContext.generateClassName(CartRAO.class)).thenReturn(CART_VARIABLE_CLASS_NAME);
		when(droolsRuleGeneratorContext.generateClassName(ProductRAO.class)).thenReturn(PRODUCT_VARIABLE_CLASS_NAME);
		when(droolsRuleGeneratorContext.generateClassName(RuleEngineResultRAO.class)).thenReturn(RESULT_VARIABLE_CLASS_NAME);

		conditionsGenerator = new DefaultDroolsRuleConditionsGenerator();
		conditionsGenerator.setDroolsRuleValueFormatter(droolsRuleValueFormatter);
	}

	@Test
	public void testSingleCondition() throws Exception
	{
		// given
		final String expectedDroolsCode = getResourceAsString("/droolsruleengineservices/test/compiler/generatedConditionsForSingleCondition.bin");

		final BigDecimal totalValue = BigDecimal.valueOf(20);

		final RuleIrAttributeCondition amountCondition = new RuleIrAttributeCondition();
		amountCondition.setVariable(CART_VARIABLE_NAME);
		amountCondition.setAttribute("total");
		amountCondition.setOperator(RuleIrAttributeOperator.GREATER_THAN);
		amountCondition.setValue(totalValue);

		ruleIrVariables.put(CART_VARIABLE_NAME, cartVariable);
		ruleIrVariables.put(RESULT_VARIABLE_NAME, resultVariable);
		ruleIr.setConditions(Arrays.asList(ruleIrResultCondition, amountCondition));

		when(droolsRuleValueFormatter.formatValue(droolsRuleGeneratorContext, totalValue)).thenReturn(
				"new java.math.BigDecimal(\"100\")");

		// when
		final String generatedDroolsCode = conditionsGenerator.generateConditions(droolsRuleGeneratorContext, INDENTATION);

		// then
		assertEquals(expectedDroolsCode, generatedDroolsCode);
	}

	@Test
	public void testMultipleConditions() throws Exception
	{
		// given
		final String expectedDroolsCode = getResourceAsString("/droolsruleengineservices/test/compiler/generatedConditionsForMultipleConditions.bin");


		final String colorValue = "blue";

		final RuleIrAttributeCondition colorCondition = new RuleIrAttributeCondition();
		colorCondition.setVariable(PRODUCT_VARIABLE_NAME);
		colorCondition.setAttribute("color");
		colorCondition.setOperator(RuleIrAttributeOperator.EQUAL);
		colorCondition.setValue(colorValue);

		final BigDecimal totalValue = BigDecimal.valueOf(100);

		final RuleIrAttributeCondition amountCondition = new RuleIrAttributeCondition();
		amountCondition.setVariable(CART_VARIABLE_NAME);
		amountCondition.setAttribute("total");
		amountCondition.setOperator(RuleIrAttributeOperator.GREATER_THAN);
		amountCondition.setValue(totalValue);

		final RuleIrGroupCondition blueOrCartCondition = new RuleIrGroupCondition();
		blueOrCartCondition.setOperator(RuleIrGroupOperator.AND);
		blueOrCartCondition.setChildren(Arrays.asList(colorCondition, amountCondition));

		final List<String> codeValues = Arrays.asList("123", "456", "789");

		final RuleIrAttributeCondition code = new RuleIrAttributeCondition();
		code.setVariable(PRODUCT_VARIABLE_NAME);
		code.setAttribute("code");
		code.setOperator(RuleIrAttributeOperator.IN);
		code.setValue(codeValues);

		final RuleIrGroupCondition group = new RuleIrGroupCondition();
		group.setOperator(RuleIrGroupOperator.OR);
		group.setChildren(Arrays.asList(code, blueOrCartCondition));

		ruleIrVariables.put(PRODUCT_VARIABLE_NAME, productVariable);
		ruleIrVariables.put(CART_VARIABLE_NAME, cartVariable);
		ruleIrVariables.put(RESULT_VARIABLE_NAME, resultVariable);
		ruleIr.setConditions(Arrays.asList(ruleIrResultCondition, group));

		when(droolsRuleValueFormatter.formatValue(droolsRuleGeneratorContext, colorValue)).thenReturn("\"blue\"");
		when(droolsRuleValueFormatter.formatValue(droolsRuleGeneratorContext, totalValue)).thenReturn(
				"new java.math.BigDecimal(\"100\")");
		when(droolsRuleValueFormatter.formatValue(droolsRuleGeneratorContext, codeValues)).thenReturn("(\"123\",\"456\",\"789\")");

		// when
		final String generatedDroolsCode = conditionsGenerator.generateConditions(droolsRuleGeneratorContext, INDENTATION);

		// then
		assertEquals(expectedDroolsCode, generatedDroolsCode);
	}
}
