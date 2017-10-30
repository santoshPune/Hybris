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
describe('test GatewayProxy Module', function() {

    var gatewayId = "toolbar";
    var $rootScope, $q, gatewayProxy, gatewayFactory;

    beforeEach(customMatchers);

    beforeEach(module('gatewayFactoryModule', function($provide) {

        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['createGateway', 'initListener']);
        gateway = jasmine.createSpyObj('gateway', ['publish', 'subscribe']);
        gatewayFactory.createGateway.andReturn(gateway);

        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _$window_, _gatewayProxy_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        $window = _$window_;
        gatewayProxy = _gatewayProxy_;
    }));

    it('gatewayProxy will proxy empty functions and subscribe listeners for non empty functions now returning promises resolving to the return value of the method', function() {

        var service = {
            gatewayId: gatewayId,
            methodToBeProxied: function() {

            },
            method2ToBeProxied: function() {

            },
            methodToBeRemotelyInvokable: function(arg) {
                return arg + "Suffix";
            },
            method2ToBeRemotelyInvokable: function(arg) {
                return arg + "Suffix2";
            }
        };

        gateway.publish.andReturn($q.defer().promise);
        gatewayProxy.initForService(service);

        expect(gatewayFactory.createGateway).toHaveBeenCalledWith(gatewayId);
        expect(gateway.subscribe).toHaveBeenCalledWith("methodToBeRemotelyInvokable", jasmine.any(Function));
        expect(gateway.subscribe).toHaveBeenCalledWith("method2ToBeRemotelyInvokable", jasmine.any(Function));

        service.methodToBeRemotelyInvokable("anything").then(function(data) {
            expect(data).toBe("anythingSuffix");
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(gateway.publish).not.toHaveBeenCalled();

        service.methodToBeProxied("arg1", "arg2");

        expect(gateway.publish).toHaveBeenCalledWith("methodToBeProxied", {
            arguments: ['arg1', 'arg2']
        });

        service.method2ToBeProxied("arg1", "arg2");

        expect(gateway.publish).toHaveBeenCalledWith("method2ToBeProxied", {
            arguments: ['arg1', 'arg2']
        });
    });

    it('gatewayProxy will proxy empty functions and subscribe listeners for a subset of methods', function() {

        var service = {
            gatewayId: gatewayId,
            methodToBeProxied: function() {

            },
            method2ToBeProxied: function() {

            },
            methodToBeRemotelyInvokable: function(arg) {
                return arg + "Suffix";
            },
            method2ToBeRemotelyInvokable: function(arg) {
                return arg + "Suffix2";
            }
        };

        gateway.publish.andReturn($q.defer().promise);
        gatewayProxy.initForService(service, ["methodToBeProxied", "methodToBeRemotelyInvokable"]);

        expect(gatewayFactory.createGateway).toHaveBeenCalledWith(gatewayId);
        expect(gateway.subscribe).toHaveBeenCalledWith("methodToBeRemotelyInvokable", jasmine.any(Function));
        expect(gateway.subscribe).not.toHaveBeenCalledWith("method2ToBeRemotelyInvokable", jasmine.any(Function));

        service.methodToBeRemotelyInvokable("anything").then(function(data) {
            expect(data).toBe("anythingSuffix");
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(gateway.publish).not.toHaveBeenCalled();

        service.methodToBeProxied("arg1", "arg2");

        expect(gateway.publish).toHaveBeenCalledWith("methodToBeProxied", {
            arguments: ['arg1', 'arg2']
        });


        service.method2ToBeProxied("arg1", "arg2");

        expect(gateway.publish).not.toHaveBeenCalledWith("method2ToBeProxied", {
            arguments: ['arg1', 'arg2']
        });

    });

    it('gatewayProxy will proxy empty functions and will wrap the value returned by the proxy in a promise', function() {
        // Arrange
        var resolved;
        var returnedValue;
        var expectedReturnValue = "This is a return value";
        var service = {
            gatewayId: gatewayId,
            methodToBeProxied: function() {}
        };

        gateway.publish.andReturn($q.when(expectedReturnValue));
        gatewayProxy.initForService(service);

        // Act
        var promise = service.methodToBeProxied("arg1", "arg2");

        // Assert
        promise.then(function(resolvedData) {
            resolved = true;
            returnedValue = resolvedData;
        });
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        expect(gateway.publish).toHaveBeenCalledWith("methodToBeProxied", {
            arguments: ['arg1', 'arg2']
        });
        expect(resolved).toBe(true);
        expect(returnedValue).toEqual(expectedReturnValue);
    });

    it('gatewayProxy will proxy empty functions and will wrap an undefined value in a promise if the remote method returns void', function() {
        // Arrange
        var resolved;
        var returnedValue;
        var expectedReturnValue;
        var service = {
            gatewayId: gatewayId,
            methodToBeProxied: function() {}
        };

        gateway.publish.andReturn($q.when());
        gatewayProxy.initForService(service);

        // Act
        var promise = service.methodToBeProxied("arg1", "arg2");

        // Assert
        promise.then(function(resolvedData) {
            resolved = true;
            returnedValue = resolvedData;
        });
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        expect(gateway.publish).toHaveBeenCalledWith("methodToBeProxied", {
            arguments: ['arg1', 'arg2']
        });
        expect(resolved).toBe(true);
        expect(returnedValue).toEqual(expectedReturnValue);
    });

    it('gatewayProxy will wrap the result of non-empty functions in a promise', function() {
        // Arrange
        var returnedValue, resolved;
        var providedArg = "some argument";
        var expectedResult = "some result";
        var service = {
            gatewayId: gatewayId,
            methodToBeRemotelyInvokable: function(arg) {
                return expectedResult + arg;
            }
        };

        gatewayProxy.initForService(service);
        var eventID = gateway.subscribe.calls[0].args[0];
        var onGatewayEvent = gateway.subscribe.calls[0].args[1];

        // Act
        var promise = onGatewayEvent(eventID, {
            arguments: [providedArg]
        });

        // Assert
        promise.then(function(resolvedData) {
            resolved = true;
            returnedValue = resolvedData;
        });
        $rootScope.$digest(); // This is needed. Otherwise the promise is never resolved.

        expect(resolved).toBe(true);
        expect(returnedValue).toEqual(expectedResult + providedArg);
    });
});
