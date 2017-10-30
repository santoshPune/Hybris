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

import  de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.catalog.enums.ProductReferenceTypeEnum;
import de.hybris.platform.commerceservices.product.data.ReferenceData;

public  class ProductReferenceData extends ReferenceData<ProductReferenceTypeEnum,ProductData> 
{


	/** <i>Generated property</i> for <code>ProductReferenceData.preselected</code> property defined at extension <code>commercefacades</code>. */
		
	private Boolean preselected;
	
	public ProductReferenceData()
	{
		// default constructor
	}
	
	
	
	public void setPreselected(final Boolean preselected)
	{
		this.preselected = preselected;
	}
	
	
	
	public Boolean getPreselected() 
	{
		return preselected;
	}
	


}