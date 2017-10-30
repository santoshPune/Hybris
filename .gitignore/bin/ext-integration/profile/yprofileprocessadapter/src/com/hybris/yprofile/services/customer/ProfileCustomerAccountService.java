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
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.impl.DefaultCustomerAccountService;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.session.impl.DefaultSession;
import de.hybris.platform.site.BaseSiteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ProfileCustomerAccountService extends DefaultCustomerAccountService {

    private static final Logger LOG = Logger.getLogger(ProfileCustomerAccountService.class);

    private ProfileTransactionService profileTransactionService;
    private BaseSiteService baseSiteService;
    private SessionService sessionService;
    private ConsentService consentService;

    @Override
    public void register(final CustomerModel customerModel, final String password) throws DuplicateUidException
    {
        super.register(customerModel, password);

        try {
            String consentReferenceId = getConsentService().getConsentReferenceFromSession();
            String sessionId = getSessionId();

            if (consentReferenceId != null) {
                getProfileTransactionService().sendUserRegistrationEvent(customerModel, consentReferenceId, sessionId, getSiteId());
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

    public SessionService getSessionService() {
        return sessionService;
    }

    @Required
    public void setSessionService(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    public ConsentService getConsentService() {
        return consentService;
    }

    @Required
    public void setConsentService(ConsentService consentService) {
        this.consentService = consentService;
    }
}
