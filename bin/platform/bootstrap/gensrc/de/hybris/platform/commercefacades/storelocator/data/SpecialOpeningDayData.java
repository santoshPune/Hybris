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

import de.hybris.platform.commercefacades.storelocator.data.OpeningDayData;
import java.util.Date;

public  class SpecialOpeningDayData extends OpeningDayData 
{


	/** <i>Generated property</i> for <code>SpecialOpeningDayData.date</code> property defined at extension <code>commercefacades</code>. */
		
	private Date date;

	/** <i>Generated property</i> for <code>SpecialOpeningDayData.formattedDate</code> property defined at extension <code>commercefacades</code>. */
		
	private String formattedDate;

	/** <i>Generated property</i> for <code>SpecialOpeningDayData.closed</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean closed;

	/** <i>Generated property</i> for <code>SpecialOpeningDayData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>SpecialOpeningDayData.comment</code> property defined at extension <code>commercefacades</code>. */
		
	private String comment;
	
	public SpecialOpeningDayData()
	{
		// default constructor
	}
	
	
	
	public void setDate(final Date date)
	{
		this.date = date;
	}
	
	
	
	public Date getDate() 
	{
		return date;
	}
	
	
	
	public void setFormattedDate(final String formattedDate)
	{
		this.formattedDate = formattedDate;
	}
	
	
	
	public String getFormattedDate() 
	{
		return formattedDate;
	}
	
	
	
	public void setClosed(final boolean closed)
	{
		this.closed = closed;
	}
	
	
	
	public boolean isClosed() 
	{
		return closed;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setComment(final String comment)
	{
		this.comment = comment;
	}
	
	
	
	public String getComment() 
	{
		return comment;
	}
	


}