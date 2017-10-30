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
var PageList = function() {

    this.pageURI = 'smarteditcontainerJSTests/e2e/clientPagedList/clientPagedList.html';
    browser.get(this.pageURI);

};

PageList.prototype = {

    totalPageCount: function() {
        return element(by.css('.paged-list-count span:first-child'));
    },
    displayedPageCount: function() {
        return element.all(by.css('.paged-list-table tbody tr')).count();
    },
    paginationCount: function() {
        return element.all(by.css('.pagination-container  > ul > li')).count();
    },
    searchInput: function() {
        return element(by.css('.page-list-search > input'));
    },
    columnHeaderForKey: function(key) {
        return element(by.css('.paged-list-table thead tr:first-child .paged-list-header-' + key));
    },
    firstRowForKey: function(key) {
        return element(by.css('.paged-list-table tbody tr:first-child .paged-list-item-' + key));
    },
    lastRowForKey: function(key) {
        return element(by.css('.paged-list-table tbody tr:last-child .paged-list-item-' + key));
    },
    elemForKeyAndRow: function(key, row, selector) {
        return element(by.css('.paged-list-table tbody tr:nth-child(' + row + ') .paged-list-item-' + key + ' ' + selector));
    },
    catalogName: function() {
        return element(by.css('.ySEPageListTitle h4'));
    }


};

module.exports = PageList;
