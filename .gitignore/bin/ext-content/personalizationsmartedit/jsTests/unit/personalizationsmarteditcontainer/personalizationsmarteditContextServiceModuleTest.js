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

    var mockConfig = {
        test: "test"
    };

    var personalizationsmarteditContextService, personalizationsmarteditContextServiceProxy, scope;

    beforeEach(module('personalizationsmarteditContextServiceModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _personalizationsmarteditContextService_) {
        mockModules.sharedDataService.get.andCallFake(function() {
            var deferred = _$q_.defer();
            deferred.resolve(mockConfig);
            return deferred.promise;
        });
        mockModules.loadConfigManagerService.loadAsObject.andCallFake(function() {
            var deferred = _$q_.defer();
            deferred.resolve(mockConfig);
            return deferred.promise;
        });
        scope = _$rootScope_.$new();
        personalizationsmarteditContextService = _personalizationsmarteditContextService_;
        personalizationsmarteditContextServiceProxy = personalizationsmarteditContextService.getContexServiceProxy();

        //Create spy objects
        spyOn(personalizationsmarteditContextService, 'refreshExperienceData').andCallThrough();
        spyOn(personalizationsmarteditContextService, 'refreshPreviewData').andCallThrough();
        spyOn(personalizationsmarteditContextService, 'refreshConfigurationData').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setSeExperienceData').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setSePreviewData').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setSeConfigurationData').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setSelectedVariations').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setSelectedCustomizations').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setSelectedComponents').andCallThrough();
        spyOn(personalizationsmarteditContextServiceProxy, 'setPersonalizationContextEnabled').andCallThrough();
    }));

    it('GIVEN that applySynchronization method is called, all objects in contex service are set properly', function() {
        //After object creation properties should have default values
        expect(personalizationsmarteditContextService.seExperienceData).toBe(null);
        expect(personalizationsmarteditContextService.seConfigurationData).toBe(null);
        expect(personalizationsmarteditContextService.sePreviewData).toBe(null);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(null);
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(null);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(null);
        expect(personalizationsmarteditContextService.personalizationEnabled).toBe(false);

        //Set some mock properties
        personalizationsmarteditContextService.selectedVariations = "mockVariation";
        personalizationsmarteditContextService.selectedCustomizations = "mockCustomization";
        personalizationsmarteditContextService.selectedComponents = "mockComponent";
        personalizationsmarteditContextService.personalizationEnabled = true;

        //Call method and run digest cycle
        personalizationsmarteditContextService.applySynchronization();
        scope.$digest();

        //Test if methods have been called properly
        expect(personalizationsmarteditContextService.refreshExperienceData).toHaveBeenCalled();
        expect(personalizationsmarteditContextService.refreshPreviewData).toHaveBeenCalled();
        expect(personalizationsmarteditContextService.refreshConfigurationData).toHaveBeenCalled();
        expect(personalizationsmarteditContextServiceProxy.setSelectedVariations).toHaveBeenCalledWith("mockVariation");
        expect(personalizationsmarteditContextServiceProxy.setSelectedCustomizations).toHaveBeenCalledWith("mockCustomization");
        expect(personalizationsmarteditContextServiceProxy.setSelectedComponents).toHaveBeenCalledWith("mockComponent");
        expect(personalizationsmarteditContextServiceProxy.setPersonalizationContextEnabled).toHaveBeenCalledWith(true);

        //Test if properties are set properly
        expect(personalizationsmarteditContextService.seExperienceData).toBe(mockConfig);
        expect(personalizationsmarteditContextService.seConfigurationData).toBe(mockConfig);
        expect(personalizationsmarteditContextService.sePreviewData).toBe(mockConfig);
    });

    it("GIVEN that function setPersonalizationContextEnabled is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setPersonalizationContextEnabled(mockConfig);
        expect(personalizationsmarteditContextService.personalizationEnabled).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setPersonalizationContextEnabled).toHaveBeenCalledWith(mockConfig);
    });

    it("GIVEN that function setSelectedComponents is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setSelectedComponents(mockConfig);
        expect(personalizationsmarteditContextService.selectedComponents).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setSelectedComponents).toHaveBeenCalledWith(mockConfig);
    });

    it("GIVEN that function setSelectedVariations is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setSelectedVariations(mockConfig);
        expect(personalizationsmarteditContextService.selectedVariations).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setSelectedVariations).toHaveBeenCalledWith(mockConfig);
    });

    it("GIVEN that function setSelectedCustomizations is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setSelectedCustomizations(mockConfig);
        expect(personalizationsmarteditContextService.selectedCustomizations).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setSelectedCustomizations).toHaveBeenCalledWith(mockConfig);
    });

    it("GIVEN that function setSeExperienceData is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setSeExperienceData(mockConfig);
        expect(personalizationsmarteditContextService.seExperienceData).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setSeExperienceData).toHaveBeenCalledWith(mockConfig);
    });

    it("GIVEN that function setSePreviewData is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setSePreviewData(mockConfig);
        expect(personalizationsmarteditContextService.sePreviewData).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setSePreviewData).toHaveBeenCalledWith(mockConfig);
    });

    it("GIVEN that function setSeConfigurationData is called, property should be set and proxy function shuld be called with same parameters", function() {
        personalizationsmarteditContextService.setSeConfigurationData(mockConfig);
        expect(personalizationsmarteditContextService.seConfigurationData).toBe(mockConfig);
        expect(personalizationsmarteditContextServiceProxy.setSeConfigurationData).toHaveBeenCalledWith(mockConfig);
    });

});
