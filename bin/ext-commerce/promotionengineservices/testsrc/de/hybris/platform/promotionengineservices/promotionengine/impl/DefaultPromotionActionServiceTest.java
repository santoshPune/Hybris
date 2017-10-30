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
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedPromotionModel;
import de.hybris.platform.promotionengineservices.order.dao.ExtendedOrderDao;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengine.versioning.ModuleVersioningService;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultPromotionActionServiceTest
{

	private static final String CART_CODE = "cartCode";
	private static final String RULE_CODE = "ruleCode";

	@InjectMocks
	private DefaultPromotionActionService defaultPromotionActionService;

	@Mock
	private ExtendedOrderDao extendedOrderDao;
	@Mock
	private ModelService modelService;
	@Mock
	private EngineRuleDao engineRuleDao;
	@Mock
	private AbstractOrderModel cart;
	@Mock
	private CartRAO cartRAO;
	@Mock
	private AbstractRuleEngineRuleModel rule;
	@Mock
	private RuleBasedPromotionModel promotion;
	@Mock
	private DiscountRAO discountRAO;
	@Mock
	private AbstractRuleBasedPromotionActionModel action1;
	@Mock
	private AbstractRuleActionRAO actionAppliedToCart;
	@Mock
	private OrderEntryRAO orderEntryRaoWithInvalidOrder;
	@Mock
	private OrderEntryRAO orderEntryRaoWithInvalidOrder2;
	@Mock
	private AbstractOrderRAO order;
	@Mock
	private AbstractOrderRAO invalidOrder;
	@Mock
	private ProductRAO product;
	@Mock
	private AbstractOrderEntryModel orderEntry;
	@Mock
	private ModuleVersioningService moduleVersioningService;


	@Before
	public void setUp()
	{
		when(extendedOrderDao.findOrderByCode(CART_CODE)).thenReturn(cart);
		when(engineRuleDao.getRuleByCode(RULE_CODE)).thenReturn(rule);
		when(discountRAO.getAppliedToObject()).thenReturn(cartRAO);
		when(discountRAO.getFiredRuleCode()).thenReturn(RULE_CODE);
		when(cartRAO.getCode()).thenReturn(CART_CODE);
		when(rule.getPromotion()).thenReturn(promotion);
		when(modelService.create(PromotionResultModel.class)).thenReturn(new PromotionResultModel());
		when(actionAppliedToCart.getAppliedToObject()).thenReturn(new CartRAO());

		when(invalidOrder.getCode()).thenReturn("unknownCartCode");
		when(product.getCode()).thenReturn("productCode");
		when(orderEntryRaoWithInvalidOrder.getOrder()).thenReturn(invalidOrder);
		when(orderEntryRaoWithInvalidOrder.getProduct()).thenReturn(product);

		when(order.getCode()).thenReturn(CART_CODE);
		when(product.getCode()).thenReturn("productCode");
		when(orderEntryRaoWithInvalidOrder2.getOrder()).thenReturn(order);
		when(orderEntryRaoWithInvalidOrder2.getEntryNumber()).thenReturn(Integer.valueOf(1));
		when(orderEntryRaoWithInvalidOrder2.getProduct()).thenReturn(product);

		when(cart.getEntries()).thenReturn(Arrays.asList(orderEntry));
		when(orderEntry.getEntryNumber()).thenReturn(Integer.valueOf(2));
	}

	/**
	 * tests that a promotionResultModel gets created with the default values
	 */
	@Test
	public void testCreatePromotionResult()
	{
		when(cart.getAllPromotionResults()).thenReturn(Collections.emptySet());
		final PromotionResultModel newPromotionResult = defaultPromotionActionService.createPromotionResult(discountRAO);
		assertNotNull(newPromotionResult);
		assertEquals(Float.valueOf(1.0F), newPromotionResult.getCertainty());
		assertEquals(promotion, newPromotionResult.getPromotion());
		assertEquals(cart, newPromotionResult.getOrder());
	}

	/**
	 * tests that if there is an existing promotionResultModel created by the same rule, the existing one is returned
	 */
	@Test
	public void testCreatePromotionResultForExistingPromotionResult()
	{
		final PK rulePk = PK.createUUIDPK(10);
		final Collection<AbstractPromotionActionModel> actions = Collections.singleton(action1);
		when(rule.getPk()).thenReturn(rulePk);
		final PromotionResultModel existingPromotionResult = new PromotionResultModel();
		existingPromotionResult.setActions(actions);
		final Set<PromotionResultModel> allPromotionResults = Collections.singleton(existingPromotionResult);
		when(cart.getAllPromotionResults()).thenReturn(allPromotionResults);
		when(action1.getRule()).thenReturn(rule);

		final PromotionResultModel newPromotionResult = defaultPromotionActionService.createPromotionResult(discountRAO);
		assertNotNull(newPromotionResult);
		assertEquals(Float.valueOf(1.0F), newPromotionResult.getCertainty());
		assertEquals(promotion, newPromotionResult.getPromotion());
		assertEquals(cart, newPromotionResult.getOrder());
		assertEquals(newPromotionResult, existingPromotionResult);
	}

	@Test
	public void testTryToGetOrderEntryFromImproperAction()
	{
		assertNull(defaultPromotionActionService.getOrderEntry(actionAppliedToCart));
		assertNull(defaultPromotionActionService.getOrderEntry(orderEntryRaoWithInvalidOrder));
		assertNull(defaultPromotionActionService.getOrderEntry(orderEntryRaoWithInvalidOrder2));
	}

	@Test
	public void testTryToGetOrderFromAction()
	{
		assertNotNull(defaultPromotionActionService.getOrder(discountRAO));
		assertNull(defaultPromotionActionService.getOrder(actionAppliedToCart));
	}

	@Test
	public void testCreatePromotionResultSetVersion()
	{
		final PromotionResultModel newPromotionResult = defaultPromotionActionService.createPromotionResult(discountRAO);
		assertThat(newPromotionResult.getRuleVersion()).isNotNull().isEqualTo(0);
	}

	@Test
	public void testCreatePromotionResultSetModuleVersion()
	{
		when(moduleVersioningService.getModuleVersion(rule)).thenReturn(valueOf(1));

		final PromotionResultModel newPromotionResult = defaultPromotionActionService.createPromotionResult(discountRAO);
		assertThat(newPromotionResult.getRuleVersion()).isNotNull().isEqualTo(0);
		assertThat(newPromotionResult.getModuleVersion()).isNotNull().isEqualTo(1);
	}
}
