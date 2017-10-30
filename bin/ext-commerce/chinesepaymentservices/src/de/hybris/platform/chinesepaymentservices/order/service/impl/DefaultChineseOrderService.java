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
package de.hybris.platform.chinesepaymentservices.order.service.impl;

import de.hybris.platform.chinesepaymentservices.checkout.ChineseCheckoutService;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.order.dao.ChineseOrderDao;
import de.hybris.platform.chinesepaymentservices.order.service.ChineseOrderService;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.enums.PaymentStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.impl.DefaultOrderService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


public class DefaultChineseOrderService extends DefaultOrderService implements ChineseOrderService
{
	private ChineseOrderDao chineseOrderDao;
	private ConfigurationService configurationService;
	private ChineseCheckoutService chineseCheckoutService;
	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;
	private ModelService modelService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void cancelOrder(final String code)
	{
		final AbstractOrderModel invalidOrder = chineseOrderDao.findOrderByCode(code);
		if (invalidOrder != null)
		{
			final OrderModel order = (OrderModel) invalidOrder;
			final OrderStatus status = order.getStatus();

			if (!OrderStatus.CANCELLED.equals(status) && !OrderStatus.CANCELLING.equals(status))
			{
				chineseCheckoutService.releaseStock(code);
				invalidOrder.setStatus(OrderStatus.CANCELLED);
				final String paymentService = invalidOrder.getChinesePaymentInfo().getPaymentProvider() + "PaymentService";
				final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy.getPaymentService(paymentService);
				chinesePaymentService.cancelPayment(invalidOrder.getCode());
				modelService.save(invalidOrder);
			}
		}
	}

	@Override
	public void markOrderAsPaid(final String orderCode)
	{
		final AbstractOrderModel order = chineseOrderDao.findOrderByCode(orderCode);
		if (order != null && PaymentStatus.PAID.equals(order.getPaymentStatus()))
		{
			order.setStatus(OrderStatus.PAYMENT_CAPTURED);
			modelService.save(order);
			modelService.refresh(order);
		}
	}

	@Override
	public void updateOrderForRefund(OrderModel orderModel, boolean refundSucceed)
	{
		if (refundSucceed)
		{
			orderModel.setStatus(OrderStatus.CANCELLED);
			modelService.save(orderModel);
			modelService.refresh(orderModel);
		}
	}

	protected ChineseOrderDao getChineseOrderDao()
	{
		return chineseOrderDao;
	}

	@Required
	public void setChineseOrderDao(final ChineseOrderDao chineseOrderDao)
	{
		this.chineseOrderDao = chineseOrderDao;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected ChineseCheckoutService getChineseCheckoutService()
	{
		return chineseCheckoutService;
	}

	@Required
	public void setChineseCheckoutService(final ChineseCheckoutService chineseCheckoutService)
	{
		this.chineseCheckoutService = chineseCheckoutService;
	}

	protected ChinesePaymentServicesStrategy getChinesePaymentServicesStrategy()
	{
		return chinesePaymentServicesStrategy;
	}

	@Required
	public void setChinesePaymentServicesStrategy(final ChinesePaymentServicesStrategy chinesePaymentServicesStrategy)
	{
		this.chinesePaymentServicesStrategy = chinesePaymentServicesStrategy;
	}

	@Override
	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	@Override
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}



}
