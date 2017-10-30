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
package de.hybris.platform.warehousingwebservices.controllers.warehouse;


import de.hybris.platform.warehousingwebservices.controllers.WarehousingBaseController;
import de.hybris.platform.warehousingwebservices.dto.store.WarehouseWsDto;
import de.hybris.platform.warehousingfacade.storelocator.data.WarehouseData;
import de.hybris.platform.warehousingfacade.warehouse.WarehousingWarehouseFacade;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * WebResource exposing {@link de.hybris.platform.warehousingfacade.warehouse.WarehousingWarehouseFacade}
 * http://host:port/warehousingwebservices/warehouses
 */
@Controller
@RequestMapping(value = "/warehouses")
public class WarehousingWarehousesController extends WarehousingBaseController
{
	@Resource
	private WarehousingWarehouseFacade warehousingWarehouseFacade;

	/**
	 * Request to get a {@link de.hybris.platform.ordersplitting.model.WarehouseModel} for the code
	 *
	 * @param code
	 * 		the name of the requested {@link de.hybris.platform.ordersplitting.model.WarehouseModel}
	 * @param fields
	 * 		defaulted to DEFAULT but can be FULL or BASIC
	 * @return the {@link de.hybris.platform.ordersplitting.model.WarehouseModel}
	 */
	@RequestMapping(value = "/{code}", method = RequestMethod.GET)
	@ResponseBody
	public WarehouseWsDto getWarehouseForCode(@PathVariable final String code,
			@RequestParam(required = false, defaultValue = DEFAULT_FIELD_SET) final String fields)
	{
		final WarehouseData warehouse = warehousingWarehouseFacade.getWarehouseForCode(code);
		return dataMapper.map(warehouse, WarehouseWsDto.class, fields);
	}

}
