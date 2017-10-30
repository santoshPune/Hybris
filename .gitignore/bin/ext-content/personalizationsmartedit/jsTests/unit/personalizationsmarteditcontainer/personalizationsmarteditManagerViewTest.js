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
describe('Test Personalizationsmartedit Manager View Module', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var personalizationsmarteditManagerView, personalizationsmarteditManagerViewController, personalizationsmarteditContextService, scope;

    beforeEach(module('personalizationsmarteditContextServiceModule', function($provide) {
        personalizationsmarteditContextService = jasmine.createSpyObj('personalizationsmarteditContextService', ['getSeExperienceData']);
        $provide.value("personalizationsmarteditContextService", personalizationsmarteditContextService);
    }));

    beforeEach(module('personalizationsmarteditManagerViewModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _$controller_, _personalizationsmarteditManagerView_, _personalizationsmarteditContextService_) {
        scope = _$rootScope_.$new();
        personalizationsmarteditManagerView = _personalizationsmarteditManagerView_;
        personalizationsmarteditContextService = _personalizationsmarteditContextService_;

        mockModules.modalService.open.andCallFake(function() {
            return _$q_.defer().promise;
        });

        mockModules.confirmationModalService.confirm.andCallFake(function() {
            return _$q_.defer().promise;
        });

        personalizationsmarteditContextService.getSeExperienceData.andCallFake(function() {
            return {
                catalogDescriptor: {
                    name: {
                        en: "testName"
                    },
                    catalogVersion: "testOnline"
                },
                languageDescriptor: {
                    isocode: "en",
                }
            };
        });

        personalizationsmarteditManagerViewController = _$controller_('personalizationsmarteditManagerViewController', {
            '$scope': scope
        });

    }));

    it('GIVEN that personalizationsmarteditManagerView is instantiated it contains proper functions', function() {
        expect(personalizationsmarteditManagerView.openManagerAction).toBeDefined();
    });

    it('GIVEN that personalizationsmarteditManagerView openManagerAction is called it is calling proper services', function() {
        personalizationsmarteditManagerView.openManagerAction();
        expect(mockModules.modalService.open).toHaveBeenCalled();
    });


    it('GIVEN that personalizationsmarteditManagerViewController is instantiated it contains proper functions and objects', function() {
        expect(scope.catalogName).toBeDefined();
        expect(scope.customizations).toBeDefined();
        expect(scope.allCustomizationsCount).not.toBeDefined();
        expect(scope.filteredCustomizationsCount).toBeDefined();
        expect(scope.search.name).toBeDefined();
        expect(scope.pagination).toBeDefined();
        expect(scope.searchInputKeypress).toBeDefined();
        expect(scope.editCustomizationAction).toBeDefined();
        expect(scope.deleteCustomizationAction).toBeDefined();
        expect(scope.editVariationAction).toBeDefined();
        expect(scope.deleteVariationAction).toBeDefined();
        expect(scope.openNewModal).toBeDefined();
        expect(scope.paginationCallback).toBeDefined();
        expect(scope.setCustomizationRank).toBeDefined();
        expect(scope.setVariationRank).toBeDefined();
        expect(scope.isUndefined).toBeDefined();
        expect(scope.isFilterEnabled).toBeDefined();
        expect(scope.resetSearchInput).toBeDefined();
    });

    it('GIVEN that function deleteCustomizationAction in personalizationsmarteditManagerViewController is called it is calling proper services', function() {
        scope.deleteCustomizationAction();
        expect(mockModules.confirmationModalService.confirm).toHaveBeenCalled();
    });

    it('GIVEN that function deleteVariationAction in personalizationsmarteditManagerViewController is called it is calling proper services', function() {
        var variation1 = {
            code: "var1"
        };
        var variation2 = {
            code: "var2"
        };
        var customization = {
            code: "test",
            variations: [variation1, variation2]
        };
        scope.deleteVariationAction(customization);
        expect(mockModules.confirmationModalService.confirm).toHaveBeenCalled();
    });

});
