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
describe('test removeComponentService class', function() {

    var restServiceFactory, removeComponentService, restServiceForRemoveComponent, experienceInterceptor, renderService, sharedDataService;

    beforeEach(function() {
        angular.module('restServiceFactoryModule', []);
        angular.module('renderServiceModule', []);
        angular.module('sharedDataServiceModule', []);
        angular.module('experienceInterceptorModule', []);
        angular.module('gatewayProxyModule', []);

    });

    beforeEach(module('removeComponentServiceModule'));

    beforeEach(module('resourceLocationsModule', function($provide) {
        $provide.constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG");
        $provide.constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION");
        $provide.constant("CONTEXT_SITE_ID", "CURRENT_CONTEXT_SITE_ID");
        $provide.constant("PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI", "/cmswebservices/v1/sites/CURRENT_CONTEXT_SITE_ID/catalogs/CURRENT_CONTEXT_CATALOG/versions/CURRENT_CONTEXT_CATALOG_VERSION/pagescontentslotscomponents");
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        restServiceForRemoveComponent = jasmine.createSpyObj('restServiceForRemoveComponent', ['remove']);

        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andCallFake(function() {
            return restServiceForRemoveComponent;
        });
        $provide.value('restServiceFactory', restServiceFactory);
        $provide.value('experienceInterceptor', experienceInterceptor);
    }));

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ["renderRemoval"]);
        renderService.renderRemoval.andReturn();
        $provide.value("renderService", renderService);
    }));

    beforeEach(module('experienceInterceptorModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);
        sharedDataService.get.andReturn();
        $provide.value('sharedDataService', sharedDataService);
        $provide.value('experienceInterceptor', experienceInterceptor);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(customMatchers);
    beforeEach(testSetup); //includes $rootScope and $q
    beforeEach(inject(function(_removeComponentService_) {
        removeComponentService = _removeComponentService_;
    }));

    var payload = {
        'slotId': 'testSlotId',
        'componentId': 'testContainerId'
    };

    var COMPONENT_ID = 'testComponentId';
    var COMPONENT_TYPE = 'componentType';
    var SLOT_ID = 'testSlotId';

    it('should remove a component from a slot', function() {

        restServiceForRemoveComponent.remove.andReturn($q.when());
        removeComponentService.removeComponent({
            slotId: SLOT_ID,
            componentId: COMPONENT_ID,
            componentType: COMPONENT_TYPE,
            slotOperationRelatedId: 'testContainerId',
            slotOperationRelatedType: 'testContainerType',
        });

        $rootScope.$digest();

        expect(restServiceForRemoveComponent.remove).toHaveBeenCalledWith(payload);
        expect(renderService.renderRemoval).toHaveBeenCalledWith(COMPONENT_ID, COMPONENT_TYPE, SLOT_ID);
    });

    it('should not remove a component from a slot', function() {

        restServiceForRemoveComponent.remove.andReturn($q.reject());
        removeComponentService.removeComponent({
            slotId: SLOT_ID,
            componentId: COMPONENT_ID,
            componentType: COMPONENT_TYPE,
            slotOperationRelatedId: 'testContainerId',
            slotOperationRelatedType: 'testContainerType',
        });

        $rootScope.$digest();

        expect(restServiceForRemoveComponent.remove).toHaveBeenCalledWith(payload);
        expect(renderService.renderRemoval).not.toHaveBeenCalled();
    });
});
