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
package de.hybris.platform.commercewebservicescommons.dto.storesession;

import de.hybris.platform.commercewebservicescommons.dto.storesession.CurrencyWsDTO;
import java.util.List;

public  class CurrencyListWsDTO  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>CurrencyListWsDTO.currencies</code> property defined at extension <code>commercewebservicescommons</code>. */
		
	private List<CurrencyWsDTO> currencies;
	
	public CurrencyListWsDTO()
	{
		// default constructor
	}
	
	
	
	public void setCurrencies(final List<CurrencyWsDTO> currencies)
	{
		this.currencies = currencies;
	}
	
	
	
	public List<CurrencyWsDTO> getCurrencies() 
	{
		return currencies;
	}
	


}