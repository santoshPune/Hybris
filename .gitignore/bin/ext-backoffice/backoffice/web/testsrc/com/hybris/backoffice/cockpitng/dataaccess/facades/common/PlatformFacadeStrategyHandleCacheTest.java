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

package com.hybris.backoffice.cockpitng.dataaccess.facades.common;


import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class PlatformFacadeStrategyHandleCacheTest
{

	private PlatformFacadeStrategyHandleCache platformFacadeStrategyHandleCache;

	@Before
	public void setUp()
	{
		platformFacadeStrategyHandleCache = new PlatformFacadeStrategyHandleCache();
	}


	@Test
	public void testCanHandle()
	{
		final String typeCode = "user";
		final TypeService typeService = Mockito.mock(TypeService.class);
		final TypeModel typeModel = Mockito.mock(TypeModel.class);
		Mockito.when(typeService.getTypeForCode(typeCode)).thenReturn(typeModel);
		getPlatformFacadeStrategyHandleCache().setTypeService(typeService);

		Assert.assertTrue(getPlatformFacadeStrategyHandleCache().canHandle(typeCode));
	}

	@Test
	public void testCannotHandle()
	{
		final String typeCode = "user";
		final TypeService typeService = Mockito.mock(TypeService.class);
		Mockito.when(typeService.getTypeForCode(typeCode)).thenReturn(null);
		getPlatformFacadeStrategyHandleCache().setTypeService(typeService);

		Assert.assertFalse(getPlatformFacadeStrategyHandleCache().canHandle(typeCode));
	}

	public PlatformFacadeStrategyHandleCache getPlatformFacadeStrategyHandleCache()
	{
		return platformFacadeStrategyHandleCache;
	}
}
