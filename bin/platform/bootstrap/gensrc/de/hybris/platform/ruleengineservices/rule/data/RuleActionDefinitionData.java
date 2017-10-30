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

import de.hybris.platform.ruleengineservices.rule.data.AbstractRuleDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionCategoryData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import java.util.List;
import java.util.Map;

public  class RuleActionDefinitionData extends AbstractRuleDefinitionData 
{


	/** <i>Generated property</i> for <code>RuleActionDefinitionData.priority</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Integer priority;

	/** <i>Generated property</i> for <code>RuleActionDefinitionData.breadcrumb</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String breadcrumb;

	/** <i>Generated property</i> for <code>RuleActionDefinitionData.translatorId</code> property defined at extension <code>ruleengineservices</code>. */
		
	private String translatorId;

	/** <i>Generated property</i> for <code>RuleActionDefinitionData.translatorParameters</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,String> translatorParameters;

	/** <i>Generated property</i> for <code>RuleActionDefinitionData.parameters</code> property defined at extension <code>ruleengineservices</code>. */
		
	private Map<String,RuleParameterDefinitionData> parameters;

	/** <i>Generated property</i> for <code>RuleActionDefinitionData.categories</code> property defined at extension <code>ruleengineservices</code>. */
		
	private List<RuleActionDefinitionCategoryData> categories;
	
	public RuleActionDefinitionData()
	{
		// default constructor
	}
	
	
	
	public void setPriority(final Integer priority)
	{
		this.priority = priority;
	}
	
	
	
	public Integer getPriority() 
	{
		return priority;
	}
	
	
	
	public void setBreadcrumb(final String breadcrumb)
	{
		this.breadcrumb = breadcrumb;
	}
	
	
	
	public String getBreadcrumb() 
	{
		return breadcrumb;
	}
	
	
	
	public void setTranslatorId(final String translatorId)
	{
		this.translatorId = translatorId;
	}
	
	
	
	public String getTranslatorId() 
	{
		return translatorId;
	}
	
	
	
	public void setTranslatorParameters(final Map<String,String> translatorParameters)
	{
		this.translatorParameters = translatorParameters;
	}
	
	
	
	public Map<String,String> getTranslatorParameters() 
	{
		return translatorParameters;
	}
	
	
	
	public void setParameters(final Map<String,RuleParameterDefinitionData> parameters)
	{
		this.parameters = parameters;
	}
	
	
	
	public Map<String,RuleParameterDefinitionData> getParameters() 
	{
		return parameters;
	}
	
	
	
	public void setCategories(final List<RuleActionDefinitionCategoryData> categories)
	{
		this.categories = categories;
	}
	
	
	
	public List<RuleActionDefinitionCategoryData> getCategories() 
	{
		return categories;
	}
	


}