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
describe('slotSharedService', function() {

    var $q, $rootScope, pagesContentSlotsResource, slotSharedService, restServiceFactory;
    var withParamSlotId = {
        pageContentSlotList: [{
            pageId: "homepage",
            slotId: "topHeaderSlot",
            position: 0
        }, {
            pageId: "homepage",
            slotId: "topHeaderSlot",
            position: 1
        }, {
            pageId: "homepage",
            slotId: "topHeaderSlot",
            position: 2
        }, {
            pageId: "homepage1",
            slotId: "topHeaderSlot",
            position: 3
        }, {
            pageId: "homepage2",
            slotId: "topHeaderSlot",
            position: 4
        }, {
            pageId: "homepage3",
            slotId: "topHeaderSlot",
            position: 0
        }]
    };
    var withParamPageId = {
        pageContentSlotList: [{
            pageId: "homepage",
            slotId: "topHeaderSlot",
            componentId: "component1",
            position: 0
        }, {
            pageId: "homepage",
            slotId: "topHeaderSlot",
            componentId: "component2",
            position: 1
        }, {
            pageId: "homepage",
            slotId: "topHeaderSlot",
            componentId: "component3",
            position: 2
        }, {
            pageId: "homepage",
            slotId: "topHeaderSlot",
            componentId: "hiddenComponent1",
            position: 3
        }, {
            pageId: "homepage",
            slotId: "topHeaderSlot",
            componentId: "hiddenComponent2",
            position: 4
        }, {
            pageId: "homepage",
            slotId: "bottomHeaderSlot",
            componentId: "component4",
            position: 0
        }, {
            pageId: "homepage",
            slotId: "footerSlot",
            componentId: "component5",
            position: 0
        }, {
            pageId: "homepage",
            slotId: "otherSlot",
            componentId: "hiddenComponent3",
            position: 0
        }]
    };

    beforeEach(customMatchers);

    beforeEach(module('slotSharedServiceModule', function($provide) {

        angular.module('componentHandlerServiceModule', []);
        componentHandlerService = jasmine.createSpyObj('componentHandlerService', ['getPageUID']);
        componentHandlerService.getPageUID.andReturn('homepage');
        $provide.value('componentHandlerService', componentHandlerService);

        $provide.service('restServiceFactory', function($q) {
            pagesContentSlotsResource = jasmine.createSpyObj('pagesContentSlotsResource', ['get']);
            pagesContentSlotsResource.get.andCallFake(function(queryParams) {

                if (queryParams.pageId === 'homepage') {
                    return $q.when(pageContentListResponse());
                } else {
                    return $q.when(pageContentListResponseForSlotId());
                }
            });
            this.get = jasmine.createSpy('get');
            this.get.andReturn(pagesContentSlotsResource);
        });
    }));

    beforeEach(inject(function(_slotSharedService_, _$rootScope_, _$q_) {
        $q = _$q_;
        slotSharedService = _slotSharedService_;
        $rootScope = _$rootScope_;
    }));

    describe('reloadSharedSlotMap', function() {
        it('should resolve with true when slots are shared', function() {
            $rootScope.$digest();
            var resolvedPromise = slotSharedService.reloadSharedSlotMap();
            $rootScope.$digest();
            expect(resolvedPromise).toBeResolvedWithData({
                topHeaderSlot: true,
                bottomHeaderSlot: true,
                footerSlot: true,
                otherSlot: true
            });
        });

        it('should resolve with false when slots are not shared', function() {
            preparePagesContentSlotsResourceResponseWithOnePageID();
            var resolvedPromise = slotSharedService.reloadSharedSlotMap();
            $rootScope.$digest();
            expect(resolvedPromise).toBeResolvedWithData({
                topHeaderSlot: false,
                bottomHeaderSlot: false,
                footerSlot: false,
                otherSlot: false
            });
        });
    });

    describe('isSlotShared', function() {
        it('should return a promise which resolves to true when the backend response indicates the slot is shared', function() {
            var resolvedPromise = slotSharedService.isSlotShared("topHeaderSlot");
            $rootScope.$digest();
            expect(resolvedPromise).toBeResolvedWithData(true);
        });

        it('should return a promise which resolves to false when the backend response indicates the slot is not shared', function() {
            preparePagesContentSlotsResourceResponseWithOnePageID();
            var resolvedPromise = slotSharedService.isSlotShared("topHeaderSlot");
            $rootScope.$digest();
            expect(resolvedPromise).toBeResolvedWithData(false);
        });
    });

    function preparePagesContentSlotsResourceResponseWithOnePageID() {
        pagesContentSlotsResource.get.andReturn(
            $q.when(withParamPageId));
    }

    function pageContentListResponse() {
        return withParamPageId;
    }

    function pageContentListResponseForSlotId() {
        return withParamSlotId;
    }
});
