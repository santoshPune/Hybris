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
package de.hybris.platform.droolsruleengineservices.commerce.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class DefaultAddProductToCartRAOActionTest extends AbstractRAOActionTest
{
	private DefaultAddProductToCartRAOAction action;

	@Mock
	private ProductModel product;

	protected static final String PRODUCT_CODE = "productCode";

	@Mock
	private ProductService productService;

	@Before
	public void setUp()
	{
		final DefaultAddOrderEntryDiscountRAOAction innerAction = new DefaultAddOrderEntryDiscountRAOAction();
		innerAction.setRuleEngineCalculationService(getRuleEngineCalculationService());
		innerAction.setEntriesSelectionStrategies(getUnitForBundleSelectorStrategies());
		innerAction.setConfigurationService(getConfigurationService());

		action = new DefaultAddProductToCartRAOAction();
		action.setRuleEngineCalculationService(getRuleEngineCalculationService());
		action.setEntriesSelectionStrategies(getUnitForBundleSelectorStrategies());
		action.setAddOrderEntryDiscountAction(innerAction);
		action.setProductService(productService);
		action.setConfigurationService(getConfigurationService());

		when(productService.getProductForCode(PRODUCT_CODE)).thenReturn(product);
		when(product.getSupercategories()).thenReturn(Collections.emptyList());
		when(product.getCode()).thenReturn(PRODUCT_CODE);

		final Configuration configuration = mock(Configuration.class);
		when(Boolean.valueOf(configuration.getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true)))
				.thenReturn(Boolean.TRUE);
		when(getConfigurationService().getConfiguration()).thenReturn(configuration);

	}

	@Test
	public void testAddFreeProductsToCart()
	{
		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 3);
		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "15.00", USD, 1);
		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "20.00", USD, 1);
		cartRao1.setEntries(set(orderEntry1, orderEntry2, orderEntry3));
		cartRao1.setDeliveryCost(new BigDecimal("5.00"));

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("70.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("65.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

		final EntriesSelectionStrategyRPD strat1 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 3, false,
				orderEntry1);
		final EntriesSelectionStrategyRPD strat2 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry2);
		final EntriesSelectionStrategyRPD strat3 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry3);
		final List<EntriesSelectionStrategyRPD> strategies = Arrays.asList(strat1, strat2, strat3);

		final FreeProductRAO freeProduct = action.addFreeProductsToCart(cartRao1, PRODUCT_CODE, 1, strategies, getResult(),
				getContext());
		final Set<OrderEntryRAO> entries = cartRao1.getEntries();
		OrderEntryRAO addedOrderEntry = null;
		for (final OrderEntryRAO entry : entries)
		{
			if (PRODUCT_CODE.equals(entry.getProduct().getCode()))
			{
				addedOrderEntry = entry;
				final LinkedHashSet<AbstractRuleActionRAO> actions = entry.getActions();
				Assert.assertEquals("expected one action, but was:" + actions.size(), 1, actions.size());
				Assert.assertTrue(entry.getActions().iterator().next() instanceof DiscountRAO);
				final DiscountRAO discount = (DiscountRAO) entry.getActions().iterator().next();
				Assert.assertTrue(discount.getCurrencyIsoCode() == null);
				Assert.assertEquals(new BigDecimal("100.00"), discount.getValue());
				break;
			}
		}

		Assert.assertTrue("expected free product added to cart, but wasnt", addedOrderEntry != null);
		Assert.assertTrue("expected an addedOrderEntry at freeProducrRAO", freeProduct.getAddedOrderEntry() != null);
		Assert.assertEquals("addedOrderEntry should be on freeProductRAO", addedOrderEntry, freeProduct.getAddedOrderEntry());
		Assert.assertEquals("appliedToObject should be the cart", cartRao1, freeProduct.getAppliedToObject());
		Assert.assertEquals(new BigDecimal("70.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("65.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

	}
}
