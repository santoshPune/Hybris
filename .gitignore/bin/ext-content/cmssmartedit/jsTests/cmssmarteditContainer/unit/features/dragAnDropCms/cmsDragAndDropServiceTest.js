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
describe('test cmsDragAndDrop module', function() {

    var cmsDragAndDropService, dragAndDropService, systemEventService, renderService, componentHandlerService, componentService, removeComponentService;

    var $q, $rootScope;

    var currentTargetSelector, targetSelector, helperSelector, componentSelector, iframeSelector, iconBckgSelector, slotsSelector, someIterationElementSelector, $translate, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION, CONTEXT_SITE_ID, translation, child;

    var PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI, CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI;

    var someIterationElement = {
        fdfdfg: 'dfgdfgdfg'
    };

    var event = {
        currentTarget: 'currentTarget',
        target: {
            id: 'targetId'
        }
    };

    var ui = {
        item: {
            index: function() {
                return 0;
            }
        },
        helper: 'helper'
    };

    beforeEach(customMatchers);

    beforeEach(function() {
        angular.module('translationServiceModule', []);
        angular.module('gatewayFactoryModule', []);
        angular.module('renderServiceModule', []);
        angular.module('restServiceFactoryModule', []);
        angular.module('gatewayProxyModule', []);
        angular.module('eventServiceModule', []);
        angular.module('dragAndDropServiceModule', []);
        angular.module('alertServiceModule', []);
        angular.module('editorModalServiceModule', []);
        angular.module('componentHandlerServiceModule', []);

    });

    beforeEach(module('resourceLocationsModule', function($provide) {
        CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI = "/cmswebservices/v1/catalogs/CURRENT_CONTEXT_CATALOG/versions/CURRENT_CONTEXT_CATALOG_VERSION/pages/:pageUid/contentslots/:slotId/typerestrictions";
        PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI = "/cmswebservices/v1/sites/CURRENT_CONTEXT_SITE_ID/catalogs/CURRENT_CONTEXT_CATALOG/versions/CURRENT_CONTEXT_CATALOG_VERSION/pagescontentslotscomponents";
        $provide.constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG");
        $provide.constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION");
        $provide.constant("CONTEXT_SITE_ID", "CURRENT_CONTEXT_SITE_ID");
        $provide.constant("CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI", CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI);
        $provide.constant("PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI", PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);
    }));

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ['renderSlots', 'toggleOverlay', 'refreshOverlayDimensions', 'renderPage']);
        $provide.value('renderService', renderService);
    }));

    beforeEach(module('componentServiceModule', function($provide) {
        componentService = jasmine.createSpyObj('ComponentService', ['addNewComponent', 'addExistingComponent']);
        $provide.value('ComponentService', componentService);
    }));

    beforeEach(module('removeComponentServiceModule', function($provide) {
        removeComponentService = jasmine.createSpyObj('removeComponentService', ['removeComponent']);
        $provide.value('removeComponentService', removeComponentService);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService', 'asdfasdf']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('editorModalServiceModule', function($provide) {
        editorModal = jasmine.createSpyObj('editorModalService', ['open']);
        $provide.value('editorModalService', editorModal);
    }));

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['sendSynchEvent']);
        $provide.value('systemEventService', systemEventService);
    }));

    beforeEach(module('cmsDragAndDropServiceModule', function($provide) {
        dragAndDropService = jasmine.createSpyObj('dragAndDropService', ['register', 'getSelectorInIframe']);
        $provide.value('dragAndDropService', dragAndDropService);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restService = jasmine.createSpyObj('restService', ['get', 'save', 'update']);
        restServiceFactory.get.andReturn(restService);

        $provide.value('restServiceFactory', restServiceFactory);

        $translate = jasmine.createSpyObj('$translate', ['instant']);
        translation = 'Translation Translation';
        $provide.value('$translate', $translate);

        $provide.value('translateFilter', function(data) {
            return data;
        });
        alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);
        $provide.value('alertService', alertService);

        $provide.value('domain', 'thedomain');

        componentHandlerService = jasmine.createSpyObj('componentHandlerService', ['getPageUID', 'getAllComponentsSelector', 'getAllSlotsSelector', 'getId', 'getType', 'setId', 'getSlotOperationRelatedId', 'getSlotOperationRelatedType']);
        $provide.value('componentHandlerService', componentHandlerService);

    }));

    beforeEach(inject(function(_$rootScope_, _cmsDragAndDropService_, _$q_, _CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI_, _PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI_) {
        $q = _$q_;
        $rootScope = _$rootScope_;
        cmsDragAndDropService = _cmsDragAndDropService_;
        currentTargetSelector = {};

        child = jasmine.createSpyObj('child', ['attr']);
        targetSelector = jasmine.createSpyObj('targetSelector', ['removeClass', 'addClass', 'hasClass', 'children', 'attr']);
        targetSelector.children.andReturn(child);

        componentSelector = jasmine.createSpyObj('componentSelector', ['removeClass']);

        helperSelector = jasmine.createSpyObj('helperSelector', ['removeClass']);

        iframeSelector = jasmine.createSpyObj('iframeSelector', ['attr']);
        iframeSelector.attr.andReturn('source');

        someIterationElementSelector = jasmine.createSpyObj('someIterationElementSelector', ['height', 'find', 'addClass']);


        spyOn(cmsDragAndDropService, '_getSelector').andCallFake(function(selectorValue) {
            if (selectorValue === event.currentTarget) {
                return currentTargetSelector;
            } else if (selectorValue === ui.item) {
                return componentSelector;
            } else if (selectorValue === event.target) {
                return targetSelector;
            } else if (selectorValue === ui.helper) {
                return helperSelector;
            } else if (selectorValue === 'iframe') {
                return iframeSelector;
            } else if (selectorValue === someIterationElement) {
                return someIterationElementSelector;
            }
        });

        spyOn(cmsDragAndDropService, '_getElementChild').andCallFake(function(element, childIndex) {
            return element;
        });

        ySEemptySlotSelector = jasmine.createSpyObj('ySEemptySlotSelector', ['removeClass']);
        ySEDirtyHeightSelector = jasmine.createSpyObj('ySEDirtyHeightSelector', ['removeClass', 'height']);
        slotsSelector = jasmine.createSpyObj('slotsSelector', ['switchClass', 'each', 'removeClass']);
        iconBckgSelector = jasmine.createSpyObj('iconBckgSelector', ['switchClass']);
        dragAndDropService.getSelectorInIframe.andCallFake(function(selectorValue) {
            if (selectorValue === '.ySEIconBckg') {
                return iconBckgSelector;
            } else if (selectorValue === '.ySEemptySlot') {
                return ySEemptySlotSelector;
            } else if (selectorValue === '.ySEDirtyHeight') {
                return ySEDirtyHeightSelector;
            } else if (selectorValue === 'mytargetselector') {
                return slotsSelector;
            }
        });

        componentHandlerService.getPageUID.andReturn('pageUid');
        spyOn(cmsDragAndDropService, '_removeDynamicStyles').andCallThrough();

        componentHandlerService.getAllComponentsSelector.andReturn("mysourceselector");
        componentHandlerService.getAllSlotsSelector.andReturn("mytargetselector");

    }));

    it('cmsDragAndDropService.register will call register method of dragAndDropService', function() {

        dragAndDropService.register.andReturn();

        cmsDragAndDropService.register();


        expect(dragAndDropService.register).toHaveBeenCalledWith(jasmine.objectContaining({
            sourceSelector: "mysourceselector",
            targetSelector: "mytargetselector"
        }));

    });

    describe('test callbacks in the payload', function() {

        beforeEach(inject(function() {

            dragAndDropService.register.andReturn();
            cmsDragAndDropService.register();
            payload = dragAndDropService.register.calls[0].args[0];

        }));

        it('startCallback will hide the overlay and set expected sourceSlotId of targeted slots', function() {

            componentHandlerService.getId.andReturn('sourceSlotId');
            payload.startCallback(event, ui);
            expect(renderService.toggleOverlay).toHaveBeenCalledWith(false);
            expect(cmsDragAndDropService._getSelector).toHaveBeenCalledWith(event.currentTarget);
            expect(componentHandlerService.getId).toHaveBeenCalledWith(currentTargetSelector);
            expect(cmsDragAndDropService.sourceSlotId).toEqual('sourceSlotId');
        });


        it('startCallback will send event to close component menu', function() {
            payload.startCallback(event, ui);
            expect(systemEventService.sendSynchEvent).toHaveBeenCalledWith('ySEComponentMenuClose', {});
        });

        it('overCallback will set enable slot class for valid components and toogle ySEDnDImageRed/ySEDnDImageBlue css classes for slots (and fetch component id from child)', function() {

            var data = {
                contentSlotName: "bottomHeaderSlot",
                validComponentTypes: [
                    "componentType1",
                    "componentType2",
                    "componentType3"
                ]
            };

            var deferred = $q.defer();
            deferred.resolve(data);
            restService.get.andReturn(deferred.promise);

            var counter = 0;
            componentHandlerService.getSlotOperationRelatedType.andCallFake(function(selectorValue) {
                if (selectorValue === componentSelector) {
                    if (counter === 0) {
                        counter++;
                        return undefined;
                    } else if (counter == 1) {
                        return 'componentType1';
                    }
                } else {
                    throw "unexpected call to componentHandlerService.getType";
                }
            });

            $translate.instant.andReturn(translation);
            componentHandlerService.getId.andReturn('targetSlotId');

            payload.overCallback(event, ui);
            $rootScope.$digest();


            expect(restServiceFactory.get).toHaveBeenCalledWith(CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI);
            expect(restService.get).toHaveBeenCalledWith({
                pageUid: 'pageUid',
                slotUid: 'targetSlotId'
            });
            expect(cmsDragAndDropService._getElementChild).toHaveBeenCalledWith(ui.item, 0);
            expect(targetSelector.addClass).toHaveBeenCalledWith("over-slot-enabled");
            expect(iconBckgSelector.switchClass).toHaveBeenCalledWith('ySEDnDImageRed', 'ySEDnDImageBlue');
            expect(componentHandlerService.getId).toHaveBeenCalledWith(targetSelector);
            expect(cmsDragAndDropService._getSelector).toHaveBeenCalledWith(event.target);
            expect(cmsDragAndDropService._getSelector).toHaveBeenCalledWith('ySEDnDPlaceHolder');
            expect(targetSelector.children).toHaveBeenCalledWith(cmsDragAndDropService._getSelector('ySEDnDPlaceHolder'));
            expect(child.attr).toHaveBeenCalledWith('data-content', translation);
            expect($translate.instant).toHaveBeenCalledWith('cmsdraganddrop.drophere');
            expect(dragAndDropService.getSelectorInIframe).toHaveBeenCalledWith('.ySEIconBckg');
        });

        it('overCallback will set disable slot class for invalid components and toogle ySEDnDImageBlue/ySEDnDImageRed css classes for slots', function() {

            var data = {
                contentSlotName: "bottomHeaderSlot",
                validComponentTypes: [
                    "componentType2",
                    "componentType3"
                ]
            };

            var deferred = $q.defer();
            deferred.resolve(data);
            restService.get.andReturn(deferred.promise);
            componentHandlerService.getId.andReturn('targetSlotId');
            componentHandlerService.getSlotOperationRelatedType.andReturn('componentType1');

            $translate.instant.andReturn(translation);
            payload.overCallback(event, ui);
            $rootScope.$digest();

            expect(restServiceFactory.get).toHaveBeenCalledWith(CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI);
            expect(restService.get).toHaveBeenCalledWith({
                pageUid: 'pageUid',
                slotUid: 'targetSlotId'
            });
            expect(cmsDragAndDropService._getElementChild).not.toHaveBeenCalled();
            expect(targetSelector.addClass).toHaveBeenCalledWith("over-slot-disabled");
            expect(iconBckgSelector.switchClass).toHaveBeenCalledWith('ySEDnDImageBlue', 'ySEDnDImageRed');

            expect(componentHandlerService.getId).toHaveBeenCalledWith(targetSelector);
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);
            expect(dragAndDropService.getSelectorInIframe).toHaveBeenCalledWith('.ySEIconBckg');
            expect($translate.instant).toHaveBeenCalledWith('cmsdraganddrop.notallowed');
            expect(targetSelector.attr).toHaveBeenCalledWith('data-content', translation);
        });

        it('dropCallback will display an error message (timed alert) if the slot onto which the component is dropped is disabled', function() {


            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return true;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return false;
                }
            });

            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selectorValue) {
                if (selectorValue === componentSelector) {
                    return 'componentId';
                }
            });
            componentHandlerService.getId.andCallFake(function(selectorValue) {
                if (selectorValue == targetSelector) {
                    return 'targetSlotId';
                }
            });

            componentHandlerService.getSlotOperationRelatedType.andReturn('componentType1');

            $translate.instant.andReturn(translation);
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);
            expect(cancel).toHaveBeenCalled();
            expect(alertService.pushAlerts).toHaveBeenCalledWith([{
                successful: false,
                message: translation,
                closeable: true
            }]);
            expect(restService.update).not.toHaveBeenCalled();

            expect($translate.instant).toHaveBeenCalledWith("drag.and.drop.not.valid.component.type", {
                componentUID: 'componentId',
                slotUID: 'targetSlotId'
            });
        });

        it('dropCallback will display a success message (timed alert) if the slot onto which the component is dropped is enabled and API call is successful', function() {

            var deferred = $q.defer();
            deferred.resolve({});
            restService.update.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentHandlerService.getSlotOperationRelatedType.andReturn('componentType1');
            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selectorValue) {
                if (selectorValue === componentSelector) {
                    return 'componentId';
                }
            });
            componentHandlerService.getId.andCallFake(function(selectorValue) {
                if (selectorValue == targetSelector) {
                    return 'targetSlotId';
                }
            });

            cmsDragAndDropService.sourceSlotId = "someSourceSlotId";
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(restServiceFactory.get).toHaveBeenCalledWith(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI + '?pageId=:pageId&slotId=:currentSlotId&componentId=:componentId', 'componentId');

            expect(restService.update).toHaveBeenCalledWith({
                pageId: 'pageUid',
                currentSlotId: 'someSourceSlotId',
                componentId: 'componentId',
                slotId: 'targetSlotId',
                position: 0
            });

            expect(cancel).not.toHaveBeenCalled();
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);

        });


        it('dropCallback will cancel the move and return a default error message (timed alert) if the move API call is failing and the response is undefined', function() {

            var errorMessage = "Failed to move component componentId to slot targetSlotId";

            var deferred = $q.defer();
            deferred.reject();
            restService.update.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentHandlerService.getSlotOperationRelatedType.andReturn('componentType1');
            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selectorValue) {
                if (selectorValue === componentSelector) {
                    return 'componentId';
                }
            });
            componentHandlerService.getId.andCallFake(function(selectorValue) {
                if (selectorValue == targetSelector) {
                    return 'targetSlotId';
                }
            });

            $translate.instant.andReturn(errorMessage);

            cmsDragAndDropService.sourceSlotId = "someSourceSlotId";
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(restServiceFactory.get).toHaveBeenCalledWith(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI + '?pageId=:pageId&slotId=:currentSlotId&componentId=:componentId', 'componentId');

            expect(restService.update).toHaveBeenCalledWith({
                pageId: 'pageUid',
                currentSlotId: 'someSourceSlotId',
                componentId: 'componentId',
                slotId: 'targetSlotId',
                position: 0
            });

            expect(cancel).toHaveBeenCalled();
            expect($translate.instant).toHaveBeenCalledWith("cmsdraganddrop.move.failed", {
                slotID: 'targetSlotId',
                componentID: 'componentId'
            });
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);
            expect(alertService.pushAlerts).toHaveBeenCalledWith([{
                successful: false,
                message: errorMessage,
                closeable: true
            }]);

        });

        it('dropCallback will cancel the move and return an error message from response (timed alert) if the move API call is failing and the response has an error message', function() {

            var errorMessage = "Failed to move component componentId to slot targetSlotId";

            var deferred = $q.defer();
            deferred.reject({
                data: {
                    errors: [{
                        "message": "error from API call"
                    }]
                }
            });
            restService.update.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentHandlerService.getSlotOperationRelatedType.andReturn('componentType1');
            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selectorValue) {
                if (selectorValue === componentSelector) {
                    return 'componentId';
                }
            });
            componentHandlerService.getId.andCallFake(function(selectorValue) {
                if (selectorValue == targetSelector) {
                    return 'targetSlotId';
                }
            });

            $translate.instant.andReturn(errorMessage);
            cmsDragAndDropService.sourceSlotId = "someSourceSlotId";
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect($translate.instant).toHaveBeenCalledWith('cmsdraganddrop.error', {
                sourceComponentId: 'componentId',
                targetSlotId: 'targetSlotId',
                detailedError: "error from API call"
            });

            expect(alertService.pushAlerts).toHaveBeenCalledWith([{
                successful: false,
                message: errorMessage,
                closeable: true
            }]);

        });

        it('outCallback will remove the added classes once dragging of components is stopped', function() {

            payload.outCallback(event, ui);

            expect(helperSelector.removeClass.callCount).toBe(1);
            expect(targetSelector.removeClass.callCount).toBe(2);

            expect(helperSelector.removeClass).toHaveBeenCalledWith("over-active");
            expect(targetSelector.removeClass).toHaveBeenCalledWith("over-slot-enabled");
            expect(targetSelector.removeClass).toHaveBeenCalledWith("over-slot-disabled");

        });



        it('stopCallback will remove height modifications of targeted slots, show the overlay and recalculate dimension/positions of decorators', function() {

            payload.stopCallback(event, ui);

            expect(cmsDragAndDropService._removeDynamicStyles).toHaveBeenCalled();
            expect(renderService.toggleOverlay).toHaveBeenCalledWith(true);
            expect(renderService.renderPage).toHaveBeenCalledWith(true);

            expect(slotsSelector.removeClass).toHaveBeenCalledWith("ySEemptySlot");
            expect(ySEDirtyHeightSelector.removeClass).toHaveBeenCalledWith("ySEDirtyHeight");
            expect(ySEDirtyHeightSelector.height).toHaveBeenCalledWith(0);

        });

        it('dropCallback will go through proper steps to successfully add the component item to the page', function() {
            var targetComponent = {
                pageId: 'pageUid',
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0
            };

            var deferred = $q.defer();
            deferred.resolve({});

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentService.addExistingComponent.andReturn(deferred.promise);
            componentHandlerService.getSlotOperationRelatedType.andReturn(targetComponent.componentType);
            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selector) {
                if (selector === componentSelector) {
                    return targetComponent.componentId;
                }
            });
            componentHandlerService.getId.andCallFake(function(selector) {
                if (selector === targetSelector) {
                    return targetComponent.slotId;
                }
            });

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addExistingComponent).toHaveBeenCalledWith(targetComponent.pageId, targetComponent.componentId, targetComponent.slotId, targetComponent.index);
            expect(renderService.renderSlots).toHaveBeenCalledWith(targetComponent.slotId);

            expect(cancel).not.toHaveBeenCalled();
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);

        });

        it('dropCallback will go through proper steps to successfully add the component type to the page', function() {

            var targetComponent = {
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0,
                name: "name",
                pageId: 'pageUid'
            };


            var response = {
                "uid": targetComponent.componentId
            };

            var deferred = $q.defer();
            deferred.resolve(response);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentService.addNewComponent.andReturn(deferred.promise);

            editorModal.open.andReturn(deferred.promise);
            componentHandlerService.getSlotOperationRelatedType.andReturn(targetComponent.componentType);
            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selector) {
                if (selector === componentSelector) {
                    return undefined;
                }
            });
            componentHandlerService.getId.andCallFake(function(selector) {
                if (selector === targetSelector) {
                    return targetComponent.slotId;
                }
            });

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addNewComponent).toHaveBeenCalledWith(targetComponent.name, targetComponent.componentType, targetComponent.pageId, targetComponent.slotId, targetComponent.index);
            expect(editorModal.open).toHaveBeenCalledWith(targetComponent.componentType, targetComponent.componentId);
            expect(renderService.renderSlots).not.toHaveBeenCalled();

            expect(cancel).not.toHaveBeenCalled();
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);

        });

        it('dropCallback will try to add the component type but modal is cancelled', function() {

            var targetComponent = {
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0,
                name: "name",
                pageId: 'pageUid'
            };


            var response = {
                "uid": targetComponent.componentId
            };

            var deferredSuccess = $q.when(response);
            componentService.addNewComponent.andReturn(deferredSuccess);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            var deferredReject = $q.defer();
            deferredReject.reject({});

            editorModal.open.andReturn(deferredReject.promise);

            componentHandlerService.getSlotOperationRelatedType.andReturn(targetComponent.componentType);
            componentHandlerService.getSlotOperationRelatedId.andCallFake(function(selector) {
                if (selector === componentSelector) {
                    return undefined;
                }
            });
            componentHandlerService.getId.andCallFake(function(selector) {
                if (selector === targetSelector) {
                    return targetComponent.slotId;
                }
            });
            removeComponentService.removeComponent.andReturn(deferredSuccess);
            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addNewComponent).toHaveBeenCalledWith(targetComponent.name, targetComponent.componentType, targetComponent.pageId, targetComponent.slotId, targetComponent.index);
            expect(editorModal.open).toHaveBeenCalledWith(targetComponent.componentType, targetComponent.componentId);
            expect(removeComponentService.removeComponent).toHaveBeenCalledWith({
                slotId: targetComponent.slotId,
                componentType: targetComponent.componentType,
                slotOperationRelatedId: targetComponent.componentId,
                componentId: targetComponent.componentId
            });

            expect(cancel).toHaveBeenCalled();
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);

        });

        it('dropCallback will try to add the component type but add new component fails', function() {

            var targetComponent = {
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0,
                name: "name",
                pageId: 'pageUid'
            };

            var response = {
                "uid": targetComponent.componentId,
                "data": {
                    errors: [{
                        message: 'this is an error'
                    }]
                }
            };


            var deferred = $q.defer();
            deferred.reject(response);
            componentService.addNewComponent.andReturn(deferred.promise);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentHandlerService.getSlotOperationRelatedType.andReturn(targetComponent.componentType);
            componentHandlerService.getSlotOperationRelatedId.andReturn(undefined);
            componentHandlerService.getId.andReturn(targetComponent.slotId);

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addNewComponent).toHaveBeenCalledWith(targetComponent.name, targetComponent.componentType, targetComponent.pageId, targetComponent.slotId, targetComponent.index);

            expect(cancel).toHaveBeenCalled();
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);
            expect(componentHandlerService.getSlotOperationRelatedId).toHaveBeenCalledWith(componentSelector);
            expect(componentHandlerService.getId).toHaveBeenCalledWith(targetSelector);
        });

        it('dropCallback will try to add the component item but fails', function() {

            var targetComponent = {
                pageId: 'pageUid',
                componentId: 'id',
                componentType: 'componentType',
                slotId: 'targetSlotId',
                index: 0
            };

            var response = {
                "data": {
                    errors: [{
                        message: 'this is an error'
                    }]
                }
            };

            var deferred = $q.defer();
            deferred.reject(response);

            var cancel = jasmine.createSpy('cancel');

            targetSelector.hasClass.andCallFake(function(cssSelector) {
                if (cssSelector === 'over-slot-disabled') {
                    return false;
                }

                if (cssSelector === 'over-slot-enabled') {
                    return true;
                }
            });

            componentService.addExistingComponent.andReturn(deferred.promise);
            componentHandlerService.getSlotOperationRelatedType.andReturn(targetComponent.componentType);
            componentHandlerService.getSlotOperationRelatedId.andReturn(targetComponent.componentId);
            componentHandlerService.getId.andReturn(targetComponent.slotId);

            payload.dropCallback(event, ui, cancel);
            $rootScope.$digest();

            expect(componentService.addExistingComponent).toHaveBeenCalledWith(targetComponent.pageId, targetComponent.componentId, targetComponent.slotId, targetComponent.index);
            expect(cancel).toHaveBeenCalled();
            expect(componentHandlerService.getSlotOperationRelatedType).toHaveBeenCalledWith(componentSelector);
            expect(componentHandlerService.getSlotOperationRelatedId).toHaveBeenCalledWith(componentSelector);
            expect(componentHandlerService.getId).toHaveBeenCalledWith(targetSelector);

        });

    });

});
