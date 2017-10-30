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

public  class CMSLinkComponentData extends AbstractCMSComponentData 
{


	/** <i>Generated property</i> for <code>CMSLinkComponentData.linkName</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String, String> linkName;

	/** <i>Generated property</i> for <code>CMSLinkComponentData.url</code> property defined at extension <code>cmswebservices</code>. */
		
	private String url;

	/** <i>Generated property</i> for <code>CMSLinkComponentData.external</code> property defined at extension <code>cmswebservices</code>. */
		
	private Boolean external;
	
	public CMSLinkComponentData()
	{
		// default constructor
	}
	
	
	
	public void setLinkName(final Map<String, String> linkName)
	{
		this.linkName = linkName;
	}
	
	
	
	public Map<String, String> getLinkName() 
	{
		return linkName;
	}
	
	
	
	public void setUrl(final String url)
	{
		this.url = url;
	}
	
	
	
	public String getUrl() 
	{
		return url;
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