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
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;


/**
 * Base class for calling Wechat Pay API, each different API needs to extend it and customize the parameters and result
 * parsing
 */
public abstract class AbstractWechatPayRequestProcessor<T> extends AbstractWechatPayProcessor<T>
{
	private String url;
	private final WechatPayHttpClient httpClient;

	public AbstractWechatPayRequestProcessor(final WechatPayConfiguration config, final WechatPayHttpClient httpClient)
	{
		super(config);
		this.httpClient = httpClient;
		this.addParameter("appid", config.getAppId());
	}

	/**
	 * Process a POST request
	 *
	 * @return the request response
	 */
	protected String post()
	{
		return this.httpClient.post(this.url, getParams().generateXml());
	}

	/**
	 * Process a GET request
	 *
	 * @return the request response
	 */
	protected String get()
	{
		return this.httpClient.get(getParams().generateGetURL(this.url));
	}

	/**
	 * @return the url
	 */
	public String getUrl()
	{
		return url;
	}

	/**
	 * @param url
	 *           the url to set
	 */
	public void setUrl(final String url)
	{
		this.url = url;
	}
}
