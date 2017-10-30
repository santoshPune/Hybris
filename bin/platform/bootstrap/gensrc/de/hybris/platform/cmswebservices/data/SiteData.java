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
package de.hybris.platform.cmswebservices.data;

import java.util.Map;

public  class SiteData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SiteData.uid</code> property defined at extension <code>cmswebservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>SiteData.previewUrl</code> property defined at extension <code>cmswebservices</code>. */
		
	private String previewUrl;

	/** <i>Generated property</i> for <code>SiteData.redirectUrl</code> property defined at extension <code>cmswebservices</code>. */
		
	private String redirectUrl;

	/** <i>Generated property</i> for <code>SiteData.name</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String, String> name;

	/** <i>Generated property</i> for <code>SiteData.thumbnailUrl</code> property defined at extension <code>cmswebservices</code>. */
		
	private String thumbnailUrl;
	
	public SiteData()
	{
		// default constructor
	}
	
	
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}
	
	
	
	public String getUid() 
	{
		return uid;
	}
	
	
	
	public void setPreviewUrl(final String previewUrl)
	{
		this.previewUrl = previewUrl;
	}
	
	
	
	public String getPreviewUrl() 
	{
		return previewUrl;
	}
	
	
	
	public void setRedirectUrl(final String redirectUrl)
	{
		this.redirectUrl = redirectUrl;
	}
	
	
	
	public String getRedirectUrl() 
	{
		return redirectUrl;
	}
	
	
	
	public void setName(final Map<String, String> name)
	{
		this.name = name;
	}
	
	
	
	public Map<String, String> getName() 
	{
		return name;
	}
	
	
	
	public void setThumbnailUrl(final String thumbnailUrl)
	{
		this.thumbnailUrl = thumbnailUrl;
	}
	
	
	
	public String getThumbnailUrl() 
	{
		return thumbnailUrl;
	}
	


}