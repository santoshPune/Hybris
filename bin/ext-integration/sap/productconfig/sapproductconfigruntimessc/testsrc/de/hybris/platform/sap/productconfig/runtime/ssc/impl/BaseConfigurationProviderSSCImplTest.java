/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.ssc.impl;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;

import org.junit.Test;


/**
 * Unit Tests
 */
@UnitTest
public class BaseConfigurationProviderSSCImplTest
{
	BaseConfigurationProviderSSCImpl classUnderTest = new BaseConfigurationProviderSSCImpl()
	{

		@Override
		protected ConfigModel fillConfigModel(final String qualifiedId)
		{
			return null;
		}
	};
	private static final String sessionId = "session1";
	private static final String configId = "12938";

	@Test
	public void testQualifiedId()
	{
		final String qualifiedId = classUnderTest.retrieveQualifiedId(sessionId, configId);
		assertTrue(qualifiedId.contains(sessionId));
		assertTrue(qualifiedId.contains(configId));

	}


}
