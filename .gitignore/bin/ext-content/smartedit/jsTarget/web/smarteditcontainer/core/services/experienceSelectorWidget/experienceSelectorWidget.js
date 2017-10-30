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
angular.module('experienceSelectorButtonModule', ['eventServiceModule', 'sharedDataServiceModule', 'sharedDataServiceModule', 'l10nModule'])
    .directive('experienceSelectorButton', ['systemEventService', 'sharedDataService', 'l10nFilter', '$interval', function(systemEventService, sharedDataService, l10nFilter, $interval) {
        return {
            templateUrl: 'web/smarteditcontainer/core/services/experienceSelectorWidget/experienceSelectorWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function($scope, elem, attrs) {

                systemEventService.registerEventHandler("experienceUpdate", function() {
                    return sharedDataService.get('experience').then(function(experience) {
                        $scope.experience = experience;
                    });
                });

                $scope.buildExperienceText = function() {
                    if (!$scope.experience) {
                        return '';
                    }

                    return l10nFilter($scope.experience.catalogDescriptor.name) + ' - ' +
                        $scope.experience.catalogDescriptor.catalogVersion + '  |  ' +
                        $scope.experience.languageDescriptor.nativeName +
                        ($scope.experience.time ? '  |  ' + $scope.experience.time : '');
                };
            }
        };
    }]);
