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
package com.hybris.yprofile.services;

import com.hybris.charon.Charon;
import com.hybris.charon.CharonBuilder;
import com.hybris.charon.conf.PropertyResolver;
import de.hybris.platform.yaasconfiguration.CharonFactory;
import org.springframework.beans.factory.annotation.Required;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.hybris.charon.utils.CharonUtils.join;


public class ProfileCharonFactory extends CharonFactory {

    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    private PropertyResolver resolver;

    /**
     * returns a cached instance of http client. the clientType must be an interface which represents the charon client.
     * the related configuration will lookup to YassConfigurationModel the only properties that are loaded from the model
     * are clientId and clientSecret
     *
     * @param appId
     * @param clientType
     * @param builder
     *           client builder modifier
     * @param <T>
     * @return
     */
    @Override
    public <T> T client(final String appId, final Class<T> clientType, final Function<CharonBuilder<T>, T> builder)
    {
        checkArgument(appId != null, "appId must not be null");
        checkArgument(clientType != null, "clientType must not be null");
        checkArgument(builder != null, "builder must not be null");

        return (T) cache.computeIfAbsent(join(appId, clientType.getName()),
                k -> builder.apply(Charon.from(clientType).config(resolver)));
    }

    /**
     * remove all clients from the cache
     */
    @Override
    public void clearCache()
    {
        cache.clear();
    }

    @Required
    public void setResolver(final PropertyResolver resolver)
    {
        this.resolver = resolver;
    }

}
