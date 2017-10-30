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
package de.hybris.platform.smarteditwebservices.configuration.facade.impl;

import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.smarteditwebservices.configuration.SmarteditConfigurationDuplicateKeyException;
import de.hybris.platform.smarteditwebservices.configuration.SmarteditConfigurationException;
import de.hybris.platform.smarteditwebservices.configuration.SmarteditConfigurationNotFoundException;
import de.hybris.platform.smarteditwebservices.configuration.facade.DefaultConfigurationKey;
import de.hybris.platform.smarteditwebservices.configuration.facade.SmarteditConfigurationFacade;
import de.hybris.platform.smarteditwebservices.configuration.service.SmarteditConfigurationService;
import de.hybris.platform.smarteditwebservices.data.ConfigurationData;
import de.hybris.platform.smarteditwebservices.dto.UpdateConfigurationDto;
import de.hybris.platform.smarteditwebservices.model.SmarteditConfigurationModel;
import de.hybris.platform.smarteditwebservices.validation.facade.FacadeValidationService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.validation.Validator;

import static java.lang.String.format;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Default implementation of the {@link SmarteditConfigurationFacade}
 */
public class DefaultSmarteditConfigurationFacade implements SmarteditConfigurationFacade
{

	private static final Logger LOGGER = getLogger(DefaultSmarteditConfigurationFacade.class);

	private SmarteditConfigurationService smarteditConfigurationService;

	private AbstractPopulatingConverter<SmarteditConfigurationModel, ConfigurationData> configurationModelToDataConverter;

	private AbstractPopulatingConverter<ConfigurationData, SmarteditConfigurationModel> configurationDataToModelConverter;

	private FacadeValidationService facadeValidationService;
	private Validator createConfigurationValidator;
	private Validator updateConfigurationValidator;

	@Override
	public List<ConfigurationData> findAll()
	{
		try
		{
			final List<SmarteditConfigurationModel> smartEditConfigurationModels = getSmarteditConfigurationService().findAll();
			if (smartEditConfigurationModels == null)
			{
				return Collections.emptyList();
			}
			return smartEditConfigurationModels.stream().map(getConfigurationModelToDataConverter()::convert).collect(
					Collectors.toList());
		}
		catch (ModelSavingException e)
		{
			throw new SmarteditConfigurationException("Error loading all configurations.", e);
		}
	}

	@Override
	public ConfigurationData create(final ConfigurationData configurationData)
	{

		getFacadeValidationService().validate(getCreateConfigurationValidator(), configurationData);

		try
		{
			final SmarteditConfigurationModel smartEditConfigurationModel = getSmarteditConfigurationService().create(
					getConfigurationDataToModelConverter().convert(configurationData));
			return getConfigurationModelToDataConverter().convert(smartEditConfigurationModel);
		}
		catch (AmbiguousIdentifierException e)
		{
			throw new SmarteditConfigurationDuplicateKeyException(e.getMessage(), e);
		}
		catch (ModelSavingException e)
		{
			throw new SmarteditConfigurationException("Error saving configuration data.", e);
		}
	}


	@Override
	public ConfigurationData update(final String uid, final ConfigurationData configurationData)
	{
		getFacadeValidationService().validate(getUpdateConfigurationValidator(),
				getUpdateConfigurationDto(uid, configurationData));

		try
		{
			final SmarteditConfigurationModel smartEditConfigurationModel = getSmarteditConfigurationService()
					.update(uid, getConfigurationDataToModelConverter().convert(configurationData));
			return getConfigurationModelToDataConverter()
					.convert(smartEditConfigurationModel);
		}
		catch (UnknownIdentifierException e)
		{
			throw new SmarteditConfigurationNotFoundException(e.getMessage(), e);
		}
		catch (AmbiguousIdentifierException e)
		{
			throw new SmarteditConfigurationDuplicateKeyException(e.getMessage(), e);
		}
		catch (ModelSavingException e)
		{
			throw new SmarteditConfigurationException("Error updating configuration data.", e);
		}
	}

