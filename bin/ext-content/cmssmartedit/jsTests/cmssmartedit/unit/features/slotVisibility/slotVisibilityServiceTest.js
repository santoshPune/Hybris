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
describe('slotVisibilityService', function() {

    var restServiceFactory, slotVisibilityService, itemResource, pagesContentSlotsComponentsResource;
    var renderService;
    var $q, $rootScope;

    beforeEach(customMatchers);

    beforeEach(function() {
        angular.module('renderServiceModule', []);
    });

    beforeEach(module('resourceLocationsModule', function($provide) {
        $provide.constant('ITEMS_RESOURCE_URI', 'ITEMS');
        $provide.constant('PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', 'SLOTS');
    }));

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ["renderSlots"]);
        renderService.renderSlots.andReturn();
        $provide.value("renderService", renderService);
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        itemResource = jasmine.createSpyObj('itemResource', ['get']);
        pagesContentSlotsComponentsResource = jasmine.createSpyObj('pagesContentSlotsComponentsResource', ['get']);
        restServiceFactory.get.andCallFake(function(uri) {
            if (uri === 'SLOTS') {
                return pagesContentSlotsComponentsResource;
            } else if (uri === 'ITEMS') {
                return itemResource;
            } else {
                return null;
            }
        });
        $provide.value('restServiceFactory', restServiceFactory);
        $provide.value('itemResource', itemResource);
        $provide.value('pagesContentSlotsComponentsResource', pagesContentSlotsComponentsResource);
    }));

    beforeEach(module('resourceModule'));

    beforeEach(module('slotVisibilityServiceModule', function($provide) {
        var componentHandlerService = jasmine.createSpyObj('componentHandlerService', ['getPageUID']);
        componentHandlerService.getPageUID.andReturn('homepage');
        $provide.value('componentHandlerService', componentHandlerService);
    }));

    beforeEach(inject(function(_SlotVisibilityService_, _$q_, _$rootScope_) {
        $q = _$q_;
        slotVisibilityService = new _SlotVisibilityService_();
        $rootScope = _$rootScope_;
    }));

    afterEach(function() {
        expect(pagesContentSlotsComponentsResource.get).toHaveBeenCalledWith({
            pageId: 'homepage'
        });
    });

    describe('content slots per page is empty', function() {
        var SLOT = 'some-slot-id';
        beforeEach(function() {
            itemResource.get.andReturn($q.when([]));
            pagesContentSlotsComponentsResource.get.andReturn($q.when([]));
        });

        it('should have an empty hidden component list.', function() {
            expect(slotVisibilityService.getHiddenComponents(SLOT)).toBeResolvedWithData([]);
        });

        it('should have zero hidden components.', function() {
            expect(slotVisibilityService.getHiddenComponentCount(SLOT)).toBeResolvedWithData(0);
        });
    });

    describe('content slots per page is not empty', function() {
        var SLOT1 = 'some-slot-id-1';
        var SLOT2 = 'some-slot-id-2';

        beforeEach(function() {
            itemResource.get.andReturn($q.when({
                componentItems: [{
                    visible: false,
                    uid: 1
                }, {
                    visible: false,
                    uid: 2
                }, {
                    visible: false,
                    uid: 3
                }]
            }));
            pagesContentSlotsComponentsResource.get.andReturn($q.when({
                pageContentSlotComponentList: [{
                    slotId: SLOT1,
                    componentId: 1
                }, {
                    slotId: SLOT1,
                    componentId: 2
                }, {
                    slotId: SLOT2,
                    componentId: 3
                }]
            }));
        });

        it('should return a non-empty the hidden component list', function() {
            expect(slotVisibilityService.getHiddenComponents(SLOT1)).toBeResolvedWithData(
                [{
                    visible: false,
                    uid: 1
                }, {
                    visible: false,
                    uid: 2
                }]);
        });

        it('should have hidden components on the count.', function() {
            expect(slotVisibilityService.getHiddenComponentCount(SLOT1)).toBeResolvedWithData(2);
        });
    });

    describe('content slots per page is not empty and the component visibility has changed', function() {
        var SLOT1 = 'some-slot-id-1';
        beforeEach(function() {
            pagesContentSlotsComponentsResource.get.andReturn($q.when({
                pageContentSlotComponentList: [{
                    slotId: SLOT1,
                    componentId: 1
                }, {
                    slotId: SLOT1,
                    componentId: 2
                }]
            }));
        });
        it('should re-render the content slot.', function() {
            itemResource.get.andReturn($q.when({
                componentItems: [{
                    visible: false,
                    uid: 1
                }, {
                    visible: false,
                    uid: 2
                }]
            }));
            slotVisibilityService.loadHiddenComponents();
            $rootScope.$digest();
            expect(renderService.renderSlots).not.toHaveBeenCalled();

            itemResource.get.andReturn($q.when({
                componentItems: [{
                    visible: false,
                    uid: 1
                }, {
                    visible: true,
                    uid: 2
                }]
            }));
            $rootScope.$digest();
            slotVisibilityService.loadHiddenComponents();
            $rootScope.$digest();
            expect(renderService.renderSlots).toHaveBeenCalledWith([SLOT1]);
        });
    });
});
