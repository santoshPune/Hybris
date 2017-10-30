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
angular.module('localizedElementModule', ['tabsetModule'])
    .directive('localizedElement', function() {

        return {
            templateUrl: 'web/common/services/localizedElement/localizedElementTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: false,
            scope: {
                model: '=',
                languages: '=',
                inputTemplate: '='
            },

            link: function($scope, element, attrs) {

                $scope.tabs = [];

                $scope.$watch("model", function(model) {
                    if (model) {
                        var inputTemplate = $scope.inputTemplate ? $scope.inputTemplate : attrs.inputTemplate;

                        $scope.tabs.length = 0;
                        Array.prototype.push.apply($scope.tabs, $scope.languages.map(function(language) {
                            return {
                                id: language.isocode,
                                title: language.isocode.toUpperCase() + (language.required ? "*" : ""),
                                templateUrl: inputTemplate
                            };
                        }));
                    }
                });

                $scope.$watch("model.field.errors", function(errors) {
                    if ($scope.model) {
                        var errorMap = errors ? errors.reduce(function(holder, next) {
                            holder[next.language] = true;
                            return holder;
                        }, {}) : {};
                        $scope.tabs.forEach(function(tab) {
                            var error = errorMap[tab.id];
                            tab.hasErrors = error !== undefined ? error : false;
                        });
                    }
                }, true);

            }
        };
    });
