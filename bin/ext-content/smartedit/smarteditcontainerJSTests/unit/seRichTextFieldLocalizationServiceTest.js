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
describe('seRichTextFieldLocalizationService', function() {
    var seRichTextFieldLocalizationService, languageService, resolvedLocaleToCKEDITORLocaleMap, $q, $rootScope;


    beforeEach(customMatchers);
    beforeEach(module('seRichTextFieldModule', function($provide) {
        languageService = jasmine.createSpyObj('languageService', ['getResolveLocale']);
        resolvedLocaleToCKEDITORLocaleMap = {
            'en': 'xx'
        };
        $provide.value('languageService', languageService);
        $provide.constant('resolvedLocaleToCKEDITORLocaleMap', resolvedLocaleToCKEDITORLocaleMap);
    }));

    beforeEach(function() {
        window.CKEDITOR = {
            config: {}
        };
    });

    beforeEach(inject(function(_seRichTextFieldLocalizationService_, _$q_, _$rootScope_) {
        seRichTextFieldLocalizationService = _seRichTextFieldLocalizationService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    describe('localizeCKEditor', function() {
        it('should set global variable CKEDITOR\'s language to the current locale\'s equivalent in CKEDITOR when the conversion exists', function() {
            var existingLocale = 'en';
            expect(resolvedLocaleToCKEDITORLocaleMap[existingLocale]).not.toBeUndefined();
            languageService.getResolveLocale.andReturn($q.when(existingLocale));
            seRichTextFieldLocalizationService.localizeCKEditor();
            $rootScope.$digest();
            expect(languageService.getResolveLocale).toHaveBeenCalled();
            expect(window.CKEDITOR.config.language).toEqual('xx');
        });

        it('should set global variable CKEDITOR\'s language to the current locale when the conversion does not exist', function() {
            var nonexistingLocale = 'zz';
            expect(resolvedLocaleToCKEDITORLocaleMap[nonexistingLocale]).toBeUndefined();
            languageService.getResolveLocale.andReturn($q.when(nonexistingLocale));
            seRichTextFieldLocalizationService.localizeCKEditor();
            $rootScope.$digest();
            expect(languageService.getResolveLocale).toHaveBeenCalled();
            expect(window.CKEDITOR.config.language).toEqual(nonexistingLocale);
        });
    });
});
