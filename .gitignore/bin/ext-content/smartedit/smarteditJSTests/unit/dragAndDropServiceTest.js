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
describe('test inner toolbarService Module', function() {

    var $rootScope, $q, $log, gatewayProxy, toolbarServiceFactory;

    beforeEach(customMatchers);

    beforeEach(module('gatewayFactoryModule', function($provide) {
        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);
    }));

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('dragAndDropServiceModule'));
    beforeEach(inject(function(_$rootScope_, _dragAndDropService_) {
        $rootScope = _$rootScope_;
        dragAndDropService = _dragAndDropService_;
    }));

    it('register function is left empty to enable proxying', function() {

        expect(dragAndDropService.register).toBeEmptyFunction();
    });

    it('dragAndDropService inits a private gateway', function() {

        expect(gatewayProxy.initForService).toHaveBeenCalledWith(dragAndDropService, ["register"]);
    });
});
