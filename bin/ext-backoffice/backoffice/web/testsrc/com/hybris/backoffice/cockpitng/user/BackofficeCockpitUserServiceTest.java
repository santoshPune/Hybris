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

package com.hybris.backoffice.cockpitng.user;


import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hybris.cockpitng.util.CockpitSessionService;


public class BackofficeCockpitUserServiceTest
{
	public static final String USER_NAME = "ADAM_321";
	public static final String UID = "3141592";

	@InjectMocks
	private final BackofficeCockpitUserService backofficeCockpitUserService = new BackofficeCockpitUserService();

	@Mock
	private UserService userService;

	@Mock
	private CockpitSessionService cockpitSessionService;

	@Mock
	private UserGroupModel adminGroup;

	@Mock
	private UserModel user;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testGetCurrentUserWithSetValue()
	{
		backofficeCockpitUserService.setUserService(userService);
		Mockito.when(userService.getCurrentUser()).thenReturn(user);
		Mockito.when(user.getUid()).thenReturn(USER_NAME);
		Assert.assertEquals(USER_NAME, backofficeCockpitUserService.getCurrentUser());
	}

	@Test
	public void testGetCurrentUserWithoutSetValue()
	{
		backofficeCockpitUserService.setUserService(userService);
		Mockito.when(userService.getCurrentUser()).thenReturn(null);
		Assert.assertEquals(null, backofficeCockpitUserService.getCurrentUser());
	}

	@Test
	public void testIsAdmin()
	{
		backofficeCockpitUserService.setUserService(userService);
		final UserModel currentUser = user;
		Mockito.when(userService.getUserForUID(UID)).thenReturn(currentUser);
		Mockito.when(userService.getUserGroupForUID(BackofficeCockpitUserService.BACKOFFICE_ADMIN_GROUP)).thenReturn(adminGroup);
		Mockito.when(Boolean.valueOf(userService.isMemberOfGroup(currentUser, adminGroup))).thenReturn(Boolean.TRUE);
		Assert.assertEquals(Boolean.TRUE, Boolean.valueOf(backofficeCockpitUserService.isAdmin(UID)));
	}

	@Test
	public void testSetCurrentUser()
	{
		backofficeCockpitUserService.setUserService(userService);
		final UserModel currentUser = user;
		Mockito.when(userService.getUserForUID(UID)).thenReturn(currentUser);
		backofficeCockpitUserService.setCurrentUser(UID);
	}
}
