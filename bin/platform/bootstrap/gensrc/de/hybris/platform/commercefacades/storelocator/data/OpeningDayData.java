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

import de.hybris.platform.commercefacades.storelocator.data.TimeData;

public  class OpeningDayData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>OpeningDayData.openingTime</code> property defined at extension <code>commercefacades</code>. */
		
	private TimeData openingTime;

	/** <i>Generated property</i> for <code>OpeningDayData.closingTime</code> property defined at extension <code>commercefacades</code>. */
		
	private TimeData closingTime;
	
	public OpeningDayData()
	{
		// default constructor
	}
	
	
	
	public void setOpeningTime(final TimeData openingTime)
	{
		this.openingTime = openingTime;
	}
	
	
	
	public TimeData getOpeningTime() 
	{
		return openingTime;
	}
	
	
	
	public void setClosingTime(final TimeData closingTime)
	{
		this.closingTime = closingTime;
	}
	
	
	
	public TimeData getClosingTime() 
	{
		return closingTime;
	}
	


}