/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
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

public class DiscountValueRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>DiscountValueRAO.value</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal value;
	/** <i>Generated property</i> for <code>DiscountValueRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String currencyIsoCode;
		
	public DiscountValueRAO()
	{
		// default constructor
	}
	
		
	public void setValue(final BigDecimal value)
	{
		this.value = value;
	}
	
		
	public BigDecimal getValue() 
	{
		return value;
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
	public boolean equals(Object that)
	{
		return EqualsBuilder.reflectionEquals(this, that);
	}
	
	@Override
	public int hashCode()
	{
		return HashCodeBuilder.reflectionHashCode(this);
	}
}