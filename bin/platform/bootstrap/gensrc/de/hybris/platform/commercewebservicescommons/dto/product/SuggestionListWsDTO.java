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
package de.hybris.platform.commercewebservicescommons.dto.product;

import de.hybris.platform.commercewebservicescommons.dto.product.SuggestionWsDTO;
import java.util.List;

public  class SuggestionListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SuggestionListWsDTO.suggestions</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<SuggestionWsDTO> suggestions;
	
	public SuggestionListWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setSuggestions(final List<SuggestionWsDTO> suggestions)
	{
		this.suggestions = suggestions;
	}
	
	
	
	public List<SuggestionWsDTO> getSuggestions() 
	{
		return suggestions;
	}
	


}