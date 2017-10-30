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
describe('inner storage service', function() {

    var $q, $rootScope, $cookies, storageService;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('storageServiceModule', function($provide) {

        $cookies = jasmine.createSpyObj('$cookies', ['getObject', 'putObject', 'remove']);
        $provide.value('$cookies', $cookies);
    }));

    beforeEach(inject(function(_$q_, _$rootScope_, _storageService_) {

        $q = _$q_;
        $rootScope = _$rootScope_;
        storageService = _storageService_;

    }));

    it('initialized by gatewayProxy', function() {

        expect(storageService.gatewayId).toBe("storage");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(storageService);
    });

    it('all functions are left empty', function() {

        expect(storageService.isInitialized).toBeEmptyFunction();
        expect(storageService.storeAuthToken).toBeEmptyFunction();
        expect(storageService.getAuthToken).toBeEmptyFunction();
        expect(storageService.removeAuthToken).toBeEmptyFunction();
        expect(storageService.removeAllAuthTokens).toBeEmptyFunction();
    });



});
