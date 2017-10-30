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
 * @name renderServiceModule
 * @description
 * This module provides the renderService, which is responsible for rendering the SmartEdit overlays used for providing
 * CMS functionality to the storefront within the context of SmartEdit.
 */
angular.module('renderServiceModule', ['functionsModule', 'gatewayProxyModule', 'renderServiceInterfaceModule', 'alertServiceModule', 'decoratorServiceModule', 'sakExecutorDecorator', 'perspectiveServiceModule', 'gatewayFactoryModule', 'componentHandlerServiceModule'])

/**
 * As a configuration step for this module, add the getCssPath method to jquery selectors. This method will return
 * the CSS path of the wrapped JQuery element.
 */
.config(function() {
    jQuery.fn.extend({
        getCssPath: function() {
            var path, node = this;
            while (node.length) {
                var realNode = node[0],
                    name = realNode.className;
                if (!name) break;
                node = node.parent();
                path = name + (path ? '>' + path : '');
            }
            return path;
        }
    });
})

/**
 * @ngdoc service
 * @name renderServiceModule.renderGateway
 * @description
 * Instance of the {@link gatewayFactoryModule.MessageGateway MessageGateway} dealing with rendering-related events.
 */
.factory('renderGateway', ['gatewayFactory', function(gatewayFactory) {
    return gatewayFactory.createGateway("render");
}])

/**
 * @ngdoc service
 * @name renderServiceModule.renderService
 * @description
 * The renderService is responsible for rendering and resizing component overlays, and re-rendering components and slots
 * from the storefront.
 */
