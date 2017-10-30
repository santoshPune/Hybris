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

import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountValueRAO;
import de.hybris.platform.ruleengineservices.rao.ProductRAO;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class OrderEntryRAO extends AbstractActionedRAO 
{

	/** <i>Generated property</i> for <code>OrderEntryRAO.entryNumber</code> property defined at extension <code>ruleengineservices</code>. */
	private Integer entryNumber;
	/** <i>Generated property</i> for <code>OrderEntryRAO.quantity</code> property defined at extension <code>ruleengineservices</code>. */
	private int quantity;
	/** <i>Generated property</i> for <code>OrderEntryRAO.basePrice</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal basePrice;
	/** <i>Generated property</i> for <code>OrderEntryRAO.totalPrice</code> property defined at extension <code>ruleengineservices</code>. */
	private BigDecimal totalPrice;
	/** <i>Generated property</i> for <code>OrderEntryRAO.currencyIsoCode</code> property defined at extension <code>ruleengineservices</code>. */
	private String currencyIsoCode;
	/** <i>Generated property</i> for <code>OrderEntryRAO.order</code> property defined at extension <code>ruleengineservices</code>. */
	private AbstractOrderRAO order;
	/** <i>Generated property</i> for <code>OrderEntryRAO.product</code> property defined at extension <code>ruleengineservices</code>. */
	private ProductRAO product;
	/** <i>Generated property</i> for <code>OrderEntryRAO.discountValues</code> property defined at extension <code>ruleengineservices</code>. */
	private List<DiscountValueRAO> discountValues;
		
	public OrderEntryRAO()
	{
		// default constructor
	}
	
		
	public void setEntryNumber(final Integer entryNumber)
	{
		this.entryNumber = entryNumber;
	}
	
		
	public Integer getEntryNumber() 
	{
		return entryNumber;
	}
		
		
	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}
	
		
	public int getQuantity() 
	{
		return quantity;
	}
		
		
	public void setBasePrice(final BigDecimal basePrice)
	{
		this.basePrice = basePrice;
	}
	
		
	public BigDecimal getBasePrice() 
	{
		return basePrice;
	}
		
		
	public void setTotalPrice(final BigDecimal totalPrice)
	{
		this.totalPrice = totalPrice;
	}
	
		
	public BigDecimal getTotalPrice() 
	{
		return totalPrice;
	}
		
		
	public void setCurrencyIsoCode(final String currencyIsoCode)
	{
		this.currencyIsoCode = currencyIsoCode;
	}
	
		
	public String getCurrencyIsoCode() 
	{
		return currencyIsoCode;
	}
		
		
	public void setOrder(final AbstractOrderRAO order)
	{
		this.order = order;
	}
	
		
	public AbstractOrderRAO getOrder() 
	{
		return order;
	}
		
		
	public void setProduct(final ProductRAO product)
	{
		this.product = product;
	}
	
		
	public ProductRAO getProduct() 
	{
		return product;
	}
		
		
	public void setDiscountValues(final List<DiscountValueRAO> discountValues)
	{
		this.discountValues = discountValues;
	}
	
		
	public List<DiscountValueRAO> getDiscountValues() 
	{
		return discountValues;
	}
		
		@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("entryNumber", entryNumber).append("product=")
				.append(product == null ? "null" : product.getCode())
				.append("quantity", quantity).append("basePrice", basePrice).append("currency", currencyIsoCode)
				.append("discountValues", discountValues).toString();
	}

 	@Override
    public boolean equals(Object obj)
    {
       if (obj == null || !this.getClass().equals(obj.getClass())) {
         return false;
       } else if (obj == this) {
         return true;
       }
       OrderEntryRAO other = (OrderEntryRAO)obj;
       return new EqualsBuilder()
          .append(order, other.order)
          .append(entryNumber, other.entryNumber)
          .isEquals();
    }
    	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(order).append(entryNumber).toHashCode();
	}
}