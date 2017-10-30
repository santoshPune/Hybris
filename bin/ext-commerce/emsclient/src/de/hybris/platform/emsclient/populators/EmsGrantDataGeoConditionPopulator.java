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
package de.hybris.platform.emsclient.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.entitlementservices.data.EmsGrantData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.hybris.services.entitlements.condition.ConditionData;

import java.util.Collection;

import org.springframework.util.StringUtils;


/**
 * Converter implementation for {@link de.hybris.platform.entitlementfacades.data.EntitlementData} as source and
 * {@link com.hybris.services.entitlements.condition.ConditionData} as target type.
 */
public class EmsGrantDataGeoConditionPopulator implements Populator<EmsGrantData, ConditionData>
{
	private static final String GRANT_PARAMETER_GEO = "geoPath";
	private static final String GEO_TYPE = "geo";

	@Override
	public void populate(final EmsGrantData source, final ConditionData target) throws ConversionException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		final Collection<String> conditionGeo = source.getConditionGeo();
		if (conditionGeo != null && conditionGeo.size() > 0)
		{
			target.setType(GEO_TYPE);
			final String s = StringUtils.arrayToCommaDelimitedString(conditionGeo.toArray());
			target.setProperty(GRANT_PARAMETER_GEO, s);
		}
	}
}