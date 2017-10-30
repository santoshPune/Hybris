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
 */
package de.hybris.platform.b2bacceleratoraddon.forms;

/**
 * Pojo for 'quote order' form.
 */
public class QuoteOrderForm
{
	private String selectedQuoteDecision;
	private String orderCode;
	private String comments;

	/**
	 * @return the selectedQuoteDecision
	 */
	public String getSelectedQuoteDecision()
	{
		return selectedQuoteDecision;
	}

	/**
	 * @param selectedQuoteDecision
	 *           the selectedQuoteDecision to set
	 */
	public void setSelectedQuoteDecision(final String selectedQuoteDecision)
	{
		this.selectedQuoteDecision = selectedQuoteDecision;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode()
	{
		return orderCode;
	}

	/**
	 * @param orderCode
	 *           the orderCode to set
	 */
	public void setOrderCode(final String orderCode)
	{
		this.orderCode = orderCode;
	}

	/**
	 * @return the comments
	 */
	public String getComments()
	{
		return comments;
	}

	/**
	 * @param comments
	 *           the comments to set
	 */
	public void setComments(final String comments)
	{
		this.comments = comments;
	}
}
