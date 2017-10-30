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
describe('test sharedDataService', function() {

    var sharedDataService, gatewayProxy;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));


    beforeEach(module("sharedDataServiceModule"));

    beforeEach(inject(function(_sharedDataService_) {
        sharedDataService = _sharedDataService_;
    }));

    it('set function is left empty to enable proxying', function() {
        expect(sharedDataService.set).toBeEmptyFunction();
    });


    it('get function is left empty to enable proxying', function() {
        expect(sharedDataService.get).toBeEmptyFunction();
    });


    it('shared data service inits a private gateway', function() {
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(sharedDataService);
    });
});
