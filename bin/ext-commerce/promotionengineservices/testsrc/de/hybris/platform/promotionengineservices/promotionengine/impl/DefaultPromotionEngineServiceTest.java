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
package de.hybris.platform.promotionengineservices.promotionengine.impl;

import static java.lang.Long.valueOf;
import static java.util.Arrays.stream;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.promotionengineservices.dao.PromotionDao;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengine.MessageLevel;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengine.RuleEngineService;
import de.hybris.platform.ruleengine.RuleEvaluationResult;
import de.hybris.platform.ruleengine.dao.RuleEngineContextDao;
import de.hybris.platform.ruleengine.enums.RuleType;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineContextModel;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.test.RuleEngineTestSupportService;
import de.hybris.platform.ruleengineservices.converters.populator.CartModelBuilder;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultPromotionEngineServiceTest extends PromotionEngineServiceBaseTest
{

	@Resource
	private ModelService modelService;
	@Resource
	private RuleEngineService commerceRuleEngineService;
	@Resource
	private DefaultPromotionEngineService defaultPromotionEngineService;
	@Resource
	private RuleEngineContextDao ruleEngineContextDao;
	@Resource
	private MediaService mediaService;
	@Resource
	private ExtendedOrderDao extendedOrderDao;
	@Resource
	private PromotionDao promotionDao;
	@Resource
	private RuleEngineTestSupportService ruleEngineTestSupportService;

	@Before
	public void setUp() throws ImpExException, IOException
	{
		// setup with promotionsenginesetup impex and override.properties
		super.setupImpexAndOverrideEngineContext("/promotionengineservices/test/promotionenginesetup.impex");
	}

	@Test
	public void testEvaluation() throws IOException
	{

		final AbstractRuleEngineRuleModel rule1 = getRuleForFile("raoPromotion01.drl", "/promotionengineservices/test/rules/");
		final AbstractRuleEngineRuleModel rule2 = getRuleForFile("raoPromotion02.drl", "/promotionengineservices/test/rules/");
		modelService.saveAll(rule1, rule2);

		initializeRuleEngine(rule1, rule2);

		doEvaluationAndAssertion("XYZ", 20L);
		doEvaluationAndAssertion("ABC", 10L);
	}

	@Test
	public void testTransferCartToOrderUpdateRule() throws IOException
	{
		final AbstractRuleEngineRuleModel rule = getRuleForFile("orderPercentageDiscount.drl",
				"/promotionengineservices/test/rules/");
		rule.setCode("orderPercentageDiscount");
		rule.setVersion(valueOf(0L));
		ruleEngineTestSupportService.decorateRuleForTest(new HashMap<String, String>()
		{
			{
				put("addOrderDiscountRAOAction", "addOrderDiscountRAOAction");
			}
		}).accept(rule);
		modelService.save(rule);
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("promoGroup1");
		final Collection<PromotionGroupModel> groupList = new ArrayList<PromotionGroupModel>();
		groupList.add(group);
		initializeRuleEngine(rule);

		// evaluate promotions for cart:
		CartModel cart1 = buildCartForUserWithCodeAndCurrency("ABC");
		modelService.save(cart1);
		defaultPromotionEngineService.updatePromotions(groupList, cart1);
		cart1 = (CartModel) extendedOrderDao.findOrderByCode(cart1.getCode());

		final OrderModel order1 = getOrderForCart(cart1);
		defaultPromotionEngineService.transferPromotionsToOrder(cart1, order1, false);
		assertEquals(1, order1.getAllPromotionResults().size());

		// change the rule...
		rule.setRuleContent(readRuleFile("orderPercentageDiscount1.drl", "/promotionengineservices/test/rules/"));
		modelService.save(rule);
		initializeRuleEngine(rule);

		CartModel cart2 = buildCartForUserWithCodeAndCurrency("ABCD");
		cart2.setCurrency(cart1.getCurrency());
		modelService.save(cart2);
		defaultPromotionEngineService.updatePromotions(groupList, cart2);
		cart2 = (CartModel) extendedOrderDao.findOrderByCode(cart2.getCode());

		final OrderModel order2 = getOrderForCart(cart2);
		defaultPromotionEngineService.transferPromotionsToOrder(cart2, order2, false);
		Assert.assertEquals(1, order2.getAllPromotionResults().size());

		final RuleBasedPromotionModel promotion1 = getOrderPromotion(order1);
		final RuleBasedPromotionModel promotion2 = getOrderPromotion(order2);
		assertNotEquals(promotion1.getPk(), promotion2.getPk());
		assertThat(promotion1.getRuleVersion()).isEqualTo(0);
		assertThat(promotion2.getRuleVersion()).isEqualTo(1);
	}

	@Test
	public void testTransferCartWithPromotionsForEntries() throws IOException
	{
		final AbstractRuleEngineRuleModel rule = getRuleForFile("percentageDiscountCameraAccessories.drl",
				"/promotionengineservices/test/rules/");
		ruleEngineTestSupportService.decorateRuleForTest(new HashMap<String, String>()
		{
			{
				put("addOrderEntryDiscountRAOAction", "addOrderEntryDiscountRAOAction");
			}
		}).accept(rule);
		modelService.save(rule);
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("promoGroup1");
		final Collection<PromotionGroupModel> groupList = new ArrayList<PromotionGroupModel>();
		groupList.add(group);
		initializeRuleEngine(rule);

		// evaluate promotions for cart:
		CartModel cart1 = buildCartForUserWithCodeProductAndCurrency("ABC", "HW1210-3411", 3);
		modelService.save(cart1);
		defaultPromotionEngineService.updatePromotions(groupList, cart1);
		cart1 = (CartModel) extendedOrderDao.findOrderByCode(cart1.getCode());

		final OrderModel order1 = getOrderForCart(cart1);
		defaultPromotionEngineService.transferPromotionsToOrder(cart1, order1, false);
		Assert.assertEquals(1, order1.getAllPromotionResults().size());

		Assert.assertEquals(1, order1.getAllPromotionResults().iterator().next().getConsumedEntries().size());
	}

	@Test
	public void testTransferCartToOrderTheSameRule() throws IOException
	{
		final AbstractRuleEngineRuleModel rule = getRuleForFile("orderPercentageDiscount.drl",
				"/promotionengineservices/test/rules/");
		rule.setCode("orderPercentageDiscount");
		ruleEngineTestSupportService.decorateRuleForTest(new HashMap<String, String>()
		{
			{
				put("addOrderDiscountRAOAction", "addOrderDiscountRAOAction");
			}
		}).accept(rule);
		getModelService().save(rule);
		final PromotionGroupModel group = promotionDao.findPromotionGroupByCode("promoGroup1");
		final Collection<PromotionGroupModel> groupList = new ArrayList<PromotionGroupModel>();
		groupList.add(group);
		initializeRuleEngine(rule);

		// evaluate promotions for cart:
		CartModel cart1 = buildCartForUserWithCodeAndCurrency("ABC");
		getModelService().save(cart1);
		defaultPromotionEngineService.updatePromotions(groupList, cart1);
		cart1 = (CartModel) extendedOrderDao.findOrderByCode(cart1.getCode());

		final OrderModel order1 = getOrderForCart(cart1);
		defaultPromotionEngineService.transferPromotionsToOrder(cart1, order1, false);
		Assert.assertEquals(1, order1.getAllPromotionResults().size());

		// not change the rule...

		CartModel cart2 = buildCartForUserWithCodeAndCurrency("ABCD");
		cart2.setCurrency(cart1.getCurrency());
		getModelService().save(cart2);
		defaultPromotionEngineService.updatePromotions(groupList, cart2);
		cart2 = (CartModel) extendedOrderDao.findOrderByCode(cart2.getCode());

		final OrderModel order2 = getOrderForCart(cart2);
		defaultPromotionEngineService.transferPromotionsToOrder(cart2, order2, false);
		Assert.assertEquals(1, order2.getAllPromotionResults().size());


		final RuleBasedPromotionModel promotion1 = getOrderPromotion(order1);
		final RuleBasedPromotionModel promotion2 = getOrderPromotion(order2);
		Assert.assertEquals(promotion1.getPk(), promotion2.getPk());
	}

	@Test
	public void testMultiplePromotionGroups() throws IOException
	{
		final AbstractRuleEngineRuleModel rule1 = getRuleForFile("raoPromotion03.drl", "/promotionengineservices/test/rules/");
		rule1.setCode("raoPromotion03.drl");
		final AbstractRuleEngineRuleModel rule2 = getRuleForFile("raoPromotion04.drl", "/promotionengineservices/test/rules/");
		rule2.setCode("raoPromotion04.drl");
		getModelService().saveAll(rule1, rule2);
		final PromotionGroupModel group1 = promotionDao.findPromotionGroupByCode("promoGroup1");
		final PromotionGroupModel group2 = promotionDao.findPromotionGroupByCode("promoGroup2");
		initializeRuleEngine(rule1, rule2);
		final Collection<PromotionGroupModel> groupList = new ArrayList<PromotionGroupModel>();
		groupList.add(group1);

		// evaluate promotions for cart with group 1
		//should be result for group1 only
		final CartModel cart1 = buildCartForUserWithCodeAndCurrency("123456");
		getModelService().save(cart1);
		final RuleEvaluationResult result = defaultPromotionEngineService.evaluate(cart1, groupList);
		Assert.assertEquals(1, result.getResult().getActions().size());
		final List<AbstractRuleActionRAO> resultList1 = new ArrayList(result.getResult().getActions());
		Assert.assertTrue(resultList1.get(0).getFiredRuleCode().equals("raoPromotion03.drl"));

		groupList.add(group2);
		// evaluate promotions for cart with both groups
		//should be result for group1 and group2
		getModelService().save(cart1);
		final RuleEvaluationResult result2 = defaultPromotionEngineService.evaluate(cart1, groupList);
		Assert.assertEquals(2, result2.getResult().getActions().size());
		final List<AbstractRuleActionRAO> resultList2 = new ArrayList(result2.getResult().getActions());
		Assert.assertTrue(resultList2.get(0).getFiredRuleCode().equals("raoPromotion03.drl")
				|| resultList2.get(1).getFiredRuleCode().equals("raoPromotion03.drl"));
		Assert.assertTrue(resultList2.get(0).getFiredRuleCode().equals("raoPromotion04.drl")
				|| resultList2.get(1).getFiredRuleCode().equals("raoPromotion04.drl"));

		groupList.remove(group1);
		// evaluate promotions for cart with group 2 only
		//should be result for group2 only
		getModelService().save(cart1);
		final RuleEvaluationResult result3 = defaultPromotionEngineService.evaluate(cart1, groupList);
		Assert.assertEquals(1, result3.getResult().getActions().size());
		final List<AbstractRuleActionRAO> resultList3 = new ArrayList(result3.getResult().getActions());
		Assert.assertTrue(resultList3.get(0).getFiredRuleCode().equals("raoPromotion04.drl"));
	}

	private RuleBasedPromotionModel getOrderPromotion(final OrderModel order)
	{
		return (RuleBasedPromotionModel) order.getAllPromotionResults().iterator().next().getPromotion();

	}

	private OrderModel getOrderForCart(final CartModel cart)
	{
		final OrderModel order = new OrderModel();
		order.setCode(UUID.randomUUID().toString());
		final ArrayList<AbstractOrderEntryModel> orderEntries = new ArrayList<AbstractOrderEntryModel>();
		for (final AbstractOrderEntryModel cartEntry : cart.getEntries())
		{
			final OrderEntryModel orderEntry = new OrderEntryModel();
			orderEntry.setEntryNumber(cartEntry.getEntryNumber());
			orderEntry.setOrder(order);
			orderEntry.setProduct(cartEntry.getProduct());
			orderEntry.setQuantity(cartEntry.getQuantity());
			orderEntry.setUnit(cartEntry.getUnit());
			orderEntries.add(orderEntry);
		}
		order.setEntries(orderEntries);
		order.setUser(cart.getUser());
		order.setDate(new Date());
		order.setCurrency(cart.getCurrency());
		return order;
	}

	/**
	 * @param cartCode
	 * @param expectedDiscount
	 */
	private void doEvaluationAndAssertion(final String cartCode, final long expectedDiscount)
	{
		final CartModel cart = buildCartWithCodeAndCurrency(cartCode);

		final RuleEvaluationResult result = defaultPromotionEngineService.evaluate(cart, getPromoGroup("promoGroup1"));
		final RuleEngineResultRAO resultRAO = result.getResult();
		Assert.assertNotNull(resultRAO);
		Assert.assertEquals("should have one action", 1, resultRAO.getActions().size());
		final AbstractRuleActionRAO resultAction = resultRAO.getActions().iterator().next();
		Assert.assertTrue("should be DiscountRAO", resultAction instanceof DiscountRAO);
		final DiscountRAO discount = (DiscountRAO) resultAction;
		Assert.assertEquals(BigDecimal.valueOf(expectedDiscount), discount.getValue());
	}

	/**
	 * Creates a (non-persisted) AbstractRuleEngineRuleModel based on the given file and path. Note that this
	 * implementation assumes that the fileName matches the rule's name (excluding the .drl extension).
	 *
	 * @param fileName
	 * @param path
	 * @return new AbstractRuleEngineRuleModel
	 * @throws IOException
	 */
	protected AbstractRuleEngineRuleModel getRuleForFile(final String fileName, final String path) throws IOException
	{
		final AbstractRuleEngineRuleModel rule = ruleEngineTestSupportService.createRuleModel();
		rule.setCode(fileName);
		rule.setUuid(fileName.substring(0, fileName.length() - 4));
		rule.setActive(Boolean.TRUE);
		rule.setRuleContent(readRuleFile(fileName, path));
		rule.setRuleType(RuleType.PROMOTION);
		return rule;
	}

	protected CartModel buildCartForUserWithCodeProductAndCurrency(final String code, final String productCode, final int items)
	{
		final CartModel cart = productCode == null ? buildCartWithCodeAndCurrency(code)
				: buildCartWithCodeProductAndCurrency(code, productCode, items);
		final UserModel user = new UserModel();
		user.setUid(UUID.randomUUID().toString());
		cart.setUser(user);
		cart.setDate(new Date());
		return cart;
	}

	protected CartModel buildCartForUserWithCodeAndCurrency(final String code)
	{
		return buildCartForUserWithCodeProductAndCurrency(code, null, 0);
	}

	protected CartModel buildCartWithCodeAndCurrency(final String code)
	{

		return CartModelBuilder.newCart(code).setCurrency("USD").getModel();
	}

	protected CartModel buildCartWithCodeProductAndCurrency(final String code, final String productCode, final int items)
	{

		return CartModelBuilder.newCart(code).setCurrency("USD").addProduct(productCode, 1, 1.0, items).getModel();
	}


	protected void initializeRuleEngine(final AbstractRuleEngineRuleModel... rules)
	{
		final AbstractRuleEngineContextModel abstractContext = getRuleEngineContextDao()
				.getRuleEngineContextByName("promotions-junit-context");
		final RuleEngineActionResult result = getCommerceRuleEngineService().initialize(
				ruleEngineTestSupportService.getTestRulesModule(abstractContext, stream(rules).collect(Collectors.toSet())));
		if (result.isActionFailed())
		{
			Assert.fail("rule engine initialization failed with errors: " + result.getMessagesAsString(MessageLevel.ERROR));
		}
	}

	protected List<PromotionGroupModel> getPromoGroup(final String code)
	{
		final PromotionGroupModel defaultPromotionGroup = promotionDao.findPromotionGroupByCode(code);
		final List<PromotionGroupModel> promoGroups = new ArrayList<PromotionGroupModel>();
		promoGroups.add(defaultPromotionGroup);
		return promoGroups;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected RuleEngineService getCommerceRuleEngineService()
	{
		return commerceRuleEngineService;
	}

	protected RuleEngineContextDao getRuleEngineContextDao()
	{
		return ruleEngineContextDao;
	}

	protected MediaService getMediaService()
	{
		return mediaService;
	}

}
