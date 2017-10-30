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
package de.hybris.platform.chinesepspalipaymockaddon.service;

import de.hybris.platform.chinesepspalipayservices.alipay.AlipayConfiguration;
import de.hybris.platform.chinesepspalipayservices.alipay.AlipayUtil;
import de.hybris.platform.chinesepspalipayservices.alipay.HttpProtocolHandler;
import de.hybris.platform.chinesepspalipayservices.data.HttpRequest;
import de.hybris.platform.chinesepspalipayservices.data.HttpResponse;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


public class MockService
{

	private final String CHECK_RESPONSE = "chinesepspalipaymockaddon.check.response";
	private final String CHECK_TRADE_STATUS = "chinesepspalipaymockaddon.check.tradestatus";

	private AlipayConfiguration alipayConfiguration;

	public String createLinkString(final Map<String, String> params)
	{
		return AlipayUtil.createLinkString(params);
	}

	public Map<String, String> getNotifyParams(final Map<String, String> params, final String tradeStatus)
	{
		final Map<String, String> notify = new HashMap<>(params);

		notify.remove("error_notify_url");
		notify.remove("notify_url");
		notify.remove("return_url");
		notify.remove("service");
		notify.put("trade_status", tradeStatus);
		notify.put("trade_no", params.get("out_trade_no"));
		notify.put("notify_id", getNotifyId());
		notify.put("notify_time", getNotifyTime());
		notify.put("notify_type", "trade_status_sync");
		notify.put("sign_type", "MD5");
		notify.put("sign", getSign(notify));

		return notify;
	}


	public Map<String, String> getNotifyErrorParams(final Map<String, String> params, final String errorCode)
	{
		final Map<String, String> notifyError = new HashMap<>();

		notifyError.put("notify_id", getNotifyId());
		notifyError.put("notify_time", getNotifyTime());
		notifyError.put("partner", params.get("partner"));
		notifyError.put("out_trade_no", params.get("out_trade_no"));
		notifyError.put("error_code", errorCode);
		notifyError.put("return_url", params.get("error_notify_url"));
		notifyError.put("buyer_id", params.get("buyer_id"));
		notifyError.put("seller_email", params.get("seller_email"));
		notifyError.put("seller_id", params.get("seller_id"));
		notifyError.put("sign_type", "MD5");
		notifyError.put("sign", getSign(notifyError));

		return notifyError;
	}


	public Map<String, String> getReturnParams(final Map<String, String> params, final String tradeStatus)
	{
		final Map<String, String> returnParams = new HashMap<>(params);

		returnParams.remove("error_notify_url");
		returnParams.remove("notify_url");
		returnParams.remove("return_url");
		returnParams.remove("service");
		returnParams.put("is_success", "T");
		returnParams.put("trade_status", tradeStatus);
		returnParams.put("notify_id", getNotifyId());
		returnParams.put("notify_time", getNotifyTime());
		returnParams.put("notify_type", "trade_status_sync");
		returnParams.put("trade_no", params.get("out_trade_no"));
		returnParams.put("sign_type", "MD5");
		returnParams.put("sign", getSign(returnParams));

		return returnParams;
	}

	public Map<String, String> getRefundNotifyParams(final Map<String, String> params, final String errorCode)
	{
		final Map<String, String> notify = new HashMap<>();
		notify.put("notify_time", getNotifyTime());
		notify.put("notify_type", "batch_refund_notify");
		notify.put("notify_id", getNotifyId());
		notify.put("sign_type", "MD5");

		notify.put("batch_no", params.get("batch_no"));
		notify.put("success_num", params.get("batch_num"));

		String detailData = params.get("detail_data");
		String refundAmount = StringUtils.split(detailData, "^")[1];
		String baseResultDetails = StringUtils.split(detailData, "^")[0] + "^" + refundAmount + "^" + errorCode;
		String refundAlipayAccount = "Mock@Alipay.com";
		String refundAlipayId = "MockAlipayId";

		if ("SUCCESS".equalsIgnoreCase(errorCode))
		{
			notify.put("result_details", baseResultDetails + "$" + refundAlipayAccount + "^" + refundAlipayId + "^0.1^" + errorCode);
		}
		else
		{
			notify.put("result_details", baseResultDetails);
		}

		notify.put("sign", getSign(notify));

		return notify;
	}


