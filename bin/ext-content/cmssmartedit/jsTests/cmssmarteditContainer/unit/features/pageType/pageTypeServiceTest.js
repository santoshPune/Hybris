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
describe('pageTypeService - ', function() {
    'use strict';

    var pageTypeService, languageService, $rootScope;
    var $q;

    beforeEach(function() {
        angular.module('languageServiceModule', []);

        module('languageServiceModule', function($provide) {
            languageService = jasmine.createSpyObj('languageService', ['getToolingLanguages']);
            $provide.value('languageService', languageService);
        });

        module('pageTypeServiceModule');
        inject(function(_$q_, _pageTypeService_, _$rootScope_) {
            $q = _$q_;
            pageTypeService = _pageTypeService_;
            $rootScope = _$rootScope_;
        });
    });

    it('GIVEN that the page types are requested, a promise is returned containing an array of Page Type IDs', function() {
        // Arrange
        var LOCALIZED_VALUE = "some-localized-value";
        var expectedResult = [{
            code: 'ContentPage',
            type: 'contentPageData',
            name: LOCALIZED_VALUE,
            description: LOCALIZED_VALUE
        }];
        var languages = 'some languages';
        languageService.getToolingLanguages.andReturn($q.when(languages));
        spyOn(pageTypeService, "localizeField").andReturn(LOCALIZED_VALUE);

        // Act
        var result = pageTypeService.getPageTypeIDs();

        // Assert
        result.then(function(pageTypes) {
            expect(pageTypeService.localizeField.calls.length).toBe(2);
            expect(pageTypeService.localizeField).toHaveBeenCalledWith('cms.pagetype.contentpage.name', languages);
            expect(pageTypeService.localizeField).toHaveBeenCalledWith('cms.pagetype.contentpage.description', languages);

            expect(pageTypes).toEqual(expectedResult);
        });

        $rootScope.$apply();
    });


});
