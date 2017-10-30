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
describe('Test Routing', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/routing');
        browser.waitForWholeAppToBeReady();

        perspectives = require("../utils/components/Perspectives.js");
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        browser.waitForWholeAppToBeReady();

    });

    it('navigates to a custom view from the smartedit container', function() {
        expect(element(by.css('.ySmartEditTitleToolbar')).isPresent()).toBe(true);
        browser.click(by.id('navigateButtonOuter'));
        expect(browser.getCurrentUrl()).toContain('/customView');
        expect(element(by.css('ySmartEditToolbars')).isPresent()).toBe(false);
        expect(element(by.css('.content')).getText()).toBe('custom view 2');
    });

    it('navigates to custom view from the inner smartedit iframe', function() {
        expect(element(by.css('.ySmartEditTitleToolbar')).isPresent()).toBe(true);
        browser.switchToIFrame();
        browser.click(by.id('navigateButtonInner'));
        expect(browser.getCurrentUrl()).toContain('/customView');
        expect(element(by.css('.ySmartEditTitleToolbar')).isPresent()).toBe(false);
        expect(element(by.css('.content')).getText()).toBe('custom view 2');
    });

    it('navigates to new view from the container', function() {
        browser.setLocation('/test').then(function() {
            browser.click(by.id('navigateButtonOuter'));
            expect(browser.getCurrentUrl()).toContain('/customView');
            expect(element(by.css('.content')).getText()).toBe('custom view 2');
        });
    });

    it('navigates to new view from the custom non smartedit iframe', function() {
        browser.setLocation('/test').then(function() {
            var iframe = element(by.tagName('iframe'));
            browser.driver.switchTo().frame(iframe.getWebElement(''));
            browser.click(by.id('navigateButtonInner'));

            expect(browser.getCurrentUrl()).toContain('/customView');
            expect(element(by.css('.content')).getText()).toBe('custom view 2');
        });
    });

});
