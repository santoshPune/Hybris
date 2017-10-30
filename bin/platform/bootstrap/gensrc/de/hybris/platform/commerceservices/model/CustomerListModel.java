/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 12:11:59 PM                    ---
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
package de.hybris.platform.commerceservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type CustomerList first defined at extension commerceservices.
 * <p>
 * A CustomerList are visible to certain Employees and represents an implementation specific search query to find customer based on different criteria.
 */
@SuppressWarnings("all")
public class CustomerListModel extends UserGroupModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CustomerList";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerList.implementationType</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String IMPLEMENTATIONTYPE = "implementationType";
	
	/** <i>Generated constant</i> - Attribute key of <code>CustomerList.priority</code> attribute defined at extension <code>commerceservices</code>. */
	public static final String PRIORITY = "priority";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public CustomerListModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public CustomerListModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CustomerListModel(final String _uid)
	{
		super();
		setUid(_uid);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _uid initial attribute declared by type <code>Principal</code> at extension <code>core</code>
	 */
	@Deprecated
	public CustomerListModel(final ItemModel _owner, final String _uid)
	{
		super();
		setOwner(_owner);
		setUid(_uid);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.implementationType</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the implementationType - The implementation type for this customer list
	 */
	@Accessor(qualifier = "implementationType", type = Accessor.Type.GETTER)
	public String getImplementationType()
	{
		return getPersistenceContext().getPropertyValue(IMPLEMENTATIONTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CustomerList.priority</code> attribute defined at extension <code>commerceservices</code>. 
	 * @return the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
	public Integer getPriority()
	{
		return getPersistenceContext().getPropertyValue(PRIORITY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerList.implementationType</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the implementationType - The implementation type for this customer list
	 */
	@Accessor(qualifier = "implementationType", type = Accessor.Type.SETTER)
	public void setImplementationType(final String value)
	{
		getPersistenceContext().setPropertyValue(IMPLEMENTATIONTYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>CustomerList.priority</code> attribute defined at extension <code>commerceservices</code>. 
	 *  
	 * @param value the priority - priority for the customer list and zero by default, this will affect the position of the list when getting list of customer lists,higher values are displayed first
	 */
	@Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
	public void setPriority(final Integer value)
	{
		getPersistenceContext().setPropertyValue(PRIORITY, value);
	}
	
}
