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
package de.hybris.platform.ruleengineservices.rule.data;

import java.util.List;

public  class RuleParameterDefinitionData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.name</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.description</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.priority</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.type</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String type;

	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.defaultValue</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Object defaultValue;

	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.required</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Boolean required;

	/** <i>Generated property</i> for <code>RuleParameterDefinitionData.validators</code> property defined at extension <code>ruleengineservices</code>. */
		
	private List<String> validators;
	
	public RuleParameterDefinitionData()
	{
		// default constructor
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	
	
	public String getDescription() 
	{
		return description;
	}
	
	
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}
	
	
	
	public Integer getPriority() 
	{
		return priority;
	}
	
	
	
	public void setType(final String type)
	{
		this.type = type;
	}
	
	
	
	public String getType() 
	{
		return type;
	}
	
	
	
	public void setDefaultValue(final Object defaultValue)
	{
		this.defaultValue = defaultValue;
	}
	
	
	
	public Object getDefaultValue() 
	{
		return defaultValue;
	}
	
	
	
	public void setRequired(final Boolean required)
	{
		this.required = required;
	}
	
	
	
	public Boolean getRequired() 
	{
		return required;
	}
	
	
	
	public void setValidators(final List<String> validators)
	{
		this.validators = validators;
	}
	
	
	
	public List<String> getValidators() 
	{
		return validators;
	}
	


}