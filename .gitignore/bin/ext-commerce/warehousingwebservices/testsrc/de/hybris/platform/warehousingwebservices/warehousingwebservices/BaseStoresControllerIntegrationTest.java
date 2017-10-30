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
public class BaseStoresControllerIntegrationTest extends BaseWarehousingWebservicesIntegrationTest
{
	@Before
	public void setup()
	{
	}

	@Test
	public void getAllDefaultWarehouse()
	{
		//When
		final Response result = getAllWarehousesByDefault();
		//then
		assertEquals("boston", getNodeByXpath(result, "//warehouses[code='boston']/code"));
		assertEquals("montreal", getNodeByXpath(result, "//warehouses[code='montreal']/code"));
	}
}
