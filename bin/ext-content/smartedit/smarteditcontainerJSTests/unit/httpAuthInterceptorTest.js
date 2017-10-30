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
describe('httpAuthInterceptor', function() {

    var $q, $httpProvider, httpAuthInterceptor, alerts, response, $httpBackend, authenticationService, storageService;
    var expectedAlertArray = [{
        successful: false,
        message: 'unknown.request.error',
        closeable: true
    }];

    beforeEach(customMatchers);
    beforeEach(module('httpAuthInterceptorModule', function($provide, _$httpProvider_) {

        $httpProvider = _$httpProvider_;
        authenticationService = jasmine.createSpyObj("authenticationService", ["isReAuthInProgress", "setReAuthInProgress", "isAuthEntryPoint", "filterEntryPoints", "authenticate"]);
        authenticationService.setReAuthInProgress.andCallFake(function() {
            return $q.when();
        });
        authenticationService.isAuthEntryPoint.andReturn(true);

        $provide.value("authenticationService", authenticationService);

        storageService = jasmine.createSpyObj("storageService", ["getAuthToken"]);

        $provide.value("storageService", storageService);

    }));

    beforeEach(inject(function(_$q_, _$rootScope_, _$httpBackend_, _httpAuthInterceptor_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        $httpBackend = _$httpBackend_;
        httpAuthInterceptor = _httpAuthInterceptor_;
        jasmine.Clock.useMock();
        storageService.getAuthToken.andReturn($q.when());
    }));

    it('$httpProvider will be loaded with only one interceptor and that will be the httpAuthInterceptor', function() {

        expect($httpProvider.interceptors).toContain('httpAuthInterceptor');

    });

    it('if url is html, config is returned, not a promise and neiher authenticationService nor storageService are ever invoked', function() {

        var config = {
            url: 'somepath/somefile.html',
            headers: {
                'key': 'value'
            }
        };

        expect(httpAuthInterceptor.request(config)).toBe(config);
        expect(httpAuthInterceptor.request(config)).toEqual(config);

        $rootScope.$digest();

        expect(config.headers).toEqual({
            key: 'value'
        });
        expect(authenticationService.filterEntryPoints).not.toHaveBeenCalled();
        expect(storageService.getAuthToken).not.toHaveBeenCalled();
    });

    it('if access_token present found, it will be added to outgoing request', function() {

        var config = {
            url: 'aurl',
            headers: {}
        };
        var authToken = {
            access_token: 'access-token1',
            token_type: 'bearer'
        };

        var entryPoints = ['entryPoint1'];
        authenticationService.filterEntryPoints.andReturn($q.when(entryPoints));

        storageService.getAuthToken.andReturn($q.when(authToken));

        expect(httpAuthInterceptor.request(config)).toBeResolvedWithData(config);

        expect(config.headers.Authorization).toBeDefined();
        expect(config.headers.Authorization).toBe(["bearer", authToken.access_token].join(" "));
        expect(authenticationService.filterEntryPoints).toHaveBeenCalledWith(config.url);
        expect(storageService.getAuthToken).toHaveBeenCalledWith('entryPoint1');
    });


    it('if access_token not found in storage, no authorization header is added to outgoing request', function() {

        var config = {
            url: 'aurl',
            headers: {}
        };
        var authToken = {
            access_token: 'access-token1',
            token_type: 'bearer'
        };

        var entryPoints = ['entryPoint1'];
        authenticationService.filterEntryPoints.andReturn($q.when(entryPoints));

        storageService.getAuthToken.andReturn($q.when(null));

        httpAuthInterceptor.request(config).then(function(returnedConfig) {
            expect(returnedConfig).toBe(config);
        }, function() {
            expect().fail();
        });

        $rootScope.$digest();

        expect(config.headers.Authorization).not.toBeDefined();
        expect(authenticationService.filterEntryPoints).toHaveBeenCalledWith('aurl');
        expect(storageService.getAuthToken).toHaveBeenCalledWith('entryPoint1');
    });

    it('if API pattern not recognised, no authorization header is added to outgoing request', function() {

        var config = {
            url: 'aurl',
            headers: {}
        };

        authenticationService.filterEntryPoints.andReturn($q.when([]));

        httpAuthInterceptor.request(config).then(function(returnedConfig) {
            expect(returnedConfig).toBe(config);
        }, function() {
            expect().fail();
        });


        $rootScope.$digest();

        expect(config.headers.Authorization).not.toBeDefined();
        expect(authenticationService.filterEntryPoints).toHaveBeenCalledWith('aurl');
        expect(storageService.getAuthToken).not.toHaveBeenCalled();
    });

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    it('if response error is 401 and not auth URL then authentication is invoked and promise of a reattempt is sent back', function() {

        var response = {
            status: 401,
            config: {
                method: 'GET',
                url: 'something'
            },
            data: {}
        };
        //final response once authenticated
        var newResponse = "anyReponse";

        authenticationService.filterEntryPoints.andReturn($q.when(['authEntryPoint']));
        authenticationService.isAuthEntryPoint.andReturn($q.when(false));
        authenticationService.isReAuthInProgress.andReturn($q.when(false));
        authenticationService.authenticate.andReturn($q.when(""));

        $httpBackend.whenGET('something').respond(newResponse);

        httpAuthInterceptor.responseError(response).then(function(success) {
            expect(success.data).toBe(newResponse);
        }, function(error) {
            expect(error).fail("the final request should have been successful");
        });
        $httpBackend.flush();

        expect(authenticationService.isAuthEntryPoint).toHaveBeenCalledWith('something');
        expect(authenticationService.isReAuthInProgress).toHaveBeenCalledWith('authEntryPoint');
        expect(authenticationService.setReAuthInProgress).toHaveBeenCalledWith('authEntryPoint');
        expect(authenticationService.authenticate).toHaveBeenCalledWith('something');
    });

    it('if more than one response error is 401 and not auth URL then authentication is invoked ONLY when same authEntryPoint and all promises of a reattempt are sent back', function() {

        var response1 = {
            status: 401,
            config: {
                method: 'GET',
                url: 'request1'
            },
            data: {}
        };
        var response2 = {
            status: 401,
            config: {
                method: 'POST',
                url: 'request2'
            },
            data: {}
        };
        //final response once authenticated
        var newResponse1 = "anyReponse1";
        var newResponse2 = "anyReponse2";

        var authDeferred = $q.defer();

        authenticationService.filterEntryPoints.andReturn($q.when(['authEntryPoint']));
        authenticationService.isAuthEntryPoint.andReturn($q.when(false));
        authenticationService.isReAuthInProgress.andReturn($q.when(false));
        authenticationService.authenticate.andReturn(authDeferred.promise);

        $httpBackend.expectGET('request1').respond(newResponse1);
        $httpBackend.expectPOST('request2').respond(newResponse2);

        httpAuthInterceptor.responseError(response1).then(function(success) {
            expect(success.data).toBe(newResponse1);
        }, function(error) {
            expect(error).fail("the final request should have been successful");
        });

        $rootScope.$digest();

        authenticationService.isReAuthInProgress.andCallFake(function(authEntryPoint) {
            if (authEntryPoint === 'authEntryPoint') {
                return $q.when(true);
            } else {
                return $q.when(false);
            }
        });

        httpAuthInterceptor.responseError(response2).then(function(success) {
            expect(success.data).toBe(newResponse2);
        }, function(error) {
            expect(error).fail("the final request should have been successful");
        });

        $rootScope.$digest();

        expect(authenticationService.isReAuthInProgress.callCount).toBe(2);
        expect(authenticationService.setReAuthInProgress.callCount).toBe(1);
        expect(authenticationService.setReAuthInProgress).toHaveBeenCalledWith('authEntryPoint');
        expect(authenticationService.authenticate.callCount).toBe(1);

        authDeferred.resolve();
        $httpBackend.flush();
    });


    it('if more than one response error is 401 and not auth URL then authentication is invoked once per authEntryPoint and all promises of a reattempt are sent back', function() {

        var response1 = {
            status: 401,
            config: {
                method: 'GET',
                url: 'request1'
            },
            data: {}
        };
        var response2 = {
            status: 401,
            config: {
                method: 'POST',
                url: 'request2'
            },
            data: {}
        };
        //final response once authenticated
        var newResponse1 = "anyReponse1";
        var newResponse2 = "anyReponse2";

        var authDeferred = $q.defer();

        authenticationService.filterEntryPoints.andCallFake(function(url) {
            if (url === 'request1') {
                return $q.when(['authEntryPoint1']);
            } else if (url === 'request2') {
                return $q.when(['authEntryPoint2']);
            }
        });
        authenticationService.isAuthEntryPoint.andReturn($q.when(false));
        authenticationService.isReAuthInProgress.andReturn($q.when(false));
        authenticationService.authenticate.andReturn(authDeferred.promise);

        $httpBackend.expectGET('request1').respond(newResponse1);
        $httpBackend.expectPOST('request2').respond(newResponse2);

        httpAuthInterceptor.responseError(response1).then(function(success) {
            expect(success.data).toBe(newResponse1);
        }, function(error) {
            expect(error).fail("the final request should have been successful");
        });

        $rootScope.$digest();

        authenticationService.isReAuthInProgress.andCallFake(function(authEntryPoint) {
            if (authEntryPoint === 'authEntryPoint1') {
                return $q.when(true);
            } else if (authEntryPoint === 'authEntryPoint2') {
                return $q.when(false);
            }
        });

        httpAuthInterceptor.responseError(response2).then(function(success) {
            expect(success.data).toBe(newResponse2);
        }, function(error) {
            expect(error).fail("the final request should have been successful");
        });

        $rootScope.$digest();

        expect(authenticationService.isReAuthInProgress.callCount).toBe(2);
        expect(authenticationService.setReAuthInProgress.callCount).toBe(2);
        expect(authenticationService.setReAuthInProgress).toHaveBeenCalledWith('authEntryPoint1');
        expect(authenticationService.setReAuthInProgress).toHaveBeenCalledWith('authEntryPoint2');
        expect(authenticationService.authenticate.callCount).toBe(2);

        authDeferred.resolve();
        $httpBackend.flush();
    });

});
