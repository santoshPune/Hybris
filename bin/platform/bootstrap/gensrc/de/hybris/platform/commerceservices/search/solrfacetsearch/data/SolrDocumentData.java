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

public  class SolrDocumentData<SEARCH_QUERY_TYPE, SOLR_DOCUMENT_TYPE>  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SolrDocumentData<SEARCH_QUERY_TYPE, SOLR_DOCUMENT_TYPE>.searchQuery</code> property defined at extension <code>commerceservices</code>. */
		
	private SEARCH_QUERY_TYPE searchQuery;

	/** <i>Generated property</i> for <code>SolrDocumentData<SEARCH_QUERY_TYPE, SOLR_DOCUMENT_TYPE>.solrDocument</code> property defined at extension <code>commerceservices</code>. */
		
	private SOLR_DOCUMENT_TYPE solrDocument;
	
	public SolrDocumentData()
	{
		// default constructor
	}
	
	
	
	public void setSearchQuery(final SEARCH_QUERY_TYPE searchQuery)
	{
		this.searchQuery = searchQuery;
	}
	
	
	
	public SEARCH_QUERY_TYPE getSearchQuery() 
	{
		return searchQuery;
	}
	
	
	
	public void setSolrDocument(final SOLR_DOCUMENT_TYPE solrDocument)
	{
		this.solrDocument = solrDocument;
	}
	
	
	
	public SOLR_DOCUMENT_TYPE getSolrDocument() 
	{
		return solrDocument;
	}
	


}