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
describe('E2E Test for bootstrap resilience', function() {
    var perspectives;

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/bootstrapResilience/bootstrapResilienceTest.html');
        browser.waitForWholeAppToBeReady();

        perspectives = require("../utils/components/Perspectives.js");
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        browser.waitForWholeAppToBeReady();
    });

    it('GIVEN a SmartEdit container module is not reachable (404) WHEN I load SmartEdit THEN the SmartEdit container still loads successfully', function() {
        expect(element(by.id('nav-expander')).isPresent()).toBe(true);
        expect(element(by.id('nav-expander')).isDisplayed()).toBe(true);
    });

    it('GIVEN a SmartEdit module is not reachable (404) WHEN I load SmartEdit THEN the SmartEdit application still loads successfully', function() {
        browser.switchToIFrame();
        expect(element(by.css('body')).getAttribute('data-smartedit-ready')).toBe('true');
    });

    it('GIVEN a SmartEdit script does not define the specified module WHEN I load SmartEdit THEN SmartEdit still loads successfully', function() {
        browser.switchToIFrame();
        expect(perspectives.getElementInOverlay('component1', 'componentType1').getText()).toContain('component1_Text_from_dummy_decorator');
    });
});
