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
package de.hybris.platform.commerceservices.search.pagedata;

import de.hybris.platform.commerceservices.search.pagedata.PageableData;

/**
 * POJO representation of search results pagination.
 */
public  class PaginationData extends PageableData 
{


	/** The total number of pages. This is the number of pages, each of pageSize, required to display the totalNumberOfResults.<br/><br/><i>Generated property</i> for <code>PaginationData.numberOfPages</code> property defined at extension <code>commerceservices</code>. */
		
	private int numberOfPages;

	/** The total number of matched results across all pages.<br/><br/><i>Generated property</i> for <code>PaginationData.totalNumberOfResults</code> property defined at extension <code>commerceservices</code>. */
		
	private long totalNumberOfResults;
	
	public PaginationData()
	{
		// default constructor
	}
	
	
	
	public void setNumberOfPages(final int numberOfPages)
	{
		this.numberOfPages = numberOfPages;
	}
	
	
	
	public int getNumberOfPages() 
	{
		return numberOfPages;
	}
	
	
	
	public void setTotalNumberOfResults(final long totalNumberOfResults)
	{
		this.totalNumberOfResults = totalNumberOfResults;
	}
	
	
	
	public long getTotalNumberOfResults() 
	{
		return totalNumberOfResults;
	}
	


}