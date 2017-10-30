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
package de.hybris.platform.chinesepspwechatpayservices.processors.impl;

import de.hybris.platform.chinesepspwechatpayservices.data.WechatPayQueryResult;
import de.hybris.platform.chinesepspwechatpayservices.exception.WechatPayException;
import de.hybris.platform.chinesepspwechatpayservices.processors.AbstractWechatPayRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.xml.sax.SAXException;

import groovy.util.Node;
import groovy.util.XmlParser;


/**
 * Processor to query order status in wechat
 */
public class OrderQueryRequestProcessor extends AbstractWechatPayRequestProcessor<Optional<WechatPayQueryResult>>
{
	public OrderQueryRequestProcessor(final WechatPayHttpClient httpClient, final String orderId,
			final WechatPayConfiguration config)
	{
		super(config, httpClient);
		this.setUrl(config.getOrderQueryURL());
		this.addParameter("mch_id", config.getMechId());
		this.addParameter("nonce_str", getParams().generateNonce());
		this.addParameter("out_trade_no", orderId);
		this.addParameter("appid", config.getAppId());
		this.addParameter("sign", getParams().generateSignature(getConfig().getMechKey()));
	}

	@Override
	public Optional<WechatPayQueryResult> process()
	{
		try
		{
			final String result = post();
			final Map<String, String> unifyResponseMap;
			final Node notifyXml = new XmlParser().parseText(result);
			unifyResponseMap = (Map<String, String>) notifyXml.children().stream().filter(x -> ((Node) x).children().size() > 0)
					.collect(Collectors.toMap(k -> ((Node) k).name(), k -> ((Node) k).children().get(0).toString()));
			final WechatPayQueryResult wechatPayQueryResult = new WechatPayQueryResult();
			final Map<String, String> camelCaseMap = WechatPayUtils.convertKey2CamelCase(unifyResponseMap);
			BeanUtils.populate(wechatPayQueryResult, camelCaseMap);
			return Optional.of(wechatPayQueryResult);
		}
		catch (IOException | SAXException | ParserConfigurationException | IllegalAccessException | InvocationTargetException
				| WechatPayException e)
		{
			debug("Problem in handling Wechatpay's query result message");
		}

		return Optional.empty();
	}

}
