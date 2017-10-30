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
/**
 * @ngdoc overview
 * @name iframeClickDetectionServiceModule
 * @description
 * # The iframeClickDetectionServiceModule
 *
 * The iframe Click Detection module provides the SmartEdit Container with the functionality to listen for
 * click events, specifically mousedown events, within the SmartEdit application.
 *
 * The module requires the Gateway Proxy module to listen for click events.
 *
 */
angular.module('iframeClickDetectionServiceModule', ['gatewayProxyModule'])

/**
 * @ngdoc service
 * @name iframeClickDetectionServiceModule.service:iframeClickDetectionService
 *
 * @description
 * The iframe Click Detection service uses the  {@link gatewayProxyModule.gatewayProxy gatewayProxy} service to listen
 * for click events, specifically mousedown events, from the SmartEdit application. This service also provides the
 * functionality to register callbacks to click events.
 *
 */
.factory('iframeClickDetectionService', function(gatewayProxy) {
    function IframeClickDetectionService() {
        this.gatewayId = 'iframeClick';
        this.callbacks = {};
        gatewayProxy.initForService(this, ["onIframeClick"]);
    }

    /**
     * @ngdoc method
     * @name iframeClickDetectionServiceModule.service:iframeClickDetectionService#registerCallback
     * @methodOf iframeClickDetectionServiceModule.service:iframeClickDetectionService
     *
     * @description
     * Registers a callback to be triggered when a click occurs in the iframe.
     *
     * @param {String} id The ID to register the given callback
     * @param {Function} callback The callback to be triggered.
     */
    IframeClickDetectionService.prototype.registerCallback = function(id, callback) {
        this.callbacks[id] = callback;
    };

    /**
     * @ngdoc method
     * @name iframeClickDetectionServiceModule.service:iframeClickDetectionService#removeCallback
     * @methodOf iframeClickDetectionServiceModule.service:iframeClickDetectionService
     *
     * @description
     * Removes a callback registered to the given ID
     *
     * @param {String} id The ID of the callback to remove
     */
    IframeClickDetectionService.prototype.removeCallback = function(id) {
        if (this.callbacks[id]) {
            delete this.callbacks[id];
        }
    };

    /**
     * @ngdoc method
     * @name iframeClickDetectionServiceModule.service:iframeClickDetectionService#onIframeClick
     * @methodOf iframeClickDetectionServiceModule.service:iframeClickDetectionService
     *
     * @description
     * Triggers all callbacks currently registered to the service. This function is registered as a listener through
     * the  {@link gatewayProxyModule.gatewayProxy gatewayProxy}.
     */
    IframeClickDetectionService.prototype.onIframeClick = function() {
        for (var ref in this.callbacks) {
            this.callbacks[ref]();
        }
    };

    return new IframeClickDetectionService();
});
