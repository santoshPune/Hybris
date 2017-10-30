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
describe('seMediaFormat', function() {
    var parentScope, scope, element, ctrl;
    var seMediaFormatConstants, seFileValidationServiceConstants;
    var seMediaService;

    beforeEach(customMatchers);
    beforeEach(module('coretemplates'));

    beforeEach(module('pascalprecht.translate', function($translateProvider) {
        $translateProvider.translations('en', {
            'media.format.upload': 'Upload',
            'media.format.replaceimage': 'Replace Image',
            'media.format.under.edit': 'Editing...'
        });
        $translateProvider.preferredLanguage('en');
    }));

    beforeEach(module('seFileValidationServiceModule'));

    beforeEach(module('seMediaServiceModule', function($provide) {
        seMediaService = jasmine.createSpyObj('seMediaService', ['getMediaByCode']);
        $provide.value('seMediaService', seMediaService);
    }));

    beforeEach(module('seMediaFormatModule'));

    beforeEach(inject(function($compile, $rootScope, $q, _seMediaFormatConstants_, _seFileValidationServiceConstants_) {
        seMediaFormatConstants = _seMediaFormatConstants_;
        seFileValidationServiceConstants = _seFileValidationServiceConstants_;

        seMediaService.getMediaByCode.andReturn($q.when({
            code: 'someCode',
            url: 'static-resources/images/edit_icon.png'
        }));

        parentScope = $rootScope.$new();
        $.extend(parentScope, {
            mediaCode: 'someCode',
            mediaFormat: 'someFormat',
            isUnderEdit: false,
            onFileSelect: jasmine.createSpy('onFileSelect')
        });

        element = $compile('<se-media-format ' +
            'data-media-code="mediaCode" ' +
            'data-media-format="mediaFormat" ' +
            'data-is-under-edit="isUnderEdit" ' +
            'data-on-file-select="onFileSelect" ' +
            '</se-media-format>')(parentScope);
        parentScope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));

    describe('controller', function() {
        it('should be initialized', function() {
            expect(ctrl.i18nKeys).toBe(seMediaFormatConstants.I18N_KEYS);
            expect(ctrl.acceptedFileTypes).toBe(seFileValidationServiceConstants.ACCEPTED_FILE_TYPES);
        });

        it('should get media if it a code is provided', function() {
            expect(ctrl.mediaCode).toBe('someCode');
            expect(ctrl.media).toEqual({
                code: 'someCode',
                url: 'static-resources/images/edit_icon.png'
            });
        });

        it('should clear the media if a code is not provided', function() {
            ctrl.mediaCode = null;
            scope.$digest();
            expect(ctrl.media).toEqual({});
        });
    });

    describe('template', function() {
        it('should display the format', function() {
            expect(element.text()).toContain('someFormat');
        });

        describe('when media code present', function() {
            it('should show the media present view', function() {
                expect(element.find('.media-present')).toExist();
                expect(element.find('.media-absent')).not.toExist();
                expect(element.find('.media-is-under-edit')).not.toExist();
            });

            it('should show the image', function() {
                expect(element.find('.thumbnail--image-preview').attr('data-ng-src')).toBe('static-resources/images/edit_icon.png');
            });

            it('should show a replace button', function() {
                expect(element.find('.media-selector--preview__left--p').text().trim()).toContain('media.format.remove');
            });
        });

        describe('when media code absent', function() {
            beforeEach(function() {
                ctrl.mediaCode = null;
                scope.$digest();
            });

            it('should show the media absent view', function() {
                expect(element.find('.media-present')).not.toExist();
                expect(element.find('.media-absent')).toExist();
                expect(element.find('.media-is-under-edit')).not.toExist();
            });

            it('should show an upload button', function() {
                expect(element.find('.media-absent se-file-selector .label__fileUpload-link').text().trim()).toBe('Upload');
            });
        });

        describe('when under edit', function() {
            beforeEach(function() {
                ctrl.isUnderEdit = true;
                scope.$digest();
            });

            it('should show the media uploading view', function() {
                expect(element.find('.media-present')).not.toExist();
                expect(element.find('.media-absent')).not.toExist();
                expect(element.find('.media-is-under-edit')).toExist();
            });

            it('should show the editing text', function() {
                expect(element.find('.media-is-under-edit').text().trim()).toContain('Upload');
            });
        });
    });
});
