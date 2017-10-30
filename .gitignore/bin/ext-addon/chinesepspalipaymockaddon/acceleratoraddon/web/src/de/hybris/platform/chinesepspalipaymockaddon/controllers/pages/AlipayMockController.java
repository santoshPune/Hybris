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
package de.hybris.platform.chinesepspalipaymockaddon.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.chinesepspalipaymockaddon.controllers.AlipayMockControllerConstants;
import de.hybris.platform.chinesepspalipaymockaddon.service.MockService;
import de.hybris.platform.chinesepspalipaymockaddon.util.imported.CSRFRequestDataValueProcessor;
import de.hybris.platform.chinesepspalipayservices.payment.DefaultAlipayPaymentService;
import de.hybris.platform.core.Registry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
@RequestMapping(value = "/checkout/multi/alipay/mock/gateway.do")
public class AlipayMockController extends AbstractController
{
	protected static final Logger LOG = Logger.getLogger(AlipayMockController.class);

	@Resource
	private MockService mockService;

	@Resource(name = "alipayPaymentService")
	private DefaultAlipayPaymentService defaultAlipayPaymentService;


	@SuppressWarnings("boxing")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String doGetGateWay(final Model model, final HttpServletRequest request)
	{
		final Map<String, String[]> requestParamMap = request.getParameterMap();
		if (requestParamMap == null)
		{
			return AlipayMockControllerConstants.Pages.AlipayMockPage;
		}
		final String baseGateWay = request.getRequestURL().toString();
		model.addAttribute("baseGateWay", baseGateWay);

		final Map<String, String> clearParams = removeUselessValue(requestParamMap);
		this.setCSRFToken(clearParams, request);

		final String service = request.getParameter("service");
		if (service == null)
		{
			return AlipayMockControllerConstants.Pages.AlipayMockPage;
		}
		XSSFilterUtil.filter(service);
		final boolean signIsValid = isSignValid(clearParams);
		model.addAttribute("signIsValid", Boolean.valueOf(signIsValid));
		model.addAttribute("params", clearParams);
		model.addAttribute("out_trade_no", clearParams.get("out_trade_no"));
		model.addAttribute("total_fee", clearParams.get("total_fee"));
		model.addAttribute("storefront", (StringUtils.substringBetween(request.getContextPath(), "/")));

		if ("refund_fastpay_by_platform_pwd".equals(service))
		{
			return AlipayMockControllerConstants.Pages.AlipayRefundPage;
		}

		return AlipayMockControllerConstants.Pages.AlipayMockPage;
	}



	@SuppressWarnings("boxing")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public void doPostGateWay(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final String service = request.getParameter("service");
		if (service == null)
		{
			return;
		}
		XSSFilterUtil.filter(service);
		if ("single_trade_query".equals(service))
		{
			doPaymentStatus(request, response);
		}
		else if ("close_trade".equals(service))
		{

			doCancelPayment(request, response);
		}
	}

	@RequestMapping(value = "/refund", method = RequestMethod.GET)
	public String view(final Model model, final HttpServletRequest request)
	{
		final Map<String, String[]> requestParamMap = request.getParameterMap();
		if (requestParamMap == null)
		{
			return AlipayMockControllerConstants.Pages.AlipayRefundTestPage;
		}
		final String baseGateWay = request.getRequestURL().toString();
		model.addAttribute("baseGateWay", baseGateWay);

		final Map<String, String> clearParams = removeUselessValue(requestParamMap);
		setCSRFToken(clearParams, request);
		return AlipayMockControllerConstants.Pages.AlipayRefundTestPage;
	}

	@RequestMapping(value = "/refund", method = RequestMethod.POST)
	public String doRefundRequest(final Model model, final HttpServletRequest request, String orderCode)
	{
		final Optional<String> refundUrl = defaultAlipayPaymentService.getRefundRequestUrl(orderCode);
		if (refundUrl.isPresent())
		{
			return "redirect:" + refundUrl.get();
		}
		return view(model, request);

	}


	@RequestMapping(value = "/notify.verify")
	public void doNotifyVerify(final HttpServletResponse response) throws IOException
	{
		response.getWriter().print("true");
	}


