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
package de.hybris.platform.sap.productconfig.model.cronjob;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.productconfig.model.impl.DataLoaderManagerContainerImpl;
import de.hybris.platform.sap.productconfig.model.intf.DataLoaderManagerContainer;

import org.junit.Test;


@SuppressWarnings("javadoc")
@UnitTest
public class DataLoaderStopJobTest
{
	DataLoaderStopJob classUnderTest = new DataLoaderStopJob();



	@Test
	public void testDataLoaderContainer()
	{
		final DataLoaderManagerContainer container = new DataLoaderManagerContainerImpl();

		classUnderTest.setDataLoaderManagerContainer(container);
		assertEquals(container, classUnderTest.getDataLoaderManagerContainer());

	}
}
