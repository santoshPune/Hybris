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
 * @name slotSharedServiceModule.slotSharedService
 * @description
 * SlotSharedService provides methods to interact with the backend for shared slot information. 
 */
angular.module('slotSharedServiceModule', ['resourceModule', 'componentHandlerServiceModule', 'resourceLocationsModule'])
    .service('slotSharedService', ['restServiceFactory', '$q', 'componentHandlerService', 'PAGES_CONTENT_SLOT_RESOURCE_URI', function(restServiceFactory, $q, componentHandlerService, PAGES_CONTENT_SLOT_RESOURCE_URI) {
        var pagesContentSlotsResource = restServiceFactory.get(PAGES_CONTENT_SLOT_RESOURCE_URI);
        var currentPageId = componentHandlerService.getPageUID();
        var sharedSlotsPromise;

        var getMapFragmentPromiseByUniqueSlotId = function(slotId) {
            return pagesContentSlotsResource.get({
                slotId: slotId
            }).then(function(pagesContentSlotsResponse) {
                var uniquePageIds = pagesContentSlotsResponse.pageContentSlotList.map(function(pageContentSlotComponent) {
                    return pageContentSlotComponent.pageId;
                }).filter(getUniqueArray);

                var mapFragment = {};
                mapFragment[slotId] = uniquePageIds.length > 1;
                return mapFragment;
            });
        };

        var getUniqueArray = function(id, position, ids) {
            return ids.indexOf(id) === position;
        };

        var getSlotIdsFromList = function(pageContentSlotComponent) {
            return pageContentSlotComponent.slotId;
        };

        /**
         * @ngdoc method
         * @name slotSharedServiceModule.slotSharedService#reloadSharedSlotMap
         * @methodOf slotSharedServiceModule.slotSharedService
         *
         * @description
         * This function fetches all the slots with the pageUID and after filtering the slotIDs list it populates slot shared Map Fragment for the unique slotIds
         * and from the Map Fragment slot shared status can be extracted based on the slot id.
         *
         * @param {String} pageUID of the page
         */
        this.reloadSharedSlotMap = function() {
            sharedSlotsPromise = pagesContentSlotsResource.get({
                pageId: currentPageId
            }).then(function(pagesContentSlotsResponse) {
                var mapFragmentPromises = (pagesContentSlotsResponse.pageContentSlotList || [])
                    .map(getSlotIdsFromList)
                    .filter(getUniqueArray)
                    .map(getMapFragmentPromiseByUniqueSlotId);
                return $q.all(mapFragmentPromises).then(function(mapFragments) {
                    return mapFragments.reduce(function(mapCopy, mapFragment) {
                        return angular.extend({}, mapCopy, mapFragment);
                    }, {});
                });
            });
            return sharedSlotsPromise;
        };

        /**
         * @ngdoc method
         * @name slotSharedServiceModule.slotSharedService#isSlotShared
         * @methodOf slotSharedServiceModule.slotSharedService
         *
         * @description
         * Checks if the slot is shared and returns true in case slot is shared and returns false if it is not. 
         * Based on this service method the slot shared button is shown or hidden for a particular slotId
         *
         * @param {String} slotId of the slot
         */
        this.isSlotShared = function(slotId) {
            return sharedSlotsPromise.then(function(slotMap) {
                return slotMap[slotId];
            });
        };
    }])
    .run(['slotSharedService', function(slotSharedService) {
        slotSharedService.reloadSharedSlotMap();
    }]);
