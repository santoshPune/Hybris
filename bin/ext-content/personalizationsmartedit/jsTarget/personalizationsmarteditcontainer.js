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
 *
 *
 */
angular.module('personalizationsmarteditCommons', [
        'alertServiceModule', 'commonsTemplates'
    ])
    .factory('personalizationsmarteditUtils', function() {
        var utils = {};

        utils.pushToArrayIfValueExists = function(array, key, value) {
            if (value) {
                array.push({
                    "key": key,
                    "value": value
                });
            }
        };

        utils.getContainerIdForElement = function(element) {
            var container = element.closest('[data-smartedit-container-id][data-smartedit-container-type="CxCmsComponentContainer"]');
            if (container.length) {
                return container.data().smarteditContainerId;
            }
            return null;
        };

        utils.getContainerIdForComponent = function(componentType, componentId) {
            var element = angular.element('[data-smartedit-component-id="' + componentId + '"][data-smartedit-component-type="' + componentType + '"]');
            if (angular.isArray(element)) {
                element = element[0];
            }
            return utils.getContainerIdForElement(element);
        };

        utils.getSlotIdForElement = function(element) {
            var slot = element.closest('[data-smartedit-component-type="ContentSlot"]');
            if (slot.length) {
                return slot.data().smarteditComponentId;
            }
            return null;
        };

        utils.getSlotIdForComponent = function(componentType, componentId) {
            var element = angular.element('[data-smartedit-component-id="' + componentId + '"][data-smartedit-component-type="' + componentType + '"]');
            if (angular.isArray(element)) {
                element = element[0];
            }
            return utils.getSlotIdForElement(element);
        };

        utils.getVariationCodes = function(variations) {
            if ((typeof variations === 'undefined') || (variations === null)) {
                return [];
            }
            var allVariationsCodes = variations.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            });
            return allVariationsCodes;
        };

        utils.getPageId = function() {
            return /page\-([\w]+)/.exec($('iframe').contents().find('body').attr('class'))[1];
        };

        utils.getVariationKey = function(customizationId, variations) {
            if (customizationId === undefined || variations === undefined) {
                return [];
            }

            var allVariationsKeys = variations.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            }).map(function(variationId) {
                return {
                    "variationCode": variationId,
                    "customizationCode": customizationId
                };
            });
            return allVariationsKeys;
        };

        utils.getSegmentTriggerForVariation = function(variation) {
            var triggers = variation.triggers || [];
            var segmentTriggerArr = triggers.filter(function(trigger) {
                return trigger.type === "segmentTriggerData";
            });

            if (segmentTriggerArr.length === 0) {
                return {};
            }

            return segmentTriggerArr[0];
        };

        utils.getActionIdForElement = function(element) {
            var action = element.closest('[data-smartedit-personalization-action-id]');
            if (action.length) {
                return action.data().smarteditPersonalizationActionId;
            }
            return null;
        };

        return utils;
    })
    .factory('personalizationsmarteditMessageHandler', ['alertService', function(alertService) {
        var sendMessage = function(message, isSuccessful) {
            alertService.pushAlerts([{
                successful: isSuccessful,
                message: message
            }]);
        };

        var messageHandler = {};
        messageHandler.sendInformation = function(informationMessage) {
            sendMessage(informationMessage, true);
        };

        messageHandler.sendError = function(errorMessage) {
            sendMessage(errorMessage, false);
        };

        return messageHandler;
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
 *
 *
 */
angular.module('personalizationsmarteditCommons')
    .directive('personalizationInfiniteScroll', ['$rootScope', '$window', '$timeout', function($rootScope, $window, $timeout) {
        return {
            link: function(scope, elem, attrs) {
                var checkWhenEnabled, handler, scrollDistance, scrollEnabled;
                $window = angular.element($window);
                elem.css('overflow-y', 'auto');
                elem.css('overflow-x', 'hidden');
                elem.css('height', 'inherit');
                scrollDistance = 0;
                if (attrs.personalizationInfiniteScrollDistance !== null) {
                    scope.$watch(attrs.personalizationInfiniteScrollDistance, function(value) {
                        return (scrollDistance = parseInt(value, 10));
                    });
                }
                scrollEnabled = true;
                checkWhenEnabled = false;
                if (attrs.personalizationInfiniteScrollDisabled !== null) {
                    scope.$watch(attrs.personalizationInfiniteScrollDisabled, function(value) {
                        scrollEnabled = !value;
                        if (scrollEnabled && checkWhenEnabled) {
                            checkWhenEnabled = false;
                            return handler();
                        }
                    });
                }
                $rootScope.$on('refreshStart', function() {
                    elem.animate({
                        scrollTop: "0"
                    });
                });
                handler = function() {
                    var container, elementBottom, remaining, shouldScroll, containerBottom;
                    container = $(elem.children()[0]);
                    elementBottom = elem.offset().top + elem.height();
                    containerBottom = container.offset().top + container.height();
                    remaining = containerBottom - elementBottom;
                    shouldScroll = remaining <= elem.height() * scrollDistance;
                    if (shouldScroll && scrollEnabled) {
                        if ($rootScope.$$phase) {
                            return scope.$eval(attrs.personalizationInfiniteScroll);
                        } else {
                            return scope.$apply(attrs.personalizationInfiniteScroll);
                        }
                    } else if (shouldScroll) {
                        return (checkWhenEnabled = true);
                    }
                };
                elem.on('scroll', handler);
                scope.$on('$destroy', function() {
                    return $window.off('scroll', handler);
                });
                return $timeout((function() {
                    if (attrs.personalizationInfiniteScrollImmediateCheck) {
                        if (scope.$eval(attrs.personalizationInfiniteScrollImmediateCheck)) {
                            return handler();
                        }
                    } else {
                        return handler();
                    }
                }), 0);
            }
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
 *
 *
 */
angular.module('personalizationsmarteditCommons')
    .directive('personalizationsmarteditPagination', function() {
        return {
            template: '<div ng-include="getContentUrl()"></div>',
            restrict: 'E',
            scope: {
                callback: "=",
                pages: "=",
                currentPage: "=",
                pageSizes: "=",
                currentSize: "=",
                pagesOffset: "=",
                fixedPageSize: "="
            },

            link: function($scope, element, attrs) {

                if (!$scope.callback) {
                    console.log("callback is undefined!");
                }

                $scope.pages = $scope.pages || [0, 1, 2];
                $scope.currentPage = $scope.currentPage || 0;
                $scope.pageSizes = $scope.pageSizes || [5, 10, 25, 50, 100];
                $scope.currentSize = $scope.currentSize || 10;
                $scope.pagesOffset = $scope.pagesOffset || 2;
                $scope.fixedPageSize = $scope.fixedPageSize || false; //NOSONAR

                $scope.getContentUrl = function() {
                    attrs.template = attrs.template || 'personalizationsmarteditPaginationTemplate.html';
                    return 'web/features/commons/personalizationsmarteditPagination/' + attrs.template;
                };

                $scope.pageClick = function(newValue) {
                    if ($scope.currentPage !== newValue) {
                        $scope.currentPage = newValue;
                        $scope.callback($scope);
                    }
                };

                $scope.pageSizeClick = function(newValue) {
                    if ($scope.currentSize !== newValue) {
                        $scope.currentSize = newValue;
                        $scope.currentPage = 0;
                        $scope.callback($scope);
                    }
                };

                $scope.hasPrevious = function() {
                    return $scope.currentPage > 0;
                };

                $scope.hasNext = function() {
                    return $scope.currentPage < $scope.pages.length - 1;
                };

                $scope.isActive = function(value) {
                    return $scope.currentPage === value;
                };

                $scope.nextClick = function() {
                    if ($scope.hasNext()) {
                        $scope.currentPage++;
                        $scope.callback($scope);
                    }
                };

                $scope.prevClick = function() {
                    if ($scope.hasPrevious()) {
                        $scope.currentPage--;
                        $scope.callback($scope);
                    }
                };

                $scope.pagesToDisplay = function() {
                    var numberOfPages = 2 * $scope.pagesOffset + 1;
                    if ($scope.pages.length <= numberOfPages) {
                        return $scope.pages;
                    } else {
                        var start = Math.max($scope.currentPage - $scope.pagesOffset, 0);
                        if (start + numberOfPages > $scope.pages.length) {
                            start = $scope.pages.length - numberOfPages;
                        }
                        return $scope.pages.slice(start, start + numberOfPages);
                    }
                };

                $scope.availablePageSizes = function() {
                    return $scope.pageSizes;
                };

                $scope.getCurrentPageSize = function() {
                    return $scope.currentSize;
                };

                $scope.isFixedPageSize = function() {
                    return $scope.fixedPageSize;
                };

            }
        };
    });

angular.module('commonsTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/commons/personalizationsmarteditPagination/personalizationsmarteditPaginationLefttoolbarTemplate.html',
    "<div class=\"row\">\n" +
    "\n" +
    "    <div class=\"col-xs-12\">\n" +
    "        <ul class=\"pagination\">\n" +
    "            <li ng-click=\"prevClick()\" ng-class=\"hasPrevious()?'enabled':'disabled'\"><a>&laquo;</a></li>\n" +
    "            <li ng-repeat=\"i in pagesToDisplay()\"><a ng-click=\"pageClick(i)\" ng-class=\"isActive({{i}})?'active':''\">{{i+1}}</a></li>\n" +
    "            <li ng-click=\"nextClick()\" ng-class=\"hasNext()?'enabled':'disabled'\"><a>&raquo;</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/commons/personalizationsmarteditPagination/personalizationsmarteditPaginationTemplate.html',
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-4\"></div>\n" +
    "    <div class=\"col-xs-4\">\n" +
    "        <ul class=\"pagination pagination-lg\">\n" +
    "            <li ng-click=\"prevClick()\" ng-class=\"hasPrevious()?'enabled':'disabled'\"><a>&laquo;</a></li>\n" +
    "            <li ng-repeat=\"i in pagesToDisplay()\"><a ng-click=\"pageClick(i)\" ng-class=\"isActive({{i}})?'active':''\">{{i+1}}</a></li>\n" +
    "            <li ng-click=\"nextClick()\" ng-class=\"hasNext()?'enabled':'disabled'\"><a>&raquo;</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "    <div class=\"col-xs-2\"></div>\n" +
    "    <div class=\"col-xs-2\" ng-if=\"!isFixedPageSize()\">\n" +
    "        <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "            <span ng-bind=\"getCurrentPageSize()\"></span>\n" +
    "            <span data-translate=\"personalization.pagination.rowsperpage\"></span>\n" +
    "            <span class=\"hyicon hyicon-arrow\"></span>\n" +
    "        </button>\n" +
    "        <ul class=\"dropdown-menu pull-right text-left\" role=\"menu\">\n" +
    "            <li ng-repeat=\"i in availablePageSizes() track by $index\"><a ng-click=\"pageSizeClick(i)\">{{i}}</a></li>\n" +
    "        </ul>\n" +
    "    </div>\n" +
    "</div>"
  );

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
 *
 *
 */
angular.module('personalizationsmarteditContextMenu', [
        'modalServiceModule',
        'personalizationsmarteditRestServiceModule',
        'personalizationsmarteditCommons',
        'ui.select',
        'genericEditorModule',
        'editorModalServiceModule',
        'gatewayProxyModule',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditcontainermodule'
    ])
    .factory('personalizationsmarteditContextModal', ['$controller', 'modalService', 'MODAL_BUTTON_ACTIONS', 'MODAL_BUTTON_STYLES', '$filter', 'personalizationsmarteditMessageHandler', 'gatewayProxy', 'personalizationsmarteditContextService', 'personalizationsmarteditIFrameUtils', function($controller, modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES, $filter, personalizationsmarteditMessageHandler, gatewayProxy, personalizationsmarteditContextService, personalizationsmarteditIFrameUtils) {

        var PersonalizationsmarteditContextModal = function() {
            this.gatewayId = "personalizationsmarteditContextModal";
            gatewayProxy.initForService(this);
        };

        var modalButtons = [{
            id: 'cancel',
            label: "personalization.modal.button.cancel",
            style: MODAL_BUTTON_STYLES.SECONDARY,
            action: MODAL_BUTTON_ACTIONS.DISMISS
        }, {
            id: 'submit',
            label: "personalization.modal.button.submit",
            action: MODAL_BUTTON_ACTIONS.CLOSE
        }];

        PersonalizationsmarteditContextModal.prototype.openDeleteAction = function(componentType, componentId, containerId, slotId, actionId) {
            modalService.open({
                size: 'md',
                title: 'personalization.modal.deleteaction.title',
                templateInline: '<div id="confirmationModalDescription">{{ "' + "personalization.modal.deleteaction.content" + '" | translate }}</div>',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.componentType = componentType;
                    $scope.componentId = componentId;
                    $scope.actionId = actionId;
                    $scope.modalManager = modalManager;
                    angular.extend(this, $controller('modalDeleteActionController', {
                        $scope: $scope
                    }));
                }],
                buttons: [{
                    id: 'confirmCancel',
                    label: 'personalization.modal.button.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY,
                    action: MODAL_BUTTON_ACTIONS.DISMISS
                }, {
                    id: 'confirmOk',
                    label: 'personalization.modal.button.ok',
                    action: MODAL_BUTTON_ACTIONS.CLOSE
                }]
            }).then(function(result) {
                var previewData = personalizationsmarteditContextService.getSePreviewData();
                personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
                personalizationsmarteditContextService.selectedComponents.splice(personalizationsmarteditContextService.selectedComponents.indexOf(containerId), 1);
            });
        };

        PersonalizationsmarteditContextModal.prototype.openAddAction = function(componentType, componentId, containerId, slotId, actionId) {
            modalService.open({
                title: "personalization.modal.addaction.title",
                templateUrl: 'web/features/personalizationsmarteditcontainer/contextMenu/personalizationsmarteditAddEditActionTemplate.html',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.componentType = componentType;
                    $scope.componentId = componentId;
                    $scope.containerId = containerId;
                    $scope.slotId = slotId;
                    $scope.actionId = actionId;
                    $scope.modalManager = modalManager;
                    $scope.editEnabled = false;
                    angular.extend(this, $controller('modalAddEditActionController', {
                        $scope: $scope
                    }));
                    angular.extend(this, $controller('modalAddActionController', {
                        $scope: $scope
                    }));
                }],
                buttons: modalButtons
            }).then(function(result) {
                var previewData = personalizationsmarteditContextService.getSePreviewData();
                personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
                personalizationsmarteditContextService.selectedComponents.push(result);
            }, function(failure) {});
        };

        PersonalizationsmarteditContextModal.prototype.openEditAction = function(componentType, componentId, containerId, slotId, actionId) {
            modalService.open({
                title: "personalization.modal.editaction.title",
                templateUrl: 'web/features/personalizationsmarteditcontainer/contextMenu/personalizationsmarteditAddEditActionTemplate.html',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.componentType = componentType;
                    $scope.componentId = componentId;
                    $scope.containerId = containerId;
                    $scope.slotId = slotId;
                    $scope.actionId = actionId;
                    $scope.modalManager = modalManager;
                    $scope.editEnabled = true;
                    angular.extend(this, $controller('modalAddEditActionController', {
                        $scope: $scope
                    }));
                    angular.extend(this, $controller('modalEditActionController', {
                        $scope: $scope
                    }));
                }],
                buttons: modalButtons
            }).then(function(result) {
                var previewData = personalizationsmarteditContextService.getSePreviewData();
                personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
            });
        };

        PersonalizationsmarteditContextModal.prototype.openInfoAction = function() {
            personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.modal.infoaction.content'));
        };

        return new PersonalizationsmarteditContextModal();

    }])
    .factory('PaginationHelper', function() {
        PaginationHelper = function(initialData) {
            initialData = initialData || {};

            this.count = initialData.count || 0;
            this.page = initialData.page || 0;
            this.totalCount = initialData.totalCount || 0;
            this.totalPages = initialData.totalPages || 0;

            this.reset = function() {
                this.count = 10;
                this.page = -1;
                this.totalPages = 1;
            };
        };

        return PaginationHelper;
    })
    .controller('modalAddEditActionController', ['$scope', '$filter', '$q', '$timeout', 'editorModalService', 'personalizationsmarteditContextService', 'personalizationsmarteditRestService', 'personalizationsmarteditMessageHandler', 'PaginationHelper', function($scope, $filter, $q, $timeout, editorModalService, personalizationsmarteditContextService, personalizationsmarteditRestService, personalizationsmarteditMessageHandler, PaginationHelper) {

        $scope.actions = [{
            id: "create",
            name: $filter('translate')("personalization.modal.addeditaction.createnewcomponent")
        }, {
            id: "use",
            name: $filter('translate')("personalization.modal.addeditaction.usecomponent")
        }];

        $scope.newComponent = {};
        $scope.component = {};
        $scope.components = [];
        $scope.newComponentTypes = [];

        var initNewComponentTypes = function() {
            personalizationsmarteditRestService.getNewComponentTypes().then(function success(resp) {
                $scope.newComponentTypes = resp.componentTypes;
            }, function error(resp) {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponentstypes'));
            });
        };

        var getAndSetComponentById = function(componentId) {
            personalizationsmarteditRestService.getComponent(componentId).then(function successCallback(resp) {
                $scope.component.selected = resp;
            }, function errorCallback(resp) {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponents'));
            });
        };

        $scope.newComponentTypeSelectedEvent = function($item, $model) {
            personalizationsmarteditRestService.createComponent("name", $scope.newComponent.selected.code, personalizationsmarteditContextService.getPageId(), null)
                .then(function(response) {
                    var sourceComponentId = response.uid;
                    var options = {
                        render: false
                    };
                    editorModalService.open(response.typeCode, sourceComponentId, options).then(
                        function(response) {
                            $scope.componentId = response.uid;
                            getAndSetComponentById($scope.componentId);
                            $scope.action = {
                                selected: $filter('filter')($scope.actions, {
                                    id: "use"
                                }, true)[0]
                            };
                        },
                        function(response) {
                            $scope.newComponent = {};
                            personalizationsmarteditRestService.removeComponent(sourceComponentId, $scope.slotId).then(function() {}, function() {
                                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.removingcomponent'));
                            });
                        });
                }, function() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.creatingcomponent'));
                });
        };

        var editAction = function(customizationId, variationId, actionId, componentId) {
            var deferred = $q.defer();
            personalizationsmarteditRestService.editAction(customizationId, variationId, actionId, componentId).then(
                function successCallback(response) {
                    personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.updatingaction'));
                    deferred.resolve();
                },
                function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingaction'));
                    deferred.reject();
                });
            return deferred.promise;
        };

        var addActionToContainer = function(componentId, containerId, customizationId, variationId) {
            var deferred = $q.defer();
            personalizationsmarteditRestService.addActionToContainer(componentId, containerId, customizationId, variationId).then(
                function successCallback(response) {
                    personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.creatingaction'));
                    deferred.resolve(containerId);
                },
                function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.creatingaction'));
                    deferred.reject();
                });
            return deferred.promise;
        };

        var buttonHandlerFn = function(buttonId) {
            if (buttonId === 'submit') {
                if ($scope.editEnabled) {
                    return editAction($scope.selectedCustomization.code, $scope.selectedVariation.code, $scope.actionId, $scope.component.selected.uid);
                } else {
                    if ($scope.containerId) {
                        return addActionToContainer($scope.component.selected.uid, $scope.containerId, $scope.selectedCustomization.code, $scope.selectedVariation.code);
                    } else {
                        return personalizationsmarteditRestService.replaceComponentWithContainer($scope.componentId, $scope.slotId).then(
                            function successCallback(result) {
                                return addActionToContainer($scope.component.selected.uid, result.uid, $scope.selectedCustomization.code, $scope.selectedVariation.code);
                            },
                            function errorCallback(response) {
                                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.replacingcomponent'));
                                return $q.defer().reject();
                            });
                    }
                }
            }
            return $q.defer().reject();
        };
        $scope.modalManager.setButtonHandler(buttonHandlerFn);

        $scope.componentFilter = {
            name: ''
        };
        var getComponentFilterObject = function() {
            return {
                currentPage: $scope.componentPagination.page + 1,
                mask: $scope.componentFilter.name,
                pageSize: 100,
                sort: 'name'
            };
        };

        $scope.componentPagination = new PaginationHelper();
        $scope.componentPagination.reset();

        $scope.moreComponentRequestProcessing = false;
        $scope.addMoreComponentItems = function() {
            if ($scope.componentPagination.page < $scope.componentPagination.totalPages - 1 && !$scope.moreComponentRequestProcessing) {
                $scope.moreComponentRequestProcessing = true;
                personalizationsmarteditRestService.getComponents(getComponentFilterObject()).then(function successCallback(response) {
                    var filteredComponents = response.componentItems.filter(function(elem) {
                        return $scope.newComponentTypes.some(function(element, index, array) {
                            return elem.typeCode === element.code;
                        });
                    });
                    Array.prototype.push.apply($scope.components, filteredComponents);
                    $scope.componentPagination = new PaginationHelper(response.pagination);
                    $scope.moreComponentRequestProcessing = false;
                    if ($scope.components.length < 20) { //not enough components on list to enable scroll
                        $timeout((function() {
                            $scope.addMoreComponentItems();
                        }), 0);
                    }
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponents'));
                    $scope.moreComponentRequestProcessing = false;
                });
            }
        };

        $scope.componentSearchInputKeypress = function(keyEvent, searchObj) {
            if (keyEvent && ([37, 38, 39, 40].indexOf(keyEvent.which) > -1)) { //keyleft, keyup, keyright, keydown
                return;
            }
            $scope.componentPagination.reset();
            $scope.componentFilter.name = searchObj;
            $scope.components.length = 0;
            $scope.addMoreComponentItems();
        };

        $scope.$watch('action.selected', function(newValue, oldValue) {
            if (newValue !== oldValue) {
                $scope.component.selected = undefined;
                if ($scope.editEnabled) {
                    getAndSetComponentById($scope.componentId);
                }
            }
        }, true);

        $scope.$watch('component.selected', function(newValue, oldValue) {
            $scope.modalManager.disableButton("submit");
            if (newValue !== undefined) {
                $scope.modalManager.enableButton("submit");
            }
        }, true);

        //init
        (function() {
            $scope.selectedCustomization = personalizationsmarteditContextService.selectedCustomizations;
            $scope.selectedVariation = personalizationsmarteditContextService.selectedVariations;

            if ($scope.editEnabled) {
                getAndSetComponentById($scope.componentId);
            }

            initNewComponentTypes();
        })();

    }])
    .controller('modalAddActionController', ['$scope', function($scope) {
        $scope.action = {};
    }])
    .controller('modalEditActionController', ['$scope', '$filter', function($scope, $filter) {
        $scope.action = {
            selected: $filter('filter')($scope.actions, {
                id: "use"
            }, true)[0]
        };
    }])
    .controller('modalDeleteActionController', ['$scope', '$q', 'personalizationsmarteditContextService', 'personalizationsmarteditRestService', function($scope, $q, personalizationsmarteditContextService, personalizationsmarteditRestService) {
        var buttonHandlerFn = function(buttonId) {
            if (buttonId === 'confirmOk') {
                return personalizationsmarteditRestService.deleteAction($scope.selectedCustomization.code, $scope.selectedVariation.code, $scope.actionId);
            }
            return $q.defer().reject();
        };
        $scope.modalManager.setButtonHandler(buttonHandlerFn);
        //init
        (function() {
            $scope.selectedVariation = personalizationsmarteditContextService.selectedVariations;
            $scope.selectedCustomization = personalizationsmarteditContextService.selectedCustomizations;
        })();
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
 *
 *
 */
angular.module('personalizationsmarteditManagerViewModule', [
        'modalServiceModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditRestServiceModule',
        'personalizationsmarteditContextServiceModule',
        'confirmationModalServiceModule',
        'personalizationsmarteditManagerModule',
        'eventServiceModule'
    ])
    .factory('personalizationsmarteditManagerView', ['modalService', 'MODAL_BUTTON_ACTIONS', 'MODAL_BUTTON_STYLES', function(modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES) {
        var manager = {};
        manager.openManagerAction = function() {
            modalService.open({
                title: "personalization.manager.modal.title",
                templateUrl: 'web/features/personalizationsmarteditcontainer/management/managerView/personalizationsmarteditManagerViewTemplate.html',
                controller: 'personalizationsmarteditManagerViewController',
                size: 'fullscreen',
                cssClasses: 'manage-customization'
            }).then(function(result) {

            }, function(failure) {});
        };

        return manager;
    }])
    .controller('personalizationsmarteditManagerViewController', ['$q', '$scope', '$filter', '$timeout', 'confirmationModalService', 'personalizationsmarteditRestService', 'personalizationsmarteditMessageHandler', 'personalizationsmarteditContextService', 'personalizationsmarteditManager', 'systemEventService', function($q, $scope, $filter, $timeout, confirmationModalService, personalizationsmarteditRestService, personalizationsmarteditMessageHandler, personalizationsmarteditContextService, personalizationsmarteditManager, systemEventService) {

        var getCustomizations = function(filter) {
            personalizationsmarteditRestService.getCustomizations(filter)
                .then(function successCallback(response) {
                    if ($scope.isUndefined($scope.allCustomizationsCount)) {
                        $scope.allCustomizationsCount = response.pagination.totalCount;
                    }
                    $scope.customizations = response.customizations || [];
                    $scope.filteredCustomizationsCount = response.pagination.totalCount;
                    $scope.pagination.currentPage = response.pagination.page;
                    $scope.pagination.pages = Array(response.pagination.totalPages).join().split(',').map(function(item, index) {
                        return index;
                    });
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomizations'));
                });
        };

        var getVariations = function(customization, filter) {
            personalizationsmarteditRestService.getCustomization(customization.code)
                .then(function successCallback(response) {
                    customization.variations = response.variations;
                    $scope.customizationClickAction(customization);
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                });
        };

        var getCustomizationsFilterObject = function() {
            return {
                name: $scope.search.name,
                currentSize: $scope.pagination.currentSize,
                currentPage: $scope.pagination.currentPage
            };
        };

        var refreshGrid = function() {
            $timeout(function() {
                getCustomizations(getCustomizationsFilterObject());
            }, 0);
        };

        var currentLanguageIsocode = personalizationsmarteditContextService.getSeExperienceData().languageDescriptor.isocode;
        $scope.catalogName = personalizationsmarteditContextService.getSeExperienceData().catalogDescriptor.name[currentLanguageIsocode];
        $scope.catalogName += " - " + personalizationsmarteditContextService.getSeExperienceData().catalogDescriptor.catalogVersion;
        $scope.customizations = [];
        $scope.allCustomizationsCount = undefined;
        $scope.filteredCustomizationsCount = 0;

        $scope.search = {
            name: ''
        };

        $scope.pagination = {};

        $scope.searchInputKeypress = function(keyEvent) {
            if (keyEvent.which === 13 || $scope.search.name.length > 2 || $scope.search.name.length === 0) {
                $scope.pagination.currentPage = 0;
                refreshGrid();
            }
        };

        $scope.resetSearchInput = function() {
            $scope.search.name = "";
            refreshGrid();
        };

        $scope.editCustomizationAction = function(customization) {
            personalizationsmarteditManager.openEditCustomizationModal(customization.code);
        };
        $scope.deleteCustomizationAction = function(customization) {
            confirmationModalService.confirm({
                description: 'personalization.manager.modal.deletecustomization.content'
            }).then(function() {
                personalizationsmarteditRestService.deleteCustomization(customization.code)
                    .then(function successCallback(response) {
                        $scope.customizations.splice($scope.customizations.indexOf(customization), 1);
                    }, function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.deletingcustomization'));
                    });
            });
        };
        $scope.editVariationAction = function(customization, variation) {
            personalizationsmarteditManager.openEditCustomizationModal(customization.code, variation.code);
        };
        $scope.deleteVariationAction = function(customization, variation, $event) {
            if (customization.variations.length >= 2) {
                confirmationModalService.confirm({
                    description: 'personalization.manager.modal.deletevariation.content'
                }).then(function() {
                    personalizationsmarteditRestService.deleteVariation(customization.code, variation.code)
                        .then(function successCallback(response) {
                            customization.variations.splice(customization.variations.indexOf(variation), 1);
                        }, function errorCallback() {
                            personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.deletingvariation'));
                        });
                });
            } else {
                $event.stopPropagation();
            }
        };

        $scope.openNewModal = function() {
            personalizationsmarteditManager.openCreateCustomizationModal();
        };

        $scope.paginationCallback = function() {
            refreshGrid();
        };

        $scope.setCustomizationRank = function(customization, increaseValue, $event, firstOrLast) {
            if ($scope.isFilterEnabled() || firstOrLast) {
                $event.stopPropagation();
            } else {
                personalizationsmarteditRestService.getCustomization(customization.code)
                    .then(function successCallback(responseCustomization) {
                        responseCustomization.rank = responseCustomization.rank + increaseValue;
                        personalizationsmarteditRestService.updateCustomization(responseCustomization)
                            .then(function successCallback() {
                                refreshGrid();
                            }, function errorCallback() {
                                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingcustomization'));
                            });
                    }, function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                    });
            }
        };

        $scope.setVariationRank = function(customization, variation, increaseValue, $event, firstOrLast) {
            if (firstOrLast) {
                $event.stopPropagation();
            } else {
                personalizationsmarteditRestService.getVariation(customization.code, variation.code)
                    .then(function successCallback(responseVariation) {
                        responseVariation.rank = responseVariation.rank + increaseValue;
                        personalizationsmarteditRestService.editVariation(customization.code, responseVariation)
                            .then(function successCallback() {
                                getVariations(customization);
                            }, function errorCallback() {
                                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.editingvariation'));
                            });
                    }, function errorCallback() {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingvariation'));
                    });
            }
        };

        $scope.isUndefined = function(value) {
            return value === undefined;
        };

        $scope.isFilterEnabled = function() {
            return $scope.filteredCustomizationsCount < $scope.allCustomizationsCount;
        };

        $scope.customizationClickAction = function(customization) {
            var filter = {
                includeActions: true
            };

            personalizationsmarteditRestService.getVariationsForCustomization(customization.code, filter).then(
                function successCallback(response) {
                    customization.variations = response.variations || [];
                    customization.variations.forEach(function(variation) {
                        variation.numberOfComponents = (variation.actions || []).length;
                    });
                },
                function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                });
        };

        systemEventService.registerEventHandler('CUSTOMIZATIONS_MODIFIED', function() {
            refreshGrid();
            return $q.when();
        });

        refreshGrid();

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
 *
 *
 */
angular.module('personalizationsmarteditManagerModule', [
        'modalServiceModule',
        'coretemplates',
        'ui.select',
        'confirmationModalServiceModule',
        'functionsModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditManagementServiceModule',
        'eventServiceModule'
    ])
    .constant('CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS', {
        BASIC_INFO_TAB_NAME: 'basicinfotab',
        BASIC_INFO_TAB_FORM_NAME: 'form.basicinfotab',
        TARGET_GROUP_TAB_NAME: 'targetgrptab',
        TARGET_GROUP_TAB_FORM_NAME: 'form.targetgrptab'
    })
    .constant('CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS', {
        CONFIRM_OK: 'confirmOk',
        CONFIRM_CANCEL: 'confirmCancel',
        CONFIRM_NEXT: 'confirmNext'
    })
    .constant('CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY', {
        CRITERIA_AND: 'AND',
        CRITERIA_OR: 'OR'
    })
    .factory('personalizationsmarteditManager', ['$controller', 'modalService', 'MODAL_BUTTON_ACTIONS', 'MODAL_BUTTON_STYLES', 'CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS', function($controller, modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES, CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS) {

        var manager = {};

        manager.openCreateCustomizationModal = function() {
            return modalService.open({
                title: 'personalization.modal.customizationvariationmanagement.title',
                templateUrl: 'web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagTemplate.html',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.modalManager = modalManager;
                    angular.extend(this, $controller('personalizationsmarteditManagerController', {
                        $scope: $scope
                    }));
                }],
                buttons: [{
                    id: CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_CANCEL,
                    label: 'personalization.modal.button.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY
                }],
                size: 'lg modal_bigger'
            });
        };

        manager.openEditCustomizationModal = function(customizationCode, variationCode) {
            return modalService.open({
                title: 'personalization.modal.customizationvariationmanagement.title',
                templateUrl: 'web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagTemplate.html',
                controller: ['$scope', 'modalManager', function($scope, modalManager) {
                    $scope.customizationCode = customizationCode;
                    $scope.variationCode = variationCode;
                    $scope.modalManager = modalManager;
                    angular.extend(this, $controller('personalizationsmarteditManagerController', {
                        $scope: $scope
                    }));
                }],
                buttons: [{
                    id: 'confirmCancel',
                    label: 'personalization.modal.button.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY
                }],
                size: 'lg modal_bigger'
            });
        };

        return manager;
    }])
    .controller('personalizationsmarteditManagerController', ['$scope', 'hitch', '$q', '$log', '$timeout', 'personalizationsmarteditManagementService', 'personalizationsmarteditMessageHandler', 'personalizationsmarteditUtils', 'confirmationModalService', '$filter', 'MODAL_BUTTON_ACTIONS', 'CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS', 'CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS', 'systemEventService', 'CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY', function($scope, hitch, $q, $log, $timeout, personalizationsmarteditManagementService, personalizationsmarteditMessageHandler, personalizationsmarteditUtils, confirmationModalService, $filter, MODAL_BUTTON_ACTIONS, CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS, CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS, systemEventService, CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY) {
        var self = this;

        var getVariationsForCustomization = function(customizationCode) {
            var filter = {
                includeTriggers: true
            };

            return personalizationsmarteditManagementService.getVariationsForCustomization(customizationCode, filter);
        };

        self.isModallDirty = false;

        $scope.form = {};

        $scope.customization = {
            code: '',
            description: '',
            rank: 0,
            variations: []
        };

        $scope.tabsArr = [{
            name: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_NAME,
            active: true,
            disabled: false,
            heading: $filter('translate')("personalization.modal.customizationvariationmanagement.basicinformationtab"),
            template: 'web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagBasicInfoTemplate.html',
            formName: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_FORM_NAME,
            isDirty: function() {
                return $scope.form.basicinfotab && $scope.form.basicinfotab.$dirty;
            },
            setPristine: function() {
                $scope.form.basicinfotab.$setPristine();
            },
            isValid: function() {
                return $scope.form.basicinfotab && $scope.form.basicinfotab.$valid;
            },
            setEnabled: function(enabled) {
                if (enabled) {
                    $scope.tabsArr[1].disabled = false;
                    $scope.modalManager.enableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
                } else {
                    $scope.tabsArr[1].disabled = true;
                    $scope.modalManager.disableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
                }
            },
            onConfirm: function() {
                $scope.tabsArr[1].active = true;
            },
            onCancel: function() {
                self.onCancel();
            }
        }, {
            name: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME,
            active: false,
            disabled: true,
            heading: $filter('translate')("personalization.modal.customizationvariationmanagement.targetgrouptab"),
            template: 'web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagTargetGrpTemplate.html',
            formName: CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_FORM_NAME,
            isDirty: function() {
                return ($scope.form.targetgrptab && $scope.form.targetgrptab.$dirty) || $scope.edit.variationsListDirty;
            },
            setPristine: function() {
                $scope.form.targetgrptab.$setPristine();
            },
            isValid: function() {
                var isVariationListValid = $scope.customization.variations.length > 0;
                return ($scope.form.targetgrptab && $scope.form.targetgrptab.$valid) && isVariationListValid;
            },
            setEnabled: function(enabled) {
                if (enabled) {
                    $scope.modalManager.enableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
                } else {
                    $scope.modalManager.disableButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
                }
            },
            onConfirm: function() {
                self.onSave();
            },
            onCancel: function() {
                self.onCancel();
            }
        }];

        $scope.edit = {
            code: '',
            name: '',
            segments: [],
            selectedSegments: [],
            allSegmentsChecked: true,
            variationsListChanged: false,
            selectedTab: $scope.tabsArr[0],
            variationsLoaded: false
        };

        $scope.editMode = angular.isDefined($scope.customizationCode);

        if ($scope.editMode) {
            personalizationsmarteditManagementService.getCustomization($scope.customizationCode).then(function successCallback(response) {
                $scope.customization = response;

                if (angular.isDefined($scope.variationCode)) {

                    getVariationsForCustomization($scope.customizationCode).then(function successCallback(response) {
                        $scope.customization.variations = response.variations;
                        $scope.edit.variationsLoaded = true;

                        var filteredCollection = $scope.customization.variations.filter(function(elem) {
                            return elem.code === $scope.variationCode;
                        });

                        if (filteredCollection.length > 0) {

                            $scope.tabsArr[1].active = true;
                            $scope.edit.selectedTab = $scope.tabsArr[1];

                            var selVariation = filteredCollection[0];
                            $scope.edit.selectedVariation = selVariation;
                        }

                    }, function errorCallback(response) {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingsegments'));
                    });
                } else {
                    $scope.edit.selectedTab = $scope.tabsArr[0];
                }
            }, function errorCallback(response) {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponents'));
            });
        }

        $scope.selectTab = function(tab) {
            $scope.edit.selectedTab = tab;
            tab.active = true;
            switch (tab.name) {
                case CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.BASIC_INFO_TAB_NAME:
                    $scope.modalManager.removeButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK);
                    $scope.modalManager.addButton({
                        id: CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT,
                        label: 'personalization.modal.button.next'
                    });
                    break;
                case CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME:
                    $scope.modalManager.removeButton(CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT);
                    $scope.modalManager.addButton({
                        id: CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK,
                        label: 'personalization.modal.button.submit',
                        action: MODAL_BUTTON_ACTIONS.CLOSE
                    });
                    break;
                default:
                    break;
            }
        };

        self.onSave = function() {

            if ($scope.editMode) {
                personalizationsmarteditManagementService.updateCustomizationPackage($scope.customization).then(function successCallback(response) {
                    systemEventService.sendSynchEvent('CUSTOMIZATIONS_MODIFIED', {});
                    personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.updatingcustomization'));
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingcustomization'));
                });
            } else {
                personalizationsmarteditManagementService.createCustomization($scope.customization).then(function successCallback(response) {
                    systemEventService.sendSynchEvent('CUSTOMIZATIONS_MODIFIED', {});
                    personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.creatingcustomization'));
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.creatingcustomization'));
                });
            }

        };

        self.onCancel = function() {
            var deferred = $q.defer();
            if (this.isModallDirty) {
                confirmationModalService.confirm({
                    description: $filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.cancelconfirmation')
                }).then(function() {
                    $scope.modalManager.dismiss();
                    deferred.resolve();
                }, function() {
                    deferred.reject();
                });
            } else {
                $scope.modalManager.dismiss();
                deferred.resolve();
            }

            return deferred.promise;
        };

        var getAllSegmentSelectedForVariation = function(variation) {
            var segmentTrigger = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation);
            return segmentTrigger.groupBy === CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_AND;
        };

        self.init = function() {
            $scope.modalManager.setDismissCallback(hitch(this, this.onCancel));

            $scope.modalManager.setButtonHandler(hitch(this, function(buttonId) {
                switch (buttonId) {
                    case CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_OK:
                        return $scope.edit.selectedTab.onConfirm();
                    case CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_NEXT:
                        return $scope.edit.selectedTab.onConfirm();
                    case CUSTOMIZATION_VARIATION_MANAGEMENT_BUTTONS.CONFIRM_CANCEL:
                        return $scope.edit.selectedTab.onCancel();
                    default:
                        $log.error($filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.invalidbuttonid'), buttonId);
                        break;
                }
            }));

            $scope.$watch(hitch(this, function() {
                var isSelectedTabDirty = $scope.edit.selectedTab.isDirty();
                var isSelectedTabValid = $scope.edit.selectedTab.isValid();
                return {
                    isDirty: isSelectedTabDirty,
                    isValid: isSelectedTabValid
                };
            }), hitch(this, function(obj) {
                if (obj.isDirty) {
                    self.isModallDirty = true;
                    if (obj.isValid) {
                        $scope.edit.selectedTab.setEnabled(true);
                    } else {
                        $scope.edit.selectedTab.setEnabled(false);
                    }
                } else if ($scope.editMode) {
                    if (obj.isValid) {
                        $scope.edit.selectedTab.setEnabled(true);
                    } else {
                        $scope.edit.selectedTab.setEnabled(false);
                    }
                } else {
                    self.isModallDirty = false;
                    $scope.edit.selectedTab.setEnabled(false);
                }
            }), true);

            $scope.$watch('customization.variations', function() {
                $scope.edit.variationsListDirty = true;
            }, true);

            $scope.$watch('edit.selectedVariation', function(variation) {
                if (variation) {
                    $scope.edit.code = variation.code;
                    $scope.edit.name = variation.name;
                    $scope.edit.selectedSegments = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation).segments;
                    $scope.edit.allSegmentsChecked = getAllSegmentSelectedForVariation(variation);
                } else {
                    $scope.edit.code = '';
                    $scope.edit.name = '';
                    $scope.edit.selectedSegments = [];
                    $scope.edit.allSegmentsChecked = true;
                }
            }, true);

            $scope.$watch('edit.selectedTab', function() {
                if ($scope.editMode && !$scope.edit.variationsLoaded && ($scope.edit.selectedTab && $scope.edit.selectedTab.name === CUSTOMIZATION_VARIATION_MANAGEMENT_TABS_CONSTANTS.TARGET_GROUP_TAB_NAME)) {

                    getVariationsForCustomization($scope.customizationCode).then(function successCallback(response) {
                        $scope.customization.variations = response.variations;
                        $scope.edit.variationsLoaded = true;
                    }, function errorCallback(response) {
                        personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingsegments'));
                    });
                }
            }, true);
        };
    }])
    .directive('negate', [
        function() {
            return {
                require: 'ngModel',
                link: function(scope, element, attribute, ngModelController) {
                    ngModelController.$isEmpty = function(value) {
                        return !!value;
                    };

                    ngModelController.$formatters.unshift(function(value) {
                        return !value;
                    });

                    ngModelController.$parsers.unshift(function(value) {
                        return !value;
                    });
                }
            };
        }
    ])
    .directive('uniquetargetgroupname',
        ['isBlank', function(isBlank) {
            var isNameTheSameAsEditedTargetGroup = function(scope, targetGroupName) {
                return scope.edit.selectedVariation && targetGroupName === scope.edit.selectedVariation.code;
            };

            return {
                restrict: "A",
                require: "ngModel",
                scope: false,
                link: function(scope, element, attributes, ctrl) {
                    ctrl.$validators.uniquetargetgroupname = function(modelValue) {
                        if (isBlank(modelValue) || isNameTheSameAsEditedTargetGroup(scope, modelValue)) {
                            return true;
                        } else {
                            return scope.customization.variations.filter(function(e) {
                                return e.code === modelValue;
                            }).length === 0;
                        }
                    };
                }
            };
        }]
    );

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
 *
 *
 */
