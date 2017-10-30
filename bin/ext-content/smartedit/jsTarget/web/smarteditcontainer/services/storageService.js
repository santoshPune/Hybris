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
 * @name storageServiceModule
 * @description
 * # The storageServiceModule
 *
 * The Storage Service Module provides a service that allows storing temporary information in the browser.
 *
 */
angular.module('storageServiceModule', ['gatewayProxyModule', 'ngCookies']) //restServiceFactoryModule already requires ngResource and ngRoute
    /**
     * @ngdoc service
     * @name storageServiceModule.service:storageService
     *
     * @description
     * The Storage service is used to store temporary information in the browser. The service keeps track of key/value pairs
     * of authTokens that authenticate the specified user on different URIs.
     *
     */
    .factory("storageService", ['$rootScope', '$cookies', '$window', 'gatewayProxy', function($rootScope, $cookies, $window, gatewayProxy) {

        var STORAGE_COOKIE_NAME = "smartedit-sessions";
        var PRINCIPAL_UID_KEY = "principal-uid";

        var StorageService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ['isInitialized', 'storeAuthToken', 'getAuthToken', 'removeAuthToken', 'removeAllAuthTokens', 'storePrincipalIdentifier', 'getPrincipalIdentifier', 'removePrincipalIdentifier', 'getValueFromCookie']);
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#isInitialized
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to determine if the storage service has been initialized properly. It
         * makes sure that the smartedit-sessions cookie is available in the browser.
         *
         * @returns {Boolean} Indicates if the storage service was properly initialized.
         */
        StorageService.prototype.isInitialized = function() {
            return $cookies.get(STORAGE_COOKIE_NAME) !== undefined;
        };


        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#storePrincipalIdentifier
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to store the principal's login name in storage service
         *
         * @param {String} principalUID Value associated to store principal's login.
         */
        StorageService.prototype.storePrincipalIdentifier = function(principalUID) {
            var sessions = this.getAuthTokens();
            sessions[PRINCIPAL_UID_KEY] = principalUID;
            this._setToAuthCookie(sessions);

        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#removePrincipalIdentifier
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to remove the principal's UID from storage service
         *
         */
        StorageService.prototype.removePrincipalIdentifier = function() {
            var sessions = this.getAuthTokens();
            delete sessions[PRINCIPAL_UID_KEY];
            this._setToAuthCookie(sessions);
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#getPrincipalIdentifier
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to retrieve the principal's login name from storage service
         *
         * @param {String} principalNameKey key associated to store principal's name.
         * @returns {String} principalNameValue principal's name associated with the key.
         */
        StorageService.prototype.getPrincipalIdentifier = function() {
            var sessions = this.getAuthTokens();
            return sessions[PRINCIPAL_UID_KEY];
        };


        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#storeAuthToken
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method creates and stores a new key/value entry. It associates an authentication token with a
         * URI.
         *
         * @param {String} authURI The URI that identifies the resource(s) to be authenticated with the authToken. Will be used as a key.
         * @param {String} auth The token to be used to authenticate the user in the provided URI.
         */
        StorageService.prototype.storeAuthToken = function(authURI, auth) {
            var sessions = this.getAuthTokens();
            sessions[authURI] = auth;
            this._setToAuthCookie(sessions);

        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#getAuthTokens
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to retrieve a map having as value the current access token stored for a given authentication entry point as key
         */
        StorageService.prototype.getAuthTokens = function() {
            return this._getValueFromCookie(STORAGE_COOKIE_NAME, true) || {};
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#getAuthToken
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method is used to retrieve the authToken associated with the provided URI.
         *
         * @param {String} authURI The URI for which the associated authToken is to be retrieved.
         * @returns {String} The authToken used to authenticate the current user in the provided URI.
         */
        StorageService.prototype.getAuthToken = function(authURI) {
            var sessions = this.getAuthTokens();
            return sessions[authURI];
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#removeAuthToken
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * Removes the authToken associated with the provided URI.
         *
         * @param {String} authURI The URI for which its authToken is to be removed.
         */
        StorageService.prototype.removeAuthToken = function(authURI) {
            var sessions = this.getAuthTokens();
            delete sessions[authURI];
            this._setToAuthCookie(sessions);
        };

        StorageService.prototype._removeAllAuthTokens = function() {
            $cookies.remove(STORAGE_COOKIE_NAME);
        };

        StorageService.prototype._putValueInCookie = function(cookieName, value, encode) {
            var processedValue = JSON.stringify(value);
            processedValue = (encode) ? btoa(processedValue) : processedValue;
            if ($window.location.protocol.indexOf("https") >= 0) {
                $cookies.put(cookieName, processedValue, {
                    secure: true
                });
            } else {
                $cookies.put(cookieName, processedValue);
            }
        };
        StorageService.prototype._getValueFromCookie = function(cookieName, isEncoded) {
            var rawValue = $cookies.get(cookieName);
            var value = null;
            if (rawValue) {
                try {
                    value = JSON.parse((isEncoded) ? atob(rawValue) : rawValue);
                } catch (e) {
                    //protecting against deserialization issue
                }
            }

            return value;
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#removeAllAuthTokens
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * This method removes all authURI/authToken key/pairs from the storage service.
         */
        StorageService.prototype.removeAllAuthTokens = function() {
            this._removeAllAuthTokens();
        };
        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#putValueInCookie
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * Stores the provided value in a cookie.
         *
         * @param {String} cookieName The name of the cookie where to store the provided value.
         * @param {String} value The value to store in the cookie
         * @param {Boolean} encode A flag that specifies whether to encode the value or not (to obfuscate its content).
         */
        StorageService.prototype.putValueInCookie = function(cookieName, value, encode) {
            this._putValueInCookie(cookieName, value, encode);
        };

        /**
         * @ngdoc method
         * @name storageServiceModule.service:storageService#getValueFromCookie
         * @methodOf storageServiceModule.service:storageService
         *
         * @description
         * Retrieves the value stored in the cookie identified by the provided name.
         *
         * @param {String} cookieName The name of the cookie where the desired value is stored.
         * @param {Boolean} isEncoded A flag that specifies whether the value is encoded or not.
         * @returns {String} The value retrieved from the cookie or null if the cookie doesn't exist.
         */
        StorageService.prototype.getValueFromCookie = function(cookieName, isEncoded) {
            return this._getValueFromCookie(cookieName, isEncoded);
        };

        StorageService.prototype._setToAuthCookie = function(sessions) {
            if (Object.keys(sessions).length > 0) {
                this._putValueInCookie(STORAGE_COOKIE_NAME, sessions, true);
            } else {
                this._removeAllAuthTokens();
            }
        };

        return new StorageService("storage");
    }]);
