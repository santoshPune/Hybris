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
 *
*/
package de.hybris.platform.product.impl;


import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ConfiguratorSettingsService;
import de.hybris.platform.product.ConfiguratorSettingsResolutionStrategy;
import de.hybris.platform.product.model.AbstractConfiguratorSettingModel;
import org.springframework.beans.factory.annotation.Required;

import javax.annotation.Nonnull;
import java.util.List;


/**
 * Default implementation of {@code ConfiguratorSettingsService}.
 */
public class DefaultConfiguratorSettingsService implements ConfiguratorSettingsService
{
	private ConfiguratorSettingsResolutionStrategy configuratorSettingsResolutionStrategy;

	@Nonnull
	@Override
	public List<AbstractConfiguratorSettingModel> getConfiguratorSettingsForProduct(
			@Nonnull final ProductModel product)
	{
			return getConfiguratorSettingsResolutionStrategy().getConfiguratorSettingsForProduct(product);
	}

	protected ConfiguratorSettingsResolutionStrategy getConfiguratorSettingsResolutionStrategy()
	{
		return configuratorSettingsResolutionStrategy;
	}

	@Required
	public void setConfiguratorSettingsResolutionStrategy(
			final ConfiguratorSettingsResolutionStrategy configuratorSettingsResolutionStrategy)
	{
		this.configuratorSettingsResolutionStrategy = configuratorSettingsResolutionStrategy;
	}
}
