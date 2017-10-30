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

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.calculation.MinimumAmountValidationStrategy;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


@UnitTest
public class DefaultRuleEngineCalculationServiceAddMethodsTest
{
	@Mock
	private Converter<AbstractOrderRAO, Order> abstractOrderRaoToOrderConverter;


	@Mock
	private MinimumAmountValidationStrategy minimumAmountValidationStrategy;


	@Mock
	private Converter<ProductModel, ProductRAO> productConverter;
	@InjectMocks
	private DefaultRuleEngineCalculationService service;


	private Currency currency;



	@Before
	public void setUp() throws Exception
	{
		service = new DefaultRuleEngineCalculationService();
		initMocks(this);
		doReturn(Boolean.TRUE).when(minimumAmountValidationStrategy).isOrderLowerLimitValid(any(), any());
		currency = new Currency("GBP", 2);
	}

	@Test
	public void testAddOrderLevelDiscountValidation() throws Exception
	{
		try
		{
			service.addOrderLevelDiscount(null, false, null);
			fail("IllegalArgumentException expected.");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("cart rao must not be null"));
		}
		try
		{
			final CartRAO cartRao = new CartRAO();
			service.addOrderLevelDiscount(cartRao, false, null);
			fail("IllegalArgumentException expected.");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("amount must not be null"));
		}
	}

	@Test
	public void testAddOrderLevelDiscountSuccessPath() throws Exception
	{
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);
		final BigDecimal discount = BigDecimal.valueOf(100, 2);
		final CartRAO cartRao = new CartRAO();
		final Order order = new Order(currency, null);

		when(abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(order);

		final OrderDiscount value = new OrderDiscount(new Money(BigDecimal.valueOf(100, 2), currency));

		//We're not testing creation methods in this class, so stub other methods.
		doReturn(value).when(serviceSpy).createOrderDiscount(any(), eq(true), any());
		final DiscountRAO discountRao = new DiscountRAO();
		when(serviceSpy.createDiscountRAO(value)).thenReturn(discountRao);
		doNothing().when(serviceSpy).recalculateTotals(cartRao, order);

		//execute
		serviceSpy.addOrderLevelDiscount(cartRao, true, discount);

		//test assertions
		verify(serviceSpy).createDiscountRAO(value);
		verify(serviceSpy).recalculateTotals(cartRao, order);

		//cart and discount added to each other:
		assertThat(discountRao.getAppliedToObject(), is(cartRao));
		assertThat(cartRao.getActions(), contains(discountRao));
	}

	@Test
	public void testAddOrderEntryLevelDiscountOrderEntryRAOBooleanBigDecimalValidation() throws Exception
	{
		final boolean absolute = false;
		final BigDecimal amount = BigDecimal.valueOf(200, 2);
		try
		{
			service.addOrderEntryLevelDiscount(null, absolute, amount);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("order entry rao must not be null"));
		}
		try
		{
			service.addOrderEntryLevelDiscount(mock(OrderEntryRAO.class), absolute, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("corresponding entry cart rao must not be null"));
		}
		final OrderEntryRAO mock = mock(OrderEntryRAO.class);
		final AbstractOrderRAO order = mock(AbstractOrderRAO.class);
		when(mock.getOrder()).thenReturn(order);
		try
		{
			service.addOrderEntryLevelDiscount(mock, absolute, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("amount must not be null"));
		}
	}

	@Test
	public void testAddOrderEntryLevelDiscountOrderEntryRAOBooleanBigDecimalSuccessPath() throws Exception
	{
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);

		final boolean absolute = false;
		final BigDecimal amount = BigDecimal.valueOf(200, 2);
		final CartRAO cartRao = new CartRAO();
		final OrderEntryRAO orderEntryRao = new OrderEntryRAO();
		final DiscountRAO discountRao = new DiscountRAO();
		final LineItemDiscount discount = mock(LineItemDiscount.class);
		final Order order = mock(Order.class);
		final NumberedLineItem lineItem = mock(NumberedLineItem.class);

		when(abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(order);

		orderEntryRao.setOrder(cartRao);

		doReturn(lineItem).when(serviceSpy).findLineItem(order, orderEntryRao);
		doReturn(discount).when(serviceSpy).createLineItemDiscount(lineItem, absolute, amount);
		doReturn(discountRao).when(serviceSpy).createDiscountRAO(discount);
		doNothing().when(serviceSpy).recalculateTotals(cartRao, order);

		serviceSpy.addOrderEntryLevelDiscount(orderEntryRao, absolute, amount);

		//test assertions
		verify(serviceSpy).createDiscountRAO(discount);
		verify(serviceSpy).recalculateTotals(cartRao, order);

		//cart and discount added to each other:
		assertThat(discountRao.getAppliedToObject(), is(orderEntryRao));
		assertThat(orderEntryRao.getActions(), contains(discountRao));
		assertEquals(1, orderEntryRao.getActions().size());
	}

	@Test
	public void testAddFixedPriceEntryDiscountValidation() throws Exception
	{
		//spy on the service to mock out non-add calls.
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);

		final BigDecimal basePrice = BigDecimal.valueOf(300, 2);
		final BigDecimal fixedPrice = BigDecimal.valueOf(200, 2);
		final OrderEntryRAO orderEntryRao = new OrderEntryRAO();
		final AbstractOrderRAO order = new AbstractOrderRAO();

		//execute
		try
		{
			serviceSpy.addFixedPriceEntryDiscount(null, fixedPrice);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("cart rao must not be null"));
		}
		try
		{
			serviceSpy.addFixedPriceEntryDiscount(orderEntryRao, fixedPrice);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("Order must not be null"));
		}
		orderEntryRao.setOrder(order);
		try
		{
			serviceSpy.addFixedPriceEntryDiscount(orderEntryRao, fixedPrice);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("Product base price is null"));
		}
		orderEntryRao.setBasePrice(basePrice);
		try
		{
			serviceSpy.addFixedPriceEntryDiscount(orderEntryRao, null);
			fail("IllegalArgumentException expected");
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("fixedPrice must not be null"));
		}

	}

	@Test
	public void testAddFixedPriceEntryDiscountSuccessPath() throws Exception
	{
		//spy on the service to mock out non-add calls.
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);
		final BigDecimal basePrice = BigDecimal.valueOf(300, 2);
		final BigDecimal fixedPrice = BigDecimal.valueOf(200, 2);
		final OrderEntryRAO orderEntryRao = new OrderEntryRAO();
		final AbstractOrderRAO order = new AbstractOrderRAO();
		final DiscountRAO discountRao = new DiscountRAO();

		orderEntryRao.setBasePrice(basePrice);
		orderEntryRao.setOrder(order);
		doReturn(discountRao).when(serviceSpy).addOrderEntryLevelDiscount(any(), eq(true), any());

		//execute
		serviceSpy.addFixedPriceEntryDiscount(orderEntryRao, fixedPrice);

		//test assertions
		verify(serviceSpy).addOrderEntryLevelDiscount(orderEntryRao, true, BigDecimal.valueOf(100, 2));
	}

	@Test
	public void testAddFreeProductsToCart() throws Exception
	{
		final int quantity = 2;
		final ProductModel product = mock(ProductModel.class);
		final ProductRAO productData = new ProductRAO();
		when(productConverter.convert(product)).thenReturn(productData);
		final CartRAO cartRao = new CartRAO();
		final FreeProductRAO addFreeProductsToCart = service.addFreeProductsToCart(cartRao, product, quantity);

		assertThat(cartRao.getActions(), contains(addFreeProductsToCart));
		assertThat(addFreeProductsToCart.getAppliedToObject(), is(cartRao));

		final OrderEntryRAO orderEntryRAO = cartRao.getEntries().stream().findFirst().get();
		assertThat(orderEntryRAO.getBasePrice(), is(BigDecimal.ZERO));
		assertThat(orderEntryRAO.getCurrencyIsoCode(), is(cartRao.getCurrencyIsoCode()));
		assertEquals(quantity, orderEntryRAO.getQuantity());
		assertThat(orderEntryRAO.getProduct(), is(productData));
		assertThat(orderEntryRAO.getOrder(), is(cartRao));
		assertEquals(Integer.valueOf(1), orderEntryRAO.getEntryNumber());
		assertThat(addFreeProductsToCart.getAddedOrderEntry(), is(orderEntryRAO));
	}

	@Test
	public void testAddFreeProductsToCartEntriesSetIsNotCreatedIfAlreadyExisting() throws Exception
	{
		final ProductModel product = mock(ProductModel.class);
		final CartRAO cartRao = mock(CartRAO.class);
		when(cartRao.getEntries()).thenReturn(new LinkedHashSet<>());
		when(productConverter.convert(product)).thenReturn(new ProductRAO());

		service.addFreeProductsToCart(cartRao, product, 2);

		verify(cartRao, times(0)).setEntries(anyObject());

	}

	@Test
	public void testAddFixedPriceEntriesDiscountEmptySelectedOrderEntriesAndRaos() throws Exception
	{

		final CartRAO cartRao = new CartRAO();
		cartRao.setEntries(new LinkedHashSet<>());
		final Map<Integer, Integer> orderEntryMap = Collections.emptyMap();
		final Set<OrderEntryRAO> orderEntryRaoList = Collections.emptySet();
		final BigDecimal fixedPrice = BigDecimal.valueOf(500, 2);
		final Order order = mock(Order.class);
		when(this.abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(order);

		final List<DiscountRAO> discountList = service.addFixedPriceEntriesDiscount(cartRao, orderEntryMap, orderEntryRaoList,
				fixedPrice);
		assertThat(discountList, is(empty()));
	}

	@Test
	public void testAddFixedPriceEntriesDiscountOrderEntryRaoNotInCartRao() throws Exception
	{

		final CartRAO cartRao = new CartRAO();
		cartRao.setEntries(new LinkedHashSet<>());
		final Set<OrderEntryRAO> orderEntryRaoList = new HashSet<>();
		final Map<Integer, Integer> orderEntryMap = Collections.emptyMap();

		final OrderEntryRAO testRao = new OrderEntryRAO();
		orderEntryRaoList.add(testRao);

		final BigDecimal fixedPrice = BigDecimal.valueOf(500, 2);
		final Order order = mock(Order.class);
		when(this.abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(order);

		try
		{
			service.addFixedPriceEntriesDiscount(cartRao, orderEntryMap, orderEntryRaoList, fixedPrice);
		}
		catch (final IllegalArgumentException e)
		{
			assertThat(e.getMessage(), is("orderEntryRao from given set of selectedOrderEntryRaos:" + testRao
					+ " must be part of the given cartRAO.entries!"));
		}
	}

	/**
	 * Can fixed price entries be added to an order at the correct time?
	 *
	 * Does not test if entries are recalculated (covered in other tests).
	 *
	 * @throws Exception
	 */
	@Test
	public void testAddFixedPriceEntriesDiscountOrderEntryRaoInCartRao() throws Exception
	{
		//create a spy so we can stub out some methods
		final DefaultRuleEngineCalculationService serviceSpy = spy(service);
		final Integer entryNumber = Integer.valueOf(1);
		final BigDecimal basePrice = BigDecimal.valueOf(1000, 2);
		final Set<OrderEntryRAO> orderEntryRaoSet = new HashSet<>();
		final Map<Integer, Integer> orderEntryMap = new HashMap<>();
		final Integer quantity = Integer.valueOf(2);
		//this test expects two of entry number 1 to be discounted
		orderEntryMap.put(entryNumber, quantity);

		final CartRAO cartRao = new CartRAO();
		cartRao.setEntries(new LinkedHashSet<>());

		final OrderEntryRAO testRao = new OrderEntryRAO();
		testRao.setEntryNumber(entryNumber);
		testRao.setBasePrice(basePrice);

		orderEntryRaoSet.add(testRao);
		cartRao.getEntries().add(testRao);

		final BigDecimal fixedPrice = BigDecimal.valueOf(200, 2);

		final NumberedLineItem lineItem = new NumberedLineItem(new Money(basePrice, currency));
		lineItem.setEntryNumber(entryNumber);

		final Order cart = mock(Order.class);

		when(cart.getLineItems()).thenReturn(Collections.singletonList(lineItem));
		when(abstractOrderRaoToOrderConverter.convert(cartRao)).thenReturn(cart);
		doReturn(Boolean.TRUE).when(minimumAmountValidationStrategy).isOrderLowerLimitValid(eq(cart), any(OrderDiscount.class));
		doNothing().when(serviceSpy).recalculateTotals(eq(cartRao), eq(cart));

		//invoke the call
		final List<DiscountRAO> discounts = serviceSpy.addFixedPriceEntriesDiscount(cartRao, orderEntryMap, orderEntryRaoSet,
				fixedPrice);
		verify(serviceSpy).recalculateTotals(cartRao, cart);

		//check that the fixed price entry is added
		assertThat(discounts, is(not(empty())));
		assertEquals(1, discounts.size());

		final DiscountRAO discountRAO = discounts.get(0);
		assertThat(discountRAO.getAppliedToObject(), is(testRao));
		assertThat(testRao.getActions(), contains(discountRAO));
		assertEquals(1, testRao.getActions().size());
	}
}
