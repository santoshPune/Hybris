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
describe('inner perspectiveService', function() {

    var gatewayProxy, systemEventService, Perspective, PerspectiveServiceInterface, perspectiveService;

    beforeEach(customMatchers);

    beforeEach(module('perspectiveServiceModule', function($provide) {

        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent']);
        $provide.value('systemEventService', systemEventService);

    }));

    beforeEach(inject(function(_Perspective_, _PerspectiveServiceInterface_, _perspectiveService_) {
        Perspective = _Perspective_;
        perspectiveService = _perspectiveService_;
        PerspectiveServiceInterface = _PerspectiveServiceInterface_;
    }));

    it('extends PerspectiveServiceInterface', function() {
        expect(perspectiveService instanceof PerspectiveServiceInterface).toBe(true);
    });
    it('initializes and invokes gatewayProxy', function() {
        expect(perspectiveService.gatewayId).toBe("perspectiveService");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(perspectiveService);
    });

    it('register is left unimplemented', function() {
        expect(perspectiveService.register).toBeEmptyFunction();
    });

    it('isEmptyPerspectiveActive is left unimplemented', function() {
        expect(perspectiveService.isEmptyPerspectiveActive).toBeEmptyFunction();
    });

});
