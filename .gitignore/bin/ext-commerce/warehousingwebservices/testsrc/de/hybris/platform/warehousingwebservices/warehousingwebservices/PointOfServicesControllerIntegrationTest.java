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
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.warehousingwebservices.dto.store.WarehouseCodesWsDto;
import de.hybris.platform.warehousingwebservices.warehousingwebservices.util.BaseWarehousingWebservicesIntegrationTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@IntegrationTest
public class PointOfServicesControllerIntegrationTest extends BaseWarehousingWebservicesIntegrationTest
{
	@Before
	public void setup()
	{
	}

	@Test
	public void getPointOfServiceByName()
	{
		//When
		final Response result = getPointOfServiceByDefault(pointsOfService.NAME_MONTREAL_DOWNTOWN);
		//then
		assertEquals(getNodeByXpath(result, "count(//pointOfService/name)"), "1");
		assertEquals(getNodeByXpath(result, "//pointOfService/name"), pointsOfService.NAME_MONTREAL_DOWNTOWN);
	}

	@Test
	public void getWarehouseForPointOfService()
	{
		//When
		final Response result = getWarehouseForPointOfServiceByDefault(pointsOfService.NAME_MONTREAL_DOWNTOWN);
		//then
		assertEquals(getNodeByXpath(result, "count(//warehouses/code)"), "1");
		assertEquals(getNodeByXpath(result, "//warehouses/code"), warehouses.CODE_MONTREAL);
	}

	@Test
	public void updatePointOfServiceWarehouses()
	{
		//When
		final WarehouseCodesWsDto warehouseCodesWsDto = new WarehouseCodesWsDto();
		warehouseCodesWsDto.setCodes(Arrays.asList("boston"));
		final PointOfServiceWsDTO result  = postUpdatePointOfServiceWarehouses(pointsOfService.NAME_MONTREAL_DOWNTOWN, warehouseCodesWsDto);
		//then
		assertEquals(2, result.getWarehouseCodes().size());
		assertEquals("montreal", result.getWarehouseCodes().get(0));
		assertEquals("boston", result.getWarehouseCodes().get(1));
	}

	@Test
	public void deleteWarehousesFromPointOfService()
	{
		//When
		final Response result = deleteWarehousesFromPointOfService(pointsOfService.NAME_MONTREAL_DOWNTOWN, warehouses.CODE_MONTREAL);
		//then
		assertEquals(result.getStatus(), 204);
	}

	@Test
	public void updatePointOfServiceAddress()
	{
		//When
		final PointOfServiceWsDTO result = putUpdatePointOfServiceAddress(pointsOfService.NAME_MONTREAL_DOWNTOWN, createUsAddress());
		//then
		assertEquals("5th Avenue", result.getAddress().getLine1());
		assertEquals("79777", result.getAddress().getPostalCode());
	}

	@Test(expected = BadRequestException.class)
	public void updatePointOfServiceWithInvalidAddress()
	{
		//When
		AddressWsDTO address = createUsAddress();
		address.setCountry(null);
		try
		{
			putUpdatePointOfServiceAddress(pointsOfService.NAME_MONTREAL_DOWNTOWN, address);
		}
		catch (BadRequestException | InternalServerErrorException e)
		{
			throw new BadRequestException();
		}
	}
}
