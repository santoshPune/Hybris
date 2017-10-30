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
package de.hybris.platform.yacceleratorfractusfulfilmentprocess.orderstatus.exception;

import de.hybris.platform.fractussyncservices.exception.YaasBusinessException;


public class UnknownEcpStatusException extends YaasBusinessException
{
	public UnknownEcpStatusException(final String message)
	{
		super(message);
	}

	public UnknownEcpStatusException(final Throwable cause)
	{
		super(cause);
	}

	public UnknownEcpStatusException(final String message, final Throwable cause)
	{
		super(message, cause);
	}
}
