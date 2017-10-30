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

package de.hybris.platform.fractussyncservices.service;

/**
 * Focuses on method to sync configuration settings to dathub.
 *
 */
public interface FractussyncService
{
	/**
	 * This method runs configuration sync job to send configuration information to datahub
	 */
	void pushYaasConfiguration();
}
