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
angular.module('componentSearchModule', []).directive('componentSearch', ['iframeClickDetectionService', function(iframeClickDetectionService) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentSearchTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        scope: false,

        link: function(scope, element, attrs) {

            scope.showResetButton = false;

            scope.selectSearch = function(oEvent) {
                oEvent.stopPropagation();
            };
            scope.preventDefault = function(oEvent) {
                oEvent.stopPropagation();
            };
            scope.closeComponentSearch = function(oEvent) {
                if (oEvent) {
                    oEvent.preventDefault();
                    oEvent.stopPropagation();
                }
                scope.status.isopen = false;
            };

            scope.$watch('status.isopen', function() {
                if (!scope.status.isopen) {
                    scope.searchTerm = "";
                }
            });

            scope.$watch('searchTerm', function() {
                scope.showResetButton = scope.searchTerm !== "";
            });

            iframeClickDetectionService.registerCallback('closeComponentSearch', function() {
                scope.closeComponentSearch();
                scope.searchTerm = "";
            });

        }
    };
}]);
