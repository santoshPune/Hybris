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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order;

/**
 * Focuses on methods to sync yaas order details
 *
 */
public interface FractusSyncExecutionService
{
	/**
	 * Run cronjob to sync yaas order status information
	 *
	 * @param orderCode
	 * @param syncExecutionId
	 */
	public void synchOrderStatus(final String orderCode, final String syncExecutionId);

	/**
	 * Run cronjob to sync yaas order shipment information
	 *
	 * @param orderCode
	 * @param syncExecutionId
	 */
	public void syncOrderShipmentDetails(final String orderCode, final String syncExecutionId);

}
