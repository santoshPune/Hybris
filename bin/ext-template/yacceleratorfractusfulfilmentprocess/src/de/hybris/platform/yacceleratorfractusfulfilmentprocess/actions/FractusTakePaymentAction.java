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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.actions;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderprocessing.model.FractusOrderProcessModel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class FractusTakePaymentAction extends AbstractSimpleDecisionAction<FractusOrderProcessModel>
{
	private static final Logger LOG = Logger.getLogger(FractusTakePaymentAction.class);

	protected static final String TAKEPAYMENT_REJECT_PRODUCT_CODE = "yacceleratorfractusfulfilmentprocess.takepayment.reject.product.code";

	private ConfigurationService configurationService;

	@Override
	public Transition executeAction(final FractusOrderProcessModel process)
	{
		final OrderModel order = process.getOrder();

		if (mockRejectPayment(order))
		{
			LOG.info("Payment captured for the given order and the status have been updated to " + OrderStatus.PAYMENT_NOT_CAPTURED);
			setOrderStatus(order, OrderStatus.PAYMENT_NOT_CAPTURED);
			return Transition.NOK;
		}
		else
		{
			LOG.info("Payment captured for the given order and the status have been updated to " + OrderStatus.PAYMENT_CAPTURED);
			setOrderStatus(order, OrderStatus.PAYMENT_CAPTURED);
			return Transition.OK;
		}
	}

	/**
	 * Mock the reject payment by given product code.
	 *
	 * @param order
	 * @return true if rejected payment
	 */
	protected boolean mockRejectPayment(final OrderModel order)
	{
		final String takePaymentRejectProductCode = getConfigurationService().getConfiguration()
				.getString(TAKEPAYMENT_REJECT_PRODUCT_CODE, StringUtils.EMPTY);

		if (StringUtils.isNotBlank(takePaymentRejectProductCode) && CollectionUtils.isNotEmpty(order.getEntries()))
		{
			for (final AbstractOrderEntryModel entryModel : order.getEntries())
			{
				if (entryModel.getProduct() != null && entryModel.getProduct().getCode() != null
						&& takePaymentRejectProductCode.equals(entryModel.getProduct().getCode()))
				{
					return true;
				}
			}
		}

		return false;
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
}
