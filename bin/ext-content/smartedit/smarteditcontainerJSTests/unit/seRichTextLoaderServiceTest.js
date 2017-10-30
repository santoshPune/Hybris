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
describe('seRichTextLoaderService', function() {

    var seRichTextLoaderService, $interval;

    beforeEach(customMatchers);
    beforeEach(module('ngMock'));
    beforeEach(module('seRichTextFieldModule'));

    beforeEach(inject(function(_seRichTextLoaderService_, _$interval_) {
        seRichTextLoaderService = _seRichTextLoaderService_;
        $interval = _$interval_;
    }));



    describe('load', function() {
        it('should return a resolved promise when CK Editor reports that it is loaded', function() {
            window.CKEDITOR = {
                status: 'loaded'
            };
            var result = seRichTextLoaderService.load();
            $interval.flush(400);

            expect(result).toBeResolved();
        });

        it('should return an unresolved promise when CK Editor is not loaded yet', function() {
            window.CKEDITOR = {
                status: 'dummyStatus'
            };
            var result = seRichTextLoaderService.load();
            $interval.flush(400);

            expect(result).not.toBeResolved();
        });
    });

});
