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
import com.hybris.charon.annotations.Http;
import com.hybris.charon.annotations.OAuth;
import com.hybris.yprofile.dto.Order;
import com.hybris.yprofile.dto.User;
import rx.Observable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * REST client to send orders and new users to yaas.
 */
@OAuth
@Http(value = "profile")
public interface ProfileClient {

    /**
     * Send orders to yProfile.
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("${profile-ecp-ctx-adapter}")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    Observable<ProfileResponse> sendOrderEvent(
            @PathParam("tenant") String tenant,
            @HeaderParam("consent-reference") String consentReferenceId,
            Order order);

    /**
     * Send users to yProfile.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("${profile-edge}")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    Observable<ProfileResponse> sendUserEvent(
            @HeaderParam("hybris-tenant") String tenant,
            @HeaderParam("event-type") String eventType,
            @HeaderParam("consent-reference") String consentReferenceId,
            User user);
}
