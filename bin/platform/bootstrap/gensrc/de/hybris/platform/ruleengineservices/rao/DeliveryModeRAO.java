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
package de.hybris.platform.ruleengineservices.rao;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DeliveryModeRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>DeliveryModeRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
	/** <i>Generated property</i> for <code>DeliveryModeRAO.cost</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal cost;
	/** <i>Generated property</i> for <code>DeliveryModeRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String currencyIsoCode;
		
	public DeliveryModeRAO()
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
		
		
	public void setCost(final BigDecimal cost)
	{
		this.cost = cost;
	}
	
		
	public BigDecimal getCost() 
	{
		return cost;
	}
		
		
	public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}
	
		
	public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
		
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("code", code).toString();
	}

 	@Override
    public boolean equals(Object obj)
    {
       if (obj == null || !this.getClass().equals(obj.getClass())) {
         return false;
       } else if (obj == this) {
         return true;
       }
       DeliveryModeRAO other = (DeliveryModeRAO)obj;
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