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
package de.hybris.platform.addressfacades.address.impl;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;
import de.hybris.platform.addressfacades.populators.CityPopulator;
import de.hybris.platform.addressfacades.populators.DistrictPopulator;
import de.hybris.platform.addressservices.address.AddressService;
import de.hybris.platform.addressservices.model.CityModel;
import de.hybris.platform.addressservices.model.DistrictModel;


public class ChineseAddressFacadeTest
{
	private static final String CITY_ISO_CODE = "CN-11-1";
	private static final String DISTRICT_ISO_CODE = "CN-11-1-1";
	@Mock
	private AddressService chineseAddressService;
	@Mock
	private CityPopulator cityPopulator;
	@Mock
	private DistrictPopulator districtPopulator;
	private ChineseAddressFacade chineseAddressFacade;
	private CityData expectedCityData;
	private DistrictData expectedDistrictData;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		chineseAddressFacade = new ChineseAddressFacade();
		chineseAddressFacade.setCityPopulator(cityPopulator);
		chineseAddressFacade.setDistrictPopulator(districtPopulator);
		chineseAddressFacade.setChineseAddressService(chineseAddressService);
	}

	@Test
	public void testGetCityWhenExistCityWithIsocode()
	{
		final CityModel cityModel = new CityModel();
		cityModel.setIsocode(CITY_ISO_CODE);

		when(chineseAddressService.getCityForIsocode(CITY_ISO_CODE)).thenReturn(cityModel);
		Mockito.doAnswer(new Answer<CityData>()
		{
			public CityData answer(InvocationOnMock invocation)
			{
				Object[] args = invocation.getArguments();
				expectedCityData = (CityData) args[1];
				return null;
			}
		}).when(cityPopulator).populate(Mockito.any(CityModel.class), Mockito.any(CityData.class));

		CityData citDataActual = chineseAddressFacade.getCityForIsocode(CITY_ISO_CODE);

		Assert.assertSame(expectedCityData, citDataActual);
	}

	@Test
	public void testGetCityWhenNonexistCityWithIsoCode()
	{
		when(chineseAddressService.getCityForIsocode(CITY_ISO_CODE)).thenReturn(null);
		CityData citDataActual = chineseAddressFacade.getCityForIsocode(CITY_ISO_CODE);

		Mockito.verify(cityPopulator, Mockito.never()).populate(Mockito.any(CityModel.class), Mockito.any(CityData.class));
		Assert.assertNull(citDataActual.getName());
		Assert.assertNull(citDataActual.getCode());
	}

	@Test
	public void testGetRegionWhenExistRegionWithIsocode()
	{
		final DistrictModel districtModel = new DistrictModel();
		districtModel.setIsocode(DISTRICT_ISO_CODE);

		when(chineseAddressService.getDistrictForIsocode(DISTRICT_ISO_CODE)).thenReturn(districtModel);
		Mockito.doAnswer(new Answer<DistrictData>()
		{
			public DistrictData answer(InvocationOnMock invocation)
			{
				Object[] args = invocation.getArguments();
				expectedDistrictData = (DistrictData) args[1];
				return null;
			}
		}).when(districtPopulator).populate(Mockito.any(DistrictModel.class), Mockito.any(DistrictData.class));
		DistrictData districtDataActual = chineseAddressFacade.getDistrcitForIsocode(DISTRICT_ISO_CODE);

		Assert.assertSame(expectedDistrictData, districtDataActual);
	}

	@Test
	public void testGetRegionWhenNonexistRegionWithIsocode()
	{
		when(chineseAddressService.getDistrictForIsocode(DISTRICT_ISO_CODE)).thenReturn(null);
		DistrictData districtDataActual = chineseAddressFacade.getDistrcitForIsocode(DISTRICT_ISO_CODE);

		Mockito.verify(districtPopulator, Mockito.never()).populate(Mockito.any(DistrictModel.class),
				Mockito.any(DistrictData.class));
		Assert.assertNull(districtDataActual.getName());
		Assert.assertNull(districtDataActual.getCode());
	}
}
