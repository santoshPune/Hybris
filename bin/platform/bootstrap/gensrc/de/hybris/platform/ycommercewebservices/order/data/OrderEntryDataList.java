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
package de.hybris.platform.ycommercewebservices.order.data;

import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import java.util.List;

public  class OrderEntryDataList  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>OrderEntryDataList.orderEntries</code> property defined at extension <code>ycommercewebservices</code>. */
		
	private List<OrderEntryData> orderEntries;
	
	public OrderEntryDataList()
	{
		// default constructor
	}
	
	
	
	public void setOrderEntries(final List<OrderEntryData> orderEntries)
	{
		this.orderEntries = orderEntries;
	}
	
	
	
	public List<OrderEntryData> getOrderEntries() 
	{
		return orderEntries;
	}
	


}