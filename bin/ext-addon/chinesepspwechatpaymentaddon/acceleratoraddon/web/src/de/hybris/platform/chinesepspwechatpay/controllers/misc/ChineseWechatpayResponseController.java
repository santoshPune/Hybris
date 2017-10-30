/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspwechatpay.controllers.misc;

import de.hybris.platform.addonsupport.controllers.AbstractAddOnController;
import de.hybris.platform.chinesepspwechatpayservices.data.WechatRawDirectPayNotification;
import de.hybris.platform.chinesepspwechatpayservices.notifications.WechatPayNotificationService;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.SignVerificationProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayUtils;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.user.UserFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.xml.sax.SAXException;

import com.sap.security.core.server.csi.XSSEncoder;

import groovy.util.Node;
import groovy.util.XmlParser;


@Controller
@Scope("tenant")
@RequestMapping("/checkout/multi/summary/wechat")
public class ChineseWechatpayResponseController extends AbstractAddOnController
{
	private static final Logger LOG = Logger.getLogger(ChineseWechatpayResponseController.class);

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "wechatPayConfiguration")
	private WechatPayConfiguration wechatPayConfiguration;

	@Resource(name = "wechatPayNotificationService")
	private WechatPayNotificationService wechatPayNotificationService;

	@RequestMapping(value = "/paymentresponse/notify", method = RequestMethod.POST)
	public void handlePaymentResponse(final HttpServletRequest request, final HttpServletResponse response)
			throws CMSItemNotFoundException, IOException
	{
		final String requestBody = getPostRequestBody(request);
		if (requestBody.isEmpty())
		{
			LOG.error("Notify body is empty");
		}
		else
		{
			final Map<String, String> unifyResponseMap;
			try
			{
				final Node notifyXml = new XmlParser().parseText(requestBody);
				unifyResponseMap = (Map<String, String>) notifyXml.children().stream()
						.collect(Collectors.toMap(k -> ((Node) k).name(), k -> ((Node) k).children().get(0).toString()));
				final SignVerificationProcessor signVerificationProcessor = new SignVerificationProcessor(wechatPayConfiguration,
						unifyResponseMap);
				if (!signVerificationProcessor.process().booleanValue())
				{
					LOG.warn("Invalid notify from Wechatpay");
					response.setContentType("text/xml");
					response.getWriter().write(XSSEncoder.encodeXML("FAIL"));
				}
				final WechatRawDirectPayNotification wechatpayNotification = new WechatRawDirectPayNotification();
				final Map<String, String> camelCaseMap = WechatPayUtils.convertKey2CamelCase(unifyResponseMap);
				BeanUtils.populate(wechatpayNotification, camelCaseMap);
				wechatPayNotificationService.handleWechatPayPaymentResponse(wechatpayNotification);
				response.setContentType("text/xml");
				response.getWriter().write(XSSEncoder.encodeXML("SUCCESS"));
			}
			catch (IOException | SAXException | ParserConfigurationException | IllegalAccessException | InvocationTargetException e)
			{
				LOG.error("Problem in handling Wechatpay's notify message", e);
			}

		}
	}

	public String getPostRequestBody(final HttpServletRequest req)
	{
		if (RequestMethod.POST.name().equalsIgnoreCase(req.getMethod()))
		{
			final StringBuilder sb = new StringBuilder();
			try (BufferedReader br = req.getReader())
			{
				final char[] charBuffer = new char[128];
				int bytesRead;
				while ((bytesRead = br.read(charBuffer)) != -1)
				{
					sb.append(charBuffer, 0, bytesRead);
				}
			}
			catch (final IOException e)
			{
				LOG.warn("failed to read request body");
			}
			return sb.toString();
		}
		return "";
	}

}
