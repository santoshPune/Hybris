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
    .factory('cmsDragAndDropService', function($rootScope, $log, $translate, dragAndDropService, restServiceFactory, parseQuery, hitch, ComponentService, editorModalService, systemEventService, renderService, alertService, removeComponentService, domain, componentHandlerService, CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {

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

    });
