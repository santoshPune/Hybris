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
package de.hybris.platform.chineseprofile.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.acceleratorstorefrontcommons.forms.GuestForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.LoginForm;
import de.hybris.platform.acceleratorstorefrontcommons.forms.RegisterForm;
import de.hybris.platform.chineseprofile.controllers.pages.imported.LoginPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * Chinese Login Controller. Handles login and register for the account flow.
 */
@Scope("tenant")
@RequestMapping(value = "/login")
public class ChineseLoginPageController extends LoginPageController
{

	private static final Logger LOGGER = Logger.getLogger(ChineseLoginPageController.class);

	private static final String FORM_GLOBAL_ERROR = "form.global.error";
	private static final String MOBILE_BIND_URL = "/mobile/register/bind";
	private static final String MOBILE_REBIND_URL = "/mobile/rebind";

	@Resource(name="httpSessionRequestCache")
	private HttpSessionRequestCache httpSessionRequestCache;


	/**
	 * This method takes data from the registration form and create a new customer account and attempts to log in using
	 * the credentials of this new user.
	 *
	 * @return true if there are no binding errors or the account does not already exists.
	 * @throws CMSItemNotFoundException
	 */
	@Override
	protected String processRegisterUserRequest(final String referer, final RegisterForm form, final BindingResult bindingResult,
			final Model model, final HttpServletRequest request, final HttpServletResponse response,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		if (bindingResult.hasErrors())
		{
			model.addAttribute(form);
			model.addAttribute(new LoginForm());
			model.addAttribute(new GuestForm());
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			return handleRegistrationError(model);
		}

		final RegisterData data = new RegisterData();
		data.setFirstName(form.getFirstName());
		data.setLastName(form.getLastName());
		data.setLogin(form.getEmail());
		data.setPassword(form.getPwd());
		data.setTitleCode(form.getTitleCode());
		try
		{
			getCustomerFacade().register(data);
			getAutoLoginStrategy().login(form.getEmail().toLowerCase(), form.getPwd(), request, response);
		}
		catch (final DuplicateUidException e)
		{
			LOGGER.warn("registration failed: " + e);
			model.addAttribute(form);
			model.addAttribute(new LoginForm());
			model.addAttribute(new GuestForm());
			bindingResult.rejectValue("email", "registration.error.account.exists.title");
			GlobalMessages.addErrorMessage(model, FORM_GLOBAL_ERROR);
			return handleRegistrationError(model);
		}

		return REDIRECT_PREFIX + MOBILE_BIND_URL;
	}

	@Override
	protected void storeReferer(final String referer, final HttpServletRequest request, final HttpServletResponse response)
	{
		if (StringUtils.isNotBlank(referer) && !StringUtils.endsWith(referer, "/login")
				&& !StringUtils.endsWith(referer, MOBILE_REBIND_URL)
				&& !StringUtils.endsWith(referer, MOBILE_BIND_URL)
				&& StringUtils.contains(referer, request.getServerName()))
		{
			httpSessionRequestCache.saveRequest(request, response);
		}
	}
}
