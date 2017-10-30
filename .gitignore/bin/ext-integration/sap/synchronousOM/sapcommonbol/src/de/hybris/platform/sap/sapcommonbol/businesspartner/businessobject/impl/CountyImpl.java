/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.sapcommonbol.businesspartner.businessobject.impl;

import de.hybris.platform.sap.core.common.exceptions.ApplicationBaseRuntimeException;
import de.hybris.platform.sap.sapcommonbol.businesspartner.businessobject.interf.County;


/**
 * BO representation of a county (a geographical entity used for tax jurisdiction code determination)
 * 
 */
public class CountyImpl implements County
{

	private String countyText;
	private String taxJurCode;

	public String getCountyText()
	{
		return countyText;
	}

	public void setCountyText(final String countyText)
	{
		this.countyText = countyText;
	}

	public String getTaxJurCode()
	{
		return taxJurCode;
	}

	public void setTaxJurCode(final String taxJurCode)
	{
		this.taxJurCode = taxJurCode;
	}

	@Override
	public County clone()
	{
		County clone;
		try
		{
			clone = (County) super.clone();
		}
		catch (final CloneNotSupportedException e)
		{
			throw new ApplicationBaseRuntimeException(
					"Failed to clone Object, check whether Cloneable Interface is still implemented", e);
		}
		return clone;
	}

}
