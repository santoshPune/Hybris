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
package de.hybris.platform.chinesepspwechatpayservices.wechatpay;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Wrapper of parameters required to call Wechat API
 */
public class WechatPayParameters
{
	private static final String NONCE_STRING_CHAR_SET = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static final int NONCE_STRING_LENGTH = 32;

	private final SortedMap<String, String> parameters;

	public WechatPayParameters()
	{
		this.parameters = new TreeMap<>();
	}

	/**
	 * Add a parameter, will be ignored if the name or value is empty
	 *
	 * @param name
	 *           Parameter name
	 * @param value
	 *           Parameter value
	 */
	public void add(final String name, final String value)
	{
		if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(name))
		{
			parameters.put(name, value);
		}
	}

	/**
	 * Generate a query string from the parameters, e.g. a=1&b=2&c=3
	 *
	 * @return the query string
	 *
	 */
	public String generateQueryString()
	{
		return parameters.keySet().stream().map(k -> k + "=" + parameters.get(k)).collect(Collectors.joining("&"));
	}

	/**
	 * Generate the parameters in XML format, e.g. <xml><a>1</a><b>2</b><c>3</c></xml>
	 *
	 * @return the parameters in XML format
	 */
	public String generateXml()
	{
		return parameters.keySet().stream().map(k -> "<" + k + ">" + parameters.get(k) + "</" + k + ">")
				.collect(Collectors.joining("", "<xml>", "</xml>"));
	}

	/**
	 * Generate the complete URL for get request, e.g. http://www.XXX.com?a=1&b=2&c=3
	 *
	 * @param base
	 *           base URL
	 *
	 * @return the complete URL
	 */
	public String generateGetURL(final String base)
	{
		return base + "?" + generateQueryString();
	}

	/**
	 * Generate a signature, for details please check https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
	 *
	 * @param key
	 *           key used to calculate signature
	 *
	 * @return the signature
	 */
	public String generateSignature(final String key)
	{
		final String query = generateQueryString() + "&key=" + key;
		return DigestUtils.md5Hex(query).toUpperCase();
	}

	/**
	 * Generate a random string, for details please check https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3
	 *
	 * @return the random string
	 */
	public String generateNonce()
	{
		return RandomStringUtils.random(NONCE_STRING_LENGTH, NONCE_STRING_CHAR_SET);
	}

	/**
	 * @return the parameters
	 */
	public Map<String, String> getParameters()
	{
		return parameters;
	}
}
