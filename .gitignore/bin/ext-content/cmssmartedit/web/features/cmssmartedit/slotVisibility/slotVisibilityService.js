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
 * @name slotVisibilityServiceModule
 * @description
 *
 * The slot visibility service module provides factories and services to manage all backend calls and loads an internal 
 * structure that provides the necessary data to the slot visibility button and slot visibility component.
 */
angular.module('slotVisibilityServiceModule', ['resourceModule', 'componentHandlerServiceModule', 'functionsModule', 'renderServiceModule'])
    /**
     * @ngdoc service
     * @name SlotVisibilityService
     * @description
     *
     * The SlotVisibilityService class provides methods to interact with the backend. 
     * The definition of the class is not instantiated immediately, whereas the service instance of 
     * this same class (@see slotVisibilityService) returns an instance of this service definition.  
     * 
     * @param {Object} itemsResource Gets all components based on their IDs. 
     * @param {Object} pagesContentSlotsComponentsResource Gets content slots and components based on their page IDs.
     * @param {Object} componentHandlerService Gets the current page ID. 
     */
    .service('SlotVisibilityService', function(itemsResource, pagesContentSlotsComponentsResource, componentHandlerService, renderService) {
        function SlotVisibilityService() {
            var hiddenComponentsMapPromise;

            var currentPageId = componentHandlerService.getPageUID();

            var loadHiddenComponentsByComponents = function(pagesContentSlotsComponents, components) {
                var hiddenComponents = (components.componentItems || []).filter(function(component) {
                    return !component.visible;
                }).reduce(function(map, component) {
                    map[component.uid] = component;
                    return map;
                }, {});

                var hiddenComponentsMap = (pagesContentSlotsComponents.pageContentSlotComponentList || [])
                    .reduce(function(map, pageContentSlotComponent) {
                        map[pageContentSlotComponent.slotId] = map[pageContentSlotComponent.slotId] || [];
                        if (hiddenComponents[pageContentSlotComponent.componentId]) {
                            map[pageContentSlotComponent.slotId].push(hiddenComponents[pageContentSlotComponent.componentId]);
                        }
                        return map;
                    }, {});

                return hiddenComponentsMap;
            };

            var loadHiddenComponentsBySlotsAndComponents = function(pagesContentSlotsComponents) {
                var componentIds = (pagesContentSlotsComponents.pageContentSlotComponentList || [])
                    .map(function(pageContentSlotComponent) {
                        return pageContentSlotComponent.componentId;
                    });

                return itemsResource.get({
                    uids: componentIds.join(',')
                }).then(function(components) {
                    return loadHiddenComponentsByComponents(pagesContentSlotsComponents, components);
                });
            };

            var loadHiddenComponentsMapPromise = function() {
                hiddenComponentsMapPromise = pagesContentSlotsComponentsResource.get({
                        pageId: currentPageId
                    })
                    .then(loadHiddenComponentsBySlotsAndComponents);
                return hiddenComponentsMapPromise;
            };

            var reRenderSlots = function(hiddenComponentsMapOld, hiddenComponentsMapNew) {
                var slotIds = [];
                Object.keys(hiddenComponentsMapOld).forEach(function(slotId) {
                    // check if the number of visible components has changed
                    if (hiddenComponentsMapOld[slotId] && hiddenComponentsMapNew[slotId] &&
                        hiddenComponentsMapOld[slotId].length !== hiddenComponentsMapNew[slotId].length) {
                        slotIds.push(slotId);
                    }
                });
                // if there is at least one slot that has been changed, then re-render the slots
                if (slotIds.length > 0) {
                    renderService.renderSlots(slotIds);
                }
            };

            /**
             * Function to load the hidden components for the slots in the page. 
             * If the hiddenComponentsMapPromise does not exist, then it only load the hidden components, 
             * otherwise it will re-load the components for the slots that have changed the status.  
             */
            this.loadHiddenComponents = function() {
                if (!hiddenComponentsMapPromise) {
                    hiddenComponentsMapPromise = loadHiddenComponentsMapPromise();
                    return hiddenComponentsMapPromise;
                }
                return this.reLoadHiddenComponents();
            };

            /**
             * re-load the hidden components data structure and re-render slots that have changed. 
             */
            this.reLoadHiddenComponents = function() {
                hiddenComponentsMapPromise.then(function(hiddenComponentsMap) {
                    var hiddenComponentsMapOld = angular.copy(hiddenComponentsMap);

                    hiddenComponentsMapPromise = loadHiddenComponentsMapPromise();
                    hiddenComponentsMapPromise.then(function(hiddenComponentsMapNew) {
                        reRenderSlots(hiddenComponentsMapOld, hiddenComponentsMapNew);
                    });
                });

                return hiddenComponentsMapPromise;
            };

            /**
             * Function to get the hidden component count. 
             * @param slotId the slot id 
             * @return the promise with the number of hidden components for slotId
             */
            this.getHiddenComponentCount = function(slotId) {
                return this.getHiddenComponents(slotId).then(function(hiddenComponents) {
                    return hiddenComponents.length;
                });
            };

            /**
             * Function to get the hidden components for a certain slot. 
             * @param slotId the slot id 
             * @return the promise with the hidden components list for slotId
             */
            this.getHiddenComponents = function(slotId) {
                return (hiddenComponentsMapPromise || this.loadHiddenComponents()).then(function(hiddenComponentsMap) {
                    return hiddenComponentsMap[slotId] || [];
                }, function(error) {
                    return [];
                });
            };
        }
        return SlotVisibilityService;
    })
    .service('slotVisibilityService', function(SlotVisibilityService, systemEventService, throttle, $q) {
        var instance = new SlotVisibilityService();
        instance.loadHiddenComponents();

        var throttledNotification = throttle(function() {
            instance.loadHiddenComponents();
        }, 2000);

        systemEventService.registerEventHandler('SLOT_CONTEXTUAL_MENU_ACTIVE', function() {
            throttledNotification();
            return $q.when();
        });
        return instance;
    });
