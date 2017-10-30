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
angular.module('sampleDecorator', ['ysmarteditmoduleTemplates'])
    .directive('sample', function() {
        return {
            templateUrl: 'web/features/ysmarteditmodule/sample/sampleDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {

                $scope.abTitleKey = 'sample.popover.title';
                $scope.abContent = "A : 30%, B : 70%";
            }
        };
    });
