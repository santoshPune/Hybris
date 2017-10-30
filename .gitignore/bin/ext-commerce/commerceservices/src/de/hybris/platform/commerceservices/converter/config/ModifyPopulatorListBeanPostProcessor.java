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
 * BeanPostProcessor that looks for beans of type ModifyPopulatorList and applies the modifications to the target list.
 *
 * @deprecated since 6.1 please use {@link de.hybris.platform.converters.impl.ModifyPopulatorListBeanPostProcessor}
 *             instead
 */
@Deprecated
public class ModifyPopulatorListBeanPostProcessor extends de.hybris.platform.converters.impl.ModifyPopulatorListBeanPostProcessor
{
	//deprecated
}
