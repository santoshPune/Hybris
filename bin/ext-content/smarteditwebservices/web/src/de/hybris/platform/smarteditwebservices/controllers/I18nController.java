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
package de.hybris.platform.smarteditwebservices.controllers;

import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageListData;
import de.hybris.platform.smarteditwebservices.data.TranslationMapData;
import de.hybris.platform.smarteditwebservices.i18n.facade.SmarteditI18nFacade;

import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller to retrieve cms management internationalization data
 */
@RestController("smartEditI18nController")
@RequestMapping(value = "/i18n")
@PreAuthorize("permitAll")
public class I18nController
{
	@Resource
	private SmarteditI18nFacade smarteEditI18nFacade;

	/**
	 * Retrieve translated data using the specified locale value.
	 *
	 * @pathparam locale The locale identifier consisting of a language identifier and a region identifier.
	 * @param request
	 *           the {@link HttpServletRequest}
	 * @param response
	 *           the {@link HttpServletResponse}
	 * @return a map of translated data
	 */
	@RequestMapping(value = "/translations/{locale}", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public TranslationMapData getTranslationMap1(@PathVariable("locale") final Locale locale, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		final TranslationMapData translationMapData = new TranslationMapData();
		translationMapData.setValue(getSmartEditI18nFacade().getTranslationMap(locale));
		return translationMapData;
	}

	/**
	 * Retrieve list o supported language
	 *
	 * @return an object of {@link SmarteditLanguageListData} that contains a {@link List} of smartedit
	 */
	@RequestMapping(value = "/languages", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public SmarteditLanguageListData getToolingLanguages()
	{
		final SmarteditLanguageListData result = new SmarteditLanguageListData();
		result.setLanguages(getSmartEditI18nFacade().getSupportedLanguages());
		return result;
	}

	protected SmarteditI18nFacade getSmartEditI18nFacade()
	{
		return smarteEditI18nFacade;
	}

	public void setSmartEditI18nFacade(final SmarteditI18nFacade cmsI18nFacade)
	{
		this.smarteEditI18nFacade = cmsI18nFacade;
	}
}