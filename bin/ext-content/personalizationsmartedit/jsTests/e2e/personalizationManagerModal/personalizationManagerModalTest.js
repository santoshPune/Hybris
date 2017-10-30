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
 *
 *
 */
describe('Personalization Manager Modal - ', function() {

    beforeEach(function() {
        browser.get('jsTests/e2e/personalizationManagerModal/personalizationManagerModalTest.html');
        browser.waitForWholeAppToBeReady();

        openPerspectiveSelector();
        clickPersonalizationPerspective();
        clickManage();
        clickManagePersonalization();
    });

    it('GIVEN personalization management modal is open WHEN user changes number of rows per page THEN a proper number of rows is displayed', function() {
        // GIVEN
        numberOfDisplayedCustomizations(6);

        // WHEN        
        openRowsPerPageDropdown();
        clickRowsPerPage(5);

        //THEN
        numberOfDisplayedCustomizations(5);

        // WHEN        
        openRowsPerPageDropdown();
        clickRowsPerPage(10);

        //THEN
        numberOfDisplayedCustomizations(6);
    });

    it('GIVEN personalization management modal is open WHEN user filters customizations by name THEN a proper amount of customizations is displayed', function() {
        // GIVEN
        numberOfDisplayedCustomizations(6);

        // WHEN        
        enterCustomizationSearch("winter");

        //THEN
        numberOfDisplayedCustomizations(1);

        // WHEN
        clearCustomizationSearch();

        //THEN
        numberOfDisplayedCustomizations(6);
    });

    it('GIVEN personalization management modal is open WHEN user click on ADD NEW CUSTOMIZATION THEN customization variation manager modal shows up with name input enabled', function() {
        // GIVEN
        numberOfDisplayedCustomizations(6);

        // WHEN
        clickAddNewCustomization();

        // THEN
        customizationVariationManagerModalIsVisible();
        customizationNameInputIsEnabled();
    });

    it('GIVEN personalization management modal is open WHEN user click on /EDIT on one of the customizations THEN customization variation manager modal shows up with name input enabled', function() {
        // WHEN
        openActionDropdownByCustomizationName("CategoryLover");
        clickEdit();

        // THEN
        customizationVariationManagerModalIsVisible();
        customizationNameInputIsEnabled();
    });

    it('GIVEN personalization management modal is open WHEN user click on ACTION/DELETE PERMANENTLY on one of the customizations THEN confirmation dialog shows up', function() {
        // WHEN
        openActionDropdownByCustomizationName("CategoryLover");
        clickRemove();

        // THEN
        removeConfirmationDialogVisible();
    });

    // Actions
    function openPerspectiveSelector() {
        var perspectiveSelectorButton = $("div[class*='ySEPerspectiveSelector']").$("button");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(perspectiveSelectorButton), browser.params.timeout, "Could not open perspective selector");
        perspectiveSelectorButton.click();
    }

    function clickPersonalizationPerspective() {
        var personalizationPerspectiveButton = $$("li[class*='ySEPerspectiveList--item']").filter(function(elm) {
            return elm.$("a").getText().then(function(text) {
                return text == 'PERSONALIZATION';
            });
        }).first();

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(personalizationPerspectiveButton), browser.params.timeout, "Could not select personalization perspective");
        personalizationPerspectiveButton.click();
    }

    function clickManage() {
        var manageButton = $("span[data-translate='personalization.manager.toolbar']").element(by.xpath('..'));

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(manageButton), browser.params.timeout, "Could not click 'MANAGE'");
        manageButton.click();
    }

    function clickCustomize() {
        var customizeButton = $("span[data-translate='personalization.right.toolbar']").element(by.xpath('..'));

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(customizeButton), browser.params.timeout, "Could not click 'CUSTOMIZE'");
        customizeButton.click();
    }

    function clickManagePersonalization() {
        var managePersonalizationButton = $("a[data-translate='personalization.topmenu.library.manager.name']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(managePersonalizationButton), browser.params.timeout, "Could not select 'MANAGE LIBRARY'");
        managePersonalizationButton.click();
    }

    function clickCreateNewCustomization() {
        var createNewCustomizationButton = $("a[data-translate='personalization.manager.modal.add.button']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(createNewCustomizationButton), browser.params.timeout, "Could not select 'CREATE NEW CUSTOMIZATION'");
        createNewCustomizationButton.click();
    }

    function enterCustomizationSearch(customizationSearch) {
        var customizationSearchInput = element(by.model('search.name'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(customizationSearchInput), browser.params.timeout, "Could not find input for customization search.");
        customizationSearchInput.sendKeys(customizationSearch);
    }

    function clearCustomizationSearch() {
        var customizationSearchInput = element(by.model('search.name'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(customizationSearchInput), browser.params.timeout, "Could not find input for customization search.");

        customizationSearchInput.clear();
        customizationSearchInput.sendKeys(protractor.Key.ENTER);
    }

    function clickAddNewCustomization() {
        var addNewCustomizationButton = $("button[class*='y-add-btn']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(addNewCustomizationButton), browser.params.timeout, "Could not click on EDIT in dropdown menu");
        addNewCustomizationButton.click();
    }

    function openActionDropdownByCustomizationName(customizationName) {
        var customization = element.all(by.repeater('customization in customizations')).filter(function(elm) {
            return elm.$("span[class*='personalizationsmartedit-customization-code']").getText().then(function(text) {
                return text == customizationName;
            });
        }).first();

        var dropdownButton = customization.$$("button[class*='dropdown-toggle']").first();

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(dropdownButton), browser.params.timeout, "Could not open dropdown for " + customizationName + " customization");
        dropdownButton.click();
    }

    function clickEdit() {
        var editButton = $("div[class*='categoryTable']").$("div[class*='open']").$("a[class*='customization-edit-button']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(editButton), browser.params.timeout, "Could not click on EDIT in dropdown menu");
        editButton.click();
    }

    function clickRemove() {
        var removeButton = $("div[class*='categoryTable']").$("div[class*='open']").$("a[class*='customization-delete-button']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(removeButton), browser.params.timeout, "Could not click on REMOVE in dropdown menu");
        removeButton.click();
    }

    function clickMoveUp() {
        var moveUpButton = $("div[class*='categoryTable']").$("div[class*='open']").$("a[class*='customization-move-up-button']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(moveUpButton), browser.params.timeout, "Could not click on MOVE UP in dropdown menu");
        moveUpButton.click();
    }

    function clickMoveDown() {
        var moveDownButton = $("div[class*='categoryTable']").$("div[class*='open']").$("a[class*='customization-move-down-button']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(moveDownButton), browser.params.timeout, "Could not click on MOVE DOWN in dropdown menu");
        moveDownButton.click();
    }

    function openRowsPerPageDropdown() {
        var rowsPerPageButton = $("personalizationsmartedit-pagination").$("button");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(rowsPerPageButton), browser.params.timeout, "Could not click on ROWS PER PAGE button");
        rowsPerPageButton.click();
    }

    function clickRowsPerPage(n) {
        var rowsPerPageLink = element.all(by.repeater('i in availablePageSizes()')).filter(function(elm) {
            return elm.$("a").getText().then(function(text) {
                return text == n;
            });
        }).first();

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(rowsPerPageLink), browser.params.timeout, "Could not select " + n + " rows per page");
        rowsPerPageLink.click();
    }

    // Expectations
    function customizationVariationManagerModalIsVisible() {
        var customizationVariationManagerModalTitle = element(by.id('smartedit-modal-title-personalization.modal.customizationvariationmanagement.title'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(customizationVariationManagerModalTitle), browser.params.timeout, "could not find title of customization variation manager modal");
    }

    function customizationNameInputIsEnabled() {
        var customizationNameInput = element(by.model('customization.name'));

        expect(customizationNameInput.isEnabled()).toBe(true);
    }

    function customizationNameInputIsDisabled() {
        var customizationNameInput = element(by.model('customization.name'));

        expect(customizationNameInput.isEnabled()).toBe(false);
    }

    function removeConfirmationDialogVisible() {
        var customizationVariationManagerModalTitle = element(by.id('smartedit-modal-title-confirmation.modal.title'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(customizationVariationManagerModalTitle), browser.params.timeout, "could not find title of customization variation manager modal");
    }

    function numberOfDisplayedCustomizations(n) {
        var customizations = element.all(by.repeater('customization in customizations'));

        expect(customizations.count()).toEqual(n);
    }
});
