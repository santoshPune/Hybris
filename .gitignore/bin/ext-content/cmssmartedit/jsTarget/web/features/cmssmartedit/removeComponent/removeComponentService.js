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
angular.module('removeComponentServiceModule', ['restServiceFactoryModule', 'renderServiceModule', 'gatewayProxyModule', 'removeComponentServiceInterfaceModule', 'experienceInterceptorModule', 'functionsModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name removeComponentService.removeComponentService
     *
     * @description
     * Service to remove a component from a slot
     */
    .factory('removeComponentService', ['restServiceFactory', 'renderService', 'extend', 'gatewayProxy', '$q', '$log', 'RemoveComponentServiceInterface', 'experienceInterceptor', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, renderService, extend, gatewayProxy, $q, $log, RemoveComponentServiceInterface, experienceInterceptor, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {
        var REMOVE_COMPONENT_CHANNEL_ID = "RemoveComponent";

        var RemoveComponentService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ["removeComponent"]);
        };

        RemoveComponentService = extend(RemoveComponentServiceInterface, RemoveComponentService);

        var restServiceForRemoveComponent = restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI + '?slotId=:slotId&componentId=:componentId', 'componentId');

        RemoveComponentService.prototype.removeComponent = function(configuration) {

            return restServiceForRemoveComponent.remove({
                slotId: configuration.slotId,
                componentId: configuration.slotOperationRelatedId
            }).then(function() {
                renderService.renderRemoval(configuration.componentId, configuration.componentType, configuration.slotId);
            });

        };

        return new RemoveComponentService(REMOVE_COMPONENT_CHANNEL_ID);

    }]);
