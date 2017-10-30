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
describe('authorizationService', function() {

    var $rootScope, $q, authorizationService, restServiceFactory, restService, storageService;

    beforeEach(customMatchers);

    beforeEach(module('restServiceFactoryModule', function($provide) {
        restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restService = jasmine.createSpyObj('restService', ['get']);
        restServiceFactory.get.andReturn(restService);
        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('storageServiceModule', function($provide) {
        storageService = jasmine.createSpyObj('storageService', ['getPrincipalIdentifier']);
        $provide.value('storageService', storageService);
    }));

    beforeEach(module('authorizationModule'));

    beforeEach(inject(function(_$rootScope_, _$q_, _authorizationService_) {
        $rootScope = _$rootScope_;
        $q = _$q_;

        authorizationService = _authorizationService_;
    }));

    describe('canPerformOperation', function() {
        var FIRST_USER = 'FIRST_USER';
        var SECOND_USER = 'SECOND_USER';
        var READ_KEY = 'smartedit.configurationcenter.read';
        var DELETE_KEY = 'smartedit.configurationcenter.delete';
        var PERMISSION_RESPONSE = {
            id: "global",
            permissions: [{
                key: 'smartedit.configurationcenter.read',
                value: "true"
            }, {
                key: 'smartedit.configurationcenter.delete',
                value: "false"
            }]
        };

        it('should return false if no user is set', function() {
            storageService.getPrincipalIdentifier.andReturn($q.when());
            var promise = authorizationService.canPerformOperation(READ_KEY);
            expect(promise).toBeResolvedWithData(false);
        });

        it('should not query permission API if no user is set', function() {
            storageService.getPrincipalIdentifier.andReturn($q.when());
            authorizationService.canPerformOperation(READ_KEY);
            expect(restService.get.calls.length).toBe(0);
        });

        it('should query permission API only once for a given user', function() {
            storageService.getPrincipalIdentifier.andReturn($q.when(FIRST_USER));
            restService.get.andReturn($q.when(PERMISSION_RESPONSE));
            expect(restService.get).not.toHaveBeenCalled();

            authorizationService.canPerformOperation(READ_KEY);
            $rootScope.$digest();
            authorizationService.canPerformOperation(DELETE_KEY);
            $rootScope.$digest();

            expect(restService.get).toHaveBeenCalled();
            expect(restService.get.calls.length).toBe(1);
        });

        it('should query permissions API once per user', function() {
            restService.get.andReturn($q.when(PERMISSION_RESPONSE));
            storageService.getPrincipalIdentifier.andReturn($q.when(FIRST_USER));
            expect(restService.get).not.toHaveBeenCalled();

            authorizationService.canPerformOperation(READ_KEY);
            $rootScope.$digest();
            expect(restService.get.calls.length).toBe(1);

            storageService.getPrincipalIdentifier.andReturn($q.when(SECOND_USER));
            authorizationService.canPerformOperation(READ_KEY);
            $rootScope.$digest();
            expect(restService.get.calls.length).toBe(2);
        });

        it('should return false for a type key that does not has a granted permission', function() {
            storageService.getPrincipalIdentifier.andReturn($q.when(FIRST_USER));
            restService.get.andReturn($q.when(PERMISSION_RESPONSE));
            var promise = authorizationService.canPerformOperation(DELETE_KEY);
            expect(promise).toBeResolvedWithData(false);
        });

        it('should return true for a type key that has granted permission', function() {
            storageService.getPrincipalIdentifier.andReturn($q.when(FIRST_USER));
            restService.get.andReturn($q.when(PERMISSION_RESPONSE));
            var promise = authorizationService.canPerformOperation(READ_KEY);
            expect(promise).toBeResolvedWithData(true);
        });

        it('should return a cached value for repeated access of the same type key', function() {
            storageService.getPrincipalIdentifier.andReturn($q.when(FIRST_USER));
            restService.get.andReturn($q.when(PERMISSION_RESPONSE));
            var promise = authorizationService.canPerformOperation(READ_KEY);
            expect(promise).toBeResolvedWithData(true);

            promise = authorizationService.canPerformOperation(READ_KEY);
            expect(promise).toBeResolvedWithData(true);
        });
    });
});
