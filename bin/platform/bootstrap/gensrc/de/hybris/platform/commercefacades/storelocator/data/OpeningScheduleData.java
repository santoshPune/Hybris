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
package de.hybris.platform.commercefacades.storelocator.data;

import de.hybris.platform.commercefacades.storelocator.data.SpecialOpeningDayData;
import de.hybris.platform.commercefacades.storelocator.data.WeekdayOpeningDayData;
import java.util.List;

public  class OpeningScheduleData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>OpeningScheduleData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>OpeningScheduleData.code</code> property defined at extension <code>commercefacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>OpeningScheduleData.weekDayOpeningList</code> property defined at extension <code>commercefacades</code>. */
		
	private List<WeekdayOpeningDayData> weekDayOpeningList;

	/** <i>Generated property</i> for <code>OpeningScheduleData.specialDayOpeningList</code> property defined at extension <code>commercefacades</code>. */
		
	private List<SpecialOpeningDayData> specialDayOpeningList;
	
	public OpeningScheduleData()
	{
		// default constructor
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setCode(final String code)
	{
		this.code = code;
	}
	
	
	
	public String getCode() 
	{
		return code;
	}
	
	
	
	public void setWeekDayOpeningList(final List<WeekdayOpeningDayData> weekDayOpeningList)
	{
		this.weekDayOpeningList = weekDayOpeningList;
	}
	
	
	
	public List<WeekdayOpeningDayData> getWeekDayOpeningList() 
	{
		return weekDayOpeningList;
	}
	
	
	
	public void setSpecialDayOpeningList(final List<SpecialOpeningDayData> specialDayOpeningList)
	{
		this.specialDayOpeningList = specialDayOpeningList;
	}
	
	
	
	public List<SpecialOpeningDayData> getSpecialDayOpeningList() 
	{
		return specialDayOpeningList;
	}
	


}