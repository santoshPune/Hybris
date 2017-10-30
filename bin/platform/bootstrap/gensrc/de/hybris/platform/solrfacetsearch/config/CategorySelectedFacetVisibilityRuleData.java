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
package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.solrfacetsearch.config.AbstractSolrFacetVisibilityRuleData;
import java.util.Collection;

public  class CategorySelectedFacetVisibilityRuleData extends AbstractSolrFacetVisibilityRuleData 
{


	/** <i>Generated property</i> for <code>CategorySelectedFacetVisibilityRuleData.categories</code> property defined at extension <code>commercesearch</code>. */
		
	private Collection<CategoryModel> categories;
	
	public CategorySelectedFacetVisibilityRuleData()
	{
		// default constructor
	}
	
	
	
	public void setCategories(final Collection<CategoryModel> categories)
	{
		this.categories = categories;
	}
	
	
	
	public Collection<CategoryModel> getCategories() 
	{
		return categories;
	}
	


}