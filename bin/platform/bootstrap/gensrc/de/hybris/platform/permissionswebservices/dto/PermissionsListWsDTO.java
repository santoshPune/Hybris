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
package de.hybris.platform.permissionswebservices.dto;

import de.hybris.platform.permissionswebservices.dto.PermissionsWsDTO;
import java.util.List;

public  class PermissionsListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>PermissionsListWsDTO.permissionsList</code> property defined at extension <code>permissionswebservices</code>. */
		
	private List<PermissionsWsDTO> permissionsList;
	
	public PermissionsListWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setPermissionsList(final List<PermissionsWsDTO> permissionsList)
	{
		this.permissionsList = permissionsList;
	}
	
	
	
	public List<PermissionsWsDTO> getPermissionsList() 
	{
		return permissionsList;
	}
	


}