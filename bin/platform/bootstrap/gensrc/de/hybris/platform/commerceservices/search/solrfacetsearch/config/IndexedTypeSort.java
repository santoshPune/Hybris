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
package de.hybris.platform.commerceservices.search.solrfacetsearch.config;

import de.hybris.platform.commerceservices.model.solrsearch.config.SolrSortModel;

public  class IndexedTypeSort  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>IndexedTypeSort.sort</code> property defined at extension <code>commerceservices</code>. */
		
	private SolrSortModel sort;

	/** <i>Generated property</i> for <code>IndexedTypeSort.code</code> property defined at extension <code>commerceservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>IndexedTypeSort.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;
	
	public IndexedTypeSort()
	{
		// default constructor
	}
	
	
	
	public void setSort(final SolrSortModel sort)
	{
		this.sort = sort;
	}
	
	
	
	public SolrSortModel getSort() 
	{
		return sort;
	}
	
	
	
	public void setCode(final String code)
	{
		this.code = code;
	}
	
	
	
	public String getCode() 
	{
		return code;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	


}