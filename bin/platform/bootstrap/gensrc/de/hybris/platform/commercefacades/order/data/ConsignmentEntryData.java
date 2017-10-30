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

import de.hybris.platform.commercefacades.order.data.OrderEntryData;

public  class ConsignmentEntryData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ConsignmentEntryData.orderEntry</code> property defined at extension <code>commercefacades</code>. */
		
	private OrderEntryData orderEntry;

	/** <i>Generated property</i> for <code>ConsignmentEntryData.quantity</code> property defined at extension <code>commercefacades</code>. */
		
	private Long quantity;

	/** <i>Generated property</i> for <code>ConsignmentEntryData.shippedQuantity</code> property defined at extension <code>commercefacades</code>. */
		
	private Long shippedQuantity;
	
	public ConsignmentEntryData()
	{
		// default constructor
	}
	
	
	
	public void setOrderEntry(final OrderEntryData orderEntry)
	{
		this.orderEntry = orderEntry;
	}
	
	
	
	public OrderEntryData getOrderEntry() 
	{
		return orderEntry;
	}
	
	
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}
	
	
	
	public Long getQuantity() 
	{
		return quantity;
	}
	
	
	
	public void setShippedQuantity(final Long shippedQuantity)
	{
		this.shippedQuantity = shippedQuantity;
	}
	
	
	
	public Long getShippedQuantity() 
	{
		return shippedQuantity;
	}
	


}