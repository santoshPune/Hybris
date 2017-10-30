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

package de.hybris.platform.chinesepspwechatpayservices.exception;

/**
 * Custom exception class to handle exceptions while executing HTTP request
 */
public class WechatPayException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public WechatPayException()
	{
		super();
	}

	public WechatPayException(String s)
	{
		super(s);
	}

	public WechatPayException(Throwable throwable)
	{
		super(throwable);
	}

	public WechatPayException(String s, Throwable throwable)
	{
		super(s, throwable);
	}

}
