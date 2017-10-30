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
package de.hybris.platform.couponservices.rao;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CouponRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>CouponRAO.couponId</code> property defined at extension <code>couponservices</code>. */
	private String couponId;
	/** <i>Generated property</i> for <code>CouponRAO.couponCode</code> property defined at extension <code>couponservices</code>. */
	private String couponCode;
		
	public CouponRAO()
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
		
		
	public void setCouponCode(final String couponCode)
	{
		this.couponCode = couponCode;
	}
	
		
	public String getCouponCode() 
	{
		return couponCode;
	}
		
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
		.append("couponCode", couponCode)
		.append("couponId", couponId)
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
       CouponRAO other = (CouponRAO)obj;
       return new EqualsBuilder()
          .append(couponCode, other.couponCode)
          .append(couponId, other.couponId)
          .isEquals();
    }
    	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(couponCode).append(couponId).toHashCode();
	}
}