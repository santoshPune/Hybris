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
angular.module('seErrorsListModule', [])
    .controller('seErrorsListController', function() {
        this.getSubjectErrors = function() {
            return (this.subject ? this.errors.filter(function(error) {
                return error.subject === this.subject;
            }.bind(this)) : this.errors);
        };
    })
    .directive('seErrorsList', function() {
        return {
            templateUrl: 'web/common/services/genericEditor/media/components/errorsList/seErrorsListTemplate.html',
            restrict: 'E',
            scope: {},
            controller: 'seErrorsListController',
            controllerAs: 'ctrl',
            bindToController: {
                errors: '=',
                subject: '='
            }
        };
    });
