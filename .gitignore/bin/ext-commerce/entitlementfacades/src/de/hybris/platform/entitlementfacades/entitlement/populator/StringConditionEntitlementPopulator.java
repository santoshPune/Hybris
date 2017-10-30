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

import com.hybris.services.entitlements.condition.ConditionData;

/**
 * Converter implementation for {@link com.hybris.services.entitlements.condition.ConditionData} as source and
 * {@link de.hybris.platform.entitlementfacades.data.EntitlementData} as target type.
 */
public class StringConditionEntitlementPopulator<SOURCE extends ConditionData, TARGET extends EntitlementData>
        implements Populator<SOURCE, TARGET>
{
    private static final String GRANT_PARAMETER_STRING = "string";
    private static final String STRING_TYPE = "string";

    @Override
    public void populate(final SOURCE source, final TARGET target)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("source", source);
        ServicesUtil.validateParameterNotNullStandardMessage("target", target);

        if(STRING_TYPE.equals(source.getType()))
        {
            final String stringCondition = source.getProperty(GRANT_PARAMETER_STRING);
            target.setConditionString(stringCondition);
        }
    }
}
