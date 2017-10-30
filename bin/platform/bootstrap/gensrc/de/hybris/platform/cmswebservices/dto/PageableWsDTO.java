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
package de.hybris.platform.cmswebservices.dto;

public  class PageableWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>PageableWsDTO.pageSize</code> property defined at extension <code>cmswebservices</code>. */
		
	private int pageSize;

	/** <i>Generated property</i> for <code>PageableWsDTO.currentPage</code> property defined at extension <code>cmswebservices</code>. */
		
	private int currentPage;

	/** <i>Generated property</i> for <code>PageableWsDTO.sort</code> property defined at extension <code>cmswebservices</code>. */
		
	private String sort;
	
	public PageableWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setPageSize(final int pageSize)
	{
		this.pageSize = pageSize;
	}
	
	
	
	public int getPageSize() 
	{
		return pageSize;
	}
	
	
	
	public void setCurrentPage(final int currentPage)
	{
		this.currentPage = currentPage;
	}
	
	
	
	public int getCurrentPage() 
	{
		return currentPage;
	}
	
	
	
	public void setSort(final String sort)
	{
		this.sort = sort;
	}
	
	
	
	public String getSort() 
	{
		return sort;
	}
	


}