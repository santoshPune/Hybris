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
package com.hybris.yprofile.consent.cookie;

import com.hybris.yprofile.services.ProfileConfigurationService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.site.BaseSiteService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ConsentReferenceCookieGenerator extends EnhancedCookieGenerator
{

    private BaseSiteService baseSiteService;
    private ProfileConfigurationService profileConfigurationService;

    @Override
    public String getCookieName()
    {
        StringBuilder cookieName = new StringBuilder();
        cookieName.append(getSiteId());
        cookieName.append("-consentReference");

        return StringUtils.deleteWhitespace(cookieName.toString());
    }

    private String getSiteId(){

        BaseSiteModel currentBaseSite = getBaseSiteService().getCurrentBaseSite();

        if (currentBaseSite != null){
            return currentBaseSite.getUid();
        }

        return getProfileConfigurationService().getBaseSiteId();
    }


    protected BaseSiteService getBaseSiteService()
    {
        return baseSiteService;
    }

    @Required
    public void setBaseSiteService(final BaseSiteService baseSiteService)
    {
        this.baseSiteService = baseSiteService;
    }

    public ProfileConfigurationService getProfileConfigurationService() {
        return profileConfigurationService;
    }

    @Required
    public void setProfileConfigurationService(ProfileConfigurationService profileConfigurationService) {
        this.profileConfigurationService = profileConfigurationService;
    }
}
