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

.controller('hasOperationPermissionController', function($scope, $log, authorizationService, systemEventService, EVENTS) {
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
})

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
