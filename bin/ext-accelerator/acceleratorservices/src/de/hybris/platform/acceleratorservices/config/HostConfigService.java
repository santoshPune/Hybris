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
package de.hybris.platform.acceleratorservices.config;

/**
 * Host config service is used to lookup a hostname specific configuration property.
 * Certain configurations are specific the the hostname that the site is accessed on,
 * these include analytics tracking packages or maps integrations.
 */
public interface HostConfigService
{
	String getProperty(String property, String hostname);

	ConfigLookup getConfigForHost(String hostname);
}
