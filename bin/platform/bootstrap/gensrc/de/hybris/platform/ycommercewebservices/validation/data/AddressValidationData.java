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
package de.hybris.platform.ycommercewebservices.validation.data;

import de.hybris.platform.webservicescommons.dto.error.ErrorListWsDTO;
import de.hybris.platform.ycommercewebservices.user.data.AddressDataList;

public  class AddressValidationData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>AddressValidationData.errors</code> property defined at extension <code>ycommercewebservices</code>. */
		
	private ErrorListWsDTO errors;

	/** <i>Generated property</i> for <code>AddressValidationData.decision</code> property defined at extension <code>ycommercewebservices</code>. */
		
	private String decision;

	/** <i>Generated property</i> for <code>AddressValidationData.suggestedAddressesList</code> property defined at extension <code>ycommercewebservices</code>. */
		
	private AddressDataList suggestedAddressesList;
	
	public AddressValidationData()
	{
		// default constructor
	}
	
	
	
	public void setErrors(final ErrorListWsDTO errors)
	{
		this.errors = errors;
	}
	
	
	
	public ErrorListWsDTO getErrors() 
	{
		return errors;
	}
	
	
	
	public void setDecision(final String decision)
	{
		this.decision = decision;
	}
	
	
	
	public String getDecision() 
	{
		return decision;
	}
	
	
	
	public void setSuggestedAddressesList(final AddressDataList suggestedAddressesList)
	{
		this.suggestedAddressesList = suggestedAddressesList;
	}
	
	
	
	public AddressDataList getSuggestedAddressesList() 
	{
		return suggestedAddressesList;
	}
	


}