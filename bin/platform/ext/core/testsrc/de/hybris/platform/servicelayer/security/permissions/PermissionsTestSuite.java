/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.servicelayer.security.permissions;

import de.hybris.platform.servicelayer.security.permissions.impl.DefaultPermissionManagementServiceTest;
import de.hybris.platform.servicelayer.security.strategies.DefaultPermissionCheckValueMappingStrategyTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses(
{
//
		DefaultPermissionManagementServiceTest.class, //
		PermissionCheckingServiceTest.class, //
		DefaultPermissionCheckValueMappingStrategyTest.class, //
		PermissionCRUDServiceTest.class //
})
public class PermissionsTestSuite
{
	//
}
