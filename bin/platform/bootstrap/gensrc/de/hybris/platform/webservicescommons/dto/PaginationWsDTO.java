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
package de.hybris.platform.webservicescommons.dto;

/**
 * Pagination info
 */
public  class PaginationWsDTO  implements java.io.Serializable 
{


	/** Number of elements on this page<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.count</code> property defined at extension <code>webservicescommons</code>. */
		
	private int count;

	/** Total number of elements<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.totalCount</code> property defined at extension <code>webservicescommons</code>. */
		
	private int totalCount;

	/** Current page number<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.page</code> property defined at extension <code>webservicescommons</code>. */
		
	private int page;

	/** Total number of pages<br/><br/><i>Generated property</i> for <code>PaginationWsDTO.totalPages</code> property defined at extension <code>webservicescommons</code>. */
		
	private int totalPages;
	
	public PaginationWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setCount(final int count)
	{
		this.count = count;
	}
	
	
	
	public int getCount() 
	{
		return count;
	}
	
	
	
	public void setTotalCount(final int totalCount)
	{
		this.totalCount = totalCount;
	}
	
	
	
	public int getTotalCount() 
	{
		return totalCount;
	}
	
	
	
	public void setPage(final int page)
	{
		this.page = page;
	}
	
	
	
	public int getPage() 
	{
		return page;
	}
	
	
	
	public void setTotalPages(final int totalPages)
	{
		this.totalPages = totalPages;
	}
	
	
	
	public int getTotalPages() 
	{
		return totalPages;
	}
	


}