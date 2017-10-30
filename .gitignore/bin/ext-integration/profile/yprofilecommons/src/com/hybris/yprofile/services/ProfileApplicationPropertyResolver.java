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

import de.hybris.platform.core.Registry;
import de.hybris.platform.yaasconfiguration.ApplicationPropertyResolver;
import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;

import java.util.Optional;

public class ProfileApplicationPropertyResolver extends ApplicationPropertyResolver{


    public ProfileApplicationPropertyResolver(YaasConfigurationService yaasConfig) {
        super(yaasConfig);
    }

    public ProfileApplicationPropertyResolver(YaasConfigurationService yaasConfig, String appId) {
        super(yaasConfig, appId);
    }

    @Override
    protected Optional<YaasApplicationModel> model() {
        /**
         * When the charon client tries to send a request for the first time,
         * it throws an IllegalStateException loading the model from the database
         * due to not active tenant
         */
        Registry.activateMasterTenant();
        return super.model();
    }
}
