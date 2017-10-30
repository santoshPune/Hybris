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
describe('siteService - ', function() {

    var siteService;
    var sharedDataService;
    var siteRestService, $q, $rootScope;

    beforeEach(customMatchers);

    beforeEach(module('restServiceFactoryModule', function($provide) {
        siteRestService = jasmine.createSpyObj('siteRestService', ['get']);
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andReturn(siteRestService);
        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('sharedDataServiceModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ['get']);
        $provide.value('sharedDataService', sharedDataService);
    }));

    beforeEach(module('siteServiceModule'));

    beforeEach(inject(function(_siteService_, _$q_, _$rootScope_) {
        siteService = _siteService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    it('GIVEN sites REST call fails WHEN I request the list of sites THEN I will receive a rejected promise', function() {
        // GIVEN
        siteRestCallFails();

        // WHEN
        var promise = siteService.getSites();

        // THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN site REST call succeeds WHEN I request the list of sites THEN I will receive a promise that will resolve to a list of sites', function() {
        // GIVEN
        sitesRESTCallSucceeds();

        // WHEN
        var promise = siteService.getSites();

        // THEN
        expect(promise).toBeResolvedWithData([{
            previewUrl: "/yacceleratorstorefront?site=electronics",
            name: {
                en: 'Electronics'
            },
            uid: "electronics"
        }, {
            previewUrl: "/yacceleratorstorefront?site=apparel",
            name: {
                en: 'Apparel'
            },
            uid: "apparel"
        }]);

    });

    it('GIVEN site REST call succeeds at least one WHEN I request sites for subsequently THEN I will receive a promise that resolves to a cached list of sites AND the rest service will not be called again', function() {
        // GIVEN
        sitesRESTCallSucceeds();

        // WHEN
        siteService.getSites();
        $rootScope.$digest();
        var promise = siteService.getSites();
        $rootScope.$digest();

        // THEN
        expect(promise).toBeResolvedWithData([{
            previewUrl: "/yacceleratorstorefront?site=electronics",
            name: {
                en: 'Electronics'
            },
            uid: "electronics"
        }, {
            previewUrl: "/yacceleratorstorefront?site=apparel",
            name: {
                en: 'Apparel'
            },
            uid: "apparel"
        }]);

        expect(siteRestService.get.calls.length).toEqual(1);
    });

    it('GIVEN site REST call succeeds WHEN I request a site by id, the promise will resolve to the expected site', function() {
        // GIVEN
        sitesRESTCallSucceeds();

        // WHEN
        var promise = siteService.getSiteById("electronics");

        // THEN
        expect(promise).toBeResolvedWithData({
            previewUrl: "/yacceleratorstorefront?site=electronics",
            name: {
                en: 'Electronics'
            },
            uid: "electronics"
        });
    });


    // Helper functions
    function sitesRESTCallSucceeds() {
        siteRestService.get.andReturn($q.when({
            sites: [{
                previewUrl: "/yacceleratorstorefront?site=electronics",
                name: {
                    en: 'Electronics'
                },
                uid: "electronics"
            }, {
                previewUrl: "/yacceleratorstorefront?site=apparel",
                name: {
                    en: 'Apparel'
                },
                uid: "apparel"
            }]
        }));
    }

    function siteRestCallFails() {
        siteRestService.get.andReturn($q.reject());
    }

});
