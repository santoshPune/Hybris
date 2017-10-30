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
describe('renderService', function() {

    // Mocks and Injected Services
    var $window, $q, $rootScope, $http, $location;
    var alertService, componentHandlerService, extractFromElement, gatewayFactory, gatewayProxy, unsafeParseHTML, renderGateway,
        renderService, RenderServiceInterface, sakExecutor, smartEditBootstrapGateway, perspectiveService;

    // Mock Test Data
    var COMPONENT_ID_ATTRIBUTE = 'data-smartedit-component-id';
    var COMPONENT_TYPE_ATTRIBUTE = 'data-smartedit-component-type';
    var COMPONENT_ID = 'someComponentId';
    var COMPONENT_TYPE = 'someComponentType';
    var SLOT_ID_1 = 'slot1';
    var SLOT_ID_2 = 'slot2';

    beforeEach(customMatchers);

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener', 'createGateway']);
        smartEditBootstrapGateway = jasmine.createSpyObj('renderGateway', ['publish']);

        gatewayFactory.createGateway.andCallFake(function(gatewayId) {
            if (gatewayId === 'smartEditBootstrap') {
                return smartEditBootstrapGateway;
            }
        });
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('sakExecutorDecorator', function($provide) {
        sakExecutor = jasmine.createSpyObj('sakExecutor', ['resetCounters', 'areAllDecoratorsProcessed', 'destroyAllScopes']);
        $provide.value('sakExecutor', sakExecutor);
    }));

    beforeEach(module('alertServiceModule', function($provide) {
        alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);
        $provide.value('alertService', alertService);
    }));

    beforeEach(module('componentHandlerServiceModule', function($provide) {
        componentHandlerService = jasmine.createSpyObj('componentHandlerService', ['getFromSelector', 'getOverlay', 'getParent', 'getComponentUnderSlot', 'getComponent', 'getOriginalComponent', 'getComponentInOverlay', 'getComponentUnderParentOverlay']);
        $provide.value('componentHandlerService', componentHandlerService);
    }));

    beforeEach(module('perspectiveServiceModule', function($provide) {
        perspectiveService = jasmine.createSpyObj('perspectiveService', ['isEmptyPerspectiveActive']);
        perspectiveService.isEmptyPerspectiveActive.andReturn();
        $provide.value('perspectiveService', perspectiveService);
    }));

    beforeEach(module('renderServiceModule', function($provide) {
        $http = jasmine.createSpy('$http');
        $provide.value('$http', $http);

        $location = jasmine.createSpyObj('$location', ['absUrl']);
        $provide.value('$location', $location);

        unsafeParseHTML = jasmine.createSpy('unsafeParseHTML');
        $provide.value('unsafeParseHTML', unsafeParseHTML);

        renderGateway = jasmine.createSpyObj('renderGateway', ['subscribe']);
        $provide.value('renderGateway', renderGateway);

        extractFromElement = jasmine.createSpy('extractFromElement');
        $provide.value('extractFromElement', extractFromElement);
    }));

    beforeEach(inject(function(_RenderServiceInterface_, _renderService_, _$q_, _$window_, _$rootScope_) {
        RenderServiceInterface = _RenderServiceInterface_;
        renderService = _renderService_;
        $window = _$window_;
        $q = _$q_;
        $rootScope = _$rootScope_;

        spyOn(renderService, "_reprocessPage");
    }));

    describe('<<init>>', function() {
        beforeEach(function() {
            spyOn(renderService, "renderPage");
        });

        it('implements the RenderServiceInterface interface', function() {
            expect(renderService.renderSlots).toBeDefined();
            expect(renderService.renderComponent).toBeDefined();
            expect(renderService.renderRemoval).toBeDefined();
            expect(renderService.renderPage).toBeDefined();
            expect(renderService.toggleOverlay).toBeDefined();
            expect(renderService.refreshOverlayDimensions).toBeDefined();
        });

        it('invokes gatewayProxy', function() {
            expect(renderService.gatewayId).toBe("Renderer");
            expect(gatewayProxy.initForService).toHaveBeenCalledWith(renderService, ["renderSlots", "renderComponent", "renderRemoval", "toggleOverlay", "refreshOverlayDimensions", "renderPage"]);
        });

        it('subscribes to the "rerender" event of renderGateway in order to renderPage', function() {
            expect(renderGateway.subscribe).toHaveBeenCalledWith("rerender", jasmine.any(Function));
            var reRenderListener = renderGateway.subscribe.calls[0].args[1];
            reRenderListener('eventId', 'someBooleanValue');
            expect(renderService.renderPage).toHaveBeenCalledWith('someBooleanValue');
        });
    });

    describe('renderRemoval', function() {
        var element;
        var actual;

        beforeEach(function() {
            element = jasmine.createSpyObj('element', ['remove']);
        });

        beforeEach(function() {
            componentHandlerService.getComponentUnderSlot.andReturn(element);
            spyOn(renderService, "refreshOverlayDimensions");
        });

        beforeEach(function() {
            actual = renderService.renderRemoval(COMPONENT_ID, COMPONENT_TYPE, SLOT_ID_1);
        });

        it('should remove the element', function() {
            expect(componentHandlerService.getComponentUnderSlot).toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE, SLOT_ID_1, null);
            expect(element.remove).toHaveBeenCalled();
        });

        it('should refresh overlay dimensions', function() {
            expect(renderService.refreshOverlayDimensions).toHaveBeenCalled();
        });
    });

    describe('renderComponent', function() {
        var slotElement, componentElement, renderSlotsPromise;

        beforeEach(function() {
            slotElement = jasmine.createSpyObj('slotElement', ['attr']);
            slotElement.attr.andReturn(SLOT_ID_1);
            componentElement = {};
            renderSlotsPromise = {};

            spyOn(renderService, 'renderSlots').andReturn(renderSlotsPromise);
            componentHandlerService.getComponent.andReturn(componentElement);
            componentHandlerService.getParent.andReturn(slotElement);
        });

        it('extracts the slot ID for the given component ID and type', function() {
            renderService.renderComponent(COMPONENT_ID, COMPONENT_TYPE);
            expect(componentHandlerService.getComponent).toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE);
            expect(componentHandlerService.getParent).toHaveBeenCalledWith(componentElement);
            expect(slotElement.attr).toHaveBeenCalledWith("data-smartedit-component-id");
        });

        it('delegates to renderSlots', function() {
            expect(renderService.renderComponent(COMPONENT_ID, COMPONENT_TYPE)).toBe(renderSlotsPromise);
            expect(renderService.renderSlots).toHaveBeenCalledWith(SLOT_ID_1);
        });
    });

    describe('renderSlots', function() {
        var EXPECTED_EXCEPTION_NO_SLOT_IDS = 'renderService.renderSlots.slotIds.required';
        var CURRENT_URL = 'currentUrl';
        var actual;

        beforeEach(function() {
            spyOn(renderService, "renderPage");
        });

        describe('when no slot ids are provided', function() {
            beforeEach(function() {
                actual = renderService.renderSlots();
            });

            it('returns a rejected promise with an error message', function() {
                expect(actual).toBeRejectedWithData(EXPECTED_EXCEPTION_NO_SLOT_IDS);
            });

            it('should not fetch the page', function() {
                expect($http).not.toHaveBeenCalled();
            });

            it('should not reprocess and render the page', function() {
                expect(renderService._reprocessPage).not.toHaveBeenCalled();
                expect(renderService.renderPage).not.toHaveBeenCalled();
            });
        });

        describe('when an empty array of slot ids is provided', function() {
            beforeEach(function() {
                actual = renderService.renderSlots([]);
            });

            it('returns a rejected promise with an error message', function() {
                expect(actual).toBeRejectedWithData(EXPECTED_EXCEPTION_NO_SLOT_IDS);
            });

            it('should not fetch the page when an empty array of slot ids is provided', function() {
                expect($http).not.toHaveBeenCalled();
            });

            it('should not reprocess and render the page when an empty array of slot ids is provided', function() {
                expect(renderService._reprocessPage).not.toHaveBeenCalled();
                expect(renderService.renderPage).not.toHaveBeenCalled();
            });
        });

        describe('when http request fails', function() {
            var EXPECTED_HTML_ERROR_MESSAGE = 'ExpectedHTMLErrorMessage';
            var actual;

            beforeEach(function() {
                $location.absUrl.andReturn(CURRENT_URL);
                $http.andReturn($q.reject({
                    message: EXPECTED_HTML_ERROR_MESSAGE
                }));
                actual = renderService.renderSlots(SLOT_ID_1);
                $rootScope.$digest();
            });

            it('should spawn an alert', function() {
                expect(alertService.pushAlerts).toHaveBeenCalledWith([{
                    successful: false,
                    message: EXPECTED_HTML_ERROR_MESSAGE,
                    closeable: true
                }]);
            });

            it('should not reprocess and render the page', function() {
                expect(renderService._reprocessPage).not.toHaveBeenCalled();
                expect(renderService.renderPage).not.toHaveBeenCalled();
            });

            it('should return a rejected promise with the message in the error response', function() {
                expect(actual).toBeRejectedWithData(EXPECTED_HTML_ERROR_MESSAGE);
            });
        });

        describe('when multiple slot IDs are provided and http request succeeds', function() {
            var EXPECTED_SLOT_1_SELECTOR = ".smartEditComponent" +
                "[data-smartedit-component-type='ContentSlot']" +
                "[data-smartedit-component-id='" + SLOT_ID_1 + "']";
            var EXPECTED_SLOT_2_SELECTOR = ".smartEditComponent" +
                "[data-smartedit-component-type='ContentSlot']" +
                "[data-smartedit-component-id='" + SLOT_ID_2 + "']";
            var EXPECTED_FETCHED_SLOT_1_HTML = 'html1';
            var EXPECTED_FETCHED_SLOT_2_HTML = 'html2';
            var EXPECTED_HTTP_REQUEST_OBJECT = {
                method: 'GET',
                url: CURRENT_URL,
                headers: {
                    Pragma: 'no-cache'
                }
            };

            var successHtmlResponse, root;
            var originalSlotToReplace1, originalSlotToReplace2, fetchedSlotToRender1, fetchedSlotToRender2;
            var actual;

            beforeEach(function() {
                originalSlotToReplace1 = jasmine.createSpyObj('originalSlotToReplace1', ['html']);
                originalSlotToReplace2 = jasmine.createSpyObj('originalSlotToReplace2', ['html']);
                fetchedSlotToRender1 = jasmine.createSpyObj('fetchedSlotToRender1', ['html']);
                fetchedSlotToRender2 = jasmine.createSpyObj('fetchedSlotToRender2', ['html']);
            });

            beforeEach(function() {
                successHtmlResponse = {
                    data: '<html>some html</html>'
                };
                root = {};
                $location.absUrl.andReturn(CURRENT_URL);
                $http.andReturn($q.when(successHtmlResponse));
                unsafeParseHTML.andReturn(root);
                extractFromElement.andCallFake(function(root, selector) {
                    if (selector === EXPECTED_SLOT_1_SELECTOR) {
                        return fetchedSlotToRender1;
                    } else if (selector === EXPECTED_SLOT_2_SELECTOR) {
                        return fetchedSlotToRender2;
                    }
                });
                componentHandlerService.getFromSelector.andCallFake(function(selector) {
                    if (selector === EXPECTED_SLOT_1_SELECTOR) {
                        return originalSlotToReplace1;
                    } else if (selector === EXPECTED_SLOT_2_SELECTOR) {
                        return originalSlotToReplace2;
                    }
                });
                fetchedSlotToRender1.html.andReturn(EXPECTED_FETCHED_SLOT_1_HTML);
                fetchedSlotToRender2.html.andReturn(EXPECTED_FETCHED_SLOT_2_HTML);
                actual = renderService.renderSlots([SLOT_ID_1, SLOT_ID_2]);
                $rootScope.$digest();
            });

            it('should call the $http service with the current page URL', function() {
                expect($http).toHaveBeenCalledWith(EXPECTED_HTTP_REQUEST_OBJECT);
            });

            it('should parse the response data', function() {
                expect(unsafeParseHTML.calls.length).toBe(1);
                expect(unsafeParseHTML).toHaveBeenCalledWith(successHtmlResponse.data);
            });

            it('should extract the slots to re-render from the DOM of the fetched page', function() {
                expect(extractFromElement.calls.length).toBe(2);
                expect(extractFromElement.calls[0].args[0]).toBe(root);
                expect(extractFromElement.calls[0].args[1]).toBe(EXPECTED_SLOT_1_SELECTOR);
                expect(extractFromElement.calls[1].args[0]).toBe(root);
                expect(extractFromElement.calls[1].args[1]).toBe(EXPECTED_SLOT_2_SELECTOR);
            });

            it('should get the existing slots elements to replace from the current storefront', function() {
                expect(componentHandlerService.getFromSelector.calls.length).toBe(2);
                expect(componentHandlerService.getFromSelector.calls[0].args[0]).toBe(EXPECTED_SLOT_1_SELECTOR);
                expect(componentHandlerService.getFromSelector.calls[1].args[0]).toBe(EXPECTED_SLOT_2_SELECTOR);
            });

            it('should fetch the html of the new slot elements', function() {
                expect(fetchedSlotToRender1.html).toHaveBeenCalled();
                expect(fetchedSlotToRender2.html).toHaveBeenCalled();
            });

            it('should replace the html of the existing slot elements with that of the new slot elements', function() {
                expect(originalSlotToReplace1.html).toHaveBeenCalledWith(EXPECTED_FETCHED_SLOT_1_HTML);
                expect(originalSlotToReplace2.html).toHaveBeenCalledWith(EXPECTED_FETCHED_SLOT_2_HTML);
            });

            it('should re-process the page responsiveness', function() {
                expect(renderService._reprocessPage.calls.length).toBe(1);
            });

            it('should render the page', function() {
                expect(renderService.renderPage.calls.length).toBe(1);
                expect(renderService.renderPage).toHaveBeenCalledWith(true);
            });

            it('should return a resolved promise', function() {
                expect(actual).toBeResolved();
            });
        });
    });

    describe('_registerPageReadyListener', function() {
        var watchDestroyFunction;

        beforeEach(function() {
            spyOn(renderService, '_markSmartEditAsReady');
            watchDestroyFunction = jasmine.createSpy('watchDestroyFunction');
            spyOn($rootScope, '$watch').andReturn(watchDestroyFunction);
            renderService._registerPageReadyListener();
        });

        it('should register a watcher on the root scope', function() {
            expect($rootScope.$watch).toHaveBeenCalledWith(jasmine.any(Function), jasmine.any(Function));
        });

        it('should watch for the decorator processing to complete', function() {
            expect($rootScope.$watch.calls[0].args[0]).toBe(sakExecutor.areAllDecoratorsProcessed);
        });

        it('should not mark SmartEdit is ready upon registering the listener', function() {
            expect(renderService._markSmartEditAsReady).not.toHaveBeenCalled();
            expect(watchDestroyFunction).not.toHaveBeenCalled();
        });

        it('should register a callback which should not mark SmartEdit as ready while the decorators are still processing', function() {
            var watchCallback = $rootScope.$watch.calls[0].args[1];
            watchCallback(false);
            expect(renderService._markSmartEditAsReady).not.toHaveBeenCalled();
            expect(watchDestroyFunction).not.toHaveBeenCalled();
        });

        it('should register a callback which marks smartedit as ready when the decorators are finished processing', function() {
            var watchCallback = $rootScope.$watch.calls[0].args[1];
            watchCallback(true);
            expect(renderService._markSmartEditAsReady).toHaveBeenCalled();
            expect(watchDestroyFunction).toHaveBeenCalled();
        });
    });

    describe('_markSmartEditAsReady', function() {
        var body;

        beforeEach(function() {
            body = jasmine.createSpyObj('body', ['attr']);
            componentHandlerService.getFromSelector.andReturn(body);
            renderService._markSmartEditAsReady();
        });

        it('should publish smartEditReady event on the smartEditBootstrap gateway', function() {
            expect(smartEditBootstrapGateway.publish).toHaveBeenCalledWith('smartEditReady');
        });

        it('should attach the data-smartedit-ready attribute to the body of the storefront', function() {
            expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith('body');
            expect(body.attr).toHaveBeenCalledWith('data-smartedit-ready', 'true');
        });
    });

    describe('toggleOverlay', function() {
        var overlay;

        beforeEach(function() {
            overlay = jasmine.createSpyObj('overlay', ['css']);
            componentHandlerService.getOverlay.andReturn(overlay);
        });

        it('should make the SmartEdit overlay visible when passed true', function() {
            renderService.toggleOverlay(true);
            expect(overlay.css).toHaveBeenCalledWith("visibility", "visible");
        });

        it('should make the SmartEdit overlay hidden when passed false', function() {
            renderService.toggleOverlay(false);
            expect(overlay.css).toHaveBeenCalledWith("visibility", "hidden");
        });
    });

    describe('refreshOverlayDimensions', function() {
        var BODY_TAG = 'body';

        var element, wrappedElement, parentOverlay, originalComponents;

        beforeEach(function() {
            element = {};
            parentOverlay = {};
            originalComponents = jasmine.createSpyObj('originalComponents', ['each']);
        });

        beforeEach(function() {
            componentHandlerService.getFromSelector.andCallFake(function(element) {
                if (element === 'body') {
                    return element;
                } else if (element === element) {
                    return wrappedElement;
                }
            });
            spyOn(renderService, '_getFirstChildren').andReturn(originalComponents);
            spyOn(renderService, '_getContainingOverlay').andReturn(parentOverlay);
            spyOn(renderService, '_updateOverlayDimensions');
        });

        it('should fetch the body as the element when given no parameters', function() {
            renderService.refreshOverlayDimensions();
            expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(BODY_TAG);
        });

        it('should use the element provided instead of the body when provided with an element', function() {
            renderService.refreshOverlayDimensions(element);
            expect(componentHandlerService.getFromSelector).not.toHaveBeenCalled();
        });

        it('should update the overlay dimensions for each child', function() {
            renderService.refreshOverlayDimensions(element);
            spyOn(renderService, 'refreshOverlayDimensions');
            var eachCallback = originalComponents.each.calls[0].args[0];
            eachCallback(0, element);

            expect(renderService._updateOverlayDimensions).toHaveBeenCalledWith(wrappedElement, null, parentOverlay);
        });

        it('should recursively call refresh overlay dimensions for each child', function() {
            renderService.refreshOverlayDimensions(element);
            spyOn(renderService, 'refreshOverlayDimensions');
            var eachCallback = originalComponents.each.calls[0].args[0];
            eachCallback(0, element);

            expect(renderService.refreshOverlayDimensions).toHaveBeenCalledWith(wrappedElement);
        });
    });

    describe('_mustRedraw', function() {
        var MOCK_COMPONENT_IN_OVERLAY, MOCK_EMPTY_COMPONENT_IN_OVERLAY;

        beforeEach(function() {
            MOCK_COMPONENT_IN_OVERLAY = {
                length: 1
            };
            MOCK_EMPTY_COMPONENT_IN_OVERLAY = {
                length: 0
            };
        });

        it('should return false if componentInOverlay is not empty and redraw is false', function() {
            expect(renderService._mustRedraw(MOCK_COMPONENT_IN_OVERLAY, false)).toBe(false);
        });

        it('should return true if componentInOverlay is empty and redraw is false', function() {
            expect(renderService._mustRedraw(MOCK_EMPTY_COMPONENT_IN_OVERLAY, false)).toBe(true);
        });

        it('should return true if componentInOverlay is not empty and redraw is true', function() {
            expect(renderService._mustRedraw(MOCK_COMPONENT_IN_OVERLAY, true)).toBe(true);
        });

        it('should return true if componentInOverlay is empty and redraw is true', function() {
            expect(renderService._mustRedraw(MOCK_EMPTY_COMPONENT_IN_OVERLAY, true)).toBe(true);
        });
    });

    describe('_updateOverlayDimensions', function() {
        var COMPONENT_ID_ATTRIBUTE = 'data-smartedit-component-id';
        var COMPONENT_TYPE_ATTRIBUTE = 'data-smartedit-component-type';
        var ORIGINAL_ELEMENT_BOUNDING_CLIENT_RECT = {
            top: 123,
            left: 456
        };
        var PARENT_ELEMENT_BOUNDING_CLIENT_RECT = {
            top: 5,
            left: 10
        };

        var componentInOverlay, componentInOverlayList, originalElement, parentOverlay, unwrappedOriginalElement, unwrappedParentOverlay;

        beforeEach(function() {
            componentInOverlay = {};
            componentInOverlay.style = {};
            componentInOverlayList = jasmine.createSpyObj('componentInOverlayList', ['get']);
            originalElement = jasmine.createSpyObj('originalElement', ['attr', 'get']);
            parentOverlay = jasmine.createSpyObj('parentOverlay', ['get']);
            unwrappedOriginalElement = jasmine.createSpyObj('unwrappedOriginalElement', ['getBoundingClientRect']);
            unwrappedParentOverlay = jasmine.createSpyObj('unwrappedParentOverlay', ['getBoundingClientRect']);
        });

        beforeEach(function() {
            unwrappedOriginalElement.getBoundingClientRect.andReturn(ORIGINAL_ELEMENT_BOUNDING_CLIENT_RECT);
            unwrappedOriginalElement.offsetHeight = 789;
            unwrappedOriginalElement.offsetWidth = 890;

            originalElement.attr.andCallFake(function(attribute) {
                if (attribute === COMPONENT_ID_ATTRIBUTE) {
                    return COMPONENT_ID;
                } else if (attribute === COMPONENT_TYPE_ATTRIBUTE) {
                    return COMPONENT_TYPE;
                }
            });
            originalElement.get.andReturn(unwrappedOriginalElement);
            unwrappedParentOverlay.getBoundingClientRect.andReturn(PARENT_ELEMENT_BOUNDING_CLIENT_RECT);
            parentOverlay.get.andReturn(unwrappedParentOverlay);
            componentInOverlayList.get.andReturn(componentInOverlay);
            componentHandlerService.getComponentInOverlay.andReturn(componentInOverlayList);
        });

        it('should fetch the component in overlay if it is not provided', function() {
            renderService._updateOverlayDimensions(originalElement, null, parentOverlay);
            expect(componentHandlerService.getComponentInOverlay).toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE);
            expect(componentInOverlayList.get).toHaveBeenCalled();
        });

        it('should not fetch the component in overlay if it is provided', function() {
            renderService._updateOverlayDimensions(originalElement, componentInOverlay, parentOverlay);
            expect(componentHandlerService.getComponentInOverlay).not.toHaveBeenCalled();
            expect(componentInOverlayList.get).not.toHaveBeenCalled();
        });

        it('should update position and dimensions of the given overlay clone from the original component', function() {
            renderService._updateOverlayDimensions(originalElement, componentInOverlay, parentOverlay);
            expect(componentInOverlay.style).toEqual({
                position: 'absolute',
                top: '118px',
                left: '446px',
                width: '890px',
                height: '789px',
                minWidth: '51px',
                minHeight: '49px'
            });
        });
    });

    describe('_cloneAndCompileComponent', function() {
        var EXPECTED_SHALLOW_COPY_ID = 'someComponentId_someComponentType_overlay';
        var EXPECTED_Z_INDEX_FOR_NAVIGATION = '7';
        var EXPECTED_OVERLAY_CLASS = 'smartEditComponentX';

        var componentInOverlay, element, parentOverlay, document, shallowCopy, smartEditWrapper, componentDecorator, compiled;

        beforeEach(function() {
            componentInOverlay = jasmine.createSpyObj('componentInOverlay', ['remove']);
            element = jasmine.createSpyObj('element', ['attr', 'get']);
            parentOverlay = jasmine.createSpyObj('parentOverlay', ['append']);
            document = jasmine.createSpyObj('document', ['createElement']);
            shallowCopy = {};
            smartEditWrapper = {};
            smartEditWrapper.style = {};
            componentDecorator = jasmine.createSpyObj('componentDecorator', ['addClass', 'attr', 'append']);
            compiled = {};
        });

        beforeEach(function() {
            element.attr.andCallFake(function(attribute) {
                if (attribute === COMPONENT_ID_ATTRIBUTE) {
                    return COMPONENT_ID;
                } else if (attribute === COMPONENT_TYPE_ATTRIBUTE) {
                    return COMPONENT_TYPE;
                }
            });
            element.get.andReturn({
                attributes: [{
                    nodeName: 'nonsmarteditattribute',
                    nodeValue: 'somevalue'
                }, {
                    nodeName: 'data-smartedit-component-id',
                    nodeValue: COMPONENT_ID
                }, {
                    nodeName: 'data-smartedit-component-type',
                    nodeValue: COMPONENT_TYPE
                }]
            });

            var callCount = 0;
            document.createElement.andCallFake(function() {
                callCount++;
                return callCount === 1 ? shallowCopy : smartEditWrapper;
            });

            componentHandlerService.getComponentUnderParentOverlay.andReturn(componentInOverlay);
            componentHandlerService.getFromSelector.andReturn(componentDecorator);

            spyOn(renderService, '_getDocument').andReturn(document);
            spyOn(renderService, '_updateOverlayDimensions');
            spyOn(renderService, '_compile').andReturn(compiled);
        });

        describe('when re-draw condition is false', function() {
            beforeEach(function() {
                spyOn(renderService, '_mustRedraw').andReturn(false);
            });

            beforeEach(function() {
                renderService._cloneAndCompileComponent(element, parentOverlay, false);
            });

            it('should fetch the component overlay under the provided parent overlay', function() {
                renderService._cloneAndCompileComponent(element, parentOverlay, false);
                expect(componentHandlerService.getComponentUnderParentOverlay)
                    .toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE, parentOverlay);
            });

            it('should not append an overlay to the parent overlay', function() {
                expect(parentOverlay.append).not.toHaveBeenCalled();
            });
        });

        describe('when re-draw condition is true', function() {
            beforeEach(function() {
                spyOn(renderService, '_mustRedraw').andReturn(true);
            });

            beforeEach(function() {
                renderService._cloneAndCompileComponent(element, parentOverlay, true);
            });

            it('should fetch the component overlay under the provided parent overlay', function() {
                renderService._cloneAndCompileComponent(element, parentOverlay, false);
                expect(componentHandlerService.getComponentUnderParentOverlay)
                    .toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE, parentOverlay);
            });

            it('should remove the component overlay if forced to re-draw', function() {
                expect(componentInOverlay.remove).toHaveBeenCalled();
            });

            it('should create a shallow copy of the component and a SmartEdit decorator wrapper', function() {
                expect(renderService._getDocument.calls.length).toBe(2);
                expect(document.createElement.calls.length).toBe(2);
                expect(document.createElement.calls[0].args[0]).toBe('div');
                expect(document.createElement.calls[1].args[0]).toBe('div');
            });

            it('should add an overlay identifier to the shallow copy element', function() {
                expect(shallowCopy.id).toBe(EXPECTED_SHALLOW_COPY_ID);
            });

            it('should update the overlay dimensions for the newly created SmartEdit decorator wrapper', function() {
                expect(renderService._updateOverlayDimensions).toHaveBeenCalledWith(element, smartEditWrapper, parentOverlay);
            });

            it('should fetch a wrapped SmartEdit decorator wrapper', function() {
                expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(smartEditWrapper);
            });

            it('should add the overlay class to the wrapped SmartEdit decorator wrapper', function() {
                expect(componentDecorator.addClass).toHaveBeenCalledWith(EXPECTED_OVERLAY_CLASS);
            });

            it('should copy attributes with "data-smartedit" prefix from the element to the wrapped SmartEdit decorator wrapper', function() {
                expect(element.get).toHaveBeenCalledWith(0);
                expect(componentDecorator.attr.calls.length).toBe(2);
                expect(componentDecorator.attr.calls[0].args).toEqual(['data-smartedit-component-id', COMPONENT_ID]);
                expect(componentDecorator.attr.calls[1].args).toEqual(['data-smartedit-component-type', COMPONENT_TYPE]);
            });

            it('should append the shallow copy onto the SmartEdit decorator wrapper', function() {
                expect(componentDecorator.append).toHaveBeenCalledWith(shallowCopy);
            });

            it('should compile the SmartEdit decorator wrapper against the root scope', function() {
                expect(renderService._compile).toHaveBeenCalledWith(smartEditWrapper, $rootScope);
            });

            it('should append the compiled element on the parent overlay', function() {
                expect(parentOverlay.append).toHaveBeenCalledWith(compiled);
            });
        });

        describe('when re-draw condition is true and component is a NavigationBarCollectionComponent', function() {
            beforeEach(function() {
                COMPONENT_TYPE = 'NavigationBarCollectionComponent';
                spyOn(renderService, '_mustRedraw').andReturn(true);
                renderService._cloneAndCompileComponent(element, parentOverlay, true);
            });

            it('should add a specific z-index to the styling of the SmartEdit decorator wrapper', function() {
                expect(smartEditWrapper.style.zIndex).toBe(EXPECTED_Z_INDEX_FOR_NAVIGATION);
            });
        });
    });

    describe('_getContainingOverlay', function() {
        var element;

        beforeEach(function() {
            element = jasmine.createSpyObj('element', ['attr', 'hasClass']);
        });

        describe('when called with a SmartEdit component', function() {
            var expectedOverlay;
            var actualOverlay;

            beforeEach(function() {
                expectedOverlay = {};
                element.hasClass.andReturn(true);
                element.attr.andCallFake(function(attribute) {
                    if (attribute === COMPONENT_ID_ATTRIBUTE) {
                        return COMPONENT_ID;
                    } else if (attribute === COMPONENT_TYPE_ATTRIBUTE) {
                        return COMPONENT_TYPE;
                    }
                });
                componentHandlerService.getComponentInOverlay.andReturn(expectedOverlay);

            });

            beforeEach(function() {
                actualOverlay = renderService._getContainingOverlay(element);
            });

            it('should check the class on the input element', function() {
                expect(element.hasClass).toHaveBeenCalledWith('smartEditComponent');
            });

            it('should fetch the overlay of the component', function() {
                expect(componentHandlerService.getComponentInOverlay).toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE);
                expect(actualOverlay).toBe(expectedOverlay);
            });
        });

        describe('when called with any element that is not a SmartEdit component', function() {
            var expectedOverlay;
            var actualOverlay;

            beforeEach(function() {
                expectedOverlay = {};
                componentHandlerService.getOverlay.andReturn(expectedOverlay);
                element.hasClass.andReturn(false);
            });

            beforeEach(function() {
                actualOverlay = renderService._getContainingOverlay(element);
            });

            it('should check the class on the input element', function() {
                expect(element.hasClass).toHaveBeenCalledWith('smartEditComponent');
            });

            it('should fetch the SmartEdit overlay', function() {
                expect(componentHandlerService.getOverlay).toHaveBeenCalled();
                expect(actualOverlay).toBe(expectedOverlay);
            });
        });
    });

    describe('_getFirstChildren', function() {
        var PARENT_CSS_PATH = "body > main > .smartEditComponent";
        var FIRST_LEVEL_CHILD_PATH = "body > main > .smartEditComponent > .smartEditComponent";
        var DEEP_LEVEL_CHILD_PATH = "body > main > .smartEditComponent > div > ul > .smartEditComponent";
        var DEEP_LEVEL_NESTED_CHILD_PATH = "body > main > .smartEditComponent > div > .smartEditComponent > ul > .smartEditComponent";
        var NON_CHILD_PATH = "body > ul > .smartEditComponent > .smartEditComponent";

        var parentComponent, childComponents, actual;

        beforeEach(function() {
            parentComponent = jasmine.createSpyObj('parentComponent', ['getCssPath', 'find']);
            childComponents = [{
                getCssPath: function() {
                    return FIRST_LEVEL_CHILD_PATH;
                }
            }, {
                getCssPath: function() {
                    return DEEP_LEVEL_CHILD_PATH;
                }
            }, {
                getCssPath: function() {
                    return DEEP_LEVEL_NESTED_CHILD_PATH;
                }
            }, {
                getCssPath: function() {
                    return NON_CHILD_PATH;
                }
            }];
        });

        beforeEach(function() {
            parentComponent.getCssPath.andReturn(PARENT_CSS_PATH);
            parentComponent.find.andReturn(childComponents);

            var callCount = -1;
            componentHandlerService.getFromSelector.andCallFake(function() {
                callCount++;
                var callReturnValues = [parentComponent].concat(childComponents);
                return callReturnValues[callCount];
            });
        });

        beforeEach(function() {
            actual = renderService._getFirstChildren(parentComponent);
        });

        it('should return the first level of SmartEdit components', function() {
            expect(actual.length).toBe(2);
            expect(actual[0]).toBe(childComponents[0]);
            expect(actual[1]).toBe(childComponents[1]);
        });
    });

    describe('_recursiveCloneAndCompile', function() {
        var element, parentOverlay, redraw, elements, children;

        beforeEach(function() {
            element = {};
            parentOverlay = {};
            redraw = true;
            elements = ["child1", "child2"];
            children = jasmine.createSpyObj('children', ['each']);
        });

        beforeEach(function() {
            children.each.andCallFake(function(callback) {
                elements.forEach(function(child) {
                    callback(0, child);
                });
            });

            var callCount = 0;
            var originalFunction = renderService._recursiveCloneAndCompile.bind(renderService);
            spyOn(renderService, '_recursiveCloneAndCompile').andCallFake(function(element, redraw) {
                if (callCount === 0) {
                    callCount++;
                    originalFunction(element, redraw);
                }
            });

            spyOn(renderService, '_getFirstChildren').andReturn(children);
            spyOn(renderService, '_getContainingOverlay').andReturn(parentOverlay);
            spyOn(renderService, '_cloneAndCompileComponent');

            componentHandlerService.getFromSelector.andCallFake(function(element) {
                return element;
            });
        });

        it('should find and compile each of the element\'s children', function() {
            // Arrange
            spyOn(renderService, '_isComponentVisible').andReturn(true);

            // Act
            renderService._recursiveCloneAndCompile(element, redraw);

            // Assert
            expect(renderService._isComponentVisible.calls.length).toBe(2);
            expect(renderService._recursiveCloneAndCompile.calls.length).toBe(3);
            expect(renderService._cloneAndCompileComponent).toHaveBeenCalledWith(elements[0], parentOverlay, redraw);
            expect(renderService._recursiveCloneAndCompile).toHaveBeenCalledWith(elements[0], redraw);
            expect(renderService._cloneAndCompileComponent).toHaveBeenCalledWith(elements[1], parentOverlay, redraw);
            expect(renderService._recursiveCloneAndCompile).toHaveBeenCalledWith(elements[1], redraw);
        });

        it('if one of the elements is visible it will not be compiled', function() {
            // Arrange
            spyOn(renderService, '_isComponentVisible').andCallFake(function(element) {
                if (element === 'child1') {
                    return false;
                }

                return true;
            });

            // Act
            renderService._recursiveCloneAndCompile(element, redraw);

            // Assert
            expect(renderService._isComponentVisible.calls.length).toBe(2);
            expect(renderService._recursiveCloneAndCompile.calls.length).toBe(2);
            expect(renderService._cloneAndCompileComponent).not.toHaveBeenCalledWith(elements[0], parentOverlay, redraw);
            expect(renderService._recursiveCloneAndCompile).not.toHaveBeenCalledWith(elements[0], redraw);
            expect(renderService._cloneAndCompileComponent).toHaveBeenCalledWith(elements[1], parentOverlay, redraw);
            expect(renderService._recursiveCloneAndCompile).toHaveBeenCalledWith(elements[1], redraw);
        });
    });

    describe('renderPage', function() {
        var overlay;

        beforeEach(function() {
            overlay = jasmine.createSpyObj('overlay', ['remove']);
        });

        beforeEach(function() {
            componentHandlerService.getOverlay.andReturn(overlay);
            perspectiveService.isEmptyPerspectiveActive.andReturn($q.when(true));

            spyOn(renderService, '_resizeSlots');
            spyOn(renderService, '_markSmartEditAsReady');
            spyOn(renderService, '_registerPageReadyListener');
        });

        beforeEach(function() {
            renderService.renderPage(false);
        });

        it('should resize the slots', function() {
            expect(renderService._resizeSlots).toHaveBeenCalled();
        });

        it('should destroy all scopes', function() {
            expect(sakExecutor.destroyAllScopes).toHaveBeenCalled();
        });

        it('should remove the overlay', function() {
            expect(overlay.remove).toHaveBeenCalled();
        });

        describe('when current perspective is "Preview"', function() {
            beforeEach(function() {
                $rootScope.$digest();
            });

            it('should not mark SmartEdit as ready', function() {
                expect(renderService._markSmartEditAsReady).toHaveBeenCalled();
            });

            it('should not register the page ready listener', function() {
                expect(renderService._registerPageReadyListener).toHaveBeenCalled();
            });
        });
    });
});
