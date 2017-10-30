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
angular.module('tabsetApp', ['ngMockE2E', 'ui.bootstrap', 'editorTabsetModule', 'loadConfigModule', 'translationServiceModule'])
    .controller('defaultController', function(editorTabsetService, restServiceFactory, sharedDataService, $scope) {
        restServiceFactory.setDomain('thedomain');
        sharedDataService.set('experience', {
            siteDescriptor: {
                uid: 'someSiteUid'
            }
        });

        var vm = this;

        // Register tabs (This will be done somewhere else)
        editorTabsetService.registerTab('tab1', 'tab1.name', 'web/features/cmssmarteditContainer/editorTabset/tab1Template.html');
        editorTabsetService.registerTab('tab2', 'tab2.name', 'web/features/cmssmarteditContainer/editorTabset/tab2Template.html');
        editorTabsetService.registerTab('tab3', 'tab3.name', 'web/features/cmssmarteditContainer/editorTabset/tab3Template.html');

        vm.componentData = {
            componentId: 'theSmarteditComponentId',
            componentType: null
        };

        vm.setComponentType = function(type) {
            vm.componentData.componentType = type;
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
            link: function(scope, elem, attr) {
                scope.innerSave = function() {
                    return $q.reject("Save from Test Tab1 error");
                };

                scope.innerReset = function() {
                    return $q.reject("Reset from Test Tab1 error");
                };
            }

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
            link: function(scope, elem, attr) {
                scope.insideSave = function() {
                    return $q.when("Save from Test Tab2");
                };

                scope.insideReset = function() {
                    return $q.when("Reset from Test Tab2");
                };
            }
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
            link: function(scope, elem, attr) {
                scope.insideSave = function() {
                    return $q.reject("Save from Test Tab3 error");
                };

                scope.insideReset = function() {
                    return $q.reject("Reset from Test Tab3 error");
                };
            }
        };
    })
    .run(['$templateCache', function($templateCache) {
        'use strict';

        $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tab1Template.html',
            "<test-tab class='sm-tab-content' inner-save='onSave' inner-reset='onReset'>Tab 1 Content</test-tab>"
        );

        $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tab2Template.html',
            "<div>" +
            "<test-tab2 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'></test-tab2>" +
            "</div>"
        );

        $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tab3Template.html',
            "<div>" +
            "<test-tab3 class='sm-tab-content' inside-save='onSave' inside-reset='onReset'></test-tab3>" +
            "</div>"
        );
    }]);
