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
package com.hybris.yprofile.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hybris.yprofile.clients.TrackingEventClient;
import com.hybris.yprofile.clients.TrackingResponse;
import com.hybris.yprofile.dto.TrackingEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Implementation for {@link ProfileTrackingEventService}. Communication service to send tracking events to Profile
 */
public class DefaultProfileTrackingEventService implements ProfileTrackingEventService {
    private static final Logger LOG = Logger.getLogger(DefaultProfileTrackingEventService.class);
    private static final String NULL = "null";

    private ProfileCharonFactory charonFactory;

    private ProfileConfigurationService profileConfigurationService;

    @Override
    public void sendTrackingEvent(final String consentReference, final TrackingEvent trackingEventDTO) {

        if (getProfileConfigurationService().isYaaSConfigurationPresent() &&
                isValidConsentReference(consentReference)) {

            getClient().sendTrackingEvent(getProfileConfigurationService().getYaaSTenant(),
                    consentReference,
                    trackingEventDTO.getUserAgent(),
                    trackingEventDTO.getAccept(),
                    trackingEventDTO.getAcceptLanguage(),
                    trackingEventDTO.getReferer(),
                    trackingEventDTO)
                    .subscribe(response -> logSuccess(response, trackingEventDTO),
                            error -> logError(trackingEventDTO, error));

        } else {
            LOG.warn("YaaS Configuration not found or Invalid consent reference");
        }
    }

    private boolean isValidConsentReference(String consentReference) {
        return StringUtils.isNotBlank(consentReference) && !NULL.equals(consentReference);
    }

    private void logSuccess(final TrackingResponse result, final TrackingEvent trackingEventDTO){
        if (LOG.isDebugEnabled()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            String event = trackingEventDTO.getAction_name();
            try {
                event = mapper.writeValueAsString(trackingEventDTO);
            } catch (JsonProcessingException e) {
                /*ignore*/
            }

            LOG.debug("Event " + event + " sent to yaas with response " + result);
        }
    }

    private void logError(final TrackingEvent trackingEventDTO, final Throwable error){
        LOG.error("Event tracking failed with dto" + trackingEventDTO.toString(), error);
    }

    private TrackingEventClient getClient() {
        return getCharonFactory().client(getProfileConfigurationService().getApplicationId(), TrackingEventClient.class);
    }

    public ProfileCharonFactory getCharonFactory() {
        return charonFactory;
    }

    @Required
    public void setCharonFactory(ProfileCharonFactory charonFactory) {
        this.charonFactory = charonFactory;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
