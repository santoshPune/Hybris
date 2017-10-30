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
package de.hybris.platform.personalizationwebservices.data;

import de.hybris.platform.personalizationfacades.data.SegmentData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import java.util.List;

public  class SegmentListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SegmentListWsDTO.pagination</code> property defined at extension <code>personalizationwebservices</code>. */
		
	private PaginationWsDTO pagination;

	/** <i>Generated property</i> for <code>SegmentListWsDTO.segments</code> property defined at extension <code>personalizationwebservices</code>. */
		
	private List<SegmentData> segments;
	
	public SegmentListWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setPagination(final PaginationWsDTO pagination)
	{
		this.pagination = pagination;
	}
	
	
	
	public PaginationWsDTO getPagination() 
	{
		return pagination;
	}
	
	
	
	public void setSegments(final List<SegmentData> segments)
	{
		this.segments = segments;
	}
	
	
	
	public List<SegmentData> getSegments() 
	{
		return segments;
	}
	


}