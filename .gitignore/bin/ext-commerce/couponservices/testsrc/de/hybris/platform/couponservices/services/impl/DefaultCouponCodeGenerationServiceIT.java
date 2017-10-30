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

import static de.hybris.platform.couponservices.constants.CouponServicesConstants.COUPON_CODE_GENERATION_GLOBAL_CHARACTERSET_PROPERTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.couponservices.couponcodegeneration.CouponCodeGenerationException;
import de.hybris.platform.couponservices.dao.impl.DefaultCouponDao;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.couponservices.redemption.strategies.impl.DefaultMultiCodeCouponRedemptionStrategy;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;


@IntegrationTest
public class DefaultCouponCodeGenerationServiceIT extends ServicelayerTransactionalTest
{

	private static final String COUPON_ALPHABET = "0123456789ABCDEF";

	private static final String COUPON_SIGNATURE = "wddu0O6HiHxOUEh/zbFMOQ==";

	@Resource(name = "defaultCouponCodeGenerationService")
	protected DefaultCouponCodeGenerationService defaultCouponCodeGenerationService;

	@Resource(name = "defaultCouponDao")
	protected DefaultCouponDao defaultCouponDao;

	@Resource
	protected ModelService modelService;

	@Resource
	protected ConfigurationService configurationService;

	@Resource(name = "defaultMultiCodeCouponRedemptionStrategy")
	protected DefaultMultiCodeCouponRedemptionStrategy defaultMultiCodeCouponRedemptionStrategy;

	@Resource(name = "defaultCouponService")
	protected DefaultCouponService defaultCouponService;

