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
package com.hybris.yprofile.listeners;

import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.orderprocessing.events.ConsignmentProcessingEvent;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Event listener for consignment processing event.
 */
public class ConsignmentEventListener extends AbstractSiteEventListener<ConsignmentProcessingEvent> {

    private static final Logger LOG = Logger.getLogger(ConsignmentProcessingEvent.class);
    private ProfileTransactionService profileTransactionService;

    @Override
    protected void onSiteEvent(ConsignmentProcessingEvent event) {

        ConsignmentModel consignment = event.getProcess().getConsignment();

        if (consignment == null) {
            LOG.warn("Unable to send consignment to yProfile");
        } else {

            try {
                getProfileTransactionService().sendConsignmentEvent(consignment);
            } catch (Exception e) {
                LOG.error("Unable to send consignment to yProfile", e);
            }
        }

    }

    @Override
    protected boolean shouldHandleEvent(ConsignmentProcessingEvent event) {
        final AbstractOrderModel order = event.getProcess().getConsignment().getOrder();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
        final BaseSiteModel site = order.getSite();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
        return SiteChannel.B2C.equals(site.getChannel());
    }

    public ProfileTransactionService getProfileTransactionService() {
        return profileTransactionService;
    }

    @Required
    public void setProfileTransactionService(ProfileTransactionService profileTransactionService) {
        this.profileTransactionService = profileTransactionService;
    }
}
