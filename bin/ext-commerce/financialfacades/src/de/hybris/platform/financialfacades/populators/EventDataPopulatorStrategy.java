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
package de.hybris.platform.financialfacades.populators;

import de.hybris.platform.commercefacades.quotation.InsuranceQuoteData;
import de.hybris.platform.financialservices.enums.QuoteType;

import java.util.Map;


/**
 * @author david.boland
 *
 */
public class EventDataPopulatorStrategy extends InsuranceDataPopulatorStrategy
{

	protected static final String CHECKOUT_CART_TITLE_EVENT = "checkout.cart.title.event";

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.financialacceleratorstorefront.populators.InsuranceDataPopulatorStrategy#
	 * processInsuranceQuoteData (de.hybris.platform.commercefacades.quotation.InsuranceQuoteData, java.util.Map)
	 */
	@Override
	public void processInsuranceQuoteData(final InsuranceQuoteData quoteData, final Map<String, Object> infoMap)
	{
		// We use this to set values initially and the others to conditionally change the values
		quoteData.setQuoteType(QuoteType.EVENT);
		quoteData.setQuoteTitle(CHECKOUT_CART_TITLE_EVENT);
	}

}
