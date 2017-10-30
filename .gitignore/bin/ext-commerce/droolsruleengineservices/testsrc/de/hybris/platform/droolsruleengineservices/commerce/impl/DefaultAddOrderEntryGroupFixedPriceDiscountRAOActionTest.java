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
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultAddOrderEntryGroupFixedPriceDiscountRAOActionTest extends AbstractRAOActionTest
{

	private DefaultAddOrderEntryGroupFixedPriceDiscountRAOAction action;

	@Before
	public void setUp()
	{
		action = new DefaultAddOrderEntryGroupFixedPriceDiscountRAOAction();
		action.setRuleEngineCalculationService(getRuleEngineCalculationService());
		action.setEntriesSelectionStrategies(getUnitForBundleSelectorStrategies());
		action.setNotExecutableActionStrategy(new DefaultDroolsNotExecutableActionStrategy());
		action.setConfigurationService(getConfigurationService());

		final Configuration configuration = mock(Configuration.class);
		when(Boolean.valueOf(configuration.getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true)))
				.thenReturn(Boolean.TRUE);
		when(getConfigurationService().getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void testAddOrderEntryGroupFixedPriceDiscount()
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

		final EntriesSelectionStrategyRPD s1 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 3, false,
				orderEntry1);
		final EntriesSelectionStrategyRPD s2 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry2);
		final EntriesSelectionStrategyRPD s3 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry3);
		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = Arrays.asList(s1, s2, s3);


		action.addOrderEntryGroupFixedPriceDiscount(cartRao1, entriesSelectionStrategyRPDs, new BigDecimal("50"), getResult(),
				getContext());
		// 50$ (as a promotion 50$ fixed price for 3xA, 1xB, 1xC) + 5$ = 55$
		Assert.assertEquals(new BigDecimal("55.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("65.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());
	}

	@Test
	public void testAddOrderEntryGroupFixedPriceDiscount2()
	{
		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 3);
		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "15.00", USD, 3);
		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "20.00", USD, 3);
		final OrderEntryRAO orderEntry4 = createOrderEntryRAO(cartRao1, "25.00", USD, 3);
		cartRao1.setEntries(set(orderEntry1, orderEntry2, orderEntry3, orderEntry4));
		cartRao1.setDeliveryCost(new BigDecimal("5.00"));

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("215.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("210.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

		final EntriesSelectionStrategyRPD s1 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 4, false,
				orderEntry1, orderEntry2);
		final EntriesSelectionStrategyRPD s2 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry3);
		final EntriesSelectionStrategyRPD s3 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry4);
		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = Arrays.asList(s1, s2, s3);

		action.addOrderEntryGroupFixedPriceDiscount(cartRao1, entriesSelectionStrategyRPDs, new BigDecimal("50"), getResult(),
				getContext());
		// 50$ (as a promotion 50$ fixed price for 3xA, 1xB, 1xC, 1D) + 2x15$ + 2x20$ + 2x25$ + 5$ = 175$
		Assert.assertEquals(new BigDecimal("175.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("210.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());
	}

	@Test
	public void testAddOrderEntryGroupFixedPriceNoDiscount()
	{
		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 3);
		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "15.00", USD, 3);
		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "20.00", USD, 3);
		final OrderEntryRAO orderEntry4 = createOrderEntryRAO(cartRao1, "25.00", USD, 3);
		cartRao1.setEntries(set(orderEntry1, orderEntry2, orderEntry3, orderEntry4));
		cartRao1.setDeliveryCost(new BigDecimal("5.00"));

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("215.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("210.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

		final EntriesSelectionStrategyRPD strat1 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 4, false,
				orderEntry1, orderEntry2);
		final EntriesSelectionStrategyRPD strat2 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry3);
		final EntriesSelectionStrategyRPD strat3 = createEntriesSelectionStrategyRPD(OrderEntrySelectionStrategy.CHEAPEST, 1, false,
				orderEntry4);
		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = Arrays.asList(strat1, strat2, strat3);

		action.addOrderEntryGroupFixedPriceDiscount(cartRao1, entriesSelectionStrategyRPDs, new BigDecimal("100"), getResult(),
				getContext());
		// the same price since total price of the promoted items is les than 100$
		Assert.assertEquals(new BigDecimal("215.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("210.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());
	}

	@Test
	public void testAddOrderEntryLevelDiscountWithoutSufficientQuantities()
	{
		// ensure that the action doesn't produce discountRAOs if the quantities aren't met

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());

		// create cart with 1 digital camera, 1 tripod and 3 memory cards
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 3, 0);
		orderEntry1.getProduct().setCode("872912");
		cartRao1.getEntries().add(orderEntry1);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "147.04", USD, 1, 1);
		orderEntry2.getProduct().setCode("325414");
		cartRao1.getEntries().add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "580.38", USD, 1, 2);
		orderEntry3.getProduct().setCode("454831");
		cartRao1.getEntries().add(orderEntry3);

		// Require 1 digital camera, 1 tripod and 4 memory cards
		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = new ArrayList<EntriesSelectionStrategyRPD>();

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD1 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 4, true, orderEntry1);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD1);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD2 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry2);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD2);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD3 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry3);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD3);

		// action should not trigger the creation of discounts
		final List<DiscountRAO> discountRAOs = action.addOrderEntryGroupFixedPriceDiscount(cartRao1, entriesSelectionStrategyRPDs,
				BigDecimal.valueOf(500.00), getResult(), getContext());

		Assert.assertNull(discountRAOs);

	}

}
