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
package de.hybris.platform.chinesepspalipayservices.dao;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.model.AlipayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;


/**
 * Provide method to find Alipay Payment Transaction with given params
 */
public interface AlipayPaymentTransactionDao extends GenericDao<AlipayPaymentTransactionModel>
{
	/**
	 * Get AlipayPaymentTransaction of the given order which satisfy these conditions: 1. There is only one entry with
	 * type Request in this transaction. 2. This entry is the latest among all transactions' entries.
	 *
	 * @param orderModel
	 *           The order contains the AlipayPaymentTransactionModel
	 * @return AlipayPaymentTransactionModel if found and null otherwise
	 *
	 */
	AlipayPaymentTransactionModel findTransactionByLatestRequestEntry(OrderModel orderModel, boolean limit);

	/**
	 * Get AlipayPaymentTransaction by alipayCode
	 *
	 * @param alipayCode
	 *           The alipayCode of the wanted AlipayPaymentTransactionModel
	 * @return AlipayPaymentTransactionModel if found and null otherwise
	 *
	 */
	AlipayPaymentTransactionModel findTransactionByAlipayCode(String alipayCode);
}
