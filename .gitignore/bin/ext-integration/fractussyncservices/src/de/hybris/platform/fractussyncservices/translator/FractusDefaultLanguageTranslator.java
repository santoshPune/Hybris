/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.fractussyncservices.translator;

import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.translators.AbstractSpecialValueTranslator;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.config.ConfigurationService;


public class FractusDefaultLanguageTranslator extends AbstractSpecialValueTranslator
{

	private ConfigurationService configurationService;
	private static final String defaultLanguage = "en";

	@Override
	public void init(SpecialColumnDescriptor columnDescriptor) throws HeaderValidationException
	{
		configurationService = (ConfigurationService) Registry.getApplicationContext().getBean("configurationService");
	}

	@Override
	public String performExport(final Item item) throws ImpExException
	{
		return getConfigurationService().getConfiguration().getString("yaas.default.language", defaultLanguage);
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
