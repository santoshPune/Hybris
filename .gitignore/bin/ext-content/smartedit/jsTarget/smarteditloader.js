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
 * @name authenticationModule
 *
 * @description
 * # The authenticationModule
 *
 * The authentication module provides a service to authenticate and logout from SmartEdit.
 * It also allows the management of entry points used to authenticate the different resources in the application.
 *
 */
angular.module('authenticationInterfaceModule', [])
    /**
     * @ngdoc service
     * @name authenticationModule.object:DEFAULT_AUTH_MAP
     *
     * @description
     * The default authentication map contains the entry points to use before an authentication map
     * can be loaded from the configuration.
     */
    .factory('DEFAULT_AUTH_MAP', ['I18N_ROOT_RESOURCE_URI', 'DEFAULT_AUTHENTICATION_ENTRY_POINT', function(I18N_ROOT_RESOURCE_URI, DEFAULT_AUTHENTICATION_ENTRY_POINT) {
        var DEFAULT_ENTRY_POINT_MATCHER = "^(?!" + I18N_ROOT_RESOURCE_URI + '\/.*$).*$';
        var DEFAULT_AUTH_MAP = {};
        DEFAULT_AUTH_MAP[DEFAULT_ENTRY_POINT_MATCHER] = DEFAULT_AUTHENTICATION_ENTRY_POINT;

        return DEFAULT_AUTH_MAP;
    }])
    /**
     * @ngdoc service
     * @name authenticationModule.object:DEFAULT_CREDENTIALS_MAP
     *
     * @description
     * The default credentials map contains the credentials to use before an authentication map
     * can be loaded from the configuration.
     */
    .factory('DEFAULT_CREDENTIALS_MAP', ['DEFAULT_AUTHENTICATION_ENTRY_POINT', 'DEFAULT_AUTHENTICATION_CLIENT_ID', function(DEFAULT_AUTHENTICATION_ENTRY_POINT, DEFAULT_AUTHENTICATION_CLIENT_ID) {
        var DEFAULT_CREDENTIALS_MAP = {};
        DEFAULT_CREDENTIALS_MAP[DEFAULT_AUTHENTICATION_ENTRY_POINT] = {
            client_id: DEFAULT_AUTHENTICATION_CLIENT_ID
        };
        return DEFAULT_CREDENTIALS_MAP;
    }])
    /**
     * @ngdoc service
     * @name authenticationModule.service:authenticationService
     *
     * @description
     * The authenticationService is used to authenticate and logout from SmartEdit.
     * It also allows the management of entry points used to authenticate the different resources in the application.
     *
     */
    .factory('AuthenticationServiceInterface', function() {


        var AuthenticationServiceInterface = function() {

        };


        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService#authenticate
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Authenticates the current SmartEdit user against the entry point assigned to the requested resource. If no
         * suitable entry point is found, the resource will be authenticated against the
         * {@link resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT DEFAULT_AUTHENTICATION_ENTRY_POINT}
         *
         * @param {String} resource The URI identifying the resource to access.
         * @returns {Promise} A promise that resolves if the authentication is successful.
         */
        AuthenticationServiceInterface.prototype.authenticate = function(resource) {};


        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService#logout
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * The logout method removes all stored authentication tokens and redirects to the
         * landing page.
         *
         */
        AuthenticationServiceInterface.prototype.logout = function() {};


        AuthenticationServiceInterface.prototype.isReAuthInProgress = function() {};


        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService#setReAuthInProgress
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Used to indicate that the user is currently within a re-authentication flow for the given entry point.
         * This flow is entered by default through authentication token expiry.
         *
         * @param {String} entryPoint The entry point which the user must be re-authenticated against.
         *
         */
        AuthenticationServiceInterface.prototype.setReAuthInProgress = function() {};


        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService#filterEntryPoints
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Will retrieve all relevant authentication entry points for a given resource.
         * A relevant entry point is an entry value of the authenticationMap found in {@link sharedDataServiceModule.service:sharedDataService sharedDataService}.The key used in that map is a regular expression matching the resource.
         * When no entry point is found, the method returns the {@link resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT DEFAULT_AUTHENTICATION_ENTRY_POINT}
         * @param {string} resource The URL for which a relevant authentication entry point must be found.
         */
        AuthenticationServiceInterface.prototype.filterEntryPoints = function() {};


        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService##isAuthEntryPoint
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Indicates if the resource URI provided is one of the registered authentication entry points.
         *
         * @param {String} resource The URI to compare
         * @returns {Boolean} Flag that will be true if the resource URI provided is an authentication entry point.
         */
        AuthenticationServiceInterface.prototype.isAuthEntryPoint = function() {};


        /**
         * @ngdoc method
         * @name authenticationModule.service:authenticationService##isAuthenticated
         * @methodOf authenticationModule.service:authenticationService
         *
         * @description
         * Indicates if the resource URI provided maps to a registered authentication entry point and the associated entry point has an authentication token.
         *
         * @param {String} resource The URI to compare
         * @returns {Boolean} Flag that will be true if the resource URI provided maps to an authentication entry point which has an authentication token.
         */
        AuthenticationServiceInterface.prototype.isAuthenticated = function() {};

        return AuthenticationServiceInterface;
    });

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
 * @name featureInterfaceModule
 */
angular.module('featureInterfaceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name featureInterfaceModule.service:FeatureServiceInterface
 *
 * @description
 * The interface stipulates how to register features in the SmartEdit application and the SmartEdit container.
 * The SmartEdit implementation stores two instances of the interface across the {@link gatewayFactoryModule.gatewayFactory gateway}: one for the SmartEdit application and one for the SmartEdit container.
 */
.factory('FeatureServiceInterface', ['$q', '$log', 'hitch', 'isBlank', function($q, $log, hitch, isBlank) {

    function FeatureServiceInterface() {

    }

    FeatureServiceInterface.prototype._validate = function(configuration) {
        if (isBlank(configuration.key)) {
            throw new Error("featureService.configuration.key.error.required");
        }
        if (isBlank(configuration.nameI18nKey)) {
            throw new Error("featureService.configuration.nameI18nKey.error.required");
        }
        if (isBlank(configuration.enablingCallback) || typeof configuration.enablingCallback != 'function') {
            throw new Error("featureService.configuration.enablingCallback.error.not.function");
        }
        if (isBlank(configuration.disablingCallback) || typeof configuration.disablingCallback != 'function') {
            throw new Error("featureService.configuration.disablingCallback.error.not.function");
        }
    };
    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#register
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     * @description
     * This method registers a feature.
     * When an end user selects a perspective, all the features that are bound to the perspective
     * will be enabled when their respective enablingCallback functions are invoked
     * and all the features that are not bound to the perspective will be disabled when their respective disablingCallback functions are invoked.
     * The SmartEdit application and the SmartEdit container hold/store an instance of the implementation because callbacks cannot cross the gateway as they are functions.
     *
     * this method is meant to register a feature (identified by a key).
     * When a perspective (registered through {@link perspectiveInterfaceModule.service:PerspectiveServiceInterface#methods_register PerspectiveServiceInterface.register}) is selected, all its bound features will be enabled by invocation of their respective enablingCallback functions
     * and any feature not bound to it will be disabled by invocation of its disablingCallback function.
     * Both SmartEdit and SmartEditContainer will hold a concrete implementation since Callbacks, being functions, cannot cross the gateway.
     * The function will keep a frame bound reference on a full feature in order to be able to invoke its callbacks when needed.
     * 
     * @param {Object} configuration The configuration that represents the feature to be registered.
     * @param {String} configuration.key The key that uniquely identifies the feature in the registry.
     * @param {String} configuration.nameI18nKey The i18n key that stores the feature name to be translated.
     * @param {String} configuration.descriptionI18nKey The i18n key that stores the feature description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
     * @param {Function} configuration.enablingCallback The callback function invoked to enable the feature when it is required by a perspective.
     * @param {Function} configuration.disablingCallback The callback function invoked to disable the feature when it is not required by a perspective.
     */
    FeatureServiceInterface.prototype.register = function(configuration) {

        this._validate(configuration);

        this.featuresToAlias = this.featuresToAlias || {};
        this.featuresToAlias[configuration.key] = {
            enablingCallback: configuration.enablingCallback,
            disablingCallback: configuration.disablingCallback
        };
        delete configuration.enablingCallback;
        delete configuration.disablingCallback;

        this._registerAliases(configuration);
    };

    FeatureServiceInterface.prototype.enable = function(key) {
        if (this.featuresToAlias && this.featuresToAlias[key]) {
            this.featuresToAlias[key].enablingCallback();
            return;
        } else {
            this._remoteEnablingFromInner(key);
        }
    };

    FeatureServiceInterface.prototype.disable = function(key) {
        if (this.featuresToAlias && this.featuresToAlias[key]) {
            this.featuresToAlias[key].disablingCallback();
            return;
        } else {
            this._remoteDisablingFromInner(key);
        }
    };

    FeatureServiceInterface.prototype._remoteEnablingFromInner = function(key) {};
    FeatureServiceInterface.prototype._remoteDisablingFromInner = function(key) {};

    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#_registerAliases
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     * @description
     * This method registers a feature, identified by a unique key, across the {@link gatewayFactoryModule.gatewayFactory gateway}.
     * It is a simplified version of the register method, from which callbacks have been removed.
     * 
     * @param {Object} configuration the configuration representing the feature to register
     * @param {String} configuration.key The key that uniquely identifies the feature in the registry.
     * @param {String} configuration.nameI18nKey The i18n key that uniquely identifies the feature name to be translated.
     * @param {String} configuration.descriptionI18nKey The description of the l18n key to be translated. An optional parameter.
     */
    FeatureServiceInterface.prototype._registerAliases = function(configuration) {};

    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#addToolbarItem
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     *
     * @description
     * This method registers toolbar items as features. It is a wrapper around {@link featureInterfaceModule.service:FeatureServiceInterface#methods_register register}.
     * 
     * @param {Object} configuration The configuration that represents the toolbar action item to be registered.
     * @param {String} configuration.toolbarId The key that uniquely identifies the toolbar that the feature is added to.
     * @param {String} configuration.keyThe key that uniquely identifies the toolbar item in the registry as as defined in the {@link toolbarInterfaceModule.ToolbarServiceInterface#addItems ToolbarServiceInterface.addItems} API.
     * @param {String} configuration.nameI18nKey The i18n key that stores the toolbar item name to be translated.
     * @param {String} configuration.descriptionI18nKey The i18n key that stores the toolbar item description to be translated. This is an optional parameter.
     * @param {Function} configuration.callback The callback that is triggered when the toolbar action item is clicked.
     * @param {String[]} configuration.icons A list of image URLs for the icon images to be displayed in the toolbar for the items. The images are only available for ACTION and HYBRID_ACTION toolbar items.
     * @param {String} configuration.type The type of toolbar item. The possible value are: TEMPLATE, ACTION, and HYBRID_ACTION.
     * @param {String} configuration.include The URL to the HTML template. By default, templates are available for TEMPLATE and HYBRID_ACTION toolbar items.

     */
    FeatureServiceInterface.prototype.addToolbarItem = function(configuration) {};

    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#addDecorator
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     *
     * @description
     * this method registers decorator and delegates to the
     *  {@link decoratorServiceModule.service:decoratorService#methods_enable enable}
     *  {@link decoratorServiceModule.service:decoratorService#methods_disable disable} methods of 
     *  {@link decoratorServiceModule.service:decoratorService decoratorService}.
     * This method is not a wrapper around {@link decoratorServiceModule.service:decoratorService#addMappings decoratorService.addMappings}:
     * From a feature stand point, we deal with decorators, not their mappings to SmartEdit components.
     * We still need to have a separate invocation of {@link decoratorServiceModule.service:decoratorService#addMappings decoratorService.addMappings} 
     * @param {Object} configuration The configuration that represents the decorator to be registered.
     * @param {Arrays} configuration.key The decorator key defined in the {@link decoratorServiceModule.service:decoratorService#addMappings decoratorService.addMappings} API
     * @param {String} configuration.nameI18nKey the i18n key that stores the decorator name to be translated.
     * @param {String} configuration.descriptionI18nKey The i18n key that stores the decorator description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
     */
    FeatureServiceInterface.prototype.addDecorator = function(configuration) {};


    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#addContextualMenuButton
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     *
     * @description
     * This method registers contextual menu buttons. It is a wrapper around {@link contextualMenuServiceModule.ContextualMenuService#methods_addItems ContextualMenuService.addItems}.
     *
     * @param {Object} configuration The configuration representing the decorator to be registered.
     * @param {Arrays} configuration.key The key that uniquely identifies the feature in the registry.
     * @param {String} configuration.regexpKey A regular expression, ant-like wildcard or strict match that identifies the component types eligible for the specified contextual menu button.
     * @param {String} configuration.nameI18nKey They key that stores the name of the button to be translated.
     * @param {String} configuration.descriptionI18nKey The key that stores the description of the button to be translated. An optional parameter.
     * @param {Object} configuration.condition An optional entry that stores the condition required to activate the menu item. it is invoked with:
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	element: the dom element of the component onto which the contextual menu is applied
		}
     * </pre>
     * @param {Object} configuration.callback The action to be performed by clicking on the menu item. It is invoked with
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	slotId: the id of the content slot containing the component
		}
     * </pre>
     * @param {Object} configuration.callbacks A object holding a list of functions where the key is the name of the event to be performed
     * on the element and the value is the event handler function to be invoked when that particular event is triggered.
     * @param {String} configuration.displayClass The CSS classes used to style the contextual menu item.
     * @param {String} configuration.iconIdle The location of the idle icon of the contextual menu item to be displayed.
     * @param {String} configuration.iconNonIdle The location of the non-idle icon of the contextual menu item to be displayed.
     * @param {String} configuration.smallIcon The location of the smaller version of the icon to be displayed when the menu item is part of the More... menu options.
     */
    FeatureServiceInterface.prototype.addContextualMenuButton = function(configuration) {};

    return FeatureServiceInterface;

}]);

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
angular.module('httpAuthInterceptorModule', ['functionsModule', 'interceptorHelperModule', 'translationServiceModule', 'authenticationModule', 'storageServiceModule'])


/**
 * @ngdoc service
 * @name httpAuthInterceptorModule.httpAuthInterceptor
 * 
 * @description
 * Provides a way for global authentication by intercepting the requests before handing them to the server
 * and response before is its given to the application code.
 * 
 * The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array. 
 * The factory is called and injected with dependencies and returns the interceptor object with contains the interceptor methods.
 */
.factory('httpAuthInterceptor', ['$q', '$location', '$rootScope', '$injector', '$log', 'hitch', 'storageService', 'authenticationService', 'interceptorHelper', function($q, $location, $rootScope, $injector, $log, hitch, storageService, authenticationService, interceptorHelper) {

        /* key : auth entry point, value : array of deferred*/
        var promisesToResolve = {};

        var errorRecoverer = {

            /** 
             * @ngdoc method
             * @name httpAuthInterceptorModule.httpAuthInterceptor#request
             * @methodOf httpAuthInterceptorModule.httpAuthInterceptor
             * 
             * @description
             * Interceptor method which gets called with a http config object, intercepts any request made with $http service.
             * A call to any REST resource will be intercepted by this method, which then adds a authentication token to the request
             * and the forwards it to the REST resource.
             * 
             * @param {Object} config - the http config object that holds the configuration information.
             */
            request: function(config) {
                return interceptorHelper.handleRequest(config, function() {
                    return authenticationService.filterEntryPoints(config.url).then(function(entryPoints) {
                        if (entryPoints && entryPoints.length > 0) {
                            var authURI = entryPoints[0];
                            return storageService.getAuthToken(authURI).then(function(authToken) {
                                if (authToken) {
                                    $log.debug(["Intercepting request adding access token: ", config.url].join(" "));
                                    config.headers.Authorization = authToken.token_type + " " + authToken.access_token;
                                } else {
                                    $log.debug(["Intercepting request no access token found: ", config.url].join(" "));
                                }
                                return config;
                            });
                        } else {
                            return config;
                        }
                    }, function() {
                        return config;
                    });
                });
            },

            /** 
             * @ngdoc method
             * @name httpAuthInterceptorModule.httpAuthInterceptor#responseError
             * @methodOf httpAuthInterceptorModule.httpAuthInterceptor
             * 
             * @description
             * Interceptor method which intercepts all the failed responses and handles them based on the response code.
             * 
             * This method handles the following response:
             * <ul>
             * 	<li>401 response errors: handles authentication failures.</li>
             * 	<li>Non 400/401 response errors: handles any other errors by displaying an error message.</li>
             * </ul>
             * 
             * @param {Object} response - the response object which contains the status code and other parameters that explain the response.
             */
            responseError: function(response) {
                return interceptorHelper.handleResponseError(response, function() {

                    var deferred = $q.defer();

                    authenticationService.isAuthEntryPoint(response.config.url).then(function(isAuthEntryPoint) {
                        if (response.status == 401 && !isAuthEntryPoint) {
                            authenticationService.filterEntryPoints(response.config.url).then(function(entryPoints) {
                                var entryPoint = entryPoints[0];
                                $log.debug(["Intercepting and trying Authentication: ", response.config.url, entryPoint].join(" "));
                                promisesToResolve[entryPoint] = promisesToResolve[entryPoint] || [];

                                promisesToResolve[entryPoint].push(deferred);
                                authenticationService.isReAuthInProgress(entryPoint).then(function(isReAuthInProgress) {
                                    if (!isReAuthInProgress) {
                                        authenticationService.setReAuthInProgress(entryPoint).then(function() {
                                            authenticationService.authenticate(response.config.url).then(hitch(entryPoint, function() {
                                                angular.forEach(promisesToResolve[this], function(def) {
                                                    def.resolve();
                                                });
                                                promisesToResolve[this] = [];
                                            }), hitch(entryPoint, function(success) {
                                                angular.forEach(promisesToResolve[this], function(def) {
                                                    def.reject();
                                                });
                                                promisesToResolve[this] = [];
                                            }));
                                        });
                                    }
                                }.bind(this));

                            });
                        } else {
                            deferred.reject(response);
                        }
                    });

                    return deferred.promise.then(function() {
                        return $injector.get('$http')(response.config);
                    });
                });
            }
        };
        return errorRecoverer;
    }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('httpAuthInterceptor');
    }]);

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
 * @ngdoc service
 * @name httpErrorInterceptorModule
 *
 * @description
 * Module that provides a service called {@link httpErrorInterceptorModule.httpErrorInterceptor httpErrorInterceptor}
 * for handling all response errors that are neither 401 errors (Unauthorized access error) nor OCC validation errors.
 */
angular.module('httpErrorInterceptorModule', ['interceptorHelperModule', 'functionsModule', 'translationServiceModule', 'authenticationModule', 'alertServiceModule', 'resourceLocationsModule'])


/**
 * @ngdoc service
 * @name httpErrorInterceptorModule.httpErrorInterceptor
 * 
 * @description
 * Provides a way for global error handling by intercepting the requests before handing them to the server
 * and response before is its given to the application code.
 * 
 * The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array. 
 * The factory is called and injected with dependencies and returns the interceptor object with contains the interceptor methods.
 */
.factory('httpErrorInterceptor', ['LANGUAGE_RESOURCE_URI', '$q', '$location', '$rootScope', '$injector', 'hitch', 'authenticationService', '$log', 'alertService', 'interceptorHelper', function(LANGUAGE_RESOURCE_URI, $q, $location, $rootScope, $injector, hitch, authenticationService, $log, alertService, interceptorHelper) {

        var errorRecoverer = {};

        errorRecoverer.removeErrorsByType = function(type, errors) {

            return (errors || []).filter(function(error) {
                return error.type != type;
            });
        };

        errorRecoverer.convertErrorToTimedAlertObject = function(error) {

            var timedAlertObject = {
                successful: false,
                message: error.message,
                closeable: true
            };

            return timedAlertObject;
        };


        errorRecoverer._failingHTML = function(response) {

            return response.config.method === "GET" && response.status === 404 && response.headers('Content-type').indexOf('text/html') >= 0;

        };

        /**
         * @ngdoc method
         * @name httpErrorInterceptorModule.httpErrorInterceptor#responseError
         * @methodOf httpErrorInterceptorModule.httpErrorInterceptor
         *
         * @description
         * Interceptor method which intercepts all the failed responses and handles them based on the response code.
         *
         * This method handles all response errors that are neither 401 errors (Unauthorized access error) nor OCC validation errors.
         *
         * @param {Object} response - the response object which contains the status code and other parameters that explain the response.
         *
         * @returns {Promise} Returns a {@link https://docs.angularjs.org/api/ng/service/$q promise} that was already resolved as rejected with the given response.
         */
        errorRecoverer.responseError = function(response) {
            return interceptorHelper.handleResponseError(response, function() {
                $log.debug(["Intercepting response error: ", response.config.url, " status: ", response.status].join(" "));
                var languageResourceRegex = new RegExp(LANGUAGE_RESOURCE_URI.replace(/\:.*\//g, '.*/'));

                if (response.status == 400) {

                    var nonValidationErrors = errorRecoverer.removeErrorsByType("ValidationError", response.data.errors);

                    if (nonValidationErrors.length !== 0) {

                        var timedAlertObjects = [];

                        nonValidationErrors.forEach(function(nonValidationError) {
                            timedAlertObjects.push(errorRecoverer.convertErrorToTimedAlertObject(nonValidationError));
                        });

                        alertService.pushAlerts(timedAlertObjects);
                    }
                } else if (response.status != 401 && !languageResourceRegex.test(response.config.url)) {

                    var errorMessage = response.message === undefined ? "unknown.request.error" : response.message;

                    if (!errorRecoverer._failingHTML(response)) {
                        alertService.pushAlerts([{
                            successful: false,
                            message: errorMessage,
                            closeable: true
                        }]);
                    }

                }
                return $q.reject(response);
            });

        };

        return errorRecoverer;
    }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('httpErrorInterceptor');
    }]);

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
 * @name perspectiveInterfaceModule
 */
angular.module('perspectiveServiceInterfaceModule', ['functionsModule'])
    //'decorator' wording to be replaced, constructor to be modified and ngdoc to be added once dependent code is refactored
    .constant('ALL_PERSPECTIVE', 'se.all')
    .constant('NONE_PERSPECTIVE', 'se.none')
    .factory('Perspective', ['copy', function(copy) {
        var Perspective = function(name, features, system) {
            this.name = name;
            this.system = system === undefined ? false : system;
            this.features = features;
            this.setFeatures = function(features) {
                this.features = copy(features);
            };
            this.getFeatures = function() {
                return this.features;
            };
        };

        Perspective.prototype.clone = function() {
            var featureClone = [];
            var thisFeatures = this.getFeatures();
            for (var pk in thisFeatures) {
                featureClone.push(thisFeatures[pk]);
            }
            return new Perspective(this.name, featureClone, this.system);
        };

        return Perspective;
    }])
    /**
     * @ngdoc service
     * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface
     *
     */
    .factory('PerspectiveServiceInterface', function() {


        function PerspectiveServiceInterface() {}

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#register
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method registers a perspective.
         * When an end user selects a perspective in the SmartEdit web application,
         * all features bound to the perspective will be enabled when their respective enablingCallback functions are invoked
         * and all features not bound to the perspective will be disabled when their respective disablingCallback functions are invoked.
         * 
         * @param {Object} configuration The configuration that represents the feature to be registered.
         * @param {String} configuration.key The key that uniquely identifies the perspective in the registry.
         * @param {String} configuration.nameI18nKey The i18n key that stores the perspective name to be translated.
         * @param {String} configuration.descriptionI18nKey The i18n key that stores the perspective description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
         * @param {Array}  configuration.features A list of features to be bound to the perspective.
         * @param {Array}  configuration.perspectives A list of referenced perspectives to be bound to this system perspective. This list is optional.
         */
        PerspectiveServiceInterface.prototype.register = function(configuration) {};

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#switchTo
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method actives a perspective identified by its key and deactivates the currently active perspective.
         * Activating a perspective consists in activating any feature that is bound to the perspective
         * or any feature that is bound to the perspective's referenced perspectives and deactivating any features
         * that are not bound to the perspective or to its referenced perspectives.
         *
         * @param {String} key The key that uniquely identifies the perspective to be activated. This is the same key as the key used in the {@link perspectiveInterfaceModule.service:PerspectiveServiceInterface#methods_register register} method.
         */
        PerspectiveServiceInterface.prototype.switchTo = function(key) {};

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#hasActivePerspective
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * 	This method returns true if a perspective is selected.
         *
         * @returns {Boolean} The key of the active perspective.
         */
        PerspectiveServiceInterface.prototype.hasActivePerspective = function() {};


        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#selectDefault
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method switches the currently-selected perspective to the default perspective.
         * If no value has been stored in the smartedit-perspectives cookie, the value of the default perspective is se.none.
         * If a value is stored in the cookie, that value is used as the default perspective.
         *
         */
        PerspectiveServiceInterface.prototype.selectDefault = function() {};

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#isEmptyPerspectiveActive
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method returns true if the current active perspective is the Preview mode (No active overlay).
         *
         * @returns {Boolean} Flag that indicates if the current perspective is the Preview mode.
         */
        PerspectiveServiceInterface.prototype.isEmptyPerspectiveActive = function() {};

        return PerspectiveServiceInterface;

    });

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
(function() {
    /**
     * @ngdoc overview
     * @name renderServiceInterfaceModule
     * @description
     * # The renderServiceInterfaceModule
     *
     * The render service interface module provides an abstract extensible
     * {@link renderServiceInterfaceModule.service:RenderServiceInterface renderService} . It is designed to
     * re-render components after an update component data operation has been performed, according to how
     * the Accelerator displays the component.
     *
     */
    angular.module('renderServiceInterfaceModule', [])
        /**
         * @ngdoc service
         * @name renderServiceInterfaceModule.service:RenderServiceInterface
         * @description
         * Designed to re-render components after an update component data operation has been performed, according to
         * how the Accelerator displays the component.
         *
         * This class serves as an interface and should be extended, not instantiated.
         *
         */
        .factory('RenderServiceInterface', createRenderServiceInterface);

    function createRenderServiceInterface() {

        function RenderServiceInterface() {

        }

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderSlots
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * Re-renders a slot in the page.
         *
         * @param {String} slotIds The slotsIds that need to be re-rendered.
         */
        RenderServiceInterface.prototype.renderSlots = function(slotIds) {};

        // Proxied Functions
        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderComponent
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * Re-renders a component in the page.
         *
         * @param {String} componentId The ID of the component.
         * @param {String} componentType The type of the component.
         * @param {String} customContent The custom content to replace the component content with. If specified, the
         * component content will be rendered with it, instead of the accelerator's. Optional.
         *
         * @returns {Promise} Promise that will resolve on render success or reject if there's an error. When rejected,
         * the promise returns an Object{message, stack}.
         */
        RenderServiceInterface.prototype.renderComponent = function(componentId, componentType, customContent) {};

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderRemoval
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * This method removes a component from a slot in the current page. Note that the component is only removed
         * on the frontend; the operation does not propagate to the backend.
         *
         * @param {String} componentId The ID of the component to remove.
         * @param {String} componentType The type of the component.
         *
         * @returns {Object} Object wrapping the removed component.
         */
        RenderServiceInterface.prototype.renderRemoval = function(componentId, componentType) {};

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#renderPage
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * Re-renders all components in the page.
         * this method first resets the HTML content all of components to the values saved by {@link decoratorServiceModule.service:decoratorService#methods_storePrecompiledComponent} at the last $compile time
         * then requires a new compilation.
         * the renderService subscribes to the 'rerender' event of the {@link renderServiceInterfaceModule.service:renderGateway renderGateway} and triggers this method upon receiving an event
         */
        RenderServiceInterface.prototype.renderPage = function() {};

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#toggleOverlay
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * Toggles on/off the visibility of the page overlay (containing the decorators).
         *
         * @param {Boolean} showOverlay Flag that indicates if the overlay must be displayed.
         */
        RenderServiceInterface.prototype.toggleOverlay = function(showOverlay) {};

        /**
         * @ngdoc method
         * @name renderServiceInterfaceModule.service:RenderServiceInterface#refreshOverlayDimensions
         * @methodOf renderServiceInterfaceModule.service:RenderServiceInterface
         *
         * @description
         * This method updates the position of the decorators in the overlay. Normally, this method must be executed every
         * time the original storefront content is updated to keep the decorators correctly positioned.
         *
         */
        RenderServiceInterface.prototype.refreshOverlayDimensions = function() {};

        return RenderServiceInterface;
    }
})();

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
 * @ngdoc service
 * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface
 *
 * @description
 * Provides an abstract extensible shared data service. Used to store any data to be used either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
angular.module('sharedDataServiceInterfaceModule', [])
    .factory('SharedDataServiceInterface', function() {

        function SharedDataServiceInterface() {}


        /** 
         * @ngdoc method
         * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#get
         * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
         *
         * @description
         * Get the data for the given key.
         *
         * @param {String} key The key of the data to fetch
         */
        SharedDataServiceInterface.prototype.get = function(key) {};


        /** 
         * @ngdoc method
         * @name sharedDataServiceInterfaceModule.SharedDataServiceInterface#set
         * @methodOf sharedDataServiceInterfaceModule.SharedDataServiceInterface
         *
         * @description
         * Set data for the given key.
         *
         * @param {String} key The key of the data to set
         * @param {String} value The value of the data to set
         */
        SharedDataServiceInterface.prototype.set = function(key, value) {};

        return SharedDataServiceInterface;
    });

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
angular.module('toolbarInterfaceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name toolbarInterfaceModule.ToolbarServiceInterface
 *
 * @description
 * Provides an abstract extensible toolbar service. Used to manage and perform actions to either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
.factory('ToolbarServiceInterface', ['$q', '$log', 'hitch', function($q, $log, hitch) {

    /////////////////////////////////////
    // ToolbarServiceInterface Prototype
    /////////////////////////////////////

    function ToolbarServiceInterface() {}

    ToolbarServiceInterface.prototype.getItems = function() {
        return this.actions;
    };

    ToolbarServiceInterface.prototype.getAliases = function() {
        return this.aliases;
    };

    /**
     * @ngdoc method
     * @name toolbarInterfaceModule.ToolbarServiceInterface#addItems
     * @methodOf toolbarInterfaceModule.ToolbarServiceInterface
     *
     * @description
     * Takes an array of actions and maps them internally for display and trigger through an internal callback key.
     *
     * @param {Object[]} actions - List of actions
     * @param {String} actions.key - Unique identifier of the toolbar action item.
     * @param {Function} actions.callback - Callback triggered when this toolbar action item is clicked.
     * @param {String} actions.i18nkey - Description translation key
     * @param {String[]} actions.icons - List of image URLs for the icon images (only for ACTION and HYBRID_ACTION)
     * @param {String} actions.type - TEMPLATE, ACTION, or HYBRID_ACTION
     * @param {String} actions.include - HTML template URL (only for TEMPLATE and HYBRID_ACTION)
     * @param {Integer} actions.priority - Determines the position of the item in the toolbar, ranging from 0-1000 with the default priority being 500.
     * @param {String} actions.section - Determines the sections(left, middle or right) of the item in the toolbar.
     * An item with a higher priority number will be to the right of an item with a lower priority number in the toolbar.
     */
    ToolbarServiceInterface.prototype.addItems = function(actions) {
        var aliases = actions.filter(hitch(this, function(action) {
            // Validate provided actions -> The filter will return only valid items.
            var includeAction = true;

            if (!action.key) {
                $log.error("addItems() - Cannot add action without key.");
                includeAction = false;
            } else if (action.key in this.actions) {
                $log.debug("addItems() - Action already exists in toolbar with key " + action.key);
                includeAction = false;
            }

            return includeAction;
        })).map(function(action) {
            var key = action.key;
            this.actions[key] = action.callback;
            return {
                key: key,
                name: action.i18nKey,
                icons: action.icons,
                type: action.type,
                include: action.include,
                className: action.className,
                priority: action.priority || 500,
                section: action.section || 'left'
            };
        }, this);

        if (aliases.length > 0) {
            this.addAliases(aliases);
        }
    };

    /////////////////////////////////////
    // Proxied Functions : these functions will be proxied if left unimplemented
    /////////////////////////////////////

    ToolbarServiceInterface.prototype.addAliases = function() {};

    ToolbarServiceInterface.prototype.removeItemByKey = function() {};

    ToolbarServiceInterface.prototype._removeItemOnInner = function() {};

    ToolbarServiceInterface.prototype.removeAliasByKey = function() {};

    ToolbarServiceInterface.prototype.addItemsStyling = function() {};

    ToolbarServiceInterface.prototype.triggerActionOnInner = function() {};

    return ToolbarServiceInterface;
}]);

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
 * @ngdoc service
 * @name urlServiceInterfaceModule.UrlServiceInterface
 *
 * @description
 * Provides an abstract extensible url service, Used to open a given URL
 * in a new browser url upon invocation. 
 * 
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
angular.module('urlServiceInterfaceModule', [])
    .factory('UrlServiceInterface', function() {

        function UrlServiceInterface() {}


        /** 
         * @ngdoc method
         * @name urlServiceInterfaceModule.UrlServiceInterface#openUrlInPopup
         * @methodOf urlServiceInterfaceModule.UrlServiceInterface
         *
         * @description
         * Opens a given URL in a pop up without authentication.
         *
         * @param {String} url - the URL we wish to open.
         */
        UrlServiceInterface.prototype.openUrlInPopup = function(url) {};

        UrlServiceInterface.prototype.path = function(path) {};

        return UrlServiceInterface;
    });

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
 * @name componentHandlerServiceModule
 * @description
 * 
 * this module aims at handling smartEdit components both on the original storefront and the smartEdit overlay
 * 
 */
angular.module('componentHandlerServiceModule', ['functionsModule'])

/**
 * @ngdoc object
 * @name componentHandlerServiceModule.OVERLAY_ID
 * @description
 * the identifier of the overlay placed in front of the storefront to where all smartEdit component decorated clones are copied.
 */
.constant('OVERLAY_ID', 'smarteditoverlay')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.COMPONENT_CLASS
     * @description
     * the css class of the smartEdit components as per contract with the storefront
     */
    .constant('COMPONENT_CLASS', 'smartEditComponent')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS
     * @description
     * the css class of the smartEdit component clones copied to the storefront overlay
     */
    .constant('OVERLAY_COMPONENT_CLASS', 'smartEditComponentX')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX
     * @description
     * If the storefront needs to expose more attributes than the minimal contract, these attributes must be prefixed with this constant value
     */
    .constant('SMARTEDIT_ATTRIBUTE_PREFIX', 'data-smartedit-')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.ID_ATTRIBUTE
     * @description
     * the id attribute of the smartEdit components as per contract with the storefront
     */
    .constant('ID_ATTRIBUTE', 'data-smartedit-component-id')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.TYPE_ATTRIBUTE
     * @description
     * the type attribute of the smartEdit components as per contract with the storefront
     */
    .constant('TYPE_ATTRIBUTE', 'data-smartedit-component-type')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE
     * @description
     * the id attribute of the smartEdit container, when applicable, as per contract with the storefront
     */
    .constant('CONTAINER_ID_ATTRIBUTE', 'data-smartedit-container-id')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE
     * @description
     * the type attribute of the smartEdit container, when applicable, as per contract with the storefront
     */
    .constant('CONTAINER_TYPE_ATTRIBUTE', 'data-smartedit-container-type')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTENT_SLOT_TYPE
     * @description
     * the type value of the smartEdit slots as per contract with the storefront
     */
    .constant('CONTENT_SLOT_TYPE', 'ContentSlot')
    /**
     * @ngdoc service
     * @name componentHandlerServiceModule.componentHandlerService
     * @description
     *
     * The service provides convenient methods to get DOM references of smartEdit components both in the original laye rof the storefornt and in the smartEdit overlay
     */
    .factory(
        'componentHandlerService',
        ['$window', 'isBlank', 'OVERLAY_ID', 'COMPONENT_CLASS', 'OVERLAY_COMPONENT_CLASS', 'ID_ATTRIBUTE', 'TYPE_ATTRIBUTE', 'CONTAINER_ID_ATTRIBUTE', 'CONTAINER_TYPE_ATTRIBUTE', 'CONTENT_SLOT_TYPE', function($window, isBlank, OVERLAY_ID, COMPONENT_CLASS, OVERLAY_COMPONENT_CLASS, ID_ATTRIBUTE, TYPE_ATTRIBUTE, CONTAINER_ID_ATTRIBUTE, CONTAINER_TYPE_ATTRIBUTE, CONTENT_SLOT_TYPE) {

            var buildComponentQuery = function(smarteditComponentId, smarteditComponentType, cssClass) {
                var query = '';
                query += (cssClass ? '.' + cssClass : '');
                query += '[' + ID_ATTRIBUTE + '=\'' + smarteditComponentId + '\']';
                query += '[' + TYPE_ATTRIBUTE + '=\'' + smarteditComponentType + '\']';
                return query;
            };

            var buildComponentInSlotQuery = function(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass) {
                var slotQuery = buildComponentQuery(smarteditSlotId, CONTENT_SLOT_TYPE);
                var componentQuery = buildComponentQuery(smarteditComponentId, smarteditComponentType, cssClass);
                return slotQuery + ' > ' + componentQuery;
            };

            return {

                _isIframe: function() {
                    return $window.frameElement;
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getPageUID
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This extracts the pageUID of the storefront page loaded in the smartedit iframe.
                 *
                 * @return {String} a string matching the page's ID
                 */
                getPageUID: function() {
                    var targetBody = this._isIframe() ? this.getFromSelector('body') : this.getFromSelector('iframe').contents().find('body');
                    return /smartedit-page-uid\-(\S+)/.exec(targetBody.attr('class'))[1];
                },

                buildOverlayQuery: function() {
                    return '#' + OVERLAY_ID;
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getFromSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This is a wrapper around jQuery selector
                 *
                 * @param {String} selector String selector as per jQuery API
                 * 
                 * @return {Object} a jQuery object for the given selector
                 */
                getFromSelector: function(selector) {
                    return $(selector);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlay
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves a handler on the smartEdit overlay div
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 */
                getOverlay: function() {
                    return this.getFromSelector(this.buildOverlayQuery());
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentUnderSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component identified by its smartEdit id, smartEdit type and an optional class
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} smarteditSlotId the slot id of the slot containing the component as per the smartEdit contract with the storefront
                 * @param {String} cssClass the css Class to further restrict the search on. This parameter is optional.
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getComponentUnderSlot: function(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass) {
                    return this.getFromSelector(buildComponentInSlotQuery(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component identified by its smartEdit id, smartEdit type and an optional class
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} cssClass the css Class to further restrict the search on. This parameter is optional.
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getComponent: function(smarteditComponentId, smarteditComponentType, cssClass) {
                    return this.getFromSelector(buildComponentQuery(smarteditComponentId, smarteditComponentType, cssClass));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component of the original storefront layer identified by its smartEdit id, smartEdit type
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getOriginalComponent: function(smarteditComponentId, smarteditComponentType) {
                    return this.getComponent(smarteditComponentId, smarteditComponentType, COMPONENT_CLASS);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponentWithinSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component of the original storefront layer identified by its smartEdit id, smartEdit type and slot ID
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} slotId the ID of the slot within which the component resides
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getOriginalComponentWithinSlot: function(smarteditComponentId, smarteditComponentType, slotId) {
                    return this.getComponentUnderSlot(smarteditComponentId, smarteditComponentType, slotId, COMPONENT_CLASS);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentInOverlay
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component of the overlay div identified by its smartEdit id, smartEdit type
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getComponentInOverlay: function(smarteditComponentId, smarteditComponentType) {
                    return this.getComponent(smarteditComponentId, smarteditComponentType, OVERLAY_COMPONENT_CLASS);
                },

                getComponentUnderParentOverlay: function(smarteditComponentId, smarteditComponentType, parentOverlay) {
                    return this.getFromSelector(parentOverlay)
                        .find(buildComponentQuery(smarteditComponentId, smarteditComponentType, OVERLAY_COMPONENT_CLASS));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the direct smartEdit component parent of a given component.
                 * The parent is fetched in the same layer (original storefront or smartEdit overlay) as the child 
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {Object} component the jQuery component for which to search a parent
                 * 
                 * @return {Object} a jQuery object wrapping the smae-layer parent component
                 */
                getParent: function(component) {
                    var parentClassToLookFor = component.hasClass(COMPONENT_CLASS) ? COMPONENT_CLASS : (component.hasClass(OVERLAY_COMPONENT_CLASS) ? OVERLAY_COMPONENT_CLASS : null);
                    if (isBlank(parentClassToLookFor)) {
                        throw "componentHandlerService.getparent.error.component.from.unknown.layer";
                    }
                    return component.closest("." + parentClassToLookFor + "[" + ID_ATTRIBUTE + "!='" + component.attr(ID_ATTRIBUTE) + "']");
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_setId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Sets the smartEdit component id of a given component
                 *
                 * @param {Object} component the jQuery component for which to set the id
                 * @param {String} id the id to be set
                 */
                setId: function(component, id) {
                    return this.getFromSelector(component).attr(ID_ATTRIBUTE, id);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component id of a given component
                 *
                 * @param {Object} component the jQuery component for which to get the id
                 * 
                 * @return {String} the component id
                 */
                getId: function(component) {
                    return this.getFromSelector(component).attr(ID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the id that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.ID_ATTRIBUTE}
                 *
                 * @param {Object} component the jQuery component for which to get the id
                 * 
                 * @return {String} the slot operations related id
                 */
                getSlotOperationRelatedId: function(component) {
                    var containerId = this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE);
                    return containerId && this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE) ? containerId : this.getFromSelector(component).attr(ID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_setType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Sets the smartEdit component type of a given component
                 *
                 * @param {Object} component the jQuery component for which to set the type
                 * @param {String} type the type to be set
                 */
                setType: function(component, type) {
                    return this.getFromSelector(component).attr(TYPE_ATTRIBUTE, type);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component type of a given component
                 *
                 * @param {Object} component the jQuery component for which to get the type
                 * 
                 * @return {String} the component type
                 */
                getType: function(component) {
                    return this.getFromSelector(component).attr(TYPE_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the type that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.TYPE_ATTRIBUTE}
                 *
                 * @param {Object} component the jQuery component for which to get the type
                 * 
                 * @return {String} the slot operations related type
                 */
                getSlotOperationRelatedType: function(component) {
                    var containerType = this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE);
                    return containerType && this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE) ? containerType : this.getFromSelector(component).attr(TYPE_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getAllComponentsSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery selector matching all smartEdit components that are not of type ContentSlot
                 */
                getAllComponentsSelector: function() {
                    return "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "!='ContentSlot']";
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getAllSlotsSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery selector matching all smartEdit components that are of type ContentSlot
                 */
                getAllSlotsSelector: function() {
                    return "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot']";
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParentSlotForComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the the slot ID for a given element
                 * 
                 * @param {Object} the DOM element which represents the component
                 * 
                 * @return {String} the slot ID for that particular component
                 */
                getParentSlotForComponent: function(component) {
                    var parent = component.closest('[' + TYPE_ATTRIBUTE + '=' + CONTENT_SLOT_TYPE + ']');
                    return parent.attr(ID_ATTRIBUTE);
                }
            };

        }]);

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
angular.module('alertsBoxModule', []).directive('alertsBox', function() {
    return {
        templateUrl: 'web/common/services/alerts/alertsTemplate.html',
        restrict: 'E', // it kicks in on <alerts-box> elements
        transclude: true,
        replace: false,
        scope: {
            alerts: '=',
        },
        link: function(scope, element, attrs) {
            scope.dismissAlert = function(index) {
                scope.alerts.splice(index, 1);
            };
        }
    };
});

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
 * @name hasOperationPermissionModule
 * @description
 * This module provides a directive used to determine if the current user has permission to perform the action defined
 * by a given permission key, and remove/add elements from the DOM accordingly.
 */
angular.module('hasOperationPermissionModule', ['authorizationModule', 'eventServiceModule'])

.controller('hasOperationPermissionController', ['$scope', '$log', 'authorizationService', 'systemEventService', 'EVENTS', function($scope, $log, authorizationService, systemEventService, EVENTS) {
    this.isPermissionGranted = false;

    this.refreshIsPermissionGranted = function() {
        authorizationService.canPerformOperation(this.hasOperationPermission).then(function(isPermissionGranted) {
            this.isPermissionGranted = isPermissionGranted;
        }.bind(this), function() {
            $log.error('Failed to retrieve authorization');
            this.isPermissionGranted = false;
        }.bind(this));
    };

    var eventHandler = this.refreshIsPermissionGranted.bind(this);

    systemEventService.registerEventHandler(EVENTS.AUTHORIZATION_SUCCESS, eventHandler);

    $scope.$on('$destroy', function() {
        systemEventService.unRegisterEventHandler(EVENTS.AUTHORIZATION_SUCCESS, eventHandler);
    });

    $scope.$watch(function() {
        return this.hasOperationPermission;
    }.bind(this), function(newValue, oldValue) {
        if (newValue !== oldValue) {
            this.refreshIsPermissionGranted();
        }
    }.bind(this));

    this.refreshIsPermissionGranted();
}])

/**
 * @ngdoc directive
 * @name authorizationModule.directive:hasOperationPermission
 * @scope
 * @restrict A
 * @element ANY
 *
 * @description
 * Authorization HTML mark-up that will remove elements from the DOM if the user does not have authorization defined
 * by the input parameter permission key. This directive makes use of the {@link authorizationModule.AuthorizationService
 * AuthorizationService} service to validate if the current user has the given permission.
 *
 * @param {Type} has-operation-permission The hybris type to check for given permission
 */
.directive('hasOperationPermission', function() {
    return {
        transclude: true,
        restrict: 'A',
        templateUrl: 'web/common/services/authorization/hasOperationPermissionTemplate.html',
        controller: 'hasOperationPermissionController',
        controllerAs: 'ctrl',
        scope: {},
        bindToController: {
            hasOperationPermission: '='
        }
    };
});

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
 * @name authorizationModule.authorizationService
 * @description
 * The authorization module provides services to fetch operation permissions for hybris types existing in the platform.
 * This module makes use of the {@link restServiceFactoryModule restServiceFactoryModule} in order to poll authorization APIs in
 * the backend.
 */
angular.module('authorizationModule', ['restServiceFactoryModule', 'loadConfigModule', 'storageServiceModule', 'resourceLocationsModule'])

.provider("permissionKeysFactory", function() {
    var keysToCheck = [];

    return {
        addKeysToCheck: function(keys) {
            keys.forEach(function(key) {
                if (keysToCheck.indexOf(key) == -1) {
                    keysToCheck.push(key);
                }
            });
        },
        $get: function() {
            return {
                getPermissionKeys: function() {
                    return angular.copy(keysToCheck);
                }
            };
        }
    };
})

/**
 * @ngdoc service
 * @name authorizationModule.AuthorizationService
 *
 * @description
 * Service that makes calls to the permissions REST API to expose permissions for a given hybris type.
 */
.service('authorizationService', ['restServiceFactory', 'storageService', 'permissionKeysFactory', 'USER_GLOBAL_PERMISSIONS_RESOURCE_URI', '$log', function(restServiceFactory, storageService, permissionKeysFactory, USER_GLOBAL_PERMISSIONS_RESOURCE_URI, $log) {

    var permissions = {};
    var permissionsResource = restServiceFactory.get(USER_GLOBAL_PERMISSIONS_RESOURCE_URI);

    var load = function() {
        return storageService.getPrincipalIdentifier().then(function(user) {
            if (!user) return [];
            permissions[user] = permissions[user] || permissionsResource.get({
                user: user,
                permissionNames: permissionKeysFactory.getPermissionKeys().join(',')
            }).then(function(permissionResponse) {
                return permissionResponse.permissions;
            });
            return permissions[user];
        }.bind(this));
    };

    var hasPermission = function(permissions, type) {
        return !!(permissions || []).find(function(entry) {
            return entry.key.toLowerCase() === type.toLowerCase() && entry.value === 'true';
        });
    };

    /**
     * @ngdoc method
     * @name authorizationModule.AuthorizationService#canPerformOperation
     * @methodOf authorizationModule.AuthorizationService
     *
     * @description
     * Confirm that an operation permission is available on the type
     *
     * @param {String} type the hybris type to check permission
     */
    this.canPerformOperation = function(type) {
        return load.call(this, type).then(function(permissions) {
            return hasPermission.call(this, permissions, type);
        }.bind(this), function() {
            $log.error('AuthorizationService - Failed to determine authorization for type ' + type);
            return false;
        });
    };

}]);

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
(function() {
    /**
     * @ngdoc overview
     * @name compileHtmlModule
     * @description
     * # The compileHtmlModule
     *
     * The compileHtmlModule provides a directive to evaluate and compile HTML markup.
     *
     */
    angular.module('compileHtmlModule', [])

    /**
     * @ngdoc directive
     * @name compileHtmlModule.directive:compileHtml
     * @scope
     * @restrict A
     * @attribute compile-html
     *
     * @description
     * Directive responsible for evaluating and compiling HTML markup.
     *
     * @param {String} String HTML string to be evaluated and compiled.
     * @example
     * <pre>
     *      <div compile-html="<a data-ng-click=\"injectedContext.onLink( item.path )\">{{ item[key.property] }}</a>"></div>
     * </pre>
     **/
    .directive('compileHtml', ['$compile', function($compile) {
        return {
            restrict: 'A',
            link: function(scope, element, attrs) {
                scope.$watch(
                    function(scope) {
                        return scope.$eval(attrs.compileHtml);
                    },
                    function(value) {
                        element.html(value);
                        $compile(element.contents())(scope);
                    }
                );
            }
        };
    }]);

})();

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
angular.module('dragAndDropServiceInterfaceModule', [])

/**
 * @ngdoc service
 * @name dragAndDropServiceInterfaceModule.DragAndDropServiceInterface
 *
 * @description
 * Provides an abstract extensible drag and drop service. Used to manage and perform actions to either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
.factory('DragAndDropServiceInterface', function() {

    function DragAndDropServiceInterface() {}

    /**
     * @ngdoc method
     * @name dragAndDropServiceInterfaceModule.DragAndDropServiceInterface#register
     * @methodOf dragAndDropServiceInterfaceModule.DragAndDropServiceInterface
     *
     * @description
     * Method used to handle drag and drop from within the iframe
     * and from outside the iframe ( from SmartEdit Container) to the iframe (SmartEdit)
     *
     * @param {Object} configuration - configuration
     * @param {Function} configuration.startCallback - Callback to be used when dragging starts
     * @param {Function} configuration.dropCallback - Callback to be used when dropped
     * @param {Function} configuration.overCallback - Callback to be used as mouse enters the scope of a sortable element
     * @param {Function} configuration.outCallback - Callback to be used as mouse exits the scope of a sortable element
     * @param {String} configuration.sourceSelector - The jQuery selector to identify items that can be dragged
     * @param {String} configuration.targetSelector - The jQuery selectot to identify locations where items can be dropped
     *
     */
    DragAndDropServiceInterface.prototype.register = function(configuration) {};


    return DragAndDropServiceInterface;
});

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
.factory('EventService', ['customTimeout', '$q', '$log', 'hitch', 'toPromise', 'EVENT_SERVICE_MODE_ASYNCH', 'EVENT_SERVICE_MODE_SYNCH', function(customTimeout, $q, $log, hitch, toPromise, EVENT_SERVICE_MODE_ASYNCH, EVENT_SERVICE_MODE_SYNCH) {
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
}])

.factory('systemEventService', ['EventService', function(EventService) {
    var es = new EventService();
    return es;
}]);

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

angular.module('experienceInterceptorModule', ['interceptorHelperModule', 'functionsModule', 'sharedDataServiceModule', 'restServiceFactoryModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name ExperienceInterceptorModule.experienceInterceptor
     *
     * @description
     * A HTTP request interceptor which intercepts all 'cmswebservices/catalogs' requests and adds the current catalog and version
     * from any URI which define the variables 'CURRENT_CONTEXT_CATALOG' and 'CURRENT_CONTEXT_CATALOG_VERSION' in the URL.
     *
     *
     * Note: The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array.
     * The factory is called and injected with dependencies and returns the interceptor object with contains the interceptor methods.
     */
    .factory('experienceInterceptor', ['hitch', 'sharedDataService', 'interceptorHelper', 'CONTEXT_CATALOG', 'CONTEXT_CATALOG_VERSION', 'CMSWEBSERVICES_PATH', 'MEDIA_PATH', 'CONTEXT_SITE_ID', function(hitch, sharedDataService, interceptorHelper, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CMSWEBSERVICES_PATH, MEDIA_PATH, CONTEXT_SITE_ID) {

        /**
         * @ngdoc method
         * @name ExperienceInterceptorModule.experienceInterceptor#request
         * @methodOf ExperienceInterceptorModule.experienceInterceptor
         *
         * @description
         * Interceptor method which gets called with a http config object, intercepts any 'cmswebservices/catalogs' requests and adds
         * the current catalog and version
         * from any URI which define the variables 'CURRENT_CONTEXT_CATALOG' and 'CURRENT_CONTEXT_CATALOG_VERSION' in the URL.
         *
         * The catalog name and catalog versions are stored in the shared data service object called 'experience' during preview initialization
         * and here we retrieve those details and set it to headers.
         *
         * @param {Object} config the http config object that holds the configuration information.
         *
         * @returns {Promise} Returns a {@link https://docs.angularjs.org/api/ng/service/$q promise} of the passed config object.
         */
        var request = function request(config) {
            return interceptorHelper.handleRequest(config, function() {
                if (config.url.indexOf(MEDIA_PATH) > -1) {
                    return sharedDataService.get('experience').then(function(data) {
                        if (data) {
                            if (config.params.params.indexOf(CONTEXT_CATALOG) > -1) {
                                // Injecting the current value for the media, when there is a search query.
                                config.params.params = config.params.params.replace(CONTEXT_CATALOG, data.catalogDescriptor.catalogId);
                            }
                            if (config.params.params.indexOf(CONTEXT_CATALOG_VERSION) > -1) {
                                // Injecting the current value for the catalogVersion, when there is a search query.
                                config.params.params = config.params.params.replace(CONTEXT_CATALOG_VERSION, data.catalogDescriptor.catalogVersion);
                            }
                        }
                        return config;
                    });
                } else if (config.url.indexOf(CMSWEBSERVICES_PATH) > -1) {
                    return sharedDataService.get('experience').then(function(data) {
                        if (data) {
                            if (config.url.indexOf(CONTEXT_CATALOG) > -1) {
                                // Injecting the current value for the catalog, when there is a search query.
                                config.url = config.url.replace(CONTEXT_CATALOG, data.catalogDescriptor.catalogId);
                            }
                            if (config.url.indexOf(CONTEXT_CATALOG_VERSION) > -1) {
                                // Injecting the current value for the catalogVersion, when there is a search query.
                                config.url = config.url.replace(CONTEXT_CATALOG_VERSION, data.catalogDescriptor.catalogVersion);
                            }
                            if (config.url.indexOf(CONTEXT_SITE_ID) > -1) {
                                // Injecting the current value for the site, when there is a search query.
                                config.url = config.url.replace(CONTEXT_SITE_ID, data.siteDescriptor.uid);
                            }
                        }
                        return config;
                    });
                } else {
                    return config;
                }
            });
        };

        var interceptor = {};
        interceptor.request = hitch(interceptor, request);
        return interceptor;
    }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('experienceInterceptor');
    }]);

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
 * @ngdoc service
 * @name functionsModule
 *
 * @description
 * provides a list of useful functions that can be used as part of the SmartEdit framework.
 */
angular.module('functionsModule', [])

.factory('ParseError', function() {
        var ParseError = function(value) {
            this.value = value;
        };
        return ParseError;
    })
    /**
     * @ngdoc service
     * @name functionsModule.getOrigin
     *
     * @description
     * returns document location origin
     * Some browsers still do not support W3C document.location.origin, this function caters for gap.
     */
    .factory('getOrigin', function() {
        return function() {
            return window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
        };
    })
    /**
     * @ngdoc service
     * @name functionsModule.isBlank
     *
     * @description
     * <b>isBlank</b> will check if a given string is undefined or null or empty.
     * - returns TRUE for undefined / null/ empty string
     * - returns FALSE otherwise
     *
     * @param {String} inputString any input string.
     * 
     * @returns {boolean} true if the string is null else false
     */
    .factory('isBlank', function() {
        return function(value) {
            return (typeof value == 'undefined' || value === null || value === "null" || value.toString().trim().length === 0);
        };
    })

/**
 * @ngdoc service
 * @name functionsModule.extend
 *
 * @description
 * <b>extend</b> provides a convenience to either default a new child or "extend" an existing child with the prototype of the parent
 *
 * @param {Class} ParentClass which has a prototype you wish to extend.
 * @param {Class} ChildClass will have its prototype set.
 * 
 * @returns {Class} ChildClass which has been extended
 */
.factory('extend', function() {
    return function(ParentClass, ChildClass) {
        if (!ChildClass) {
            ChildClass = function() {};
        }
        ChildClass.prototype = Object.create(ParentClass.prototype);
        return ChildClass;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.hitch
 *
 * @description
 * <b>hitch</b> will create a new function that will pass our desired context (scope) to the given function.
 * This method will also pre-bind the given parameters.
 *
 * @param {Object} scope scope that is to be assigned.
 * @param {Function} method the method that needs binding. 
 * 
 * @returns {Function} a new function thats binded to the given scope
 */
.factory('hitch', function() {
    return function(scope, method) {

        var argumentArray = Array.prototype.slice.call(arguments); // arguments is not an array
        // (from  http://www.sitepoint.com/arguments-a-javascript-oddity/)

        var preboundArguments = argumentArray.slice(2);

        return function lockedMethod() {

            // from here, "arguments" are the arguments passed to lockedMethod

            var postBoundArguments = Array.prototype.slice.call(arguments);

            return method.apply(scope, preboundArguments.concat(postBoundArguments));

        };

    };
})

/**
 * @ngdoc service
 * @name functionsModule.customTimeout
 *
 * @description
 * <b>customTimeout</b> will call the javascrit's native setTimeout method to execute a given function after a specified period of time.
 * This method is better than using $timeout since it is difficult to assert on $timeout during end-to-end testing.
 *
 * @param {Function} func function that needs to be executed after the specified duration.
 * @param {Number} duration time in milliseconds. 
 */
.factory('customTimeout', ['$rootScope', function($rootScope) {
    return function(func, duration) {
        setTimeout(function() {
            func();
            $rootScope.$digest();
        }, duration);
    };
}])

/**
 * @ngdoc service
 * @name functionsModule.copy
 *
 * @description
 * <b>copy</b> will do a deep copy of the given input object.
 *
 * @param {Object} candidate the javaScript value that needs to be deep copied.
 * 
 * @returns {Object} A deep copy of the input
 */
.factory('copy', function() {
    return function(candidate) {
        return JSON.parse(JSON.stringify(candidate));
    };
})

/**
 * @ngdoc service
 * @name functionsModule.merge
 *
 * @description
 * <b>merge</b> will merge the contents of two objects together into the first object.
 *
 * @param {Object} target any JavaScript object.
 * @param {Object} source any JavaScript object.
 * 
 * @returns {Object} a new object as a result of merge
 */
.factory('merge', function() {
    return function(source, target) {

        jQuery.extend(source, target);

        return source;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.getQueryString
 *
 * @description
 * <b>getQueryString</b> will convert a given object into a query string.
 * 
 * Below is the code snippet for sample input and sample output:
 * 
 * <pre>
 * var params = {
 *  key1 : 'value1',
 *  key2 : 'value2',
 *  key3 : 'value3'
 *  }
 *  
 *  var output = getQueryString(params);
 *  
 *  // The output is '?&key1=value1&key2=value2&key3=value3' 
 *
 *</pre>
 *
 * @param {Object} params Object containing a list of params.
 * 
 * @returns {String} a query string
 */
.factory('getQueryString', function() {
    return function(params) {

        var queryString = "";
        if (params) {
            for (var param in params) {
                queryString += '&' + param + "=" + params[param];
            }
        }
        return "?" + queryString;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.getURI
 *
 * @description
 * Will return the URI part of a URL
 * @param {String} url the URL the URI of which is to be returned
 */
.factory('getURI', function() {
    return function(url) {
        return url && url.indexOf("?") > -1 ? url.split("?")[0] : url;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.parseQuery
 *
 * @description
 * <b>parseQuery</b> will convert a given query string to an object.
 *
 * Below is the code snippet for sample input and sample output:
 *
 * <pre>
 * var query = '?key1=value1&key2=value2&key3=value3';
 *  
 * var output = parseQuery(query);
 * 
 * // The output is { key1 : 'value1', key2 : 'value2', key3 : 'value3' }
 *
 *</pre>
 *
 * @param {String} query String that needs to be parsed.
 * 
 * @returns {Object} an object containing all params of the given query
 */
.factory('parseQuery', function() {
    return function(str) {

        var objURL = {};

        str.replace(new RegExp("([^?=&]+)(=([^&]*))?", "g"), function($0, $1, $2, $3) {
            objURL[$1] = $3;
        });
        return objURL;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.trim
 *
 * @description
 * <b>trim</b> will remove spaces at the beginning and end of a given string.
 *
 * @param {String} inputString any input string.
 * 
 * @returns {String} the newly modified string without spaces at the beginning and the end
 */
.factory('trim', function() {

    return function(aString) {
        var regExpBeginning = /^\s+/;
        var regExpEnd = /\s+$/;
        return aString.replace(regExpBeginning, "").replace(regExpEnd, "");
    };
})

/**
 * @ngdoc service
 * @name functionsModule.convertToArray
 *
 * @description
 * <b>convertToArray</b> will convert the given object to array.
 * The output array elements are an object that has a key and value,
 * where key is the original key and value is the original object.
 * 
 * @param {Object} inputObject any input object.
 * 
 * @returns {Array} the array created from the input object
 */
.factory('convertToArray', function() {

    return function(object) {
        var configuration = [];
        for (var key in object) {
            if (key.indexOf('$') !== 0 && key.indexOf('toJSON') !== 0) {
                configuration.push({
                    key: key,
                    value: object[key]
                });
            }
        }
        return configuration;
    };

})

/**
 * @ngdoc service
 * @name functionsModule.injectJS
 *
 * @description
 * <b>injectJS</b> will inject script tags into html for a given set of sources.
 *
 */
.factory('injectJS', ['customTimeout', '$log', 'hitch', function(customTimeout, $log, hitch) {

    function getInjector() {
        return $script;
    }

    return {
        getInjector: getInjector,

        /** 
         * @ngdoc method
         * @name functionsModule.injectJS#execute
         * @methodOf functionsModule.injectJS
         * 
         * @description
         * <b>execute</b> will extract a given set of sources from the provided configuration object
         * and then inject each source as a JavaScript source tag and potential callbacks once all the
         * sources are wired.
         * 
         * @param {Object} configuration - a given set of configurations.
         * @param {Array} configuration.sources - an array of sources that needs to be added.
         * @param {Function} configuration.callback - Callback to be triggered once all the sources are wired.
         */
        execute: function(conf) {
            var srcs = conf.srcs;
            var index = conf.index;
            var callback = conf.callback;
            if (index === undefined) {
                index = 0;
            }
            if (srcs[index] !== undefined) {
                this.getInjector()(srcs[index], hitch(this, function() {
                    if (index + 1 < srcs.length) {
                        this.execute({
                            srcs: srcs,
                            index: index + 1,
                            callback: callback
                        });
                    } else if (typeof callback == 'function') {
                        callback();
                    }
                }));

            }
        }
    };
}])

/**
 * @ngdoc service
 * @name functionsModule.uniqueArray
 *
 * @description
 * <b>uniqueArray</b> will return the first Array argument supplemented with new entries from the second Array argument.
 * 
 * @param {Array} array1 any JavaScript array.
 * @param {Array} array2 any JavaScript array.
 */
.factory('uniqueArray', function() {

    return function(array1, array2) {

        array2.forEach(function(instance) {
            if (array1.indexOf(instance) == -1) {
                array1.push(instance);
            }
        });

        return array1;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.regExpFactory
 *
 * @description
 * <b>regExpFactory</b> will convert a given pattern into a regular expression.
 * This method will prepend and append a string with ^ and $ respectively replaces
 * and wildcards (*) by proper regex wildcards.
 * 
 * @param {String} pattern any string that needs to be converted to a regular expression.
 * 
 * @returns {RegExp} a regular expression generated from the given string.
 *
 */
.factory('regExpFactory', function() {

    return function(pattern) {

        var onlyAlphanumericsRegex = new RegExp(/^[a-zA-Z\d]+$/i);
        var antRegex = new RegExp(/^[a-zA-Z\d\*]+$/i);

        if (onlyAlphanumericsRegex.test(pattern)) {
            regexpKey = ['^', '$'].join(pattern);
        } else if (antRegex.test(pattern)) {
            regexpKey = ['^', '$'].join(pattern.replace(/\*/, '.*'));
        } else {
            regexpKey = pattern;
        }

        return new RegExp(regexpKey, 'g');
    };
})

/**
 * @ngdoc service
 * @name functionsModule.generateIdentifier
 *
 * @description
 * <b>generateIdentifier</b> will generate a unique string based on system time and a random generator.
 * 
 * @returns {String} a unique identifier.
 *
 */
.factory('generateIdentifier', function() {
    return function() {
        var d = new Date().getTime();
        if (window.performance && typeof window.performance.now === "function") {
            d += performance.now(); //use high-precision timer if available
        }
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
        return uuid;
    };
})




/**
 * @ngdoc service
 * @name functionsModule.escapeHtml
 *
 * @description
 * <b>escapeHtml</b> will escape &, <, >, " and ' characters .
 *
 * @param {String} a string that needs to be escaped.
 *
 * @returns {String} the escaped string.
 *
 */
.factory('escapeHtml', function() {
    return function(string) {
        if (typeof string === 'string') {
            return string.replace(/&/g, '&amp;')
                .replace(/>/g, '&gt;')
                .replace(/</g, '&lt;')
                .replace(/"/g, '&quot;')
                .replace(/'/g, '&apos;');
        } else {
            return string;
        }
    };
})


/**
 * @ngdoc service
 * @name functionsModule.sanitizeHTML
 *
 * @description
 * <b>sanitizeHTML</b> will remove breaks and space .
 *
 * @param {String} a string that needs to be escaped.
 *
 * @returns {String} the sanitized HTML.
 *
 */
.factory('sanitizeHTML', function() {
    return function(obj, localized) {
        var result = angular.copy(obj);
        if (localized) {
            angular.forEach(result.value, function(val, key) {
                if (val !== null) {
                    result.value[key] = val.replace(/(\r\n|\n|\r)/gm, '').replace(/>\s+</g, '><').replace(/<\/br\>/g, '');
                }
            });
        } else {
            if (result !== null) {
                result = result.replace(/(\r\n|\n|\r)/gm, '').replace(/>\s+</g, '><').replace(/<\/br\>/g, '');
            }
        }
        return result;
    };
})

/**
 * @ngdoc service
 * @name functionsModule.toPromise
 *
 * @description
 * <b>toPromise</> transforms a function into a function that is guaranteed to return a Promise that resolves to the
 * original return value of the function.
 */
.factory('toPromise', ['$q', function($q) {
    return function(method, context) {
        return function() {
            return $q.when(method.apply(context, arguments));
        };
    };
}])

/**
 * Checks if `value` is a function.
 *
 * @static
 * @memberOf _
 * @category Objects
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if the `value` is a function, else `false`.
 * @example
 *
 * _.isFunction(_);
 * // => true
 */
.factory('isFunction', function() {
    return function(value) {
        return typeof value == 'function';
    };
})

// check if the value is the ECMAScript language type of Object
.factory('isObject', function() {
    /** Used to determine if values are of the language type Object */
    var objectTypes = {
        'boolean': false,
        'function': true,
        'object': true,
        'number': false,
        'string': false,
        'undefined': false
    };
    return function(value) {
        return !!(value && objectTypes[typeof value]);
    };
})

/**
 * Creates a function that will delay the execution of `func` until after
 * `wait` milliseconds have elapsed since the last time it was invoked.
 * Provide an options object to indicate that `func` should be invoked on
 * the leading and/or trailing edge of the `wait` timeout. Subsequent calls
 * to the debounced function will return the result of the last `func` call.
 *
 * Note: If `leading` and `trailing` options are `true` `func` will be called
 * on the trailing edge of the timeout only if the the debounced function is
 * invoked more than once during the `wait` timeout.
 *
 * @static
 * @category Functions
 * @param {Function} func The function to debounce.
 * @param {number} wait The number of milliseconds to delay.
 * @param {Object} [options] The options object.
 * @param {boolean} [options.leading=false] Specify execution on the leading edge of the timeout.
 * @param {number} [options.maxWait] The maximum time `func` is allowed to be delayed before it's called.
 * @param {boolean} [options.trailing=true] Specify execution on the trailing edge of the timeout.
 * @returns {Function} Returns the new debounced function.
 * @example
 *
 * // avoid costly calculations while the window size is in flux
 * var lazyLayout = _.debounce(calculateLayout, 150);
 * jQuery(window).on('resize', lazyLayout);
 *
 * // execute `sendMail` when the click event is fired, debouncing subsequent calls
 * jQuery('#postbox').on('click', _.debounce(sendMail, 300, {
 *   'leading': true,
 *   'trailing': false
 * });
 *
 * // ensure `batchLog` is executed once after 1 second of debounced calls
 * var source = new EventSource('/stream');
 * source.addEventListener('message', _.debounce(batchLog, 250, {
 *   'maxWait': 1000
 * }, false);
 */
.factory('debounce', ['isFunction', 'isObject', function(isFunction, isObject) {
    function TypeError() {

    }

    return function(func, wait, options) {
        var args,
            maxTimeoutId,
            result,
            stamp,
            thisArg,
            timeoutId,
            trailingCall,
            leading,
            lastCalled = 0,
            maxWait = false,
            trailing = true,
            isCalled;

        if (!isFunction(func)) {
            throw new TypeError();
        }
        wait = Math.max(0, wait) || 0;
        if (options === true) {
            leading = true;
            trailing = false;
        } else if (isObject(options)) {
            leading = options.leading;
            maxWait = 'maxWait' in options && (Math.max(wait, options.maxWait) || 0);
            trailing = 'trailing' in options ? options.trailing : trailing;
        }
        var delayed = function() {
            var remaining = wait - (Date.now() - stamp);
            if (remaining <= 0) {
                if (maxTimeoutId) {
                    clearTimeout(maxTimeoutId);
                }
                isCalled = trailingCall;
                maxTimeoutId = timeoutId = trailingCall = undefined;
                if (isCalled) {
                    lastCalled = Date.now();
                    result = func.apply(thisArg, args);
                    if (!timeoutId && !maxTimeoutId) {
                        args = thisArg = null;
                    }
                }
            } else {
                timeoutId = setTimeout(delayed, remaining);
            }
        };

        var maxDelayed = function() {
            if (timeoutId) {
                clearTimeout(timeoutId);
            }
            maxTimeoutId = timeoutId = trailingCall = undefined;
            if (trailing || (maxWait !== wait)) {
                lastCalled = Date.now();
                result = func.apply(thisArg, args);
                if (!timeoutId && !maxTimeoutId) {
                    args = thisArg = null;
                }
            }
        };

        return function() {
            args = arguments;
            stamp = Date.now();
            thisArg = this;
            trailingCall = trailing && (timeoutId || !leading);
            var leadingCall, isCalled;

            if (maxWait === false) {
                leadingCall = leading && !timeoutId;
            } else {
                if (!maxTimeoutId && !leading) {
                    lastCalled = stamp;
                }
                var remaining = maxWait - (stamp - lastCalled);
                isCalled = remaining <= 0;

                if (isCalled) {
                    if (maxTimeoutId) {
                        maxTimeoutId = clearTimeout(maxTimeoutId);
                    }
                    lastCalled = stamp;
                    result = func.apply(thisArg, args);
                } else if (!maxTimeoutId) {
                    maxTimeoutId = setTimeout(maxDelayed, remaining);
                }
            }
            if (isCalled && timeoutId) {
                timeoutId = clearTimeout(timeoutId);
            } else if (!timeoutId && wait !== maxWait) {
                timeoutId = setTimeout(delayed, wait);
            }
            if (leadingCall) {
                isCalled = true;
                result = func.apply(thisArg, args);
            }
            if (isCalled && !timeoutId && !maxTimeoutId) {
                args = thisArg = null;
            }
            return result;
        };
    };
}])

/**
 * Creates a function that, when executed, will only call the `func` function
 * at most once per every `wait` milliseconds. Provide an options object to
 * indicate that `func` should be invoked on the leading and/or trailing edge
 * of the `wait` timeout. Subsequent calls to the throttled function will
 * return the result of the last `func` call.
 *
 * Note: If `leading` and `trailing` options are `true` `func` will be called
 * on the trailing edge of the timeout only if the the throttled function is
 * invoked more than once during the `wait` timeout.
 *
 * @static
 * @category Functions
 * @param {Function} func The function to throttle.
 * @param {number} wait The number of milliseconds to throttle executions to.
 * @param {Object} [options] The options object.
 * @param {boolean} [options.leading=true] Specify execution on the leading edge of the timeout.
 * @param {boolean} [options.trailing=true] Specify execution on the trailing edge of the timeout.
 * @returns {Function} Returns the new throttled function.
 * @example
 *
 * // avoid excessively updating the position while scrolling
 * var throttled = _.throttle(updatePosition, 100);
 * jQuery(window).on('scroll', throttled);
 *
 * // execute `renewToken` when the click event is fired, but not more than once every 5 minutes
 * jQuery('.interactive').on('click', _.throttle(renewToken, 300000, {
 *   'trailing': false
 * }));
 */
.factory('throttle', ['debounce', 'isFunction', 'isObject', function(debounce, isFunction, isObject) {
    return function(func, wait, options) {
        var leading = true,
            trailing = true;

        if (!isFunction(func)) {
            throw new TypeError();
        }
        if (options === false) {
            leading = false;
        } else if (isObject(options)) {
            leading = 'leading' in options ? options.leading : leading;
            trailing = 'trailing' in options ? options.trailing : trailing;
        }
        options = {};
        options.leading = leading;
        options.maxWait = wait;
        options.trailing = trailing;

        return debounce(func, wait, options);
    };
}])

/**
 * @ngdoc service
 * @name functionsModule.parseHTML
 *
 * @description
 * parses a string HTML into a queriable DOM object, stripping any JavaScript from the HTML.
 *
 * @param {String} stringHTML, the string representation of the HTML to parse
 */
.factory('parseHTML', function() {
    return function(stringHTML) {
        return $.parseHTML(stringHTML);
    };
})

/**
 * @ngdoc service
 * @name functionsModule.unsafeParseHTML
 *
 * @description
 * parses a string HTML into a queriable DOM object, preserving any JavaScript present in the HTML.
 * Note - as this preserves the JavaScript present it must only be used on HTML strings originating
 * from a known safe location. Failure to do so may result in an XSS vulnerability.
 *
 * @param {String} stringHTML, the string representation of the HTML to parse
 */
.factory('unsafeParseHTML', function() {
    return function(stringHTML) {
        return $.parseHTML(stringHTML, null, true);
    };
})

/**
 * @ngdoc service
 * @name functionsModule.extractFromElement
 *
 * @description
 * parses a string HTML into a queriable DOM object
 *
 * @param {Object} parent, the DOM element from which we want to extract matching selectors
 * @param {String} extractionSelector, the jQuery selector identifying the elements to be extracted
 */
.factory('extractFromElement', function() {
    return function(parent, extractionSelector) {
        parent = $(parent);
        return parent.filter(extractionSelector).add(parent.find(extractionSelector));
    };
});

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
    .service('gatewayFactory', ['$rootScope', '$q', 'hitch', 'getOrigin', 'systemEventService', 'customTimeout', '$log', '$window', '$injector', 'WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY', 'TIMEOUT_TO_RETRY_PUBLISHING', function($rootScope, $q, hitch, getOrigin, systemEventService, customTimeout, $log, $window, $injector, WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY, TIMEOUT_TO_RETRY_PUBLISHING) {

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

    }]);

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
    .factory('gatewayProxy', ['$log', '$q', 'hitch', 'toPromise', 'gatewayFactory', function($log, $q, hitch, toPromise, gatewayFactory) {
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
    }]);

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
angular.module('FetchDataHandlerInterfaceModule', [])
    /**
     * @ngdoc service
     * @name FetchDataHandlerInterfaceModule.FetchDataHandlerInterface
     *
     * @description
     * Interface describing the contract of a fetchDataHandler fetched through dependency injection by the
     * {@link genericEditorModule.service:GenericEditor GenericEditor} to populate dropdowns
     */
    .factory('FetchDataHandlerInterface', function() {

        var FetchDataHandlerInterface = function() {};

        /**
         * @ngdoc method
         * @name FetchDataHandlerInterfaceModule.FetchDataHandlerInterface#getById
         * @methodOf FetchDataHandlerInterfaceModule.FetchDataHandlerInterface
         *
         * @description
         * will returns a promise resolving to an entity, of type defined by field, matching the given identifier
         *
         * @param {object} field the field descriptor in {@link genericEditorModule.service:GenericEditor GenericEditor}
         * @param {string} identifier the value identifying the entity to fetch
         * @returns {object} an entity
         */
        FetchDataHandlerInterface.prototype.getById = function(field, identifier) {
            throw "getById is not implemented";
        };

        /**
         * @ngdoc method
         * @name FetchDataHandlerInterfaceModule.FetchDataHandlerInterface#findBymask
         * @methodOf FetchDataHandlerInterfaceModule.FetchDataHandlerInterface
         *
         * @description
         * will returns a promise resolving to list of entities, of type defined by field, eligible for a given search mask
         *
         * @param {object} field the field descriptor in {@link genericEditorModule.service:GenericEditor GenericEditor}
         * @param {string} mask the value against witch to fetch entities.
         * @returns {Array} a list of eligible entities
         */
        FetchDataHandlerInterface.prototype.findBymask = function(field, mask) {
            throw "findBymask is not implemented";
        };

        return FetchDataHandlerInterface;
    });

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
angular.module('fetchEnumDataHandlerModule', ['FetchDataHandlerInterfaceModule', 'restServiceFactoryModule', 'resourceLocationsModule'])

.factory('fetchEnumDataHandler', ['$q', 'FetchDataHandlerInterface', 'restServiceFactory', 'isBlank', 'extend', 'ENUM_RESOURCE_URI', function($q, FetchDataHandlerInterface, restServiceFactory, isBlank, extend, ENUM_RESOURCE_URI) {

    var cache = {};

    var FetchEnumDataHandler = function() {
        this.restServiceForEnum = restServiceFactory.get(ENUM_RESOURCE_URI);
    };

    FetchEnumDataHandler = extend(FetchDataHandlerInterface, FetchEnumDataHandler);

    FetchEnumDataHandler.prototype.findByMask = function(field, search) {
        return (cache[field.cmsStructureEnumType] ? $q.when(cache[field.cmsStructureEnumType]) : this.restServiceForEnum.get({
            enumClass: field.cmsStructureEnumType
        })).then(function(response) {
            cache[field.cmsStructureEnumType] = response;
            return cache[field.cmsStructureEnumType].enums.filter(function(element) {
                return isBlank(search) || element.label.toUpperCase().indexOf(search.toUpperCase()) > -1;
            });
        });
    };

    return new FetchEnumDataHandler();
}]);

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
angular.module('fetchMediaDataHandlerModule', ['FetchDataHandlerInterfaceModule', 'experienceInterceptorModule', 'restServiceFactoryModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name fetchMediaDataHandlerModule.service:FetchMediaDataHandler
 * @description
 * implementation of {@link FetchDataHandlerInterfaceModule.FetchDataHandlerInterface FetchDataHandlerInterface} for "Media" cmsStructureType
 */
.factory('fetchMediaDataHandler', ['FetchDataHandlerInterface', 'restServiceFactory', 'hitch', 'copy', 'extend', '$q', 'CONTEXT_CATALOG', 'CONTEXT_CATALOG_VERSION', 'MEDIA_PATH', 'MEDIA_RESOURCE_URI', function(FetchDataHandlerInterface, restServiceFactory, hitch, copy, extend, $q, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, MEDIA_PATH, MEDIA_RESOURCE_URI) {

    var FetchMediaDataHandler = function() {
        this.restServiceForMediaSearch = restServiceFactory.get(MEDIA_PATH);
        this.restServiceForMediaFetch = restServiceFactory.get(MEDIA_RESOURCE_URI);
    };

    FetchMediaDataHandler = extend(FetchDataHandlerInterface, FetchMediaDataHandler);

    /**
     * @ngdoc method
     * @name fetchMediaDataHandlerModule.service:FetchMediaDataHandler#findByMask
     * @methodOf fetchMediaDataHandlerModule.service:FetchMediaDataHandler
     * @description
     * implementation of {@link FetchDataHandlerInterfaceModule.FetchDataHandlerInterface#findBymask FetchDataHandlerInterface.findBymask} 
     * Given a field descriptor from {@link genericEditorModule.service:GenericEditor GenericEditor} and a mask
     * this method will query a REST API and returned a filtered list of media assets with the following format:
     * <ul>
     * 	<li><strong>id:</strong> Identifier of the media.</li>
     * 	<li><strong>url:</strong>  URL where the media is located.</li>
     * </ul>
     * <pre>
     * [{
     *		  id: 'someid1',
     *		  url: 'fullURLToMedia1'
     *	  }, {
     *		  id: 'someid2',
     *		  url: 'fullURLToMedia2'
     *    }]
     * </pre>
     *
     */
    FetchMediaDataHandler.prototype.findByMask = function(field, search) {
        var deferred = $q.defer();
        var params = {};
        params.namedQuery = "namedQueryMediaSearchByCodeCatalogVersion";
        params.params = "code:" + search + ",catalogId:" + CONTEXT_CATALOG + ",catalogVersion:" + CONTEXT_CATALOG_VERSION;

        this.restServiceForMediaSearch.get(params).then(
            function(response) {
                deferred.resolve(response.media);
            },
            function() {
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    FetchMediaDataHandler.prototype.getById = function(field, identifier) {
        var deferred = $q.defer();
        this.restServiceForMediaFetch = restServiceFactory.get(MEDIA_RESOURCE_URI + "/" + identifier);

        this.restServiceForMediaFetch.get().then(
            function(response) {
                deferred.resolve(response);
            },
            function() {
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    return new FetchMediaDataHandler();
}]);

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
angular.module('previewDataGenericEditorResponseHandlerModule', [])
    /**
     * @ngdoc service
     * @name previewDataGenericEditorResponseHandlerModule.service:previewDataGenericEditorResponseHandler
     * @description
     * previewDataGenericEditorResponseHandler is invoked by {@link genericEditorModule.service:GenericEditor GenericEditor} to handle POST response
     * to the preview API.
     * It is responsible for retrieving ticketId and setting it into the model for use in the updateCallback
     * It must not set the smarteditComponentId into the editor so as to allow for multiple POST
     * @param {Object} editor the editor invoking the response handler
     * @param {Object} response the response from the POST call to the server
     */
    .factory('previewDataGenericEditorResponseHandler', function() {

        var PreviewDataGenericEditorResponseHandler = function(editor, response) {
            editor.pristine.ticketId = response.ticketId;
        };


        return PreviewDataGenericEditorResponseHandler;
    });

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
 * @name dateTimePickerModule
 * @description
 * # The dateTimePickerModule
 *
 * The date time picker service module is a module used for displaying a date time picker
 *
 * Use the {@link dateTimePickerModule.directive:dateTimePicker dateTimePicker} to open the date time picker.
 *
 * Once the datetimepicker is opened, its {@link dateTimePickerModule.service:dateTimePickerLocalizationService dateTimePickerLocalizationService} is used to localize the tooling.
 *
 *
 */
angular.module('dateTimePickerModule', ['languageServiceModule', 'translationServiceModule'])
    /**
     * @ngdoc directive
     * @name dateTimePickerModule.directive:dateTimePicker
     * @description
     * # The dateTimePicker
     *
     */
    .directive('dateTimePicker', ['$rootScope', 'dateTimePickerLocalizationService', function($rootScope, dateTimePickerLocalizationService) {
        return {
            templateUrl: 'web/common/services/genericEditor/dateTimePicker/dateTimePickerTemplate.html',
            restrict: 'E',
            transclude: true,
            replace: false,
            scope: {
                name: '=',
                model: '=',
                isEditable: '='
            },
            link: function($scope, elem) {
                $scope.placeholderText = 'componentform.select.date';


                if ($scope.isEditable) {
                    $(elem.children()[0])
                        .datetimepicker({
                            format: 'L LT',
                            showClear: true,
                            showClose: true,
                            useCurrent: false,
                            minDate: 0,
                            keepOpen: true
                        })
                        .on('dp.change', function() {
                            $scope.model = elem.find('input')[0].value;
                            $rootScope.$digest();
                        })
                        .on('dp.show', function() {
                            var datetimepicker = $(elem.children()[0]).datetimepicker().data("DateTimePicker");
                            dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);
                        });
                }

            }
        };
    }])
    /**
     * @ngdoc object
     * @name dateTimePickerModule.object:resolvedLocaleToMomentLocaleMap
     *
     * @description
     * Contains a map of all inconsistent locales ISOs between SmartEdit and MomentJS
     *
     */
    .constant('resolvedLocaleToMomentLocaleMap', {
        'in': 'id',
        'zh': 'zh-cn'
    })
    /**
     * @ngdoc object
     * @name dateTimePickerModule.object: tooltipsMap
     *
     * @description
     * Contains a map of all tooltips to be localized in the date time picker
     *
     */
    .constant('tooltipsMap', {
        today: 'datetimepicker.today',
        clear: 'datetimepicker.clear',
        close: 'datetimepicker.close',
        selectMonth: 'datetimepicker.selectmonth',
        prevMonth: 'datetimepicker.previousmonth',
        nextMonth: 'datetimepicker.nextmonth',
        selectYear: 'datetimepicker.selectyear',
        prevYear: 'datetimepicker.prevyear',
        nextYear: 'datetimepicker.nextyear',
        selectDecade: 'datetimepicker.selectdecade',
        prevDecade: 'datetimepicker.prevdecade',
        nextDecade: 'datetimepicker.nextdecade',
        prevCentury: 'datetimepicker.prevcentury',
        nextCentury: 'datetimepicker.nextcentury',
        pickHour: 'datetimepicker.pickhour',
        incrementHour: 'datetimepicker.incrementhour',
        decrementHour: 'datetimepicker.decrementhour',
        pickMinute: 'datetimepicker.pickminute',
        incrementMinute: 'datetimepicker.incrementminute',
        decrementMinute: 'datetimepicker.decrementminute',
        pickSecond: 'datetimepicker.picksecond',
        incrementSecond: 'datetimepicker.incrementsecond',
        decrementSecond: 'datetimepicker.decrementsecond',
        togglePeriod: 'datetimepicker.toggleperiod',
        selectTime: 'datetimepicker.selecttime'
    })
    /**
     * @ngdoc service
     * @name dateTimePickerModule.service:dateTimePickerLocalizationService
     *
     * @description
     * The dateTimePickerLocalizationService is responsible for both localizing the date time picker as well as the tooltips
     */
    .service('dateTimePickerLocalizationService', ['$translate', 'resolvedLocaleToMomentLocaleMap', 'tooltipsMap', 'languageService', function($translate, resolvedLocaleToMomentLocaleMap, tooltipsMap, languageService) {

        var convertResolvedToMomentLocale = function(resolvedLocale) {
            var conversion = resolvedLocaleToMomentLocaleMap[resolvedLocale];
            if (conversion) {
                return conversion;
            } else {
                return resolvedLocale;
            }
        };

        var getLocalizedTooltips = function() {

            var localizedTooltips = {};


            for (var index in tooltipsMap) {
                localizedTooltips[index] = $translate.instant(tooltipsMap[index]);
            }

            return localizedTooltips;

        };

        var compareTooltips = function(tooltips1, tooltips2) {
            for (var index in tooltipsMap) {
                if (tooltips1[index] !== tooltips2[index]) {
                    return false;
                }
            }
            return true;
        };

        var localizeDateTimePickerUI = function(datetimepicker) {
            languageService.getResolveLocale().then(function(language) {

                var momentLocale = convertResolvedToMomentLocale(language);

                //This if statement was added to prevent infinite recursion, at the moment it triggers twice
                //due to what seems like datetimepicker.locale(<string>) broadcasting dp.show
                if (datetimepicker.locale() !== momentLocale) {
                    datetimepicker.locale(momentLocale);
                }

            });

        };

        var localizeDateTimePickerTooltips = function(datetimepicker) {
            var currentTooltips = datetimepicker.tooltips();
            var translatedTooltips = getLocalizedTooltips();

            //This if statement was added to prevent infinite recursion, at the moment it triggers twice
            //due to what seems like datetimepicker.tooltips(<tooltips obj>) broadcasting dp.show
            if (!compareTooltips(currentTooltips, translatedTooltips)) {
                datetimepicker.tooltips(translatedTooltips);
            }

        };

        this.localizeDateTimePicker = function(datetimepicker) {
            localizeDateTimePickerUI(datetimepicker);
            localizeDateTimePickerTooltips(datetimepicker);
        };

    }]);

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
angular.module('seGenericEditorFieldErrorsModule', [])
    .controller('seGenericEditorFieldErrorsController', function() {
        this.getFilteredErrors = function() {
            return (this.field.errors || []).filter(function(error) {
                return error.language === this.qualifier && !error.format;
            }.bind(this)).map(function(error) {
                return error.message;
            });
        };
    })
    .directive('seGenericEditorFieldErrors', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/errors/seGenericEditorFieldErrorsTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seGenericEditorFieldErrorsController',
            controllerAs: 'ctrl',
            bindToController: {
                field: '=',
                qualifier: '='
            }
        };
    });

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
 * @name genericEditorModule
 */
angular.module('genericEditorModule', ['genericEditorFieldModule', 'restServiceFactoryModule', 'functionsModule', 'eventServiceModule', 'coretemplates', 'translationServiceModule', 'localizedElementModule', 'languageServiceModule', 'experienceInterceptorModule', 'dateTimePickerModule', 'seMediaFieldModule', 'fetchEnumDataHandlerModule', 'seRichTextFieldModule', 'seMediaContainerFieldModule', 'seValidationErrorParserModule'])

/**
 * @ngdoc service
 * @name genericEditorModule.service:GenericEditor
 * @description
 * The Generic Editor is a class that makes it possible for SmartEdit users (CMS managers, editors, etc.) to edit components in the SmartEdit interface.
 * The Generic Editor class is used by the {@link genericEditorModule.directive:genericEditor genericEditor} directive.
 * The genericEditor directive makes a call either to a Structure API or, if the Structure API is not available, it reads the data from a local structure to request the information that it needs to build an HTML form.
 * It then requests the component by its type and ID from the Content API. The genericEditor directive populates the form with the data that is has received.
 * The form can now be used to edit the component. The modified data is saved using the Content API.
 * <br/><br/>
 * <strong>The structure and the REST structure API</strong>.
 * <br/>
 * The constructor of the {@link genericEditorModule.service:GenericEditor GenericEditor} must be provided with the pattern of a REST Structure API, which must contain the string  ":smarteditComponentType", or with a local data structure.
 * If the pattern, Structure API, or the local structure is not provided, the Generic Editor will fail. If the Structure API is used, it must return a JSON payload that holds an array within the attributes property.
 * If the actual structure is used, it must return an array. Each entry in the array provides details about a component property to be displayed and edited. The following details are provided for each property:
 *
 *<ul>
 * <li><strong>qualifier:</strong> Name of the property.
 * <li><strong>i18nKey:</strong> Key of the property label to be translated into the requested language.
 * <li><strong>editable:</strong> Boolean that indicates if a property is editable or not. The default value is true.
 * <li><strong>localized:</strong> Boolean that indicates if a property is localized or not. The default value is false.
 * <li><strong>required:</strong> Boolean that indicates if a property is mandatory or not. The default value is false.
 * <li><strong>cmsStructureType:</strong> Value that is used to determine which form widget to display for a specified property. The possible values are:
 * 	<ul>
 * 		<li>ShortString: Displays an input field of type text.</li>
 * 		<li>LongString:  Displays a textarea field.</li>
 * 		<li>RichText:    Displays an HTML/rich text editor.</li>
 * 		<li>Boolean:     Displays a checkbox field.</li>
 * 		<li>Date:        Displays an input field with a date picker.</li>
 * 		<li>Media:       Displays a filterable dropdown list of media types.</li>
 * 		<li>Enum:		 Displays a filterable dropdown of the enum class values identified by cmsStructureEnumType property
 * </ul>
 * <li><strong>cmsStructureEnumType:</strong> the qualified name of the Enum class when cmsStructureType is "Enum"
 * </li>
 * <ul><br/>
 * The following is an example of the JSON payload that is returned by the Structure API:
 * <pre>
 * {
 *     attributes: [{
 *         cmsStructureType: "ShortString",
 *         qualifier: "someQualifier1",
 *         i18nKey: 'i18nkeyForsomeQualifier1',
 *         localized: false
 *     }, {
 *         cmsStructureType: "LongString",
 *         qualifier: "someQualifier2",
 *         i18nKey: 'i18nkeyForsomeQualifier2',
 *         localized: false
 *    }, {
 *         cmsStructureType: "RichText",
 *         qualifier: "someQualifier3",
 *         i18nKey: 'i18nkeyForsomeQualifier3',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Boolean",
 *         qualifier: "someQualifier4",
 *         i18nKey: 'i18nkeyForsomeQualifier4',
 *         localized: false
 *     }, {
 *         cmsStructureType: "Date",
 *         qualifier: "someQualifier5",
 *         i18nKey: 'i18nkeyForsomeQualifier5',
 *         localized: false
 *     }, {
 *         cmsStructureType: "Media",
 *         qualifier: "someQualifier6",
 *         i18nKey: 'i18nkeyForsomeQualifier6',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Enum",
 *         cmsStructureEnumType:'de.mypackage.Orientation'
 *         qualifier: "someQualifier7",
 *         i18nKey: 'i18nkeyForsomeQualifier7',
 *         localized: true,
 *         required: true
 *     }]
 * }
 * </pre>
 * The following is an example of the expected format of a structure:
 * <pre>
 *    [{
 *         cmsStructureType: "ShortString",
 *         qualifier: "someQualifier1",
 *         i18nKey: 'i18nkeyForsomeQualifier1',
 *         localized: false
 *     }, {
 *         cmsStructureType: "LongString",
 *         qualifier: "someQualifier2",
 *         i18nKey: 'i18nkeyForsomeQualifier2',
 *         editable: false,
 *         localized: false
 *    }, {
 *         cmsStructureType: "RichText",
 *         qualifier: "someQualifier3",
 *         i18nKey: 'i18nkeyForsomeQualifier3',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Boolean",
 *         qualifier: "someQualifier4",
 *         i18nKey: 'i18nkeyForsomeQualifier4',
 *         localized: false
 *     }, {
 *         cmsStructureType: "Date",
 *         qualifier: "someQualifier5",
 *         i18nKey: 'i18nkeyForsomeQualifier5',
 *         editable: false,
 *         localized: false
 *     }, {
 *         cmsStructureType: "Media",
 *         qualifier: "someQualifier6",
 *         i18nKey: 'i18nkeyForsomeQualifier6',
 *         localized: true,
 *         required: true
 *     }, {
 *         cmsStructureType: "Enum",
 *         cmsStructureEnumType:'de.mypackage.Orientation'
 *         qualifier: "someQualifier7",
 *         i18nKey: 'i18nkeyForsomeQualifier7',
 *         localized: true,
 *         required: true
 *     }]
 * </pre>
 * 
 * <strong>The REST CRUD API</strong>, is given to the constructor of {@link genericEditorModule.service:GenericEditor GenericEditor}.
 * The CRUD API must support GET and PUT of JSON payloads.
 * The PUT method must return the updated payload in its response. Specific to the GET and PUT, the payload must fulfill the following requirements:
 * <ul>
 * 	<li>Date types: Must be serialized as long timestamps.</li>
 * 	<li>Media types: Must be serialized as identifier strings.</li>
 * 	<li>If a cmsStructureType is localized, then we expect that the CRUD API returns a map containing the type (string or map) and the map of values, where the key is the language and the value is the content that the type returns.</li>
 * </ul>
 *
 * The following is an example of a localized payload:
 * <pre>
 * {
 *    content: {
 * 		'en': 'content in english',
 * 		'fr': 'content in french',
 * 		'hi': 'content in hindi'
 * 	  }
 * }
 * </pre>
 *
 * <br/><br/>
 *
 * If a validation error occurs, the PUT method of the REST CRUD API will return a validation error object that contains an array of validation errors. The information returned for each validation error is as follows:
 * <ul>
 * 	<li><strong>subject:</strong> The qualifier that has the error</li>
 * 	<li><strong>message:</strong> The error message to be displayed</li>
 * 	<li><strong>type:</strong> The type of error returned. This is always of the type ValidationError.</li>
 * 	<li><strong>language:</strong> The language the error needs to be associated with. If no language property is provided, a match with regular expression /(Language: \[)[a-z]{2}\]/g is attempted from the message property. As a fallback, it implies that the field is not localized.</li>
 * </ul>
 *
 * The following code is an example of an error response object:
 * <pre>
 * {
 *    errors: [{
 *		  subject: 'qualifier1',
 *		  reason: 'error message for qualifier',
 *		  type: 'ValidationError'
 *	  }, {
 *		  subject: 'qualifier2 language: [fr]',
 *		  reason: 'error message for qualifier2',
 *		  type: 'ValidationError'
 *    }, {
 *		  subject: 'qualifier3',
 *		  reason: 'error message for qualifier2',
 *		  type: 'ValidationError'
 *    }]
 * }
 * 
 * </pre>
 *
 * Whenever any sort of dropdown is used in one of the cmsStructureType widgets, it is advised using {@link genericEditorModule.service:GenericEditor#methods_refreshOptions refreshOptions method}. See this method documentation to learn more.
 *
 */
.factory('GenericEditor', ['restServiceFactory', 'languageService', 'sharedDataService', 'systemEventService', 'hitch', 'escapeHtml', 'sanitizeHTML', 'copy', 'isBlank', '$q', '$log', '$translate', '$injector', 'RESPONSE_HANDLER_SUFFIX', 'seValidationErrorParser', function(restServiceFactory, languageService, sharedDataService, systemEventService, hitch, escapeHtml, sanitizeHTML, copy, isBlank, $q, $log, $translate, $injector, RESPONSE_HANDLER_SUFFIX, seValidationErrorParser) {

    var primitiveTypes = ["Boolean", "ShortString", "LongString", "RichText", "Date", "Dropdown"];
    var LINK_TO_TOGGLE_QUALIFIER = "linkToggle";
    var GENERIC_EDITOR_UNRELATED_VALIDATION_ERRORS_EVENT = "UnrelatedValidationErrors";

    var validate = function(conf) {
        if (isBlank(conf.structure) && isBlank(conf.structureApi)) {
            throw "genericEditor.configuration.error.no.structure";
        } else if (!isBlank(conf.structure) && !isBlank(conf.structureApi)) {
            throw "genericEditor.configuration.error.2.structures";
        }
        if (isBlank(conf.contentApi) && isBlank(conf.onSubmit)) {
            throw "genericEditor.configuration.error.no.contentapi";
        }
    };

    /**
     * @constructor
     */
    var GenericEditor = function(conf) {

        validate(conf);
        this.id = conf.id;
        this.smarteditComponentType = conf.smarteditComponentType;
        this.smarteditComponentId = conf.smarteditComponentId;
        this.updateCallback = conf.updateCallback;
        this.onStructureResolved = conf.onStructureResolved;
        this.structure = conf.structure;
        if (conf.structureApi) {
            this.editorStructureService = restServiceFactory.get(conf.structureApi);
        }
        if (conf.contentApi) {
            this.editorCRUDService = restServiceFactory.get(conf.contentApi);
        }
        this.initialContent = conf.content;
        this.component = null;
        this.fields = [];
        this.languages = [];
        this.linkToStatus = null;

        if (conf.onSubmit) {
            this.onSubmit = conf.onSubmit;
        }

        /* any GenericEditor may receive notificatino from another GenericEditor that the latter received validation errors not relevant for themselves
         * In such a situation every GenericEditor will try and see if errors our relevant to them and notify editorTabset to mark them in failure if applicable
         */
        systemEventService.registerEventHandler(GENERIC_EDITOR_UNRELATED_VALIDATION_ERRORS_EVENT, hitch(this, this._handleUnrelatedValidationErrors));
    };

    GenericEditor.prototype._handleUnrelatedValidationErrors = function(key, validationErrors) {
        // A tab only cares about unrelated validation errors in other tabs if it isn't dirty.
        // Otherwise its default handling mechanism will find the errors.
        if (!this.isDirty()) {
            var hasErrors = this._displayValidationErrors(validationErrors);
            if (hasErrors) {
                systemEventService.sendAsynchEvent("EDITOR_IN_ERROR_EVENT", this.id);
            }
        }

        return $q.when();
    };

    GenericEditor.prototype._isPrimitive = function(type) {
        return primitiveTypes.indexOf(type) > -1;
    };

    GenericEditor.prototype._getSelector = function(selector) {
        return $(selector);
    };

    /**
     * @ngdoc method
     * @name genericEditorModule.service:GenericEditor#reset
     * @methodOf genericEditorModule.service:GenericEditor
     *
     * @description
     * Sets the content within the editor to its original state.
     *
     * @param {Object} componentForm The component form to be reset.
     */
    GenericEditor.prototype.reset = function(componentForm) {
        //need to empty the searches for refreshOptions to enable restting to pristine state
        this._getSelector('.ui-select-search').val('');
        this._getSelector('.ui-select-search').trigger('input');
        this.removeValidationErrors();
        this.component = copy(this.pristine);

        this.fields.forEach(function(field) {
            delete field.initiated;
        });
        if (!this.holders) {
            this.holders = this.fields.map(hitch(this, function(field) {
                return {
                    editor: this,
                    field: field
                };
            }));
        }
        if (componentForm) {
            componentForm.$setPristine();
        }
        return $q.when();
    };

    GenericEditor.prototype.removeValidationErrors = function() {
        for (var f = 0; f < this.fields.length; f++) {
            var field = this.fields[f];
            field.errors = undefined;
        }
    };

    GenericEditor.prototype.fetch = function() {
        return this.initialContent ? $q.when(this.initialContent) : (this.smarteditComponentId ? this.editorCRUDService.get({
            identifier: this.smarteditComponentId
        }) : $q.when({}));
    };

    GenericEditor.prototype.sanitizeLoad = function(response) {
        this.fields.forEach(function(field) {
            if (field.localized === true && isBlank(response[field.qualifier])) {
                response[field.qualifier] = {};
            }
        });
        return response;
    };


    GenericEditor.prototype.load = function() {
        var deferred = $q.defer();
        this.fetch().then(
            hitch(this, function(response) {
                this.pristine = this.sanitizeLoad(response);
                this.reset();

                deferred.resolve();
            }),
            function(failure) {
                $log.error("GenericEditor.load failed");
                deferred.reject();
            }
        );
        return deferred.promise;
    };

    /**
     * upon submitting, server side may have been updated,
     * since we PUT and not PATCH, we need to take latest of the fields not presented and push them back with the editable ones
     */
    GenericEditor.prototype._merge = function(refreshedComponent, modifiedComponent) {

        var merger = refreshedComponent;
        modifiedComponent = copy(modifiedComponent);

        this.fields.forEach(function(field) {
            if (field.editable === true) {
                merger[field.qualifier] = modifiedComponent[field.qualifier];
            }
        });
        if (this.linkToStatus.hasBoth()) {
            merger.external = modifiedComponent.external;
            merger.urlLink = modifiedComponent.urlLink;
        }

        return merger;
    };

    GenericEditor.prototype.getComponent = function(componentForm) {
        return this.component;
    };

    GenericEditor.prototype.sanitizePayload = function(payload, fields) {

        var CMS_STRUCTURE_TYPE = {
            SHORT_STRING: "ShortString",
            LONG_STRING: "LongString",
            LINK_TOGGLE: "LinkToggle"
        };
        var LINK_TOGGLE_QUALIFIER = "linkToggle";
        var qualifierToEscape = [];

        fields.forEach(function(field) {
            if (field.cmsStructureType === CMS_STRUCTURE_TYPE.LONG_STRING || field.cmsStructureType === CMS_STRUCTURE_TYPE.SHORT_STRING || field.cmsStructureType === CMS_STRUCTURE_TYPE.LINK_TOGGLE) {
                qualifierToEscape.push({
                    name: field.qualifier === LINK_TOGGLE_QUALIFIER ? "urlLink" : field.qualifier,
                    localized: field.localized ? true : false
                });
            }
        });

        qualifierToEscape.forEach(function(qualifier) {
            if (typeof payload[qualifier.name] !== 'undefined' && qualifier.name in payload) {
                if (qualifier.localized) {
                    var qualifierValueObject = payload[qualifier.name].value;
                    Object.keys(qualifierValueObject).forEach(function(locale) {
                        qualifierValueObject[locale] = escapeHtml(qualifierValueObject[locale]);
                    });
                } else {
                    payload[qualifier.name] = escapeHtml(payload[qualifier.name]);
                }
            }
        });

        return payload;
    };

    GenericEditor.prototype._fieldsAreUserChecked = function() {
        return this.fields.every(function(field) {
            var requiresUserCheck = false;
            for (var qualifier in field.requiresUserCheck) {
                requiresUserCheck = requiresUserCheck || field.requiresUserCheck[qualifier];
            }
            return !requiresUserCheck || field.isUserChecked;
        });
    };

    GenericEditor.prototype.onSubmit = function() {

        return this.fetch().then(hitch(this, function(refreshedComponent) {
            var payload = this._merge(refreshedComponent, this.component);

            payload = this.sanitizePayload(payload, this.fields);

            if (this.smarteditComponentId) {
                payload.identifier = this.smarteditComponentId;
            }

            var promise = this.smarteditComponentId ? this.editorCRUDService.update(payload) : this.editorCRUDService.save(payload);
            return promise.then(function(response) {
                return {
                    payload: payload,
                    response: response
                };
            });
        }));
    };

    /**
     * @ngdoc method
     * @name genericEditorModule.service:GenericEditor#submit
     * @methodOf genericEditorModule.service:GenericEditor
     *
     * @description
     * Saves the content within the form for a specified component. If there are any validation errors returned by the CRUD API after saving the content, it will display the errors.
     *
     * @param {Object} componentForm The component form to be saved.
     */
    GenericEditor.prototype.submit = function(componentForm) {
        var deferred = $q.defer();

        var cleanComponent = this.getComponent(componentForm);

        // It's necessary to remove validation errors even if the form is not dirty. This might be because of unrelated validation errors
        // triggered in other tab.
        this.removeValidationErrors();
        this.hasFrontEndValidationErrors = false;

        if (!this._fieldsAreUserChecked()) {
            deferred.reject(true); // Mark this tab as "in error" due to front-end validation. 
            this.hasFrontEndValidationErrors = true;
        } else if (this.isDirty() && componentForm.$valid) {
            /*
             * upon submitting, server side may have been updated,
             * since we PUT and not PATCH, we need to take latest of the fields not presented and send them back with the editable ones
             */
            this.onSubmit().then(hitch(this, function(submitResult) {
                //this.pristine = response; //because PUT call does nto actually return the payload
                this.pristine = copy(submitResult.payload);
                delete this.pristine.identifier;

                var responseHandlerName = this.smarteditComponentType + RESPONSE_HANDLER_SUFFIX;
                if (!this.smarteditComponentId && $injector.has(responseHandlerName)) {
                    var responseAdapter = $injector.get(responseHandlerName);
                    //whether or not smarteditComponentId is set into the editor after the POST is left to the implementation of the responseAdpater
                    responseAdapter(this, submitResult.response);
                }
                this.removeValidationErrors();

                this.reset(componentForm);
                deferred.resolve(this.pristine);
                if (this.updateCallback) {
                    this.updateCallback(this.pristine, submitResult.response);
                }
            }), hitch(this, function(failure) {
                this.removeValidationErrors();
                this._displayValidationErrors(failure.data.errors);
                //send unrelated validation errors to any other listening genericEditor when no other errors
                var unrelatedValidationErrors = this._collectUnrelatedValidationErrors(failure.data.errors);
                if (unrelatedValidationErrors.length > 0 && unrelatedValidationErrors.length === failure.data.errors.length) {
                    systemEventService.sendAsynchEvent(GENERIC_EDITOR_UNRELATED_VALIDATION_ERRORS_EVENT, unrelatedValidationErrors);
                    deferred.reject(false); // Don't mark this tab as "in error". The error should be displayed in another tab.
                } else {
                    deferred.reject(true); // Marks this tab as "in error".
                }
            }));
        } else {
            deferred.resolve(cleanComponent);
        }
        return deferred.promise;
    };

    GenericEditor.prototype._displayValidationErrors = function(errors) {
        var hasErrors = false;
        errors.filter(function(error) {
            return error.type === 'ValidationError';
        }).forEach(hitch(this, function(validationError) {

            var field = this.fields.filter(function(element) {
                return (element.qualifier === validationError.subject) ||
                    (validationError.subject === 'urlLink' && element.qualifier === LINK_TO_TOGGLE_QUALIFIER);
            })[0];

            if (field) {
                if (field.errors === undefined) {
                    field.errors = [];
                }
                hasErrors = true;

                var error = seValidationErrorParser.parse(validationError.message);
                error.language = field.localized ? error.language : field.qualifier;

                field.errors.push(error);
            }
        }));
        return hasErrors;
    };

    GenericEditor.prototype._collectUnrelatedValidationErrors = function(errors) {

        return errors.filter(hitch(this, function(error) {
            return error.type === 'ValidationError' && this.fields.filter(function(field) {
                return field.qualifier === error.subject;
            }).length === 0;
        }));
    };

    GenericEditor.prototype.fieldAdaptor = function(fields) {

        fields.forEach(hitch(this, function(field) {
            if (field.editable === undefined) {
                field.editable = true;
            }

            if (!field.postfixText) {
                var key = this.smarteditComponentType.toLowerCase() + '.' + field.qualifier.toLowerCase() + '.postfix.text';
                var translated = $translate.instant(key);
                field.postfixText = translated != key ? translated : "";
            }
        }));



        this.linkToStatus = fields.reduce(function(prev, next) {

            if (next.qualifier === "external") {
                prev.hasExternal = true;
                prev.externalI18nKey = next.i18nKey;
            }
            if (next.qualifier === "urlLink") {
                prev.hasUrlLink = true;
                prev.urlLinkI18nKey = next.i18nKey;
            }
            return prev;
        }, {
            hasExternal: false,
            hasUrlLink: false,
            hasBoth: function() {
                return this.hasExternal && this.hasUrlLink;
            },
        });



        if (this.linkToStatus.hasBoth()) {
            fields = fields.filter(function(field) {
                return field.qualifier !== "external" && field.qualifier !== "urlLink";
            });

            fields.push({
                cmsStructureType: "LinkToggle",
                qualifier: LINK_TO_TOGGLE_QUALIFIER,
                i18nKey: "editor.linkto.label",
                externalI18nKey: "editor.linkto.external.label",
                internalI18nKey: "editor.linkto.internal.label"
            });
        }

        return fields;
    };

    GenericEditor.prototype.emptyUrlLink = function() {
        this.component.urlLink = '';
    };

    /**
     * @ngdoc method
     * @name genericEditorModule.service:GenericEditor#refreshOptions
     * @methodOf genericEditorModule.service:GenericEditor
     *
     * @description
     * Is invoked by HTML field templates that update and manage dropdowns.
     *  It updates the dropdown list upon initialization (creates a list of one option) and when performing a search (returns a filtered list).
     *  To do this, the GenericEditor fetches an implementation of the  {@link FetchDataHandlerInterfaceModule.FetchDataHandlerInterface FetchDataHandlerInterface} using the following naming convention: 
     * <pre>"fetch" + cmsStructureType + "DataHandler"</pre>
     * The {@link fetchMediaDataHandlerModule.service:FetchMediaDataHandler FetchMediaDataHandler} is the only native {@link FetchDataHandlerInterfaceModule.FetchDataHandlerInterface FetchDataHandlerInterface} implementation shipped with the Generic Editor. It handles the 'Media' cmsStructureType.
     * @param {Object} field The field in the structure that requires a dropdown to be built.
     * @param {string} qualifier For a non-localized field, it is the actual field.qualifier. For a localized field, it is the ISO code of the language.
     * @param {string} search The value of the mask to filter the dropdown entries on.
     */

    GenericEditor.prototype.refreshOptions = function(field, qualifier, search) {
        var theHandlerObj = "fetch" + field.cmsStructureType + "DataHandler";
        var theIdentifier, optionsIdentifier;

        if (field.localized) {
            theIdentifier = this.component[field.qualifier][qualifier];
            optionsIdentifier = qualifier;
        } else {
            theIdentifier = this.component[field.qualifier];
            optionsIdentifier = field.qualifier;
        }

        var objHandler = $injector.get(theHandlerObj);

        field.initiated = field.initiated || [];
        field.options = field.options || {};

        if (field.cmsStructureType === 'Enum') {
            field.initiated.push(optionsIdentifier);
        }
        if (field.initiated.indexOf(optionsIdentifier) > -1) {
            if (search.length > 2 || field.cmsStructureType === 'Enum') {
                objHandler.findByMask(field, search).then(function(entities) {
                    field.options[optionsIdentifier] = entities;
                });
            }
        } else if (theIdentifier) {
            objHandler.getById(field, theIdentifier).then(hitch(field, function(entity) {
                field.options[optionsIdentifier] = [entity];
                field.initiated.push(optionsIdentifier);
            }));
        } else {
            field.initiated.push(optionsIdentifier);
        }
    };

    GenericEditor.prototype._buildComparable = function(obj) {
        if (!obj) return obj;
        var comparable = {};
        var fields = this.fields;
        angular.forEach(obj, function(value, key) {
            var field = fields.filter(function(field) {
                return field.qualifier === key && field.cmsStructureType === 'RichText';
            });

            if (field.length == 1) {
                comparable[key] = sanitizeHTML(value, field[0].localized);
            } else {
                comparable[key] = value;
            }
        });
        return comparable;
    };



    /**
     * @ngdoc method
     * @name genericEditorModule.service:GenericEditor#isDirty
     * @methodOf genericEditorModule.service:GenericEditor
     *
     * @description
     * A predicate function that returns true if the editor is in dirty state or false if it not.
     *  The state of the editor is determined by comparing the current state of the component with the state of the component when it was pristine.
     *
     * @return {Boolean} An indicator if the editor is in dirty state or not.
     */
    GenericEditor.prototype.isDirty = function() {
        //try to get away from angular.equals
        return !angular.equals(this._buildComparable(this.pristine), this._buildComparable(this.component));
    };

    GenericEditor.prototype.init = function() {
        var deferred = $q.defer();

        var structurePromise = this.editorStructureService ? this.editorStructureService.get({
            smarteditComponentType: this.smarteditComponentType
        }) : $q.when({
            attributes: this.structure
        });

        structurePromise.then(
            function(structure) {
                if (this.onStructureResolved) {
                    this.onStructureResolved(this.id, structure);
                }

                sharedDataService.get('experience').then(hitch(this, function(experience) {
                    languageService.getLanguagesForSite(experience.siteDescriptor.uid).then(hitch(this, function(languages) {
                        this.languages = languages;
                        this.fields = this.fieldAdaptor(structure.attributes);

                        this.load().then(function() {
                            deferred.resolve();
                        }, function(failure) {
                            deferred.reject();
                        });
                    }), function() {
                        $log.error("GenericEditor failed to fetch storefront languages");
                        deferred.reject();
                    });
                }));
            }.bind(this),
            function() {
                $log.error("GenericEditor.init failed");
                deferred.reject();
            });

        return deferred.promise;
    };

    return GenericEditor;

}])

/**
 * @ngdoc directive
 * @name genericEditorModule.directive:genericEditor
 * @scope
 * @restrict E
 * @element generic-editor
 *
 * @description
 * Directive responsible for generating custom HTML CRUD form for any smarteditComponent type.
 *
 * The link function has a method that creates a new instance for the {@link genericEditorModule.service:GenericEditor GenericEditor}
 * and sets the scope of smarteditComponentId and smarteditComponentType to a value that has been extracted from the original DOM element in the storefront.
 *
 * @param {String} smartedit-component-type The SmartEdit component type that is to be created, read, updated, or deleted.
 * @param {String} smartedit-component-id The identifier of the SmartEdit component that is to be created, read, updated, or deleted.
 * @param {String} structureApi An optional parameter. The data binding to a REST Structure API that fulfills the contract described in the  {@link genericEditorModule.service:GenericEditor GenericEditor} service. Only the Structure API or the local structure must be set.
 * @param {String} structure An optional parameter. he data binding to a REST Structure API that fulfills the contract described in the {@link genericEditorModule.service:GenericEditor GenericEditor} service. Only the Structure API or the local structure must be set.
 * @param {String} contentApi The REST API used to create, read, update, or delete content.
 * @param {Function} submit An optional parameter. It exposes the inner submit function to the invoker scope. If this parameter is set, the directive will not display an inner submit button.
 * @param {Function} reset An optional parameter. It exposes the inner reset function to the invoker scope. If this parameter is set, the directive will not display an inner cancel button.
 */
.directive('genericEditor', ['GenericEditor', 'isBlank', '$log', '$q', function(GenericEditor, isBlank, $log, $q) {
        return {
            templateUrl: 'web/common/services/genericEditor/genericEditorTemplate.html',
            restrict: 'E',
            transclude: true,
            replace: false,
            scope: {
                id: '=',
                smarteditComponentId: '=',
                smarteditComponentType: '=',
                structureApi: '=',
                structure: '=',
                contentApi: '=',
                content: '=',
                submit: '=',
                getComponent: '=',
                reset: '=',
                isDirty: '=',
                updateCallback: '=',
                onStructureResolved: '='
            },
            link: function($scope, element, attrs) {

                $scope.editor = new GenericEditor({
                    id: $scope.id,
                    smarteditComponentType: $scope.smarteditComponentType,
                    smarteditComponentId: $scope.smarteditComponentId,
                    structureApi: $scope.structureApi,
                    structure: $scope.structure,
                    contentApi: $scope.contentApi,
                    updateCallback: $scope.updateCallback,
                    onStructureResolved: $scope.onStructureResolved,
                    content: $scope.content
                });

                $scope.editor.init();

                $scope.editor.showReset = isBlank(attrs.reset);
                $scope.editor.showSubmit = isBlank(attrs.submit);

                $scope.submitButtonText = 'componentform.actions.submit';
                $scope.cancelButtonText = 'componentform.actions.cancel';

                $scope.reset = function() {
                    return $scope.editor.reset($scope.componentForm);
                };

                $scope.submit = function() {
                    return $scope.editor.submit($scope.componentForm);
                };

                $scope.getComponent = function() {
                    return $scope.editor.getComponent($scope.componentForm);
                };

                $scope.isDirty = function() {
                    return $scope.editor.isDirty();
                };

                // Prevent enter key from triggering form submit
                $(element.find('.no-enter-submit')[0]).bind('keypress', function(key) {
                    if (key.keyCode === 13) {
                        return false;
                    }
                });


                //#################################################################################################################

                // Disable weekend selection
                $scope.editor.disabled = function(date, mode) {
                    return (mode === 'day' && (date.getDay() === 0 || date.getDay() === 6));
                };

                $scope.editor.dateOptions = {
                    formatYear: 'yy',
                    startingDay: 1
                };

                $scope.editor.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate', 'MMM dd, yyyy hh:mm'];
                $scope.editor.format = $scope.editor.formats[4];

            }
        };
    }])
    .constant('RESPONSE_HANDLER_SUFFIX', 'GenericEditorResponseHandler');

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
angular.module('genericEditorFieldModule', ['translationServiceModule', 'ui.bootstrap', 'ui.select', 'ngSanitize', 'seGenericEditorFieldErrorsModule'])
    .directive('genericEditorField', function() {

        return {
            templateUrl: 'web/common/services/genericEditor/genericEditorFieldTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: false,
            scope: {
                editor: '=',
                field: '=',
                model: '=',
                qualifier: '='
            },

        };
    });

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
angular.module('seErrorsListModule', [])
    .controller('seErrorsListController', function() {
        this.getSubjectErrors = function() {
            return (this.subject ? this.errors.filter(function(error) {
                return error.subject === this.subject;
            }.bind(this)) : this.errors);
        };
    })
    .directive('seErrorsList', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/errorsList/seErrorsListTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seErrorsListController',
            controllerAs: 'ctrl',
            bindToController: {
                errors: '=',
                subject: '='
            }
        };
    });

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
angular.module('seFileSelectorModule', [])
    .controller('seFileSelectorController', function() {
        this.imageRoot = this.imageRoot || "";
        this.disabled = this.disabled || false;
        this.customClass = this.customClass || "";

        this.buildAcceptedFileTypesList = function() {
            return this.acceptedFileTypes.map(function(fileType) {
                return '.' + fileType;
            }).join(',');
        };
    })
    .directive('seFileSelector', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/fileSelector/seFileSelectorTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seFileSelectorController',
            controllerAs: 'ctrl',
            bindToController: {
                imageRoot: '=',
                uploadIcon: '=',
                labelI18nKey: '=',
                acceptedFileTypes: '=',
                customClass: '=',
                disabled: '=',
                onFileSelect: '&'
            },
            link: function($scope, $element) {
                $element.find('input').on('change', function(event) {
                    $scope.$apply(function() {
                        $scope.ctrl.onFileSelect({
                            files: event.target.files
                        });
                        var input = $element.find('input');
                        input.replaceWith(input.clone(true));
                    });
                });
            }
        };
    });

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
angular.module('seMediaAdvancedPropertiesModule', ['ui.bootstrap'])
    .constant('seMediaAdvancedPropertiesConstants', {
        CONTENT_URL: 'web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesContentTemplate.html',
        I18N_KEYS: {
            DESCRIPTION: 'media.advanced.information.description',
            CODE: 'media.advanced.information.code',
            ALT_TEXT: 'media.advanced.information.alt.text',
            ADVANCED_INFORMATION: 'media.advanced.information',
            INFORMATION: 'media.information'
        }
    })
    .controller('seMediaAdvancedPropertiesController', ['seMediaAdvancedPropertiesConstants', function(seMediaAdvancedPropertiesConstants) {
        this.i18nKeys = seMediaAdvancedPropertiesConstants.I18N_KEYS;
        this.contentUrl = seMediaAdvancedPropertiesConstants.CONTENT_URL;
    }])
    .directive('seMediaAdvancedProperties', function() {
        return {
            restrict: 'E',
            scope: {},
            bindToController: {
                code: '=',
                advInfoIcon: '=',
                description: '=',
                altText: '='
            },
            controller: 'seMediaAdvancedPropertiesController',
            controllerAs: 'ctrl',
            templateUrl: 'web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesTemplate.html'
        };
    })
    .directive('seMediaAdvancedPropertiesCondensed', function() {
        return {
            restrict: 'E',
            scope: {},
            bindToController: {
                code: '=',
                advInfoIcon: '=',
                description: '=',
                altText: '='
            },
            controller: 'seMediaAdvancedPropertiesController',
            controllerAs: 'ctrl',
            templateUrl: 'web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesCondensedTemplate.html'
        };
    });

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
angular.module('seMediaContainerFieldModule', [
        'seMediaUploadFormModule',
        'seMediaFormatModule',
        'seErrorsListModule',
        'seFileValidationServiceModule'
    ])
    .controller('seMediaContainerFieldController', ['seFileValidationService', function(seFileValidationService) {
        this.image = {};
        this.fileErrors = [];

        this.fileSelected = function(files, format) {
            var previousFormat = this.image.format;
            this.resetImage();

            if (files.length === 1) {
                seFileValidationService.validate(files[0], this.fileErrors).then(function() {
                    this.image = {
                        file: files[0],
                        format: format || previousFormat
                    };
                }.bind(this));
            }
        };

        this.resetImage = function() {
            this.fileErrors = [];
            this.image = {};
        };

        this.imageUploaded = function(code) {
            this.model[this.qualifier][this.image.format] = code;
            this.resetImage();
        };

        this.imageDeleted = function(format) {
            this.model[this.qualifier][format] = {};
        };

        this.isFormatUnderEdit = function(format) {
            return format === this.image.format;
        };
    }])
    .directive('seMediaContainerField', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaContainerField/seMediaContainerFieldTemplate.html',
            restrict: 'E',
            controller: 'seMediaContainerFieldController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '='
            }
        };
    });

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
angular.module('seMediaFieldModule', [
        'seMediaSelectorModule',
        'seMediaUploadFormModule',
        'seFileValidationServiceModule',
        'seErrorsListModule'
    ])
    .factory('seMediaFieldConstants', ['SMARTEDIT_ROOT', function(SMARTEDIT_ROOT) {
        return {
            I18N_KEYS: {
                UPLOAD_IMAGE_TO_LIBRARY: 'upload.image.to.library'
            },
            UPLOAD_ICON_URL: 'static-resources/images/upload_image.png',
            DELETE_ICON_URL: 'static-resources/images/remove_image_small.png',
            REPLACE_ICON_URL: 'static-resources/images/replace_image_small.png',
            ADV_INFO_ICON_URL: 'static-resources/images/info_image_small.png'
        };
    }])
    .controller('seMediaFieldController', ['seMediaFieldConstants', 'seFileValidationServiceConstants', 'seFileValidationService', function(seMediaFieldConstants, seFileValidationServiceConstants, seFileValidationService) {
        this.i18nKeys = seMediaFieldConstants.I18N_KEYS;
        this.acceptedFileTypes = seFileValidationServiceConstants.ACCEPTED_FILE_TYPES;
        this.uploadIconUrl = seMediaFieldConstants.UPLOAD_ICON_URL;
        this.deleteIconUrl = seMediaFieldConstants.DELETE_ICON_URL;
        this.replaceIconUrl = seMediaFieldConstants.REPLACE_ICON_URL;
        this.advInfoIconUrl = seMediaFieldConstants.ADV_INFO_ICON_URL;
        this.image = {};
        this.fileErrors = [];

        this.fileSelected = function(files, format) {
            this.resetImage();
            if (files.length === 1) {
                seFileValidationService.validate(files[0], this.fileErrors).then(function() {
                    this.image = {
                        file: files[0],
                        format: format || this.image.format
                    };
                }.bind(this));
            }
        };

        this.resetImage = function() {
            this.fileErrors = [];
            this.image = {};
        };

        this.imageUploaded = function(code) {
            this.resetImage();
            this.model[this.qualifier] = code;
            if (this.field.initiated) {
                this.field.initiated.length = 0;
            }
            this.editor.refreshOptions(this.field, this.qualifier, '');
        };
    }])
    .directive('seMediaField', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaField/seMediaFieldTemplate.html',
            restrict: 'E',
            controller: 'seMediaFieldController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '='
            }
        };
    });

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
angular.module('seMediaFormatModule', ['seMediaServiceModule', 'seFileSelectorModule', 'seFileValidationServiceModule'])
    .constant('seMediaFormatConstants', {
        I18N_KEYS: {
            UPLOAD: 'media.format.upload',
            REPLACE: 'media.format.replace',
            UNDER_EDIT: 'media.format.under.edit',
            REMOVE: 'media.format.remove'
        },
        UPLOAD_ICON_URL: 'static-resources/images/upload_image.png',
        UPLOAD_ICON_DIS_URL: 'static-resources/images/upload_image_disabled.png',
        DELETE_ICON_URL: 'static-resources/images/remove_image_small.png',
        REPLACE_ICON_URL: 'static-resources/images/replace_image_small.png',
        ADV_INFO_ICON_URL: 'static-resources/images/info_image_small.png'
    })
    .controller('seMediaFormatController', ['seMediaService', 'seMediaFormatConstants', 'seFileValidationServiceConstants', '$scope', function(seMediaService, seMediaFormatConstants, seFileValidationServiceConstants, $scope) {
        this.i18nKeys = seMediaFormatConstants.I18N_KEYS;
        this.acceptedFileTypes = seFileValidationServiceConstants.ACCEPTED_FILE_TYPES;

        this.uploadIconUrl = seMediaFormatConstants.UPLOAD_ICON_URL;
        this.uploadIconDisabledUrl = seMediaFormatConstants.UPLOAD_ICON_DIS_URL;

        this.deleteIconUrl = seMediaFormatConstants.DELETE_ICON_URL;
        this.replaceIconUrl = seMediaFormatConstants.REPLACE_ICON_URL;
        this.advInfoIconUrl = seMediaFormatConstants.ADV_INFO_ICON_URL;

        this.fetchMediaForCode = function() {
            seMediaService.getMediaByCode(this.mediaCode).then(function(val) {
                this.media = val;
            }.bind(this));
        };

        this.isMediaCodeValid = function() {
            return this.mediaCode && typeof this.mediaCode === 'string';
        };

        this.getErrors = function() {
            return (this.errors || []).filter(function(error) {
                return error.format === this.mediaFormat;
            }.bind(this)).map(function(error) {
                return error.message;
            });
        };

        if (this.isMediaCodeValid()) {
            this.fetchMediaForCode();
        }

        $scope.$watch(function() {
            return this.mediaCode;
        }.bind(this), function(mediaCode, oldMediaCode) {
            if (mediaCode && typeof mediaCode === 'string') {
                if (mediaCode !== oldMediaCode) {
                    this.fetchMediaForCode();
                }
            } else {
                this.media = {};
            }
        }.bind(this));
    }])
    .directive('seMediaFormat', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaFormat/seMediaFormatTemplate.html',
            restrict: 'E',
            controller: 'seMediaFormatController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                mediaCode: '=',
                mediaFormat: '=',
                isUnderEdit: '=',
                errors: '=',
                onFileSelect: '&',
                onDelete: '&'
            }
        };
    });

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
angular.module('seMediaPreviewModule', [])
    .constant('seMediaPreviewConstants', {
        CONTENT_URL: 'web/common/services/genericEditor/media/components/mediaPreview/seMediaPreviewContentTemplate.html',
        I18N_KEYS: {
            PREVIEW: 'media.preview'
        }
    })
    .controller('seMediaPreviewController', ['seMediaPreviewConstants', function(seMediaPreviewConstants) {
        this.i18nKeys = seMediaPreviewConstants.I18N_KEYS;
        this.contentUrl = seMediaPreviewConstants.CONTENT_URL;
    }])
    .directive('seMediaPreview', function() {
        return {
            restrict: 'E',
            scope: {},
            bindToController: {
                imageUrl: '='
            },
            controller: 'seMediaPreviewController',
            controllerAs: 'ctrl',
            templateUrl: 'web/common/services/genericEditor/media/components/mediaPreview/seMediaPreviewTemplate.html'
        };
    });

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
angular.module('seMediaSelectorModule', ['seMediaAdvancedPropertiesModule', 'seMediaPreviewModule'])
    .controller('seMediaSelectorController', function() {
        this.onDelete = function($select, $event) {
            $event.preventDefault();
            $event.stopPropagation();
            $select.selected = undefined;
        };
    })
    .directive('seMediaSelector', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaSelector/seMediaSelectorTemplate.html',
            restrict: 'E',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '=',
                deleteIcon: '=',
                replaceIcon: '=',
                advInfoIcon: '='
            },
            controller: 'seMediaSelectorController',
            controllerAs: 'ctrl'
        };
    });

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
angular.module('seMediaUploadFieldModule', [])
    .controller('seMediaUploadFieldController', function() {
        this.displayImage = false;
    })
    .directive('seMediaUploadField', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFieldTemplate.html',
            restrict: 'E',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                error: '='
            },
            controller: 'seMediaUploadFieldController',
            controllerAs: 'ctrl',
            link: function(scope, element, attrs, ctrl) {
                element.bind("mouseover", function(e) {
                    ctrl.displayImage = true;
                    scope.$digest();
                });
                element.bind("mouseout", function(e) {
                    ctrl.displayImage = false;
                    scope.$digest();
                });
            }
        };
    });

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
angular.module('seMediaUploadFormModule', [
        'seFileValidationServiceModule',
        'seMediaServiceModule',
        'seObjectValidatorFactoryModule',
        'seBackendValidationHandlerModule',
        'seMediaUploadFieldModule',
        'functionsModule'
    ])
    .constant('seMediaUploadFormConstants', {
        MAX_FILENAME_CHARACTERS_DISPLAYED: 15,
        I18N_KEYS: {
            UPLOAD_IMAGE_CANCEL: 'upload.image.cancel',
            UPLOAD_IMAGE_SUBMIT: 'upload.image.submit',
            UPLOAD_IMAGE_REPLACE: 'upload.image.replace',
            UPLOAD_IMAGE_TO_LIBRARY: 'upload.image.to.library',
            DESCRIPTION: 'uploaded.image.description',
            CODE: 'uploaded.image.code',
            ALT_TEXT: 'uploaded.image.alt.text',
            UPLOADING: 'uploaded.is.uploading',
            CODE_IS_REQUIRED: 'uploaded.image.code.is.required'
        }
    })
    .factory('seMediaUploadFormValidators', ['seMediaUploadFormConstants', function(seMediaUploadFormConstants) {
        return [{
            subject: 'code',
            message: seMediaUploadFormConstants.I18N_KEYS.CODE_IS_REQUIRED,
            validate: function(code) {
                return !!code;
            }
        }];
    }])
    .controller('seMediaUploadFormController', ['seFileValidationServiceConstants', 'seMediaService', 'seMediaUploadFormConstants', 'seObjectValidatorFactory', 'seMediaUploadFormValidators', 'seBackendValidationHandler', 'escapeHtml', '$scope', function(seFileValidationServiceConstants, seMediaService, seMediaUploadFormConstants, seObjectValidatorFactory, seMediaUploadFormValidators, seBackendValidationHandler, escapeHtml, $scope) {
        this.i18nKeys = seMediaUploadFormConstants.I18N_KEYS;
        this.acceptedFileTypes = seFileValidationServiceConstants.ACCEPTED_FILE_TYPES;

        this.imageParameters = {};
        this.isUploading = false;
        this.fieldErrors = [];
        this.showEditIcon = false;

        // TODO replace with this.$onChanges in Angular 1.5
        $scope.$watch(function() {
            return this.image;
        }.bind(this), function() {
            if (this.image && this.image.file) {
                this.imageParameters = {
                    code: this.image.file.name,
                    description: this.image.file.name,
                    altText: this.image.file.name
                };
            }
        }.bind(this));

        this.onCancel = function() {
            this.imageParameters = {};
            this.fieldErrors = [];
            this.isUploading = false;
            this.onCancelCallback();
        };

        this.onImageUploadSuccess = function(response) {
            this.imageParameters = {};
            this.fieldErrors = [];
            this.isUploading = false;
            this.onUploadCallback({
                code: response.code
            });
        };

        this.onImageUploadFail = function(response) {
            this.isUploading = false;
            seBackendValidationHandler.handleResponse(response, this.fieldErrors);
        };

        this.onMediaUploadSubmit = function() {
            this.fieldErrors = [];
            var validator = seObjectValidatorFactory.build(seMediaUploadFormValidators);
            if (validator.validate(this.imageParameters, this.fieldErrors)) {
                this.isUploading = true;

                seMediaService.uploadMedia({
                    file: this.image.file,
                    code: escapeHtml(this.imageParameters.code),
                    description: escapeHtml(this.imageParameters.description),
                    altText: escapeHtml(this.imageParameters.altText)
                }).then(this.onImageUploadSuccess.bind(this), this.onImageUploadFail.bind(this));
            }
        };

        this.getErrorsForField = function(field) {
            return this.fieldErrors.filter(function(error) {
                return error.subject === field;
            }).map(function(error) {
                return error.message;
            });
        };

        this.hasError = function(field) {
            return this.fieldErrors.some(function(error) {
                return error.subject === field;
            });
        };

        this.getTruncatedName = function() {
            var truncatedName = this.image && this.image.file && this.image.file.name || '';
            if (truncatedName.length > seMediaUploadFormConstants.MAX_FILENAME_CHARACTERS_DISPLAYED) {
                truncatedName = truncatedName.substring(0, seMediaUploadFormConstants.MAX_FILENAME_CHARACTERS_DISPLAYED);
                truncatedName += '...';
            }
            return truncatedName;
        };
    }])
    .directive('seMediaUploadForm', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFormTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seMediaUploadFormController',
            controllerAs: 'ctrl',
            bindToController: {
                field: '=',
                image: '=',
                onUploadCallback: '&',
                onCancelCallback: '&',
                onSelectCallback: '&'
            }
        };
    });

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
 * @name seBackendValidationHandlerModule
 * @description
 * This module provides the seBackendValidationHandler service, which handles standard OCC validation errors received
 * from the backend.
 */
angular.module('seBackendValidationHandlerModule', [])

/**
 * @ngdoc service
 * @name seBackendValidationHandlerModule.seBackendValidationHandler
 * @description
 * The seBackendValidationHandler service handles validation errors received from the backend.
 */
.factory('seBackendValidationHandler', function() {

    /**
     * @ngdoc method
     * @name seBackendValidationHandlerModule.seBackendValidationHandler.handleResponse
     * @methodOf seBackendValidationHandlerModule.seBackendValidationHandler
     * @description
     * Extracts validation errors from the provided response and appends them to a specified contextual errors list.
     *
     * The expected error response from the backend matches the contract of the following response example:
     *
     * <pre>
     * var response = {
     *     data: {
     *         errors: [{
     *             type: 'ValidationError',
     *             subject: 'mySubject',
     *             message: 'Some validation exception occurred'
     *         }, {
     *             type: 'SomeOtherError',
     *             subject: 'mySubject'
     *             message: 'Some other exception occurred'
     *         }]
     *     }
     * }
     * </pre>
     *
     * Example of use:
     * <pre>
     * var errorsContext = [];
     * seBackendValidationHandler.handleResponse(response, errorsContext);
     * </pre>
     *
     * The resulting errorsContext would be as follows:
     * <pre>
     * [{
     *     subject: 'mySubject',
     *     message: 'Some validation exception occurred'
     * }]
     * </pre>
     *
     * @param {Object} response A response consisting of a list of errors; for details of the expected format, see the
     * example above.
     * @param {Array} errorsContext An array that all validation errors are appended to. It is an output parameter.
     * @returns {Array} The error context list originally provided, or a new list, appended with the validation errors
     */
    var handleResponse = function(response, errorsContext) {
        errorsContext = errorsContext || [];
        if (response && response.data && response.data.errors) {
            response.data.errors.filter(function(error) {
                return error.type === 'ValidationError';
            }).forEach(function(validationError) {
                var subject = validationError.subject;
                if (subject) {
                    errorsContext.push({
                        'subject': subject,
                        'message': validationError.message
                    });
                }
            });
        }
        return errorsContext;
    };

    return {
        handleResponse: handleResponse
    };
});

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
angular.module('seFileMimeTypeServiceModule', [])
    .constant('seFileMimeTypeServiceConstants', {
        VALID_IMAGE_MIME_TYPE_CODES: ['FFD8FFDB', 'FFD8FFE0', 'FFD8FFE1', '474946383761', '424D', '49492A00', '4D4D002A', '89504E470D0A1A0A']
    })
    .factory('seFileReader', function() {
        var read = function(file, config) {
            var fileReader = new FileReader();

            config = config || {};
            fileReader.onloadend = config.onLoadEnd;
            fileReader.onerror = config.onError;

            fileReader.readAsArrayBuffer(file);
            return fileReader;
        };

        return {
            read: read
        };
    })
    .factory('seFileMimeTypeService', ['seFileMimeTypeServiceConstants', 'seFileReader', '$q', function(seFileMimeTypeServiceConstants, seFileReader, $q) {
        var _validateMimeTypeFromFile = function(loadedFile) {
            var fileAsBytes = (new Uint8Array(loadedFile)).subarray(0, 8);
            var header = fileAsBytes.reduce(function(header, byte) {
                var byteAsStr = byte.toString(16);
                if (byteAsStr.length === 1) {
                    byteAsStr = '0' + byteAsStr;
                }
                header += byteAsStr;
                return header;
            }, '');

            return seFileMimeTypeServiceConstants.VALID_IMAGE_MIME_TYPE_CODES.some(function(mimeTypeCode) {
                return header.toLowerCase().indexOf(mimeTypeCode.toLowerCase()) === 0;
            });
        };

        var isFileMimeTypeValid = function(file) {
            var deferred = $q.defer();
            seFileReader.read(file, {
                onLoadEnd: function(e) {
                    if (_validateMimeTypeFromFile(e.target.result)) {
                        deferred.resolve();
                    } else {
                        deferred.reject();
                    }
                },
                onError: function() {
                    deferred.reject();
                }
            });
            return deferred.promise;
        };

        return {
            isFileMimeTypeValid: isFileMimeTypeValid
        };
    }]);

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
 * @name seFileValidationServiceModule
 * @description
 * This module provides the seFileValidationService service, which validates if a specified file meets the required file
 * type and file size constraints of SAP Hybris Commerce.
 */
angular.module('seFileValidationServiceModule', ['seObjectValidatorFactoryModule', 'seFileMimeTypeServiceModule'])

/**
 * @ngdoc object
 * @name seFileValidationServiceModule.seFileValidationServiceConstants
 * @description
 * The constants provided by the file validation service.
 *
 * <b>ACCEPTED_FILE_TYPES</b>: A list of file types supported by the platform.
 * <b>MAX_FILE_SIZE_IN_BYTES</b>: The maximum size, in bytes, for an uploaded file.
 * <b>I18N_KEYS</b>: A map of all the internationalization keys used by the file validation service.
 */
.constant('seFileValidationServiceConstants', {
    ACCEPTED_FILE_TYPES: ['jpeg', 'jpg', 'gif', 'bmp', 'tiff', 'tif', 'png'],
    MAX_FILE_SIZE_IN_BYTES: 20 * 1024 * 1024,
    I18N_KEYS: {
        FILE_TYPE_INVALID: 'upload.file.type.invalid',
        FILE_SIZE_INVALID: 'upload.file.size.invalid'
    }
})

/**
 * @ngdoc object
 * @name seFileValidationServiceModule.seFileObjectValidators
 * @description
 * A list of file validators, that includes a validator for file-size constraints and a validator for file-type
 * constraints.
 */
.factory('seFileObjectValidators', ['seFileValidationServiceConstants', function(seFileValidationServiceConstants) {
    return [{
        subject: 'size',
        message: seFileValidationServiceConstants.I18N_KEYS.FILE_SIZE_INVALID,
        validate: function(size) {
            return size <= seFileValidationServiceConstants.MAX_FILE_SIZE_IN_BYTES;
        }
    }];
}])

/**
 * @ngdoc service
 * @name seFileValidationServiceModule.seFileValidationService
 * @description
 * The seFileValidationService validates that the file provided is of a specified file type and that the file does not
 * exceed the maxium size limit for the file's file type.
 */
.factory('seFileValidationService', ['seObjectValidatorFactory', 'seFileObjectValidators', 'seFileValidationServiceConstants', 'seFileMimeTypeService', '$q', function(seObjectValidatorFactory, seFileObjectValidators, seFileValidationServiceConstants, seFileMimeTypeService, $q) {
    /**
     * @ngdoc method
     * @name seFileValidationServiceModule.seFileValidationService.buildAcceptedFileTypesList
     * @methodOf seFileValidationServiceModule.seFileValidationService
     * @description
     * Transforms the supported file types provided by the seFileValidationServiceConstants service into a comma-
     * separated list of file type extensions.
     *
     * @returns {String} A comma-separated list of supported file type extensions
     */
    var buildAcceptedFileTypesList = function() {
        return seFileValidationServiceConstants.ACCEPTED_FILE_TYPES.map(function(fileType) {
            return '.' + fileType;
        }).join(',');
    };

    /**
     * @ngdoc method
     * @name seFileValidationServiceModule.seFileValidationService.validate
     * @methodOf seFileValidationServiceModule.seFileValidationService
     * @description
     * Validates the specified file object using validator provided by the {@link
     * seFileValidationServiceModule.seFileObjectValidators seFileObjectValidators} and the file header validator
     * provided by the {@link seFileMimeTypeServiceModule.seFileMimeTypeService seFileMimeTypeService}. It appends the
     * errors to the error context array provided or it creates a new error context array.
     *
     * @param {File} file The web API file object to be validated.
     * @param {Array} context The contextual error array to append the errors to. It is an output parameter.
     * @returns {Promise} A promise that resolves if the file is valid or a list of errors if the promise is rejected.
     */
    var validate = function(file, errorsContext) {
        seObjectValidatorFactory.build(seFileObjectValidators).validate(file, errorsContext);
        return seFileMimeTypeService.isFileMimeTypeValid(file).then(function() {
            return errorsContext.length === 0 ? $q.when() : $q.reject(errorsContext);
        }, function() {
            errorsContext.push({
                subject: 'type',
                message: seFileValidationServiceConstants.I18N_KEYS.FILE_TYPE_INVALID
            });
            return $q.reject(errorsContext);
        });
    };

    return {
        buildAcceptedFileTypesList: buildAcceptedFileTypesList,
        validate: validate
    };
}]);

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
 * @name seMediaServiceModule
 * @description
 * The media service module provides a service to create an image file for a catalog through AJAX calls. This module
 * uses a dedicated transformed $resource that posts a multipart form data in the request.
 */
angular.module('seMediaServiceModule', ['resourceLocationsModule', 'experienceInterceptorModule', 'ngResource'])

/**
 * @ngdoc service
 * @name seMediaServiceModule.seMediaResource
 * @description
 * A {@link https://docs.angularjs.org/api/ngResource/service/$resource $resource} that makes REST calls to the default
 * CMS catalog media API. It supports HTTP GET and POST methods against this API. The GET method is used to retrieve a collection of media.
 *
 * The POST methods transform the POJO into a {@link https://developer.mozilla.org/en/docs/Web/API/FormData FormData}
 * object before the request is made to the API. This transformation is required for file uploads that use the
 * Content-Type 'multipart/form-data'.
 */
.factory('seMediaResource', ['$resource', 'MEDIA_RESOURCE_URI', function($resource, MEDIA_RESOURCE_URI) {
    return $resource(MEDIA_RESOURCE_URI, {}, {
        get: {
            method: 'GET',
            cache: true,
            headers: {}
        },
        save: {
            method: 'POST',
            headers: {
                'Content-Type': undefined,
                enctype: 'multipart/form-data',
                'x-requested-with': 'Angular'
            },
            transformRequest: function(data) {
                var formData = new FormData();
                angular.forEach(data, function(value, key) {
                    formData.append(key, value);
                });
                return formData;
            }
        }
    });
}])

/**
 * @ngdoc service
 * @name seMediaServiceModule.seMediaResourceService
 * @description
 * This service provides an interface to the {@link https://docs.angularjs.org/api/ngResource/service/$resource $resource} that makes REST 
 * calls to the default CMS catalog media API. It supports HTTP GET method returning against this API. 
 * The GET method is used to retrieve a single media.
 */
.factory('seMediaResourceService', ['$resource', 'MEDIA_RESOURCE_URI', function($resource, MEDIA_RESOURCE_URI) {

    var getMediaByCode = function(mediaCode) {
        return $resource(MEDIA_RESOURCE_URI + "/" + mediaCode, {}, {
            get: {
                method: 'GET',
                cache: true,
                headers: {}
            }
        });
    };

    return {
        getMediaByCode: getMediaByCode
    };
}])

/**
 * @ngdoc service
 * @name seMediaServiceModule.seMediaService
 * @description
 * This service provides an interface to the {@link https://docs.angularjs.org/api/ngResource/service/$resource
 * $resource} provided by the {@link seMediaServiceModule.seMediaResource seMediaResource} service and 
 * the {@link seMediaServiceModule.seMediaResourceService seMediaResourceService} service. It provides the
 * functionality to upload images and to fetch images by code for a specific catalog-catalog version combination.
 */
.factory('seMediaService', ['seMediaResource', 'seMediaResourceService', function(seMediaResource, seMediaResourceService) {
    /**
     * @ngdoc method
     * @name seMediaServiceModule.seMediaService.uploadMedia
     * @methodOf seMediaServiceModule.seMediaService
     *
     * @description
     * Uploads the media to the catalog.
     *
     * @param {Object} media The media to be uploaded
     * @param {String} media.code A unique code identifier for the media
     * @param {String} media.description A description of the media
     * @param {String} media.altText An alternate text to be shown for the media
     * @param {File} media.file The {@link https://developer.mozilla.org/en/docs/Web/API/File File} object to be
     * uploaded.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves with the media POJO. If the
     * request fails, it resolves with errors from the backend.
     */
    var uploadMedia = function(media) {
        return seMediaResource.save(media).$promise;
    };

    /**
     * @ngdoc method
     * @name seMediaServiceModule.seMediaService.getMediaByCode
     * @methodOf seMediaServiceModule.seMediaService
     *
     * @description
     * Fetches the media for the selected catalog corresponding to the specified code
     *
     * @param {String} code A unique code identifier that corresponds to the media as it exists in the backend.
     *
     * @returns {Promise} If request is successful, it returns a promise that resolves with the media POJO. If the
     * request fails, it resolves with errors from the backend.
     */
    var getMediaByCode = function(code) {
        return seMediaResourceService.getMediaByCode(code).get().$promise;
    };

    return {
        uploadMedia: uploadMedia,
        getMediaByCode: getMediaByCode
    };
}]);

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
 * @name seObjectValidatorFactoryModule
 * @description
 * This module provides the seObjectValidatorFactory service, which is used to build a validator for a specified list of
 * validator objects.
 */
angular.module('seObjectValidatorFactoryModule', [])

/**
 * @ngdoc service
 * @name seObjectValidatorFactoryModule.seObjectValidatorFactory
 * @description
 * This service provides a factory method to build a validator for a specified list of validator objects.
 */
.factory('seObjectValidatorFactory', function() {
    function _validate(validators, objectUnderValidation, errorsContext) {
        errorsContext = errorsContext || [];
        validators.forEach(function(validator) {
            var valueToValidate = objectUnderValidation[validator.subject];
            if (!validator.validate(valueToValidate, objectUnderValidation)) {
                errorsContext.push({
                    'subject': validator.subject,
                    'message': validator.message
                });
            }
        });
        return errorsContext.length === 0;
    }

    /**
     * @ngdoc method
     * @name seObjectValidatorFactoryModule.seObjectValidatorFactory.build
     * @methodOf seObjectValidatorFactoryModule.seObjectValidatorFactory
     * @description
     *
     * Builds a new validator for a specified list of validator objects. Each validator object must consist of a
     * parameter to validate, a predicate function to run against the value, and a message to associate with this
     * predicate function's fail case.
     *
     * For example, The resulting validating object has a single validate function that takes two parameters: an object
     * to validate against and a contextual error list to append errors to:
     *
     * <pre>
     * var validators = [{
     *     subject: 'code',
     *     validate: function(code) {
     *         return code !== 'Invalid';
     *     },
     *     message: 'Code must not be "Invalid"'
     * }]
     *
     * var validator = seObjectValidatorFactory.build(validators);
     * var errorsContext = []
     * var objectUnderValidation = {
     *     code: 'Invalid'
     * };
     * var isValid = validate.validate(objectUnderValidation, errorsContext);
     * </pre>
     *
     * The result of the above code block would be that isValid is false beause it failed the predicate function of the
     * single validator in the validator list and the errorsContext would be as follows:
     *
     * <pre>
     * [{
     *     subject: 'code',
     *     message: 'Code must not be "Invalid"'
     * }]
     * </pre>
     *
     * @param {Array} validators A list of validator objects as specified above.
     * @returns {Object} A validator that consists of a validate function, as described above.
     */
    var build = function(validators) {
        return {
            validate: _validate.bind(null, validators)
        };
    };

    return {
        build: build
    };
});

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
angular.module('seRichTextFieldModule', ['ngSanitize', 'languageServiceModule'])
    .constant('seRichTextConfiguration', {
        toolbar: 'full',
        toolbar_full: [{
                name: 'basicstyles',
                items: ['Bold', 'Italic', 'Strike', 'Underline']
            }, {
                name: 'paragraph',
                items: ['BulletedList', 'NumberedList', 'Blockquote']
            }, {
                name: 'editing',
                items: ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']
            }, {
                name: 'links',
                items: ['Link', 'Unlink', 'Anchor']
            }, {
                name: 'tools',
                items: ['SpellChecker', 'Maximize']
            },
            '/', {
                name: 'styles',
                items: ['Format', 'FontSize', 'TextColor', 'PasteText', 'PasteFromWord', 'RemoveFormat']
            }, {
                name: 'insert',
                items: ['Image', 'Table', 'SpecialChar']
            }, {
                name: 'forms',
                items: ['Outdent', 'Indent']
            }, {
                name: 'clipboard',
                items: ['Undo', 'Redo']
            }, {
                name: 'document',
                items: ['PageBreak', 'Source']
            }
        ],
        disableNativeSpellChecker: false,
        height: '100px',
        width: '100%',
        autoParagraph: false,
        enterMode: CKEDITOR.ENTER_BR,
        shiftEnterMode: CKEDITOR.ENTER_BR,
        basicEntities: false,
        allowedContent: true,
        fillEmptyBlocks: false,
        contentsCss: 'static-resources/dist/smartedit/css/outer-styling.css'
    })
    .service('seRichTextLoaderService', ['$q', '$interval', function($q, $interval) {
        var loadDeferred = $q.defer();

        var checkLoadedInterval = $interval(function() {
            if (CKEDITOR.status === 'loaded') {
                loadDeferred.resolve();
                $interval.cancel(checkLoadedInterval);
            }
        }, 100);

        return {
            load: function() {
                var deferred = $q.defer();
                loadDeferred.promise.then(function() {
                    deferred.resolve();
                });
                return deferred.promise;
            }
        };
    }])
    .service('genericEditorSanitizationService', ['$sanitize', function($sanitize) {
        return {
            isSanitized: function(content) {

                var sanitizedContent = $sanitize(content);
                sanitizedContent = sanitizedContent.replace(/&#10;/g, '\n').replace(/&#160;/g, "\u00a0").replace(/<br>/g, '<br />');
                content = content.replace(/&#10;/g, '\n').replace(/&#160;/g, "\u00a0").replace(/<br>/g, '<br />');
                var sanitizedContentMatchesContent = sanitizedContent === content;
                return sanitizedContentMatchesContent;
            }
        };
    }])
    .controller('seRichTextFieldController', function() {})
    .directive('seRichTextField', ['seRichTextLoaderService', 'seRichTextConfiguration', 'genericEditorSanitizationService', 'seRichTextFieldLocalizationService', function(seRichTextLoaderService, seRichTextConfiguration, genericEditorSanitizationService, seRichTextFieldLocalizationService) {
        return {
            restrict: 'E',
            templateUrl: 'web/common/services/genericEditor/richText/seRichTextFieldTemplate.html',
            scope: {},
            controller: 'seRichTextFieldController',
            controllerAs: 'ctrl',
            bindToController: {
                field: '=',
                qualifier: '=',
                model: '=',
                editor: '='

            },
            link: function($scope, $element) {
                seRichTextLoaderService.load().then(function() {
                    var textAreaElement = $element.find('textarea')[0];
                    var editorInstance = CKEDITOR.replace(textAreaElement, seRichTextConfiguration);

                    seRichTextFieldLocalizationService.localizeCKEditor();

                    $element.bind('$destroy', function() {
                        if (editorInstance && CKEDITOR.instances[editorInstance.name]) {
                            CKEDITOR.instances[editorInstance.name].destroy();
                        }
                    });

                    $scope.ctrl.onChange = function() {
                        $scope.$apply(function() {
                            $scope.ctrl.model[$scope.ctrl.qualifier] = editorInstance.getData();
                            $scope.ctrl.reassignUserCheck();

                        });
                    };

                    $scope.ctrl.onMode = function() {
                        if (this.mode == 'source') {
                            var editable = editorInstance.editable();
                            editable.attachListener(editable, 'input', function() {
                                editorInstance.fire('change');
                            });
                        }
                    };

                    $scope.ctrl.onInstanceReady = function(ev) {
                        ev.editor.dataProcessor.writer.setRules('br', {
                            indent: false,
                            breakBeforeOpen: false,
                            breakAfterOpen: false,
                            breakBeforeClose: false,
                            breakAfterClose: false
                        });
                    };

                    $scope.ctrl.requiresUserCheck = function() {
                        var requiresUserCheck = false;
                        for (var qualifier in this.field.requiresUserCheck) {
                            requiresUserCheck = requiresUserCheck || this.field.requiresUserCheck[qualifier];
                        }
                        return requiresUserCheck;
                    };


                    $scope.ctrl.reassignUserCheck = function() {
                        if ($scope.ctrl.model && $scope.ctrl.qualifier && $scope.ctrl.model[$scope.ctrl.qualifier]) {
                            var sanitizedContentMatchesContent = genericEditorSanitizationService.isSanitized($scope.ctrl.model[$scope.ctrl.qualifier]);
                            $scope.ctrl.field.requiresUserCheck = $scope.ctrl.field.requiresUserCheck || {};
                            $scope.ctrl.field.requiresUserCheck[$scope.ctrl.qualifier] = !sanitizedContentMatchesContent;
                        } else {
                            $scope.ctrl.field.requiresUserCheck = $scope.ctrl.field.requiresUserCheck || {};
                            $scope.ctrl.field.requiresUserCheck[$scope.ctrl.qualifier] = false;
                        }
                    };

                    editorInstance.on('change', $scope.ctrl.onChange);
                    editorInstance.on('mode', $scope.ctrl.onMode);
                    CKEDITOR.on('instanceReady', $scope.ctrl.onInstanceReady);

                });
            }
        };
    }])
    .constant('resolvedLocaleToCKEDITORLocaleMap', {
        'in': 'id',
        'es_CO': 'es'
    })
    .service('seRichTextFieldLocalizationService', ['languageService', 'resolvedLocaleToCKEDITORLocaleMap', function(languageService, resolvedLocaleToCKEDITORLocaleMap) {

        var convertResolvedToCKEditorLocale = function(resolvedLocale) {
            var conversion = resolvedLocaleToCKEDITORLocaleMap[resolvedLocale];
            if (conversion) {
                return conversion;
            } else {
                return resolvedLocale;
            }
        };

        this.localizeCKEditor = function() {
            languageService.getResolveLocale().then(function(locale) {
                CKEDITOR.config.language = convertResolvedToCKEditorLocale(locale);
            });
        };

    }]);

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
 * @name seValidationErrorParserModule
 * @description
 * This module provides the validationErrorsParser service, which is used to parse validation errors for parameters
 * such as language and format, which are sent as part of the message itself.
 */
angular.module('seValidationErrorParserModule', [])

/**
 * @ngdoc service
 * @name seValidationErrorParserModule.seValidationErrorParser
 * @description
 * This service provides the functionality to parse validation errors received from the backend.
 */
.service('seValidationErrorParser', function() {

    /**
     * @ngdoc method
     * @name seValidationErrorParserModule.seValidationErrorParser.parse
     * @methodOf seValidationErrorParserModule.seValidationErrorParser
     * @description
     * Parses extra details, such as language and format, from a validation error message. These details are also
     * stripped out of the final message. This function expects the message to be in the following format:
     *
     * <pre>
     * var message = "Some validation error occurred. Language: [en]. Format: [widescreen]. SomeKey: [SomeVal]."
     * </pre>
     *
     * The resulting error object is as follows:
     * <pre>
     * {
     *     message: "Some validation error occurred."
     *     language: "en",
     *     format: "widescreen",
     *     somekey: "someval"
     * }
     * </pre>
     */
    this.parse = function(message) {
        var expression = new RegExp('[a-zA-Z]+: (\[|\{)([a-zA-Z]+)(\]|\})\.?', 'g');
        var matches = message.match(expression) || [];
        return matches.reduce(function(errors, match) {
            errors.message = errors.message.replace(match, '').trim();
            var key = match.split(':')[0].trim().toLowerCase();
            var value = match.split(':')[1].match(/[a-zA-Z]+/g)[0];

            errors[key] = value;
            return errors;
        }, {
            message: message
        });
    };
});

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
angular.module('i18nInterceptorModule', ['interceptorHelperModule', 'resourceLocationsModule', 'languageServiceModule']) //loose dependency on loadConfigModule since it exists in smartEditContainer but not smartEdit
    .constant('temporaryKeys', {})
    /**
     * @ngdoc object
     * @name i18nInterceptorModule.object:I18NAPIROOT
     *
     * @description
     * The I18NAPIroot is a hard-coded URI that is used to initialize the {@link translationServiceModule}.
     * The {@link i18nInterceptorModule.service:i18nInterceptor#methods_request i18nInterceptor.request} intercepts the URI and replaces it with the {@link resourceLocationsModule.object:I18N_RESOURCE_URI I18N_RESOURCE_URI}.
     */
    .constant('I18NAPIROOT', 'i18nAPIRoot')
    /**
     * @ngdoc object
     * @name i18nInterceptorModule.object:UNDEFINED_LOCALE
     *
     * @description
     * The undefined locale set as the preferred language of the {@link translationServiceModule} so that
     * an {@link i18nInterceptorModule.service:i18nInterceptor#methods_request i18nInterceptor.request} can intercept it and replace it with the browser locale.
     */
    .constant('UNDEFINED_LOCALE', 'UNDEFINED')
    /**
     * @ngdoc service
     * @name i18nInterceptorModule.service:i18nInterceptor
     *
     * @description
     * A HTTP request interceptor that intercepts all i18n calls and handles them as required in the {@link i18nInterceptorModule.service:i18nInterceptor#methods_request i18nInterceptor.request} method.
     *
     * The interceptors are service factories that are registered with the $httpProvider by adding them to the $httpProvider.interceptors array.
     * The factory is called and injected with dependencies and returns the interceptor object, which contains the interceptor methods.
     */
    .factory('i18nInterceptor', ['$injector', 'interceptorHelper', 'I18N_RESOURCE_URI', 'I18NAPIROOT', 'UNDEFINED_LOCALE', 'temporaryKeys', function($injector, interceptorHelper, I18N_RESOURCE_URI, I18NAPIROOT, UNDEFINED_LOCALE, temporaryKeys) {

        return {

            /**
             * @ngdoc method
             * @name i18nInterceptorModule.service:i18nInterceptor#request
             * @methodOf i18nInterceptorModule.service:i18nInterceptor
             *
             * @description
             * Interceptor method that is invoked with a HTTP configuration object.
             *  It intercepts all requests that are i18n calls, that is, it intercepts all requests that have an {@link i18nInterceptorModule.object:I18NAPIROOT I18NAPIROOT} in their calls.
             *  It replaces the URL provided in a request with the URL provided by the {@link resourceLocationsModule.object:I18N_RESOURCE_URI I18N_RESOURCE_URI}.
             *  If a locale has not already been defined, the interceptor method appends the locale retrieved using the {@link languageServiceModule.service:languageService#methods_getResolveLocale languageService.getResolveLocale}.


             * @param {Object} config The HTTP configuration information that contains the configuration information.
             *
             * @returns {Promise} Returns a {@link https://docs.angularjs.org/api/ng/service/$q promise} of the passed configuration object.
             */
            request: function(config) {
                return interceptorHelper.handleRequest(config, function() {
                    /*
                     * always intercept i18n calls so as to replace URI by one from configuration (cannot be done at config time of $translateProvider)
                     * regex matching /i18nAPIRoot/<my_locale>
                     */
                    var regex = new RegExp(I18NAPIROOT + "\/([a-zA-Z_]+)$");
                    if (regex.test(config.url)) {
                        var locale = regex.exec(config.url)[1];
                        return $injector.get('languageService').getResolveLocale().then(function(isoCode) {
                            config.url = [I18N_RESOURCE_URI, isoCode].join('/');
                            return config;
                        });
                    } else {
                        return config;
                    }
                });
            },
            response: function(response) {
                return interceptorHelper.handleResponse(response, function() {
                    /*
                     * if it intercepts a call to I18N_RESOURCE_URI the response body will be adapted to
                     * read the value from response.data.value instead.
                     */
                    var regex = new RegExp(I18N_RESOURCE_URI + "/([a-zA-Z_]+)$");
                    if (response.config.url) {
                        var url = response.config.url;
                        if (regex.test(url) && response.data.value) {
                            response.data = response.data.value;
                        }
                        if (regex.test(url)) {
                            /*
                             * MISF-445 : temporary adding of i18n keys that did nto make it to 6.0 feature freeze
                             */
                            for (var key in temporaryKeys) {
                                if (!response.data[key]) {
                                    response.data[key] = temporaryKeys[key];
                                }
                            }
                        }

                    }
                    $injector.get('languageService').setInitialized(true);
                    return response;
                });
            }
        };
    }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('i18nInterceptor');
    }]);

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
 * @name yInfiniteScrollingModule
 * @description
 * <h1>This module holds the base web component to perform infinite scrolling from paged backend</h1>
 */
angular.module('yInfiniteScrollingModule', ['infinite-scroll', 'functionsModule'])
    //value used by third party infinite-scroll and used here when typing new value for mask
    /**
     * @ngdoc object
     * @name yInfiniteScrollingModule.object:THROTTLE_MILLISECONDS
     *
     * @description
     * Configures the {@link yInfiniteScrollingModule.directive:yInfiniteScrolling yInfiniteScrolling} directive to throttle the page fetching with the value provided in milliseconds.
     */
    .value('THROTTLE_MILLISECONDS', 250)
    .controller('yInfiniteScrollingController', ['$scope', 'throttle', 'THROTTLE_MILLISECONDS', 'generateIdentifier', '$timeout', function($scope, throttle, THROTTLE_MILLISECONDS, generateIdentifier, $timeout) {

        this.initiated = false;
        this.containerId = generateIdentifier();
        this.containerIdSelector = "#" + this.containerId;

        this.context = this.context || this;

        this.reset = function() {
            this.distance = this.distance || 0;
            this.context.items = [];
            this.currentPage = -1;
            this.pagingDisabled = false;
            //necessary so that infinite-scroll directive can rely on resolved container id
            if (!this.container) {
                $timeout(function() {
                    this.container = $(this.containerIdSelector).get(0);
                    this.initiated = true;
                }.bind(this), 0);
            }
        };

        this.nextPage = function() {
            if (this.pagingDisabled) {
                return;
            }
            this.pagingDisabled = true;
            this.currentPage++;
            this.mask = this.mask || "";
            this.fetchPage(this.mask, this.pageSize, this.currentPage).then(function(page) {
                Array.prototype.push.apply(this.context.items, page.results);
                this.pagingDisabled = page.results.length === 0 || this.context.items.length == page.pagination.totalCount;
            }.bind(this));
        }.bind(this);

        this.$onChanges = throttle(function() {
            var wasInitiated = this.initiated;
            this.reset();
            if (wasInitiated) {
                this.nextPage();
            }
        }.bind(this), THROTTLE_MILLISECONDS);

        $scope.$watch(function() {
            return this.mask;
        }.bind(this), this.$onChanges);

    }])
    /**
     * @ngdoc object
     * @name yInfiniteScrollingModule.object:Page
     * @description
     * An object representing the backend response to a paged query
     */
    /**
     * @ngdoc property
     * @name results
     * @propertyOf yInfiniteScrollingModule.object:Page
     * @description
     * The array containing the elements pertaining to the requested page, its size will not exceed the requested page size.
     **/
    /**
     * @ngdoc property
     * @name pagination
     * @propertyOf yInfiniteScrollingModule.object:Page
     * @description
     * The returned {@link yInfiniteScrollingModule.object:Pagination Pagination} object
     */

/**
 * @ngdoc object
 * @name yInfiniteScrollingModule.object:Pagination
 * @description
 * An object representing the returned pagination information from backend
 */

/**
 * @ngdoc property
 * @name totalCount
 * @propertyOf yInfiniteScrollingModule.object:Pagination
 * @description
 * the total of elements matching the given mask once all pages are returned
 **/

/**
 * @ngdoc directive
 * @name yInfiniteScrollingModule.directive:yInfiniteScrolling
 * @scope
 * @restrict E
 *
 * @description
 * A directive that you can use to implement infinite scrolling for an expanding content (typically with a ng-repeat) nested in it.
 * It is meant to handle paginated requests from a backend when data is expected to be large.
 * Since the expanding content is a <b>transcluded</b> element, we must specify the context to which the items will be attached:
 * If context is myContext, each pagination will push its new items to myContext.items.
 * @param {String} pageSize The maximum size of each page requested from the backend.
 * @param {String} mask A string value sent to the server upon fetching a page to further restrict the search, it is sent as query string "mask".
 * <br>The directive listens for change to mask and will reset the scroll and re-fetch data.
 * <br/>It it left to the implementers to decide what it filters on
 * @param {String} distance A number representing how close the bottom of the element must be to the bottom of the container before the expression specified by fetchPage function is triggered. Measured in multiples of the container height; for example, if the container is 1000 pixels tall and distance is set to 2, the infinite scroll expression will be evaluated when the bottom of the element is within 2000 pixels of the bottom of the container. Defaults to 0 (e.g. the expression will be evaluated when the bottom of the element crosses the bottom of the container).
 * @param {Object} context The container object to which the items of the fetched {@link yInfiniteScrollingModule.object:Page Page} will be added
 * @param {Function} fetchPage function to fetch the next page when the bottom of the element approaches the bottom of the container.
 *        fetchPage will be invoked with 3 arguments : <b>mask, pageSize, currentPage</b>. The currentPage is determined by the scrolling and starts with 0. The function must return a page of type {@link yInfiniteScrollingModule.object:Page Page}.
 * @param {String} dropDownContainerClass An optional CSS class to be added to the container of the dropdown. It would typically be used to override the default height. <b>The resolved CSS must set a height (or max-height) and overflow-y:scroll.</b>
 * @param {String} dropDownClass An optional CSS class to be added to the dropdown. <b>Neither height nor overflow should be set on the dropdown, it must be free to fill up the space and reach the container size. Failure to do so will cause the directive to call nextPage as many times as the number of available pages on the server.</b>
 */
.directive(
    'yInfiniteScrolling',
    function() {
        return {
            templateUrl: 'web/common/services/infiniteScrolling/yInfiniteScrollingTemplate.html',
            transclude: true,
            replace: true,
            controller: 'yInfiniteScrollingController',
            controllerAs: 'scroll',
            scope: {},
            bindToController: {
                pageSize: '=',
                mask: '=?',
                fetchPage: '=',
                distance: '=?',
                context: '=?',
                dropDownContainerClass: '@?',
                dropDownClass: '@?'
            }
        };
    }
);

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
angular.module('interceptorHelperModule', [])
    /**
     * @ngdoc service
     * @name interceptorHelperModule.service:interceptorHelper
     *
     * @description
     * Helper service used to handle request and response in interceptors
     */
    .service('interceptorHelper', ['$q', '$log', function($q, $log) {

        return {

            _isEligibleForInterceptors: function(config) {
                return config && config.url && !/.+\.html$/.test(config.url);
            },

            _handle: function(chain, config, callback, isError) {
                try {
                    if (this._isEligibleForInterceptors(config)) {
                        return $q.when(callback());
                    } else {
                        if (isError) {
                            return $q.reject(chain);
                        } else {
                            return chain;
                        }
                    }
                } catch (e) {
                    $log.error("caught error in one of the interceptors", e);
                    if (isError) {
                        return $q.reject(chain);
                    } else {
                        return chain;
                    }
                }
            },

            /** 
             * @ngdoc method
             * @name interceptorHelperModule.service:interceptorHelper#methodsOf_handleRequest
             * @methodOf interceptorHelperModule.service:interceptorHelper
             *
             * @description
             * Handles body of an interceptor request
             * @param {object} config the request's config to be handled by an interceptor method
             * @param {callback} callback the callback function to handle the object. callback will either return a promise or the initial object.
             * @return {object} the config or a promise resolving or rejecting with the config
             */
            handleRequest: function(config, callback) {
                return this._handle(config, config, callback, false);
            },

            /** 
             * @ngdoc method
             * @name interceptorHelperModule.service:interceptorHelper#methodsOf_handleResponse
             * @methodOf interceptorHelperModule.service:interceptorHelper
             *
             * @description
             * Handles body of an interceptor response success
             * @param {object} response the response to be handled by an interceptor method
             * @param {callback} callback the callback function to handle the response. callback will either return a promise or the initial object.
             * @return {object} the response or a promise resolving or rejecting with the response
             */
            handleResponse: function(response, callback) {
                return this._handle(response, response.config, callback, false);
            },
            /** 
             * @ngdoc method
             * @name interceptorHelperModule.service:interceptorHelper#methodsOf_handleResponseError
             * @methodOf interceptorHelperModule.service:interceptorHelper
             *
             * @description
             * Handles body of an interceptor response error
             * @param {object} response the response to be handled by an interceptor method
             * @param {callback} callback the callback function to handle the response in error. callback will either return a promise or the initial object.
             * @return {object} the response or a promise resolving or rejecting with the response
             */
            handleResponseError: function(response, callback) {
                return this._handle(response, response.config, callback, true);
            }
        };
    }]);

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
 * @name languageSelectorModule
 * @description
 *
 * The language selector module contains a directive which allow the user to select a language.
 *
 * Use the {@link languageServiceModule.service:languageService languageService}
 * to call backend API in order to get the list of supported languages
 */
angular
    .module('languageSelectorModule', ['languageServiceModule', 'ui.select', 'ngSanitize', 'eventServiceModule'])
    .controller('languageSelectorController', ['systemEventService', '$scope', 'languageService', 'SWITCH_LANGUAGE_EVENT', function(systemEventService, $scope, languageService, SWITCH_LANGUAGE_EVENT) {


        this.setSelectedLanguage = function(item) {
            languageService.setSelectedToolingLanguage(item);
        };

        languageService.getToolingLanguages().then(function(data) {
            this.languages = data;
            languageService.getResolveLocale().then(function(isoCode) {
                this.selectedLanguage = this.languages.find(function(obj) {
                    return obj.isoCode === isoCode;
                });
            }.bind(this));
        }.bind(this));

        this.eventHandler = function() {
            return languageService.getResolveLocale().then(function(isoCode) {
                this.selectedLanguage = this.languages.find(function(obj) {
                    return obj.isoCode === isoCode;
                });
            }.bind(this));
        };

        var boundEventHandler = this.eventHandler.bind(this);
        systemEventService.registerEventHandler(SWITCH_LANGUAGE_EVENT, boundEventHandler);

        $scope.$on('$destroy', function() {
            systemEventService.unRegisterEventHandler(SWITCH_LANGUAGE_EVENT, boundEventHandler);
        });
    }])
    /**
     * @ngdoc directive
     * @name languageSelectorModule.directive:languageSelector
     * @scope
     * @restrict E
     * @element ANY
     * @description
     * Language selector provides a drop-down list which contains a list of supported languages.
     * It is used to select a language and translate the system accordingly.
     */
    .directive(
        'languageSelector',
        ['languageService', '$q', function(languageService, $q) {
            return {
                templateUrl: 'web/common/services/languageSelector/languageSelectorTemplate.html',
                restrict: 'E',
                transclude: true,
                scope: {},
                bindToController: {},
                controller: 'languageSelectorController',
                controllerAs: 'ctrl'
            };
        }]);

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
 * @name languageServiceModule
 * @description
 * # The languageServiceModule
 *
 * The Language Service module provides a service that fetches all languages that are supported for specified site.
 */
angular.module('languageServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule', 'storageServiceModule', 'translationServiceModule', 'gatewayFactoryModule', 'eventServiceModule'])
    /**
     * @ngdoc object
     * @name languageServiceModule.SELECTED_LANGUAGE
     *
     * @description
     * A constant that is used as key to store the selected language in the storageService
     */
    .constant('SELECTED_LANGUAGE', 'SELECTED_LANGUAGE')
    /**
     * @ngdoc object
     * @name languageServiceModule.SWITCH_LANGUAGE_EVENT
     *
     * @description
     * A constant that is used as key to publish and receive events when a language is changed.
     */
    .constant('SWITCH_LANGUAGE_EVENT', 'SWITCH_LANGUAGE_EVENT')
    /**
     * @ngdoc service
     * @name languageServiceModule.service:languageService
     *
     * @description
     * The Language Service fetches all languages for a specified site using REST service calls to the cmswebservices languages API.
     */
    .factory('languageServiceGateway', ['gatewayFactory', function(gatewayFactory) {
        return gatewayFactory.createGateway("languageSwitch");
    }])
    .factory('languageService', ['restServiceFactory', 'LANGUAGE_RESOURCE_URI', '$q', 'I18N_LANGUAGES_RESOURCE_URI', 'SELECTED_LANGUAGE', 'storageService', 'languageServiceGateway', '$translate', 'SWITCH_LANGUAGE_EVENT', 'systemEventService', function(restServiceFactory, LANGUAGE_RESOURCE_URI, $q, I18N_LANGUAGES_RESOURCE_URI, SELECTED_LANGUAGE, storageService, languageServiceGateway, $translate, SWITCH_LANGUAGE_EVENT, systemEventService) {

        var cache = {};
        var languageRestService = restServiceFactory.get(LANGUAGE_RESOURCE_URI);

        var i18nLanguageRestService = restServiceFactory.get(I18N_LANGUAGES_RESOURCE_URI);

        var _getBrowserLocale = function() {
            var locale = (navigator.language || navigator.userLanguage).split("-");

            if (locale.length === 1) {
                locale = locale[0];
            } else {
                locale = locale[0] + "_" + locale[1].toUpperCase();
            }
            return locale;
        };

        var _getDefaultLanguage = function() {
            var browserLocale = _getBrowserLocale();
            return storageService.getValueFromCookie(SELECTED_LANGUAGE, false).then(
                function(selectedLanguage) {
                    if (selectedLanguage) {
                        return selectedLanguage.isoCode;
                    } else {
                        return browserLocale;
                    }
                },
                function() {
                    return browserLocale;
                }
            );
        };

        var initDeferred = $q.defer();

        return {
            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#getBrowserLanguageIsoCode
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * Uses the browser's current locale to determine the selected language ISO code.
             *
             * @returns {String} The language ISO code of the browser's currently selected locale.
             */
            getBrowserLanguageIsoCode: function() {
                return (navigator.language || navigator.userLanguage).split("-")[0];
            },

            setInitialized: function(_initialized) {
                if (_initialized === true) {
                    initDeferred.resolve();
                } else {
                    initDeferred.reject();
                }
            },
            isInitialized: function() {
                return initDeferred.promise;
            },

            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#getBrowserLocale
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * determines the browser locale in the format en_US
             *
             * @returns {string} the browser locale
             */
            getBrowserLocale: function() {
                return _getBrowserLocale();
            },

            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#getResolveLocale
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * Resolve the user preference tooling locale. It determines in the
             * following order:
             *
             * 1. Check if the user has previously selected the language
             * 2. Check if the user browser locale is supported in the system
             * @returns {string} the locale
             */
            getResolveLocale: function() {
                return $q.when(_getDefaultLanguage());
            },

            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#getLanguagesForSite
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * Fetches a list of language descriptors for the specified storefront site UID. The object containing the list of sites is fetched
             * using REST calls to the cmswebservices languages API.
             * @param {string} siteUID the site unique identifier.
             * @returns {Array} An array of language descriptors. Each descriptor provides the following language
             * properties: isocode, nativeName, name, active, and required.
             * format:
             * <pre>
             * [{
             *   language: 'en',
             *   required: true
             *  }, {
             *   language: 'fr',
             *  }]
             * </pre>
             */
            getLanguagesForSite: function(siteUID) {
                return cache[siteUID] ? $q.when(cache[siteUID]) : languageRestService.get({
                    siteUID: siteUID
                }).then(function(languagesListDTO) {
                    cache[siteUID] = languagesListDTO.languages;
                    return cache[siteUID];
                });
            },

            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#getToolingLanguages
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * Retrieves a list of language descriptors using REST calls to the smarteditwebservices i18n API.
             *
             * @returns {Array} An array of language descriptors. Each descriptor provides the following language
             * properties: isocode, name
             * format:
             * <pre>
             * [{
             *   isoCode: 'en',
             *   name: 'English'
             *  }, {
             *   isoCode: 'fr',
             *   name: 'French'
             *  }]
             * </pre>
             */

            getToolingLanguages: function() {
                var deferred = $q.defer();

                i18nLanguageRestService.get().then(
                    function(response) {
                        deferred.resolve(response.languages);
                    },
                    function() {
                        deferred.reject();
                    }
                );

                return deferred.promise;
            },

            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#setSelectedLanguage
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * Set the user preference language in the storage service
             *
             * @param {object} the language object to be saved. the object contains the following properties:isoCode and name.
             * <pre>
             * {
             * isoCode:'fr',
             * name: 'French'
             * }
             * </pre>
             */
            setSelectedToolingLanguage: function(language) {
                storageService.putValueInCookie(SELECTED_LANGUAGE, language, false);
                $translate.use(language.isoCode);
                languageServiceGateway.publish(SWITCH_LANGUAGE_EVENT, {
                    isoCode: language.isoCode
                });
                systemEventService.sendEvent(SWITCH_LANGUAGE_EVENT);
            },
            /**
             * @ngdoc method
             * @name languageServiceModule.service:languageService#registerSwitchLanguage
             * @methodOf languageServiceModule.service:languageService
             *
             * @description
             * Register a callback function to the gateway in order to switch the tooling language
             */
            registerSwitchLanguage: function() {
                languageServiceGateway.subscribe(SWITCH_LANGUAGE_EVENT, function(eventId, data) {
                    return $translate.use(data.isoCode);
                });
            }
        };
    }]);

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
 * @name loadConfigModule
 * @description
 * The loadConfigModule supplies configuration information to SmartEdit. Configuration is stored in key/value pairs.
 * The module exposes a service which is used to load configuration as an array or object.
 * @requires functionsModule
 * @requires ngResource
 * @requires sharedDataServiceModule
 * @requires resourceLocationsModule
 */
angular.module('loadConfigModule', ['functionsModule', 'ngResource', 'sharedDataServiceModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name loadConfigModule.service:LoadConfigManager
     * @description
     * The LoadConfigManager is used to retrieve configurations stored in configuration API.
     * @requires $resource
     * @requires hitch
     * @requires copy
     * @requires convertToArray
     * @requires sharedDataService
     * @requires $log
     * @requires resourceLocationsModule.object:SMARTEDIT_ROOT
     * @requires resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP
     * @requires resourceLocationsModule.object:CONFIGURATION_URI
     */
    .factory('LoadConfigManager', ['$resource', 'hitch', 'copy', '$q', 'convertToArray', 'sharedDataService', '$log', 'SMARTEDIT_ROOT', 'SMARTEDIT_RESOURCE_URI_REGEXP', 'CONFIGURATION_URI', function($resource, hitch, copy, $q, convertToArray, sharedDataService, $log, SMARTEDIT_ROOT, SMARTEDIT_RESOURCE_URI_REGEXP, CONFIGURATION_URI) {

        /**
         * @ngdoc method
         * @name loadConfigModule.service:LoadConfigManager#LoadConfigManager
         * @methodOf loadConfigModule.service:LoadConfigManager
         * @description
         * This function is used to create a new object of the LoadConfigManager. 
         */
        var LoadConfigManager = function() {
            this.editorViewService = $resource(CONFIGURATION_URI);
            this.configuration = [];

            this._convertToObject = function(configuration) {
                var configurations = configuration.reduce(function(previous, current, index, array) {
                    try {
                        previous[current.key] = JSON.parse(current.value);
                    } catch (parseError) {
                        $log.error("item _key_ from configuration contains unparsable JSON data _value_ and was ignored".replace("_key_", current.key).replace("_value_", current.value));
                    }
                    return previous;
                }, {});

                configurations.domain = SMARTEDIT_RESOURCE_URI_REGEXP.exec(this._getLocation())[1];
                configurations.smarteditroot = configurations.domain + '/' + SMARTEDIT_ROOT;
                return configurations;
            };

            this._getLocation = function() {
                return document.location.href;
            };
        };

        LoadConfigManager.prototype._parse = function(configuration) {
            var conf = copy(configuration);
            Object.keys(conf).forEach(function(key) {
                try {
                    conf[key] = JSON.parse(conf[key]);
                } catch (e) {
                    //expected for properties coming form $resource framework such as $promise.... and one wants the configuration editor itself to deal with parsable issues
                }
            });
            return conf;
        };

        /**
         * @ngdoc method
         * @name loadConfigModule.service.LoadConfigManager#loadAsArray
         * @methodOf loadConfigModule.service:LoadConfigManager
         * @description
         * Retrieves configuration from an API and returns as an array of mapped key/value pairs.
         *
         * Example:
         * <pre>
         * loadConfigManagerService.loadAsArray().then(
         *   hitch(this, function(response) {
         *     this._prettify(response);
         * }));
         * </pre>
         * 
         * @returns {Array} a promise of configuration values as an array of mapped configuration key/value pairs
         */
        LoadConfigManager.prototype.loadAsArray = function() {
            var deferred = $q.defer();
            this.editorViewService.query().$promise.then(
                hitch(this, function(response) {
                    deferred.resolve(this._parse(response));
                }),
                function(failure) {
                    $log.log("Fail to load the configurations.");
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name loadConfigModule.service.LoadConfigManager#loadAsObject
         * @methodOf loadConfigModule.service:LoadConfigManager
         *
         * @description
         * Retrieves a configuration from the API and converts it to an object.  
         * 
         * Example
         * <pre>
         * loadConfigManagerService.loadAsObject().then(function(conf) {
         *   sharedDataService.set('defaultToolingLanguage', conf.defaultToolingLanguage);
         *  });
         * </pre>
         * @returns {Object} a promise of configuration values as an object of mapped configuration key/value pairs
         */
        LoadConfigManager.prototype.loadAsObject = function() {
            var deferred = $q.defer();
            this.loadAsArray().then(
                hitch(this, function(response) {
                    var conf = this._convertToObject(response);
                    sharedDataService.set('configuration', conf);
                    deferred.resolve(conf);
                }),
                function(failure) {
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        return LoadConfigManager;

    }])

/**
 * @ngdoc service
 * @name loadConfigModule.service:loadConfigManagerService
 *
 * @description
 * A service that is a singleton of {@link loadConfigModule.service:LoadConfigManager}  which is used to 
 * retrieve smartedit configuration values.
 * This services is the entry point of the smartedit configuration module. 
 * @requires LoadConfigManager
 */
.factory('loadConfigManagerService', ['LoadConfigManager', function(LoadConfigManager) {
    return new LoadConfigManager();
}]);

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
angular.module('localizedElementModule', ['tabsetModule'])
    .directive('localizedElement', function() {

        return {
            templateUrl: 'web/common/services/localizedElement/localizedElementTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: false,
            scope: {
                model: '=',
                languages: '=',
                inputTemplate: '='
            },

            link: function($scope, element, attrs) {

                $scope.tabs = [];

                $scope.$watch("model", function(model) {
                    if (model) {
                        var inputTemplate = $scope.inputTemplate ? $scope.inputTemplate : attrs.inputTemplate;

                        $scope.tabs.length = 0;
                        Array.prototype.push.apply($scope.tabs, $scope.languages.map(function(language) {
                            return {
                                id: language.isocode,
                                title: language.isocode.toUpperCase() + (language.required ? "*" : ""),
                                templateUrl: inputTemplate
                            };
                        }));
                    }
                });

                $scope.$watch("model.field.errors", function(errors) {
                    if ($scope.model) {
                        var errorMap = errors ? errors.reduce(function(holder, next) {
                            holder[next.language] = true;
                            return holder;
                        }, {}) : {};
                        $scope.tabs.forEach(function(tab) {
                            var error = errorMap[tab.id];
                            tab.hasErrors = error !== undefined ? error : false;
                        });
                    }
                }, true);

            }
        };
    });

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
 * @name modalServiceModule
 * @description
 * # The modalServiceModule
 *
 * The modal service module is a module devoted to providing an easy way to create, use, manage, and style modal windows,
 * and it contains all the components related to achieving this goal.
 *
 * Use the {@link modalServiceModule.modalService modalService} to open modal windows.
 *
 * Once a modal window is opened, you can use it's {@link modalServiceModule.service:ModalManager ModalManager} to manage
 * the modal, such as closing the modal, adding buttons, or chaning the title.
 *
 * Additionally you may use {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button actions} or
 * {@link modalServiceModule.object:MODAL_BUTTON_STYLES button styles} to affect how buttons look and behave on the modal
 * window.
 *
 */
angular.module('modalServiceModule', ['ui.bootstrap', 'translationServiceModule', 'functionsModule', 'coretemplates'])

/**
 * @ngdoc object
 * @name modalServiceModule.object:MODAL_BUTTON_ACTIONS
 *
 * @description
 * Injectable angular constant<br/>
 * Defines the action to be taken after executing a button on a modal window. To be used when adding a button to the modal,
 * either when opening a modal (see {@link modalServiceModule.service:ModalManager#methods_getButtons ModalManager.getButtons()}) or
 * when adding a button to an existing modal (see {@link modalServiceModule.modalService#methods_open modalService.open()})
 *
 * Example:
 * <pre>
 *      myModalManager.addButton({
 *          id: 'button id',
 *          label: 'close_modal',
 *          action: MODAL_BUTTON_ACTIONS.CLOSE
 *      });
 * </pre>
 */
.constant('MODAL_BUTTON_ACTIONS', {
    /**
     * @ngdoc property
     * @name NONE
     * @propertyOf modalServiceModule.object:MODAL_BUTTON_ACTIONS
     *
     * @description
     * Indicates to the {@link modalServiceModule.service:ModalManager ModalManager} that after executing the modal button
     * no action should be performed.
     **/
    NONE: "none",

    /**
     * @ngdoc property
     * @name CLOSE
     * @propertyOf modalServiceModule.object:MODAL_BUTTON_ACTIONS
     *
     * @description
     * Indicates to the {@link modalServiceModule.service:ModalManager ModalManager} that after executing the modal button,
     * the modal window should close, and the {@link https://docs.angularjs.org/api/ng/service/$q promise} returned by the modal should be resolved.
     **/
    CLOSE: "close",

    /**
     * @ngdoc property
     * @name DISMISS
     * @propertyOf modalServiceModule.object:MODAL_BUTTON_ACTIONS
     *
     * @description
     * Indicates to the {@link modalServiceModule.service:ModalManager ModalManager} that after executing the modal button,
     * the modal window should close, and the {@link https://docs.angularjs.org/api/ng/service/$q promise} returned by the modal should be rejected.
     **/
    DISMISS: "dismiss"
})

/**
 * @ngdoc object
 * @name modalServiceModule.object:MODAL_BUTTON_STYLES
 *
 * @description
 * Injectable angular constant<br/>
 * Defines the look and feel of a button on a modal window. To be used when adding a button to the modal,
 * either when opening a modal (see {@link modalServiceModule.service:ModalManager#methods_getButtons ModalManager.getButtons()}) or
 * when adding a button to an existing modal (see {@link modalServiceModule.modalService#methods_open modalService.open()})
 *
 * Example:
 * <pre>
 *      myModalManager.addButton({
 *          id: 'button id',
 *          label: 'cancel_button',
 *          style: MODAL_BUTTON_STYLES.SECONDARY
 *      });
 * </pre>
 */
.constant('MODAL_BUTTON_STYLES', {

    /**
     * @ngdoc property
     * @name DEFAULT
     * @propertyOf modalServiceModule.object:MODAL_BUTTON_STYLES
     *
     * @description
     * Equivalent to SECONDARY
     **/
    DEFAULT: "default",

    /**
     * @ngdoc property
     * @name PRIMARY
     * @propertyOf modalServiceModule.object:MODAL_BUTTON_STYLES
     *
     * @description
     * Indicates to the modal window that this button is the primary button of the modal, such as save or submit,
     * and should be styled accordingly.
     **/
    PRIMARY: "primary",

    /**
     * @ngdoc property
     * @name SECONDARY
     * @propertyOf modalServiceModule.object:MODAL_BUTTON_STYLES
     *
     * @description
     * Indicates to the modal window that this button is a secondary button of the modal, such as cancel,
     * and should be styled accordingly.
     **/
    SECONDARY: "default"
})

/**
 * @ngdoc service
 * @name modalServiceModule.modalService
 *
 * @description
 * Convenience service to open and style a promise-based templated modal window.
 *
 * Simple Example:
 * <pre>
    angular.module('app', ['modalServiceModule'])
        .factory('someService', function($log, modalService, MODAL_BUTTON_ACTIONS) {

            modalService.open({
                title: "My Title",
                template: '<div>some content</div>',
                buttons: [{
                    label: "Close",
                    action: MODAL_BUTTON_ACTIONS.CLOSE
                }]
            }).then(function (result) {
                $log.debug("modal closed!");
                }, function (failure) {
            }
        );

    });
 * </pre>
 *
 * More complex example:
 * <pre>
 *
    angular.module('app', ['modalServiceModule'])

     .factory('someService',
        function($q, modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES) {

            modalService.open({
                title: "modal.title",
                template: '<div>some content</div>',
                controller: 'modalController',
                buttons: [
                    {
                        id: 'submit',
                        label: "Submit",
                        action: MODAL_BUTTON_ACTIONS.CLOSE
                    },
                    {
                        label: "Cancel",
                        action: MODAL_BUTTON_ACTIONS.DISMISS
                    },

                ]
            }).then(function (result) {
                $log.log("Modal closed with data:", result);
            }, function (failure) {
            });
        }
     )

     .controller('modalController', function($scope, $q) {

            function validateSomething() {
                return true;
            };

            var buttonHandlerFn = function (buttonId) {
                if (buttonId == 'submit') {
                    var deferred = $q.defer();
                    if (validateSomething()) {
                        deferred.resolve("someResult");
                    } else {
                        deferred.reject();  // cancel the submit button's close action
                    }
                    return deferred.promise;
                }
            };

            $scope.modalManager.setButtonHandler(buttonHandlerFn);

        });
 * </pre>
 */
.factory('modalService', ['$modal', '$controller', '$rootScope', '$templateCache', '$translate', '$log', 'MODAL_BUTTON_ACTIONS', 'MODAL_BUTTON_STYLES', 'merge', 'copy', 'hitch', 'generateIdentifier', function($modal, $controller, $rootScope, $templateCache, $translate, $log, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES, merge, copy, hitch, generateIdentifier) {


    /**
     * @ngdoc service
     * @name modalServiceModule.service:ModalManager
     *
     * @description
     * The ModalManager is a service designed to provide easy runtime modification to various aspects of a modal window,
     * such as the modifying the title, adding a buttons, setting callbacks, etc...
     *
     * The ModalManager constructor is not exposed publicly, but an instance of ModalManager is added to the scope of
     * the modal content implicitly through the scope chain/prototyping. As long as you don't create an
     * {@link https://docs.angularjs.org/guide/scope isolated scope} for the modal, you can access it through $scope.modalManager
     *
     * <pre>
     *  .controller('modalTestController', function($scope, $log) {
     *    var buttonHandlerFn = function (buttonId) {
     *        $log.debug("button with id", buttonId, "was pressed!");
     *    };
     *    $scope.modalManager.setButtonHandler(buttonHandlerFn);
     *    ...
     * </pre>
     *
     */
    function ModalManager(conf) {

        var buttonEventCallback;
        var showDismissX = true;
        var dismissCallback = null;
        var buttons = [];

        if (!conf.modalInstance) {
            throw 'no.modalInstance.injected';
        }
        this.closeFunction = conf.modalInstance.close;
        this.dismissFunction = conf.modalInstance.dismiss;

        this._defaultButtonOptions = {
            id: 'button.id',
            label: 'button.label',
            action: MODAL_BUTTON_ACTIONS.NONE,
            style: MODAL_BUTTON_STYLES.PRIMARY,
            disabled: false,
            callback: null
        };

        this._createButton = function(buttonConfig) {
            var defaultButtonConfig = copy(this._defaultButtonOptions);

            merge(defaultButtonConfig, buttonConfig || {});
            $translate(defaultButtonConfig.label).then(
                function(translatedValue) {
                    defaultButtonConfig.label = translatedValue;
                }
            );

            var styleValidated = false;
            for (var style in MODAL_BUTTON_STYLES) {
                if (MODAL_BUTTON_STYLES[style] === defaultButtonConfig.style) {
                    styleValidated = true;
                    break;
                }
            }
            if (!styleValidated) {
                throw 'modalService.ModalManager._createButton.illegal.button.style';
            }

            var actionValidated = false;
            for (var action in MODAL_BUTTON_ACTIONS) {
                if (MODAL_BUTTON_ACTIONS[action] === defaultButtonConfig.action) {
                    actionValidated = true;
                    break;
                }
            }
            if (!actionValidated) {
                throw 'modalService.ModalManager._createButton.illegal.button.action';
            }

            return defaultButtonConfig;
        };

        this.title = "";

        if (typeof conf.title == 'string') {
            this.title = conf.title;
        }

        if (typeof conf.titleSuffix == 'string') {
            this.titleSuffix = conf.titleSuffix;
        }

        if (conf.buttons) {
            for (var index = 0; index < conf.buttons.length; index++) {
                buttons.push(this._createButton(conf.buttons[index]));
            }
        }

        this._buttonPressed = function(button) {
            var callbackReturnedPromise = null;
            if (button.callback) {
                callbackReturnedPromise = button.callback();
            } else if (buttonEventCallback) {
                callbackReturnedPromise = buttonEventCallback(button.id);
            }
            // if there is no modal action, simply return
            if (button.action == MODAL_BUTTON_ACTIONS.NONE) {
                // do nothing
            } else {
                // by contract, callbackReturnedPromise must be a promise if it exists by this point
                var exitFn = button.action == MODAL_BUTTON_ACTIONS.CLOSE ? this.close : this.dismiss;
                if (callbackReturnedPromise) {
                    callbackReturnedPromise.then(hitch(this, function(data) {
                        exitFn.call(this, data);
                    }));
                    // if promise rejected - do nothing
                } else {
                    exitFn.call(this);
                }
            }
        };

        this._handleDismissButton = function() {
            if (dismissCallback) {
                var promise = dismissCallback();
                promise.then(hitch(this, function(result) {
                    this.dismiss(result);
                }));
            } else {
                this.dismiss();
            }
        };


        this._showDismissButton = function() {
            return showDismissX;
        };

        this._hasButtons = function() {
            return buttons.length > 0;
        };

        // -------------------------- Public API -----------------------------

        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#addButton
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {Object} conf (OPTIONAL) Button configuration
         * @param {String} [conf.id='button.id'] An ID for the button. It does not need to be unique, but it is suggested.
         * Can be used with the modal manager to enable/disable buttons, see which button is fired in the button handler, etc...
         * @param {String} [conf.label='button.label'] An i18n key that will be translated, and applied as the label of the button
         * @param {Boolean} [conf.disabled=false] Flag to enable/disable the button on the modal
         * @param {MODAL_BUTTON_STYLES} [conf.style=MODAL_BUTTON_STYLES.DEFAULT] One of {@link modalServiceModule.object:MODAL_BUTTON_STYLES MODAL_BUTTON_STYLES}
         * @param {MODAL_BUTTON_ACTIONS} [conf.action=MODAL_BUTTON_ACTIONS.NONE] One of {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS MODAL_BUTTON_ACTIONS}
         * @param {function} [conf.callback=null] A function that will be called with no parameters when the button is pressed.
         * This (optional) function may return null, undefined, or a {@link https://docs.angularjs.org/api/ng/service/$q promise}.
         * Resolving the {@link https://docs.angularjs.org/api/ng/service/$q promise} will trigger the
         * {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action} (if any), and rejecting the
         * {@link https://docs.angularjs.org/api/ng/service/$q promise} will prevent the action from being executed.
         *
         * Note: If a button has a callback and the ModalManager has registered a
         * {@link modalServiceModule.service:ModalManager#methods_setButtonHandler button handler}, only the button callback
         * will be executed on button press. This is to avoid the unnecessary complexity of having multiple handlers for a single button.
         *
         * @returns {Object} An object representing the newly added button
         */
        this.addButton = function(newButtonConf) {
            var newButton = this._createButton(newButtonConf);
            buttons.push(newButton);
            return newButton;
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#getButtons
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @description
         * Caution!
         *
         * This is a reference to the buttons being used by the modal manager, not a clone. This should
         * only be used to read or update properties provided in the Button configuration. See
         * {@link modalServiceModule.service:ModalManager#methods_addButton addButton()} for more details.
         *
         * @returns {Array} An array of all the buttons on the modal window, empty array if there are no buttons.
         */
        this.getButtons = function() {
            return buttons;
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#removeAllButtons
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @description
         * Remove all buttons from the modal window
         *
         */
        this.removeAllButtons = function() {
            buttons.splice(0, buttons.length);
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#removeButton
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {String} id The id of the button to be removed.
         *
         * @description
         * Remove a buttons from the modal window
         *
         */
        this.removeButton = function(buttonId) {
            for (var buttonIndex = buttons.length - 1; buttonIndex >= 0; buttonIndex--) {
                if (buttons[buttonIndex].id == buttonId) {
                    buttons.splice(buttonIndex, 1);
                }
            }
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#enableButton
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {String} id The id of the button to be enabled.
         *
         * @description
         * Enables a button on the modal window, allowing it to be pressed.
         *
         */
        this.enableButton = function(buttonId) {
            for (var buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
                if (buttons[buttonIndex].id == buttonId) {
                    buttons[buttonIndex].disabled = false;
                }
            }
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#disableButton
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {String} id The id of the button to be disabled.
         *
         * @description
         * Disabled a button on the modal window, preventing it from be pressed.
         *
         */
        this.disableButton = function(buttonId) {
            for (var buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
                if (buttons[buttonIndex].id == buttonId) {
                    buttons[buttonIndex].disabled = true;
                }
            }
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#getButton
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {String} id The id of the button to be fetched
         *
         * @returns {Object} The first button found with a matching id, or null
         */
        this.getButton = function(buttonId) {
            for (var buttonIndex = 0; buttonIndex < buttons.length; buttonIndex++) {
                if (buttons[buttonIndex].id == buttonId) {
                    return buttons[buttonIndex];
                }
            }
            return null;
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#setShowHeaderDismiss
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {boolean} showX Flag to show/hide the X dismiss button at the top right corner of the modal window,
         * when the modal header is displayed
         *
         */
        this.setShowHeaderDismiss = function(showButton) {
            if (typeof showButton == 'boolean') {
                showDismissX = showButton;
            } else {
                throw 'modalService.ModalManager.showDismissX.illegal.param';
            }
        };


        /**
         * @ngdoc method
         * @name modalServiceModule.service:ModalManager#setDismissCallback
         * @methodOf modalServiceModule.service:ModalManager
         *
         * @param {function} dismissCallback A function to be called when the X dismiss button at the top right corner of the modal window
         * is pressed. This function must either return null or a {@link https://docs.angularjs.org/api/ng/service/$q promise}.
         *
         * If the {@link https://docs.angularjs.org/api/ng/service/$q promise} is resolved, or if the function returns null or undefined, then the modal is closed and the returned
         * modal {@link https://docs.angularjs.org/api/ng/service/$q promise} is rejected.
         *
         * If the callback {@link https://docs.angularjs.org/api/ng/service/$q promise} is rejected, the modal is not closed, allowing you to provide some kind of validation
         * before closing.
         *
         */
        this.setDismissCallback = function(dismissCallbackFunction) {
            dismissCallback = dismissCallbackFunction;
        };


        /**
             * @ngdoc method
             * @name modalServiceModule.service:ModalManager#setButtonHandler
             * @methodOf modalServiceModule.service:ModalManager
             *
             * @description
             *
             * @param {Function} buttonPressedCallback The buttonPressedCallback is a function that is called when any button on the
             * modal, that has no {@link modalServiceModule.service:ModalManager#methods_addButton button callback}, is pressed. If a button has a
             * {@link modalServiceModule.service:ModalManager#methods_addButton button callback} function, then that function will be
             * called instead of the buttonPressedCallback.
             *
             * This buttonPressedCallback receives a single parameter, which is the string ID of the button that was pressed.
             * Additionally, this function must either return null, undefined or a {@link https://docs.angularjs.org/api/ng/service/$q promise}.
             *
             * If null/undefined is return, the modal will continue to process the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}
             * In this case, no data will be returned to the modal {@link https://docs.angularjs.org/api/ng/service/$q promise} if the modal is closed.
             *
             * If a promise is returned by this function, then the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}
             * may be cancelled/ignored by rejecting the promise. If the promise is resolved, the {@link modalServiceModule.service:ModalManager ModalManager}
             * will continue to process the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}.
             *
             * If by resolving the promise returned by the buttonHandlerFunction with data passed to the resolve, and the {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button action}
             * is such that it results in the modal closing, then the modal promise is resolved/rejected with that same data. This allows you to pass data from the  buttonHandlerFunction
             * the the modalService.open(...) caller.
             *
             * See {@link modalServiceModule.service:ModalManager#methods_addButton addButton() for more details on the button callback }
             *
             *
             * A few scenarios for example:
             * #1 A button with a button callback is pressed.
             * <br/>Result: buttonPressedCallback is never called.
             *
             * #2 A button is pressed, buttonPressedCallback return null
             * <br/>Result: The modal manager will execute any action on the button
             *
             * #3 A button is pressed, buttonPressedCallback returns a promise, that promise is rejected
             * <br/>Result: Modal Manager will ignore the button action and nothing else will happen
             *
             * #4 A button with a dismiss action is pressed, buttonPressedCallback returns a promise, and that promise is resolved with data "Hello"
             * <br/>Result: ModalManager will execute the dismiss action, closing the modal, and errorCallback of the modal promise, passing "Hello" as data
             *
             *
             * Code sample of validating some data before closing the modal
             * <pre>
             function validateSomething() {
        return true;
    };

             var buttonHandlerFn = function (buttonId) {
        if (buttonId == 'submit') {
            var deferred = $q.defer();
            if (validateSomething()) {
                deferred.resolve("someResult");
            } else {
                deferred.reject();  // cancel the submit button's close action
            }
            return deferred.promise;
        }
    };

             $scope.modalManager.setButtonHandler(buttonHandlerFn);
             * </pre>
             */
        this.setButtonHandler = function(buttonHandlerFunction) {
            buttonEventCallback = buttonHandlerFunction;
        };

    }

    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#close
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     * The close function will close the modal window, passing the provided data (if any) to the successCallback
     * of the modal {@link https://docs.angularjs.org/api/ng/service/$q promise} by resolving the {@link https://docs.angularjs.org/api/ng/service/$q promise}.
     *
     * @param {Object} data Any data to be returned to the resolved modal {@link https://docs.angularjs.org/api/ng/service/$q promise} when the modal is closed.
     *
     */
    ModalManager.prototype.close = function(dataToReturn) {
        if (this.closeFunction) {
            this.closeFunction(dataToReturn);
        }
    };


    /**
     * @ngdoc method
     * @name modalServiceModule.service:ModalManager#dismiss
     * @methodOf modalServiceModule.service:ModalManager
     *
     * @description
     * The dismiss function will close the modal window, passing the provided data (if any) to the {@link https://docs.angularjs.org/api/ng/service/$q errorCallback}
     * of the modal {@link https://docs.angularjs.org/api/ng/service/$q promise} by rejecting the {@link https://docs.angularjs.org/api/ng/service/$q promise}.
     *
     * @param {Object} data Any data to be returned to the rejected modal {@link https://docs.angularjs.org/api/ng/service/$q promise} when the modal is closed.
     *
     */
    ModalManager.prototype.dismiss = function(dataToReturn) {
        if (this.dismissFunction) {
            this.dismissFunction(dataToReturn);
        }
    };


    function getControllerClass(conf) {
        var that = this;
        return ['$scope', '$modalInstance', function ModalController($scope, $modalInstance) {
            conf.modalInstance = $modalInstance;

            this._modalManager = new ModalManager(conf);
            that._modalManager = this._modalManager;
            $scope.modalController = this;

            if (conf.controller) {
                angular.extend(this, $controller(conf.controller, {
                    $scope: $scope,
                    modalManager: this._modalManager
                }));

                if (this.init) {
                    this.init();
                }
            }
            if (conf.controllerAs) {
                $scope[conf.controllerAs] = this;
            }

            if (conf.templateInline) {
                this.templateUrl = "modalTemplateKey" + btoa(generateIdentifier());
                $templateCache.put(this.templateUrl, conf.templateInline);
            } else {
                this.templateUrl = conf.templateUrl;
            }

            this.close = hitch(this, function(data) {
                this._modalManager.close(data);
                $templateCache.remove(this.templateUrl);
            });

            this.dismiss = hitch(this, function(data) {
                this._modalManager.dismiss(data);
                $templateCache.remove(this.templateUrl);
            });
        }];
    }


    function ModalOpener() {}


    /**
     * @ngdoc method
     * @name modalServiceModule.modalService#open
     * @methodOf modalServiceModule.modalService
     *
     * @description
     * Open provides a simple way to open modal windows with custom content, that share a common look and feel.
     *
     * The modal window can be closed multiple ways, through {@link modalServiceModule.object:MODAL_BUTTON_ACTIONS button actions},
     * by explicitly calling the {@link modalServiceModule.service:ModalManager#methods_close close} or
     * {@link modalServiceModule.service:ModalManager#methods_close dismiss} functions, etc... Depending on how you
     * choose to close a modal, either the modal {@link https://docs.angularjs.org/api/ng/service/$q promise's}
     * {@link https://docs.angularjs.org/api/ng/service/$q successCallback} or {@link https://docs.angularjs.org/api/ng/service/$q errorCallback}
     * will be called. You can use the callbacks to return data from the modal content to the caller of this function.
     *
     * @param {Object} conf configuration
     * @param {String} conf.title (OPTIONAL) key for your modal title to be translated
     * @param {Array} conf.buttons (OPTIONAL) Array of button configurations. See {@link modalServiceModule.service:ModalManager#methods_addButton ModalManager.addButton()} for config details.
     * @param {String} conf.templateUrl path to an HTML fragment you mean to display in the modal window
     * @param {String} conf.templateInline inline HTML fragment you mean to display in the the modal window
     * @param {function} conf.controller the piece of logic that acts as controller of your template. It can be declared through its ID in AngularJS dependency injection or through explicit function
     * @param {String} conf.cssClasses space separated list of additional css classed to be added to the overall modal
     *
     * @returns {function} {@link https://docs.angularjs.org/api/ng/service/$q promise} that will either be resolved or
     * rejected when the modal window is closed.
     */
    ModalOpener.prototype.open = function(conf) {
        var configuration = conf || {};

        if (!configuration.templateUrl && !configuration.templateInline) {
            throw "modalService.configuration.errors.no.template.provided";
        }
        if (configuration.templateUrl && configuration.templateInline) {
            throw "modalService.configuration.errors.2.templates.provided";
        }

        return $modal.open({
            templateUrl: 'web/common/services/modalTemplate.html',
            size: configuration.size || 'lg',
            backdrop: 'static',
            keyboard: false,
            controller: getControllerClass.call(this, configuration),
            controllerAs: 'modalController',
            windowClass: configuration.cssClasses || null
        }).result;
    };

    return new ModalOpener();
}]);

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
.factory('previewTicketInterceptor', ['parseQuery', 'interceptorHelper', function(parseQuery, interceptorHelper) {
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
    }])
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('previewTicketInterceptor');
    }]);

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
(function() {

    var STORE_FRONT_CONTEXT_VAR = '/storefront';

    var CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
    var CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';
    var CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';

    angular.module('resourceLocationsModule', [])

    .constant('CONTEXT_CATALOG', CONTEXT_CATALOG)
        .constant('CONTEXT_CATALOG_VERSION', CONTEXT_CATALOG_VERSION)
        .constant('CONTEXT_SITE_ID', CONTEXT_SITE_ID)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SMARTEDIT_ROOT
         *
         * @description
         * the name of the webapp root context
         */
        .constant('SMARTEDIT_ROOT', 'smartedit')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SMARTEDIT_RESOURCE_URI_REGEXP
         *
         * @description
         * to calculate platform domain URI, this regular expression will be used
         */
        .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smartedit/)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CONFIGURATION_URI
         *
         * @description
         * the name of the SmartEdit configuration API root
         */
        .constant('CONFIGURATION_URI', '/smartedit/configuration/:key')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:CONFIGURATION_COLLECTION_URI
     *
     * @description
     * The SmartEdit configuration collection API root
     */
    .constant('CONFIGURATION_COLLECTION_URI', '/smartedit/configuration')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:CMSWEBSERVICES_RESOURCE_URI
     *
     * @description
     * Constant for the cmswebservices API root
     */
    .constant('CMSWEBSERVICES_RESOURCE_URI', '/cmswebservices')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:DEFAULT_AUTHENTICATION_ENTRY_POINT
         *
         * @description
         * When configuration is not available yet to provide authenticationMap, one needs a default authentication entry point to access configuration API itself
         */
        .constant('DEFAULT_AUTHENTICATION_ENTRY_POINT', '/authorizationserver/oauth/token')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:DEFAULT_AUTHENTICATION_CLIENT_ID
         *
         * @description
         * The default OAuth 2 client id to use during authentication.
         */
        .constant('DEFAULT_AUTHENTICATION_CLIENT_ID', 'smartedit')
        /**
         * Root resource URI of i18n API 
         */
        .constant('I18N_ROOT_RESOURCE_URI', '/smarteditwebservices/v1/i18n')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:I18N_RESOURCE_URI
         *
         * @description
         * Resource URI to fetch the i18n initialization map for a given locale.
         */
        .constant('I18N_RESOURCE_URI', '/smarteditwebservices/v1/i18n/translations')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:I18N_LANGUAGE_RESOURCE_URI
         *
         * @description
         * Resource URI to fetch the supported i18n languages.
         */
        .constant('I18N_LANGUAGES_RESOURCE_URI', '/smarteditwebservices/v1/i18n/languages')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:LANGUAGE_RESOURCE_URI
         *
         * @description
         * Resource URI of the languages REST service.
         */
        .constant('LANGUAGE_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/languages')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CATALOG_VERSION_DETAILS_RESOURCE_URI
         *
         * @description
         * Resource URI of the catalog version details REST service.
         */
        .constant('CATALOG_VERSION_DETAILS_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/catalogversiondetails')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SITES_RESOURCE_URI
         *
         * @description
         * Resource URI of the sites REST service.
         */
        .constant('SITES_RESOURCE_URI', '/cmswebservices/v1/sites')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:LANDING_PAGE_PATH
         *
         * @description
         * Path of the landing page
         */
        .constant('LANDING_PAGE_PATH', '/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STOREFRONT_PATH
         *
         * @description
         * Path of the storefront
         */
        .constant('STOREFRONT_PATH', STORE_FRONT_CONTEXT_VAR + '/:siteId/:catalogId/:catalogVersion/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STOREFRONT_PATH_WITH_PAGE_ID
         *
         * @description
         * Path of the storefront with a page ID
         */
        .constant('STOREFRONT_PATH_WITH_PAGE_ID', STORE_FRONT_CONTEXT_VAR + '/:siteId/:catalogId/:catalogVersion/:pageId')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:STORE_FRONT_CONTEXT
         *
         * @description
         * to fetch the store front context for inflection points.
         */
        .constant('STORE_FRONT_CONTEXT', STORE_FRONT_CONTEXT_VAR)
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CATALOGS_PATH
         *
         * @description
         * Path of the catalogs
         */
        .constant('CATALOGS_PATH', '/cmswebservices/v1/catalogs/')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:MEDIA_PATH
         *
         * @description
         * Path of the media
         */
        .constant('MEDIA_PATH', '/cmswebservices/v1/media')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:CMSWEBSERVICES_PATH
         *
         * @description
         * Path of the cmswebservices
         */
        .constant('CMSWEBSERVICES_PATH', '/cmswebservices')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PREVIEW_RESOURCE_URI
         *
         * @description
         * Path of the preview ticket API
         */
        .constant('PREVIEW_RESOURCE_URI', '/previewwebservices/v1/preview')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:ENUM_RESOURCE_URI
         *
         * @description
         * Path to fetch list of values of a given enum type
         */
        .constant('ENUM_RESOURCE_URI', "/cmswebservices/v1/enums")
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:PERMISSIONSWEBSERVICES_RESOURCE_URI
         *
         * @description
         * Path to fetch permissions of a given type
         */
        .constant('USER_GLOBAL_PERMISSIONS_RESOURCE_URI', "/permissionswebservices/v1/permissions/principals/:user/global")
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:SYNC_PATH
         *
         * @description
         * Path of the synchronization service
         */
        .constant('SYNC_PATH', '/cmswebservices/v1/catalogs/:catalog/versions/Staged/synchronizations/versions/Online')
        /**
         * @ngdoc object
         * @name resourceLocationsModule.object:MEDIA_RESOURCE_URI
         *
         * @description
         * Resource URI of the media REST service.
         */
        .constant('MEDIA_RESOURCE_URI', '/cmswebservices/v1/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/media')

    /**
     * @ngdoc service
     * @name resourceLocationsModule.resourceLocationToRegex
     *
     * @description
     * Generates a regular expresssion matcher from a given resource location URL, replacing predefined keys by wildcard
     * matchers.
     *
     * Example:
     * <pre>
     *     // Get a regex matcher for the someResource endpoint, ie: /\/smarteditwebservices\/someResource\/.*$/g
     *     var endpointRegex = resourceLocationToRegex('/smarteditwebservices/someResource/:id');
     *
     *     // Use the regex to match hits to the mocked HTTP backend. This regex will match for any ID passed in to the
     *     // someResource endpoint.
     *     $httpBackend.whenGET(endpointRegex).respond({someKey: 'someValue'});
     * </pre>
     */
    .factory('resourceLocationToRegex', function() {
        return function(str) {
            return new RegExp(str.replace(/\/:[^\/]*/g, '/.*'));
        };
    });
})();

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
    .factory('restServiceFactory', ['$resource', '$q', 'hitch', 'copy', 'isBlank', function($resource, $q, hitch, copy, isBlank) {

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
    }]);

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
(function() {

    /**
     * @ngdoc overview
     * @name tabsetModule
     * @description
     *
     * The Tabset module provides the directives required to display a group of tabsets within a tabset. The
     * {@link tabsetModule.directive:yTabset yTabset} is of particular interest to SmartEdit developers
     * because this directive is responsible for displaying and organizing tabs.
     *
     */
    angular.module('tabsetModule', ['ui.bootstrap', 'coretemplates', 'functionsModule', 'eventServiceModule', 'translationServiceModule'])

    /**
     * @ngdoc directive
     * @name tabsetModule.directive:yTabset
     * @scope
     * @restrict E
     * @element smartedit-tabset
     *
     * @description
     * The directive responsible for displaying and organizing tabs within a tabset. A specified number of tabs will
     * display a tab header. If there are more tabs than the maximum number defined, the remaining tabs will be grouped
     * in a drop-down menu with the header "More". When a user clicks on a tab header or an item from the drop-down
     * menu, the content of the tabset changes to the body of the selected tab.
     *
     * Note: The body of each tab is wrapped within a {@link tabsetModule.directive:yTab yTab} directive.
     *
     * @param {Object} model Custom data to be passed to each tab. Neither the smartedit-tabset directive or the
     * smartedit-tab directive can modify this value. The tabs' contents determine how to parse and use this object.
     * @param {Object[]} tabsList A list that contains the configuration for each of the tabs to be displayed in the tabset.
     * @param {string} tabsList.id The ID used to track the tab within the tabset.
     * @param {String} tabsList.title The tab header.
     * @param {String} tabsList.templateUrl Path to the HTML fragment to be displayed as the tab content.
     * @param {boolean} tabsList.hasErrors Flag that indicates whether a visual error is to be displayed in the tab or not.
     * @param {Function} tabControl  An optional parameter. A function that will be called with scope as its parameter.
     * It allows the caller to register extra functionality to be executed in the child tabs.
     * @param {Number} numTabsDisplayed The number of tabs for which tab headers will be displayed. The remaining tab
     * headers will be grouped within the 'MORE' drop-down menu.
     *
     */
    .directive('yTabset', ['$log', '$q', 'systemEventService', function($log, $q, systemEventService) {
        return {
            restrict: 'E',
            transclude: false,
            templateUrl: 'web/common/services/tabset/yTabsetTemplate.html',
            scope: {
                model: '=',
                tabsList: '=',
                tabControl: '=',
                numTabsDisplayed: '@'
            },
            link: function(scope, elem, attr) {

                var prevValue = 0;

                scope.$watch(
                    function() {
                        return (prevValue !== scope.tabsList.length);
                    },
                    function() {
                        scope.initializeTabs();
                        prevValue = scope.tabsList.length;
                    });

                scope.initializeTabs = function() {
                    if (scope.tabsList && scope.tabsList.length > 0) {

                        scope.selectedTab = scope.tabsList[0];

                        for (var tabIdx in scope.tabsList) {
                            var tab = scope.tabsList[tabIdx];
                            tab.active = (tabIdx === '0');
                            tab.hasErrors = false;
                        }
                    }
                };

                scope.selectTab = function(tabToSelect) {
                    if (tabToSelect) {
                        scope.selectedTab.active = false;
                        scope.selectedTab = tabToSelect;
                        scope.selectedTab.active = true;
                    }
                };

                scope.dropDownHasErrors = function() {
                    var tabsInDropDown = scope.tabsList.slice(scope.numTabsDisplayed - 1);

                    return tabsInDropDown.some(function(tab) {
                        return tab.hasErrors;
                    });
                };

                scope.markTabsInError = function(tabsInErrorList) {
                    scope.resetTabErrors();

                    var tabId;
                    var tabFilter = function(tab) {
                        return (tab.id === tabId);
                    };

                    for (var idx in tabsInErrorList) {
                        tabId = tabsInErrorList[idx];
                        var resultTabs = scope.tabsList.filter(tabFilter);

                        if (resultTabs[0]) {
                            resultTabs[0].hasErrors = true;
                        }
                    }
                };

                scope.resetTabErrors = function() {
                    for (var tabKey in scope.tabsList) {
                        scope.tabsList[tabKey].hasErrors = false;
                    }
                };

                scope.initializeTabs();
            }
        };
    }])

    /**
     * @ngdoc directive
     * @name tabsetModule.directive:yTab
     * @scope
     * @restrict E
     * @element smartedit-tab
     *
     * @description
     * The directive  responsible for wrapping the content of a tab within a
     * {@link tabsetModule.directive:yTabset yTabset} directive.
     *
     * @param {Number} tabId The ID used to track the tab within the tabset. It must match the ID used in the tabset.
     * @param {String} content Path to the HTML fragment to be displayed as the tab content.
     * @param {Object} model Custom data. Neither the smartedit-tabset directive or the smartedit-tab directive
     * can modify this value. The tabs' contents determine how to parse and use this object.
     * @param {function} tabControl An optional parameter. A function that will be called with scope as its parameter.
     * It allows the caller to register extra functionality to be executed in the tab.
     *
     */
    .directive('yTab', ['$log', '$templateCache', '$compile', '$q', 'hitch', 'systemEventService', function($log, $templateCache, $compile, $q, hitch, systemEventService) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                content: '=',
                model: '=',
                tabControl: '=',
                tabId: '@'
            },
            link: function(scope, elem, attr) {

                var template = $templateCache.get(scope.content);
                var compile = $compile(template);
                var compiled = compile(scope);
                elem.append(compiled);

                if (scope.tabControl) {
                    hitch(this, scope.tabControl(scope));
                }
            }
        };
    }]);
})();

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
 * @ngdoc service
 * @name translationServiceModule
 *
 * @description
 * 
 * This module is used to configure the translate service, the filter, and the directives from the 'pascalprecht.translate' package. The configuration consists of:
 * 
 * <br/>- Initializing the translation map from the {@link i18nInterceptorModule.object:I18NAPIROOT I18NAPIROOT} constant.
 * <br/>- Setting the preferredLanguage to the {@link i18nInterceptorModule.object:UNDEFINED_LOCALE UNDEFINED_LOCALE} so that the {@link i18nInterceptorModule.service:i18nInterceptor#methods_request i18nInterceptor request} can replace it with the appropriate URI combined with the runtime browser locale retrieved from the {@link languageServiceModule.service:languageService#methods_getBrowserLocale languageService.getBrowserLocale}, which is unaccessible at configuration time.
 */
angular.module('translationServiceModule', ['pascalprecht.translate', 'i18nInterceptorModule'])
    .config(['$translateProvider', 'I18NAPIROOT', 'UNDEFINED_LOCALE', function($translateProvider, I18NAPIROOT, UNDEFINED_LOCALE) {

        /*
         * hard coded url that is always intercepted by i18nInterceptor so as to replace by value from configuration REST call
         */
        $translateProvider.useStaticFilesLoader({
            prefix: '/' + I18NAPIROOT + '/',
            suffix: ''
        });

        // Tell the module what language to use by default
        $translateProvider.preferredLanguage(UNDEFINED_LOCALE);

    }]);

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

angular.module('wizardServiceModule', ['ui.bootstrap', 'translationServiceModule', 'functionsModule', 'coretemplates', 'modalServiceModule'])

.factory('wizardActions', ['$log', function($log) {

    var defaultAction = {
        id: "wizard_action_id",
        i18n: 'wizard_action_label',
        isMainAction: true,
        enableIfCondition: function() {
            return true;
        },
        executeIfCondition: function() {
            return true;
        },
        execute: function(wizardService) {}
    };

    function newAction(conf) {
        var defaultCopy = angular.copy(defaultAction);
        return angular.merge(defaultCopy, conf);
    }

    return {

        customAction: function(conf) {
            return newAction(conf);
        },

        done: function(conf) {
            var nextConf = {
                id: 'ACTION_DONE',
                i18n: 'action.done',
                execute: function(wizardService) {
                    wizardService.close();
                }
            };
            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        next: function(conf) {
            var nextConf = {
                id: 'ACTION_NEXT',
                i18n: 'action.next',
                execute: function(wizardService) {
                    if (this.nextStepId) {
                        wizardService.goToStepWithId(this.nextStepId);
                    } else if (this.nextStepIndex) {
                        wizardService.goToStepWithIndex(this.nextStepIndex);
                    } else {
                        wizardService.goToStepWithIndex(wizardService.getCurrentStepIndex() + 1);
                    }
                }
            };

            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        navBarAction: function(conf) {
            if (!conf.wizardService || conf.destinationIndex === null) {
                throw "Error initializating navBarAction, must provide the wizardService and destinationIndex fields";
            }

            var nextConf = {
                id: 'ACTION_GOTO',
                i18n: 'action.goto',
                enableIfCondition: function() {
                    return conf.wizardService.getCurrentStepIndex() >= conf.destinationIndex;
                },
                execute: function(wizardService) {
                    wizardService.goToStepWithIndex(conf.destinationIndex);
                }
            };

            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        back: function(conf) {
            var nextConf = {
                id: 'ACTION_BACK',
                i18n: 'action.back',
                isMainAction: false,
                execute: function(wizardService) {
                    if (this.backStepId) {
                        wizardService.goToStepWithId(this.backStepId);
                    } else if (this.backStepIndex) {
                        wizardService.goToStepWithIndex(this.backStepIndex);
                    } else {
                        var currentIndex = wizardService.getCurrentStepIndex();
                        if (currentIndex <= 0) {
                            throw "Failure to execute BACK action, no previous index exists!";
                        }
                        wizardService.goToStepWithIndex(currentIndex - 1);
                    }
                }
            };

            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        cancel: function() {
            return newAction({
                id: 'ACTION_CANCEL',
                i18n: 'action.cancel',
                isMainAction: false,
                execute: function(wizardService) {
                    wizardService.cancel();
                }
            });
        }

    };
}])

.service('modalWizard', ['modalService', 'modalWizardControllerFactory', function(modalService, modalWizardControllerFactory) {

    this.validateConfig = function(config) {
        if (!config.controller) {
            throw "WizardService - initialization exception. No controller provided";
        }
    };

    this.open = function(config) {
        this.validateConfig(config);
        return modalService.open({
            templateUrl: 'web/common/services/wizard/modalWizardTemplate.html',
            controller: modalWizardControllerFactory.fromConfig(config),
            controllerAs: 'wizardController'
        });
    };
}])


.service('modalWizardControllerFactory', ['$controller', 'wizardServiceFactory', 'wizardActions', 'MODAL_BUTTON_STYLES', '$q', function($controller, wizardServiceFactory, wizardActions, MODAL_BUTTON_STYLES, $q) {

    this.fromConfig = function(config) {
        return ['$scope', '$rootScope', 'modalManager',
            function WizardController($scope, $rootScope, modalManager) {

                var wizardServiceImpl = wizardServiceFactory.newWizardService();

                angular.extend(this, $controller(config.controller, {
                    $scope: $scope,
                    wizardManager: wizardServiceImpl
                }));
                if (config.controllerAs) {
                    $scope[config.controllerAs] = this;
                }

                this._wizardContext = {};
                if (typeof this.getWizardConfig !== 'function') {
                    throw "The provided controller must provide a getWizardConfig() function.";
                }
                var modalConfig = this.getWizardConfig();
                var controller = this;

                this.executeAction = function(action) {
                    wizardServiceImpl.getActionExecutor().executeAction(action);
                };

                function setupNavBar(steps) {
                    controller._wizardContext.navActions = steps.map(function(step, index) {
                        return wizardActions.navBarAction({
                            stepIndex: index,
                            wizardService: wizardServiceImpl,
                            destinationIndex: index,
                            i18n: step.name,
                            isCurrentStep: function() {
                                return this.stepIndex === wizardServiceImpl.getCurrentStepIndex();
                            }
                        });
                    });
                }

                function setupModal(modalConfig) {
                    controller._wizardContext.templateOverride = modalConfig.templateOverride;
                    if (modalConfig.cancelAction) {
                        modalManager.setDismissCallback(function() {
                            wizardServiceImpl.getActionExecutor().executeAction(modalConfig.cancelAction);
                            return $q.reject();
                        });
                    }
                    if (modalConfig.cancelAction) {
                        modalManager.setDismissCallback(function() {
                            wizardServiceImpl.getActionExecutor().executeAction(modalConfig.cancelAction);
                            return $q.reject();
                        });
                    }

                    // strategy stuff TODO - move to strategy layer
                    setupNavBar(modalConfig.steps);
                }

                function actionToButtonConf(action) {
                    return {
                        id: action.id,
                        style: action.isMainAction ? MODAL_BUTTON_STYLES.PRIMARY : MODAL_BUTTON_STYLES.SECONDARY,
                        label: action.i18n,
                        callback: function() {
                            wizardServiceImpl.getActionExecutor().executeAction(action);
                        }
                    };
                }

                wizardServiceImpl.onLoadStep = function(stepIndex, step) {
                    modalManager.title = step.title;
                    controller._wizardContext.templateUrl = step.templateUrl;
                    modalManager.removeAllButtons();
                    (step.actions || []).forEach(function(action) {

                        if (typeof action.enableIfCondition === 'function') {
                            // TODO - this has pretty bad implications if someone tries to do server side validation, and asynch is not possible I guess
                            $rootScope.$watch(action.enableIfCondition, function(newVal, oldVal, scope) {
                                if (newVal) {
                                    modalManager.enableButton(action.id);
                                } else {
                                    modalManager.disableButton(action.id);
                                }
                            });
                        }
                        modalManager.addButton(actionToButtonConf(action));
                    }.bind(this));
                };

                wizardServiceImpl.onClose = function(result) {
                    modalManager.close(result);
                };

                wizardServiceImpl.onCancel = function() {
                    modalManager.dismiss();
                };

                wizardServiceImpl.onStepsUpdated = function(steps) {
                    setupNavBar(steps);
                };

                wizardServiceImpl.initialize(modalConfig);
                setupModal(modalConfig);

            }
        ];
    };
}])


.service('defaultWizardActionStrategy', ['wizardActions', function(wizardActions) {

    function applyOverrides(wizardService, action, label, executeCondition, enableCondition) {
        if (label) {
            action.i18n = label;
        }
        if (executeCondition) {
            action.executeIfCondition = function() {
                return executeCondition(wizardService.getCurrentStepId());
            };
        }
        if (enableCondition) {
            action.enableIfCondition = function() {
                return enableCondition(wizardService.getCurrentStepId());
            };
        }
        return action;
    }

    this.applyStrategy = function(wizardService, conf) {
        var nextAction = applyOverrides(wizardService, wizardActions.next(), conf.nextLabel, conf.onNext, conf.isFormValid);
        var doneAction = applyOverrides(wizardService, wizardActions.done(), conf.doneLabel, conf.onDone, conf.isFormValid);

        var backConf = conf.backLabel ? {
            i18n: conf.backLabel
        } : null;
        var backAction = wizardActions.back(backConf);

        conf.steps.forEach(function(step, index) {
            step.actions = [];
            if (index > 0) {
                step.actions.push(backAction);
            }
            if (index === (conf.steps.length - 1)) {
                step.actions.push(doneAction);
            } else {
                step.actions.push(nextAction);
            }
        });

        conf.cancelAction = applyOverrides(wizardService, wizardActions.cancel(), conf.cancelLabel, conf.onCancel, null);
        conf.templateOverride = 'web/common/services/wizard/modalWizardNavBarTemplate.html';
    };
}])

.service('wizardServiceFactory', ['WizardService', function(WizardService) {
    this.newWizardService = function() {
        return new WizardService();
    };
}])


.factory('WizardService', ['$q', 'defaultWizardActionStrategy', 'generateIdentifier', function($q, defaultWizardActionStrategy, generateIdentifier) {

    function validateConfig(config) {
        if (!config.steps || config.steps.length <= 0) {
            throw "Invalid WizardService configuration - no steps provided";
        }

        config.steps.forEach(function(step) {
            if (!step.templateUrl) {
                throw "Invalid WizardService configuration - Step missing a url: " + step;
            }
        });
    }

    function validateStepUids(steps) {
        var stepIds = {};
        steps.forEach(function(step) {
            if (step.id === undefined && step.id === null) {
                step.id = generateIdentifier();
            } else {
                if (stepIds[step.id]) {
                    throw "Invalid (Duplicate) step id: " + step.id;
                }
                stepIds[step.id] = step.id;
            }
        });
    }

    function WizardService() {
        // the overridable callbacks
        this.onLoadStep = function(step) {};
        this.onClose = function() {};
        this.onCancel = function() {};
        this.onStepsUpdated = function() {};
    }

    WizardService.prototype.initialize = function(conf) {

        validateConfig(conf);

        this._actionStrategy = conf.actionStrategy || defaultWizardActionStrategy;
        this._actionStrategy.applyStrategy(this, conf);

        this._currentIndex = 0;
        this._conf = angular.copy(conf);
        this._steps = this._conf.steps;
        this._getResult = conf.resultFn;
        validateStepUids(this._steps);

        this.goToStepWithIndex(0);
    };

    WizardService.prototype.goToStepWithIndex = function(index) {
        var nextStep = this.getStepWithIndex(index);
        if (nextStep) {
            this.onLoadStep(index, nextStep);
            this._currentIndex = index;
        }
    };

    WizardService.prototype.getActionExecutor = function() {
        return this;
    };

    WizardService.prototype.goToStepWithId = function(id) {
        this.goToStepWithIndex(this.getStepIndexFromId(id));
    };

    WizardService.prototype.addStep = function(newStep, index) {
        if (newStep.id !== 0 && !newStep.id) {
            newStep.id = generateIdentifier();
        }
        if (!index) {
            index = 0;
        }
        if (this._currentIndex >= index) {
            this._currentIndex++;
        }
        this._steps.splice(index, 0, newStep);
        validateStepUids(this._steps);
        this._actionStrategy.applyStrategy(this, this._conf);
        this.onStepsUpdated(this._steps);
    };

    WizardService.prototype.removeStepById = function(id) {
        this.removeStepByIndex(this.getStepIndexFromId(id));
    };

    WizardService.prototype.removeStepByIndex = function(index) {
        if (index >= 0 && index < this.getStepsCount()) {
            this._steps.splice(index, 1);
            if (index === this._currentIndex) {
                this.goToStepWithIndex(0);
            }
            this._actionStrategy.applyStrategy(this, this._conf);
            this.onStepsUpdated(this._steps);
        }
    };

    WizardService.prototype.close = function() {
        var result;
        if (typeof this._getResult === 'function') {
            result = this._getResult();
        }
        this.onClose(result);
    };

    WizardService.prototype.cancel = function() {
        this.onCancel();
    };

    WizardService.prototype.executeAction = function(action) {
        if (action.executeIfCondition) {
            $q.when(action.executeIfCondition()).then(function(result) {
                return $q.when(action.execute(this));
            }.bind(this));
        } else {
            return $q.when(action.execute(this));
        }

    };

    WizardService.prototype.getSteps = function() {
        return this._steps;
    };


    // Helpers ------------------------------------------------

    WizardService.prototype.getStepIndexFromId = function(stepId) {
        var index = this._steps.findIndex(function(step) {
            return step.id === stepId;
        });
        return index;
    };

    WizardService.prototype.containsStep = function(stepId) {
        return this.getStepIndexFromId(stepId) >= 0;
    };

    WizardService.prototype.getCurrentStepId = function() {
        return this.getCurrentStep().id;
    };

    WizardService.prototype.getCurrentStepIndex = function() {
        return this._currentIndex;
    };

    WizardService.prototype.getCurrentStep = function() {
        return this.getStepWithIndex(this._currentIndex);
    };

    WizardService.prototype.getStepsCount = function() {
        return this._steps.length;
    };

    WizardService.prototype.getStepWithId = function(id) {
        var index = this.getStepIndexFromId(id);
        if (index >= 0) {
            return this.getStepWithIndex(index);
        }
    };

    WizardService.prototype.getStepWithIndex = function(index) {
        if (index >= 0 && index < this.getStepsCount()) {
            return this._steps[index];
        }
        throw ("wizardService.getStepForIndex - Index out of bounds: " + index);
    };

    return WizardService;
}]);

angular.module('coretemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/common/core/services/loginDialog.html',
    "<div data-ng-cloak data-ng-class=\"{'smartEditLogin':modalController.initialized!==true, 'smartEditLoginModal':modalController.initialized!==false, 'vertical-center':true}\">\n" +
    "    <div class=\"ySELoginContent\">\n" +
    "        <div class=\"row text-center ySELogo\">\n" +
    "            <img src=\"static-resources/images/SmartEditLogo.png\">\n" +
    "        </div>\n" +
    "        <form data-ng-submit=\"modalController.submit(loginDialogForm)\" class=\"signinForm\" name='loginDialogForm' novalidate>\n" +
    "            <div class=\"row ySELoginAuthMessage\" data-ng-show=\"modalController.initialized!==false\">\n" +
    "                <div data-ng-class=\"{'col-xs-8 col-xs-push-2 text-center':true}\" data-translate=\"logindialogform.reauth.message1\"></div>\n" +
    "                <div data-ng-class=\"{'col-xs-8 col-xs-push-2 text-center':true}\" data-translate=\"logindialogform.reauth.message2\"></div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group hyLoginError\" data-ng-if=\"loginDialogForm.errorMessage && loginDialogForm.posted && (loginDialogForm.$invalid || loginDialogForm.failed)\">\n" +
    "                <div data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false, 'alert alert-error-bluebg errorDiv':true}\" id=\"invalidError\"> {{loginDialogForm.errorMessage | translate}}</div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group \">\n" +
    "                <input data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false}\" type=\"text\" id=\"username_{{modalController.authURIKey}}\" name=\"username\" placeholder=\"{{ 'authentication.form.input.username' | translate}}\" autofocus data-ng-model=\"modalController.auth.username\" required/>\n" +
    "            </div>\n" +
    "            <div class=\"row form-group\">\n" +
    "                <input data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false}\" type=\"password\" id='password_{{modalController.authURIKey}}' name='password' placeholder=\"{{ 'authentication.form.input.password' | translate}}\" data-ng-model=\"modalController.auth.password\" required/>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group\">\n" +
    "                <language-selector data-ng-class=\"{'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false}\"></language-selector>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row form-group top-buffer2\">\n" +
    "                <button data-ng-class=\"{'btn btn-lg btn-primary':true, 'col-xs-12 col-sm-8 col-md-6 col-sm-push-2 col-md-push-3 ':modalController.initialized!==true, 'col-xs-8 col-xs-push-2':modalController.initialized!==false, 'text-uppercase':true}\" id=\"submit_{{modalController.authURIKey}}\" name=\"submit\" type=\"submit\" data-translate=\"authentication.form.button.submit\"></button>\n" +
    "            </div>\n" +
    "\n" +
    "        </form>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/core/services/waitDialog.html',
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-8\">\n" +
    "        <span data-translate=\"wait.dialog.message\"></span>\n" +
    "    </div>\n" +
    "    <div class=\"col-xs-4\">\n" +
    "        <div class=\"spinner pull-right\">\n" +
    "            <div class=\"spinner-container spinner-container1\">\n" +
    "                <div class=\"spinner-circle1\"></div>\n" +
    "                <div class=\"spinner-circle2\"></div>\n" +
    "                <div class=\"spinner-circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "            <div class=\"spinner-container spinner-container2\">\n" +
    "                <div class=\"spinner-circle1\"></div>\n" +
    "                <div class=\"spinner-circle2\"></div>\n" +
    "                <div class=\"spinner-circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "            <div class=\"spinner-container spinner-container3\">\n" +
    "                <div class=\"spinner-circle1\"></div>\n" +
    "                <div class=\"spinner-circle2\"></div>\n" +
    "                <div class=\"spinner-circle3\"></div>\n" +
    "                <div class=\"circle4\"></div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/alerts/alertsTemplate.html',
    "<div class=\"row-fluid\" data-ng-if=\"alerts!=null &amp;&amp; alerts.length>0\">\n" +
    "    <div data-ng-repeat=\"alert in alerts\" class=\"ng-class: {'col-xs-12':true, 'alert':true, 'alert-danger':(alert.successful==false),'alert-success':(alert.successful==true)};\">\n" +
    "        <button type=\"button\" class=\"close\" data-ng-hide=\"alert.closeable==false\" data-ng-click=\"dismissAlert($index);\">&times;</button>\n" +
    "        <span id=\"alertMsg\">{{alert.message | translate}}</span>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/authorization/hasOperationPermissionTemplate.html',
    "<div>\n" +
    "    <div ng-if=\"ctrl.isPermissionGranted\">\n" +
    "        <div ng-transclude></div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/dateTimePicker/dateTimePickerTemplate.html',
    "<div class=\"input-group date ySEDateField\" data-ng-class=\"{'ySEDataDisabled':!isEditable}\" id=\"date-time-picker-{{name}}\">\n" +
    "    <input type='text' class=\"form-control\" placeholder=\"{{ placeholderText | translate}}\" ng-disabled=\"!isEditable\" name=\"{{name}}\" data-ng-model=\"model\" />\n" +
    "    <span class=\"input-group-addon\" data-ng-show=\"isEditable\">\n" +
    "        <span class=\"glyphicon glyphicon-calendar\"></span>\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/errors/seGenericEditorFieldErrorsTemplate.html',
    "<div data-ng-if=\"ctrl.getFilteredErrors().length > 0\">\n" +
    "    <span data-ng-repeat=\"error in ctrl.getFilteredErrors()\" class=\"error-input help-block\">\n" +
    "        {{error | translate}}\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/genericEditorFieldTemplate.html',
    "<div data-ng-switch=\"field.cmsStructureType\" validation-id=\"{{field.qualifier}}\" data-ng-cloack class=\"ySEField\">\n" +
    "\n" +
    "    <div data-ng-if=\"field.prefixText\" class=\"ySEText ySEFieldPrefix\">{{field.prefixText | translate}}</div>\n" +
    "    <div data-ng-switch-when=\"Boolean\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/booleanTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"ShortString\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/shortStringTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"LongString\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/longStringTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"RichText\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/richTextTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"LinkToggle\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/linkToggleTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Media\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/mediaTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"MediaContainer\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/mediaContainerTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Dropdown\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/dropdownTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Date\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/dateTimePickerWrapperTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div data-ng-switch-when=\"Enum\">\n" +
    "        <div data-ng-include=\"'web/common/services/genericEditor/templates/enumTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <se-generic-editor-field-errors data-field=\"field\" data-qualifier=\"qualifier\"></se-generic-editor-field-errors>\n" +
    "    <div data-ng-if=\"field.postfixText\" class=\"ySEText ySEFieldPostfix\">{{field.postfixText | translate}}</div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/genericEditorFieldWrapperTemplate.html',
    "<generic-editor-field data-editor=\"model.editor\" data-field=\"model.field\" data-model=\"model.editor.component[model.field.qualifier]\" data-qualifier=\"tabId\" />"
  );


  $templateCache.put('web/common/services/genericEditor/genericEditorTemplate.html',
    "<div data-ng-cloak class=\"ySEGenericEditor\">\n" +
    "    <div>\n" +
    "        <alerts-box alerts=\"editor.alerts\" />\n" +
    "    </div>\n" +
    "    <form name=\"componentForm\" novalidate data-ng-submit=\"editor.submit(componentForm)\" class=\"col-xs-12 no-enter-submit\">\n" +
    "        <div class=\"modal-header\" data-ng-show=\"modalHeaderTitle\">\n" +
    "            <h4 class=\"modal-title\">{{modalHeaderTitle| translate}}</h4>\n" +
    "        </div>\n" +
    "        <div class=\"ySErow\" data-ng-repeat=\"holder in editor.holders;\" data-ng-switch=\"field.cmsStructureType\">\n" +
    "            <div class=\"\">\n" +
    "                <label id=\"{{holder.field.qualifier}}-label\">{{holder.field.i18nKey | lowercase | translate}}\n" +
    "                    <span data-ng-if=\"holder.field.required\">*</span>\n" +
    "                </label>\n" +
    "\n" +
    "            </div>\n" +
    "            <div class=\"\" id=\"{{holder.field.qualifier}}\">\n" +
    "                <localized-element data-ng-if=\"holder.field.localized\" data-model=\"holder\" data-languages=\"editor.languages\" data-input-template=\"web/common/services/genericEditor/genericEditorFieldWrapperTemplate.html\"></localized-element>\n" +
    "                <generic-editor-field data-ng-if=\"!holder.field.localized\" data-editor=\"holder.editor\" data-field=\"holder.field\" data-model=\"editor.component\" data-qualifier=\"holder.field.qualifier\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"ySEBtnRow modal-footer\" data-ng-show=\"(editor.alwaysShowReset || (editor.showReset===true && editor.isDirty())) || (editor.alwaysShowSubmit || (editor.showSubmit===true && editor.isDirty() && componentForm.$valid))\">\n" +
    "            <button type=\"button\" id=\"cancel\" class=\"btn btn-subordinate\" data-ng-if=\"editor.alwaysShowReset || (editor.showReset===true && editor.isDirty())\" data-ng-click=\"reset()\">{{cancelButtonText| translate}}</button>\n" +
    "            <button type=\"submit\" id=\"submit\" class=\"btn btn-primary\" data-ng-if=\"editor.alwaysShowSubmit || (editor.showSubmit===true && editor.isDirty() && componentForm.$valid)\" data-ng-disabled=\"!(editor.isDirty() && componentForm.$valid)\">{{submitButtonText| translate}}</button>\n" +
    "        </div>\n" +
    "    </form>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/errorsList/seErrorsListTemplate.html',
    "<div class=\"field-errors\">\n" +
    "    <div data-ng-repeat=\"error in ctrl.getSubjectErrors()\">{{error.message}}</div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/fileSelector/seFileSelectorTemplate.html',
    "<div class=\"seFileSelector {{::ctrl.customClass}}\">\n" +
    "    <label class=\" btn__fileSelector\" data-ng-show=\"!ctrl.disabled\">\n" +
    "        <img class=\"img-upload\" data-ng-src=\"{{::ctrl.uploadIcon}}\" />\n" +
    "        <p class=\"label label__fileUpload label__fileUpload-link\">{{::ctrl.labelI18nKey | translate}}</p>\n" +
    "        <input type=\"file\" class=\"hide\" accept=\"{{ctrl.buildAcceptedFileTypesList()}}\">\n" +
    "    </label>\n" +
    "    <label class=\" btn__fileSelector\" data-ng-show=\"ctrl.disabled\">\n" +
    "        <img class=\"img-upload\" data-ng-src=\"{{::ctrl.uploadIcon}}\" />\n" +
    "        <p class=\"label label__fileUpload\">{{::ctrl.labelI18nKey | translate}}</p>\n" +
    "    </label>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesCondensedTemplate.html',
    "<a popover-template=\"ctrl.contentUrl\" popover-trigger=\"focus\" tabindex=\"0\" class=\"media-container-advanced-information\">\n" +
    "    <img class=\"media-advanced-information--image\" data-ng-src=\"{{ctrl.advInfoIcon}}\" alt=\"{{ctrl.i18nKeys.INFORMATION | translate}}\" title=\"{{ctrl.i18nKeys.INFORMATION | translate}}\">\n" +
    "    <p class=\"media-advanced-information--p\">{{ctrl.i18nKeys.INFORMATION | translate}}</p>\n" +
    "</a>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesContentTemplate.html',
    "<div class=\"se-adv-media-info\">\n" +
    "    <span class=\"se-adv-media-info--data advanced-information-description\">\n" +
    "        {{ctrl.i18nKeys.DESCRIPTION | translate}}: {{ctrl.description}}\n" +
    "    </span>\n" +
    "    <span class=\"se-adv-media-info--data advanced-information-code\">\n" +
    "        {{ctrl.i18nKeys.CODE | translate}}: {{ctrl.code}}\n" +
    "    </span>\n" +
    "    <span class=\"se-adv-media-info--data advanced-information-alt-text\">\n" +
    "        {{ctrl.i18nKeys.ALT_TEXT | translate}}: {{ctrl.altText}}\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaAdvancedProperties/seMediaAdvancedPropertiesTemplate.html',
    "<a popover-template=\"ctrl.contentUrl\" data-ng-click=\"$event.stopPropagation()\" popover-append-to-body=\"true\" popover-placement=\"left\" popover-trigger=\"click\" tabindex=\"0\" class=\"media-advanced-information btn btn-subordinate\">\n" +
    "    {{ctrl.i18nKeys.INFORMATION | translate}}\n" +
    "    <img class=\"media-selector--preview--icon\" data-ng-src=\"{{ctrl.advInfoIcon}}\">\n" +
    "</a>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaContainerField/seMediaContainerFieldTemplate.html',
    "<div class=\"se-media-container-field\">\n" +
    "    <div class=\"row\">\n" +
    "        <se-media-format class=\"col-xs-3 se-media-container-cell\" ng-repeat=\"option in ctrl.field.options\" data-media-code=\"ctrl.model[ctrl.qualifier][option.value]\" data-is-under-edit=\"ctrl.isFormatUnderEdit(option.value)\" data-media-format=\"option.value\" data-errors=\"ctrl.field.errors\" data-on-file-select=\"ctrl.fileSelected(files, option.value)\" data-on-delete=\"ctrl.imageDeleted(option.value)\">\n" +
    "        </se-media-format>\n" +
    "\n" +
    "    </div>\n" +
    "    <se-media-upload-form data-ng-if=\"ctrl.image.file\" data-image=\"ctrl.image\" data-field=\"ctrl.field\" data-on-upload-callback=\"ctrl.imageUploaded(code)\" data-on-cancel-callback=\"ctrl.resetImage()\" data-on-select-callback=\"ctrl.fileSelected(files)\">\n" +
    "    </se-media-upload-form>\n" +
    "    <se-errors-list data-errors=\"ctrl.fileErrors\"></se-errors-list>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaField/seMediaFieldTemplate.html',
    "<div class=\"media-field\">\n" +
    "    <se-media-selector ng-if=\"!ctrl.image.file\" data-field=\"ctrl.field\" data-model=\"ctrl.model\" data-editor=\"ctrl.editor\" data-qualifier=\"ctrl.qualifier\" data-replace-icon=\"ctrl.replaceIconUrl\" data-delete-icon=\"ctrl.deleteIconUrl\" data-adv-info-icon=\"ctrl.advInfoIconUrl\" data-image-root=\"ctrl.imageRoot\">\n" +
    "    </se-media-selector>\n" +
    "    <se-file-selector ng-if=\"!ctrl.image.file\" data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD_IMAGE_TO_LIBRARY\" data-accepted-file-types=\"ctrl.acceptedFileTypes\" data-on-file-select=\"ctrl.fileSelected(files)\" data-upload-icon=\"ctrl.uploadIconUrl\" data-image-root=\"ctrl.imageRoot\"></se-file-selector>\n" +
    "    <se-media-upload-form ng-if=\"ctrl.image.file\" data-image=\"ctrl.image\" data-field=\"ctrl.field\" data-on-upload-callback=\"ctrl.imageUploaded(code)\" data-on-cancel-callback=\"ctrl.resetImage()\" data-on-select-callback=\"ctrl.fileSelected(files)\"></se-media-upload-form>\n" +
    "    <se-errors-list ng-if=\"ctrl.fileErrors.length > 0\" data-errors=\"ctrl.fileErrors\"></se-errors-list>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaFormat/seMediaFormatTemplate.html',
    "<div class=\"{{ctrl.mediaFormat}} se-media-format\">\n" +
    "    <div class=\"se-media-format-screentype\">\n" +
    "        {{ctrl.mediaFormat}}\n" +
    "    </div>\n" +
    "    <!-- when the image is already uploaded -->\n" +
    "    <div class=\"media-present\" data-ng-if=\"ctrl.isMediaCodeValid() && !ctrl.isUnderEdit\">\n" +
    "        <div class=\"media-present-preview\">\n" +
    "            <se-media-preview data-image-url=\"ctrl.media.url\"></se-media-preview>\n" +
    "            <div class=\"se-media-preview-image-wrapper  se-media-format-image-wrapper\">\n" +
    "                <img class=\"thumbnail thumbnail--image-preview\" data-ng-src=\"{{ctrl.media.url}}\">\n" +
    "            </div>\n" +
    "            <span class=\"media-preview-code se-media-format--code\">{{ctrl.media.code}}</span>\n" +
    "\n" +
    "        </div>\n" +
    "\n" +
    "        <se-media-advanced-properties-condensed data-adv-info-icon=\"ctrl.advInfoIconUrl\" data-code=\"ctrl.media.code\" data-description=\"ctrl.media.description\" data-alt-text=\"ctrl.media.altText\"></se-media-advanced-properties-condensed>\n" +
    "\n" +
    "        <se-file-selector data-custom-class=\" 'media-format-present-replace' \" data-label-i18n-key=\"ctrl.i18nKeys.REPLACE \" data-accepted-file-types=\"ctrl.acceptedFileTypes \" data-on-file-select=\"ctrl.onFileSelect({files: files, format: ctrl.mediaFormat}) \" data-upload-icon=\"ctrl.replaceIconUrl \" data-image-root=\"ctrl.imageRoot \"></se-file-selector>\n" +
    "\n" +
    "        <button class=\"media-selector--preview__left remove-image btn btn-subordinate \" data-ng-click=\"ctrl.onDelete({format: ctrl.mediaFormat}) \">\n" +
    "            <img class=\"media-selector--preview--icon \" data-ng-src=\"{{ctrl.deleteIconUrl}} \" alt=\"{{ctrl.i18nKeys.REMOVE | translate}} \" />\n" +
    "            <p class=\"media-selector--preview__left--p media-selector--preview__left--p__error \">{{ctrl.i18nKeys.REMOVE | translate}}</p>\n" +
    "        </button>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- when the image is not yet uploaded -->\n" +
    "    <div class=\"media-absent \" data-ng-if=\"!ctrl.isMediaCodeValid() && !ctrl.isUnderEdit \">\n" +
    "        <se-file-selector data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD \" data-accepted-file-types=\"ctrl.acceptedFileTypes \" data-on-file-select=\"ctrl.onFileSelect({files: files, format: ctrl.mediaFormat}) \" data-upload-icon=\"ctrl.uploadIconUrl \" data-image-root=\"ctrl.imageRoot \"></se-file-selector>\n" +
    "    </div>\n" +
    "\n" +
    "    <!-- when the image is under edit -->\n" +
    "    <div data-ng-if=\"ctrl.isUnderEdit \" class=\"media-under-edit-parent\">\n" +
    "        <div class=\"media-is-under-edit \">\n" +
    "            <se-file-selector data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD \" data-disabled=\"true\" data-custom-class=\" 'file-selector-disabled' \" data-accepted-file-types=\"ctrl.acceptedFileTypes \" data-on-file-select=\"ctrl.onFileSelect({files: files, format: ctrl.mediaFormat}) \" data-upload-icon=\"ctrl.uploadIconDisabledUrl \" data-image-root=\"ctrl.imageRoot \"></se-file-selector>\n" +
    "        </div>\n" +
    "        <span class=\"media-preview-under-edit \">{{ctrl.i18nKeys.UNDER_EDIT | translate}}</span>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"error-input help-block \" data-ng-repeat=\"error in ctrl.getErrors() \">\n" +
    "        {{error}}\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaPreview/seMediaPreviewContentTemplate.html',
    "<img class=\"preview-image\" data-ng-src=\"{{ctrl.imageUrl}}\" />"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaPreview/seMediaPreviewTemplate.html',
    "<a popover-template=\"ctrl.contentUrl\" data-ng-click=\"$event.stopPropagation()\" popover-append-to-body=\"true\" popover-placement=\"right\" popover-trigger=\"click\" tabindex=\"1\" class=\"media-preview\">\n" +
    "    <span class=\"hyicon hyicon-search media-preview-icon\"></span>\n" +
    "</a>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaSelector/seMediaSelectorTemplate.html',
    "<div class=\"media-selector\">\n" +
    "    <ui-select id=\"{{ctrl.field.qualifier}}-selector\" ng-model=\"ctrl.model[ctrl.qualifier]\" theme=\"select2\" data-ng-disabled=\"!ctrl.field.editable\" reset-search-input=\"false\" title=\"\" style=\"width:100%\">\n" +
    "        <ui-select-match placeholder=\"{{'genericeditor.dropdown.placeholder' | translate}}\" allow-clear=\"true\">\n" +
    "            <div class=\"media-selector--preview\">\n" +
    "                <div class=\"row\">\n" +
    "                    <span class=\"col-xs-6\">\n" +
    "                        <se-media-preview data-image-url=\"ctrl.field.options[ctrl.qualifier][0].url\"></se-media-preview>{{ctrl.imagePreviewUrl}}\n" +
    "                        <div class=\"se-media-preview-image-wrapper\">\n" +
    "                            <img class=\"thumbnail thumbnail--image-preview\" data-ng-src={{$select.selected.url}}>\n" +
    "                        </div>\n" +
    "                        <span class=\"media-preview-code\">{{$select.selected.code}}</span>\n" +
    "                    </span>\n" +
    "                    <span class=\"col-xs-6 media-selector--preview--2\">\n" +
    "                        <se-media-advanced-properties class=\"media-selector--preview__right\" data-code=\"ctrl.field.options[ctrl.qualifier][0].code\" data-description=\"ctrl.field.options[ctrl.qualifier][0].description\" data-alt-text=\"ctrl.field.options[ctrl.qualifier][0].altText\" data-adv-info-icon=\"ctrl.advInfoIcon\"></se-media-advanced-properties>\n" +
    "\n" +
    "                        <button class=\"media-selector--preview__right replace-image btn btn-subordinate\" data-ng-click=\"ctrl.onDelete($select, $event)\">\n" +
    "                            <p class=\"media-selector--preview__right--p\">{{'upload.image.replace' | translate}}</p>\n" +
    "                            <img class=\"media-selector--preview--icon\" data-ng-src=\"{{ctrl.replaceIcon}}\" />\n" +
    "                        </button>\n" +
    "                        <button class=\"media-selector--preview__right remove-image btn btn-subordinate\" data-ng-click=\"ctrl.onDelete($select, $event)\">\n" +
    "                            <p class=\"media-selector--preview__right--p\">{{'media.format.remove' | translate}}</p>\n" +
    "                            <img class=\"media-selector--preview--icon\" data-ng-src=\"{{ctrl.deleteIcon}}\" />\n" +
    "                        </button>\n" +
    "                    </span>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices id=\"{{ctrl.field.qualifier}}-list\" repeat=\"option.code as option in ctrl.field.options[ctrl.qualifier]\" refresh=\"ctrl.editor.refreshOptions(ctrl.field, ctrl.qualifier, $select.search)\" value=\"{{$select.selected.code}}\" refresh-delay=\"0\" position=\"down\">\n" +
    "            <small>\n" +
    "                <img class=\"thumbnail\" data-ng-src={{option.url}}>\n" +
    "                <span>{{option.code}}</span>\n" +
    "            </small>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFieldTemplate.html',
    "<div class=\"form-control se-mu--fileinfo--wrapper\">\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"se-mu--fileinfo--wrapper-col1 col-xs-10\">\n" +
    "            <input type=\"text\" data-ng-class=\"{'se-mu--fileinfo--field--input': true, 'se-mu--fileinfo--field__error': ctrl.error}\" class=\"se-mu--fileinfo--field--input\" name=\"{{ctrl.field}}\" data-ng-model=\"ctrl.model[ctrl.field]\">\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"se-mu--fileinfo--wrapper-col2 col-xs-2\">\n" +
    "            <img src=\"static-resources/images/edit_icon.png\" class=\"se-mu--fileinfo--field--icon \" data-ng-if=\"ctrl.displayImage && !ctrl.error\" />\n" +
    "            <img src=\"static-resources/images/edit_icon_error.png\" class=\"se-mu--fileinfo--field--icon__error \" data-ng-if=\"ctrl.error\" />\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFormTemplate.html',
    "<div class=\"se-media-upload-form\">\n" +
    "\n" +
    "    <div class=\"container-fluid se-media-upload-form--toolbar\">\n" +
    "        <div class=\"navbar-left se-media-upload-form--toolbar--title\">\n" +
    "            <h4 class=\"se-media-upload-form--toolbar--title--h4\">{{ctrl.i18nKeys.UPLOAD_IMAGE_TO_LIBRARY | translate}}</h4>\n" +
    "        </div>\n" +
    "        <div class=\"navbar-right se-media-upload-form--toolbar--buttons\">\n" +
    "            <div class=\"y-toolbar\">\n" +
    "                <div class=\"y-toolbar-cell\">\n" +
    "                    <button class=\"btn btn-subordinate btn-lg navbar-btn se-media-upload-btn__cancel\" type=\"button\" data-ng-click=\"ctrl.onCancel()\">{{ctrl.i18nKeys.UPLOAD_IMAGE_CANCEL | translate}}</button>\n" +
    "                </div>\n" +
    "                <div class=\"y-toolbar-cell\">\n" +
    "                    <button class=\"btn btn-default btn-lg navbar-btn se-media-upload-btn__submit\" type=\"button\" data-ng-click=\"ctrl.onMediaUploadSubmit()\">{{ctrl.i18nKeys.UPLOAD_IMAGE_SUBMIT | translate}}</button>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"container-fluid se-media-upload--filename\">\n" +
    "        <div class=\"row\">\n" +
    "            <div class=\"se-media-upload--filename-col1 col-xs-10\">\n" +
    "                <img src=\"static-resources/images/image_placeholder.png\">\n" +
    "                <div class=\"se-media-upload--fn--name\">{{ctrl.getTruncatedName()}}</div>\n" +
    "            </div>\n" +
    "            <div class=\"se-media-upload--filename-col2 col-xs-2\">\n" +
    "                <se-file-selector data-label-i18n-key=\"ctrl.i18nKeys.UPLOAD_IMAGE_REPLACE\" data-accepted-file-types=\"ctrl.acceptedFileTypes\" data-on-file-select=\"ctrl.onSelectCallback({files: files})\"></se-file-selector>\n" +
    "\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <form>\n" +
    "        <div class=\"se-media-upload--fileinfo\">\n" +
    "            <div class=\"se-media-upload--fileinfo--field form-group\">\n" +
    "                <label name=\"label-description\" data-ng-class=\"{ 'se-media-upload-has-error': ctrl.hasError('description'), 'se-media-upload--fileinfo--label': true }\">{{ctrl.i18nKeys.DESCRIPTION | translate}}</label>\n" +
    "                <se-media-upload-field data-field=\"'description'\" data-model=\"ctrl.imageParameters\"></se-media-upload-field>\n" +
    "                <span class=\"upload-field-error upload-field-error-description\" data-ng-repeat=\"error in ctrl.getErrorsForField('description')\">{{error}}</span>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"se-media-upload--fileinfo--field form-group\">\n" +
    "                <label name=\"label-code\" data-ng-class=\"{ 'se-media-upload-has-error': ctrl.hasError('code')}\">{{ctrl.i18nKeys.CODE | translate}}*</label>\n" +
    "                <se-media-upload-field data-error=\"ctrl.hasError('code')\" data-field=\"'code'\" data-model=\"ctrl.imageParameters\"></se-media-upload-field>\n" +
    "                <span class=\"upload-field-error upload-field-error-code\" data-ng-repeat=\"error in ctrl.getErrorsForField('code')\">{{error}}</span>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"se-media-upload--fileinfo--field form-group\">\n" +
    "                <label name=\"label-alt-text\" data-ng-class=\"{ 'se-media-upload-has-error': ctrl.hasError('altText') }\">{{ctrl.i18nKeys.ALT_TEXT | translate}}</label>\n" +
    "                <se-media-upload-field data-field=\"'altText'\" data-model=\"ctrl.imageParameters\"></se-media-upload-field>\n" +
    "                <span class=\"upload-field-error upload-field-error-alt-text \" data-ng-repeat=\"error in ctrl.getErrorsForField('altText')\">{{error}}</span>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "\n" +
    "    </form>\n" +
    "    <span data-ng-if=\"ctrl.isUploading\" class=\"upload-image-in-progress\">\n" +
    "        <!--<img src=\"static-resources/images/spinner.png\"> {{ctrl.i18nKeys.UPLOADING | translate}}-->\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/richText/seRichTextFieldTemplate.html',
    "<textarea class=\"ng-class:{'col-xs-12':true, 'has-error':ctrl.field.errors.length>0}\" name=\"{{ctrl.field.qualifier}}-{{ctrl.qualifier}}\" data-ng-disabled=\"!ctrl.field.editable\" data-ng-model=\"ctrl.model[ctrl.qualifier]\" data-ng-change=\"ctrl.reassignUserCheck()\"></textarea>\n" +
    "<div data-ng-if=\"ctrl.requiresUserCheck()\">\n" +
    "    <input type=\"checkbox\" data-ng-model=\"ctrl.field.isUserChecked\" />\n" +
    "    <span class=\"ng-class:{'warning-check-msg':true, 'not-checked':ctrl.editor.hasFrontEndValidationErrors && !ctrl.field.isCheckedByUser}\" data-translate=\"editor.richtext.check\">{{'editor.richtext.check' | translate}}</span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/booleanTemplate.html',
    "<div class=\"ySEBooleanField\">\n" +
    "    <span class=\"y-toggle y-toggle-lg\">\n" +
    "        <input type=\"checkbox\" id=\"{{field.qualifier}}-checkbox\" class=\"ySEBooleanField__input\" placeholder=\"{{field.tooltip| translate}}\" name=\"{{field.qualifier}}\" data-ng-disabled=\"!field.editable\" data-ng-model=\"model[qualifier]\" />\n" +
    "        <label class=\"ySEBooleanField__label\" for=\"{{field.qualifier}}-checkbox\"></label>\n" +
    "        <p data-ng-if=\"field.labelText && !model[qualifier]\" class=\"ySEBooleanField__text\">{{field.labelText| translate}}</p>\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/dateTimePickerWrapperTemplate.html',
    "<date-time-picker data-name=\"field.qualifier\" data-model=\"model[qualifier]\" data-is-editable=\"field.editable\"></date-time-picker>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/dropdownTemplate.html',
    "<ui-select id=\"{{field.qualifier}}-selector\" ng-model=\"model[qualifier]\" theme=\"select2\" title=\"\" style=\"width:100%\" search-enabled=\"false\" data-dropdown-auto-width=\"false\">\n" +
    "    <ui-select-match placeholder=\"Select an option\">\n" +
    "        <span ng-bind=\"$select.selected.label\"></span>\n" +
    "    </ui-select-match>\n" +
    "    <ui-select-choices id=\"{{field.qualifier}}-list\" repeat=\"option in field.options\" position=\"down\" value=\"{{$selected.selected}}\">\n" +
    "        <span ng-bind=\"option.label\"></span>\n" +
    "    </ui-select-choices>\n" +
    "</ui-select>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/enumTemplate.html',
    "<ui-select id=\"{{field.qualifier}}-selector\" ng-model=\"model[qualifier]\" theme=\"select2\" data-ng-disabled=\"!field.editable\" reset-search-input=\"false\" title=\"\" style=\"width:100%\">\n" +
    "    <ui-select-match placeholder=\"{{'genericeditor.dropdown.placeholder' | translate}}\" allow-clear=\"true\">\n" +
    "        <span id=\"enum-{{field.qualifier}}\">{{$select.selected.label}}</span>\n" +
    "        <br/>\n" +
    "    </ui-select-match>\n" +
    "    <ui-select-choices id=\"{{field.qualifier}}-list\" repeat=\"option.code as option in field.options[qualifier]\" refresh=\"editor.refreshOptions(field, qualifier, $select.search)\" value=\"{{$select.selected.code}}\" refresh-delay=\"0\" position=\"down\">\n" +
    "        <small>\n" +
    "            <span>{{option.label}}</span>\n" +
    "        </small>\n" +
    "    </ui-select-choices>\n" +
    "</ui-select>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/errorMessageTemplate.html',
    "<span data-ng-if=\"field.errors\" data-ng-repeat=\"error in field.errors | filter:{language:qualifier}\" id='validation-error-{{field.qualifier}}-{{qualifier}}-{{$index}}' class=\"error-input help-block\">\n" +
    "    {{error.message|translate}}\n" +
    "</span>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/linkToggleTemplate.html',
    "<div>\n" +
    "    <input type=\"radio\" name=\"external\" id=\"external\" data-ng-model=\"model.external\" data-ng-value=\"true\" data-ng-change=\"editor.emptyUrlLink()\" />\n" +
    "    <label>{{field.externalI18nKey| translate}}</label>\n" +
    "    <input type=\"radio\" name=\"external\" id=\"internal\" data-ng-model=\"model.external\" data-ng-value=\"false\" data-ng-change=\"editor.emptyUrlLink()\" />\n" +
    "    <label>{{field.internalI18nKey| translate}}</label>\n" +
    "    <br/>\n" +
    "    <input type=\"text\" id=\"urlLink\" name=\"urlLink\" data-ng-model=\"model.urlLink\" class=\"ng-class:{'col-xs-12':true, 'form-control':true, 'has-error':field.errors.length>0} \" />\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/longStringTemplate.html',
    "<textarea class=\"ng-class:{'col-xs-12':true, 'has-error':field.errors.length>0}\" placeholder=\"{{field.tooltip| translate}}\" name=\"{{field.qualifier}}\" data-ng-disabled=\"!field.editable\" data-ng-model=\"model[qualifier]\" data-ng-maxlength=\"1000\"></textarea>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/mediaContainerTemplate.html',
    "<se-media-container-field data-field=\"field\" data-model=\"model\" data-editor=\"editor\" data-qualifier=\"qualifier\"></se-media-container-field>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/mediaTemplate.html',
    "<se-media-field data-field=\"field\" data-model=\"model\" data-editor=\"editor\" data-qualifier=\"qualifier\"></se-media-field>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/richTextTemplate.html',
    "<se-rich-text-field data-field=\"field\" data-qualifier=\"qualifier\" data-model=\"model\" data-editor=\"editor\"></se-rich-text-field>"
  );


  $templateCache.put('web/common/services/genericEditor/templates/shortStringTemplate.html',
    "<input type=\"text\" id=\"{{field.qualifier}}-shortstring\" class=\"ng-class:{'col-xs-12':true, 'form-control':true, 'has-error':field.errors.length>0} \" placeholder=\"{{field.tooltip| translate}}\" name=\"{{field.qualifier}}\" data-ng-disabled=\"!field.editable\" data-ng-model=\"model[qualifier]\" />"
  );


  $templateCache.put('web/common/services/infiniteScrolling/yInfiniteScrollingTemplate.html',
    "<div data-ng-attr-id=\"{{scroll.containerId}}\" class=\"ySEInfiniteScrolling-container {{scroll.dropDownContainerClass}}\">\n" +
    "    <div class=\"ySEInfiniteScrolling {{scroll.dropDownClass}}\" data-infinite-scroll=\"scroll.nextPage()\" data-infinite-scroll-disabled=\"scroll.pagingDisabled\" data-infinite-scroll-distance=\"scroll.distance\" data-infinite-scroll-immediate-check=\"true\" data-infinite-scroll-container='scroll.container'>\n" +
    "        <div data-ng-transclude></div>\n" +
    "    </div>\n" +
    "    <div data-ng-if=\"!scroll.pagingDisabled\" class=\"panel panel-default panel__ySEInfiniteScrolling\">\n" +
    "        <div class=\"panel-body panel-body__ySEInfiniteScrolling\">\n" +
    "            <div class=\"spinner\">\n" +
    "                <div class=\"spinner-container spinner-container1\">\n" +
    "                    <div class=\"spinner-circle1\"></div>\n" +
    "                    <div class=\"spinner-circle2\"></div>\n" +
    "                    <div class=\"spinner-circle3\"></div>\n" +
    "                    <div class=\"circle4\"></div>\n" +
    "                </div>\n" +
    "                <div class=\"spinner-container spinner-container2\">\n" +
    "                    <div class=\"spinner-circle1\"></div>\n" +
    "                    <div class=\"spinner-circle2\"></div>\n" +
    "                    <div class=\"spinner-circle3\"></div>\n" +
    "                    <div class=\"circle4\"></div>\n" +
    "                </div>\n" +
    "                <div class=\"spinner-container spinner-container3\">\n" +
    "                    <div class=\"spinner-circle1\"></div>\n" +
    "                    <div class=\"spinner-circle2\"></div>\n" +
    "                    <div class=\"spinner-circle3\"></div>\n" +
    "                    <div class=\"circle4\"></div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/languageSelector/languageSelectorTemplate.html',
    "<ui-select id=\"uiSelectToolingLanguage\" ng-model=\"ctrl.selectedLanguage\" on-select=\"ctrl.setSelectedLanguage($item)\" theme=\"select2\" title=\"\" style=\"width:100%\" search-enabled=\"false\" data-dropdown-auto-width=\"false\">\n" +
    "    <ui-select-match placeholder=\"{{'languageselector.dropdown.placeholder' | translate}}\">\n" +
    "        <span ng-bind=\"ctrl.selectedLanguage.name \"></span>\n" +
    "    </ui-select-match>\n" +
    "\n" +
    "    <ui-select-choices repeat=\"language in ctrl.languages track by language.isoCode\" position=\"down \">\n" +
    "        <span ng-bind=\"language.name\"></span>\n" +
    "    </ui-select-choices>\n" +
    "</ui-select>"
  );


  $templateCache.put('web/common/services/localizedElement/localizedElementTemplate.html',
    "<y-tabset data-model=\"model\" tabs-list=\"tabs\" data-num-tabs-displayed=\"6\">\n" +
    "</y-tabset>"
  );


  $templateCache.put('web/common/services/modalTemplate.html',
    "<div class=\"modal-header\" data-ng-if=\"modalController._modalManager.title\">\n" +
    "    <button type=\"button\" class=\"close\" data-ng-if=\"modalController._modalManager._showDismissButton()\" data-ng-click=\"modalController._modalManager._handleDismissButton()\">\n" +
    "        <span class=\"hyicon hyicon-close\"></span>\n" +
    "    </button>\n" +
    "    <h4 id=\"smartedit-modal-title-{{ modalController._modalManager.title }}\">{{ modalController._modalManager.title | translate }}&nbsp;{{ modalController._modalManager.titleSuffix | translate }}</h4>\n" +
    "</div>\n" +
    "<div class=\"modal-body\" id=\"modalBody\">\n" +
    "    <div data-ng-include=\"modalController.templateUrl\" />\n" +
    "</div>\n" +
    "<div class=\"modal-footer\" data-ng-if=\"modalController._modalManager._hasButtons()\">\n" +
    "    <span data-ng-repeat=\"button in modalController._modalManager.getButtons()\">\n" +
    "        <button id='{{ button.id }}' type=\"button\" data-ng-disabled=\"button.disabled\" data-ng-class=\"{ 'btn':true, 'btn-subordinate':button.style=='default', 'btn-primary':button.style=='primary' }\" data-ng-click=\"modalController._modalManager._buttonPressed(button)\">{{ button.label | translate }}</button>\n" +
    "    </span>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/tabset/yTabsetTemplate.html',
    "<div>\n" +
    "    <ul class=\"nav nav-tabs\">\n" +
    "        <li ng-if=\"tabsList.length!=numTabsDisplayed\" data-ng-repeat=\"tab in tabsList.slice( 0, numTabsDisplayed-1 ) track by $index\" data-ng-class=\"{'active': tab.active }\" data-tab-id=\"{{tab.id}}\">\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"!tab.message\">{{tab.title | translate}}</a>\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"tab.message\" data-popover=\"{{tab.message}}\" data-popover-trigger=\"mouseenter\">{{tab.title | translate}}</a>\n" +
    "        </li>\n" +
    "        <li ng-if=\"tabsList.length==numTabsDisplayed\" data-ng-repeat=\"tab in tabsList.slice( 0, numTabsDisplayed ) track by $index\" data-ng-class=\"{'active': tab.active }\" data-tab-id=\"{{tab.id}}\">\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"!tab.message\">{{tab.title | translate}}</a>\n" +
    "            <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\" data-ng-if=\"tab.message\" data-popover=\"{{tab.message}}\" data-popover-trigger=\"mouseenter\">{{tab.title | translate}}</a>\n" +
    "        </li>\n" +
    "        <li data-ng-if=\"tabsList.length > numTabsDisplayed\" class=\"dropdown\" data-ng-class=\"{'active': tabsList.indexOf(selectedTab) >= numTabsDisplayed}\">\n" +
    "            <a data-ng-class=\"{'sm-tab-error': dropDownHasErrors()}\" class=\"dropdown-toggle\" data-toggle=\"dropdown\" data-translate=\"ytabset.tabs.more\">\n" +
    "                <span class=\"caret\"></span>\n" +
    "            </a>\n" +
    "            <ul class=\"dropdown-menu\">\n" +
    "                <li ng-if=\"tabsList.length!=numTabsDisplayed\" data-ng-repeat=\"tab in tabsList.slice( numTabsDisplayed-1)\" data-tab-id=\"{{tab.id}}\">\n" +
    "                    <a data-ng-class=\"{'sm-tab-error': tab.hasErrors}\" data-ng-click=\"selectTab(tab)\">{{tab.title | translate}}</a>\n" +
    "                </li>\n" +
    "            </ul>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "\n" +
    "    <div>\n" +
    "        <y-tab data-ng-repeat=\"tab in tabsList\" ng-show=\"tab.active\" data-tab-id=\"{{tab.id}}\" content=\"tab.templateUrl\" data-model=\"model\" tab-control=\"tabControl\">\n" +
    "        </y-tab>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/wizard/modalWizardNavBarTemplate.html',
    "<div class=\"modal-wizard-template\">\n" +
    "    <div class=\"modal-wizard-template__steps\">\n" +
    "        <div data-ng-repeat=\"action in wizardController._wizardContext.navActions\" class=\"modal-wizard-template-step\">\n" +
    "            <button id=\"action.id\" data-ng-class=\"{ 'btn modal-wizard-template-step__action': true, 'modal-wizard-template-step__action__enabled': action.enableIfCondition(), 'modal-wizard-template-step__action__disabled': !action.enableIfCondition(), 'modal-wizard-template-step__action__current': action.isCurrentStep() }\" data-ng-click=\"wizardController.executeAction(action)\" data-ng-disabled=\"!action.enableIfCondition()\">{{ action.i18n | translate }}\n" +
    "            </button>\n" +
    "            <span data-ng-if=\"!$last\" class=\"\" data-ng-class=\"{ 'modal-wizard-template-step__glyph-enabled':  action.enableIfCondition(), 'modal-wizard-template-step__glyph-disabled': !action.enableIfCondition()}\"></span>\n" +
    "\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"modal-wizard-template-content\" data-ng-include=\"wizardController._wizardContext.templateUrl\"></div>\n" +
    "</div>"
  );


  $templateCache.put('web/common/services/wizard/modalWizardTemplate.html',
    "<div>\n" +
    "    <div data-ng-include=\"wizardController._wizardContext.templateOverride || wizardController._wizardContext.templateUrl\"></div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/experienceSelectorWidget/experienceSelectorWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" class=\"ySEpreview\" auto-close=\"disabled\">\n" +
    "    <button type=\"button\" dropdown-toggle id=\"experience-selector-btn\" class=\"btn yWebsiteSelectBtn\" aria-pressed=\"false\" title=\"{{action.name}}\" alt=\"{{action.name}}\" data-ng-click=\"resetExperienceSelector()\">\n" +
    "        <span class=\"yWebsiteSelectBtn--label\" data-translate=\"experience.selector.previewing\"></span>\n" +
    "        <span class=\"yWebsiteSelectBtn--text\">{{buildExperienceText()}}</span>\n" +
    "        <span class=\"hyicon hyicon-arrow \"></span>\n" +
    "    </button>\n" +
    "    <div class=\"dropdown-menu bottom btn-block yDdResolution \" role=\"menu\">\n" +
    "        <experience-selector data-experience=\"experience\" data-dropdown-status=\"status\" data-reset-experience-selector=\"resetExperienceSelector\"></experience-selector>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/inflectionPointSelectorWidget/inflectionPointSelectorWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" auto-close=\"disabled\" id=\"inflectionPtDropdown\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction yDddlbResolution\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"{{imageRoot}}{{currentPointSelected.icon}}\" data-ng-typeSelected=\"{{currentPointSelected.type}}\" />\n" +
    "    </button>\n" +
    "    <ul class=\"dropdown-menu bottom btn-block yDdResolution \" role=\"menu\">\n" +
    "        <li ng-repeat=\"choice in points\" class=\"item text-center\">\n" +
    "            <a href data-ng-click=\"selectPoint(choice);\">\n" +
    "                <img data-ng-show=\"currentPointSelected.type !== choice.type\" data-ng-src=\"{{imageRoot}}{{choice.icon}}\" class=\"file\" />\n" +
    "                <img data-ng-show=\"currentPointSelected.type === choice.type\" data-ng-src=\"{{imageRoot}}{{choice.selectedIcon}}\" class=\"file\" />\n" +
    "            </a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/clonePage/clonePageTemplate.html',
    "<div id=\"clone-page-template\">\n" +
    "    {{ \"template.clone.page.title\" | translate }}\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/clonePage/clonePageWrapperTemplate.html',
    "<clone-page selected-item-callbacks=\"selectedItemCallbacks\"></clone-page>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageInformation/pageInformationTemplate.html',
    "<div id=\"page-info-template\">\n" +
    "    {{ \"template.page.information.title\" | translate }}\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageInformation/pageInformationWrapperTemplate.html',
    "<page-information selected-item-callbacks=\"selectedItemCallbacks\"></page-information>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageToolMenuTemplate.html',
    "<div>\n" +
    "    <nav class=\"pageToolMenu\">\n" +
    "        <ul class=\"list-unstyled\">\n" +
    "            <li class=\"text-right yCloseToolbar\">\n" +
    "                <a id=\"page-tool-menu-close\" href=\"#\" class=\"nav-close\" data-ng-click=\"closePageToolMenu($event)\">\n" +
    "                    <span class=\"hyicon hyicon-close\"></span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul id=\"page-tool-menu-home\" class=\"list-unstyled page-tool-menu\">\n" +
    "            <li class=\"text-left\" data-ng-repeat=\"item in actions\" data-ng-switch=\"item.type\">\n" +
    "                <div data-ng-switch-when=\"TEMPLATE\" class=\"yTemplateToolbar\">\n" +
    "                    <div data-ng-include=\"item.include\" data-ng-if=\"item.include\"></div>\n" +
    "                </div>\n" +
    "\n" +
    "                <div data-ng-switch-when=\"ACTION\">\n" +
    "                    <a href data-ng-click=\"triggerAction(item, $event)\">\n" +
    "                        <span id=\"{{toolbarName}}_option_{{$index}}\" class=\"{{ item.className || 'yStatusDefault' }}\">\n" +
    "                            <img data-ng-src=\"{{imageRoot}}{{item.icons.default}} \" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />{{item.name | translate}}\n" +
    "                        </span>\n" +
    "                    </a>\n" +
    "                </div>\n" +
    "\n" +
    "                <div data-ng-switch-when=\"HYBRID_ACTION\" dropdown is-open=\"status.isopen \">\n" +
    "                    <a href data-ng-click=\"triggerAction(item, $event)\">\n" +
    "                        <span id=\"{{toolbarName}}_option_{{$index}}\" class=\"{{ item.className || 'yStatusDefault' }}\" dropdown-toggle>\n" +
    "                            <img data-ng-src=\"{{imageRoot}}{{item.icons.default}} \" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />{{item.name | translate}}\n" +
    "                        </span>\n" +
    "                    </a>\n" +
    "                </div>\n" +
    "\n" +
    "                <div data-ng-switch-default></div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul class=\"list-unstyled page-tool-menu hideLevel2\" id=\"page-tool-menu-selected-item\">\n" +
    "            <li class=\"text-left\">\n" +
    "                <a id=\"page-tool-menu-back-anchor\" data-ng-click=\"showPageToolMenuHome()\">\n" +
    "                    <span id=\"page-tool-menu-back\" class=\" icon-back \" />{{backButtonI18nKey | translate}}</a>\n" +
    "            </li>\n" +
    "            <li>\n" +
    "                <div data-ng-include=\"selected.include\" data-ng-if=\"selected.include\"></div>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </nav>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/pageToolMenuWrapperTemplate.html',
    "<page-tool-menu data-image-root=\"imageRoot\" data-toolbar-name=\"pageToolMenu\"></page-tool-menu>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/syncStatus/syncStatusTemplate.html',
    "<a href>\n" +
    "    <span class=\"yStatusInfo\">\n" +
    "        <img data-ng-src=\"{{imageRoot}}static-resources/images/icon_small_sync.png\" />{{syncStatus | translate}}\n" +
    "    </span>\n" +
    "    <div class=\"yStatusSubtitle\">{{syncDate | date : 'dd/MM/yy - h:mm:ss a'}}</div>\n" +
    "</a>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/pageToolMenu/syncStatus/syncStatusWrapperTemplate.html',
    "<sync-status></sync-status>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/perspectiveSelectorWidget/perspectiveSelectorWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEPerspectiveSelector\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false\">\n" +
    "        <span class=\"btn-text\">{{perspectiveData.activePerspective.name | translate}}</span>\n" +
    "        <span class=\"hyicon hyicon-arrow \"></span>\n" +
    "    </button>\n" +
    "    <ul class=\"dropdown-menu bottom btn-block ySEPerspectiveList\" role=\"menu\">\n" +
    "        <li ng-repeat=\"choice in getPerspectives()\" class=\"item ySEPerspectiveList--item\" data-ng-click=\"selectPerspective(choice.key)\">\n" +
    "            <a href data-ng-click=\"selectPerspective(choice.key);\">{{choice.name | translate}}</a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/deviceSupportTemplate.html',
    "<inflection-point-selector data-ng-if=\"isOnStorefront()\"></inflection-point-selector>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/experienceSelectorWrapperTemplate.html',
    "<experience-selector-button data-ng-if=\"isOnStorefront()\"></experience-selector-button>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/leftToolbarTemplate.html',
    "<div>\n" +
    "    <nav class=\"leftNav\">\n" +
    "\n" +
    "        <ul class=\"list-unstyled leftNav--user\">\n" +
    "            <li class=\"leftNav--user__close\">\n" +
    "                <a href=\"#\" class=\"nav-close\" data-ng-click=\"closeLeftToolbar($event)\">\n" +
    "                    <span class=\"hyicon hyicon-close\"></span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--user__info\">\n" +
    "                <p>{{username | uppercase}}</p>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--user__info\">\n" +
    "                <a href=\"#\" id=\"\" data-ng-click=\"signOut($event)\">{{'left.toolbar.sign.out' | translate}}</a>\n" +
    "            </li>\n" +
    "\n" +
    "            <li class=\"leftNav--user__info\">\n" +
    "                <language-selector></language-selector>\n" +
    "            </li>\n" +
    "            <li class=\"divider leftNav--user__divider\"></li>\n" +
    "        </ul>\n" +
    "\n" +
    "        <ul class=\"list-unstyled leftNav--level1\" id=\"hamburger-menu-level1\">\n" +
    "            <li class=\"leftNav--listItem\">\n" +
    "                <a data-ng-click=\"showSites()\">{{'left.toolbar.sites' | translate}}</a>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--listItem\" has-operation-permission=\"configurationCenterReadPermissionKey\">\n" +
    "                <a id=\"configurationCenter\" data-ng-click=\"showCfgCenter($event)\">{{'left.toolbar.configuration.center' | translate}}\n" +
    "                    <span class=\"icon \"></span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "        <ul class=\"list-unstyled leftNav--level2 ySELeftHideLevel2\" id=\"hamburger-menu-level2\">\n" +
    "            <li class=\"leftNav--listItem\">\n" +
    "                <a data-ng-click=\"goBack()\">\n" +
    "                    <span class=\" icon-back \">{{'left.toolbar.back' | translate}}</span>\n" +
    "                </a>\n" +
    "            </li>\n" +
    "            <li class=\"leftNav--listItem\">\n" +
    "                <general-configuration/>\n" +
    "            </li>\n" +
    "        </ul>\n" +
    "    </nav>\n" +
    "\n" +
    "    <div class=\"navbar-header pull-left leftNavBtn\">\n" +
    "        <button type=\"button\" class=\"navbar-toggle collapsed \" data-toggle=\"collapse \" data-target=\"#bs-example-navbar-collapse-1 \" id=\"nav-expander\" data-ng-click=\"showToolbar($event) \">\n" +
    "            <span class=\"sr-only \">{{'left.toolbar.toggle.navigation' | translate}}</span>\n" +
    "            <span class=\"icon-bar \"></span>\n" +
    "            <span class=\"icon-bar \"></span>\n" +
    "            <span class=\"icon-bar \"></span>\n" +
    "        </button>\n" +
    "    </div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/leftToolbarWrapperTemplate.html',
    "<left-toolbar data-image-root=\"imageRoot\"></left-toolbar>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/logoTemplate.html',
    "<img data-ng-src=\"{{imageRoot}}static-resources/images/logo_smartEdit.png \" class=\"ySmartEditAppLogo \" alt=\"Smart Edit \" />"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/perspectiveSelectorWrapperTemplate.html',
    "<perspective-selector></perspective-selector>"
  );


  $templateCache.put('web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html',
    "<div>\n" +
    "    <div data-ng-if=\"item.type == 'ACTION'\" class=\"btn-group \">\n" +
    "        <button type=\"button\" dropdown-toggle=\"true\" data-toggle=\"button\" class=\"btn\" aria-pressed=\"false\" data-ng-click=\"triggerAction(item, $event)\">\n" +
    "            <span>{{item.name | translate}}</span>\n" +
    "            <img id=\"{{toolbarName}}_option_{{item.key}}\" data-ng-src=\"{{::imageRoot}}{{item.icons.default}} \" class=\"file\" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />\n" +
    "        </button>\n" +
    "    </div>\n" +
    "    <div data-ng-if=\"item.type == 'HYBRID_ACTION'\" class=\"btn-group ySEHybridAction\" dropdown is-open=\"status.isopen \">\n" +
    "        <button data-ng-show=\"item.icons.default\" type=\"button\" class=\"btn btn-default yHybridAction yDddlbResolution\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false \" data-ng-click=\"triggerAction(item, $event)\">\n" +
    "            <span>{{item.name | translate}}</span>\n" +
    "            <img id=\"{{toolbarName}}_option_{{item.key}}\" data-ng-src=\"{{::imageRoot}}{{item.icons.default}}\" class=\"file\" title=\"{{item.name | translate}}\" alt=\"{{item.name | translate}}\" />\n" +
    "        </button>\n" +
    "\n" +
    "        <div data-ng-include=\"item.include\" data-ng-if=\"item.include\"></div>\n" +
    "\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/core/toolbar/toolbarTemplate.html',
    "<div class='{{cssClass}} yToolbar'>\n" +
    "\n" +
    "    <div class=\"yToolbar__Left\">\n" +
    "        <div data-ng-repeat=\"item in actions | filter:{section:'left'}\" class=\"yTemplateToolbar {{::item.className}}\" data-ng-include=\"item.include && item.type =='TEMPLATE' ? item.include : 'web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div class=\"yToolbar__Middle\">\n" +
    "        <div data-ng-repeat=\"item in actions | filter:{section:'middle'}\" class=\"yTemplateToolbar {{::item.className}}\" data-ng-include=\"item.include && item.type =='TEMPLATE' ? item.include : 'web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "    <div class=\"yToolbar__Right\">\n" +
    "        <div data-ng-repeat=\"item in actions | filter:{section:'right'}\" class=\"yTemplateToolbar {{::item.className}}\" data-ng-include=\"item.include && item.type =='TEMPLATE' ? item.include : 'web/smarteditcontainer/core/services/topToolbars/toolbarActionTemplate.html'\"></div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/fragments/landingPage.html',
    "<div class=\"ySmartEditToolbars\" style=\"position:absolute\">\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditTitleToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"smartEditTitleToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div class=\"landingPageWrapper\">\n" +
    "    <div class=\"ySELandingTitle\">\n" +
    "        <h1 class=\"landing-page-title\" data-translate='landingpage.title'></h1>\n" +
    "        <h4 class=\"landing-page-label\" data-translate='landingpage.label'></h4>\n" +
    "    </div>\n" +
    "    <div class=\"container-fluid ySELandingPage\">\n" +
    "        <div class=\"row\">\n" +
    "            <div class=\"ySECatalogEntry\" data-ng-repeat=\"catalog in landingCtl.catalogs | startFrom:(landingCtl.currentPage-1)*landingCtl.CATALOGS_PER_PAGE | limitTo:landingCtl.CATALOGS_PER_PAGE\">\n" +
    "                <catalog-details catalog=\"catalog\"></catalog-details>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"pagination-container\">\n" +
    "            <pagination boundary-links=\"true\" total-items=\"landingCtl.totalItems\" items-per-page=\"landingCtl.CATALOGS_PER_PAGE\" ng-model=\"landingCtl.currentPage\" class=\"pagination-sm\" previous-text=\"&lsaquo;\" next-text=\"&rsaquo;\" first-text=\"&laquo;\" last-text=\"&raquo;\"></pagination>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/fragments/mainview.html',
    "<div class=\"ySmartEditToolbars\" style=\"position:absolute\">\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditTitleToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"smartEditTitleToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditExperienceSelectorToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"experienceSelectorToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div ng-class=\"{'alert-overlay': true, 'ySEEmptyMessage': (!alerts || alerts.length == 0 ) }\">\n" +
    "    <alerts-box alerts=\"alerts\" />\n" +
    "</div>\n" +
    "<div id=\"js_iFrameWrapper\" class=\"iframeWrapper\">\n" +
    "    <iframe src=\"\" hy-dropabpe-iframe></iframe>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/modules/administrationModule/editConfigurationsTemplate.html',
    "<div id=\"editConfigurationsBody\" class=\"ySEConfigBody\">\n" +
    "    <form name=\"form.configurationForm\" novalidate data-ng-submit=\"editor.submit(form.configurationForm)\">\n" +
    "        <div class=\"row ySECfgTableHeader\">\n" +
    "            <div class=\"col-xs-3\">\n" +
    "                <label data-translate=\"configurationform.header.key.name\"></label>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-9\">\n" +
    "                <label data-translate=\"configurationform.header.value.name\"></label>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"row ySECfgAddEntity\">\n" +
    "            <button class=\"y-add-btn\" type=\"button\" data-ng-click=\"editor.addEntry(); \">\n" +
    "                <span class=\"hyicon hyicon-add \"></span>\n" +
    "                {{'general.configuration.add.button' | translate}}\n" +
    "            </button>\n" +
    "        </div>\n" +
    "        <div class=\"row ySECfgEntity \" data-ng-repeat=\"entry in editor.filterConfiguration() \" data-ng-mouseenter=\"mouseenter() \" data-ng-mouseout=\"mouseout() \">\n" +
    "            <div class=\"col-xs-3 \">\n" +
    "                <input type=\"text \" class=\"ng-class:{ 'col-xs-12':true, 'has-error':entry.errors.keys.length>0}\" name=\"{{entry.key}}_key\" data-ng-model=\"entry.key\" data-ng-required=\"true\" data-ng-disabled=\"!entry.isNew\" />\n" +
    "                <span id=\"{{entry.key}}_error_{{$index}}\" data-ng-if=\"entry.errors.keys\" data-ng-repeat=\"error in entry.errors.keys\" class=\"error-input help-block\">\n" +
    "                    {{error.message|translate}}\n" +
    "                </span>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-8\">\n" +
    "                <textarea class=\"ng-class:{'col-xs-12':true, 'has-error':entry.errors.values.length>0}\" name=\"{{entry.key}}_value\" data-ng-model=\"entry.value\" data-ng-required=\"true\" data-ng-change=\"editor._validateUserInput(entry)\"></textarea>\n" +
    "                <div data-ng-if=\"entry.requiresUserCheck\">\n" +
    "                    <input id=\"{{entry.key}}_absoluteUrl_check_{{$index}}\" type=\"checkbox\" data-ng-model=\"entry.isCheckedByUser\" />\n" +
    "                    <span id=\"{{entry.key}}_absoluteUrl_msg_{{$index}}\" class=\"ng-class:{'warning-check-msg':true, 'not-checked':entry.hasErrors && !entry.isCheckedByUser}\">{{'configurationform.absoluteurl.check' | translate}}</span>\n" +
    "                </div>\n" +
    "\n" +
    "                <span id=\"{{entry.key}}_error_{{$index}}\" data-ng-if=\"entry.errors.values\" data-ng-repeat=\"error in entry.errors.values\" class=\"error-input help-block\">\n" +
    "                    {{error.message|translate}}\n" +
    "                </span>\n" +
    "            </div>\n" +
    "            <div class=\"col-xs-1\">\n" +
    "                <button type=\"button\" id=\"{{entry.key}}_removeButton_{{$index}}\" class=\"btn btn-subordinate\" data-ng-click=\"editor.removeEntry(entry, form.configurationForm);\">\n" +
    "                    <span class=\"hyicon hyicon-remove\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </form>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/modules/administrationModule/generalConfigurationTemplate.html',
    "<a id=\"generalConfiguration\" data-ng-click=\"editConfiguration()\">{{'general.configuration.title' | translate}}</a>"
  );


  $templateCache.put('web/smarteditcontainer/services/widgets/catalogDetails/catalogDetailsTemplate.html',
    "<div class=\"col-xs-12 col-sm-6 ySECatalog\">\n" +
    "    <div class=\"catalog-container\">\n" +
    "        <div class=\"catalog-body\">\n" +
    "            <div class=\"catalog-header row\">\n" +
    "                <h4>{{catalog.name | l10n}}</h4>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"row\">\n" +
    "                <div data-ng-repeat=\"catalogVersion in catalog.catalogVersions\" class=\"col-xs-6 catalog-version-item\">\n" +
    "                    <div class=\"catalog-version-container\" data-ng-click=\"onCatalogSelect(catalogVersion)\">\n" +
    "                        <div class=\"catalog-thumbnail-default-img\">\n" +
    "                            <div class=\"catalog-thumbnail\" style=\"background-image: url('{{catalogVersion.thumbnailUrl}}');\"></div>\n" +
    "                        </div>\n" +
    "                        <div class=\"catalog-version-info\">{{catalogVersion.catalogVersion}}</div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"catalog-footer row\">\n" +
    "                <div data-ng-repeat=\"template in templates\" ng-include=\"template.include\" />\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smarteditcontainer/services/widgets/clientPagedList/clientPagedList.html',
    "<div class=\"fluid-container ySEPageListResult\">\n" +
    "\n" +
    "    <p class=\"paged-list-count\" ng-if=\"displayCount\">\n" +
    "        <span>({{ filteredItems.length }} {{'pagelist.countsearchresult' | translate}})</span>\n" +
    "    </p>\n" +
    "\n" +
    "    <table class=\"paged-list-table table table-striped table-hover techne-table\">\n" +
    "        <thead>\n" +
    "            <tr>\n" +
    "                <th data-ng-repeat=\"key in keys\" data-ng-click=\"orderByColumn(key.property)\" data-ng-style=\"{ 'width': columnWidth + '%' }\" class=\"paged-list-header\" data-ng-class=\"'paged-list-header-'+key.property\">\n" +
    "                    {{ key.i18n | translate }}\n" +
    "                    <span class=\"header-icon\" ng-show=\"visibleSortingHeader === key.property\" ng-class=\"{ 'down': headersSortingState[key.property] == true, 'up': headersSortingState[key.property] == false }\"></span>\n" +
    "                </th>\n" +
    "            </tr>\n" +
    "        </thead>\n" +
    "        <tbody>\n" +
    "            <tr data-ng-repeat=\" item in filteredItems | startFrom:(currentPage-1)*itemsPerPage | limitTo:itemsPerPage \" class=\"techne-table-xs-right techne-table-xs-left paged-list-item \">\n" +
    "                <td ng-repeat=\"key in keys\" ng-class=\"'paged-list-item-'+key.property\">\n" +
    "                    <div data-ng-if=\"renderers[key.property]\" compile-html=\"renderers[key.property](item, key)\"></div>\n" +
    "                    <span data-ng-if=\"!renderers[key.property]\">{{ item[key.property] }}</span>\n" +
    "                </td>\n" +
    "            </tr>\n" +
    "        </tbody>\n" +
    "    </table>\n" +
    "\n" +
    "    <div class=\"pagination-container \">\n" +
    "        <pagination boundary-links=\"true\" total-items=\"totalItems\" items-per-page=\"itemsPerPage\" ng-model=\"currentPage\" class=\"pagination-sm\" previous-text=\"&lsaquo;\" next-text=\"&rsaquo;\" first-text=\"&laquo;\" last-text=\"&raquo;\"></pagination>\n" +
    "    </div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/core/decoratorFilter/managePerspectivesTemplate.html',
    "<div class=\"modal-header\">\n" +
    "    <h3 class=\"modal-title\">{{ 'modal.perpectives.title' | translate }}</h3>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "    <table>\n" +
    "        <tr>\n" +
    "            <td rowspan=\"2\">\n" +
    "                {{'modal.perpectives.header.perspectives.name' | translate}}\n" +
    "                <div>\n" +
    "                    <select size=\"12\" data-ng-change=\"perspectiveChanged()\" class=\"form-control\" data-ng-options=\"p.name | translate for p in perspectives\" data-ng-model=\"perspective\"></select>\n" +
    "                </div>\n" +
    "                <button type=\"button\" class=\"btn btn-default btn-xs\" data-ng-click=\"createPerspective()\">\n" +
    "                    <span class=\"glyphicon glyphicon-plus\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "                <button type=\"button\" class=\"btn btn-danger btn-xs\" data-ng-if=\"perspective.system !== true\" data-ng-click=\"deletePerspective()\">\n" +
    "                    <span class=\"glyphicon glyphicon-remove\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "            </td>\n" +
    "            <td width=\"15px\">\n" +
    "            </td>\n" +
    "            <td valign=\"top\">\n" +
    "                {{'modal.perpectives.header.name.name' | translate}}:\n" +
    "                <span data-ng-if=\"perspective.system===true\">\n" +
    "                    <span style=\"padding-left: 5px\">{{ perspective.name | translate }}</span>\n" +
    "                    <span class='pull-right' style=\"color: #feffc1; padding-left: 10px\">{{ warningForSystem | translate }}</span>\n" +
    "                </span>\n" +
    "                <input style='padding-left: 5px; color: black' data-ng-if=\"perspective.system!==true\" type=\"text\" data-ng-required=\"true\" data-ng-trim=\"true\" data-ng-change=\"nameChanged()\" data-ng-disabled=\"perspective.system === true\" data-ng-model=\"perspective.name\">\n" +
    "                <span style=\"color: #ff0e18\">{{ renameError | translate }}</span>\n" +
    "            </td>\n" +
    "        </tr>\n" +
    "        <tr>\n" +
    "            <td width=\"25px\">\n" +
    "            </td>\n" +
    "            <td>\n" +
    "                {{'modal.perpectives.header.decorators.name' | translate}}\n" +
    "                <div data-ng-repeat=\"ps in decoratorSet\">\n" +
    "                    <input type=\"checkbox\" data-ng-model=\"ps.checked\" data-ng-change=\"save()\" data-ng-disabled=\"perspective.system === true\"> {{ ps.name }}\n" +
    "                </div>\n" +
    "            </td>\n" +
    "        </tr>\n" +
    "    </table>\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-default\" data-ng-click=\"close()\" data-translate=\"perspectives.actions.close\"></button>\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/core/decoratorFilter/perspectiveTemplate.html',
    "<div>\n" +
    "    <span class=\"floating-perspective\" data-ng-mouseleave=\"mouseOff()\" data-ng-mouseenter=\"mouseOn()\">\n" +
    "        <div>\n" +
    "            <span class=\"xposed-perspectives\">\n" +
    "                <span class=\"glyphicon glyphicon-th\" aria-hidden=\"true\"></span>\n" +
    "            </span>\n" +
    "        </div>\n" +
    "        <div class=\"floating-bg\" data-ng-show=\"!showButtonOnly()\">\n" +
    "            <div>\n" +
    "                <span data-translate=\"modal.perpectives.header.perspectives.name\"></span>\n" +
    "            </div>\n" +
    "            <div class=\"form-group form-inline\">\n" +
    "                <select data-ng-change=\"perspectiveSelected(perspective)\" class=\"form-control\" data-ng-options=\"p.name | translate for p in perspectives\" ng-model=\"perspective\" />\n" +
    "                <button type=\"button\" data-ng-click=\"manage()\" class=\"btn btn-default\" aria-hidden=\"true\">\n" +
    "                    <span class=\"glyphicon glyphicon-edit\" aria-hidden=\"true\"></span>\n" +
    "                </button>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </span>\n" +
    "</div>\n" +
    "<div data-ng-transclude></div>"
  );


  $templateCache.put('web/smartedit/modules/systemModule/features/contextualMenu/contextualMenuDecoratorTemplate.html',
    "<div class=\"cmsx-ctx-wrapper1\">\n" +
    "    <div class=\"cmsx-ctx-wrapper2\">\n" +
    "        <div class=\"decorative-top-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"decorative-right-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"decorative-bottom-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"decorative-left-border decorative-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "        <div class=\"contextualMenuOverlay\" data-ng-show=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "            <div data-ng-repeat=\"item in ctrl.getItems().leftMenuItems\" class=\"btn btn-primary cmsx-ctx-btns\" data-ng-init=\"itemsrc = item.iconIdle\" data-ng-mouseout=\"itemsrc = item.iconIdle\" data-ng-mouseover=\"itemsrc = item.iconNonIdle\">\n" +
    "                <span id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-contextual-menu-item=\"\" data-item=\"item\" data-component-id=\"{{ctrl.smarteditComponentId}}\" data-component-type=\"{{ctrl.smarteditComponentType}}\" data-container-id=\"{{ctrl.smarteditContainerId}}\" data-container-type=\"{{ctrl.smarteditContainerType}}\" class=\"ng-class:{clickable:true}\">\n" +
    "                    <img data-ng-src=\"{{itemsrc}}\" id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" />\n" +
    "                </span>\n" +
    "                <img id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" class=\"{{item.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-ng-src=\"{{itemsrc}}\" data-contextual-menu-item=\"\" data-item=\"item\" data-component-id=\"{{ctrl.smarteditComponentId}}\" data-component-type=\"{{ctrl.smarteditComponentType}}\" data-container-id=\"{{ctrl.smarteditContainerId}}\" data-container-type=\"{{ctrl.smarteditContainerType}}\" />\n" +
    "            </div>\n" +
    "            <div data-ng-if=\"ctrl.getItems().moreMenuItems.length > 0\" class=\"cmsx-ctx-more\">\n" +
    "\n" +
    "                <div class=\"btn-group yCmsCtxMenu\" dropdown is-open=\"ctrl.status.isopen\" dropdown-append-to-body data-ng-init=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseout=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseover=\"itemsrc = ctrl.moreButton.iconNonIdle\">\n" +
    "\n" +
    "                    <button type=\"button\" class=\"cmsx-ctx-more-btn pull-right\" dropdown-toggle data-ng-click=\"ctrl.toggleDropdown($event)\">\n" +
    "                        <img title=\"{{ctrl.moreButton.i18nKey | translate}}\" data-ng-src=\"{{ctrl.status.isopen? ctrl.moreButton.iconNonIdle : itemsrc}}\" data-ng-class=\"{noboxshadow:ctrl.status.isopen}\" />\n" +
    "                    </button>\n" +
    "                    <ul class=\"dropdown-menu\" id=\"{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-more-menu\" role=\"menu\">\n" +
    "                        <li data-ng-repeat=\"item in ctrl.getItems().moreMenuItems\">\n" +
    "                            <a data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-item=\"item\" data-smartedit-id=\"{{ctrl.smarteditComponentId}}\" data-smartedit-type=\"{{ctrl.smarteditComponentType}}\">\n" +
    "                                <span data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-contextual-menu-item=\"\" data-item=\"item\" data-component-id=\"{{ctrl.smarteditComponentId}}\" data-component-type=\"{{ctrl.smarteditComponentType}}\" data-container-id=\"{{ctrl.smarteditContainerId}}\" data-container-type=\"{{ctrl.smarteditContainerType}}\" class=\"ng-class:{clickable:true}\">\n" +
    "                                    <img class=\"cmsx-ctx-menu-more-icon\" data-ng-src=\"{{itemsrc}}\" id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" />\n" +
    "\n" +
    "                                </span>\n" +
    "                                <!--<span class=\"icontext {{item.displayClass}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" />-->\n" +
    "                                <img id=\"{{item.i18nKey | translate}}-{{ctrl.smarteditComponentId}}-{{ctrl.smarteditComponentType}}-{{$index}}\" title=\"{{item.i18nKey | translate}}\" class=\"cmsx-ctx-menu-more-icon\" data-ng-if=\"item.smallIcon && !ctrl.isHybrisIcon(item.smallIcon)\" data-ng-src=\"{{item.smallIcon}}\" />\n" +
    "                                <span class=\"labeltext\">{{item.i18nKey | translate}}</span>\n" +
    "                            </a>\n" +
    "                        </li>\n" +
    "                    </ul>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "            <div class=\"contextualmenuBackground\"></div>\n" +
    "        </div>\n" +
    "        <div class=\"yWrapperData\">\n" +
    "            <div data-ng-transclude></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/modules/systemModule/features/slotContextualMenu/basicSlotContextualMenuDecoratorTemplate.html',
    "<div class=\"cmsx-ctx-wrapper1 se-slot-contextual-menu-level1\" data-ng-if=\"ctrl.isSlotNotEmpty\">\n" +
    "    <div class=\"cmsx-ctx-wrapper2 se-slot-contextual-menu-level2\">\n" +
    "        <div class=\"decorative-panel-basic-slot-contextual-menu\" data-ng-if=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "            <div data-ng-repeat=\"item in ctrl.getItems().leftMenuItems\" class=\"btn btn-primary cmsx-ctx-btns pull-right\" data-ng-init=\"itemsrc = item.iconIdle\" data-ng-mouseout=\"itemsrc = item.iconIdle\" data-ng-mouseover=\"itemsrc = item.iconNonIdle\">\n" +
    "                <div data-ng-if=\"!item.templateUrl\">\n" +
    "                    <span id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon\" data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" class=\"ng-class:{clickable:true}\">\n" +
    "                        <img data-ng-src=\"{{itemsrc}}\" id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon-img\" title=\"{{::item.i18nKey | translate}}\" />\n" +
    "                    </span>\n" +
    "                    <img id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}\" title=\"{{::item.i18nKey | translate}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" class=\"{{item.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-ng-src=\"{{itemsrc}}\" alt=\"{{item.i18nKey}}\" />\n" +
    "                </div>\n" +
    "                <div data-ng-if=\"item.templateUrl\">\n" +
    "                    <div data-ng-include=\"item.templateUrl\"></div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div data-ng-if=\"ctrl.getItems().moreMenuItems.length > 0\" dropdown dropdown-append-to-body is-open=\"ctrl.status.isopen\" class=\"btn btn-primary pull-right cmsx-ctx-btns dropdown\" data-ng-init=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseout=\"itemsrc = ctrl.moreButton.iconIdle\" data-ng-mouseover=\"itemsrc = ctrl.moreButton.iconNonIdle\">\n" +
    "                <img id=\"{{ctrl.moreButton.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}\" title=\"{{ctrl.moreButton.i18nKey | translate}}\" data-ng-if=\"ctrl.moreButton.iconIdle && !ctrl.isHybrisIcon(ctrl.moreButton.displayClass)\" class=\"{{ctrl.moreButton.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(ctrl.moreButton, $event)\" data-ng-src=\"{{itemsrc}}\" />\n" +
    "                <ul class=\"dropdown-menu\" role=\"menu\">\n" +
    "                    <li role=\"menu\" data-ng-repeat=\"item in ctrl.getItems().moreMenuItems\" id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-more\">\n" +
    "                        <div data-ng-if=\"!item.templateUrl\">\n" +
    "                            <a data-ng-click=\"ctrl.triggerItemCallback(item, $event)\">\n" +
    "                                <span data-ng-if=\"item.displayClass\" class=\"icontext {{item.displayClass}}\"></span>\n" +
    "                                <img title=\"{{::item.i18nKey | translate}}\" class=\"cmsx-ctx-menu-more-icon\" data-ng-if=\"item.smallIcon && !ctrl.isHybrisIcon(item.smallIcon)\" data-ng-src=\"{{item.smallIcon}}\" alt=\"{{item.i18nKey}}\" />\n" +
    "                                <span class=\"labeltext\">{{::item.i18nKey | translate}}</span>\n" +
    "                            </a>\n" +
    "                        </div>\n" +
    "                        <div data-ng-if=\"item.templateUrl\">\n" +
    "                            <div data-ng-include=\"item.templateUrl\"></div>\n" +
    "                        </div>\n" +
    "                    </li>\n" +
    "                </ul>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "        <div class=\"decorator-padding-container\">\n" +
    "            <div class=\"decorator-basic-slot-border\" data-ng-if=\"ctrl.active\"></div>\n" +
    "            <div data-ng-transclude class=\"yWrapperData\"></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/smartedit/modules/systemModule/features/slotContextualMenu/slotContextualMenuDecoratorTemplate.html',
    "<div>\n" +
    "    <div class=\"cmsx-ctx-wrapper1 se-slot-contextual-menu-level1\">\n" +
    "        <div class=\"cmsx-ctx-wrapper2 se-slot-contextual-menu-level2\">\n" +
    "            <div class=\"decorative-panel-area\" data-ng-if=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "                <p class=\"decorative-panel-text\">{{::ctrl.smarteditComponentId}}</p>\n" +
    "                <div class=\"decorator-panel-padding-center\"></div>\n" +
    "                <div class=\"decorative-panel-slot-contextual-menu\" data-ng-if=\"ctrl.showOverlay() || ctrl.status.isopen\">\n" +
    "                    <div data-ng-repeat=\"item in ctrl.getItems().leftMenuItems\" class=\"btn btn-primary cmsx-ctx-btns pull-right\" data-ng-init=\"itemsrc = item.iconIdle\" data-ng-mouseout=\"itemsrc = item.iconIdle\" data-ng-mouseover=\"itemsrc = item.iconNonIdle\">\n" +
    "                        <div data-ng-if=\"!item.templateUrl\">\n" +
    "                            <span id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon\" data-ng-if=\"item.iconIdle && ctrl.isHybrisIcon(item.displayClass)\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" class=\"ng-class:{clickable:true}\">\n" +
    "                                <img data-ng-src=\"{{itemsrc}}\" id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}-hyicon-img\" title=\"{{::item.i18nKey | translate}}\" />\n" +
    "                            </span>\n" +
    "                            <img id=\"{{::item.i18nKey | translate}}-{{::ctrl.smarteditComponentId}}-{{::ctrl.smarteditComponentType}}\" title=\"{{::item.i18nKey | translate}}\" data-ng-if=\"item.iconIdle && !ctrl.isHybrisIcon(item.displayClass)\" class=\"{{item.displayClass}}\" data-ng-class=\"{clickable:true}\" data-ng-click=\"ctrl.triggerItemCallback(item, $event)\" data-ng-src=\"{{itemsrc}}\" alt=\"{{item.i18nKey}}\" />\n" +
    "                        </div>\n" +
    "                        <div data-ng-if=\"item.templateUrl\">\n" +
    "                            <div data-ng-include=\"item.templateUrl\"></div>\n" +
    "                        </div>\n" +
    "                        <div class=\"slot-context-menu-divider\"></div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "\n" +
    "            <div class=\"decorator-padding-container\">\n" +
    "                <div class=\"decorator-padding-left\" data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "                <div class=\"decorator-slot-border\" data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "                <div class=\"yWrapperData\" data-ng-transclude data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "                <div class=\"decorator-padding-right\" data-ng-class=\"{active: ctrl.active}\"></div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );

}]);

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
angular.module('smarteditloader', [
        'loadConfigModule',
        'bootstrapServiceModule',
        'coretemplates',
        'translationServiceModule',
        'httpAuthInterceptorModule',
        'httpErrorInterceptorModule', 'alertsBoxModule'
    ])
    .config(['$logProvider', function($logProvider) {
        $logProvider.debugEnabled(false);
    }])
    .run(['loadConfigManagerService', 'bootstrapService', function(loadConfigManagerService, bootstrapService) {

        loadConfigManagerService.loadAsObject().then(function(configurations) {
            bootstrapService.bootstrapContainerModules(configurations);
        });

    }]);
