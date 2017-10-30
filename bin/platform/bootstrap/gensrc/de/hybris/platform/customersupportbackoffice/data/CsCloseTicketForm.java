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

import de.hybris.platform.ticket.enums.CsResolutionType;

public  class CsCloseTicketForm  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CsCloseTicketForm.resolution</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private CsResolutionType resolution;

	/** <i>Generated property</i> for <code>CsCloseTicketForm.message</code> property defined at extension <code>customersupportbackoffice</code>. */
		
	private String message;
	
	public CsCloseTicketForm()
	{
		// default constructor
	}
	
	
	
	public void setResolution(final CsResolutionType resolution)
	{
		this.resolution = resolution;
	}
	
	
	
	public CsResolutionType getResolution() 
	{
		return resolution;
	}
	
	
	
	public void setMessage(final String message)
	{
		this.message = message;
	}
	
	
	
	public String getMessage() 
	{
		return message;
	}
	


}