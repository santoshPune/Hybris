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
package de.hybris.platform.commerceservices.storefinder.data;

import de.hybris.platform.storelocator.model.PointOfServiceModel;

public  class PointOfServiceDistanceData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>PointOfServiceDistanceData.pointOfService</code> property defined at extension <code>commerceservices</code>. */
		
	private PointOfServiceModel pointOfService;

	/** <i>Generated property</i> for <code>PointOfServiceDistanceData.distanceKm</code> property defined at extension <code>commerceservices</code>. */
		
	private double distanceKm;
	
	public PointOfServiceDistanceData()
	{
		// default constructor
	}
	
	
	
	public void setPointOfService(final PointOfServiceModel pointOfService)
	{
		this.pointOfService = pointOfService;
	}
	
	
	
	public PointOfServiceModel getPointOfService() 
	{
		return pointOfService;
	}
	
	
	
	public void setDistanceKm(final double distanceKm)
	{
		this.distanceKm = distanceKm;
	}
	
	
	
	public double getDistanceKm() 
	{
		return distanceKm;
	}
	


}