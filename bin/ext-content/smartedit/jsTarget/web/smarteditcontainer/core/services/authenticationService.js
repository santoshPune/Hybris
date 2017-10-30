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
angular.module('authenticationModule', ['authenticationInterfaceModule', 'gatewayProxyModule', 'functionsModule', 'languageSelectorModule', 'resourceLocationsModule', 'storageServiceModule', 'sharedDataServiceModule', 'alertServiceModule', 'modalServiceModule', 'ui.bootstrap', 'eventServiceModule'])
    /* 1) ngResource and ngRoute pulled transitively
     * 2) translationServiceModule is needed since the templates/modal/loginDialog.html template uses translate filter
     * Not declaring it will make UNIT fail.
     * 3) because of translationServiceModule pulling $http, one cannot wire here $modal, restServiceFactory or profileService
     * otherwise one ends up with cyclic reference. On then needs resort to dynamic 'runtime' injection via $injector.get
     */
    .factory('authenticationService', ['$rootScope', '$q', '$injector', '$log', 'AuthenticationServiceInterface', 'LANDING_PAGE_PATH', 'MODAL_BUTTON_ACTIONS', 'DEFAULT_AUTHENTICATION_ENTRY_POINT', 'DEFAULT_AUTH_MAP', 'DEFAULT_CREDENTIALS_MAP', 'gatewayProxy', 'sharedDataService', 'storageService', 'alertService', 'systemEventService', 'EVENTS', 'getQueryString', 'convertToArray', 'hitch', 'copy', 'merge', 'isBlank', 'extend', function($rootScope, $q, $injector, $log, AuthenticationServiceInterface, LANDING_PAGE_PATH, MODAL_BUTTON_ACTIONS, DEFAULT_AUTHENTICATION_ENTRY_POINT, DEFAULT_AUTH_MAP, DEFAULT_CREDENTIALS_MAP, gatewayProxy, sharedDataService, storageService, alertService, systemEventService, EVENTS, getQueryString, convertToArray, hitch, copy, merge, isBlank, extend) {

        var AuthenticationService = function() {
            this.reauthInProgress = {};
            this.gatewayId = "authenticationService";
            gatewayProxy.initForService(this);
        };

        AuthenticationService = extend(AuthenticationServiceInterface, AuthenticationService);

        AuthenticationServiceInterface.prototype._launchAuth = function(authURIAndClientCredentials) {
            return $injector.get('languageService').isInitialized().then(function(initialized) {
                return storageService.isInitialized().then(function(initialized) {
                    return $injector.get('modalService').open({
                        cssClasses: initialized ? "ySELoginInit" : "ySELoginNotInit",
                        templateUrl: 'web/common/core/services/loginDialog.html',
                        controller: ['modalManager', function(modalManager) {

                            modalManager.setShowHeaderDismiss(false);

                            this.initialized = initialized;
                            this.auth = {
                                username: '',
                                password: ''
                            };
                            this.authURI = authURIAndClientCredentials.authURI;
                            storageService.removeAuthToken(this.authURI);

                            this.authURIKey = btoa(this.authURI).replace(/=/g, "");

                            this.submit = function(loginDialogForm) {
                                var deferred = $q.defer();

                                loginDialogForm.posted = true;
                                loginDialogForm.errorMessage = '';
                                loginDialogForm.failed = false;

                                if (loginDialogForm.$valid) {
                                    var payload = copy(authURIAndClientCredentials.clientCredentials || {});
                                    payload.username = this.auth.username;
                                    payload.password = this.auth.password;
                                    payload.grant_type = 'password';

                                    $injector.get('$http')({
                                        method: 'POST',
                                        url: this.authURI,
                                        headers: {
                                            'Content-Type': 'application/x-www-form-urlencoded'
                                        },
                                        data: getQueryString(payload).replace("?", "")
                                    }).
                                    success(hitch(this, function(data, status, headers, config) {
                                        storageService.storeAuthToken(this.authURI, data);
                                        storageService.storePrincipalIdentifier(payload.username);
                                        $log.debug(["API Authentication Success: ", this.authURI, " status: ", status].join(" "));
                                        modalManager.close();
                                        deferred.resolve();
                                    })).
                                    error(hitch(this, function(data, status, headers, config) {
                                        storageService.removePrincipalIdentifier();
                                        $log.debug(["API Authentication Failure: ", this.authURI, " status: ", status].join(" "));
                                        loginDialogForm.errorMessage = data && data.error_description || 'logindialogform.oauth.error.default';
                                        loginDialogForm.failed = true;
                                        deferred.reject();
                                    }));
                                } else {

                                    loginDialogForm.errorMessage = 'logindialogform.username.and.password.required';
                                    deferred.reject();

                                }
                                return deferred.promise;
                            };

                        }]
                    });
                });
            });
        };

        AuthenticationServiceInterface.prototype.filterEntryPoints = function(resource) {
            return sharedDataService.get('authenticationMap').then(function(authenticationMap) {
                authenticationMap = merge(authenticationMap || {}, DEFAULT_AUTH_MAP);
                return convertToArray(authenticationMap).filter(function(entry) {
                    return (new RegExp(entry.key, 'g')).test(resource);
                }).map(function(element) {
                    return element.value;
                });
            });
        };

        AuthenticationServiceInterface.prototype.isAuthEntryPoint = function(resource) {
            return sharedDataService.get('authenticationMap').then(function(authenticationMap) {
                var authEntryPoints = convertToArray(authenticationMap).map(function(element) {
                    return element.value;
                });
                return authEntryPoints.indexOf(resource) > -1 || resource === DEFAULT_AUTHENTICATION_ENTRY_POINT;
            });
        };
        /*
         * will try determine first relevant authentication entry point from authenticationMap and retrieve potential client credentials to be added on top of user credentials
         */
        AuthenticationServiceInterface.prototype._findAuthURIAndClientCredentials = function(resource) {
            return this.filterEntryPoints(resource).then(function(entryPoints) {
                return sharedDataService.get('credentialsMap').then(function(credentialsMap) {
                    credentialsMap = angular.extend(credentialsMap || {}, DEFAULT_CREDENTIALS_MAP);
                    var authURI = entryPoints[0];
                    return {
                        'authURI': authURI,
                        'clientCredentials': credentialsMap[authURI]
                    };

                });
            });
        };

        AuthenticationServiceInterface.prototype.authenticate = function(resource) {
            return this._findAuthURIAndClientCredentials(resource).then(function(authURIAndClientCredentials) {
                return this._launchAuth(authURIAndClientCredentials).then(function(success) {
                    systemEventService.sendEvent(EVENTS.AUTHORIZATION_SUCCESS);
                    this.reauthInProgress[authURIAndClientCredentials.authURI] = false;
                }.bind(this));
            }.bind(this));
        };

        AuthenticationServiceInterface.prototype.logout = function() {
            // First, indicate the services that SmartEdit is logging out. This should give them the opportunity to clean up.
            // NOTE: This is not synchronous since some clean-up might be lengthy, and logging out should be fast.
            return systemEventService.sendAsynchEvent(EVENTS.LOGOUT, {})
                .finally(hitch(this, function(storage) {
                    storage.removeAllAuthTokens();

                    var $location = $injector.get('$location');
                    var currentLocation = $location.url();
                    if (isBlank(currentLocation) || currentLocation === LANDING_PAGE_PATH) {
                        $injector.get('$route').reload();
                    } else {
                        $location.url(LANDING_PAGE_PATH);
                    }

                }, storageService));
        };

        AuthenticationServiceInterface.prototype.isReAuthInProgress = function(entryPoint) {
            return $q.when(this.reauthInProgress[entryPoint] === true);
        };

        AuthenticationServiceInterface.prototype.setReAuthInProgress = function(entryPoint) {
            this.reauthInProgress[entryPoint] = true;
            return $q.when();
        };

        AuthenticationServiceInterface.prototype.isAuthenticated = function(url) {
            return this.filterEntryPoints(url).then(function(entryPoints) {
                var authURI = entryPoints && entryPoints[0];
                return storageService.getAuthToken(authURI).then(function(authToken) {
                    return !!authToken;
                });
            });
        };

        return new AuthenticationService();

    }]);