.service('renderService', ['$q', '$compile', '$rootScope', '$http', '$location', '$window', 'alertService', 'componentHandlerService', 'decoratorService', 'extend', 'extractFromElement', 'gatewayFactory', 'gatewayProxy', 'hitch', 'isBlank', 'sakExecutor', 'perspectiveService', 'renderGateway', 'RenderServiceInterface', 'unsafeParseHTML', 'COMPONENT_CLASS', 'ID_ATTRIBUTE', 'OVERLAY_COMPONENT_CLASS', 'OVERLAY_ID', 'SMARTEDIT_ATTRIBUTE_PREFIX', 'TYPE_ATTRIBUTE', function($q, $compile, $rootScope, $http, $location, $window, alertService, componentHandlerService, decoratorService, extend, extractFromElement, gatewayFactory, gatewayProxy, hitch, isBlank, sakExecutor, perspectiveService, renderGateway, RenderServiceInterface, unsafeParseHTML, COMPONENT_CLASS, ID_ATTRIBUTE, OVERLAY_COMPONENT_CLASS, OVERLAY_ID, SMARTEDIT_ATTRIBUTE_PREFIX, TYPE_ATTRIBUTE) {
    angular.extend(this, RenderServiceInterface.prototype);

    this.gatewayId = "Renderer";
    this._slotOriginalHeights = {};
    this._smartEditBootstrapGateway = gatewayFactory.createGateway('smartEditBootstrap');

    /**
     * @ngdoc function
     * @name renderServiceModule.renderService.toggleOverlay
     * @methodOf renderServiceModule.renderService
     * @description
     * Toggles the visibility of the overlay using CSS.
     *
     * @param {Boolean} isVisible Flag to show/hide the overlay.
     */
    this.toggleOverlay = function(isVisible) {
        var overlay = componentHandlerService.getOverlay();
        overlay.css('visibility', (isVisible ? 'visible' : 'hidden'));
    };

    /**
     * @ngdoc function
     * @name renderServiceModule.renderService.refreshOverlayDimensions
     * @methodOf renderServiceModule.renderService
     * @description
     * Refreshes the dimensions and positions of the SmartEdit overlays. The overlays need to remain in synced with the
     * dynamic resizing of their original elements. In particular, this method is bound to the window resizing event
     * to refresh overlay dimensions for responsive storefronts.
     *
     * The implementation itself will search for children SmartEdit components from the root element provided. If no root
     * element is provided, the method will default to using the body element. The overlay specific to this component
     * is then fetched and resized, according to the dimensions of the component.
     *
     * @param {Element} element The root element from which to traverse and discover SmartEdit components.
     */
    this.refreshOverlayDimensions = function(element) {
        element = element || componentHandlerService.getFromSelector('body');
        var children = this._getFirstChildren(element);
        var containingOverlay = this._getContainingOverlay(element);

        children.each(function(index, childElement) {
            var wrappedChild = componentHandlerService.getFromSelector(childElement);
            this._updateOverlayDimensions(wrappedChild, null, containingOverlay);
            this.refreshOverlayDimensions(wrappedChild);
        }.bind(this));
    };

    /**
     * @ngdoc function
     * @name renderServiceModule.renderService._updateOverlayDimensions
     * @methodOf renderServiceModule.renderService
     * @private
     * @description
     * Updates the dimensions of the component overlay element given the original component element. If no component
     * overlay element is provided, the overlay element is fetched by jQuery.
     *
     * The overlay is resized to be the same dimensions of the component for which it overlays, and positioned absolutely
     * on the page. Additionally, it is provided with a minimum height and width. The resizing takes into account both
     * the size of the component element, and the position based on iframe scrolling.
     *
     * @param {Element} componentElem The original CMS component element from the storefront.
     * @param {Element} componentOverlayElem The component overlay element. If none is provided, the overlay is fetched using jQuery.
     * @param {Element} parentOverlayElem The containing overlay element of the given component overlay.
     */
    this._updateOverlayDimensions = function(componentElem, componentOverlayElem, parentOverlayElem) {
        var smarteditComponentId = componentElem.attr(ID_ATTRIBUTE);
        var smarteditComponentType = componentElem.attr(TYPE_ATTRIBUTE);
        componentOverlayElem = componentOverlayElem ||
            componentHandlerService.getComponentInOverlay(smarteditComponentId, smarteditComponentType).get(0);

        if (componentOverlayElem) {
            var pos = componentElem.get(0).getBoundingClientRect();
            var parentPos = parentOverlayElem.get(0).getBoundingClientRect();

            var innerHeight = componentElem.get(0).offsetHeight;
            var innerWidth = componentElem.get(0).offsetWidth;

            // Update the position based on the IFrame Scrolling
            var elementTopPos = pos.top - parentPos.top;
            var elementLeftPos = pos.left - parentPos.left;

            componentOverlayElem.style.position = "absolute";
            componentOverlayElem.style.top = elementTopPos + "px";
            componentOverlayElem.style.left = elementLeftPos + "px";
            componentOverlayElem.style.width = innerWidth + "px";
            componentOverlayElem.style.height = innerHeight + "px";
            componentOverlayElem.style.minWidth = "51px";
            componentOverlayElem.style.minHeight = "49px";

            var shallowCopy = $(componentOverlayElem)
                .find('[id="' + smarteditComponentId + '_' + smarteditComponentType + '_overlay"]');
            shallowCopy.width(innerWidth);
            shallowCopy.height(innerHeight);
            shallowCopy.css('min-height', 49);
            shallowCopy.css('min-width', 51);
        }
    };

    this._mustRedraw = function(componentInOverlay, redraw) {
        return componentInOverlay.length === 0 || redraw === true;
    };

    this._getContainingOverlay = function(element) {
        var parentOverlay;
        if (element.hasClass(COMPONENT_CLASS)) {
            var smarteditComponentId = element.attr(ID_ATTRIBUTE);
            var smarteditComponentType = element.attr(TYPE_ATTRIBUTE);

            parentOverlay = componentHandlerService.getComponentInOverlay(smarteditComponentId, smarteditComponentType);
        } else {
            parentOverlay = componentHandlerService.getOverlay();
        }

        return parentOverlay;
    };

    this._recursiveCloneAndCompile = function(element, redraw) {
        var children = this._getFirstChildren(element);
        var parentOverlay = this._getContainingOverlay(element);

        children.each(function(index, childElement) {
            if (this._isComponentVisible(childElement)) {
                var wrappedChild = componentHandlerService.getFromSelector(childElement);
                this._cloneAndCompileComponent(wrappedChild, parentOverlay, redraw);
                this._recursiveCloneAndCompile(wrappedChild, redraw);
            }
        }.bind(this));
    };


    this._cloneAndCompileComponent = function(element, parentOverlay, redraw) {
        var smarteditComponentId = element.attr(ID_ATTRIBUTE);
        var smarteditComponentType = element.attr(TYPE_ATTRIBUTE);

        var componentInOverlay = componentHandlerService.getComponentUnderParentOverlay(smarteditComponentId, smarteditComponentType, parentOverlay);

        if (this._mustRedraw(componentInOverlay, redraw)) {
            if (redraw) {
                componentInOverlay.remove();
            }
            var shallowCopy = this._getDocument().createElement("div");
            shallowCopy.id = smarteditComponentId + "_" + smarteditComponentType + "_overlay";

            var smartEditWrapper = this._getDocument().createElement("div");
            var componentDecorator = componentHandlerService.getFromSelector(smartEditWrapper);
            componentDecorator.append(shallowCopy);

            this._updateOverlayDimensions(element, smartEditWrapper, parentOverlay);

            if (smarteditComponentType === "NavigationBarCollectionComponent") {
                // Make sure the Navigation Bar is on top of the navigation items
                smartEditWrapper.style.zIndex = "7";
            }

            componentDecorator.addClass(OVERLAY_COMPONENT_CLASS);
            $(element.get(0).attributes).each(function() {
                if (this.nodeName.indexOf(SMARTEDIT_ATTRIBUTE_PREFIX) === 0) {
                    componentDecorator.attr(this.nodeName, this.nodeValue);
                }
            });

            var compiled = this._compile(smartEditWrapper, $rootScope);
            parentOverlay.append(compiled);
        }
    };

    // Component Rendering
    this.renderPage = function(isRerender) {
        this._resizeSlots();

        //need to destroy scopes BEFORE removing the directive elements
        sakExecutor.destroyAllScopes();
        componentHandlerService.getOverlay().remove();
        perspectiveService.isEmptyPerspectiveActive().then(function(isActive) {
            if (isActive) {
                this._markSmartEditAsReady();
            }

            this._registerPageReadyListener();

            if (isRerender) {
                // Currently CMSLinkComponent cause problems while rendering, since this type is nested within other components.
                // In future tickets, the slot render api will help solve this problem.
                sakExecutor.resetCounters(componentHandlerService.getFromSelector('.' + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot']").length);

                var overlayWrapper = componentHandlerService.getOverlay();
                if (overlayWrapper.length === 0) {
                    var overlay = document.createElement("div");
                    overlay.id = OVERLAY_ID;
                    overlay.style.background = "none";
                    overlay.style.zIndex = "0";
                    overlay.style.position = "absolute";
                    overlay.style.top = "0px";
                    overlay.style.left = "0px";
                    overlay.style.bottom = "0px";
                    overlay.style.right = "0px";

                    document.body.appendChild(overlay);
                } else {
                    overlayWrapper.empty();
                }
                var body = componentHandlerService.getFromSelector('body');
                this._recursiveCloneAndCompile(body);
            }
        }.bind(this));
    };

    /**
     * @ngdoc function
     * @name renderServiceModule.renderService._resizeSlots
     * @methodOf renderServiceModule.renderService
     * @private
     * @description
     * Resizes the height of all slots on the page based on the sizes of the components. The new height of the
     * slot is set to the minimum height encompassing its sub-components, calculated by comparing each of the
     * sub-components' top and bottom bounding rectangle values.
     *
     * Slots that do not have components inside still appear in the DOM. If the CMS manager is in a perspective in which
     * slot contextual menus are displayed, slots must have a height. Otherwise, overlays will overlap. Thus, empty slots
     * are given a minimum size so that overlays match.
     */
    this._resizeSlots = function() {
        this._getFirstChildren('body').each(function(index, slotComponent) {
            var slotComponentDimensions = slotComponent.getBoundingClientRect();
            var slotComponentID = componentHandlerService.getFromSelector(slotComponent).attr(ID_ATTRIBUTE);
            var slotComponentType = componentHandlerService.getFromSelector(slotComponent).attr(ID_ATTRIBUTE);

            var newSlotTop = -1;
            var newSlotBottom = -1;

            componentHandlerService.getFromSelector(slotComponent)
                .find("." + COMPONENT_CLASS)
                .filter(function(index, componentInSlot) {
                    componentInSlot = componentHandlerService.getFromSelector(componentInSlot);
                    return (componentInSlot.attr(ID_ATTRIBUTE) !== slotComponentID && componentInSlot.attr(TYPE_ATTRIBUTE) !== slotComponentType);
                })
                .each(function(compIndex, component) {
                    if (componentHandlerService.getFromSelector(component).is(":visible")) {
                        var componentDimensions = component.getBoundingClientRect();
                        newSlotTop = newSlotTop === -1 ? componentDimensions.top :
                            Math.min(newSlotTop, componentDimensions.top);
                        newSlotBottom = newSlotBottom === -1 ? componentDimensions.bottom :
                            Math.max(newSlotBottom, componentDimensions.bottom);
                    }
                });

            var currentSlotHeight = slotComponentDimensions.bottom - slotComponentDimensions.top;
            var newSlotHeight = newSlotBottom - newSlotTop;

            if (currentSlotHeight !== newSlotHeight) {
                var slotUniqueKey = slotComponentID + "_" + slotComponentType;
                var oldSlotHeight = this._slotOriginalHeights[slotUniqueKey];
                if (!oldSlotHeight) {
                    oldSlotHeight = currentSlotHeight;
                    this._slotOriginalHeights[slotUniqueKey] = oldSlotHeight;
                }

                if (currentSlotHeight >= oldSlotHeight) {
                    slotComponent.style.height = newSlotHeight + "px";
                } else {
                    slotComponent.style.height = oldSlotHeight + 'px';
                }
            }
        }.bind(this));
    };

    this.renderSlots = function(slotIds) {
        if (isBlank(slotIds) || (slotIds instanceof Array && slotIds.length === 0)) {
            return $q.reject("renderService.renderSlots.slotIds.required");
        }
        if (typeof slotIds == 'string') {
            slotIds = [slotIds];
        }
        return $http({
            method: 'GET',
            url: $location.absUrl(),
            headers: {
                'Pragma': 'no-cache'
            }
        }).then(function(response) {
            var root = unsafeParseHTML(response.data);
            slotIds.forEach(function(slotId) {
                var slotSelector = "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot'][" + ID_ATTRIBUTE + "='" + slotId + "']";
                var slotToBeRerendered = extractFromElement(root, slotSelector);
                var originalSlot = componentHandlerService.getFromSelector(slotSelector);
                originalSlot.html(slotToBeRerendered.html());
            });
            this._reprocessPage();
            this.renderPage(true);
        }.bind(this), function(errorResponse) {
            alertService.pushAlerts([{
                successful: false,
                message: errorResponse.message,
                closeable: true
            }]);
            return $q.reject(errorResponse.message);
        });
    };

    this.renderComponent = function(componentId, componentType) {
        var component = componentHandlerService.getComponent(componentId, componentType);
        var slotId = componentHandlerService.getParent(component).attr(ID_ATTRIBUTE);
        return this.renderSlots(slotId);
    };

    this.renderRemoval = function(componentId, componentType, slotId) {
        var removedComponents = componentHandlerService.getComponentUnderSlot(componentId, componentType, slotId, null).remove();
        this.refreshOverlayDimensions();
        return removedComponents;
    };

    this._markSmartEditAsReady = function() {
        this._smartEditBootstrapGateway.publish('smartEditReady');
        componentHandlerService.getFromSelector('body').attr('data-smartedit-ready', 'true');
    };

    this._registerPageReadyListener = function() {
        this.removePageReadyWatch = $rootScope.$watch(sakExecutor.areAllDecoratorsProcessed, function(newValue) {
            if (newValue) {
                this._markSmartEditAsReady();
                this.removePageReadyWatch();
            }
        }.bind(this));
    };

    this._getFirstChildren = function(seComponent) {
        var stem = componentHandlerService.getFromSelector(seComponent);
        var parentCssPath = stem.getCssPath();
        var firstChildrenRegex = new RegExp(COMPONENT_CLASS, 'g');

        return stem.find("." + COMPONENT_CLASS).filter(function(index) {
            return componentHandlerService.getFromSelector(this)
                .getCssPath().replace(parentCssPath, "").match(firstChildrenRegex).length === 1;
        });
    };

    this._isComponentVisible = function(component) {
        // NOTE: This might not work as expected for fixed positioned items. For those cases a more expensive
        // check must be performed (get the component style and check if it's visible or not).
        return (component.offsetParent !== null);
    };

    this._reprocessPage = function() {
        window.smartedit.reprocessPage();
    };

    this._compile = function(component, scope) {
        return $compile(component)(scope);
    };

    this._getDocument = function(selector) {
        return document;
    };

    // Initialization
    $window.addEventListener('resize', function() {
        this.refreshOverlayDimensions();
    }.bind(this));

    renderGateway.subscribe('rerender', function(eventId, data) {
        this.renderPage(data);
    }.bind(this));

    gatewayProxy.initForService(this, ["renderSlots", "renderComponent", "renderRemoval", "toggleOverlay", "refreshOverlayDimensions", "renderPage"]);
}]);
