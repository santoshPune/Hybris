/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.ruleengine;

import java.util.Collection;

public class RuleEngineActionResult  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>RuleEngineActionResult.moduleName</code> property defined at extension <code>ruleengine</code>. */
	private String moduleName;
	/** <i>Generated property</i> for <code>RuleEngineActionResult.actionFailed</code> property defined at extension <code>ruleengine</code>. */
	private boolean actionFailed;
	/** <i>Generated property</i> for <code>RuleEngineActionResult.results</code> property defined at extension <code>ruleengine</code>. */
	private Collection<ResultItem> results;
		
	public RuleEngineActionResult()
	{
		// default constructor
	}
	
		
	public void setModuleName(final String moduleName)
	{
		this.moduleName = moduleName;
	}
	
		
	public String getModuleName() 
	{
		return moduleName;
	}
		
		
	public void setActionFailed(final boolean actionFailed)
	{
		this.actionFailed = actionFailed;
	}
	
		
	public boolean isActionFailed() 
	{
		return actionFailed;
	}
		
		
	public void setResults(final Collection<ResultItem> results)
	{
		this.results = results;
	}
	
		
	public Collection<ResultItem> getResults() 
	{
		return results;
	}
		
	
	/**
	 * returns a string representation of all messages of a given Level (or all if given Level is null).
	 */
	public String getMessagesAsString(final MessageLevel level)
	{
		final StringBuilder sb = new StringBuilder("RulesModule:").append(getModuleName());
		if (results != null)
		{
			int n = 0;
			for (final ResultItem item : results)
			{
				if (level == null || level.equals(item.getLevel()))
				{
					final String messageLine = String.format("%s line %d : %s", item.getPath(), Integer.valueOf(item.getLine()),
							item.getMessage());
					sb.append(results.size() > 1 ? String.format("%d) ", Integer.valueOf(++n)) : "").append(messageLine)
							.append(System.lineSeparator());
				}
			}
		}
		return sb.toString();
	}

}