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
describe('Integratation of toolbar directives into the framework', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/toolbars/frameworkIntegration/frameworkIntegrationTest.html');
    });

    /*seems to break with new double bootstrapping of smarteditcontainer*/
    describe('availability of SmartEdit title toolbar and experience selector toolbar', function() {

        it('SmartEdit title toolbar and experience selector toolbar exists and are correctly bootstrapped', function() {
            expect(element.all(by.css('div.ySmartEditTitleToolbar')).count()).toBe(1);
            expect(element.all(by.css('div.ySmartEditExperienceSelectorToolbar')).count()).toBe(1);

        });
    });
});
