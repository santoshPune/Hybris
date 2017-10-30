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
package de.hybris.platform.addressservices.address.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.addressservices.address.daos.AddressDao;
import de.hybris.platform.addressservices.model.CityModel;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@UnitTest
public class ChineseAddressServiceTest
{
	@Mock
	private AddressDao chineseAddressDao;

	private ChineseAddressService chineseAddressService;

	@Before
	public void prepare()
	{
		MockitoAnnotations.initMocks(this);
		chineseAddressService = new ChineseAddressService();
		chineseAddressService.setChineseAddressDao(chineseAddressDao);
	}

	@Test
	public void test_Get_Empty_Cities_For_Region()
	{
		given(chineseAddressDao.getCitiesForRegion("zh")).willReturn(Collections.emptyList());
		List<CityModel> cities = chineseAddressService.getCitiesForRegion("zh");
		assertEquals(Collections.EMPTY_LIST, cities);
	}



}
