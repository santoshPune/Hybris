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
package de.hybris.platform.commerceservices.search.facetdata;

/**
 * POJO representing a spell checker suggestion.
 */
public  class SpellingSuggestionData<STATE>  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SpellingSuggestionData<STATE>.suggestion</code> property defined at extension <code>commerceservices</code>. */
		
	private String suggestion;

	/** <i>Generated property</i> for <code>SpellingSuggestionData<STATE>.query</code> property defined at extension <code>commerceservices</code>. */
		
	private STATE query;
	
	public SpellingSuggestionData()
	{
		// default constructor
	}
	
	
	
	public void setSuggestion(final String suggestion)
	{
		this.suggestion = suggestion;
	}
	
	
	
	public String getSuggestion() 
	{
		return suggestion;
	}
	
	
	
	public void setQuery(final STATE query)
	{
		this.query = query;
	}
	
	
	
	public STATE getQuery() 
	{
		return query;
	}
	


}