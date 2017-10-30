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
package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.commercefacades.product.data.VariantCategoryData;
import java.util.Collection;

public  class VariantValueCategoryData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>VariantValueCategoryData.name</code> property defined at extension <code>commercefacades</code>. */
		
	private String name;

	/** <i>Generated property</i> for <code>VariantValueCategoryData.sequence</code> property defined at extension <code>commercefacades</code>. */
		
	private int sequence;

	/** <i>Generated property</i> for <code>VariantValueCategoryData.superCategories</code> property defined at extension <code>commercefacades</code>. */
		
	private Collection<VariantCategoryData> superCategories;
	
	public VariantValueCategoryData()
	{
		// default constructor
	}
	
	
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	
	
	public String getName() 
	{
		return name;
	}
	
	
	
	public void setSequence(final int sequence)
	{
		this.sequence = sequence;
	}
	
	
	
	public int getSequence() 
	{
		return sequence;
	}
	
	
	
	public void setSuperCategories(final Collection<VariantCategoryData> superCategories)
	{
		this.superCategories = superCategories;
	}
	
	
	
	public Collection<VariantCategoryData> getSuperCategories() 
	{
		return superCategories;
	}
	


}