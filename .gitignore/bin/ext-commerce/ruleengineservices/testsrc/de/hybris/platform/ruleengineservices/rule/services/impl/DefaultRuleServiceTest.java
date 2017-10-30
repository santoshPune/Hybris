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
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleTemplateModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


@IntegrationTest
public class DefaultRuleServiceTest extends ServicelayerTransactionalTest
{
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); //NOPMD

	@Resource
	private DefaultRuleService defaultRuleService;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws Exception
	{
		importCsv("/ruleengineservices/test/rule/DefaultRuleServiceTest.impex", "utf-8");
	}

	@Test
	public void testCreateRuleFromTemplate()
	{
		// given
		final SourceRuleTemplateModel ruleTemplate = modelService.create(SourceRuleTemplateModel.class);
		ruleTemplate.setCode("testRuleTemplate");
		ruleTemplate.setName("Test Rule Template");
		ruleTemplate.setConditions("[{\"parameters\":{},\"definitionId\":\"group\"}]");
		ruleTemplate.setActions("[{\"parameters\":{\"value\":{\"value\":10}},\"definitionId\":\"y_order_percentage_discount\"}]");

		// when
		final SourceRuleModel createdRule = defaultRuleService.createRuleFromTemplate((AbstractRuleTemplateModel) ruleTemplate);

		// then
		assertTrue(createdRule.getCode().startsWith(ruleTemplate.getCode() + "-"));
		assertEquals("Test Rule Template", createdRule.getName());
		assertEquals(ruleTemplate.getConditions(), createdRule.getConditions());
		assertEquals(ruleTemplate.getActions(), createdRule.getActions());
	}

	@Test
	public void testCreateRuleFromTemplateTemplateNull()
	{
		// expect
		expectedException.expect(IllegalArgumentException.class);

		// when
		defaultRuleService.createRuleFromTemplate((AbstractRuleTemplateModel) null);
	}
}
