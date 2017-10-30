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
package de.hybris.platform.chinesepspwechatpayservices.dao;

import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionModel;

import java.util.List;


/**
 * Provide method to find Wechatpay Payment Transaction Entry
 */
public interface WechatPayPaymentTransactionEntryDao
{

	/**
	 * Get the list of WechatPayPaymentTransactionEntries by the given PaymentTransactionEntry type, transaction status
	 * and WechatPayPaymentTransaction.
	 *
	 * @param capture
	 *           PaymentTransactionEntry type: CAPTURE,CANCEL and so on
	 * @param status
	 *           PaymentTransactionStatus: ACCEPTED,REJECTED and so on
	 * @param wechatpayPaymentTransaction
	 *           Wechatpay Payment Transaction
	 * @return List of WechatPayPaymentTransactionEntries found
	 */
	public List<WechatpayPaymentTransactionEntryModel> findPaymentTransactionEntryByTypeAndStatus(
			final PaymentTransactionType capture, final TransactionStatus status,
			final WechatpayPaymentTransactionModel wechatpayPaymentTransaction);

}
