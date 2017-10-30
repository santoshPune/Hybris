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
package de.hybris.platform.sap.productconfig.frontend.util;

import de.hybris.platform.sap.productconfig.facades.ConfigurationData;


public interface ConfigDataMergeProcessor
{

	public void completeInput(final ConfigurationData targetConfigData);

	public void mergeConfigurationData(final ConfigurationData source, final ConfigurationData target);

}
