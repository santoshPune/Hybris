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
describe('Sample Container (sample service) Tests', function() {
    var sampleService, featureService;

    beforeEach(function() {
        angular.module('featureServiceModule', []);
    });

    beforeEach(module('trainingContainer', function($provide) {
        featureService = jasmine.createSpyObj('featureService', ['addToolbarItem']);
        featureService.addToolbarItem.andReturn();

        $provide.value('featureService', featureService);
    }));

    beforeEach(inject(function(_sampleService_) {
        sampleService = _sampleService_;
    }));

    it('sampleNum returns the sample of the provided input number', function() {
        var inputNum = 5;
        var expectedOutput = 25;
        var result = sampleService.squareNum(inputNum);
        expect(result).toBe(expectedOutput);
    });

});
