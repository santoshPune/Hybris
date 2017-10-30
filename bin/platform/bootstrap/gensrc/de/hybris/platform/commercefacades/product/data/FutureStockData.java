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
package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.commercefacades.product.data.StockData;
import java.util.Date;

public  class FutureStockData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>FutureStockData.stock</code> property defined at extension <code>commercefacades</code>. */
		
	private StockData stock;

	/** <i>Generated property</i> for <code>FutureStockData.date</code> property defined at extension <code>commercefacades</code>. */
		
	private Date date;

	/** <i>Generated property</i> for <code>FutureStockData.formattedDate</code> property defined at extension <code>commercefacades</code>. */
		
	private String formattedDate;
	
	public FutureStockData()
	{
		// default constructor
	}
	
	
	
	public void setStock(final StockData stock)
	{
		this.stock = stock;
	}
	
	
	
	public StockData getStock() 
	{
		return stock;
	}
	
	
	
	public void setDate(final Date date)
	{
		this.date = date;
	}
	
	
	
	public Date getDate() 
	{
		return date;
	}
	
	
	
	public void setFormattedDate(final String formattedDate)
	{
		this.formattedDate = formattedDate;
	}
	
	
	
	public String getFormattedDate() 
	{
		return formattedDate;
	}
	


}