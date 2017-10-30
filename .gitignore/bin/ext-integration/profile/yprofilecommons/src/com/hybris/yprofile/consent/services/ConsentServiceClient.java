/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */
package com.hybris.yprofile.consent.services;

import com.hybris.charon.annotations.Control;
import com.hybris.charon.annotations.Http;
import com.hybris.charon.annotations.OAuth;
import rx.Observable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@OAuth
@Http(value = "profile")
public interface ConsentServiceClient {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("${profile-consent}")
    @Control(retries = "${retries:3}", retriesInterval = "${retriesInterval:2000}", timeout = "${timeout:4000}")
    Observable<ConsentResponse> getConsentReference(
            @PathParam("tenant") String tenant,
            @HeaderParam("hybris-user") String userId);
}
