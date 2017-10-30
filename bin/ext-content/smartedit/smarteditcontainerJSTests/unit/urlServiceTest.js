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
describe('test urlService', function() {

    var urlService, gatewayProxy;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {
        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module("urlServiceModule"));

    beforeEach(inject(function(_urlService_) {
        urlService = _urlService_;
    }));

    it('GIVEN urlService is configured openUrl function is not left empty as we have a concrete implementation', function() {
        expect(urlService.openUrlInPopup).not.toBeEmptyFunction();
    });

    it('GIVEN url service is configured it should init a private gateway', function() {
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(urlService);
    });
});
