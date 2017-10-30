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
 */
package de.hybris.platform.print.comet.utils.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.print.model.PageModel;
import de.hybris.platform.util.WebSessionFunctions;
import org.apache.log4j.lf5.LogLevel;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static org.mockito.Mockito.when;


@UnitTest
public class DefaultCometLogToolsTest
{
	private static final String REQUEST_IP = "127.1.2.3";
	private static final String REQUEST_METHOD = "myPOST";
	private static final String REQUEST_USER = "myTestUserId";
	private static final String REQUEST_PAGE = "myTestPage";
	private static final String REQUEST_MESSAGE_PROPERTY = "comet.useraction.pushnotes.start";
	private final DefaultCometLogTools defaultCometLogTools = Mockito
			.spy((DefaultCometLogTools) Registry.getApplicationContext().getBean("defaultCometLogTools"));

	/*
	 * Test if IP, user id, and request method are logged with log4j
	 */
	@Test
	public void testLogUserAction()
	{
		final UserModel userModel = new UserModel();
		userModel.setUid(REQUEST_USER);

		final PageModel pageModel = new PageModel();
		pageModel.setCode(REQUEST_PAGE);

		// preparing fake request
		final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		when(request.getRemoteAddr()).thenReturn(REQUEST_IP);
		when(request.getMethod()).thenReturn(REQUEST_METHOD);
		WebSessionFunctions.setCurrentHttpServletRequest(request);

		// Setting default locale to make sure this tests runs on every system
		Locale.setDefault(Locale.ENGLISH);

		defaultCometLogTools.logUserAction(userModel, pageModel.getCode(), REQUEST_MESSAGE_PROPERTY, LogLevel.INFO);

		Mockito.verify(defaultCometLogTools).logMessage(Matchers.argThat(new BaseMatcher<String>()
		{
			@Override
			public boolean matches(final Object o)
			{
				if (o instanceof String)
				{
					final String message = (String) o;
					return message.contains(REQUEST_IP) && message.contains(REQUEST_METHOD) && message.contains(REQUEST_USER);
				}
				return false;
			}

			@Override
			public void describeTo(final Description description)
			{

			}
		}), Matchers.eq(LogLevel.INFO));
	}
}
