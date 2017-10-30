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


import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayErrorInfo;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawDirectPayNotification;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundRequestData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.AlipayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;

import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Methods to change payment transaction and payment transaction entries by given params
 */
public interface AlipayPaymentTransactionStrategy
{

	/**
	 * Save new transaction with entry for some order once new direct_pay request is issued.
	 *
	 * @param orderModel
	 *           order launching direct_pay
	 * @param requestUrl
	 *           accessible URL
	 */
	void createForNewRequest(OrderModel orderModel, String requestUrl);


	/**
	 *
	 * Save alipayPaymentTransactionEntry once payment status check (Alipay's check trade) is completed
	 *
	 * @param orderModel
	 *           order launching check trade
	 * @param checkTradeResponseData
	 *           Data needed for launching check trade
	 * @return AlipayPaymentTransactionEntryModel The PaymentTransactionEntry which is updated by checkTradeResponseData
	 *
	 */
	AlipayPaymentTransactionEntryModel saveForStatusCheck(OrderModel orderModel, AlipayRawPaymentStatus checkTradeResponseData);

	/**
	 * Update alipayPaymentTransaction and entry once notify data from alipay is received.
	 *
	 * @param orderModel
	 *           Order handled by the notify data {@link OrderModel}
	 * @param directPayNotifyResponseData
	 *           Notify data from alipay {@link AlipayRawDirectPayNotification}
	 */
	void updateForNotification(OrderModel orderModel, AlipayRawDirectPayNotification directPayNotifyResponseData);

	/**
	 * Update alipayPaymentTransaction and entry once cancel trade is launched
	 *
	 * @param orderModel
	 *           order launching cancel trade
	 * @param alipayRawCancelPaymentResult
	 *           Response data from alipay after canceling trade {@link AlipayRawCancelPaymentResult}
	 */
	void updateForCancelPayment(OrderModel orderModel, final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult);

	/**
	 * Update alipayPaymentTransaction and entry once error data from alipay is received.
	 *
	 * @param orderModel
	 *           Order handled by the error data
	 * @param aipayRawDirectPayErrorInfo
	 *           Error data from alipay {@link AlipayRawDirectPayErrorInfo}
	 */
	void updateForError(OrderModel orderModel, AlipayRawDirectPayErrorInfo aipayRawDirectPayErrorInfo);

	/**
	 * Whether the alipayPaymentTransaction under a order which has CAPTURED entry exists
	 *
	 * @param orderModel
	 *           Order needed to check
	 *
	 * @param status
	 *           transaction status {@link TransactionStatus}
	 * @return if false, the transaction exists.Otherwise,not exist
	 */
	boolean checkCaptureTransactionEntry(final OrderModel orderModel, final TransactionStatus status);

	/**
	 * Find payment transaction entry with given params
	 *
	 * @param orderModel
	 *           Order needed to check
	 * @param status
	 *           transaction status {@link TransactionStatus}
	 * @param paymentTransactionType
	 *           payment transaction status {@link PaymentTransactionType}
	 * @return An Optional describing the result of AlipayPaymentTransactionEntryModel if a value is present, otherwise
	 *         an empty Optional
	 */
	Optional<AlipayPaymentTransactionEntryModel> getPaymentTransactionEntry(final OrderModel orderModel,
			final TransactionStatus status, final PaymentTransactionType paymentTransactionType);

	/**
	 * Find payment transaction in which exists at least one payment transaction entry whose transaction type is CAPTURE
	 *
	 * @param orderModel
	 *           Order needed to check
	 * @param status
	 *           transaction status {@link TransactionStatus}
	 * @return An Optional describing the result of AlipayPaymentTransactionEntryModel if a value is present, otherwise
	 *         an empty Optional
	 */
	Optional<AlipayPaymentTransactionModel> getPaymentTransactionWithCaptureEntry(final OrderModel orderModel,
			final TransactionStatus status);


	/**
	 * Update transaction for refund notification
	 *
	 * @param alipayRefundDatas
	 *           refund data {@link AlipayRefundData}}
	 * @return refund status map of every order
	 */
	Map<OrderModel, Boolean> updateForRefundNotification(final List<AlipayRefundData> alipayRefundData);

	/**
	 * Create a transaction entry when user create a refund request
	 *
	 * @param orderModel
	 *           Order needed to refund
	 * @param alipayRefundRequestData
	 *           Refund request data created send to Alipay
	 */
	void updateTransactionForRefundRequest(OrderModel orderModel, AlipayRefundRequestData alipayRefundRequestData);
}
