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
import de.hybris.platform.warehousing.enums.DeclineReason;
import de.hybris.platform.warehousingwebservices.warehousingwebservices.util.BaseWarehousingWebservicesIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


@IntegrationTest
public class ConsignmentsControllerIntegrationTest extends BaseWarehousingWebservicesIntegrationTest
{
	@Before
	public void setup()
	{
		createConsignmentAndOrder();
	}

	@Test
	public void getAllConsignments()
	{
		//when
		Response result = getAllConsignmentsByDefault();
		//then
		assertEquals( "2", getNodeByXpath(result, "count(//consignments/code)"));
		assertEquals("con_0", getNodeByXpath(result, "//consignments[code='con_0']/code"));
		assertEquals("con_1", getNodeByXpath(result, "//consignments[code='con_1']/code"));
	}

	@Test
	public void getConsignmentForCode()
	{
		//When
		final Response result = getConsignmentsForCodeByDefault("con_0");
		//then
		assertEquals("1", getNodeByXpath(result, "count(//consignment/code)"));
		assertEquals("con_0", getNodeByXpath(result, "//consignment/code"));
	}

	@Test
	public void getConsignmentStatus()
	{
		//When
		final Response result = getConsignmentStatusByDefault();
		//then
		assertEquals("8", getNodeByXpath(result, "count(//consignmentStatusList/statuses)"));
	}

	@Test
	public void getDeclineReasons()
	{
		//When
		final Response result = getDeclineReasonsByDefault();
		//then
		assertEquals("5", getNodeByXpath(result, "count(//declineReasonList/reasons)"));

		List<DeclineReason> declineReasonList = getEnumerationService().getEnumerationValues(DeclineReason._TYPECODE);
		assertTrue(
				declineReasonList.stream().anyMatch(reason -> reason.name().equals(getNodeByXpath(result, "//declineReasonList/reasons"))));
	}

	@Test
	public void getConsignmentEntries()
	{
		//When
		final Response result = getConsignmentEntriesByDefault("con_0");
		//then
		assertEquals("1", getNodeByXpath(result, "count(//consignmentEntries)"));
	}

	@Test
	public void getSourcingLocations()
	{
		//When
		final Response result = getSourcingLocationsByDefault("con_0");
		//then
		assertEquals("1", getNodeByXpath(result, "count(//warehouses)"));
		assertEquals("boston", getNodeByXpath(result, "//warehouses/code"));
	}
}
