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
package de.hybris.platform.ycommercewebservices.product.data;

import de.hybris.platform.commercefacades.product.data.ReviewData;
import java.util.List;

public  class ReviewDataList  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>ReviewDataList.reviews</code> property defined at extension <code>ycommercewebservices</code>. */
		
	private List<ReviewData> reviews;
	
	public ReviewDataList()
	{
		// default constructor
	}
	
	
	
	public void setReviews(final List<ReviewData> reviews)
	{
		this.reviews = reviews;
	}
	
	
	
	public List<ReviewData> getReviews() 
	{
		return reviews;
	}
	


}