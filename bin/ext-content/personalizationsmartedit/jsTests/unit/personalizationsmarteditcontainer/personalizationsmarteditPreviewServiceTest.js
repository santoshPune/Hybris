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
 *
 *
 */
describe('personalizationsmarteditPreviewService service', function() {

    var personalizationsmarteditPreviewService;
    var sharedDataService, personalizationsmarteditRestService, personalizationsmarteditUtils, personalizationsmarteditMessageHandler, personalizationsmarteditContextService;
    var $q, $rootScope, $translate;

    beforeEach(customMatchers);

    beforeEach(module('sharedDataServiceModule', function($provide) {
        sharedDataService = jasmine.createSpyObj('sharedDataService', ['set', 'get']);
        $provide.value("sharedDataService", sharedDataService);
    }));

    beforeEach(module('personalizationsmarteditRestServiceModule', function($provide) {
        personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getPreviewTicket', 'updatePreviewTicket', 'createPreviewTicket']);
        $provide.value("personalizationsmarteditRestService", personalizationsmarteditRestService);
    }));

    beforeEach(module('personalizationsmarteditCommons', function($provide) {

        $translate = jasmine.createSpyObj('$translate', ['instant']);
        $provide.value('$translate', $translate);
        $provide.value('translateFilter', function(data) {
            return data;
        });

        personalizationsmarteditUtils = jasmine.createSpyObj('personalizationsmarteditUtils', ['getVariationCodes']);
        $provide.value("personalizationsmarteditUtils", personalizationsmarteditUtils);

        personalizationsmarteditMessageHandler = jasmine.createSpyObj('personalizationsmarteditMessageHandler', ['sendError', 'sendInformation']);
        $provide.value("personalizationsmarteditMessageHandler", personalizationsmarteditMessageHandler);
    }));

    beforeEach(module('personalizationsmarteditContextServiceModule', function($provide) {
        personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['refreshPreviewData', 'getSeExperienceData', 'getSeConfigurationData']);
        $provide.value("personalizationsmarteditContextService", personalizationsmarteditContextService);
    }));

    beforeEach(module('personalizationsmarteditPreviewServiceModule'));
    beforeEach(inject(function(_personalizationsmarteditPreviewService_, _$q_, _$rootScope_) {
        personalizationsmarteditPreviewService = _personalizationsmarteditPreviewService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    it('implements all called by other services functions', function() {
        expect(personalizationsmarteditPreviewService.createPreviewTicket).toBeDefined();
        expect(personalizationsmarteditPreviewService.updatePreviewTicketWithVariations).toBeDefined();
        expect(personalizationsmarteditPreviewService.removePersonalizationDataFromPreview).toBeDefined();
        expect(personalizationsmarteditPreviewService.storePreviewTicketData).toBeDefined();
    });

    it('storePreviewTicketData should call shareddataservice', function() {
        //having
        var resourcePath = 'previewResourcePath';
        var ticketId = 'previewTicketId';

        //when
        personalizationsmarteditPreviewService.storePreviewTicketData(resourcePath, ticketId);

        //then
        expect(sharedDataService.set).toHaveBeenCalledWith('preview', {
            previewTicketId: ticketId,
            resourcePath: resourcePath
        });
    });

    it('calls updatepreviewticket functionality with empty array to remove personalization data from preview', function() {

        spyOn(personalizationsmarteditPreviewService, 'updatePreviewTicketWithVariations');

        personalizationsmarteditPreviewService.updatePreviewTicketWithVariations.andReturn($q.defer().promise);
        //having
        var previewTicketId = 'previewTicketId';
        //when
        personalizationsmarteditPreviewService.removePersonalizationDataFromPreview(previewTicketId);
        //then
        expect(personalizationsmarteditPreviewService.updatePreviewTicketWithVariations).toHaveBeenCalledWith(previewTicketId, []);
    });

    it('updates preview ticket after successful retrieving ticket with given id', function() {
        //having
        var selectedVariations = ["var1", "var2"];

        var previewTicket = {
            ticketId: "previewTicketId",
            catalog: "catalogId",
            catalogVersion: "catalogVersion",
            language: "languageIsoCode",
            resourcePath: "resourcePath",
            variations: selectedVariations
        };

        personalizationsmarteditRestService.getPreviewTicket.andReturn($q.when(previewTicket));
        //when
        personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicket.ticketId, selectedVariations);
        $rootScope.$digest();

        //then
        expect(personalizationsmarteditRestService.getPreviewTicket).toHaveBeenCalledWith(previewTicket.ticketId);
        expect(personalizationsmarteditRestService.updatePreviewTicket).toHaveBeenCalledWith(previewTicket);
    });

    it('creates new preview ticket after unsuccessful retrieving ticket with given id for updating preview ticket call', function() {

        spyOn(personalizationsmarteditPreviewService, 'createPreviewTicket');

        //having
        var selectedVariations = ["var1", "var2"];

        var previewTicket = {
            ticketId: "wrongPreviewTicketId"
        };

        var notFoundResponse = {
            status: 404
        };
        personalizationsmarteditRestService.getPreviewTicket.andReturn($q.reject(notFoundResponse));
        personalizationsmarteditUtils.getVariationCodes.andReturn(selectedVariations);

        //when
        personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicket.ticketId, selectedVariations);
        $rootScope.$digest();

        //then
        expect(personalizationsmarteditRestService.getPreviewTicket).toHaveBeenCalledWith(previewTicket.ticketId);
        expect(personalizationsmarteditRestService.updatePreviewTicket).not.toHaveBeenCalled();
        expect(personalizationsmarteditPreviewService.createPreviewTicket).toHaveBeenCalledWith(selectedVariations);
    });

    it('error message is being shown through calling personalizationsmarteditMessageHandler when GET for previewTicket returns 400 response status for updating preview ticket call', function() {

        spyOn(personalizationsmarteditPreviewService, 'createPreviewTicket');

        //having
        var selectedVariations = ["var1", "var2"];

        var previewTicket = {
            ticketId: "wrongPreviewTicketId"
        };

        var badRequestResponse = {
            status: 400
        };
        personalizationsmarteditRestService.getPreviewTicket.andReturn($q.reject(badRequestResponse));
        personalizationsmarteditUtils.getVariationCodes.andReturn(selectedVariations);

        //when
        personalizationsmarteditPreviewService.updatePreviewTicketWithVariations(previewTicket.ticketId, selectedVariations);
        $rootScope.$digest();

        //then
        expect(personalizationsmarteditRestService.getPreviewTicket).toHaveBeenCalledWith(previewTicket.ticketId);
        expect(personalizationsmarteditRestService.updatePreviewTicket).not.toHaveBeenCalled();
        expect(personalizationsmarteditMessageHandler.sendError).toHaveBeenCalled();
    });

    it('creates preview ticket with default values from SE configuration and given array of variations', function() {
        spyOn(personalizationsmarteditPreviewService, 'storePreviewTicketData');

        //having
        var selectedVariations = ["var1", "var2"];

        var experience = {
            siteDescriptor: {
                name: "some name",
                previewUrl: "/someURI/?someSite=site",
                uid: "some uid"
            },
            catalogDescriptor: {
                name: "some cat name",
                catalogId: "some cat uid",
                catalogVersion: "some cat version"
            },
            languageDescriptor: {
                isocode: "some language isocode",
            },
            time: null
        };

        var configuration = {
            previewTicketURI: 'thepreviewTicketUri'
        };

        var resourcePath = configuration.domain + experience.siteDescriptor.previewUrl;

        var previewTicket = {
            catalog: experience.catalogDescriptor.catalogId,
            catalogVersion: experience.catalogDescriptor.catalogVersion,
            language: experience.languageDescriptor.isocode,
            resourcePath: resourcePath,
            variations: selectedVariations
        };

        var responsePreviewTicket = {
            ticketId: "ticketId",
            resourcePath: resourcePath
        };

        personalizationsmarteditContextService.getSeExperienceData.andReturn(experience);
        personalizationsmarteditContextService.getSeConfigurationData.andReturn(configuration);

        personalizationsmarteditRestService.createPreviewTicket.andReturn($q.when(responsePreviewTicket));

        //when
        personalizationsmarteditPreviewService.createPreviewTicket(selectedVariations);
        $rootScope.$digest();

        //then
        expect(personalizationsmarteditRestService.createPreviewTicket).toHaveBeenCalledWith(previewTicket);
        expect(personalizationsmarteditPreviewService.storePreviewTicketData).toHaveBeenCalledWith(responsePreviewTicket.resourcePath, responsePreviewTicket.ticketId);
        expect(personalizationsmarteditMessageHandler.sendInformation).toHaveBeenCalled();
    });

    it('shows error message if 400 response status has been returned after creating preview ticket call', function() {
        spyOn(personalizationsmarteditPreviewService, 'storePreviewTicketData');

        //having
        var selectedVariations = ["var1", "var2"];

        var experience = {
            siteDescriptor: {
                name: "some name",
                previewUrl: "/someURI/?someSite=site",
                uid: "some uid"
            },
            catalogDescriptor: {
                name: "some cat name",
                catalogId: "some cat uid",
                catalogVersion: "some cat version"
            },
            languageDescriptor: {
                isocode: "some language isocode",
            },
            time: null
        };

        var configuration = {
            previewTicketURI: 'thepreviewTicketUri'
        };

        var badRequestResponse = {
            status: 400
        };

        personalizationsmarteditContextService.getSeExperienceData.andReturn(experience);
        personalizationsmarteditContextService.getSeConfigurationData.andReturn(configuration);

        personalizationsmarteditRestService.createPreviewTicket.andReturn($q.reject(badRequestResponse));

        //when
        personalizationsmarteditPreviewService.createPreviewTicket(selectedVariations);
        $rootScope.$digest();

        //then
        expect(personalizationsmarteditMessageHandler.sendError).toHaveBeenCalled();
    });
});
