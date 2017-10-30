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
package de.hybris.platform.emsclient.facades.impl;

import de.hybris.platform.entitlementservices.data.EmsGrantData;
import de.hybris.platform.entitlementservices.facades.EntitlementFacadeDecorator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import com.hybris.services.entitlements.api.GrantData;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * DefaultEMSEntitlementFacadeDecorator class. Facade to Entitlements and Metering service.
 */
public class DefaultEMSEntitlementFacadeDecorator implements EntitlementFacadeDecorator
{
	private static final Logger LOG = Logger.getLogger(EntitlementFacadeDecorator.class);
	private Converter<EmsGrantData, GrantData> emsGrantGrantConverter;
	private com.hybris.services.entitlements.api.EntitlementFacade entitlementFacade;



	@Override
	public String createEntitlement(final EmsGrantData emsGrantData)
	{
		final GrantData grantData = getEmsGrantGrantConverter().convert(emsGrantData);
		final GrantData grantDataStored = getEntitlementFacade().createGrant(grantData);
		return grantDataStored != null ? grantDataStored.getId() : StringUtils.EMPTY;
	}

    @Override
    public Collection<GrantData> getGrants(String userId)
    {
        final List<GrantData> grants = getEntitlementFacade().getGrants(userId, null, null, null);
		return grants == null ? Collections.<GrantData>emptyList() : grants;
    }

	protected Converter<EmsGrantData, GrantData> getEmsGrantGrantConverter()
	{
		return emsGrantGrantConverter;
	}

	@Required
	public void setEmsGrantGrantConverter(final Converter<EmsGrantData, GrantData> emsGrantGrantConverter)
	{
		this.emsGrantGrantConverter = emsGrantGrantConverter;
	}

	protected com.hybris.services.entitlements.api.EntitlementFacade getEntitlementFacade()
	{
		return entitlementFacade;
	}

	@Required
	public void setEntitlementFacade(final com.hybris.services.entitlements.api.EntitlementFacade entitlementFacade)
	{
		this.entitlementFacade = entitlementFacade;
	}
}
