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

import java.util.Date;
import java.util.Map;

public  class AbstractPageData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>AbstractPageData.pk</code> property defined at extension <code>cmswebservices</code>. */
		
	private String pk;

	/** <i>Generated property</i> for <code>AbstractPageData.creationtime</code> property defined at extension <code>cmswebservices</code>. */
		
	private Date creationtime;

	/** <i>Generated property</i> for <code>AbstractPageData.modifiedtime</code> property defined at extension <code>cmswebservices</code>. */
		
	private Date modifiedtime;

	/** <i>Generated property</i> for <code>AbstractPageData.uid</code> property defined at extension <code>cmswebservices</code>. */
		
	private String uid;

	/** <i>Generated property</i> for <code>AbstractPageData.name</code> property defined at extension <code>cmswebservices</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>AbstractPageData.title</code> property defined at extension <code>cmswebservices</code>. */
		
	private Map<String,String> title;

	/** <i>Generated property</i> for <code>AbstractPageData.typeCode</code> property defined at extension <code>cmswebservices</code>. */
		
	private String typeCode;

	/** <i>Generated property</i> for <code>AbstractPageData.template</code> property defined at extension <code>cmswebservices</code>. */
		
	private String template;

	/** <i>Generated property</i> for <code>AbstractPageData.defaultPage</code> property defined at extension <code>cmswebservices</code>. */
		
	private Boolean defaultPage;
	
	public AbstractPageData()
	{
		// default constructor
	}
	
	
	
	public void setPk(final String pk)
	{
		this.pk = pk;
	}
	
	
	
	public String getPk() 
	{
		return pk;
	}
	
	
	
	public void setCreationtime(final Date creationtime)
	{
		this.creationtime = creationtime;
	}
	
	
	
	public Date getCreationtime() 
	{
		return creationtime;
	}
	
	
	
	public void setModifiedtime(final Date modifiedtime)
	{
		this.modifiedtime = modifiedtime;
	}
	
	
	
	public Date getModifiedtime() 
	{
		return modifiedtime;
	}
	
	
	
	public void setUid(final String uid)
	{
		this.uid = uid;
	}
	
	
	
	public String getUid() 
	{
		return uid;
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setTitle(final Map<String,String> title)
	{
		this.title = title;
	}
	
	
	
	public Map<String,String> getTitle() 
	{
		return title;
	}
	
	
	
	public void setTypeCode(final String typeCode)
	{
		this.typeCode = typeCode;
	}
	
	
	
	public String getTypeCode() 
	{
		return typeCode;
	}
	
	
	
	public void setTemplate(final String template)
	{
		this.template = template;
	}
	
	
	
	public String getTemplate() 
	{
		return template;
	}
	
	
	
	public void setDefaultPage(final Boolean defaultPage)
	{
		this.defaultPage = defaultPage;
	}
	
	
	
	public Boolean getDefaultPage() 
	{
		return defaultPage;
	}
	


}