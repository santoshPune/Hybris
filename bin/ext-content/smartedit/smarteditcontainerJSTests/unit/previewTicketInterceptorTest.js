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
describe('unit test previewTicket interceptor', function() {

    var $q, $httpProvider, interceptor, parseQueryMock;

    beforeEach(customMatchers);

    beforeEach(module('previewTicketInterceptorModule', function($provide, _$httpProvider_) {
        $httpProvider = _$httpProvider_;

        parseQueryMock = jasmine.createSpy('parseQuery');
        $provide.value('parseQuery', parseQueryMock);
    }));

    beforeEach(inject(function(_$q_, _previewTicketInterceptor_) {
        $q = _$q_;
        interceptor = _previewTicketInterceptor_;
    }));

    it('will be loaded with the interceptor', function() {

        expect($httpProvider.interceptors).toContain('previewTicketInterceptor');

    });

    it('will append the preview ticket header if cmsTicketId exists', function() {

        spyOn(interceptor, "_getLocation").andReturn("http://success");
        parseQueryMock.andReturn({
            cmsTicketId: "preview-ticket"
        });

        var config = {
            url: 'something'
        };

        expect(interceptor.request(config)).toBeResolvedWithData({
            url: 'something',
            headers: {
                'X-Preview-Ticket': 'preview-ticket'
            }
        });
        expect(parseQueryMock).toHaveBeenCalledWith("http://success");
    });

    it('will NOT append the preview ticket header if cmsTicketId does not exist', function() {

        spyOn(interceptor, "_getLocation").andReturn("http://failure");
        parseQueryMock.andReturn({});

        var config = {
            url: 'something'
        };

        expect(interceptor.request(config)).toBeResolvedWithData({
            url: 'something'
        });
        expect(parseQueryMock).toHaveBeenCalledWith("http://failure");
    });

});
