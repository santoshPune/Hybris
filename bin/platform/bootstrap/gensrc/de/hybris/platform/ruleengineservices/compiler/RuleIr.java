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
package de.hybris.platform.ruleengineservices.compiler;

import de.hybris.platform.ruleengineservices.compiler.RuleIrAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrVariablesContainer;
import java.util.List;

public  class RuleIr  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>RuleIr.variablesContainer</code> property defined at extension <code>ruleengineservices</code>. */
		
	private RuleIrVariablesContainer variablesContainer;

	/** <i>Generated property</i> for <code>RuleIr.conditions</code> property defined at extension <code>ruleengineservices</code>. */
		
	private List<RuleIrCondition> conditions;

	/** <i>Generated property</i> for <code>RuleIr.actions</code> property defined at extension <code>ruleengineservices</code>. */
		
	private List<RuleIrAction> actions;
	
	public RuleIr()
	{
		// default constructor
	}
	
	
	
	public void setVariablesContainer(final RuleIrVariablesContainer variablesContainer)
	{
		this.variablesContainer = variablesContainer;
	}
	
	
	
	public RuleIrVariablesContainer getVariablesContainer() 
	{
		return variablesContainer;
	}
	
	
	
	public void setConditions(final List<RuleIrCondition> conditions)
	{
		this.conditions = conditions;
	}
	
	
	
	public List<RuleIrCondition> getConditions() 
	{
		return conditions;
	}
	
	
	
	public void setActions(final List<RuleIrAction> actions)
	{
		this.actions = actions;
	}
	
	
	
	public List<RuleIrAction> getActions() 
	{
		return actions;
	}
	


}