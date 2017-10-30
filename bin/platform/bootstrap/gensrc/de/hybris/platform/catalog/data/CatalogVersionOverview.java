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
package de.hybris.platform.catalog.data;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Map;

public  class CatalogVersionOverview  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CatalogVersionOverview.catalogVersion</code> property defined at extension <code>catalog</code>. */
		
	private CatalogVersionModel catalogVersion;

	/** <i>Generated property</i> for <code>CatalogVersionOverview.typeAmounts</code> property defined at extension <code>catalog</code>. */
		
	private Map<ComposedTypeModel, Long> typeAmounts;
	
	public CatalogVersionOverview()
	{
		// default constructor
	}
	
	
	
	public void setCatalogVersion(final CatalogVersionModel catalogVersion)
	{
		this.catalogVersion = catalogVersion;
	}
	
	
	
	public CatalogVersionModel getCatalogVersion() 
	{
		return catalogVersion;
	}
	
	
	
	public void setTypeAmounts(final Map<ComposedTypeModel, Long> typeAmounts)
	{
		this.typeAmounts = typeAmounts;
	}
	
	
	
	public Map<ComposedTypeModel, Long> getTypeAmounts() 
	{
		return typeAmounts;
	}
	


}