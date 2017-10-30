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
    .factory('experienceInterceptor', function(hitch, sharedDataService, interceptorHelper, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CMSWEBSERVICES_PATH, MEDIA_PATH, CONTEXT_SITE_ID) {

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
    })
    .config(function($httpProvider) {
        $httpProvider.interceptors.push('experienceInterceptor');
    });
