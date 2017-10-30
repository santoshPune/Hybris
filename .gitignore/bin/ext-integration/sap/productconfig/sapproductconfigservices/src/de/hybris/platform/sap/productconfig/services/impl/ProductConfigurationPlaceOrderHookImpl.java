/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.services.impl;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.sap.productconfig.services.SessionAccessService;
import de.hybris.platform.sap.productconfig.services.intf.ProductConfigurationService;

import org.apache.log4j.Logger;


/**
 * Releases configuration sessions on placing an order
 *
 */
public class ProductConfigurationPlaceOrderHookImpl implements CommercePlaceOrderMethodHook
{

	private static final Logger LOG = Logger.getLogger(ProductConfigurationPlaceOrderHookImpl.class);



	private ProductConfigurationService productConfigurationService;
	private SessionAccessService sessionAccessService;

	/**
	 * @return the sessionAccessService
	 */
	public SessionAccessService getSessionAccessService()
	{
		return sessionAccessService;
	}

	/**
	 * @param sessionAccessService
	 *           the sessionAccessService to set
	 */
	public void setSessionAccessService(final SessionAccessService sessionAccessService)
	{
		this.sessionAccessService = sessionAccessService;
	}

	/**
	 * @param productConfigurationService
	 *           the productConfigurationService to set
	 */
	public void setProductConfigurationService(final ProductConfigurationService productConfigurationService)
	{
		this.productConfigurationService = productConfigurationService;
	}

	@Override
	public void afterPlaceOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult orderModel)
			throws InvalidCartException
	{

		for (final AbstractOrderEntryModel cartEntry : parameter.getCart().getEntries())
		{
			final String configId = sessionAccessService.getConfigIdForCartEntry(cartEntry.getPk().toString());
			if (configId != null && (!configId.isEmpty()))
			{
				productConfigurationService.releaseSession(configId);
				sessionAccessService.removeSessionArtifactsForCartEntry(cartEntry.getPk().toString(), cartEntry.getProduct()
						.getCode());

				if (LOG.isDebugEnabled())
				{
					LOG.debug("Released configuration for session: " + configId);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook#beforePlaceOrder(de.hybris.platform
	 * .commerceservices.service.data.CommerceCheckoutParameter)
	 */
	@Override
	public void beforePlaceOrder(final CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		// Nothing done here

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook#beforeSubmitOrder(de.hybris.platform
	 * .commerceservices.service.data.CommerceCheckoutParameter,
	 * de.hybris.platform.commerceservices.service.data.CommerceOrderResult)
	 */
	@Override
	public void beforeSubmitOrder(final CommerceCheckoutParameter parameter, final CommerceOrderResult result)
			throws InvalidCartException
	{
		// Nothing done here
	}

	/**
	 * @return product configuration service
	 */
	public ProductConfigurationService getProductConfigurationService()
	{
		return this.productConfigurationService;
	}

}
