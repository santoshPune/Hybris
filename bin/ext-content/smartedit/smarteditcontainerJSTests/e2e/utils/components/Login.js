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
module.exports = {
    STORE_FRONT_HOME_PAGE: "dummystorefront.html",

    // Elements
    mainLoginUsernameInput: function() {
        return element(by.id('username_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
    },
    mainLoginPasswordInput: function() {
        return element(by.id('password_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
    },
    mainLoginSubmitButton: function() {
        return element(by.id('submit_L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4'));
    },
    fake1LoginUsernameInput: function() {
        return element(by.id('username_L2F1dGhFbnRyeVBvaW50MQ'));
    },
    fake1LoginPasswordInput: function() {
        return element(by.id('password_L2F1dGhFbnRyeVBvaW50MQ'));
    },
    fake1LoginSubmitButton: function() {
        return element(by.id('submit_L2F1dGhFbnRyeVBvaW50MQ'));
    },
    fake2LoginUsernameInput: function() {
        return element(by.id('username_L2F1dGhFbnRyeVBvaW50Mg'));
    },
    fake2LoginPasswordInput: function() {
        return element(by.id('password_L2F1dGhFbnRyeVBvaW50Mg'));
    },
    fake2LoginSubmitButton: function() {
        return element(by.id('submit_L2F1dGhFbnRyeVBvaW50Mg'));
    },
    requiredError: function() {
        return element(by.id('requiredError'));
    },
    authenticationError: function() {
        return element(by.id('invalidError'));
    },
    burgerMenuButton: function() {
        return element(by.id('nav-expander'));
    },
    logoutButton: function() {
        return element(by.css('.leftNav--user> li:nth-of-type(3) > a'));
    },


    // Actions
    logoutUser: function() {
        browser.switchToParent();
        this.burgerMenuButton().click();
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(this.logoutButton()), 5000, "Timed out waiting for logout button");
        this.logoutButton().click();
    },

    loginAsUser: function(username, password) {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(this.mainLoginUsernameInput()), 5000, "Timed out waiting for username input");
        this.mainLoginUsernameInput().sendKeys(username);
        this.mainLoginPasswordInput().sendKeys(password);
        this.mainLoginSubmitButton().click();
    },

    loginAsCmsManager: function() {
        this.loginAsUser('cmsmanager', '1234');
        browser.waitForWholeAppToBeReady();
    },

    loginAsAdmin: function() {
        this.loginAsUser('admin', '1234');
        browser.waitForWholeAppToBeReady();
    },

    loginAsCmsManagerToLandingPage: function() {
        this.loginAsUser('cmsmanager', '1234');
        browser.waitForContainerToBeReady();
    },

    loginAsInvalidUser: function() {
        this.loginAsUser('invalid', 'invalid');
    },

    loginToAuthForFake1: function() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(this.fake1LoginUsernameInput()), 5000,
            "Timed out waiting for fake 2 username input");
        this.fake1LoginUsernameInput().sendKeys('fake1');
        this.fake1LoginPasswordInput().sendKeys('1234');
        this.fake1LoginSubmitButton().click();
    },

    loginToAuthForFake2: function() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(this.fake2LoginUsernameInput()), 5000,
            "Timed out waiting for fake 2 username input");
        this.fake2LoginUsernameInput().sendKeys('fake2');
        this.fake2LoginPasswordInput().sendKeys('1234');
        this.fake2LoginSubmitButton().click();
    }

};
