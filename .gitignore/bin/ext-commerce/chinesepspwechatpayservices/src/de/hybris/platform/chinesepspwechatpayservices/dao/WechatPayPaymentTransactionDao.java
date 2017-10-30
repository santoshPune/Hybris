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

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;

import java.util.Optional;



/**
 * Provide method to find Wechatpay Payment Transaction with given params
 */
public interface WechatPayPaymentTransactionDao extends GenericDao<WechatpayPaymentTransactionModel>
{
	/**
	 * Get WechatpayPaymentTransaction of the given order which satisfy these conditions: 1. There is only one entry with
	 * type Request in this transaction. 2. This entry is the latest among all transactions' entries.
	 *
	 * @param orderModel
	 *           The order contains the WechatpayPaymentTransactionModel
	 * @return WechatpayPaymentTransactionModel if found and an empty Optional otherwise
	 *
	 */
	Optional<WechatpayPaymentTransactionModel> findTransactionByLatestRequestEntry(OrderModel orderModel, boolean limit);

	/**
	 * Get WechatpayPaymentTransaction by WechatPayCode
	 *
	 * @param WechatPayCode
	 *           The WechatPayCode of the wanted WechatPayPaymentTransactionModel
	 * @return WechatpayPaymentTransactionModel if found and an empty Optional otherwise
	 *
	 */
	Optional<WechatpayPaymentTransactionModel> findTransactionByWechatPayCode(String wechatPayCode);
}
