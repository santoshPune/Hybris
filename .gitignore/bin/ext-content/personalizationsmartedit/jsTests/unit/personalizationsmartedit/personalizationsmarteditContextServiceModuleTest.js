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
describe('Test Personalizationsmartedit Context Service Module', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var personalizationsmarteditContextService, scope;

    beforeEach(module('personalizationsmarteditContextServiceModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _personalizationsmarteditContextService_) {
        scope = _$rootScope_.$new();
        personalizationsmarteditContextService = _personalizationsmarteditContextService_;
    }));

    it('GIVEN that after object creation, all properties are set to default values', function() {
        //After object creation properties should have default values
        expect(personalizationsmarteditContextService.seExperienceData).toBe(null);
        expect(personalizationsmarteditContextService.seConfigurationData).toBe(null);
        expect(personalizationsmarteditContextService.sePreviewData).toBe(null);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(null);
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(null);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(null);
        expect(personalizationsmarteditContextService.personalizationEnabled).toBe(false);
    });

    it('GIVEN that setter methods are working properly', function() {
        //Set some mock properties
        personalizationsmarteditContextService.setSeExperienceData("mock1");
        personalizationsmarteditContextService.setSeConfigurationData("mock2");
        personalizationsmarteditContextService.setSePreviewData("mock3");
        personalizationsmarteditContextService.setSelectedVariations("mock4");
        personalizationsmarteditContextService.setSelectedCustomizations("mock5");
        personalizationsmarteditContextService.setSelectedComponents("mock6");
        personalizationsmarteditContextService.setPersonalizationContextEnabled(true);

        //Test if properties are set properly
        expect(personalizationsmarteditContextService.seExperienceData).toBe("mock1");
        expect(personalizationsmarteditContextService.seConfigurationData).toBe("mock2");
        expect(personalizationsmarteditContextService.sePreviewData).toBe("mock3");
        expect(personalizationsmarteditContextService.selectedVariations).toBe("mock4");
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe("mock5");
        expect(personalizationsmarteditContextService.selectedComponents).toBe("mock6");
        expect(personalizationsmarteditContextService.personalizationEnabled).toBe(true);
    });

});
