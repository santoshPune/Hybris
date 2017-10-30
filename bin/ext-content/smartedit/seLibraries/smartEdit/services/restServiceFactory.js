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
 * @name restServiceFactoryModule
 *
 * @description
 * # The restServiceFactoryModule
 *
 * A module providing a factory used to generate a REST service wrapper for a given resource URL.
 */
angular.module('restServiceFactoryModule', ['ngResource', 'functionsModule'])
    /**
     * @ngdoc service
     * @name restServiceFactoryModule.service:restServiceFactory
     *
     * @description
     * A factory used to generate a REST service wrapper for a given resource URL, providing a means to perform HTTP
     * operations (GET, POST, etc) for the given resource.
     */
    .factory('restServiceFactory', function($resource, $q, hitch, copy, isBlank) {

        var headers = {
            'x-requested-with': 'Angular'
        };
        var nocacheheader = copy(headers);
        nocacheheader.Pragma = 'no-cache';

        var methodMap = {
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#getById
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             *
             * @description
             * Loads a component based on its identifier.
             *
             * @param {String} identifier The value of the object identifier.
             *
             * @returns {Object} A {@link https://docs.angularjs.org/api/ng/service/$q promise} that rejects or resolves
             * to the single object returned by the call.
             */
            getById: {
                method: 'GET',
                params: {},
                isArray: false,
                cache: false,
                headers: nocacheheader
            },
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#get
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             *
             * @description
             * Loads a unique component based on its identifier that must match the specified get parameters.
             * <br/>The REST Service Factory evaluates placeholders in the URI, if any are provided, to verify if they
             * match the search parameters.
             *
             * @param {object} searchParams The object that contains the query parameters, which are then mapped to the
             * query string
             *
             * @returns {object} A {@link https://docs.angularjs.org/api/ng/service/$q promise} that rejects or resolves to the single object returned by the call.
             */
            get: {
                method: 'GET',
                params: {},
                isArray: false,
                cache: false,
                headers: nocacheheader
            },
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#query
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             *
             * @description
             * Loads a list of components that match the specified search parameters.
             * <br/>The REST service evaluates the placeholders in the URI, if any are provided, to verify if
             * they match the search parameters.
             *
             * @param {object} searchParams The object that contains the query parameters, which are then mapped to the
             * query string
             *
             * @returns {object} A {@link https://docs.angularjs.org/api/ng/service/$q promise} A promise that rejects
             * or resolves to the list of objects returned by the call.
             */
            query: {
                method: 'GET',
                params: {},
                isArray: true,
                cache: false,
                headers: nocacheheader
            },
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#page
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             * @description
             * To be called instead of {@link restServiceFactoryModule.service:ResourceWrapper#query query} when the list is wrapped by server in an object (ex: Page).
             * <br/>The service will evaluate any placeholder in the URI with matching search params.
             * @param {object} searchParams The object that contains the query parameters, which are then mapped to the
             * query string
             * @returns {object} a {@link https://docs.angularjs.org/api/ng/service/$q promise} rejecting or resolving to the page object returned by the call.
             */
            page: {
                method: 'GET',
                params: {},
                isArray: false, // due to spring Page
                cache: false,
                headers: nocacheheader,
                transformResponse: function(data, headersGetter) {
                    var deserialized = !isBlank(data) ? angular.fromJson(data) : {};
                    deserialized.headers = headersGetter();
                    return deserialized;
                }
            },
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#update
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             *
             * @description
             * Updates a component.  It appends the value of the identifier to the URI.
             *
             * <br/>The REST service warpper evaluates the placeholders in the URI, if any are provided, to verify if
             * they match the search parameters.
             *
             * @param {object} payload The object to be updated. <br/>The promise will be rejected if the payload does not contain the identifier.
             *
             * @returns {object} A {@link https://docs.angularjs.org/api/ng/service/$q promise} A promise that rejects or resolves to what the server returns.
             */
            update: {
                method: 'PUT',
                cache: false,
                headers: headers
            },
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#save
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             *
             * @description
             * Saves a component. It appends the value of the identifier to the URI.
             *
             * <br/>The REST service wrapper evaluates the placeholders in the URI, if any are provided, to verify if
             * they match the search parameters.
             *
             * @param {object} payload The object to be saved.
             * <br/>The promise will be rejected if the payload does not contain the identifier.
             *
             * @returns {object} A {@link https://docs.angularjs.org/api/ng/service/$q promise} that rejects or resolves
             * to what the server returns.
             */
            save: {
                method: 'POST',
                cache: false,
                headers: headers
            },
            /**
             * @ngdoc service
             * @name restServiceFactoryModule.service:ResourceWrapper#remove
             * @methodOf restServiceFactoryModule.service:ResourceWrapper
             *
             * @description
             * Deletes a component. It appends the value of the identifier to the URI.
             *
             * <br/>The REST service wrapper evaluates the placeholders in the URI, if any are provided, to verify if
             * they match the search parameters.
             *
             * @param {object} payload The object to be updated.
             * <br/>The promise will be rejected if the payload does not contain the identifier.
             *
             * @returns {object} A {@link https://docs.angularjs.org/api/ng/service/$q promise} that rejects or resolves
             * to what the server returns.
             */
            remove: {
                method: 'DELETE',
                cache: false,
                headers: headers
            }
        };

        var serviceMap = {};
        var IDENTIFIER = "identifier";
        var DOMAIN = null;

        var wrapMethod = function(method, initialURI, payload) {
            if (payload === undefined) {
                payload = {};
            }

            var deferred = $q.defer();

            //only keep params to be found in the URI or query params
            var params = typeof payload === 'object' ? Object.keys(payload).reduce(hitch(this, function(prev, next, index, array) {
                if (new RegExp(":" + next + "\/").test(this.uri) || new RegExp(":" + next + "$").test(this.uri) || new RegExp(":" + next + "&").test(this.uri)) {
                    prev[next] = payload[next];
                }
                return prev;
            }), {}) : {};

            if (method == 'getById') {
                //payload is the actual identifier value
                var identifierValue = payload;
                payload = {};
                payload[this.IDENTIFIER] = identifierValue;
            }
            if (method == 'update' || method == 'remove') {
                if (!payload[this.IDENTIFIER]) {
                    deferred.reject("no data was found under the " + this.IDENTIFIER + " field of object " + JSON.stringify(payload) + ", it is necessary for update and remove operations");
                    return deferred.promise;
                }
                params[this.IDENTIFIER] = payload[this.IDENTIFIER];
                if (method == 'remove') {
                    payload = {};
                }
            }
            if (method == 'get' || method == 'getById' || method == 'query' || method == 'page') {
                //params and payload are inverted
                params = copy(payload);
                payload = {};
            }
            this[method](params, payload).$promise.then(
                function(success) {
                    deferred.resolve(success);
                },
                function(error) {
                    deferred.reject(error);
                }
            );
            return deferred.promise;
        };

        return {

            IDENTIFIER: function() {
                return IDENTIFIER;
            },
            /**
             * @ngdoc method
             * @name restServiceFactoryModule.service:restServiceFactory#setDomain
             * @methodOf restServiceFactoryModule.service:restServiceFactory
             *
             * @description
             * When working with multiple services that reference the same domain, it is best to only specify relative
             * paths for the services and specify the context-wide domain in a separate location. The {@link
             * restServiceFactoryModule.service:restServiceFactory#get get} method of the {@link
             * restServiceFactoryModule.service:restServiceFactory restServiceFactory} will then prefix the specified service
             * URIs with the domain and a forward slash.
             *
             * @param {String} domain The context-wide domain that all URIs will be prefixed with when services are
             * created/when a service is created
             */
            setDomain: function(domain) {
                DOMAIN = domain;
            },
            getDomain: function() {
                return DOMAIN;
            },


            /**
             * @ngdoc method
             * @name restServiceFactoryModule.service:restServiceFactory#get
             * @methodOf restServiceFactoryModule.service:restServiceFactory
             *
             * @description
             * A factory method used to create a REST service that points to the given resource URI. The returned
             * service wraps a $resource object. As opposed to a $resource, the REST services retrieved from the
             * restServiceFactory can only take one object argument. The object argument will automatically be split
             * into a parameter object and a payload object before they are delegated to the wrapped $resource object.
             * If the domain is set, the domain is prepended to the given URI.
             *
             * @param {String} uri The URI of the REST service to be retrieved.
             * @param {String} identifier An optional parameter. The name of the placeholder that is appended to the end
             * of the URI if the name is not already provided as part of the URI. The default value is "identifier".
             * <pre>
             * 	if identifier is "resourceId" and uri is "resource/:resourceId/someValue", the target URI will remain the same.
             * 	if identifier is "resourceId" and uri is "resource", the target URI will be "resource/:resourceId".
             * </pre>
             *
             * @returns {ResourceWrapper} A {@link restServiceFactoryModule.service:ResourceWrapper wrapper} around a {@link https://docs.angularjs.org/api/ngResource/service/$resource $resource}
             */
            get: function(uri, identifier) {

                if (!identifier) {
                    identifier = IDENTIFIER;
                }
                var service = serviceMap[uri];

                if (typeof service != 'undefined') {
                    return service;
                } else {
                    var initialURI = '';
                    if (/^https?\:\/\//.test(uri) || /^\//.test(uri)) {
                        initialURI = uri;
                    } else {
                        initialURI = isBlank(DOMAIN) ? uri : DOMAIN + '/' + uri;
                    }

                    var finalURI = initialURI;
                    if (finalURI.indexOf(':' + identifier) == -1) {
                        finalURI += '/:' + identifier;
                    }

                    service = $resource(finalURI, {}, methodMap);

                    var wrapper = {};

                    ['save', 'remove'].concat(Object.keys(methodMap)).forEach(function(methodName) {
                        wrapper[methodName] = hitch(service, wrapMethod, methodName, initialURI);
                    });

                    serviceMap[uri] = wrapper;
                    service.uri = finalURI;
                    service.IDENTIFIER = identifier;

                    /**
                     * @ngdoc service
                     * @name restServiceFactoryModule.service:ResourceWrapper
                     *
                     * @description
                     * The ResourceWrapper is service used to make REST calls to the wrapped target URI. It is created
                     * by the {@link restServiceFactoryModule.service:restServiceFactory#methods_get get} method of the {@link
                     * restServiceFactoryModule.service:restServiceFactory restServiceFactory}. Each resource wrapper
                     * method returns a {@link https://docs.angularjs.org/api/ng/service/$q promise}.
                     */
                    return wrapper;
                }
            }

        };
    });
