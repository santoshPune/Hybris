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
var DdropPage = require('./dragAndDropPageObject.js');
describe('E2E Test for CMS drag and drop service within page', function() {

    var page;
    beforeEach(function() {

        page = new DdropPage();
        browser.waitForContainerToBeReady();

    });

    var alertBox = function() {
        return element(by.tagName('alerts-box'));
    };

    var alertMsg = function() {
        return element(by.id('alertMsg'));
    };


    xit("SimpleBannerComponent will successfully move to empty bottom header slot", function() {

        expect(page.storeNextDayDeliveryBanner.isPresent()).toBe(false);

        page.openComponentMenu().selectItemsTab().dragAndDropFromMenuToCoord(page.menuNextDayDeliveryBanner, page.slot2);

        expect(page.storeNextDayDeliveryBanner.isPresent()).toBe(true);

    });

});
