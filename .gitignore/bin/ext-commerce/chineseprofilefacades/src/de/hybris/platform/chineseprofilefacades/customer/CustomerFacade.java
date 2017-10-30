/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chineseprofilefacades.customer;

import de.hybris.platform.chineseprofileservices.data.VerificationData;



public interface CustomerFacade extends de.hybris.platform.commercefacades.customer.CustomerFacade
{
	/**
	 * save email language for current user
	 *
	 * @param languageISO
	 */
	void saveEmailLanguageForCurrentUser(String languageISO);

	/**
	 * Generate a verification code.
	 *
	 * @return Verification Code
	 */
	String generateVerificationCode();

	/**
	 * Send verification code
	 *
	 * @param data
	 */
	void sendVerificationCode(VerificationData data);

	/**
	 * Save code in session.
	 *
	 * @param data
	 *           the value will be store in session.
	 * @param name
	 *           name of key in session.
	 */
	void saveVerificationCodeInSession(VerificationData data, String name);

	/**
	 * remove verification code from session.
	 *
	 * @param name
	 *           name of key in session.
	 */
	void removeVerificationCodeFromSession(String name);

	/**
	 * Set customer's mobile number
	 *
	 * @param data
	 *           the data contains mobile number.
	 */
	void saveMobileNumber(VerificationData data);

	/**
	 * Get verification code's timeout.
	 *
	 * @param key
	 *           The configuration key.
	 * @return timeout in seconds
	 */
	int getVerificationCodeTimeout(String key);


	/**
	 * check the mobile number whether has been registered.
	 *
	 * @param mobileNumber
	 * @return return true if the number has not been used and false otherwise
	 */
	boolean isMobileNumberUnique(String mobileNumber);
}
