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

import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.commercefacades.order.data.ConfigurationInfoData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import java.util.List;
import java.util.Map;

public  class OrderEntryData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>OrderEntryData.entryNumber</code> property defined at extension <code>commercefacades</code>. */
		
	private Integer entryNumber;

	/** <i>Generated property</i> for <code>OrderEntryData.quantity</code> property defined at extension <code>commercefacades</code>. */
		
	private Long quantity;

	/** <i>Generated property</i> for <code>OrderEntryData.basePrice</code> property defined at extension <code>commercefacades</code>. */
		
	private PriceData basePrice;

	/** <i>Generated property</i> for <code>OrderEntryData.totalPrice</code> property defined at extension <code>commercefacades</code>. */
		
	private PriceData totalPrice;

	/** <i>Generated property</i> for <code>OrderEntryData.product</code> property defined at extension <code>commercefacades</code>. */
		
	private ProductData product;

	/** <i>Generated property</i> for <code>OrderEntryData.updateable</code> property defined at extension <code>commercefacades</code>. */
		
	private boolean updateable;

	/** <i>Generated property</i> for <code>OrderEntryData.deliveryMode</code> property defined at extension <code>commercefacades</code>. */
		
	private DeliveryModeData deliveryMode;

	/** <i>Generated property</i> for <code>OrderEntryData.deliveryPointOfService</code> property defined at extension <code>commercefacades</code>. */
		
	private PointOfServiceData deliveryPointOfService;

	/** <i>Generated property</i> for <code>OrderEntryData.entries</code> property defined at extension <code>commercefacades</code>. */
		
	private List<OrderEntryData> entries;

	/** <i>Generated property</i> for <code>OrderEntryData.configurationInfos</code> property defined at extension <code>commercefacades</code>. */
		
	private List<ConfigurationInfoData> configurationInfos;

	/** <i>Generated property</i> for <code>OrderEntryData.statusSummaryMap</code> property defined at extension <code>commercefacades</code>. */
		
	private Map<ProductInfoStatus, Integer> statusSummaryMap;
	
	public OrderEntryData()
	{
		// default constructor
	}
	
	
	
	public void setEntryNumber(final Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}
	
	
	
	public Integer getEntryNumber() 
	{
		return entryNumber;
	}
	
	
	
	public void setQuantity(final Long quantity)
	{
		this.quantity = quantity;
	}
	
	
	
	public Long getQuantity() 
	{
		return quantity;
	}
	
	
	
	public void setBasePrice(final PriceData basePrice)
	{
		this.basePrice = basePrice;
	}
	
	
	
	public PriceData getBasePrice() 
	{
		return basePrice;
	}
	
	
	
	public void setTotalPrice(final PriceData totalPrice)
	{
		this.totalPrice = totalPrice;
	}
	
	
	
	public PriceData getTotalPrice() 
	{
		return totalPrice;
	}
	
	
	
	public void setProduct(final ProductData product)
	{
		this.product = product;
	}
	
	
	
	public ProductData getProduct() 
	{
		return product;
	}
	
	
	
	public void setUpdateable(final boolean updateable)
	{
		this.updateable = updateable;
	}
	
	
	
	public boolean isUpdateable() 
	{
		return updateable;
	}
	
	
	
	public void setDeliveryMode(final DeliveryModeData deliveryMode)
	{
		this.deliveryMode = deliveryMode;
	}
	
	
	
	public DeliveryModeData getDeliveryMode() 
	{
		return deliveryMode;
	}
	
	
	
	public void setDeliveryPointOfService(final PointOfServiceData deliveryPointOfService)
	{
		this.deliveryPointOfService = deliveryPointOfService;
	}
	
	
	
	public PointOfServiceData getDeliveryPointOfService() 
	{
		return deliveryPointOfService;
	}
	
	
	
	public void setEntries(final List<OrderEntryData> entries)
	{
		this.entries = entries;
	}
	
	
	
	public List<OrderEntryData> getEntries() 
	{
		return entries;
	}
	
	
	
	public void setConfigurationInfos(final List<ConfigurationInfoData> configurationInfos)
	{
		this.configurationInfos = configurationInfos;
	}
	
	
	
	public List<ConfigurationInfoData> getConfigurationInfos() 
	{
		return configurationInfos;
	}
	
	
	
	public void setStatusSummaryMap(final Map<ProductInfoStatus, Integer> statusSummaryMap)
	{
		this.statusSummaryMap = statusSummaryMap;
	}
	
	
	
	public Map<ProductInfoStatus, Integer> getStatusSummaryMap() 
	{
		return statusSummaryMap;
	}
	


}