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
angular.module('previewTicketInterceptorModule', ['interceptorHelperModule', 'functionsModule'])

/**
 * @ngdoc service
 * @name previewTicketInterceptorModule.previewTicketInterceptor
 *
 * @description
 * A HTTP request interceptor that adds the preview ticket to the HTTP header object before a request is made.
 *
 * Interceptors are service factories that are registered with the $httpProvider by adding them to the
 * $httpProvider.interceptors array. The factory is called and injected with dependencies and returns the interceptor
 * object, which contains the interceptor methods.
 */
.factory('previewTicketInterceptor', function(parseQuery, interceptorHelper) {
        var getLocation = function() {
            var location;

            if (window.frameElement) {
                location = document.location.href;
            } else {
                location = $("iframe").attr("src");
            }
            return location;
        };

        /**
         * @ngdoc method
         * @name previewTicketInterceptorModule.previewTicketInterceptor#request
         * @methodOf previewTicketInterceptorModule.previewTicketInterceptor
         *
         * @description
         * Interceptor method that is called with a HTTP configuration object. It extracts the preview ticket ID (if
         * available) from the URL of the current page and then adds it to the HTTP header object as an
         * "X-Preview-Ticket" property before a request is made to the resource.
         *
         * @param {Object} config The HTTP configuration object that holds the configuration information.
         *
         * @returns {Object} Returns the modified configuration object.
         */
        var request = function request(config) {
            return interceptorHelper.handleRequest(config, function() {
                var location = this._getLocation();
                if (location) {
                    var previewTicket = parseQuery(location).cmsTicketId;
                    if (previewTicket) {
                        config.headers = config.headers || {};
                        config.headers["X-Preview-Ticket"] = previewTicket;
                    }
                }
                return config;
            }.bind(this));
        };

        var interceptor = {};
        interceptor.request = request.bind(interceptor);
        interceptor._getLocation = getLocation.bind(interceptor);
        return interceptor;
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('previewTicketInterceptor');
    });
