/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:02 PM
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

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchRequest;

public  class SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE>  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE>.request</code> property defined at extension <code>commerceservices</code>. */
		
	private SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> request;

	/** <i>Generated property</i> for <code>SolrSearchResponse<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE, SEARCH_RESULT_TYPE>.searchResult</code> property defined at extension <code>commerceservices</code>. */
		
	private SEARCH_RESULT_TYPE searchResult;
	
	public SolrSearchResponse()
	{
		// default constructor
	}
	
	
	
	public void setRequest(final SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> request)
	{
		this.request = request;
	}
	
	
	
	public SolrSearchRequest<FACET_SEARCH_CONFIG_TYPE, INDEXED_TYPE_TYPE, INDEXED_PROPERTY_TYPE, SEARCH_QUERY_TYPE, INDEXED_TYPE_SORT_TYPE> getRequest() 
	{
		return request;
	}
	
	
	
	public void setSearchResult(final SEARCH_RESULT_TYPE searchResult)
	{
		this.searchResult = searchResult;
	}
	
	
	
	public SEARCH_RESULT_TYPE getSearchResult() 
	{
		return searchResult;
	}
	


}