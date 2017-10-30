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
package de.hybris.platform.ruleengineservices.calculation;

import de.hybris.order.calculation.strategies.CalculationStrategies;
import de.hybris.order.calculation.strategies.impl.DefaultRoundingStrategy;
import de.hybris.order.calculation.strategies.impl.DefaultTaxRoundingStrategy;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.calculation.impl.DefaultMinimumAmountValidationStrategy;
import de.hybris.platform.ruleengineservices.calculation.impl.DefaultRuleEngineCalculationService;
import de.hybris.platform.ruleengineservices.converters.AbstractOrderRaoToCurrencyConverter;
import de.hybris.platform.ruleengineservices.converters.AbstractOrderRaoToOrderConverter;
import de.hybris.platform.ruleengineservices.converters.OrderEntryRaoToNumberedLineItemConverter;
import de.hybris.platform.ruleengineservices.converters.populator.ProductRaoPopulator;
import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.ruleengineservices.rao.util.DefaultRaoService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;


public class AbstractRuleEngineTest
{
	protected static final String USD = "USD";
	private DefaultRaoService raoService;
	private AbstractOrderRaoToOrderConverter cartRaoToOrderConverter;
	private DefaultRuleEngineCalculationService ruleEngineCalculationService;
	private AbstractOrderRaoToCurrencyConverter abstractOrderRaoToCurrencyConverter;
	private Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> unitForBundleSelectorStrategies;
	private static int entryNumber = 0;

	@Before
	public void setUpAbstractRuleEngineTest()
	{
		raoService = newDefaultRaoService();
		abstractOrderRaoToCurrencyConverter = newAbstractOrderRaoToCurrencyConverter();
		final OrderEntryRaoToNumberedLineItemConverter orderEntryRaoToNumberedLineItemConverter = newOrderEntryRaoToNumberedLineItemConverter(abstractOrderRaoToCurrencyConverter);
		cartRaoToOrderConverter = newCartRaoToOrderConverter(orderEntryRaoToNumberedLineItemConverter,
				abstractOrderRaoToCurrencyConverter);
		unitForBundleSelectorStrategies = newUnitForBundleSelectorStrategies();
		final AbstractPopulatingConverter<ProductModel, ProductRAO> productRaoConverter = new AbstractPopulatingConverter<ProductModel, ProductRAO>();
		productRaoConverter.setPopulators(Collections.singletonList(new ProductRaoPopulator()));
		productRaoConverter.setTargetClass(ProductRAO.class);
		final MinimumAmountValidationStrategy minimumAmountValidationStrategy = newMinimumAmountValidationStrategy();
		ruleEngineCalculationService = newRuleEngineCalculationService(cartRaoToOrderConverter, productRaoConverter,
				minimumAmountValidationStrategy);
	}

	private MinimumAmountValidationStrategy newMinimumAmountValidationStrategy()
	{
		return new DefaultMinimumAmountValidationStrategy();
	}

	private DefaultRuleEngineCalculationService newRuleEngineCalculationService(
			final AbstractOrderRaoToOrderConverter abstractOrderRaoToOrderConverter,
			final AbstractPopulatingConverter<ProductModel, ProductRAO> productRaoConverter,
			final MinimumAmountValidationStrategy minimumAmountValidationStrategy)

	{
		final DefaultRuleEngineCalculationService ruleEngineCalculationService = new DefaultRuleEngineCalculationService();
		ruleEngineCalculationService.setAbstractOrderRaoToOrderConverter(abstractOrderRaoToOrderConverter);
		ruleEngineCalculationService.setProductConverter(productRaoConverter);
		ruleEngineCalculationService.setMinimumAmountValidationStrategy(minimumAmountValidationStrategy);
		return ruleEngineCalculationService;
	}

	private Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> newUnitForBundleSelectorStrategies()
	{
		final Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> result = new HashMap<>();
		result.put(OrderEntrySelectionStrategy.CHEAPEST, new EntriesSelectionStrategy()
		{

			@Override
			public Map<Integer, Integer> pickup(final EntriesSelectionStrategyRPD strategy)
			{
				final Map<Integer, Integer> result = new HashMap<Integer, Integer>();
				int itemsToConsume = strategy.getQuantity();
				for (final OrderEntryRAO orderEntry : strategy.getOrderEntries().stream()
						.sorted((o1, o2) -> o1.getBasePrice().compareTo(o2.getBasePrice())).collect(Collectors.toList()))
				{
					if (itemsToConsume > 0)
					{
						int applicableUnits = 0;
						if (itemsToConsume >= orderEntry.getQuantity())
						{
							applicableUnits = orderEntry.getQuantity();
							itemsToConsume -= orderEntry.getQuantity();
						}
						else
						{
							applicableUnits = itemsToConsume;
							itemsToConsume = 0;
						}
						result.put(orderEntry.getEntryNumber(), Integer.valueOf(applicableUnits));
					}
					else
					{
						break;
					}
				}
				return result;
			}
		});
		return result;
	}

	private AbstractOrderRaoToOrderConverter newCartRaoToOrderConverter(
			final OrderEntryRaoToNumberedLineItemConverter orderEntryRaoToNumberedLineItemConverter,
			final AbstractOrderRaoToCurrencyConverter cartRaoToCurrencyConverter)
	{
		final AbstractOrderRaoToOrderConverter cartRaoToOrderConverter = new AbstractOrderRaoToOrderConverter();
		cartRaoToOrderConverter.setOrderEntryRaoToNumberedLineItemConverter(orderEntryRaoToNumberedLineItemConverter);
		cartRaoToOrderConverter.setAbstractOrderRaoToCurrencyConverter(cartRaoToCurrencyConverter);
		final CalculationStrategies calculationStrategies = new CalculationStrategies();
		calculationStrategies.setRoundingStrategy(new DefaultRoundingStrategy());
		calculationStrategies.setTaxRoundingStrategy(new DefaultTaxRoundingStrategy());
		cartRaoToOrderConverter.setCalculationStrategies(calculationStrategies);
		return cartRaoToOrderConverter;
	}

