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
describe('Test Personalizationsmartedit Manager Module', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var personalizationsmarteditManager, scope;

    beforeEach(module('personalizationsmarteditManagerModule'));
    beforeEach(inject(function(_$rootScope_, _$q_, _$controller_, _personalizationsmarteditManager_) {
        scope = _$rootScope_.$new();
        personalizationsmarteditManager = _personalizationsmarteditManager_;
    }));

    it('GIVEN that personalizationsmarteditManager is instantiated it contains proper functions', function() {
        expect(personalizationsmarteditManager.openCreateCustomizationModal).toBeDefined();
        expect(personalizationsmarteditManager.openEditCustomizationModal).toBeDefined();
    });


});
