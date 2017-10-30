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
describe('catalogService - ', function() {

    var catalogService;
    var siteService, catalogRestService, $q, $rootScope;

    beforeEach(customMatchers);

    beforeEach(module('siteServiceModule', function($provide) {
        siteService = jasmine.createSpyObj('siteService', ['getSites']);
        $provide.value('siteService', siteService);
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        catalogRestService = jasmine.createSpyObj('catalogRestService', ['get']);
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andReturn(catalogRestService);
        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('catalogServiceModule'));

    beforeEach(inject(function(_catalogService_, _$q_, _$rootScope_) {
        catalogService = _catalogService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    it('GIVEN catalog rest service call fails WHEN I request the list of catalogs THEN it should return a rejected promise', function() {
        // GIVEN
        catalogRESTcallFails();

        // WHEN
        var promise = catalogService.getCatalogsForSite('electronics');

        // THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN catalog rest service call succeeds WHEN I request the list of catalogs THEN it should return a promise resolving to a list of catalog descriptor objects', function() {
        // GIVEN
        catalogRESTCallSucceeds();

        // WHEN
        var promise = catalogService.getCatalogsForSite('electronics');

        // THEN
        expect(promise).toBeResolvedWithData([{
            name: {
                en: 'Electronics Content Catalog'
            },
            catalogId: 'electronicsContentCatalog',
            catalogVersion: 'Online'
        }]);
    });

    it('GIVEN catalog rest call succeeds at least once WHEN I request the list of catalogs for the same site subsequently THEN I will receive a promise that is resolved to a cached list of catalogs AND the rest service will not be called again', function() {
        // GIVEN
        catalogRESTCallSucceeds();

        // WHEN
        catalogService.getCatalogsForSite('electronics');
        $rootScope.$digest();
        var promise = catalogService.getCatalogsForSite('electronics');
        $rootScope.$digest();

        // THEN
        expect(promise).toBeResolvedWithData([{
            name: {
                en: 'Electronics Content Catalog'
            },
            catalogId: 'electronicsContentCatalog',
            catalogVersion: 'Online'
        }]);
        expect(catalogRestService.get.calls.length).toEqual(1);
    });

    it('GIVEN site service call fails WHEN I request a list of all catalogs grouped THEN it should return rejected promise', function() {
        //GIVEN
        siteServiceCallFails();

        //WHEN
        var promise = catalogService.getAllCatalogsGroupedById();

        //THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN site service call succeeds and catalog REST call fails WHEN I request a list of all catalogs grouped THEN it should return rejected promise', function() {
        //GIVEN
        siteServiceCallSucceeds();
        catalogRESTcallFails();

        //WHEN
        var promise = catalogService.getAllCatalogsGroupedById();

        //THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN site service call succeeds AND catalog REST call succeeds WHEN I request a list of all catalogs grouped THEN it should return a promise resolved with the list of all catalogs grouped', function() {
        //GIVEN
        siteServiceCallSucceeds();
        catalogRESTCallSucceeds();

        //WHEN
        var promise = catalogService.getAllCatalogsGroupedById();

        //THEN
        expect(promise).toBeResolvedWithData([{
            name: {
                en: 'Electronics Content Catalog'
            },
            catalogId: 'electronicsContentCatalog',
            thumbnailUrl: undefined,
            catalogVersions: [{
                name: {
                    en: 'Electronics Content Catalog'
                },
                catalogId: 'electronicsContentCatalog',
                catalogVersion: 'Online',
                thumbnailUrl: undefined,
                siteDescriptor: {
                    uid: 'electronics'
                }
            }]
        }, {
            name: {
                en: 'Apparel Content Catalog'
            },
            catalogId: 'apparelContentCatalog',
            thumbnailUrl: undefined,
            catalogVersions: [{
                name: {
                    en: 'Apparel Content Catalog'
                },
                catalogId: 'apparelContentCatalog',
                catalogVersion: 'Online',
                thumbnailUrl: undefined,
                siteDescriptor: {
                    uid: 'apparel'
                }
            }, {
                name: {
                    en: 'Apparel Content Catalog'
                },
                catalogId: 'apparelContentCatalog',
                catalogVersion: 'Staged',
                thumbnailUrl: undefined,
                siteDescriptor: {
                    uid: 'apparel'
                }
            }]
        }]);

    });

    // Helpers functions
    function catalogRESTcallFails() {
        catalogRestService.get.andReturn($q.reject());
    }

    function catalogRESTCallSucceeds() {
        catalogRestService.get.andCallFake(function(siteDTO) {
            if (siteDTO.siteUID === 'electronics') {
                return $q.when({
                    name: {
                        en: 'Electronics'
                    },
                    catalogVersionDetails: [{
                        name: {
                            en: 'Electronics Content Catalog'
                        },
                        catalogId: 'electronicsContentCatalog',
                        version: 'Online'
                    }, {
                        catalogId: 'electronicsProductCatalog',
                        version: 'Staged'
                    }]
                });
            } else if (siteDTO.siteUID === 'apparel') {
                return $q.when({
                    name: {
                        en: 'Apparel'
                    },
                    catalogVersionDetails: [{
                        name: {
                            en: 'Apparel Content Catalog'
                        },
                        catalogId: 'apparelContentCatalog',
                        version: 'Online'
                    }, {
                        name: {
                            en: 'Apparel Content Catalog'
                        },
                        catalogId: 'apparelContentCatalog',
                        version: 'Staged'
                    }]
                });
            }
        });
    }

    function siteServiceCallFails() {
        siteService.getSites.andReturn($q.reject());
    }

    function siteServiceCallSucceeds() {
        siteService.getSites.andReturn($q.when([{
            uid: 'electronics'
        }, {
            uid: 'apparel'
        }]));
    }

});
