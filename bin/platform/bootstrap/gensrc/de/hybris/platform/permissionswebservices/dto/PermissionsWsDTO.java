/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:01 PM
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

import java.util.Map;

public  class PermissionsWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>PermissionsWsDTO.id</code> property defined at extension <code>permissionswebservices</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>PermissionsWsDTO.permissions</code> property defined at extension <code>permissionswebservices</code>. */
		
	private Map<String, String> permissions;
	
	public PermissionsWsDTO()
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
	
	
	
	public void setPermissions(final Map<String, String> permissions)
	{
		this.permissions = permissions;
	}
	
	
	
	public Map<String, String> getPermissions() 
	{
		return permissions;
	}
	


}