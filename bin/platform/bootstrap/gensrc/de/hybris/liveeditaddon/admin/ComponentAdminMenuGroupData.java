/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:02 PM
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
 */
package de.hybris.liveeditaddon.admin;

import de.hybris.liveeditaddon.admin.ComponentAdminMenuItemData;
import de.hybris.liveeditaddon.enums.CMSComponentAdminActionGroup;
import java.util.List;

public  class ComponentAdminMenuGroupData extends ComponentAdminMenuItemData 
{


	/** <i>Generated property</i> for <code>ComponentAdminMenuGroupData.actionGroupType</code> property defined at extension <code>liveeditaddon</code>. */
		
	private CMSComponentAdminActionGroup actionGroupType;

	/** <i>Generated property</i> for <code>ComponentAdminMenuGroupData.items</code> property defined at extension <code>liveeditaddon</code>. */
		
	private List<ComponentAdminMenuItemData> items;
	
	public ComponentAdminMenuGroupData()
	{
		// default constructor
	}
	
	
	
	public void setActionGroupType(final CMSComponentAdminActionGroup actionGroupType)
	{
		this.actionGroupType = actionGroupType;
	}
	
	
	
	public CMSComponentAdminActionGroup getActionGroupType() 
	{
		return actionGroupType;
	}
	
	
	
	public void setItems(final List<ComponentAdminMenuItemData> items)
	{
		this.items = items;
	}
	
	
	
	public List<ComponentAdminMenuItemData> getItems() 
	{
		return items;
	}
	


}