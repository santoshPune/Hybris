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
describe('seRichTextField', function() {

    var seRichTextLoaderService, seRichTextConfiguration, $interval, $controller, $scope, editable, genericEditorSanitizationService, $sanitize, seRichTextFieldLocalizationService;

    beforeEach(customMatchers);
    beforeEach(module('seRichTextFieldModule', function($provide) {
        seRichTextLoaderService = jasmine.createSpyObj('seRichTextLoaderService', ['load']);
        seRichTextConfiguration = {};
        seRichTextFieldLocalizationService = jasmine.createSpyObj('seRichTextFieldLocalizationService', ['localizeCKEditor']);

        $provide.value('seRichTextLoaderService', seRichTextLoaderService);
        $provide.constant('seRichTextConfiguration', seRichTextConfiguration);
        $provide.value('seRichTextFieldLocalizationService', seRichTextFieldLocalizationService);
    }));
    beforeEach(module('coretemplates'));

    beforeEach(module('pascalprecht.translate', function($translateProvider) {
        $translateProvider.translations('en', {});
        $translateProvider.preferredLanguage('en');
    }));

    beforeEach(function() {
        window.CKEDITOR = jasmine.createSpyObj('CKEDITOR', ['replace', 'on']);

        var editorInstance = jasmine.createSpyObj('editorInstance', ['destroy', 'on', 'getData', 'fire', 'editable']);

        editable = jasmine.createSpyObj('editable', ['attachListener']);
        editorInstance.editable.andReturn(editable);
        CKEDITOR.replace.andReturn(editorInstance);
        editorInstance.getData.andReturn("changed value");
    });

    beforeEach(inject(function($rootScope, $compile, _$q_, _genericEditorSanitizationService_) {
        $q = _$q_;
        genericEditorSanitizationService = _genericEditorSanitizationService_;
        seRichTextLoaderService.load.andReturn($q.when());
        scope = $rootScope.$new();
        $.extend(scope, {
            field: {
                qualifier: 'someQualifier'
            },
            qualifier: 'en',
            model: {},
            reassignUserCheck: reassignUserCheck = jasmine.createSpy('reassignUserCheck')
        });
        element = $compile('<se-rich-text-field ' +
            'data-field="field" ' +
            'data-qualifier="qualifier" ' +
            'data-model="model" ' +
            'data-reassign-user-check="reassignUserCheck()">' +
            '</se-rich-text-field>')(scope);
        scope.$digest();
        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));


    describe('controller', function() {

        describe('onChange', function() {

            it('should call the reassignUserCheck and expect the changed value', function() {
                element.val("changed value");
                ctrl.onChange();
                expect(reassignUserCheck.toHaveBeenCalled);
                expect(ctrl.model[ctrl.qualifier]).toEqual('changed value');
            });
        });

        describe('onMode', function() {

            it('should be attach editable listener if the mode is source', function() {
                ctrl.mode = 'source';
                ctrl.onMode();
                expect(editable.attachListener).toHaveBeenCalled();
            });
        });

        describe('onInstanceReady', function() {

            it('should be called set rules method with attributes', function() {
                var setRules = jasmine.createSpy('setRules');

                var MOCK_EV = {
                    editor: {
                        dataProcessor: {
                            writer: {
                                setRules: setRules
                            }
                        }
                    }
                };

                ctrl.onInstanceReady(MOCK_EV);
                expect(setRules).toHaveBeenCalledWith('br', {
                    indent: false,
                    breakBeforeOpen: false,
                    breakAfterOpen: false,
                    breakBeforeClose: false,
                    breakAfterClose: false
                });
            });

        });
    });

    describe('link', function() {

        it('should call the replace API of the  CK Editor with the textarea element and the editor configuration and get localized', function() {
            expect(window.CKEDITOR.replace).toHaveBeenCalledWith(element.find('textarea')[0], seRichTextConfiguration);
            expect(seRichTextFieldLocalizationService.localizeCKEditor).toHaveBeenCalled();
        });

    });

    describe('on data change', function() {

        it("should call genericEditorSanitizationService", function() {
            ctrl.qualifier = 'en';
            ctrl.model = {
                en: '<div><script>alert(/"I am a snippet/");</script></div>'
            };
            ctrl.field = {};
            ctrl.reassignUserCheck();
            expect(genericEditorSanitizationService.isSanitized.toHaveBeenCalled);


        });

        it("reassignUserCheck WILL set requiresUserCheck as true on field with javascript snippet WHEN sanitized content does not match unsanitized content", function() {
            ctrl.qualifier = 'en';
            ctrl.model = {
                en: '<div><script>alert(/"I am a snippet/");</script></div>'
            };
            ctrl.field = {};
            ctrl.reassignUserCheck();
            expect(ctrl.field.requiresUserCheck[ctrl.qualifier]).toBe(true);
        });

        it("reassignUserCheck WILL set requiresUserCheck as true on field WHEN sanitized content does not match unsanitized content", function() {
            ctrl.qualifier = 'en';

            ctrl.model = {
                en: '\"http://\"'
            };
            ctrl.field = {};
            ctrl.reassignUserCheck();
            expect(ctrl.field.requiresUserCheck[ctrl.qualifier]).toBe(true);
        });

        it("reassignUserCheck WILL not set requiresUserCheck on field WHEN  sanitized content matches unsanitized content.", function() {

            ctrl.qualifier = 'en';
            ctrl.model = {
                en: '<p>Valid Html</p>'
            };
            ctrl.field = {};
            ctrl.reassignUserCheck();
            expect(ctrl.field.requiresUserCheck[ctrl.qualifier]).toBe(false);
        });

        it("reassignUserCheck WILL not set requiresUserCheck on field WHEN there is no content", function() {
            ctrl.model = {};
            ctrl.field = {};

            ctrl.reassignUserCheck();
            expect(ctrl.field.requiresUserCheck[ctrl.qualifier]).toBe(false);
        });

        it("reassignUserCheck WILL not set requiresUserCheck on field WHEN the model is not defined", function() {
            ctrl.field = {};

            ctrl.reassignUserCheck();
            expect(ctrl.field.requiresUserCheck[ctrl.qualifier]).toBe(false);
        });
    });

});
