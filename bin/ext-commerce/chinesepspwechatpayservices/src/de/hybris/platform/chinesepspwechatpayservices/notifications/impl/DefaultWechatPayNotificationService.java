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
package de.hybris.platform.chinesepspwechatpayservices.notifications.impl;

import de.hybris.platform.chinesepaymentservices.order.service.ChineseOrderService;
import de.hybris.platform.chinesepspwechatpayservices.constants.WechatPaymentConstants;
import de.hybris.platform.chinesepspwechatpayservices.dao.WechatPayOrderDao;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;
import de.hybris.platform.chinesepspwechatpayservices.notifications.WechatPayNotificationService;
import de.hybris.platform.chinesepspwechatpayservices.strategies.WechatPayPaymentTransactionStrategy;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.servicelayer.model.ModelService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class DefaultWechatPayNotificationService implements WechatPayNotificationService
{
	private ModelService modelService;

	private WechatPayPaymentTransactionStrategy wechatPayPaymentTransactionStrategy;

	private WechatPayOrderDao wechatPayOrderDao;

	private ChineseOrderService chineseOrderService;

	private static final Logger LOG = Logger.getLogger(DefaultWechatPayNotificationService.class.getName());

	@Override
	public void handleWechatPayPaymentResponse(final WechatRawDirectPayNotification wechatpayNotification)
	{
		getWechatPayOrderDao().findOrderByCode(wechatpayNotification.getOutTradeNo()).ifPresent(
				orderModel -> {
					LOG.info("Handle Response for order: " + wechatpayNotification.getOutTradeNo());
					getWechatPayPaymentTransactionStrategy().updateForNotification(orderModel, wechatpayNotification);
					if (WechatPaymentConstants.Notification.RETURN_SUCCESS.equals(wechatpayNotification.getReturnCode())
							&& WechatPaymentConstants.Notification.RESULT_SUCCESS.equals(wechatpayNotification.getResultCode()))
					{
						orderModel.setPaymentStatus(PaymentStatus.PAID);
					}
					else
					{
						orderModel.setPaymentStatus(PaymentStatus.NOTPAID);
					}
					getModelService().save(orderModel);
					getChineseOrderService().markOrderAsPaid(orderModel.getCode());
				});
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	protected WechatPayPaymentTransactionStrategy getWechatPayPaymentTransactionStrategy()
	{
		return wechatPayPaymentTransactionStrategy;
	}

	@Required
	public void setWechatPayPaymentTransactionStrategy(
			final WechatPayPaymentTransactionStrategy wechatPayPaymentTransactionStrategy)
	{
		this.wechatPayPaymentTransactionStrategy = wechatPayPaymentTransactionStrategy;
	}

	protected WechatPayOrderDao getWechatPayOrderDao()
	{
		return wechatPayOrderDao;
	}

	@Required
	public void setWechatPayOrderDao(final WechatPayOrderDao wechatPayOrderDao)
	{
		this.wechatPayOrderDao = wechatPayOrderDao;
	}

	protected ChineseOrderService getChineseOrderService()
	{
		return chineseOrderService;
	}

	@Required
	public void setChineseOrderService(final ChineseOrderService chineseOrderService)
	{
		this.chineseOrderService = chineseOrderService;
	}


}
