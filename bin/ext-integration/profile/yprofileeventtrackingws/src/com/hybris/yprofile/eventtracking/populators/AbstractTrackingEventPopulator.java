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
import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.services.constants.TrackingEventJsonFields;
import de.hybris.eventtracking.services.populators.AbstractTrackingEventGenericPopulator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.Map;

public class AbstractTrackingEventPopulator extends AbstractTrackingEventGenericPopulator {


    public AbstractTrackingEventPopulator(ObjectMapper mapper) {
        super(mapper);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AbstractTrackingEvent.class.isAssignableFrom(clazz);
    }

    @Override
    public void populate(Map<String, Object> trackingEventData, AbstractTrackingEvent trackingEvent) throws ConversionException {

        trackingEvent.setConsentReference((String) trackingEventData.get(TrackingEventJsonFields.CONSENT_REFERENCE.getKey()));

        trackingEvent.setIdsite((String)trackingEventData.get(TrackingEventJsonFields.IDSITE.getKey()));
        trackingEvent.setRes((String)trackingEventData.get(TrackingEventJsonFields.SCREEN_RESOLUTION.getKey()));
        trackingEvent.setUserAgent((String)trackingEventData.get(TrackingEventJsonFields.USER_AGENT.getKey()));
        trackingEvent.setAccept((String)trackingEventData.get(TrackingEventJsonFields.ACCEPT.getKey()));
        trackingEvent.setAcceptLanguage((String) trackingEventData.get(TrackingEventJsonFields.ACCEPT_LANGUAGE.getKey()));

        trackingEvent.setReferer((String)trackingEventData.get(TrackingEventJsonFields.REFERER.getKey()));

        trackingEvent.setCvar((String)trackingEventData.get(TrackingEventJsonFields.COMMON_CVAR_PAGE.getKey()));

        trackingEvent.setData((String) trackingEventData.get(TrackingEventJsonFields.DATA.getKey()));
        trackingEvent.setSearch_cat((String) trackingEventData.get(TrackingEventJsonFields.SEARCH_CATEGORY.getKey()));
        trackingEvent.setSearch_count((String)trackingEventData.get(TrackingEventJsonFields.SEARCH_COUNT.getKey()));

        trackingEvent.setEc_id((String)trackingEventData.get(TrackingEventJsonFields.COMMERCE_ORDER_ID.getKey()));
        trackingEvent.setEc_items((String)trackingEventData.get(TrackingEventJsonFields.COMMERCE_CART_ITEMS.getKey()));
        trackingEvent.setEc_st((String)trackingEventData.get(TrackingEventJsonFields.COMMERCE_ST.getKey()));
        trackingEvent.setEc_tx((String)trackingEventData.get(TrackingEventJsonFields.COMMERCE_TX.getKey()));
        trackingEvent.setEc_dt((String)trackingEventData.get(TrackingEventJsonFields.COMMERCE_DT.getKey()));
    }
}
