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
package de.hybris.platform.ruleengineservices.rule.data;

import de.hybris.platform.ruleengineservices.rule.data.ImageData;

public  class RuleActionDefinitionCategoryData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>RuleActionDefinitionCategoryData.id</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>RuleActionDefinitionCategoryData.name</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>RuleActionDefinitionCategoryData.priority</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>RuleActionDefinitionCategoryData.icon</code> property defined at extension <code>ruleengineservices</code>. */
		
	private ImageData icon;
	
	public RuleActionDefinitionCategoryData()
	{
		// default constructor
	}
	
	
	
	public void setId(final String id)
	{
		this.id = id;
	}
	
	
	
	public String getId() 
	{
		return id;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}
	
	
	
	public Integer getPriority() 
	{
		return priority;
	}
	
	
	
	public void setIcon(final ImageData icon)
	{
		this.icon = icon;
	}
	
	
	
	public ImageData getIcon() 
	{
		return icon;
	}
	


}