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

import de.hybris.platform.smarteditwebservices.configuration.facade.SmarteditConfigurationFacade;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.dto.ConfigurationDataListWsDto;
import de.hybris.platform.smarteditwebservices.security.IsAuthorizedAdmin;
import de.hybris.platform.smarteditwebservices.security.IsAuthorizedCmsManager;
import de.hybris.platform.smarteditwebservices.validation.facade.ValidationException;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Controller to manage cms configuration data
 */
@Controller
@RequestMapping("/configurations")
public class ConfigurationController
{
	@Resource
	private SmarteditConfigurationFacade smarteditConfigurationFacade;

	/**
	 * Retrieve all cms configuration data.
	 *
	 * @return a list of cms configuration data
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@IsAuthorizedCmsManager
	public ConfigurationDataListWsDto loadAll()
	{
		final ConfigurationDataListWsDto configurations = new ConfigurationDataListWsDto();
		configurations.setConfigurations(getSmarteditConfigurationFacade().findAll());
		return configurations;
	}

	/**
	 * Create cms configuration data
	 *
	 * @bodyparam data {@link ConfigurationData}
	 * @return newly created cms configuration data
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	@IsAuthorizedAdmin
	public ConfigurationData save(@RequestBody final ConfigurationData data)
	{
		try
		{
			return getSmarteditConfigurationFacade().create(data);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * Retrieve cms configuration data that matches the given key value
	 *
	 * @pathparam key configuration data identifier
	 * @return cms configuration data
	 */
	@RequestMapping(value = "/{key:.+}", method = RequestMethod.GET)
	@ResponseBody
	@IsAuthorizedCmsManager
	public ConfigurationData findByKey(@PathVariable("key") final String key)
	{
		return getSmarteditConfigurationFacade().findByUid(key);
	}

	/**
	 * Update cms configuration data
	 *
	 * @bodyparam data {@link ConfigurationData}
	 * @return updated cms configuration data
	 * @throws WebserviceValidationException
	 *            when the inputed data is invalid
	 */
	@RequestMapping(value = "/{key:.+}", method = RequestMethod.PUT)
	@ResponseBody
	@IsAuthorizedAdmin
	public ConfigurationData update(@RequestBody final ConfigurationData data, @PathVariable("key") final String key)
	{
		try
		{
			return getSmarteditConfigurationFacade().update(key, data);
		}
		catch (final ValidationException e)
		{
			throw new WebserviceValidationException(e.getValidationObject());
		}
	}

	/**
	 * Remove cms configuration data that matches the given key
	 *
	 * @pathparam key configuration data identifier
	 */
	@RequestMapping(value = "/{key:.+}", method = RequestMethod.DELETE)
	@ResponseBody
	@IsAuthorizedAdmin
	public void delete(@PathVariable("key") final String key)
	{
		getSmarteditConfigurationFacade().delete(key);
	}

	public SmarteditConfigurationFacade getSmarteditConfigurationFacade()
	{
		return smarteditConfigurationFacade;
	}

	public void setSmarteditConfigurationFacade(final SmarteditConfigurationFacade smarteditConfigurationFacade)
	{
		this.smarteditConfigurationFacade = smarteditConfigurationFacade;
	}
}
