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
 * @name urlServiceModule
 * @description
 * # The urlServiceModule
 *
 * The Url Service Module is a module for providing functionality to open a URL
 * in a new browser url from within the SmartEdit container.
 *
 */
angular.module('urlServiceModule', ['gatewayProxyModule', 'urlServiceInterfaceModule'])

/**
 * @ngdoc service
 * @name urlServiceModule.urlService
 *
 * @description
 * The Url Service is used to open a given URL in a new browser url, directly from SmartEdit. 
 * It uses the {@link gatewayProxyModule.gatewayProxy gatewayProxy} service to share data between
 * the SmartEdit application and the container, making the call to open the URL from the container. 
 * It uses the gateway ID "urlService".
 *
 * The Url Service extends the {@link urlServiceInterfaceModule.UrlServiceInterface}.
 *
 */
.factory('urlService', function(gatewayProxy, UrlServiceInterface, extend) {

    var UrlService = function(gatewayId) {
        this.gatewayId = gatewayId;

        gatewayProxy.initForService(this);
    };


    UrlService = extend(UrlServiceInterface, UrlService);

    return new UrlService('urlService');
});
