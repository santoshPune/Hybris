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
package de.hybris.platform.ruleengineservices.calculation.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.ruleengineservices.calculation.AbstractRuleEngineTest;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


/**
 * Contains unit tests for the DefaultRuleEngineCalculationService.
 */
@UnitTest
public class DefaultRuleEngineCalculationServiceTest extends AbstractRuleEngineTest
{
	@Test
	public void testSimpleCalculateTotals()
	{

		// simple cart with 1 entry of quantity 2
		final CartRAO simple01 = createCartRAO("simple01", USD);
		simple01.setEntries(Collections.singleton(createOrderEntryRAO(simple01, "12.34", USD, 2, 0)));
		getRuleEngineCalculationService().calculateTotals(simple01);
		Assert.assertEquals(new BigDecimal("24.68"), simple01.getTotal());
		Assert.assertEquals(new BigDecimal("24.68"), simple01.getSubTotal());

		// simple cart with 2 entries and delivery cost
		final CartRAO simple02 = createCartRAO("simple02", USD);
		simple02.setEntries(set(createOrderEntryRAO(simple02, "12.34", USD, 2, 0),
				createOrderEntryRAO(simple02, "23.45", USD, 3, 1)));
		simple02.setDeliveryCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple02);
		Assert.assertEquals(new BigDecimal("100.03"), simple02.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple02.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), simple02.getDeliveryCost());
	}

	@Test
	public void testChangeDeliveryMode()
	{
		// simple cart with 2 entries and payment cost
		final CartRAO simple03 = createCartRAO("simple03", USD);
		simple03.setEntries(set(createOrderEntryRAO(simple03, "12.34", USD, 2, 0),
				createOrderEntryRAO(simple03, "23.45", USD, 3, 1)));
		simple03.setPaymentCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple03);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), simple03.getPaymentCost());

		// now assume there is already a delivery cost
		simple03.setDeliveryCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple03);
		Assert.assertEquals(new BigDecimal("105.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());

		// now add a free shipping discount
		final DeliveryModeRAO freeDeliveryMode = new DeliveryModeRAO();
		freeDeliveryMode.setCode("FREE SHIPPING");
		freeDeliveryMode.setCost(new BigDecimal("0.00"));
		getRuleEngineCalculationService().changeDeliveryMode(simple03, freeDeliveryMode);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("0.00"), simple03.getDeliveryCost());

		// now just recalculate to make sure nothing changes
		getRuleEngineCalculationService().calculateTotals(simple03);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("0.00"), simple03.getDeliveryCost());

		// now add a not-so-free shipping discount
		final DeliveryModeRAO notFreeDeliveryMode = new DeliveryModeRAO();
		notFreeDeliveryMode.setCode("PREMIUM-OVERNIGHT");
		notFreeDeliveryMode.setCost(new BigDecimal("20.00"));
		getRuleEngineCalculationService().changeDeliveryMode(simple03, notFreeDeliveryMode);
		Assert.assertEquals(new BigDecimal("120.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("20.00"), simple03.getDeliveryCost());

		// now back to free shipping discount (I could do this all day)
		getRuleEngineCalculationService().changeDeliveryMode(simple03, freeDeliveryMode);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("0.00"), simple03.getDeliveryCost());

		// now just recalculate to make sure nothing changes
		getRuleEngineCalculationService().calculateTotals(simple03);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("0.00"), simple03.getDeliveryCost());
	}


	@Test
	public void testAddOrderLevelDiscountPercentage()
	{
		// simple cart with 2 entries and delivery cost
		final CartRAO simple03 = createCartRAO("simple03", USD);
		simple03.setEntries(set(createOrderEntryRAO(simple03, "12.34", USD, 2, 0),
				createOrderEntryRAO(simple03, "23.45", USD, 3, 1)));
		simple03.setDeliveryCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple03);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), simple03.getDeliveryCost());

		// now add a 10% oder-level discount
		// 10% of sub total = 9.50
		// total is 100.03 - 9.50 = 90.53
		getRuleEngineCalculationService().addOrderLevelDiscount(simple03, false, new BigDecimal("10.00"));
		Assert.assertEquals(new BigDecimal("5.00"), simple03.getDeliveryCost());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("90.53"), simple03.getTotal());
	}

	@Test
	public void testAddOrderLevelDiscountAbsolute()
	{
		// simple cart with 2 entries and delivery cost
		final CartRAO simple03 = createCartRAO("simple03", USD);
		simple03.setEntries(set(createOrderEntryRAO(simple03, "12.34", USD, 2, 0),
				createOrderEntryRAO(simple03, "23.45", USD, 3, 1)));
		simple03.setDeliveryCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple03);
		Assert.assertEquals(new BigDecimal("100.03"), simple03.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), simple03.getDeliveryCost());

		// now add a 10USD oder-level discount
		// total is 100.03 - 10 = 90.03
		getRuleEngineCalculationService().addOrderLevelDiscount(simple03, true, new BigDecimal("10.00"));
		Assert.assertEquals(new BigDecimal("5.00"), simple03.getDeliveryCost());
		Assert.assertEquals(new BigDecimal("95.03"), simple03.getSubTotal());
		Assert.assertEquals(new BigDecimal("90.03"), simple03.getTotal());
	}

	@Test
	public void testAddOrderEntryLevelDiscountPercentage()
	{
		// simple cart with 2 entries and delivery cost
		final CartRAO simple04 = createCartRAO("simple04", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(simple04, "12.34", USD, 2, 0);
		simple04.setEntries(set(orderEntry1, createOrderEntryRAO(simple04, "23.45", USD, 3, 1)));
		simple04.setDeliveryCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple04);
		Assert.assertEquals(new BigDecimal("100.03"), simple04.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple04.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), simple04.getDeliveryCost());

		// now add a 10% oder-entry-level discount
		// subtotal is: 12.34*2*0.9 + 23.45*3 = 92.562
		// total is: 12.34*2*0.9 + 23.45*3 + 5 = 97.562
		getRuleEngineCalculationService().addOrderEntryLevelDiscount(orderEntry1, false, new BigDecimal("10.00"));
		Assert.assertEquals(new BigDecimal("5.00"), simple04.getDeliveryCost());
		Assert.assertEquals(new BigDecimal("92.56"), simple04.getSubTotal());
		Assert.assertEquals(new BigDecimal("97.56"), simple04.getTotal());
	}

	@Test
	public void testAddOrderEntryLevelDiscountAbsolute()
	{
		// simple cart with 2 entries and delivery cost
		final CartRAO simple05 = createCartRAO("simple05", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(simple05, "12.34", USD, 2, 0);
		simple05.setEntries(set(orderEntry1, createOrderEntryRAO(simple05, "23.45", USD, 3, 1)));
		simple05.setDeliveryCost(new BigDecimal("5.00"));
		getRuleEngineCalculationService().calculateTotals(simple05);
		Assert.assertEquals(new BigDecimal("100.03"), simple05.getTotal());
		Assert.assertEquals(new BigDecimal("95.03"), simple05.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), simple05.getDeliveryCost());

		// now add a 10USD oder-entry-level discount
		// subtotal is: (12.34-10)*2 + 23.45*3 = 75.03
		// total is: (12.34-10)*2 + 23.45*3 + 5 = 80.03
		getRuleEngineCalculationService().addOrderEntryLevelDiscount(orderEntry1, true, new BigDecimal("10.00"));
		Assert.assertEquals(new BigDecimal("5.00"), simple05.getDeliveryCost());
		Assert.assertEquals(new BigDecimal("75.03"), simple05.getSubTotal());
		Assert.assertEquals(new BigDecimal("80.03"), simple05.getTotal());
	}


	@Test
	public void testAddOrderEntryLevelFixedPrice()
	{
		// simple cart with 2 entries and delivery cost
		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "12.50", USD, 1, 0);
		cartRao1.setEntries(set(orderEntry1, createOrderEntryRAO(cartRao1, "20.25", USD, 2, 1)));
		cartRao1.setDeliveryCost(new BigDecimal("5.00"));

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("58.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("53.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

		getRuleEngineCalculationService().addFixedPriceEntryDiscount(orderEntry1, new BigDecimal("10.00"));
		Assert.assertEquals(new BigDecimal("55.50"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("50.50"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());
	}


	@Test
	public void testAddOrderEntryLevelFixedPriceError()
	{
		// fixed price amount is greater than the base price for this item so no discount should be created
		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "12.50", USD, 1, 0);
		cartRao1.setEntries(set(orderEntry1, createOrderEntryRAO(cartRao1, "20.25", USD, 2, 1)));
		cartRao1.setDeliveryCost(new BigDecimal("5.00"));

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("58.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("53.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

		getRuleEngineCalculationService().addFixedPriceEntryDiscount(orderEntry1, new BigDecimal("50"));

		//no discount should be applied because fixed price was higher that original price
		Assert.assertEquals(new BigDecimal("58.00"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("53.00"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());
	}

	@Test
	public void testExcludedProducts()
	{
		final List<ProductRAO> excludedProducts = new ArrayList();
		final ProductRAO prodRao1 = new ProductRAO();
		prodRao1.setCode("13579");
		excludedProducts.add(prodRao1);

		final CartRAO cartRao1 = createCartRAO("cart01", USD);
		final OrderEntryRAO orderEntry1 = createOrderEntryRAO(cartRao1, "12.50", USD, 1, 0);
		orderEntry1.getProduct().setCode("13579");
		final OrderEntryRAO orderEntry2 = createOrderEntryRAO(cartRao1, "24.00", USD, 1, 1);
		orderEntry2.getProduct().setCode("24680");
		cartRao1.setEntries(set(orderEntry1, orderEntry2));
		cartRao1.setDeliveryCost(new BigDecimal("5.00"));

		getRuleEngineCalculationService().calculateTotals(cartRao1);
		Assert.assertEquals(new BigDecimal("41.50"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("36.50"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

		final BigDecimal result = getRuleEngineCalculationService().calculateSubTotals(cartRao1, excludedProducts);

		//sub total without the excluded products
		Assert.assertEquals(new BigDecimal("24.00"), result);

		//Original cartRao is unchanged
		Assert.assertEquals(new BigDecimal("41.50"), cartRao1.getTotal());
		Assert.assertEquals(new BigDecimal("36.50"), cartRao1.getSubTotal());
		Assert.assertEquals(new BigDecimal("5.00"), cartRao1.getDeliveryCost());

	}
}
