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
import java.util.HashSet;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@UnitTest
public class DefaultAddFreeProductDiscountActionTest extends AbstractRAOActionTest
{
	private DefaultAddFreeProductDiscountAction action;


	@Before
	public void setUp()
	{
		action = new DefaultAddFreeProductDiscountAction();
		action.setRuleEngineCalculationService(getRuleEngineCalculationService());
		action.setEntriesSelectionStrategies(getUnitForBundleSelectorStrategies());
		action.setConfigurationService(getConfigurationService());

		final Configuration configuration = mock(Configuration.class);
		when(Boolean.valueOf(configuration.getBoolean("droolsruleengineservices.validate.droolsrule.rulecode", true)))
				.thenReturn(Boolean.TRUE);
		when(getConfigurationService().getConfiguration()).thenReturn(configuration);
	}

	@Test
	public void testAddFreeProductDiscount()
	{
		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		cartRao1.setEntries(new HashSet<OrderEntryRAO>());
		final List<OrderEntryRAO> entryList = new ArrayList();
		final EntriesSelectionStrategyRPD entriesSelectionStrategyRPD = new EntriesSelectionStrategyRPD();

		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "12.00", USD, 3, 0);
		orderEntry1.getProduct().setCode("67890");
		cartRao1.getEntries().add(orderEntry1);
		entryList.add(orderEntry1);

		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "10.00", USD, 2, 1);
		orderEntry2.getProduct().setCode("67891");
		cartRao1.getEntries().add(orderEntry2);
		entryList.add(orderEntry2);

		final OrderEntryRAO orderEntry3 = createOrderEntryRAO(cartRao1, "6.50", USD, 1, 2);
		orderEntry3.getProduct().setCode("67892");
		cartRao1.getEntries().add(orderEntry3);
		entryList.add(orderEntry3);

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("62.50"), cartRao1.getTotal());

		entriesSelectionStrategyRPD.setOrderEntries(entryList);
		entriesSelectionStrategyRPD.setQuantity(2);
		entriesSelectionStrategyRPD.setSelectionStrategy(OrderEntrySelectionStrategy.CHEAPEST);

		//a buy-3-get-1-free discount with 6 products, giving a free quantity of 2
		final List<DiscountRAO> list = action.addDiscount(entriesSelectionStrategyRPD, 2, 3, getResult(), getContext());

		getRuleEngineCalculationService().calculateTotals(cartRao1);

		Assert.assertEquals(2, list.size());
		Assert.assertEquals(new BigDecimal("6.50"), list.get(1).getValue());

		// Discount for orderEntry2 is 10$
		Assert.assertEquals(new BigDecimal("10.00"), list.get(0).getValue());

		Assert.assertEquals(new BigDecimal("46.00"), cartRao1.getTotal());
	}

}
