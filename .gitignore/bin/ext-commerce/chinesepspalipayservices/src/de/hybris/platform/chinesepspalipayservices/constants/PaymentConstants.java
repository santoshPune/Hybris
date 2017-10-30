/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspalipayservices.constants;

import de.hybris.platform.payment.dto.TransactionStatus;

import java.util.HashMap;
import java.util.Map;


/**
 * Global class for all alipay constants.
 */
@SuppressWarnings("PMD")
public interface PaymentConstants
{
	/**
	 * Basic request constants
	 */

	interface Basic
	{
		String EXTENSIONNAME = "chinesepspalipayservices";

		String BANK_PAY_METHOD = "bankPay";

		String INSTANT_PAY_METHOD = "directPay";

		String EXPRESS_PAY_METHOD = "expressGateway";

		String SEC_ID = "0001";

		String INPUT_CHARSET = "utf-8";

		String SIGN_TYPE = "MD5";

		String DEFAULT_LOGIN = "Y";

		String PAYMENT_PROVIDER = "Alipay";

		String MOBILE_FORMAT = "xml";

		String MOBILE_REQUEST_VERSION = "2.0";

		String MOBILE_REQUEST_TYPE = "POST";

		String RESPONSE_ROOT = "alipay";

		String RESPONSE_ATTR_PARAM = "param";

		String REFUND_BATCH_NUM = "1";

		interface PaymentType
		{
			String BUY_PRODUCT = "1";
			String DONATE = "4";
		}
	}

	interface ErrorHandler
	{
		String OUT_TRADE_NO = "out_trade_no";
		String ERROR_CODE = "error_code";
	}

	/**
	 * Controller constants
	 */
	interface Controller
	{
		String _Prefix = "checkout/multi/summary" + "/alipay/";
		String _Suffix = "Controller";

		String DIRECT_AND_EXPRESS_RETURN_URL = _Prefix + "pspsyncresponse/" + "return" + _Suffix;
		String DIRECT_AND_EXPRESS_NOTIFY_URL = _Prefix + "pspasynresponse/" + "notify" + _Suffix;
		String ERROR_NOTIFY_URL = _Prefix + "pspasynresponse/" + "error" + _Suffix;
		String WAP_RETURN_URL = _Prefix + "mobile/return" + _Suffix;
		String WAP_NOTIFY_URL = _Prefix + "mobile/notify" + _Suffix;
		String GET_REFUND_URL = _Prefix + "refund" + _Suffix;
		String REFUND_NOTIFY_URL = _Prefix + "pspasynresponse/" + "refundnotify" + _Suffix;
	}


	interface HTTP
	{

		static final String METHOD_POST = "POST";
		static final String METHOD_GET = "GET";

	}

	interface TransactionStatusMap
	{
		static final Map<String, TransactionStatus> AlipayToHybris = new HashMap()
		{
			{
				put("TRADE_SUCCESS", TransactionStatus.ACCEPTED);
				put("TRADE_FINISHED", TransactionStatus.ACCEPTED);
				put("WAIT_SELLER_SEND_GOODS", TransactionStatus.ACCEPTED);
				put("WAIT_BUYER_CONFIRM_GOODS", TransactionStatus.ACCEPTED);
				put("BUYER_PRE_AUTH", TransactionStatus.ACCEPTED);
				put("TRADE_PENDING", TransactionStatus.REVIEW);
				put("WAIT_SYS_CONFIRM_PAY", TransactionStatus.REVIEW);
				put("WAIT_SYS_PAY_SELLER", TransactionStatus.REVIEW);
				put("TRADE_CLOSED", TransactionStatus.REJECTED);
				put("TRADE_REFUSE", TransactionStatus.REJECTED);
				put("TRADE_REFUSE_DEALING", TransactionStatus.REJECTED);
				put("TRADE_CANCEL", TransactionStatus.REJECTED);
				put("WAIT_BUYER_PAY", null);
			}
		};
	}


}
