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
package de.hybris.platform.personalizationwebservices.data;

import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.webservicescommons.dto.PaginationWsDTO;
import java.util.List;

public  class CustomizationListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CustomizationListWsDTO.pagination</code> property defined at extension <code>personalizationwebservices</code>. */
		
	private PaginationWsDTO pagination;

	/** <i>Generated property</i> for <code>CustomizationListWsDTO.customizations</code> property defined at extension <code>personalizationwebservices</code>. */
		
	private List<CustomizationData> customizations;
	
	public CustomizationListWsDTO()
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
	
	
	
	public void setCustomizations(final List<CustomizationData> customizations)
	{
		this.customizations = customizations;
	}
	
	
	
	public List<CustomizationData> getCustomizations() 
	{
		return customizations;
	}
	


}