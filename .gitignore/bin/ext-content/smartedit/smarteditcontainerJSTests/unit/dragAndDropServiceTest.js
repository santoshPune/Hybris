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
describe('test outer dragAndDropServiceModule Module', function() {

    var dragAndDropService, $rootScope;
    var gatewayFactory, gatewayProxy, dragAndDropScrollingService;

    // Page content
    var body, iFrame, iFrameBody, slots, helper, draggableElements;

    var configuration, sortableMock, draggableMock;
    var startCallback, overCallback, outCallback, dropCallback, stopCallback;

    // Constants
    var targetSelector = 'targetSelector';
    var sourceSelector = 'sourceSelector';

    beforeEach(customMatchers);

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('dragAndDropScrollingModule', function($provide) {
        dragAndDropScrollingService = jasmine.createSpyObj('dragAndDropScrollingService', ['connectWithDragAndDrop', 'enableScrolling', 'disableScrolling']);
        $provide.value('dragAndDropScrollingService', dragAndDropScrollingService);
    }));

    beforeEach(module('dragAndDropServiceModule'));

    beforeEach(inject(function(_$rootScope_, _dragAndDropService_) {
        $rootScope = _$rootScope_;
        dragAndDropService = _dragAndDropService_;
    }));

    beforeEach(function() {
        configuration = loadConfiguration();
        createFakePageContent();
        createSortableMock();
        createDraggableMock();

        spyOn(dragAndDropService, 'getSelector').andCallFake(function(selectorValue) {
            if (selectorValue === '._sm_helper') {
                return helper;
            } else if (selectorValue === sourceSelector) {
                return draggableElements;
            }
            return selectorValue;
        });

        spyOn(dragAndDropService, 'getSelectorInIframe').andCallFake(function(selectorValue) {
            if (selectorValue === 'body') {
                return iFrameBody;
            }
            if (selectorValue === targetSelector) {
                return slots;
            } else if (selectorValue === '._sm_helper') {
                return helper;
            }
            //} else if (selectorValue === 'body') {
            //    return htmlElement;
            //}
        });

        spyOn(dragAndDropService, '_getReference').andCallFake(function(reference) {
            if (reference.drag) {
                return draggableElements;
            } else {
                return slots;
            }
        });

    });

    // Tests
    it('WHEN dragAndDropService is initialized THEN it inits a private gateway', function() {
        // Arrange/Act

        // Assert
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(dragAndDropService, ["register"]);
    });

    it('WHEN register is called with a configuration without an id THEN it must throw an exception', function() {
        // Arrange
        var configuration = {};

        // Act/Assert
        expect(function() {
            dragAndDropService.register(configuration);
        }).toThrow();
    });

    it('WHEN register is called with a configuration with an id already present THEN it must be replaced', function() {
        // Arrange
        var configuration1 = {
            id: 'configuration1',
            someProperty: 'property1'
        };
        var configuration2 = {
            id: 'configuration1',
            someProperty: 'property2'
        };

        spyOn(dragAndDropService, 'registerSortable').andReturn();
        spyOn(dragAndDropService, 'registerDrag').andReturn();

        // Act
        dragAndDropService.register(configuration1);
        dragAndDropService.register(configuration2);
        dragAndDropService.apply();

        // Assert
        expect(dragAndDropService.registerSortable.calls.length).toEqual(1);
        expect(dragAndDropService.registerDrag.calls.length).toEqual(1);

        expect(dragAndDropService.registerSortable.calls[0].args).toEqual([configuration2]);
        expect(dragAndDropService.registerDrag.calls[0].args).toEqual([configuration2]);
    });


    it('WHEN dragAndDropService.apply is called THEN each configuration is properly bootstrapped', function() {
        // Arrange
        spyOn(dragAndDropService, 'registerSortable').andReturn();
        spyOn(dragAndDropService, 'registerDrag').andReturn();

        var secondConfiguration = loadConfiguration();
        secondConfiguration.id = "someID2";

        // Act
        dragAndDropService.register(configuration);
        dragAndDropService.register(secondConfiguration);
        dragAndDropService.apply();

        // Assert
        expect(dragAndDropService.registerSortable.calls.length).toEqual(2);
        expect(dragAndDropService.registerDrag.calls.length).toEqual(2);
        expect(dragAndDropScrollingService.connectWithDragAndDrop.calls.length).toEqual(2);

        expect(dragAndDropService.registerSortable.calls[0].args).toEqual([configuration]);
        expect(dragAndDropService.registerSortable.calls[1].args).toEqual([secondConfiguration]);
        expect(dragAndDropScrollingService.connectWithDragAndDrop).toHaveBeenCalledWith(configuration);
    });

    it('WHEN registerSortable is called THEN sortables will be registered on targetSelectors on iFrame and connected with other targetSelectors in iFrame', function() {
        // Arrange

        // Act
        dragAndDropService.registerSortable(configuration);

        // Assert
        expect(slots.sortable).toHaveBeenCalledWith(jasmine.objectContaining({
            connectWith: 'targetSelector',
            items: 'sourceSelector',
            iframeFix: true,
            cursorAt: {
                top: 0,
                left: 0
            },
            intersect: "pointer",
            placeholder: 'ySEDnDPlaceHolder'
        }));
        expect(slots.disableSelection).toHaveBeenCalled();
    });

    it('WHEN sortable methods are called THEN appropriate callbacks will be called', function() {
        // Arrange
        spyOn(dragAndDropService, 'cancel').andReturn();
        spyOn(dragAndDropService, '_removeDynamicStyles').andReturn();
        spyOn(dragAndDropService, 'updateSlotsPositionCaching').andReturn();

        var jQueryElem = jasmine.createSpyObj("jQueryElem", ["closest"]);
        jQueryElem.closest.andReturn([slots]);

        var ui = {
            item: jQueryElem
        };
        var event = {
            target: slots
        };

        // Act
        dragAndDropService.registerSortable(configuration);

        // Assert
        expect(dragAndDropScrollingService.enableScrolling).not.toHaveBeenCalled();
        expect(slots.addClass).not.toHaveBeenCalledWith('eligible-slot');
        sortableMock.start(event, ui);
        expect(configuration.startCallback).toHaveBeenCalledWith(event, ui);
        expect(slots.addClass).toHaveBeenCalledWith('eligible-slot');
        expect(dragAndDropService.updateSlotsPositionCaching).toHaveBeenCalled();
        expect(dragAndDropScrollingService.enableScrolling).toHaveBeenCalledWith(false, false);

        sortableMock.over(event, ui);
        expect(configuration.overCallback).toHaveBeenCalledWith(event, ui);

        sortableMock.out(event, ui);
        expect(configuration.outCallback).toHaveBeenCalledWith(event, ui);

        sortableMock.update(event, ui);
        expect(configuration.dropCallback).toHaveBeenCalledWith(event, ui, jasmine.any(Function));

        expect(helper.remove).not.toHaveBeenCalled();
        expect(dragAndDropScrollingService.disableScrolling).not.toHaveBeenCalled();
        expect(dragAndDropService._removeDynamicStyles).not.toHaveBeenCalled();
        sortableMock.stop(event, ui);
        expect(dragAndDropScrollingService.disableScrolling).toHaveBeenCalledWith(false, false);
        expect(dragAndDropService._removeDynamicStyles).toHaveBeenCalled();
        expect(configuration.stopCallback).toHaveBeenCalledWith(event, ui);

        configuration.dropCallback.calls[0].args[2]();
        expect(dragAndDropService.cancel).toHaveBeenCalledWith('sourceSelector', [slots], event, ui);
    });

    it('WHEN registerDraggable is called THEN draggables will be registered on outer elements', function() {
        // Arrange

        // Act
        dragAndDropService.registerDrag(configuration);

        // Assert
        expect(draggableElements.draggable).toHaveBeenCalledWith(jasmine.objectContaining({
            revert: false,
            iframeFix: true,
            scroll: false,
            cursorAt: {
                left: 0,
                top: 0
            },
            connectToSortable: slots
        }));
    });

    it('WHEN sortable methods are called THEN appropriate callbacks will be called', function() {
        // Arrange
        spyOn(dragAndDropService, '_removeDynamicStyles').andReturn();
        spyOn(dragAndDropService, 'updateSlotsPositionCaching').andReturn();

        var jQueryElem = jasmine.createSpyObj("jQueryElem", ["closest"]);

        var ui = {
            item: jQueryElem,
            helper: [helper]
        };
        var event = {
            target: slots
        };

        ui.helper.detach = helper.detach;
        ui.helper.appendTo = helper.appendTo;

        // Act
        dragAndDropService.registerDrag(configuration);

        // Assert
        expect(dragAndDropScrollingService.enableScrolling).not.toHaveBeenCalled();
        expect(configuration.startCallback).not.toHaveBeenCalledWith(event, ui);
        draggableMock.start(event, ui);
        expect(configuration.startCallback).toHaveBeenCalledWith(event, ui);
        expect(dragAndDropScrollingService.enableScrolling).toHaveBeenCalledWith(true, true);
        expect(dragAndDropService.updateSlotsPositionCaching).toHaveBeenCalled();

        draggableMock.drag(event, ui);

        expect(dragAndDropScrollingService.disableScrolling).not.toHaveBeenCalled();
        expect(configuration.stopCallback).not.toHaveBeenCalledWith(event, ui);
        draggableMock.stop(event, ui);
        expect(configuration.stopCallback).toHaveBeenCalledWith(event, ui);
        expect(dragAndDropScrollingService.disableScrolling).toHaveBeenCalledWith(true, true);
        expect(dragAndDropService._removeDynamicStyles).toHaveBeenCalled();
    });

    it('WHEN dragAndDropService.registerSortable is called THEN helper shall be wrapped in div', function() {
        // Arrange
        var event = "event";
        var expectedInnerContent = "Some content";
        var element = jasmine.createSpyObj('element', ['html']);

        helper.prop.andReturn(expectedInnerContent);
        configuration.helper = function() {
            return helper;
        };
        spyOn(configuration, "helper").andCallThrough();
        spyOn(dragAndDropService, "_copyElementProperties").andReturn();

        // Act
        dragAndDropService.registerSortable(configuration);

        // Assert
        var result = sortableMock.helper(event, element);
        expect(configuration.helper).toHaveBeenCalledWith(event, element);
        expect(dragAndDropService._copyElementProperties).toHaveBeenCalledWith(element, helper);
        expect(result[0].innerHTML).toEqual(expectedInnerContent);
    });

    it('WHEN dragAndDropService.registerSortable is called with no helper THEN element shall be wrapped in div', function() {
        // Arrange
        var event = "event";
        var htmlBody = "someHtmlBody";
        var element = jasmine.createSpyObj('element', ['html']);
        element.html.andReturn(htmlBody);
        configuration.helper = null;

        // Act
        dragAndDropService.registerSortable(configuration);

        // Assert
        var result = sortableMock.helper(event, element);
        expect(result[0].innerHTML).toEqual(htmlBody);
    });

    it('WHEN dragAndDropService.registerDrag is called THEN helper shall be wrapped in div', function() {
        // Arrange
        var event = "event";
        var expectedInnerContent = "Some content";
        var element = jasmine.createSpyObj('element', ['html', 'removeAttr', 'addClass', 'removeClass']);
        element.removeAttr.andReturn(element);

        helper.prop.andReturn(expectedInnerContent);
        configuration.helper = function() {
            return helper;
        };
        spyOn(configuration, "helper").andCallThrough();
        spyOn(dragAndDropService, "_copyElementProperties").andReturn();
        spyOn(dragAndDropService, 'cloneElement').andReturn(element);

        // Act
        dragAndDropService.registerDrag(configuration);

        // Assert
        var result = draggableMock.helper(event, element);
        expect(configuration.helper).toHaveBeenCalled();
        expect(dragAndDropService._copyElementProperties).toHaveBeenCalledWith(element, helper);
        expect(result[0].innerHTML).toEqual(expectedInnerContent);
    });

    it('WHEN dragAndDropService.registerDrag is called with no helper THEN element shall be cloned in div', function() {
        // Arrange
        var event = "event";
        var ui = "ui";
        configuration.helper = null;
        var expectedInnerContent = "some content";

        var element = jasmine.createSpyObj('element', ['html', 'removeAttr', 'addClass', 'removeClass']);
        element.html.andReturn(expectedInnerContent);
        element.removeAttr.andReturn(element);

        spyOn(dragAndDropService, 'cloneElement').andReturn(element);

        // Act
        dragAndDropService.registerDrag(configuration);

        // Assert
        var result = draggableMock.helper(event, ui);
        expect(dragAndDropService.cloneElement).toHaveBeenCalledWith(draggableElements);
        expect(element.removeAttr).toHaveBeenCalledWith('id');
        expect(element.removeClass).toHaveBeenCalledWith('ui-draggable ui-draggable-handle');
        expect(element.addClass).toHaveBeenCalledWith('ui-sortable-handle');
        expect(result[0].innerHTML).toEqual(expectedInnerContent);
    });

    it('when _removeDynamicStyles any remaining helper and dynamic styles will be removed', function() {
        // Arrange
        spyOn(dragAndDropService, '_removeDynamicStyles').andCallThrough();
        spyOn(dragAndDropService, '_removeHelpers').andCallThrough();

        expect(dragAndDropService._removeHelpers).not.toHaveBeenCalled();
        expect(slots.removeClass).not.toHaveBeenCalledWith('eligible-slot');

        // Act
        dragAndDropService._removeDynamicStyles(configuration);

        // Assert
        expect(dragAndDropService._removeHelpers).toHaveBeenCalled();
        expect(slots.removeClass).toHaveBeenCalledWith('eligible-slot');
    });

    it('WHEN unregister is called THEN service is cleaned properly', function() {
        // Arrange
        var configurationID = "someID";
        var configuration = jasmine.createSpyObj('configuration', ['_unbindWatcher']);
        configuration.id = configurationID;

        dragAndDropService.configurations = {
            "someID": configuration
        };

        var draggable = jasmine.createSpyObj('draggable', ['draggable']);
        dragAndDropService.getSelector.andReturn(draggable);

        var sortable = jasmine.createSpyObj('sortable', ['sortable']);
        dragAndDropService.getSelectorInIframe.andReturn(sortable);

        spyOn(dragAndDropService, '_clearVariables');
        expect(Object.keys(dragAndDropService.configurations).length).toBe(1);

        // Act
        dragAndDropService.unregister([configurationID]);

        // Assert
        expect(draggable.draggable).toHaveBeenCalledWith("destroy");
        expect(sortable.sortable).toHaveBeenCalledWith("destroy");
        expect(configuration._unbindWatcher).toHaveBeenCalled();
        expect(dragAndDropService._clearVariables).toHaveBeenCalled();
        expect(Object.keys(dragAndDropService.configurations).length).toBe(0);
    });

    // Helper Functions
    function loadConfiguration() {
        startCallback = jasmine.createSpy('startCallback');
        overCallback = jasmine.createSpy('overCallback');
        outCallback = jasmine.createSpy('outCallback');
        dropCallback = jasmine.createSpy('dropCallback');
        stopCallback = jasmine.createSpy('stopCallback');

        return {
            id: 'someId',
            sourceSelector: sourceSelector,
            targetSelector: targetSelector,
            startCallback: startCallback,
            overCallback: overCallback,
            outCallback: outCallback,
            dropCallback: dropCallback,
            stopCallback: stopCallback
        };
    }

    function createFakePageContent() {
        body = jasmine.createSpyObj("pageBody", ['append']);
        iFrame = jasmine.createSpyObj("iFrame", ['append', 'contents']);
        iFrameBody = jasmine.createSpyObj('iFrameBody', ['append', 'bind', 'addClass', 'removeClass']);
        slots = jasmine.createSpyObj('slots', ['sortable', 'disableSelection', 'addClass', 'removeClass']);
        draggableElements = jasmine.createSpyObj('draggableElements', ['draggable']);

        helper = jasmine.createSpyObj('helperSelector', ['remove', 'detach', 'appendTo', 'style', 'prop']);
        helper.detach.andReturn(helper);
    }

    function createSortableMock() {
        sortableMock = {};
        slots.sortable.andCallFake(function(arg) {
            if (arg === 'refresh' || arg === 'refreshPositions') {
                return arg;
            } else if (arg) {
                sortableMock.start = arg.start;
                sortableMock.stop = arg.stop;
                sortableMock.over = arg.over;
                sortableMock.update = arg.update;
                sortableMock.out = arg.out;
                sortableMock.helper = arg.helper;

                return sortableMock;
            }

            return null;
        });
    }

    function createDraggableMock() {
        draggableMock = {};

        draggableElements.draggable.andCallFake(function(arg) {
            if (arg) {
                draggableMock.start = arg.start;
                draggableMock.drag = arg.drag;
                draggableMock.stop = arg.stop;
                draggableMock.helper = arg.helper;
            }
        });
    }
});
