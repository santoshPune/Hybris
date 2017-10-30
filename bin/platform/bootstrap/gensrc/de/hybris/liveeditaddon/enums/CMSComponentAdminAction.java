/*
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
package de.hybris.liveeditaddon.enums;

import de.hybris.platform.core.HybrisEnumValue;

/**
 * Generated enum CMSComponentAdminAction declared at extension liveeditaddon.
 */
@SuppressWarnings("PMD")
public enum CMSComponentAdminAction implements HybrisEnumValue
{
	/**
	 * Generated enum value for CMSComponentAdminAction.EDIT declared at extension liveeditaddon.
	 */
	EDIT("EDIT"),
	/**
	 * Generated enum value for CMSComponentAdminAction.EDIT_POPUP declared at extension liveeditaddon.
	 */
	EDIT_POPUP("EDIT_POPUP"),
	/**
	 * Generated enum value for CMSComponentAdminAction.CLONE declared at extension liveeditaddon.
	 */
	CLONE("CLONE"),
	/**
	 * Generated enum value for CMSComponentAdminAction.REMOVE declared at extension liveeditaddon.
	 */
	REMOVE("REMOVE"),
	/**
	 * Generated enum value for CMSComponentAdminAction.REMOVE_CONTAINER declared at extension liveeditaddon.
	 */
	REMOVE_CONTAINER("REMOVE_CONTAINER"),
	/**
	 * Generated enum value for CMSComponentAdminAction.SYNC declared at extension liveeditaddon.
	 */
	SYNC("SYNC"),
	/**
	 * Generated enum value for CMSComponentAdminAction.INSPECTOR declared at extension liveeditaddon.
	 */
	INSPECTOR("INSPECTOR"),
	/**
	 * Generated enum value for CMSComponentAdminAction.HIDE declared at extension liveeditaddon.
	 */
	HIDE("HIDE"),
	/**
	 * Generated enum value for CMSComponentAdminAction.SHOW declared at extension liveeditaddon.
	 */
	SHOW("SHOW"),
	/**
	 * Generated enum value for CMSComponentAdminAction.ADD declared at extension liveeditaddon.
	 */
	ADD("ADD"),
	/**
	 * Generated enum value for CMSComponentAdminAction.OVERRIDE declared at extension liveeditaddon.
	 */
	OVERRIDE("OVERRIDE"),
	/**
	 * Generated enum value for CMSComponentAdminAction.MANAGE_PRODUCT_IMAGE declared at extension liveeditaddon.
	 */
	MANAGE_PRODUCT_IMAGE("MANAGE_PRODUCT_IMAGE"),
	/**
	 * Generated enum value for CMSComponentAdminAction.RESTRICTIONS declared at extension liveeditaddon.
	 */
	RESTRICTIONS("RESTRICTIONS"),
	/**
	 * Generated enum value for CMSComponentAdminAction.EDIT_PRODUCT_REFERENCES declared at extension liveeditaddon.
	 */
	EDIT_PRODUCT_REFERENCES("EDIT_PRODUCT_REFERENCES"),
	/**
	 * Generated enum value for CMSComponentAdminAction.SHOW_ALL declared at extension liveeditaddon.
	 */
	SHOW_ALL("SHOW_ALL"),
	/**
	 * Generated enum value for CMSComponentAdminAction.MANAGE_MEDIA declared at extension liveeditaddon.
	 */
	MANAGE_MEDIA("MANAGE_MEDIA"),
	/**
	 * Generated enum value for CMSComponentAdminAction.EDIT_NAVIGATION declared at extension liveeditaddon.
	 */
	EDIT_NAVIGATION("EDIT_NAVIGATION"),
	/**
	 * Generated enum value for CMSComponentAdminAction.MANAGE_PRODUCT_FEATURE_IMAGE declared at extension liveeditaddon.
	 */
	MANAGE_PRODUCT_FEATURE_IMAGE("MANAGE_PRODUCT_FEATURE_IMAGE");
	 
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "CMSComponentAdminAction";
	
	/**<i>Generated simple class name constant.</i>*/
	public final static String SIMPLE_CLASSNAME = "CMSComponentAdminAction";
	
	/** The code of this enum.*/
	private final String code;
	
	/**
	 * Creates a new enum value for this enum type.
	 *  
	 * @param code the enum value code
	 */
	private CMSComponentAdminAction(final String code)
	{
		this.code = code.intern();
	}
	
	
	/**
	 * Gets the code of this enum value.
	 *  
	 * @return code of value
	 */
	@Override
	public String getCode()
	{
		return this.code;
	}
	
	/**
	 * Gets the type this enum value belongs to.
	 *  
	 * @return code of type
	 */
	@Override
	public String getType()
	{
		return SIMPLE_CLASSNAME;
	}
	
}
