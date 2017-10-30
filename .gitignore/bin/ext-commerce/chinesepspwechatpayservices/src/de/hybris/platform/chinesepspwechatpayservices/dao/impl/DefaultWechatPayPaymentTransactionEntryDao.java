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
package de.hybris.platform.chinesepspwechatpayservices.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayPaymentTransactionEntryDao;
import de.hybris.platform.payment.dto.TransactionStatus;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;


public class DefaultWechatPayPaymentTransactionEntryDao extends DefaultGenericDao<WechatpayPaymentTransactionEntryModel>
		implements WechatPayPaymentTransactionEntryDao
{
	private static final Logger LOG = Logger.getLogger(DefaultWechatPayPaymentTransactionDao.class);

	private static final String FIND_ENTRY_BY_TYPE_STATUS_QUERY = "SELECT {" + WechatpayPaymentTransactionEntryModel.PK
			+ "} FROM {" + WechatpayPaymentTransactionEntryModel._TYPECODE + "} " + "WHERE {"
			+ WechatpayPaymentTransactionEntryModel.TRANSACTIONSTATUS + "}=?transactionstatus " + "AND {"
			+ WechatpayPaymentTransactionEntryModel.PAYMENTTRANSACTION + "}=?transaction " + "AND {"
			+ WechatpayPaymentTransactionEntryModel.TYPE + "} in ({{ select {pk} from {" + PaymentTransactionType._TYPECODE
			+ "} where {code}=?type }}) ORDER BY {" + WechatpayPaymentTransactionEntryModel.TIME + "} DESC";

	private static final String TRANSACTION_STATUS = "transactionstatus";
	private static final String TYPE = "type";
	private static final String PAYMENT_TRANSACTION = "transaction";

	public DefaultWechatPayPaymentTransactionEntryDao()
	{
		super(WechatpayPaymentTransactionEntryModel._TYPECODE);
	}

	@Override
	public List<WechatpayPaymentTransactionEntryModel> findPaymentTransactionEntryByTypeAndStatus(
			final PaymentTransactionType capture, final TransactionStatus status,
			final WechatpayPaymentTransactionModel wechatpayPaymentTransaction)
	{
		validateParameterNotNull(capture, "PaymentTransactionType cannot be null");
		validateParameterNotNull(status, "status cannot be null");
		validateParameterNotNull(wechatpayPaymentTransaction, "AlipayPaymentTransaction cannot be null");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_ENTRY_BY_TYPE_STATUS_QUERY);

		query.addQueryParameter(TRANSACTION_STATUS, status.name());
		query.addQueryParameter(TYPE, capture.name());
		query.addQueryParameter(PAYMENT_TRANSACTION, wechatpayPaymentTransaction.getPk().getLongValueAsString());

		final List<WechatpayPaymentTransactionEntryModel> entries = getFlexibleSearchService()
				.<WechatpayPaymentTransactionEntryModel> search(query).getResult();

		if (entries == null)
		{
			LOG.error("There is no Alipay payment transaction entry with type: " + capture + " and status: " + status);
			return Collections.emptyList();
		}
		return entries;
	}
}
