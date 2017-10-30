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
describe('Test Personalizationsmartedit Container Module', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var mockVariation = {
        code: "testVariation"
    };
    var mockCustomization = {
        code: "testCustomization",
        variations: [mockVariation]
    };
    var mockComponentList = ['component1', 'component2'];

    var personalizationsmarteditRightToolbarController, scope, personalizationsmarteditContextService;

    beforeEach(module('personalizationsmarteditRestServiceModule', function($provide) {
        mockModules.personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getCustomizations', 'getComponenentsIdsForVariation']);
        $provide.value('personalizationsmarteditRestService', mockModules.personalizationsmarteditRestService);
    }));

    beforeEach(module('personalizationsmarteditPreviewServiceModule', function($provide) {
        mockModules.personalizationsmarteditPreviewService = jasmine.createSpyObj('personalizationsmarteditPreviewService', ['updatePreviewTicketWithVariations']);
        $provide.value('personalizationsmarteditPreviewService', mockModules.personalizationsmarteditPreviewService);
    }));

    beforeEach(module('personalizationsmarteditManagerModule', function($provide) {
        mockModules.personalizationsmarteditManager = jasmine.createSpyObj('personalizationsmarteditManager', ['openCreateCustomizationModal']);
        $provide.value('personalizationsmarteditManager', mockModules.personalizationsmarteditManager);
    }));

    beforeEach(module('personalizationsmarteditcontainermodule'));
    beforeEach(inject(function($controller, _$rootScope_, _$q_, _$timeout_, _personalizationsmarteditContextService_) {
        scope = _$rootScope_.$new();
        $timeout = _$timeout_;
        mockModules.personalizationsmarteditRestService.getCustomizations.andCallFake(function() {
            var deferred = _$q_.defer();
            deferred.resolve({
                customizations: [mockCustomization, mockCustomization],
                pagination: {
                    count: 5,
                    page: 0,
                    totalCount: 5,
                    totalPages: 1
                }
            });
            return deferred.promise;
        });
        mockModules.personalizationsmarteditRestService.getComponenentsIdsForVariation.andCallFake(function() {
            var deferred = _$q_.defer();
            deferred.resolve({
                components: mockComponentList
            });
            return deferred.promise;
        });
        mockModules.personalizationsmarteditPreviewService.updatePreviewTicketWithVariations.andCallFake(function() {
            return _$q_.defer().promise;
        });
        personalizationsmarteditRightToolbarController = $controller('personalizationsmarteditRightToolbarController', {
            $scope: scope
        });
        personalizationsmarteditContextService = _personalizationsmarteditContextService_;
        spyOn(personalizationsmarteditContextService, 'getSePreviewData').andCallFake(function() {
            return {
                previewTicketId: "mockTicketId"
            };
        });
    }));

    it('GIVEN that personalizationsmarteditcontainermodule is instantiated menu values should be added to smartedit toolbars', function() {
        expect(mockModules.featureService.addToolbarItem).toHaveBeenCalled();
        expect(mockModules.featureService.register).toHaveBeenCalled();
        expect(mockModules.perspectiveService.register).toHaveBeenCalled();
    });

    it('GIVEN that personalizationsmarteditRightToolbarController is instantiated array scope.customizations should be empty', function() {
        expect(scope.customizationsOnPage).toBeDefined();
        expect(scope.customizationsOnPage.length).toBe(0);
    });

    it('GIVEN that getCustomization method is called, array scope.customizations should contain objects return by REST service', function() {
        scope.$digest();

        $timeout.flush();

        expect(scope.customizationsOnPage).toBeDefined();
        expect(scope.customizationsOnPage.length).toBe(2);
        expect(scope.customizationsOnPage).toContain(mockCustomization);
    });

    it('GIVEN that variationClick method is called, all objects in contex service are set properly', function() {
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(null);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(null);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(null);
        scope.variationClick(mockCustomization, mockVariation);
        scope.$digest();
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(null);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(mockVariation);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(mockComponentList);
    });

    it('GIVEN that customizationClick method is called, all objects in contex service are set properly', function() {
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(null);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(null);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(null);
        scope.customizationClick(mockCustomization);
        scope.$digest();
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(mockCustomization);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(mockCustomization.variations);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(mockComponentList);
    });

});
