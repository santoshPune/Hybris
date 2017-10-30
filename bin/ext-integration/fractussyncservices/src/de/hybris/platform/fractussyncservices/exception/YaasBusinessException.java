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
*/
package de.hybris.platform.fractussyncservices.exception;

/**
 * Exception related to yaas business logic
 *
 */
public class YaasBusinessException extends Exception
{

	/**
	 *
	 */
	public YaasBusinessException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public YaasBusinessException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public YaasBusinessException(final String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public YaasBusinessException(final Throwable cause)
	{
		super(cause);
	}

}
