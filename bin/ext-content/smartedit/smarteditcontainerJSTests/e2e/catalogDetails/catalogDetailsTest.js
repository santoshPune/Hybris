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

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/catalogDetails/catalogDetailsTest.html');
        browser.waitForContainerToBeReady();
    });

    it('GIVEN I am on the landing page WHEN the page is fully loaded THEN I expect to see the injected tempplate via the bridge', function() {
        var tmplateOne = element.all(by.css('.template_1'));
        var tmplateTwo = element.all(by.css('.template_2'));

        expect(tmplateOne.count()).toBe(4);
        expect(tmplateTwo.count()).toBe(4);

        expect(tmplateOne.get(0).getText()).toEqual("Hello");
        expect(tmplateTwo.get(0).getText()).toEqual("World");
    });

});
