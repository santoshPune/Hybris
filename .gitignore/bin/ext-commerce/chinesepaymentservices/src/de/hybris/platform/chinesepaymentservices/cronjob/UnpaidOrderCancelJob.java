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
package de.hybris.platform.chinesepaymentservices.cronjob;

import de.hybris.platform.chinesepaymentservices.checkout.ChineseCheckoutService;
import de.hybris.platform.chinesepaymentservices.checkout.strategies.ChinesePaymentServicesStrategy;
import de.hybris.platform.chinesepaymentservices.order.dao.ChineseOrderDao;
import de.hybris.platform.chinesepaymentservices.payment.ChinesePaymentService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


public class UnpaidOrderCancelJob extends AbstractJobPerformable<CronJobModel>
{
	private ChineseOrderDao chineseOrderDao;
	private ConfigurationService configurationService;
	private ChineseCheckoutService chineseCheckoutService;
	private ChinesePaymentServicesStrategy chinesePaymentServicesStrategy;
	private ModelService modelService;

	@Override
	public PerformResult perform(final CronJobModel arg0)
	{
		final double validTime = configurationService.getConfiguration().getDouble("order.validtimeinhours");
		final long vaildTimeInMills = (long) validTime * 3600 * 1000;
		final List<AbstractOrderModel> notPaidOrders = chineseOrderDao.findUnpaidOrders(vaildTimeInMills);
		for (final AbstractOrderModel notPaidOrder : notPaidOrders)
		{
			chineseCheckoutService.releaseStock(notPaidOrder.getCode());
			notPaidOrder.setStatus(OrderStatus.CANCELLED);
			final String paymentService = notPaidOrder.getChinesePaymentInfo().getPaymentProvider() + "PaymentService";
			final ChinesePaymentService chinesePaymentService = chinesePaymentServicesStrategy.getPaymentService(paymentService);
			chinesePaymentService.cancelPayment(notPaidOrder.getCode());
			modelService.save(notPaidOrder);
		}
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
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
