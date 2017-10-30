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
package de.hybris.platform.couponfacades.converters.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.couponservices.service.data.CouponCodeDetails;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class CouponCodeDetailsModelToVoucherDataPopulatorTest
{
	private final static String COUPON_CODE = "couponCode";

	@InjectMocks
	private CouponCodeDetailsModelToVoucherDataPopulator couponCodeDetailsModelToVoucherDataPopulator;

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateSourceNull()
	{
		final VoucherData voucherData = new VoucherData();
		couponCodeDetailsModelToVoucherDataPopulator.populate(null, voucherData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetNull()
	{
		final CouponCodeDetails couponCodeDetails = new CouponCodeDetails();
		couponCodeDetailsModelToVoucherDataPopulator.populate(couponCodeDetails, null);
	}

	@Test
	public void testPopulate()
	{
		final CouponCodeDetails couponCodeDetails = new CouponCodeDetails();
		couponCodeDetails.setCode(COUPON_CODE);

		final VoucherData voucherData = new VoucherData();
		couponCodeDetailsModelToVoucherDataPopulator.populate(couponCodeDetails, voucherData);

		assertEquals(COUPON_CODE, voucherData.getCode());
	}

}
