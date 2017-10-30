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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.dao;

import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception.NoMatchingYaasStatusException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.model.YaasOrderStatusMapModel;


public interface YaasOrderStatusDao
{
	public YaasOrderStatusMapModel findYaasOrderStatus(String ecpOrderStatus) throws NoMatchingYaasStatusException;
}
