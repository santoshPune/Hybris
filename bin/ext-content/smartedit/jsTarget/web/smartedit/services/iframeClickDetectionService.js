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
 * The iframe Click Detection module provides the functionality to transmit a click event from anywhere within
 * the contents of the SmartEdit application iFrame to the SmartEdit container. Specifically, the module uses jQuery to
 * bind mousedown events on the iFrame document to a proxy function, triggering the event on the SmartEdit container.
 *
 * The iframe Click Detection module requires gatewayProxyModule to propagate click events.
 *
 */
angular.module('iframeClickDetectionServiceModule', ['gatewayProxyModule'])

/**
 * @ngdoc service
 * @name iframeClickDetectionServiceModule.service:iframeClickDetectionService
 *
 * @description
 * The iframe Click Detection service leverages the {@link gatewayProxyModule.gatewayProxy gatewayProxy} service to
 * propagate click events, specifically mousedown events, to the SmartEdit container.
 *
 */
.factory('iframeClickDetectionService', ['gatewayProxy', function(gatewayProxy) {
        function IframeClickDetectionService() {
            this.gatewayId = 'iframeClick';
            gatewayProxy.initForService(this, ["onIframeClick"]);
        }

        /**
         * @ngdoc method
         * @name iframeClickDetectionServiceModule.service:iframeClickDetectionService#onIframeClick
         * @methodOf iframeClickDetectionServiceModule.service:iframeClickDetectionService
         *
         * @description
         * The proxy function that delegates calls to the SmartEdit container.
         *
         * @returns {Promise} Promise that is resolved when the SmartEdit container completes the callback.
         */
        IframeClickDetectionService.prototype.onIframeClick = function() {};

        return new IframeClickDetectionService();
    }])
    .run(['iframeClickDetectionService', '$document', function(iframeClickDetectionService, $document) {
        $document.on('mousedown', function() {
            iframeClickDetectionService.onIframeClick();
        });
    }]);
