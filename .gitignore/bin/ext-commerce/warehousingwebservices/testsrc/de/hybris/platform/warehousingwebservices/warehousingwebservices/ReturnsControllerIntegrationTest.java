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
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.warehousingwebservices.warehousingwebservices.util.BaseWarehousingWebservicesIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;


@IntegrationTest
public class ReturnsControllerIntegrationTest extends BaseWarehousingWebservicesIntegrationTest
{
	protected String rma = "";
	protected RefundEntryModel refundEntry;

	@Before
	public void setup()
	{
		refundEntry = createReturnAndReadyToAcceptGoods();
		rma = refundEntry.getReturnRequest().getRMA().toString();
	}

	@Test
	public void postAcceptGoods()
	{
		//When
		final Response result = postAcceptGoodsByDefault(rma);
		//then
		assertOk(result, true);
	}
}
