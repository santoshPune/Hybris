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

import java.util.Map;


/**
 * Methods to validate response from Alipay
 */
public interface AlipayResponseValidationStrategy
{
	/**
	 * Validate response map is response from Alipay.Return true if response is correct
	 *
	 * @param params
	 *           Alipay request map
	 * @return true if NotifyId and Signature are valid, return false otherwise
	 */
	public boolean validateResponse(final Map<String, String> params);
}
