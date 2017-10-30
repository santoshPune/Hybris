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
angular.module('pageInformationModule', ['alertServiceModule'])
    .directive('pageInformation', ['$log', 'alertService', function($log, alertService) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/pageToolMenu/pageInformation/pageInformationTemplate.html',
            restrict: 'E',
            transclude: true,
            scope: {
                selectedItemCallbacks: "="
            },
            link: function(scope, elem, attrs) {
                scope.selectedItemCallbacks.onClose = function() {
                    $log.debug('Page Information close callback triggered');
                };
            }
        };
    }]);
