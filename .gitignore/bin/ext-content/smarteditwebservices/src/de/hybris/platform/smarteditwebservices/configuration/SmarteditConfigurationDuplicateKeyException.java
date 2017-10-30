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
package de.hybris.platform.smarteditwebservices.configuration;


/**
 * Thrown when a key is already present in the data store while creating a new configuration
 */
public class SmarteditConfigurationDuplicateKeyException extends RuntimeException
{

	public SmarteditConfigurationDuplicateKeyException(final String message)
	{
		super(message);
	}

	public SmarteditConfigurationDuplicateKeyException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	public SmarteditConfigurationDuplicateKeyException(final Throwable cause)
	{
		super(cause);
	}
}
