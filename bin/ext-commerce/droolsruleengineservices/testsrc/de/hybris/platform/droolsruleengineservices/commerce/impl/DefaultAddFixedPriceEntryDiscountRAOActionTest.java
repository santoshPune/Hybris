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

import static java.util.Collections.singletonList;
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
import java.util.HashSet;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultAddFixedPriceEntryDiscountRAOActionTest extends AbstractRAOActionTest
{

	private DefaultAddFixedPriceEntryDiscountRAOAction action;


	@Before
	public void setUp()
	{

		action = new DefaultAddFixedPriceEntryDiscountRAOAction();
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
	public void testAddFixedPriceEntriesDiscount()
	{
		//Order threshold perfect partner fixed price promotion. Get 1 of the products 67890 or 67891 for fixed price of $50

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());
		final List<OrderEntryRAO> entryList = new ArrayList();
		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD = new EntriesSelectionStrategyRPD();

		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "100.00", USD, 2, 0);
		orderEntry1.getProduct().setCode("67890");
		cartRao1.getEntries().add(orderEntry1);
		entryList.add(orderEntry1);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "150.00", USD, 2, 1);
		orderEntry2.getProduct().setCode("67891");
		cartRao1.getEntries().add(orderEntry2);
		entryList.add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "10.00", USD, 1, 2);
		orderEntry3.getProduct().setCode("67892");
		cartRao1.getEntries().add(orderEntry3);

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("510.00"), cartRao1.getTotal());

		entriesSelectionStrategyRPD.setOrderEntries(entryList);
		entriesSelectionStrategyRPD.setQuantity(1);
		entriesSelectionStrategyRPD.setSelectionStrategy(OrderEntrySelectionStrategy.CHEAPEST);
		entriesSelectionStrategyRPD.setTargetOfAction(true);

		final List<DiscountRAO> discountRAOs = action.addFixedPriceEntriesDiscount(singletonList(entriesSelectionStrategyRPD),
				BigDecimal.valueOf(50.00), 1, getResult(), getContext());

		getRuleEngineCalculationService().calculateTotals(cartRao1);

		Assert.assertEquals(1, discountRAOs.size());
		Assert.assertEquals(new BigDecimal("50.00"), discountRAOs.get(0).getValue());
		Assert.assertEquals(1, discountRAOs.get(0).getAppliedToQuantity());
		Assert.assertEquals(new BigDecimal("460.00"), cartRao1.getTotal());
	}

	@Test
	public void testAddFixedPriceEntriesDiscountWithEntriesSelectionStrategies()
	{
		//Order threshold perfect partner fixed price promotion. Get 1 digital camera, 1 tripod and 2 memory cards and get
		//2 memory cards for fixed price of 3USD per unit

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());


		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 2, 0);
		orderEntry1.getProduct().setCode("872912");
		cartRao1.getEntries().add(orderEntry1);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "147.04", USD, 1, 1);
		orderEntry2.getProduct().setCode("325414");
		cartRao1.getEntries().add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "925.82", USD, 1, 2);
		orderEntry3.getProduct().setCode("454831");
		cartRao1.getEntries().add(orderEntry3);

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("1092.86"), cartRao1.getTotal());

		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = new ArrayList<EntriesSelectionStrategyRPD>();

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD1 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 2, true, orderEntry1);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD1);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD2 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry2);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD2);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD3 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry3);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD3);

		final List<DiscountRAO> discountRAOs = action.addFixedPriceEntriesDiscount(entriesSelectionStrategyRPDs,
				BigDecimal.valueOf(3.00), -1, getResult(), getContext());

		getRuleEngineCalculationService().calculateTotals(cartRao1);

		Assert.assertEquals(1, discountRAOs.size());
		Assert.assertEquals(new BigDecimal("7.00"), discountRAOs.get(0).getValue());
		Assert.assertEquals(2, discountRAOs.get(0).getAppliedToQuantity());
		Assert.assertEquals(new BigDecimal("1078.86"), cartRao1.getTotal());
	}

	@Test
	public void testAddFixedPriceEntriesDiscountWithEntriesSelectionStrategies1()
	{
		//Order threshold perfect partner fixed price promotion. Get 1 digital camera,

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());


		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "10.00", USD, 1, 0);
		orderEntry1.getProduct().setCode("872912");
		cartRao1.getEntries().add(orderEntry1);

		final OrderEntryRAO orderEntry11 = createOrderEntryRAO(cartRao1, "5.00", USD, 1, 3);
		orderEntry11.getProduct().setCode("11111");
		cartRao1.getEntries().add(orderEntry11);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "50.00", USD, 1, 1);
		orderEntry2.getProduct().setCode("325414");
		cartRao1.getEntries().add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "40.00", USD, 1, 2);
		orderEntry3.getProduct().setCode("454831");
		cartRao1.getEntries().add(orderEntry3);

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("105.00"), cartRao1.getTotal());

		final List<EntriesSelectionStrategyRPD> entriesSelectionStrategyRPDs = new ArrayList<EntriesSelectionStrategyRPD>();

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD1 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 2, true, orderEntry1, orderEntry11);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD1);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD2 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry2);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD2);

		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD3 = createEntriesSelectionStrategyRPD(
				OrderEntrySelectionStrategy.CHEAPEST, 1, false, orderEntry3);
		entriesSelectionStrategyRPDs.add(entriesSelectionStrategyRPD3);

		final List<DiscountRAO> discountRAOs = action.addFixedPriceEntriesDiscount(entriesSelectionStrategyRPDs,
				BigDecimal.valueOf(3.00), -1, getResult(), getContext());

		getRuleEngineCalculationService().calculateTotals(cartRao1);

		Assert.assertEquals(2, discountRAOs.size());

		for (final DiscountRAO discount : discountRAOs)
		{
			final OrderEntryRAO orderEntry = (OrderEntryRAO) discount.getAppliedToObject();

			if (orderEntry.getProduct().getCode().equals("872912"))
			{
				Assert.assertEquals(new BigDecimal("7.00"), discount.getValue());

			}
			else
			{
				Assert.assertEquals(new BigDecimal("2.00"), discount.getValue());
			}
			Assert.assertEquals(1, discount.getAppliedToQuantity());
		}

		Assert.assertEquals(new BigDecimal("96.00"), cartRao1.getTotal());
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
		final List<DiscountRAO> discountRAOs = action.addFixedPriceEntriesDiscount(entriesSelectionStrategyRPDs,
				BigDecimal.valueOf(50.00), -1, getResult(), getContext());

		Assert.assertNull(discountRAOs);
	}

}
