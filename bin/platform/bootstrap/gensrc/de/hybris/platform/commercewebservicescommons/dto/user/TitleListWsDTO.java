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
package de.hybris.platform.commercewebservicescommons.dto.user;

import de.hybris.platform.commercewebservicescommons.dto.user.TitleWsDTO;
import java.util.List;

public  class TitleListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>TitleListWsDTO.titles</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<TitleWsDTO> titles;
	
	public TitleListWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setTitles(final List<TitleWsDTO> titles)
	{
		this.titles = titles;
	}
	
	
	
	public List<TitleWsDTO> getTitles() 
	{
		return titles;
	}
	


}