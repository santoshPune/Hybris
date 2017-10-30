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
describe("GenericEditor", function() {

    var path;

    beforeEach(function() {
        require("../commonFunctions.js");
        path = require('path');
        browser.driver.manage().timeouts().implicitlyWait(0);
        browser.get('smarteditcontainerJSTests/e2e/genericEditor/componentMedia/genericEditorTest.html');
    });

    describe('Media Upload', function() {
        it('GIVEN a Media structure type is present in the Generic Editor ' +
            'WHEN I select an invalid image ' +
            'THEN I expect to see the file errors displayed',
            function() {
                selectFileToUpload('invalid.doc');
                expect(element(by.css('se-media-selector')).isPresent()).toBe(true);
                expect(element(by.cssContainingText('se-file-selector', 'upload.image.to.library')).isPresent()).toBe(true);
                expect(element(by.css('se-media-upload-form')).isPresent()).toBe(false);
                expect(element(by.css('se-errors-list')).isPresent()).toBe(true);
                expect(element(by.css("se-errors-list")).getText()).toContain("upload.file.type.invalid");
            });

        it('GIVEN a Media structure type is present in the generic editor ' +
            'WHEN I select a valid image' +
            'THEN I expect to see the media upload form populated',
            function() {
                selectFileToUpload('more_bckg.png');

                expect(element(by.css('.se-media-upload--fn--name')).getText()).toBe('more_bckg.png');
                expect(element(by.css('input[name="code"')).getAttribute('value')).toBe('more_bckg.png');
                expect(element(by.css('input[name="description"')).getAttribute('value')).toBe('more_bckg.png');
                expect(element(by.css('input[name="altText"')).getAttribute('value')).toBe('more_bckg.png');
            });

        it('GIVEN a Media structure type is present in the generic editor ' +
            'WHEN I select a valid image AND upload with a missing code' +
            'THEN I expect to see a field error for code',
            function() {
                selectFileToUpload('more_bckg.png');
                clearCodeField();
                clickUpload();

                expect(element(by.css('se-media-selector')).isPresent()).toBe(false);
                expect(element(by.cssContainingText('se-file-selector', 'upload.image.to.library')).isPresent()).toBe(false);
                expect(element(by.css('se-media-upload-form')).isPresent()).toBe(true);
                expect(element(by.css('se-errors-list')).isPresent()).toBe(false);

                expect(element(by.css('.upload-field-error-code')).getText()).toContain('uploaded.image.code.is.required');
            });

        it('GIVEN a Media structure type is present in the generic editor ' +
            'WHEN I select a valid image AND upload successfully ' +
            'THEN I expect to see the image selector dropdown with the newly uploaded image',
            function() {
                selectFileToUpload('more_bckg.png');

                expect(element(by.css('se-media-selector')).isPresent()).toBe(false);
                expect(element(by.cssContainingText('se-file-selector', 'upload.image.to.library')).isPresent()).toBe(false);
                expect(element(by.css('se-media-upload-form')).isPresent()).toBe(true);
                expect(element(by.css('se-errors-list')).isPresent()).toBe(false);

                clickUpload();

                expect(element(by.css('se-media-selector')).isPresent()).toBe(true);
                expect(element(by.cssContainingText('se-file-selector', 'upload.image.to.library')).isPresent()).toBe(true);
                expect(element(by.css('se-media-upload-form')).isPresent()).toBe(false);
                expect(element(by.css('se-errors-list')).isPresent()).toBe(false);

                expect(element(by.css('.thumbnail')).getAttribute('src')).toContain('web/webroot/static-resources/images/more_bckg.png');
                expect(element(by.css('.media-preview-code')).getText()).toBe('more_bckg.png');
            });
    });

    describe('Media Selector Preview', function() {
        it('GIVEN a media is selected WHEN I click the preview button THEN I expect to see a popover with the image in it.', function() {
            element(by.css(".media-preview-icon")).click();
            expect(element(by.css(".preview-image"))).toBeDisplayed();
            expect(element(by.css(".preview-image")).getAttribute("src")).toContain("contextualmenu_delete_off.png");
        });
    });

    describe('Media Selector Advanced Information', function() {
        it('GIVEN a media is selected ' +
            'WHEN I click the advanced information ' +
            'THEN I expect to see a popover with the alt text, code and description in it.',
            function() {
                element(by.css('.media-advanced-information')).click();
                expect(element(by.css(".advanced-information-description"))).toBeDisplayed();
                expect(element(by.css(".advanced-information-code"))).toBeDisplayed();
                expect(element(by.css(".advanced-information-alt-text"))).toBeDisplayed();
                expect(element(by.css('.advanced-information-description')).getText()).toContain("contextualmenu_delete_off");
                expect(element(by.css('.advanced-information-code')).getText()).toContain("contextualmenu_delete_off");
                expect(element(by.css('.advanced-information-alt-text')).getText()).toContain("contextualmenu_delete_off");
            });
    });

    function clearCodeField() {
        var keySeries = '';
        for (var i = 0; i < 20; i++) {
            keySeries += protractor.Key.BACK_SPACE;
        }
        element(by.css('input[name="code"]')).sendKeys(keySeries);
    }

    function clickUpload() {
        $('.se-media-upload-btn__submit').click();
    }

    function selectFileToUpload(fileName) {
        var absolutePath = path.resolve(__dirname, fileName);
        $('input[type="file"]').sendKeys(absolutePath);
    }

});
