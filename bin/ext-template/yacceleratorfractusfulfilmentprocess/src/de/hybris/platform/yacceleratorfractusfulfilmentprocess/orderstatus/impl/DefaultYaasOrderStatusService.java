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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.impl;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.fractussyncservices.exception.YaasBusinessException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.YaasOrderStatusService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.dao.YaasOrderStatusDao;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.UnknownEcpStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.model.YaasOrderStatusMapModel;

import org.apache.commons.lang.StringUtils;


/**
 * Default implementation of {@link YaasOrderStatusService}
 *
 */
public class DefaultYaasOrderStatusService implements YaasOrderStatusService
{

	private YaasOrderStatusDao yaasOrderStatusDao;

	/*
	 * Uses the YaasOrderStatusMap item to look for a corresponding Yaas item status to the one already set on the ECP
	 * order. If there is no entry for the ECP order code, then the first exception is thrown. If there is an entry, but
	 * nothing against it, the lesser exception is throw to tell the calling method that no matching status can be found
	 * so that this issue can be logged at a much lower level.
	 *
	 * @see
	 * de.hybris.platform.order.YaasOrderStatusService#determineYaasStatus(de.hybris.platform.core.model.orderOrderModel
	 * ecpOrderModel)
	 */
	@Override
	public String determineYaasStatus(final OrderModel ecpOrderModel) throws YaasBusinessException
	{
		final OrderStatus status = ecpOrderModel.getStatus();

		if (status == null)
		{
			throw new UnknownEcpStatusException("Unable to locate ECP order status in the mapping table for null order status ");
		}

		final String ecpOrderStatus = status.getCode();
		final YaasOrderStatusMapModel yaasOrderStatusModel = getYaasOrderStatusDao().findYaasOrderStatus(ecpOrderStatus);

		if (yaasOrderStatusModel == null)
		{
			throw new UnknownEcpStatusException("Unable to locate ECP order status in the mapping table for : " + ecpOrderStatus);
		}

		final String yaasOrderStatus = yaasOrderStatusModel.getYaasOrderStatus();

		if (StringUtils.isEmpty(yaasOrderStatus))
		{
			throw new NoMatchingYaasStatusException("No corresponding Yaas status available for ECP status of : " + ecpOrderStatus);
		}

		return yaasOrderStatus;
	}


	/**
	 * @return the yaasOrderStatusDao
	 */
	public YaasOrderStatusDao getYaasOrderStatusDao()
	{
		return yaasOrderStatusDao;
	}

	/**
	 * @param yaasOrderStatusDao
	 *           the yaasOrderStatusDao to set
	 */
	public void setYaasOrderStatusDao(final YaasOrderStatusDao yaasOrderStatusDao)
	{
		this.yaasOrderStatusDao = yaasOrderStatusDao;
	}
}
