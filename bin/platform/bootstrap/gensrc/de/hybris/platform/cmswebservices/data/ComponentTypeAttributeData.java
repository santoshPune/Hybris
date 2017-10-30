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

import de.hybris.platform.cmswebservices.data.OptionData;
import java.util.List;

public  class ComponentTypeAttributeData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ComponentTypeAttributeData.qualifier</code> property defined at extension <code>cmswebservices</code>. */
		
	private String qualifier;

	/** <i>Generated property</i> for <code>ComponentTypeAttributeData.localized</code> property defined at extension <code>cmswebservices</code>. */
		
	private Boolean localized;

	/** <i>Generated property</i> for <code>ComponentTypeAttributeData.cmsStructureType</code> property defined at extension <code>cmswebservices</code>. */
		
	private String cmsStructureType;

	/** <i>Generated property</i> for <code>ComponentTypeAttributeData.cmsStructureEnumType</code> property defined at extension <code>cmswebservices</code>. */
		
	private String cmsStructureEnumType;

	/** <i>Generated property</i> for <code>ComponentTypeAttributeData.i18nKey</code> property defined at extension <code>cmswebservices</code>. */
		
	private String i18nKey;

	/** <i>Generated property</i> for <code>ComponentTypeAttributeData.options</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<OptionData> options;
	
	public ComponentTypeAttributeData()
	{
		// default constructor
	}
	
	
	
	public void setQualifier(final String qualifier)
	{
		this.qualifier = qualifier;
	}
	
	
	
	public String getQualifier() 
	{
		return qualifier;
	}
	
	
	
	public void setLocalized(final Boolean localized)
	{
		this.localized = localized;
	}
	
	
	
	public Boolean getLocalized() 
	{
		return localized;
	}
	
	
	
	public void setCmsStructureType(final String cmsStructureType)
	{
		this.cmsStructureType = cmsStructureType;
	}
	
	
	
	public String getCmsStructureType() 
	{
		return cmsStructureType;
	}
	
	
	
	public void setCmsStructureEnumType(final String cmsStructureEnumType)
	{
		this.cmsStructureEnumType = cmsStructureEnumType;
	}
	
	
	
	public String getCmsStructureEnumType() 
	{
		return cmsStructureEnumType;
	}
	
	
	
	public void setI18nKey(final String i18nKey)
	{
		this.i18nKey = i18nKey;
	}
	
	
	
	public String getI18nKey() 
	{
		return i18nKey;
	}
	
	
	
	public void setOptions(final List<OptionData> options)
	{
		this.options = options;
	}
	
	
	
	public List<OptionData> getOptions() 
	{
		return options;
	}
	


}