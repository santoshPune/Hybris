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
angular.module('seGenericEditorFieldErrorsModule', [])
    .controller('seGenericEditorFieldErrorsController', function() {
        this.getFilteredErrors = function() {
            return (this.field.errors || []).filter(function(error) {
                return error.language === this.qualifier && !error.format;
            }.bind(this)).map(function(error) {
                return error.message;
            });
        };
    })
    .directive('seGenericEditorFieldErrors', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/errors/seGenericEditorFieldErrorsTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seGenericEditorFieldErrorsController',
            controllerAs: 'ctrl',
            bindToController: {
                field: '=',
                qualifier: '='
            }
        };
    });
