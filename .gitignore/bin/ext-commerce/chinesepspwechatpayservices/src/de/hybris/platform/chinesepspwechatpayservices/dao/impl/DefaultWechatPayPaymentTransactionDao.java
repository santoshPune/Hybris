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

import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayPaymentTransactionDao;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.payment.enums.PaymentTransactionType;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionEntryModel;
import de.hybris.platform.payment.model.WechatpayPaymentTransactionModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.List;
import java.util.Optional;

import org.apache.log4j.Logger;



public class DefaultWechatPayPaymentTransactionDao extends DefaultGenericDao<WechatpayPaymentTransactionModel>
		implements WechatPayPaymentTransactionDao
{
	private static final Logger LOG = Logger.getLogger(DefaultWechatPayPaymentTransactionDao.class);

	public DefaultWechatPayPaymentTransactionDao()
	{
		super(WechatpayPaymentTransactionModel._TYPECODE);
	}

	@Override
	public Optional<WechatpayPaymentTransactionModel> findTransactionByLatestRequestEntry(final OrderModel orderModel,
			final boolean limit)
	{
		final String order = orderModel.getPk().getLongValueAsString();

		final StringBuilder queryString = new StringBuilder();
		queryString.append("select {" + WechatpayPaymentTransactionModel.PK + "} from {"
				+ WechatpayPaymentTransactionModel._TYPECODE + "} where {" + WechatpayPaymentTransactionModel.PK + "} in"
				+ " ({{ select * from" + "({{ " + "select {" + PaymentTransactionModel._TYPECODE + "} from {"
				+ WechatpayPaymentTransactionEntryModel._TYPECODE + " as entry join " + PaymentTransactionType._TYPECODE
				+ " as type on {entry.type}={type.pk} } where {type.code}='WECHAT_REQUEST'" + " and {entry.paymenttransaction} in ({{"
				+ "select {" + WechatpayPaymentTransactionModel.PK + "} from {" + WechatpayPaymentTransactionModel._TYPECODE
				+ " as t} where {t.order}=?order" + "}} )  order by {entry.time} desc limit 1 }}) as temp }})");

		if (limit)
		{
			queryString.append(" and {pk} in ({{select {" + PaymentTransactionModel._TYPECODE + "} from {"
					+ WechatpayPaymentTransactionEntryModel._TYPECODE + "} group by {" + PaymentTransactionModel._TYPECODE
					+ "}  having  count(*)=1 }})");
		}
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString());

		query.addQueryParameter("order", order);

		try
		{
			final WechatpayPaymentTransactionModel result = getFlexibleSearchService().searchUnique(query);
			return Optional.ofNullable(result);
		}
		catch (final ModelNotFoundException e)
		{
			LOG.info("No result for the given query for :" + order);
		}
		return Optional.empty();

	}


	@Override
	public Optional<WechatpayPaymentTransactionModel> findTransactionByWechatPayCode(final String wechatPayCode)
	{
		final String queryString = "select {pk} from {" + WechatpayPaymentTransactionModel._TYPECODE + "} where {"
				+ WechatpayPaymentTransactionModel.WECHATPAYCODE + "}=?wechatPayCode";
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString);

		query.addQueryParameter("wechatPayCode", wechatPayCode);

		final List<WechatpayPaymentTransactionModel> searchResult = getFlexibleSearchService()
				.<WechatpayPaymentTransactionModel> search(query).getResult();
		if (searchResult.size() > 0)
		{
			return Optional.ofNullable(searchResult.get(0));
		}

		return Optional.empty();

	}


}
