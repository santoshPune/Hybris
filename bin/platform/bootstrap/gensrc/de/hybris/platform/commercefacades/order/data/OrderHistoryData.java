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

import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.core.enums.OrderStatus;
import java.util.Date;

public  class OrderHistoryData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>OrderHistoryData.code</code> property defined at extension <code>commercefacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>OrderHistoryData.status</code> property defined at extension <code>commercefacades</code>. */
		
	private OrderStatus status;

	/** <i>Generated property</i> for <code>OrderHistoryData.statusDisplay</code> property defined at extension <code>commercefacades</code>. */
		
	private String statusDisplay;

	/** <i>Generated property</i> for <code>OrderHistoryData.placed</code> property defined at extension <code>commercefacades</code>. */
		
	private Date placed;

	/** <i>Generated property</i> for <code>OrderHistoryData.guid</code> property defined at extension <code>commercefacades</code>. */
		
	private String guid;

	/** <i>Generated property</i> for <code>OrderHistoryData.total</code> property defined at extension <code>commercefacades</code>. */
		
	private PriceData total;
	
	public OrderHistoryData()
	{
		// default constructor
	}
	
	
	
	public void setCode(final String code)
	{
		this.code = code;
	}
	
	
	
	public String getCode() 
	{
		return code;
	}
	
	
	
	public void setStatus(final OrderStatus status)
	{
		this.status = status;
	}
	
	
	
	public OrderStatus getStatus() 
	{
		return status;
	}
	
	
	
	public void setStatusDisplay(final String statusDisplay)
	{
		this.statusDisplay = statusDisplay;
	}
	
	
	
	public String getStatusDisplay() 
	{
		return statusDisplay;
	}
	
	
	
	public void setPlaced(final Date placed)
	{
		this.placed = placed;
	}
	
	
	
	public Date getPlaced() 
	{
		return placed;
	}
	
	
	
	public void setGuid(final String guid)
	{
		this.guid = guid;
	}
	
	
	
	public String getGuid() 
	{
		return guid;
	}
	
	
	
	public void setTotal(final PriceData total)
	{
		this.total = total;
	}
	
	
	
	public PriceData getTotal() 
	{
		return total;
	}
	


}