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
package de.hybris.platform.financialfacades.strategies;

import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.List;


/**
 * Quote Policy Strategy to retrieve user's quotes and policies information
 */
public interface QuotePolicyStrategy
{
	/**
	 * Obtain a list of policies with product information.
	 */
	List<ProductData> getPoliciesProducts();

	/**
	 * Obtain a list of quotes with product information.
	 */
	List<ProductData> getQuotesProducts();
}
