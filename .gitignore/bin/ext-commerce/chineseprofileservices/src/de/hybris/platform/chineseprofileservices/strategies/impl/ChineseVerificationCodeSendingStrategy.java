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
package de.hybris.platform.chineseprofileservices.strategies.impl;

import de.hybris.platform.chineseprofileservices.sms.SmsService;
import de.hybris.platform.chineseprofileservices.strategies.VerificationCodeSendingStrategy;

import org.springframework.beans.factory.annotation.Required;



/**
 * A demo implementation for MobileVerificationCodeSendingStrategy.
 */
public class ChineseVerificationCodeSendingStrategy implements VerificationCodeSendingStrategy
{

	private SmsService smsService;

	@Override
	public void send(final String mobileNumber, final String code)
	{
		smsService.sendMsg(mobileNumber, code);
	}

	protected SmsService getSmsService()
	{
		return smsService;
	}

	@Required
	public void setSmsService(SmsService smsService)
	{
		this.smsService = smsService;
	}

}
