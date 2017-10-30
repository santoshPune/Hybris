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
package de.hybris.platform.b2cbtgaddon.filters.btg.support.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.ProductService;


/**
 * Implementation of {@link AbstractParsingPkResolvingStrategy} that retrieves a product pk from the request
 */
public class ProductPkResolvingStrategy extends AbstractParsingPkResolvingStrategy
{
	private ProductService productService;

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Override
	protected ItemModel retrieveModel(final String key)
	{
		return productService.getProductForCode(key);
	}
}
