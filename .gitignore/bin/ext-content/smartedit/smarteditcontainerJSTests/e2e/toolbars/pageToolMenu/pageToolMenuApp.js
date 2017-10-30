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
angular.module('pageToolMenuApp', ['toolbarModule', 'alertServiceModule'])
    .directive('firstDummyPage', function(alertService, $rootScope) {
        return {
            template: '<div id="dummyItem1">Hello 1</div>',
            restrict: 'E',
            transclude: true,
            scope: {
                selectedItemCallbacks: "="
            },
            link: function(scope, elem, attrs) {
                scope.selectedItemCallbacks.onClose = function() {
                    alertService.pushAlerts([{
                        successful: true,
                        message: "Dummy page 1 onClose Callback",
                        closeable: false
                    }]);
                };
            }
        };
    })
    .directive('secondDummyPage', function(alertService, $rootScope) {
        return {
            template: '<div id="dummyItem2">Hello 2</div>',
            restrict: 'E',
            transclude: true,
            scope: {
                selectedItemCallbacks: "="
            },
            link: function(scope, elem, attrs) {
                scope.selectedItemCallbacks.onClose = function() {
                    alertService.pushAlerts([{
                        successful: true,
                        message: "Dummy page 2 onClose Callback",
                        closeable: false
                    }]);
                };
            }
        };
    })
    .run(function(toolbarServiceFactory, $templateCache) {
        $templateCache.put('path/to/dummyItem1.html', '<first-dummy-page selected-item-callbacks="selectedItemCallbacks"/>');
        $templateCache.put('path/to/dummyItem2.html', '<second-dummy-page selected-item-callbacks="selectedItemCallbacks"/>');

        var toolbarService = toolbarServiceFactory.getToolbarService('pageToolMenu');
        toolbarService.addItems([{
            key: 'page.tool.menu.dummy.item.1',
            i18nKey: 'page.tool.menu.dummy.item.1',
            type: 'HYBRID_ACTION',
            icons: {
                'default': 'static-resources/images/icon_small_info.png'
            },
            include: 'path/to/dummyItem1.html'
        }, {
            key: 'page.tool.menu.dummy.item.2',
            i18nKey: 'page.tool.menu.dummy.item.2',
            type: 'HYBRID_ACTION',
            icons: {
                'default': 'static-resources/images/icon_small_info.png'
            },
            include: 'path/to/dummyItem2.html'
        }]);
    });
