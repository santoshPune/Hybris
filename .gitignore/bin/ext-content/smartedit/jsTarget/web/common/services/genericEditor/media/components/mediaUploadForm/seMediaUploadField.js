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
angular.module('seMediaUploadFieldModule', [])
    .controller('seMediaUploadFieldController', function() {
        this.displayImage = false;
    })
    .directive('seMediaUploadField', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/mediaUploadForm/seMediaUploadFieldTemplate.html',
            restrict: 'E',
            scope: {},
            bindToController: {
                field: '=',
                model: '=',
                error: '='
            },
            controller: 'seMediaUploadFieldController',
            controllerAs: 'ctrl',
            link: function(scope, element, attrs, ctrl) {
                element.bind("mouseover", function(e) {
                    ctrl.displayImage = true;
                    scope.$digest();
                });
                element.bind("mouseout", function(e) {
                    ctrl.displayImage = false;
                    scope.$digest();
                });
            }
        };
    });
