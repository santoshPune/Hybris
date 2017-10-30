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

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ShipmentRAO extends AbstractRuleActionRAO 
{

	/** <i>Generated property</i> for <code>ShipmentRAO.mode</code> property defined at extension <code>ruleengineservices</code>. */
	private DeliveryModeRAO mode;
		
	public ShipmentRAO()
	{
		// default constructor
	}
	
		
	public void setMode(final DeliveryModeRAO mode)
	{
		this.mode = mode;
	}
	
		
	public DeliveryModeRAO getMode() 
	{
		return mode;
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