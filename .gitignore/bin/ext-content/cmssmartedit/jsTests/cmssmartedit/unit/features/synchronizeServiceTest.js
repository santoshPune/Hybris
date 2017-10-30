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
describe('test SynchronizeService class', function() {

    var $rootScope, $q, smarteditComponentType, smarteditComponentId, SynchronizeService, restServiceFactory, restService;

    beforeEach(function() {
        angular.module('translationServiceModule', []);
        angular.module('restServiceFactoryModule', []);
    });

    beforeEach(module('restServiceFactoryModule', function($provide) {
        restService = jasmine.createSpyObj('restService', ['get', 'save']);
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andCallFake(function(uri) {
            if (uri === '/cmswebservices/:smarteditComponentType/:smarteditComponentId/synchronize') {
                return restService;
            }
        });

        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('synchronizeDecorator', function() {

        smarteditComponentType = "smarteditComponentType";
        smarteditComponentId = "smarteditComponentId";

    }));

    beforeEach(customMatchers);
    beforeEach(testSetup); //includes $rootScope and $q
    beforeEach(inject(function(_$rootScope_, _$q_, _SynchronizeService_, _$httpBackend_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        SynchronizeService = _SynchronizeService_;
        _$httpBackend_.whenGET('somePath/en_US').respond({});
    }));

    it('SynchronizeService initializes fine', function() {

        var editor = new SynchronizeService(smarteditComponentType, smarteditComponentId);

        expect(editor.synced).toBe(true);
        expect(editor.smarteditComponentType).toBe(smarteditComponentType);
        expect(editor.smarteditComponentId).toBe(smarteditComponentId);
        expect(editor.restService).toBe(restService);

    });


    it('successful load will set sync status returned by REST call', function() {

        var response = {
            status: false
        };
        var deferred = $q.defer();
        deferred.resolve(response);
        restService.get.andReturn(deferred.promise);
        var editor = new SynchronizeService(smarteditComponentType, smarteditComponentId);

        editor.load();
        //for promises to actually resolve :
        $rootScope.$digest();

        expect(restService.get).toHaveBeenCalledWith({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId
        });
        expect(editor.synced).toBe(false);

    });

    it('successful synchronize will set sync status returned by REST call', function() {

        var response = {
            status: true
        };
        var deferred = $q.defer();
        deferred.resolve(response);
        restService.save.andReturn(deferred.promise);
        var editor = new SynchronizeService(smarteditComponentType, smarteditComponentId);
        editor.synced = false;

        editor.synchronize();
        //for promises to actually resolve :
        $rootScope.$digest();

        expect(restService.save).toHaveBeenCalledWith({
            smarteditComponentType: smarteditComponentType,
            smarteditComponentId: smarteditComponentId
        }, {});
        expect(editor.synced).toBe(true);

    });

});
