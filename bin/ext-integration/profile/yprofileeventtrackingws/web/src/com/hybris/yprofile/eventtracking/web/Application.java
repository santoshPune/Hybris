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
package com.hybris.yprofile.eventtracking.web;

import com.hybris.yprofile.eventtracking.rest.EventTrackingEndpoint;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Configuration for Jersey application.
 */
public class Application extends ResourceConfig
{
    /**
     * Creates new configuration with registered components.
     */
    public Application()
    {
        register(EventTrackingEndpoint.class);
        register(RuntimeExceptionMapper.class);
    }
}
