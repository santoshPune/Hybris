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
package de.hybris.platform.cmswebservices.data;

import java.util.Collection;
import java.util.Map;

public  class CatalogVersionData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CatalogVersionData.uid</code> property defined at extension <code>cmswebservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>CatalogVersionData.name</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String, String> name;

	/** <i>Generated property</i> for <code>CatalogVersionData.version</code> property defined at extension <code>cmswebservices</code>. */
		
	private String version;

	/** <i>Generated property</i> for <code>CatalogVersionData.catalogVersionDetails</code> property defined at extension <code>cmswebservices</code>. */
		
	private Collection<CatalogVersionDetailData> catalogVersionDetails;
	
	public CatalogVersionData()
	{
		// default constructor
	}
	
	
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}
	
	
	
	public String getUid() 
	{
		return uid;
	}
	
	
	
	public void setName(final Map<String, String> name)
	{
		this.name = name;
	}
	
	
	
	public Map<String, String> getName() 
	{
		return name;
	}
	
	
	
	public void setVersion(final String version)
	{
		this.version = version;
	}
	
	
	
	public String getVersion() 
	{
		return version;
	}
	
	
	
	public void setCatalogVersionDetails(final Collection<CatalogVersionDetailData> catalogVersionDetails)
	{
		this.catalogVersionDetails = catalogVersionDetails;
	}
	
	
	
	public Collection<CatalogVersionDetailData> getCatalogVersionDetails() 
	{
		return catalogVersionDetails;
	}
	


}