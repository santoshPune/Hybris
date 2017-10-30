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
angular.module('perspectiveSelectorModule', ['iframeClickDetectionServiceModule', 'perspectiveServiceModule'])
    .constant('perspectiveTest', false)
    .directive('perspectiveSelector', ['$document', '$log', 'perspectiveService', 'iframeClickDetectionService', 'hitch', 'ALL_PERSPECTIVE', 'perspectiveTest', function($document, $log, perspectiveService, iframeClickDetectionService, hitch, ALL_PERSPECTIVE, perspectiveTest) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/perspectiveSelectorWidget/perspectiveSelectorWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function(scope) {

                // Directive Properties
                scope.status = {
                    isopen: false
                };

                scope.getPerspectives = function() {
                    return perspectiveService.getPerspectives().filter(function(persp) {

                        return (!perspectiveService.data.activePerspective || persp.key != perspectiveService.data.activePerspective.key) && (persp.key != ALL_PERSPECTIVE || perspectiveTest);
                    });
                };
                scope.perspectiveData = perspectiveService.data;

                // Methods
                scope.selectPerspective = function(choice) {
                    try {
                        perspectiveService.switchTo(choice);
                        scope.status.isopen = false;
                    } catch (e) {
                        $log.error("selectPerspective() - Cannot select perspective.", e);
                    }
                };

                scope.toggleDropdown = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    scope.status.isopen = !scope.status.isopen;
                };

                iframeClickDetectionService.registerCallback('perspectiveSelectorClose', function() {
                    scope.status.isopen = false;
                });

                $document.on('click', function(event) {

                    if ($(event.target).parents('.ySEPerspectiveSelector').length <= 0 && scope.status.isopen) {
                        scope.status.isopen = false;
                        scope.$apply();
                    }
                });

            }
        };
    }]);
