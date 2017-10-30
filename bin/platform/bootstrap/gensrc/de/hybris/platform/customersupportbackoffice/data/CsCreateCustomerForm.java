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
package de.hybris.platform.customersupportbackoffice.data;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.TitleModel;

public  class CsCreateCustomerForm  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CsCreateCustomerForm.title</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private TitleModel title;

	/** <i>Generated property</i> for <code>CsCreateCustomerForm.name</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>CsCreateCustomerForm.email</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String email;

	/** <i>Generated property</i> for <code>CsCreateCustomerForm.site</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private BaseSiteModel site;

	/** <i>Generated property</i> for <code>CsCreateCustomerForm.address</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private AddressModel address;
	
	public CsCreateCustomerForm()
	{
		// default constructor
	}
	
	
	
	public void setTitle(final TitleModel title)
	{
		this.title = title;
	}
	
	
	
	public TitleModel getTitle() 
	{
		return title;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setEmail(final String email)
	{
		this.email = email;
	}
	
	
	
	public String getEmail() 
	{
		return email;
	}
	
	
	
	public void setSite(final BaseSiteModel site)
	{
		this.site = site;
	}
	
	
	
	public BaseSiteModel getSite() 
	{
		return site;
	}
	
	
	
	public void setAddress(final AddressModel address)
	{
		this.address = address;
	}
	
	
	
	public AddressModel getAddress() 
	{
		return address;
	}
	


}