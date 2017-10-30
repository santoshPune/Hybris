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
package de.hybris.platform.commercewebservicescommons.dto.store;

public  class TimeWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>TimeWsDTO.hour</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Byte hour;

	/** <i>Generated property</i> for <code>TimeWsDTO.minute</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Byte minute;

	/** <i>Generated property</i> for <code>TimeWsDTO.formattedHour</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String formattedHour;
	
	public TimeWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setHour(final Byte hour)
	{
		this.hour = hour;
	}
	
	
	
	public Byte getHour() 
	{
		return hour;
	}
	
	
	
	public void setMinute(final Byte minute)
	{
		this.minute = minute;
	}
	
	
	
	public Byte getMinute() 
	{
		return minute;
	}
	
	
	
	public void setFormattedHour(final String formattedHour)
	{
		this.formattedHour = formattedHour;
	}
	
	
	
	public String getFormattedHour() 
	{
		return formattedHour;
	}
	


}