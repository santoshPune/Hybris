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
package de.hybris.platform.chinesepspalipayservices.strategies.impl;

import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundNotification;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayHandleResponseStrategy;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;


public class DefaultAlipayHandleResponseStrategy implements AlipayHandleResponseStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultAlipayHandleResponseStrategy.class);

	@Override
	public Object camelCaseFormatter(Map<String, String> responseMap, Object directRawData)
	{
		final Map<String, String> camelCaseMap = convertKey2CamelCase(responseMap);
		try
		{
			BeanUtils.populate(directRawData, camelCaseMap);
		}
		catch (IllegalAccessException | InvocationTargetException e)
		{
			LOG.error("Problem in handling Alipay's notify message", e);
		}

		return directRawData;
	}

	@Override
	public List<AlipayRefundData> getAlipayRefundDataList(AlipayRefundNotification alipayRefundNotification)
	{
		if (Integer.valueOf(alipayRefundNotification.getSuccessNum()) <= 0)
		{
			return new ArrayList<AlipayRefundData>();
		}

		String[] refundList = alipayRefundNotification.getResultDetails().split("#");
		List<AlipayRefundData> alipayRefundDataList = new ArrayList<AlipayRefundData>();
		for (String refundDetail : refundList)
		{
			if (refundDetail.contains("$"))
			{
				String[] payerInfo = (refundDetail.split("\\$"))[0].split("\\^");
				String[] sellerInfo = (refundDetail.split("\\$"))[1].split("\\^");
				AlipayRefundData alipayRefundData = new AlipayRefundData();
				alipayRefundData.setBatchNo(alipayRefundNotification.getBatchNo());

				alipayRefundData.setAlipayCode(payerInfo[0]);
				alipayRefundData.setPayerRefundAmount(Double.valueOf(payerInfo[1]));
				alipayRefundData.setPayerRefundStatus(payerInfo[2]);

				alipayRefundData.setSellerEmail(sellerInfo[0]);
				alipayRefundData.setSellerId(sellerInfo[1]);
				alipayRefundData.setSellerRefundAmount(Double.valueOf(sellerInfo[2]));
				alipayRefundData.setSellerRefundStatus(sellerInfo[3]);

				alipayRefundDataList.add(alipayRefundData);
			}
			else
			{
				String[] refundInfo = refundDetail.split("\\^");

				AlipayRefundData alipayRefundData = new AlipayRefundData();
				alipayRefundData.setBatchNo(alipayRefundNotification.getBatchNo());

				alipayRefundData.setAlipayCode(refundInfo[0]);
				alipayRefundData.setPayerRefundAmount(Double.valueOf(refundInfo[1]));
				alipayRefundData.setPayerRefundStatus(refundInfo[2]);
				alipayRefundDataList.add(alipayRefundData);

			}

		}
		return alipayRefundDataList;
	}

	protected Map<String, String> convertKey2CamelCase(final Map<String, String> snakeCaseMap)
	{
		final Map<String, String> camelCaseMap = new LinkedHashMap<String, String>();
		for (final String key : snakeCaseMap.keySet())
		{
			final String value = snakeCaseMap.get(key);
			String camelKey = WordUtils.capitalizeFully(key, new char[]
			{ '_' }).replaceAll("_", "");
			camelKey = WordUtils.uncapitalize(camelKey);
			camelCaseMap.put(camelKey, value);
		}
		return camelCaseMap;
	}

}
