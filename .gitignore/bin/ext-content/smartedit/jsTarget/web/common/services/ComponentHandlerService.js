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
 * @name componentHandlerServiceModule
 * @description
 * 
 * this module aims at handling smartEdit components both on the original storefront and the smartEdit overlay
 * 
 */
angular.module('componentHandlerServiceModule', ['functionsModule'])

/**
 * @ngdoc object
 * @name componentHandlerServiceModule.OVERLAY_ID
 * @description
 * the identifier of the overlay placed in front of the storefront to where all smartEdit component decorated clones are copied.
 */
.constant('OVERLAY_ID', 'smarteditoverlay')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.COMPONENT_CLASS
     * @description
     * the css class of the smartEdit components as per contract with the storefront
     */
    .constant('COMPONENT_CLASS', 'smartEditComponent')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.OVERLAY_COMPONENT_CLASS
     * @description
     * the css class of the smartEdit component clones copied to the storefront overlay
     */
    .constant('OVERLAY_COMPONENT_CLASS', 'smartEditComponentX')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.SMARTEDIT_ATTRIBUTE_PREFIX
     * @description
     * If the storefront needs to expose more attributes than the minimal contract, these attributes must be prefixed with this constant value
     */
    .constant('SMARTEDIT_ATTRIBUTE_PREFIX', 'data-smartedit-')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.ID_ATTRIBUTE
     * @description
     * the id attribute of the smartEdit components as per contract with the storefront
     */
    .constant('ID_ATTRIBUTE', 'data-smartedit-component-id')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.TYPE_ATTRIBUTE
     * @description
     * the type attribute of the smartEdit components as per contract with the storefront
     */
    .constant('TYPE_ATTRIBUTE', 'data-smartedit-component-type')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE
     * @description
     * the id attribute of the smartEdit container, when applicable, as per contract with the storefront
     */
    .constant('CONTAINER_ID_ATTRIBUTE', 'data-smartedit-container-id')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE
     * @description
     * the type attribute of the smartEdit container, when applicable, as per contract with the storefront
     */
    .constant('CONTAINER_TYPE_ATTRIBUTE', 'data-smartedit-container-type')
    /**
     * @ngdoc object
     * @name componentHandlerServiceModule.CONTENT_SLOT_TYPE
     * @description
     * the type value of the smartEdit slots as per contract with the storefront
     */
    .constant('CONTENT_SLOT_TYPE', 'ContentSlot')
    /**
     * @ngdoc service
     * @name componentHandlerServiceModule.componentHandlerService
     * @description
     *
     * The service provides convenient methods to get DOM references of smartEdit components both in the original laye rof the storefornt and in the smartEdit overlay
     */
    .factory(
        'componentHandlerService',
        ['$window', 'isBlank', 'OVERLAY_ID', 'COMPONENT_CLASS', 'OVERLAY_COMPONENT_CLASS', 'ID_ATTRIBUTE', 'TYPE_ATTRIBUTE', 'CONTAINER_ID_ATTRIBUTE', 'CONTAINER_TYPE_ATTRIBUTE', 'CONTENT_SLOT_TYPE', function($window, isBlank, OVERLAY_ID, COMPONENT_CLASS, OVERLAY_COMPONENT_CLASS, ID_ATTRIBUTE, TYPE_ATTRIBUTE, CONTAINER_ID_ATTRIBUTE, CONTAINER_TYPE_ATTRIBUTE, CONTENT_SLOT_TYPE) {

            var buildComponentQuery = function(smarteditComponentId, smarteditComponentType, cssClass) {
                var query = '';
                query += (cssClass ? '.' + cssClass : '');
                query += '[' + ID_ATTRIBUTE + '=\'' + smarteditComponentId + '\']';
                query += '[' + TYPE_ATTRIBUTE + '=\'' + smarteditComponentType + '\']';
                return query;
            };

            var buildComponentInSlotQuery = function(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass) {
                var slotQuery = buildComponentQuery(smarteditSlotId, CONTENT_SLOT_TYPE);
                var componentQuery = buildComponentQuery(smarteditComponentId, smarteditComponentType, cssClass);
                return slotQuery + ' > ' + componentQuery;
            };

            return {

                _isIframe: function() {
                    return $window.frameElement;
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getPageUID
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This extracts the pageUID of the storefront page loaded in the smartedit iframe.
                 *
                 * @return {String} a string matching the page's ID
                 */
                getPageUID: function() {
                    var targetBody = this._isIframe() ? this.getFromSelector('body') : this.getFromSelector('iframe').contents().find('body');
                    return /smartedit-page-uid\-(\S+)/.exec(targetBody.attr('class'))[1];
                },

                buildOverlayQuery: function() {
                    return '#' + OVERLAY_ID;
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getFromSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This is a wrapper around jQuery selector
                 *
                 * @param {String} selector String selector as per jQuery API
                 * 
                 * @return {Object} a jQuery object for the given selector
                 */
                getFromSelector: function(selector) {
                    return $(selector);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOverlay
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves a handler on the smartEdit overlay div
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 */
                getOverlay: function() {
                    return this.getFromSelector(this.buildOverlayQuery());
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentUnderSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component identified by its smartEdit id, smartEdit type and an optional class
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} smarteditSlotId the slot id of the slot containing the component as per the smartEdit contract with the storefront
                 * @param {String} cssClass the css Class to further restrict the search on. This parameter is optional.
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getComponentUnderSlot: function(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass) {
                    return this.getFromSelector(buildComponentInSlotQuery(smarteditComponentId, smarteditComponentType, smarteditSlotId, cssClass));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component identified by its smartEdit id, smartEdit type and an optional class
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} cssClass the css Class to further restrict the search on. This parameter is optional.
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getComponent: function(smarteditComponentId, smarteditComponentType, cssClass) {
                    return this.getFromSelector(buildComponentQuery(smarteditComponentId, smarteditComponentType, cssClass));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component of the original storefront layer identified by its smartEdit id, smartEdit type
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getOriginalComponent: function(smarteditComponentId, smarteditComponentType) {
                    return this.getComponent(smarteditComponentId, smarteditComponentType, COMPONENT_CLASS);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getOriginalComponentWithinSlot
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component of the original storefront layer identified by its smartEdit id, smartEdit type and slot ID
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * @param {String} slotId the ID of the slot within which the component resides
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getOriginalComponentWithinSlot: function(smarteditComponentId, smarteditComponentType, slotId) {
                    return this.getComponentUnderSlot(smarteditComponentId, smarteditComponentType, slotId, COMPONENT_CLASS);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getComponentInOverlay
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery wrapper around a smartEdit component of the overlay div identified by its smartEdit id, smartEdit type
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 * 
                 * @param {String} smarteditComponentId the component id as per the smartEdit contract with the storefront
                 * @param {String} smarteditComponentType the component type as per the smartEdit contract with the storefront
                 * 
                 * @return {Object} a jQuery object wrapping the searched component
                 */
                getComponentInOverlay: function(smarteditComponentId, smarteditComponentType) {
                    return this.getComponent(smarteditComponentId, smarteditComponentType, OVERLAY_COMPONENT_CLASS);
                },

                getComponentUnderParentOverlay: function(smarteditComponentId, smarteditComponentType, parentOverlay) {
                    return this.getFromSelector(parentOverlay)
                        .find(buildComponentQuery(smarteditComponentId, smarteditComponentType, OVERLAY_COMPONENT_CLASS));
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the direct smartEdit component parent of a given component.
                 * The parent is fetched in the same layer (original storefront or smartEdit overlay) as the child 
                 * This method can only be invoked from the smartEdit application and not the smartEdit container.
                 *
                 * @param {Object} component the jQuery component for which to search a parent
                 * 
                 * @return {Object} a jQuery object wrapping the smae-layer parent component
                 */
                getParent: function(component) {
                    var parentClassToLookFor = component.hasClass(COMPONENT_CLASS) ? COMPONENT_CLASS : (component.hasClass(OVERLAY_COMPONENT_CLASS) ? OVERLAY_COMPONENT_CLASS : null);
                    if (isBlank(parentClassToLookFor)) {
                        throw "componentHandlerService.getparent.error.component.from.unknown.layer";
                    }
                    return component.closest("." + parentClassToLookFor + "[" + ID_ATTRIBUTE + "!='" + component.attr(ID_ATTRIBUTE) + "']");
                },

                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_setId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Sets the smartEdit component id of a given component
                 *
                 * @param {Object} component the jQuery component for which to set the id
                 * @param {String} id the id to be set
                 */
                setId: function(component, id) {
                    return this.getFromSelector(component).attr(ID_ATTRIBUTE, id);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component id of a given component
                 *
                 * @param {Object} component the jQuery component for which to get the id
                 * 
                 * @return {String} the component id
                 */
                getId: function(component) {
                    return this.getFromSelector(component).attr(ID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedId
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the id that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_ID_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.ID_ATTRIBUTE}
                 *
                 * @param {Object} component the jQuery component for which to get the id
                 * 
                 * @return {String} the slot operations related id
                 */
                getSlotOperationRelatedId: function(component) {
                    var containerId = this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE);
                    return containerId && this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE) ? containerId : this.getFromSelector(component).attr(ID_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_setType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Sets the smartEdit component type of a given component
                 *
                 * @param {Object} component the jQuery component for which to set the type
                 * @param {String} type the type to be set
                 */
                setType: function(component, type) {
                    return this.getFromSelector(component).attr(TYPE_ATTRIBUTE, type);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the smartEdit component type of a given component
                 *
                 * @param {Object} component the jQuery component for which to get the type
                 * 
                 * @return {String} the component type
                 */
                getType: function(component) {
                    return this.getFromSelector(component).attr(TYPE_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getSlotOperationRelatedType
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Gets the type that is relevant to be able to perform slot related operations for this components
                 * It typically is {@link componentHandlerServiceModule.CONTAINER_TYPE_ATTRIBUTE} when applicable and defaults to {@link componentHandlerServiceModule.TYPE_ATTRIBUTE}
                 *
                 * @param {Object} component the jQuery component for which to get the type
                 * 
                 * @return {String} the slot operations related type
                 */
                getSlotOperationRelatedType: function(component) {
                    var containerType = this.getFromSelector(component).attr(CONTAINER_TYPE_ATTRIBUTE);
                    return containerType && this.getFromSelector(component).attr(CONTAINER_ID_ATTRIBUTE) ? containerType : this.getFromSelector(component).attr(TYPE_ATTRIBUTE);
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getAllComponentsSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery selector matching all smartEdit components that are not of type ContentSlot
                 */
                getAllComponentsSelector: function() {
                    return "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "!='ContentSlot']";
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getAllSlotsSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the jQuery selector matching all smartEdit components that are of type ContentSlot
                 */
                getAllSlotsSelector: function() {
                    return "." + COMPONENT_CLASS + "[" + TYPE_ATTRIBUTE + "='ContentSlot']";
                },
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getParentSlotForComponent
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * Retrieves the the slot ID for a given element
                 * 
                 * @param {Object} the DOM element which represents the component
                 * 
                 * @return {String} the slot ID for that particular component
                 */
                getParentSlotForComponent: function(component) {
                    var parent = component.closest('[' + TYPE_ATTRIBUTE + '=' + CONTENT_SLOT_TYPE + ']');
                    return parent.attr(ID_ATTRIBUTE);
                }
            };

        }]);
