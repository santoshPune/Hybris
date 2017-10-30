/*
 * [y] hybris Platform *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 *("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 */
package de.hybris.platform.chinesepspalipayservices.strategies.impl;

import de.hybris.platform.chinesepspalipayservices.alipay.AlipayConfiguration;
import de.hybris.platform.chinesepspalipayservices.alipay.AlipayUtil;
import de.hybris.platform.chinesepspalipayservices.data.AlipayCancelPaymentRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayDirectPayRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayPaymentStatusRequestData;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus;
import de.hybris.platform.chinesepspalipayservices.data.AlipayRefundRequestData;
import de.hybris.platform.chinesepspalipayservices.strategies.AlipayCreateRequestStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class DefaultAlipayCreateRequestStrategy implements AlipayCreateRequestStrategy
{

	private static final Logger LOG = Logger.getLogger(DefaultAlipayCreateRequestStrategy.class);

	private AlipayConfiguration alipayConfiguration;

	@Override
	public String createDirectPayUrl(final AlipayDirectPayRequestData requestData) throws Exception
	{

		final Map<String, String> payRequestMap = describeRequest(requestData);
		return AlipayUtil.generateUrl(payRequestMap, getAlipayConfiguration());
	}

	@Override
	public AlipayRawPaymentStatus submitPaymentStatusRequest(final AlipayPaymentStatusRequestData checkRequest)
			throws ReflectiveOperationException
	{
		final Map<String, String> alipayPaymentStatusRequestData = describeRequest(checkRequest);
		final String xmlString = AlipayUtil.postRequest(alipayPaymentStatusRequestData, getAlipayConfiguration());
		if (StringUtils.isNotEmpty(xmlString))
		{
			final AlipayRawPaymentStatus alipayRawPaymentStatus = (AlipayRawPaymentStatus) parserXML(xmlString,
					"de.hybris.platform.chinesepspalipayservices.data.AlipayRawPaymentStatus");
			return alipayRawPaymentStatus;
		}
		return null;
	}

	@Override
	public AlipayRawCancelPaymentResult submitCancelPaymentRequest(final AlipayCancelPaymentRequestData closeRequest)
			throws ReflectiveOperationException
	{
		final Map<String, String> alipayCancelPaymentRequestData = describeRequest(closeRequest);
		final String xmlString = AlipayUtil.postRequest(alipayCancelPaymentRequestData, getAlipayConfiguration());
		if (StringUtils.isNotEmpty(xmlString))
		{
			final AlipayRawCancelPaymentResult alipayRawCancelPaymentResult = (AlipayRawCancelPaymentResult) parserXML(xmlString,
					"de.hybris.platform.chinesepspalipayservices.data.AlipayRawCancelPaymentResult");
			return alipayRawCancelPaymentResult;
		}
		return null;
	}

	@Override
	public String createRefundUrl(AlipayRefundRequestData refundData) throws Exception
	{
		final Map<String, String> refundRequestMap = describeRequest(refundData);
		return AlipayUtil.generateUrl(refundRequestMap, getAlipayConfiguration());
	}

	protected Map<String, String> describeRequest(final Object bean) throws ReflectiveOperationException
	{
		Map<String, String> describeMap = new HashMap<>();
		describeMap = BeanUtils.describe(bean);
		describeMap.remove("class");
		describeMap.remove("quantity");
		final Map<String, String> AlipayRequestMap = new HashMap<>();

		for (final Map.Entry<String, String> entry : describeMap.entrySet())
		{
			String alipayKey = covert2SnakeCase(entry.getKey());
			if ("input_charset".equalsIgnoreCase(alipayKey))
			{
				alipayKey = "_" + alipayKey;
			}
			if (StringUtils.isNotEmpty(entry.getValue()) && entry.getValue() != null)
			{
				AlipayRequestMap.put(alipayKey, entry.getValue());
			}
			else
			{
				AlipayRequestMap.put(alipayKey, "");
			}
		}

		return AlipayRequestMap;
	}

	protected Object parserXML(final String xmlString, final String className)
	{
		try
		{

			final Class responseClass = Class.forName(className);
			final Object newResponse = responseClass.newInstance();
			final Map<String, String> responseMap = new HashMap<String, String>();
			final SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
			final SAXParser saxParser = factory.newSAXParser();
			final DefaultHandler handler = new DefaultHandler()
			{
				private String preTag = null;

				@Override
				public void startElement(final String uri, final String localName, final String qName, final Attributes attributes)
						throws SAXException
				{
					preTag = qName;
				}

				@Override
				public void endElement(final String uri, final String localName, final String qName) throws SAXException
				{
					if ("alipay".equalsIgnoreCase(qName))
					{
						preTag = null;
						try
						{
							BeanUtils.populate(newResponse, responseMap);
						}
						catch (final IllegalAccessException | InvocationTargetException e)
						{
							LOG.error("Convert xml to Bean error", e);
						}
					}

				}

				@Override
				public void characters(final char[] ch, final int start, final int length) throws SAXException
				{
					if (preTag != null)
					{
						final String content = new String(ch, start, length);
						String camelTag = WordUtils.capitalizeFully(preTag, new char[]
						{ '_' }).replaceAll("_", "");
						camelTag = WordUtils.uncapitalize(camelTag);

						if (camelTag != null && !camelTag.isEmpty())
						{
							if (!content.isEmpty() && !"\n".equalsIgnoreCase(content))
							{
								responseMap.put(camelTag, content);
							}
						}
					}
				}
			};

			final InputStream in = IOUtils.toInputStream(xmlString, "UTF-8");
			saxParser.parse(in, handler);
			return newResponse;
		}
		catch (final SAXException | IOException | ParserConfigurationException e)
		{
			LOG.error("Parse xml error", e);
		}
		catch (final InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			LOG.error("New Instance error", e);
		}
		return null;

	}

	protected String covert2SnakeCase(final String camelCase)
	{
		final String regex = "([a-z])([A-Z])";
		final String replacement = "$1_$2";
		return camelCase.replaceAll(regex, replacement).toLowerCase();
	}

	protected AlipayConfiguration getAlipayConfiguration()
	{
		return alipayConfiguration;
	}

	@Required
	public void setAlipayConfiguration(final AlipayConfiguration alipayConfiguration)
	{
		this.alipayConfiguration = alipayConfiguration;
	}

}
