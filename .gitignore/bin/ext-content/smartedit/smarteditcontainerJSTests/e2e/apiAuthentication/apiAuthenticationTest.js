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


describe("authentication API", function() {

    var perspectives;

    beforeEach(function() {
        browser.driver.manage().deleteAllCookies();
        browser.get('http://127.0.0.1:7000/smarteditcontainerJSTests/e2e/apiAuthentication/apiAuthenticationTest.html');

        perspectives = require("../utils/components/Perspectives.js");

    });
    afterEach(function() {
        browser.driver.manage().deleteAllCookies();
    });


    it("when first login, user is presented with a login dialog",
        function() {

            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('username_' + MAIN_AUTH_SUFFIX))), 20000, "username input field is not clickable");
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('password_' + MAIN_AUTH_SUFFIX))), 20000, "password input field is not clickable");
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('submit_' + MAIN_AUTH_SUFFIX))), 20000, "submit input field is not clickable");

        });

    it("submitting an empty auth form will cause an error",
        function() {

            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            expect(element(by.id('invalidError')).getText()).toBe('Username and password required');
        });

    it("submitting wrong credentials will cause an error",
        function() {

            element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('admin');
            element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('nimda');
            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            expect(element(by.id('invalidError')).getText()).toBe('Invalid username or password');
        });


    it("submitting right credentials will cause successful authentication",
        function() {

            element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('customermanager');
            element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('123');
            browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));
            browser.waitForContainerToBeReady();
        });

    describe("after successful first auth", function() {

        beforeEach(function() {});

        it("custom iframe successfully retrieves access token map", function() {
            //for this test we don't need authenticate with iframe APIs, we just need to redirect to the custom view
            loginUser(false, true, perspectives);
            browser.waitForWholeAppToBeReady();
            browser.click(by.id('navigateButtonOuter'));
            browser.switchToIFrame(false);

            //since it is custom iframe, no angular is to be expected inside
            browser.ignoreSynchronization = true;
            browser.click(by.id('retrieveAccessTokens'));

            expect(element(by.id('tokens')).getText()).toBe("{\"/authorizationserver/oauth/token\":{\"access_token\":\"access-token0\",\"token_type\":\"bearer\"},\"principal-uid\":\"customermanager\"}");
            browser.ignoreSynchronization = false;

        });

        it("user is presented with another login dialog and neither fake 1 nor fake 2 are visible",
            function() {
                loginUser(true, false, perspectives);
                expect(by.id('fake1')).toBeAbsent();
                expect(by.id('fake2')).toBeAbsent();
            });

        xit("submitting right credentials to fake2 api will cause successful authentication and fake 2 appears",
            function() {
                loginUser(true, false, perspectives);
                element(by.id('username_' + FAKE2_AUTH_SUFFIX)).sendKeys('customermanager');
                element(by.id('password_' + FAKE2_AUTH_SUFFIX)).sendKeys('12345');
                browser.click(by.id('submit_' + FAKE2_AUTH_SUFFIX));

                expect(by.id('fake1')).toBeAbsent();
                expect(element(by.id('fake2')).isPresent()).toBe(true);

            });

        xit("submitting right credentials to fake2 AND fake1 api will cause successful authentications and both fake 2 and fake 1 appear",
            function() {
                loginUser(true, false, perspectives);
                element(by.id('username_' + FAKE2_AUTH_SUFFIX)).sendKeys('customermanager');
                element(by.id('password_' + FAKE2_AUTH_SUFFIX)).sendKeys('12345');
                browser.click(by.id('submit_' + FAKE2_AUTH_SUFFIX));

                expect(by.id('fake1')).toBeAbsent();
                expect(element(by.id('fake2')).isPresent()).toBe(true);

                browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('submit_' + FAKE1_AUTH_SUFFIX))), 20000, "submit input field is not clickable in iframe login 1");
                element(by.id('username_' + FAKE1_AUTH_SUFFIX)).sendKeys('customermanager');
                element(by.id('password_' + FAKE1_AUTH_SUFFIX)).sendKeys('1234');
                browser.click(by.id('submit_' + FAKE1_AUTH_SUFFIX));

                expect(element(by.id('fake1')).isPresent()).toBe(true);
                expect(element(by.id('fake2')).isPresent()).toBe(true);

            });

    });

});
