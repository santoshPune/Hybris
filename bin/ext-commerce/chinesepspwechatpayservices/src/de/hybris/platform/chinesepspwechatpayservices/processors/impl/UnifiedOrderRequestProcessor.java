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

import de.hybris.platform.chinesepspwechatpayservices.constants.WechatPaymentConstants;
import de.hybris.platform.chinesepspwechatpayservices.exception.WechatPayException;
import de.hybris.platform.chinesepspwechatpayservices.processors.AbstractWechatPayRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;
import de.hybris.platform.core.model.order.OrderModel;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import groovy.util.Node;
import groovy.util.NodeList;
import groovy.util.XmlParser;


/**
 * Processor to get pre-pay Id from wechat
 */
public class UnifiedOrderRequestProcessor extends AbstractWechatPayRequestProcessor<String>
{
	public UnifiedOrderRequestProcessor(final WechatPayConfiguration config, final WechatPayHttpClient httpClient,
			final String openId, final OrderModel order, final String clientIp)
	{
		super(config, httpClient);
		this.setUrl(config.getUnifiedOrderURL());
		this.addParameter("appid", config.getAppId());
		this.addParameter("mch_id", config.getMechId());
		this.addParameter("device_info", "WEB");
		this.addParameter("nonce_str", getParams().generateNonce());
		this.addParameter("body", order.getEntries().get(0).getProduct().getName());
		this.addParameter("detail", "");
		this.addParameter("attach", "");
		this.addParameter("out_trade_no", order.getCode());
		//TODO if currency is not CNY, it will meet issues
		//this.addParameter("fee_type", order.getCurrency().getName());
		if ("true".equals(config.getTestMode()))
		{
			this.addParameter("total_fee", "1");
		}
		else
		{
			this.addParameter("total_fee", String.valueOf(order.getTotalPrice().intValue() * 100));
		}
		this.addParameter("spbill_create_ip", clientIp);
		this.addParameter("time_start", "");
		this.addParameter("time_expire", "");
		this.addParameter("goods_tag", "");
		this.addParameter("notify_url", config.getBasePath() + WechatPaymentConstants.Controller.NOTIFY_URL);
		this.addParameter("trade_type", "JSAPI");
		this.addParameter("product_id", "");
		this.addParameter("limit_pay", "");
		this.addParameter("openid", openId);
		this.addParameter("sign", getParams().generateSignature(getConfig().getMechKey()));
	}

	@Override
	public String process()
	{
		String prePayId = "";
		try
		{
			final XmlParser xmlParser = new XmlParser();
			final Node node = xmlParser.parseText(post());
			final NodeList prePayIdList = (NodeList) node.get("prepay_id");
			if (prePayIdList.size() > 0)
			{
				final Node prePayIdNode = (Node) prePayIdList.get(0);
				prePayId = prePayIdNode.children().get(0).toString();
				return prePayId;
			}
			throw new WechatPayException("Unify Order Fail");
		}
		catch (ParserConfigurationException | IOException | SAXException e)
		{
			debug("XML Paser Exception" + e.toString());
			throw new WechatPayException("XML Paser Exception", e);
		}
	}
}