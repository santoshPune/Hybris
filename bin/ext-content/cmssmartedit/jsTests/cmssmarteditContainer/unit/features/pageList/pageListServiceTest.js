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
describe('pageListService - ', function() {

    var pageListService;
    var pageRestService, $q, $rootScope;

    beforeEach(customMatchers);

    beforeEach(module('resourceLocationsModule', function($provide) {
        $provide.constant("CONTEXT_CATALOG", "CURRENT_CONTEXT_CATALOG");
        $provide.constant("CONTEXT_CATALOG_VERSION", "CURRENT_CONTEXT_CATALOG_VERSION");
        $provide.constant("CONTEXT_SITE_ID", "CURRENT_CONTEXT_SITE_ID");
        $provide.constant("PAGES_LIST_RESOURCE_URI", "/cmswebservices/v;/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pages");
    }));

    beforeEach(module('restServiceFactoryModule', function($provide) {
        pageRestService = jasmine.createSpyObj('pageRestService', ['get']);
        var restServiceFactory = jasmine.createSpyObj('restServiceFactory', ['get']);
        restServiceFactory.get.andReturn(pageRestService);
        $provide.value('restServiceFactory', restServiceFactory);
    }));

    beforeEach(module('pageListServiceModule'));

    beforeEach(inject(function(_pageListService_, _$q_, _$rootScope_) {
        pageListService = _pageListService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    it('GIVEN page rest service call fails WHEN I request the list of pages for a catalog THEN it will return a rejected promise', function() {
        // GIVEN
        pagesRESTcallFails();

        // WHEN
        var promise = pageListService.getPageListForCatalog('apparel-uk', 'apparel-ukContentCatalog', 'Online');

        // THEN
        expect(promise).toBeRejected();
    });

    it('GIVEN page rest service call succeeds WHEN I request the list of pages for a catalog THEN it will resolve a promise with a list of pages descriptor', function() {
        // GIVEB
        pagesRESTcallSucceeds();

        // WHEN
        var promise = pageListService.getPageListForCatalog('apparel-uk', 'apparel-ukContentCatalog', 'Online');

        // THEN
        expect(promise).toBeResolvedWithData({
            pages: [{
                creationtime: "2016-04-08T21:16:41+0000",
                modifiedtime: "2016-04-08T21:16:41+0000",
                pk: "8796387968048",
                template: "PageTemplate",
                title: "page1TitleSuffix",
                typeCode: "ContentPage",
                uid: "uid11"
            }]
        });
    });


    // Helper functions
    function pagesRESTcallFails() {
        pageRestService.get.andReturn($q.reject());
    }

    function pagesRESTcallSucceeds() {
        pageRestService.get.andCallFake(function() {
            return $q.when({
                pages: [{
                    creationtime: "2016-04-08T21:16:41+0000",
                    modifiedtime: "2016-04-08T21:16:41+0000",
                    pk: "8796387968048",
                    template: "PageTemplate",
                    title: "page1TitleSuffix",
                    typeCode: "ContentPage",
                    uid: "uid11"
                }]
            });
        });
    }

});
