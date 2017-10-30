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
package de.hybris.platform.acceleratorfacades.product.data;

import de.hybris.platform.commercefacades.product.data.ProductData;

public  class ProductWrapperData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ProductWrapperData.productData</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private ProductData productData;

	/** <i>Generated property</i> for <code>ProductWrapperData.errorMsg</code> property defined at extension <code>acceleratorfacades</code>. */
		
	private String errorMsg;
	
	public ProductWrapperData()
	{
		// default constructor
	}
	
	
	
	public void setProductData(final ProductData productData)
	{
		this.productData = productData;
	}
	
	
	
	public ProductData getProductData() 
	{
		return productData;
	}
	
	
	
	public void setErrorMsg(final String errorMsg)
	{
		this.errorMsg = errorMsg;
	}
	
	
	
	public String getErrorMsg() 
	{
		return errorMsg;
	}
	


}