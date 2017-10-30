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
 */
package de.hybris.platform.yacceleratorordermanagement.integration;

import de.hybris.platform.basecommerce.util.SpringCustomContextLoader;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.junit.Ignore;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;


@ContextConfiguration(locations =
		{ "classpath:/yacceleratorordermanagement-spring-test.xml" })
@Ignore("Base integration test class for yacceleratorordermanagement")
public class BaseAcceleratorIntegrationTest extends ServicelayerTest
{
	protected static SpringCustomContextLoader springCustomContextLoader = null;

	@Resource
	protected FlexibleSearchService flexibleSearchService;

	public BaseAcceleratorIntegrationTest()
	{
		if (springCustomContextLoader == null)
		{
			try
			{
				springCustomContextLoader = new SpringCustomContextLoader(getClass());
				springCustomContextLoader.loadApplicationContexts((GenericApplicationContext) Registry.getGlobalApplicationContext());
				springCustomContextLoader
						.loadApplicationContextByConvention((GenericApplicationContext) Registry.getGlobalApplicationContext());
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
}
