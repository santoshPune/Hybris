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
 * @name eventServiceModule
 * @description
 *
 * eventServiceModule contains an event service which is supported by the SmartEdit {@link gatewayFactoryModule.gatewayFactory gatewayFactory} to propagate events between SmartEditContainer and SmartEdit.
 *
 */
angular.module('eventServiceModule', ['functionsModule'])

/**
 * @ngdoc object
 * @name eventServiceModule.EVENT_SERVICE_MODE_ASYNCH
 *
 * @description
 * A constant used in the constructor of the Event Service to specify asynchronous event transmission.
 */
.constant('EVENT_SERVICE_MODE_ASYNCH', 'EVENT_SERVICE_MODE_ASYNCH')

/**
 * @ngdoc object
 * @name eventServiceModule.EVENT_SERVICE_MODE_SYNCH
 *
 * @description
 * A constant that is used in the constructor of the Event Service to specify synchronous event transmission.
 */
.constant('EVENT_SERVICE_MODE_SYNCH', 'EVENT_SERVICE_MODE_SYNCH')

/**
 * @ngdoc object
 * @name eventServiceModule.EVENTS
 *
 * @description
 * Events that are fired/handled in the SmartEdit application
 */
.constant('EVENTS', {
    AUTHORIZATION_SUCCESS: 'AUTHORIZATION_SUCCESS',
    LOGOUT: 'SE_LOGOUT_EVENT',
    CLEAR_PERSPECTIVE_FEATURES: 'CLEAR_PERSPECTIVE_FEATURES'
})

/**
 * @ngdoc service
 * @name eventServiceModule.EventService
 * @description
 *
 * The event service is used to transmit events synchronously or asynchronously. It also contains options to send
 * events, as well as register and unregister event handlers.
 *
 * @param {String} defaultMode Uses constants to set event transmission. The EVENT_SERVICE_MODE_ASYNCH constant sets
 * event transmission to asynchronous mode and the EVENT_SERVICE_MODE_SYNCH constant sets the event transmission to
 * synchronous mode.
 *
 */
.factory('EventService', function(customTimeout, $q, $log, hitch, toPromise, EVENT_SERVICE_MODE_ASYNCH, EVENT_SERVICE_MODE_SYNCH) {
    var EventService = function(defaultMode) {


        this.eventHandlers = {};

        this.mode = EVENT_SERVICE_MODE_ASYNCH;
        if (defaultMode === EVENT_SERVICE_MODE_ASYNCH || defaultMode === EVENT_SERVICE_MODE_SYNCH) {
            this.mode = defaultMode;
        }

        this._recursiveCallToEventHandlers = function(eventId, data, index) {
            index = index || 0;
            var promiseClosure = toPromise(this.eventHandlers[eventId][index]);
            return promiseClosure(eventId, data).then(hitch(this, function(resolvedDataOfLastSubscriber) {
                if (index < this.eventHandlers[eventId].length - 1) {
                    return this._recursiveCallToEventHandlers(eventId, data, index + 1);
                } else {
                    return resolvedDataOfLastSubscriber;
                }
            }));
        };

        /**
         * @ngdoc method
         * @name eventServiceModule.EventService#sendEvent
         * @methodOf eventServiceModule.EventService
         *
         * @description
         * Send the event with data. The event is sent either synchronously or asynchronously depending on the event
         * mode.
         *
         * @param {String} eventId The identifier of the event.
         * @param {String} data The event payload.
         */
        this.sendEvent =
            function(eventId, data) {
                if (this.mode === EVENT_SERVICE_MODE_ASYNCH) {
                    this.sendAsynchEvent(eventId, data);
                } else if (this.mode === EVENT_SERVICE_MODE_SYNCH) {
                    this.sendSynchEvent(eventId, data);
                } else {
                    throw ('Unknown event service mode: ' + this.mode);
                }
            };

        /**
         * @ngdoc method
         * @name eventServiceModule.EventService#sendEvent
         * @methodOf eventServiceModule.EventService
         *
         * @description
         * send the event with data synchronously.
         *
         * @param {String} eventId The identifier of the event.
         * @param {String} data The event payload.
         */
        this.sendSynchEvent = function(eventId, data) {
            var deferred = $q.defer();
            if (!eventId) {
                $log.error('Failed to send event. No event ID provided for data: ' + data);
                deferred.reject();
                return;
            }
            if (this.eventHandlers[eventId] && this.eventHandlers[eventId].length > 0) {
                this._recursiveCallToEventHandlers(eventId, data).then(
                    function(resolvedDataOfLastSubscriber) {
                        deferred.resolve(resolvedDataOfLastSubscriber);
                    },
                    function() {
                        deferred.reject();
                    }
                );
            } else {
                deferred.resolve();
            }
            return deferred.promise;
        };

        this.sendAsynchEvent = function(eventId, data) {
            var deferred = $q.defer();
            customTimeout(hitch(this, function() {
                this.sendSynchEvent(eventId, data).then(
                    function(resolvedData) {
                        deferred.resolve(resolvedData);
                    },
                    function() {
                        deferred.reject();
                    }
                );
            }), 0);
            return deferred.promise;
        };

        this.registerEventHandler = function(eventId, handler) {
            if (!eventId || !handler) {
                $log.error('Failed to register event handler for event: ' + eventId);
                return;
            }
            // create handlers array for this event if not already created
            if (this.eventHandlers[eventId] === undefined) {
                this.eventHandlers[eventId] = [];
            }
            this.eventHandlers[eventId].push(handler);
        };

        this.unRegisterEventHandler = function(eventId, handler) {
            var index = this.eventHandlers[eventId].indexOf(handler);
            if (index >= 0) {
                this.eventHandlers[eventId].splice(index, 1);
            } else {
                $log.warn('Attempting to remove event handler for ' + eventId + ' but handler not found.');
            }
        };
    };



    return EventService;
})

.factory('systemEventService', function(EventService) {
    var es = new EventService();
    return es;
});
