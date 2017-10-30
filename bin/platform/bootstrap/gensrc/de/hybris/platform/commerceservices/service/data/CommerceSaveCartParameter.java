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

import de.hybris.platform.core.model.order.CartModel;

public  class CommerceSaveCartParameter  implements java.io.Serializable 
{


	/** The CartModel to be saved<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.cart</code> property defined at extension <code>commerceservices</code>. */
		
	private CartModel cart;

	/** The name of the saved cart provided by user or auto-generated<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.name</code> property defined at extension <code>commerceservices</code>. */
		
	private String name;

	/** The description of the saved cart provided by user or auto-generated<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.description</code> property defined at extension <code>commerceservices</code>. */
		
	private String description;

	/** Should the method hooks be executed<br/><br/><i>Generated property</i> for <code>CommerceSaveCartParameter.enableHooks</code> property defined at extension <code>commerceservices</code>. */
		
	private boolean enableHooks;
	
	public CommerceSaveCartParameter()
	{
		// default constructor
	}
	
	
	
	public void setCart(final CartModel cart)
	{
		this.cart = cart;
	}
	
	
	
	public CartModel getCart() 
	{
		return cart;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setDescription(final String description)
	{
		this.description = description;
	}
	
	
	
	public String getDescription() 
	{
		return description;
	}
	
	
	
	public void setEnableHooks(final boolean enableHooks)
	{
		this.enableHooks = enableHooks;
	}
	
	
	
	public boolean isEnableHooks() 
	{
		return enableHooks;
	}
	


}