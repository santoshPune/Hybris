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
package de.hybris.platform.cmswebservices.dto;

import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;

public  class UpdateComponentValidationDto  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>UpdateComponentValidationDto.originalUid</code> property defined at extension <code>cmswebservices</code>. */
		
	private String originalUid;

	/** <i>Generated property</i> for <code>UpdateComponentValidationDto.component</code> property defined at extension <code>cmswebservices</code>. */
		
	private AbstractCMSComponentData component;
	
	public UpdateComponentValidationDto()
	{
		// default constructor
	}
	
	
	
	public void setOriginalUid(final String originalUid)
	{
		this.originalUid = originalUid;
	}
	
	
	
	public String getOriginalUid() 
	{
		return originalUid;
	}
	
	
	
	public void setComponent(final AbstractCMSComponentData component)
	{
		this.component = component;
	}
	
	
	
	public AbstractCMSComponentData getComponent() 
	{
		return component;
	}
	


}