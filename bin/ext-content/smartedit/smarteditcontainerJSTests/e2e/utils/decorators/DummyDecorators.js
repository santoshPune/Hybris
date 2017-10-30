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
/**
 * @ngdoc overview
 * @name DecoratorsModule
 * @description
 * Wires dummy decorators to dummy components provided in the dummy storefront.
 */
angular.module('DummyDecoratorsModule', ['textDisplayDecorator', 'slotTextDisplayDecorator', 'buttonDisplayDecorator', 'slotButtonDisplayDecorator'])
    .run(function(decoratorService) {
        decoratorService.addMappings({
            'componentType1': ['textDisplay'],
            'componentType2': ['buttonDisplay'],
            'componentType3': ['textDisplay', 'buttonDisplay'],
            'componentType4': ['textDisplay', 'buttonDisplay'],
            'ContentSlot': ['slotTextDisplay', 'slotButtonDisplay']
        });

        decoratorService.enable('textDisplay');
        decoratorService.enable('buttonDisplay');
        decoratorService.enable('slotTextDisplay');
        decoratorService.enable('slotButtonDisplay');
    });

/**
 * @ngdoc overview
 * @name textDisplayDecorator
 * @description
 * Provides a simple text display decorator for components.
 */
angular.module('textDisplayDecorator', ['DummyDecoratorTemplatesModule', 'translationServiceModule'])
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

/**
 * @ngdoc overview
 * @name slotTextDisplayDecorator
 * @description
 * Provides a simple text display decorator for slots.
 */
angular.module('slotTextDisplayDecorator', ['DummyDecoratorTemplatesModule', 'translationServiceModule'])
    .directive('slotTextDisplay', function() {
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
                $scope.textDisplayContent = "slot_text_is_been_displayed_SlotTextDisplayDecorator";
            }
        };
    });

/**
 * @ngdoc overview
 * @name buttonDisplayDecorator
 * @description
 * Provides a simple button display decorator for components.
 */
angular.module('buttonDisplayDecorator', ['DummyDecoratorTemplatesModule', 'translationServiceModule'])
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

/**
 * @ngdoc overview
 * @name slotButtonDisplayDecorator
 * @description
 * Provides a simple button display decorator for slots.
 */
angular.module('slotButtonDisplayDecorator', ['DummyDecoratorTemplatesModule', 'translationServiceModule'])
    .directive('slotButtonDisplay', function() {
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
                $scope.buttonDisplayContent = "Slot_button_is_been_Displayed";
            }
        };
    });

/**
 * @ngdoc overview
 * @name DummyDecoratorTemplatesModule
 * @description
 * Provides templates for the dummy decorators.
 */
angular.module('DummyDecoratorTemplatesModule', [])
    .run(['$templateCache', function($templateCache) {
        $templateCache.put('web/features/textDisplay/textDisplayDecoratorTemplate.html',
            "<div >\n" +
            "<div data-ng-transclude></div>\n" +
            "{{textDisplayContent}}\n" +
            "</div>"
        );

        $templateCache.put('web/features/buttonDisplay/buttonDisplayDecoratorTemplate.html',
            "<div>\n" +
            "<div data-ng-transclude></div>\n" +
            "<button>{{buttonDisplayContent}}</button>\n" +
            "</div>"
        );
    }]);
