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
    .factory('LoadConfigManager', function($resource, hitch, copy, $q, convertToArray, sharedDataService, $log, SMARTEDIT_ROOT, SMARTEDIT_RESOURCE_URI_REGEXP, CONFIGURATION_URI) {

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

    })

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
.factory('loadConfigManagerService', function(LoadConfigManager) {
    return new LoadConfigManager();
});
