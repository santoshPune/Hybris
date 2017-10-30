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
angular.module('slotContextualMenuDecoratorModule', ['contextualMenuServiceModule', 'contextualMenuDecoratorModule',
        'eventServiceModule', 'ui.bootstrap', 'componentHandlerServiceModule'
    ])
    .controller('slotContextualMenuController', ['$controller', '$scope', 'smarteditroot', 'systemEventService', function($controller, $scope, smarteditroot, systemEventService) {
        angular.extend(this, $controller('baseContextualMenuController', {
            $scope: $scope
        }));

        this.moreButton = {
            key: 'slot.context.menu.title.more',
            i18nKey: 'slot.context.menu.title.more',
            iconIdle: smarteditroot + '/static-resources/images/slot_contextualmenu_placeholder_off.png',
            iconNonIdle: smarteditroot + '/static-resources/images/slot_contextualmenu_placeholder_on.png',
            iconOpen: smarteditroot + '/static-resources/images/slot_contextualmenu_placeholder_open.png',
            callback: function(configuration, $event) {
                this.remainOpenMap.moreButton = !this.remainOpenMap.moreButton;
                this.toggleDropdown($event);
            }.bind(this)
        };

        this.triggerItemCallback = function(item, $event) {
            item.callback({
                componentType: this.smarteditComponentType,
                componentId: this.smarteditComponentId
            }, $event);
        };

    }])
    .directive('slotContextualMenu', ['contextualMenuService', '$timeout', 'systemEventService', function(contextualMenuService, $timeout, systemEventService) {
        return {
            templateUrl: 'web/smartedit/modules/systemModule/features/slotContextualMenu/slotContextualMenuDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            controller: 'slotContextualMenuController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope, $element) {
                $scope.ctrl.maxContextualMenuItems = 3;

                $scope.ctrl.getItems = function() {
                    return contextualMenuService.getContextualMenuItems({
                        componentType: this.smarteditComponentType,
                        componentId: this.smarteditComponentId,
                        iLeftBtns: this.maxContextualMenuItems,
                        element: $element
                    });
                };

                $scope.ctrl.positionPanelVertically = function() {
                    var marginTop = $element.offset().top <= $element.find('.decorative-panel-area').height() ?
                        $element.find('.decorator-padding-container').height() : -42;
                    $element.find('.decorative-panel-area').css('margin-top', marginTop);
                };

                $scope.ctrl.positionPanelHorizontally = function() {
                    var leftMarginPx = $element.find('.decorative-panel-area').css('margin-left') || '0px';
                    var leftMargin = parseInt(leftMarginPx.replace('px', ''));
                    var rightMostOffsetFromElement = $element.find('.decorative-panel-area').width() + leftMargin;
                    var rightMostOffsetFromPage = $element.offset().left + rightMostOffsetFromElement;

                    var isOnLeft = rightMostOffsetFromPage >= $('body').width();
                    if (isOnLeft) {
                        var offset = $element.find('.decorative-panel-area').outerWidth() - $element.find('.yWrapperData').width();
                        $element.find('.decorative-panel-area').css('margin-left', -offset);
                        $element.find('.decorator-padding-left').css('margin-left', -offset);
                    }

                    var paddingSelector = isOnLeft ? '.decorator-padding-left' : '.decorator-padding-right';
                    $element.find(paddingSelector).css('display', 'flex');
                };


                $scope.ctrl.positionPanel = function() {
                    $scope.ctrl.positionPanelVertically();
                    $scope.ctrl.positionPanelHorizontally();
                };

                $scope.ctrl.hidePadding = function() {
                    $element.find('.decorator-padding-left').css('display', 'none');
                    $element.find('.decorator-padding-right').css('display', 'none');
                };

                $scope.$watch('ctrl.active', function(isActive) {
                    $timeout(function() {
                        $scope.ctrl.hidePadding();
                        if (!!isActive) {
                            $scope.ctrl.positionPanel();
                            systemEventService.sendEvent('SLOT_CONTEXTUAL_MENU_ACTIVE');
                        }
                    });
                });
            }
        };
    }])
    .directive('basicSlotContextualMenu', ['contextualMenuService', '$timeout', 'componentHandlerService', 'COMPONENT_CLASS', 'TYPE_ATTRIBUTE', 'ID_ATTRIBUTE', function(contextualMenuService, $timeout, componentHandlerService, COMPONENT_CLASS, TYPE_ATTRIBUTE, ID_ATTRIBUTE) {
        return {
            templateUrl: 'web/smartedit/modules/systemModule/features/slotContextualMenu/basicSlotContextualMenuDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            controller: 'slotContextualMenuController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },
            link: function($scope, $element) {
                $scope.ctrl.maxContextualMenuItems = (function() {
                    var ctxSize = 40;
                    var buttonMaxCapacity = Math.round($element.width() / ctxSize) - 1;
                    return buttonMaxCapacity >= 3 ? 2 : buttonMaxCapacity - 1;
                }());

                $scope.ctrl.getItems = function() {
                    return contextualMenuService.getContextualMenuItems({
                        componentType: this.smarteditComponentType,
                        componentId: this.smarteditComponentId,
                        iLeftBtns: this.maxContextualMenuItems,
                        element: $element
                    });
                };

                var slotSelector = "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot'][" + ID_ATTRIBUTE + "='" + $scope.ctrl.smarteditComponentId + "']";
                var originalSlot = componentHandlerService.getFromSelector(slotSelector);
                $scope.ctrl.isSlotNotEmpty = originalSlot.children().length >= 1;
            }
        };
    }]);
angular.module('sakExecutorDecorator').requires.push('slotContextualMenuDecoratorModule');
