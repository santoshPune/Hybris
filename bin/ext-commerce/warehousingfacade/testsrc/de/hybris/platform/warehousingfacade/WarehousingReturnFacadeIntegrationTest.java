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
package de.hybris.platform.warehousingfacade;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.warehousingfacade.returns.WarehousingReturnFacade;
import de.hybris.platform.warehousingfacade.util.BaseWarehousingFacadeIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class WarehousingReturnFacadeIntegrationTest extends BaseWarehousingFacadeIntegrationTest
{
	@Resource
	private WarehousingReturnFacade warehousingReturnFacade;
	protected String rma = "";
	protected RefundEntryModel refundEntry;
	@Before
	public void setup() {}

	@Test
	public void isAcceptGoodsConfirmable_Successfully()
	{
		//when
		refundEntry = createReturnAndReadyToAcceptGoods();
		rma = refundEntry.getReturnRequest().getRMA();
		//then
		assertTrue(warehousingReturnFacade.isAcceptGoodsConfirmable(rma));
	}

	@Test
	public void isAcceptGoodsConfirmable_Fail_ReturnNotInCorrectStatus()
	{
		//when
		final RefundEntryModel refundEntry = createDefaultReturnRequest(createDefaultConsignmentAndOrder());
		modelService.saveAll();
		rma = refundEntry.getReturnRequest().getRMA();
		//then
		assertFalse(warehousingReturnFacade.isAcceptGoodsConfirmable(rma));
	}

	@Test (expected = IllegalStateException.class)
	public void acceptGoods_Fail_ReturnNotInCorrectStatus()
	{
		//given
		final RefundEntryModel refundEntry = createDefaultReturnRequest(createDefaultConsignmentAndOrder());
		modelService.saveAll();
		rma = refundEntry.getReturnRequest().getRMA();
		//when
		warehousingReturnFacade.acceptGoods(rma);
	}
}
