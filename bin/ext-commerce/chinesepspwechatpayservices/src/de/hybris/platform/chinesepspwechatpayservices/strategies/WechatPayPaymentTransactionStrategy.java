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
package de.hybris.platform.chinesepspwechatpayservices.strategies;


import de.hybris.platform.chinesepspwechatpayservices.data.WechatPayQueryResult;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;

import java.util.Optional;


/**
 * Methods to change payment transaction and payment transaction entries by given params
 */
public interface WechatPayPaymentTransactionStrategy
{

	/**
	 * Save new transaction with entry for some order once new direct_pay request is issued.
	 *
	 * @param orderModel
	 *           order launching direct_pay
	 */
	void createForNewRequest(OrderModel orderModel);


	/**
	 * Update wechatPayPaymentTransaction and entry once notify data from wechat is received.
	 *
	 * @param orderModel
	 *           Order handled by the notify data {@link OrderModel}
	 * @param wechatPayNotifyResponseData
	 *           Notify data from wechatpay {@link WechatRawDirectPayNotification}
	 */
	void updateForNotification(OrderModel orderModel, WechatRawDirectPayNotification wechatPayNotifyResponseData);

	/**
	 *
	 * Save WechatpayPaymentTransactionEntry once payment status check (WechatPay's check trade) is completed
	 *
	 * @param orderModel
	 *           order launching check trade
	 * @param wechatPayQueryResult
	 *           Data needed for launching check trade
	 * @return WechatpayPaymentTransactionEntryModel The PaymentTransactionEntry which is updated by
	 *         checkTradeResponseData
	 *
	 */
	Optional<WechatpayPaymentTransactionEntryModel> saveForStatusCheck(OrderModel orderModel,
			WechatPayQueryResult wechatPayQueryResult);


}