	public String getPaymentStatusRequest(final Map<String, String> params)
	{
		final String tradeStatus = Config.getParameter(CHECK_TRADE_STATUS);
		final String outTradeNo = params.get("out_trade_no");
		final String totalFee = Registry.getMasterTenant().getConfig().getString("test.amount", "0.01");
		final String tradeNo = params.get("out_trade_no");


		final Map<String, String> trade = new HashMap<>();
		trade.put("out_trade_no", outTradeNo);
		trade.put("trade_status", tradeStatus);
		trade.put("total_fee", totalFee);

		final String signTrade = getSign(trade);
		final StringBuffer result = new StringBuffer();
		final boolean tradeSuccess = Boolean.parseBoolean(Config.getParameter(CHECK_RESPONSE));
		if (tradeSuccess)
		{
			result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><alipay><is_success>T</is_success>");
			result.append(
					"<request><param name=\"_input_charset\">utf-8</param><param name=\"service\">single_trade_query</param><param name=\"partner\">"
							+ xssEncode(alipayConfiguration.getWapPartner()) + "</param><param name=\"out_trade_no\">"
							+ xssEncode(outTradeNo) + "</param></request>");

			result.append("<response><trade>");
			result.append("<out_trade_no>" + xssEncode(outTradeNo) + "</out_trade_no>");
			result.append("<trade_no>" + xssEncode(tradeNo) + "</trade_no>");
			result.append("<trade_status>" + xssEncode(tradeStatus) + "</trade_status>");
			result.append("<total_fee>" + xssEncode(totalFee) + "</total_fee>");
			result.append("</trade></response>");
			result.append("<sign_type>MD5</sign_type>");
			result.append("<sign>" + signTrade + "</sign>");
			result.append("</alipay>");
		}
		else
		{
			result.append("<?xml version=\"1.0\" encoding=\"utf-8\"?><alipay><is_success>F</is_success>");
			result.append("<error>TRADE_IS_NOT_EXIST</error>");
			result.append("</alipay>");
		}
		return result.toString();
	}


	public String getCancelPaymentRequest()
	{
		final String result = "<?xml version=\"1.0\" encoding=\"utf-8\"?><alipay><is_success>T</is_success></alipay>";
		return result;
	}

	public String sendPostInfo(final Map<String, String> params, final String gateway)
	{

		final HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance("true");
		final HttpRequest request = new HttpRequest();

		stripOffCSRFToken(params);

		request.setCharset("utf-8");
		request.setMethod("POST");
		request.setParameters(generateNameValuePair(params));
		request.setUrl(gateway);

		final HttpResponse response = httpProtocolHandler.execute(request);
		if (response == null)
		{
			return null;
		}
		return response.getStringResult();
	}

	public String getSign(final Map<String, String> Params)
	{
		final String key = alipayConfiguration.getWebKey();
		final Map<String, String> sParaNew = AlipayUtil.paraFilter(Params);
		stripOffCSRFToken(sParaNew);
		return AlipayUtil.buildMysign(sParaNew, key, "MD5");
	}

	public void stripOffCSRFToken(final Map<String, String> params)
	{
		if (params != null)
		{
			params.remove("CSRFToken");
		}
	}

	protected static String xssEncode(final String value)
	{
		return (value == null) ? null : xssfilter(value);
	}

	protected static String xssfilter(final String value)
	{
		if (value == null)
		{
			return null;
		}
		String sanitized = value;
		sanitized = sanitized.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		sanitized = sanitized.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		sanitized = sanitized.replaceAll("'", "&#39;");
		sanitized = sanitized.replaceAll("eval\\((.*)\\)", "");
		sanitized = sanitized.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
		return sanitized;
	}



	protected static String getNotifyTime()
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	protected static String getNotifyId()
	{
		return AlipayUtil.encrypt("MD5", String.valueOf(System.currentTimeMillis()));
	}

	protected static List<NameValuePair> generateNameValuePair(final Map<String, String> properties)
	{
		final List<NameValuePair> nameValuePair = new ArrayList<>();
		for (final Map.Entry<String, String> entry : properties.entrySet())
		{
			nameValuePair.add(new NameValuePair(entry.getKey(), entry.getValue()));
		}

		return nameValuePair;
	}

	@Required
	public void setAlipayConfiguration(final AlipayConfiguration alipayConfiguration)
	{
		this.alipayConfiguration = alipayConfiguration;
	}

	protected AlipayConfiguration getAlipayConfiguration()
	{
		return alipayConfiguration;
	}

}
