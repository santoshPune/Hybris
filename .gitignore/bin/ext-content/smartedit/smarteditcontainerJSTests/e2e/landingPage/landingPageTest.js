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

describe('Landing page - ', function() {

    var landingPage;

    beforeEach(function() {
        landingPage = require('../utils/components/LandingPage.js');

        browser.get('smarteditcontainerJSTests/e2e/landingPage/landingPageTest.html');
        browser.waitForContainerToBeReady();
        browser.disableAnimations();
    });

    it('GIVEN I am on the landing page WHEN the page is fully loaded THEN I expect to see the first four catalogs', function() {
        // THEN
        expect(landingPage.catalogs().count()).toEqual(4);
    });

    it('GIVEN I am on the landing page WHEN I click on the "2" link of the pagination THEN I expect to see the remaining catalogs', function() {
        // WHEN
        landingPage.catalogPageNumber(2).click();

        // THEN
        expect(landingPage.catalogs().count()).toEqual(1);
    });

    it('GIVEN I am on the landing page WHEN I click on the first of the catalogs THEN I will be redirected to its storefront', function() {
        // WHEN
        landingPage.firstCatalog().click();
        browser.waitForWholeAppToBeReady();
        browser.waitForUrlToMatch(/\/storefront/);

        // THEN
        expect(browser.getCurrentUrl()).toContain('/storefront');
        expect(landingPage.experienceSelectorWidget().getText()).toBe('Electronics Content Catalog - Online | English');
    });

    it('GIVEN I am on a store front WHEN I click on the burger menu and the SITES link THEN I will be redirected to the landing page', function() {
        // WHEN
        landingPage.firstCatalog().click();
        browser.waitForWholeAppToBeReady();
        landingPage.leftMenuButton().click();
        landingPage.sitesButton().click();
        browser.waitForContainerToBeReady();
        browser.waitForUrlToMatch(/^(?!.*storefront)/);

        // THEN
        expect(browser.getCurrentUrl()).not.toContain('/storefront');
        expect(landingPage.catalogs().count()).toBe(4);
    });

    xit('GIVEN I am on the landing page and inflection point icon should not be visible on this page', function() {
        // THEN
        expect(landingPage.inflectionPointSelector()).not.toBeDisplayed();
    });


});
