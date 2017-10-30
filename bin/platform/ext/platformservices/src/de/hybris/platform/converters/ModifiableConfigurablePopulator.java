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
 */
package de.hybris.platform.converters;

import de.hybris.platform.converters.config.ConfigurablePopulatorModification;


/**
 * Interface for modifiable configurable populators.
 */
public interface ModifiableConfigurablePopulator<SOURCE, TARGET, OPTION> extends ConfigurablePopulator<SOURCE, TARGET, OPTION>
{
	void applyModification(ConfigurablePopulatorModification<SOURCE, TARGET, OPTION> modification);
}
