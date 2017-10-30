package com.hybris.yprofile.listeners;

import com.hybris.yprofile.services.ProfileTransactionService;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.enums.SiteChannel;
import de.hybris.platform.commerceservices.event.AbstractSiteEventListener;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.events.SubmitOrderEvent;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Event listener for order submit event.
 */
public class NewOrderEventListener extends AbstractSiteEventListener<SubmitOrderEvent>
{
    private static final Logger LOG = Logger.getLogger(NewOrderEventListener.class);
    private ProfileTransactionService profileTransactionService;

    @Override
    protected void onSiteEvent(final SubmitOrderEvent event) {
        final OrderModel order = event.getOrder();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);

        if (order == null) {
            LOG.warn("Unable to send order to yProfile");
        } else {
            try {
                getProfileTransactionService().sendSubmitOrderEvent(order);
            } catch (Exception e) {
                LOG.error("Unable to send order to yProfile", e);
            }
        }
    }

    @Override
    protected boolean shouldHandleEvent(final SubmitOrderEvent event) {
        final OrderModel order = event.getOrder();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order", order);
        final BaseSiteModel site = order.getSite();
        ServicesUtil.validateParameterNotNullStandardMessage("event.order.site", site);
        return SiteChannel.B2C.equals(site.getChannel());
    }


    protected ProfileTransactionService getProfileTransactionService() {
        return profileTransactionService;
    }

    @Required
    public void setProfileTransactionService(final ProfileTransactionService profileTransactionService) {
        this.profileTransactionService = profileTransactionService;
    }
}
