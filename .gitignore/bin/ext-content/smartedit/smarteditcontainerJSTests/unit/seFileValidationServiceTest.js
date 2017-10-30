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
describe('seFileValidationService', function() {
    var seFileValidationService, seFileObjectValidators, $q;
    var seObjectValidatorFactory, seFrontendValidator, seFileMimeTypeService;

    beforeEach(customMatchers);

    beforeEach(module('seFileMimeTypeServiceModule', function($provide) {
        seFileMimeTypeService = jasmine.createSpyObj('seFileMimeTypeService', ['isFileMimeTypeValid']);
        $provide.value('seFileMimeTypeService', seFileMimeTypeService);
    }));

    beforeEach(module('seObjectValidatorFactoryModule', function($provide) {
        seFrontendValidator = jasmine.createSpyObj('seFrontendValidator', ['validate']);
        seObjectValidatorFactory = jasmine.createSpyObj('seObjectValidatorFactory', ['build']);
        seObjectValidatorFactory.build.andReturn(seFrontendValidator);

        $provide.value('seObjectValidatorFactory', seObjectValidatorFactory);
    }));

    beforeEach(module('seFileValidationServiceModule', function($provide) {
        $provide.constant('seFileValidationServiceConstants', {
            ACCEPTED_FILE_TYPES: ['png', 'jpg'],
            MAX_FILE_SIZE_IN_BYTES: 8,
            I18N_KEYS: {
                FILE_TYPE_INVALID: 'upload.file.type.invalid',
                FILE_MIME_TYPE_HEADER_INVALID: 'upload.file.mime.type.header.invalid',
                FILE_SIZE_INVALID: 'upload.file.size.invalid'
            }
        });
    }));

    beforeEach(inject(function(_seFileValidationService_, _seFileObjectValidators_, _$q_) {
        $q = _$q_;
        seFileValidationService = _seFileValidationService_;
        seFileObjectValidators = _seFileObjectValidators_;
    }));

    describe('validate', function() {
        it('should return a resolved promise if the given file is valid', function() {
            var file = {};
            var context = [];
            seFileMimeTypeService.isFileMimeTypeValid.andReturn($q.when());
            seFrontendValidator.validate.andReturn(true);
            var promise = seFileValidationService.validate(file, context);

            expect(promise).toBeResolved();
        });

        it('should return a rejected promise with the errors context if the file header is valid but there are object validation errors', function() {
            var file = {};
            var context = [];
            seFileMimeTypeService.isFileMimeTypeValid.andReturn($q.when());
            seFrontendValidator.validate.andCallFake(function(file, errorsContext) {
                errorsContext.push({
                    subject: 'size',
                    message: 'upload.file.size.invalid'
                });
                return false;
            });
            var promise = seFileValidationService.validate(file, context);

            expect(promise).toBeRejectedWithDataContaining({
                message: 'upload.file.size.invalid',
                subject: 'size'
            });
        });

        it('should return a rejected promise with the errors context if the file is invalid', function() {
            var file = {};
            var context = [];
            seFileMimeTypeService.isFileMimeTypeValid.andReturn($q.reject());
            seFrontendValidator.validate.andCallFake(function(file, errorsContext) {
                errorsContext.push({
                    subject: 'size',
                    message: 'upload.file.size.invalid'
                });
                return false;
            });
            var promise = seFileValidationService.validate(file, context);

            expect(promise).toBeRejectedWithDataContaining({
                message: 'upload.file.type.invalid',
                subject: 'type'
            });
            expect(promise).toBeRejectedWithDataContaining({
                message: 'upload.file.size.invalid',
                subject: 'size'
            });
        });
    });


    describe('buildAcceptedFileTypesList', function() {
        it('should return a comma separated list of file extension', function() {
            expect(seFileValidationService.buildAcceptedFileTypesList()).toBe('.png,.jpg');
        });
    });
});
