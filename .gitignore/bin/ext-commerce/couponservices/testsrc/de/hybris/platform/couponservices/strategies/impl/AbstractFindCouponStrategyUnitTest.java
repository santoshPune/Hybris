/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.couponservices.strategies.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import de.hybris.platform.couponservices.dao.CouponDao;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.strategies.FindCouponStrategy;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;


/**
 * base class for the unit tests of the concrete find strategies
 */
@Ignore
public abstract class AbstractFindCouponStrategyUnitTest
{

	protected static final String COUPON_ID = "COUPON_ID";

	@Mock
	protected AbstractCouponModel abstractCoupon;

	@Mock
	protected CouponDao couponDao;


	abstract FindCouponStrategy getStrategy();

	@Before
	public void setup()
	{
		when(couponDao.findCouponById(COUPON_ID)).thenReturn(abstractCoupon);
		when(abstractCoupon.getActive()).thenReturn(Boolean.TRUE);
		when(abstractCoupon.getStartDate()).thenReturn(DateUtils.addDays(new Date(), -1));
		when(abstractCoupon.getEndDate()).thenReturn(DateUtils.addDays(new Date(), 1));
	}

	@Test
	public void testFindCoupon()
	{
		final Optional<AbstractCouponModel> foundCouponOptional = getStrategy().findCouponForCouponCode(COUPON_ID);
		assertTrue(foundCouponOptional.isPresent());
		assertEquals(abstractCoupon, foundCouponOptional.get());
	}

	@Test
	public void testFindCouponNotFound()
	{
		when(couponDao.findCouponById(Matchers.anyString())).thenThrow(ModelNotFoundException.class);
		assertEquals(Optional.empty(), getStrategy().findCouponForCouponCode(COUPON_ID));
	}

	@Test
	public void testFindCouponNotActive()
	{
		when(abstractCoupon.getActive()).thenReturn(Boolean.FALSE);
		assertEquals(Optional.empty(), getStrategy().findCouponForCouponCode(COUPON_ID));
	}

	@Test
	public void testFindCouponNotStartDateReached()
	{
		when(abstractCoupon.getStartDate()).thenReturn(DateUtils.addDays(new Date(), 1));
		assertEquals(Optional.empty(), getStrategy().findCouponForCouponCode(COUPON_ID));
	}

	@Test
	public void testFindCouponEndDateExceeded()
	{
		when(abstractCoupon.getEndDate()).thenReturn(DateUtils.addDays(new Date(), -1));
		assertEquals(Optional.empty(), getStrategy().findCouponForCouponCode(COUPON_ID));
	}

}
