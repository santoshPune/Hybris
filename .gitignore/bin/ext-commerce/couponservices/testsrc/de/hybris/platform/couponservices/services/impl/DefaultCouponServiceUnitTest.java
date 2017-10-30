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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponservices.CouponServiceException;
import de.hybris.platform.couponservices.service.data.CouponCodeDetails;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponManagementService;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import java.util.Collections;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * JUnit test suite for {@link DefaultCouponService}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultCouponServiceUnitTest
{

	private static final String COUPON_CODE = "testCouponCode";
	private static final String COUPON_ID = "testCouponId";

	@InjectMocks
	private DefaultCouponService couponService;
	@Mock
	private CouponManagementService couponManagementService;
	@Mock
	private CalculationService calculationService;
	@Mock
	private PromotionsService promotionsService;
	@Mock
	private BaseSiteService baseSiteService;
	@Mock
	private ModelService modelService;


	@Test
	public void testReleaseCouponCode() throws Exception
	{
		final CartModel cart = new CartModel();
		cart.setAppliedCouponCodes(Collections.singleton(COUPON_CODE));

		doNothing().when(couponManagementService).releaseCouponCode(COUPON_CODE);

		couponService = spy(couponService);

		couponService.releaseCouponCode(COUPON_CODE, cart);

		verify(couponManagementService).releaseCouponCode(eq(COUPON_CODE));
		verify(couponService).removeCouponAndTriggerCalculation(eq(COUPON_CODE), eq(cart));

		verify(calculationService).calculate(eq(cart));
		verify(couponService).getPromotionGroups();
		verify(promotionsService).updatePromotions(anyCollectionOf(PromotionGroupModel.class), eq(cart));
		verify(baseSiteService).getCurrentBaseSite();

		Assertions.assertThat(cart.getAppliedCouponCodes()).isEmpty();
	}

	@Test
	public void testReleaseCouponCodeCodeNotFoundInCart() throws Exception
	{
		final CartModel cart = new CartModel();
		cart.setAppliedCouponCodes(Collections.singleton("NOT_FOUND_CODE"));

		doNothing().when(couponManagementService).releaseCouponCode(COUPON_CODE);

		couponService = spy(couponService);

		couponService.releaseCouponCode(COUPON_CODE, cart);

		verify(couponManagementService).releaseCouponCode(eq(COUPON_CODE));
		verify(couponService).removeCouponAndTriggerCalculation(eq(COUPON_CODE), eq(cart));

		verify(calculationService, times(0)).calculate(eq(cart));
		verify(promotionsService, times(0)).updatePromotions(any(), any(CartModel.class));
		verify(baseSiteService, times(0)).getCurrentBaseSite();

		assertThat(cart.getAppliedCouponCodes()).hasSize(1);
	}

	@Test(expected = CouponServiceException.class)
	public void testReleaseCouponCodeManagementServiceFailure()
	{
		final CartModel cart = new CartModel();
		cart.setAppliedCouponCodes(Collections.singleton(COUPON_CODE));

		doThrow(CouponServiceException.class).when(couponManagementService).releaseCouponCode(COUPON_CODE);

		couponService.releaseCouponCode(COUPON_CODE, cart);
	}

	@Test(expected = CouponServiceException.class)
	public void testReleaseCouponCodeCalculationFailure() throws Exception
	{
		final CartModel cart = new CartModel();
		cart.setAppliedCouponCodes(Collections.singleton(COUPON_CODE));

		doNothing().when(couponManagementService).releaseCouponCode(COUPON_CODE);
		doThrow(CalculationException.class).when(calculationService).calculate(cart);

		couponService.releaseCouponCode(COUPON_CODE, cart);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedeemCouponCodeCouponCodeNull()
	{
		couponService.redeemCoupon(null, new CartModel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRedeemCouponCodeCartNull()
	{
		final CartModel cart = null;
		couponService.redeemCoupon(COUPON_CODE, cart);
	}

	@Test
	public void testRedeemCouponInCart() throws CalculationException
	{
		final CartModel cart = new CartModel();

		couponService = spy(couponService);
		when(Boolean.valueOf(couponManagementService.redeem(COUPON_CODE, cart))).thenReturn(Boolean.TRUE);
		final CouponResponse response = couponService.redeemCoupon(COUPON_CODE, cart);

		verify(calculationService).calculate(eq(cart));
		verify(couponService).getPromotionGroups();
		verify(promotionsService).updatePromotions(anyCollectionOf(PromotionGroupModel.class), eq(cart));
		verify(baseSiteService).getCurrentBaseSite();

		assertEquals(COUPON_CODE, cart.getAppliedCouponCodes().iterator().next());
		assertNotNull(response);
		assertTrue(response.getSuccess().booleanValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCouponCodeCouponCodeNull()
	{
		couponService.verifyCouponCode(null, new CartModel());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testVerifyCouponCodeCartNull()
	{
		couponService.verifyCouponCode(COUPON_CODE, null);
	}

	@Test
	public void testVerifyCouponCodeCouponCodeValid()
	{
		when(couponManagementService.verifyCouponCode(Matchers.anyString(), Matchers.any(AbstractOrderModel.class))).thenReturn(
				getSuccessfulResponse());

		final CouponResponse response = couponService.verifyCouponCode(COUPON_CODE, new CartModel());

		assertNotNull(response);
		assertTrue(response.getSuccess().booleanValue());
		assertTrue(StringUtils.isNotEmpty(response.getCouponId()));
	}

	@Test
	public void testGetCouponDetails()
	{
		final CouponCodeDetails couponCodeDetails = new CouponCodeDetails();
		couponCodeDetails.setCouponId(COUPON_ID);
		when(couponManagementService.getCouponDetails(COUPON_CODE)).thenReturn(Optional.of(couponCodeDetails));

		final Optional<CouponCodeDetails> optional = couponService.getCouponDetails(COUPON_CODE);
		assertThat(optional.isPresent());
		assertThat(optional.get().getCouponId()).isEqualTo(COUPON_ID);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetCouponDetailsEmptyCouponCode()
	{
		couponService.getCouponDetails(null);
	}

	@Test
	public void testGetCouponDetailsWrongCode()
	{
		final CouponCodeDetails couponCodeDetails = mock(CouponCodeDetails.class);
		couponCodeDetails.setCouponId(COUPON_ID);
		when(couponManagementService.getCouponDetails("WRONG_CODE")).thenReturn(Optional.empty());

		final Optional<CouponCodeDetails> optional = couponService.getCouponDetails("WRONG_CODE");
		assertThat(!optional.isPresent());
	}

	private CouponResponse getSuccessfulResponse()
	{
		final CouponResponse response = new CouponResponse();
		response.setSuccess(Boolean.TRUE);
		response.setCouponId(COUPON_ID);
		return response;
	}
}
