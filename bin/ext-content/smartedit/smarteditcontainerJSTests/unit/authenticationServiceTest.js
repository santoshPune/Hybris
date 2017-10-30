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
describe('outer AuthenticationService', function() {
    var authenticationService,
        storageService,
        sharedDataService,
        controllerContext,
        $httpBackend,
        storageServiceMock,
        modalService,
        modalManager,
        languageService,
        loginDialogForm,
        parseQuery,
        $q,
        $location,
        $route,
        $timeout,
        systemEventService,
        EVENTS,
        $rootScope;

    beforeEach(customMatchers);
    beforeEach(module("ngMock"));

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'sendEvent']);
        $provide.value('systemEventService', systemEventService);

        EVENTS = {
            LOGOUT: 'some logout event'
        };
        $provide.value('EVENTS', EVENTS);
    }));

    beforeEach(module("authenticationModule", function($provide) {

        modalService = jasmine.createSpyObj('modalService', ['open']);
        $provide.value('modalService', modalService);

        languageService = jasmine.createSpyObj('languageService', ['isInitialized']);
        $provide.value('languageService', languageService);

        $provide.constant('DEFAULT_AUTHENTICATION_ENTRY_POINT', 'defaultAuthEntryPoint');
        $provide.constant('DEFAULT_AUTH_MAP', {
            'api3': 'defaultAuthEntryPoint'
        });
        $provide.constant('DEFAULT_AUTHENTICATION_CLIENT_ID', 'smartedit');

        $location = jasmine.createSpyObj('$location', ['url']);
        $provide.value('$location', $location);

        $route = jasmine.createSpyObj('$route', ['reload']);
        $provide.value('$route', $route);

    }));

    beforeEach(inject(function(_authenticationService_, _sharedDataService_, _$httpBackend_, _$q_, _$timeout_, _$rootScope_, _parseQuery_, _storageService_) {
        $timeout = _$timeout_;
        $httpBackend = _$httpBackend_;
        $q = _$q_;
        $rootScope = _$rootScope_;
        storageService = _storageService_;
        parseQuery = _parseQuery_;
        spyOn(storageService, 'storeAuthToken').andReturn();
        spyOn(storageService, 'removeAuthToken').andReturn();
        spyOn(storageService, 'removeAllAuthTokens').andCallFake(function() {});
        spyOn(storageService, 'storePrincipalIdentifier').andReturn();
        spyOn(storageService, 'removePrincipalIdentifier').andReturn();
        spyOn(storageService, 'isInitialized').andReturn($q.when("someState"));

        authenticationService = _authenticationService_;
        sharedDataService = _sharedDataService_;

        spyOn(sharedDataService, 'get').andCallFake(function(key) {
            if (key === 'authenticationMap') {
                return $q.when({
                    "api1": "authEntryPoint1",
                    "api1more": "authEntryPoint2",
                    "api2": "authEntryPoint3",
                });
            } else if (key === 'credentialsMap') {
                return $q.when({
                    authEntryPoint1: {
                        client_id: "client_id_1",
                        client_secret: "client_secret_1"
                    },
                    authEntryPoint2: {
                        client_id: "client_id_2",
                        client_secret: "client_secret_2"
                    }
                });
            } else if (key === 'configuration') {
                return $q.when({
                    domain: 'thedomain'
                });
            }
        });

    }));

    it('initializes and invokes gatewayProxy', function() {
        expect(authenticationService.gatewayId).toBe("authenticationService");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(authenticationService);
    });

    it('isReAuthInProgress reads status set by setReAuthInProgress', function() {

        expect(authenticationService.isReAuthInProgress("someURL")).toBeResolvedWithData(false);
        authenticationService.setReAuthInProgress("someURL");
        expect(authenticationService.isReAuthInProgress("someURL")).toBeResolvedWithData(true);

    });


    it('WHEN an entry point is filtered using filterEntryPoints AND the entry point matches one in the default auth map THEN the auth entry points returned will include the matched entry point', function() {
        // WHEN
        var promise = authenticationService.filterEntryPoints('api3');

        // THEN
        expect(promise).toBeResolvedWithData(['defaultAuthEntryPoint']);
    });

    it('filterEntryPoints only keeps the values of authenticationMap the regex keys of which match the resource', function() {

        authenticationService.filterEntryPoints("api1moreandmore").then(function(value) {
            expect(value).toEqualData(['authEntryPoint1', 'authEntryPoint2']);
        }, function() {
            expect().fail("failed to resolve to ['authEntryPoint1', 'authEntryPoint2']");
        });

        $rootScope.$digest();

        authenticationService.filterEntryPoints("api2/more").then(function(value) {
            expect(value).toEqualData(['authEntryPoint3']);
        }, function() {
            expect().fail("failed to resolve to ['authEntryPoint3']");
        });

        $rootScope.$digest();

        authenticationService.filterEntryPoints("notfound").then(function(value) {
            expect(value).toEqualData([]);
        }, function() {
            expect().fail("failed to resolve to []");
        });

        $rootScope.$digest();

    });

    it('isAuthEntryPoint returns true only if resource exactly matches at least one of the auth entry points or default auth entry point', function() {

        authenticationService.isAuthEntryPoint("api1moreandmore").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();

        authenticationService.isAuthEntryPoint("authEntryPoint1").then(function(value) {
            expect(value).toBe(true);
        }, function() {
            expect().fail("failed to resolve to true");
        });

        $rootScope.$digest();

        authenticationService.isAuthEntryPoint("authEntryPoint1more").then(function(value) {
            expect(value).toBe(false);
        }, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();


        authenticationService.isAuthEntryPoint("defaultAuthEntryPoint").then(function(value) {
            expect(value).toBe(true);
        }, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();

    });

    it('WHEN the entry point matches one in the default, default auth entry point is returned along with default client id', function() {
        // WHEN
        expect(authenticationService._findAuthURIAndClientCredentials('api3')).toBeResolvedWithData({
            authURI: 'defaultAuthEntryPoint',
            clientCredentials: {
                client_id: 'smartedit'
            }
        });

    });

    it('WHEN the entry point matches one in the auth map, corresponding entry point is returned along with relevant credentials', function() {
        // WHEN
        expect(authenticationService._findAuthURIAndClientCredentials('api1more')).toBeResolvedWithData({
            authURI: 'authEntryPoint1',
            clientCredentials: {
                client_id: 'client_id_1',
                client_secret: 'client_secret_1'
            }
        });

    });


    it('authenticate will launch modalService and remove authInprogress flag', function() {

        modalService.open.andReturn($q.when("something"));
        languageService.isInitialized.andReturn($q.when());

        authenticationService.setReAuthInProgress("authEntryPoint1");
        authenticationService.authenticate("api1/more").then(function() {}, function() {
            expect().fail("failed to resolve to false");
        });

        $rootScope.$digest();

        expect(modalService.open).toHaveBeenCalledWith({
            cssClasses: "ySELoginInit",
            templateUrl: 'web/common/core/services/loginDialog.html',
            controller: jasmine.any(Array)
        });
        expect(languageService.isInitialized).toHaveBeenCalled();
        expect(authenticationService.isReAuthInProgress("authEntryPoint1")).toBeResolvedWithData(false);

    });

    describe('controller of authenticationService', function() {

        beforeEach(function() {

            modalService.open.andReturn($q.when("something"));
            languageService.isInitialized.andReturn($q.when());

            authenticationService.authenticate("api1/more").then(function(data) {
                //expect(data).toBe("something");
            }, function() {
                expect().fail("failed to resolve to false");
            });

            $rootScope.$digest();

            var controller = modalService.open.calls[0].args[0].controller[1];
            expect(controller).toBeDefined();

            modalManager = jasmine.createSpyObj('modalManager', ['setShowHeaderDismiss', 'close']);

            controllerContext = {};
            controller.call(controllerContext, modalManager);

            systemEventService.sendAsynchEvent.andCallFake(function() {
                return $q.when();
            });
        });

        it('is set with the expected properties and functions', function() {

            expect(controllerContext.authURI).toBe("authEntryPoint1");
            expect(controllerContext.auth).toEqual({
                username: '',
                password: ''
            });

            expect(modalManager.setShowHeaderDismiss).toHaveBeenCalledWith(false);
            expect(controllerContext.initialized).toBe("someState");
            expect(storageService.isInitialized).toHaveBeenCalled();
        });

        it('submit will be rejected is form is invalid', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };

            var loginDialogForm = {
                $valid: false
            };

            controllerContext.submit(loginDialogForm).then(function() {
                expect().fail("submit should have rejected");
            });

            expect(storageService.storePrincipalIdentifier).not.toHaveBeenCalled();
            expect(storageService.storeAuthToken).not.toHaveBeenCalled();
            expect(storageService.removeAuthToken).toHaveBeenCalled();
            expect(storageService.removePrincipalIdentifier).not.toHaveBeenCalled();
        });

        it('submit will prepare a paylod with optional credentials to auth entry point and resolves and store AuthToken', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };
            var oauthToken = {
                access_token: 'access-token1',
                token_type: 'bearer'
            };
            $httpBackend.whenPOST('authEntryPoint1').respond(function(method, url, data) {
                var expectedPayload = {
                    client_id: 'client_id_1',
                    client_secret: 'client_secret_1',
                    username: 'someusername',
                    password: 'somepassword',
                    grant_type: 'password'
                };
                if (angular.equals(expectedPayload, parseQuery(data))) {
                    return [200, oauthToken];
                } else {
                    return [401];
                }
            });

            var loginDialogForm = {
                $valid: true
            };

            controllerContext.submit(loginDialogForm).then(function() {}, function() {
                expect().fail("submit should have resolved");
            });

            expect(storageService.storePrincipalIdentifier).not.toHaveBeenCalled();
            expect(storageService.storeAuthToken).not.toHaveBeenCalled();
            $httpBackend.flush();
            expect(storageService.storePrincipalIdentifier).toHaveBeenCalled();
            expect(storageService.storeAuthToken).toHaveBeenCalledWith("authEntryPoint1", oauthToken);
            expect(storageService.removePrincipalIdentifier).not.toHaveBeenCalled();
            expect(storageService.removeAuthToken).toHaveBeenCalled();
            expect(modalManager.close).toHaveBeenCalled();
        });

        it('submit will prepare a paylod with optional credentials to auth entry point and reject and remove AuthToken', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };
            var oauthToken = {
                access_token: 'access-token1',
                token_type: 'bearer'
            };

            $httpBackend.whenPOST('authEntryPoint1').respond(function(method, url, data) {
                var expectedPayload = {
                    client_id: 'client_id_1',
                    client_secret: 'client_secret_1',
                    username: 'someusername',
                    password: 'somepassword',
                    grant_type: 'password'
                };
                if (angular.equals(expectedPayload, parseQuery(data))) {
                    return [401, {
                        error_description: 'Required fields are missing'
                    }];
                }
            });

            var loginDialogForm = {
                $valid: true
            };

            controllerContext.submit(loginDialogForm).then(function() {
                expect().fail("submit should have rejected");
            }, function() {});


            expect(storageService.removePrincipalIdentifier).not.toHaveBeenCalled();
            expect(storageService.removeAuthToken).toHaveBeenCalled();
            $httpBackend.flush();
            expect(storageService.removePrincipalIdentifier).toHaveBeenCalled();
            expect(storageService.removeAuthToken).toHaveBeenCalledWith("authEntryPoint1");
            expect(storageService.storePrincipalIdentifier).not.toHaveBeenCalled();
            expect(storageService.storeAuthToken).not.toHaveBeenCalled();
            expect(loginDialogForm.errorMessage).toBe('Required fields are missing');
            expect(modalManager.close).not.toHaveBeenCalled();
        });

        it('submit will be rejected and oauth wont respond the request then the error message should use default', function() {

            controllerContext.auth = {
                username: 'someusername',
                password: 'somepassword'
            };
            var oauthToken = {
                access_token: 'access-token1',
                token_type: 'bearer'
            };

            $httpBackend.whenPOST('authEntryPoint1').respond(function(method, url, data) {
                var expectedPayload = {
                    client_id: 'client_id_1',
                    client_secret: 'client_secret_1',
                    username: 'someusername',
                    password: 'somepassword',
                    grant_type: 'password'
                };
                if (angular.equals(expectedPayload, parseQuery(data))) {
                    return [401];
                }
            });

            var loginDialogForm = {
                $valid: true
            };

            controllerContext.submit(loginDialogForm).then(function() {
                expect().fail("submit should have rejected");
            }, function() {});

            $httpBackend.flush();
            expect(loginDialogForm.errorMessage).toBe('logindialogform.oauth.error.default');
        });

        it('invalid form will be rejected then the error message should be populated with invalid form', function() {

            var loginDialogForm = {
                $valid: false
            };

            controllerContext.submit(loginDialogForm).then(function() {
                expect().fail("submit should have rejected");
            }, function() {});

            expect(loginDialogForm.errorMessage).toBe('logindialogform.username.and.password.required');
        });

        it('logout will remove auth tokens from cookie and reload current page if current page is landing page', function() {
            $location.url.andCallFake(function(arg) {
                if (!arg) {
                    return "/";
                }
            });
            var result = authenticationService.logout();
            $rootScope.$apply();

            result.then(function() {
                expect(storageService.removeAllAuthTokens).toHaveBeenCalled();
                expect($location.url).toHaveBeenCalledWith();
                expect($route.reload).toHaveBeenCalled();
            });
        });

        it('logout will remove auth tokens from cookie and reload current page if current page is empty', function() {

            $location.url.andCallFake(function(arg) {
                if (!arg) {
                    return "";
                }
            });
            var result = authenticationService.logout();
            $rootScope.$digest();

            result.then(function() {
                expect(storageService.removeAllAuthTokens).toHaveBeenCalled();
                expect($location.url).toHaveBeenCalledWith();
                expect($route.reload).toHaveBeenCalled();
            });
        });

        it('logout will remove auth tokens from cookie and redirect to landing page if current page is not landing page', function() {

            $location.url.andCallFake(function(arg) {
                if (!arg) {
                    return "/somepage";
                }
            });
            var result = authenticationService.logout();
            $rootScope.$digest();

            result.then(function() {
                expect(storageService.removeAllAuthTokens).toHaveBeenCalled();
                expect($location.url.calls.length).toBe(2);
                expect($location.url.calls[0].args).toEqual([]);
                expect($location.url.calls[1].args).toEqual(["/"]);
            });
        });

        it('WHEN logout is called THEN a LOGOUT event is raised', function() {
            // Arrange

            // Act
            var result = authenticationService.logout();
            $rootScope.$digest();

            // Assert
            result.then(function() {
                expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith(EVENTS.LOGOUT);
            });
        });

    });

    it('should return false when the access_token is not found in storage', function() {

        var entryPoints = ['entryPoint1'];
        spyOn(authenticationService, 'filterEntryPoints').andReturn($q.when(entryPoints));
        spyOn(storageService, 'getAuthToken').andReturn($q.when(null));

        expect(authenticationService.isAuthenticated('url')).toBeResolvedWithData(false);

        expect(authenticationService.filterEntryPoints).toHaveBeenCalledWith('url');
        expect(storageService.getAuthToken).toHaveBeenCalledWith('entryPoint1');
    });

    it('should return true when the access_token is found in the storage', function() {

        var entryPoints = ['entryPoint1'];
        spyOn(authenticationService, 'filterEntryPoints').andReturn($q.when(entryPoints));

        var authToken = {
            access_token: 'access-token1',
            token_type: 'bearer'
        };
        spyOn(storageService, 'getAuthToken').andReturn($q.when(authToken));

        expect(authenticationService.isAuthenticated('url')).toBeResolvedWithData(true);

        expect(authenticationService.filterEntryPoints).toHaveBeenCalledWith('url');
        expect(storageService.getAuthToken).toHaveBeenCalledWith('entryPoint1');
    });

    it('should return false when the entry point is not found in the authentication', function() {

        spyOn(authenticationService, 'filterEntryPoints').andReturn($q.when(null));
        spyOn(storageService, 'getAuthToken').andReturn($q.when(null));

        expect(authenticationService.isAuthenticated('url')).toBeResolvedWithData(false);

        expect(authenticationService.filterEntryPoints).toHaveBeenCalledWith('url');
        expect(storageService.getAuthToken).toHaveBeenCalledWith(null);
    });
});
