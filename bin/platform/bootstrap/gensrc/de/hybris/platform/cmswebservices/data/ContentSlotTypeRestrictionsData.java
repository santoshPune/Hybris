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

import java.util.List;

public  class ContentSlotTypeRestrictionsData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ContentSlotTypeRestrictionsData.contentSlotUid</code> property defined at extension <code>cmswebservices</code>. */
		
	private String contentSlotUid;

	/** <i>Generated property</i> for <code>ContentSlotTypeRestrictionsData.validComponentTypes</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<String> validComponentTypes;
	
	public ContentSlotTypeRestrictionsData()
	{
		// default constructor
	}
	
	
	
	public void setContentSlotUid(final String contentSlotUid)
	{
		this.contentSlotUid = contentSlotUid;
	}
	
	
	
	public String getContentSlotUid() 
	{
		return contentSlotUid;
	}
	
	
	
	public void setValidComponentTypes(final List<String> validComponentTypes)
	{
		this.validComponentTypes = validComponentTypes;
	}
	
	
	
	public List<String> getValidComponentTypes() 
	{
		return validComponentTypes;
	}
	


}