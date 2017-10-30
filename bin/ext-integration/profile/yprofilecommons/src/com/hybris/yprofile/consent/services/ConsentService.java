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
package com.hybris.yprofile.consent.services;

import rx.Observable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ConsentService interface. Service is responsible to generate and provide the consent reference.
 */
public interface ConsentService {

    /**
     * Executes the rest call to the consent service to get the consent reference for a given user
     *
     * @param userId the user id
     * @return the consent service response
     */
    Observable<ConsentResponse> generateConsentReferenceForUser(String userId);

    /**
     * Generates the consent reference for the user and stores it both in a cookie
     * and in the session
     *
     * @param request Http request
     * @param response Http response with the consent reference cookie
     */
    void generateConsentReference(final HttpServletRequest request, final HttpServletResponse response);

    /**
     * Generates the consent reference for the user and stores it both in a cookie
     * and in the session
     * @param request Http request
     * @param response Http response with the consent reference cookie
     * @param shouldGenerateConsentReference should generate consent reference
     */
    void generateConsentReference(final HttpServletRequest request, final HttpServletResponse response, final boolean shouldGenerateConsentReference);

    /**
     * Fetches the consent reference from session
     * @return consent reference
     */
    String getConsentReferenceFromSession();

    /**
     * Fetches the consent reference from cookie
     * @param request http request
     * @return consent reference
     */
    String getConsentReferenceFromCookie(final HttpServletRequest request);
}
