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
angular.module('gatewayProxyModule', ['gatewayFactoryModule'])
    /**
     * @ngdoc service
     * @name gatewayProxyModule.gatewayProxy
     *
     * @description
     * To seamlessly integrate the gateway factory between two services on different frames, you can use a gateway
     * proxy. The gateway proxy service simplifies using the gateway module by providing an API that registers an
     * instance of a service that requires a gateway for communication.
     *
     * This registration process automatically attaches listeners to each of the service's functions (turned into promises), allowing stub
     * instances to forward calls to these functions using an instance of a gateway from {@link
     * gatewayFactoryModule.gatewayFactory gatewayFactory}. Any function that has an empty body declared on the service is used
     * as a proxy function. It delegates a publish call to the gateway under the same function name, and wraps the result
     * of the call in a Promise.
     */
    .factory('gatewayProxy', function($log, $q, hitch, toPromise, gatewayFactory) {
        var isFunctionEmpty = function(fn) {
            return fn.toString().match(/\{([\s\S]*)\}/m)[1].trim() === '';
        };

        var turnToProxy = function(fnName, service, gateway) {
            delete service[fnName];
            service[fnName] = function() {
                return gateway.publish(fnName, {
                    arguments: Array.prototype.slice.call(arguments)
                }).then(function(resolvedData) {
                    return resolvedData;
                });
            };
        };

        var onGatewayEvent = function(fnName, _service, eventId, data) {
            return _service[fnName].apply(_service, data.arguments);
        };

        /**
         * @ngdoc method
         * @name gatewayProxyModule.gatewayProxy#initForService
         * @methodOf gatewayProxyModule.gatewayProxy
         *
         * @description Mutates the given service into a proxied service. The method will fail if the service is not
         * assigned a gatewayId.
         *
         * @param {Service} service Service to mutate into a proxied service.
         * @param {Array} methodsSubset OPTIONAL : subset of methods on which the gatewayProxy will trigger. If not specified, all functions will be eligible. This is particularly useful to avoid inner methods being unnecessarily turned into promises.
         */
        var initForService = function(service, methodsSubset) {
            if (!service.gatewayId) {
                $log.error('initForService() - service expected to have an associated gatewayId');
                return null;
            }

            var gateway = gatewayFactory.createGateway(service.gatewayId);

            var loopedOver = methodsSubset;
            if (!loopedOver) {
                loopedOver = [];
                for (var key in service) {
                    if (typeof service[key] === "function") {
                        loopedOver.push(key);
                    }
                }
            }

            loopedOver.forEach(function(fnName) {
                if (typeof service[fnName] === 'function') {
                    if (isFunctionEmpty(service[fnName])) {
                        turnToProxy(fnName, service, gateway);
                    } else {
                        service[fnName] = toPromise(service[fnName], service);
                        gateway.subscribe(fnName, hitch(null, onGatewayEvent, fnName, service));
                    }
                }
            });
        };

        return {
            initForService: initForService
        };
    });
