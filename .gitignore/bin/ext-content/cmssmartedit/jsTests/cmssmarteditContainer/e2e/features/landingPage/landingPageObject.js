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
var LandingPage = function() {

    this.timeStamp = "Not set";
    this.pageURI = 'jsTests/cmssmarteditContainer/e2e/features/landingPage/landingPageTest.html';
    browser.get(this.pageURI);
};

LandingPage.prototype = Object.create({}, {

    catalogs: {
        get: function() {
            return element.all(by.css('.catalog-container'));
        }
    },
    firstCatalog: {
        get: function() {
            return element(by.css('.catalog-container .catalog-version-container'));
        }
    },
    selectCatalogPageNumber: {
        get: function(index) {
            var STARTING_POSITION = 2;
            index = index + STARTING_POSITION;
            return element(by.css('.pagination-container .pagination li:nth-child(' + index + ') a'));
        }
    },
    experienceSelectorWidgetText: {
        get: function() {
            browser.click(by.css('.navbar-header .navbar-toggle'));
            browser.sleep(500);
            browser.click(by.css('.leftNav a[data-ng-click="showSites()"]'));
            browser.sleep(500);
        }
    },

    clickSitesFromLeftMenu: {
        get: function() {

            return element(by.css('#slot1'));
        }
    },
    catalogBodies: {
        get: function() {
            return element.all(by.css('.catalog-body'));
        }
    },
    catalogBodyOf: {
        value: function(term) {
            return element(By.cssContainingText('.catalog-body', term));
        }
    },
    syncButtonOf: {
        value: function(term) {
            return this.catalogBodyOf(term).element(by.xpath('.//*[contains(@class,"catalog-sync-btn")]'));
        }
    },
    syncStatusOf: {
        value: function(term) {
            return this.catalogBodyOf(term).element(by.xpath('.//*[contains(@class,"ySESyncProgress")]'));
        }
    },
    failureStatusOf: {
        value: function(term) {
            return this.catalogBodyOf(term).element(by.xpath('.//*[contains(@class,"label-error")]'));
        }
    },
    syncDateOf: {
        value: function(term) {
            return this.catalogBodyOf(term).element(by.xpath('.//*[contains(@class,"catalog-last-synced")]'));
        }
    },
    syncConfirmPopup: {
        get: function() {
            return element(by.xpath('.//*[contains(@class,"modal-dialog")]'));
        }
    },
    syncConfirmOK: {
        get: function() {
            return element(by.id('confirmOk'));
        }
    },

    //helpers

    siblingOf: {
        value: function(originalElement) {
            return originalElement.element(by.xpath('following-sibling::div'));
        }
    },
    parentOf: {
        value: function(originalElement) {
            return originalElement.element(by.xpath('following-sibling::div'));
        }
    }

});

module.exports = LandingPage;
