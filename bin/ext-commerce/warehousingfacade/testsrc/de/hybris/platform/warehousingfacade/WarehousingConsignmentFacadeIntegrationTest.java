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
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.warehousing.sourcing.ban.service.SourcingBanService;
import de.hybris.platform.warehousingfacade.order.WarehousingConsignmentFacade;
import de.hybris.platform.warehousingfacade.util.BaseWarehousingFacadeIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@IntegrationTest
public class WarehousingConsignmentFacadeIntegrationTest extends BaseWarehousingFacadeIntegrationTest
{
	@Resource
	protected WarehousingConsignmentFacade warehousingConsignmentFacade;
	@Resource
	protected SourcingBanService sourcingBanService;

	protected PageableData pageableData;

	@Before
	public void setup()
	{
		pageableData = createPageable(DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE, DEFAULT_SORT);
	}

	@Test
	public void getSourcingLocationsForConsignmentCode_Success_OneLocationAvailable()
	{
		//when
		 createDefaultConsignmentAndOrder();
		//then
		assertEquals(1, warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("con_0", pageableData).getResults().size());
		assertEquals("boston", warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("con_0", pageableData).getResults().get(0).getCode());
	}

	@Test
	public void getSourcingLocationsForConsignmentCode_Success_MultipleLocationsAvailable()
	{
		//when
		createDefaultConsignmentAndOrder();
		stockLevels.Camera(warehouses.Toronto(), 4);
		//then
		assertEquals(2, warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("con_0", pageableData).getResults().size());
		assertEquals("boston", warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("con_0", pageableData).getResults().get(0).getCode());
		assertEquals("toronto", warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("con_0", pageableData).getResults().get(1).getCode());
	}

	@Test
	public void getSourcingLocationsForConsignmentCode_Success_NoLocationsAvailableAfterBan()
	{
		//when
		createDefaultConsignmentAndOrder();
		sourcingBanService.createSourcingBan(warehouses.Boston());
		//then
		assertTrue(warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("con_0", pageableData).getResults().isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public void getSourcingLocationsForConsignmentCode_Fail_NullCode()
	{
		warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode(null, pageableData);
	}

	@Test(expected = UnknownIdentifierException.class)
	public void getSourcingLocationsForConsignmentCode_Fail_WrongCode()
	{
		warehousingConsignmentFacade.getSourcingLocationsForConsignmentCode("wrongCode", pageableData);
	}

}
