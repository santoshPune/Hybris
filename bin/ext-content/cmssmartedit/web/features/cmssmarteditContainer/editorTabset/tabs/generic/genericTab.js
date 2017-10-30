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
    angular.module('genericTabModule', ['genericEditorModule', 'resourceLocationsModule'])
        .directive('genericTab', function($log, $q, TYPES_RESOURCE_URI, ITEMS_RESOURCE_URI) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/generic/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    onStructureResolved: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {
                    scope.structureApi = TYPES_RESOURCE_URI + '/:smarteditComponentType';
                    scope.contentApi = ITEMS_RESOURCE_URI;
                }
            };
        });
})();
