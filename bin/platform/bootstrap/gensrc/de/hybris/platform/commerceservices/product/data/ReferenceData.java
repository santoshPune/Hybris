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
package de.hybris.platform.commerceservices.product.data;

public  class ReferenceData<TYPE, TARGET>  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.referenceType</code> property defined at extension <code>commerceservices</code>. */
		
	private TYPE referenceType;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.description</code> property defined at extension <code>commerceservices</code>. */
		
	private String description;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.quantity</code> property defined at extension <code>commerceservices</code>. */
		
	private Integer quantity;

	/** <i>Generated property</i> for <code>ReferenceData<TYPE, TARGET>.target</code> property defined at extension <code>commerceservices</code>. */
		
	private TARGET target;
	
	public ReferenceData()
	{
		// default constructor
	}
	
	
	
	public void setReferenceType(final TYPE referenceType)
	{
		this.referenceType = referenceType;
	}
	
	
	
	public TYPE getReferenceType() 
	{
		return referenceType;
	}
	
	
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	
	
	public String getDescription() 
	{
		return description;
	}
	
	
	
	public void setQuantity(final Integer quantity)
	{
		this.quantity = quantity;
	}
	
	
	
	public Integer getQuantity() 
	{
		return quantity;
	}
	
	
	
	public void setTarget(final TARGET target)
	{
		this.target = target;
	}
	
	
	
	public TARGET getTarget() 
	{
		return target;
	}
	


}