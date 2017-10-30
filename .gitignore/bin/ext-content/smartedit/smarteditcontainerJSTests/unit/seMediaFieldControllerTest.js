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
describe('seMediaField', function() {
    var scope, ctrl;
    var seFileValidationService;
    var seMediaFieldConstants, seFileValidationServiceConstants, $q;

    beforeEach(customMatchers);
    beforeEach(module('coretemplates'));
    beforeEach(module('pascalprecht.translate'));

    beforeEach(module('seFileValidationServiceModule', function($provide) {
        seFileValidationService = jasmine.createSpyObj('seFileValidationService', ['validate']);
        $provide.value('seFileValidationService', seFileValidationService);
    }));

    beforeEach(module('seMediaFieldModule'));

    beforeEach(inject(function(_seMediaFieldConstants_, _seFileValidationServiceConstants_, _$q_, $controller, $rootScope) {
        seMediaFieldConstants = _seMediaFieldConstants_;
        seFileValidationServiceConstants = _seFileValidationServiceConstants_;
        $q = _$q_;

        scope = $rootScope.$new();
        ctrl = $controller('seMediaFieldController', {
            $scope: scope
        }, {
            field: {},
            model: {
                someQualifier: {
                    someFormat: 'someCode'
                }
            },
            editor: {
                refreshOptions: jasmine.createSpy('refreshOptions')
            },
            qualifier: 'someQualifier'
        });
    }));

    describe('initialization', function() {
        it('should be initialized', function() {
            expect(ctrl.i18nKeys).toBe(seMediaFieldConstants.I18N_KEYS);
            expect(ctrl.acceptedFileTypes).toBe(seFileValidationServiceConstants.ACCEPTED_FILE_TYPES);
            expect(ctrl.image).toEqual({});
            expect(ctrl.fileErrors).toEqual([]);
        });
    });

    describe('fileSelected', function() {
        it('should set the image on the scope if only one file is selected and the file is valid', function() {
            var MOCK_FILES = [{
                name: 'someName'
            }];
            var MOCK_FORMAT = 'someFormat';
            seFileValidationService.validate.andReturn($q.when());
            ctrl.fileSelected(MOCK_FILES, MOCK_FORMAT);
            scope.$digest();

            expect(ctrl.fileErrors).toEqual([]);
            expect(ctrl.image).toEqual({
                file: {
                    name: 'someName'
                },
                format: 'someFormat'
            });
        });

        it('should clear image if there are validation errors', function() {
            var MOCK_FILES = [{
                name: 'someName'
            }];
            var MOCK_FORMAT = 'someFormat';
            seFileValidationService.validate.andCallFake(function(file, errorsContext) {
                errorsContext.push({
                    subject: 'code'
                });
                return $q.reject(errorsContext);
            });
            ctrl.fileSelected(MOCK_FILES, MOCK_FORMAT);
            scope.$digest();

            expect(ctrl.fileErrors).toEqual([{
                subject: 'code'
            }]);
            expect(ctrl.image).toEqual({});
        });
    });

    describe('resetImage', function() {
        it('should reset the image and file errors', function() {
            ctrl.resetImage();
            expect(ctrl.image).toEqual({});
            expect(ctrl.fileErrors).toEqual([]);
        });
    });

    describe('imageUploaded', function() {
        it('should update the model and reset the image and file errors', function() {
            ctrl.imageUploaded('someNewCode');
            expect(ctrl.model[ctrl.qualifier]).toEqual('someNewCode');
            expect(ctrl.image).toEqual({});
            expect(ctrl.fileErrors).toEqual([]);
            expect(ctrl.editor.refreshOptions).toHaveBeenCalledWith(ctrl.field, ctrl.qualifier, '');
        });

        it('should trigger a callback to refresh the dropdown', function() {
            ctrl.imageUploaded('someNewCode');
            expect(ctrl.editor.refreshOptions).toHaveBeenCalledWith(ctrl.field, ctrl.qualifier, '');
        });
    });
});
