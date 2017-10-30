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
package de.hybris.platform.commerceservices.converter.config;

/**
 * Spring Bean used to configure modifications to existing populator lists. Supports adding or removing a converter (or
 * both). The actual modification is done by a BeanPostProcessor.
 *
 * @deprecated since 6.1 please use {@link de.hybris.platform.converters.impl.ModifyPopulatorList}
 */
@Deprecated
public class ModifyPopulatorList<SOURCE, TARGET> extends de.hybris.platform.converters.impl.ModifyPopulatorList<SOURCE, TARGET>
{
	//deprecated
}
