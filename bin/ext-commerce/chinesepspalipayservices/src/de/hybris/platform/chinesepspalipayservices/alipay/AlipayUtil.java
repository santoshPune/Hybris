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
package de.hybris.platform.chinesepspalipayservices.alipay;

import de.hybris.platform.chinesepspalipayservices.constants.PaymentConstants;
import de.hybris.platform.chinesepspalipayservices.data.HttpRequest;
import de.hybris.platform.chinesepspalipayservices.data.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.sap.security.core.server.csi.util.URLEncoder;


/**
 *
 */
public class AlipayUtil
{
	private static final Logger LOG = Logger.getLogger(AlipayUtil.class);

	public static String generateUrl(final Map<String, String> sParaTemp, final AlipayConfiguration alipayConfig) throws Exception
	{
		final Map<String, String> sPara = buildRequestPara(sParaTemp, alipayConfig.getWebKey(), alipayConfig.getSignType());
		final StringBuffer strResult = new StringBuffer();
		strResult.append(alipayConfig.getWebGateway());
		strResult.append(createLinkStringWithEncoding(sPara));
		return strResult.toString();
	}

	/**
	 * Simulate the HTTP POST request, use this to get the XML response from Alipay
	 *
	 * @param sParaTemp
	 *           Request Parameters
	 * @param alipayConfig
	 * @return XML response returned by Alipay
	 */
	public static String postRequest(final Map<String, String> sParaTemp, final AlipayConfiguration alipayConfig)
	{
		final Map<String, String> sPara = buildRequestPara(sParaTemp, alipayConfig.getWebKey(), alipayConfig.getSignType());
		final HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance(alipayConfig.getTrustGateway());
		final HttpRequest request = new HttpRequest();
		request.setCharset(PaymentConstants.Basic.INPUT_CHARSET);
		request.setParameters(generatNameValuePairList(sPara));
		request.setUrl(alipayConfig.getWebGateway() + "_input_charset=" + PaymentConstants.Basic.INPUT_CHARSET);
		request.setMethod(PaymentConstants.HTTP.METHOD_POST);
		final HttpResponse response = httpProtocolHandler.execute(request);
		if (response == null)
		{
			return null;
		}
		final String strResult = response.getStringResult();
		return strResult;
	}


	public static Map<String, String> paraFilter(final Map<String, String> sArray)
	{
		final Map<String, String> result = new HashMap<String, String>();

		if (sArray == null || sArray.size() <= 0)
		{
			return result;
		}

		for (final String key : sArray.keySet())
		{
			final String value = sArray.get(key);
			if (value == null || "".equals(value) || "sign".equalsIgnoreCase(key) || "sign_type".equalsIgnoreCase(key))
			{
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	public static String buildMysign(final Map<String, String> sArray, final String key, final String signType)
	{
		String prestr = createLinkString(sArray);
		prestr = prestr + key;
		final String mysign = encrypt(signType, prestr);
		return mysign;
	}


	protected static Map<String, String> buildRequestPara(final Map<String, String> sParaTemp, final String key,
			final String signType)
	{
		final Map<String, String> sPara = paraFilter(sParaTemp);
		final String mysign = buildMysign(sPara, key, signType);
		sPara.put("sign", mysign);
		sPara.put("sign_type", signType);
		return sPara;
	}



	public static String encrypt(final String signType, final String preStr)
	{
		if ("MD5".equalsIgnoreCase(signType))
		{
			return DigestUtils.md5Hex(preStr);
		}
		else if ("DSA".equalsIgnoreCase(signType))
		{
			//TODO
		}
		else if ("RSA".equalsIgnoreCase(signType))
		{
			//TODO
		}
		return "";

	}



	public static String createLinkStringWithEncoding(final Map<String, String> params) throws UnsupportedEncodingException
	{
		final List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		StringBuilder prestr = new StringBuilder();

		for (int i = 0; i < keys.size(); i++)
		{
			final String key = keys.get(i);
			final String value = URLEncoder.encode(params.get(key), "UTF-8");
			if (i == keys.size() - 1)
			{
				prestr.append(key + "=" + value);
			}
			else
			{
				prestr.append(key + "=" + value + "&");
			}
		}

		return prestr.toString();
	}


	public static String createLinkString(final Map<String, String> params)
	{
		final List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		StringBuilder prestr = new StringBuilder();
		for (int i = 0; i < keys.size(); i++)
		{
			final String key = keys.get(i);
			final String value = params.get(key);
			if (i == keys.size() - 1)
			{
				prestr.append(key + "=" + value);
			}
			else
			{
				prestr.append(key + "=" + value + "&");
			}
		}

		return prestr.toString();
	}


	protected static List<NameValuePair> generatNameValuePairList(final Map<String, String> properties)
	{

		final List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();


		for (final Map.Entry<String, String> entry : properties.entrySet())
		{

			nameValuePairList.add(new NameValuePair(entry.getKey(), entry.getValue()));
		}

		return nameValuePairList;
	}



}
