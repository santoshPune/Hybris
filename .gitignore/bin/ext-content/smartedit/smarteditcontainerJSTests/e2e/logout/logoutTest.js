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
require('../utils/components/LoginLogoutFunctions.js');

describe("after a successful authentification", function() {

    var electronicsContentCatalogOnlineSelector = ".landingPageWrapper .row > .ySECatalogEntry:nth-child(2) .catalog-version-container";
    var hasReAuthModal;
    var redirectLocationIsLandingPage;

    beforeEach(function() {

        browser.get('smarteditcontainerJSTests/e2e/logout/logoutTest.html');

        hasReAuthModal = false;
        redirectLocationIsLandingPage = false;

        // TODO This fails on CI
        loginUser(hasReAuthModal, redirectLocationIsLandingPage);
    });

    xit("when the user logouts and re-login, he will be re-directed to the home page of the storefront after selecting the same catalog", function() {

        // Deeplink
        browser.click(by.id('deepLink'));
        browser.switchToParent();
        browser.waitForWholeAppToBeReady();

        // Logout
        logoutUser();

        // Re-login in SmartEdit
        hasReAuthModal = false;
        redirectLocationIsLandingPage = true;
        loginUser(hasReAuthModal, redirectLocationIsLandingPage);

        browser.click(by.css(electronicsContentCatalogOnlineSelector));

        browser.waitForWholeAppToBeReady();

        browser.switchToParent();

        element(by.css('#js_iFrameWrapper iframe')).getAttribute('src').then(function(src) {
            var iframe_src_url = src;
            expect(iframe_src_url.indexOf(STORE_FRONT_HOME_PAGE) > 0).toBeTruthy();
        });

    });

    xit("when the user goes back to the landing page, he will be re-directed to the home page of the storefront after selecting the same catalog", function() {

        // Deeplink
        browser.click(by.id('deepLink'));
        browser.switchToParent();
        browser.waitForWholeAppToBeReady();

        // Redirect the user to the landing page
        redirectUserToLandingPage();

        browser.click(by.css(electronicsContentCatalogOnlineSelector));

        browser.waitForWholeAppToBeReady();

        element(by.css('#js_iFrameWrapper iframe')).getAttribute('src').then(function(src) {
            var iframe_src_url = src;
            expect(iframe_src_url.indexOf(STORE_FRONT_HOME_PAGE) > 0).toBeTruthy();
        });

    });

});
