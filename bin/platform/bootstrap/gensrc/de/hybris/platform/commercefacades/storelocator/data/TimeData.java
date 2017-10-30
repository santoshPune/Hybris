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
package de.hybris.platform.commercefacades.storelocator.data;

public  class TimeData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>TimeData.hour</code> property defined at extension <code>commercefacades</code>. */
		
	private byte hour;

	/** <i>Generated property</i> for <code>TimeData.minute</code> property defined at extension <code>commercefacades</code>. */
		
	private byte minute;

	/** <i>Generated property</i> for <code>TimeData.formattedHour</code> property defined at extension <code>commercefacades</code>. */
		
	private String formattedHour;
	
	public TimeData()
	{
		// default constructor
	}
	
	
	
	public void setHour(final byte hour)
	{
		this.hour = hour;
	}
	
	
	
	public byte getHour() 
	{
		return hour;
	}
	
	
	
	public void setMinute(final byte minute)
	{
		this.minute = minute;
	}
	
	
	
	public byte getMinute() 
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