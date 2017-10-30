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
describe('seFileObjectValidators', function() {
    var seFileObjectValidators;
    var mockseFileValidationServiceConstants = {
        ACCEPTED_FILE_TYPES: ['png', 'jpg'],
        MAX_FILE_SIZE_IN_BYTES: 8,
        I18N_KEYS: {
            FILE_TYPE_INVALID: 'upload.file.type.invalid',
            FILE_SIZE_INVALID: 'upload.file.size.invalid'
        }
    };

    beforeEach(module('seFileValidationServiceModule', function($provide) {
        $provide.constant('seFileValidationServiceConstants', mockseFileValidationServiceConstants);
    }));

    beforeEach(inject(function(_seFileObjectValidators_) {
        seFileObjectValidators = _seFileObjectValidators_;
    }));

    describe('file size validator', function() {
        var validator;
        beforeEach(function() {
            validator = seFileObjectValidators.find(function(validator) {
                return validator.subject === 'size';
            });
        });

        it('should be defined', function() {
            expect(validator).toBeTruthy();
            expect(validator.subject).toBe('size');
            expect(validator.message).toBe(mockseFileValidationServiceConstants.I18N_KEYS.FILE_SIZE_INVALID);
        });

        it('should successfully validate the file size', function() {
            expect(validator.validate(8)).toBe(true);
        });

        it('should fail to validate the file size', function() {
            expect(validator.validate(9)).toBe(false);
        });
    });
});
