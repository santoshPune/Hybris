/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.hybris.yprofile.eventtracking.populators;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.eventtracking.model.events.AbstractProductAwareTrackingEvent;
import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.services.populators.AbstractProductAwareTrackingEventPopulator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.List;
import java.util.Map;

public class ProductAwareTrackingEventPopulator extends AbstractProductAwareTrackingEventPopulator {

    public ProductAwareTrackingEventPopulator(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public void populate(final Map<String, Object> trackingEventData, final AbstractTrackingEvent trackingEvent)
            throws ConversionException
    {
        final Map<String, Object> customVariablesPageScoped = getPageScopedCvar(trackingEventData);
        String categoryIds = null;
        String productPrice = null;
        if (customVariablesPageScoped != null)
        {
            final List<String> pkpData = (List) customVariablesPageScoped.get("2");
            if (pkpData != null && pkpData.size() > 0)
            {
                productPrice =  pkpData.get(1);
            }

            final List<String> pkcData = (List) customVariablesPageScoped.get("5");
            if (pkcData != null && pkcData.size() > 0)
            {
                categoryIds =  pkcData.get(1);
            }
        }

        ((AbstractProductAwareTrackingEvent) trackingEvent).setCategoryId(categoryIds);
        ((AbstractProductAwareTrackingEvent) trackingEvent).setProductPrice(productPrice);
    }
}
