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

import java.util.Date;

public  class SyncJobData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SyncJobData.syncStatus</code> property defined at extension <code>cmswebservices</code>. */
		
	private String syncStatus;

	/** <i>Generated property</i> for <code>SyncJobData.startDate</code> property defined at extension <code>cmswebservices</code>. */
		
	private Date startDate;

	/** <i>Generated property</i> for <code>SyncJobData.endDate</code> property defined at extension <code>cmswebservices</code>. */
		
	private Date endDate;

	/** <i>Generated property</i> for <code>SyncJobData.creationDate</code> property defined at extension <code>cmswebservices</code>. */
		
	private Date creationDate;

	/** <i>Generated property</i> for <code>SyncJobData.lastModifiedDate</code> property defined at extension <code>cmswebservices</code>. */
		
	private Date lastModifiedDate;

	/** <i>Generated property</i> for <code>SyncJobData.syncResult</code> property defined at extension <code>cmswebservices</code>. */
		
	private String syncResult;
	
	public SyncJobData()
	{
		// default constructor
	}
	
	
	
	public void setSyncStatus(final String syncStatus)
	{
		this.syncStatus = syncStatus;
	}
	
	
	
	public String getSyncStatus() 
	{
		return syncStatus;
	}
	
	
	
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}
	
	
	
	public Date getStartDate() 
	{
		return startDate;
	}
	
	
	
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}
	
	
	
	public Date getEndDate() 
	{
		return endDate;
	}
	
	
	
	public void setCreationDate(final Date creationDate)
	{
		this.creationDate = creationDate;
	}
	
	
	
	public Date getCreationDate() 
	{
		return creationDate;
	}
	
	
	
	public void setLastModifiedDate(final Date lastModifiedDate)
	{
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
	
	public Date getLastModifiedDate() 
	{
		return lastModifiedDate;
	}
	
	
	
	public void setSyncResult(final String syncResult)
	{
		this.syncResult = syncResult;
	}
	
	
	
	public String getSyncResult() 
	{
		return syncResult;
	}
	


}