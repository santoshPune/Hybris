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

import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import java.util.List;

public  class CustomerSegmentationListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CustomerSegmentationListWsDTO.pagination</code> property defined at extension <code>personalizationwebservices</code>. */
		
	private PaginationWsDTO pagination;

	/** <i>Generated property</i> for <code>CustomerSegmentationListWsDTO.customerSegmentations</code> property defined at extension <code>personalizationwebservices</code>. */
		
	private List<CustomerSegmentationData> customerSegmentations;
	
	public CustomerSegmentationListWsDTO()
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
	
	
	
	public void setCustomerSegmentations(final List<CustomerSegmentationData> customerSegmentations)
	{
		this.customerSegmentations = customerSegmentations;
	}
	
	
	
	public List<CustomerSegmentationData> getCustomerSegmentations() 
	{
		return customerSegmentations;
	}
	


}