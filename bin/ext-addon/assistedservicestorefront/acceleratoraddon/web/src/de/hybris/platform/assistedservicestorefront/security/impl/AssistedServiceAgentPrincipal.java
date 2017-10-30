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
 *
 *  
 */
package de.hybris.platform.assistedservicestorefront.security.impl;

/**
 * Represents Assisted Service Agent principal.
 */
public class AssistedServiceAgentPrincipal
{
	private final String name;

	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	public AssistedServiceAgentPrincipal(final String name)
	{
		this.name = name;
	}
}