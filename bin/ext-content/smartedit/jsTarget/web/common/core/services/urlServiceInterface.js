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
 * @ngdoc service
 * @name urlServiceInterfaceModule.UrlServiceInterface
 *
 * @description
 * Provides an abstract extensible url service, Used to open a given URL
 * in a new browser url upon invocation. 
 * 
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
angular.module('urlServiceInterfaceModule', [])
    .factory('UrlServiceInterface', function() {

        function UrlServiceInterface() {}


        /** 
         * @ngdoc method
         * @name urlServiceInterfaceModule.UrlServiceInterface#openUrlInPopup
         * @methodOf urlServiceInterfaceModule.UrlServiceInterface
         *
         * @description
         * Opens a given URL in a pop up without authentication.
         *
         * @param {String} url - the URL we wish to open.
         */
        UrlServiceInterface.prototype.openUrlInPopup = function(url) {};

        UrlServiceInterface.prototype.path = function(path) {};

        return UrlServiceInterface;
    });
