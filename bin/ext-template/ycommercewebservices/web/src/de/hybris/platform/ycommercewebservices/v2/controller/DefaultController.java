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
 *
 */
package de.hybris.platform.ycommercewebservices.v2.controller;

import de.hybris.platform.webservicescommons.util.YSanitizer;
import de.hybris.platform.ycommercewebservices.exceptions.UnknownResourceException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class DefaultController
{
	@RequestMapping
	public void defaultRequest(final HttpServletRequest request)
	{
		throw new UnknownResourceException("There is no resource for path " + YSanitizer.sanitize(request.getRequestURI()));
	}
}
