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
package de.hybris.platform.chinesepspwechatpayservices.processors;

import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayParameters;

import org.apache.log4j.Logger;


/**
 * Abstraction of interactions with Wechat Pay, e.g. calling Wechat API, handle response from Wechat Pay
 */
public abstract class AbstractWechatPayProcessor<T>
{
	private final WechatPayParameters params;
	private final WechatPayConfiguration config;

	protected final Logger logger = Logger.getLogger(getClass());

	public AbstractWechatPayProcessor(final WechatPayConfiguration config)
	{
		this.config = config;
		this.params = new WechatPayParameters();
	}

	/**
	 * Add a parameter, will be ignored if the name or value is empty
	 *
	 * @param msg
	 *           message to be logged
	 */
	protected void debug(final String msg)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug(msg);
		}
	}

	/**
	 * Process this operation
	 */
	abstract public T process();

	/**
	 * Add a parameter, will be ignored if the name or value is empty
	 *
	 * @param name
	 *           Parameter name
	 * @param value
	 *           Parameter value
	 */
	protected void addParameter(final String name, final String value)
	{
		this.params.add(name, value);
	}

	/**
	 * @return the params
	 */
	public WechatPayParameters getParams()
	{
		return params;
	}

	/**
	 * @return the config
	 */
	public WechatPayConfiguration getConfig()
	{
		return config;
	}

}
