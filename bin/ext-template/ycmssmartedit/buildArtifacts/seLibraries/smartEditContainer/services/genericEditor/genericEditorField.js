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
angular.module('genericEditorFieldModule', ['translationServiceModule', 'ui.bootstrap', 'ui.select', 'ngSanitize', 'seGenericEditorFieldErrorsModule'])
    .directive('genericEditorField', function() {

        return {
            templateUrl: 'web/common/services/genericEditor/genericEditorFieldTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: false,
            scope: {
                editor: '=',
                field: '=',
                model: '=',
                qualifier: '='
            },

        };
    });
