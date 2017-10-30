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
package de.hybris.platform.ordermanagementwebservices.order;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.ordermanagementwebservices.util.BaseOrderManagementWebservicesIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


@IntegrationTest
public class OrderControllerIntegrationTest extends BaseOrderManagementWebservicesIntegrationTest
{
	@Before
	public void setup()
	{
		try
		{
			importCsv("/test/OrderTestData.csv", "UTF-8");
		}
		catch (ImpExException e)
		{
			e.printStackTrace();
		}
	}

	@Test
	public void getAllDefaultOrder()
	{
		//When
		final Response result = getAllOrderByDefault();
		//then
		assertEquals("O-K2010-C0000-001", getNodeByXpath(result, "//orders[code='O-K2010-C0000-001']/code"));
		assertEquals("O-K2010-C0005-001", getNodeByXpath(result, "//orders[code='O-K2010-C0005-001']/code"));
	}

	@Test
	public void getDefaultOrderByCode()
	{
		//When
		final Response result = getOrderByCode("O-K2010-C0000-001");
		//then
		assertEquals( "1", getNodeByXpath(result, "count(/order/code)"));
		assertEquals("O-K2010-C0000-001", getNodeByXpath(result, "//order[code='O-K2010-C0000-001']/code"));
	}

	@Test
	public void getAllOrderStatusByDefault()
	{
		//When
		final Response result = getOrderStatusByDefault();
		//then
		assertNotEquals("", getNodeByXpath(result, "//orderStatusList[statuses='SUSPENDED']/statuses"));
	}
}

