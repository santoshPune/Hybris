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
describe('test outer renderServiceInterface Module', function() {

    var $rootScope, RenderServiceInterface;

    beforeEach(customMatchers);

    beforeEach(module('renderServiceInterfaceModule'));
    beforeEach(inject(function(_$rootScope_, _RenderServiceInterface_) {
        $rootScope = _$rootScope_;
        RenderServiceInterface = _RenderServiceInterface_;
    }));

    it('RenderServiceInterface declares the expected set of empty functions', function() {

        expect(RenderServiceInterface.prototype.renderComponent).toBeEmptyFunction();
        expect(RenderServiceInterface.prototype.renderRemoval).toBeEmptyFunction();
        expect(RenderServiceInterface.prototype.toggleOverlay).toBeEmptyFunction();
        expect(RenderServiceInterface.prototype.refreshOverlayDimensions).toBeEmptyFunction();
    });

});
