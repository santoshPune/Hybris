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
angular.module('seMediaSelectorModule', ['seMediaAdvancedPropertiesModule', 'seMediaPreviewModule'])
    .controller('seMediaSelectorController', function() {
        this.onDelete = function($select, $event) {
            $event.preventDefault();
            $event.stopPropagation();
            $select.selected = undefined;
        };
    })
    .directive('seMediaSelector', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaSelector/seMediaSelectorTemplate.html',
            restrict: 'E',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                editor: '=',
                qualifier: '=',
                deleteIcon: '=',
                replaceIcon: '=',
                advInfoIcon: '='
            },
            controller: 'seMediaSelectorController',
            controllerAs: 'ctrl'
        };
    });
