/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.couponservices.service.data;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CouponCodeDetails  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>CouponCodeDetails.couponId</code> property defined at extension <code>couponservices</code>. */
	private String couponId;
	/** <i>Generated property</i> for <code>CouponCodeDetails.code</code> property defined at extension <code>couponservices</code>. */
	private String code;
	/** <i>Generated property</i> for <code>CouponCodeDetails.redeemed</code> property defined at extension <code>couponservices</code>. */
	private Boolean redeemed;
		
	public CouponCodeDetails()
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
		
		
	public void setCode(final String code)
	{
		this.code = code;
	}
	
		
	public String getCode() 
	{
		return code;
	}
		
		
	public void setRedeemed(final Boolean redeemed)
	{
		this.redeemed = redeemed;
	}
	
		
	public Boolean getRedeemed() 
	{
		return redeemed;
	}
		
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("code", code)
		.toString();
	}

 	@Override
    public boolean equals(Object obj)
    {
       if (obj == null || !this.getClass().equals(obj.getClass())) {
         return false;
       } else if (obj == this) {
         return true;
       }
       CouponCodeDetails other = (CouponCodeDetails)obj;
       return new EqualsBuilder()
          .append(code, other.code)
          .isEquals();
    }
    	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(code).toHashCode();
	}
}