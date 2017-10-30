/*
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
package de.hybris.platform.servicelayer.event.events;

/**
 * Thrown when error while sending exception.
 */
public class MessageSendingException extends RuntimeException
{

	public MessageSendingException()
	{
		super();
	}

	public MessageSendingException(final String message, final Throwable exception)
	{
		super(message, exception);
	}

	public MessageSendingException(final String message)
	{
		super(message);
	}

	public MessageSendingException(final Throwable exception)
	{
		super(exception);
	}


}
