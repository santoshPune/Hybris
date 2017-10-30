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

var pageList = require('./pageListObject.js');

describe('pageList - ', function() {

    var list;
    var NUMBER_OF_ARROWS_IN_PAGINATION_LIST = 4;
    var FIRST_CATALOG_NAME = 'APPAREL UK CONTENT CATALOG - ONLINE';

    beforeEach(function() {

        list = new pageList();
        browser.waitForContainerToBeReady();
        navigateToFirstCatalogPageList();

    });

    it('GIVEN I am on the page list of the first catalog WHEN the page is fully loaded THEN I expect to see a paginated list of 10 pages max, sorted by name ascending', function() {

        // Expect the page collection size to be 12
        expect(list.totalPageCount().getText()).toBe("(12 Pages found)");

        // Expect the number of page displayed to be 10
        expect(list.displayedPageCount()).toBe(10);

        // Expect the list to be sorted by name, ascending
        expect(list.firstRowForKey('name').getText()).toBe("ADVERTISE");
        navigateToIndex(2);
        expect(list.lastRowForKey('name').getText()).toBe("WELCOMEPAGE");

        // Expect the pagination navigation to have 2 pages entries
        list.paginationCount().then(function(totalCount) {
            var pageEntries = totalCount - NUMBER_OF_ARROWS_IN_PAGINATION_LIST;
            expect(pageEntries).toBe(2);
        });

    });

    it('GIVEN I am on the page list of the first catalog WHEN I search for a page THEN I expect the list to show the pages that match the query for any header', function() {

        // Perform a search on a name
        searchForPage('welcomepage', 'name', 1);
        list.paginationCount().then(function(totalCount) {
            var pageEntries = totalCount - NUMBER_OF_ARROWS_IN_PAGINATION_LIST;
            expect(pageEntries).toBe(1);
        });

        // Perform a search on a page UID
        searchForPage('uid1', 'uid', 4);

        // Perform a search on a page type
        searchForPage('product', 'typeCode', 4);

        // Perform a search on a page template
        searchForPage('mycustompagetemplate', 'template', 1);
        list.paginationCount().then(function(totalCount) {
            var pageEntries = totalCount - NUMBER_OF_ARROWS_IN_PAGINATION_LIST;
            expect(pageEntries).toBe(1);
        });

    });


    it('GIVEN I am on the page list of the first catalog WHEN I click on the name column header THEN I expect the list to be re-sorted by this key in the descending order', function() {

        // Sorting by name
        clickOnColumnHeader('name');
        list.firstRowForKey('name').getText().then(function(text) {
            expect(text.toLowerCase()).toBe("welcomepage");
        });
        navigateToIndex(2);
        list.lastRowForKey('name').getText().then(function(text) {
            expect(text.toLowerCase()).toBe("advertise");
        });

    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the UID column header THEN I expect the list to be re-sorted by this key in the descending order', function() {

        // Sorting by uid
        clickOnColumnHeader('uid');
        expect(list.firstRowForKey('uid').getText()).toBe("zuid12");
        navigateToIndex(2);
        expect(list.lastRowForKey('uid').getText()).toBe("auid1");

    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the page type column header THEN I expect the list to be re-sorted by this key in the descending order', function() {

        // Sorting by page type
        clickOnColumnHeader('typeCode');
        expect(list.firstRowForKey('typeCode').getText()).toBe("WallPage");
        navigateToIndex(2);
        expect(list.lastRowForKey('typeCode').getText()).toBe("ActionPage");

    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on the name column header THEN I expect the list to be re-sorted by this key in the descending order', function() {

        // Sorting by page template
        clickOnColumnHeader('template');
        expect(list.firstRowForKey('template').getText()).toBe("ZTemplate");
        navigateToIndex(2);
        expect(list.lastRowForKey('template').getText()).toBe("ActionTemplate");

    });


    it('GIVEN I am on the page list of the first catalog WHEN the page is fully loaded THEN I expect to see the catalog name and catalog version', function() {

        expect(list.catalogName().getText()).toBe(FIRST_CATALOG_NAME);

    });

    it('GIVEN I am on the page list of the first catalog WHEN I click on a linkable page name THEN I expect to be redirected to this page', function() {

        // Click on the first page name link
        list.linkForKeyAndRow('name', 1, 'a').click();

        // Expect to be redirected to the right page
        var EXPECTED_IFRAME_SRC = "/jsTests/cmssmarteditContainer/e2e/features/dummystorefrontAlternatelayout.html?cmsTicketId=previewTicketForPageId";
        var EXPECTED_BROWSER_URL = "/storefront/apparel-uk/apparel-ukContentCatalog/Online/uid2";

        browser.waitForWholeAppToBeReady();
        browser.switchToParent();
        expect(getIframe().getAttribute('src')).toContain(EXPECTED_IFRAME_SRC);
        getCurrentUrl().then(function(url) {
            expect(url.indexOf(EXPECTED_BROWSER_URL) >= 0);
        });

    });


    // Helper functions
    function navigateToFirstCatalogPageList() {
        return element(by.css('.catalog-container .page-list-link-item a')).click();
    }

    function navigateToIndex(index) {
        return browser.executeScript('window.scrollTo(0,document.body.scrollHeight);').then(function() {
            element(by.css('.pagination-container  > ul > li:nth-child(' + (NUMBER_OF_ARROWS_IN_PAGINATION_LIST / 2 + index) + ') a')).click();
        });
    }

    function searchForPage(query, columnHeader, expectedNumber) {
        list.searchInput().clear();
        list.searchInput().sendKeys(query);

        expect(list.totalPageCount().getText()).toBe("(" + expectedNumber.toString() + " Pages found)");
        expect(list.displayedPageCount()).toBe(expectedNumber);

        list.firstRowForKey(columnHeader).getText().then(function(text) {
            expect(text.toLowerCase().indexOf(query) >= 0).toBeTruthy();
        });
    }

    function clickOnColumnHeader(key) {
        list.columnHeaderForKey(key).click();
    }

    function getIframe() {
        return element(by.css('#js_iFrameWrapper iframe', 'iFrame not found'));
    }

    function getCurrentUrl() {
        return browser.getCurrentUrl();
    }

});
