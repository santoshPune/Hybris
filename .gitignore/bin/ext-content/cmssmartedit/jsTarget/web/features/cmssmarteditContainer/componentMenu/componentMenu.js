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
angular.module('componentMenuModule', ['componentsModule', 'componentServiceModule', 'assetsServiceModule', 'cmsDragAndDropServiceModule'])
    .controller('ComponentMenuController', ['$log', '$timeout', 'ComponentService', 'cmsDragAndDropService', function($log, $timeout, ComponentService, cmsDragAndDropService) {

        this.types = {};

        ComponentService.loadComponentTypes().then(function() {
            angular.copy(ComponentService.listOfComponentTypes, this.types);
        }.bind(this));

        this.loadComponentItems = function(mask, pageSize, currentPage) {
            return ComponentService.loadPagedComponentItems(mask, pageSize, currentPage).then(function(page) {
                page.results = page.componentItems;
                delete page.componentItems;
                return page;
            });
        };

        this.preventDefault = function(oEvent) {
            oEvent.stopPropagation();
        };

    }])
    .filter('nameFilter', function() {
        return function(components, criteria) {
            var filterResult = [];
            if (!criteria || criteria.length < 3)
                return components;

            criteria = criteria.toLowerCase();
            var criteriaList = criteria.split(" ");

            (components || []).forEach(function(component) {
                var match = true;
                var term = component.name.toLowerCase();

                criteriaList.forEach(function(item) {
                    if (term.indexOf(item) == -1) {
                        match = false;
                        return false;
                    }
                });

                if (match && filterResult.indexOf(component) == -1) {
                    filterResult.push(component);
                }
            });
            return filterResult;
        };
    })
    /**
     * @ngdoc directive
     * @name componentMenuModule.directive:componentMenu
     * @scope
     * @restrict E
     * @element ANY
     *
     * @description
     * Component Menu widget that shows all the component types and customized components.
     */
    .directive('componentMenu', ['$rootScope', 'systemEventService', '$q', '$document', 'assetsService', 'cmsDragAndDropService', 'DRAG_AND_DROP_EVENTS', function($rootScope, systemEventService, $q, $document, assetsService, cmsDragAndDropService, DRAG_AND_DROP_EVENTS) {
        return {
            templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentMenuWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function($scope, elem, attrs) {

                $scope.parentBtn = elem.closest('.ySEHybridAction');

                $scope.status = {
                    isopen: false
                };

                $scope.icons = {
                    closed: ".." + assetsService.getAssetsRoot() + "/images/icon_add.png",
                    open: ".." + assetsService.getAssetsRoot() + "/images/icon_add_s.png"
                };

                $scope.menuIcon = $scope.icons.closed;

                $scope.updateIcon = function() {
                    if ($scope.status.isopen) {
                        $scope.menuIcon = $scope.icons.open;
                        $scope.parentBtn.addClass("ySEOpenComponent");

                    } else {
                        $scope.menuIcon = $scope.icons.closed;
                        $scope.parentBtn.removeClass("ySEOpenComponent");
                    }
                };

                systemEventService.registerEventHandler('ySEComponentMenuClose', function() {
                    $scope.status.isopen = false;
                    return $q.when();
                });

                $scope.$watch('status.isopen', $scope.updateIcon);

                $scope.preventDefault = function(oEvent) {
                    oEvent.stopPropagation();
                };

                $document.on('click', function(event) {

                    if ($(event.target).parents('.ySEComponentMenuW').length <= 0 && $scope.status.isopen) {

                        $scope.status.isopen = false;
                        $scope.$apply();
                    }
                });

                systemEventService.registerEventHandler(DRAG_AND_DROP_EVENTS.DRAG_STARTED, function() {
                    $scope.status.isopen = false;
                });
            }
        };
    }]);
