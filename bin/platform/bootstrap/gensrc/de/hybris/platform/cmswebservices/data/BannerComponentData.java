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

import java.util.Map;

public  class BannerComponentData extends AbstractCMSComponentData 
{


	/** <i>Generated property</i> for <code>BannerComponentData.content</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String, String> content;

	/** <i>Generated property</i> for <code>BannerComponentData.headline</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String, String> headline;

	/** <i>Generated property</i> for <code>BannerComponentData.media</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String, String> media;

	/** <i>Generated property</i> for <code>BannerComponentData.urlLink</code> property defined at extension <code>cmswebservices</code>. */
		
	private String urlLink;

	/** <i>Generated property</i> for <code>BannerComponentData.external</code> property defined at extension <code>cmswebservices</code>. */
		
	private Boolean external;
	
	public BannerComponentData()
	{
		// default constructor
	}
	
	
	
	public void setContent(final Map<String, String> content)
	{
		this.content = content;
	}
	
	
	
	public Map<String, String> getContent() 
	{
		return content;
	}
	
	
	
	public void setHeadline(final Map<String, String> headline)
	{
		this.headline = headline;
	}
	
	
	
	public Map<String, String> getHeadline() 
	{
		return headline;
	}
	
	
	
	public void setMedia(final Map<String, String> media)
	{
		this.media = media;
	}
	
	
	
	public Map<String, String> getMedia() 
	{
		return media;
	}
	
	
	
	public void setUrlLink(final String urlLink)
	{
		this.urlLink = urlLink;
	}
	
	
	
	public String getUrlLink() 
	{
		return urlLink;
	}
	
	
	
	public void setExternal(final Boolean external)
	{
		this.external = external;
	}
	
	
	
	public Boolean getExternal() 
	{
		return external;
	}
	


}