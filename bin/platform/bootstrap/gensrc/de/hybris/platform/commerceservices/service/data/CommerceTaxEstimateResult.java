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
package de.hybris.platform.commerceservices.service.data;

import java.math.BigDecimal;

public  class CommerceTaxEstimateResult  implements java.io.Serializable 
{


	/** the total tax<br/><br/><i>Generated property</i> for <code>CommerceTaxEstimateResult.tax</code> property defined at extension <code>commerceservices</code>. */
		
	private BigDecimal tax;
	
	public CommerceTaxEstimateResult()
	{
		// default constructor
	}
	
	
	
	public void setTax(final BigDecimal tax)
	{
		this.tax = tax;
	}
	
	
	
	public BigDecimal getTax() 
	{
		return tax;
	}
	


}