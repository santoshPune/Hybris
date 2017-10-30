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
angular.module('bootstrapServiceModule', ['functionsModule', 'configurationExtractorServiceModule', 'sharedDataServiceModule', 'gatewayFactoryModule'])
    .factory('bootstrapService', function(configurationExtractorService, sharedDataService, injectJS, $log, $http, $q, hitch, customTimeout, WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY) {

        return {

            _getIframe: function() {
                return document.getElementsByTagName('iframe')[0].contentWindow;
            },
            bootstrapSmartEditContainer: function() {
                angular.bootstrap(document, ["smarteditcontainer"]);
            },
            addDependencyToSmartEditContainer: function(app) {
                try {
                    angular.module(app);
                    $log.debug('Adding app: "' + app + '" to smarteditcontainer');
                    angular.module('smarteditcontainer').requires.push(app);
                } catch (ex) {
                    $log.error('Failed to load module ' + app + '; SmartEdit functionality may be compromised.');
                }
            },
            bootstrapContainerModules: function(configurations) {

                var seContainerModules = configurationExtractorService.extractSEContainerModules(configurations);
                $log.debug("outerAppLocations are:", seContainerModules.appLocations);

                sharedDataService.set('authenticationMap', seContainerModules.authenticationMap);
                sharedDataService.set('credentialsMap', configurations['authentication.credentials']);

                angular.module('smarteditcontainer')
                    .constant('domain', configurations.domain)
                    .constant('smarteditroot', configurations.smarteditroot)
                    .constant(WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY, configurations[WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY]);

                injectJS.execute({
                    srcs: seContainerModules.appLocations,
                    callback: hitch(this, function() {
                        seContainerModules.applications.forEach(hitch(this, function(app) {
                            this.addDependencyToSmartEditContainer(app);
                        }));
                        this.bootstrapSmartEditContainer();
                    })
                });
            },

            bootstrapSEApp: function(configurations) {

                var seModules = configurationExtractorService.extractSEModules(configurations);

                sharedDataService.set('authenticationMap', seModules.authenticationMap);
                sharedDataService.set('credentialsMap', configurations['authentication.credentials']);

                var resources = {
                    properties: {
                        domain: configurations.domain,
                        smarteditroot: configurations.smarteditroot,
                        applications: seModules.applications
                    },
                    js: [
                        configurations.smarteditroot + '/static-resources/dist/smartedit/js/presmartedit.js'
                    ],
                    css: [
                        configurations.smarteditroot + '/static-resources/dist/smartedit/css/inner-styling.css'
                    ]
                };
                resources.properties[WHITE_LISTED_STOREFRONTS_CONFIGURATION_KEY] = configurations.whiteListedStorefronts;

                var validUris = [];
                var validApplications = [];
                $q.all(seModules.appLocations.map(function(appUri, index) {
                    var deferred = $q.defer();
                    $http.get(appUri).then(function() {
                        validApplications.push(seModules.applications[index]);
                        validUris.push(appUri);
                        deferred.resolve();
                    }, function() {
                        $log.error('Failed to load module ' + seModules.applications[index] + '; SmartEdit functionality may be compromised.');
                        deferred.resolve();
                    });
                    return deferred.promise;
                })).then(hitch(this, function() {
                    resources.js = resources.js.concat(validUris);
                    resources.js.push(configurations.smarteditroot + '/static-resources/dist/smartedit/js/postsmartedit.js');
                    resources.properties.applications = validApplications;

                    this._getIframe().postMessage({
                        eventName: 'smarteditBootstrap',
                        resources: resources
                    }, '*');
                }));

            }
        };
    });
