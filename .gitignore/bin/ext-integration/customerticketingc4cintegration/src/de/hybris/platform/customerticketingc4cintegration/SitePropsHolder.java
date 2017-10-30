/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 *
 *
 */
package de.hybris.platform.customerticketingc4cintegration;

import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import de.hybris.platform.site.BaseSiteService;

/**
 * Hold some dynamic properties.
 */
public class SitePropsHolder
{
    private Logger logger = Logger.getLogger(SitePropsHolder.class);

    @Autowired
    private BaseSiteService baseSiteService;

    private String siteId;

    /**
     * @return current site id property for c4c-requests.
     */
    public String getSiteId()
    {
        logger.info(baseSiteService.getCurrentBaseSite().getUid());
        logger.info(Config.getParameter(String.format("customerticketingc4cintegration.siteId.%s", baseSiteService.getCurrentBaseSite().getUid())));
        if (StringUtils.isEmpty(siteId))
            siteId = Config.getParameter(String.format("customerticketingc4cintegration.siteId.%s", baseSiteService.getCurrentBaseSite().getUid()));
        return siteId;
    }

    /**
     * @return boolean
     */
    public boolean isB2C()
    {
        String type = Config.getParameter(String.format("customerticketingc4cintegration.siteId.%s.type", baseSiteService.getCurrentBaseSite().getUid()));
        logger.info(type);
        return "b2c".equalsIgnoreCase(type);
    }
}
