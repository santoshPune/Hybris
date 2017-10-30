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
package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.commercefacades.product.data.ProductData;
import java.util.List;

public  class ProductResultData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ProductResultData.products</code> property defined at extension <code>commercefacades</code>. */
		
	private List<ProductData> products;

	/** <i>Generated property</i> for <code>ProductResultData.totalCount</code> property defined at extension <code>commercefacades</code>. */
		
	private int totalCount;

	/** <i>Generated property</i> for <code>ProductResultData.count</code> property defined at extension <code>commercefacades</code>. */
		
	private int count;

	/** <i>Generated property</i> for <code>ProductResultData.requestedCount</code> property defined at extension <code>commercefacades</code>. */
		
	private int requestedCount;

	/** <i>Generated property</i> for <code>ProductResultData.requestedStart</code> property defined at extension <code>commercefacades</code>. */
		
	private int requestedStart;
	
	public ProductResultData()
	{
		// default constructor
	}
	
	
	
	public void setProducts(final List<ProductData> products)
	{
		this.products = products;
	}
	
	
	
	public List<ProductData> getProducts() 
	{
		return products;
	}
	
	
	
	public void setTotalCount(final int totalCount)
	{
		this.totalCount = totalCount;
	}
	
	
	
	public int getTotalCount() 
	{
		return totalCount;
	}
	
	
	
	public void setCount(final int count)
	{
		this.count = count;
	}
	
	
	
	public int getCount() 
	{
		return count;
	}
	
	
	
	public void setRequestedCount(final int requestedCount)
	{
		this.requestedCount = requestedCount;
	}
	
	
	
	public int getRequestedCount() 
	{
		return requestedCount;
	}
	
	
	
	public void setRequestedStart(final int requestedStart)
	{
		this.requestedStart = requestedStart;
	}
	
	
	
	public int getRequestedStart() 
	{
		return requestedStart;
	}
	


}