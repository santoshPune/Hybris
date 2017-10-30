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

require('./../utils/components/LoginLogoutFunctions.js');

afterEach(function() {

    afterEachAuthentificationTest();

});

describe("languageSelector", function() {

    var experienceSelector, leftToolbar;

    beforeEach(function() {
        browser.driver.manage().deleteAllCookies();
        browser.get('smarteditcontainerJSTests/e2e/languageSelector/languageSelectorTest.html');
    });

    afterEach(function() {
        browser.driver.manage().deleteAllCookies();
    });

    it("GIVEN my browser has unsupported language, THEN the translation map is still fetched for that language", function() {
        browser.manage().addCookie("SELECTED_LANGUAGE", JSON.stringify({
            isoCode: "kl",
            name: "Klingon"
        }));
        browser.refresh();
        expect(element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('klName');
        expect(element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('klPassword');
    });

    it("GIVEN I am on the login page WHEN I select English language THEN it should translate the i18n keys", function() {
        selectLanguage('English');
        expect(element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Name');
        expect(element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Password');
    });

    it("GIVEN I am on the login page WHEN I select French language THEN it should translate the i18n keys",
        function() {
            selectLanguage('French');
            expect(element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Nom');
            expect(element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Mot de passe');
        });

    it("GIVEN I am on the login page, AND I select French language, WHEN coming back to the page, THEN it should load the french localization",
        function() {
            selectLanguage('French');
            expect(element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Nom');
            browser.refresh();
            expect(element(by.id('username_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Nom');
            expect(element(by.id('password_' + MAIN_AUTH_SUFFIX)).getAttribute('placeholder')).toBe('Mot de passe');
        });

    it("GIVEN I select French Language, AND submitting right credentials, THEN the container should be localized with French",
        function() {

            //GIVEN
            login('French');

            //THEN
            experienceSelector = require('./../utils/components/ExperienceSelector.js');
            experienceSelector.widget.button().click();

            //ASSERT
            expect(experienceSelector.catalog.label().getText()).toBe('CATALOGUE');
            expect(experienceSelector.dateAndTime.label().getText()).toBe('DATE ET HEURE');
            expect(experienceSelector.language.label().getText()).toBe('LANGUE');
        });

    it("GIVEN I select French Language, AND submitting right credentials, THEN the store front should be localized with French",
        function() {
            //GIVEN
            login('French');

            //THEN
            browser.switchToIFrame();
            expect(element(by.id('localizationField')).getText()).toBe('Je suis localisée');
            browser.switchToParent();
        });


    it("GIVEN I logged in smartedit, AND change the language on the left toolbar, THEN the store front should be localized with French",
        function() {

            //GIVEN
            login('English');

            //THEN
            leftToolbar = require('./../utils/components/Configurations.js');
            leftToolbar.openLeftToolbar();

            selectLanguage('French');
            browser.switchToIFrame();

            //ASSERT
            expect(element(by.id('localizationField')).getText()).toBe('Je suis localisée');
            browser.switchToParent();
        });


    it("GIVEN I selected French on the login page, AND I changed it to English on the leftToolbar, WHEN I logout THEN the login page should be localized in English",
        function() {

            //GIVEN
            login('French');

            //THEN
            leftToolbar = require('./../utils/components/Configurations.js');
            leftToolbar.openLeftToolbar();

            selectLanguage('English');

            logoutUser();

            var languageSelector = element(by.id('uiSelectToolingLanguage')).getText();
            expect(languageSelector).toBe('English');
        });


    function login(language) {
        element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('customermanager');
        element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('123');
        selectLanguage(language);
        browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
        browser.waitForWholeAppToBeReady();
    }

    function selectLanguage(value) {
        var languageSelector = element(by.id('uiSelectToolingLanguage'));
        var languageSelectorToggle = languageSelector.element(by.css('.ui-select-toggle'));
        var languageSelectorChoice = languageSelector.element(by.cssContainingText('.ui-select-choices-row', value));

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(languageSelectorToggle, 10000, "UI select toggle not clickable"));
        languageSelectorToggle.click();

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(languageSelectorChoice, 10000, "UI select choice with value " + value + " not clickable"));
        languageSelectorChoice.click();
    }
});
