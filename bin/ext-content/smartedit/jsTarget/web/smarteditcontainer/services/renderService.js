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
(function() {
    angular.module('renderServiceModule', ['gatewayProxyModule', 'renderServiceInterfaceModule'])
        .factory('renderService', ['extend', 'gatewayProxy', 'RenderServiceInterface', function createRenderService(extend, gatewayProxy, RenderServiceInterface) {
            var RENDERER_CHANNEL_ID = "Renderer";

            var RenderService = function(gatewayId) {
                this.gatewayId = gatewayId;
                gatewayProxy.initForService(this, ["renderSlots", "renderComponent", "renderRemoval", "toggleOverlay", "refreshOverlayDimensions", "renderPage"]);
            };

            RenderService = extend(RenderServiceInterface, RenderService);

            // Methods are delegated to the SmartEdit implementation of the service.

            return new RenderService(RENDERER_CHANNEL_ID);
        }]);

})();
