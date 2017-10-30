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
angular.module('inflectionPointSelectorModule', ['sharedDataServiceModule', 'sharedDataServiceModule', 'iFrameManagerModule', 'resourceLocationsModule', 'iframeClickDetectionServiceModule'])
    .directive('inflectionPointSelector', function(sharedDataService, iFrameManager, iframeClickDetectionService, $document) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/inflectionPointSelectorWidget/inflectionPointSelectorWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function($scope, $rootScope, elem, attrs, $route) {
                $scope.selectPoint = function(choice) {

                    $scope.currentPointSelected = choice;
                    $scope.status.isopen = !$scope.status.isopen;

                    if (choice !== undefined) {
                        iFrameManager.apply(choice, $scope.deviceOrientation);
                    }


                };
                var deviceSupportedLst = [];

                deviceSupportedLst = iFrameManager.getDeviceSupports();

                $scope.currentPointSelected = deviceSupportedLst[5];

                $scope.points = deviceSupportedLst;

                $scope.status = {
                    isopen: false
                };

                $scope.toggleDropdown = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.status.isopen = !$scope.status.isopen;
                };

                iframeClickDetectionService.registerCallback('inflectionPointClose', function() {
                    $scope.status.isopen = false;
                });

                $document.on('click', function(event) {

                    if ($(event.target).parents('inflection-point-selector').length <= 0 && $scope.status.isopen) {

                        $scope.status.isopen = false;
                        $scope.$apply();
                    }
                });

            }
        };
    });
