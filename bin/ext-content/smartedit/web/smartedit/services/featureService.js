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
angular.module('featureServiceModule', ['functionsModule', 'featureInterfaceModule', 'gatewayProxyModule', 'decoratorServiceModule', 'contextualMenuServiceModule'])

.factory('featureService', function($log, extend, hitch, copy, FeatureServiceInterface, gatewayProxy, decoratorService, contextualMenuService) {

    var FeatureService = function() {
        this.gatewayId = "featureService";
        gatewayProxy.initForService(this);
    };

    FeatureService = extend(FeatureServiceInterface, FeatureService);

    FeatureService.prototype._remoteEnablingFromInner = function(key) {
        if (this.featuresToAlias && this.featuresToAlias[key]) {
            this.featuresToAlias[key].enablingCallback();
            return;
        } else {
            $log.warn("could not enable feature named " + key + ", it was not found in the iframe");
        }
    };

    FeatureService.prototype._remoteDisablingFromInner = function(key) {
        if (this.featuresToAlias && this.featuresToAlias[key]) {
            this.featuresToAlias[key].disablingCallback();
            return;
        } else {
            $log.warn("could not disable feature named " + key + ", it was not found in the iframe");
        }
    };

    FeatureService.prototype.addDecorator = function(configuration) {
        var prevEnablingCallback = configuration.enablingCallback;
        var prevDisablingCallback = configuration.disablingCallback;

        configuration.enablingCallback = hitch(decoratorService, function() {
            this.enable(configuration.key);

            if (prevEnablingCallback) {
                prevEnablingCallback();
            }
        });

        configuration.disablingCallback = hitch(decoratorService, function() {
            this.disable(configuration.key);

            if (prevDisablingCallback) {
                prevDisablingCallback();
            }
        });

        this.register(configuration);
    };



    FeatureService.prototype.addContextualMenuButton = function(item) {

        var clone = copy(item);

        delete item.nameI18nKey;
        delete item.descriptionI18nKey;
        delete item.regexpKeys;

        var configuration = {
            key: clone.key,
            nameI18nKey: clone.nameI18nKey,
            descriptionI18nKey: clone.descriptionI18nKey,
            enablingCallback: function() {
                var mapping = {};
                clone.regexpKeys.forEach(function(regexpKey) {
                    mapping[regexpKey] = [item];
                });
                contextualMenuService.addItems(mapping);
            },
            disablingCallback: function() {
                contextualMenuService.removeItemByKey(clone.key);
            }
        };

        this.register(configuration);
    };

    return new FeatureService();
});
