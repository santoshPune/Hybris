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

import  java.util.Map;
import java.util.Map;

public  class SimpleResponsiveBannerComponentData extends AbstractCMSComponentData 
{


	/** <i>Generated property</i> for <code>SimpleResponsiveBannerComponentData.media</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String,Map<String, String>> media;

	/** <i>Generated property</i> for <code>SimpleResponsiveBannerComponentData.urlLink</code> property defined at extension <code>cmswebservices</code>. */
		
	private String urlLink;
	
	public SimpleResponsiveBannerComponentData()
	{
		// default constructor
	}
	
	
	
	public void setMedia(final Map<String,Map<String, String>> media)
	{
		this.media = media;
	}
	
	
	
	public Map<String,Map<String, String>> getMedia() 
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
	


}