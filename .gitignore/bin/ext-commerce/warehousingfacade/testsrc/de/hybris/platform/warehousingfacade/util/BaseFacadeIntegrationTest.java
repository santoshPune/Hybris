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
package de.hybris.platform.warehousingfacade.util;

import de.hybris.platform.basecommerce.util.SpringCustomContextLoader;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import org.junit.Ignore;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;


@Ignore("Just a testing base class.")
@ContextConfiguration(locations = { "classpath:/warehousingfacade-spring-test.xml" })
public class BaseFacadeIntegrationTest extends ServicelayerTransactionalTest
{
	protected static SpringCustomContextLoader springCustomContextLoader = null;

	public BaseFacadeIntegrationTest()
	{
		if (springCustomContextLoader == null)
		{
			try
			{
				springCustomContextLoader = new SpringCustomContextLoader(getClass());
				springCustomContextLoader.loadApplicationContexts((GenericApplicationContext) Registry.getCoreApplicationContext());
				springCustomContextLoader.loadApplicationContextByConvention((GenericApplicationContext) Registry
						.getCoreApplicationContext());
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
}
