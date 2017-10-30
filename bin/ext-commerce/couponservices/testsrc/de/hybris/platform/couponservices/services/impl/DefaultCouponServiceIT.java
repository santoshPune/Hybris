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
package de.hybris.platform.couponservices.services.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCouponServiceIT extends ServicelayerBaseTest
{

	private static final String CURRENCY_ISO_CODE = "USD";
	private static final String CART_CODE = "testCartCode";
	private static final String COUPON_CODE = "testCouponCode0";
	private static final String INVALID_COUPON_CODE = "INVALID_COUPON_CODE";

	@Resource
	private DefaultCouponService couponService;

	@Resource
	private UserService userService;

	@Before
	public void setUp() throws Exception
	{
		final CalculationService calculationService = mock(CalculationService.class);
		doNothing().when(calculationService).calculate(any(CartModel.class));
		final PromotionsService promotionsService = mock(PromotionsService.class);
		when(promotionsService.updatePromotions(anyCollectionOf(PromotionGroupModel.class), any(CartModel.class))).thenReturn(null);
		couponService.setCalculationService(calculationService);
		couponService.setPromotionsService(promotionsService);
	}

	@Test
	public void testReleaseCouponCode() throws Exception
	{
		final CartModel cart = createCartModel(CART_CODE, COUPON_CODE);

		couponService.releaseCouponCode(COUPON_CODE, cart);

		assertThat(cart.getAppliedCouponCodes()).isEmpty();
	}

	@Test
	public void testReleaseCouponCodeCodeNotFoundInCart() throws Exception
	{
		final CartModel cart = createCartModel(CART_CODE, INVALID_COUPON_CODE);

		couponService.releaseCouponCode(COUPON_CODE, cart);

		assertThat(cart.getAppliedCouponCodes()).hasSize(1);
	}

	private CartModel createCartModel(final String code, final String couponCode)
	{
		final CartModel cart = new CartModel();
		cart.setCode(code);
		cart.setEntries(new ArrayList<>());
		cart.setDate(new Date());
		cart.setUser(userService.getCurrentUser());
		final CurrencyModel currency = new CurrencyModel();
		currency.setIsocode(CURRENCY_ISO_CODE);
		cart.setCurrency(currency);
		cart.setAppliedCouponCodes(Collections.singleton(couponCode));
		return cart;
	}
}
