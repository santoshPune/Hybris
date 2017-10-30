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
package de.hybris.platform.entitlementfacades.entitlement.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.entitlementfacades.data.EntitlementData;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.hybris.commons.conversion.ConversionException;
import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Required;

/**
 * Converter implementation for {@link com.hybris.services.entitlements.condition.ConditionData} as source and
 * {@link de.hybris.platform.entitlementfacades.data.EntitlementData} as target type.
 */
public class TimeframeConditionEntitlementPopulator<SOURCE extends ConditionData, TARGET extends EntitlementData>
        implements Populator<SOURCE, TARGET>
{
    private static final String GRANT_PARAMETER_START = "startTime";
    private static final String GRANT_PARAMETER_END = "endTime";
	private static final String TIME_FRAME_TYPE = "timeframe";

	private DateTimeConverter dateTimeConverter;

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		if (TIME_FRAME_TYPE.equals(source.getType()))
		{
			try
			{
				final String startTime = source.getProperty(GRANT_PARAMETER_START);
				target.setStartTime(dateTimeConverter.convertStringToDate(startTime));

				final String endTime = source.getProperty(GRANT_PARAMETER_END);
				target.setEndTime(dateTimeConverter.convertStringToDate(endTime));
			}
			catch (final ParseException e)
			{
				throw new ConversionException("Wrong date format of timeframe condition", e);
			}
		}
	}

	@Required
	public void setDateTimeConverter(final DateTimeConverter dateTimeConverter)
	{
		this.dateTimeConverter = dateTimeConverter;
	}
}
