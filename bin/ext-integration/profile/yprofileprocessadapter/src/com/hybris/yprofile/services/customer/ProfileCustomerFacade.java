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
package com.hybris.yprofile.services.customer;

import com.hybris.yprofile.consent.services.ConsentService;
import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.platform.commercefacades.customer.impl.DefaultCustomerFacade;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.impl.DefaultSession;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ProfileCustomerFacade extends DefaultCustomerFacade {

    private static final Logger LOG = Logger.getLogger(ProfileCustomerFacade.class);

    private ProfileTransactionService profileTransactionService;
    private BaseSiteService baseSiteService;
    private ConsentService consentService;


    @Override
    public void loginSuccess(){

        super.loginSuccess();

        UserModel currentUser = getUserService().getCurrentUser();

        try {
            String consentReferenceId = getConsentService().getConsentReferenceFromSession();
            String sessionId = getSessionId();

            if (consentReferenceId != null) {
                getProfileTransactionService().sendLoginEvent(currentUser, consentReferenceId, sessionId, getSiteId());
            }
        } catch (Exception e){
            LOG.error("Error sending login event to yaas", e);
        }
    }

    private String getSessionId() {

        Session currentSession = getSessionService().getCurrentSession();

        if (currentSession instanceof DefaultSession) {
            return ((DefaultSession) currentSession).getJaloSession().getHttpSessionId();
        }

        return currentSession.getSessionId();
    }

    private String getSiteId() {
        return getBaseSiteService().getCurrentBaseSite().getUid();
    }

    public ProfileTransactionService getProfileTransactionService() {
        return profileTransactionService;
    }

    @Required
    public void setProfileTransactionService(ProfileTransactionService profileTransactionService) {
        this.profileTransactionService = profileTransactionService;
    }

    public BaseSiteService getBaseSiteService() {
        return baseSiteService;
    }

    @Required
    public void setBaseSiteService(BaseSiteService baseSiteService) {
        this.baseSiteService = baseSiteService;
    }

    public ConsentService getConsentService() {
        return consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }
}
