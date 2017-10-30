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
package de.hybris.platform.chinesepspalipayservices.strategies;

import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundNotification;

import java.util.List;
import java.util.Map;


/**
 * Methods to handle response from Alipay
 */
public interface AlipayHandleResponseStrategy
{
	/**
	 * Format the response from Alipay to camelCase
	 *
	 * @param responseMap
	 *           Original response map get from http response, it is snake case.
	 * @param responseRawData
	 *           Target response POJO data whose properties are camel cased.
	 * @return Object Response POJO in camel case with value from response map.
	 */
	Object camelCaseFormatter(final Map<String, String> responseMap, Object responseRawData);



	/**
	 * Get refund data list from refund notification
	 *
	 * @param alipayRefundNotification
	 * @return List of all refund data {@link AlipayRefundData}
	 */
	List<AlipayRefundData> getAlipayRefundDataList(final AlipayRefundNotification alipayRefundNotification);


}
