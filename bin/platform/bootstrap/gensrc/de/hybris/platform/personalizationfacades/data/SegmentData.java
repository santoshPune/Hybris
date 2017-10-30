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

import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentTriggerData;
import java.util.List;

public  class SegmentData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SegmentData.code</code> property defined at extension <code>personalizationfacades</code>. */
		
	private String code;

	/** <i>Generated property</i> for <code>SegmentData.customerLinks</code> property defined at extension <code>personalizationfacades</code>. */
		
	private List<CustomerSegmentationData> customerLinks;

	/** <i>Generated property</i> for <code>SegmentData.segmentTriggers</code> property defined at extension <code>personalizationfacades</code>. */
		
	private List<SegmentTriggerData> segmentTriggers;
	
	public SegmentData()
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
	
	
	
	public void setCustomerLinks(final List<CustomerSegmentationData> customerLinks)
	{
		this.customerLinks = customerLinks;
	}
	
	
	
	public List<CustomerSegmentationData> getCustomerLinks() 
	{
		return customerLinks;
	}
	
	
	
	public void setSegmentTriggers(final List<SegmentTriggerData> segmentTriggers)
	{
		this.segmentTriggers = segmentTriggers;
	}
	
	
	
	public List<SegmentTriggerData> getSegmentTriggers() 
	{
		return segmentTriggers;
	}
	


}