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
package de.hybris.platform.cmswebservices.data;

public  class SyncJobRequestData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SyncJobRequestData.catalogId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String catalogId;

	/** <i>Generated property</i> for <code>SyncJobRequestData.sourceVersionId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String sourceVersionId;

	/** <i>Generated property</i> for <code>SyncJobRequestData.targetVersionId</code> property defined at extension <code>cmswebservices</code>. */
		
	private String targetVersionId;
	
	public SyncJobRequestData()
	{
		// default constructor
	}
	
	
	
	public void setCatalogId(final String catalogId)
	{
		this.catalogId = catalogId;
	}
	
	
	
	public String getCatalogId() 
	{
		return catalogId;
	}
	
	
	
	public void setSourceVersionId(final String sourceVersionId)
	{
		this.sourceVersionId = sourceVersionId;
	}
	
	
	
	public String getSourceVersionId() 
	{
		return sourceVersionId;
	}
	
	
	
	public void setTargetVersionId(final String targetVersionId)
	{
		this.targetVersionId = targetVersionId;
	}
	
	
	
	public String getTargetVersionId() 
	{
		return targetVersionId;
	}
	


}