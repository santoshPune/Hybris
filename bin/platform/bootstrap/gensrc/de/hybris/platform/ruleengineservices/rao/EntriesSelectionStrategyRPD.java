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

import de.hybris.platform.ruleengineservices.enums.OrderEntrySelectionStrategy;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EntriesSelectionStrategyRPD  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.orderEntries</code> property defined at extension <code>ruleengineservices</code>. */
	private List<OrderEntryRAO> orderEntries;
	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.selectionStrategy</code> property defined at extension <code>ruleengineservices</code>. */
	private OrderEntrySelectionStrategy selectionStrategy;
	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.quantity</code> property defined at extension <code>ruleengineservices</code>. */
	private int quantity;
	/** <i>Generated property</i> for <code>EntriesSelectionStrategyRPD.targetOfAction</code> property defined at extension <code>ruleengineservices</code>. */
	private boolean targetOfAction;
		
	public EntriesSelectionStrategyRPD()
	{
		// default constructor
	}
	
		
	public void setOrderEntries(final List<OrderEntryRAO> orderEntries)
	{
		this.orderEntries = orderEntries;
	}
	
		
	public List<OrderEntryRAO> getOrderEntries() 
	{
		return orderEntries;
	}
		
		
	public void setSelectionStrategy(final OrderEntrySelectionStrategy selectionStrategy)
	{
		this.selectionStrategy = selectionStrategy;
	}
	
		
	public OrderEntrySelectionStrategy getSelectionStrategy() 
	{
		return selectionStrategy;
	}
		
		
	public void setQuantity(final int quantity)
	{
		this.quantity = quantity;
	}
	
		
	public int getQuantity() 
	{
		return quantity;
	}
		
		
	public void setTargetOfAction(final boolean targetOfAction)
	{
		this.targetOfAction = targetOfAction;
	}
	
		
	public boolean isTargetOfAction() 
	{
		return targetOfAction;
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