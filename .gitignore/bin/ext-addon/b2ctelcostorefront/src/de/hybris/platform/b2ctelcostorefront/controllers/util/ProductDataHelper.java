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
package de.hybris.platform.b2ctelcostorefront.controllers.util;


import javax.servlet.http.HttpServletRequest;


/**
 * Helper that contains product data related utility methods.
 */
public class ProductDataHelper
{
	public static final String CURRENT_PRODUCT = "currentProductCode";

    /**
     * Get current product's code from a request.
     *
     * @param request
     * @return
     */
	public static String getCurrentProduct(final HttpServletRequest request)
	{
		return (String) request.getAttribute(CURRENT_PRODUCT);
	}

    /**
     * Set current product's code to a request.
     *
     * @param request
     * @param currentProductCode
     */
	public static void setCurrentProduct(final HttpServletRequest request, final String currentProductCode)
	{
		request.setAttribute(CURRENT_PRODUCT, currentProductCode);
	}
}
