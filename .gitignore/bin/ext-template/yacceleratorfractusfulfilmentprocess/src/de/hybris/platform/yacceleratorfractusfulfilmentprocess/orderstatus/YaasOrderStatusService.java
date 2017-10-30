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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fractussyncservices.exception.YaasBusinessException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.UnknownEcpStatusException;


/**
 * Focuses on methods to determine yaas status corresponding to given ecp status.
 *
 */
public interface YaasOrderStatusService
{
	/**
	 * Returns yaas status corresponding to given ecp status
	 *
	 * @param ecpOrder
	 * @return
	 * @throws UnknownEcpStatusException
	 * @throws NoMatchingYaasStatusException
	 */
	public String determineYaasStatus(OrderModel ecpOrder) throws YaasBusinessException;
}
