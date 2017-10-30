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

var landingPage = require('./landingPageObject.js');
describe('Landing page - ', function() {

    var page;
    beforeEach(function() {

        page = new landingPage();
        browser.waitForContainerToBeReady();


    });

    it('GIVEN I am on the landing page WHEN the page is fully loaded THEN I expect to see the time stamp of Apparel UK returned correctly and sync in progress message will be shown to the user as the response result has runnning and user can not initiate another sync till the ongoing is completed or failed and sync initiated info will be rendered to the user ', function() {

        //correct time stamp is being shown
        expect(page.syncDateOf("Apparel UK").getText()).toBe("1/29/16 4:25 PM");
        //the in progress message is shown
        expect(page.syncStatusOf("Apparel UK").isPresent()).toBe(true);
        //no failure status is shown
        expect(page.failureStatusOf("Apparel UK").isPresent()).toBe(false);
        //sync button is hidden as the response is running
        expect(page.syncButtonOf("Apparel UK").isPresent()).toBe(false);
    });

    it('GIVEN I am on the landing page WHEN the page is fully loaded and I press the sync button of THEN I expect to see the time stamp changed, the failure error hidden initially, and on failure or error response , error message is shown and sync button is appeared again   ', function() {


        expect(page.syncDateOf("Apparel DE").getText()).toBe("1/29/16 4:25 PM");
        expect(page.failureStatusOf("Apparel DE").isPresent()).toBe(false);
        expect(page.syncStatusOf("Apparel DE").isPresent()).toBe(false);
        expect(page.syncButtonOf("Apparel DE").isPresent()).toBe(true);

        page.syncButtonOf("Apparel DE").click();
        page.syncConfirmOK.click();

        expect(page.syncDateOf("Apparel DE").getText()).toBe("1/30/16 4:25 PM");
        expect(page.failureStatusOf("Apparel DE").isPresent()).toBe(true);
        expect(page.syncButtonOf("Apparel DE").isPresent()).toBe(true);
        expect(page.syncStatusOf("Apparel DE").isPresent()).toBe(false);

    });
});
