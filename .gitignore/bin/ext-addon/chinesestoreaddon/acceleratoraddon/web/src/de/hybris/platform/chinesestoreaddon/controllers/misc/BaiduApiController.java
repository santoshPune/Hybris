/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesestoreaddon.controllers.misc;

import de.hybris.platform.acceleratorservices.config.HostConfigService;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Baidu map find key Controller
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/baidu")
public class BaiduApiController extends AbstractController
{
	private static final String BAIDU_API_KEY = "baiduApiKey";

	@Resource(name = "hostConfigService")
	private HostConfigService hostConfigService;

	@RequestMapping(value = "/key", method = RequestMethod.GET, produces = "text/html")
	public @ResponseBody String baiduKey(final HttpServletRequest request)
	{
		return getHostConfigService().getProperty(BAIDU_API_KEY, request.getServerName());
	}

	protected HostConfigService getHostConfigService()
	{
		return hostConfigService;
	}
}
