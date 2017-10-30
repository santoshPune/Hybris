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
angular.module('PermissionMockModule', ['ngMockE2E', 'resourceLocationsModule', 'functionsModule'])
    .run(function($httpBackend, resourceLocationToRegex, USER_GLOBAL_PERMISSIONS_RESOURCE_URI) {
        $httpBackend.whenGET(resourceLocationToRegex(USER_GLOBAL_PERMISSIONS_RESOURCE_URI)).respond(function(method, url) {
            var user = getUserFromUrl(url);
            if (user === 'admin') {
                return [200, {
                    "id": "global",
                    "permissions": [{
                        "key": "smartedit.configurationcenter.read",
                        "value": "true"
                    }, {
                        "key": "Smartedit.permissionType1.Read",
                        "value": "true"
                    }, {
                        "key": "Smartedit.permissionType2.Update",
                        "value": "true"
                    }, {
                        "key": "Smartedit.permissionType3.Delete",
                        "value": "false"
                    }]
                }];
            } else {
                return [200, {
                    "id": "global",
                    "permissions": [{
                        "key": "smartedit.configurationcenter.read",
                        "value": "false"
                    }, {
                        "key": "Smartedit.permissionType1.Read",
                        "value": "true"
                    }, {
                        "key": "Smartedit.permissionType2.Update",
                        "value": "true"
                    }, {
                        "key": "Smartedit.permissionType3.Delete",
                        "value": "false"
                    }]
                }];
            }
        });

        function getUserFromUrl(url) {
            return /principals\/(.+)\/.*/.exec(url)[1];
        }
    });

try {
    angular.module('smarteditloader').requires.push('PermissionMockModule');
    angular.module('smarteditcontainer').requires.push('PermissionMockModule');
} catch (ex) {}
