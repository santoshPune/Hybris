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
package de.hybris.platform.cmswebservices.data;

import de.hybris.platform.cmswebservices.data.AbstractPageData;
import java.util.List;

public  class PageListData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>PageListData.pages</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<AbstractPageData> pages;
	
	public PageListData()
	{
		// default constructor
	}
	
	
	
	public void setPages(final List<AbstractPageData> pages)
	{
		this.pages = pages;
	}
	
	
	
	public List<AbstractPageData> getPages() 
	{
		return pages;
	}
	


}