	@SuppressWarnings("boxing")
	@RequestMapping(value = "/directpay", method = RequestMethod.POST)
	public void doPostDirectPay(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws IOException
	{
		doDirectPay(request, response);
	}

	@RequestMapping(value = "/doRefund", method = RequestMethod.GET)
	public @ResponseBody String doRefund(final Model model, final HttpServletRequest request, final HttpServletResponse response)
			throws IOException
	{
		final Map<String, String[]> requestParamMap = request.getParameterMap();
		if (requestParamMap == null)
		{
			return "";
		}
		final Map<String, String> requestType = createRequestTypeMap(requestParamMap);
		final Map<String, String> clearParams = removeUselessValue(requestParamMap);
		final String errorCode = XSSFilterUtil.filter(requestType.get("error_code"));
		this.setCSRFToken(clearParams, request);
		final String url = clearParams.get("notify_url");
		final Map<String, String> notify = mockService.getRefundNotifyParams(clearParams, errorCode);
		mockService.sendPostInfo(notify, url);

		final String action = request.getParameter("action");
		if (action == null)
		{
			return "";
		}
		String resultMessage = "";
		if ("notify".equals(action))
		{
			resultMessage = "Refund Finished!";
		}
		return resultMessage;
	}

	@RequestMapping(value = "/directpay", method = RequestMethod.GET)
	public @ResponseBody String doGetDirectPay(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException
	{
		doDirectPay(request, response);
		final String action = request.getParameter("action");
		if (action == null)
		{
			return "";
		}
		String resultMessage = "";
		if ("notify".equals(action))
		{
			resultMessage = "DirectPay Success!";
		}
		else if ("notify_error".equals(action))
		{
			resultMessage = "DirectPay Fails!";
		}
		return resultMessage;
	}

	protected void doDirectPay(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final Map<String, String[]> requestParamMap = request.getParameterMap();
		if (requestParamMap == null)
		{
			return;
		}
		final Map<String, String> requestType = createRequestTypeMap(requestParamMap);
		final Map<String, String> clearParams = removeUselessValue(requestParamMap);
		this.setCSRFToken(clearParams, request);

		final String sign = mockService.getSign(clearParams);
		final boolean signIsValid = sign.equals(clearParams.get("sign"));
		if (signIsValid)
		{
			final String service = request.getParameter("service");
			if (service != null)
			{
				XSSFilterUtil.filter(service);
				if ("create_direct_pay_by_user".equals(service))
				{
					handleDirectPayRequest(clearParams, response, signIsValid, requestType);
				}
			}
		}
	}

	protected void doPaymentStatus(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final Map<String, String[]> requestParamMap = request.getParameterMap();
		if (requestParamMap == null)
		{
			return;
		}
		final Map<String, String> clearParams = removeUselessValue(requestParamMap);
		this.setCSRFToken(clearParams, request);

		if (isSignValid(clearParams))
		{
			final String result = mockService.getPaymentStatusRequest(clearParams);
			response.getWriter().print(result);
		}
	}


	protected void doCancelPayment(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{

		final Map<String, String[]> requestParamMap = request.getParameterMap();
		if (requestParamMap == null)
		{
			return;
		}
		final Map<String, String> clearParams = removeUselessValue(requestParamMap);
		this.setCSRFToken(clearParams, request);

		if (isSignValid(clearParams))
		{
			final String result = mockService.getCancelPaymentRequest();
			response.getWriter().print(result);
		}
	}

	protected boolean isSignValid(final Map<String, String> requestMap)
	{
		final String generateSign = mockService.getSign(requestMap);
		return generateSign.equals(requestMap.get("sign"));
	}


	protected Map<String, String> createRequestTypeMap(final Map<String, String[]> params)
	{
		final Map<String, String> RequestType = new HashMap<>();
		RequestType.put("action", params.get("action")[0]);
		RequestType.put("trade_status", params.get("trade_status")[0]);
		RequestType.put("error_code", params.get("error_code")[0]);
		return RequestType;
	}

	protected Map<String, String> removeUselessValue(final Map<String, String[]> params)
	{
		final Map<String, String> clearMap = new HashMap<>();
		for (final String key : params.keySet())
		{
			if ("action".equalsIgnoreCase(key) || "trade_status".equalsIgnoreCase(key) || "error_code".equalsIgnoreCase(key))
			{
				continue;
			}

			final String value = params.get(key)[0];
			clearMap.put(key, value);
		}
		return clearMap;
	}

	protected void handleDirectPayRequest(final Map<String, String> params, final HttpServletResponse response,
			final boolean signIsValid, final Map<String, String> requestType) throws IOException
	{
		final String tradeStatus = XSSFilterUtil.filter(requestType.get("trade_status"));
		final String errorCode = XSSFilterUtil.filter(requestType.get("error_code"));
		final String action = XSSFilterUtil.filter(requestType.get("action"));
		LOG.info("Payment request");


		if ("notify".equalsIgnoreCase(action))
		{
			notify(params, tradeStatus);
		}
		else if ("notify_error".equalsIgnoreCase(action))
		{
			notifyError(params, errorCode);
		}
		else if ("return".equalsIgnoreCase(action))
		{
			returnResponse(response, params, tradeStatus);
		}

		else if (tradeStatus == null)
		{
			if (signIsValid)
			{
				final String defaultTradeStatus = Registry.getMasterTenant().getConfig().getString("alipay.mock.default.trade.status",
						"WAIT_BUYER_PAY");
				notify(params, "WAIT_BUYER_PAY");
				if (!"WAIT_BUYER_PAY".equals(defaultTradeStatus))
				{
					notify(params, defaultTradeStatus);

					if ("TRADE_SUCCESS".equals(defaultTradeStatus))
					{
						returnResponse(response, params, tradeStatus);
					}
				}
			}
		}
	}

	protected void notify(final Map<String, String> params, final String tradeStatus)
	{
		final String url = params.get("notify_url");
		final Map<String, String> notify = mockService.getNotifyParams(params, tradeStatus);
		mockService.sendPostInfo(notify, url);
	}

	protected void notifyError(final Map<String, String> params, final String errorCode)
	{
		final Map<String, String> notify = mockService.getNotifyErrorParams(params, errorCode);
		final String url = params.get("error_notify_url");
		mockService.sendPostInfo(notify, url);
	}

	protected void returnResponse(final HttpServletResponse response, final Map<String, String> params, final String tradeStatus)
			throws IOException
	{
		mockService.stripOffCSRFToken(params);
		final String returnUrl = getReturnShopUrl(params, tradeStatus);
		response.sendRedirect(returnUrl);
	}

	protected String getReturnShopUrl(final Map<String, String> params, final String tradeStatus)
	{
		final Map<String, String> notify = mockService.getReturnParams(params, tradeStatus);
		final String baseUrl = params.get("return_url");
		final String url = baseUrl + "?" + mockService.createLinkString(notify);
		return url;
	}



	protected void setCSRFToken(final Map<String, String> params, final HttpServletRequest request)
	{
		final CSRFRequestDataValueProcessor proc = new CSRFRequestDataValueProcessor();
		final Map<String, String> csrfHiddenField = proc.getExtraHiddenFields(request);
		params.putAll(csrfHiddenField);
	}



}
