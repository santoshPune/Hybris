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
 */
angular.module('featureServiceModule', ['functionsModule', 'featureInterfaceModule', 'gatewayProxyModule', 'toolbarModule'])

.factory('featureService', function(extend, copy, hitch, FeatureServiceInterface, gatewayProxy, toolbarServiceFactory, $log) {

    /////////////////////////////////////
    // PerspectiveService Prototype
    /////////////////////////////////////

    var FeatureService = function() {
        this.features = [];
        this.gatewayId = "featureService";
        gatewayProxy.initForService(this, ['_registerAliases', 'addToolbarItem', 'register', 'enable', 'disable', '_remoteEnablingFromInner', '_remoteDisablingFromInner', 'addDecorator', 'addContextualMenuButton']);
    };

    FeatureService = extend(FeatureServiceInterface, FeatureService);

    FeatureService.prototype._registerAliases = function(configuration) {
        var feature = this.features.filter(function(feature) {
            return feature.key === configuration.key;
        })[0];
        if (!feature) {
            this.features.push({
                id: btoa(configuration.key),
                key: configuration.key,
                nameI18nKey: configuration.nameI18nKey,
                descriptionI18nKey: configuration.descriptionI18nKey
            });
        }
    };

    FeatureService.prototype.getFeatureKeys = function(configuration) {
        return this.features.map(function(feature) {
            return feature.key;
        });
    };

    FeatureService.prototype.addToolbarItem = function(item) {

        var clone = copy(item);
        var toolbar = toolbarServiceFactory.getToolbarService(clone.toolbarId);

        delete item.nameI18nKey;
        delete item.descriptionI18nKey;

        var configuration = {
            key: clone.key,
            nameI18nKey: clone.nameI18nKey,
            descriptionI18nKey: clone.descriptionI18nKey,
            enablingCallback: hitch(toolbar, function() {
                this.addItems([item]);
            }),
            disablingCallback: hitch(toolbar, function() {
                this.removeItemByKey(clone.key);
            }),
        };

        this.register(configuration);
    };

    return new FeatureService();

});
