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
package de.hybris.platform.b2ctelcostorefront.controllers.pages.myaccount;

/**
 * Silent Order Post Response URL Strategy.
 *
 * Used to determine the response (callback) URL for a SOP payment service request.
 */
public interface SopResponseUrlStrategy
{

	/**
	 * Get the URL for the SOP Response.
	 * 
	 * @return the implementation-specific SOP response URL.
	 */
	String getUrl();

}
