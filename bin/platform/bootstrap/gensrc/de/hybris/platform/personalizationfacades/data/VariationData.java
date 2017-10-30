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
package de.hybris.platform.personalizationfacades.data;

import de.hybris.platform.personalizationfacades.data.ActionData;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.TriggerData;
import java.util.List;

public  class VariationData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>VariationData.code</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>VariationData.name</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VariationData.active</code> property defined at extension <code>personalizationfacades</code>. */
		
	private boolean active;

	/** <i>Generated property</i> for <code>VariationData.enabled</code> property defined at extension <code>personalizationfacades</code>. */
		
	private boolean enabled;

	/** <i>Generated property</i> for <code>VariationData.rank</code> property defined at extension <code>personalizationfacades</code>. */
		
	private Integer rank;

	/** <i>Generated property</i> for <code>VariationData.customization</code> property defined at extension <code>personalizationfacades</code>. */
		
	private CustomizationData customization;

	/** <i>Generated property</i> for <code>VariationData.triggers</code> property defined at extension <code>personalizationfacades</code>. */
		
	private List<TriggerData> triggers;

	/** <i>Generated property</i> for <code>VariationData.actions</code> property defined at extension <code>personalizationfacades</code>. */
		
	private List<ActionData> actions;
	
	public VariationData()
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
	
	
	
	public void setActive(final boolean active)
	{
		this.active = active;
	}
	
	
	
	public boolean isActive() 
	{
		return active;
	}
	
	
	
	public void setEnabled(final boolean enabled)
	{
		this.enabled = enabled;
	}
	
	
	
	public boolean isEnabled() 
	{
		return enabled;
	}
	
	
	
	public void setRank(final Integer rank)
	{
		this.rank = rank;
	}
	
	
	
	public Integer getRank() 
	{
		return rank;
	}
	
	
	
	public void setCustomization(final CustomizationData customization)
	{
		this.customization = customization;
	}
	
	
	
	public CustomizationData getCustomization() 
	{
		return customization;
	}
	
	
	
	public void setTriggers(final List<TriggerData> triggers)
	{
		this.triggers = triggers;
	}
	
	
	
	public List<TriggerData> getTriggers() 
	{
		return triggers;
	}
	
	
	
	public void setActions(final List<ActionData> actions)
	{
		this.actions = actions;
	}
	
	
	
	public List<ActionData> getActions() 
	{
		return actions;
	}
	


}