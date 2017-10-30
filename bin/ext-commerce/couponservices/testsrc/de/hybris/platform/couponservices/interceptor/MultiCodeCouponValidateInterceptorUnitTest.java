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
package de.hybris.platform.couponservices.interceptor;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.valueOf;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.couponservices.model.CodeGenerationConfigurationModel;
import de.hybris.platform.couponservices.model.MultiCodeCouponModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class MultiCodeCouponValidateInterceptorUnitTest
{

	private static final String COUPON_ID = "testCouponId123";
	private static final String CODE_SEPARATOR = "-";
	private static final String MEDIA_MODEL_CODE = "mmTestCode";

	private MultiCodeCouponValidateInterceptor validator;
	@Mock
	private InterceptorContext ctx;
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private Configuration configuration;

	@Before
	public void setUp() throws Exception
	{
		when(configurationService.getConfiguration()).thenReturn(configuration);
		when(configuration.getString("couponservices.code.generation.prefix.regexp")).thenReturn("[A-Za-z0-9]+");

		validator = new MultiCodeCouponValidateInterceptor();
		validator.setConfigurationService(configurationService);
		validator.afterPropertiesSet();

		setCouponIdModified(FALSE);
		setCodeGenerationConfigModified(FALSE);
		setCodeGeneratedCodesModified(FALSE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOnValidateModelIsNull() throws InterceptorException
	{
		validator.onValidate(null, ctx);
	}

	@Test
	public void testOnValidateGeneratedCodeIsNull() throws InterceptorException
	{
		final MultiCodeCouponModel model = getMultiCodeCouponModel(TRUE);
		model.setGeneratedCodes(null);
		validator.onValidate(model, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateActiveGeneratedCodesEmptyAndCouponIdIsModified() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(TRUE);
		setCouponIdModified(TRUE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);

		validator.onValidate(couponModel, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateNonActiveGeneratedCodesPresentAndCouponIdIsModified() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		setCouponIdModified(TRUE);
		final Collection<MediaModel> generatedCodes = Arrays.asList(getMediaModel(MEDIA_MODEL_CODE),
				getMediaModel(MEDIA_MODEL_CODE + 1));
		couponModel.setGeneratedCodes(generatedCodes);

		validator.onValidate(couponModel, ctx);
	}

	@Test
	public void testOnValidateNonActiveGeneratedCodesEmptyAndCouponIdIsModified() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		setCouponIdModified(TRUE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);

		validator.onValidate(couponModel, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateActiveGeneratedCodesEmptyAndCodeGenerationConfigModified() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(TRUE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGenerationConfigModified(TRUE);

		validator.onValidate(couponModel, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateNonActiveGeneratedCodesPresentAndCodeGenerationConfigModified() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		final Collection<MediaModel> generatedCodes = Arrays.asList(getMediaModel(MEDIA_MODEL_CODE),
				getMediaModel(MEDIA_MODEL_CODE + 1));
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGenerationConfigModified(TRUE);

		validator.onValidate(couponModel, ctx);
	}

	@Test
	public void testOnValidateNonActiveGeneratedCodesEmptyAndCodeGenerationConfigModified() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGenerationConfigModified(TRUE);

		validator.onValidate(couponModel, ctx);
	}

	@Test
	public void testOnValidateActiveGeneratedCodesEmptyAndCodeGeneratCodesModifiedEmptyCodes() throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(TRUE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGeneratedCodesModified(TRUE);
		loadOriginalValueForGeneratedCodes(Collections.EMPTY_LIST);

		validator.onValidate(couponModel, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateActiveGeneratedCodesEmptyAndCodeGeneratCodesModifiedNonEmptyOriginalCodes()
			throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(TRUE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGeneratedCodesModified(TRUE);
		loadOriginalValueForGeneratedCodes(Arrays.asList(getMediaModel(MEDIA_MODEL_CODE), getMediaModel(MEDIA_MODEL_CODE + 1)));

		validator.onValidate(couponModel, ctx);
	}

	@Test(expected = CouponInterceptorException.class)
	public void testOnValidateNonActiveGeneratedCodesEmptyAndCodeGeneratCodesModifiedNonEmptyOriginalCodes()
			throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		final Collection<MediaModel> generatedCodes = Collections.EMPTY_LIST;
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGeneratedCodesModified(TRUE);
		loadOriginalValueForGeneratedCodes(Arrays.asList(getMediaModel(MEDIA_MODEL_CODE), getMediaModel(MEDIA_MODEL_CODE + 1)));

		validator.onValidate(couponModel, ctx);
	}

	@Test
	public void testOnValidateNonActiveGeneratedCodesPresentAndCodeGeneratedCodesModifiedNonEmptyGenerated()
			throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		final Collection<MediaModel> generatedCodes = Arrays.asList(getMediaModel(MEDIA_MODEL_CODE),
				getMediaModel(MEDIA_MODEL_CODE + 1));
		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGeneratedCodesModified(TRUE);
		loadOriginalValueForGeneratedCodes(Collections.EMPTY_LIST);

		validator.onValidate(couponModel, ctx);
	}

	@Test
	public void testOnValidateNonActiveGeneratedCodesEmptyAndCodeGeneratedCodesModifiedConsistentCodeCollections()
			throws InterceptorException
	{
		final MultiCodeCouponModel couponModel = getMultiCodeCouponModel(FALSE);
		final Collection<MediaModel> generatedCodes = Arrays.asList(getMediaModel(MEDIA_MODEL_CODE),
				getMediaModel(MEDIA_MODEL_CODE + 1), getMediaModel(MEDIA_MODEL_CODE + 2));
		final Collection<MediaModel> originalGeneratedCodes = Arrays.asList(getMediaModel(MEDIA_MODEL_CODE),
				getMediaModel(MEDIA_MODEL_CODE + 1));

		couponModel.setGeneratedCodes(generatedCodes);
		setCodeGeneratedCodesModified(TRUE);
		loadOriginalValueForGeneratedCodes(originalGeneratedCodes);

		validator.onValidate(couponModel, ctx);
	}

	@Test
	public void testOnValidateTrue() throws InterceptorException
	{
		validator.onValidate(getMultiCodeCouponModel(TRUE), ctx);
	}

	private MultiCodeCouponModel getMultiCodeCouponModel(final Boolean active)
	{
		final MultiCodeCouponModel model = new MultiCodeCouponModel();
		model.setCouponId(COUPON_ID);
		model.setActive(active);
		final CodeGenerationConfigurationModel configModel = new CodeGenerationConfigurationModel();
		configModel.setCodeSeparator(CODE_SEPARATOR);
		configModel.setCouponPartCount(3);
		configModel.setName("TEST_CONFIG");
		model.setCodeGenerationConfiguration(configModel);
		return model;
	}

	private MediaModel getMediaModel(final String code)
	{
		final MediaModel mediaModel = new MediaModel();
		mediaModel.setCode(code);
		return mediaModel;
	}

	private void setCouponIdModified(final Boolean modified)
	{
		when(valueOf(ctx.isModified(any(MultiCodeCouponModel.class), eq(MultiCodeCouponModel.COUPONID)))).thenReturn(modified);
	}

	private void setCodeGenerationConfigModified(final Boolean modified)
	{
		when(valueOf(ctx.isModified(any(MultiCodeCouponModel.class), eq(MultiCodeCouponModel.CODEGENERATIONCONFIGURATION))))
				.thenReturn(modified);
	}

	private void setCodeGeneratedCodesModified(final Boolean modified)
	{
		when(valueOf(ctx.isModified(any(MultiCodeCouponModel.class), eq(MultiCodeCouponModel.GENERATEDCODES))))
				.thenReturn(modified);
	}

	private void loadOriginalValueForGeneratedCodes(final Collection<MediaModel> mediaModels)
	{
		validator = spy(validator);

		doReturn(mediaModels).when(validator).getOriginal(any(MultiCodeCouponModel.class), eq(ctx),
				eq(MultiCodeCouponModel.GENERATEDCODES));
	}

}
