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
angular.module('tabsetApp', ['ngMockE2E', 'ui.bootstrap', 'tabsetModule', 'loadConfigModule', 'translationServiceModule'])
    .controller('defaultController', function() {
        var vm = this;

        // Register tabs (This will be done somewhere else)
        vm.tabsList = [{
            id: 'tab1',
            title: 'tab1.name',
            templateUrl: 'web/features/commons/tabset/tab1Template.html',
            hasErrors: false
        }, {
            id: 'tab2',
            title: 'tab2.name',
            templateUrl: 'web/features/commons/tabset/tab2Template.html',
            hasErrors: false
        }, {
            id: 'tab3',
            title: 'tab3.name',
            templateUrl: 'web/features/commons/tabset/tab3Template.html',
            hasErrors: false
        }, {
            id: 'tab4',
            title: 'tab4.name',
            templateUrl: 'web/features/commons/tabset/tab4Template.html',
            hasErrors: false
        }];

        vm.tabsetData = {
            someData: "some data"
        };

        vm.addErrorToTab = function(tabIndex) {
            vm.tabsList[tabIndex].hasErrors = true;
        };

        vm.resetErrors = function() {
            for (var idx in vm.tabsList) {
                vm.tabsList[idx].hasErrors = false;
            }
        };
    })
    .directive('testTab', function($q) {
        return {
            restrict: 'E',
            transclude: false,
            scope: {
                innerSave: '=',
                innerReset: '='
            },
            link: function(scope, elem, attr) {}

        };
    })
    .directive('testTab2', function($q) {
        return {
            restrict: 'E',
            transclude: false,
            scope: {
                insideSave: '=',
                insideReset: '='
            },
            template: "<div>Tab 2 Content</div>",
            link: function(scope, elem, attr) {}
        };
    })
    .directive('testTab3', function($q) {
        return {
            restrict: 'E',
            transclude: false,
            scope: {
                insideSave: '=',
                insideReset: '='
            },
            template: "<div>Tab 3 Content</div>",
            link: function(scope, elem, attr) {}
        };
    })
    .run(['$templateCache', function($templateCache) {
        'use strict';

        $templateCache.put('web/features/commons/tabset/tab1Template.html',
            "<test-tab class='sm-tab-content' inner-save='onSave' inner-reset='onReset'>Tab 1 Content</test-tab>"
        );

        $templateCache.put('web/features/commons/tabset/tab2Template.html',
            "<div>" +
            "<test-tab2 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'></test-tab2>" +
            "</div>"
        );

        $templateCache.put('web/features/commons/tabset/tab3Template.html',
            "<div>" +
            "<test-tab3 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'></test-tab3>" +
            "</div>"
        );

        $templateCache.put('web/features/commons/tabset/tab4Template.html',
            "<div>" +
            "<test-tab1 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'>Tab 4 (Repeats 1)</test-tab1>" +
            "</div>"
        );
    }]);
