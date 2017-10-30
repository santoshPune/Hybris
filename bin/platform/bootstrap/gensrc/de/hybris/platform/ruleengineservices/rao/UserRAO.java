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

import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.UserGroupRAO;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UserRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>UserRAO.id</code> property defined at extension <code>ruleengineservices</code>. */
	private String id;
	/** <i>Generated property</i> for <code>UserRAO.orders</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<AbstractOrderRAO> orders;
	/** <i>Generated property</i> for <code>UserRAO.groups</code> property defined at extension <code>ruleengineservices</code>. */
	private Set<UserGroupRAO> groups;
		
	public UserRAO()
	{
		// default constructor
	}
	
		
	public void setId(final String id)
	{
		this.id = id;
	}
	
		
	public String getId() 
	{
		return id;
	}
		
		
	public void setOrders(final Set<AbstractOrderRAO> orders)
	{
		this.orders = orders;
	}
	
		
	public Set<AbstractOrderRAO> getOrders() 
	{
		return orders;
	}
		
		
	public void setGroups(final Set<UserGroupRAO> groups)
	{
		this.groups = groups;
	}
	
		
	public Set<UserGroupRAO> getGroups() 
	{
		return groups;
	}
		
		@Override
	public boolean equals(Object that)
	{
		if (that == null || !this.getClass().equals(that.getClass()))
		{
			return false;
		}
		else if (that == this)
		{
			return true;
		}
		UserRAO other = (UserRAO) that;
		return new EqualsBuilder().append(id, other.id).isEquals();
	}
	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(id).toHashCode();
	}
}