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
package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.order.data.OrderEntryGroupData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

public  class PickupOrderEntryGroupData extends OrderEntryGroupData 
{


	/** <i>Generated property</i> for <code>PickupOrderEntryGroupData.deliveryPointOfService</code> property defined at extension <code>commercefacades</code>. */
		
	private PointOfServiceData deliveryPointOfService;

	/** <i>Generated property</i> for <code>PickupOrderEntryGroupData.distance</code> property defined at extension <code>commercefacades</code>. */
		
	private Double distance;
	
	public PickupOrderEntryGroupData()
	{
		// default constructor
	}
	
	
	
	public void setDeliveryPointOfService(final PointOfServiceData deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}
	
	
	
	public PointOfServiceData getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	
	
	
	public void setDistance(final Double distance)
	{
		this.distance = distance;
	}
	
	
	
	public Double getDistance() 
	{
		return distance;
	}
	


}