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
describe('end-to-end Test for contextual menu service module', function() {

    var perspectives;

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/contextualMenu/contextualMenuTest.html');
        browser.waitForWholeAppToBeReady();

        perspectives = require("../utils/components/Perspectives.js");
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        browser.waitForWholeAppToBeReady();
    });

    it("Upon loading SmartEdit, contextualMenu named 'INFO' will be added to ComponentType1 and contextualMenu named 'DELETE' will be added to ComponentType2",
        function() {
            browser.switchToIFrame();

            //Assert on ComponentType1
            perspectives.getElementInOverlay('component1', 'componentType1').click();
            expect(element(by.id('INFO-component1-componentType1-0')).isPresent()).toBe(true);
            expect(by.id('DELETE-component1-componentType1-0')).toBeAbsent();

            //Assert on ComponentType2
            perspectives.getElementInOverlay('component2', 'componentType2').click();
            expect(by.id('INFO-component2-componentType2-0')).toBeAbsent();
            expect(element(by.id('DELETE-component2-componentType2-0')).isPresent()).toBe(true);

        });


    it("contextualMenu item WILL change the DOM element of ComponentType1 WHEN condition callback is called",
        function() {
            browser.switchToIFrame();
            //Assert on ComponentType1
            perspectives.getElementInOverlay('component1', 'componentType1').click();
            expect(element(by.className('conditionClass1')).isPresent()).toBe(true);
        });

    xit("Can add and remove contextual menu items on the fly", function() {
        // Arrange
        browser.switchToIFrame();
        expect(by.id('INFO-component4-componentType4-0')).toBeAbsent();

        // Act / Assert
        browser.actions().mouseMove(element(by.id('component3'))).perform();

        browser.click(by.id('enable-component3-componentType3-0'));
        expect(element(by.id('INFO-component4-componentType4-0')).isPresent()).toBe(true);

        browser.click(by.id('enable-component3-componentType3-0'));
        expect(by.id('INFO-component4-componentType4-0')).toBeAbsent();
    });
});
