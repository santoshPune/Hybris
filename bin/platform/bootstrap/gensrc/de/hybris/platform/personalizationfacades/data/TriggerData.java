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
package de.hybris.platform.personalizationfacades.data;

import de.hybris.platform.personalizationfacades.data.VariationData;

public  class TriggerData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>TriggerData.code</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>TriggerData.variation</code> property defined at extension <code>personalizationfacades</code>. */
		
	private VariationData variation;
	
	public TriggerData()
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
	
	
	
	public void setVariation(final VariationData variation)
	{
		this.variation = variation;
	}
	
	
	
	public VariationData getVariation() 
	{
		return variation;
	}
	


}