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
angular.module('perspectiveServiceApp', [
        'textDisplayDecorator',
        'buttonDisplayDecorator',
        'perspectiveServiceModule',
        'featureServiceModule'
    ])
    .run(function(decoratorService, perspectiveService, featureService, $log) {
        $log.warn("Loading this");

        decoratorService.addMappings({
            'componentType1': ['textDisplay'],
            'componentType2': ['buttonDisplay'],
            'componentType3': ['textDisplay', 'buttonDisplay']
        });

        featureService.addDecorator({
            key: 'textDisplay',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey'
        });
        featureService.addDecorator({
            key: 'buttonDisplay',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey'
        });

        perspectiveService.register({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            features: ['textDisplay', 'buttonDisplay'],
            perspectives: []
        });

    });

angular.module('textDisplayDecorator', ['decoratortemplates', 'translationServiceModule'])
    .directive('textDisplay', function() {
        return {
            templateUrl: 'web/features/textDisplay/textDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope) {
                $scope.textDisplayContent = "Text_is_been_displayed_TextDisplayDecorator";
            }
        };
    });


angular.module('buttonDisplayDecorator', ['decoratortemplates', 'translationServiceModule'])
    .directive('buttonDisplay', function() {
        return {
            templateUrl: 'web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope) {
                $scope.buttonDisplayContent = "Button_is_been_Displayed";
            }
        };
    });

angular.module('decoratortemplates', []).run(['$templateCache', function($templateCache) {
    'use strict';

    $templateCache.put('web/features/textDisplay/textDisplayDecoratorTemplate.html',
        "<div >\n" +
        "<div class=\"row\" data-ng-if=\"!active\">\n" +
        "</div>\n" +
        "{{textDisplayContent}}\n" +
        "<div data-ng-transclude></div>\n" +
        "</div>"
    );

    $templateCache.put('web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
        "<div>\n" +
        "<div class=\"row\" data-ng-if=\"!active\">\n" +
        "</div>\n" +
        "<button>{{buttonDisplayContent}}</button>\n" +
        "<div data-ng-transclude></div>\n" +
        "</div>"
    );
}]);
