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
describe('seMediaContainerField', function() {
    var scope, ctrl;
    var seFileValidationService;
    var $q;

    beforeEach(customMatchers);
    beforeEach(module('coretemplates'));
    beforeEach(module('pascalprecht.translate'));

    beforeEach(module('seFileValidationServiceModule', function($provide) {
        seFileValidationService = jasmine.createSpyObj('seFileValidationService', ['validate']);
        $provide.value('seFileValidationService', seFileValidationService);
    }));

    beforeEach(module('seMediaContainerFieldModule'));

    beforeEach(inject(function($controller, $rootScope, _$q_) {
        $q = _$q_;
        scope = $rootScope.$new();
        ctrl = $controller('seMediaContainerFieldController', {
            $scope: scope
        }, {
            field: {},
            model: {
                someQualifier: {
                    someFormat: 'someCode'
                }
            },
            editor: {},
            qualifier: 'someQualifier'
        });
    }));

    describe('initialization', function() {
        it('should be initialized', function() {
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
            expect(ctrl.model[ctrl.qualifier][ctrl.image.format]).toEqual('someNewCode');
            expect(ctrl.image).toEqual({});
            expect(ctrl.fileErrors).toEqual([]);
        });
    });

    describe('isFormatUnderEdit', function() {
        it('should return true if the format is under edit', function() {
            ctrl.image = {
                format: 'someFormat'
            };
            expect(ctrl.isFormatUnderEdit('someFormat')).toBe(true);
        });
    });
});
