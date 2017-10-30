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
describe('Test Personalizationsmartedit Module', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    beforeEach(module('personalizationsmarteditmodule'));
    beforeEach(inject(function() {
        //
    }));

    it('GIVEN that personalizationsmarteditmodule is instantiated menu values should be added to smartedit toolbars', function() {
        expect(mockModules.featureService.addContextualMenuButton).toHaveBeenCalled();
        expect(mockModules.decoratorService.addMappings).toHaveBeenCalled();
        expect(mockModules.featureService.addDecorator).toHaveBeenCalled();
    });

});
