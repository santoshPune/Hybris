/*
* [y] hybris Platform
*
* Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
* All rights reserved.
*
* This software is the confidential and proprietary information of SAP
* ("Confidential Information"). You shall not disclose such Confidential
* Information and shall use it only in accordance with the terms of the
* license agreement you entered into with SAP.
*
*/
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.impl;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.impl.DefaultOrderService;
import de.hybris.platform.order.strategies.SubmitOrderStrategy;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusCheckOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusOrderService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.dao.YaasOrderDao;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link FractusOrderService}.
 */
public class DefaultFractusOrderService extends DefaultOrderService implements FractusOrderService
{
	private YaasOrderDao yaasOrderDao;
	private FractusCheckOrderService checkOrderService;
	private Map<String, SubmitOrderStrategy> submitOrderStrategies;
	private CommerceCheckoutService commerceCheckoutService;
	private static final String FRACTUS_ORDER_STRATEGY_KEY = "fractus";
	private static final String ECP_ORDER_STRATEGY_KEY = "ecp";

	private static final Logger LOG = Logger.getLogger(DefaultFractusOrderService.class);

	public YaasOrderDao getYaasOrderDao()
	{
		return yaasOrderDao;
	}

	@Required
	public void setYaasOrderDao(final YaasOrderDao yaasOrderDao)
	{
		this.yaasOrderDao = yaasOrderDao;
	}

	public FractusCheckOrderService getCheckOrderService()
	{
		return checkOrderService;
	}

	@Required
	public void setCheckOrderService(final FractusCheckOrderService checkOrderService)
	{
		this.checkOrderService = checkOrderService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<OrderModel> getOrdersForFulfilment()
	{
		final List<OrderModel> ordersWithCreatedStatus = getYaasOrderDao().findOrdersForFulFilment();
		final List<OrderModel> ordersForFulfillment = new ArrayList<>();
		for (final OrderModel order : ordersWithCreatedStatus)
		{
			final boolean hasFractusOrderFulfillmentProcess = hasFractusFulfilmentProcess(order);
			if (!hasFractusOrderFulfillmentProcess && getCheckOrderService().check(order))
			{
				ordersForFulfillment.add(order);
			}
		}
		return ordersForFulfillment;
	}

	/**
	 * @param order
	 * @return
	 */
	protected boolean hasFractusFulfilmentProcess(final OrderModel order)
	{
		//checking if order already has fractus order process associated
		boolean hasFractusOrderFulfillmentProcess = false;
		final Collection<OrderProcessModel> orderProcesses = order.getOrderProcess();
		for (final OrderProcessModel orderProcess : orderProcesses)
		{
			if (orderProcess instanceof FractusOrderProcessModel)
			{
				hasFractusOrderFulfillmentProcess = true;
				break;
			}
		}
		return hasFractusOrderFulfillmentProcess;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void startFulfilmentProcess(final OrderModel order)
	{
		final BaseStoreModel store = order.getStore();

		if (store == null)
		{
			LOG.warn("Unable to start fulfilment process for order [" + order.getCode()
					+ "]. Store not set on Order and no current base store defined in session.");
		}
		else
		{
			final CommerceCheckoutParameter parameter = new CommerceCheckoutParameter();
			parameter.setOrder(order);
			try
			{
				getCommerceCheckoutService().placeOrder(parameter);
			}
			catch (final InvalidCartException e)
			{
				LOG.error(e.getMessage(), e);
			}

		}
	}

	@Override
	public void submitOrder(final OrderModel order)
	{
		if (isFractusOrder(order))
		{
			getSubmitOrderStrategies().get(FRACTUS_ORDER_STRATEGY_KEY).submitOrder(order);
		}
		else
		{
			getSubmitOrderStrategies().get(ECP_ORDER_STRATEGY_KEY).submitOrder(order);
		}

	}

	@Override
	public boolean isFractusOrder(final OrderModel order)
	{
		return SalesApplication.YAAS.equals(order.getSalesApplication());
	}

	protected Map<String, SubmitOrderStrategy> getSubmitOrderStrategies()
	{
		return submitOrderStrategies;
	}

	@Required
	public void setSubmitOrderStrategies(final Map<String, SubmitOrderStrategy> submitOrderStrategies)
	{
		this.submitOrderStrategies = submitOrderStrategies;
	}

	protected CommerceCheckoutService getCommerceCheckoutService()
	{
		return commerceCheckoutService;
	}

	@Required
	public void setCommerceCheckoutService(final CommerceCheckoutService commerceCheckoutService)
	{
		this.commerceCheckoutService = commerceCheckoutService;
	}
}
