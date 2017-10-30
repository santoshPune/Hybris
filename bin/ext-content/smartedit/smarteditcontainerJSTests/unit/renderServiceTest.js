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
describe('test renderService', function() {

    var RenderServiceInterface, renderService, gatewayProxy;

    beforeEach(customMatchers);


    beforeEach(module('renderServiceModule', function($provide) {

        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener', 'createGateway']);
        $provide.value('gatewayFactory', gatewayFactory);

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

    }));

    beforeEach(inject(function(_RenderServiceInterface_, _renderService_) {
        RenderServiceInterface = _RenderServiceInterface_;
        renderService = _renderService_;
    }));

    it('extends RenderServiceInterface', function() {
        expect(renderService instanceof RenderServiceInterface).toBe(true);
    });

    it('initializes and invokes gatewayProxy', function() {
        expect(renderService.gatewayId).toBe("Renderer");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(renderService, ["renderSlots", "renderComponent", "renderRemoval", "toggleOverlay", "refreshOverlayDimensions", "renderPage"]);
    });


    it('leaves the expected set of functions empty', function() {

        expect(renderService.renderSlots).toBeEmptyFunction();
        expect(renderService.renderComponent).toBeEmptyFunction();
        expect(renderService.renderRemoval).toBeEmptyFunction();
        expect(renderService.toggleOverlay).toBeEmptyFunction();
        expect(renderService.refreshOverlayDimensions).toBeEmptyFunction();
    });


});
