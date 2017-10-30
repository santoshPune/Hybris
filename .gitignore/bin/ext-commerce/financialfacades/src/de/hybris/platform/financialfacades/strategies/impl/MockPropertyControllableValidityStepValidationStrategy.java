/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *
 */
package de.hybris.platform.financialfacades.strategies.impl;


import de.hybris.platform.financialfacades.strategies.StepValidationStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.springframework.beans.factory.annotation.Required;


public class MockPropertyControllableValidityStepValidationStrategy implements StepValidationStrategy
{
	private ConfigurationService configurationService;
	private String propertyFlagKey;

	/**
	 * Check if step is valid strategy.
	 */
	@Override
	public boolean isValid()
	{
		final String property_str = getConfigurationService().getConfiguration().getString(propertyFlagKey);
		return Boolean.parseBoolean(property_str);
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	protected String getPropertyFlagKey()
	{
		return propertyFlagKey;
	}

	@Required
	public void setPropertyFlagKey(final String propertyFlagKey)
	{
		this.propertyFlagKey = propertyFlagKey;
	}
}
