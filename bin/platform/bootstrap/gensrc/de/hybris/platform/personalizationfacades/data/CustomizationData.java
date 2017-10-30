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

import de.hybris.platform.personalizationfacades.data.VariationData;
import java.util.Date;
import java.util.List;

public  class CustomizationData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CustomizationData.code</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>CustomizationData.name</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CustomizationData.description</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>CustomizationData.rank</code> property defined at extension <code>personalizationfacades</code>. */
		
	private Integer rank;

	/** <i>Generated property</i> for <code>CustomizationData.active</code> property defined at extension <code>personalizationfacades</code>. */
		
	private boolean active;

	/** <i>Generated property</i> for <code>CustomizationData.enabledStartDate</code> property defined at extension <code>personalizationfacades</code>. */
		
	private Date enabledStartDate;

	/** <i>Generated property</i> for <code>CustomizationData.enabledEndDate</code> property defined at extension <code>personalizationfacades</code>. */
		
	private Date enabledEndDate;

	/** <i>Generated property</i> for <code>CustomizationData.variations</code> property defined at extension <code>personalizationfacades</code>. */
		
	private List<VariationData> variations;
	
	public CustomizationData()
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
	
	
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	
	
	public String getDescription() 
	{
		return description;
	}
	
	
	
	public void setRank(final Integer rank)
	{
		this.rank = rank;
	}
	
	
	
	public Integer getRank() 
	{
		return rank;
	}
	
	
	
	public void setActive(final boolean active)
	{
		this.active = active;
	}
	
	
	
	public boolean isActive() 
	{
		return active;
	}
	
	
	
	public void setEnabledStartDate(final Date enabledStartDate)
	{
		this.enabledStartDate = enabledStartDate;
	}
	
	
	
	public Date getEnabledStartDate() 
	{
		return enabledStartDate;
	}
	
	
	
	public void setEnabledEndDate(final Date enabledEndDate)
	{
		this.enabledEndDate = enabledEndDate;
	}
	
	
	
	public Date getEnabledEndDate() 
	{
		return enabledEndDate;
	}
	
	
	
	public void setVariations(final List<VariationData> variations)
	{
		this.variations = variations;
	}
	
	
	
	public List<VariationData> getVariations() 
	{
		return variations;
	}
	


}