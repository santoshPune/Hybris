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
describe('Editor TabSet for Supported Component Type', function() {

    var editorTabset = require('./EditorTabsetPageObject.js');

    beforeEach(function() {
        browser.get('jsTests/cmssmarteditContainer/e2e/features/editorTabset/editorTabset.html');
        browser.click(by.id('supportedType-button'));
    });

    it('will load tabset with all default tabs', function() {

        expect(editorTabset.editorTabset().isPresent()).toBe(true, 'Expected editorTabset to be present');

        expect(editorTabset.basicTab().isPresent()).toBe(true, 'Expected basicTab to be present');
        expect(editorTabset.basicTabContent().isPresent()).toBe(true, 'Expected basicTabContent to be present');
        expect(editorTabset.adminTab().isPresent()).toBe(true, 'Expected adminTab to be present');
        expect(editorTabset.adminTabContent().isPresent()).toBe(true, 'Expected adminTabContent to be present');
        expect(editorTabset.visibilityTab().isPresent()).toBe(true, 'Expected visibilityTab to be present');
        expect(editorTabset.visibilityTabContent().isPresent()).toBe(true, 'Expected visibilityTabContent to be present');
        expect(editorTabset.genericTab().isPresent()).toBe(true, 'Expected genericTab to be present');
        expect(editorTabset.genericTabContent().isPresent()).toBe(true, 'Expected genericTabContent to be present');
    });

    it('will load tabset with test tabs', function() {

        expect(editorTabset.editorTabset().isPresent()).toBe(true, 'Expected editorTabset to be present');

        expect(editorTabset.tab1DropdownMenu().isPresent()).toBe(true, 'Expected tab1DropdownMenu to be present');
        expect(editorTabset.tab1TabContent().isPresent()).toBe(true, 'Expected tab1TabContent to be present');
        expect(editorTabset.tab2DropdownMenu().isPresent()).toBe(true, 'Expected tab2DropdownMenu to be present');
        expect(editorTabset.tab2TabContent().isPresent()).toBe(true, 'Expected tab2Content to be present');
        expect(editorTabset.tab3DropdownMenu().isPresent()).toBe(true, 'Expected tab3DropdownMenu to be present');
        expect(editorTabset.tab3TabContent().isPresent()).toBe(true, 'Expected tab3TabContent to be present');
    });

    it('clicking on a tab will change the displayed content to the view of the selected tab', function() {
        // Arrange
        var dropDownHeader = getDropDownHeader();
        var targetTab = editorTabset.tab2DropdownMenu();
        var targetTabLink = editorTabset.tab2DropdownMenuLink();
        var targetTabContent = editorTabset.tab2TabContent();
        var currentSelectedHeader = editorTabset.genericTabLink();
        var currentSelectedBody = editorTabset.genericTabContent();

        expect(targetTab).not.toBe(currentSelectedHeader);
        expect(targetTab).not.toBeUndefined();
        expect(hasClass(targetTab, 'active')).toBeFalsy();
        expect(targetTabContent).not.toBeUndefined();
        expect(targetTabContent.isDisplayed()).toBeFalsy();
        expect(currentSelectedHeader).not.toBeUndefined();
        expect(currentSelectedBody).not.toBeUndefined();

        // Act
        dropDownHeader.click();
        targetTabLink.click();

        // Assert
        expect(currentSelectedBody.isDisplayed()).toBeFalsy();
        expect(targetTabContent.isDisplayed()).toBeTruthy();
    });

    it('clicking on a tab will change the displayed content to the view of the selected tab even when ' +
        'the tab has validation errors',
        function() {
            // Arrange
            var dropDownHeader = getDropDownHeader();
            var saveButton = element(by.css('#save-button'));
            var targetTab = editorTabset.tab1DropdownMenu();
            var targetTabLink = editorTabset.tab1DropdownMenuLink();
            var targetTabContent = editorTabset.tab1TabContent();
            var currentSelectedHeader = editorTabset.tab1DropdownMenu();
            var currentSelectedBody = editorTabset.tab1TabContent();

            expect(targetTab).not.toBe(currentSelectedHeader);
            expect(targetTab).not.toBeUndefined();
            expect(hasClass(targetTab, 'active')).toBeFalsy();
            expect(targetTabContent).not.toBeUndefined();
            expect(targetTabContent.isDisplayed()).toBeFalsy();
            expect(currentSelectedHeader).not.toBeUndefined();
            expect(currentSelectedBody).not.toBeUndefined();

            saveButton.click().then(function() {
                expect(hasClass(targetTabLink, 'sm-tab-error')).toBeTruthy();
            });

            // Act
            dropDownHeader.click();
            targetTabLink.click();

            // Assert
            expect(targetTabContent.isDisplayed()).toBeTruthy();
        });

    it('clicking on save will execute save on all tabs', function() {
        // Arrange
        var saveButton = element(by.css('#save-button'));
        var targetTabHeader1 = editorTabset.tab1DropdownMenuLink();
        var targetTabHeader2 = editorTabset.tab2DropdownMenuLink();
        var targetTabHeader3 = editorTabset.tab3DropdownMenuLink();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();

        // Act
        saveButton.click();

        // Assert
        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeTruthy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeTruthy();
    });

    it('clicking on reset will clear all tabs', function() {
        // Arrange
        var saveButton = element(by.css('#save-button'));
        var resetButton = element(by.css('#reset-button'));
        var targetTabHeader1 = editorTabset.tab1DropdownMenuLink();
        var targetTabHeader2 = editorTabset.tab2DropdownMenuLink();
        var targetTabHeader3 = editorTabset.tab3DropdownMenuLink();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();

        saveButton.click();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeTruthy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeTruthy();

        // Act
        resetButton.click();

        // Assert
        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();
    });

    it('clicking on cancel will clear all tabs', function() {
        // Arrange
        var saveButton = element(by.css('#save-button'));
        var cancelButton = element(by.css('#cancel-button'));
        var targetTabHeader1 = editorTabset.tab1DropdownMenuLink();
        var targetTabHeader2 = editorTabset.tab2DropdownMenuLink();
        var targetTabHeader3 = editorTabset.tab3DropdownMenuLink();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();

        saveButton.click();

        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeTruthy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeTruthy();

        // Act
        cancelButton.click();

        // Assert
        expect(hasClass(targetTabHeader1, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader2, 'sm-tab-error')).toBeFalsy();
        expect(hasClass(targetTabHeader3, 'sm-tab-error')).toBeFalsy();
    });

    function hasClass(element, className) {
        return element.getAttribute('class').then(function(classes) {
            return classes.split(' ').indexOf(className) !== -1;
        });
    }

    function getDropDownHeader() {
        return element.all(by.css('ul.nav.nav-tabs li a.dropdown-toggle')).get(0);
    }
});
