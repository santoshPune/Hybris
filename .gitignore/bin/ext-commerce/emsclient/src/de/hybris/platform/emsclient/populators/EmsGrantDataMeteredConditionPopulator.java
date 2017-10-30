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

import org.apache.log4j.Logger;


/**
 * Converter implementation for {@link de.hybris.platform.entitlementfacades.data.EntitlementData} as source and
 * {@link com.hybris.services.entitlements.condition.ConditionData} as target type.
 */
public class EmsGrantDataMeteredConditionPopulator implements Populator<EmsGrantData, ConditionData>
{
	private static final String METERED_TYPE = "metered";
	private static final int UNLIMITED_QUANTITY = -1;
	private static final String GRANT_PARAMETER_MAX_QUANTITY = "maxQuantity";
	private static final String GRANT_PARAMETER_OVERAGE = "allowOverage";

	private static final Logger LOG = Logger.getLogger(EmsGrantDataMeteredConditionPopulator.class);

	@Override
	public void populate(final EmsGrantData source, final ConditionData target) throws ConversionException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		final Integer maxQuantity = source.getMaxQuantity();
		if (maxQuantity != null) {
			if (isUnlimited(maxQuantity))
			{
				target.setType(METERED_TYPE);
				target.setProperty(GRANT_PARAMETER_MAX_QUANTITY, "0");
				target.setProperty(GRANT_PARAMETER_OVERAGE, "true");
			}
			else if (maxQuantity > 0)
			{
				target.setType(METERED_TYPE);
				target.setProperty(GRANT_PARAMETER_MAX_QUANTITY, String.valueOf(maxQuantity));
			}
			else
			{
				throw new ConversionException("EmsGrantData.maxQuantity can not be " + maxQuantity);
			}
		}
	}

	private boolean isUnlimited(final int quantity)
	{
		return quantity == 0 || quantity == UNLIMITED_QUANTITY;
	}
}
