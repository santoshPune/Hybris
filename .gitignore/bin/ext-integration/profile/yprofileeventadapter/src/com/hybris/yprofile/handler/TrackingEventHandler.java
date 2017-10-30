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
package com.hybris.yprofile.handler;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.hybris.yprofile.dto.TrackingEvent;
import com.hybris.yprofile.services.ProfileTrackingEventService;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.List;

/**
 * Service responsible for handling event tracking data and format.
 */
public class TrackingEventHandler {

    private static final Logger LOG = Logger.getLogger(TrackingEventHandler.class);
    private final CsvMapper mapper;
    private final CsvSchema schema;

    private final Converter<TrackingEventCsvData, TrackingEvent> yProfileTrackingEventConverter;

    private ProfileTrackingEventService profileTrackingEventService;

    public TrackingEventHandler(final CsvMapper mapper, final List<String> trackingEventCsvOrderedFields,
                                final Converter<TrackingEventCsvData, TrackingEvent> yProfileTrackingEventConverter)
    {
        this.mapper = mapper;
        final CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder();

        for (final String trackingEventCsvOrderedField : trackingEventCsvOrderedFields)
        {
            csvSchemaBuilder.addColumn(trackingEventCsvOrderedField);
        }
        this.schema = csvSchemaBuilder.build();
        this.yProfileTrackingEventConverter = yProfileTrackingEventConverter;
    }

    /**
     * Get the string message and convert it to the correct event object.
     * @param s message
     * @return converted message
     */
    public TrackingEventCsvData receiveEvent(final String s){
        if (LOG.isDebugEnabled()) {
            LOG.debug("TrackingEvent received");
        }
        TrackingEventCsvData trackingEventCsvData = null;
        try {
            trackingEventCsvData = mapper.reader(TrackingEventCsvData.class).with(schema).readValue(s);
        } catch (IOException e) {
            LOG.error("Error receiving event", e);
        }
        return trackingEventCsvData;
    }

    /**
     * Creates event tracking dto and sends it to Profile.
     * @param trackingEventCsvData message
     */
    public void handleEvent(final TrackingEventCsvData trackingEventCsvData){
        Registry.activateMasterTenant();
        if (LOG.isDebugEnabled()){
            LOG.debug("TrackingEvent handled");
        }
        TrackingEvent trackingEventDTO = yProfileTrackingEventConverter.convert(trackingEventCsvData);

        profileTrackingEventService.sendTrackingEvent(trackingEventCsvData.getConsentReference(),
                trackingEventDTO);
    }

    public ProfileTrackingEventService getProfileTrackingEventService() {
        return profileTrackingEventService;
    }

    @Required
    public void setProfileTrackingEventService(ProfileTrackingEventService profileTrackingEventService) {
        this.profileTrackingEventService = profileTrackingEventService;
    }
}
