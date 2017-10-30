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
package de.hybris.platform.commercewebservicescommons.dto.catalog;

import java.util.Date;

public  class AbstractCatalogItemWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>AbstractCatalogItemWsDTO.id</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String id;

	/** <i>Generated property</i> for <code>AbstractCatalogItemWsDTO.lastModified</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private Date lastModified;

	/** <i>Generated property</i> for <code>AbstractCatalogItemWsDTO.name</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AbstractCatalogItemWsDTO.url</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private String url;
	
	public AbstractCatalogItemWsDTO()
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
	
	
	
	public void setLastModified(final Date lastModified)
	{
		this.lastModified = lastModified;
	}
	
	
	
	public Date getLastModified() 
	{
		return lastModified;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setUrl(final String url)
	{
		this.url = url;
	}
	
	
	
	public String getUrl() 
	{
		return url;
	}
	


}