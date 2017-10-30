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

import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.user.CustomerModel;

public  class CsCreateAddressForm  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CsCreateAddressForm.owner</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private CustomerModel owner;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.addressLine1</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String addressLine1;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.addressLine2</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String addressLine2;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.town</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String town;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.country</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private CountryModel country;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.region</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private RegionModel region;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.postalcode</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String postalcode;

	/** <i>Generated property</i> for <code>CsCreateAddressForm.phone1</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String phone1;
	
	public CsCreateAddressForm()
	{
		// default constructor
	}
	
	
	
	public void setOwner(final CustomerModel owner)
	{
		this.owner = owner;
	}
	
	
	
	public CustomerModel getOwner() 
	{
		return owner;
	}
	
	
	
	public void setAddressLine1(final String addressLine1)
	{
		this.addressLine1 = addressLine1;
	}
	
	
	
	public String getAddressLine1() 
	{
		return addressLine1;
	}
	
	
	
	public void setAddressLine2(final String addressLine2)
	{
		this.addressLine2 = addressLine2;
	}
	
	
	
	public String getAddressLine2() 
	{
		return addressLine2;
	}
	
	
	
	public void setTown(final String town)
	{
		this.town = town;
	}
	
	
	
	public String getTown() 
	{
		return town;
	}
	
	
	
	public void setCountry(final CountryModel country)
	{
		this.country = country;
	}
	
	
	
	public CountryModel getCountry() 
	{
		return country;
	}
	
	
	
	public void setRegion(final RegionModel region)
	{
		this.region = region;
	}
	
	
	
	public RegionModel getRegion() 
	{
		return region;
	}
	
	
	
	public void setPostalcode(final String postalcode)
	{
		this.postalcode = postalcode;
	}
	
	
	
	public String getPostalcode() 
	{
		return postalcode;
	}
	
	
	
	public void setPhone1(final String phone1)
	{
		this.phone1 = phone1;
	}
	
	
	
	public String getPhone1() 
	{
		return phone1;
	}
	


}