angular.module('personalizationsmarteditManagementServiceModule', ['personalizationsmarteditRestServiceModule',
        'sharedDataServiceModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditContextServiceModule'
    ])
    .factory('personalizationsmarteditManagementService', ['personalizationsmarteditRestService', function(personalizationsmarteditRestService) {
        var ManagementService = function() {};

        ManagementService.getSegments = function(filter) {
            return personalizationsmarteditRestService.getSegments(filter);
        };

        ManagementService.getCustomization = function(customizationCode) {
            return personalizationsmarteditRestService.getCustomization(customizationCode);
        };

        ManagementService.getVariation = function(customizationCode, variationCode) {
            return personalizationsmarteditRestService.getVariation(customizationCode, variationCode);
        };

        ManagementService.createCustomization = function(customization) {
            return personalizationsmarteditRestService.createCustomization(customization);
        };

        ManagementService.updateCustomizationPackage = function(customization) {
            return personalizationsmarteditRestService.updateCustomizationPackage(customization);
        };


        ManagementService.createVariationForCustomization = function(customizationCode, variation) {
            return personalizationsmarteditRestService.createVariationForCustomization(customizationCode, variation);
        };

        ManagementService.getVariationsForCustomization = function(customizationCode, filter) {
            return personalizationsmarteditRestService.getVariationsForCustomization(customizationCode, filter);
        };

        return ManagementService;
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
 *
 *
 */
angular.module('ui.select')
    .run(["$templateCache", function($templateCache) {
        $templateCache.put("select2personalization/choices.tpl.html", $templateCache.get("select2/choices.tpl.html"));
        var fixedTemplate = $templateCache.get("select2/match-multiple.tpl.html").replace("$item in $select.selected", "$item in $select.selected track by $index");
        $templateCache.put("select2personalization/match-multiple.tpl.html", fixedTemplate);
        $templateCache.put("select2personalization/match.tpl.html", $templateCache.get("select2/match.tpl.html"));
        $templateCache.put("select2personalization/select-multiple.tpl.html", $templateCache.get("select2/select-multiple.tpl.html"));
        $templateCache.put("select2personalization/select.tpl.html", $templateCache.get("select2/select.tpl.html"));

    }]);

angular.module('personalizationsmarteditManagerModule')
    .controller('personalizationsmarteditManagerTargetGrpController', ['$scope', 'isBlank', '$filter', 'personalizationsmarteditUtils', 'personalizationsmarteditManagementService', 'personalizationsmarteditMessageHandler', 'confirmationModalService', 'CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY', function($scope, isBlank, $filter, personalizationsmarteditUtils, personalizationsmarteditManagementService, personalizationsmarteditMessageHandler, confirmationModalService, CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY) {

        var getSegmentGroupByForCriteria = function(allSegmentsSelected) {
            return allSegmentsSelected ? CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_AND : CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_OR;
        };

        $scope.addVariationClick = function() {
            $scope.customization.variations.push({
                name: $scope.edit.name,
                enabled: true,
                triggers: [{
                    type: 'segmentTriggerData',
                    groupBy: getSegmentGroupByForCriteria($scope.edit.allSegmentsChecked),
                    segments: $scope.edit.selectedSegments
                }],
                allSegmentsCriteria: $scope.edit.allSegmentsChecked,
                rank: $scope.customization.variations.length,
                isNew: true
            });

            $scope.edit.name = '';
            $scope.edit.selectedSegments = [];

            resetPagination();
            resetSegmentFiltering();
        };

        $scope.submitChangesClick = function() {
            $scope.edit.selectedVariation.triggers = $scope.edit.selectedVariation.triggers || [];
            var segmentTriggerArr = $scope.edit.selectedVariation.triggers.filter(function(trigger) {
                return trigger.type === "segmentTriggerData";
            });

            if (segmentTriggerArr.length === 0 && $scope.edit.selectedSegments.length > 0) {
                $scope.edit.selectedVariation.triggers.push({
                    type: 'segmentTriggerData',
                    groupBy: '',
                    segments: []
                });
            }

            $scope.edit.selectedVariation.name = $scope.edit.name;
            var triggerSegment = personalizationsmarteditUtils.getSegmentTriggerForVariation($scope.edit.selectedVariation);
            triggerSegment.segments = $scope.edit.selectedSegments;
            triggerSegment.groupBy = getSegmentGroupByForCriteria($scope.edit.allSegmentsChecked);
            $scope.edit.selectedVariation.allSegmentsCriteria = $scope.edit.allSegmentsChecked;
            $scope.edit.selectedVariation = undefined;
        };

        $scope.cancelChangesClick = function() {
            $scope.edit.selectedVariation = undefined;
        };

        $scope.removeVariationClick = function(variation) {
            confirmationModalService.confirm({
                description: 'personalization.manager.modal.deletevariation.content'
            }).then(function() {
                $scope.customization.variations.splice($scope.customization.variations.indexOf(variation), 1);
                $scope.edit.selectedVariation = undefined;
            });
        };

        $scope.setVariationRank = function(variation, increaseValue, $event, firstOrLast) {
            if (firstOrLast) {
                $event.stopPropagation();
            } else {
                var from = $scope.customization.variations.indexOf(variation);
                var to = from + increaseValue;
                var variationsArr = $scope.customization.variations;
                if (to >= 0 && to < variationsArr.length) {
                    variationsArr.splice(to, 0, variationsArr.splice(from, 1)[0]);
                    $scope.recalculateRanksForVariations();
                }
            }
        };

        $scope.recalculateRanksForVariations = function() {
            $scope.customization.variations.forEach(function(part, index) {
                $scope.customization.variations[index].rank = index + 1;
            });
        };

        $scope.canShowVariationSegmentationCriteria = function() {
            return $scope.edit.selectedSegments && $scope.edit.selectedSegments.length > 1;
        };

        $scope.editVariationAction = function(variation) {
            $scope.edit.selectedVariation = variation;
        };

        $scope.canSaveVariation = function() {
            return $scope.edit.selectedSegments && $scope.edit.selectedSegments.length > 0 && !isBlank($scope.edit.name);
        };


        $scope.isVariationSelected = function() {
            return angular.isDefined($scope.edit.selectedVariation);
        };

        $scope.getSegmentCodesStrForVariation = function(variation) {
            var segments = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation).segments || [];
            var segmentCodes = segments.map(function(elem) {
                return elem.code;
            }).filter(function(elem) {
                return typeof elem !== 'undefined';
            });
            return segmentCodes.join(", ");
        };

        $scope.getSegmentLenghtForVariation = function(variation) {
            var segments = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation).segments || [];
            return segments.length;
        };

        $scope.getCriteriaDescrForVariation = function(variation) {
            var segmentTrigger = personalizationsmarteditUtils.getSegmentTriggerForVariation(variation);
            if (segmentTrigger.groupBy === CUSTOMIZATION_VARIATION_MANAGEMENT_SEGMENTTRIGGER_GROUPBY.CRITERIA_AND) {
                return $filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.allsegments');
            } else {
                return $filter('translate')('personalization.modal.customizationvariationmanagement.targetgrouptab.anysegments');
            }
        };

        var getSegmentsFilterObject = function() {
            return {
                code: $scope.segmentFilter.code,
                pageSize: $scope.segmentPagination.count,
                currentPage: $scope.segmentPagination.page + 1
            };
        };

        var resetPagination = function() {
            $scope.segmentPagination.count = 10;
            $scope.segmentPagination.page = -1;
            $scope.segmentPagination.totalPages = 1;
        };

        var resetSegmentFiltering = function() {
            $scope.segmentFilter = {
                code: ''
            };
            $scope.edit.segments.length = 0;
        };

        $scope.segmentPagination = {};

        resetPagination();
        resetSegmentFiltering();

        $scope.moreSegmentRequestProcessing = false;
        $scope.addMoreSegmentItems = function() {
            if ($scope.segmentPagination.page < $scope.segmentPagination.totalPages - 1 && !$scope.moreSegmentRequestProcessing) {
                $scope.moreSegmentRequestProcessing = true;
                personalizationsmarteditManagementService.getSegments(getSegmentsFilterObject()).then(function successCallback(response) {
                    Array.prototype.push.apply($scope.edit.segments, response.segments);
                    $scope.segmentPagination = response.pagination;
                    $scope.moreSegmentRequestProcessing = false;
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingsegments'));
                    $scope.moreSegmentRequestProcessing = false;
                });
            }
        };

        $scope.segmentSearchInputKeypress = function(keyEvent, searchObj) {
            resetPagination();
            $scope.segmentFilter.code = searchObj;
            $scope.edit.segments.length = 0;
            $scope.addMoreSegmentItems();
        };

        $scope.segmentSelectedEvent = function(item, uiSelectController) {
            var valueArr = $scope.edit.selectedSegments.map(function(elem) {
                return elem.code;
            });
            var isDuplicate = valueArr.some(function(elem, idx) {
                return valueArr.indexOf(elem) != idx;
            });
            if (isDuplicate) {
                var duplicated = $scope.edit.selectedSegments.filter(function(elem) {
                    return elem.code == item.code;
                });
                $scope.edit.selectedSegments.splice($scope.edit.selectedSegments.indexOf(duplicated[1]), 1);
            }

            uiSelectController.search = '';
            $scope.segmentSearchInputKeypress(null, '');
        };

        $scope.initUiSelect = function(uiSelectController) {
            if (uiSelectController.open) {
                $scope.segmentSearchInputKeypress(null, uiSelectController.search);
            }
        };

        $scope.$watch('edit.selectedSegments', function() {
            if (($scope.edit.selectedSegments || []).length === 0)
                $scope.segmentSearchInputKeypress(null, '');
        }, true);

    }]) //To remove when angular-ui-select would be upgraded to version > 0.19
    .directive('uisOpenClose', ['$parse', '$timeout', function($parse, $timeout) {
        return {
            restrict: 'A',
            require: 'uiSelect',
            link: function(scope, element, attrs, $select) {
                $select.onOpenCloseCallback = $parse(attrs.uisOpenClose);

                scope.$watch('$select.open', function(isOpen, previousState) {
                    if (isOpen !== previousState) {
                        $timeout(function() {
                            $select.onOpenCloseCallback(scope, {
                                isOpen: isOpen
                            });
                        });
                    }
                });
            }
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
 *
 *
 */
jQuery(document).ready(function($) {

    var loadCSS = function(href) {
        var cssLink = $("<link rel='stylesheet' type='text/css' href='" + href + "'>");
        $("head").append(cssLink);
    };

    loadCSS("/personalizationsmartedit/css/style.css");

});

angular.module('personalizationsmarteditcontainermodule', [
        'personalizationsmarteditcontainerTemplates',
        'personalizationsmarteditContextServiceModule',
        'personalizationsmarteditRestServiceModule',
        'ui.bootstrap',
        'personalizationsmarteditCommons',
        'functionsModule',
        'personalizationsmarteditPreviewServiceModule',
        'personalizationsmarteditManagerModule',
        'personalizationsmarteditManagerViewModule',
        'personalizationsmarteditContextMenu',
        'featureServiceModule',
        'perspectiveServiceModule',
        'iFrameManagerModule'
    ])
    .factory('personalizationsmarteditIFrameUtils', ['iFrameManager', function(iFrameManager) {
        var iframeUtils = {};
        iframeUtils.reloadPreview = function(resourcePath, previewTicketId) {
            iFrameManager.loadPreview(resourcePath, previewTicketId);
        };
        return iframeUtils;
    }])
    .controller('topToolbarMenuController', ['$scope', 'personalizationsmarteditManager', 'personalizationsmarteditManagerView', function($scope, personalizationsmarteditManager, personalizationsmarteditManagerView) {
        $scope.status = {
            isopen: false
        };

        $scope.preventDefault = function(oEvent) {
            oEvent.stopPropagation();
        };

        $scope.createCustomizationClick = function() {
            personalizationsmarteditManager.openCreateCustomizationModal();
        };

        $scope.managerViewClick = function() {
            personalizationsmarteditManagerView.openManagerAction();
        };

    }])
    .run(
        ['$log', '$filter', 'personalizationsmarteditContextService', 'personalizationsmarteditContextServiceReverseProxy', 'personalizationsmarteditManagerView', 'personalizationsmarteditContextModal', 'featureService', 'perspectiveService', 'personalizationsmarteditPreviewService', 'personalizationsmarteditIFrameUtils', 'personalizationsmarteditMessageHandler', function($log, $filter, personalizationsmarteditContextService, personalizationsmarteditContextServiceReverseProxy, personalizationsmarteditManagerView, personalizationsmarteditContextModal, featureService, perspectiveService, personalizationsmarteditPreviewService, personalizationsmarteditIFrameUtils, personalizationsmarteditMessageHandler) {
            var PersonalizationviewContextServiceReverseProxy = new personalizationsmarteditContextServiceReverseProxy('PersonalizationCtxReverseGateway');

            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.right.toolbar',
                type: 'HYBRID_ACTION',
                nameI18nKey: 'personalization.right.toolbar',
                priority: 4,
                section: 'left',
                include: 'web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemWrapperTemplate.html'
            });
            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'personalizationsmartedit.container.manager.toolbar',
                type: 'HYBRID_ACTION',
                nameI18nKey: 'personalization.manager.toolbar',
                priority: 5,
                section: 'left',
                include: 'web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagMenuTemplate.html'
            });
            featureService.register({
                key: 'personalizationsmartedit.context.service',
                nameI18nKey: 'personalization.context.service.name',
                descriptionI18nKey: 'personalization.context.service.description',
                enablingCallback: function() {
                    personalizationsmarteditContextService.setPersonalizationContextEnabled(true);
                },
                disablingCallback: function() {

                    var currentVariations = personalizationsmarteditContextService.selectedVariations;
                    if (angular.isObject(currentVariations) && !angular.isArray(currentVariations)) {
                        var previewTicketId = personalizationsmarteditContextService.getSePreviewData().previewTicketId;
                        personalizationsmarteditPreviewService.removePersonalizationDataFromPreview(previewTicketId).then(function successCallback() {
                            var previewData = personalizationsmarteditContextService.getSePreviewData();
                            personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
                        }, function errorCallback() {
                            personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingpreviewticket'));
                        });
                    }

                    personalizationsmarteditContextService.setPersonalizationContextEnabled(false);
                    personalizationsmarteditContextService.selectedCustomizations = null;
                    personalizationsmarteditContextService.selectedVariations = null;
                    personalizationsmarteditContextService.selectedComponents = null;

                }
            });

            perspectiveService.register({
                key: 'personalizationsmartedit.perspective',
                nameI18nKey: 'personalization.perspective.name',
                descriptionI18nKey: 'personalization.perspective.description',
                features: ['personalizationsmartedit.context.service',
                    'personalizationsmartedit.container.right.toolbar',
                    'personalizationsmartedit.container.manager.toolbar',
                    'personalizationsmarteditComponentLightUp',
                    'personalizationsmartedit.context.add.action',
                    'personalizationsmartedit.context.edit.action',
                    'personalizationsmartedit.context.delete.action',
                    'personalizationsmartedit.context.info.action',
                    'se.contextualMenu',
                    'se.cms.edit'
                ],
                perspectives: []
            });

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
 *
 *
 */
angular.module('personalizationsmarteditcontainermodule')
    .directive('personalizationsmarteditRightToolbarItem', function() {
        return {
            templateUrl: 'web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemTemplate.html',
            restrict: 'E',
            transclude: true,
            link: function(scope, elem, attrs) {
                //none
            }
        };
    })
    .controller('personalizationsmarteditRightToolbarController', ['$scope', '$filter', '$timeout', 'personalizationsmarteditRestService', 'personalizationsmarteditContextService', 'personalizationsmarteditMessageHandler', 'personalizationsmarteditPreviewService', 'personalizationsmarteditUtils', 'personalizationsmarteditIFrameUtils', 'personalizationsmarteditManager', function($scope, $filter, $timeout, personalizationsmarteditRestService, personalizationsmarteditContextService, personalizationsmarteditMessageHandler, personalizationsmarteditPreviewService, personalizationsmarteditUtils, personalizationsmarteditIFrameUtils, personalizationsmarteditManager) {
        $scope.hidePersonalizationButtonI18nKey = "personalization.right.toolbar";
        $scope.customizationButtonI18nKey = "personalization.right.toolbar.customization.button";

        $scope.customizationsOnPage = [];
        $scope.allCustomizationsCount = undefined;

        $scope.search = {
            pageId: personalizationsmarteditContextService.getPageId(),
            libraryCustomizations: [],
            selectedLibraryCustomizations: [],
            searchCustomizationEnabled: false
        };

        $scope.pagination = {
            fixedPageSize: true,
            currentSize: 5
        };

        var removeArrayFromArrayByCode = function(fromArray, toRemoveArray) {
            var filteredArray = fromArray.filter(function(elem) {
                return toRemoveArray.map(function(e) {
                    return e.code;
                }).indexOf(elem.code) < 0;
            });

            return filteredArray;
        };

        var getCustomizations = function(categoryFilter) {
            personalizationsmarteditRestService.getCustomizations(categoryFilter)
                .then(function successCallback(response) {
                    if (angular.isUndefined($scope.allCustomizationsCount)) {
                        $scope.allCustomizationsCount = response.pagination.totalCount;
                    }
                    $scope.customizationsOnPage = response.customizations || [];

                    $scope.pagination.currentPage = response.pagination.page;
                    $scope.pagination.pages = Array(response.pagination.totalPages).join().split(',').map(function(item, index) {
                        return index;
                    });

                    $scope.search.libraryCustomizations.length = 0;
                    resetCustLibraryPagination($scope.libraryCustPagination);
                    $scope.addMoreCustomizationItems();
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomizations'));
                });
        };

        var getAndSetComponentsForVariation = function(customizationId, variationId) {
            personalizationsmarteditRestService.getComponenentsIdsForVariation(customizationId, variationId).then(function successCallback(response) {
                personalizationsmarteditContextService.setSelectedComponents(response.components);
            }, function errorCallback() {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcomponentsforvariation'));
            });
        };

        var updatePreviewTicket = function(customizationId, variationArray) {
            var previewTicketId = personalizationsmarteditContextService.getSePreviewData().previewTicketId;
            var variationKeys = personalizationsmarteditUtils.getVariationKey(customizationId, variationArray);
            personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicketId, variationKeys).then(function successCallback() {
                var previewData = personalizationsmarteditContextService.getSePreviewData();
                personalizationsmarteditIFrameUtils.reloadPreview(previewData.resourcePath, previewData.previewTicketId);
            }, function errorCallback() {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.updatingpreviewticket'));
            });
        };

        var getCustomizationsFilterObject = function() {
            return {
                pageId: personalizationsmarteditContextService.getPageId(),
                currentSize: $scope.pagination.currentSize,
                currentPage: $scope.pagination.currentPage
            };
        };

        var getCustomizationsFilterObjectForLibrary = function() {
            return {
                pageId: personalizationsmarteditContextService.getPageId(),
                negatePageId: true,
                name: $scope.customizationFilter.name,
                currentSize: $scope.libraryCustPagination.count,
                currentPage: $scope.libraryCustPagination.page + 1
            };
        };

        var resetCustLibraryPagination = function(pagination) {
            pagination.count = 10;
            pagination.page = -1;
            pagination.totalPages = 1;
        };

        $scope.customizationFilter = {
            name: ''
        };

        $scope.libraryCustPagination = {};
        resetCustLibraryPagination($scope.libraryCustPagination);
        $scope.moreCustomizationsRequestProcessing = false;

        $scope.addMoreCustomizationItems = function() {
            if ($scope.libraryCustPagination.page < $scope.libraryCustPagination.totalPages - 1 && !$scope.moreCustomizationsRequestProcessing) {
                $scope.moreCustomizationsRequestProcessing = true;
                personalizationsmarteditRestService.getCustomizations(getCustomizationsFilterObjectForLibrary()).then(function successCallback(response) {
                    var filteredCategories = removeArrayFromArrayByCode(response.customizations, $scope.customizationsOnPage);
                    filteredCategories.forEach(function(customization) {
                        customization.fromLibrary = true;
                    });
                    Array.prototype.push.apply($scope.search.libraryCustomizations, filteredCategories);

                    $scope.libraryCustPagination = response.pagination;
                    $scope.moreCustomizationsRequestProcessing = false;
                }, function errorCallback(response) {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomizations'));
                    $scope.moreCustomizationsRequestProcessing = false;
                });
            }
        };

        $scope.customizationSearchInputKeypress = function(keyEvent, searchObj) {
            resetCustLibraryPagination($scope.libraryCustPagination);
            $scope.customizationFilter.name = searchObj;
            $scope.search.libraryCustomizations.length = 0;
            $scope.addMoreCustomizationItems();
        };

        var refreshGrid = function() {
            $timeout(function() {
                getCustomizations(getCustomizationsFilterObject());
            }, 0);
        };

        $scope.variationClick = function(customization, variation) {
            personalizationsmarteditContextService.setSelectedVariations(variation);
            getAndSetComponentsForVariation(customization.code, variation.code);
            updatePreviewTicket(customization.code, [variation]);
        };

        $scope.customizationClick = function(customization) {
            var currentVariations = personalizationsmarteditContextService.selectedVariations;
            personalizationsmarteditContextService.setSelectedCustomizations(customization);
            personalizationsmarteditContextService.setSelectedVariations(customization.variations || null);
            if (customization.variations.length > 0) {
                var allVariations = personalizationsmarteditUtils.getVariationCodes(customization.variations).join(",");
                getAndSetComponentsForVariation(customization.code, allVariations);
            }
            if (angular.isObject(currentVariations) && !angular.isArray(currentVariations)) {
                updatePreviewTicket();
            }

            $scope.customizationsOnPage.filter(function(cust) {
                return customization.name !== cust.name;
            }).forEach(function(cust, index) {
                cust.collapsed = true;
            });
        };

        $scope.addCustomizationFromLibrary = function() {
            $scope.customizationsOnPage = $scope.customizationsOnPage.concat($scope.search.selectedLibraryCustomizations);
            $scope.search.libraryCustomizations = removeArrayFromArrayByCode($scope.search.libraryCustomizations, $scope.search.selectedLibraryCustomizations);
            $scope.search.selectedLibraryCustomizations = [];
            $scope.toggleAddMoreCustomizationsClick();
        };

        $scope.toggleAddMoreCustomizationsClick = function() {
            $scope.search.searchCustomizationEnabled = !$scope.search.searchCustomizationEnabled;
        };

        $scope.editCustomizationAction = function(customization) {
            personalizationsmarteditManager.openEditCustomizationModal(customization.code);
        };

        $scope.customizationClickAction = function(customization) {
            personalizationsmarteditRestService.getVariationsForCustomization(customization.code)
                .then(function successCallback(response) {
                    customization.variations = response.variations;
                }, function errorCallback() {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingcustomization'));
                });
        };

        $scope.paginationCallback = function() {
            refreshGrid();
        };

        $scope.initCustomization = function(customization) {
            customization.collapsed = true;
            if ((personalizationsmarteditContextService.selectedCustomizations || {}).code === customization.code) {
                customization.collapsed = false;
            }
        };

        $scope.getSelectedVariationClass = function(variation) {
            if (angular.equals(variation, personalizationsmarteditContextService.selectedVariations)) {
                return "selectedVariation";
            }
        };

        refreshGrid();
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
 *
 *
 */
angular.module('personalizationsmarteditContextServiceModule', ['sharedDataServiceModule', 'loadConfigModule'])
    .factory('personalizationsmarteditContextService', ['personalizationsmarteditContextServiceProxy', 'sharedDataService', 'loadConfigManagerService', function(personalizationsmarteditContextServiceProxy, sharedDataService, loadConfigManagerService) {

        var ContextServiceProxy = new personalizationsmarteditContextServiceProxy('PersonalizationCtxGateway');

        var ContextService = {};
        ContextService.personalizationEnabled = false;
        ContextService.selectedCustomizations = null;
        ContextService.selectedVariations = null;
        ContextService.selectedComponents = null;
        ContextService.seExperienceData = null;
        ContextService.seConfigurationData = null;
        ContextService.sePreviewData = null;
        ContextService.pageId = null;

        ContextService.isPersonalizationContextEnabled = function() {
            return ContextService.personalizationEnabled;
        };

        ContextService.setPersonalizationContextEnabled = function(persCtxEnabled) {
            ContextService.personalizationEnabled = persCtxEnabled;
            ContextServiceProxy.setPersonalizationContextEnabled(persCtxEnabled);
        };

        ContextService.setSelectedComponents = function(newSelectedComponents) {
            ContextService.selectedComponents = newSelectedComponents;
            ContextServiceProxy.setSelectedComponents(newSelectedComponents);
        };

        ContextService.setSelectedVariations = function(newSelectedVariations) {
            ContextService.selectedVariations = newSelectedVariations;
            ContextServiceProxy.setSelectedVariations(newSelectedVariations);
        };

        ContextService.setSelectedCustomizations = function(newSelectedCustomization) {
            ContextService.selectedCustomizations = newSelectedCustomization;
            ContextServiceProxy.setSelectedCustomizations(newSelectedCustomization);
        };

        ContextService.refreshExperienceData = function() {
            sharedDataService.get('experience').then(function(data) {
                ContextService.setSeExperienceData(data);
            });
        };

        ContextService.getSeExperienceData = function() {
            return ContextService.seExperienceData;
        };

        ContextService.setSeExperienceData = function(newSeExperienceData) {
            ContextService.seExperienceData = newSeExperienceData;
            ContextServiceProxy.setSeExperienceData(newSeExperienceData);
        };

        ContextService.getSePreviewData = function() {
            return ContextService.sePreviewData;
        };

        ContextService.setSePreviewData = function(newSePreviewData) {
            ContextService.sePreviewData = newSePreviewData;
            ContextServiceProxy.setSePreviewData(newSePreviewData);
        };

        ContextService.refreshPreviewData = function() {
            sharedDataService.get('preview').then(function(data) {
                ContextService.setSePreviewData(data);
            });
        };

        ContextService.refreshConfigurationData = function() {
            loadConfigManagerService.loadAsObject().then(function(configurations) {
                ContextService.setSeConfigurationData(configurations);
            });
        };

        ContextService.getSeConfigurationData = function() {
            return ContextService.seConfigurationData;
        };

        ContextService.setSeConfigurationData = function(newSeConfigurationData) {
            ContextService.seConfigurationData = newSeConfigurationData;
            ContextServiceProxy.setSeConfigurationData(newSeConfigurationData);
        };

        ContextService.getPageId = function() {
            return ContextService.pageId;
        };

        ContextService.setPageId = function(newPageId) {
            ContextService.pageId = newPageId;
        };

        ContextService.applySynchronization = function() {
            ContextServiceProxy.setSelectedVariations(ContextService.selectedVariations);
            ContextServiceProxy.setSelectedCustomizations(ContextService.selectedCustomizations);
            ContextServiceProxy.setSelectedComponents(ContextService.selectedComponents);
            ContextServiceProxy.setPersonalizationContextEnabled(ContextService.personalizationEnabled);

            ContextService.refreshExperienceData();
            ContextService.refreshPreviewData();
            ContextService.refreshConfigurationData();
        };

        ContextService.getContexServiceProxy = function() {
            return ContextServiceProxy;
        };

        return ContextService;
    }])
    .factory('personalizationsmarteditContextServiceProxy', ['gatewayProxy', function(gatewayProxy) {
        var proxy = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this);
        };
        proxy.prototype.setPersonalizationContextEnabled = function() {};
        proxy.prototype.setSelectedComponents = function() {};
        proxy.prototype.setSelectedVariations = function() {};
        proxy.prototype.setSelectedCustomizations = function() {};
        proxy.prototype.setSeExperienceData = function() {};
        proxy.prototype.setSeConfigurationData = function() {};
        proxy.prototype.setSePreviewData = function() {};

        return proxy;
    }])
    .factory('personalizationsmarteditContextServiceReverseProxy', ['gatewayProxy', 'personalizationsmarteditContextService', function(gatewayProxy, personalizationsmarteditContextService) {
        var reverseProxy = function(gatewayId) {
            this.gatewayId = gatewayId;
            gatewayProxy.initForService(this);
        };

        reverseProxy.prototype.applySynchronization = function() {
            personalizationsmarteditContextService.applySynchronization();
        };

        reverseProxy.prototype.setPageId = function(newPageId) {
            personalizationsmarteditContextService.setPageId(newPageId);
        };

        return reverseProxy;
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
 *
 *
 */
angular.module('personalizationsmarteditPreviewServiceModule', ['personalizationsmarteditRestServiceModule',
        'sharedDataServiceModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditContextServiceModule'
    ])
    .factory('personalizationsmarteditPreviewService', ['$q', '$filter', 'personalizationsmarteditRestService', 'sharedDataService', 'personalizationsmarteditUtils', 'personalizationsmarteditMessageHandler', 'personalizationsmarteditContextService', function($q, $filter, personalizationsmarteditRestService, sharedDataService, personalizationsmarteditUtils, personalizationsmarteditMessageHandler, personalizationsmarteditContextService) {

        var previewService = {};

        previewService.removePersonalizationDataFromPreview = function(previewTicketId) {
            var deferred = $q.defer();
            previewService.updatePreviewTicketWithVariations(previewTicketId, []).then(function successCallback(previewTicket) {
                deferred.resolve(previewTicket);
            }, function errorCallback(response) {
                deferred.reject(response);
            });
            return deferred.promise;
        };

        previewService.updatePreviewTicketWithVariations = function(previewTicketId, variations) {
            var deferred = $q.defer();
            personalizationsmarteditRestService.getPreviewTicket(previewTicketId).then(function successCallback(previewTicket) {
                previewTicket.variations = variations;
                deferred.resolve(personalizationsmarteditRestService.updatePreviewTicket(previewTicket));
            }, function errorCallback(response) {
                if (response.status === 404) {
                    //preview ticket not found - let's try to create a new one with the same parameters
                    deferred.resolve(previewService.createPreviewTicket(variations));
                } else {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingpreviewticket'));
                    deferred.reject(response);
                }
            });

            return deferred.promise;
        };

        previewService.createPreviewTicket = function(variationsForPreview) {
            var experience = personalizationsmarteditContextService.getSeExperienceData();
            var configuration = personalizationsmarteditContextService.getSeConfigurationData();

            var deferred = $q.defer();

            var resourcePath = configuration.domain + experience.siteDescriptor.previewUrl;

            var previewTicket = {
                catalog: experience.catalogDescriptor.catalogId,
                catalogVersion: experience.catalogDescriptor.catalogVersion,
                language: experience.languageDescriptor.isocode,
                resourcePath: resourcePath,
                variations: variationsForPreview
            };

            personalizationsmarteditRestService.createPreviewTicket(previewTicket).then(function successCallback(response) {
                previewService.storePreviewTicketData(response.resourcePath, response.ticketId);
                personalizationsmarteditContextService.refreshPreviewData();
                personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.newpreviewticketcreated'));
                deferred.resolve(response);
            }, function errorCallback(response) {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.creatingpreviewticket'));
                deferred.reject(response);
            });

            return deferred.promise;
        };

        previewService.storePreviewTicketData = function(resourcePathToStore, previewTicketIdToStore) {
            var previewToStore = {
                previewTicketId: previewTicketIdToStore,
                resourcePath: resourcePathToStore
            };
            sharedDataService.set('preview', previewToStore);
        };


        return previewService;
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
 *
 *
 */
angular.module('personalizationsmarteditRestServiceModule', ['restServiceFactoryModule', 'personalizationsmarteditCommons', 'personalizationsmarteditContextServiceModule'])
    .factory('personalizationsmarteditRestService', ['restServiceFactory', 'personalizationsmarteditUtils', 'personalizationsmarteditContextService', function(restServiceFactory, personalizationsmarteditUtils, personalizationsmarteditContextService) {

        var CUSTOMIZATIONS = "/personalizationwebservices/v1/catalogs/:catalogId/catalogVersions/:catalogVersion/customizations";
        var CUSTOMIZATION = CUSTOMIZATIONS + "/:customizationCode";

        var CUSTOMIZATION_PACKAGES = "/personalizationwebservices/v1/catalogs/:catalogId/catalogVersions/:catalogVersion/customizationpackages";
        var CUSTOMIZATION_PACKAGE = CUSTOMIZATION_PACKAGES + "/:customizationCode";

        var VARIATIONS = CUSTOMIZATION + "/variations";
        var VARIATION = VARIATIONS + "/:variationCode";

        var ACTIONS = VARIATION + "/actions";
        var ACTION = ACTIONS + "/:actionId";

        var CXCMSC_ACTIONS_FROM_VARIATIONS = "/personalizationwebservices/v1/query/cxcmscomponentsfromvariations";

        var PREVIEWTICKET = "/previewwebservices/v1/preview/:ticketId";
        var SEGMENTS = "/personalizationwebservices/v1/segments";

        var CATALOGS = "/cmswebservices/v1/sites/:siteId/catalogs/:catalogId/versions/:catalogVersion/items";
        var CATALOG = CATALOGS + "/:itemId";

        var ADD_CONTAINER = "/personalizationwebservices/v1/query/cxReplaceComponentWithContainer";

        var COMPONENT_TYPES = '/cmswebservices/v1/types?category=COMPONENT';

        var VARIATION_FOR_CUSTOMIZATION_FIELDS = ["active", "enabled", "code", "name", "rank"];
        var VARIATION_FOR_CUSTOMIZATION_TRIGGERS_FIELD = "triggers(code,groupBy,segments)";
        var VARIATION_FOR_CUSTOMIZATION_ACTIONS_FIELDS = "actions";

        var extendRequestParamObjWithCatalogAwarePathVariables = function(requestParam) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var catalogAwareParams = {
                catalogId: experienceData.catalogDescriptor.catalogId,
                catalogVersion: experienceData.catalogDescriptor.catalogVersion
            };
            requestParam = angular.extend(requestParam, catalogAwareParams);
            return requestParam;
        };

        var extendRequestParamObjWithCustomizatonCode = function(requestParam, customizatiodCode) {
            var customizationCodeParam = {
                customizationCode: customizatiodCode
            };
            requestParam = angular.extend(requestParam, customizationCodeParam);
            return requestParam;
        };

        var getParamsAction = function(oldComponentId, newComponentId, slotId, containerId, customizationId, variationId) {
            var entries = [];
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "oldComponentId", oldComponentId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "newComponentId", newComponentId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "slotId", slotId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "containerId", containerId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "variationId", variationId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "customizationId", customizationId);
            return {
                "params": {
                    "entry": entries
                }
            };
        };

        var getPathVariablesObjForModifyingActionURI = function(customizationId, variationId, actionId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            return {
                customizationCode: customizationId,
                variationCode: variationId,
                actionId: actionId,
                catalogId: experienceData.catalogDescriptor.catalogId,
                catalogVersion: experienceData.catalogDescriptor.catalogVersion
            };
        };

        var restService = {};

        restService.getCustomizations = function(filter) {
            filter = filter || {};

            var restService = restServiceFactory.get(CUSTOMIZATIONS);
            var requestParams = {};

            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams);

            requestParams.pageSize = filter.currentSize || 10;
            requestParams.currentPage = filter.currentPage || 0;

            if (angular.isDefined(filter.code)) {
                requestParams.code = filter.code || '';
            }
            if (angular.isDefined(filter.pageId)) {
                requestParams.pageId = filter.pageId || '';
            }
            if (angular.isDefined(filter.name)) {
                requestParams.name = filter.name || '';
            }
            if (angular.isDefined(filter.negatePageId)) {
                requestParams.negatePageId = filter.negatePageId || 'false';
            }
            return restService.get(requestParams);
        };

        restService.getComponenentsIdsForVariation = function(customizationId, variationId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();

            var restService = restServiceFactory.get(CXCMSC_ACTIONS_FROM_VARIATIONS);
            var entries = [];
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "customization", customizationId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "variations", variationId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "catalog", experienceData.catalogDescriptor.catalogId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(entries, "catalogVersion", experienceData.catalogDescriptor.catalogVersion);
            var requestParams = {
                "params": {
                    "entry": entries
                }
            };
            return restService.save(requestParams);
        };

        restService.getPreviewTicket = function() {
            var previewTicketData = personalizationsmarteditContextService.getSePreviewData();
            var restService = restServiceFactory.get(PREVIEWTICKET, "ticketId");
            var previewTicket = {
                ticketId: previewTicketData.previewTicketId
            };
            return restService.get(previewTicket);
        };

        restService.updatePreviewTicket = function(previewTicket) {
            var restService = restServiceFactory.get(PREVIEWTICKET, "ticketId");
            return restService.update(previewTicket);
        };

        restService.createPreviewTicket = function(previewTicket) {
            var previewRESTService = restServiceFactory.get(PREVIEWTICKET);
            return previewRESTService.save(previewTicket);
        };

        restService.getSegments = function(filter) {
            var restService = restServiceFactory.get(SEGMENTS);
            return restService.get(filter);
        };

        restService.getCustomization = function(customizationCode) {
            var restService = restServiceFactory.get(CUSTOMIZATION, "customizationCode");

            var requestParams = {
                customizationCode: customizationCode
            };

            return restService.get(extendRequestParamObjWithCatalogAwarePathVariables(requestParams));
        };

        restService.createCustomization = function(customization) {
            var restService = restServiceFactory.get(CUSTOMIZATION_PACKAGES);

            return restService.save(extendRequestParamObjWithCatalogAwarePathVariables(customization));
        };

        restService.updateCustomization = function(customization) {
            var restService = restServiceFactory.get(CUSTOMIZATION, "customizationCode");
            customization.customizationCode = customization.code;
            return restService.update(extendRequestParamObjWithCatalogAwarePathVariables(customization));
        };

        restService.updateCustomizationPackage = function(customization) {
            var restService = restServiceFactory.get(CUSTOMIZATION_PACKAGE, "customizationCode");
            customization.customizationCode = customization.code;
            return restService.update(extendRequestParamObjWithCatalogAwarePathVariables(customization));
        };

        restService.deleteCustomization = function(customizationCode) {
            var restService = restServiceFactory.get(CUSTOMIZATION, "customizationCode");

            var requestParams = {
                customizationCode: customizationCode
            };

            return restService.remove(extendRequestParamObjWithCatalogAwarePathVariables(requestParams));
        };

        restService.getVariation = function(customizationCode, variationCode) {
            var restService = restServiceFactory.get(VARIATION, "variationCode");
            var requestParams = {
                variationCode: variationCode
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams, customizationCode);
            return restService.get(requestParams);
        };

        restService.editVariation = function(customizationCode, variation) {
            var restService = restServiceFactory.get(VARIATION, "variationCode");

            variation = extendRequestParamObjWithCatalogAwarePathVariables(variation);
            variation = extendRequestParamObjWithCustomizatonCode(variation, customizationCode);
            variation.variationCode = variation.code;
            return restService.update(variation);
        };

        restService.deleteVariation = function(customizationCode, variationCode) {
            var restService = restServiceFactory.get(VARIATION, "variationCode");
            var requestParams = {
                variationCode: variationCode
            };

            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams, customizationCode);

            return restService.remove(requestParams);
        };

        restService.createVariationForCustomization = function(customizationCode, variation) {
            var restService = restServiceFactory.get(VARIATIONS);

            variation = extendRequestParamObjWithCatalogAwarePathVariables(variation);
            variation = extendRequestParamObjWithCustomizatonCode(variation, customizationCode);

            return restService.save(variation);
        };

        restService.getVariationsForCustomization = function(customizationCode, filter) {
            var restService = restServiceFactory.get(VARIATIONS);

            var requestParams = {};
            var fieldsForRequest = [];
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            requestParams = extendRequestParamObjWithCustomizatonCode(requestParams, customizationCode);

            fieldsForRequest = fieldsForRequest.concat(VARIATION_FOR_CUSTOMIZATION_FIELDS);
            if (filter.includeActions) {
                fieldsForRequest.push(VARIATION_FOR_CUSTOMIZATION_ACTIONS_FIELDS);
            }
            if (filter.includeTriggers) {
                fieldsForRequest.push(VARIATION_FOR_CUSTOMIZATION_TRIGGERS_FIELD);
            }

            requestParams.fields = "variations(" + fieldsForRequest.join() + ")";

            return restService.get(requestParams);
        };

        restService.getComponents = function(filter) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOGS);
            var compomentsParams = {
                catalogId: experienceData.catalogDescriptor.catalogId,
                catalogVersion: experienceData.catalogDescriptor.catalogVersion,
                siteId: experienceData.siteDescriptor.uid
            };
            compomentsParams = angular.extend(compomentsParams, filter);
            return restService.get(compomentsParams);
        };

        restService.replaceComponentWithContainer = function(componentId, slotId) {
            var restService = restServiceFactory.get(ADD_CONTAINER);
            var catalogParams = extendRequestParamObjWithCatalogAwarePathVariables({});
            var requestParams = getParamsAction(componentId, null, slotId, null, null, null);
            personalizationsmarteditUtils.pushToArrayIfValueExists(requestParams.params.entry, "catalog", catalogParams.catalogId);
            personalizationsmarteditUtils.pushToArrayIfValueExists(requestParams.params.entry, "catalogVersion", catalogParams.catalogVersion);

            return restService.save(requestParams);
        };

        restService.addActionToContainer = function(componentId, containerId, customizationId, variationId) {
            var restService = restServiceFactory.get(ACTIONS);
            var pathVariables = getPathVariablesObjForModifyingActionURI(customizationId, variationId);
            var requestParams = {
                "type": "cxCmsActionData",
                "containerId": containerId,
                "componentId": componentId
            };
            requestParams = angular.extend(requestParams, pathVariables);
            return restService.save(requestParams);
        };

        restService.editAction = function(customizationId, variationId, actionId, newComponentId) {
            var restService = restServiceFactory.get(ACTION, "actionId");

            var requestParams = getPathVariablesObjForModifyingActionURI(customizationId, variationId, actionId);

            return restService.get(requestParams).then(function successCallback(actionInfo) {
                actionInfo = angular.extend(actionInfo, requestParams);
                actionInfo.componentId = newComponentId;
                return restService.update(actionInfo);
            });
        };

        restService.deleteAction = function(customizationId, variationId, actionId) {
            var restService = restServiceFactory.get(ACTION, "actionId");

            var requestParams = getPathVariablesObjForModifyingActionURI(customizationId, variationId, actionId);

            return restService.remove(requestParams);
        };

        restService.createComponent = function(componentName, componentType, pageId, slotId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOGS);
            var requestParams = {
                name: componentName,
                slotId: slotId,
                pageId: pageId,
                position: 1,
                typeCode: componentType,
                siteId: experienceData.siteDescriptor.uid
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);

            return restService.save(requestParams);
        };

        restService.removeComponent = function(componentId, slotId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOG, "itemId");
            var requestParams = {
                slotId: slotId,
                itemId: componentId,
                siteId: experienceData.siteDescriptor.uid
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);

            return restService.remove(requestParams);
        };

        restService.getComponent = function(itemId) {
            var experienceData = personalizationsmarteditContextService.getSeExperienceData();
            var restService = restServiceFactory.get(CATALOG, "itemId");
            var requestParams = {
                itemId: itemId,
                siteId: experienceData.siteDescriptor.uid
            };
            requestParams = extendRequestParamObjWithCatalogAwarePathVariables(requestParams);
            return restService.get(requestParams);
        };

        restService.getNewComponentTypes = function() {
            var restService = restServiceFactory.get(COMPONENT_TYPES);
            return restService.get();
        };

        return restService;
    }]);

angular.module('personalizationsmarteditcontainerTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/personalizationsmarteditcontainer/contextMenu/personalizationsmarteditAddEditActionTemplate.html',
    "<div class=\"customization-component\">\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase text-smaller dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.customization.title\"></dt>\n" +
    "                <dd class=\"dd-text-dark-blue-bold\">{{selectedCustomization.name}}</dd>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase text-smaller dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.mastercomponent.title\"></dt>\n" +
    "                <dd class=\"dd-text-dark-blue-bold\">{{componentType}}</dd>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase text-smaller dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.variation.title\"></dt>\n" +
    "                <dd class=\"dd-text-dark-blue-bold\">{{selectedVariation.name}}</dd>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"row\">\n" +
    "        <div class=\"col-md-6\">\n" +
    "            <dl>\n" +
    "                <dt class=\"text-uppercase dt-text-grey-semibold\" data-translate=\"personalization.modal.addeditaction.selected.actions.title\"></dt>\n" +
    "            </dl>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <ui-select ng-model=\"action.selected\" theme=\"select2\" title=\"\" search-enabled=\"false\">\n" +
    "        <ui-select-match placeholder=\"{{'personalization.modal.addeditaction.dropdown.placeholder' | translate}}\">\n" +
    "            <span ng-bind=\"$select.selected.name | translate\"></span>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices repeat=\"item in actions\" position=\"down\">\n" +
    "            <span ng-bind=\"item.name | translate\"></span>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "\n" +
    "    <ui-select ng-show=\"action.selected.id == 'use'\" ng-model=\"component.selected\" ng-keyup=\"componentSearchInputKeypress($event, $select.search)\" theme=\"select2\" title=\"\" reset-search-input=\"false\">\n" +
    "        <ui-select-match placeholder=\"{{'personalization.modal.addeditaction.dropdown.componentlist.placeholder' | translate}}\">\n" +
    "            <span ng-bind=\"$select.selected.name | translate\"></span>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices repeat=\"item in components\" position=\"down\" personalization-infinite-scroll=\"addMoreComponentItems()\" personalization-infinite-scroll-distance=\"2\">\n" +
    "            <div class=\"row\">\n" +
    "                <span class=\"col-md-7\" ng-bind=\"item.name | translate\"></span>\n" +
    "                <span class=\"col-md-5\" ng-bind=\"item.typeCode | translate\"></span>\n" +
    "            </div>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "\n" +
    "    <ui-select ng-show=\"action.selected.id == 'create'\" on-select=\"newComponentTypeSelectedEvent($item, $model)\" ng-model=\"newComponent.selected\" theme=\"select2\" title=\"\" search-enabled=\"false\">\n" +
    "        <ui-select-match placeholder=\"{{'personalization.modal.addeditaction.dropdown.componenttype.placeholder' | translate}}\">\n" +
    "            <span ng-bind=\"$select.selected.name | translate\"></span>\n" +
    "        </ui-select-match>\n" +
    "        <ui-select-choices repeat=\"item in newComponentTypes\" position=\"down\">\n" +
    "            <span ng-bind=\"item.name | translate\"></span>\n" +
    "        </ui-select-choices>\n" +
    "    </ui-select>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/managerView/personalizationsmarteditManagerViewTemplate.html',
    "<div id=\"editConfigurationsBody\" class=\"manage-customization\">\n" +
    "\n" +
    "    <h2 class=\"text-capitalize h2-color--grey-font\" ng-bind=\"catalogName\"></h2>\n" +
    "\n" +
    "    <div class=\"input-group customization-search\">\n" +
    "        <span class=\"input-group-addon glyphicon glyphicon-search ySESearchIcon\"></span>\n" +
    "        <input type=\"text\" class=\"form-control ng-pristine ng-untouched ng-valid\" placeholder=\"{{ 'personalization.manager.modal.search.placeholder' | translate}}\" ng-model=\"search.name\" ng-keyup=\"searchInputKeypress($event)\"></input>\n" +
    "    </div>\n" +
    "    <div ng-show=\"isFilterEnabled()\">\n" +
    "        <div class=\"search-results\">\n" +
    "            <span ng-bind=\"filteredCustomizationsCount\"></span>\n" +
    "            <span data-translate=\"personalization.manager.modal.search.result.label\"></span>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"categoryTable\">\n" +
    "        <!-- headers -->\n" +
    "        <div class=\"row row-reset-margin tablehead hidden-xs\">\n" +
    "            <div class=\"col-xs-8 text-left\" data-translate=\"personalization.manager.modal.grid.header.customization\"></div>\n" +
    "            <div class=\"col-xs-2 text-left\" data-translate=\"personalization.manager.modal.grid.header.variations\"></div>\n" +
    "            <div class=\"col-xs-2 text-left\" data-translate=\"personalization.manager.modal.grid.header.components\"></div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"row row-reset-margin\">\n" +
    "            <div class=\"col-xs-12 col-reset-padding\">\n" +
    "                <button class=\"y-add y-add-btn button-margin\" type=\"button\" data-ng-click=\"openNewModal();\">\n" +
    "                    <span class=\"hyicon hyicon-add\" data-translate=\"personalization.manager.modal.add.button\"></span>\n" +
    "                </button>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div data-ng-repeat=\"customization in customizations\" ng-init=\"isCollapsed=true\">\n" +
    "            <div class=\"row row-reset-margin customizationLayout divider\">\n" +
    "                <div class=\"col-xs-half text-right\" ng-click=\"isCollapsed=! isCollapsed; customizationClickAction(customization)\">\n" +
    "                    <a class=\"btn btn-link category-toggle\">\n" +
    "                        <span class=\"glyphicon glyphicon-chevron-down \"></span>\n" +
    "                    </a>\n" +
    "                </div>\n" +
    "                <div class=\"col-xs-8 pull-left text-left\" ng-click=\"isCollapsed=! isCollapsed; customizationClickAction(customization)\">\n" +
    "                    <p class=\"primaryData customization-rank-{{customization.rank}}-row \">\n" +
    "                        <span class=\"categoryName personalizationsmartedit-customization-code \">{{customization.name}}</span>\n" +
    "                    </p>\n" +
    "                </div>\n" +
    "                <div class=\"col-xs-2 pull-left text-left\">\n" +
    "                    <p>{{customization.variations.length || 0}}</p>\n" +
    "                </div>\n" +
    "                <div class=\"col-xs-half col-offset-1 pull-right\">\n" +
    "                    <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "                        <span class=\"hyicon hyicon-more\"></span>\n" +
    "                    </button>\n" +
    "                    <ul class=\"dropdown-menu pull-right text-left\" role=\"menu\">\n" +
    "                        <li>\n" +
    "                            <a class=\"customization-edit-button\" ng-click=\"editCustomizationAction(customization)\" data-translate=\"personalization.manager.modal.button.edit\"></a>\n" +
    "                        </li>\n" +
    "                        <li>\n" +
    "                            <a class=\"customization-delete-button\" ng-click=\"deleteCustomizationAction(customization)\" data-translate=\"personalization.manager.modal.button.delete\"></a>\n" +
    "                        </li>\n" +
    "                        <li ng-class=\"isFilterEnabled() || $first ? 'disabled' : '' \">\n" +
    "                            <a class=\"customization-move-up-button\" ng-click=\"setCustomizationRank(customization, -1, $event, $first)\" data-translate=\"personalization.manager.modal.button.moveup\"></a>\n" +
    "                        </li>\n" +
    "                        <li ng-class=\"isFilterEnabled() || $last ? 'disabled' : '' \">\n" +
    "                            <a class=\"customization-move-down-button\" ng-click=\"setCustomizationRank(customization, 1, $event, $last)\" data-translate=\"personalization.manager.modal.button.movedown\"></a>\n" +
    "                        </li>\n" +
    "                    </ul>\n" +
    "                </div>\n" +
    "                <!--end col-sm-5 for contextual menu-->\n" +
    "            </div>\n" +
    "            <!--end customizationLayout-->\n" +
    "\n" +
    "            <div collapse=\"isCollapsed\">\n" +
    "                <div ng-repeat=\"variation in customization.variations\">\n" +
    "                    <div class=\"variationLayout divider\">\n" +
    "                        <div class=\"row row-reset-margin variation-rank-{{variation.rank}}-row\">\n" +
    "                            <div class=\"col-xs-half\"></div>\n" +
    "                            <div class=\"col-xs-10 text-left\">\n" +
    "                                <span>{{variation.name}}</span>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-1 text-left \">\n" +
    "                                <span>{{variation.numberOfComponents}}</span>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-half pull-right\">\n" +
    "                                <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right\" data-toggle=\"dropdown\">\n" +
    "                                    <span class=\"hyicon hyicon-more\"></span>\n" +
    "                                </button>\n" +
    "                                <ul class=\"dropdown-menu text-left pull-right\" role=\"menu\">\n" +
    "                                    <li>\n" +
    "                                        <a class=\"customization-edit-button\" ng-click=\"editVariationAction(customization, variation)\" data-translate=\"personalization.manager.modal.button.edit\"></a>\n" +
    "                                    </li>\n" +
    "                                    <li ng-class=\"customization.variations.length < 2 ? 'disabled' : '' \">\n" +
    "                                        <a class=\"customization-delete-button\" ng-click=\"deleteVariationAction(customization, variation, $event)\" data-translate=\"personalization.manager.modal.button.delete\"></a>\n" +
    "                                    </li>\n" +
    "                                    <li ng-class=\"$first ? 'disabled' : '' \">\n" +
    "                                        <a class=\"variation-move-down-button\" ng-click=\"setVariationRank(customization, variation, -1, $event, $first)\" data-translate=\"personalization.manager.modal.button.moveup\"></a>\n" +
    "                                    </li>\n" +
    "                                    <li ng-class=\"$last ? 'disabled' : '' \">\n" +
    "                                        <a class=\"variation-move-down-button\" ng-click=\"setVariationRank(customization, variation, 1, $event, $last)\" data-translate=\"personalization.manager.modal.button.movedown\"></a>\n" +
    "                                    </li>\n" +
    "                                </ul>\n" +
    "                            </div>\n" +
    "                            <!--end col-sm-2 for contextual menu-->\n" +
    "                        </div>\n" +
    "                        <!--end variation-rank-->\n" +
    "                    </div>\n" +
    "                    <!--end variationLayout-->\n" +
    "                </div>\n" +
    "                <!--end variation-repeat-->\n" +
    "            </div>\n" +
    "            <!--end collapse-->\n" +
    "        </div>\n" +
    "        <!--end customization-repeat-->\n" +
    "    </div>\n" +
    "    <!--end categoryTable-->\n" +
    "    <personalizationsmartedit-pagination pages=\"pagination.pages \" current-page=\"pagination.currentPage \" page-sizes=\"pagination.pageSizes \" current-size=\"pagination.currentSize \" pages-offset=\"pagination.pagesOffset \" callback=\"paginationCallback \" />\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagMenuTemplate.html',
    "<div ng-controller=\"topToolbarMenuController\" dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEComponentMenuW\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction ySEComponentMenuW--button personalizationsmarteditTopToolbarButton\" dropdown-toggle ng-disabled=\"disabled\" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"../personalizationsmartedit/icons/icon_library.png\" />\n" +
    "        <span class=\"ySEComponentMenuW--button--txt\" data-translate=\"personalization.manager.toolbar\"></span>\n" +
    "    </button>\n" +
    "    <ul ng-if=\"status.isopen\" class=\"dropdown-menu btn-block ySEComponentMenu ySEComponentMenu-customized\" role=\"menu\" ng-click=\"preventDefault($event);\">\n" +
    "        <div class=\"ySEComponentMenu-headers\">\n" +
    "            <h2 class=\"text-uppercase h2\" data-translate=\"personalization.right.toolbar.library.name\"></h2>\n" +
    "            <small class=\"small\" data-translate=\"personalization.topmenu.library.info\"></small>\n" +
    "        </div>\n" +
    "        <li class=\"divider divider-lefttoolbar\">\n" +
    "        </li>\n" +
    "        <li class=\"item ySEComponentMenuList--item\">\n" +
    "            <a class=\"ySEComponentMenu-anchor\" id=\"personalizationsmartedit-right-toolbar-customization-anchor\" data-translate=\"personalization.topmenu.library.manager.name\" data-ng-click=\"managerViewClick()\"></a>\n" +
    "        </li>\n" +
    "        <li class=\"divider divider-lefttoolbar\"></li>\n" +
    "        <li class=\"item ySEComponentMenuList--item\">\n" +
    "            <a class=\"ySEComponentMenu-anchor\" id=\"personalizationsmartedit-right-toolbar-customization-anchor\" data-translate=\"personalization.manager.modal.add.button\" data-ng-click=\"createCustomizationClick()\"></a>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/personalizationsmarteditCustomizationManagTemplate.html',
    "<div>\n" +
    "    <tabset>\n" +
    "        <tab ng-repeat=\"tab in tabsArr\" select=\"selectTab(tab)\" active=\"tab.active\" disable=\"tab.disabled\" heading=\"{{tab.heading}}\">\n" +
    "            <form name=\"{{tab.formName}}\" novalidate>\n" +
    "                <div>\n" +
    "                    <ng-include src=\"tab.template\"></ng-include>\n" +
    "                </div>\n" +
    "            </form>\n" +
    "        </tab>\n" +
    "    </tabset>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagBasicInfoTemplate.html',
    "<div class=\"customization-form--size-large\">\n" +
    "    <div class=\"form-group\">\n" +
    "        <label data-translate=\"personalization.modal.customizationvariationmanagement.basicinformationtab.name\" class=\"customization-form-control-label--size\"></label>\n" +
    "        <input type=\"text\" class=\"form-control customization-form-control-input--size\" placeholder=\"{{'personalization.modal.customizationvariationmanagement.basicinformationtab.name.placeholder' | translate}}\" name=\"{{customization.name}}_key\" data-ng-model=\"customization.name\" data-ng-required=\"true\">\n" +
    "    </div>\n" +
    "    <div class=\"form-group\">\n" +
    "        <label data-translate=\"personalization.modal.customizationvariationmanagement.basicinformationtab.details\" class=\"customization-form-control-label--size\"></label>\n" +
    "        <textarea rows=\"8\" class=\"form-control customization-form-control-input--size\" placeholder=\"{{'personalization.modal.customizationvariationmanagement.basicinformationtab.details.placeholder' | translate}}\" name=\"{{customization.description}}_key\" data-ng-model=\"customization.description\"></textarea>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/management/tabs/personalizationsmarteditCustVarManagTargetGrpTemplate.html',
    "<div ng-controller=\"personalizationsmarteditManagerTargetGrpController\" class=\"row customization-form--size-large\">\n" +
    "    <div class=\"customization-form-target-group-tab-sub-title--size--font\">\n" +
    "        <span ng-bind=\"customization.name\"></span>\n" +
    "        <span ng-if='customization.description'>:</span>\n" +
    "        <span class=\"customization-form-target-group-tab-sub-title-description--font\"></span>\n" +
    "        <span ng-bind=\"customization.description\"></span>\n" +
    "    </div>\n" +
    "    <div class=\"col-md-6\">\n" +
    "        <div ng-if='customization.variations.length === 0'>\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab\"></label>\n" +
    "            <h6 data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.notargetgroups\"></h6>\n" +
    "        </div>\n" +
    "\n" +
    "        <div ng-if='customization.variations.length > 0'>\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab\"></label>\n" +
    "\n" +
    "            <ul class=\"list-group\">\n" +
    "                <li class=\"list-group-item list-group-item-size list-group-item--hover\" data-ng-repeat=\"variation in customization.variations \">\n" +
    "\n" +
    "                    <div class=\"row target-group-list\">\n" +
    "                        <div class=\"col-md-11\">\n" +
    "                            <div class=\"customization-variation-code-font-text-style\" ng-bind=\"variation.name\"></div>\n" +
    "                            <div ng-bind=\"getSegmentCodesStrForVariation(variation)\"></div>\n" +
    "                            <div ng-if=\"getSegmentLenghtForVariation(variation) > 1\">\n" +
    "                                </br>\n" +
    "                                <span class=\"customization-criteria-font-size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.criteria.colon\"></span>\n" +
    "                                <span ng-bind=\"getCriteriaDescrForVariation(variation)\"></span>\n" +
    "                                </br>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "\n" +
    "                        <div class=\"col-md-1\">\n" +
    "                            <button type=\"button\" class=\"dropdown pull-right btn btn-link dropdown-toggle\" id=\"dropdownMenu1\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\">\n" +
    "                                <span class=\"hyicon hyicon-more hyicon-in-modal-size\"></span>\n" +
    "                            </button>\n" +
    "                            <ul class=\"dropdown-menu\" aria-labelledby=\"dropdownMenu1\" role=\"menu\">\n" +
    "                                <li>\n" +
    "                                    <a ng-click=\"editVariationAction(variation)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.edit\" />\n" +
    "                                </li>\n" +
    "\n" +
    "                                <li>\n" +
    "                                    <a ng-click=\"removeVariationClick(variation)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.remove\" />\n" +
    "                                </li>\n" +
    "\n" +
    "                                <li ng-class=\"$first ? 'disabled' : '' \">\n" +
    "                                    <a ng-click=\"setVariationRank(variation, -1, $event, $first)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.moveup\" />\n" +
    "                                </li>\n" +
    "\n" +
    "                                <li ng-class=\"$last ? 'disabled' : '' \">\n" +
    "                                    <a ng-click=\"setVariationRank(variation, 1, $event, $last)\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.action.movedown\" />\n" +
    "                                </li>\n" +
    "                            </ul>\n" +
    "\n" +
    "                        </div>\n" +
    "\n" +
    "                    </div>\n" +
    "\n" +
    "                </li>\n" +
    "            </ul>\n" +
    "\n" +
    "        </div>\n" +
    "    </div>\n" +
    "    <div class=\"col-md-6\">\n" +
    "\n" +
    "        <div class=\"form-group\">\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroupname\"></label>\n" +
    "            <input uniquetargetgroupname type=\"text\" class=\"form-control\" placeholder=\"{{'personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroupname.placeholder' | translate}}\" name=\"variationname_key\" data-ng-model=\"edit.name\">\n" +
    "            <span ng-show=\"{{tab.formName}}.variationname_key.$error.uniquetargetgroupname\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroup.uniquename.validation.message\"></span>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"form-group\">\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.segments\"></label>\n" +
    "            <ui-select uis-open-close=\"initUiSelect($select)\" multiple='true' ng-model=\"edit.selectedSegments\" theme=\"select2personalization\" ng-disabled=\"disabled\" class=\"form-control\" ng-keyup=\"segmentSearchInputKeypress($event, $select.search)\" on-select=\"segmentSelectedEvent($item, $select)\" on-remove=\"segmentSelectedEvent($item, $select)\">\n" +
    "                <ui-select-match placeholder=\"{{ 'personalization.modal.customizationvariationmanagement.targetgrouptab.segments.placeholder' | translate}}\">\n" +
    "                    <span class=\"customization-ui-select-match-item--colors\">{{$item.code}} &lt;{{$item.code}}&gt;</span>\n" +
    "                    <span class=\"close ui-select-match-close\" ng-hide=\"$select.disabled\" ng-click=\"$selectMultiple.removeChoice($index)\">&nbsp;×</span>\n" +
    "                </ui-select-match>\n" +
    "                <ui-select-choices repeat=\"item in edit.segments\" position=\"down\" personalization-infinite-scroll=\"addMoreSegmentItems()\" personalization-infinite-scroll-distance=\"2\">\n" +
    "                    <div ng-html-bind=\"item.code\"></div>\n" +
    "                    <small>\n" +
    "                        Segment: {{item.code}}\n" +
    "                    </small>\n" +
    "                </ui-select-choices>\n" +
    "            </ui-select>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"form-group\" ng-if=\"canShowVariationSegmentationCriteria()\">\n" +
    "            <label class=\"customization-form-control-label--size\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.criteria\"></label>\n" +
    "            <div class=\"row\">\n" +
    "                <div class=\"col-md-6\">\n" +
    "                    <input type=\"checkbox\" data-ng-model=\"edit.allSegmentsChecked\">\n" +
    "                    <label data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.allsegments\" ng-click=\"edit.allSegmentsChecked = !edit.allSegmentsChecked\"></label>\n" +
    "                </div>\n" +
    "                <div class=\"col-md-6\">\n" +
    "                    <input type=\"checkbox\" negate data-ng-model=\"edit.allSegmentsChecked\">\n" +
    "                    <label data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.anysegments\" ng-click=\"edit.allSegmentsChecked = !edit.allSegmentsChecked\"></label>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "\n" +
    "        <div ng-if=\"!isVariationSelected()\">\n" +
    "            <button type=\"button\" class=\"btn btn-default add-target-group\" data-ng-click=\"addVariationClick()\" data-ng-disabled=\"!canSaveVariation()\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.addvariation\"></button>\n" +
    "        </div>\n" +
    "        <div ng-if=\"isVariationSelected()\">\n" +
    "            <button type=\"button\" class=\"btn btn-default submit-target-group-edit\" data-ng-click=\"submitChangesClick()\" data-ng-disabled=\"!canSaveVariation()\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.savechanges\"></button>\n" +
    "            <button type=\"button\" class=\"btn btn-subordinate cancel-target-group-edit\" data-ng-click=\"cancelChangesClick()\" data-translate=\"personalization.modal.customizationvariationmanagement.targetgrouptab.cancelchanges\"></button>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemTemplate.html',
    "<div id=\"personalizationsmartedit-right-toolbar-item-template\" class=\"manage-customization\">\n" +
    "\n" +
    "    <div ng-controller=\"personalizationsmarteditRightToolbarController\" data-customizations-loaded=\"{{customizationsOnPage.length > 0}}\">\n" +
    "\n" +
    "        <div class=\"reset-padding\" ng-show=\"!search.searchCustomizationEnabled\">\n" +
    "            <button class=\"y-add y-add-btn y-add-btn-lefttoolbar\" type=\"button\" data-ng-click=\"toggleAddMoreCustomizationsClick()\">\n" +
    "                <span class=\"hyicon hyicon-add\" data-translate=\"personalization.right.toolbar.addmorecustomizations.button\" />\n" +
    "            </button>\n" +
    "        </div>\n" +
    "        <div class=\"text-left search-item-lefttoolbar\" ng-show=\"search.searchCustomizationEnabled\">\n" +
    "            <ui-select multiple class=\"form-control customizations-from-library-multi-select\" ng-model=\"search.selectedLibraryCustomizations\" theme=\"select2\" ng-disabled=\"disabled\" on-select=\"addCustomizationFromLibrary()\" ng-keyup=\"customizationSearchInputKeypress($event, $select.search)\">\n" +
    "                <ui-select-match placeholder=\"{{'personalization.right.toolbar.addmorecustomizations.customization.library.search.placeholder' | translate}}\">\n" +
    "                    {{$item.name}} &lt;{{$item.name}}&gt;\n" +
    "                </ui-select-match>\n" +
    "                <ui-select-choices repeat=\"item in search.libraryCustomizations\" personalization-infinite-scroll=\"addMoreCustomizationItems()\" personalization-infinite-scroll-distance=\"2\">\n" +
    "                    <div ng-html-bind=\"item.name | highlight: $select.search\"></div>\n" +
    "                    <small>{{item.name}}</small>\n" +
    "                </ui-select-choices>\n" +
    "            </ui-select>\n" +
    "        </div>\n" +
    "\n" +
    "        <div class=\"overflow-wrapper-lefttoolbar\">\n" +
    "            <div class=\"categoryTable categoryTable-lefttoolbar\">\n" +
    "                <div data-ng-repeat=\"customization in customizationsOnPage | orderBy: 'rank'\" ng-init=\"initCustomization(customization)\">\n" +
    "                    <div class=\"row row-lefttoolbar\">\n" +
    "                        <div ng-class=\"{'custFromLibExtraStyling customization-from-library':customization.fromLibrary, 'customization-rank-{{customization.rank}}':true}\">\n" +
    "                            <div class=\"col-xs-1 text-right\" ng-click=\"customization.collapsed = !customization.collapsed; customizationClick(customization)\">\n" +
    "                                <a class=\"btn btn-link category-toggle\">\n" +
    "                                    <span class=\"glyphicon glyphicon-chevron-down glyphicon-down-lefttoolbar\"></span>\n" +
    "                                </a>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-9 text-left\" ng-click=\"customization.collapsed = !customization.collapsed; customizationClick(customization)\">\n" +
    "                                <span class=\"primaryData\">{{customization.name}}</span>\n" +
    "                            </div>\n" +
    "                            <div class=\"col-xs-1\" ng-init=\"subMenu=false\">\n" +
    "\n" +
    "                                <button type=\"button\" class=\"btn btn-link dropdown-toggle pull-right customization-rank-{{customization.rank}}-dropdown-toggle\" data-toggle=\"dropdown\" ng-click=\"subMenu = !subMenu\">{{'personalization.right.toolbar.customization.cust.onpage.action' | translate}}\n" +
    "                                    <span class=\"hyicon hyicon-more\"></span>\n" +
    "                                </button>\n" +
    "                                <ul ng-if=\"subMenu\" class=\"dropdown-menu pull-right text-left dropdown-menu-leftoolbar\" role=\"menu\">\n" +
    "                                    <li>\n" +
    "                                        <a class=\"dropdown-menu-single-item cutomization-rank-{{customization.rank}}-edit-button\" ng-click=\"editCustomizationAction(customization)\" data-translate=\"personalization.manager.modal.button.edit\"></a>\n" +
    "                                    </li>\n" +
    "                                </ul>\n" +
    "\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                    <!--end row desktopLayout-->\n" +
    "\n" +
    "                    <div collapse=\"customization.collapsed\">\n" +
    "                        <div ng-repeat=\"variation in customization.variations\">\n" +
    "                            <div class=\"row row-lefttoolbar\" ng-class=\"getSelectedVariationClass(variation)\">\n" +
    "                                <div class=\"col-xs-9 col-xs-offset-1 text-left\" ng-click=\"variationClick(customization, variation)\">\n" +
    "                                    <span>{{variation.name}}</span>\n" +
    "                                </div>\n" +
    "                                <div class=\"col-xs-1 text-left selectedVariation-icon\"></div>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "                <!--end data repeat-->\n" +
    "            </div>\n" +
    "            <!--end of categoryTable-->\n" +
    "\n" +
    "            <personalizationsmartedit-pagination pages=\"pagination.pages\" current-page=\"pagination.currentPage\" page-sizes=\"pagination.pageSizes\" current-size=\"pagination.currentSize\" pages-offset=\"pagination.pagesOffset\" fixed-page-size=\"pagination.fixedPageSize\" callback=\"paginationCallback\" template=\"personalizationsmarteditPaginationLefttoolbarTemplate.html\" />\n" +
    "        </div>\n" +
    "        <!--end of overflow-wrapper-lefttoolbar -->\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/personalizationsmarteditcontainer/rightToolbar/personalizationsmarteditRightToolbarItemWrapperTemplate.html',
    "<div ng-controller=\"topToolbarMenuController\" dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEComponentMenuW\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction ySEComponentMenuW--button personalizationsmarteditTopToolbarButton\" dropdown-toggle ng-disabled=\"disabled\" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"../personalizationsmartedit/icons/icon_customize.png\" />\n" +
    "        <span class=\"ySEComponentMenuW--button--txt\" data-translate=\"personalization.right.toolbar\"></span>\n" +
    "    </button>\n" +
    "    <ul ng-if=\"status.isopen\" class=\"dropdown-menu dropdown-menu-left btn-block ySEComponentMenu ySEComponentMenu-customized\" role=\"menu\" ng-click=\"preventDefault($event);\">\n" +
    "        <div class=\"ySEComponentMenu-headers\">\n" +
    "            <h2 class=\"text-uppercase h2\" data-translate=\"personalization.right.toolbar.customization.heading\"></h2>\n" +
    "            <small class=\"small\" data-translate=\"personalization.right.toolbar.customization.description\"></small>\n" +
    "        </div>\n" +
    "        <li role=\"menuitem\" class=\"item menuitem-lefttoolbar\">\n" +
    "            <personalizationsmartedit-right-toolbar-item selected-item-callbacks=\"selectedItemCallbacks\" close-page-tool-menu=\"closePageToolMenu()\"></personalizationsmartedit-right-toolbar-item>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );

}]);
