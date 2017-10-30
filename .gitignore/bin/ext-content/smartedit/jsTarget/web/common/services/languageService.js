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
