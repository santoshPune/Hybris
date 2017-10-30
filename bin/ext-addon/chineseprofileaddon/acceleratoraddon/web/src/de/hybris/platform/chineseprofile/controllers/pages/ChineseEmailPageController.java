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
package de.hybris.platform.chineseprofile.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.annotations.RequireHardLogIn;
import de.hybris.platform.acceleratorstorefrontcommons.forms.UpdateEmailForm;
import de.hybris.platform.chineseprofile.controllers.pages.imported.AccountPageController;
import de.hybris.platform.chineseprofilefacades.customer.CustomerFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.user.UserFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Scope("tenant")
@RequestMapping("/my-account/update-email")
public class ChineseEmailPageController extends AccountPageController
{
	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "chineseCustomerFacade")
	private CustomerFacade chineseCustomerFacade;

	@Override
	@RequestMapping(value = "", method = RequestMethod.POST, params = "emailLanguage")
	@RequireHardLogIn
	public String updateEmail(final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		final String emailLanguage = request.getParameter("emailLanguage");
		if (StringUtils.isNotEmpty(emailLanguage))
		{
			chineseCustomerFacade.saveEmailLanguageForCurrentUser(emailLanguage);
		}
		return super.updateEmail(updateEmailForm, bindingResult, model, redirectAttributes);
	}

}
