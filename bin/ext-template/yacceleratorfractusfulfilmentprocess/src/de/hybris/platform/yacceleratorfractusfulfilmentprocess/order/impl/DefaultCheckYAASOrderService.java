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

import de.hybris.platform.commerceservices.i18n.LanguageResolver;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.yacceleratorfractusfulfilmentprocess.order.FractusCheckOrderService;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 * Implementation to check if yaas order is valid and has all required attributes.
 *
 */
public class DefaultCheckYAASOrderService implements FractusCheckOrderService
{
	private static final Logger LOG = Logger.getLogger(DefaultCheckYAASOrderService.class);

	@Resource(name = "languageResolver")
	private LanguageResolver languageResolver;

	@Override
	public boolean check(final OrderModel order)
	{
		LOG.info("YAAS Order validation service is fired to validate the requested order");

		if (order.getEntries().isEmpty())
		{
			// Order must have some lines
			return false;
		}
		else
		{
			// Order delivery options must be valid
			return checkDeliveryOptions(order);
		}
	}

	protected boolean checkDeliveryOptions(final OrderModel order)
	{

		//It is the temporary code used to populate the address details from the user .. it should be removed once
		// the ticket HIPPO-217 is fixed.
		setAddress(order);

		checkLanguage(order);


		if (order.getDeliveryMode() == null)
		{
			// Order must have an overall delivery mode
			return false;
		}

		if (order.getDeliveryAddress() == null)
		{
			for (final AbstractOrderEntryModel entry : order.getEntries())
			{
				if (entry.getDeliveryPointOfService() == null && entry.getDeliveryAddress() == null)
				{
					// Order and Entry have no delivery address and some entries are not for pickup
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param order
	 */
	protected void setAddress(final OrderModel order)
	{
		if (order.getDeliveryAddress() == null || order.getPaymentAddress() == null)
		{
			for (final AddressModel address : ((AbstractOrderModel) order).getUser().getAddresses())
			{

				if (address.getShippingAddress().booleanValue())
				{
					order.setDeliveryAddress(address);
				}
				else if (address.getBillingAddress().booleanValue())
				{
					order.setPaymentAddress(address);
				}
			}
		}
	}

	/**
	 * @param order
	 */
	protected void checkLanguage(final OrderModel order)
	{
		//It requires Language from the order that needs to fix part of HIPPO-217
		if (order.getLanguage() == null)
		{
			final LanguageModel languageModel = languageResolver.getLanguage(order.getUser().getSessionLanguage().getIsocode());

			order.setLanguage(languageModel);
		}
	}
}
