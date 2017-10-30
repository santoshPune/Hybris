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
 *
 *
 */
angular.module('personalizationsmarteditCommons')
    .directive('personalizationsmarteditPagination', function() {
        return {
            template: '<div ng-include="getContentUrl()"></div>',
            restrict: 'E',
            scope: {
                callback: "=",
                pages: "=",
                currentPage: "=",
                pageSizes: "=",
                currentSize: "=",
                pagesOffset: "=",
                fixedPageSize: "="
            },

            link: function($scope, element, attrs) {

                if (!$scope.callback) {
                    console.log("callback is undefined!");
                }

                $scope.pages = $scope.pages || [0, 1, 2];
                $scope.currentPage = $scope.currentPage || 0;
                $scope.pageSizes = $scope.pageSizes || [5, 10, 25, 50, 100];
                $scope.currentSize = $scope.currentSize || 10;
                $scope.pagesOffset = $scope.pagesOffset || 2;
                $scope.fixedPageSize = $scope.fixedPageSize || false; //NOSONAR

                $scope.getContentUrl = function() {
                    attrs.template = attrs.template || 'personalizationsmarteditPaginationTemplate.html';
                    return 'web/features/commons/personalizationsmarteditPagination/' + attrs.template;
                };

                $scope.pageClick = function(newValue) {
                    if ($scope.currentPage !== newValue) {
                        $scope.currentPage = newValue;
                        $scope.callback($scope);
                    }
                };

                $scope.pageSizeClick = function(newValue) {
                    if ($scope.currentSize !== newValue) {
                        $scope.currentSize = newValue;
                        $scope.currentPage = 0;
                        $scope.callback($scope);
                    }
                };

                $scope.hasPrevious = function() {
                    return $scope.currentPage > 0;
                };

                $scope.hasNext = function() {
                    return $scope.currentPage < $scope.pages.length - 1;
                };

                $scope.isActive = function(value) {
                    return $scope.currentPage === value;
                };

                $scope.nextClick = function() {
                    if ($scope.hasNext()) {
                        $scope.currentPage++;
                        $scope.callback($scope);
                    }
                };

                $scope.prevClick = function() {
                    if ($scope.hasPrevious()) {
                        $scope.currentPage--;
                        $scope.callback($scope);
                    }
                };

                $scope.pagesToDisplay = function() {
                    var numberOfPages = 2 * $scope.pagesOffset + 1;
                    if ($scope.pages.length <= numberOfPages) {
                        return $scope.pages;
                    } else {
                        var start = Math.max($scope.currentPage - $scope.pagesOffset, 0);
                        if (start + numberOfPages > $scope.pages.length) {
                            start = $scope.pages.length - numberOfPages;
                        }
                        return $scope.pages.slice(start, start + numberOfPages);
                    }
                };

                $scope.availablePageSizes = function() {
                    return $scope.pageSizes;
                };

                $scope.getCurrentPageSize = function() {
                    return $scope.currentSize;
                };

                $scope.isFixedPageSize = function() {
                    return $scope.fixedPageSize;
                };

            }
        };
    });
