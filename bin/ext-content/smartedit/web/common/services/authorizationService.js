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
.service('authorizationService', function(restServiceFactory, storageService, permissionKeysFactory, USER_GLOBAL_PERMISSIONS_RESOURCE_URI, $log) {

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

});
