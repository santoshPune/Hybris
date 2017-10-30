/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.addressfacades.address;


import de.hybris.platform.addressfacades.data.CityData;
import de.hybris.platform.addressfacades.data.DistrictData;

import java.util.List;


public interface AddressFacade
{
	/**
	 * Find city by its code
	 *
	 * @param isocode
	 * @return city
	 */
	CityData getCityForIsocode(String isocode);

	/**
	 * Find district by its code
	 *
	 * @param isocode
	 * @return district
	 */
	DistrictData getDistrcitForIsocode(String isocode);

	/**
	 * Find cities by region code
	 *
	 * @param regionCode
	 * @return cities
	 */
	List<CityData> getCitiesForRegion(String regionCode);

	/**
	 * Find districts by city code
	 *
	 * @param cityCode
	 * @return districts
	 */
	List<DistrictData> getDistrictsForCity(String cityCode);
}
