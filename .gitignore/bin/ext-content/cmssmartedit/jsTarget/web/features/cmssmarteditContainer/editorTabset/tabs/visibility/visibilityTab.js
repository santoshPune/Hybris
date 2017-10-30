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
    angular.module('visibilityTabModule', ['genericEditorModule', 'resourceLocationsModule'])
        .directive('visibilityTab', ['$log', '$q', 'ITEMS_RESOURCE_URI', function($log, $q, ITEMS_RESOURCE_URI) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {
                    scope.tabStructure = [{
                        cmsStructureType: "Boolean",
                        qualifier: "visible",
                        prefixText: 'visible.prefix.text',
                        labelText: 'visible.postfix.text'
                    }];
                    scope.contentApi = ITEMS_RESOURCE_URI;
                }
            };
        }]);
})();
