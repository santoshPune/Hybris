/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */
package de.hybris.platform.financialfacades.strategies.impl;

import de.hybris.platform.financialfacades.facades.InsuranceCartFacade;
import de.hybris.platform.financialfacades.strategies.StepEnabledAnalysisStrategy;

import org.springframework.beans.factory.annotation.Required;


public class QuoteStatusStepEnabledStrategy implements StepEnabledAnalysisStrategy
{
	private InsuranceCartFacade cartFacade;

	public boolean isEnabled()
	{
		return getCartFacade().checkStepIsEnabledFromQuoteInCurrentCart();
	}

	protected InsuranceCartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final InsuranceCartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}


}
