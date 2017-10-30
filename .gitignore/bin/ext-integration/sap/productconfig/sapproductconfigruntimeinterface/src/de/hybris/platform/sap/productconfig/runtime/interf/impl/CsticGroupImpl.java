/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.sap.productconfig.runtime.interf.impl;

import de.hybris.platform.sap.productconfig.runtime.interf.CsticGroup;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CsticGroupImpl implements CsticGroup

{

	private String name;
	private String description;
	private List<CsticModel> cstics = Collections.emptyList();

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroup#getName()
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroup#setName(java.lang.String)
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroup#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroup#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroup#getCsticGroups()
	 */
	@Override
	public List<CsticModel> getCstics()
	{
		if (cstics.equals(Collections.emptyList()))
		{
			cstics = new ArrayList<>(10);
		}
		return cstics;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.sap.productconfig.runtime.interf.impl.CsticGroup#setCsticGroups(java.util.List)
	 */
	@Override
	public void setCstics(final List<CsticModel> cstics)
	{
		this.cstics = cstics;
	}

}
