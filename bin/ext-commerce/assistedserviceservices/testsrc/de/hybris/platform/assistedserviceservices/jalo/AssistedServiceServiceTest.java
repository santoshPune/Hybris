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
package de.hybris.platform.assistedserviceservices.jalo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.assistedserviceservices.AssistedServiceService;
import de.hybris.platform.assistedserviceservices.impl.DefaultAssistedServiceService;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link DefaultAssistedServiceService}
 */
@IntegrationTest
public class AssistedServiceServiceTest extends ServicelayerTest
{
    @Resource
    private AssistedServiceService assistedServiceService;

    @Resource
    private BaseSiteService baseSiteService;

    @Resource
    private UserService userService;

    @Before
    public void setup() throws Exception
    {
        importCsv("/assistedserviceservices/test/cart_data.impex", "UTF-8");
        baseSiteService.setCurrentBaseSite("testSite", true);
    }

    @Test
    public void latestModifiedCartTest()
    {
        assertEquals("00000002", assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer")).getCode());
        assertEquals("00000003", assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer2")).getCode());
        assertNull(assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer3")));
    }
}