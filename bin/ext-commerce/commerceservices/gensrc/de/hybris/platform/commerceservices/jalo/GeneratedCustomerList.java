/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 11:21:07 AM                    ---
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
 *  
 */
package de.hybris.platform.commerceservices.jalo;

import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.user.UserGroup;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.user.UserGroup CustomerList}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedCustomerList extends UserGroup
{
	/** Qualifier of the <code>CustomerList.implementationType</code> attribute **/
	public static final String IMPLEMENTATIONTYPE = "implementationType";
	/** Qualifier of the <code>CustomerList.priority</code> attribute **/
	public static final String PRIORITY = "priority";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(UserGroup.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(IMPLEMENTATIONTYPE, AttributeMode.INITIAL);
		tmp.put(PRIORITY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.implementationType</code> attribute.
	 * @return the implementationType - The implementation type for this customer list
	 */
	public String getImplementationType(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IMPLEMENTATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.implementationType</code> attribute.
	 * @return the implementationType - The implementation type for this customer list
	 */
	public String getImplementationType()
	{
		return getImplementationType( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerList.implementationType</code> attribute. 
	 * @param value the implementationType - The implementation type for this customer list
	 */
	public void setImplementationType(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IMPLEMENTATIONTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerList.implementationType</code> attribute. 
	 * @param value the implementationType - The implementation type for this customer list
	 */
	public void setImplementationType(final String value)
	{
		setImplementationType( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.priority</code> attribute.
	 * @return the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public Integer getPriority(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.priority</code> attribute.
	 * @return the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public Integer getPriority()
	{
		return getPriority( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.priority</code> attribute. 
	 * @return the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public int getPriorityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getPriority( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.priority</code> attribute. 
	 * @return the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public int getPriorityAsPrimitive()
	{
		return getPriorityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerList.priority</code> attribute. 
	 * @param value the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public void setPriority(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, PRIORITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerList.priority</code> attribute. 
	 * @param value the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public void setPriority(final Integer value)
	{
		setPriority( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerList.priority</code> attribute. 
	 * @param value the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public void setPriority(final SessionContext ctx, final int value)
	{
		setPriority( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CustomerList.priority</code> attribute. 
	 * @param value the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	public void setPriority(final int value)
	{
		setPriority( getSession().getSessionContext(), value );
	}
	
}
