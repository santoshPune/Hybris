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
package com.hybris.yprofile.clients;

import com.hybris.charon.annotations.Control;
import com.hybris.charon.annotations.Header;
import com.hybris.charon.annotations.Http;
import com.hybris.charon.annotations.OAuth;
import com.hybris.yprofile.dto.TrackingEvent;
import org.springframework.http.HttpHeaders;
import rx.Observable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@OAuth
@Http(value = "profile")
public interface TrackingEventClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("${profile-edge}")
    @Header(name = "event-type", val = "piwik")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    Observable<TrackingResponse> sendTrackingEvent(
            @HeaderParam("hybris-tenant") String tenant,
            @HeaderParam("consent-reference") String consentReferenceId,
            @HeaderParam(HttpHeaders.USER_AGENT) String userAgent,
            @HeaderParam(HttpHeaders.ACCEPT) String accept,
            @HeaderParam(HttpHeaders.ACCEPT_LANGUAGE) String acceptLanguage,
            @HeaderParam(HttpHeaders.REFERER) String referer,
            TrackingEvent trackingEvent);


}
