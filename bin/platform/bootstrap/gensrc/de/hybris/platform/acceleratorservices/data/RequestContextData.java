/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:01 PM
 * ----------------------------------------------------------------
 *
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.acceleratorservices.data;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

/**
 * Holds context data for rendering the current request
 */
public  class RequestContextData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>RequestContextData.product</code> property defined at extension <code>acceleratorcms</code>. */
		
	private ProductModel product;

	/** <i>Generated property</i> for <code>RequestContextData.category</code> property defined at extension <code>acceleratorcms</code>. */
		
	private CategoryModel category;

	/** <i>Generated property</i> for <code>RequestContextData.search</code> property defined at extension <code>acceleratorcms</code>. */
		
	private SearchPageData search;
	
	public RequestContextData()
	{
		// default constructor
	}
	
	
	
	public void setProduct(final ProductModel product)
	{
		this.product = product;
	}
	
	
	
	public ProductModel getProduct() 
	{
		return product;
	}
	
	
	
	public void setCategory(final CategoryModel category)
	{
		this.category = category;
	}
	
	
	
	public CategoryModel getCategory() 
	{
		return category;
	}
	
	
	
	public void setSearch(final SearchPageData search)
	{
		this.search = search;
	}
	
	
	
	public SearchPageData getSearch() 
	{
		return search;
	}
	


}