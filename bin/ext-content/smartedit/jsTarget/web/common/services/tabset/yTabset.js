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
(function() {

    /**
     * @ngdoc overview
     * @name tabsetModule
     * @description
     *
     * The Tabset module provides the directives required to display a group of tabsets within a tabset. The
     * {@link tabsetModule.directive:yTabset yTabset} is of particular interest to SmartEdit developers
     * because this directive is responsible for displaying and organizing tabs.
     *
     */
    angular.module('tabsetModule', ['ui.bootstrap', 'coretemplates', 'functionsModule', 'eventServiceModule', 'translationServiceModule'])

    /**
     * @ngdoc directive
     * @name tabsetModule.directive:yTabset
     * @scope
     * @restrict E
     * @element smartedit-tabset
     *
     * @description
     * The directive responsible for displaying and organizing tabs within a tabset. A specified number of tabs will
     * display a tab header. If there are more tabs than the maximum number defined, the remaining tabs will be grouped
     * in a drop-down menu with the header "More". When a user clicks on a tab header or an item from the drop-down
     * menu, the content of the tabset changes to the body of the selected tab.
     *
     * Note: The body of each tab is wrapped within a {@link tabsetModule.directive:yTab yTab} directive.
     *
     * @param {Object} model Custom data to be passed to each tab. Neither the smartedit-tabset directive or the
     * smartedit-tab directive can modify this value. The tabs' contents determine how to parse and use this object.
     * @param {Object[]} tabsList A list that contains the configuration for each of the tabs to be displayed in the tabset.
     * @param {string} tabsList.id The ID used to track the tab within the tabset.
     * @param {String} tabsList.title The tab header.
     * @param {String} tabsList.templateUrl Path to the HTML fragment to be displayed as the tab content.
     * @param {boolean} tabsList.hasErrors Flag that indicates whether a visual error is to be displayed in the tab or not.
     * @param {Function} tabControl  An optional parameter. A function that will be called with scope as its parameter.
     * It allows the caller to register extra functionality to be executed in the child tabs.
     * @param {Number} numTabsDisplayed The number of tabs for which tab headers will be displayed. The remaining tab
     * headers will be grouped within the 'MORE' drop-down menu.
     *
     */
    .directive('yTabset', ['$log', '$q', 'systemEventService', function($log, $q, systemEventService) {
        return {
            restrict: 'E',
            transclude: false,
            templateUrl: 'web/common/services/tabset/yTabsetTemplate.html',
            scope: {
                model: '=',
                tabsList: '=',
                tabControl: '=',
                numTabsDisplayed: '@'
            },
            link: function(scope, elem, attr) {

                var prevValue = 0;

                scope.$watch(
                    function() {
                        return (prevValue !== scope.tabsList.length);
                    },
                    function() {
                        scope.initializeTabs();
                        prevValue = scope.tabsList.length;
                    });

                scope.initializeTabs = function() {
                    if (scope.tabsList && scope.tabsList.length > 0) {

                        scope.selectedTab = scope.tabsList[0];

                        for (var tabIdx in scope.tabsList) {
                            var tab = scope.tabsList[tabIdx];
                            tab.active = (tabIdx === '0');
                            tab.hasErrors = false;
                        }
                    }
                };

                scope.selectTab = function(tabToSelect) {
                    if (tabToSelect) {
                        scope.selectedTab.active = false;
                        scope.selectedTab = tabToSelect;
                        scope.selectedTab.active = true;
                    }
                };

                scope.dropDownHasErrors = function() {
                    var tabsInDropDown = scope.tabsList.slice(scope.numTabsDisplayed - 1);

                    return tabsInDropDown.some(function(tab) {
                        return tab.hasErrors;
                    });
                };

                scope.markTabsInError = function(tabsInErrorList) {
                    scope.resetTabErrors();

                    var tabId;
                    var tabFilter = function(tab) {
                        return (tab.id === tabId);
                    };

                    for (var idx in tabsInErrorList) {
                        tabId = tabsInErrorList[idx];
                        var resultTabs = scope.tabsList.filter(tabFilter);

                        if (resultTabs[0]) {
                            resultTabs[0].hasErrors = true;
                        }
                    }
                };

                scope.resetTabErrors = function() {
                    for (var tabKey in scope.tabsList) {
                        scope.tabsList[tabKey].hasErrors = false;
                    }
                };

                scope.initializeTabs();
            }
        };
    }])

    /**
     * @ngdoc directive
     * @name tabsetModule.directive:yTab
     * @scope
     * @restrict E
     * @element smartedit-tab
     *
     * @description
     * The directive  responsible for wrapping the content of a tab within a
     * {@link tabsetModule.directive:yTabset yTabset} directive.
     *
     * @param {Number} tabId The ID used to track the tab within the tabset. It must match the ID used in the tabset.
     * @param {String} content Path to the HTML fragment to be displayed as the tab content.
     * @param {Object} model Custom data. Neither the smartedit-tabset directive or the smartedit-tab directive
     * can modify this value. The tabs' contents determine how to parse and use this object.
     * @param {function} tabControl An optional parameter. A function that will be called with scope as its parameter.
     * It allows the caller to register extra functionality to be executed in the tab.
     *
     */
    .directive('yTab', ['$log', '$templateCache', '$compile', '$q', 'hitch', 'systemEventService', function($log, $templateCache, $compile, $q, hitch, systemEventService) {
        return {
            restrict: 'E',
            transclude: true,
            scope: {
                content: '=',
                model: '=',
                tabControl: '=',
                tabId: '@'
            },
            link: function(scope, elem, attr) {

                var template = $templateCache.get(scope.content);
                var compile = $compile(template);
                var compiled = compile(scope);
                elem.append(compiled);

                if (scope.tabControl) {
                    hitch(this, scope.tabControl(scope));
                }
            }
        };
    }]);
})();
