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
package de.hybris.platform.commercefacades.storelocator.data;

import de.hybris.platform.commercefacades.product.data.StockData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;

public  class PointOfServiceStockData extends PointOfServiceData 
{


	/** <i>Generated property</i> for <code>PointOfServiceStockData.stockData</code> property defined at extension <code>commercefacades</code>. */
		
	private StockData stockData;
	
	public PointOfServiceStockData()
	{
		// default constructor
	}
	
	
	
	public void setStockData(final StockData stockData)
	{
		this.stockData = stockData;
	}
	
	
	
	public StockData getStockData() 
	{
		return stockData;
	}
	


}