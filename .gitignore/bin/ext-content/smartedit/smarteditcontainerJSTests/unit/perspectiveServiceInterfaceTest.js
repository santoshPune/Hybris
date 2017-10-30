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
describe('perspectiveServiceInterface', function() {

    var PerspectiveServiceInterface;

    beforeEach(customMatchers);

    beforeEach(module('perspectiveServiceModule'));

    beforeEach(inject(function(_PerspectiveServiceInterface_) {
        PerspectiveServiceInterface = _PerspectiveServiceInterface_;
    }));


    it('registers unimplemented register method', function() {
        expect(PerspectiveServiceInterface.prototype.register).toBeEmptyFunction();
        expect(PerspectiveServiceInterface.prototype.switchTo).toBeEmptyFunction();
        expect(PerspectiveServiceInterface.prototype.hasActivePerspective).toBeEmptyFunction();
        expect(PerspectiveServiceInterface.prototype.selectDefault).toBeEmptyFunction();
        expect(PerspectiveServiceInterface.prototype.isEmptyPerspectiveActive).toBeEmptyFunction();
    });

});
