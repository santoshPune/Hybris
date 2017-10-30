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
package com.hybris.yprofile.eventtracking.rest;

import com.hybris.yprofile.eventtracking.services.RawEventEnricher;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


@Component
@Path("/events")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EventTrackingEndpoint {

    private static final Logger LOG = Logger.getLogger(EventTrackingEndpoint.class);

    private static final String EVENTS_ENDPOINT_ENABLED_CONF_PROPERTY = "yprofileeventtrackingws.events_endpoint.enabled";

    private QueueChannel rawTrackingEventsChannel;

    private RawEventEnricher rawEventEnricher;

    private ConfigurationService configurationService;

    @POST
    public Response trackEvent(@Context HttpServletRequest request, JSONObject event) throws IOException
    {
        if (isEventEndpointEnabled())
        {
            final String body = event.toJSONString();
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Events endpoint handling track event request with body: \n" + body);
            }

            final String payload = rawEventEnricher.enrich(body, request);

            forwardForProcessing(payload);
        }
        else
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Events endpoint is disabled. Ignoring request.");
            }
        }

        return Response.ok().build();
    }

    private boolean isEventEndpointEnabled() {
        return getConfigurationService().getConfiguration().getBoolean(EVENTS_ENDPOINT_ENABLED_CONF_PROPERTY, true);
    }

    protected void forwardForProcessing(final String payload)
    {
        final Message<String> message = new GenericMessage<String>(payload);
        rawTrackingEventsChannel.send(message);
    }


    public QueueChannel getRawTrackingEventsChannel() {
        return rawTrackingEventsChannel;
    }

    @Required
    public void setRawTrackingEventsChannel(QueueChannel rawTrackingEventsChannel) {
        this.rawTrackingEventsChannel = rawTrackingEventsChannel;
    }

    public RawEventEnricher getRawEventEnricher() {
        return rawEventEnricher;
    }

    @Required
    public void setRawEventEnricher(RawEventEnricher rawEventEnricher) {
        this.rawEventEnricher = rawEventEnricher;
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    @Required
    public void setConfigurationService(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }
}
