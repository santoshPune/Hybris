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
package de.hybris.platform.commercefacades.converter;

import de.hybris.platform.commercefacades.converter.config.ConfigurablePopulatorModification;


/**
 * Interface for modifiable configurable populators.
 *
 * @deprecated use {@link de.hybris.platform.converters.ModifiableConfigurablePopulator} instead. Will be removed in
 *             version 6.2
 */
@SuppressWarnings("deprecation")
@Deprecated
public interface ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION> extends ConfigurablePopulator<SOURCE, TARGET, OPTION>,
		de.hybris.platform.converters.ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	@Deprecated
	void addModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);

	@Deprecated
	void applyModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);
}
