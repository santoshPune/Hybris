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
package de.hybris.platform.promotionengineservices.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.model.CatForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedCatForRuleModel;
import de.hybris.platform.promotionengineservices.model.ExcludedProductForRuleModel;
import de.hybris.platform.promotionengineservices.model.ProductForPromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.rule.dao.RuleDao;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultPromotionSourceRuleDaoIT extends ServicelayerTransactionalTest
{
	@Resource
	private DefaultPromotionSourceRuleDao promotionSourceRuleDao;

	@Resource
	private RuleDao ruleDao;

	@Resource
	private PromotionDao promotionDao;

	@Before
	public void setUp() throws ImpExException
	{
		importCsv("/promotionengineservices/test/defaultPromotionSourceRuleDaoTest.impex", "utf-8");
	}

	@Test
	public void testFindAllProductForPromotionSourceRuleNoProducts()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule1");

		final List<ProductForPromotionSourceRuleModel> productsForRule = promotionSourceRuleDao
				.findAllProductForPromotionSourceRule(rule);

		assertNotNull(productsForRule);
		assertEquals(0, productsForRule.size());
	}

	@Test
	public void testFindAllProductForPromotionSourceRule()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule2");

		final List<ProductForPromotionSourceRuleModel> productsForRule = promotionSourceRuleDao
				.findAllProductForPromotionSourceRule(rule);

		assertNotNull(productsForRule);
		assertEquals(2, productsForRule.size());
	}

	@Test
	public void testFindAllCatForPromotionSourceRuleNoCategories()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule1");

		final List<CatForPromotionSourceRuleModel> categoriesForRule = promotionSourceRuleDao
				.findAllCatForPromotionSourceRule(rule);

		assertNotNull(categoriesForRule);
		assertEquals(0, categoriesForRule.size());
	}

	@Test
	public void testFindAllCatForPromotionSourceRule()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule2");

		final List<CatForPromotionSourceRuleModel> categoriesForRule = promotionSourceRuleDao
				.findAllCatForPromotionSourceRule(rule);

		assertNotNull(categoriesForRule);
		assertEquals(2, categoriesForRule.size());
	}

	@Test
	public void testFindPromotionSourceRules()
	{
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final Set<String> categoryCodes = new HashSet<String>();
		categoryCodes.add("576");
		categoryCodes.add("brand_5");

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "111111", categoryCodes);

		assertNotNull(promotions);
		assertEquals(2, promotions.size());
	}

	@Test
	public void testFindPromotionSourceRulesOnlyCat()
	{
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final Set<String> categoryCodes = new HashSet<String>();
		categoryCodes.add("brand_5");

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "444444", categoryCodes);

		assertNotNull(promotions);
		assertEquals(1, promotions.size());
	}

	@Test
	public void testFindPromotionSourceRulesNotFound()
	{
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final Set<String> categoryCodes = new HashSet<String>();
		categoryCodes.add("555");

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "555555", categoryCodes);

		assertNotNull(promotions);
		assertEquals(0, promotions.size());
	}

	@Test
	public void testFindPromotionSourceRulesOnlyProd()
	{
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "111111", null);

		assertNotNull(promotions);
		assertEquals(2, promotions.size());
	}

	@Test
	public void testFindPromotionSourceRulesCombinedCats()
	{
		//prod10_1 cat10_1,cat10_2 -> rule10 (promotion10)
		//prod10_2 cat10_3,cat10_4 -> rule10 (promotion10)
		//exclProd10_1 cat10_1,cat10_2 -> no
		//exclProd10_2 cat10_3,cat10_4 -> no

		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final Set<String> categoryCodes = new HashSet<String>();
		categoryCodes.add("cat10_1");
		categoryCodes.add("cat10_2");

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "prod10_1", categoryCodes);

		assertNotNull(promotions);
		assertEquals(1, promotions.size());
		assertEquals("promotion10", promotions.get(0).getCode());
	}

	@Test
	public void testFindPromotionSourceRulesCombinedCatsWithExcludedProduct()
	{
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final Set<String> categoryCodes = new HashSet<String>();
		categoryCodes.add("cat10_1");
		categoryCodes.add("cat10_2");

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "exclProd10_1",
				categoryCodes);

		assertNotNull(promotions);
		assertEquals(0, promotions.size());
	}

	@Test
	public void testFindPromotionSourceRulesCatsWithExcludedProduct()
	{
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("website1");
		final Collection groups = new ArrayList();
		groups.add(group);

		final Set<String> categoryCodes = new HashSet<String>();
		categoryCodes.add("cat11_1");

		final List<RuleBasedPromotionModel> promotions = promotionSourceRuleDao.findPromotions(groups, "exclProd11_1",
				categoryCodes);

		assertNotNull(promotions);
		assertEquals(0, promotions.size());
	}

	@Test
	public void testFindLastConditionIdForRuleAlreadyExists()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule6");
		final Integer lastConditionId = promotionSourceRuleDao.findLastConditionIdForRule(rule);

		assertEquals(2, lastConditionId.intValue());
	}

	@Test
	public void testFindLastConditionIdForRuleNotExists()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule7");
		final Integer lastConditionId = promotionSourceRuleDao.findLastConditionIdForRule(rule);

		assertNull(lastConditionId);
	}

	@Test
	public void testFindAllExcludedCatForPromotionSourceRule()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule8");
		final List<ExcludedCatForRuleModel> excludedCategories = promotionSourceRuleDao
				.findAllExcludedCatForPromotionSourceRule(rule);

		assertEquals(2, excludedCategories.size());

		final List<ExcludedCatForRuleModel> excludedCategoriesCat1 = excludedCategories.stream()
				.filter(ec -> ec.getCategoryCode().equals("cat1")).collect(Collectors.toList());
		assertEquals(1, excludedCategoriesCat1.size());

		final List<ExcludedCatForRuleModel> excludedCategoriesCat2 = excludedCategories.stream()
				.filter(ec -> ec.getCategoryCode().equals("cat2")).collect(Collectors.toList());
		assertEquals(1, excludedCategoriesCat2.size());
	}

	@Test
	public void testFindAllExcludedCatForPromotionSourceRuleNoExculded()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule9");
		final List<ExcludedCatForRuleModel> excludedCategories = promotionSourceRuleDao
				.findAllExcludedCatForPromotionSourceRule(rule);

		assertEquals(0, excludedCategories.size());
	}

	@Test
	public void testFindAllExcludedProductForPromotionSourceRule()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule8");
		final List<ExcludedProductForRuleModel> excludedProducts = promotionSourceRuleDao
				.findAllExcludedProductForPromotionSourceRule(rule);

		assertEquals(2, excludedProducts.size());

		final List<ExcludedProductForRuleModel> excludedCategoriesCat1 = excludedProducts.stream()
				.filter(ec -> ec.getProductCode().equals("prod1")).collect(Collectors.toList());
		assertEquals(1, excludedCategoriesCat1.size());

		final List<ExcludedProductForRuleModel> excludedCategoriesCat2 = excludedProducts.stream()
				.filter(ec -> ec.getProductCode().equals("prod2")).collect(Collectors.toList());
		assertEquals(1, excludedCategoriesCat2.size());
	}

	@Test
	public void testFindAllExcludedProductForPromotionSourceRuleNoExculded()
	{
		final PromotionSourceRuleModel rule = (PromotionSourceRuleModel) ruleDao.findRuleByCode("rule9");
		final List<ExcludedProductForRuleModel> excludedProducts = promotionSourceRuleDao
				.findAllExcludedProductForPromotionSourceRule(rule);

		assertEquals(0, excludedProducts.size());
	}
}
