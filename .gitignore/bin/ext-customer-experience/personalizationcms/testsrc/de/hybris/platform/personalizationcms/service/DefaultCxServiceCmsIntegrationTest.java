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
package de.hybris.platform.personalizationcms.service;


import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.service.CxService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultCxServiceCmsIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String USER_ID = "customer1@hybris.com";

	@Resource
	private CxService cxService;
	@Resource
	private UserService userService;
	@Resource
	private CatalogVersionService catalogVersionService;


	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		createDefaultCatalog();
		importCsv("/personalizationcms/test/testdata_personalizationcms.impex", "utf-8");

	}


	@Test
	public void testActionProcessing()
	{
		final UserModel user = userService.getUserForUID(USER_ID);

		//when
		cxService.calculatePersonalizationForUser(user, catalogVersionService.getCatalogVersion("testCatalog", "Online"));

		//then
		// We expect no exception is thrown.
		// Actions will get recalculated, but there is nothing returned.
		// So we have no easy way to verify things other than no exception happened here.
		// Actual effect of recalculation can be verified in tests on higher level.
	}


	public CxService getCxService()
	{
		return cxService;
	}

	public void setCxService(final CxService cxService)
	{
		this.cxService = cxService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	public CatalogVersionService getCatalogVersionService()
	{
		return catalogVersionService;
	}

	public void setCatalogVersionService(final CatalogVersionService catalogVersionService)
	{
		this.catalogVersionService = catalogVersionService;
	}
}
