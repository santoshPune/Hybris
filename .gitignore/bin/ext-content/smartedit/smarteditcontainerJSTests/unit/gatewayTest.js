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
describe('test Gateway Module', function() {

    var $rootScope, $q, $window, $log, systemEventService, gatewayFactory, sendDeferred, listener, gateway, getOrigin;

    beforeEach(customMatchers);
    beforeEach(function() {
        $window = {};
    });
    beforeEach(module('eventServiceModule', function($provide) {

        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler']);

        $provide.value('systemEventService', systemEventService);

        $log = jasmine.createSpyObj("$log", ["error", "debug"]);
        $provide.value('$log', $log);

        $provide.value('whiteListedStorefronts', ['sometrusteddomain', 'someothertrusteddomain']);
    }));

    beforeEach(module('gatewayFactoryModule', function($provide) {
        $provide.value('$window', $window);
    }));

    beforeEach(inject(function(_$rootScope_, _$q_, _gatewayFactory_, _getOrigin_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        gatewayFactory = _gatewayFactory_;
        getOrigin = _getOrigin_;
    }));

    it('should attach a W3C postMessage event when addEventListener exists on window', function() {
        $window.addEventListener = jasmine.createSpy('addEventListener');

        gatewayFactory.initListener();

        expect($window.addEventListener).toHaveBeenCalledWith('message', jasmine.any(Function), false);
    });

    it('should attach a W3C postMessage event when attachEvent exists on window', function() {
        $window.addEventListener = null;
        $window.attachEvent = function(eventName, callback) {};
        spyOn($window, 'attachEvent').andReturn();

        gatewayFactory.initListener();

        expect($window.attachEvent).toHaveBeenCalledWith('onmessage', jasmine.any(Function), false);
    });

    describe('GIVEN that the parent frame receives message', function() {

        beforeEach(function() {
            $window.addEventListener = function(eventName, callback) {};
            spyOn($window, 'addEventListener').andReturn();
            gatewayFactory.initListener();
            gateway = gatewayFactory.createGateway('test');
            spyOn(gateway, '_processEvent').andReturn();
            listener = $window.addEventListener.calls[0].args[1];
        });

        it('SHOULD have the listener\'s callback log error and not process event, GIVEN the domain is not white listed and the url is not same origin, ', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                origin: 'untrusted'
            };
            listener(e);
            expect(gateway._processEvent).not.toHaveBeenCalled();
            expect($log.error).toHaveBeenCalledWith('disallowed storefront is trying to communicate with smarteditcontainer');
        });

        it('SHOULD have the listener\'s callback process event of gateway only once, GIVEN url is same origin and gatewayId is test', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                data: {
                    pk: 'somepk',
                    gatewayId: 'test'
                },
                origin: getOrigin()
            };
            listener(e);
            expect(gateway._processEvent).toHaveBeenCalledWith(e.data);
            expect($log.error).not.toHaveBeenCalled();
            listener(e);
            expect(gateway._processEvent.calls.length).toBe(1);
        });

        it('SHOULD have the listener\'s callback process the event of gateway only once, GIVEN url is not same origin but is white listed and gatewayId is test', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                data: {
                    pk: 'sometrusteddomain',
                    gatewayId: 'test'
                },
                origin: getOrigin()
            };
            listener(e);
            expect(gateway._processEvent).toHaveBeenCalledWith(e.data);
            expect($log.error).not.toHaveBeenCalled();
            listener(e);
            expect(gateway._processEvent.calls.length).toBe(1);
        });


        it('SHOULD have the listener callback\'s not process the event of the gateway, GIVEN url same origin and gatewayId is not test', function() {
            spyOn(gatewayFactory, '_isIframe').andReturn(null);
            var e = {
                data: {
                    pk: 'sometrusteddomain',
                    gatewayId: 'nottest'
                },
                origin: getOrigin()
            };
            listener(e);
            expect(gateway._processEvent).not.toHaveBeenCalled();
            expect($log.error).not.toHaveBeenCalled();
        });

    });

    it('SHOULD return no gateway on subsequent calls to createGateway with the same gateway id', function() {
        var gateway = gatewayFactory.createGateway('TestChannel1');
        var duplicateGateway = gatewayFactory.createGateway('TestChannel1');

        expect(gateway).toBeDefined();
        expect(duplicateGateway).toBeNull();
    });

    it('SHOULD subscribe to the system event service with the event id <gateway_id>:<event_id>', function() {
        var CHANNEL_ID = 'TestChannel';
        var EVENT_ID = 'someEvent';
        var SYSTEM_EVENT_ID = CHANNEL_ID + ':' + EVENT_ID;

        var handler = function() {};
        var gateway = gatewayFactory.createGateway(CHANNEL_ID);

        gateway.subscribe(EVENT_ID, handler);

        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(SYSTEM_EVENT_ID, handler);
    });

    describe('publish', function() {

        var gatewayId, eventId, data, gateway, targetFrame, pk, successEvent;

        beforeEach(function() {
            $window.frameElement = {};
            $window.parent = jasmine.createSpyObj('parent', ['postMessage']);
            gatewayId = "TestChannel";
            eventId = "_testEvent";
            data = {
                "arguments": [{
                    "key": "testKey"
                }]
            };
            gateway = gatewayFactory.createGateway(gatewayId);
            pk = 'sgeydnkuykertvahdr';
            spyOn(gateway, '_generateIdentifier').andReturn(pk);
            successEvent = {
                eventId: 'promiseReturn',
                data: {
                    pk: pk,
                    type: 'success',
                    resolvedDataOfLastSubscriber: 'someData'
                }
            };

        });

        it('SHOULD post a W3C message to the target frame and return a hanging promise', function() {
            targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
            spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);
            var promise = gateway.publish(eventId, data);

            $rootScope.$digest();

            expect(promise).not.toBeResolved();
            expect(promise).not.toBeRejected();
            expect(targetFrame.postMessage).toHaveBeenCalledWith({
                pk: pk,
                eventId: eventId,
                gatewayId: gatewayId,
                data: data
            }, '*');


        });

        it('SHOULD return a resolved promise even though there is no target frame', function() {
            targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
            spyOn(gateway, '_getTargetFrame').andThrow(new Error('It is standalone. There is no iframe'));
            var promise = gateway.publish(eventId, data);

            gateway._processEvent(successEvent);

            $rootScope.$digest();

            expect(promise).toBeRejected();
        });

        it('SHOULD return a promise from publish that is resolved to event.data.resolvedDataOfLastSubscriber when incoming success promiseReturn with same pk', function() {
            targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
            spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);

            var promise = gateway.publish(eventId, data);

            gateway._processEvent(successEvent);

            $rootScope.$digest();

            expect(promise).toBeResolved();

        });


        it('SHOULD return a promise from publish that is rejected WHEN incoming failure promiseReturn with same pk', function() {
            targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
            spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);

            var promise = gateway.publish(eventId, data);

            var failureEvent = {
                eventId: 'promiseReturn',
                data: {
                    pk: pk,
                    type: 'failure'
                }
            };

            gateway._processEvent(failureEvent);

            $rootScope.$digest();

            expect(promise).toBeRejected();

        });

        it('SHOULD return a promise from publish that is still hanging WHEN incoming promiseReturn with different pk', function() {
            targetFrame = jasmine.createSpyObj('targetFrame', ['postMessage']);
            spyOn(gateway, '_getTargetFrame').andReturn(targetFrame);
            var promise = gateway.publish(eventId, data);
            var randomPk = 'fgsdfgssf';

            var differentEvent = {
                eventId: 'promiseReturn',
                data: {
                    pk: randomPk,
                    type: 'success',
                    resolvedDataOfLastSubscriber: 'someData'
                }
            };
            gateway._processEvent(differentEvent);

            $rootScope.$digest();
            expect(promise).not.toBeResolved();
            expect(promise).not.toBeRejected();
        });

    });

    describe('_processEvent', function() {
        var gateway, event;

        beforeEach(function() {
            gateway = gatewayFactory.createGateway('TestChannel');
            event = {
                pk: 'rlktqnvghsliutergwe',
                eventId: 'someEvent',
                data: {
                    key1: 'abc'
                }
            };
        });

        it("SHOULD be different from 'promiseReturn' and 'promiseAcknowledgement' will call systemEventService.sendAsynchEvent and publish a success promiseReturn event with the last resolved data from subscribers", function() {
            var sendDeferred = $q.defer();
            sendDeferred.resolve('someResolvedData');
            systemEventService.sendAsynchEvent.andReturn(sendDeferred.promise);

            spyOn(gateway, 'publish').andReturn($q.defer().promise);

            gateway._processEvent(event);

            $rootScope.$digest();

            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith('TestChannel:someEvent', {
                key1: 'abc'
            });
            expect(gateway.publish).toHaveBeenCalledWith("promiseReturn", {
                pk: 'rlktqnvghsliutergwe',
                type: 'success',
                resolvedDataOfLastSubscriber: 'someResolvedData'
            });
        });

        it("SHOULD be different from 'promiseReturn' and 'promiseAcknowldgement' will call systemEventService.sendAsynchEvent and publish a failure promiseReturn event", function() {

            var sendDeferred = $q.defer();
            sendDeferred.reject();
            systemEventService.sendAsynchEvent.andReturn(sendDeferred.promise);

            spyOn(gateway, 'publish').andReturn($q.defer().promise);

            gateway._processEvent(event);

            $rootScope.$digest();

            expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith('TestChannel:someEvent', {
                key1: 'abc'
            });
            expect(gateway.publish).toHaveBeenCalledWith("promiseReturn", {
                pk: 'rlktqnvghsliutergwe',
                type: 'failure'
            });
        });
    });

    describe('_getTargetFrame', function() {


        it('SHOULD return the parent frame if called within the iframe', function() {
            $window.frameElement = {};
            $window.parent = {};

            var gateway = gatewayFactory.createGateway('TestChannel');
            var targetFrame = gateway._getTargetFrame();

            expect(targetFrame).toBe($window.parent);
        });

        it('SHOULD return the iframe if called from the parent window', function() {
            var contentWindowContent = 'TestContentWindow';
            $window.document = jasmine.createSpyObj('document', ['getElementsByTagName']);
            $window.document.getElementsByTagName.andReturn([{
                contentWindow: contentWindowContent
            }]);
            var gateway = gatewayFactory.createGateway('TestChannel');
            var targetFrame = gateway._getTargetFrame();
            expect(targetFrame).toBe(contentWindowContent);

        });

        it('SHOULD throw an exception when called from the parent and no iframe exists', function() {
            $window.document = jasmine.createSpyObj('document', ['getElementsByTagName']);
            $window.document.getElementsByTagName.andReturn([]);
            var gateway = gatewayFactory.createGateway('TestChannel');
            expect(function() {
                gateway._getTargetFrame();
            }).toThrow(new Error('It is standalone. There is no iframe'));

        });
    });

});
