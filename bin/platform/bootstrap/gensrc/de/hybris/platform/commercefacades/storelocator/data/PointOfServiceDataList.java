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

import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import java.util.List;

public  class PointOfServiceDataList  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>PointOfServiceDataList.pointOfServices</code> property defined at extension <code>acceleratorwebservicesaddon</code>. */
		
	private List<PointOfServiceData> pointOfServices;
	
	public PointOfServiceDataList()
	{
		// default constructor
	}
	
	
	
	public void setPointOfServices(final List<PointOfServiceData> pointOfServices)
	{
		this.pointOfServices = pointOfServices;
	}
	
	
	
	public List<PointOfServiceData> getPointOfServices() 
	{
		return pointOfServices;
	}
	


}