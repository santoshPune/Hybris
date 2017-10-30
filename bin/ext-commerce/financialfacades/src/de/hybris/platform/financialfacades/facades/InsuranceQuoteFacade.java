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
package de.hybris.platform.financialfacades.facades;

import de.hybris.platform.commercefacades.insurance.data.InsuranceQuoteListingData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.financialservices.enums.QuoteBindingState;
import de.hybris.platform.financialservices.enums.QuoteWorkflowStatus;
import de.hybris.platform.financialservices.model.InsuranceQuoteModel;

import java.util.List;


public interface InsuranceQuoteFacade extends QuoteFacade
{
	/**
	 * Create a quote object in session cart.
	 */
	void createQuoteInSessionCart();

	/**
	 * Unbind quote for the quote
	 */
	InsuranceQuoteModel unbindQuote(final InsuranceQuoteModel quoteModel);

	/**
	 * Bind quote for the quote
	 */
	InsuranceQuoteModel bindQuote(final InsuranceQuoteModel insuranceQuote);

	/**
	 * Get quote state from the session cart
	 */
	QuoteBindingState getQuoteStateFromSessionCart();


	/**
	 * Sort the cart data list based on the quote listing
	 */
	List<InsuranceQuoteListingData> sortCartListForQuoteListing(List<CartData> cartDataList);

	/**
	 * Get the quote workflow type.
	 */
	String getQuoteWorkflowType();

	/**
	 * Get the quote workflow status for session cart.
	 */
	QuoteWorkflowStatus getQuoteWorkflowStatus();
}
