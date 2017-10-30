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

import de.hybris.eventtracking.model.events.AbstractProductAwareTrackingEvent;
import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.publisher.csv.converters.AbstractProductAwareTrackingEventToCsvDataPopulator;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class ProductAwareTrackingEventToCsvDataPopulator extends AbstractProductAwareTrackingEventToCsvDataPopulator {

    /**
     * @see de.hybris.platform.converters.Populator#populate(java.lang.Object, java.lang.Object)
     */
    @Override
    public void populate(final AbstractTrackingEvent source, final TrackingEventCsvData target) throws ConversionException
    {
        if (AbstractProductAwareTrackingEvent.class.isAssignableFrom(source.getClass()))
        {
            target.setCategoryId(((AbstractProductAwareTrackingEvent) source).getCategoryId());
            target.setProductPrice(((AbstractProductAwareTrackingEvent) source).getProductPrice());
        }
    }
}
