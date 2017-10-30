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
angular.module('componentsModule', ['cmssmarteditContainerTemplates', 'componentSearchModule', 'componentTypeModule', 'componentItemModule', 'eventServiceModule'])

.directive('componentTabs', ['$rootScope', 'systemEventService', '$q', function($rootScope, systemEventService, $q) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentTabsTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        link: function(scope, element, attrs) {

            scope.tabs = [{
                title: 'compomentmenu.tabs.componenttypes',
                disabled: false
            }, {
                title: 'compomentmenu.tabs.customizedcomp',
                disabled: false
            }];

            scope.selectTab = function(oEvent) {
                oEvent.stopPropagation();
            };

            systemEventService.registerEventHandler('ySEComponentMenuOpen', function() {
                scope.searchTerm = "";
                scope.activateItemsTab(0);

                return $q.when(); //temporary as we don't need to return anything
            });

            scope.focusOnSearch = function(oEvent) {
                if (scope.searchTerm && scope.searchTerm.length > 1) {
                    scope.activateItemsTab(1);
                }
            };

            scope.resetSearch = function(oEvent) {
                oEvent.stopPropagation();
                scope.activateItemsTab(0);

            };

            scope.activateItemsTab = function(iTab) {
                scope.tabs[iTab].active = true;
            };

        }
    };
}]);
