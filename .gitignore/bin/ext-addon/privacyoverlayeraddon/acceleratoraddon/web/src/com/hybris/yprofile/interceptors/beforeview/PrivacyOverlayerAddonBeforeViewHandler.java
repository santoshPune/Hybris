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
package com.hybris.yprofile.interceptors.beforeview;

import com.hybris.yprofile.services.ProfileConfigurationService;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.addonsupport.config.javascript.BeforeViewJsPropsHandlerAdaptee;
import de.hybris.platform.servicelayer.session.SessionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrivacyOverlayerAddonBeforeViewHandler extends BeforeViewJsPropsHandlerAdaptee {

    private static final String ACCEPT_ENDPOINT_URL = "privacyoverlayeraddon.accept.url";
    private static final String ACCEPT_ENDPOINT_HTTPS_URL = "privacyoverlayeraddon.accept.https.url";

    private static final String CONSENT_REFERENCE_SESSION_ATTR_KEY = "consent-reference";

    private SiteConfigService siteConfigService;
    private SessionService sessionService;

    private ProfileConfigurationService profileConfigurationService;
    /**
     * Sets values needed for the cookie bar in model object.
     *
     * @param request
     * @param response
     * @param model
     * @param viewName
     * @return
     */
    @Override
    public String beforeViewJsProps(HttpServletRequest request, HttpServletResponse response, ModelMap model, String viewName) {

        if (model != null)
        {
            model.addAttribute("ACCEPT_ENDPOINT_URL", getSiteConfigService().getProperty(ACCEPT_ENDPOINT_URL));
            model.addAttribute("ACCEPT_ENDPOINT_HTTPS_URL", getSiteConfigService().getProperty(ACCEPT_ENDPOINT_HTTPS_URL));

            model.addAttribute("TENANT", getTenant());
            model.addAttribute("CONSENT_REFERENCE_ID", getSessionService().getAttribute(CONSENT_REFERENCE_SESSION_ATTR_KEY));

        }

        return viewName;
    }

    private String getTenant() {
        if (getProfileConfigurationService().isYaaSConfigurationPresent()){
            return getProfileConfigurationService().getYaaSTenant();
        }
        return StringUtils.EMPTY;
    }

    public SiteConfigService getSiteConfigService() {
        return siteConfigService;
    }

    @Required
    public void setSiteConfigService(SiteConfigService siteConfigService) {
        this.siteConfigService = siteConfigService;
    }

    public SessionService getSessionService() {
        return sessionService;
    }

    @Required
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
