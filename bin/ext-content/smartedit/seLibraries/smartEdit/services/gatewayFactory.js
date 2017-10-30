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
angular.module('gatewayFactoryModule', ['eventServiceModule'])
    /**
     * @ngdoc object
     * @name gatewayFactoryModule.object:WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY
     *
     * @description
     * the name of the configuration key containing the list of white listed storefront domain names
     */
    .constant('WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY', 'whiteListedStorefronts')
    /**
     * @ngdoc object
     * @name gatewayFactoryModule.object:TIMEOUT_TO_RETRY_PUBLISHING
     *
     * @description
     * Period between two retries of a {@link gatewayFactoryModule.MessageGateway} to publish an event
     * this value must be greater than the time needed by the browser to process a postMessage back and forth across two frames.
     * Internet Explorer is now known to need more than 100ms.
     */
    .constant('TIMEOUT_TO_RETRY_PUBLISHING', 500)
    /**
     * @ngdoc service
     * @name gatewayFactoryModule.gatewayFactory
     *
     * @description
     * The Gateway Factory controls the creation of and access to {@link gatewayFactoryModule.MessageGateway MessageGateway}
     * instances.
     *
     * To construct and access a gateway, you must use the GatewayFactory's createGateway method and provide the channel
     * ID as an argument. If you try to create the same gateway twice, the second call will return a null.
     */
    .service('gatewayFactory', function($rootScope, $q, hitch, getOrigin, systemEventService, customTimeout, $log, $window, $injector, WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY, TIMEOUT_TO_RETRY_PUBLISHING) {

        var PROMISE_ACKNOWLEDGEMENT_EVENT_ID = 'promiseAcknowledgement';
        var PROMISE_RETURN_EVENT_ID = 'promiseReturn';
        var SUCCESS = 'success';
        var FAILURE = 'failure';

        //TODO: a resolved or rejected promise is never cleaned up, are there memory leak issues?
        var promisesToResolve = [];

        var messageGatewayMap = {};

        /**
         * @ngdoc method
         * @name gatewayFactoryModule.gatewayFactory#initListener
         * @methodOf gatewayFactoryModule.gatewayFactory
         *
         * @description
         * Initializes a postMessage event handler that dispatches the handling of an event to the specified gateway.
         * If the corresponding gateway does not exist, an error is logged.
         */
        function initListener() {
            // Create IE + others compatible event handler
            var eventMethod = $window.addEventListener ? "addEventListener" : "attachEvent";
            var messageEventName = eventMethod == "attachEvent" ? "onmessage" : "message";
            var processedPrimaryKeys = [];

            // Listen to message from child window
            $window[eventMethod](messageEventName, hitch(this, function(e) {

                if (this._isAllowed(e.origin)) {
                    //add control on e.origin
                    var event = e.data;

                    if (processedPrimaryKeys.indexOf(event.pk) > -1) {
                        return;
                    }
                    processedPrimaryKeys.push(event.pk);
                    $log.debug('message event handler called', event.eventId);

                    var gatewayId = event.gatewayId;
                    var gateway = messageGatewayMap[gatewayId];

                    if (!gateway) {
                        $log.debug('Incoming message on gateway ' + gatewayId + ', but no destination exists.');
                        return;
                    }

                    gateway._processEvent(event);
                } else {
                    $log.error("disallowed storefront is trying to communicate with smarteditcontainer");
                }

            }), false);
        }

        /**
         * @ngdoc method
         * @name gatewayFactoryModule.gatewayFactory#createGateway
         * @methodOf gatewayFactoryModule.gatewayFactory
         *
         * @description
         * Creates a gateway for the specified gateway identifier and caches it in order to handle postMessage events
         * later in the application lifecycle. This method will fail on subsequent calls in order to prevent two
         * clients from using the same gateway.
         *
         * @param {String} gatewayId The identifier of the gateway.
         * @returns {MessageGateway} Returns the newly created Message Gateway or null.
         */
        function createGateway(gatewayId) {
            if (messageGatewayMap[gatewayId]) {
                $log.error('Message Gateway for ' + gatewayId + ' already reserved');
                return null;
            }

            messageGatewayMap[gatewayId] = new MessageGateway(gatewayId);
            return messageGatewayMap[gatewayId];
        }


        /**
         * @ngdoc service
         * @name gatewayFactoryModule.MessageGateway
         *
         * @description
         * The Message Gateway is a private channel that is used to publish and subscribe to events across iFrame
         * boundaries. The gateway uses the W3C-compliant postMessage as its underlying technology. The benefits of
         * the postMessage are that:
         * <ul>
         *     <li>It works in cross-origin scenarios.</li>
         *     <li>The receiving end can reject messages based on their origins.</li>
         * </ul>
         *
         * The creation of instances is controlled by the {@link gatewayFactoryModule.gatewayFactory gatewayFactory}. Only one
         * instance can exist for each gateway ID.
         *
         * @param {String} gatewayId The channel identifier
         * @constructor
         */
        var MessageGateway = function MessageGateway(gatewayId) {
            this.gatewayId = gatewayId;

            this._getTargetFrame = function() {
                if ($window.frameElement) {
                    return $window.parent;
                } else if ($window.document.getElementsByTagName('iframe').length > 0) {
                    return $window.document.getElementsByTagName('iframe')[0].contentWindow;
                } else {
                    throw new Error('It is standalone. There is no iframe');
                }
            };

            this._generateIdentifier = function() {
                return new Date().getTime() + Math.random().toString();
            };

            this._getSystemEventId = function(eventId) {
                return this.gatewayId + ':' + eventId;
            };

            /**
             * @ngdoc method
             * @name gatewayFactoryModule.MessageGateway#publish
             * @methodOf gatewayFactoryModule.MessageGateway
             *
             * @description
             * Publishes a message across the gateway using the postMessage.
             *
             * The gateway's publish method implements promises, which are an AngularJS implementation. To resolve a
             * publish promise, all listener promises on the side of the channel must resolve. If a failure occurs in the
             * chain, the chain is interrupted and the publish promise is rejected.
             *
             * @param {String} eventId Event identifier
             * @param {Object} data Message payload
             * @param {Number} retries The current number of attempts to publish a message.
             * @param {Number} pk An optional parameter. It is a primary key for the event, which is generated after
             * the first attempt to send a message.
             * @returns {Promise} Promise to resolve
             */
            this.publish = function(eventId, data, retries, pk) {
                if (!retries) {
                    retries = 0;
                }

                var deferred = $q.defer();
                var target;
                try {
                    target = this._getTargetFrame();
                } catch (e) {
                    deferred.reject();
                    return deferred.promise;
                }
                pk = pk || this._generateIdentifier();
                target.postMessage({
                    pk: pk, //necessary to identify a incoming postMessage that would carry the response to resolve the promise
                    gatewayId: this.gatewayId,
                    eventId: eventId,
                    data: data
                }, '*');

                promisesToResolve[pk] = deferred;

                //in case promise does not return because, say, a non ready frame
                customTimeout(hitch(this, function(deferred, retries) {
                    if (!deferred.acknowledged && eventId !== PROMISE_RETURN_EVENT_ID && eventId !== PROMISE_ACKNOWLEDGEMENT_EVENT_ID) { //still pending
                        if (retries < 5) {
                            retries++;
                            $log.debug(document.location.href, "is retrying to publish event", eventId);
                            target.postMessage({
                                pk: pk, //necessary to identify a incoming postMessage that would carry the response to resolve the promise
                                gatewayId: this.gatewayId,
                                eventId: eventId,
                                data: data
                            }, '*');
                        } else {
                            $log.error(document.location.href, "failed to publish event", eventId);
                            deferred.reject();
                        }
                    }

                }, deferred, retries), TIMEOUT_TO_RETRY_PUBLISHING);

                return deferred.promise;
            };

            /**
             * @ngdoc method
             * @name gatewayFactoryModule.MessageGateway#subscribe
             * @methodOf gatewayFactoryModule.MessageGateway
             *
             * @description
             * Registers the given callback function to the given event ID.
             *
             * @param {String} eventId Event identifier
             * @param {Function} callback Callback function to be invoked
             */
            this.subscribe = function(eventId, callback) {
                var systemEventId = this._getSystemEventId(eventId);
                systemEventService.registerEventHandler(systemEventId, callback);
            };

            this._processEvent = function(event) {
                if (event.eventId !== PROMISE_RETURN_EVENT_ID && event.eventId !== PROMISE_ACKNOWLEDGEMENT_EVENT_ID) {
                    $log.debug(document.location.href, "sending acknowledgement for", event);

                    this.publish(PROMISE_ACKNOWLEDGEMENT_EVENT_ID, {
                        pk: event.pk,
                    });

                    var systemEventId = this._getSystemEventId(event.eventId);
                    systemEventService.sendAsynchEvent(systemEventId, event.data)
                        .then(
                            hitch(this, function(resolvedDataOfLastSubscriber) {
                                $log.debug(document.location.href, "sending promise resolve", event);
                                this.publish(PROMISE_RETURN_EVENT_ID, {
                                    pk: event.pk,
                                    type: SUCCESS,
                                    resolvedDataOfLastSubscriber: resolvedDataOfLastSubscriber
                                });
                            }),
                            hitch(this, function() {
                                $log.debug(document.location.href, "sending promise reject", event);
                                this.publish(PROMISE_RETURN_EVENT_ID, {
                                    pk: event.pk,
                                    type: FAILURE
                                });
                            })
                        );
                } else if (event.eventId === PROMISE_RETURN_EVENT_ID) {

                    if (promisesToResolve[event.data.pk]) {
                        if (event.data.type === SUCCESS) {
                            $log.debug(document.location.href, "received promise resolve", event);
                            promisesToResolve[event.data.pk].resolve(event.data.resolvedDataOfLastSubscriber);
                        } else if (event.data.type === FAILURE) {
                            $log.debug(document.location.href, "received promise reject", event);
                            promisesToResolve[event.data.pk].reject();
                        }
                    }

                } else if (event.eventId === PROMISE_ACKNOWLEDGEMENT_EVENT_ID) {
                    if (promisesToResolve[event.data.pk]) {
                        $log.debug(document.location.href, "received acknowledgement", event);
                        promisesToResolve[event.data.pk].acknowledged = true;
                    }

                }
            };
        };

        return {
            initListener: initListener,
            createGateway: createGateway,
            _isIframe: function(origin) {
                return $window.frameElement;
            },
            /**
             * allowed if receiving end is frame or [container + (white listed storefront or same origin)]
             */
            _isAllowed: function(origin) {
                var whiteListedStorefronts = $injector.has(WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY) ? $injector.get(WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY) : [];
                return this._isIframe() || getOrigin() === origin || (whiteListedStorefronts.some(function(allowedURI) {
                    return origin.indexOf(allowedURI) > -1;
                }));
            }

        };

    });
