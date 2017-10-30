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
import de.hybris.platform.entitlementservices.enums.EntitlementTimeUnit;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Converter implementation for {@link de.hybris.platform.entitlementservices.data.EmsGrantData} as source and
 * {@link com.hybris.services.entitlements.condition.ConditionData} as target type.
 */

public class EmsGrantDataTimeConditionPopulator implements Populator<EmsGrantData, ConditionData>
{
	static final String GRANT_PARAMETER_START = "startTime";
	static final String GRANT_PARAMETER_END = "endTime";

	private static final Logger LOG = Logger.getLogger(EmsGrantDataTimeConditionPopulator.class);

	private static final String TIMEFRAME_TYPE = "timeframe";
	private static final Map<EntitlementTimeUnit, Integer> TIME_UNIT_MAPPINGS = new HashMap<EntitlementTimeUnit, Integer>()
	{
		{
			this.put(EntitlementTimeUnit.DAY, Calendar.DAY_OF_MONTH);
			this.put(EntitlementTimeUnit.MONTH, Calendar.MONTH);
		}
	};
	private DateTimeConverter dateTimeConverter;

	@Override
	public void populate(final EmsGrantData source, final ConditionData target) throws ConversionException
	{
		ServicesUtil.validateParameterNotNullStandardMessage("source", source);
		ServicesUtil.validateParameterNotNullStandardMessage("target", target);

		if (source.getTimeUnit() != null)
		{
			final Integer start = source.getTimeUnitStart();
			if (start != null)
			{
				final Date creation = source.getCreatedAt() == null ? new Date() : source.getCreatedAt();
				final Integer duration = source.getTimeUnitDuration();
				final EntitlementTimeUnit timeUnit = source.getTimeUnit();

				if (start > 0)
				{
					final Date beginning = add(creation, start-1, timeUnit);

					target.setType(TIMEFRAME_TYPE);
					target.setProperty(GRANT_PARAMETER_START, getDateTimeConverter().convertDateToString(beginning));

					if (duration != null)
					{
						if (duration < 0)
						{
							throw new ConversionException("Duration must be positive");
						}
						if (duration > 0)
						{
							final Date ending = add(beginning, duration, timeUnit);
							target.setProperty(GRANT_PARAMETER_END, getDateTimeConverter().convertDateToString(ending));
						}
					}
				}
				else
				{
					throw new ConversionException("Start time must be positive");
				}
			}
			else
			{
				if (source.getTimeUnitDuration() == null)
				{
					LOG.warn("ProductEntitlement \"" + source.getProductEntitlementId() +
							"\": Time unit is defined without any time bound. Define start time or clean time unit.");
				}
				else
				{
					throw new ConversionException("Duration without start time");
				}
			}
		}
		else
		{
			if (source.getTimeUnitStart() != null)
			{
				LOG.warn("ProductEntitlement \"" + source.getProductEntitlementId() +
						"\": Start time is defined, but time unit isn't. Define time unit or clean start time.");
			}
		}
	}

	private Date add(final Date date, final int increment, final EntitlementTimeUnit timeUnit)
	{
		if (increment == 0)
		{
			return date;
		}
		final Integer step = TIME_UNIT_MAPPINGS.get(timeUnit);
		if (step == null)
		{
			throw new IllegalStateException("Unknown timeUnit " + timeUnit.toString());
		}
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(step, increment);
		return cal.getTime();
	}

	private DateTimeConverter getDateTimeConverter()
	{
		return dateTimeConverter;
	}

	@Required
	public void setDateTimeConverter(final DateTimeConverter dateTimeConverter)
	{
		this.dateTimeConverter = dateTimeConverter;
	}
}
