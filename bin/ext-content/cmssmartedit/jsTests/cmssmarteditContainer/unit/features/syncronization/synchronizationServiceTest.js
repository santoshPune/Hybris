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
describe("sync service  - unit test", function() {
    var $httpProvider, synchronizationService, restServiceFactory, $q, $rootScope, SYNC_PATH;
    var interval, authenticationService;

    var theCatalog = {
        catalogId: "catalog"
    };
    var theCatalogGetStatus = {
        "date": "2016-02-12T16:08:29+0000",
        "status": "FINISHED"
    };
    var theCatalogUpdateStatus = {
        "date": "2016-02-12T17:09:29+0000",
        "status": "FINISHED"
    };

    var secondCatalog = {
        catalogId: 'second catalog'
    };
    var secondCatalogGetStatus = {
        "date": "2016-04-01T12:00:00+0000",
        "status": "PENDING"
    };

    beforeEach(module('pascalprecht.translate'));

    beforeEach(function() {
        angular.module('resourceLocationsModule', []);
        angular.module('translationServiceModule', []);
        angular.module('alertServiceModule', []);
        angular.module('confirmationModalServiceModule', []);
        angular.module('authenticationModule', []);
    });

    beforeEach(module("synchronizationServiceModule", function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;
        $provide.constant("SYNC_PATH", "the_sync_path/:catalog");
    }));

    beforeEach(module('authenticationModule', function($provide) {
        authenticationService = jasmine.createSpyObj('authenticationService', ['isAuthenticated']);
        authenticationService.isAuthenticated.andCallFake(function(url) {
            if (url === SYNC_PATH.replace(":catalog", theCatalog.catalogId)) {
                return $q.when(true);
            } else {
                return $q.when(false);
            }
        });
        $provide.value('authenticationService', authenticationService);
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        var restServiceForSync = jasmine.createSpyObj('searchRestService', ['get', 'update']);

        restServiceFactory.get.andCallFake(function(uri, catalog) {
            return restServiceForSync;
        });

        restServiceForSync.update.andCallFake(function(value) {
            if (value.catalog == "catalog") {
                return $q.when(theCatalogUpdateStatus);
            }
        });

        restServiceForSync.get.andCallFake(function(value) {
            if (value.catalog == "catalog") {
                return $q.when(theCatalogGetStatus);
            } else if (value.catalog == "second catalog") {
                return $q.when(secondCatalogGetStatus);
            }
        });

        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('alertServiceModule', function($provide) {
        var alertService = jasmine.createSpyObj('alertService', ['pushAlerts']);
        $provide.value('alertService', alertService);
    }));

    beforeEach(inject(function(_$rootScope_, _synchronizationService_, _$q_, _$interval_, _SYNC_PATH_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        interval = _$interval_;
        synchronizationService = _synchronizationService_;
        SYNC_PATH = _SYNC_PATH_;
    }));


    it('should update sync status ', function() {

        var result = synchronizationService.updateCatalogSync(theCatalog);

        $rootScope.$digest();

        result.then(
            function(response) {
                expect(response.date).toEqual("2016-02-12T17:09:29+0000");
                expect(response.status).toEqual("FINISHED");
            }
        );
        $rootScope.$digest();
    });


    it('should get catalog sync status', function() {

        var result = synchronizationService.getCatalogSyncStatus(theCatalog);

        $rootScope.$digest();

        result.then(
            function(response) {
                expect(response.date).toEqual("2016-02-12T16:08:29+0000");
                expect(response.status).toEqual("FINISHED");
            }
        );
        $rootScope.$digest();
    });


    it('should call "get synchronization status" after interval.', function() {

        var callback = jasmine.createSpy('callback');

        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        interval.flush(5000);
        $rootScope.$digest();
        expect(callback.calls.length).toBe(1);
        interval.flush(5000);
        $rootScope.$digest();
        expect(callback.calls.length).toBe(2);
    });

    it('should stop calling "get sync update" on destroy', function() {

        var callback = jasmine.createSpy('callback');

        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        interval.flush(5000);
        $rootScope.$digest();
        expect(callback.calls.length).toBe(1);

        synchronizationService.stopAutoGetSyncData(theCatalog);
        interval.flush(5000);
        $rootScope.$digest();
        expect(callback.calls.length).toBe(1);
    });

    it('should stop calling "get sync update" on authentication failure', function() {

        var callback = jasmine.createSpy('callback');

        spyOn(synchronizationService, 'stopAutoGetSyncData').andCallThrough();
        spyOn(synchronizationService, 'getCatalogSyncStatus').andReturn($q.reject());

        synchronizationService.startAutoGetSyncData(secondCatalog, callback);
        interval.flush(5000);
        $rootScope.$digest();

        expect(synchronizationService.stopAutoGetSyncData).toHaveBeenCalled();
        expect(synchronizationService.getCatalogSyncStatus).toHaveBeenCalled();
    });

    it('should continue calling "get sync update" on authentication success', function() {

        var callback = jasmine.createSpy('callback').andReturn($q.reject());

        spyOn(synchronizationService, 'stopAutoGetSyncData').andCallThrough();
        spyOn(synchronizationService, 'getCatalogSyncStatus').andCallThrough();

        synchronizationService.startAutoGetSyncData(theCatalog, callback);
        interval.flush(5000);
        $rootScope.$digest();

        expect(synchronizationService.stopAutoGetSyncData).not.toHaveBeenCalled();
        expect(synchronizationService.getCatalogSyncStatus).toHaveBeenCalled();
    });
});
