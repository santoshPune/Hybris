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
package com.sap.hybris.reco.test.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.hybris.reco.common.util.UserIdProvider;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

@UnitTest
public class TestUserIdProvider
{
	@Mock
	private UserModel um;
	@Mock
	private CustomerModel cm;
	UserIdProvider uip;
	
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		Mockito.when(
				cm.getUid()).thenReturn("test@sap.com");
	}
	
	@Test
	public void testGetUserIdNull()
	{
		uip = new UserIdProvider();
		String uid = uip.getUserId(null);
		assertEquals(uid, null);
	}
	
	@Test
	public void testCustomerId()
	{
		uip = new UserIdProvider();
		String uid = uip.getUserId(cm);
		assertEquals("test@sap.com", uid);
	}

}
