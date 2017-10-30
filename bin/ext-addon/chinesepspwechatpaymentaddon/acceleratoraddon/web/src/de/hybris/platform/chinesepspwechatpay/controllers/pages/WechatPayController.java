/**
 *
 */
package de.hybris.platform.chinesepspwechatpay.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.chinesepspwechatpay.constants.ControllerConstants;
import de.hybris.platform.chinesepspwechatpayservices.exception.WechatPayException;
import de.hybris.platform.chinesepspwechatpayservices.order.WechatPayOrderService;
import de.hybris.platform.chinesepspwechatpayservices.payment.impl.DefaultWechatPayPaymentService;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.OpenIdRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.StartPaymentRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.UnifiedOrderRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.processors.impl.UserCodeRequestProcessor;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayConfiguration;
import de.hybris.platform.chinesepspwechatpayservices.wechatpay.WechatPayHttpClient;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.core.model.order.OrderModel;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
@Scope("tenant")
@RequestMapping("/checkout/multi/wechat")
public class WechatPayController extends AbstractPageController
{
	private static final String ORDER_CODE_PATH_VARIABLE_PATTERN = "{orderCode:.*}";
	protected static final String MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL = "multiStepCheckoutSummary";

	@Value("#{ wechatPayConfiguration.timeout }")
	private Integer timeout;

	@Resource(name = "wechatPayConfiguration")
	private WechatPayConfiguration wechatPayConfiguration;

	@Resource(name = "wechatPayHttpClient")
	private WechatPayHttpClient wechatPayHttpClient;

	@Resource(name = "wechatPayOrderService")
	private WechatPayOrderService wechatPayOrderService;

	@Resource(name = "wechatpayPaymentService")
	private DefaultWechatPayPaymentService wechatpayPaymentService;

	@RequestMapping(value = "/pay/" + ORDER_CODE_PATH_VARIABLE_PATTERN)
	public String process(@PathVariable final String orderCode, @RequestParam(value = "code", required = false) final String code,
			final HttpServletRequest request, final HttpServletResponse response, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			if (StringUtils.isEmpty(code))
			{
				new UserCodeRequestProcessor(wechatPayConfiguration, request, response).process();
				return null;
			}

			final Optional<OrderModel> optional = wechatPayOrderService.getOrderByCode(orderCode);
			if (!optional.isPresent())
			{
				throw new WechatPayException("Can't find order for code:" + orderCode);
			}

			final String openId = new OpenIdRequestProcessor(wechatPayConfiguration, wechatPayHttpClient, code).process();
			final String prepayId = new UnifiedOrderRequestProcessor(wechatPayConfiguration, wechatPayHttpClient, openId,
					optional.get(), request.getRemoteAddr()).process();
			request.setAttribute("paymentData", new StartPaymentRequestProcessor(wechatPayConfiguration, prepayId).process());
			request.setAttribute("orderCode", orderCode);
			model.addAttribute("wechatPayTimeout", timeout);

			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));

			return ControllerConstants.Views.Pages.Checkout.WeChatPayPage;
		}
		catch (final WechatPayException e)
		{
			return REDIRECT_PREFIX + "/checkout/multi/summary/checkPaymentResult/" + orderCode;
		}
	}

	@RequestMapping(value = "/startPay")
	public void paySuccess(@RequestParam(value = "orderCode", required = false) final String code)
	{
		wechatpayPaymentService.createTransactionForNewRequest(code);
	}
}
