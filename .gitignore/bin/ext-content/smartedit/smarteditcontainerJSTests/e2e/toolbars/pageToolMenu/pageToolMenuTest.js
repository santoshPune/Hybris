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
xdescribe('Page Tool Menu - ', function() {

    var alerts;

    beforeEach(function() {
        alerts = require('../../utils/components/Alerts.js');

        browser.get('smarteditcontainerJSTests/e2e/toolbars/pageToolMenu/pageToolMenuTest.html');
    });

    it('GIVEN cog is available AND items are configured WHEN cog is clicked THEN configured toolbar items are available', function() {
        // WHEN
        clickCog();

        // THEN
        pageToolMenuHomeIsDisplayed();
        pageToolMenuSelectedItemIsNotDisplayed();

        expect(element(by.id("pageToolMenu_option_0")).getText()).toBe("DUMMY ITEM 1");
        expect(element(by.id("pageToolMenu_option_1")).getText()).toBe("DUMMY ITEM 2");
    });

    it('GIVEN page information is available WHEN item is clicked THEN sub toolbar is rendered AND back button is visible AND main toolbar is hidden', function() {
        // GIVEN
        clickCog();

        // WHEN
        clickFirstDummyItem();

        // THEN
        pageToolMenuHomeIsNotDisplayed();
        pageToolMenuSelectedItemIsDisplayed();
        firstDummyItemTemplateIsDisplayed();
    });

    it('GIVEN an item is displayed WHEN another item is clicked from the main menu THEN sub toolbar is rendered with the new content', function() {
        // GIVEN
        clickCog();
        clickFirstDummyItem();

        // WHEN
        clickBackButton();
        alerts.waitForAlertsToClear();
        clickSecondDummyItem();

        // THEN
        secondDummyItemTemplateIsDisplayed();
    });


    it('GIVEN the back button is available WHEN button is clicked THEN the main toolbar is displayed', function() {
        // GIVEN
        clickCog();
        clickFirstDummyItem();

        // WHEN
        clickBackButton();

        // THEN
        pageToolMenuHomeIsDisplayed();
        pageToolMenuSelectedItemIsNotDisplayed();
    });

    it('GIVEN that sub toolbar was last displayed WHEN cog is clicked THEN the main toolbar is displayed', function() {
        // GIVEN
        clickCog();
        clickFirstDummyItem();
        clickCloseButton();
        alerts.waitForAlertsToClear();

        // WHEN
        clickCog();

        // THEN
        pageToolMenuHomeIsDisplayed();
        pageToolMenuSelectedItemIsNotDisplayed();
    });


    it('GIVEN that the page tool menu is visible WHEN close is clicked THEN the toolbar is closed', function() {
        // GIVEN
        clickCog();

        // WHEN
        clickCloseButton();

        // THEN
        pageToolMenuIsNotDisplayed();
    });

    it('GIVEN that the page information sub menu is visible WHEN close is clicked THEN the menu is closed', function() {
        // GIVEN
        clickCog();
        clickFirstDummyItem();

        // THEN
        clickCloseButton();

        // THEN
        pageToolMenuIsNotDisplayed();
    });

    it('GIVEN that the page tool menu is open WHEN I click somewhere in the SmartEdit Container THEN the menu is closed', function() {
        // GIVEN
        clickCog();

        // THEN
        clickInSmartEditContainer();

        // THEN
        pageToolMenuIsNotDisplayed();
    });

    it('GIVEN that the page tool menu is open WHEN I click somewhere in the SmartEdit Application THEN the toolbar is closed', function() {
        // GIVEN
        clickCog();

        // THEN
        clickInSmartEdit();

        // THEN
        pageToolMenuIsNotDisplayed();
    });

    it('GIVEN a sub menu is open WHEN I click the back button THEN the sub menu\'s close callback is triggered', function() {
        // GIVEN
        clickCog();
        clickFirstDummyItem();

        // WHEN
        clickBackButton();

        // THEN
        firstDummyItemCloseCallbackIsTriggered();
    });

    it('GIVEN a sub menu is open WHEN I click the X button THEN the sub menu\'s close callback is triggered', function() {
        // GIVEN
        clickCog();
        clickFirstDummyItem();

        // WHEN
        clickCloseButton();

        // THEN
        firstDummyItemCloseCallbackIsTriggered();
    });

    // Actions
    function clickCog() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('#experienceSelectorToolbar_option_0')), 5000, "Could not click cog");
        element(by.id("experienceSelectorToolbar_option_0")).click();
    }

    function clickFirstDummyItem() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('#pageToolMenu_option_0')), 5000, "Could not click first dummy item");
        element(by.id("pageToolMenu_option_0")).click();
    }

    function clickSecondDummyItem() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('#pageToolMenu_option_1')), 5000, "Could not click second dummy item");
        element(by.id("pageToolMenu_option_1")).click();
    }

    function clickBackButton() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('#page-tool-menu-back-anchor')), 5000, "Could not click back button");
        element(by.id("page-tool-menu-back-anchor")).click();
    }

    function clickCloseButton() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('#page-tool-menu-close')), 5000, "could not click close");
        element(by.id("page-tool-menu-close")).click();
    }

    function clickInSmartEditContainer() {
        element(by.css('.ySmartEditAppLogo')).click();
    }

    function clickInSmartEdit() {
        browser.switchToIFrame();
        element(by.css('.noOffset1')).click();
        browser.switchToParent();
    }

    // Expectations
    function pageToolMenuIsNotDisplayed() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable($('#experienceSelectorToolbar_option_0')), 5000, "Could not click cog");
        element(by.id('page-tool-menu-selected-item')).getCssValue('opacity').then(function(value) {
            expect(Math.round(value)).toBe(0);
        });
        element(by.id('page-tool-menu-home')).getCssValue('opacity').then(function(value) {
            expect(Math.round(value)).toBe(1);
        });
    }

    function pageToolMenuHomeIsDisplayed() {
        expect(element(by.id('page-tool-menu-home')).getAttribute('class')).not.toContain('hide');
        expect(element(by.id('page-tool-menu-home')).isDisplayed()).toBe(true);
    }

    function pageToolMenuSelectedItemIsNotDisplayed() {
        expect(element(by.id('page-tool-menu-selected-item')).getAttribute('class')).toContain('hide');
    }

    function pageToolMenuHomeIsNotDisplayed() {
        expect(element(by.id('page-tool-menu-home')).getAttribute('class')).toContain('hide');
    }

    function pageToolMenuSelectedItemIsDisplayed() {
        expect(element(by.id('page-tool-menu-selected-item')).getAttribute('class')).not.toContain('hide');
        expect(element(by.id('page-tool-menu-selected-item')).isDisplayed()).toBe(true);

        expect(element(by.id('dummyItem1')).isPresent()).toBe(true);
        expect(element(by.id('page-tool-menu-back')).isDisplayed()).toBe(true);
    }

    function firstDummyItemTemplateIsDisplayed() {
        expect(element(by.id('dummyItem1')).isPresent()).toBe(true);
        expect(element(by.id('dummyItem1')).isDisplayed()).toBe(true);
    }

    function secondDummyItemTemplateIsDisplayed() {
        expect(element(by.id('dummyItem2')).isPresent()).toBe(true);
        expect(element(by.id('dummyItem2')).isDisplayed()).toBe(true);
    }

    function firstDummyItemCloseCallbackIsTriggered() {
        expect(alerts.alertMsg().isDisplayed()).toBe(true);
        expect(element(by.binding('alert.message')).getText()).toBe('Dummy page 1 onClose Callback');
    }

});
