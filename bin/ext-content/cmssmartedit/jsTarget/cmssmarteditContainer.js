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
angular.module('cmssmarteditContainer', [
        'experienceInterceptorModule',
        'resourceLocationsModule',
        'cmssmarteditContainerTemplates',
        'featureServiceModule',
        'componentMenuModule',
        'editorModalServiceModule',
        'genericEditorModule',
        'eventServiceModule',
        'cmsDragAndDropServiceModule',
        'catalogDetailsModule',
        'synchronizeCatalogModule',
        'perspectiveServiceModule',
        'pageListLinkModule',
        'pageListControllerModule',
        'clientPagedListModule',
        'assetsServiceModule'
    ])
    .config(['PAGE_LIST_PATH', '$routeProvider', function(PAGE_LIST_PATH, $routeProvider) {
        $routeProvider.when(PAGE_LIST_PATH, {
            templateUrl: 'web/features/cmssmarteditContainer/pageList/pageListTemplate.html',
            controller: 'pageListController',
            controllerAs: 'pageListCtl'
        });
    }])
    .run(
        ['$log', '$rootScope', 'ComponentService', 'systemEventService', 'cmsDragAndDropService', 'catalogDetailsService', 'featureService', 'perspectiveService', 'assetsService', function($log, $rootScope, ComponentService, systemEventService, cmsDragAndDropService, catalogDetailsService, featureService, perspectiveService, assetsService) {

            featureService.addToolbarItem({
                toolbarId: 'experienceSelectorToolbar',
                key: 'se.cms.componentMenuTemplate',
                nameI18nKey: 'cms.toolbarItem.componentMenuTemplate.name',
                descriptionI18nKey: 'cms.toolbarItem.componentMenuTemplate.description',
                type: 'HYBRID_ACTION',
                callback: function() {
                    systemEventService.sendSynchEvent('ySEComponentMenuOpen', {});
                },
                include: 'web/features/cmssmarteditContainer/componentMenu/componentMenuTemplate.html'
            });

            featureService.register({
                key: 'se.cms.dragAndDrop',
                nameI18nKey: 'se.cms.dragAndDrop.name',
                descriptionI18nKey: 'se.cms.dragAndDrop.description',
                enablingCallback: function() {
                    cmsDragAndDropService.register();
                },
                disablingCallback: function() {
                    cmsDragAndDropService.unregister();
                }
            });

            catalogDetailsService.addItems([{
                include: 'web/features/cmssmarteditContainer/pageList/pageListLinkTemplate.html'
            }]);

            catalogDetailsService.addItems([{
                include: 'web/features/cmssmarteditContainer/synchronize/catalogDetailsSyncTemplate.html'
            }]);

            perspectiveService.register({
                key: 'se.cms.perspective.basic',
                nameI18nKey: 'se.cms.perspective.basic.name',
                descriptionI18nKey: 'se,cms.perspective.basic.description',
                features: ['se.contextualMenu', 'se.cms.dragandropbutton', 'se.cms.remove', 'se.cms.edit', 'se.cms.componentMenuTemplate', 'se.cms.dragAndDrop', 'se.emptySlotFix'],
                perspectives: []
            });

            /* Note: For advance edit mode, the ordering of the entries in the features list will determine the order the buttons will show in the slot contextual menu */
            perspectiveService.register({
                key: 'se.cms.perspective.advanced',
                nameI18nKey: 'se.cms.perspective.advanced.name',
                descriptionI18nKey: 'se.cms.perspective.advanced.description',
                features: ['se.slotContextualMenu', 'se.slotSharedButton', 'se.slotContextualMenuVisibility', 'se.contextualMenu', 'se.cms.dragandropbutton', 'se.cms.remove', 'se.cms.edit', 'se.cms.componentMenuTemplate', 'se.cms.dragAndDrop', 'se.emptySlotFix'],
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
 */
angular.module('componentItemModule', ['assetsServiceModule']).directive('componentItem', ['$log', 'assetsService', function($log, assetsService) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentItemTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,
        scope: {
            componentItem: '='
        },
        link: function(scope, element, attrs) {
            scope.imageRoot = assetsService.getAssetsRoot();
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
 */
angular.module('componentMenuModule', ['componentsModule', 'componentServiceModule', 'assetsServiceModule', 'cmsDragAndDropServiceModule'])
    .controller('ComponentMenuController', ['$log', '$timeout', 'ComponentService', 'cmsDragAndDropService', function($log, $timeout, ComponentService, cmsDragAndDropService) {

        this.types = {};

        ComponentService.loadComponentTypes().then(function() {
            angular.copy(ComponentService.listOfComponentTypes, this.types);
        }.bind(this));

        this.loadComponentItems = function(mask, pageSize, currentPage) {
            return ComponentService.loadPagedComponentItems(mask, pageSize, currentPage).then(function(page) {
                page.results = page.componentItems;
                delete page.componentItems;
                return page;
            });
        };

        this.preventDefault = function(oEvent) {
            oEvent.stopPropagation();
        };

    }])
    .filter('nameFilter', function() {
        return function(components, criteria) {
            var filterResult = [];
            if (!criteria || criteria.length < 3)
                return components;

            criteria = criteria.toLowerCase();
            var criteriaList = criteria.split(" ");

            (components || []).forEach(function(component) {
                var match = true;
                var term = component.name.toLowerCase();

                criteriaList.forEach(function(item) {
                    if (term.indexOf(item) == -1) {
                        match = false;
                        return false;
                    }
                });

                if (match && filterResult.indexOf(component) == -1) {
                    filterResult.push(component);
                }
            });
            return filterResult;
        };
    })
    /**
     * @ngdoc directive
     * @name componentMenuModule.directive:componentMenu
     * @scope
     * @restrict E
     * @element ANY
     *
     * @description
     * Component Menu widget that shows all the component types and customized components.
     */
    .directive('componentMenu', ['$rootScope', 'systemEventService', '$q', '$document', 'assetsService', 'cmsDragAndDropService', 'DRAG_AND_DROP_EVENTS', function($rootScope, systemEventService, $q, $document, assetsService, cmsDragAndDropService, DRAG_AND_DROP_EVENTS) {
        return {
            templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentMenuWidgetTemplate.html',
            restrict: 'E',
            transclude: true,
            $scope: {},
            link: function($scope, elem, attrs) {

                $scope.parentBtn = elem.closest('.ySEHybridAction');

                $scope.status = {
                    isopen: false
                };

                $scope.icons = {
                    closed: ".." + assetsService.getAssetsRoot() + "/images/icon_add.png",
                    open: ".." + assetsService.getAssetsRoot() + "/images/icon_add_s.png"
                };

                $scope.menuIcon = $scope.icons.closed;

                $scope.updateIcon = function() {
                    if ($scope.status.isopen) {
                        $scope.menuIcon = $scope.icons.open;
                        $scope.parentBtn.addClass("ySEOpenComponent");

                    } else {
                        $scope.menuIcon = $scope.icons.closed;
                        $scope.parentBtn.removeClass("ySEOpenComponent");
                    }
                };

                systemEventService.registerEventHandler('ySEComponentMenuClose', function() {
                    $scope.status.isopen = false;
                    return $q.when();
                });

                $scope.$watch('status.isopen', $scope.updateIcon);

                $scope.preventDefault = function(oEvent) {
                    oEvent.stopPropagation();
                };

                $document.on('click', function(event) {

                    if ($(event.target).parents('.ySEComponentMenuW').length <= 0 && $scope.status.isopen) {

                        $scope.status.isopen = false;
                        $scope.$apply();
                    }
                });

                systemEventService.registerEventHandler(DRAG_AND_DROP_EVENTS.DRAG_STARTED, function() {
                    $scope.status.isopen = false;
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
 */
angular.module('componentSearchModule', []).directive('componentSearch', ['iframeClickDetectionService', function(iframeClickDetectionService) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentSearchTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        scope: false,

        link: function(scope, element, attrs) {

            scope.showResetButton = false;

            scope.selectSearch = function(oEvent) {
                oEvent.stopPropagation();
            };
            scope.preventDefault = function(oEvent) {
                oEvent.stopPropagation();
            };
            scope.closeComponentSearch = function(oEvent) {
                if (oEvent) {
                    oEvent.preventDefault();
                    oEvent.stopPropagation();
                }
                scope.status.isopen = false;
            };

            scope.$watch('status.isopen', function() {
                if (!scope.status.isopen) {
                    scope.searchTerm = "";
                }
            });

            scope.$watch('searchTerm', function() {
                scope.showResetButton = scope.searchTerm !== "";
            });

            iframeClickDetectionService.registerCallback('closeComponentSearch', function() {
                scope.closeComponentSearch();
                scope.searchTerm = "";
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
 */
angular.module('componentsModule', ['cmssmarteditContainerTemplates', 'componentSearchModule', 'componentTypeModule', 'componentItemModule', 'eventServiceModule'])

.directive('componentTabs', ['$rootScope', 'systemEventService', '$q', function($rootScope, systemEventService, $q) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentTabsTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        link: function(scope, element, attrs) {

            scope.tabs = [{
                title: 'compomentmenu.tabs.componenttypes',
                disabled: false
            }, {
                title: 'compomentmenu.tabs.customizedcomp',
                disabled: false
            }];

            scope.selectTab = function(oEvent) {
                oEvent.stopPropagation();
            };

            systemEventService.registerEventHandler('ySEComponentMenuOpen', function() {
                scope.searchTerm = "";
                scope.activateItemsTab(0);

                return $q.when(); //temporary as we don't need to return anything
            });

            scope.focusOnSearch = function(oEvent) {
                if (scope.searchTerm && scope.searchTerm.length > 1) {
                    scope.activateItemsTab(1);
                }
            };

            scope.resetSearch = function(oEvent) {
                oEvent.stopPropagation();
                scope.activateItemsTab(0);

            };

            scope.activateItemsTab = function(iTab) {
                scope.tabs[iTab].active = true;
            };

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
 */
angular.module('componentTypeModule', []).directive('componentType', ['$log', 'domain', function($log, domain) {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/componentMenu/componentTypeTemplate.html',
        restrict: 'E',
        transclude: false,
        replace: true,

        link: function(scope, element, attrs) {
            scope.imageRoot = domain + "/cmssmartedit";
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
 */
/**
 * @ngdoc overview
 * @name cmsDragAndDropServiceModule
 * @description
 * # The cmsDragAndDropServiceModule
 *
 * The CMS Drag and Drop Service Module provides a service that extends the base drag and drop implementation to enable CMS specific features.
 */
angular.module('cmsDragAndDropServiceModule', ['translationServiceModule', 'dragAndDropServiceModule', 'restServiceFactoryModule', 'componentServiceModule', 'editorModalServiceModule', 'eventServiceModule', 'renderServiceModule', 'alertServiceModule', 'removeComponentServiceModule', 'componentHandlerServiceModule'])
    .constant('DRAG_AND_DROP_EVENTS', {
        DRAG_STARTED: 'CMS_DRAG_STARTED',
        DRAG_STOPPED: 'CMS_DRAG_STOPPED'
    })
    /**
     * @ngdoc service
     * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService
     *
     * @description
     * Service that extends the base SmartEdit drag and drop implementation to enable CMS specific features.
     */
    .factory('cmsDragAndDropService', ['$rootScope', '$log', '$translate', 'dragAndDropService', 'restServiceFactory', 'parseQuery', 'hitch', 'ComponentService', 'editorModalService', 'systemEventService', 'renderService', 'alertService', 'removeComponentService', 'domain', 'componentHandlerService', 'CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function($rootScope, $log, $translate, dragAndDropService, restServiceFactory, parseQuery, hitch, ComponentService, editorModalService, systemEventService, renderService, alertService, removeComponentService, domain, componentHandlerService, CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {

        // Constants
        var CMS_DRAG_AND_DROP_ID = "se.cms.dragAndDrop";

        var slotDetailRestService = restServiceFactory.get(CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI);

        var slotUpdateRestService = restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI + '?pageId=:pageId&slotId=:currentSlotId&componentId=:componentId', 'componentId');

        var _getSelector = function(selector) {
            return $(selector);
        };

        var _getElementChild = function(element, childIndex) {
            return element.children()[childIndex];
        };

        var _removeDynamicStyles = function(dndService, targetSelector) {
            var contentSlots = dndService.getSelectorInIframe(targetSelector);
            contentSlots.removeClass("over-slot-enabled");
            contentSlots.removeClass("over-slot-disabled");
            contentSlots.removeClass("ySEemptySlot");

            var aSlots = dndService.getSelectorInIframe('.ySEDirtyHeight');
            aSlots.removeClass('ySEDirtyHeight');
            aSlots.height(0);
        };

        var _generateDragAndDropErrorMessage = function(sourceComponentId, targetSlotId, requestResponse) {
            var detailedError = (requestResponse.data && requestResponse.data.errors && requestResponse.data.errors.length > 0) ?
                requestResponse.data.errors[0].message : "";

            return $translate.instant('cmsdraganddrop.error', {
                sourceComponentId: sourceComponentId,
                targetSlotId: targetSlotId,
                detailedError: detailedError
            });
        };

        /**
         * @ngdoc method
         * @name cmsDragAndDropServiceModule.service:cmsDragAndDropService#register
         * @methodOf cmsDragAndDropServiceModule.service:cmsDragAndDropService
         *
         * @description
         * Configures and enables a CMS specific implementation of the drag and drop service in the current page.
         *
         */
        var register = function() {

            var targetSelector = componentHandlerService.getAllSlotsSelector();

            dragAndDropService.register({
                id: CMS_DRAG_AND_DROP_ID,
                sourceSelector: componentHandlerService.getAllComponentsSelector(),
                targetSelector: targetSelector,
                stopCallback: hitch(this, function(dndService, event, ui) {
                    // Remove any slot styling left (due to 'out' events not triggered properly)
                    this._removeDynamicStyles(dndService, targetSelector);
                    renderService.toggleOverlay(true);
                    renderService.renderPage(true);
                }, dragAndDropService),
                startCallback: hitch(this, function(dndService, event, ui) {

                    // First hide the overlay. Otherwise overlays will prevent correct operation from happening.
                    renderService.toggleOverlay(false);
                    this.sourceSlotId = componentHandlerService.getId(this._getSelector(event.currentTarget));

                    systemEventService.sendSynchEvent('ySEComponentMenuClose', {});

                }, dragAndDropService),
                overCallback: hitch(this, function(dndService, event, ui) {
                    // The provided ui.item is the helper. Thus, to get the SmartEdit information it's necessary to retrieve
                    // it from the wrapped element.
                    var wrappedElement = this._getSelector(ui.item, 0);
                    if (!componentHandlerService.getSlotOperationRelatedType(wrappedElement)) {
                        wrappedElement = this._getSelector(this._getElementChild(ui.item, 0));
                    }

                    var sourceComponentType = componentHandlerService.getSlotOperationRelatedType(wrappedElement);
                    var targetSlotId = componentHandlerService.getId(this._getSelector(event.target));
                    slotDetailRestService.get({
                        pageUid: componentHandlerService.getPageUID(),
                        slotUid: targetSlotId
                    }).then(hitch(this, function(response) {

                        var slotIsEnabled = response.validComponentTypes.indexOf(sourceComponentType) > -1;

                        var element = this._getSelector(event.target);
                        var translation;

                        if (slotIsEnabled) {
                            dndService.getSelectorInIframe('.ySEIconBckg').switchClass('ySEDnDImageRed', 'ySEDnDImageBlue');
                            translation = $translate.instant('cmsdraganddrop.drophere');
                            var placeholder = element.children(this._getSelector('ySEDnDPlaceHolder'));
                            element.addClass("over-slot-enabled");
                            placeholder.attr('data-content', translation);
                        } else {
                            dndService.getSelectorInIframe('.ySEIconBckg').switchClass('ySEDnDImageBlue', 'ySEDnDImageRed');
                            translation = $translate.instant('cmsdraganddrop.notallowed');
                            element.addClass("over-slot-disabled");
                            element.attr('data-content', translation);
                        }
                    }), function() {
                        $log.error("failed to retrieve slot details for slot ");
                    });
                }, dragAndDropService),
                dropCallback: hitch(this, function(event, ui, cancel) {
                    // Unlike the overCallback, the ui.item for this callback item is not the helper, but the actual
                    // element. It should directly have the necessary SmartEdit information.
                    var sourceComponentId = componentHandlerService.getSlotOperationRelatedId(this._getSelector(ui.item));
                    var targetSlotId = componentHandlerService.getId(this._getSelector(event.target));
                    var sourceComponentType = componentHandlerService.getSlotOperationRelatedType(this._getSelector(ui.item));
                    if (this._getSelector(event.target).hasClass("over-slot-disabled")) {
                        cancel();
                        var translation = $translate.instant("drag.and.drop.not.valid.component.type", {
                            slotUID: targetSlotId,
                            componentUID: sourceComponentId
                        });
                        alertService.pushAlerts([{
                            successful: false,
                            message: translation,
                            closeable: true
                        }]);
                    } else if (this._getSelector(event.target).hasClass('over-slot-enabled')) { //after a cancel, dropCallback is called again but nothing must be done and nothing must prevent it

                        var cmsDragAndDropService = this; // Add variable to avoid excessive hitch nesting.

                        if (this.sourceSlotId === undefined) {
                            // Add new component from type
                            if (sourceComponentId === undefined) {
                                ComponentService.addNewComponent("name", sourceComponentType, componentHandlerService.getPageUID(), targetSlotId, ui.item.index()).then(function(response) {
                                    sourceComponentId = response.uid;
                                    componentHandlerService.setId(ui.item, sourceComponentId);
                                    editorModalService.open(sourceComponentType, sourceComponentId).then(
                                        function() {},
                                        function(response) {
                                            removeComponentService.removeComponent({
                                                slotId: targetSlotId,
                                                componentType: sourceComponentType,
                                                slotOperationRelatedId: sourceComponentId,
                                                componentId: sourceComponentId
                                            }).then(function() {}, function() {
                                                var errorMessage = cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response);
                                                alertService.pushAlerts([{
                                                    successful: false,
                                                    message: errorMessage,
                                                    closeable: true
                                                }]);
                                            });
                                            cancel();
                                        });
                                }, function(response) {
                                    var errorMessage = cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response);
                                    alertService.pushAlerts([{
                                        successful: false,
                                        message: errorMessage,
                                        closeable: true
                                    }]);
                                    cancel();
                                });

                            } else { // Add exisiting component
                                ComponentService.addExistingComponent(
                                    componentHandlerService.getPageUID(),
                                    sourceComponentId,
                                    targetSlotId,
                                    ui.item.index()
                                ).then(function(response) {
                                    renderService.renderSlots(targetSlotId);
                                }, function(response) {
                                    var errorMessage = cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response);
                                    alertService.pushAlerts([{
                                        successful: false,
                                        message: errorMessage,
                                        closeable: true
                                    }]);
                                    cancel();
                                });
                            }
                        } else {
                            slotUpdateRestService.update({
                                pageId: componentHandlerService.getPageUID(),
                                currentSlotId: this.sourceSlotId,
                                componentId: sourceComponentId,
                                slotId: targetSlotId,
                                position: ui.item.index()
                            }).then(hitch(this, function(response) {
                                renderService.renderSlots([this.sourceSlotId, targetSlotId]);
                            }), function(response) {
                                var errorMessage = (response === undefined) ? $translate.instant("cmsdraganddrop.move.failed", {
                                    slotID: targetSlotId,
                                    componentID: sourceComponentId
                                }) : cmsDragAndDropService._generateErrorMessage(sourceComponentId, targetSlotId, response);
                                alertService.pushAlerts([{
                                    successful: false,
                                    message: errorMessage,
                                    closeable: true
                                }]);
                                cancel();
                            });
                        }

                    } else {
                        // No longer hovering over the content slot
                        cancel();
                    }
                }),
                outCallback: hitch(this, function(event, ui) {
                    this._getSelector(ui.helper).removeClass("over-active");
                    this._getSelector(event.target).removeClass("over-slot-enabled");
                    this._getSelector(event.target).removeClass("over-slot-disabled");
                }),
                helper: function(event, targetSelector, originalItem) {
                    return "<img class='ySEIconBckg' src='" + domain + "/cmssmartedit/images/component_default.png'/>";
                }
            });

        };

        var unregister = function() {
            dragAndDropService.unregister([CMS_DRAG_AND_DROP_ID]);
        };

        return {
            register: register,
            unregister: unregister,
            _getSelector: _getSelector,
            _getElementChild: _getElementChild,
            _removeDynamicStyles: _removeDynamicStyles,
            _generateErrorMessage: _generateDragAndDropErrorMessage
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
/**
 * @ngdoc overview
 * @name editorModalServiceModule
 * @description
 * # The editorModalServiceModule
 *
 * The editor modal service module provides a service that allows opening an editor modal for a given component type and
 * component ID. The editor modal is populated with a save and cancel button, and is loaded with the {@link
 * editorTabsetModule.directive:editorTabset editorTabset} as its content, providing a way to edit
 * various fields of the given component.
 */
angular.module('editorModalServiceModule', ['modalServiceModule', 'gatewayProxyModule', 'coretemplates', 'editorTabsetModule', 'confirmationModalServiceModule'])

/**
 * @ngdoc service
 * @name editorModalServiceModule.service:editorModalService
 *
 * @description
 * Convenience service to open an editor modal window for a given component type and component ID.
 *
 * Example: A button is bound to the function '$scope.onClick' via the ng-click directive. Clicking the button will
 * trigger the opening of an editor modal for a CMSParagraphComponent with the ID 'termsAndConditionsParagraph'
 *
 * <pre>
 angular.module('app', ['editorModalServiceModule'])
 .controller('someController', function($scope, editorModalService) {
            $scope.onClick = function() {
                editorModalService.open('CMSParagraphComponent', 'termsAndConditionsParagraph');
            };
        });
 * </pre>
 */
.factory('editorModalService', ['modalService', 'MODAL_BUTTON_ACTIONS', 'MODAL_BUTTON_STYLES', 'systemEventService', 'gatewayProxy', 'renderService', 'confirmationModalService', '$q', '$log', function(modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES, systemEventService, gatewayProxy, renderService, confirmationModalService, $q, $log) {
        function EditorModalService() {
            this.gatewayId = 'EditorModal';
            gatewayProxy.initForService(this, ["open", "openAndRerenderSlot"]);
        }

        var _openEditorModalWithSaveCallback = function(componentType, componentId, saveCallback) {
            return modalService.open({
                title: 'type.' + componentType.toLowerCase() + '.name',
                titleSuffix: 'editor.title.suffix',
                templateInline: '<editor-tabset control="modalController.tabsetControl" model="modalController.componentData"></editor-tabset>',
                controller: ['editorModalService', 'modalManager', 'systemEventService', 'hitch', '$scope', '$log', '$q',
                    function(editorModalService, modalManager, systemEventService, hitch, $scope, $log, $q) {
                        this.isDirty = false;

                        this.componentData = {
                            componentId: componentId,
                            componentType: componentType
                        };

                        this.onSave = function() {
                            return this.tabsetControl.saveTabs().then(function(item) {
                                saveCallback();
                                return item;
                            });
                        };

                        this.onCancel = function() {
                            var deferred = $q.defer();
                            if (this.isDirty) {
                                confirmationModalService.confirm({
                                    description: 'editor.cancel.confirm'
                                }).then(hitch(this, function() {
                                    this.tabsetControl.cancelTabs().then(function() {
                                        deferred.resolve();
                                    }, function() {
                                        deferred.reject();
                                    });
                                }), function() {
                                    deferred.reject();
                                });
                            } else {
                                deferred.resolve();
                            }

                            return deferred.promise;
                        };

                        this.init = function() {
                            modalManager.setDismissCallback(hitch(this, this.onCancel));

                            modalManager.setButtonHandler(hitch(this, function(buttonId) {
                                switch (buttonId) {
                                    case 'save':
                                        return this.onSave();
                                    case 'cancel':
                                        return this.onCancel();
                                    default:
                                        $log.error('A button callback has not been registered for button with id', buttonId);
                                        break;
                                }
                            }));

                            $scope.$watch(hitch(this, function() {
                                var isDirty = this.tabsetControl && typeof this.tabsetControl.isDirty === 'function' && this.tabsetControl.isDirty();
                                return isDirty;
                            }), hitch(this, function(isDirty) {
                                if (typeof isDirty === 'boolean') {
                                    if (isDirty) {
                                        this.isDirty = true;
                                        modalManager.enableButton('save');
                                    } else {
                                        this.isDirty = false;
                                        modalManager.disableButton('save');
                                    }
                                }
                            }));
                        };
                    }
                ],
                buttons: [{
                    id: 'cancel',
                    label: 'compoment.confirmation.modal.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY,
                    action: MODAL_BUTTON_ACTIONS.DISMISS
                }, {
                    id: 'save',
                    label: 'component.confirmation.modal.save',
                    action: MODAL_BUTTON_ACTIONS.CLOSE,
                    disabled: true
                }]
            });
        };

        /**
         * @ngdoc method
         * @name editorModalServiceModule.service:editorModalService#open
         * @methodOf editorModalServiceModule.service:editorModalService
         *
         * @description
         * Uses the {@link modalServiceModule.modalService modalService} to open an editor modal.
         *
         * The editor modal is initialized with a title in the format '<TypeName> Editor', ie: 'Paragraph Editor'. The
         * editor modal is also wired with a save and cancel button.
         *
         * The content of the editor modal is the {@link editorTabsetModule.directive:editorTabset editorTabset}.
         *
         * @param {String} componentType The type of component as defined in the platform.
         * @param {String} componentId The ID of the component as defined in the database.
         *
         * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
         */
        EditorModalService.prototype.open = function(componentType, componentId) {
            return _openEditorModalWithSaveCallback(componentType, componentId, function() {
                renderService.renderComponent(componentId, componentType);
            });
        };

        /**
         * @ngdoc method
         * @name editorModalServiceModule.service:editorModalService#openAndRerenderSlot
         * @methodOf editorModalServiceModule.service:editorModalService
         *
         * @description
         * Uses the {@link modalServiceModule.modalService modalService} to open an editor modal.
         *
         * The editor modal is initialized with a title in the format '<TypeName> Editor', ie: 'Paragraph Editor'. The
         * editor modal is also wired with a save and cancel button.
         *
         * The content of the editor modal is the {@link editorTabsetModule.directive:editorTabset editorTabset}.
         *
         * @param {String} componentType The type of component as defined in the platform.
         * @param {String} componentId The ID of the component as defined in the database.
         * @param {String} slotId The ID of the slot as defined in the database.
         *
         * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
         */
        EditorModalService.prototype.openAndRerenderSlot = function(componentType, componentId, slotId) {
            return _openEditorModalWithSaveCallback(componentType, componentId, function() {
                renderService.renderSlots(slotId);
            });
        };

        return new EditorModalService();
    }])
    .run(['editorModalService', function(editorModalService) {
        // Trigger instantiation of editorModalService
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
(function() {
    var DIALOG_SAVE_EVENT = "sm-editor-save";
    var DIALOG_CANCEL_EVENT = "sm-editor-cancel";
    var DIALOG_RESET_EVENT = "sm-editor-reset";
    var DIALOG_IS_DIRTY_EVENT = "sm-editor-is-dirty";

    /**
     * @ngdoc overview
     * @name editorTabsetModule
     * @description
     *
     * The editor tabset module contains the service and the directives necessary to organize and display editing forms
     * in tabs.
     *
     * Use the {@link editorTabsetModule.service:editorTabsetService editorTabsetService} to manage the list of
     * tabs that will be present in the tabset.
     *
     * Use the {@link editorTabsetModule.directive:editorTabset editorTabset} directive to display editing
     * forms organized as tabs within a tabset (it will contain the tabs registered in the editorTabsetService).
     *
     */
    angular.module('editorTabsetModule', ['ui.bootstrap', 'functionsModule', 'experienceInterceptorModule', 'tabsetModule', 'eventServiceModule', 'cmssmarteditContainerTemplates', 'adminTabModule', 'basicTabModule', 'visibilityTabModule', 'genericTabModule'])

    /**
     * @ngdoc service
     * @name editorTabsetModule.service:editorTabsetService
     * @description
     *
     * The Editor Tabset Service keeps track of a list of tabs and their configuration, which includes the id, title,
     * and template  URL. The service allows registering, removing, and listing tabs. By default it contains the following ones:
     * - Admin Tab
     * - Basic Tab
     * - Generic Tab
     *
     * This service is used by the {@link editorTabsetModule.directive:editorTabset editorTabset}
     * directive to determine the tabs to be displayed in the tabset.
     */
    .factory('editorTabsetService', ['copy', function(copy) {
        var EditorTabsetService = function() {
            this.tabsList = [];

            // Adds basic services
            this.registerTab('genericTab', 'editortabset.generictab.title',
                'web/features/cmssmarteditContainer/editorTabset/tabs/generic/genericTabTemplate.html');
            this.registerTab('basicTab', 'editortabset.basictab.title',
                'web/features/cmssmarteditContainer/editorTabset/tabs/basic/basicTabTemplate.html');
            this.registerTab('adminTab', 'editortabset.admintab.title',
                'web/features/cmssmarteditContainer/editorTabset/tabs/admin/adminTabTemplate.html');
            this.registerTab('visibilityTab', 'editortabset.visibilitytab.title',
                'web/features/cmssmarteditContainer/editorTabset/tabs/visibility/visibilityTabTemplate.html');
        };

        /**
         * @ngdoc method
         * @name editorTabsetModule.service:editorTabsetService#registerTab
         * @methodOf editorTabsetModule.service:editorTabsetService
         *
         * @description
         * This method allows registering a tab to be displayed in an editing tabset.
         *
         * Note: If the tabId is not unique, this method will override the previous tab configuration.
         *
         * @param {String} tabId An ID for the tab. It has to be unique, otherwise the previous tab configuration will be overriden.
         * @param {String} tabTitle The string displayed as the tab header.
         * @param {String} tabTemplateUrl Path to the HTML fragment to be displayed as the tab content.
         */
        EditorTabsetService.prototype.registerTab = function(tabId, tabTitle, tabTemplateUrl) {
            this._validateTab(tabId, tabTitle, tabTemplateUrl);

            this.tabsList.push({
                id: tabId,
                title: tabTitle,
                templateUrl: tabTemplateUrl,
                hasErrors: false
            });
        };

        EditorTabsetService.prototype._validateTab = function(tabId, tabTitle, tabTemplateUrl) {
            if (!tabId) {
                throw new Error("editorTabsetService.registerTab.invalidTabID");
            }

            if (!tabTitle) {
                throw new Error("editorTabsetService.registerTab.missingTabTitle");
            }

            if (!tabTemplateUrl) {
                throw new Error("editorTabsetService.registerTab.missingTemplateUrl");
            }
        };

        /**
         * @ngdoc method
         * @name editorTabsetModule.service:editorTabsetService#deleteTab
         * @methodOf editorTabsetModule.service:editorTabsetService
         *
         * @description
         * Removes a tab from the list of registered tabs.
         *
         * @param {String} tabId The ID of the tab to be removed.
         */
        EditorTabsetService.prototype.deleteTab = function(tabId) {
            var tabIndex = getIndexOfElementByAttr(this.tabsList, 'id', tabId);

            if (tabIndex >= 0) {
                delete this.tabsList[tabIndex];
            } else {
                throw new Error("editorTabsetService.deleteTab.tabNotFound");
            }
        };

        /**
         * @ngdoc method
         * @name editorTabsetModule.service:editorTabsetService#getTabsList
         * @methodOf editorTabsetModule.service:editorTabsetService
         *
         * @description
         * Retrieves the list of registered tabs to display in an editing tabset.
         *
         * @returns {Array} array containing the registered tabs to display in an editing tabset.
         */
        EditorTabsetService.prototype.getTabsList = function() {
            return copy(this.tabsList);
        };

        return new EditorTabsetService();
    }])

    /**
     * @ngdoc directive
     * @name editorTabsetModule.directive:editorTabset
     * @scope
     * @restrict E
     * @element smartedit-editor-tabset
     *
     * @description
     * Directive responsible for displaying and organizing editing forms as tabs within a tabset.
     *
     * The directive allows communication with it via the control object. To do so, it exposes several methods
     * (saveTabs, resetTabs, and cancelTabs). When called, the directive will delegate the corresponding operation to
     * each of the tabs, which are represented internally as SmartEdit yTab directives.
     *
     * @param {Object} control The object that enables communication with the directive itself. It exposes the
     * following methods:
     * @param {Function} control.saveTabs Instructs internal tabs to execute their onSave callback, where they shall
     * validate and persist their changes. If a tab is unable to complete the save operation successfully, the tabset
     * will display an error in the tab's header.
     * @param {Function} control.resetTabs Instructs internal tabs to execute their onReset callback, where they shall
     * discard their modifications and return to a pristine state. It also clears all errors in the tabset.
     * @param {Function} control.cancelTabs Instructs internal tabs to execute their onCancel callback, which allows them to discard
     * their modifications and clean up as necessary. It also clears all errors in the tabset.
     * @param {Boolean} control.isDirty States whether one or more tabs are not in pristine state (e.g., have been modified).
     * @param {Object} data The custom data to pass to each of the individual tabs. When used within the editor modal
     * it must contain component information, such as the componentID and the componentType.
     *
     */
    .directive('editorTabset', ['$q', '$log', 'systemEventService', 'editorTabsetService', 'merge', function($q, $log, systemEventService, editorTabsetService, merge) {
        return {
            restrict: 'E',
            transclude: false,
            templateUrl: 'web/features/cmssmarteditContainer/editorTabset/editorTabsetTemplate.html',
            scope: {
                control: '=',
                model: '='
            },
            link: function(scope, elem, attr) {
                var isDirty = false;

                var removeTabErrors = function() {
                    for (var tabKey in scope.tabsList) {
                        scope.tabsList[tabKey].hasErrors = false;
                    }
                };

                scope.tabsList = editorTabsetService.getTabsList();
                scope.numTabsDisplayed = 6; //it includes more tab

                scope.model.onStructureResolved = function(tabId, structure) {
                    if (tabId && structure && structure.attributes.length === 0) {
                        scope.tabsList = editorTabsetService.getTabsList().filter(function(tab) {
                            return tab.id !== tabId;
                        });
                    }
                };

                scope.control = {

                    saveTabs: function() {
                        var result = {
                            errorsList: [],
                            item: {},
                            operationSuccessful: true
                        };
                        var errorsList = [];
                        var item = {};
                        var deferred = $q.defer();

                        systemEventService.sendAsynchEvent(DIALOG_SAVE_EVENT, result)
                            .then(function() {
                                removeTabErrors();

                                for (var idx in result.errorsList) {
                                    var tabId = result.errorsList[idx];
                                    var tabIndex = getIndexOfElementByAttr(scope.tabsList, "id", tabId);

                                    if (tabIndex >= 0) {
                                        scope.tabsList[tabIndex].hasErrors = true;
                                    }
                                }

                                if (result.operationSuccessful) {
                                    deferred.resolve(result.item);
                                } else {
                                    deferred.reject();
                                }
                            });

                        return deferred.promise;
                    },

                    resetTabs: function() {
                        return systemEventService.sendAsynchEvent(DIALOG_RESET_EVENT)
                            .then(function() {
                                removeTabErrors();
                            });
                    },

                    cancelTabs: function() {
                        return systemEventService.sendAsynchEvent(DIALOG_CANCEL_EVENT)
                            .then(function() {
                                removeTabErrors();
                            });
                    },

                    isDirty: function() {
                        return isDirty;
                    }
                };

                var dirtyBook = {};

                var onIsDirty = function(eventId, newTabEntry) {
                    dirtyBook[newTabEntry.tabId] = newTabEntry.isDirty;
                    for (var tabId in dirtyBook) {
                        if (dirtyBook[tabId]) {
                            isDirty = true;
                            return $q.when(true);
                        }
                    }
                    isDirty = false;
                    return $q.when(true);
                };

                //when outside of a DIALOG_SAVE_EVENT event, an editor with a tab may require to be marked in error
                systemEventService.registerEventHandler("EDITOR_IN_ERROR_EVENT", function(key, tabId) {
                    var tabIndex = getIndexOfElementByAttr(scope.tabsList, "id", tabId);
                    if (tabIndex >= 0) {
                        scope.tabsList[tabIndex].hasErrors = true;
                    }
                    return $q.when();
                });


                systemEventService.registerEventHandler(DIALOG_IS_DIRTY_EVENT, onIsDirty);

                scope.$on('$destroy', function() {
                    systemEventService.unRegisterEventHandler(DIALOG_IS_DIRTY_EVENT, onIsDirty);
                });

                scope.tabControl = function(tabScope) {
                    var onSave = function(event, result) {
                        return $q.when(function() {
                            if (tabScope.onSave) {
                                return tabScope.onSave()
                                    .then(function(data) {
                                        result.item = merge(result.item, data);
                                    }, function(showError) {
                                        if (showError) {
                                            result.errorsList.push(tabScope.tabId);
                                        }

                                        result.operationSuccessful = false;

                                        return showError;
                                    });
                            } else {
                                $log.warn("Cannot save tab", tabScope.tabId, ". Save callback not defined.");
                            }
                        }());
                    };

                    var onReset = function(event, result) {
                        return $q.when(function() {
                            if (tabScope.onReset) {
                                return tabScope.onReset()
                                    .then(function(data) {
                                        return data;
                                    }, function(error) {
                                        return error;
                                    });
                            } else {
                                $log.warn("Cannot reset tab", tabScope.tabId, ". Reset callback not defined.");
                            }
                        }());
                    };

                    var onCancel = function(event, result) {
                        return $q.when(function() {
                            if (tabScope.onCancel) {
                                return tabScope.onCancel()
                                    .then(function(data) {
                                        return data;
                                    }, function(error) {
                                        return error;
                                    });
                            } else {
                                $log.warn("Cannot cancel tab", tabScope.tabId, ". Cancel callback not defined.");
                            }
                        }());
                    };

                    tabScope.$watch(function() {
                        return typeof tabScope.isDirty === 'function' && tabScope.isDirty();
                    }, function(currentDirtyState, oldDirtyState) {
                        if (typeof currentDirtyState === 'boolean' && currentDirtyState !== oldDirtyState) {
                            systemEventService.sendAsynchEvent(DIALOG_IS_DIRTY_EVENT, {
                                tabId: tabScope.tabId,
                                isDirty: currentDirtyState
                            });
                        }
                    });

                    systemEventService.registerEventHandler(DIALOG_SAVE_EVENT, onSave);
                    systemEventService.registerEventHandler(DIALOG_RESET_EVENT, onReset);
                    systemEventService.registerEventHandler(DIALOG_CANCEL_EVENT, onCancel);

                    tabScope.$on('$destroy', function() {
                        systemEventService.unRegisterEventHandler(DIALOG_SAVE_EVENT, onSave);
                        systemEventService.unRegisterEventHandler(DIALOG_RESET_EVENT, onReset);
                        systemEventService.unRegisterEventHandler(DIALOG_CANCEL_EVENT, onCancel);
                    });
                };
            }
        };
    }]);

    function getIndexOfElementByAttr(arr, attr, value) {
        for (var i = 0; i < arr.length; i++) {
            if (arr[i][attr] == value) {
                return i;
            }
        }

        return -1;
    }
})();

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
    angular.module('adminTabModule', ['genericEditorModule', 'resourceLocationsModule'])
        .directive('adminTab', ['$log', '$q', 'ITEMS_RESOURCE_URI', function($log, $q, ITEMS_RESOURCE_URI) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {

                    scope.tabStructure = [{
                        cmsStructureType: "ShortString",
                        qualifier: "uid",
                        i18nKey: 'type.cmsitem.uid.name',
                        editable: false
                    }, {
                        cmsStructureType: "ShortString",
                        qualifier: "pk",
                        i18nKey: 'type.item.pk.name',
                        editable: false
                    }];
                    scope.contentApi = ITEMS_RESOURCE_URI;
                }
            };
        }]);
})();

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
    angular.module('basicTabModule', ['genericEditorModule', 'resourceLocationsModule'])
        .directive('basicTab', ['$log', '$q', 'ITEMS_RESOURCE_URI', function($log, $q, ITEMS_RESOURCE_URI) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {
                    scope.tabStructure = [{
                        cmsStructureType: "ShortString",
                        qualifier: "name",
                        i18nKey: 'type.cmsitem.name.name',
                        required: true
                    }, {
                        cmsStructureType: "Date",
                        qualifier: "creationtime",
                        i18nKey: 'type.item.creationtime.name',
                        editable: false
                    }, {
                        cmsStructureType: "Date",
                        qualifier: "modifiedtime",
                        i18nKey: 'type.item.modifiedtime.name',
                        editable: false
                    }];
                    scope.contentApi = ITEMS_RESOURCE_URI;
                }
            };
        }]);
})();

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
    angular.module('genericTabModule', ['genericEditorModule', 'resourceLocationsModule'])
        .directive('genericTab', ['$log', '$q', 'TYPES_RESOURCE_URI', 'ITEMS_RESOURCE_URI', function($log, $q, TYPES_RESOURCE_URI, ITEMS_RESOURCE_URI) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/generic/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    onStructureResolved: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {
                    scope.structureApi = TYPES_RESOURCE_URI + '/:smarteditComponentType';
                    scope.contentApi = ITEMS_RESOURCE_URI;
                }
            };
        }]);
})();

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
    angular.module('visibilityTabModule', ['genericEditorModule', 'resourceLocationsModule'])
        .directive('visibilityTab', ['$log', '$q', 'ITEMS_RESOURCE_URI', function($log, $q, ITEMS_RESOURCE_URI) {
            return {
                restrict: 'E',
                transclude: false,
                templateUrl: 'web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
                scope: {
                    saveTab: '=',
                    resetTab: '=',
                    cancelTab: '=',
                    isDirtyTab: '=',
                    componentId: '=',
                    componentType: '=',
                    tabId: '='
                },
                link: function(scope, elem, attr) {
                    scope.tabStructure = [{
                        cmsStructureType: "Boolean",
                        qualifier: "visible",
                        prefixText: 'visible.prefix.text',
                        labelText: 'visible.postfix.text'
                    }];
                    scope.contentApi = ITEMS_RESOURCE_URI;
                }
            };
        }]);
})();

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
 * @name pageListModule
 * @description
 *
 * The page list module contains the controller associated to the page list view.
 *
 * This view displays the list of pages for a specific catalog and allows the user
 * to search and sort the list.
 *
 * Use the {@link pageListServiceModule.service:pageListService pageListService}
 * to call backend API in order to get the list of pages for a specific catalog
 *
 */
angular.module('pageListControllerModule', ['pageListServiceModule', 'functionsModule', 'resourceLocationsModule', 'addPageServiceModule', 'experienceServiceModule', 'eventServiceModule', 'resourceLocationsModule'])

/**
 * @ngdoc controller
 * @name pageListModule.controller:pageListController
 *
 * @description
 * The page list controller fetches pages for a specified catalog using the {@link pageListServiceModule.service:pageListService pageListService}
 */
.controller('pageListController', ['$scope', '$routeParams', '$location', 'STOREFRONT_PATH_WITH_PAGE_ID', 'hitch', 'pageListService', 'catalogService', 'addPageWizardService', 'experienceService', 'sharedDataService', 'systemEventService', 'LANDING_PAGE_PATH', function($scope, $routeParams, $location, STOREFRONT_PATH_WITH_PAGE_ID, hitch, pageListService, catalogService, addPageWizardService, experienceService, sharedDataService, systemEventService, LANDING_PAGE_PATH) {

    this.siteUID = $routeParams.siteId;
    this.catalogId = $routeParams.catalogId;
    this.catalogVersion = $routeParams.catalogVersion;
    this.pages = [];
    this.catalogName = "";
    this.query = {
        value: ""
    };
    var filteredCatalog = [];

    this.reloadPages = function() {
        pageListService.getPageListForCatalog(this.siteUID, this.catalogId, this.catalogVersion).then(function(pageListResponse) {
            this.pages = pageListResponse.pages.map(function(page) {
                return {
                    name: page.name,
                    uid: page.uid,
                    typeCode: page.typeCode,
                    template: page.template
                };
            });
        }.bind(this));
    };
    this.reloadPages();

    catalogService.getCatalogsForSite(this.siteUID).then(function(catalogs) {

        filteredCatalog = catalogs.filter(hitch(this, function(catalog) {
            return catalog.catalogVersion === this.catalogVersion;
        }));

        if (filteredCatalog.length == 1) {
            this.catalogName = filteredCatalog[0].name;
        }

    }.bind(this));

    // renderers Object that contains custom HTML renderers for a given key
    this.renderers = {
        name: function(item, key) {
            return "<a data-ng-click=\"injectedContext.onLink( item.uid )\">{{ item[key.property] }}</a>";
        }
    };

    // injectedContext Object. This object is passed to the client-paged-list directive.
    this.injectedContext = {
        onLink: function(uid) {
            if (uid) {
                var experiencePath = this._buildExperiencePath(uid);
                //iFrameManager.setCurrentLocation(link);
                $location.path(experiencePath);
            }
        }.bind(this)
    };

    //needed for genericEditor invoked in addPageWizard
    experienceService.buildDefaultExperience($routeParams).then(function(experience) {
        sharedDataService.set('experience', experience).then(function(experience) {
            systemEventService.sendAsynchEvent("experienceUpdate");
        }, function(buildError) {
            $log.error("the provided path could not be parsed: " + $location.url());
            $log.error(buildError);
            $location.url(LANDING_PAGE_PATH);
        });
    });


    this._buildExperiencePath = function(uid) {
        return STOREFRONT_PATH_WITH_PAGE_ID
            .replace(":siteId", this.siteUID)
            .replace(":catalogId", this.catalogId)
            .replace(":catalogVersion", this.catalogVersion)
            .replace(":pageId", uid);
    };

    this.openAddPageWizard = function() {
        addPageWizardService.openAddPageWizard().then(function() {
            this.reloadPages();
        }.bind(this));
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
(function() {
    /**
     * @ngdoc overview
     * @name pageListLinkModule
     * @description
     * # The pageListLinkModule
     *
     * The pageListLinkModule provides a directive to display a link to the list of pages of a catalogue
     *
     */
    angular.module('pageListLinkModule', [])
        /**
         * @ngdoc directive
         * @name pageListLinkModule.directive:pageListLink
         * @scope
         * @restrict E
         * @element <page-list-link></page-list-link>
         *
         * @description
         * Directive that displays a link to the list of pages of a catalogue. It can only be used if catalog.catalogVersions is in the current scope.
         */
        .directive('pageListLink', function() {
            return {
                templateUrl: 'web/features/cmssmarteditContainer/pageList/pageListLinkDirectiveTemplate.html',
                restrict: 'E',
                transclude: true,
                $scope: {},
                link: function($scope, elem, attrs) {}
            };
        });
})();

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
 * @name pageListServiceModule
 * @description
 * # The pageListServiceModule
 *
 * The Page List Service module provides a service that fetches pages for a specified catalog
 *
 */
angular.module('pageListServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])

/**
 * @ngdoc service
 * @name pageListServiceModule.service:pageListService
 *
 * @description
 * The Page List Service fetches pages for a specified catalog using REST calls to the cmswebservices pages API.
 */
.factory('pageListService', ['PAGES_LIST_RESOURCE_URI', 'restServiceFactory', '$q', function(PAGES_LIST_RESOURCE_URI, restServiceFactory, $q) {
    var pageRestService = restServiceFactory.get(PAGES_LIST_RESOURCE_URI);

    return {
        /**
         * @ngdoc method
         * @name pageListServiceModule.service:pageListService#getPageListForCatalog
         * @methodOf pageListServiceModule.service:pageListService
         *
         * @description
         * Fetches a list of pages for the catalog that corresponds to the specified site UID, catalogId and catalogVersion. The pages are
         * retrieved using REST calls to the cmswebservices pages API.
         *
         * @param {String} siteUID The UID of the site that the pages are to be fetched.
         * @param {String} catalogId The ID of the catalog that the pages are to be fetched.
         * @param {String} catalogVersion The version of the catalog that the pages are to be fetched.
         *
         * @returns {Array} An array of pages descriptors. Each descriptor provides the following pages properties:
         * creationtime, modifiedtime, pk, template, title, typeCode, uid.
         */
        getPageListForCatalog: function(siteUID, catalogId, catalogVersion) {

            return pageRestService.get({
                siteUID: siteUID,
                catalogId: catalogId,
                catalogVersion: catalogVersion
            }).then(function(pages) {
                return pages;
            });

        }

    };

}]);

(function() {
    /**
     * @ngdoc overview
     * @name addPageServiceModule
     * @description
     * # The addPageServiceModule
     *
     * The add page service module provides the functionality necessary to enable the creation of pages through a modal wizard.
     *
     * Use the {@link addPageServiceModule.addPageWizardService addPageWizardService} to open the add page wizard modal.
     *
     */
    angular.module('addPageServiceModule', ['wizardServiceModule', 'pageTypeServiceModule', 'pageTemplateServiceModule', 'languageServiceModule', 'addPageInfoDirectiveModule', 'createPageServiceModule'])
        /**
         * @ngdoc object
         * @name addPageServiceModule.object:BASE_PAGE_INFO_FIELDS
         *
         * @description
         * Injectable angular constant<br/>
         * Defines the generic editor fields that are necessary to edit any page.
         *
         */
        .constant('BASE_PAGE_INFO_FIELDS', [{
            cmsStructureType: "ShortString",
            qualifier: "name",
            i18nKey: 'se.cms.pageinfoform.name',
            required: true
        }, {
            cmsStructureType: "ShortString",
            qualifier: "title",
            i18nKey: 'se.cms.pageinfoform.title',
            localized: true,
            required: true
        }, {
            cmsStructureType: "ShortString",
            qualifier: "uid",
            i18nKey: 'se.cms.pageinfoform.id'
        }])
        /**
         * @ngdoc object
         * @name addPageServiceModule.object:PAGE_INFO_FIELDS_MAP
         *
         * @description
         * Injectable angular constant<br/>
         * Defines a map with the generic editor fields registered to edit pages of a page type.
         *
         */
        .value('PAGE_INFO_FIELDS_MAP', {
            "EmailPage": [{
                cmsStructureType: "ShortString",
                qualifier: "fromEmail",
                i18nKey: 'se.cms.pageinfoform.fromemail'
            }, {
                cmsStructureType: "ShortString",
                qualifier: "fromName",
                i18nKey: 'se.cms.pageinfoform.fromname'
            }],
            "ContentPage": [{
                cmsStructureType: "ShortString",
                qualifier: "label",
                i18nKey: 'se.cms.pageinfoform.label'
            }]
        })
        /**
         * @ngdoc service
         * @name addPageServiceModule.service:addPageWizardService
         *
         * @description
         * The add page wizard service allows opening a modal wizard to create a page.
         *
         */
        .factory('addPageWizardService', ['modalWizard', 'BASE_PAGE_INFO_FIELDS', 'PAGE_INFO_FIELDS_MAP', function(modalWizard, BASE_PAGE_INFO_FIELDS, PAGE_INFO_FIELDS_MAP) {
            return {
                /**
                 * @ngdoc method
                 * @name addPageServiceModule.service:addPageWizardService#openAddPageWizard
                 * @methodOf addPageServiceModule.service:addPageWizardService
                 *
                 * @description
                 * When called, this method opens a modal window containing a wizard to create new pages.
                 *
                 * @returns {Promise} A promise that will resolve when the modal wizard is closed or reject if it's canceled.
                 *
                 */
                openAddPageWizard: function() {
                    return modalWizard.open({
                        controller: 'addPageWizardController',
                        controllerAs: 'addPageWizardCtl'
                    });
                },
                /**
                 * @ngdoc method
                 * @name addPageServiceModule.service:addPageWizardService#getPageTypeFields
                 * @methodOf addPageServiceModule.service:addPageWizardService
                 *
                 * @description
                 * This method returns the list of generic editor fields used to provide all the page info necessary to create a page of a page type.
                 *
                 * @param {String} pageTypeCode The page type for which to retrieve its associated generic editor fields.
                 * @returns {Array} The list of generic editor fields associated to the provided page type. If no match is found, this method returns the set of default fields.
                 *
                 */
                getPageTypeFields: function(pageTypeCode) {
                    var registeredFields = BASE_PAGE_INFO_FIELDS;
                    var pageInfoFields = (PAGE_INFO_FIELDS_MAP[pageTypeCode]) ? PAGE_INFO_FIELDS_MAP[pageTypeCode] : [];
                    return registeredFields.concat(pageInfoFields);
                }
            };
        }])
        /**
         * @ngdoc controller
         * @name addPageServiceModule.controller:addPageWizardController
         *
         * @description
         * The add page wizard controller manages the operation of the wizard used to create new pages.
         */
        .controller('addPageWizardController', ['$q', '$routeParams', 'hitch', 'pageTypeService', 'pageTemplateService', 'languageService', 'addPageWizardService', 'createPageService', function($q, $routeParams, hitch, pageTypeService, pageTemplateService, languageService, addPageWizardService, createPageService) {
            // Constants
            var ADD_PAGE_WIZARD_STEPS = {
                PAGE_TYPE: 'pageType',
                PAGE_TEMPLATE: 'pageTemplate',
                PAGE_INFO: 'pageInfo'
            };
            var DEFAULT_TOOLING_LANGUAGE = "en";

            this.model = {};

            this.siteUID = $routeParams.siteId;
            this.catalogId = $routeParams.catalogId;
            this.catalogVersion = $routeParams.catalogVersion;

            // Wizard Configuration
            this.getWizardConfig = hitch(this, function() {
                var wizardConfig = {
                    isFormValid: hitch(this, this.isFormValid),
                    onNext: hitch(this, this.onNext),
                    onDone: hitch(this, this.onDone),
                    steps: [{
                        id: ADD_PAGE_WIZARD_STEPS.PAGE_TYPE,
                        name: 'se.cms.addpagewizard.pagetype.tabname',
                        title: 'se.cms.addpagewizard.pagetype.title',
                        templateUrl: 'web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTypeStepTemplate.html',
                        actions: []
                    }, {
                        id: ADD_PAGE_WIZARD_STEPS.PAGE_TEMPLATE,
                        name: 'se.cms.addpagewizard.pagetemplate.tabname',
                        title: 'se.cms.addpagewizard.pagetype.title',
                        templateUrl: 'web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTemplateStepTemplate.html',
                        actions: []
                    }, {
                        id: ADD_PAGE_WIZARD_STEPS.PAGE_INFO,
                        name: 'se.cms.addpagewizard.pageinfo.tabname',
                        title: 'se.cms.addpagewizard.pagetype.title',
                        templateUrl: 'web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageInfoStepTemplate.html',
                        actions: []
                    }]
                };

                return wizardConfig;
            });

            // Wizard Navigation
            this.isFormValid = function(stepId) {
                var result = false;
                switch (stepId) {
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TYPE:
                        result = (this.model.selectedType && this.model.selectedType.isSelected);
                        break;
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TEMPLATE:
                        result = (this.model.selectedTemplate && this.model.selectedTemplate.isSelected);
                        break;
                    case ADD_PAGE_WIZARD_STEPS.PAGE_INFO:
                        result = (this.model.editor && this.model.editor.isDirty() === true);
                        break;
                }

                return result;
            };

            this.onNext = function(stepId) {
                var result;
                switch (stepId) {
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TYPE:
                        if (this.model.selectedType && this.model.selectedType.isSelected) {
                            result = this.getPageTemplates(this.model.selectedType.code);
                        }
                        break;
                    case ADD_PAGE_WIZARD_STEPS.PAGE_TEMPLATE:
                        if (this.model.selectedTemplate && this.model.selectedTemplate.isSelected) {
                            var typeCode = this.model.selectedType.code;
                            this.model.pageInfoStructure = addPageWizardService.getPageTypeFields(typeCode);

                            result = $q.when(true);
                        }
                        break;
                    default:
                        result = $q.when(false);
                }

                return result;
            };

            this.onDone = function() {
                return $q.when(this.model.editor.submit(this.model.componentForm));
            };

            this.createPage = function() {
                var page = this.model.editor.component;

                page.type = this.model.selectedType.type;
                page.typeCode = this.model.selectedType.code;
                page.template = this.model.selectedTemplate.uid;
                page.defaultPage = false;

                return createPageService.createPage(this.siteUID, this.catalogId, this.catalogVersion, page).then(hitch(this, function(response) {
                    return {
                        payload: this.model.editor.component,
                        response: response
                    };
                }));
            };

            // Page Type
            pageTypeService.getPageTypeIDs().then(hitch(this, function(pageTypes) {
                this.model.pageTypes = pageTypes;
                this.model.selectedType = null;
            }));

            this.selectType = function(pageType) {
                if (this.model.selectedType) {
                    this.model.selectedType.isSelected = false;
                }

                pageType.isSelected = true;
                this.model.selectedType = pageType;
            };

            // Page Templates
            this.getPageTemplates = function(pageType) {
                return pageTemplateService.getPageTemplatesForType(this.siteUID, this.catalogId, this.catalogVersion, pageType)
                    .then(hitch(this, function(result) {
                        this.model.selectedTemplate = null;
                        this.model.pageTemplates = result.templates;

                        return $q.when(true);
                    }));
            };

            this.selectTemplate = function(pageTemplate) {
                if (this.model.selectedTemplate) {
                    this.model.selectedTemplate.isSelected = false;
                }

                pageTemplate.isSelected = true;
                this.model.selectedTemplate = pageTemplate;
            };

            // Helper Methods
            this.model.toolingLanguage = DEFAULT_TOOLING_LANGUAGE;
            languageService.getResolveLocale().then(hitch(this, function(selectedLanguage) {
                this.model.toolingLanguage = selectedLanguage;
            }));

            this.getLocalizedValue = function(fieldLocalizationMap) {
                var localizedValue = "";
                if (fieldLocalizationMap[this.model.toolingLanguage]) {
                    localizedValue = fieldLocalizationMap[this.model.toolingLanguage];
                } else {
                    var macroLanguage = this.model.toolingLanguage.substring(0, 2);

                    for (var key in fieldLocalizationMap) {
                        if (key.indexOf(macroLanguage) === 0) {
                            localizedValue = fieldLocalizationMap[key];
                            break;
                        }
                    }
                }

                return localizedValue;
            };

        }]);
})();

(function() {
    angular.module('addPageInfoDirectiveModule', ['genericEditorModule'])
        .directive('pageInfoForm', ['GenericEditor', function(GenericEditor) {
            return {
                templateUrl: 'web/common/services/genericEditor/genericEditorTemplate.html',
                restrict: 'E',
                transclude: true,
                scope: {
                    page: '=',
                    structure: '=',
                    sharedData: '=',
                    onSubmit: '&'
                },
                link: function($scope) {
                    // Initialize the structure required by the generic editor.
                    $scope.editor = new GenericEditor({
                        structureApi: null,
                        smarteditComponentType: "",
                        structure: $scope.structure,
                        onSubmit: $scope.onSubmit,
                        content: $scope.page
                    });

                    $scope.sharedData.editor = $scope.editor;
                    $scope.sharedData.componentForm = $scope.componentForm;

                    $scope.editor.init();
                }
            };
        }]);
})();

(function() {
    /**
     * @ngdoc overview
     * @name createPageServiceModule
     * @description
     * # The createPageServiceModule
     *
     * The create page service module provides a service that allows creating new pages.
     *
     */
    angular.module('createPageServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        /**
         * @ngdoc service
         * @name createPageServiceModule.service:createPageService
         *
         * @description
         * The createPageService allows creating new pages.
         *
         */
        .factory('createPageService', ['restServiceFactory', 'PAGES_LIST_RESOURCE_URI', function(restServiceFactory, PAGES_LIST_RESOURCE_URI) {
            var pageRestService = restServiceFactory.get(PAGES_LIST_RESOURCE_URI);

            return {
                /**
                 * @ngdoc method
                 * @name createPageServiceModule.service:createPageService#createPage
                 * @methodOf createPageServiceModule.service:createPageService
                 *
                 * @description
                 * When called this service creates a new page based on the information provided.
                 *
                 * @param {String} siteUID The UID of the current site.
                 * @param {String} catalogId The id of the selected catalog.
                 * @param {String} catalogVersion The current catalog version.
                 * @param {String} page The payload containing the information necessary to create the new page.
                 * NOTE: The payload must at least provide the following fields.
                 * - type: The type of the page.
                 * - typeCode: The type code of the page.
                 * - template: The uid of the page template to use.
                 *
                 * @returns {Promise} A promise that will resolve after saving the page in the backend.
                 *
                 */
                createPage: function(siteUID, catalogId, catalogVersion, page) {
                    var payload = page;
                    payload.siteUID = siteUID;
                    payload.catalogId = catalogId;
                    payload.catalogVersion = catalogVersion;

                    return pageRestService.save(payload);
                }
            };
        }]);
})();

(function() {
    /**
     * @ngdoc overview
     * @name pageTemplateServiceModule
     * @description
     * # The pageTemplateServiceModule
     *
     * The page template service module provides a service that allows the retrieval of page templates associated to a page type.
     *
     */
    angular.module('pageTemplateServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        .constant('NON_SUPPORTED_TEMPLATES', [
            "layout/landingLayout1Page",
            "layout/landingLayout3Page",
            "layout/landingLayout4Page",
            "layout/landingLayout5Page",
            "layout/landingLayout6Page",
            "layout/landingLayoutPage",
            "account/accountRegisterPage",
            "checkout/checkoutRegisterPage"
        ])
        /**
         * @ngdoc service
         * @name pageTemplateServiceModule.service:pageTemplateService
         *
         * @description
         * This service allows the retrieval of page templates associated to a page type.
         *
         */
        .factory('pageTemplateService', ['restServiceFactory', 'PAGE_TEMPLATES_URI', 'NON_SUPPORTED_TEMPLATES', function(restServiceFactory, PAGE_TEMPLATES_URI, NON_SUPPORTED_TEMPLATES) {
            var pageTemplateRestService = restServiceFactory.get(PAGE_TEMPLATES_URI);

            return {
                /**
                 * @ngdoc method
                 * @name pageTemplateServiceModule.service:pageTemplateService#getPageTemplatesForType
                 * @methodOf pageTemplateServiceModule.service:pageTemplateService
                 *
                 * @description
                 * When called, this method retrieves the page templates associated to the provided page type.
                 *
                 * @param {String} siteUID The UID of the current site.
                 * @param {String} catalogId The id of the selected catalog.
                 * @param {String} catalogVersion The current catalog version.
                 * @param {String} pageType The page type for which to retrieve its associated page templates.
                 *
                 * @returns {Promise} A promise that will resolve with the page templates retrieved for the provided page type.
                 *
                 */
                getPageTemplatesForType: function(siteUID, catalogId, catalogVersion, pageType) {
                    return pageTemplateRestService.get({
                        siteUID: siteUID,
                        catalogId: catalogId,
                        catalogVersion: catalogVersion,
                        pageTypeCode: pageType,
                        active: true
                    }).then(function(pageTemplates) {
                        return {
                            templates: pageTemplates.templates.filter(function(pageTemplate) {
                                return NON_SUPPORTED_TEMPLATES.indexOf(pageTemplate.frontEndName) === -1;
                            })
                        };
                    });
                }
            };
        }]);
})();

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
 * @name pageTypeServiceModule
 *
 * @description
 * The Page Type Service module provides a service that fetches page types
 *
 */
angular.module('pageTypeServiceModule', ['functionsModule', 'languageServiceModule'])

/**
 * @ngdoc service
 * @name pageTypeServiceModule.pageTypeService
 *
 * @description
 * The Page Type Service fetches page types
 */
.service('pageTypeService', ['$q', 'hitch', 'languageService', function($q, hitch, languageService) {

    var PageTypeService = function() {
        // NOTE: This is a temporary method. The actual map will be retrieved directly from the backend.
        this.localizeField = function(fieldValue, languagesList) {
            var localizedField = {};

            languagesList.forEach(function(language) {
                localizedField[language.isoCode] = fieldValue;
            });

            return localizedField;
        };

        /**
         * @ngdoc method
         * @name pageTypeServiceModule.pageTypeService#getPageTypeIDs
         * @methodOf pageTypeServiceModule.pageTypeService
         *
         * @description
         * Returns the list of page types.
         *
         * @returns {Array} An array of page types.
         */
        this.getPageTypeIDs = function() {
            return languageService.getToolingLanguages().then(hitch(this, function(languages) {
                var pageTypes = [
                    /*{
                                        code: 'EmailPage',
                                        type: 'emailPageData',
                                        name: this.localizeField('cms.pagetype.email.name', languages),
                                        description: this.localizeField('cms.pagetype.email.description', languages)
                                    },*/
                    {
                        code: 'ContentPage',
                        type: 'contentPageData',
                        name: this.localizeField('cms.pagetype.contentpage.name', languages),
                        description: this.localizeField('cms.pagetype.contentpage.description', languages)
                    }
                ];

                return $q.when(pageTypes);
            }));
        };
    };

    return new PageTypeService();
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
angular.module('removeComponentServiceModule', ['removeComponentServiceInterfaceModule', 'gatewayProxyModule'])

.factory('removeComponentService', ['$q', '$log', 'extend', 'gatewayProxy', 'RemoveComponentServiceInterface', function($q, $log, extend, gatewayProxy, RemoveComponentServiceInterface) {

    var REMOVE_COMPONENT_CHANNEL_ID = "RemoveComponent";

    var removeComponentService = function(gatewayId) {
        this.gatewayId = gatewayId;

        gatewayProxy.initForService(this, ["removeComponent"]);
    };

    removeComponentService = extend(RemoveComponentServiceInterface, removeComponentService);

    return new removeComponentService(REMOVE_COMPONENT_CHANNEL_ID);

}]);

/**
 * @ngdoc overview
 * @name synchronizationServiceModule
 * @description
 *
 * The synchronization module contains the service and the directives necessary 
 * to perform catalog synchronization.
 *
 * The {@link synchronizationServiceModule.service:synchronizationService synchronizationService} 
 * calls backend API in order to get synchronization status or trigger a catalog synchronaization.
 *
 * The {@link synchronizationServiceModule.directive:synchronizeCatalog synchronizeCatalog} directive is used to display
 * the synchronization area in the landing page for each store.
 *
 */
angular.module('synchronizationServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule', 'alertServiceModule', 'authenticationModule'])
    /**
     * @ngdoc service
     * @name synchronizationServiceModule.service:synchronizationService
     * @description
     *
     * The synchronization service manages RESTful calls to the synchronization service's backend API.
     * 
     */
    .service('synchronizationService', ['restServiceFactory', '$interval', '$q', '$translate', 'alertService', 'SYNC_PATH', 'authenticationService', function(restServiceFactory, $interval, $q, $translate, alertService, SYNC_PATH, authenticationService) {

        var INTERVAL_IN_MILLISECONDS = 5000;
        var restServiceForSync = restServiceFactory.get(SYNC_PATH, 'catalog');
        var intervalHandle = {};

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#updateCatalogSync
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method is used to synchronize a catalog.
         *
         * Note: synchronization is one-way from a staged catalog version to an online catalog version.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         */
        this.updateCatalogSync = function(catalog) {
            return restServiceForSync.update({
                'catalog': catalog.catalogId
            }).then(function(response) {
                return response;
            }.bind(this), function(reason) {
                var translationErrorMsg = $translate.instant('sync.running.error.msg', {
                    catalogName: catalog.name
                });
                if (reason.statusText === 'Conflict') {
                    alertService.pushAlerts([{
                        successful: false,
                        message: translationErrorMsg,
                        closeable: true
                    }]);
                }
                return false;
            }.bind(this));
        };

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#getCatalogSyncStatus
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method is used to get the synchronization status of a catalog.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         */
        this.getCatalogSyncStatus = function(catalog) {
            return restServiceForSync.get({
                'catalog': catalog.catalogId
            });
        };


        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#startAutoGetSyncData
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method triggers the auto synchronization status update for a given catalog.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         * @param {Function} callback The callback function that will be called upon catalog status update. 
         */
        this.startAutoGetSyncData = function(catalog, callback) {
            intervalHandle[catalog.catalogId] = $interval(function() {

                var url = SYNC_PATH.replace(":catalog", catalog.catalogId);
                authenticationService.isAuthenticated(url).then(
                    function(response) {
                        if (!response) {
                            this.stopAutoGetSyncData(catalog);
                        }
                        this.getCatalogSyncStatus(catalog)
                            .then(callback)
                            .then(function(response) {
                                if (!intervalHandle[catalog.catalogId]) {
                                    this.startAutoGetSyncData(catalog, callback);
                                }
                            }.bind(this));
                    }.bind(this));
            }.bind(this), INTERVAL_IN_MILLISECONDS);
        };

        /**
         * @ngdoc method
         * @name synchronizationServiceModule.service:synchronizationService#stopAutoGetSyncData
         * @methodOf synchronizationServiceModule.service:synchronizationService
         *
         * @description
         * This method stops the auto synchronization status update for a given catalog.
         *
         * @param {Object} catalog An object that contains the information about the catalog to be synchronized.
         */
        this.stopAutoGetSyncData = function(catalog) {
            if (intervalHandle[catalog.catalogId]) {
                $interval.cancel(intervalHandle[catalog.catalogId]);
                intervalHandle[catalog.catalogId] = undefined;
            }
        };

    }]);

/**
 * @ngdoc overview
 * @name synchronizeCatalogModule
 * @description
 *
 * The synchronization module contains the service and the directives necessary 
 * to perform catalog synchronization.
 *
 * The {@link synchronizeCatalogModule.service:synchronizationService synchronizationService} 
 * calls backend API in order to get synchronization status or trigger a catalog synchronaization.
 *
 * The {@link synchronizeCatalogModule.directive:synchronizeCatalog synchronizeCatalog} directive is used to display
 * the synchronization area in the landing page for each store.
 *
 */
angular.module('synchronizeCatalogModule', ['confirmationModalServiceModule', 'synchronizationServiceModule', 'l10nModule'])

.controller('synchronizeCatalogController', ['$scope', 'synchronizationService', '$q', 'confirmationModalService', 'l10nFilter', function($scope, synchronizationService, $q, confirmationModalService, l10nFilter) {

    this.syncInitiatedFlag = false;
    this.syncData = {
        showSyncBtn: false,
        lastSyncTS: '',
        startSyncTS: '',
        showFailure: false
    };

    var updateSyncData = function(response) {
        this.syncInitiatedFlag = ((response.syncStatus == "RUNNING" || response.syncResult == "UNKNOWN") ? true : false);
        this.syncData = {
            showSyncBtn: ((response.syncStatus == "RUNNING" || response.syncResult == "UNKNOWN") ? false : true),
            lastSyncTS: (response.endDate),
            startSyncTS: (response.creationDate ? response.creationDate : "NEVER"),
            showFailure: ((response.syncResult == "ERROR" || response.syncResult == "FAILURE") ? true : false)
        };
    }.bind(this);

    // update catalog synchronization status
    var invokeGetSyncData = function() {
        synchronizationService
            .getCatalogSyncStatus(this.catalog)
            .then(function(response) {
                updateSyncData(response);
            }.bind(this));
    }.bind(this);

    var onDestroyStopAutoGetSyncData = function() {
        // on destroy, cancel auto update
        $scope.$on('$destroy', function() {
            synchronizationService.stopAutoGetSyncData(this.catalog);
        }.bind(this));
    }.bind(this);

    this.syncCatalog = function() {
        var catalogName = l10nFilter(this.catalog.name);
        confirmationModalService.confirm({
            description: 'sync.confirm.msg',
            title: 'sync.confirmation.title',
            descriptionPlaceholders: {
                catalogName: catalogName
            }
        }).then(function() {
            synchronizationService.updateCatalogSync(this.catalog).then(function(response) {
                updateSyncData(response);
            });
        }.bind(this));
    };

    // on init, start auto updating synchronization data
    synchronizationService.startAutoGetSyncData(this.catalog, updateSyncData);
    // on destroy, stop auto update
    onDestroyStopAutoGetSyncData();
    // call the update for the first time. 
    invokeGetSyncData();
}])

/**
 * @ngdoc directive
 * @name synchronizationServiceModule.directive:synchronizeCatalog
 * @restrict E
 * @element synchronize-catalog
 *
 * @description
 * The Synchronize Catalog directive is used to display the synchronization area in the landing page for 
 * each catalog and to update the status of the synchronization button.
 *
 * The directive provides communication by exposing the (syncCatalog) method. When called, the directive 
 * accesses the synchronization service to synchronize a specified catalog.
 *
 * @param {Object} catalog An object that contains the catalog details. It allows the directive 
 * to load or update the synchronization data for a specified catalog. 
 * @param {Function} syncCatalog A function that triggers the synchronization of a catalog using the synchronization service. 
 * It invokes a modal pop-up to confirm the action before it calls the service layer.
 * 
 */
.directive('synchronizeCatalog', function() {
    return {
        templateUrl: 'web/features/cmssmarteditContainer/synchronize/synchronizationTemplate.html',
        restrict: 'E',
        controller: 'synchronizeCatalogController',
        controllerAs: 'ctrl',
        scope: {},
        bindToController: {
            catalog: '='
        }
    };
});

angular.module('cmssmarteditContainerTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentItemTemplate.html',
    "<div class=\"ySEComponentItem col-xs-6\">\n" +
    "\n" +
    "\t<div class=\"smartEditComponent\" data-smartedit-component-id=\"{{componentItem.uid}}\" data-smartedit-component-type=\"{{componentItem.typeCode}}\">\n" +
    "\t\t<img data-ng-src=\"{{imageRoot}}/images/component_default.png\" class=\" \" alt=\"{{componentItem.name}}\" />\n" +
    "\t</div>\n" +
    "\t<div class=\"ySECustomCompTitle\" title=\"{{componentItem.name}} - {{componentItem.typeCode}}\">\n" +
    "\t\t<div class=\"ySECustCompName\" >{{componentItem.name}}</div>\n" +
    "\t\t<div class=\"ySECustCompType\" >{{componentItem.typeCode}}</div>\n" +
    "\t\t<img data-ng-if=\"!componentItem.visible\" class=\"ySEComponentVisibility\" ng-src=\"{{imageRoot}}/images/icon_visibility.png\" >\n" +
    "\t</div>\n" +
    "\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentItemWrapperTemplate.html',
    "<component-item data-component-item=\"item\"></component-item>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentMenuTemplate.html',
    "<component-menu></component-menu>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentMenuWidgetTemplate.html',
    "<div dropdown is-open=\"status.isopen\" auto-close=\"disabled\" class=\"ySEComponentMenuW\">\n" +
    "    <button type=\"button\" class=\"btn btn-default yHybridAction ySEComponentMenuW--button\" dropdown-toggle ng-disabled=\"disabled \" aria-pressed=\"false\">\n" +
    "        <img data-ng-src=\"{{imageRoot}}{{menuIcon}}\"/>\n" +
    "        <span class=\"ySEComponentMenuW--button--txt\">{{'componentmenu.btn.label.addcomponent' | translate}}</span>\n" +
    "    </button>\n" +
    "    <ul class=\"dropdown-menu dropdown-menu-left btn-block ySEComponentMenu\" role=\"menu\" ng-click=\"preventDefault($event);\" >\n" +
    "        <li role=\"menuitem \" class=\"item \">\n" +
    "            <component-search></component-search>\n" +
    "        </li>\n" +
    "        <li class=\"divider \"></li>\n" +
    "        <li role=\"menuitem \" class=\"item \">\n" +
    "            <component-tabs></component-tabs>\n" +
    "        </li>\n" +
    "    </ul>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentSearchTemplate.html',
    "<div class=\"input-group ySEComponentSearch\">\n" +
    "    <span class=\"input-group-addon glyphicon glyphicon-search ySESearchIcon\" ng-click=\"preventDefault($event); \"></span>\n" +
    "    <input type=\"text\" class=\"form-control ySECompSearchField\" name=\"srch-term\" ng-model=\"searchTerm\" ng-keypress=\"focusOnSearch($event);\" ng-click=\"selectSearch($event); \" placeholder=\"{{'componentmenu.search.placeholder' | translate}}\">\n" +
    "    <span data-ng-show=\"showResetButton\" class=\"input-group-addon glyphicon glyphicon-remove-sign ySESearchIcon\" ng-click=\"resetSearch($event); searchTerm='';\"></span>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentTabsTemplate.html',
    "<div class=\"ySEComponentsResult\" ng-controller=\"ComponentMenuController as menuCtrl\">\n" +
    "    <tabset>\n" +
    "        <tab class=\"ySEComTypeTab\" heading=\"{{tabs[0].title | translate}}\" active=\"tabs[0].active\" disable=\"{{tabs[0].disabled}}\" ng-click=\"activateItemsTab(0); selectTab($event); \">\n" +
    "            <div class=\"ySEComponents yFixed\">\n" +
    "                <component-type ng-repeat=\"componentType in menuCtrl.types.componentTypes | nameFilter:searchTerm track by $id(componentType) \"></component-type>\n" +
    "            </div>\n" +
    "        </tab>\n" +
    "        <tab class=\"ySEComTypeTab\" heading=\"{{tabs[1].title | translate}}\" active=\"tabs[1].active\" disable=\"{{tabs[1].disabled}}\" ng-click=\"activateItemsTab(1); selectTab($event); \">\n" +
    "            <y-infinite-scrolling data-ng-if=\"tabs[1].active\"\n" +
    "                data-drop-down-class=\"ySEComponents\"\n" +
    "                data-page-size=\"10\"\n" +
    "                data-mask=\"searchTerm\"\n" +
    "                data-fetch-page=\"menuCtrl.loadComponentItems\"\n" +
    "                data-context=\"menuCtrl\">\n" +
    "                <div data-ng-repeat=\"item in menuCtrl.items\">\n" +
    "                    <component-item data-component-item=\"item\"></component-item>\n" +
    "                </div>\n" +
    "            </y-infinite-scrolling>\n" +
    "        </tab>\n" +
    "    </tabset>\n" +
    "\n" +
    "    <span class=\"label ySEBottomLabel\">{{'componentmenu.label.draganddrop' | translate}}</span>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/componentMenu/componentTypeTemplate.html',
    "<div class=\"ySEComponentItem col-xs-6\">\n" +
    "\t<div class=\"smartEditComponent\" data-smartedit-component-type=\"{{componentType.code}}\">\n" +
    "\t\t<img data-ng-src=\"{{imageRoot}}/images/component_default.png\" class=\" \" alt=\"{{componentItem.name}}\" />\n" +
    "\t</div>\n" +
    "\t<div class=\"ySECustomCompTitle ySECompTypeTitle\">\n" +
    "\t\t<div class=\"ySECustCompType\" title=\"{{componentType.name}}\">{{componentType.name}}</div>\n" +
    "\t</div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/editorTabsetTemplate.html',
    "<y-tabset \n" +
    "\t\tdata-model=\"model\" \n" +
    "\t\ttabs-list=\"tabsList\" \n" +
    "\t\tdata-num-tabs-displayed=\"{{numTabsDisplayed}}\" \n" +
    "\t\ttab-control=\"tabControl\">\n" +
    "</y-tabset>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/admin/adminTabTemplate.html',
    "<admin-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</admin-tab>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/basic/basicTabTemplate.html',
    "<basic-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</basic-tab>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/generic/genericTabTemplate.html',
    "<generic-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tdata-on-structure-resolved=\"model.onStructureResolved\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</generic-tab>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/generic/tabInnerTemplate.html',
    "<generic-editor \n" +
    "\t\tdata-id=\"tabId\" \n" +
    "\t\tdata-smartedit-component-id=\"componentId\" \n" +
    "\t\tdata-smartedit-component-type=\"componentType\" \n" +
    "\t\tdata-structure-api=\"structureApi\" \n" +
    "\t\tdata-content-api=\"contentApi\" \n" +
    "\t\tdata-submit=\"saveTab\" \n" +
    "\t\tdata-reset=\"resetTab\" \n" +
    "\t\tdata-is-dirty=\"isDirtyTab\" \n" +
    "\t\tdata-on-structure-resolved=\"onStructureResolved\">\n" +
    "</generic-editor>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/tabInnerTemplate.html',
    "<generic-editor \n" +
    "\t\tdata-id=\"tabId\" \n" +
    "\t\tdata-smartedit-component-id=\"componentId\" \n" +
    "\t\tdata-smartedit-component-type=\"componentType\" \n" +
    "\t\tdata-structure=\"tabStructure\" \n" +
    "\t\tdata-content-api=\"contentApi\" \n" +
    "\t\tdata-submit=\"saveTab\" \n" +
    "\t\tdata-reset=\"resetTab\" \n" +
    "\t\tdata-is-dirty=\"isDirtyTab\">\n" +
    "</generic-editor>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/editorTabset/tabs/visibility/visibilityTabTemplate.html',
    "<visibility-tab \n" +
    "\t\tclass='sm-tab-content' \n" +
    "\t\tsave-tab='onSave' \n" +
    "\t\treset-tab='onReset' \n" +
    "\t\tcancel-tab=\"onCancel\" \n" +
    "\t\tis-dirty-tab=\"isDirty\" \n" +
    "\t\tdata-tab-id=\"tabId\" \n" +
    "\t\tcomponent-id=\"model.componentId\" \n" +
    "\t\tcomponent-type=\"model.componentType\">\n" +
    "</visibility-tab>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageList/pageListLinkDirectiveTemplate.html',
    "<div class=\"page-list-link-container\">\n" +
    "    <div data-ng-repeat=\"catalogVersion in catalog.catalogVersions\" class=\"col-xs-6 page-list-link-item\">\n" +
    "        <a data-ng-href=\"#/pages/{{catalogVersion.siteDescriptor.uid}}/{{catalogVersion.catalogId}}/{{catalogVersion.catalogVersion}}\" data-translate=\"cataloginfo.pagelist\"></a>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div class=\"row\">\n" +
    "    <div class=\"col-xs-12\">\n" +
    "        <hr class=\"ySECatalogDivider\" />\n" +
    "    </div>\n" +
    "</div>\n" +
    "\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageList/pageListLinkTemplate.html',
    "<page-list-link></page-list-link>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageList/pageListTemplate.html',
    "<div class=\"ySmartEditToolbars\" style=\"position:absolute\">\n" +
    "    <div>\n" +
    "        <toolbar data-css-class=\"ySmartEditTitleToolbar\" data-image-root=\"imageRoot\" data-toolbar-name=\"smartEditTitleToolbar\"></toolbar>\n" +
    "    </div>\n" +
    "</div>\n" +
    "<div ng-class=\"{'alert-overlay': true, 'ySEEmptyMessage': (!alerts || alerts.length == 0 ) }\">\n" +
    "    <alerts-box alerts=\"alerts\" />\n" +
    "</div>\n" +
    "<div class=\"pageListWrapper\">\n" +
    "    <div class=\"ySEPageListTitle\">\n" +
    "        <h1 class=\"ySEPage-list-title\" data-translate='pagelist.title'></h1>\n" +
    "        <h4 class=\"ySEPage-list-label\">{{pageListCtl.catalogName | l10n}} - {{pageListCtl.catalogVersion}}</h4>\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"ySEPage-list-search input-group\">\n" +
    "        <span class=\"input-group-addon glyphicon glyphicon-search ySEPage-list-search-icon\" ng-click=\"preventDefault($event); \"></span>\n" +
    "        <input type=\"text\" class=\"form-control ySEPage-list-search-input\" placeholder=\"{{ 'pagelist.searchplaceholder' | translate }}\" data-ng-model=\"pageListCtl.query.value\" name=\"query\">\n" +
    "    </div>\n" +
    "\n" +
    "    <div class=\"ySEAdd-Page-button\">\n" +
    "        <button class=\"\" data-ng-click=\"pageListCtl.openAddPageWizard()\">\n" +
    "            <img data-ng-src=\"../cmssmartedit/images/icon_add.png\" />\n" +
    "            <span class=\"\">{{'se.cms.addpagewizard.addpage' | translate}}</span>\n" +
    "        </button>\n" +
    "    </div>\n" +
    "\n" +
    "    <client-paged-list items=\"pageListCtl.pages\"\n" +
    "                keys=\"[{\n" +
    "                    property:'name',\n" +
    "                    i18n:'pagelist.headerpagename'\n" +
    "                    },{\n" +
    "                    property:'uid',\n" +
    "                    i18n:'pagelist.headerpageid'\n" +
    "                    },{\n" +
    "                    property:'typeCode',\n" +
    "                    i18n:'pagelist.headerpagetype'\n" +
    "                    },{\n" +
    "                    property:'template',\n" +
    "                    i18n:'pagelist.headerpagetemplate'\n" +
    "                    }]\"\n" +
    "                renderers=\"pageListCtl.renderers\"\n" +
    "                injected-context=\"pageListCtl.injectedContext\"\n" +
    "                sort-by=\"'name'\"\n" +
    "                reversed=\"false\"\n" +
    "                items-per-page=\"10\"\n" +
    "                query=\"pageListCtl.query.value\"\n" +
    "                display-count=\"true\"\n" +
    "    ></client-paged-list>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageInfoStepTemplate.html',
    "<page-info-form data-page=\"addPageWizardCtl.model.page\" data-structure=\"addPageWizardCtl.model.pageInfoStructure\" data-shared-data=\"addPageWizardCtl.model\" data-on-submit=\"addPageWizardCtl.createPage()\"></page-info-form>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTemplateStepTemplate.html',
    "<div class=\"page-type-step-template\">\n" +
    "\n" +
    "    <div class=\"page-wizard-list\">\n" +
    "        <div data-ng-repeat=\"template in addPageWizardCtl.model.pageTemplates\" data-ng-class=\"{ 'page-type-step-template__item__selected': template.isSelected}\" class=\"page-type-step-template__item\" data-ng-click=\"addPageWizardCtl.selectTemplate(template)\">\n" +
    "            <span class=\"hyicon hyicon-checked page-type-step-template__item__icon\"></span>\n" +
    "            <div class=\"page-type-step-template__item--info\">\n" +
    "                <div class=\"page-type-step-template__item--info__title\">{{template.name}}</div>\n" +
    "                <div class=\"page-type-step-template__item--info__description\"></div>\n" +
    "        \n" +
    "            </div>\n" +
    "\n" +
    "        </div>\n" +
    "    </div>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/pageManagement/addPageWizard/templates/pageTypeStepTemplate.html',
    "<div class=\"page-type-step-template\">\n" +
    "\n" +
    "    <div class=\"page-type-step-template__text ySEText\">{{'se.cms.addpagewizard.pagetype.description' | translate}}</div>\n" +
    "    <div data-ng-repeat=\"pageType in addPageWizardCtl.model.pageTypes\" data-ng-class=\"{ 'page-type-step-template__item__selected': pageType.isSelected}\" class=\"page-type-step-template__item\" data-ng-click=\"addPageWizardCtl.selectType(pageType)\">\n" +
    "        <span class=\"hyicon hyicon-checked page-type-step-template__item__icon\"></span>\n" +
    "        \n" +
    "        <div class=\"page-type-step-template__item--info\">\n" +
    "            <!--\n" +
    "                NOTE: The last translate is only needed while the temporary pageTypeService is used.\n" +
    "                This should be removed when migrating to the backend service.\n" +
    "            -->\n" +
    "            <div class=\"page-type-step-template__item--info__title\">{{addPageWizardCtl.getLocalizedValue(pageType.name) | translate}}</div>\n" +
    "            <div class=\"page-type-step-template__item--info__description\">{{addPageWizardCtl.getLocalizedValue(pageType.description) | translate}}</div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>"
  );


  $templateCache.put('web/features/cmssmarteditContainer/synchronize/catalogDetailsSyncTemplate.html',
    "<synchronize-catalog data-catalog=\"catalog\"></synchronize-catalog>\n"
  );


  $templateCache.put('web/features/cmssmarteditContainer/synchronize/synchronizationTemplate.html',
    "<div>\n" +
    "\t<div class=\"ySECatalogStatus col-xs-8\">\n" +
    "\t\t<div>\n" +
    "\t\t\t<label class=\"ySELastSync\" data-ng-if=\"!ctrl.syncInitiatedFlag\" data-translate=\"cataloginfo.lastsynced\" ></label>\n" +
    "\t\t\t<label class=\"ySELastSync\" data-ng-if=\"ctrl.syncInitiatedFlag\" data-translate=\"cataloginfo.sync.initiated\"></label>\n" +
    "\t\t\t<span class=\"catalog-last-synced\" data-ng-if=\"ctrl.syncInitiatedFlag\">{{ctrl.syncData.startSyncTS| date:'short'}}</span>\n" +
    "\t\t\t<span class=\"catalog-last-synced\" data-ng-if=\"!ctrl.syncInitiatedFlag\">{{ctrl.syncData.lastSyncTS| date:'short'}}</span>\n" +
    "\t\t\t<span data-ng-if=\"ctrl.syncData.showFailure\" class=\"label-error\" data-translate=\"sync.status.synced.syncfailed\"></span>\n" +
    "\t\t</div>\n" +
    "\t</div>\n" +
    "\t<div class=\"ySECatalogSync col-xs-4 pull-right \">\n" +
    "\t\t<button data-ng-if=\"ctrl.syncData.showSyncBtn\" class=\"btn btn-default catalog-sync-btn pull-right\" data-ng-click=\"ctrl.syncCatalog()\">{{ 'cataloginfo.btn.sync' | translate}}</button>\n" +
    "\t\t<label class=\"ySESyncProgress pull-right\" data-ng-if=\"!ctrl.syncData.showSyncBtn\" data-translate=\"sync.status.synced.inprogress\"></label>\n" +
    "\t</div>\n" +
    "</div>\n"
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
 */
/**
 * @ngdoc overview
 * @name assetsServiceModule
 * @description
 * # The assetsServiceModule
 *
 * The assetsServiceModule provides methods to handle assets such as images
 *
 */
angular.module('assetsServiceModule', [])
    /**
     * @ngdoc object
     * @name cmsConstantsModule.object:testAssets
     *
     * @description
     * overridable constant to specify whether cmssmartedit is in test mode
     */
    .constant('testAssets', false)
    /**
     * @ngdoc object
     * @name cmsConstantsModule.service:assetsService
     *
     * @description
     * returns the assets resources root depending whether or not we are in test mode
     */
    .factory('assetsService', ['testAssets', function(testAssets) {
        return {
            getAssetsRoot: function() {
                return testAssets ? '/web/webroot' : '/cmssmartedit';
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
 */
/**
 * @ngdoc overview
 * @name removeComponentServiceInterfaceModule
 * @description
 * # The removeComponentServiceInterfaceModule
 *
 * Provides a service with the ability to remove a component from a slot.
 */
angular.module('removeComponentServiceInterfaceModule', [])
    /**
     * @ngdoc service
     * @name removeComponentServiceInterfaceModule.service:RemoveComponentServiceInterface
     * @description
     * Service interface specifying the contract used to remove a component from a slot.
     *
     * This class serves as an interface and should be extended, not instantiated.
     */
    .factory('RemoveComponentServiceInterface', function() {
        function RemoveComponentServiceInterface() {}

        /**
         * @ngdoc method
         * @name removeComponentServiceInterfaceModule.service:RemoveComponentServiceInterface#removeComponent
         * @methodOf removeComponentServiceInterfaceModule.service:RemoveComponentServiceInterface
         *
         * @description
         * Removes the component specified by the given ID from the component specified by the given ID.
         *
         * @param {String} slotId The ID of the slot from which to remove the component.
         * @param {String} componentId The ID of the component to remove from the slot.
         */
        RemoveComponentServiceInterface.prototype.removeComponent = function(slotId, componentId) {};

        return RemoveComponentServiceInterface;
    });

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
    /**
     * @ngdoc overview
     * @name resourceModule
     *
     * @description
     * The resource module provides $resource factories.  
     */
    angular.module('resourceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        .factory('itemsResource', ['restServiceFactory', 'ITEMS_RESOURCE_URI', function(restServiceFactory, ITEMS_RESOURCE_URI) {
            return restServiceFactory.get(ITEMS_RESOURCE_URI);
        }])
        .factory('pagesContentSlotsComponentsResource', ['restServiceFactory', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {
            return restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);
        }]);
})();

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

    var CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
    var CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';
    var CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';

    angular.module('resourceLocationsModule')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:TYPES_RESOURCE_URI
     *
     * @description
     * Resource URI of the component types REST service.
     */
    .constant('TYPES_RESOURCE_URI', '/cmswebservices/v1/types')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:ITEMS_RESOURCE_URI
     *
     * @description
     * Resource URI of the custom components REST service.
     */
    .constant('ITEMS_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI
     *
     * @description
     * Resource URI of the pages content slot component REST service.
     */
    .constant('PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pagescontentslotscomponents')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI
     *
     * @description
     * Resource URI of the content slot type restrictions REST service.
     */
    .constant('CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI', '/cmswebservices/v1/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pages/:pageUid/contentslots/:slotUid/typerestrictions')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_LIST_RESOURCE_URI
     *
     * @description
     * Resource URI of the pages REST service.
     */
    .constant('PAGES_LIST_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pages')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGE_LIST_PATH
     *
     * @description
     * Path of the page list
     */
    .constant('PAGE_LIST_PATH', '/pages/:siteId/:catalogId/:catalogVersion')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_RESOURCE_URI
     *
     * @description
     * Resource URI of the page content slots REST service
     */
    .constant('PAGES_CONTENT_SLOT_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pagescontentslots')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGE_TEMPLATES_URI
     *
     * @description
     * Resource URI of the page templates REST service
     */
    .constant('PAGE_TEMPLATES_URI', '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pagetemplates');

})();

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
angular.module('componentServiceModule', ['restServiceFactoryModule', 'functionsModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name componentMenuModule.ComponentService
     *
     * @description
     * Service which manages component types and items
     */
    .factory('ComponentService', ['restServiceFactory', 'hitch', '$q', '$log', 'parseQuery', 'TYPES_RESOURCE_URI', 'ITEMS_RESOURCE_URI', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, hitch, $q, $log, parseQuery, TYPES_RESOURCE_URI, ITEMS_RESOURCE_URI, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {

        var restServiceForTypes = restServiceFactory.get(TYPES_RESOURCE_URI);
        var restServiceForItems = restServiceFactory.get(ITEMS_RESOURCE_URI);
        var restServiceForAddNewComponent = restServiceFactory.get(ITEMS_RESOURCE_URI);
        var restServiceForAddExistingComponent = restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);

        var _typesLoaded = false;
        var _itemsLoaded = false;
        var _listOfComponentTypes = {};
        var _listOfComponentItems = {};
        var _payload = {};

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#addNewComponent
         * @methodOf componentMenuModule.ComponentService
         *
         * @description given a component type and a slot id, a new componentItem is created and added to a slot
         *
         * @param {String} componenCode of the ComponentType to be created and added to the slot.
         * @param {String} componentName of the new component to be created.
         * @param {String} pageId used to identify the current page template.
         * @param {String} slotId used to identify the slot in the current template.
         * @param {String} position used to identify the position in the slot in the current template.
         */
        var _addNewComponent = function(componentName, componentCode, pageId, slotId, position) {

            var deferred = $q.defer();

            _payload.name = componentName;
            _payload.slotId = slotId;
            _payload.pageId = pageId;
            _payload.position = position;
            _payload.typeCode = componentCode;

            restServiceForAddNewComponent.save(_payload).then(
                function(response) {
                    deferred.resolve(response);
                },
                function(response) {
                    deferred.reject(response);
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#addExistingComponent
         * @methodOf componentMenuModule.ComponentService
         *
         * @description add an existing component item to a slot
         *
         * @param {String} pageId used to identify the page containing the slot in the current template.
         * @param {String} componentId used to identify the existing component which will be added to the slot.
         * @param {String} slotId used to identify the slot in the current template.
         * @param {String} position used to identify the position in the slot in the current template.
         */
        var _addExistingComponent = function(pageId, componentId, slotId, position) {

            var deferred = $q.defer();

            _payload.pageId = pageId;
            _payload.slotId = slotId;
            _payload.componentId = componentId;
            _payload.position = position;

            restServiceForAddExistingComponent.save(_payload).then(
                function(response) {
                    deferred.resolve();
                },
                function(response) {
                    deferred.reject(response);
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentTypes
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all component types are retrieved
         */
        var _loadComponentTypes = function() {

            var deferred = $q.defer();

            restServiceForTypes.get().then(
                function(response) {
                    angular.copy(response, _listOfComponentTypes);
                    deferred.resolve(_listOfComponentTypes);
                    _typesLoaded = true;
                    return deferred.promise;

                },
                function() {
                    _typesLoaded = false;
                    deferred.reject();
                    return deferred.promise;
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentItem
         * @methodOf componentMenuModule.ComponentService
         *
         * @description load a component identified by its id
         */
        var _loadComponentItem = function(id) {
            return restServiceForItems.getById(id);
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentItems
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all existing component items for the current catalog are retrieved
         */
        var _loadComponentItems = function() {

            var deferred = $q.defer();

            restServiceForItems.get().then(
                function(response) {
                    angular.copy(response, _listOfComponentItems);
                    deferred.resolve(_listOfComponentItems);
                    _itemsLoaded = true;
                },
                function() {
                    _itemsLoaded = false;
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadPagedComponentItems
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all existing component items for the current catalog are retrieved in the form of pages
         * used for pagination especially when the result set is very large.
         * 
         * @param {String} mask the search string to filter the results.
         * @param {String} pageSize the number of elements that a page can contain.
         * @param {String} page the current page number.
         */
        var _loadPagedComponentItems = function(mask, pageSize, page) {

            return restServiceForItems.get({
                pageSize: pageSize,
                currentPage: page,
                mask: mask,
                sort: 'name'
            });
        };

        return {
            loadComponentTypes: _loadComponentTypes,
            listOfComponentTypes: _listOfComponentTypes,
            loadComponentItem: _loadComponentItem,
            loadComponentItems: _loadComponentItems,
            loadPagedComponentItems: _loadPagedComponentItems,
            listOfComponentItems: _listOfComponentItems,
            addNewComponent: _addNewComponent,
            addExistingComponent: _addExistingComponent,
        };

    }]);

