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
package de.hybris.platform.warehousingwebservices.controllers.returns;

import de.hybris.platform.warehousingfacade.returns.WarehousingReturnFacade;
import de.hybris.platform.warehousingwebservices.controllers.WarehousingBaseController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.annotation.Resource;


/**
 * WebResource exposing {@link de.hybris.platform.warehousingfacade.returns.WarehousingReturnFacade}
 * http://host:port/warehousingwebservices/returns
 */
@Controller
@RequestMapping(value = "/returns")
public class WarehousingReturnsController extends WarehousingBaseController
{
	@Resource
	private WarehousingReturnFacade warehousingReturnFacade;

	/**
	 * Request to accept goods.
	 *
	 * @param code
	 * 		code for the requested returnRequest.
	 */
	@RequestMapping(value = "{code}/acceptGoods", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void acceptGoods(@PathVariable final String code)
	{
		warehousingReturnFacade.acceptGoods(code);
	}

}
