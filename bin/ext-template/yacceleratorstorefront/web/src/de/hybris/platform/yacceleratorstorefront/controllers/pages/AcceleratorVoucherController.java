/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.yacceleratorstorefront.controllers.pages;

import de.hybris.platform.acceleratorfacades.cart.data.CartWrapperData;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.util.XSSFilterUtil;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


@Controller
@RequestMapping("/voucher")
public class AcceleratorVoucherController extends AbstractPageController
{
	@Resource(name = "voucherFacade")
	private VoucherFacade voucherFacade;

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@ResponseBody
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public CartWrapperData applyVoucher(final String voucherCode)
	{
		String errorMsg = null;
		final String encodedVoucherCode = XSSFilterUtil.filter(voucherCode);
		try
		{
			voucherFacade.applyVoucher(encodedVoucherCode);
		}
		catch (VoucherOperationException e)
		{
			errorMsg = getMessageSource().getMessage("text.voucher.apply.invalid.error", null, getI18nService().getCurrentLocale());
		}
		return makeCartWrapperData(cartFacade.getSessionCart(), errorMsg,
				(errorMsg == null) ? getMessageSource().getMessage("text.voucher.apply.applied.success", new Object[]
				{ voucherCode }, getI18nService().getCurrentLocale()) : null);
	}

	@ResponseBody
	@RequestMapping(value = "/{code}/remove", method = RequestMethod.DELETE)
	public CartWrapperData removeVoucher(@PathVariable("code") final String code, final Model model)
	{
		String errorMsg = null;
		try
		{
			voucherFacade.releaseVoucher(code);
		}
		catch (VoucherOperationException e)
		{
			errorMsg = getMessageSource().getMessage("text.voucher.release.error", null, getI18nService().getCurrentLocale());
		}
		return makeCartWrapperData(cartFacade.getSessionCart(), errorMsg, null);
	}


	@ResponseBody
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<VoucherData> getVouchersForCart()
	{
		return voucherFacade.getVouchersForCart();
	}


	protected CartWrapperData makeCartWrapperData(final CartData cartData, final String errorMsg, final String successMsg)
	{
		final CartWrapperData cartWrapperData = new CartWrapperData();
		cartWrapperData.setCartData(cartData);
		cartWrapperData.setErrorMsg(errorMsg);
		cartWrapperData.setSuccessMsg(successMsg);
		return cartWrapperData;
	}
}
