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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.strategies.impl;

import de.hybris.platform.commerceservices.order.CommercePlaceOrderStrategy;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusOrderService;

import org.springframework.beans.factory.annotation.Required;


/**
 * The class of FractusCommercePlaceOrderStrategy.
 */
public class FractusCommercePlaceOrderStrategy implements CommercePlaceOrderStrategy
{

	private FractusOrderService orderService;

	private CommercePlaceOrderStrategy commercePlaceOrderStrategy;

	@Override
	public CommerceOrderResult placeOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		if (isFractusOrder(parameter))
		{
			return placeFractusOrder(parameter);
		}
		else
		{
			return getCommercePlaceOrderStrategy().placeOrder(parameter);
		}
	}

	/**
	 * The fractus order is created from datahub import, so there is no cart exists. The fractus order status initial is
	 * CREATED.
	 */
	protected boolean isFractusOrder(final CommerceCheckoutParameter parameter)
	{
		return parameter.getOrder() != null && parameter.getCart() == null
				&& getOrderService().isFractusOrder(parameter.getOrder());
	}

	protected CommerceOrderResult placeFractusOrder(final CommerceCheckoutParameter parameter)
	{
		final CommerceOrderResult result = new CommerceOrderResult();

		getOrderService().submitOrder(parameter.getOrder());

		result.setOrder(parameter.getOrder());

		return result;
	}

	protected FractusOrderService getOrderService()
	{
		return orderService;
	}

	@Required
	public void setOrderService(final FractusOrderService orderService)
	{
		this.orderService = orderService;
	}

	protected CommercePlaceOrderStrategy getCommercePlaceOrderStrategy()
	{
		return commercePlaceOrderStrategy;
	}

	@Required
	public void setCommercePlaceOrderStrategy(final CommercePlaceOrderStrategy commercePlaceOrderStrategy)
	{
		this.commercePlaceOrderStrategy = commercePlaceOrderStrategy;
	}
}
