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
package de.hybris.platform.yacceleratorstorefront.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.cms.AbstractCMSComponentController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.servicelayer.services.CMSComponentService;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("GenericComponentController")
@RequestMapping("/cms/component")
public class GenericCMSComponentController extends AbstractController
{
	private static final Logger LOG = Logger.getLogger(GenericCMSComponentController.class);
	private static final String SANITIZE_REGEX = "[^a-zA-Z0-9_]";

	@Resource(name = "cmsComponentService")
	private CMSComponentService cmsComponentService;

	@Resource(name = "userService")
	private UserService userService;

	@Value("#{configurationService.configuration.getProperty('yacceleratorstorefront.generic.cms.component.controller.authorized.group')}")
	private String authorizedGroupId;

	@RequestMapping(method = RequestMethod.GET)
	public String getCMSComponent(@RequestParam(required = true) final String componentUid, final Model model,
			final HttpServletRequest request, final HttpServletResponse response)
	{
		if (!authorizeUser())
		{
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return null;
		}

		try
		{
			final AbstractCMSComponentModel component = getCmsComponentService().getAbstractCMSComponent(componentUid);
			return getCMSComponentControllerForType(component.getItemtype()).handleGet(request, response, model);
		}
		catch (final BeansException e)
		{
			LOG.error("Could not find controller for component with UID [" + componentUid.replaceAll(SANITIZE_REGEX, "") + "]");
			throw new AbstractPageController.HttpNotFoundException(e);
		}
		catch (final CMSItemNotFoundException e)
		{
			LOG.error("Could not find component with UID [" + componentUid.replaceAll(SANITIZE_REGEX, "") + "]");
			throw new AbstractPageController.HttpNotFoundException(e);
		}
	}

	protected boolean authorizeUser()
	{
		try
		{
			final UserModel currentUser = getUserService().getCurrentUser();
			final UserGroupModel authorizedGroup = getUserService().getUserGroupForUID(authorizedGroupId);

			return getUserService().isMemberOfGroup(currentUser, authorizedGroup);
		}
		catch (final UnknownIdentifierException e)
		{
			LOG.error(String.format("Authorized user group [%s] not found.", authorizedGroupId), e);
			return false;
		}
	}

	protected AbstractCMSComponentController getCMSComponentControllerForType(final String componentType)
	{
		if (Registry.getApplicationContext().containsBean(componentType + "Controller"))
		{
			return Registry.getApplicationContext().getBean(componentType + "Controller", AbstractCMSComponentController.class);
		}
		else
		{
			return Registry.getApplicationContext().getBean("DefaultCMSComponentController", AbstractCMSComponentController.class);
		}
	}

	protected CMSComponentService getCmsComponentService()
	{
		return cmsComponentService;
	}

	protected UserService getUserService()
	{
		return userService;
	}
}
