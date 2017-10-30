angular.module('personalizationsmarteditTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/personalizationsmartedit/componentLightUpDecorator/personalizationsmarteditComponentLightUpDecoratorTemplate.html',
    "<div ng-class=\"getPersonalizationComponentBorderClass()\">\n" +
    "    <div data-ng-transclude></div>\n" +
    "</div>"
  );

}]);
