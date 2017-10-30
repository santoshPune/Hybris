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
package de.hybris.platform.personalizationfacades.data;

import de.hybris.platform.personalizationfacades.data.CustomerData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import java.math.BigDecimal;

public  class CustomerSegmentationData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CustomerSegmentationData.code</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>CustomerSegmentationData.affinity</code> property defined at extension <code>personalizationfacades</code>. */
		
	private BigDecimal affinity;

	/** <i>Generated property</i> for <code>CustomerSegmentationData.customer</code> property defined at extension <code>personalizationfacades</code>. */
		
	private CustomerData customer;

	/** <i>Generated property</i> for <code>CustomerSegmentationData.segment</code> property defined at extension <code>personalizationfacades</code>. */
		
	private SegmentData segment;
	
	public CustomerSegmentationData()
	{
		// default constructor
	}
	
	
	
	public void setCode(final String code)
	{
		this.code = code;
	}
	
	
	
	public String getCode() 
	{
		return code;
	}
	
	
	
	public void setAffinity(final BigDecimal affinity)
	{
		this.affinity = affinity;
	}
	
	
	
	public BigDecimal getAffinity() 
	{
		return affinity;
	}
	
	
	
	public void setCustomer(final CustomerData customer)
	{
		this.customer = customer;
	}
	
	
	
	public CustomerData getCustomer() 
	{
		return customer;
	}
	
	
	
	public void setSegment(final SegmentData segment)
	{
		this.segment = segment;
	}
	
	
	
	public SegmentData getSegment() 
	{
		return segment;
	}
	


}