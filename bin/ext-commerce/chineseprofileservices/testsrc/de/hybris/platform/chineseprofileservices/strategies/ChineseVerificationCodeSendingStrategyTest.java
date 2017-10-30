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
package de.hybris.platform.chineseprofileservices.strategies;

import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.chineseprofileservices.sms.SmsService;
import de.hybris.platform.chineseprofileservices.strategies.impl.ChineseVerificationCodeSendingStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ChineseVerificationCodeSendingStrategyTest
{

	@Mock
	private SmsService smsService;

	private ChineseVerificationCodeSendingStrategy strategy;

	private String mobileNumber = "13812345678";

	private String msg = "8596";

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);

		strategy = new ChineseVerificationCodeSendingStrategy();
		strategy.setSmsService(smsService);
	}

	@Test
	public void test_send()
	{
		strategy.send(mobileNumber, msg);
		verify(smsService).sendMsg(mobileNumber, msg);
	}
}
