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
angular.module('CMSApp', [
        'buttonDisplayDecorator'
    ])
    .run(function(decoratorService) {

        decoratorService.addMappings({
            'innerContentType': ['buttonDisplay']
        });

        decoratorService.enable('buttonDisplay');

    });

angular.module('buttonDisplayDecorator', ['decoratortemplates', 'urlServiceModule'])
    .directive('buttonDisplay', function(urlService) {
        return {
            templateUrl: 'web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
            },

            link: function($scope) {
                $scope.navigate = function() {
                    urlService.path('/customView');
                };
            }
        };
    });

angular.module('decoratortemplates', []).run(['$templateCache', function($templateCache) {
    'use strict';

    $templateCache.put('web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
        "<div>\n" +
        "</div>\n" +
        "<button id='navigateButtonInner' ng-click='navigate()'>Navigate to Custom View</button>\n" +
        "<div data-ng-transclude></div>\n" +
        "</div>"
    );
}]);
