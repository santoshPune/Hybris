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
package com.sap.hybris.reco.dao;

import de.hybris.platform.core.model.product.ProductModel;


/**
 * Product Recommendation
 */
public class ProductRecommendation
{
	private ProductModel product;

	/**
	 * @return ProductModel product
	 */
	public ProductModel getProduct()
	{
		return product;
	}

	/**
	 * @param product
	 *           the product to set
	 */
	public void setProduct(final ProductModel product)
	{
		this.product = product;
	}
}
