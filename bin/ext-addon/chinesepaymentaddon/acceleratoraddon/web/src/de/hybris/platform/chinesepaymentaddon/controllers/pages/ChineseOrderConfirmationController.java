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
package de.hybris.platform.chinesepaymentaddon.controllers.pages;

import de.hybris.platform.acceleratorservices.controllers.page.PageType;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.ThirdPartyConstants;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestRegisterForm;
import de.hybris.platform.chinesepaymentaddon.constants.ControllerConstants;
import de.hybris.platform.chinesepaymentaddon.controllers.imported.CheckoutController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.util.ResponsiveUtils;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Scope("tenant")
@RequestMapping(value = "/checkout")
public class ChineseOrderConfirmationController extends CheckoutController
{
	private static final String CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL = "orderConfirmation";
	private static final String CONTINUE_URL_KEY = "continueUrl";

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Override
	protected String processOrderCode(final String orderCode, final Model model, final HttpServletRequest request)
			throws CMSItemNotFoundException
	{
		final OrderData orderDetails = orderFacade.getOrderDetailsForCode(orderCode);

		if (orderDetails.isGuestCustomer()
				&& !StringUtils.substringBefore(orderDetails.getUser().getUid(), "|").equals(
						getSessionService().getAttribute(WebConstants.ANONYMOUS_CHECKOUT_GUID)))
		{
			return getCheckoutRedirectUrl();
		}

		if (orderDetails.getEntries() != null && !orderDetails.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : orderDetails.getEntries())
			{
				final String productCode = entry.getProduct().getCode();
				final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
				entry.setProduct(product);
			}
		}

		model.addAttribute("orderCode", orderCode);
		model.addAttribute("orderData", orderDetails);
		model.addAttribute("allItems", orderDetails.getEntries());
		model.addAttribute("deliveryAddress", orderDetails.getDeliveryAddress());
		model.addAttribute("deliveryMode", orderDetails.getDeliveryMode());
		model.addAttribute("paymentInfo", orderDetails.getPaymentInfo());
		model.addAttribute("pageType", PageType.ORDERCONFIRMATION.name());

		final String uid;

		if (orderDetails.isGuestCustomer() && !model.containsAttribute("guestRegisterForm"))
		{
			final GuestRegisterForm guestRegisterForm = new GuestRegisterForm();
			guestRegisterForm.setOrderCode(orderDetails.getGuid());
			uid = StringUtils.substringAfter(orderDetails.getUser().getUid(), "|");
			guestRegisterForm.setUid(uid);
			model.addAttribute(guestRegisterForm);
		}
		else
		{
			uid = orderDetails.getUser().getUid();
		}
		model.addAttribute("email", uid);

		final String continueUrl = (String) getSessionService().getAttribute(WebConstants.CONTINUE_URL);
		model.addAttribute(CONTINUE_URL_KEY, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);

		final AbstractPageModel cmsPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL);
		storeCmsPageInModel(model, cmsPage);
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL));
		model.addAttribute(ThirdPartyConstants.SeoRobots.META_ROBOTS, ThirdPartyConstants.SeoRobots.NOINDEX_NOFOLLOW);

		if (ResponsiveUtils.isResponsive())
		{
			return getViewForPage(model);
		}

		return ControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
	}
}
