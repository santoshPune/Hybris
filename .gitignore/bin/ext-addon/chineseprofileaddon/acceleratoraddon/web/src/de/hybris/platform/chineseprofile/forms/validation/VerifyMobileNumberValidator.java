/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chineseprofile.forms.validation;

import de.hybris.platform.chineseprofile.constants.WebConstants;
import de.hybris.platform.chineseprofile.forms.VerificationCodeForm;
import de.hybris.platform.chineseprofilefacades.customer.CustomerFacade;
import de.hybris.platform.chineseprofileservices.data.VerificationData;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


/**
 * Validator for verify mobile number binding and unbinding.
 */
@Component("verifyMobileNumberValidator")
public class VerifyMobileNumberValidator implements Validator
{

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "chineseCustomerFacade")
	private CustomerFacade customerFacade;

	@Override
	public boolean supports(final Class<?> clazz)
	{
		return VerificationCodeForm.class.equals(clazz);
	}

	@Override
	public void validate(final Object object, final Errors errors)
	{
		final VerificationCodeForm form = (VerificationCodeForm) object;
		final String verificationCode = form.getVerificationCode();
		final String mobileNumber = form.getMobileNumber();
		final String codeType = form.getCodeType();

		if (CodeType.BINDING.getType().equals(codeType))
		{
			validateMobileNumber(mobileNumber, errors);
		}
		validateVerificationCode(verificationCode, WebConstants.VERIFICATION_CODE_FOR_MOBILE_BINDING, errors);
	}

	/**
	 * Validate the binding mobile number before saving.
	 */
	protected void validateMobileNumber(final String mobileNumber, final Errors errors)
	{

		final VerificationData data = sessionService.getAttribute(WebConstants.VERIFICATION_CODE_FOR_MOBILE_BINDING);
		if (StringUtils.isBlank(mobileNumber) || data == null)
		{
			errors.rejectValue("mobileNumber", "register.mobileNumber.invalid");
			return;
		}

		if (!mobileNumber.equals(data.getMobileNumber()))
		{
			errors.rejectValue("mobileNumber", "register.mobileNumber.difference");
			return;
		}

		if (!customerFacade.isMobileNumberUnique(mobileNumber))
		{
			errors.rejectValue("mobileNumber", "register.mobileNumber.registered");
		}
	}

	/**
	 * Validate the verification code before saving.
	 */
	protected void validateVerificationCode(final String verificationCode, final String key, final Errors errors)
	{

		if (StringUtils.isBlank(verificationCode))
		{
			errors.rejectValue("verificationCode", "register.verificationCode.invalid");
			return;
		}

		final VerificationData data = sessionService.getAttribute(key);
		if (data == null)
		{
			errors.rejectValue("verificationCode", "register.verificationCode.timeout");
			return;
		}

		if (!verificationCode.equals(data.getVerificationCode()))
		{
			errors.rejectValue("verificationCode", "register.verificationCode.invalid");
			return;
		}

		if (isVerificationCodeExpired(data.getTime()))
		{
			customerFacade.removeVerificationCodeFromSession(key);
			errors.rejectValue("verificationCode", "register.verificationCode.timeout");
		}
	}

	/**
	 * Check the verification code is expired.
	 *
	 * @param date
	 *           The time when verification code was creating.
	 * @return whether is expired.
	 */
	protected boolean isVerificationCodeExpired(Date date)
	{
		final Date currentTime = new Date();
		long timeout = customerFacade.getVerificationCodeTimeout(WebConstants.VERIFICATION_CODE_OUT_TIME_KEY) * 1000;
		return date.getTime() + timeout < currentTime.getTime();
	}

	private enum CodeType
	{
		BINDING("binding"), UNBINDING("unbinding");

		private final String type;

		private CodeType(String type)
		{
			this.type = type;
		}

		public String getType()
		{
			return type;
		}
	}
}
