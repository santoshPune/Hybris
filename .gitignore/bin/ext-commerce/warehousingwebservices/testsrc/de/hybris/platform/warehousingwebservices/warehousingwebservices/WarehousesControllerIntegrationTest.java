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
package de.hybris.platform.warehousingwebservices.warehousingwebservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.warehousingwebservices.warehousingwebservices.util.BaseWarehousingWebservicesIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static junit.framework.Assert.assertEquals;


@IntegrationTest
public class WarehousesControllerIntegrationTest extends BaseWarehousingWebservicesIntegrationTest
{
	@Before
	public void setup()
	{
	}

	@Test
	public void getWarehouseByCode()
	{
		//When
		final Response result = getWarehouseByDefault("boston");
		//then
		assertEquals("1", getNodeByXpath(result, "count(//warehouseWsDto/code)"));
		assertEquals( "boston", getNodeByXpath(result, "//warehouseWsDto/code"));
	}
}
