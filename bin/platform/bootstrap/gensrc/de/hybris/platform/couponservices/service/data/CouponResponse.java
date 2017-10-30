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
package de.hybris.platform.couponservices.service.data;

import de.hybris.platform.couponservices.service.data.CouponCodeDetails;

public  class CouponResponse  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CouponResponse.couponId</code> property defined at extension <code>couponservices</code>. */
		
	private String couponId;

	/** <i>Generated property</i> for <code>CouponResponse.success</code> property defined at extension <code>couponservices</code>. */
		
	private Boolean success;

	/** <i>Generated property</i> for <code>CouponResponse.message</code> property defined at extension <code>couponservices</code>. */
		
	private String message;

	/** <i>Generated property</i> for <code>CouponResponse.couponCodeDetails</code> property defined at extension <code>couponservices</code>. */
		
	private CouponCodeDetails couponCodeDetails;
	
	public CouponResponse()
	{
		// default constructor
	}
	
	
	
	public void setCouponId(final String couponId)
	{
		this.couponId = couponId;
	}
	
	
	
	public String getCouponId() 
	{
		return couponId;
	}
	
	
	
	public void setSuccess(final Boolean success)
	{
		this.success = success;
	}
	
	
	
	public Boolean getSuccess() 
	{
		return success;
	}
	
	
	
	public void setMessage(final String message)
	{
		this.message = message;
	}
	
	
	
	public String getMessage() 
	{
		return message;
	}
	
	
	
	public void setCouponCodeDetails(final CouponCodeDetails couponCodeDetails)
	{
		this.couponCodeDetails = couponCodeDetails;
	}
	
	
	
	public CouponCodeDetails getCouponCodeDetails() 
	{
		return couponCodeDetails;
	}
	


}