	private OrderEntryRaoToNumberedLineItemConverter newOrderEntryRaoToNumberedLineItemConverter(
			final AbstractOrderRaoToCurrencyConverter cartRaoToCurrencyConverter)
	{
		final OrderEntryRaoToNumberedLineItemConverter orderEntryRaoToNumberedLineItemConverter = new OrderEntryRaoToNumberedLineItemConverter();
		orderEntryRaoToNumberedLineItemConverter.setAbstractOrderRaoToCurrencyConverter(cartRaoToCurrencyConverter);
		return orderEntryRaoToNumberedLineItemConverter;
	}

	private AbstractOrderRaoToCurrencyConverter newAbstractOrderRaoToCurrencyConverter()
	{
		return new AbstractOrderRaoToCurrencyConverter();
	}

	private DefaultRaoService newDefaultRaoService()
	{
		return new DefaultRaoService();
	}

	protected Set<OrderEntryRAO> set(final OrderEntryRAO... entries)
	{
		return new LinkedHashSet<>(Arrays.asList(entries));
	}

	protected CartRAO createCartRAO(final String code, final String currencyIso)
	{
		final CartRAO cart = raoService.createCart();
		cart.setCode(code);
		cart.setCurrencyIsoCode(currencyIso);
		return cart;
	}

	protected OrderEntryRAO createOrderEntryRAO(final AbstractOrderRAO order, final String basePrice, final String currencyIso,
			final int quantity)
	{
		return createOrderEntryRAO(order, basePrice, currencyIso, quantity, ++entryNumber);
	}

	protected OrderEntryRAO createOrderEntryRAO(final String basePrice, final String currencyIso, final int quantity,
			final int entryNumber)
	{
		return createOrderEntryRAO(null, basePrice, currencyIso, quantity, entryNumber);
	}

	protected OrderEntryRAO createOrderEntryRAO(final AbstractOrderRAO order, final String basePrice, final String currencyIso,
			final int quantity, final int entryNumber)
	{
		final OrderEntryRAO entry = raoService.createOrderEntry();
		entry.setOrder(order);
		entry.setCurrencyIsoCode(currencyIso);
		entry.setEntryNumber(Integer.valueOf(entryNumber));
		final ProductRAO product = new ProductRAO();
		product.setCode("product01");
		entry.setBasePrice(new BigDecimal(basePrice));
		entry.setQuantity(quantity);
		entry.setProduct(product);
		return entry;
	}

	protected DiscountRAO createDiscount(final AbstractActionedRAO discountFor, final String discountValue,
			final String currencyIso)
	{
		final DiscountRAO discountRAO = new DiscountRAO();
		discountRAO.setCurrencyIsoCode(currencyIso);
		discountRAO.setValue(new BigDecimal(discountValue));
		discountFor.getActions().add(discountRAO);
		return discountRAO;
	}

	protected EntriesSelectionStrategyRPD createEntriesSelectionStrategyRPD(final OrderEntrySelectionStrategy strategy,
			final int quantity, final boolean isTargetOfAction, final OrderEntryRAO... orderEntryRAO)
	{
		final EntriesSelectionStrategyRPD rao = raoService.createEntriesSelectionStrategyRPD();
		rao.setSelectionStrategy(strategy);
		rao.setQuantity(quantity);
		rao.setOrderEntries(Arrays.asList(orderEntryRAO));
		rao.setTargetOfAction(isTargetOfAction);
		return rao;
	}

	protected DefaultRuleEngineCalculationService getRuleEngineCalculationService()
	{
		return ruleEngineCalculationService;
	}

	public void setRuleEngineCalculationService(final DefaultRuleEngineCalculationService ruleEngineCalculationService)
	{
		this.ruleEngineCalculationService = ruleEngineCalculationService;
	}

	protected DefaultRaoService getRaoService()
	{
		return raoService;
	}

	public void setRaoService(final DefaultRaoService raoService)
	{
		this.raoService = raoService;
	}

	protected AbstractOrderRaoToOrderConverter getCartRaoToOrderConverter()
	{
		return cartRaoToOrderConverter;
	}

	public void setCartRaoToOrderConverter(final AbstractOrderRaoToOrderConverter cartRaoToOrderConverter)
	{
		this.cartRaoToOrderConverter = cartRaoToOrderConverter;
	}

	protected AbstractOrderRaoToCurrencyConverter getAbstractOrderRaoToCurrencyConverter()
	{
		return abstractOrderRaoToCurrencyConverter;
	}

	public void setAbstractOrderRaoToCurrencyConverter(
			final AbstractOrderRaoToCurrencyConverter abstractOrderRaoToCurrencyConverter)
	{
		this.abstractOrderRaoToCurrencyConverter = abstractOrderRaoToCurrencyConverter;
	}

	protected Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> getUnitForBundleSelectorStrategies()
	{
		return unitForBundleSelectorStrategies;
	}

	public void setUnitForBundleSelectorStrategies(
			final Map<OrderEntrySelectionStrategy, EntriesSelectionStrategy> unitForBundleSelectorStrategies)
	{
		this.unitForBundleSelectorStrategies = unitForBundleSelectorStrategies;
	}
}
