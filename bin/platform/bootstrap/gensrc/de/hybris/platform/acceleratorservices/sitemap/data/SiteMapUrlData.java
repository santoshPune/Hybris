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
package de.hybris.platform.acceleratorservices.sitemap.data;

import java.util.List;

public  class SiteMapUrlData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SiteMapUrlData.loc</code> property defined at extension <code>acceleratorservices</code>. */
		
	private String loc;

	/** <i>Generated property</i> for <code>SiteMapUrlData.images</code> property defined at extension <code>acceleratorservices</code>. */
		
	private List<String> images;
	
	public SiteMapUrlData()
	{
		// default constructor
	}
	
	
	
	public void setLoc(final String loc)
	{
		this.loc = loc;
	}
	
	
	
	public String getLoc() 
	{
		return loc;
	}
	
	
	
	public void setImages(final List<String> images)
	{
		this.images = images;
	}
	
	
	
	public List<String> getImages() 
	{
		return images;
	}
	


}