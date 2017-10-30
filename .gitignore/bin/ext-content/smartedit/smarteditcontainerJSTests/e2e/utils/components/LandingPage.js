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
    catalogs: function() {
        return element.all(by.css('.catalog-container'));
    },

    firstCatalog: function() {
        return element.all(by.css('.catalog-container .catalog-version-container')).get(0);
    },

    catalogPageNumber: function(index) {
        // The starting position of the first pagination number is 2 in the <ul> list as the first two items are the navigation arrows
        var STARTING_POSITION = 2;
        index = index + STARTING_POSITION;
        return element(by.css('.pagination-container .pagination li:nth-child(' + index + ') a'));
    },

    experienceSelectorWidget: function() {
        return element(by.css('[id=\'experience-selector-btn\'] > span:nth-child(2)', 'Selector widget not found'));
    },

    inflectionPointSelector: function() {
        return element(by.tagName('inflection-point-selector'));
    },

    leftMenuButton: function() {
        return element(by.css('.navbar-header .navbar-toggle'));
    },

    sitesButton: function() {
        return element(by.css('.leftNav a[data-ng-click="showSites()"]'));
    }
};
