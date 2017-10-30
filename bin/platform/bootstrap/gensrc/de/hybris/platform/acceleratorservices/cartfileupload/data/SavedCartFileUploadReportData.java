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
package de.hybris.platform.acceleratorservices.cartfileupload.data;

import  de.hybris.platform.commerceservices.order.CommerceCartModification;
import java.util.List;

public  class SavedCartFileUploadReportData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.errorModificationList</code> property defined at extension <code>acceleratorservices</code>. */
		
	private List<CommerceCartModification> errorModificationList;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.successCount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer successCount;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.partialImportCount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer partialImportCount;

	/** <i>Generated property</i> for <code>SavedCartFileUploadReportData.failureCount</code> property defined at extension <code>acceleratorservices</code>. */
		
	private Integer failureCount;
	
	public SavedCartFileUploadReportData()
	{
		// default constructor
	}
	
	
	
	public void setErrorModificationList(final List<CommerceCartModification> errorModificationList)
	{
		this.errorModificationList = errorModificationList;
	}
	
	
	
	public List<CommerceCartModification> getErrorModificationList() 
	{
		return errorModificationList;
	}
	
	
	
	public void setSuccessCount(final Integer successCount)
	{
		this.successCount = successCount;
	}
	
	
	
	public Integer getSuccessCount() 
	{
		return successCount;
	}
	
	
	
	public void setPartialImportCount(final Integer partialImportCount)
	{
		this.partialImportCount = partialImportCount;
	}
	
	
	
	public Integer getPartialImportCount() 
	{
		return partialImportCount;
	}
	
	
	
	public void setFailureCount(final Integer failureCount)
	{
		this.failureCount = failureCount;
	}
	
	
	
	public Integer getFailureCount() 
	{
		return failureCount;
	}
	


}