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

import de.hybris.platform.yaasconfiguration.model.YaasApplicationModel;
import de.hybris.platform.yaasconfiguration.service.YaasConfigurationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.Optional;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang.StringUtils.isBlank;

public class DefaultProfileConfigurationService implements ProfileConfigurationService {

    private final String appId;
    private YaasApplicationModel yaasConfig;
    private YaasConfigurationService yaasConfigurationService;


    public DefaultProfileConfigurationService(String appId) {
        this.appId = appId;
    }

    @Override
    public boolean isYaaSConfigurationPresent(){
        return (getYaasConfig() != null);
    }

    @Override
    public String getYaaSTenant(){

        if (isYaaSConfigurationPresent() && getYaasConfig().getYaasProject() != null) {
            return getYaasConfig().getYaasProject().getIdentifier();
        }

        return StringUtils.EMPTY;
    }

    @Override
    public String getBaseSiteId(){

        if (isYaaSConfigurationPresent() && getYaasConfig().getYaasProject() != null) {
            return getYaasConfig().getYaasProject().getBaseSite().getUid();
        }

        return StringUtils.EMPTY;
    }

    @Override
    public String getApplicationId(){
        if (isBlank(appId) && isYaaSConfigurationPresent()) {
            return getYaasConfig().getIdentifier();
        }
        return appId;
    }

    protected YaasApplicationModel getYaasConfig() {
        if (yaasConfig == null){
            Optional<YaasApplicationModel> model = getYaasApplicationModel();

            if (model.isPresent()) {
                yaasConfig = model.get();
            }
        }

        return yaasConfig;
    }

    protected Optional<YaasApplicationModel> getYaasApplicationModel()
    {
        if (isBlank(appId))
        {
            return getYaasConfigurationService().takeFirstModel();
        }
        return ofNullable(getYaasConfigurationService().getYaasApplicationForId(appId));
    }

    public YaasConfigurationService getYaasConfigurationService() {
        return yaasConfigurationService;
    }

    @Required
    public void setYaasConfigurationService(YaasConfigurationService yaasConfigurationService) {
        this.yaasConfigurationService = yaasConfigurationService;
    }
}
