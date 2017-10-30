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

import de.hybris.platform.cmswebservices.data.ComponentTypeAttributeData;
import java.util.List;

public  class ComponentTypeData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ComponentTypeData.code</code> property defined at extension <code>cmswebservices</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>ComponentTypeData.name</code> property defined at extension <code>cmswebservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>ComponentTypeData.i18nKey</code> property defined at extension <code>cmswebservices</code>. */
		
	private String i18nKey;

	/** <i>Generated property</i> for <code>ComponentTypeData.attributes</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<ComponentTypeAttributeData> attributes;
	
	public ComponentTypeData()
	{
		// default constructor
	}
	
	
	
	public void setCode(final String code)
	{
		this.code = code;
	}
	
	
	
	public String getCode() 
	{
		return code;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setI18nKey(final String i18nKey)
	{
		this.i18nKey = i18nKey;
	}
	
	
	
	public String getI18nKey() 
	{
		return i18nKey;
	}
	
	
	
	public void setAttributes(final List<ComponentTypeAttributeData> attributes)
	{
		this.attributes = attributes;
	}
	
	
	
	public List<ComponentTypeAttributeData> getAttributes() 
	{
		return attributes;
	}
	


}