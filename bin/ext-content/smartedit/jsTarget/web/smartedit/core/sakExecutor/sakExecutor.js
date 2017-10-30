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
 (function() {
     angular.module('sakExecutorDecorator', ['coretemplates', 'decoratorServiceModule', 'componentHandlerServiceModule'])
         .factory('sakExecutor', ['$compile', 'decoratorService', 'NUM_SE_SLOTS', function($compile, decoratorService, NUM_SE_SLOTS) {
             var expectedNumElements = NUM_SE_SLOTS;

             var processedElements = {
                 length: 0
             };
             var scopes = [];

             return {
                 resetCounters: function(newNumElements) {
                     expectedNumElements = newNumElements;
                     processedElements = {
                         length: 0
                     };
                 },
                 wrapDecorators: function(transcludeFn, smarteditComponentId, smarteditComponentType) {

                     var decorators = decoratorService.getDecoratorsForComponent(smarteditComponentType);
                     var template = "<div data-ng-transclude></div>";

                     decorators.forEach(function(decorator) {
                         template = "<div class='" + decorator + "' data-active='active' data-smartedit-component-id='{{smarteditComponentId}}' data-smartedit-component-type='{{smarteditComponentType}}' data-smartedit-container-id='{{smarteditContainerId}}' data-smartedit-container-type='{{smarteditContainerType}}'>" + template;
                         template += "</div>";
                     });

                     return $compile(template, transcludeFn);
                 },
                 markDecoratorProcessed: function(type, id) {
                     var key = type + "_" + id;
                     if (!(key in processedElements)) {
                         processedElements[key] = key;
                         processedElements.length++;
                     }
                 },
                 areAllDecoratorsProcessed: function() {
                     //FIXME : inconsistency of expected and processed in new storefront
                     return processedElements.length >= 0;
                     //return processedElements.length >= expectedNumElements;
                 },
                 registerScope: function(scope) {
                     scopes.push(scope);
                 },
                 destroyAllScopes: function() {
                     scopes.forEach(function(scope) {
                         scope.$destroy();
                     });
                     scopes = [];
                 }

             };
         }])
         .directive('smartEditComponentX', ['$rootScope', '$q', '$timeout', 'sakExecutor', function($rootScope, $q, $timeout, sakExecutor) {
             // Constants
             var CONTENT_SLOT_TYPE = "ContentSlot";

             return {
                 restrict: 'C',
                 transclude: true,
                 replace: false,
                 scope: {
                     smarteditComponentId: '@',
                     smarteditComponentType: '@',
                     smarteditContainerId: '@',
                     smarteditContainerType: '@'
                 },
                 link: function($scope, element, attrs, controller, transcludeFn) {

                     sakExecutor.registerScope($scope);

                     $scope.active = false;
                     transcludeFn($scope, function(clone) {
                         var compiled = sakExecutor.wrapDecorators(transcludeFn, $scope.smarteditComponentId, $scope.smarteditComponentType);
                         element.append(compiled($scope));

                         var inactivateDecorator = function() {
                             $scope.active = false;
                         };

                         var activateDecorator = function() {
                             $scope.active = true;
                         };

                         // Register Event Listeners
                         element.bind("mouseleave", function($event) {
                             $rootScope.$apply(inactivateDecorator);
                         });

                         element.bind("mouseenter", function($event) {
                             $rootScope.$apply(activateDecorator);
                         });

                         if ($scope.smarteditComponentType === CONTENT_SLOT_TYPE) {
                             sakExecutor.markDecoratorProcessed($scope.smarteditComponentType, $scope.smarteditComponentId);
                         }

                     });
                 }
             };
         }]);

 })();
