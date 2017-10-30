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
package de.hybris.platform.commercefacades.order.data;

import java.util.List;

public  class DeliveryModesData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>DeliveryModesData.deliveryModes</code> property defined at extension <code>commercefacades</code>. */
		
	private List<? extends DeliveryModeData> deliveryModes;
	
	public DeliveryModesData()
	{
		// default constructor
	}
	
	
	
	public void setDeliveryModes(final List<? extends DeliveryModeData> deliveryModes)
	{
		this.deliveryModes = deliveryModes;
	}
	
	
	
	public List<? extends DeliveryModeData> getDeliveryModes() 
	{
		return deliveryModes;
	}
	


}