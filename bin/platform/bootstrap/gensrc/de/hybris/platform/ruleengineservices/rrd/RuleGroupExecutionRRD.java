/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.ruleengineservices.rrd;

import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents a Rule Group execution during rule evaluation (gets inserted as one fact per rule group, the code being the rule group's code).
		This is used as a control fact to track and check the rules that have been executed for this group. Use the custom (see template) method allowedToExecute(RuleConfigurationRRD config) for checking whether a specific rule is allowed to execute.
 */
public class RuleGroupExecutionRRD  implements java.io.Serializable 
{

	/** the code of the rule group this execution control fact represents.<br/><br/><i>Generated property</i> for <code>RuleGroupExecutionRRD.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
	/** contains the map of rule codes and how often the rule has been executed.<br/><br/><i>Generated property</i> for <code>RuleGroupExecutionRRD.executedRules</code> property defined at extension <code>ruleengineservices</code>. */
	private Map<String,Integer> executedRules;
		
	public RuleGroupExecutionRRD()
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
		
		
	public void setExecutedRules(final Map<String,Integer> executedRules)
	{
		this.executedRules = executedRules;
	}
	
		
	public Map<String,Integer> getExecutedRules() 
	{
		return executedRules;
	}
		
		@Override
	public String toString() {
		return new ToStringBuilder(this).append("code", code).toString();
	}

 	@Override
    public boolean equals(Object obj)
    {
       if (obj == null || !this.getClass().equals(obj.getClass())) {
         return false;
       } else if (obj == this) {
         return true;
       }
       RuleGroupExecutionRRD other = (RuleGroupExecutionRRD)obj;
       return new EqualsBuilder()
          .append(code, other.code)
          .isEquals();
    }
    	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(code).toHashCode();
	}
	
	/**
	 * Determines whether the given rule (identified by its config object) is allowed to be executed. The logic for this method is as
	 * follows: By default, the first rule of the group that gets executed marks the group as no longer allowed to be
	 * executed, thereby this method will return false for all other rules. The only exception is when the first rule gets triggered again, 
	 * it is allowed to be executed as many times as specified by the rules 'maximum allowed executions' attribute. 
	 */
	public boolean allowedToExecute(final RuleConfigurationRRD ruleConfig)
	{
		if (this.executedRules == null)
		{
			// first execution of the group
			return true;
		}
		else
		{
			if (this.executedRules.entrySet().isEmpty())
			{
				// first execution of the group
				return true;
			}
			else
			{
				// no more executions allowed
				// unless this rule has been triggered already and has more than 1 executions allowed
				final Integer current = this.executedRules.get(ruleConfig.getRuleCode());
				if (current == null)
				{
					// this rule hasn't been tracked, so its not allowed to trigger again
					// as another rule has "consumed" this group
					return false;
				}
				Integer max = ruleConfig.getMaxAllowedRuns();
				if (max == null)
				{
					max = Integer.valueOf(1);
				}
				return current.compareTo(max) < 0;
			}
		}
	}
}
