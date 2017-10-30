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

public  class EmailPageData extends AbstractPageData 
{


	/** <i>Generated property</i> for <code>EmailPageData.fromEmail</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String,String> fromEmail;

	/** <i>Generated property</i> for <code>EmailPageData.fromName</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String,String> fromName;
	
	public EmailPageData()
	{
		// default constructor
	}
	
	
	
	public void setFromEmail(final Map<String,String> fromEmail)
	{
		this.fromEmail = fromEmail;
	}
	
	
	
	public Map<String,String> getFromEmail() 
	{
		return fromEmail;
	}
	
	
	
	public void setFromName(final Map<String,String> fromName)
	{
		this.fromName = fromName;
	}
	
	
	
	public Map<String,String> getFromName() 
	{
		return fromName;
	}
	


}