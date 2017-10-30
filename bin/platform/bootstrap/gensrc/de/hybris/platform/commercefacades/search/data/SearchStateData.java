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
package de.hybris.platform.commercefacades.search.data;

import de.hybris.platform.commercefacades.search.data.SearchQueryData;

public  class SearchStateData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SearchStateData.url</code> property defined at extension <code>commercefacades</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>SearchStateData.query</code> property defined at extension <code>commercefacades</code>. */
		
	private SearchQueryData query;
	
	public SearchStateData()
	{
		// default constructor
	}
	
	
	
	public void setUrl(final String url)
	{
		this.url = url;
	}
	
	
	
	public String getUrl() 
	{
		return url;
	}
	
	
	
	public void setQuery(final SearchQueryData query)
	{
		this.query = query;
	}
	
	
	
	public SearchQueryData getQuery() 
	{
		return query;
	}
	


}