	@Test
	public void testMultiCodeCouponRedemptionPossible() throws CouponCodeGenerationException
	{
		final MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
		final String code = defaultCouponCodeGenerationService.generateCouponCode(coupon);
		final boolean redeemable = defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, code);
		assertTrue(redeemable);
	}

	@Test
	public void testMultiCodeCouponRedemptionNotPossibleAnymore() throws CouponCodeGenerationException
	{
		final MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(4, 4, "-");
		final String code = defaultCouponCodeGenerationService.generateCouponCode(coupon);
		final CouponRedemptionModel redemption = modelService.create(CouponRedemptionModel.class);
		redemption.setCoupon(coupon);
		redemption.setCouponCode(code);
		modelService.save(redemption);
		final boolean redeemable = defaultMultiCodeCouponRedemptionStrategy.isRedeemable(coupon, null, code);
		assertFalse(redeemable);
	}


	@Test
	public void testMultiCodeCouponInitDefaultsInterceptorTriggeredByCreatingAlphabetWithCodeSeparatorsInIt()
	{

		final List<String> allowedCodeSeparators = Arrays.asList("-", "#", "_", ";", "|", "+", "*", ".");
		allowedCodeSeparators.stream().forEach(this::doTestCodeSeparatorPartOfGlobalCharachterSet);
	}

	protected void doTestCodeSeparatorPartOfGlobalCharachterSet(final String codeSeparator)
	{
		final String currentAlphabet = configurationService.getConfiguration()
				.getString(COUPON_CODE_GENERATION_GLOBAL_CHARACTERSET_PROPERTY);
		try
		{
			// when setting the global character set to include a code separator, coupon generation should fail.
			configurationService.getConfiguration().setProperty(COUPON_CODE_GENERATION_GLOBAL_CHARACTERSET_PROPERTY,
					currentAlphabet + codeSeparator);
			modelService.create(MultiCodeCouponModel.class);
			fail("should have thrown exception during initdefaults interceptor");
		}
		catch (final ModelInitializationException e)
		{
			assertTrue(true);
		}
		finally
		{
			// reset current value
			configurationService.getConfiguration().setProperty(COUPON_CODE_GENERATION_GLOBAL_CHARACTERSET_PROPERTY,
					currentAlphabet);
		}

	}

	@Test(expected = ModelSavingException.class)
	public void testChangingCodeConfigurationAfterCouponUsesItFails()
	{
		// generate code configuration
		final CodeGenerationConfigurationModel config = generateCodeGenerationConfiguration(4, 4, "-");
		// modify it without any coupons using it is fine
		config.setName("config456");
		config.setCouponPartCount(5);
		try
		{
			modelService.save(config);
		}
		catch (final ModelSavingException ex)
		{
			fail("Code Generation Configuration Model failed to save");

		}

		// now we generate a coupon with this config
		final MultiCodeCouponModel coupon = generateMultiCodeCoupon(config, "testChangingCodeConfigurationAfterCouponUsesItFails");
		coupon.setCouponCodeNumber(Long.valueOf(100));
		// changing the config now should not be allowed
		config.setCouponPartCount(4);
		modelService.save(config);
	}

	@Test
	public void testCodeGenerationConfigurationValidateInterceptorTriggeredByWrongLengths()
	{
		// test all part length and part count combinations from -10 to 50
		for (int i = -10; i < 50; i++)
		{
			for (int j = -10; j < 50; j++)
			{
				doTestCouponPartAndCountLengthRestrictions(i, j);
			}
		}
	}

	protected void doTestCouponPartAndCountLengthRestrictions(final int count, final int length)
	{
		final CodeGenerationConfigurationModel config = modelService.create(CodeGenerationConfigurationModel.class);
		config.setCodeSeparator("-");
		config.setCouponPartCount(count);
		config.setCouponPartLength(length);
		config.setName("config-" + count + length);
		if (count <= 0 || length <= 0 || (count * length > 40) || (count * length) < 4 || (count * length) % 4 != 0)
		{
			// expect error
			try
			{
				modelService.save(config);
				fail("should have failed to save with partCount:" + count + ", partLength:" + length);
			}
			catch (final ModelSavingException e)
			{
				// expected
			}
		}
		else
		{
			// expect no error
			modelService.save(config);
		}
	}

	protected void doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(final long couponCodeNumber, final int partCount,
			final int partLength, final String separator) throws CouponCodeGenerationException
	{
		assertTrue("couponCodeNumber must be > 0", couponCodeNumber > 0);
		// first we create a valid code for coupon code number 10
		final MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(partCount, partLength, separator);
		assertNotNull(coupon);
		coupon.setCouponCodeNumber(Long.valueOf(couponCodeNumber));
		final String code = defaultCouponCodeGenerationService.generateCouponCode(coupon);
		// we check that the code is deemed valid
		final boolean valid = defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
		assertTrue("coupon code" + code + " not valid", valid);
		// but when the couponCodeNumber is lower than our code
		coupon.setCouponCodeNumber(Long.valueOf(couponCodeNumber - 1));
		final boolean notValid = defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
		assertFalse("coupon code" + code + " should not be valid anymore.", notValid);
	}

	// serves as base method for all testCouponCodeGenerationAndVerificationYxZ methods below
	protected void doCouponCodeGenerationAndValidationYxZ(final int partCount, final int partLength, final String separator)
			throws CouponCodeGenerationException
	{
		final MultiCodeCouponModel coupon = generateCouponAndConfigurationForLengths(partCount, partLength, separator);
		assertNotNull(coupon);
		final String code = defaultCouponCodeGenerationService.generateCouponCode(coupon);
		final int expectedLength = (partCount * partLength) + coupon.getCouponId().length()
				+ (coupon.getCodeGenerationConfiguration().getCodeSeparator().length() * partCount);
		assertEquals(expectedLength, code.length());
		final boolean valid = defaultCouponCodeGenerationService.verifyCouponCode(coupon, code);
		assertTrue("coupon code" + code + " not valid", valid);
	}

	// serves as base method for all methods below
	protected MultiCodeCouponModel generateCouponAndConfigurationForLengths(final int partCount, final int partLength,
			final String codeSeparator)
	{
		final String id = "id" + partCount + partLength;
		final CodeGenerationConfigurationModel config = generateCodeGenerationConfiguration(partCount, partLength, codeSeparator);
		return generateMultiCodeCoupon(config, id);
	}

	protected CodeGenerationConfigurationModel generateCodeGenerationConfiguration(final int partCount, final int partLength,
			final String codeSeparator)
	{
		final CodeGenerationConfigurationModel config = modelService.create(CodeGenerationConfigurationModel.class);
		config.setCodeSeparator(codeSeparator);
		config.setCouponPartCount(partCount);
		config.setCouponPartLength(partLength);
		config.setName("config for separator:" + codeSeparator + "count:" + partCount + "length:" + partLength);
		modelService.save(config);
		return config;
	}

	protected MultiCodeCouponModel generateMultiCodeCoupon(final CodeGenerationConfigurationModel config, final String id)
	{
		final MultiCodeCouponModel coupon = modelService.create(MultiCodeCouponModel.class);
		coupon.setCouponId(id);
		coupon.setActive(Boolean.TRUE);
		coupon.setAlphabet(COUPON_ALPHABET);
		coupon.setSignature(COUPON_SIGNATURE);
		coupon.setCodeGenerationConfiguration(config);
		coupon.setStartDate(new Date());
		coupon.setEndDate(DateUtils.addYears(new Date(), 1));
		coupon.setName(id);
		modelService.save(coupon);
		return coupon;
	}


	//////////////////////////////////////
	// CODE GENERATION AND VERIFICATION //
	//////////////////////////////////////

	//// LENGTH 4
	@Test
	public void testCouponCodeGenerationAndVerification1x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 1, "-");
	}

	//// LENGTH 8
	@Test
	public void testCouponCodeGenerationAndVerification1x8() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification8x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(8, 1, "-");
	}

	//// LENGTH 12
	@Test
	public void testCouponCodeGenerationAndVerification1x12() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 12, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x6() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification3x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(3, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x3() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 3, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification6x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(6, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification12x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(12, 1, "-");
	}

	////LENGTH 16
	@Test
	public void testCouponCodeGenerationAndVerification1x16() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 16, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x8() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification8x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(8, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification16x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(12, 1, "-");
	}

	//// LENGTH 20
	@Test
	public void testCouponCodeGenerationAndVerification1x20() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 20, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x10() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 10, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x5() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 5, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification5x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(5, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification10x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(10, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification20x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(20, 1, "-");
	}

	//// LENGTH 24
	@Test
	public void testCouponCodeGenerationAndVerification1x24() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 24, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x12() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 12, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification3x8() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(3, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x6() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification6x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(6, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification8x3() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(8, 3, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification12x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(12, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification24x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(24, 1, "-");
	}

	////LENGTH 28
	@Test
	public void testCouponCodeGenerationAndVerification1x28() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 28, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x14() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 14, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x7() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 7, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification7x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(7, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification14x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(14, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification28x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(28, 1, "-");
	}

	////LENGTH 32
	@Test
	public void testCouponCodeGenerationAndVerification1x32() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 32, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x16() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 16, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x8() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification8x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(8, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification16x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(16, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification32x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(32, 1, "-");
	}

	////LENGTH 36
	@Test
	public void testCouponCodeGenerationAndVerification1x36() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 36, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x18() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 18, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification3x12() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(3, 12, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x9() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 9, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification6x6() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(6, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification9x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(9, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification12x3() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(12, 3, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification18x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(18, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification36x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(36, 1, "-");
	}


	////LENGTH 40
	@Test
	public void testCouponCodeGenerationAndVerification1x40() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(1, 40, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification2x20() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(2, 20, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification4x10() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(4, 10, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification5x8() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(5, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification10x4() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(10, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification20x2() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(20, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndVerification40x1() throws CouponCodeGenerationException
	{
		doCouponCodeGenerationAndValidationYxZ(40, 1, "-");
	}


	///////////////////////////////////
	// CODE GENERATION AND REJECTION //
	///////////////////////////////////
	// tests that a MultiCodeCoupon code is only accepted if the couponCodeNumber is greater than the number used to generate the code.

	//// LENGTH 4
	@Test
	public void testCouponCodeGenerationAndRejection1x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(255L, 1, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 4, 1, "-");
	}

	//// LENGTH 8
	@Test
	public void testCouponCodeGenerationAndRejection1x8() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(65535L, 1, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection8x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 1, "-");
	}

	//// LENGTH 12
	@Test
	public void testCouponCodeGenerationAndRejection1x12() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(16777215L, 1, 12, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x6() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(16777215L, 2, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection3x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(16777215L, 3, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x3() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 4, 3, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection6x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 6, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection12x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 12, 1, "-");
	}

	////LENGTH 16
	@Test
	public void testCouponCodeGenerationAndRejection1x16() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 16, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x8() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x4_() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 4, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection8x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection16x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 16, 1, "-");
	}


	//// LENGTH 20
	@Test
	public void testCouponCodeGenerationAndRejection1x20() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 20, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x10() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 10, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x5() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 5, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection5x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 5, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection10x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 10, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection20x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 20, 1, "-");
	}


	//// LENGTH 24
	@Test
	public void testCouponCodeGenerationAndRejection1x24() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 24, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x12() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 12, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection3x8() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 3, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x6() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection6x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 6, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection8x3() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 3, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection12x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 12, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection24x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 24, 1, "-");
	}

	////LENGTH 28
	@Test
	public void testCouponCodeGenerationAndRejection1x28() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 28, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x14() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 14, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x7() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 7, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection7x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 7, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection14x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 14, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection28x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 28, 1, "-");
	}


	////LENGTH 32
	@Test
	public void testCouponCodeGenerationAndRejection1x32() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 32, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x16() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 16, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x8() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection8x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection16x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 16, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection32x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 32, 1, "-");
	}


	////LENGTH 36
	@Test
	public void testCouponCodeGenerationAndRejection1x36() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 36, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x18() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 18, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection3x12() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 3, 12, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x9() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 9, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection6x6() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 6, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection6x6_() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 6, 6, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection9x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 9, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection12x3() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 12, 3, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection18x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 18, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection36x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 36, 1, "-");
	}


	////LENGTH 40
	@Test
	public void testCouponCodeGenerationAndRejection1x40() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 1, 40, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection2x20() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 2, 20, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection4x10() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 4, 10, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection5x8() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(4294967295L, 5, 8, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection8x5() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 8, 5, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection10x4() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 10, 4, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection20x2() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 20, 2, "-");
	}

	@Test
	public void testCouponCodeGenerationAndRejection40x1() throws CouponCodeGenerationException
	{
		doTestGeneratedCodeGetsRejectedDueToCouponCodeNumberCheck(1L, 40, 1, "-");
	}
}