	/**
	 * Builds a new bean for updating the configuraiton
	 * @param uid the uid sent to this façade through the update method
	 * @param configurationData the data bean sent for modificaiton
	 * @return a UpdateConfigurationDto
	 */
	protected UpdateConfigurationDto getUpdateConfigurationDto(final String uid, final ConfigurationData configurationData)
	{
		UpdateConfigurationDto updateConfigurationDto = new UpdateConfigurationDto();
		updateConfigurationDto.setUid(uid);
		updateConfigurationDto.setKey(configurationData.getKey());
		updateConfigurationDto.setValue(configurationData.getValue());
		return updateConfigurationDto;
	}

	@Override
	public ConfigurationData findByUid(final String uid)
	{
		try
		{
			return getConfigurationModelToDataConverter().convert(getSmarteditConfigurationService().findByKey(uid));
		}
		catch (UnknownIdentifierException e)
		{
			throw new SmarteditConfigurationNotFoundException(e.getMessage(), e);
		}
		catch (ModelSavingException e)
		{
			throw new SmarteditConfigurationException("Error retrieving configuration data.", e);
		}
	}

	@Override
	public ConfigurationData findByDefaultConfigurationKey(final DefaultConfigurationKey key) {
		return findByUid(key.getUid());
	}

	@Override
	public Optional<ConfigurationData> tryAndFindByDefaultConfigurationKey(final DefaultConfigurationKey key) {
		try {
			return ofNullable(findByDefaultConfigurationKey(key));
		} catch (SmarteditConfigurationException | SmarteditConfigurationNotFoundException smarteditException) {
			LOGGER.info(format("Failed to find configuration for [%s]", key.getKey()), smarteditException);
		}
		return empty();
	}

	@Override
	public void delete(final String uid)
	{
		try
		{
			getSmarteditConfigurationService().delete(uid);
		}
		catch (UnknownIdentifierException e)
		{
			throw new SmarteditConfigurationNotFoundException(e.getMessage(), e);
		}
		catch (ModelSavingException e)
		{
			throw new SmarteditConfigurationException("Error deleting configuration data.", e);
		}
	}

	protected SmarteditConfigurationService getSmarteditConfigurationService()
	{
		return smarteditConfigurationService;
	}

	@Required
	public void setSmarteditConfigurationService(final SmarteditConfigurationService smarteditConfigurationService)
	{
		this.smarteditConfigurationService = smarteditConfigurationService;
	}

	protected AbstractPopulatingConverter<ConfigurationData, SmarteditConfigurationModel> getConfigurationDataToModelConverter()
	{
		return configurationDataToModelConverter;
	}

	@Required
	public void setConfigurationDataToModelConverter(
			final AbstractPopulatingConverter<ConfigurationData, SmarteditConfigurationModel> configurationDataToModelConverter)
	{
		this.configurationDataToModelConverter = configurationDataToModelConverter;
	}

	protected AbstractPopulatingConverter<SmarteditConfigurationModel, ConfigurationData> getConfigurationModelToDataConverter()
	{
		return configurationModelToDataConverter;
	}

	@Required
	public void setConfigurationModelToDataConverter(
			final AbstractPopulatingConverter<SmarteditConfigurationModel, ConfigurationData> configurationModelToDataConverter)
	{
		this.configurationModelToDataConverter = configurationModelToDataConverter;
	}

	protected FacadeValidationService getFacadeValidationService()
	{
		return facadeValidationService;
	}

	@Required
	public void setFacadeValidationService(final FacadeValidationService facadeValidationService)
	{
		this.facadeValidationService = facadeValidationService;
	}

	protected Validator getCreateConfigurationValidator()
	{
		return createConfigurationValidator;
	}

	@Required
	public void setCreateConfigurationValidator(final Validator createConfigurationValidator)
	{
		this.createConfigurationValidator = createConfigurationValidator;
	}

	protected Validator getUpdateConfigurationValidator()
	{
		return updateConfigurationValidator;
	}

	@Required
	public void setUpdateConfigurationValidator(final Validator updateConfigurationValidator)
	{
		this.updateConfigurationValidator = updateConfigurationValidator;
	}
}
