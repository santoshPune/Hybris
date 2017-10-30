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

import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class OrderEntryConsumedRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.firedRuleCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String firedRuleCode;
	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.orderEntry</code> property defined at extension <code>ruleengineservices</code>. */
	private OrderEntryRAO orderEntry;
	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.quantity</code> property defined at extension <code>ruleengineservices</code>. */
	private int quantity;
	/** <i>Generated property</i> for <code>OrderEntryConsumedRAO.adjustedUnitPrice</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal adjustedUnitPrice;
		
	public OrderEntryConsumedRAO()
	{
		// default constructor
	}
	
		
	public void setFiredRuleCode(final String firedRuleCode)
	{
		this.firedRuleCode = firedRuleCode;
	}
	
		
	public String getFiredRuleCode() 
	{
		return firedRuleCode;
	}
		
		
	public void setOrderEntry(final OrderEntryRAO orderEntry)
	{
		this.orderEntry = orderEntry;
	}
	
		
	public OrderEntryRAO getOrderEntry() 
	{
		return orderEntry;
	}
		
		
	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}
	
		
	public int getQuantity() 
	{
		return quantity;
	}
		
		
	public void setAdjustedUnitPrice(final BigDecimal adjustedUnitPrice)
	{
		this.adjustedUnitPrice = adjustedUnitPrice;
	}
	
		
	public BigDecimal getAdjustedUnitPrice() 
	{
		return adjustedUnitPrice;
	}
		
		@Override
	public String toString()
	{
		return new ToStringBuilder(this)
				.append(orderEntry == null ? "orderEntry=null" : "orderEntry=" + orderEntry.getEntryNumber())
				.append("quantity", quantity).append("firedRuleCode", firedRuleCode).append("adjustedUnitPrice", adjustedUnitPrice)
				.toString();
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
