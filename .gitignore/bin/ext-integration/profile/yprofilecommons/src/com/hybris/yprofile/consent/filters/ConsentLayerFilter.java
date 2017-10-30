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
package com.hybris.yprofile.consent.filters;

import com.hybris.yprofile.consent.services.ConsentService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Generates the consent reference for the user and stores it in a cookie and in session
 */
public class ConsentLayerFilter extends OncePerRequestFilter
{
    private ConsentService consentService;

    private boolean enabled;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        getConsentService().generateConsentReference(httpServletRequest, httpServletResponse, isEnabled());

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public ConsentService getConsentService() {
        return consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Required
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
