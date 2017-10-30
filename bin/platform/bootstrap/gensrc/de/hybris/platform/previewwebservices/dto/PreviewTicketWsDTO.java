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
package de.hybris.platform.previewwebservices.dto;

import de.hybris.platform.personalizationservices.data.CxVariationKey;
import java.util.Collection;
import java.util.Date;

/**
 * Preview Ticket.<br/> 
 */
public  class PreviewTicketWsDTO  implements java.io.Serializable 
{


	/** Catalog id of the preview ticket.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.catalog</code> property defined at extension <code>previewwebservices</code>. */
		
	private String catalog;

	/** Catalog version of the preview ticket.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.catalogVersion</code> property defined at extension <code>previewwebservices</code>. */
		
	private String catalogVersion;

	/** Resource path.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.resourcePath</code> property defined at extension <code>previewwebservices</code>. */
		
	private String resourcePath;

	/** User of the preview ticket.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.user</code> property defined at extension <code>previewwebservices</code>. */
		
	private String user;

	/** User group of the preview ticket.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.userGroup</code> property defined at extension <code>previewwebservices</code>. */
		
	private String userGroup;

	/** Language of the preview ticket.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.language</code> property defined at extension <code>previewwebservices</code>. */
		
	private String language;

	/** Time of the preview ticket.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.time</code> property defined at extension <code>previewwebservices</code>. */
		
	private Date time;

	/** Preview ticket id.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.ticketId</code> property defined at extension <code>previewwebservices</code>. */
		
	private String ticketId;

	/** Cms page id to use for the preview.<br/> <br/><br/><i>Generated property</i> for <code>PreviewTicketWsDTO.pageId</code> property defined at extension <code>previewwebservices</code>. */
		
	private String pageId;

	/** <i>Generated property</i> for <code>PreviewTicketWsDTO.variations</code> property defined at extension <code>previewpersonalizationweb</code>. */
		
	private Collection<CxVariationKey> variations;
	
	public PreviewTicketWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setCatalog(final String catalog)
	{
		this.catalog = catalog;
	}
	
	
	
	public String getCatalog() 
	{
		return catalog;
	}
	
	
	
	public void setCatalogVersion(final String catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}
	
	
	
	public String getCatalogVersion() 
	{
		return catalogVersion;
	}
	
	
	
	public void setResourcePath(final String resourcePath)
	{
		this.resourcePath = resourcePath;
	}
	
	
	
	public String getResourcePath() 
	{
		return resourcePath;
	}
	
	
	
	public void setUser(final String user)
	{
		this.user = user;
	}
	
	
	
	public String getUser() 
	{
		return user;
	}
	
	
	
	public void setUserGroup(final String userGroup)
	{
		this.userGroup = userGroup;
	}
	
	
	
	public String getUserGroup() 
	{
		return userGroup;
	}
	
	
	
	public void setLanguage(final String language)
	{
		this.language = language;
	}
	
	
	
	public String getLanguage() 
	{
		return language;
	}
	
	
	
	public void setTime(final Date time)
	{
		this.time = time;
	}
	
	
	
	public Date getTime() 
	{
		return time;
	}
	
	
	
	public void setTicketId(final String ticketId)
	{
		this.ticketId = ticketId;
	}
	
	
	
	public String getTicketId() 
	{
		return ticketId;
	}
	
	
	
	public void setPageId(final String pageId)
	{
		this.pageId = pageId;
	}
	
	
	
	public String getPageId() 
	{
		return pageId;
	}
	
	
	
	public void setVariations(final Collection<CxVariationKey> variations)
	{
		this.variations = variations;
	}
	
	
	
	public Collection<CxVariationKey> getVariations() 
	{
		return variations;
	}
	


}