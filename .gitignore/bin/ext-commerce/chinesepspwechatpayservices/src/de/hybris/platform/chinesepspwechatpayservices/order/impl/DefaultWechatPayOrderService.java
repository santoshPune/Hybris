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
package de.hybris.platform.chinesepspwechatpayservices.order.impl;

import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayOrderDao;
import de.hybris.platform.chinesepspwechatpayservices.order.WechatPayOrderService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.impl.DefaultOrderService;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Required;


public class DefaultWechatPayOrderService extends DefaultOrderService implements WechatPayOrderService
{
	private WechatPayOrderDao wechatPayOrderDao;

	@Override
	public Optional<OrderModel> getOrderByCode(final String code)
	{
		return wechatPayOrderDao.findOrderByCode(code);
	}

	@Required
	public void setWechatPayOrderDao(final WechatPayOrderDao alipayOrderDao)
	{
		this.wechatPayOrderDao = alipayOrderDao;
	}

	protected WechatPayOrderDao getWechatPayOrderDao()
	{
		return wechatPayOrderDao;
	}


}
