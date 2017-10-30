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
describe('fetch media data handler - unit test', function() {

    var $httpProvider, fetchMediaDataHandler, restServiceFactory, $q, $rootScope, CONTEXT_CATALOG, CONTEXT_CATALOG_VERSION;

    beforeEach(module('fetchMediaDataHandlerModule', function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;

        $provide.constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG");
        $provide.constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION");

    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        restServiceForMediaSearch = jasmine.createSpyObj('searchRestService', ['get']);
        restServiceForMediaFetch = jasmine.createSpyObj('fetchRestService', ['get']);
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        //restServiceFactory.get.andReturn(catalogRestService);

        restServiceFactory.get.andCallFake(function(uri) {
            if (uri === '/cmswebservices/v1/media') {
                return restServiceForMediaSearch;
            } else {
                return restServiceForMediaFetch;
            }
        });

        restServiceForMediaSearch.get.andCallFake(function(value) {
            return $q.when({
                media: [{
                    1: 'a',
                    2: 'b'
                }, {
                    3: 'c',
                    4: 'd'
                }]
            });
        });

        restServiceForMediaFetch.get.andCallFake(function(value) {
            return $q.when({
                media: {
                    3: 'c',
                    4: 'd'
                }
            });
        });

        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(inject(function(_$rootScope_, _fetchMediaDataHandler_, _$q_, _CONTEXT_CATALOG_, _CONTEXT_CATALOG_VERSION_) {
        $rootScope = _$rootScope_;
        $q = _$q_;
        fetchMediaDataHandler = _fetchMediaDataHandler_;
        CONTEXT_CATALOG = _CONTEXT_CATALOG_;
        CONTEXT_CATALOG_VERSION = _CONTEXT_CATALOG_VERSION_;
    }));

    beforeEach(customMatchers);

    it('should search for the media', function() {

        var result = fetchMediaDataHandler.findByMask("test");

        $rootScope.$digest();

        result.then(
            function(response) {
                expect(response.length == 2).toBe(true);
            }
        );
        $rootScope.$digest();
    });


    it('should fetch media by id', function() {

        var result = fetchMediaDataHandler.getById("code");

        $rootScope.$digest();

        result.then(
            function(response) {
                expect(typeof(response) == typeof({})).toBe(true);
            }
        );
        $rootScope.$digest();
    });

});
