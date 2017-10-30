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
describe('test injectJS Service of Functions Module', function() {

    var injectorMockHolder, injectJS;
    var appLocations = ['SEContainerLocationForAppA', 'SEContainerLocationForAppB'];

    beforeEach(customMatchers);
    beforeEach(module('functionsModule'));
    beforeEach(inject(function(_injectJS_) {
        injectJS = _injectJS_;
    }));

    it('injectJS will injects all sources in sequence and then call an optional callback', function() {

        injectorMockHolder = jasmine.createSpyObj('injectorMockHolder', ['scriptJSMock']);
        injectorMockHolder.scriptJSMock.andCallFake(function(url, scriptCallback) {
            scriptCallback();
        });

        var callback = jasmine.createSpy('callback');
        spyOn(injectJS, 'getInjector').andReturn(injectorMockHolder.scriptJSMock);
        injectJS.execute({
            srcs: appLocations,
            callback: callback
        });

        expect(injectorMockHolder.scriptJSMock.callCount).toBe(2);

        expect(injectorMockHolder.scriptJSMock.calls[0].args[0]).toEqualData('SEContainerLocationForAppA', jasmine.any(Function));
        expect(injectorMockHolder.scriptJSMock.calls[1].args[0]).toEqualData('SEContainerLocationForAppB', jasmine.any(Function));
        expect(callback).toHaveBeenCalled();
    });

});
