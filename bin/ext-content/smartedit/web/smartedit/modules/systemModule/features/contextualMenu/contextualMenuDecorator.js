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
angular.module('contextualMenuDecoratorModule', ['componentHandlerServiceModule', 'contextualMenuServiceModule', 'ui.bootstrap'])
    .controller('baseContextualMenuController', function($scope, smarteditroot) {
        this.status = {
            isopen: false
        };

        this.isHybrisIcon = function(icon) {
            return icon && icon.indexOf("hyicon") >= 0;
        };

        this.remainOpenMap = {};

        /*
            setRemainOpen receives a button name and a boolean value
            the button name needs to be unique across all buttons so it won' t collide with other button actions.
        */
        this.setRemainOpen = function(button, remainOpen) {
            this.remainOpenMap[button] = remainOpen;
        };

        this.showOverlay = function() {
            var remainOpen = Object.keys(this.remainOpenMap)
                .reduce(function(isOpen, button) {
                    return isOpen || this.remainOpenMap[button];
                }.bind(this), false);

            return this.active || remainOpen;
        };

        this.toggleDropdown = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            this.status.isopen = !this.status.isopen;

            var moreMenuIconImg = $($event.target).find('img');

            if (this.status.isopen) {
                moreMenuIconImg.attr('src', this.moreButton.iconOpen);
                moreMenuIconImg.addClass('bottom-shadow');
            } else {
                moreMenuIconImg.attr('src', this.moreButton.iconIdle);
                moreMenuIconImg.addClass('bottom-shadow');
            }
        };

        this.moreButton = {
            key: 'context.menu.title.more',
            displayClass: 'hyicon hyicon-remove',
            i18nKey: 'context.menu.title.more',
            iconIdle: smarteditroot + '/static-resources/images/contextualmenu_more_off.png',
            iconNonIdle: smarteditroot + '/static-resources/images/contextualmenu_more_on.png',
            iconOpen: smarteditroot + '/static-resources/images/more_white.png',
            condition: function() {
                return true;
            },

            callback: function(configuration, $event) {
                this.remainOpenMap.moreButton = !this.remainOpenMap.moreButton;
                this.toggleDropdown($event);
                $('.dropdown-toggle').dropdown();
                this.icon = smarteditroot + '/static-resources/images/more_white.png';
            }.bind(this)
        };

        this.triggerItemCallback = function(item, $event) {
            item.callback({
                componentType: this.smarteditComponentType,
                componentId: this.smarteditComponentId,
                containerType: this.smarteditContainerType,
                containerId: this.smarteditContainerId,
                slotId: this.smarteditSlotId
            }, $event);
        };

    })
    .controller('contextualMenuController', function($scope, $element, $controller, $timeout, smarteditroot) {
        angular.extend(this, $controller('baseContextualMenuController', {
            $scope: $scope
        }));

        this.positionMoreMenu = function() {
            $timeout(function() {
                var moreButtonElement = $element.find('.cmsx-ctx-more');
                var moreMenuElement = $('#' + this.smarteditComponentId + '-' + this.smarteditComponentType + '-more-menu');

                var moreButtonWidth = moreButtonElement.width();
                var moreMenuWidth = moreMenuElement.width();

                var offset = moreMenuWidth - moreButtonWidth;
                moreMenuElement.offset({
                    top: moreMenuElement.offset().top,
                    left: moreMenuElement.offset().left - offset
                });
            }.bind(this));
        };

        this.toggleDropdown = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            this.remainOpenMap.moreButton = !this.remainOpenMap.moreButton;

            var moreMenuIconImg = $($event.target).find('img');

            if (this.status.isopen) {
                moreMenuIconImg.attr('src', this.moreButton.iconOpen);
                moreMenuIconImg.addClass('bottom-shadow');
            } else {
                moreMenuIconImg.attr('src', this.moreButton.iconIdle);
                moreMenuIconImg.addClass('bottom-shadow');
            }

            this.positionMoreMenu();
        };
    })
    .directive('contextualMenu', function($timeout, contextualMenuService, componentHandlerService, smarteditroot) {
        return {
            templateUrl: 'web/smartedit/modules/systemModule/features/contextualMenu/contextualMenuDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            controller: 'contextualMenuController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                smarteditContainerId: '@',
                smarteditContainerType: '@',
                active: '='
            },
            link: function($scope, $element) {
                $scope.ctrl.smarteditSlotId = componentHandlerService.getParentSlotForComponent($element);
                $scope.ctrl.maxContextualMenuItems = (function() {
                    var ctxSize = 50;
                    var buttonMaxCapacity = Math.round($element.width() / ctxSize) - 1;
                    var leftButtons = buttonMaxCapacity >= 4 ? 3 : buttonMaxCapacity - 1;
                    leftButtons = (leftButtons < 0 ? 0 : leftButtons);
                    return leftButtons;
                }());

                $scope.ctrl.getItems = function() {
                    return contextualMenuService.getContextualMenuItems({
                        componentType: this.smarteditComponentType,
                        componentId: this.smarteditComponentId,
                        containerType: this.smarteditContainerType,
                        containerId: this.smarteditContainerId,
                        iLeftBtns: this.maxContextualMenuItems,
                        element: $element
                    });
                };
            }
        };
    })
    .directive('contextualMenuItem', function(componentHandlerService) {
        return {
            restrict: 'A',
            scope: {
                item: '=',
                componentType: '@',
                componentId: '@',
                containerType: '@',
                containerId: '@',
            },
            link: function($scope, $element) {
                $element.on('load', function() {
                    if ($scope.item.callbacks) {
                        angular.forEach($scope.item.callbacks, function(value, key) {
                            $scope.smarteditSlotId = componentHandlerService.getParentSlotForComponent($element);
                            $element.on(key, value.bind(undefined, {
                                componentType: $scope.componentType,
                                componentId: $scope.componentId,
                                containerType: $scope.containerType,
                                containerId: $scope.containerId,
                                slotId: $scope.smarteditSlotId
                            }));
                        });
                    }

                    $element.unbind("load");
                });
            }
        };
    });
angular.module('sakExecutorDecorator').requires.push('contextualMenuDecoratorModule');
