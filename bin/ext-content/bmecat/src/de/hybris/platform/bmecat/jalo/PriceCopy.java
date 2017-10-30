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
package de.hybris.platform.bmecat.jalo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;


/**
 * PriceCopy
 */
public class PriceCopy extends GeneratedPriceCopy
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(PriceCopy.class.getName());

	@Override
	public String toString()
	{
		if (getImplementation() == null)
		{
			return super.toString();
		}
		final StringBuilder out = new StringBuilder();
		out.append(" product: ").append(getProductCode());
		out.append(" price: ").append(getPriceValueAsPrimitive());
		out.append(" currency: ").append(getCurrency() != null ? getCurrency().getIsoCode() : null);
		out.append(" daterange: ");
		final DateFormat priceDateFormat = new SimpleDateFormat("yyyy.MM.dd");
		out.append(getStartDate() == null ? "null" : priceDateFormat.format(getStartDate()));
		out.append(getEndDate() == null ? "null" : priceDateFormat.format(getEndDate()));

		return out.toString();
	}
}
