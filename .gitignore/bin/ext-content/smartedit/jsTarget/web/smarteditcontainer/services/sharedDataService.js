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
 * @name pageToolMenuModule
 * @description
 * # The sharedDataServiceModule
 *
 * The Shared Data Service Module provides a service used to store and retrieve data using a key. The data can be shared
 * between the SmartEdit application and SmartEdit container.
 *
 */
angular.module('sharedDataServiceModule', ['gatewayProxyModule', 'sharedDataServiceInterfaceModule'])

/**
 * @ngdoc service
 * @name sharedDataServiceModule.sharedDataService
 *
 * @description
 * The Shared Data Service is used to store data that is to be shared between the SmartEdit application and the
 * SmartEdit container. It uses the {@link gatewayProxyModule.gatewayProxy gatewayProxy} service to share data between
 * the SmartEdit application and the container. It uses the gateway ID "sharedData".
 *
 * The Shared Data Service extends the {@link sharedDataServiceInterfaceModule.SharedDataServiceInterface
 * SharedDataServiceInterface}.
 *
 */
.factory('sharedDataService', ['gatewayProxy', 'SharedDataServiceInterface', 'extend', function(gatewayProxy, SharedDataServiceInterface, extend) {

    var _storage = {};

    var SharedDataService = function(gatewayId) {
        this.gatewayId = gatewayId;
        gatewayProxy.initForService(this);
    };


    SharedDataService = extend(SharedDataServiceInterface, SharedDataService);

    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#get
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Retrieve the data for the given key.
     *
     * @param {String} key The key of the data to retrieve
     */
    SharedDataService.prototype.get = function(name) {
        return _storage[name];
    };

    /**
     * @ngdoc method
     * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#set
     * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
     *
     * @description
     * Store data for the given key.
     *
     * @param {String} key The key of the data to store
     * @param {String} value The value of the data to store
     */
    SharedDataService.prototype.set = function(name, value) {
        _storage[name] = value;
    };

    return new SharedDataService('sharedData');
}]);
