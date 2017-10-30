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

import de.hybris.eventtracking.model.events.AbstractTrackingEvent;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class AbstractTrackingEventToCsvDataPopulator implements Populator<AbstractTrackingEvent, TrackingEventCsvData> {

    @Override
    public void populate(final AbstractTrackingEvent source, final TrackingEventCsvData target) throws ConversionException {

        target.setConsentReference(source.getConsentReference());

        target.setIdsite(source.getIdsite());
        target.setRes(source.getRes());
        target.setUserAgent(source.getUserAgent());
        target.setAccept(source.getAccept());
        target.setAcceptLanguage(source.getAcceptLanguage());
        target.setReferer(source.getReferer());

        target.setCvar(source.getCvar());
        target.setData(source.getData());
        target.setSearch_cat(source.getSearch_cat());

        target.setSearch_count(source.getSearch_count());
        target.setEc_id(source.getEc_id());
        target.setEc_items(source.getEc_items());
        target.setEc_st(source.getEc_st());
        target.setEc_tx(source.getEc_tx());
        target.setEc_dt(source.getEc_dt());
    }
}
