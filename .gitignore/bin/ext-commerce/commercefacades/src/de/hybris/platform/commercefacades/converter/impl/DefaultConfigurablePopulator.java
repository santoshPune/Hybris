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
 *
 */
package de.hybris.platform.commercefacades.converter.impl;

import de.hybris.platform.commercefacades.converter.ConfigurablePopulator;
import de.hybris.platform.converters.impl.DefaultModifableConfigurablePopulator;


/**
 * Default implementation of the {@link ConfigurablePopulator} extending {@link AbstractModifiableConfigurablePopulator}
 *
 * @deprecated use {@link DefaultModifableConfigurablePopulator} instead. Will be removed in version 6.2.
 */
@SuppressWarnings("deprecation")
@Deprecated
public class DefaultConfigurablePopulator<SOURCE, TARGET, OPTION> extends
		AbstractModifiableConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	//deprecated
}
