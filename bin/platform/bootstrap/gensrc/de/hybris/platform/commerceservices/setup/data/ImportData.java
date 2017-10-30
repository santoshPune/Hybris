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
package de.hybris.platform.commerceservices.setup.data;

import java.util.List;

public  class ImportData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ImportData.productCatalogName</code> property defined at extension <code>commerceservices</code>. */
		
	private String productCatalogName;

	/** <i>Generated property</i> for <code>ImportData.contentCatalogNames</code> property defined at extension <code>commerceservices</code>. */
		
	private List<String> contentCatalogNames;

	/** <i>Generated property</i> for <code>ImportData.storeNames</code> property defined at extension <code>commerceservices</code>. */
		
	private List<String> storeNames;
	
	public ImportData()
	{
		// default constructor
	}
	
	
	
	public void setProductCatalogName(final String productCatalogName)
	{
		this.productCatalogName = productCatalogName;
	}
	
	
	
	public String getProductCatalogName() 
	{
		return productCatalogName;
	}
	
	
	
	public void setContentCatalogNames(final List<String> contentCatalogNames)
	{
		this.contentCatalogNames = contentCatalogNames;
	}
	
	
	
	public List<String> getContentCatalogNames() 
	{
		return contentCatalogNames;
	}
	
	
	
	public void setStoreNames(final List<String> storeNames)
	{
		this.storeNames = storeNames;
	}
	
	
	
	public List<String> getStoreNames() 
	{
		return storeNames;
	}
	


}