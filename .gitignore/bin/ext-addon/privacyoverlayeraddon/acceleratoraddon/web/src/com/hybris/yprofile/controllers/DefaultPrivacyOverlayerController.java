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
package com.hybris.yprofile.controllers;

import com.hybris.yprofile.consent.services.ConsentService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller("defaultPrivacyOverlayerController")
@RequestMapping(value = "/accept")
public class DefaultPrivacyOverlayerController {

    private ConsentService consentService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> generateConsentReference(final HttpServletRequest request, final HttpServletResponse response) throws IOException
    {
        getConsentService().generateConsentReference(request, response);

        return new ResponseEntity<String>(new HttpHeaders(), HttpStatus.OK);
    }

    public ConsentService getConsentService() {
        return consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }
}
