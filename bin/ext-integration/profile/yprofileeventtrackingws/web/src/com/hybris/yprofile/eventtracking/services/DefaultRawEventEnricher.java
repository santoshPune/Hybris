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
package com.hybris.yprofile.eventtracking.services;

import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import com.hybris.yprofile.consent.services.ConsentService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class DefaultRawEventEnricher implements RawEventEnricher
{
    private static final Logger LOG = Logger.getLogger(DefaultRawEventEnricher.class);

    private static final String ENRICHMENT_SPEC_TEMPLATE = "[{\"operation\":\"default\", " +
                                                             "\"spec\": { \"session_id\": \"%s\", " +
                                                                        "\"timestamp\": \"%s\", " +
                                                                        "\"user_id\": \"%s\", " +
                                                                        "\"user_email\": \"%s\", " +
                                                                        "\"consent_reference\": \"%s\", " +
                                                                        "\"user_agent\": \"%s\", " +
                                                                        "\"accept\": \"%s\", " +
            "\"accept_language\": \"%s\", " +
                                                                        "\"referer\": \"%s\" " +
                                                                        "} " +
                                                            "}]";

    private UserService userService;

    private ConsentService consentService;

    /**
     * @see com.hybris.yprofile.eventtracking.services.RawEventEnricher#enrich(java.lang.String,
     *      javax.servlet.http.HttpServletRequest)
     */
    @Override
    public String enrich(final String json, final HttpServletRequest request)
    {
        final HttpSession session = request.getSession();
        final String sessionId = session.getId();
        final String timestamp = Integer.toString(Math.round(System.currentTimeMillis() / 1000)); // seconds since Unix epoch

        Map<String, Object> sourceData = jsonToMap(json);

        String userId = null;
        String userEmail = null;
        final UserModel user = getUserService().getCurrentUser();
        if (user != null && !getUserService().isAnonymousUser(user) && CustomerModel.class.isAssignableFrom(user.getClass()))
        {
            userId = user.getUid();
            userEmail = ((CustomerModel) user).getContactEmail();
        }

        userId = StringUtils.trimToEmpty(userId);
        userEmail = StringUtils.trimToEmpty(userEmail);

        String consentReference = getConsentService().getConsentReferenceFromCookie(request);

        //propagate the headers
        String userAgent = StringUtils.trimToEmpty(getUserAgent(request));
        String accept = StringUtils.trimToEmpty(getAccept(request));
        String acceptLanguage = StringUtils.trimToEmpty(getAcceptLanguage(request));

        String referer = StringUtils.trimToEmpty(getReferer(request));

        final Chainr chainr = Chainr.fromSpec(JsonUtils.jsonToList(String.format(ENRICHMENT_SPEC_TEMPLATE, sessionId, timestamp,
                userId, userEmail, consentReference, userAgent, accept, acceptLanguage, referer)));

        return JsonUtils.toJsonString(chainr.transform(sourceData));
    }

    private Map<String, Object> jsonToMap(String json) {
        Map<String, Object> jsonObjectMap;
        try
        {
            jsonObjectMap = JsonUtils.javason(json);
            return jsonObjectMap;
        }
        catch (final IOException e)
        {
            LOG.error("Error mapping json object to map", e);
        }
        return null;
    }

    private String getUserAgent(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.USER_AGENT);
    }

    private String getAccept(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.ACCEPT);
    }

    private String getAcceptLanguage(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
    }

    private String getReferer(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.REFERER);
    }

    public UserService getUserService() {
        return userService;
    }

    @Required
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ConsentService getConsentService() {
        return consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }
}
