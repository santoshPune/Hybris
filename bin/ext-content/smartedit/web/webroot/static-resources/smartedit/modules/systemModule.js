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
    .controller('baseContextualMenuController', ['$scope', 'smarteditroot', function($scope, smarteditroot) {
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

    }])
    .controller('contextualMenuController', ['$scope', '$element', '$controller', '$timeout', 'smarteditroot', function($scope, $element, $controller, $timeout, smarteditroot) {
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
    }])
    .directive('contextualMenu', ['$timeout', 'contextualMenuService', 'componentHandlerService', 'smarteditroot', function($timeout, contextualMenuService, componentHandlerService, smarteditroot) {
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
    }])
    .directive('contextualMenuItem', ['componentHandlerService', function(componentHandlerService) {
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
    }]);
angular.module('sakExecutorDecorator').requires.push('contextualMenuDecoratorModule');

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
angular.module('toolbarModule', ['gatewayProxyModule', 'toolbarInterfaceModule'])

/**
 * @ngdoc service
 * @name toolbarModule.toolbarServiceFactory
 *
 * @description
 * The toolbar service factory generates instances of the {@link toolbarModule.ToolbarService ToolbarService} based on
 * the gateway ID (toolbar-name) provided. Only one ToolbarService instance exists for each gateway ID, that is, the
 * instance is a singleton with respect to the gateway ID.
 */
.factory('toolbarServiceFactory', ['$log', 'gatewayProxy', 'copy', 'extend', 'ToolbarServiceInterface', function($log, gatewayProxy, copy, extend, ToolbarServiceInterface) {

    /**
     * @ngdoc service
     * @name toolbarModule.ToolbarService
     * @constructor
     *
     * @description
     * The inner toolbar service is used to add toolbar actions that affect the inner application, publish aliases to
     * the outer application through the proxy, and store and manage callbacks made by private key. The service is a
     * managed singleton; the {@link toolbarModule.toolbarServiceFactory toolbarServiceFactory} provides a
     * getToolbarService function that returns a single instance of the ToolbarService for the gateway identifier,
     * that is, the toolbar-name provided by the outer toolbar.
     *
     * Uses {@link gatewayProxyModule.gatewayProxy gatewayProxy} for cross iframe communication, using the toolbar name
     * as the gateway ID.
     *
     * <b>Inherited Methods from {@link toolbarInterfaceModule.ToolbarServiceInterface
     * ToolbarServiceInterface}</b>: {@link toolbarInterfaceModule.ToolbarServiceInterface#methods_addItems
     * addItems}
     *
     * @param {String} gatewayId Toolbar name used by the gateway proxy service.
     */
    var ToolbarService = function(gatewayId) {
        this.gatewayId = gatewayId;
        this.actions = {};

        gatewayProxy.initForService(this, ["addAliases", 'removeItemByKey', 'removeAliasByKey', "_removeItemOnInner", "triggerActionOnInner"]);
    };

    ToolbarService = extend(ToolbarServiceInterface, ToolbarService);

    ToolbarService.prototype._removeItemOnInner = function(itemKey) {
        if (itemKey in this.actions) {
            delete this.actions[itemKey];
        }

        $log.warn("removeItemByKey() - Failed to find action for key " + itemKey);
    };

    ToolbarService.prototype.triggerActionOnInner = function(action) {
        if (!this.actions[action.key]) {
            $log.error('triggerActionByKey() - Failed to find action for key ' + action.key);
            return;
        }
        this.actions[action.key]();
    };

    /////////////////////////////////////
    // Factory and Management
    /////////////////////////////////////
    var toolbarServicesByGatewayId = {};

    /**
     * @ngdoc method
     * @name toolbarModule.toolbarServiceFactory#getToolbarService
     * @methodOf toolbarModule.toolbarServiceFactory
     *
     * @description
     * Returns a single instance of the ToolbarService for the given gateway identifier. If one does not exist, an
     * instance is created and cached.
     *
     * @param {string} gatewayId The toolbar name used for cross iframe communication (see {@link
     * gatewayProxyModule.gatewayProxy gatewayProxy})
     * @returns {ToolbarService} Corresponding ToolbarService instance for given gateway ID.
     */
    var getToolbarService = function(gatewayId) {
        if (!toolbarServicesByGatewayId[gatewayId]) {
            toolbarServicesByGatewayId[gatewayId] = new ToolbarService(gatewayId);
        }
        return toolbarServicesByGatewayId[gatewayId];
    };

    return {
        getToolbarService: getToolbarService
    };
}]);

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
angular.module('systemModule', [
    'contextualMenuDecoratorModule',
    'slotContextualMenuDecoratorModule',
    'decoratorServiceModule'
]);
