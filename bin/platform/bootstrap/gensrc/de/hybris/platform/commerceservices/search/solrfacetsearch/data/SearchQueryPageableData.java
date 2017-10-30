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
package de.hybris.platform.commerceservices.search.solrfacetsearch.data;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;

public  class SearchQueryPageableData<STATE>  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SearchQueryPageableData<STATE>.searchQueryData</code> property defined at extension <code>commerceservices</code>. */
		
	private STATE searchQueryData;

	/** <i>Generated property</i> for <code>SearchQueryPageableData<STATE>.pageableData</code> property defined at extension <code>commerceservices</code>. */
		
	private PageableData pageableData;
	
	public SearchQueryPageableData()
	{
		// default constructor
	}
	
	
	
	public void setSearchQueryData(final STATE searchQueryData)
	{
		this.searchQueryData = searchQueryData;
	}
	
	
	
	public STATE getSearchQueryData() 
	{
		return searchQueryData;
	}
	
	
	
	public void setPageableData(final PageableData pageableData)
	{
		this.pageableData = pageableData;
	}
	
	
	
	public PageableData getPageableData() 
	{
		return pageableData;
	}
	


}