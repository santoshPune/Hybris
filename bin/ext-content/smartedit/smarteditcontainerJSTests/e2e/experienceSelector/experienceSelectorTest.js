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
describe('Experience Selector - ', function() {

    var ELECTRONICS_SITE = {
        CATALOGS: {
            ONLINE: 3,
            STAGED: 4
        },
        LANGUAGES: {
            ENGLISH: 1,
            POLISH: 2,
            ITALIAN: 3
        }
    };

    var APPAREL_SITE = {
        CATALOGS: {
            ONLINE: 1,
            STAGED: 2
        },
        LANGUAGES: {
            ENGLISH: 1,
            FRENCH: 2
        }
    };

    var experienceSelector, alerts;

    beforeEach(function() {
        experienceSelector = require('./../utils/components/ExperienceSelector.js');
        alerts = require("../utils/components/Alerts.js");

        browser.get('smarteditcontainerJSTests/e2e/experienceSelector/experienceSelectorTest.html');
        browser.waitForWholeAppToBeReady();
    });

    it("GIVEN I'm in the SmartEdit application WHEN I click the Experience Selector button THEN I expect to see the Experience Selector", function() {
        //WHEN
        experienceSelector.widget.button().click();

        //THEN
        expect(experienceSelector.catalog.label().getText()).toBe('CATALOG');
        expect(experienceSelector.dateAndTime.label().getText()).toBe('DATE/TIME');
        expect(experienceSelector.language.label().getText()).toBe('LANGUAGE');

        expect(experienceSelector.buttons.ok().getText()).toBe('APPLY');
        expect(experienceSelector.buttons.cancel().getText()).toBe('CANCEL');
    });

    it("GIVEN I'm in the SmartEdit application WHEN I click the Experience Selector THEN I expect to see the currently selected experience in the Experience Selector", function() {
        //WHEN
        experienceSelector.widget.button().click();

        //THEN
        expect(experienceSelector.catalog.selectedOption().getText()).toBe('Electronics Content Catalog - Online');
        expect(experienceSelector.language.selectedOption().getText()).toBe('English');
        expect(experienceSelector.dateAndTime.field().getAttribute('placeholder')).toBe('Select a Date and Time');
    });

    it("GIVEN I'm in the experience selector WHEN I do not choose a catalog from the catalog dropdown THEN I expect to see a disabled Apply button", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //THEN
        expect(experienceSelector.buttons.ok().getAttribute('disabled')).toBe('true');
    });

    it("GIVEN I'm in the experience selector WHEN I click on the catalog selector dropdown THEN I expect to see all catalog/catalog versions combinations", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //WHEN
        experienceSelector.catalog.dropdown().click();

        // THEN
        expect(experienceSelector.catalog.options().count()).toBe(4);
        expect(experienceSelector.catalog.option(1).getText()).toBe('Apparel UK Content Catalog - Online');
        expect(experienceSelector.catalog.option(2).getText()).toBe('Apparel UK Content Catalog - Staged');
        expect(experienceSelector.catalog.option(3).getText()).toBe('Electronics Content Catalog - Online');
        expect(experienceSelector.catalog.option(4).getText()).toBe('Electronics Content Catalog - Staged');
    });

    it("GIVEN I'm in the experience selector WHEN I select a catalog THEN I expect to see the apply button enabled", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //WHEN
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(APPAREL_SITE.CATALOGS.ONLINE).click();

        //THEN
        expect(experienceSelector.buttons.ok().getAttribute('disabled')).toBeFalsy();
    });

    it("GIVEN I'm in the experience selector WHEN I select a catalog belonging to the electronics site THEN I expect to see the language dropdown populated with the electronics sites languages", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //WHEN
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();
        experienceSelector.language.dropdown().click();

        //THEN
        expect(experienceSelector.language.options().count()).toBe(3);
        expect(experienceSelector.language.option(1).getText()).toBe('English');
        expect(experienceSelector.language.option(2).getText()).toBe('Polish');
        expect(experienceSelector.language.option(3).getText()).toBe('Italian');
    });

    it("GIVEN I'm in the experience selector WHEN I select a catalog belonging to the apparel site THEN I expect to see the language dropdown populated with the apprel sites languages", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //WHEN
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(APPAREL_SITE.CATALOGS.ONLINE).click();
        experienceSelector.language.dropdown().click();

        expect(experienceSelector.language.options().count()).toBe(2);
        expect(experienceSelector.language.option(1).getText()).toBe('English');
        expect(experienceSelector.language.option(2).getText()).toBe('French');
    });

    it("GIVEN I'm in the experience selector WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect the smartEdit application with the new preview ticket", function() {
        //GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(APPAREL_SITE.CATALOGS.ONLINE).click();
        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(APPAREL_SITE.LANGUAGES.ENGLISH).click();

        //WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        //THEN
        var expectedUriPostfix = '/smarteditcontainerJSTests/e2e/dummystorefront.html?cmsTicketId=validTicketId';
        expect(experienceSelector.page.iframe().getAttribute('src')).toContain(expectedUriPostfix);
    });

    // TODO this should be part of a unit test
    it("GIVEN I'm in the experience selector WHEN I click the apply button AND the REST call to the preview service fails due to an invalid catalog and catalog version THEN I expect to see an error displayed", function() {
        //GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();
        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.ITALIAN).click();

        //WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        //THEN
        expect(alerts.alertMsg().isDisplayed()).toBe(true);
    });

    it("GIVEN I'm in the experience selector AND I click on the apply button to update the experience with the one I chose THEN it should update the experience widget text", function() {
        //GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(APPAREL_SITE.CATALOGS.ONLINE).click();
        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(APPAREL_SITE.LANGUAGES.FRENCH).click();

        //WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        //THEN
        var VALID_EXPERIENCE_WIDGET_TEXT = 'Apparel UK Content Catalog - Online | French';
        expect(experienceSelector.widget.text()).toBe(VALID_EXPERIENCE_WIDGET_TEXT);
    });

    it("GIVEN I'm in the experience selector AND I select a date and time using the date-time picker WHEN I click the apply button THEN it should update the experience widget text", function() {
        browser.waitForWholeAppToBeReady();

        // GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(APPAREL_SITE.CATALOGS.ONLINE).click();

        experienceSelector.dateAndTime.button().click();
        experienceSelector.actions.selectExpectedDate();
        experienceSelector.dateAndTime.button().click();

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(APPAREL_SITE.LANGUAGES.FRENCH).click();

        // WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        // THEN
        var VALID_EXPERIENCE_WIDGET_TEXT = 'Apparel UK Content Catalog - Online | French | 01/01/2016 1:00 PM';
        expect(experienceSelector.widget.text()).toBe(VALID_EXPERIENCE_WIDGET_TEXT);
    });

    it("GIVEN I'm in the experience selector WHEN I click outside the experience selector in the SmartEdit container THEN the experience selector is closed and reset", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //WHEN
        experienceSelector.actions.clickInApplication();

        //THEN
        expect(experienceSelector.catalog.label().isDisplayed()).toBe(false);
    });

    it("GIVEN I'm in the experience selector WHEN I click outside the experience selector in the SmartEdit application THEN the experience selector is closed and reset", function() {
        //GIVEN
        experienceSelector.widget.button().click();

        //WHEN
        experienceSelector.actions.clickInIframe();

        //THEN
        expect(experienceSelector.catalog.label().isDisplayed()).toBe(false);
    });

    it("GIVEN I have selected an experience with a time WHEN I click the apply button AND the REST call to the preview service succeeds AND I re-open the experience selector THEN I expect to see the newly selected experience", function() {
        //GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.STAGED).click();

        experienceSelector.dateAndTime.field().click();
        experienceSelector.dateAndTime.field().sendKeys("01/01/2016 12:00 AM");

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.ITALIAN).click();

        //WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();
        experienceSelector.widget.button().click();

        //THEN
        expect(experienceSelector.catalog.selectedOption().getText()).toBe('Electronics Content Catalog - Staged');
        expect(experienceSelector.language.selectedOption().getText()).toBe('Italian');
        expect(experienceSelector.dateAndTime.field().getAttribute('value')).toBe('01/01/2016 12:00 AM');
    });

    it("GIVEN I have selected an experience without a time WHEN I click the apply button AND the REST call to the preview service succeeds AND I re-open the experience selector THEN I expect to see the newly selected experience", function() {
        browser.waitForWholeAppToBeReady();

        //GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        //WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();
        experienceSelector.widget.button().click();

        //THEN
        expect(experienceSelector.catalog.selectedOption().getText()).toBe('Electronics Content Catalog - Online');
        expect(experienceSelector.language.selectedOption().getText()).toBe('Polish');
        expect(experienceSelector.dateAndTime.field().getAttribute('placeholder')).toBe('Select a Date and Time');
    });

    it("GIVEN I'm in the experience selector AND I've changed the values in the editor fields WHEN I click cancel AND I re-open the experience selector THEN I expect to see the currently selected experience", function() {
        //GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.STAGED).click();

        experienceSelector.dateAndTime.field().click();
        experienceSelector.dateAndTime.field().sendKeys("01/01/2016 12:00 AM");

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.ITALIAN).click();

        //WHEN
        experienceSelector.actions.clickInApplication();
        experienceSelector.widget.button().click();

        //THEN
        expect(experienceSelector.catalog.selectedOption().getText()).toBe('Electronics Content Catalog - Online');
        expect(experienceSelector.language.selectedOption().getText()).toBe('English');
        expect(experienceSelector.dateAndTime.field().getAttribute('placeholder')).toBe('Select a Date and Time');
    });

    it("GIVEN I have selected an experience without a time WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect the payload to match the API's expected payload", function() {
        browser.waitForWholeAppToBeReady();

        // GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        // WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        // THEN
        var EXPECTED_URI_SUFFIX = '/smarteditcontainerJSTests/e2e/dummystorefront.html?cmsTicketId=validTicketId1';
        expect(experienceSelector.page.iframe().getAttribute('src')).toContain(EXPECTED_URI_SUFFIX);
    });

    it("GIVEN I have selected an experience with a time WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect the payload to match the API's expected payload", function() {
        browser.waitForWholeAppToBeReady();

        // GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        experienceSelector.dateAndTime.field().click();
        experienceSelector.dateAndTime.field().sendKeys("01/01/2016 1:00 PM");

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        // WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        // THEN
        var EXPECTED_URI_SUFFIX = '/smarteditcontainerJSTests/e2e/dummystorefront.html?cmsTicketId=validTicketId2';
        expect(experienceSelector.page.iframe().getAttribute('src')).toContain(EXPECTED_URI_SUFFIX);
    });

    it("GIVEN that I have deep linked and I have selected a new experience with a time WHEN I click the apply button AND the REST call to the preview service succeeds THEN I expect to reload the page to which I have deep linked without a preview ticket", function() {
        browser.switchToIFrame();
        browser.click(by.id("deepLink"));
        browser.switchToParent();
        browser.waitForWholeAppToBeReady();

        // GIVEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(ELECTRONICS_SITE.CATALOGS.ONLINE).click();

        experienceSelector.dateAndTime.field().click();
        experienceSelector.dateAndTime.field().sendKeys("01/01/2016 1:00 PM");

        experienceSelector.language.dropdown().click();
        experienceSelector.language.option(ELECTRONICS_SITE.LANGUAGES.POLISH).click();

        // WHEN
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        // THEN
        var EXPECTED_URI_SUFFIX = '/smarteditcontainerJSTests/e2e/dummystorefrontSecondPage.html';
        expect(experienceSelector.page.iframe().getAttribute('src')).toContain(EXPECTED_URI_SUFFIX);
    });


    it('GIVEN that I have deep linked WHEN I select a new experience and the current page does not exist for this new experience THEN I will be redirected to the landing page of the new experience', function() {

        // GIVEN
        browser.switchToIFrame();
        browser.click(by.id("deepLinkFailsWhenNewExperience"));
        browser.switchToParent();
        browser.waitForWholeAppToBeReady();

        // WHEN
        experienceSelector.widget.button().click();
        experienceSelector.catalog.dropdown().click();
        experienceSelector.catalog.option(APPAREL_SITE.CATALOGS.STAGED).click();
        experienceSelector.buttons.ok().click();
        browser.waitForWholeAppToBeReady();

        var APPAREL_UK_STAGED_HOMEPAGE = 'dummystorefront.html?cmsTicketId=apparel-ukContentCatalogStagedValidTicket';
        expect(experienceSelector.page.iframe().getAttribute('src')).toContain(APPAREL_UK_STAGED_HOMEPAGE);

    });


});
