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
 *
 */
package de.hybris.platform.financialacceleratorstorefront.strategies.impl;

import de.hybris.platform.acceleratorstorefrontcommons.strategy.impl.MergingCartRestorationStrategy;
import de.hybris.platform.commercefacades.order.data.CommerceSaveCartResultData;
import de.hybris.platform.commerceservices.order.CommerceSaveCartException;
import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * The Class FinancialMergingCartRestorationStrategy. Override the restoreCart to make sure always save session cart
 * after successful login.
 */
public class FinancialMergingCartRestorationStrategy extends MergingCartRestorationStrategy
{
	private final Logger LOG = Logger.getLogger(FinancialMergingCartRestorationStrategy.class);

	private InsuranceCartFacade insuranceCartFacade;


	@Override
	public void restoreCart(final HttpServletRequest request)
	{
		final boolean saveCart = getCartFacade().hasEntries();
		super.restoreCart(request);

		// save session cart if the cart had entries before restoration
		if (saveCart)
		{
			saveSessionCart();
		}
	}

	/**
	 * Save session cart.
	 *
	 * @return the commerce save cart result data
	 */
	protected CommerceSaveCartResultData saveSessionCart()
	{
		if (getCartFacade().hasEntries())
		{
			try
			{
				return getInsuranceCartFacade().saveCurrentUserCart();
			}
			catch (final CommerceSaveCartException e)
			{
				LOG.error("Unable to save cart : " + e.getMessage(), e);
			}
		}

		return null;
	}

	protected InsuranceCartFacade getInsuranceCartFacade()
	{
		return insuranceCartFacade;
	}

	@Required
	public void setInsuranceCartFacade(final InsuranceCartFacade insuranceCartFacade)
	{
		this.insuranceCartFacade = insuranceCartFacade;
	}
}
