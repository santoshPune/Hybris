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
 */
package de.hybris.platform.atddrunner.service;

import org.springframework.beans.factory.annotation.Required;

/**
 * Configuration of a servlet.
 */
public class ServletDefinition
{

    private String contextPath;
    private String servletPath;
    private String heartbeat;

    /**
     *
     * @return servlet end point URI
     */
    public String getContextPath()
    {
        return contextPath;
    }

    /**
     *
     * @param contextPath servlet end point URI
     */
    public void setContextPath(final String contextPath)
    {
        this.contextPath = contextPath;
    }

    /**
     *
     * @return path to servlet file
     */
    @Required
    public String getServletPath()
    {
        return servletPath;
    }

    /**
     * Path to servlet file. Can be absolute or relative to directory that contains this class
     *
     * @param servletPath path to servlet file
     */
    public void setServletPath(final String servletPath)
    {
        this.servletPath = servletPath;
    }

    /**
     *
     * @return heartbeat url
     */
    public String getHeartbeat()
    {
        return heartbeat;
    }

    /**
     * Heartbeat allows to let know that the servlet is still alive.
     *
     * @param heartbeat heartbeat url
     */
    public void setHeartbeat(final String heartbeat)
    {
        this.heartbeat = heartbeat;
    }
}
