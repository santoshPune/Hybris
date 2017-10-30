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
import de.hybris.platform.entitlementfacades.enums.EntitlementStatus;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.hybris.services.entitlements.api.GrantData;
import com.hybris.services.entitlements.condition.ConditionData;
import com.hybris.services.entitlements.conversion.DateTimeConverter;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * Converter implementation for {@link com.hybris.services.entitlements.api.GrantData} as source and
 * {@link de.hybris.platform.entitlementfacades.data.EntitlementData} as target type.
 */
public class GrantEntitlementPopulator<SOURCE extends GrantData, TARGET extends EntitlementData>
        implements Populator<SOURCE, TARGET>
{
    private List<Populator<ConditionData, EntitlementData>> conditionPopulators;
	private DateTimeConverter dateTimeConverter;

    @Override
    public void populate(final SOURCE source, final TARGET target) throws ConversionException
    {
		Assert.notNull(target, "Target must not be null");
		Assert.notNull(source, "Source must not be null");
		try
		{
			target.setGrantTime(dateTimeConverter.convertStringToDate(source.getGrantTime()));
		}
		catch (final ParseException e)
		{
			throw new ConversionException("Wrong date format of grantTime", e);
		}
		try
		{
			if (source.getStatus() == null)
			{
				target.setStatus(EntitlementStatus.ACTIVE);
			}
			else
			{
				target.setStatus(EntitlementStatus.valueOf(source.getStatus().toString()));
			}
		}
		catch (final IllegalArgumentException e)
		{
			throw new ConversionException("Unknown Grant status " + source.getStatus(), e);
		}

        final List<ConditionData> conditions = source.getConditions();
        if (conditions != null)
        {
            for (final ConditionData condition : conditions)
            {
                for (final Populator<ConditionData, EntitlementData> populator : getConditionPopulators())
                {
                    populator.populate(condition, target);
                }
            }
        }

		if (target.getStartTime() == null)
		{
			target.setStartTime(target.getGrantTime());
		}

	}

    protected List<Populator<ConditionData, EntitlementData>> getConditionPopulators()
    {
        return conditionPopulators;
    }

	@Required
    public void setConditionPopulators(final List<Populator<ConditionData, EntitlementData>> conditionPopulators)
    {
        this.conditionPopulators = conditionPopulators;
    }

	@Required
	public void setDateTimeConverter(final DateTimeConverter dateTimeConverter)
	{
		this.dateTimeConverter = dateTimeConverter;
	}
}
