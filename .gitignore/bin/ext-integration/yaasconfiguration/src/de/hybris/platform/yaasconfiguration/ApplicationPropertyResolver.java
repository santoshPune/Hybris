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
package de.hybris.platform.yaasconfiguration;

import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import com.hybris.charon.conf.PropertyResolver;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;


/**
 * maps YaasApplicationModel to charon properties. Only oauth.clientId, oauth.clientSecret are returned
 */
public class ApplicationPropertyResolver implements PropertyResolver
{
	private final YaasConfigurationService yaasConfig;
	private final String appId;


	/**
	 * Indicated for single-tenant applications. It pickups the first YaasConfigurationModel.
	 *
	 * @param yaasConfig
	 */
	public ApplicationPropertyResolver(final YaasConfigurationService yaasConfig)
	{
		this.yaasConfig = yaasConfig;
		this.appId = null;
	}

	/**
	 * Indicated for multi-tenant applications. It pickups the given YaasConfigurationModel with appId identifier.
	 *
	 * @param yaasConfig
	 * @param appId
	 */
	public ApplicationPropertyResolver(final YaasConfigurationService yaasConfig, final String appId)
	{
		this.yaasConfig = yaasConfig;
		this.appId = appId;
	}

	@Override
	public boolean contains(final String key)
	{
		return credentials(key, StringUtils::isNotEmpty, false);
	}

	@Override
	public String lookup(final String key)
	{
		return credentials(key, identity(), null);
	}

	protected <T> T credentials(final String key, final Function<String, T> func, final T defaultValue)
	{
		final Optional<YaasApplicationModel> model = model();
		if (!model.isPresent())
		{
			return defaultValue;
		}
		switch (key)
		{
			case "oauth.clientId":
				return func.apply(model.get().getClientId());
			case "oauth.clientSecret":
				return func.apply(model.get().getClientSecret());
			default:
				return defaultValue;
		}
	}


	protected Optional<YaasApplicationModel> model()
	{
		if (isEmpty(appId))
		{
			return yaasConfig.takeFirstModel();
		}
		return ofNullable(yaasConfig.getYaasApplicationForId(appId));
	}
}
