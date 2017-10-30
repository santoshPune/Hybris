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
package de.hybris.platform.ruleengineservices.rule.daos.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleTemplateModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultRuleDaoTest extends ServicelayerTransactionalTest
{
	private static final String RULE_CODE = "rule1";
	private static final String RULE_DESCRIPTION = "description1";
	private static final String DEFAULT_RULE_TYPE = "DEFAULT";

	@Resource
	private RuleDao ruleDao;

	@Resource
	private ModelService modelService;

	@Before
	public void setUp() throws ImpExException
	{
		final List<AbstractRuleModel> rules = ruleDao.findAllRules();
		rules.stream().forEach(rule -> modelService.remove(rule));

		importCsv("/ruleengineservices/test/rule/defaultRuleDaoTest.impex", "utf-8");
	}

	@Test
	public void testFindAll()
	{
		// when
		final List<AbstractRuleModel> rules = ruleDao.findAllRules();

		// then
		assertNotNull(rules);
		assertEquals(3, rules.size());
	}

	@Test
	public void testFindAllActiveRules()
	{
		// when
		final List<AbstractRuleModel> rules = ruleDao.findAllActiveRules();

		// then
		assertNotNull(rules);
		assertEquals(2, rules.size());
	}

	@Test
	public void testFindRuleByCode()
	{
		// when
		final AbstractRuleModel rule = ruleDao.findRuleByCode(RULE_CODE);

		// then
		assertNotNull(rule);
		assertEquals(RULE_DESCRIPTION, rule.getDescription());
	}

	@Test
	public void testFindAllByType()
	{
		// when
		final List<AbstractRuleModel> rules = ruleDao.findAllRulesByType(AbstractRuleModel.class);

		// then
		assertNotNull(rules);
		assertEquals(3, rules.size());
	}

	@Test
	public void testFindAllActiveRulesByType()
	{
		// when
		final List<AbstractRuleModel> rules = ruleDao.findAllActiveRulesByType(AbstractRuleModel.class);

		// then
		assertNotNull(rules);
		assertEquals(2, rules.size());
	}

	@Test
	public void testFindRuleByCodeByType()
	{
		// when
		final AbstractRuleModel rule = ruleDao.findRuleByCodeAndType(RULE_CODE, AbstractRuleModel.class);

		// then
		assertNotNull(rule);
		assertEquals(RULE_DESCRIPTION, rule.getDescription());
	}

	@Test
	public void testFindEngineRuleTypeForRuleType()
	{
		final RuleType ruleType = ruleDao.findEngineRuleTypeByRuleType(SourceRuleModel.class);
		assertEquals(DEFAULT_RULE_TYPE, ruleType.getCode());
	}

	@Test
	public void testFindEngineRuleTypeForRuleTypeNotFound()
	{
		final RuleType ruleType = ruleDao.findEngineRuleTypeByRuleType(SourceRuleTemplateModel.class);
		assertNull(ruleType);
	}
}
