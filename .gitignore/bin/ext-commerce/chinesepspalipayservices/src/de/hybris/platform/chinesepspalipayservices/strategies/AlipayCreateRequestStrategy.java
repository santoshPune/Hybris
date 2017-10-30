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

import de.hybris.platform.chinesepspalipayservices.data.AlipayCancelPaymentRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayPaymentStatusRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundRequestData;


/**
 * Payment related methods to interact with Alipay
 */
public interface AlipayCreateRequestStrategy
{
	/**
	 * Create direct_pay_url from requestData
	 *
	 * @param requestData
	 *           Direct_Pay request data needed by alipay {@link AlipayDirectPayRequestData}
	 * @return Created url by the requestData
	 * @throws Exception
	 */
	public String createDirectPayUrl(final AlipayDirectPayRequestData requestData) throws Exception;

	/**
	 *
	 * Send check request with POST method to Alipay
	 *
	 * @param checkRequest
	 *           Check request data needed by alipay
	 * @return Check status {@link AlipayRawPaymentStatus}
	 * @throws Exception
	 */
	public AlipayRawPaymentStatus submitPaymentStatusRequest(final AlipayPaymentStatusRequestData checkRequest) throws Exception;

	/**
	 *
	 * Send close request with POST method to Alipay
	 *
	 * @param closeRequest
	 *           Close request data needed by alipay
	 * @return The result of close request {@link AlipayRawCancelPaymentResult}
	 * @throws Exception
	 */
	public AlipayRawCancelPaymentResult submitCancelPaymentRequest(final AlipayCancelPaymentRequestData closeRequest)
			throws Exception;

	/**
	 *
	 * Create refund url by Alipay refund request data
	 *
	 * @param refundData
	 *           Refund request data needed by alipay {@link AlipayRefundRequestData}
	 * @return Created url by the refundData
	 * @throws Exception
	 */
	public String createRefundUrl(final AlipayRefundRequestData refundData) throws Exception;
}
