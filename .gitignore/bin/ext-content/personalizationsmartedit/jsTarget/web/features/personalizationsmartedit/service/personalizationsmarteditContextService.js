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
angular.module('personalizationsmarteditContextServiceModule', ['personalizationsmarteditCommons'])
    .factory('personalizationsmarteditContextService', ['personalizationsmarteditUtils', 'personalizationsmarteditContextServiceReverseProxy', function(personalizationsmarteditUtils, personalizationsmarteditContextServiceReverseProxy) {

        var ContextService = {};
        var ContextServiceReverseProxy = new personalizationsmarteditContextServiceReverseProxy('PersonalizationCtxReverseGateway');

        var isContextualMenuEnabled = function() {
            var isEnabled = ContextService.personalizationEnabled;
            isEnabled = isEnabled && angular.isObject(ContextService.selectedCustomizations);
            isEnabled = isEnabled && angular.isObject(ContextService.selectedVariations);
            isEnabled = isEnabled && !angular.isArray(ContextService.selectedVariations);

            return isEnabled;
        };

        var isElementHighlighted = function(element) {
            var containerId = personalizationsmarteditUtils.getContainerIdForElement(element);
            var elementHighlighted = $.inArray(containerId, ContextService.selectedComponents) > -1;
            return elementHighlighted;
        };

        ContextService.personalizationEnabled = false;
        ContextService.selectedCustomizations = null;
        ContextService.selectedVariations = null;
        ContextService.selectedComponents = null;
        ContextService.seExperienceData = null;
        ContextService.seConfigurationData = null;
        ContextService.sePreviewData = null;
        ContextService.pageId = null;

        ContextService.isPersonalizationContextEnabled = function() {
            return ContextService.personalizationEnabled;
        };

        ContextService.setPersonalizationContextEnabled = function(persCtxEnabled) {
            ContextService.personalizationEnabled = persCtxEnabled;
        };

        ContextService.setSelectedComponents = function(newSelectedComponents) {
            ContextService.selectedComponents = newSelectedComponents;
        };

        ContextService.setSelectedVariations = function(newSelectedVariations) {
            ContextService.selectedVariations = newSelectedVariations;
        };

        ContextService.setSelectedCustomizations = function(newSelectedCustomizations) {
            ContextService.selectedCustomizations = newSelectedCustomizations;
        };

        ContextService.isContextualMenuAddItemEnabled = function(element) {
            return isContextualMenuEnabled() && (!isElementHighlighted(element));
        };

        ContextService.isContextualMenuEditItemEnabled = function(element) {
            return isContextualMenuEnabled() && isElementHighlighted(element);
        };

        ContextService.isContextualMenuDeleteItemEnabled = function(element) {
            return isContextualMenuEnabled() && isElementHighlighted(element);
        };

        ContextService.isContextualMenuInfoItemEnabled = function(element) {
            var isEnabled = ContextService.personalizationEnabled;
            isEnabled = isEnabled && !angular.isObject(ContextService.selectedVariations);
            isEnabled = isEnabled || angular.isArray(ContextService.selectedVariations);

            return isEnabled;
        };

        ContextService.applySynchronization = function() {
            ContextServiceReverseProxy.applySynchronization();
        };

        ContextService.getSeExperienceData = function() {
            return ContextService.seExperienceData;
        };

        ContextService.setSeExperienceData = function(newSeExperienceData) {
            ContextService.seExperienceData = newSeExperienceData;
        };

        ContextService.getSeConfigurationData = function() {
            return ContextService.seConfigurationData;
        };

        ContextService.setSeConfigurationData = function(newSeConfigurationData) {
            ContextService.seConfigurationData = newSeConfigurationData;
        };

        ContextService.getSePreviewData = function() {
            return ContextService.sePreviewData;
        };

        ContextService.setSePreviewData = function(newSePreviewData) {
            ContextService.sePreviewData = newSePreviewData;
        };

        ContextService.setPageId = function(newPageId) {
            ContextService.pageId = newPageId;
            ContextServiceReverseProxy.setPageId(newPageId);
        };

        ContextService.getPageId = function() {
            return ContextService.pageId;
        };

        return ContextService;
    }])
    .factory('personalizationsmarteditContextServiceProxy', ['gatewayProxy', 'personalizationsmarteditContextService', function(gatewayProxy, personalizationsmarteditContextService) {
        var proxy = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this);
        };
        proxy.prototype.setPersonalizationContextEnabled = function(persCtxEnabled) {
            personalizationsmarteditContextService.setPersonalizationContextEnabled(persCtxEnabled);
        };
        proxy.prototype.setSelectedComponents = function(newSelectedComponents) {
            personalizationsmarteditContextService.setSelectedComponents(newSelectedComponents);
        };
        proxy.prototype.setSelectedVariations = function(newSelectedVariations) {
            personalizationsmarteditContextService.setSelectedVariations(newSelectedVariations);
        };
        proxy.prototype.setSelectedCustomizations = function(newSelectedCustomizations) {
            personalizationsmarteditContextService.setSelectedCustomizations(newSelectedCustomizations);
        };
        proxy.prototype.setSeExperienceData = function(newSeExperienceData) {
            personalizationsmarteditContextService.setSeExperienceData(newSeExperienceData);
        };
        proxy.prototype.setSeConfigurationData = function(newSeConfigurationData) {
            personalizationsmarteditContextService.setSeConfigurationData(newSeConfigurationData);
        };
        proxy.prototype.setSePreviewData = function(newSePreviewData) {
            personalizationsmarteditContextService.setSePreviewData(newSePreviewData);
        };

        return proxy;
    }])
    .factory('personalizationsmarteditContextServiceReverseProxy', ['gatewayProxy', function(gatewayProxy) {
        var reverseProxy = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this);
        };
        reverseProxy.prototype.applySynchronization = function() {};
        reverseProxy.prototype.setPageId = function(newPageId) {};

        return reverseProxy;
    }]);
